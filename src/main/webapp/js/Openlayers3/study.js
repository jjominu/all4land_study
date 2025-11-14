// ======================= VWORLD 설정 ==========================
const VWORLD_KEY = "17C7EB57-45CC-3193-9DB9-AADAC973D076";

// ======================= 전역 변수 ============================
var USE_DOG  = false; // 초기 표시 여부

var baseMap = null;
var baseLayer = null;   // 베이스맵 레이어
var gsDog   = null;
var dogWfsJson = null;
var dogSource = null;

//  팝업 관련 전역 변수
var popupOverlay = null;
var popupContainer = null;
var popupContent = null;
var popupCloser = null;

// ======================= 스타일 ===============================
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

// ======================= 목록 & 팝업 ==========================
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

// ======================= 레이어 버튼 (VWorld 베이스맵 스위칭) ======================
$(".layer-btn").on("click", function () {
  var type = $(this).data("type");
  if (!baseLayer) return;

  switch (type) {
    case "base":
      baseLayer.setSource(new ol.source.XYZ({
        url: 'http://api.vworld.kr/req/wmts/1.0.0/' + VWORLD_KEY + '/Base/{z}/{y}/{x}.png'
      }));
      break;

    case "sat":
      baseLayer.setSource(new ol.source.XYZ({
        url: 'http://api.vworld.kr/req/wmts/1.0.0/' + VWORLD_KEY + '/Satellite/{z}/{y}/{x}.jpeg'
      }));
      break;

    case "terrain":  // 지형/gray 계열
      baseLayer.setSource(new ol.source.XYZ({
        url: 'http://api.vworld.kr/req/wmts/1.0.0/' + VWORLD_KEY + '/gray/{z}/{y}/{x}.png'
      }));
      break;
  }
});

// ======================= 문서 로딩 ============================
$(document).ready(function () {
  popupContainer = document.getElementById('popup');
  popupContent   = document.getElementById('popup-content');
  popupCloser    = document.getElementById('popup-closer');

  if (popupCloser && popupOverlay) {
    popupCloser.onclick = function () {
      popupOverlay.setPosition(undefined);
      return false;
    };
  }

  $("#chkDog").prop("checked", USE_DOG);

  // GeoServer WFS(JSON) 호출
  $.ajax({
    url: "/board-test/api/map/getDogApi.do",
    type: "GET",
    contentType: "application/json;charset=UTF-8",
    dataType: "json",
    success: function (data, status) {
      dogWfsJson = data;
      initMap();   // 데이터 받은 뒤 지도 초기화
    },
    error: function (xhr, status, err) {
      alert("반려견 놀이터 WFS 호출 실패: " + status);
    }
  });

  // 체크박스로 레이어 on/off
  $("#chkDog").on("change", function () {
    if (gsDog) {
      gsDog.setVisible(this.checked);
    }
  });
});

// ======================= 지도 초기화 ==========================
function initMap() {
  // VWorld WMTS는 기본적으로 EPSG:3857 이라서 proj4 별도 설정 없어도 됨
  var view = new ol.View({
    projection: 'EPSG:3857',
    center: [14177553.107181, 4308348.8448386], // 적당히 한반도 중앙
    zoom: 7,
    minZoom: 6,
    maxZoom: 19
  });

  // ✅ 1) VWorld 기본지도 레이어
  baseLayer = new ol.layer.Tile({
    division: 'TILE',
    layerName: 'VWORLD_BASE',
    visible: true,
    source: new ol.source.XYZ({
      url: 'http://api.vworld.kr/req/wmts/1.0.0/' + VWORLD_KEY + '/Base/{z}/{y}/{x}.png'
    })
  });

  // ✅ 2) 지도 생성
  baseMap = new ol.Map({
    target: 'baseMap',   // <div id="baseMap"> 이어야 함
    layers: [
      baseLayer
    ],
    controls: ol.control.defaults({
      attributionOptions: {
        collapsible: false
      }
    }),
    view: view
  });

  // ✅ 3) 팝업 오버레이 생성
  popupOverlay = new ol.Overlay({
    element: popupContainer,
    autoPan: true,
    autoPanAnimation: {
      duration: 250
    }
  });
  baseMap.addOverlay(popupOverlay);

  if (popupCloser) {
    popupCloser.onclick = function () {
      popupOverlay.setPosition(undefined);
      return false;
    };
  }

  // ✅ 4) WFS GeoJSON → Feature 변환 (이미 3857이므로 재투영 X)
  var dogFeatures = new ol.format.GeoJSON().readFeatures(dogWfsJson, {
    dataProjection: 'EPSG:3857',
    featureProjection: 'EPSG:3857'
  });
  updateDogList(dogFeatures);

  // ✅ 5) 벡터 소스 & 레이어 생성
  dogSource = new ol.source.Vector({
    features: dogFeatures
  });

  gsDog = new ol.layer.Vector({
    visible: USE_DOG,        // 초기 표시 여부
    source: dogSource,
    style: dogMarkerStyleFn  // 중심에 마커 찍는 스타일
  });

  // ✅ 6) 지도에 추가
  baseMap.addLayer(gsDog);

  // ✅ 7) WFS 영역으로 줌 맞추기
  if (dogSource.getFeatures().length > 0) {
    baseMap.getView().fit(dogSource.getExtent(), {
      padding: [50, 50, 50, 50],
      maxZoom: 12
    });
  }

  // ✅ 8) 클릭 선택 인터랙션
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
