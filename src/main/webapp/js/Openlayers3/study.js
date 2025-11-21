// ======================= VWORLD 설정 ==========================
const VWORLD_KEY = "17C7EB57-45CC-3193-9DB9-AADAC973D076";
var USE_DOG = true; // 초기 표시 여부

// 전역 변수 선언
var baseMap = null;
var baseLayer = null;
var gsDog = null;
var dogSource = null;
var dogWfsJson = null; // 데이터 저장용

// 팝업 관련 변수
var popupOverlay = null;
var popupContainer = null;
var popupContent = null;
var popupCloser = null;

// ======================= 스타일 정의 (핵심 수정) ==========================

// 1. 마커 이미지 (점)
var baseMarkerImage = new ol.style.Circle({
    radius: 6,
    fill: new ol.style.Fill({ color: '#ffffff' }),
    stroke: new ol.style.Stroke({ color: '#0d6efd', width: 2 })
});

var selectedMarkerImage = new ol.style.Circle({
    radius: 8,
    fill: new ol.style.Fill({ color: '#dc3545' }),
    stroke: new ol.style.Stroke({ color: '#ffffff', width: 3 })
});

// 2. 폴리곤 스타일 (면)
var polygonStyle = new ol.style.Style({
    stroke: new ol.style.Stroke({
        color: 'rgba(13, 110, 253, 0.6)',
        width: 2
    }),
    fill: new ol.style.Fill({
        color: 'rgba(13, 110, 253, 0.1)'
    })
});

var selectedPolygonStyle = new ol.style.Style({
    stroke: new ol.style.Stroke({
        color: '#dc3545',
        width: 3
    }),
    fill: new ol.style.Fill({
        color: 'rgba(220, 53, 69, 0.2)'
    })
});

// ★ [평상시 스타일] 폴리곤이면 "면 + 중심점"을 배열로 반환
function dogStyleFunction(feature) {
    var geom = feature.getGeometry();
    var type = geom.getType();
    var styles = [];

    // 1. 폴리곤인 경우: 면도 그리고 + 중심에 점도 찍음
    if (type === 'Polygon' || type === 'MultiPolygon') {
        styles.push(polygonStyle); // 면 그리기

        var center = ol.extent.getCenter(geom.getExtent());
        styles.push(new ol.style.Style({
            geometry: new ol.geom.Point(center),
            image: baseMarkerImage // 점 그리기
        }));
    } 
    // 2. 점인 경우: 점만 찍음
    else if (type === 'Point') {
        styles.push(new ol.style.Style({
            image: baseMarkerImage
        }));
    }

    return styles;
}

// ★ [선택시 스타일] 빨간색 면 + 빨간색 점
function selectStyleFunction(feature) {
    var geom = feature.getGeometry();
    var type = geom.getType();
    var styles = [];

    if (type === 'Polygon' || type === 'MultiPolygon') {
        styles.push(selectedPolygonStyle); // 빨간 면

        var center = ol.extent.getCenter(geom.getExtent());
        styles.push(new ol.style.Style({
            geometry: new ol.geom.Point(center),
            image: selectedMarkerImage, // 빨간 점
            zIndex: 999
        }));
    } else {
        styles.push(new ol.style.Style({
            image: selectedMarkerImage,
            zIndex: 999
        }));
    }
    return styles;
}


// ======================= 기능 함수들 ==========================

