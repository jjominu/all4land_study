// ======================= VWORLD 설정 ==========================
const VWORLD_KEY = "17C7EB57-45CC-3193-9DB9-AADAC973D076";

// ======================= 전역 변수 ============================
var USE_DOG  = true; // 초기 표시 여부

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

// ======================= 팝업 ==========================
function updateDogList(features) {

  features.forEach(f => {
	var id = f.get("id");
    var name = f.get("park_nm") || "(이름 없음)";
    var addr = f.get("addr") || "";
    

  
  });

}

//팝업
function showDogPopup(feature) {
  if (!feature || !popupOverlay || !popupContent) return;

  // 클러스터 레이어인 경우, 안에 실제 피처 목록이 들어있음
  var originalFeatures = feature.get('features');
  if (originalFeatures && originalFeatures.length) {
    feature = originalFeatures[0];
  }

  var props = feature.getProperties();
  delete props.geometry;

  var sd_nm   = props.sd_nm   || '정보 없음';
  var sgg_nm  = props.sgg_nm  || '정보 없음';
  var park_nm = props.park_nm || '정보 없음';
  var addr    = props.addr    || '정보 없음';
  var oper_tm = props.oper_tm || '정보 없음';
  var hldy    = props.hldy    || '정보 없음';
  var fcar    = props.fcar    || '정보 없음';
  var telno   = props.telno   || '정보 없음';

  var html = ''
    + '<b>' + park_nm + '</b><br/>'
    + (sd_nm || sgg_nm ? sd_nm + ' ' + sgg_nm + '<br/>' : '')
    + ( '주소: ' + addr    + '<br/>' )
    + ( '운영시간: ' + oper_tm + '<br/>' )
    + ( '휴무일: ' + hldy    + '<br/>' )
    + ( '면적: ' + fcar    + '<br/>' )
    + ( '연락처: ' + telno   + '<br/>' );

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



function focusDogOnMap(id) {
  if (!dogSource || !baseMap) return;

  // id가 문자열이면 그대로, 숫자면 String 변환해서 사용하는 것도 안전
  var feature = dogSource.getFeatureById(String(id));

  if (!feature) {
    alert("지도에 해당 지점이 없습니다. (id=" + id + ")");
    return;
  }

  var geom = feature.getGeometry();
  if (!geom) return;

  var coord;
  if (geom.getType() === "Point") {
    // 포인트면 그대로 좌표 사용
    coord = geom.getCoordinates();
  } else {
    // Polygon, MultiPolygon 같은 경우는 extent 중심 사용
    coord = ol.extent.getCenter(geom.getExtent());
  }

  // 팝업 같이 띄우기
  showDogPopup(feature);
  zoomToDogFeature(feature);
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



// ======================= 문서 로딩 ============================
$(document).ready(function () {
  popupContainer = document.getElementById('popup');
  popupContent   = document.getElementById('popup-content');
  popupCloser    = document.getElementById('popup-closer');
 // ⭐ 여기 추가
  if (popupCloser) {
    popupCloser.onclick = function () {
      popupOverlay.setPosition(undefined);
      popupCloser.blur();
      return false;
    };
  }
  if (popupCloser && popupOverlay) {
    popupCloser.onclick = function () {
      popupOverlay.setPosition(undefined);
      return false;
    };
  }

  $("#chkDog").prop("checked", USE_DOG);
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

  }
});
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
  var view = new ol.View({
    projection: 'EPSG:3857',
    center: [14177553.107181, 4308348.8448386], 
    zoom: 7,
    minZoom: 6,
    maxZoom: 19
  });

  //VWorld 기본지도 레이어
  baseLayer = new ol.layer.Tile({
    division: 'TILE',
    layerName: 'VWORLD_BASE',
    visible: true,
    source: new ol.source.XYZ({
      url: 'http://api.vworld.kr/req/wmts/1.0.0/' + VWORLD_KEY + '/Base/{z}/{y}/{x}.png'
    })
  });

  //지도 생성
  baseMap = new ol.Map({
    target: 'baseMap',  
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

  //팝업 오버레이 생성
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

  //WFS GeoJSON → Feature 변환
  var dogFeatures = new ol.format.GeoJSON().readFeatures(dogWfsJson, {
    dataProjection: 'EPSG:3857',
    featureProjection: 'EPSG:3857'
  });
  dogFeatures.forEach(function (f) {
    var attrId = f.get("id");  
    console.log(f.get("id"))   // GeoJSON 속성 id
    if (attrId !== undefined && attrId !== null) {
        f.setId(String(attrId));  // OL feature ID로 설정
    }
});
  updateDogList(dogFeatures);

  //벡터 소스 레이어 생성
  dogSource = new ol.source.Vector({
    features: dogFeatures
  });

  gsDog = new ol.layer.Vector({
    visible: USE_DOG,        // 초기 표시 여부
    source: dogSource,
    style: dogMarkerStyleFn  // 중심에 마커 찍는 스타일
  });

  // 지도에 추가
  baseMap.addLayer(gsDog);

  //WFS 영역으로 줌 맞추기
  if (dogSource.getFeatures().length > 0) {
    baseMap.getView().fit(dogSource.getExtent(), {
      padding: [50, 50, 50, 50],
      maxZoom: 12
    });
  }

  //  8) 클릭 선택 인터랙션
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
