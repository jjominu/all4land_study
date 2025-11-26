// ======================= VWORLD 설정 ==========================
const VWORLD_KEY = "17C7EB57-45CC-3193-9DB9-AADAC973D076";
var USE_DOG = true; // 초기 표시 여부

// 전역 변수 선언
var baseMap = null;
var baseLayer = null;
var gsDog = null;
var dogSource = null;
var dogWfsJson = null; // 전체 데이터 저장용

// 팝업 관련 변수
var popupOverlay = null;
var popupContainer = null;
var popupContent = null;
var popupCloser = null;

// ======================= 스타일 정의 ==========================

// 마커 이미지 (점)
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

// 폴리곤 스타일 (면)
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
//  클러스터 스타일 캐시
var styleCache = {};

//클러스터 스타일 함수
function clusterStyleFunction(feature) {
    var size = feature.get('features').length; 

    if (size === 1) {
        var originalFeature = feature.get('features')[0];
        var props = originalFeature.getProperties();
        var styles = [];

        //  폴리곤 그리기
        styles.push(new ol.style.Style({
            stroke: new ol.style.Stroke({ color: 'rgba(13, 110, 253, 0.6)', width: 2 }),
            fill: new ol.style.Fill({ color: 'rgba(13, 110, 253, 0.1)' }),
            geometry: originalFeature.getGeometry() 
        }));

        //  중심점 핀 그리기 
        if (props.center_geom && props.center_geom.coordinates) {
            styles.push(new ol.style.Style({
                image: baseMarkerImage,
                geometry: new ol.geom.Point(props.center_geom.coordinates)
            }));
        }

        return styles;
    }

    //클러스터 
    var style = styleCache[size];
    if (!style) {
        style = new ol.style.Style({
            image: new ol.style.Circle({
                radius: 15, // 원 크기
                stroke: new ol.style.Stroke({ color: '#fff', width: 2 }),
                fill: new ol.style.Fill({ color: '#0d6efd' }) 
            }),
            text: new ol.style.Text({
                text: size.toString(), // 개수 표시
                fill: new ol.style.Fill({ color: '#fff' }),
                font: 'bold 12px "Noto Sans KR", sans-serif'
            })
        });
        styleCache[size] = style;
    }
    return style;
}
function dogStyleFunction(feature) {
    var geom = feature.getGeometry();
    var type = geom.getType();
    var styles = [];
    var props = feature.getProperties();

    // 1. 폴리곤 그리기
    if (type === 'Polygon' || type === 'MultiPolygon') {
        styles.push(polygonStyle); // 면 스타일

        var pointGeom;
        if (props.center_geom && props.center_geom.coordinates) {
            // GeoJSON 좌표로 바로 Point 생성
            pointGeom = new ol.geom.Point(props.center_geom.coordinates);
        } else {
            // 없으면 계산
            var center = ol.extent.getCenter(geom.getExtent());
            pointGeom = new ol.geom.Point(center);
        }

        // 마커 스타일 추가
        styles.push(new ol.style.Style({
            geometry: pointGeom,
            image: baseMarkerImage
        }));
    } 
    // 2. 점 데이터인 경우
    else if (type === 'Point') {
        styles.push(new ol.style.Style({ image: baseMarkerImage }));
    }

    return styles;
}
//선택시 스타일
function selectStyleFunction(feature) {
    var geom = feature.getGeometry();
    var type = geom.getType();
    var styles = [];

    if (type === 'Polygon' || type === 'MultiPolygon') {
        styles.push(selectedPolygonStyle);
        var center = ol.extent.getCenter(geom.getExtent());
        styles.push(new ol.style.Style({
            geometry: new ol.geom.Point(center),
            image: selectedMarkerImage,
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


// ======================= 팝업 표시 함수 ==========================
function showDogPopup(feature) {
    if (!feature || !popupContent) return;

    var props = feature.getProperties();
    var id = feature.getId(); 

    var park_nm = props.park_nm || '이름 없음';
    var addr    = props.addr    || '주소 없음';
    var oper_tm = props.oper_tm || '';
    var telno   = props.telno   || '-';
    var fcs     = props.fcs     || '-';
    var park_img = props.park_img; 

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

    var html = `
        <div class="text-start" style="min-width: 220px;">
            ${imgHtml}

            <div class="d-flex align-items-center justify-content-between mb-2">
                <div class="d-flex align-items-center gap-2">
                    <h6 class="fw-bold mb-0" style="font-size:1.1rem; color:#333;">${park_nm}</h6>
                    <i id="popup_bm_icon" class="bi bi-bookmark-star" 
                       style="font-size:1.2rem; cursor:pointer; color:#ccc;"
                       onclick="togglePopupBookmark('${id}')" 
                       title="즐겨찾기 추가/해제"></i>
                </div>
                
                <a href="/test/map/detail.do?id=${id}" target="_blank" class="text-decoration-none text-primary fw-bold small">
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

    checkPopupBookmarkStatus(id);

    var coord = null;

    if (props.center_geom && props.center_geom.coordinates) {
        coord = props.center_geom.coordinates;
    } 
    else {
        var geom = feature.getGeometry();
        if (geom) {
            coord = ol.extent.getCenter(geom.getExtent());
        }
    }

    if (coord) {
        popupOverlay.setPosition(coord);
    } else {
        console.error("팝업 좌표를 결정할 수 없습니다. Feature:", feature);
    }
}
// 팝업 내 즐겨찾기 토글
function togglePopupBookmark(parkId) {
   
    if (!parkId) {
        alert("공원 정보를 찾을 수 없습니다.");
        return;
    }
    
    var cleanId = String(parkId).replace(/[^0-9]/g, ""); 
    
    if (cleanId === "") {
        console.error("유효하지 않은 ID입니다:", parkId);
        return;
    }

    $.ajax({
        url: "/test/api/bookmark/toggle.do",
        type: "POST",
        data: { parkId: cleanId }, 
        success: function(res) {
            var icon = $("#popup_bm_icon");
            if(res === 'inserted') {
                icon.removeClass('bi-bookmark-star').addClass('bi-bookmark-star-fill text-warning');
            } else if (res === 'login_required') {
                alert("로그인이 필요한 서비스입니다.");
            } else {
                icon.removeClass('bi-bookmark-star-fill text-warning').addClass('bi-bookmark-star');
                // alert("즐겨찾기가 해제되었습니다.");
            }
        },
        error: function(err) {
            alert("로그인이 필요한 서비스입니다.");
        }
    });
}

// 팝업 열릴 때 상태 체크
function checkPopupBookmarkStatus(parkId) {
    if (!parkId) return;

    // 문자열로 변환 후 숫자만 남기기
    var cleanId = String(parkId).replace(/[^0-9]/g, "");

    if (cleanId === "") return;

    $.ajax({
        url: "/test/api/bookmark/status.do",
        type: "POST",
        data: { parkId: cleanId }, // 정제된 숫자 ID 전송
        success: function(res) {
            var icon = $("#popup_bm_icon");
            if(res > 0) {
                icon.removeClass('bi-bookmark-star').addClass('bi-bookmark-star-fill text-warning');
                icon.css("color", ""); 
            } else {
                icon.removeClass('bi-bookmark-star-fill text-warning').addClass('bi-bookmark-star');
                icon.css("color", "#ccc");
            }
        },
        error: function(err) {

            console.error("상태 체크 에러:", err);
        }
    });
}

function focusDogOnMap(id) {
    if (!dogSource || !baseMap) return;
    var feature = dogSource.getFeatureById(String(id));
    if (!feature) {
        alert("지도에 해당 지점이 없습니다. (id=" + id + ")");
        return;
    }
    showDogPopup(feature);
    zoomToDogFeature(feature);
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

// ======================= 초기화 ==========================

$(document).ready(function () {
    console.log("✅ study.js 초기화 시작");

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

    $("#chkDog").prop("checked", USE_DOG);

    // 레이어 버튼
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

    // 초기 데이터 로드
    $.ajax({
        url: "/test/api/map/getDogApi.do",
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

    $("#chkDog").on("change", function () {
        if (gsDog) gsDog.setVisible(this.checked);
    });

    // 검색 폼
    $("#parkSearchForm").on("submit", function (e) {
        e.preventDefault();
        var formData = $(this).serialize();   
        
        // 리스트 갱신
        $.ajax({
            url: "/test/api/map/ajaxDogList.do",
            type: "POST",              
            data: formData,           
            success: function (html) {
                $("#dog-list").html(html); 
            },
            error: function (xhr, status, err) {
                alert("검색 중 오류가 발생했습니다.");
            }
        });
        
        // 지도 마커 필터링 (클라이언트 사이드)
        if(dogWfsJson && dogSource) {
            var parkNm = $("input[name='parkNm']").val().trim();
            // ... 필요한 다른 필터 값들도 가져와서 filter 로직 수행 ...
            // (단순화를 위해 리스트 로직과 맞추거나, ajaxDogList가 JSON을 리턴하면 더 좋음)
        }
    });

    // 시군구 선택
 const sggData = {
    "서울": [
        "종로구", "중구", "용산구", "성동구", "광진구",
        "동대문구", "중랑구", "성북구", "강북구", "도봉구",
        "노원구", "은평구", "서대문구", "마포구", "양천구",
        "강서구", "구로구", "금천구", "영등포구", "동작구",
        "관악구", "서초구", "강남구", "송파구", "강동구"
    ],

    "인천": [
        "중구", "동구", "미추홀구", "연수구", "남동구",
        "부평구", "계양구", "서구",
        "강화군", "옹진군"
    ],

    "경기": [
        "수원", "성남", "의정부", "안양", "부천",
        "광명", "평택", "동두천", "안산", "고양",
        "과천", "구리", "남양주", "오산", "시흥",
        "군포", "의왕", "하남", "용인", "파주",
        "이천", "안성", "김포", "화성", "광주",
        "양주", "포천", "여주",
        "연천군", "가평군", "양평군"
    ]
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

    // 초기화 버튼
    $("#btnReset").on("click", function() {
        $("#parkSearchForm")[0].reset();
        $("#sggNm").empty().append('<option value="">시군구 선택</option>');
        $("#parkSearchForm").trigger("submit");
        // 전체 데이터 복구
        if(dogWfsJson) {
            dogSource.clear();
            var features = new ol.format.GeoJSON().readFeatures(dogWfsJson, {
                dataProjection: 'EPSG:3857', featureProjection: 'EPSG:3857'
            });
            features.forEach(function(f){ if(f.get("id")) f.setId(String(f.get("id"))); });
            dogSource.addFeatures(features);
            baseMap.getView().fit(dogSource.getExtent(), { padding: [50,50,50,50], maxZoom: 12 });
        }
    });
//  즐겨찾기 모아보기 버튼 클릭
    $("#btnFilterBookmark").on("click", function() {
        
        $.ajax({
            url: "/test/api/map/ajaxBookmarkList.do",
            type: "POST",
            success: function(html) {
                $("#dog-list").html(html);
                
            },
            error: function(xhr) {
                if(xhr.status === 400 || xhr.status === 500) {
                     alert("로그인이 필요한 서비스입니다.");
                } else {
                     alert("로그인이 필요한 서비스입니다.");
                }
            }
        });
    });


});

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

    var dogFeatures = new ol.format.GeoJSON().readFeatures(dogWfsJson, {
        dataProjection: 'EPSG:3857',
        featureProjection: 'EPSG:3857'
    });

    dogFeatures.forEach(function (f) {
        var attrId = f.get("id");
        if (attrId) f.setId(String(attrId));
    });

    dogSource = new ol.source.Vector({ features: dogFeatures });

    var clusterSource = new ol.source.Cluster({
        distance: 40, // 뭉치는 거리 (픽셀 단위)
        source: dogSource,
        geometryFunction: function(feature) {
            var props = feature.getProperties();
            if (props.center_geom && props.center_geom.coordinates) {
                return new ol.geom.Point(props.center_geom.coordinates);
            }
            return null; // 좌표 없으면 무시
        }
    });

    gsDog = new ol.layer.Vector({
        visible: USE_DOG,
        source: clusterSource, // clusterSource 사용
        style: clusterStyleFunction // 위에서 만든 스타일 함수
    });
    baseMap.addLayer(gsDog);

    //인터랙션 
    var selectInteraction = new ol.interaction.Select({
    });
    baseMap.addInteraction(selectInteraction);

    selectInteraction.on('select', function (e) {
        var selected = e.selected;
        if (selected.length === 0) {
            popupOverlay.setPosition(undefined);
            return;
        }

        var clusterFeature = selected[0];
        var rawFeatures = clusterFeature.get('features'); 

        if (rawFeatures.length > 1) {
            var extent = ol.extent.createEmpty();
            rawFeatures.forEach(function(f) {
                var props = f.getProperties();
                if(props.center_geom) {
                    ol.extent.extend(extent, new ol.geom.Point(props.center_geom.coordinates).getExtent());
                }
            });
            
            baseMap.getView().fit(extent, { 
                padding: [100, 100, 100, 100], 
                duration: 500,
                maxZoom: 15 
            });
            
            selectInteraction.getFeatures().clear();
            
        } else {
            var originalFeature = rawFeatures[0];
            showDogPopup(originalFeature);
            zoomToDogFeature(originalFeature);
        }
    });
    
    if (dogSource.getFeatures().length > 0) {
        baseMap.getView().fit(dogSource.getExtent(), { padding: [50,50,50,50], maxZoom: 12 });
    }
}