function showDogPopup(feature) {
    if (!feature || !popupContent) return;

    var props = feature.getProperties();
    
    // 1. 속성 값 가져오기
    var park_nm = props.park_nm || '이름 없음';
    var addr    = props.addr    || '주소 없음';
    var oper_tm = props.oper_tm || '';
    var telno   = props.telno   || '-';
    var fcs     = props.fcs     || '-';
    
    // DB 컬럼명 'park_img' 가져오기
    var park_img = props.park_img; 

    // 2. 이미지 HTML 생성
    // 이미지가 있으면 <img> 태그 생성, 없으면 빈 문자열
    var imgHtml = '';
    if (park_img) {
        imgHtml = `
            <div style="width:100%; height:120px; overflow:hidden; border-radius:8px; margin-bottom:12px; border:1px solid #eee;">
                <img src="/uploads/${park_img}" 
                     style="width:100%; height:100%; object-fit:cover;" 
                     alt="${park_nm}"
                     onerror="this.parentElement.style.display='none'"> 
            </div>
        `;
    }

    // 3. 전체 HTML 조립
    var html = `
        <div class="text-start" style="min-width: 220px;">
            
            ${imgHtml}

            <div class="d-flex align-items-center justify-content-between mb-2">
                <h6 class="fw-bold mb-0" style="font-size:1.1rem; color:#333;">${park_nm}</h6>
                <a href="/board-test/map/detail.do?id=${feature.getId()}"target="_blank" class="text-decoration-none text-primary fw-bold small">
                    상세보기 <i class="bi bi-chevron-right"></i>
                </a>
            </div>
            
            <p class="text-secondary small mb-2" style="font-size:0.85rem;">
                <i class="bi bi-geo-alt-fill text-danger me-1"></i>${addr}
            </p>
            
            <hr class="my-2" style="opacity:0.1">
            
            <div style="font-size: 0.85rem; line-height: 1.6;">
                ${ oper_tm ? `<div><span class="text-muted me-2">운영시간</span>${oper_tm}</div>` : '' }
                <div><span class="text-muted me-2">연락처</span>${telno}</div>
                <div><span class="text-muted me-2">시설</span><span class="d-inline-block text-truncate" style="max-width:150px; vertical-align:bottom;">${fcs}</span></div>
            </div>
        </div>
    `;

    popupContent.innerHTML = html;

    // 팝업 위치 설정
    var geom = feature.getGeometry();
    var coord = ol.extent.getCenter(geom.getExtent());
    popupOverlay.setPosition(coord);
}

function focusDogOnMap(id) {
    if (!dogSource || !baseMap) return;
    
    var feature = dogSource.getFeatureById(String(id));
    if (!feature) {
        alert("지도에 해당 지점이 없습니다. (id=" + id + ")");
        return;
    }
    
    // 팝업 표시 및 이동
    showDogPopup(feature);
    zoomToDogFeature(feature);
    
    // (선택적) 강제 선택 효과를 주려면 interaction 객체를 전역으로 빼서 selectInteraction.getFeatures().push(feature) 해야 함
}

function zoomToDogFeature(feature) {
    if (!feature || !baseMap) return;
    var geom = feature.getGeometry();
    if (!geom) return;

    baseMap.getView().fit(geom.getExtent(), {
        padding: [100, 100, 100, 100],
        maxZoom: 16,
        duration: 600
    });
}

// ======================= 초기화 및 이벤트 (DOM Ready) ==========================

$(document).ready(function () {
    console.log("✅ study.js 초기화 시작");

    // 1. 팝업 요소 바인딩
    popupContainer = document.getElementById('popup');
    popupContent   = document.getElementById('popup-content');
    popupCloser    = document.getElementById('popup-closer');

    if (popupCloser) {
        popupCloser.onclick = function () {
            popupOverlay.setPosition(undefined);
            popupCloser.blur();
            return false;
        };
    }

    // 2. 체크박스 초기화
    $("#chkDog").prop("checked", USE_DOG);

    // 3. 레이어 버튼 클릭 이벤트
    $(".layer-btn").on("click", function () {
        $(".layer-btn").removeClass("active btn-primary text-white").addClass("btn-outline-secondary");
        $(this).addClass("active btn-primary text-white").removeClass("btn-outline-secondary");

        var type = $(this).data("type");
        if (baseLayer) {
            var url = (type === 'base') 
                ? 'http://api.vworld.kr/req/wmts/1.0.0/' + VWORLD_KEY + '/Base/{z}/{y}/{x}.png'
                : 'http://api.vworld.kr/req/wmts/1.0.0/' + VWORLD_KEY + '/Satellite/{z}/{y}/{x}.jpeg';
            baseLayer.setSource(new ol.source.XYZ({ url: url }));
        }
    });

    // 4. 데이터 로드
    $.ajax({
        url: "/board-test/api/map/getDogApi.do",
        type: "GET",
        contentType: "application/json;charset=UTF-8",
        dataType: "json",
        success: function (data) {
            console.log("✅ 데이터 수신 완료:", data);
            dogWfsJson = data;
            initMap(); 
        },
        error: function (xhr, status, err) {
            console.error("데이터 로드 실패:", status);
            alert("지도 데이터를 불러오지 못했습니다.");
        }
    });

    // 5. 레이어 On/Off
    $("#chkDog").on("change", function () {
        if (gsDog) gsDog.setVisible(this.checked);
    });

    // 6. 검색 폼 (AJAX Load)
   $("#parkSearchForm").on("submit", function (e) {
  e.preventDefault();

  var formData = $(this).serialize();   

  $.ajax({
    url: "/board-test/api/map/ajaxDogList.do",
    type: "POST",              
    data: formData,           
    success: function (html) {
      $("#dog-list").html(html); 
    },
    error: function (xhr, status, err) {
      console.error("ajaxDogList 실패:", status, err);
      alert("목록 조회 중 오류가 발생했습니다.");
    }
  });
});
    // 7. 시군구 선택 로직
    const sggData = {
        "서울": ["강북구", "광진구", "마포구", "동작구", "영등포구", "구로구", "송파구", "도봉구", "동대문구", "강서구"],
        "인천": ["미추홀구", "계양구", "연수구", "남동구"]
    };

    $("#sdNm").on("change", function () {
        const sd = $(this).val();
        const $sggSelect = $("#sggNm");
        $sggSelect.empty().append('<option value="">시군구 선택</option>');

        if (sd && sggData[sd]) {
            sggData[sd].forEach(function (sgg) {
                $sggSelect.append('<option value="' + sgg + '">' + sgg + '</option>');
            });
        }
    });
    // ★ 필터 초기화 버튼 클릭 이벤트
    $("#btnReset").on("click", function() {
        // 1. 폼 내부의 모든 입력값 초기화 (텍스트, 셀렉트박스 등)
        $("#parkSearchForm")[0].reset();

        // 2. 시/군/구 셀렉트박스도 강제로 초기화 (옵션 날리기)
        $("#sggNm").empty().append('<option value="">시군구 선택</option>');

        // 3. 지도와 리스트를 '전체 상태'로 되돌리기 위해 검색(submit) 강제 실행
        //    (빈 값으로 검색하면 전체 리스트가 나오도록 백엔드가 되어있다고 가정)
        $("#parkSearchForm").trigger("submit");
        
        // 만약 지도를 처음에 로드했던 상태(줌 레벨 등)로 돌리고 싶다면:
        
    });

}); // ready 끝


