<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!doctype html>
<html lang="ko">
<head>
  <meta charset="utf-8" />
  <title>OpenLayers + 개방海(해양 WMS) 데모</title>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/ol/ol.css">
  <style>
    html, body, #map { margin:0; padding:0; width:100%; height:100%; }
    .ol-attribution, .ol-zoom { font-size: 12px; }
  </style>
</head>
<body>
  <div id="map"></div>

  <script src="https://cdn.jsdelivr.net/npm/ol/ol.js"></script>
  <script>
  const vworldKey = '${vworldKey}';
  const safeMapApiKey = '${safeMapKey}';
    // 1) VWorld WMTS 배경지도 (EPSG:3857)
    // WMTS 스펙: https://www.vworld.kr/dev/v4dv_wmtsguide_s001.do
    const vworldKey = 'YOUR_VWORLD_KEY'; // ← 발급키로 교체
    // 레벨 0~18 정도 지원. OpenLayers는 WMTS tileGrid가 필요.
    // 간단히 XYZ 템플릿을 써도 되지만, WMTS 정식 작성:
    // 다만 VWorld는 XYZ Template도 지원돼서 아래처럼 간단 버전을 사용.
    const vworldBase = new ol.layer.Tile({
      title: 'VWorld Base',
      source: new ol.source.XYZ({
        url: `https://api.vworld.kr/req/wmts/1.0.0/${vworldKey}/Base/{z}/{y}/{x}.png`
      })
    });

    // 2) 해안침수예상도 WMS 오버레이 (KHOA 원자료 기반, 재난안전포털 SafeMap)
    // 안내: https://safemap.go.kr/opna/data/dataView.do?objtId=211
    // WMS 호출 엔드포인트: safemap.go.kr/openApiService/wms/getLayerData.do
    const safeMapApiKey = 'YOUR_SAFEMAP_API_KEY'; // 선택 (일부 레이어는 키 필요)
    const safeMapWmsUrl = 'https://safemap.go.kr/openApiService/wms/getLayerData.do';

    const coastFloodLayer = new ol.layer.Tile({
      title: '해안침수예상도(2021~)',
      visible: true,
      source: new ol.source.TileWMS({
        url: safeMapWmsUrl,
        params: {
          SERVICE: 'WMS',
          VERSION: '1.3.0',
          REQUEST: 'GetMap',
          FORMAT: 'image/png',
          TRANSPARENT: true,
          LAYERS: 'fludexpect22',   // 문서의 예시 레이어명(페이지에서 확인)
          CRSS: 'EPSG:3857',        // 일부 서버는 SRS/CRS 파라미터 요구 방식이 다름
          // API 키 필요 시:
          API_KEY: safeMapApiKey
        },
        crossOrigin: 'anonymous'
      })
    });

    // 3) 지도 초기화
    const map = new ol.Map({
      target: 'map',
      layers: [vworldBase, coastFloodLayer],
      view: new ol.View({
        center: ol.proj.fromLonLat([126.9780, 37.5665]), // 서울
        zoom: 7
      }),
      controls: ol.control.defaults().extend([
        new ol.control.ScaleLine(),
        new ol.control.Attribution()
      ])
    });

    // 4) 클릭 시 WMS GetFeatureInfo 예시 (속성 조회)
    map.on('singleclick', function (evt) {
      const viewResolution = map.getView().getResolution();
      const source = coastFloodLayer.getSource();
      const url = source.getFeatureInfoUrl(
        evt.coordinate,
        viewResolution,
        'EPSG:3857',
        { 'INFO_FORMAT': 'application/json' } // 서버가 지원하는 포맷 확인
      );
      if (url) {
        fetch(url).then(r => r.json()).then(json => {
          console.log('GetFeatureInfo:', json);
          alert('피처 정보는 콘솔을 확인하세요.');
        }).catch(() => alert('GetFeatureInfo 요청 실패'));
      }
    });
  </script>
</body>
</html>
