<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8" />
  <title>공공문화체육시설 지도</title>

  <script src="<c:url value='/js/jquery-3.1.1.min.js'/>"></script>
    <script src="<c:url value='/js/Openlayers3/proj4.js'/>"></script>
  
  <script src="<c:url value='/js/Openlayers3/ol.js'/>"></script>
  <script src="<c:url value='/js/Openlayers3/transCoord.js'/>"></script>

  <script src="https://www.khoa.go.kr/oceanmap/BASEMAP_RLTM3857/otmsVectormapApi.do?ServiceKey=F062497BC44DD448FDAE699EE&version=2"></script>

  <script src="<c:url value='/js/Openlayers3/apiVectorSample(3857).js'/>"></script>

</head>

 <style>
  .map-toolbar {
    position: absolute; right: 12px; top: 12px; z-index: 1000;
   
  }
  .map-toolbar label { display: inline-flex; align-items: center; gap: 6px; margin-right: 10px; }
</style>
<body>


<div id="baseMap" style="width:100%; height:600px; position:relative;"></div>
<div class="map-toolbar">
  <label><input type="checkbox" id="chkWms"> WMS</label>
  <label><input type="checkbox" id="chkWfs" checked> WFS</label>
</div>
</body>
</html>
