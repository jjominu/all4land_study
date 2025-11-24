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
// ★ [추가] 클러스터(뭉침) 스타일 캐시
var styleCache = {};

// ★ [핵심] 클러스터 스타일 함수
function clusterStyleFunction(feature) {
    var size = feature.get('features').length; // 뭉친 개수

    // 1. 개별 아이템 (1개만 있을 때) -> 폴리곤 + 핀 보여주기
    if (size === 1) {
        var originalFeature = feature.get('features')[0];
        var props = originalFeature.getProperties();
        var styles = [];

        // (1) 폴리곤 그리기
        styles.push(new ol.style.Style({
            stroke: new ol.style.Stroke({ color: 'rgba(13, 110, 253, 0.6)', width: 2 }),
            fill: new ol.style.Fill({ color: 'rgba(13, 110, 253, 0.1)' }),
            geometry: originalFeature.getGeometry() // 원래의 MultiPolygon 사용
        }));

        // (2) 중심점 핀 그리기 (DB 좌표 사용)
        if (props.center_geom && props.center_geom.coordinates) {
            styles.push(new ol.style.Style({
                image: baseMarkerImage,
                geometry: new ol.geom.Point(props.center_geom.coordinates)
            }));
        }

        return styles;
    }

    // 2. 클러스터 (2개 이상 뭉쳤을 때) -> 원 + 숫자
    var style = styleCache[size];
    if (!style) {
        style = new ol.style.Style({
            image: new ol.style.Circle({
                radius: 15, // 원 크기
                stroke: new ol.style.Stroke({ color: '#fff', width: 2 }),
                fill: new ol.style.Fill({ color: '#0d6efd' }) // 파란색
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
// [평상시 스타일]
function dogStyleFunction(feature) {
    var geom = feature.getGeometry();
    var type = geom.getType();
    var styles = [];
    var props = feature.getProperties();

    // 1. 폴리곤 그리기
    if (type === 'Polygon' || type === 'MultiPolygon') {
        styles.push(polygonStyle); // 면 스타일

        // ★ [수정] 마커 위치 결정
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
// [선택시 스타일]
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


// ======================= 팝업 관련 기능 ==========================

// ======================= 팝업 표시 함수 (수정완료) ==========================
function showDogPopup(feature) {
    if (!feature || !popupContent) return;

    var props = feature.getProperties();
    var id = feature.getId(); // Feature ID

    // 1. 속성 값 가져오기 (값이 없을 경우를 대비한 기본값 처리)
    var park_nm = props.park_nm || '이름 없음';
    var addr    = props.addr    || '주소 없음';
    var oper_tm = props.oper_tm || '';
    var telno   = props.telno   || '-';
    var fcs     = props.fcs     || '-';
    var park_img = props.park_img; 

    // 2. 이미지 HTML 생성
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

    // 3. 팝업 내용 HTML 조립
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

    // 4. HTML 적용
    popupContent.innerHTML = html;

    // 5. 즐겨찾기 상태 확인 (비동기 호출)
    checkPopupBookmarkStatus(id);

    // 6. ★ [핵심 수정] 팝업 위치 좌표 설정 로직 ★
    var coord = null;

    // (1) DB에서 가져온 포인트 좌표가 있으면 최우선 사용
    if (props.center_geom && props.center_geom.coordinates) {
        coord = props.center_geom.coordinates;
    } 
    // (2) 없다면 도형(Polygon/MultiPolygon)의 물리적 중심점 계산
    else {
        var geom = feature.getGeometry();
        if (geom) {
            coord = ol.extent.getCenter(geom.getExtent());
        }
    }

    // 7. 좌표가 유효하면 팝업 위치 지정
    if (coord) {
        popupOverlay.setPosition(coord);
    } else {
        console.error("팝업 좌표를 결정할 수 없습니다. Feature:", feature);
    }
}
// [기능] 팝업 내 즐겨찾기 토글
function togglePopupBookmark(parkId) {
    // 1. ID 값 검증 및 정제 (숫자가 아닌 문자가 섞여있으면 제거)
    // 예: "dog_park.15" -> "15"
    if (!parkId) {
        alert("공원 정보를 찾을 수 없습니다.");
        return;
    }
    
    // 문자열로 변환 후 숫자만 남기기
    var cleanId = String(parkId).replace(/[^0-9]/g, ""); 
    
    if (cleanId === "") {
        console.error("유효하지 않은 ID입니다:", parkId);
        return;
    }

    $.ajax({
        url: "/test/api/bookmark/toggle.do",
        type: "POST",
        data: { parkId: cleanId }, // 정제된 숫자 ID 전송
        success: function(res) {
            var icon = $("#popup_bm_icon");
            if(res === 'inserted') {
                icon.removeClass('bi-bookmark-star').addClass('bi-bookmark-star-fill text-warning');
                // alert("즐겨찾기에 추가되었습니다."); // 너무 자주 뜨면 귀찮으니 생략 가능
            } else if (res === 'login_required') {
                alert("로그인이 필요한 서비스입니다.");
                // 필요시 로그인 페이지로 이동: location.href = "/test/home";
            } else {
                icon.removeClass('bi-bookmark-star-fill text-warning').addClass('bi-bookmark-star');
                // alert("즐겨찾기가 해제되었습니다.");
            }
        },
        error: function(err) {
            console.error("즐겨찾기 토글 에러:", err);
            // 400 에러가 또 나면 콘솔에서 확인 가능
        }
    });
}

// [기능] 팝업 열릴 때 상태 체크
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
            // res가 1이면 즐겨찾기 됨, 0이면 안됨
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
            // 로그인 안 한 상태에서는 400이 아니라 그냥 0을 리턴해야 함.
            // 만약 여기서 400이 뜬다면 Controller가 int 변환을 실패한 것.
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

// ======================= 초기화 및 이벤트 (DOM Ready) ==========================

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
// ★ [수정] 즐겨찾기 모아보기 버튼 클릭
    $("#btnFilterBookmark").on("click", function() {
        
        // 1. 서버에서 완성된 HTML 리스트 받아오기
        $.ajax({
            url: "/test/api/map/ajaxBookmarkList.do", // 컨트롤러 URL
            type: "POST",
            success: function(html) {
                // 2. 리스트 영역 덮어쓰기
                $("#dog-list").html(html);
                
                // 3. 지도 마커도 리스트에 있는 것만 남기기 (함수 정의 추가함)
            },
            error: function(xhr) {
                if(xhr.status === 400 || xhr.status === 500) {
                     alert("로그인이 필요한 서비스입니다.");
                } else {
                     alert("목록을 불러오는 중 오류가 발생했습니다.");
                }
            }
        });
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

   // 1. 원본 데이터 소스 (Vector)
    var dogFeatures = new ol.format.GeoJSON().readFeatures(dogWfsJson, {
        dataProjection: 'EPSG:3857',
        featureProjection: 'EPSG:3857'
    });

    dogFeatures.forEach(function (f) {
        var attrId = f.get("id");
        if (attrId) f.setId(String(attrId));
    });

    dogSource = new ol.source.Vector({ features: dogFeatures });

    // ★ [핵심] 2. 클러스터 소스 생성 (DB의 center_geom 이용!)
    var clusterSource = new ol.source.Cluster({
        distance: 40, // 뭉치는 거리 (픽셀 단위)
        source: dogSource,
        // ★ 중요: 폴리곤 대신 DB에서 가져온 중심점(center_geom)을 기준으로 클러스터링함
        geometryFunction: function(feature) {
            var props = feature.getProperties();
            if (props.center_geom && props.center_geom.coordinates) {
                return new ol.geom.Point(props.center_geom.coordinates);
            }
            return null; // 좌표 없으면 무시
        }
    });

    // 3. 레이어 생성 (Cluster Source 연결)
    gsDog = new ol.layer.Vector({
        visible: USE_DOG,
        source: clusterSource, // clusterSource 사용
        style: clusterStyleFunction // 위에서 만든 스타일 함수
    });
    baseMap.addLayer(gsDog);

    // 4. 인터랙션 (클릭 이벤트)
    var selectInteraction = new ol.interaction.Select({
        // 스타일은 선택 시 빨간색 등으로 바꾸고 싶다면 별도 함수 필요 (여기선 기본 처리)
    });
    baseMap.addInteraction(selectInteraction);

    selectInteraction.on('select', function (e) {
        var selected = e.selected;
        if (selected.length === 0) {
            popupOverlay.setPosition(undefined);
            return;
        }

        // 선택된 피처는 'Cluster' 피처임 (내부에 원본들이 들어있음)
        var clusterFeature = selected[0];
        var rawFeatures = clusterFeature.get('features'); // 묶인 개수 확인

        if (rawFeatures.length > 1) {
            // A. 여러 개 뭉친 걸 클릭함 -> 펼쳐지게 줌인(Zoom In)
            var extent = ol.extent.createEmpty();
            rawFeatures.forEach(function(f) {
                // 각 피처의 중심점을 기준으로 영역 계산
                var props = f.getProperties();
                if(props.center_geom) {
                    ol.extent.extend(extent, new ol.geom.Point(props.center_geom.coordinates).getExtent());
                }
            });
            
            // 해당 영역으로 부드럽게 이동 (여백 좀 주고)
            baseMap.getView().fit(extent, { 
                padding: [100, 100, 100, 100], 
                duration: 500,
                maxZoom: 15 
            });
            
            // 선택 해제 (팝업 안 뜨게)
            selectInteraction.getFeatures().clear();
            
        } else {
            // B. 딱 1개 남은 걸 클릭함 -> 팝업 표시
            var originalFeature = rawFeatures[0];
            showDogPopup(originalFeature);
            zoomToDogFeature(originalFeature);
        }
    });
    
    // 초기 줌
    if (dogSource.getFeatures().length > 0) {
        baseMap.getView().fit(dogSource.getExtent(), { padding: [50,50,50,50], maxZoom: 12 });
    }
}