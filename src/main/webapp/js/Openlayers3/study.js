var GEOSERVER_BASE = "http://localhost:9090/geoserver";  // 예: http://localhost:8080/geoserver
var WORKSPACE      = "vworld";                            // 워크스페이스
var LAYER_NAME     = "C_UQ155";                           // 레이어명
var FULL_LAYER     = WORKSPACE + ":" + LAYER_NAME;

var USE_WMS = true;   // 초기 표시 여부
var USE_WFS = false;  // 초기 표시 여부

var baseMap = null;
var gsWms   = null;
var gsVector= null;
$(document).ready(function () {
  // 체크박스 상태 초기화
  $("#chkWms").prop("checked", USE_WMS);
  $("#chkWfs").prop("checked", USE_WFS);

  // WMS 토글 이벤트
  $("#chkWms").on("change", function () {
    if (gsWms) gsWms.setVisible(this.checked);
  });

  // WFS 토글 이벤트
  $("#chkWfs").on("change", function () {
    if (gsVector) gsVector.setVisible(this.checked);
  });

  // 지도 초기화
  initMap();
});
function initMap(){
	//뷰(좌표 및 줌 설정)
	proj4.defs("EPSG:3857", "+proj=merc +lon_0=0 +k=1 +x_0=0 +y_0=0 +datum=WGS84 +units=m +no_defs");
	ol.proj.proj4.register(proj4);
	var proj3857 = ol.proj.get('EPSG:3857');
	var resolutions = [156543.03, 78271.52, 39135.76, 19567.88, 9783.94, 4891.96981025128125, 2445.98490512, 1222.99245256, 611.49622628, 305.74811314, 152.87405657, 76.43702828, 38.21851414, 19.10925707, 9.55462853, 4.77731426, 2.38865713, 1.19433, 0.5972, 0.298583];
	var tileExtent = [-20037508.3427892439067364, -20037508.3427892550826073, 20037508.3427892439067364, 20037508.3427892439067364];
	var initExtent = [18321.13581588259, 1424794.937360047, 1894734.6292558827, 2214452.282516047];
	var initBasemapType = "서비스명" 
	var minZoomLevel = 0;
	var maxZoomLevel = 10;
	var feature = null;
	var view =  new ol.View({
				projection: proj3857,
				extent: tileExtent,
				center: [14177553.107181, 4308348.8448386 ],
				zoom: 1,
				minZoom: minZoomLevel,
				maxZoom: maxZoomLevel,
				maxResolution: 1954.597389
	});

	//베이스맵 설정
	baseMap = new ol.Map({
		target: 'baseMap',
		layers: [			
			new ol.layer.Tile({
				division : 'TILE',
				layerName: 'BASEMAP',
				visible: true,
				
				source: new ol.source.TileWMS({
				matrixSet: 'EPSG:3857',
				projection: 'EPSG:3857',		
				hidpi: false,
				tileGrid: new ol.tilegrid.TileGrid({
						extent: tileExtent, 
						origin: [ tileExtent[0], tileExtent[1] ],
						resolutions: resolutions
					}),
				url:_vectorMapUrl,
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
	     url: function (extent) {
      var base = GEOSERVER_BASE + "/" + WORKSPACE + "/ows";
      var q =
        "?service=WFS" +
        "&version=2.0.0" +
        "&request=GetFeature" +
        // ⬇️ typename → typeNames
        "&typeNames=" + encodeURIComponent(FULL_LAYER) +
        "&outputFormat=application/json" +
        // ⬇️ 좌표계 명시
        "&srsName=EPSG:3857" +
        // ⬇️ 화면 범위만 요청
        "&bbox=" + extent.join(",") + ",EPSG:3857" 
        // (선택) 한번에 너무 많이 안 받도록
        ;
      return base + q;
    }, strategy: ol.loadingstrategy.bbox
	  })
	});
	baseMap.addLayer(gsVector);
}
//베이스맵 요청 시 사용
function fn_fillzero(n, digits) {
	var zero = '';
	n = n.toString();
	if (digits > n.length) {
		for (var i = 0; digits - n.length > i; i++) {
			zero += '0';
		}
	}
	return zero + n;
}


