var USE_WMS = true;   // 초기 표시 여부
var USE_WFS = false;  // 초기 표시 여부

var baseMap = null;
var gsWms   = null;
var gsVector= null;

var container = document.getElementById('popup'); //팝업이 담길 컨테이너 요소
var content1 = document.getElementById('popup-content'); //팝업 내용 요소
$(document).ready(function () {
  $("#chkWms").prop("checked", USE_WMS);
  $("#chkWfs").prop("checked", USE_WFS);

  $("#chkWms").on("change", function () {
    gsWms.setVisible(this.checked);
  });

  $("#chkWfs").on("change", function () {
    gsVector.setVisible(this.checked);
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

	
	baseMap = new ol.Map({//베이스맵 설정
		target: 'baseMap',
		layers: [			
			new ol.layer.Tile({//레이어정의
				division : 'TILE', //이건 뭐지 공식문서 안 나옴
				layerName: 'BASEMAP',
				visible: true,
				
				source: new ol.source.TileWMS({//source 정의
					matrixSet: 'EPSG:3857',
					projection: 'EPSG:3857',		
					hidpi: false,
					tileGrid: new ol.tilegrid.TileGrid({//타일 grid정의
							extent: tileExtent, 
							origin: [ tileExtent[0], tileExtent[1] ],
							resolutions: resolutions
					}),
				url:_vectorMapUrl,
				serverType: "mapserver"
				})
			})
			
		],
		controls: ol.control.defaults({//Map 속성 중 하나 
						attributionOptions: ({
							collapsible: false //시작 시 속성을 축소할지 여부를 지정합니다. 기본값은 .입니다 true
						})
		}),
		view: view
	});
	 gsWms = new ol.layer.Tile({
    zIndex: 10,
    visible: USE_WMS,
    source: new ol.source.TileWMS({
      url: "http://localhost:9090/geoserver/vworld/wms",
      params: {
        SERVICE: "WMS",
        VERSION: "1.1.0",
        REQUEST: "GetMap",
        LAYERS: "C_UQ155",
        STYLES: "",
        FORMAT: "image/png",
        SRS: "EPSG:3857"
      },
      serverType: "geoserver",
      crossOrigin: "anonymous"
    })
  });
  baseMap.addLayer(gsWms);
  gsVectorSource = new ol.source.Vector({
	    format: new ol.format.GeoJSON(),
	     url: function (extent) {
			var a ="http://localhost:9090/geoserver/vworld/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=vworld%3AC_UQ155&outputFormat=application%2Fjson&srsname=EPSG:3857&"+
              'bbox=' + extent.join(',') + ',EPSG:3857';

      return a;
    }, strategy: ol.loadingstrategy.bbox
	  })
	  
	  
	  
	gsVector = new ol.layer.Vector({
	  visible: USE_WFS,
	  source: gsVectorSource,
	  style: new ol.style.Style({
          stroke: new ol.style.Stroke({
            color: 'rgba(64, 156, 255, 1.0)',
            width: 3
             }),
      fill: new ol.style.Fill({
     	 color: 'rgba(0, 0, 0, 0.001)'   
    })
        })
	});
	baseMap.addLayer(gsVector);
	var select = null;
 
    var selectSingleClick = new ol.interaction.Select({
        multi: true
    });
 
    
    var selectPointerMove = new ol.interaction.Select({
        condition: ol.events.condition.pointerMove,
        multi: true
    });
 
    
 
    var selectElement = document.getElementById('type');
 
    var changeInteraction = function() {
        if (select !== null) {
            baseMap.removeInteraction(select);
        }
        var value = selectElement.value;
        if (value == 'singleclick') {
            select = selectSingleClick;
        }  else if (value == 'pointermove') {
            select = selectPointerMove;
        } else {
            select = null;
        }
        if (select !== null) {
            baseMap.addInteraction(select);
            
			 
        }
    };
 
    selectElement.onchange = changeInteraction;
    changeInteraction();    
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


