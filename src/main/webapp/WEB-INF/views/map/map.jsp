<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8"/>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/ol@latest/ol.css">
  <style>html,body,#map{height:100%;margin:0}</style>
</head>
<body>
<div id="map"></div>
<script src="https://cdn.jsdelivr.net/npm/ol@latest/dist/ol.js"></script>
<script>
  const VWORLD_KEY = 'CC1A65C4-B521-3FB2-9278-51A9F4519BFE';
  const vworld = new ol.layer.Tile({
    source: new ol.source.XYZ({
      url:'https://api.vworld.kr/req/wmts/1.0.0/' + VWORLD_KEY + '/Base/{z}/{y}/{x}.png',
      crossOrigin: 'anonymous'
    })
  });

  const gs = new ol.layer.Tile({
    source: new ol.source.TileWMS({
      url: 'http://localhost:9090/geoserver/wms',
      params: {
        LAYERS: 'map:places',   // ★ 여기!
        TILED: true,
        FORMAT: 'image/png',
        TRANSPARENT: true
      },
      serverType: 'geoserver',
      crossOrigin: 'anonymous'
    }),
    opacity: 0.95
  });

  const mapView = new ol.View({
    center: ol.proj.fromLonLat([126.9784, 37.5667]),
    zoom: 11
  });

  const mapOl = new ol.Map({
    target: 'map',
    layers: [vworld, gs],
    view: mapView
  });
</script>
</body>
</html>
