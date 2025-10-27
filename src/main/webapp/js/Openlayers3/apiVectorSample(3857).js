var GEOSERVER_BASE = "http://localhost:9090/geoserver";  // 예: http://localhost:8080/geoserver
var WORKSPACE      = "vworld";                            // 워크스페이스
var LAYER_NAME     = "C_UQ155";                           // 레이어명
var FULL_LAYER     = WORKSPACE + ":" + LAYER_NAME;

var USE_WMS = true;   // 초기 표시 여부
var USE_WFS = false;  // 초기 표시 여부

var baseMap = null;
var gsWms   = null;
var gsVector= null;

(function () {
  ("#chkWms").prop("checked", USE_WMS);
  ("#chkWfs").prop("checked", USE_WFS);

  ("#chkWms").on("change", function () {
    if (gsWms) gsWms.setVisible(this.checked);
  });
  ("#chkWfs").on("change", function () {
    if (gsVector) gsVector.setVisible(this.checked);
  });

  initMap();
});

function initMap() {
	proj4.defs("EPSG:3857", "+proj=merc +lon_0=0 +k=1 +x_0=0 +y_0=0 +datum=WGS84 +units=m +no_defs");
	ol.proj.proj4.register(proj4);
	var proj3857 = ol.proj.get('EPSG:3857');
  var tileExtent= [-20037508.342789244, -20037508.342789255, 20037508.342789244, 20037508.342789244];

  var baseLayer;
  if (typeof window._vectorMapUrl === "string" && window._vectorMapUrl.indexOf("http") === 0) {
    baseLayer = new ol.layer.Tile({
      source: new ol.source.TileWMS({
        url: window._vectorMapUrl,
        params: { FORMAT: "image/png", TRANSPARENT: true },
        projection: "EPSG:3857",
        crossOrigin: "anonymous"
      })
    });
  } else {
    baseLayer = new ol.layer.Tile({ source: new ol.source.OSM() });
  }

  baseMap = new ol.Map({
    target: "baseMap",
    layers: [ baseLayer ],
    view: new ol.View({
      projection: proj3857,
      extent: tileExtent,
      center: [14177553.107181, 4308348.8448386], 
      zoom: 1,
      minZoom: 0,
      maxZoom: 10
    }),
    controls: ol.control.defaults()
  });

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
	  visible: USE_WFS,
	  source: new ol.source.Vector({
	    format: new ol.format.GeoJSON(),
	    url: GEOSERVER_BASE + "/" + WORKSPACE + "/ows" +
	         "?service=WFS&version=2.0.0&request=GetFeature" +
	         "&typename=" + FULL_LAYER +
	         "&outputFormat=application/json",
	  })
	});
	baseMap.addLayer(gsVector);

}
