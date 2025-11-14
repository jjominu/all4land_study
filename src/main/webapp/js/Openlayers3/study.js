var USE_DOG  = false; // 초기 표시 여부

var baseMap = null;
var gsDog   = null;
var dogWfsJson = null;
var dogSource = null;
//  팝업 관련 전역 변수
var popupOverlay = null;
var popupContainer = null;
var popupContent = null;
var popupCloser = null;
// 동그란 마커 스타일
var dogMarkerStyle = new ol.style.Style({
  image: new ol.style.Circle({
    radius: 6,
    fill: new ol.style.Fill({
      color: 'rgba(64, 156, 255, 1)'    // 안쪽 색
    }),
    stroke: new ol.style.Stroke({
      color: 'rgba(255, 255, 255, 1)',  // 테두리 흰색
      width: 2
    })
  })
});

// Polygon / MultiPolygon 중심에 마커 찍는 스타일 함수
function dogMarkerStyleFn(feature) {
  var geom = feature.getGeometry();
  var type = geom.getType();

  // 이미 Point면 그대로 마커 스타일
  if (type === 'Point') {
    return dogMarkerStyle;
  }

  // Polygon, MultiPolygon이면 중심점에 Point를 새로 찍어서 그려줌
  if (type === 'Polygon' || type === 'MultiPolygon') {
    var center = ol.extent.getCenter(geom.getExtent());
    return new ol.style.Style({
      geometry: new ol.geom.Point(center),
      image: dogMarkerStyle.getImage()
    });
  }

  return dogMarkerStyle;
}
function updateDogList(features) {
    var html = "";

    features.forEach(f => {
        var name = f.get("park_nm") || "(이름 없음)";
        var addr = f.get("addr") || "";

        html += `
          <div class="dog-item">
            <b>${name}</b><br>
            <small>${addr}</small>
            <hr/>
          </div>
        `;
    });

    $("#dog-list").html(html);
}

// ✅ 클릭한 피처 정보 팝업에 표시 + 마커위에 위치
function showDogPopup(feature) {
  if (!feature || !popupOverlay || !popupContent) return;

  // 클러스터 레이어인 경우, 안에 실제 피처 목록이 들어있음
  var originalFeatures = feature.get('features');
  if (originalFeatures && originalFeatures.length) {
    feature = originalFeatures[0];
  }

  var props = feature.getProperties();
  delete props.geometry;

  var sd_nm   = props.sd_nm   || '';
  var sgg_nm  = props.sgg_nm  || '';
  var park_nm = props.park_nm || '';
  var addr    = props.addr    || '';
  var oper_tm = props.oper_tm || '';
  var hldy    = props.hldy    || '';
  var fcar    = props.fcar    || '';
  var telno   = props.telno   || '';

  var html = ''
    + '<b>' + park_nm + '</b><br/>'
    + (sd_nm || sgg_nm ? sd_nm + ' ' + sgg_nm + '<br/>' : '')
    + (addr    ? '주소: ' + addr    + '<br/>' : '')
    + (oper_tm ? '운영시간: ' + oper_tm + '<br/>' : '')
    + (hldy    ? '휴무일: ' + hldy    + '<br/>' : '')
    + (fcar    ? '면적: ' + fcar    + '<br/>' : '')
    + (telno   ? '연락처: ' + telno   + '<br/>' : '');

  popupContent.innerHTML = html;

  // 위치: 포인트면 그대로, 폴리곤/멀티폴리곤이면 중심점
  var geom = feature.getGeometry();
  if (!geom) return;

  var coord;
  if (geom.getType() === 'Point') {
    coord = geom.getCoordinates();
  } else {
    coord = ol.extent.getCenter(geom.getExtent());
  }

  popupOverlay.setPosition(coord);
}

// 선택한 피처 위치로 확대 (원하면 같이 사용)
function zoomToDogFeature(feature) {
  if (!feature || !baseMap) return;

  var originalFeatures = feature.get('features');
  if (originalFeatures && originalFeatures.length) {
    feature = originalFeatures[0];
  }

  var geom = feature.getGeometry();
  if (!geom) return;

  var extent = geom.getExtent ? geom.getExtent() : null;
  if (!extent) return;

  baseMap.getView().fit(extent, {
    padding: [50, 50, 50, 50],
    maxZoom: 16,
    duration: 500
  });
}
$(".layer-btn").on("click", function () {
    var type = $(this).data("type");

    switch (type) {
        case "base":
            baseLayer.setSource(new ol.source.TileWMS({
                url: _vectorMapUrl,
                serverType: "mapserver"
            }));
            break;

        case "sat":
            baseLayer.setSource(
                new ol.source.XYZ({
                    url: "https://map.pstatic.net/nrb/styles/satellite/{z}/{x}/{y}.jpg"
                })
            );
            break;

        case "terrain":
            baseLayer.setSource(
                new ol.source.XYZ({
                    url: "https://map.pstatic.net/nrb/styles/terrain/{z}/{x}/{y}.png"
                })
            );
            break;
    }
});