// ======================= 지도 초기화 함수 ==========================

function initMap() {
    var view = new ol.View({
        projection: 'EPSG:3857',
        center: [14177553.107181, 4308348.8448386],
        zoom: 7,
        minZoom: 6,
        maxZoom: 19
    });

    baseLayer = new ol.layer.Tile({
        source: new ol.source.XYZ({
            url: 'http://api.vworld.kr/req/wmts/1.0.0/' + VWORLD_KEY + '/Base/{z}/{y}/{x}.png'
        })
    });

    baseMap = new ol.Map({
        target: 'baseMap',
        layers: [baseLayer],
        controls: ol.control.defaults({ attribution: false, zoom: false }),
        view: view
    });

    popupOverlay = new ol.Overlay({
        element: popupContainer,
        autoPan: true,
        autoPanAnimation: { duration: 250 },
        offset: [0, -10]
    });
    baseMap.addOverlay(popupOverlay);

    // 데이터 파싱
    var dogFeatures = new ol.format.GeoJSON().readFeatures(dogWfsJson, {
        dataProjection: 'EPSG:3857', // 3857로 잘 나온다 하셨으므로 유지
        featureProjection: 'EPSG:3857'
    });

    // ID 세팅
    dogFeatures.forEach(function (f) {
        var attrId = f.get("id");
        if (attrId) f.setId(String(attrId));
    });

    dogSource = new ol.source.Vector({ features: dogFeatures });

    // 벡터 레이어 생성 (여기서 styleFunction 연결)
    gsDog = new ol.layer.Vector({
        visible: USE_DOG,
        source: dogSource,
        style: dogStyleFunction // ★ 평상시 스타일 함수
    });
    baseMap.addLayer(gsDog);

    // 인터랙션 설정 (클릭 시 스타일 변경)
    var selectInteraction = new ol.interaction.Select({
        multi: true,
        style: selectStyleFunction // ★ 선택시 스타일 함수 (여기에 폴리곤+점 로직 포함됨)
    });

    baseMap.addInteraction(selectInteraction);

    selectInteraction.on('select', function (e) {
        var selected = e.selected;
        if (selected.length > 0) {
            var feature = selected[0];
            showDogPopup(feature);
            zoomToDogFeature(feature);
        } else {
            popupOverlay.setPosition(undefined);
        }
    });
    
    // 초기 줌 설정
    if (dogSource.getFeatures().length > 0) {
        baseMap.getView().fit(dogSource.getExtent(), { padding: [50,50,50,50], maxZoom: 12 });
    }
}