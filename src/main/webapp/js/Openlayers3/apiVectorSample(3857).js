// ====== 설정값 (환경에 맞게 수정) ======
var GEOSERVER_BASE = "http://localhost:9090/geoserver"; // 예: "http://localhost:8080/geoserver"
var WORKSPACE      = "vworld";                        // 워크스페이스
var LAYER_NAME     = "C_UQ155";                       // 레이어명
var FULL_LAYER     = WORKSPACE + ":" + LAYER_NAME;

var USE_WMS = true;   // 서버 SLD 기반 타일 겹침(빠름)
var USE_WFS = false;  // 클라이언트 벡터(스타일/인터랙션 자유, 데이터 적을 때 권장)

var baseMap, gsWms = null, gsVector = null;
$(document).ready(function () {
  initMap();
  // 토글 이벤트 연결
  $('#chkWms').on('change', function () {
    if (gsWms) gsWms.setVisible(this.checked);
    // 둘 중 하나만 보이게 하고 싶다면 아래 한 줄도 사용:
    // if (this.checked && gsVector) gsVector.setVisible(false), $('#chkWfs').prop('checked', false);
  });
  $('#chkWfs').on('change', function () {
    if (gsVector) gsVector.setVisible(this.checked);
    // 단일 모드 원한다면:
    // if (this.checked && gsWms) gsWms.setVisible(false), $('#chkWms').prop('checked', false);
  });

  $('#chkWms').prop('checked', USE_WMS);
  $('#chkWfs').prop('checked', USE_WFS);
});

function initMap() {
  // --- 좌표계 등록(필요 시) ---
  if (typeof proj4 !== "undefined") {
    proj4.defs("EPSG:3857", "+proj=merc +lon_0=0 +k=1 +x_0=0 +y_0=0 +datum=WGS84 +units=m +no_defs");
    if (ol?.proj?.proj4?.register) ol.proj.proj4.register(proj4);
  }

  var proj3857 = ol.proj.get("EPSG:3857");
  var tileExtent = [-20037508.342789244, -20037508.342789255, 20037508.342789244, 20037508.342789244];

  baseMap = new ol.Map({
    target: "baseMap",
    layers: [
      // 베이스맵: _vectorMapUrl 없으면 OSM 폴백
      (typeof _vectorMapUrl === "string" && _vectorMapUrl.startsWith("http"))
        ? new ol.layer.Tile({
            source: new ol.source.TileWMS({
              url: _vectorMapUrl,
              params: { FORMAT: "image/png" },
              projection: "EPSG:3857",
              crossOrigin: "anonymous"
            })
          })
        : new ol.layer.Tile({ source: new ol.source.OSM() })
    ],
    view: new ol.View({
      projection: proj3857,
      extent: tileExtent,
      center: [14177553.107181, 4308348.8448386],
      zoom: 7, minZoom: 0, maxZoom: 19
    }),
    controls: ol.control.defaults({ attributionOptions: { collapsible: false } })
  });

  // ===== 1) WMS 레이어 생성(초기 visible은 USE_WMS) =====
  gsWms = new ol.layer.Tile({
    zIndex: 10,
    visible: USE_WMS,
    source: new ol.source.TileWMS({
      url: GEOSERVER_BASE + "/" + WORKSPACE + "/wms",
      params: {
        SERVICE: "WMS",
        VERSION: "1.1.1",
        REQUEST: "GetMap",
        LAYERS: FULL_LAYER,
        STYLES: "",
        FORMAT: "image/png",
        TILED: true,
        TRANSPARENT: true,
        SRS: "EPSG:3857"
      },
      serverType: "geoserver",
      crossOrigin: "anonymous"
    })
  });
  baseMap.addLayer(gsWms);

  gsVector = new ol.layer.Vector({
    zIndex: 11,
    visible: USE_WFS,
    source: new ol.source.Vector({
      format: new ol.format.GeoJSON(),
      url: function (extent) {
        return GEOSERVER_BASE + "/" + WORKSPACE + "/ows"
          + "?service=WFS&version=2.0.0&request=GetFeature"
          + "&typename=" + encodeURIComponent(FULL_LAYER)
          + "&outputFormat=application/json"
          + "&srsName=EPSG:3857"
          + "&bbox=" + extent.join(",") + ",EPSG:3857";
      },
      strategy: ol.loadingstrategy.bbox
    }),
    style: new ol.style.Style({
      image: new ol.style.Circle({
        radius: 5,
        fill: new ol.style.Fill({ color: "rgba(0,0,0,0.4)" }),
        stroke: new ol.style.Stroke({ color: "#ffffff", width: 1 })
      }),
      stroke: new ol.style.Stroke({ color: "#ff6600", width: 2 }),
      fill: new ol.style.Fill({ color: "rgba(255,165,0,0.25)" })
    })
  });
  baseMap.addLayer(gsVector);

  // (옵션) 클릭으로 속성 보기: 현재 보이는 레이어만 처리
  baseMap.on("singleclick", function (evt) {
    if (gsVector?.getVisible()) {
      // 벡터(클라이언트) 픽킹
      var feature = baseMap.forEachFeatureAtPixel(evt.pixel, f => f);
      if (feature) {
        var props = feature.getProperties();
        delete props.geometry;
        alert(Object.keys(props).map(k => k + " : " + props[k]).join("\n"));
        return;
      }
    }
    if (gsWms?.getVisible()) {
      // WMS GFI
      var url = gsWms.getSource().getFeatureInfoUrl(
        evt.coordinate,
        baseMap.getView().getResolution(),
        baseMap.getView().getProjection(),
        { INFO_FORMAT: "application/json", FEATURE_COUNT: 10, QUERY_LAYERS: FULL_LAYER }
      );
      if (!url) return;
      $.getJSON(url).done(function (json) {
        if (!json?.features?.length) return alert("선택된 피처가 없습니다.");
        var props = json.features[0].properties || {};
        alert(Object.keys(props).map(k => k + " : " + props[k]).join("\n"));
      }).fail(function () {
        // CORS나 MIME 이슈일 때 눈으로 확인
        window.open(url.replace("INFO_FORMAT=application/json", "INFO_FORMAT=text/html"), "_blank");
      });
    }
  });
}