$(document).ready(function () {
	popupContainer = document.getElementById('popup');
  popupContent   = document.getElementById('popup-content');
  popupCloser    = document.getElementById('popup-closer');

  $("#chkDog").prop("checked", USE_DOG);

  $.ajax({
    url: "/board-test/api/map/getDogApi.do",
    type: "GET",
    contentType: "application/json;charset=UTF-8",
    dataType: "json",
    success: function (data, status) {
      dogWfsJson = data;
      initMap();
    },
    error: function (status) {
      alert(status + "dsadsad");
    }
  });

  // 체크박스로 레이어 on/off
  $("#chkDog").on("change", function () {
    if (gsDog) {
      gsDog.setVisible(this.checked);
    }
  });
}); 
			

    


function initMap() {
  // 좌표계 정의
  proj4.defs("EPSG:3857", "+proj=merc +lon_0=0 +k=1 +x_0=0 +y_0=0 +datum=WGS84 +units=m +no_defs");
  ol.proj.proj4.register(proj4);
  var proj3857 = ol.proj.get('EPSG:3857');

  var resolutions = [156543.03, 78271.52, 39135.76, 19567.88, 9783.94, 4891.96981025128125, 2445.98490512, 1222.99245256, 611.49622628, 305.74811314, 152.87405657, 76.43702828, 38.21851414, 19.10925707, 9.55462853, 4.77731426, 2.38865713, 1.19433, 0.5972, 0.298583];
  var tileExtent = [-20037508.3427892439067364, -20037508.3427892550826073, 20037508.3427892439067364, 20037508.3427892439067364];
  var minZoomLevel = 0;
  var maxZoomLevel = 10;

  var view = new ol.View({
    projection: proj3857,
    extent: tileExtent,
    center: [14177553.107181, 4308348.8448386],
    zoom: 1,
    minZoom: minZoomLevel,
    maxZoom: maxZoomLevel,
    maxResolution: 1954.597389
  });

  // 베이스맵
  baseMap = new ol.Map({
    target: 'baseMap',
    layers: [
      new ol.layer.Tile({
        division: 'TILE',
        layerName: 'BASEMAP',
        visible: true,
        source: new ol.source.TileWMS({
          matrixSet: 'EPSG:3857',
          projection: 'EPSG:3857',
          hidpi: false,
          tileGrid: new ol.tilegrid.TileGrid({
            extent: tileExtent,
            origin: [tileExtent[0], tileExtent[1]],
            resolutions: resolutions
          }),
          url: _vectorMapUrl,
          serverType: "mapserver"
        })
      })
    ],
    controls: ol.control.defaults({
      attributionOptions: ({
        collapsible: false
      })
    }),
    view: view
  });
popupOverlay = new ol.Overlay({
  element: popupContainer,
  autoPan: true,
  autoPanAnimation: {
    duration: 250
  }
});
baseMap.addOverlay(popupOverlay)
  // ✅ 1) WFS GeoJSON → Feature 변환 (이미 3857이므로 재투영 X)
  var dogFeatures = new ol.format.GeoJSON().readFeatures(dogWfsJson, {
    dataProjection: 'EPSG:3857',
    featureProjection: 'EPSG:3857'
  });
updateDogList(dogFeatures);

  // ✅ 2) 소스 생성
  dogSource = new ol.source.Vector({
    features: dogFeatures
  });

  // ✅ 3) 마커 레이어 생성
  gsDog = new ol.layer.Vector({
    visible: USE_DOG,        // 초기 표시 여부
    source: dogSource,
    style: dogMarkerStyleFn  // 중심에 마커 찍는 스타일
  });

  // ✅ 4) 지도에 추가
  baseMap.addLayer(gsDog);

  // ✅ 5) WFS 영역으로 줌 맞추기
  if (dogSource.getFeatures().length > 0) {
    baseMap.getView().fit(dogSource.getExtent(), {
      padding: [50, 50, 50, 50],
      maxZoom: 12
    });
  }

  // (아래 선택 인터랙션 부분은 네 코드 그대로 유지)

  var selectSingleClick = new ol.interaction.Select({
    multi: true
  });


baseMap.addInteraction(selectSingleClick);

 

selectSingleClick.on('select', function (e) {
  var selected = e.selected;

  if (!selected || selected.length === 0) {
    if (popupOverlay) popupOverlay.setPosition(undefined);
    return;
  }

  var feature = selected[0];

  // 마커 위 팝업
  showDogPopup(feature);

  // 확대
  zoomToDogFeature(feature);
});
}



