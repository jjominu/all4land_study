// 1) 베이스맵(OSM)
const base = new ol.layer.Tile({ source: new ol.source.OSM() });

// 2) 맵 생성 (대한민국 중심 대략)
const map = new ol.Map({
  target: 'map',
  layers: [base],
  view: new ol.View({
    center: ol.proj.fromLonLat([127.8, 36.3]), // [lon, lat]
    zoom: 6
  })
});
