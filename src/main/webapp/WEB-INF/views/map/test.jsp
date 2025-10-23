<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8" />
  <title>baseMapSample</title>

  <!-- 정적 리소스: 컨텍스트 루트 포함해서 안전하게 로드 -->
  <script src="<c:url value='/js/jquery-3.1.1.min.js'/>"></script>
  <script src="<c:url value='/js/Openlayers3/ol.js'/>"></script>

  <!-- 폴더명: Openlayers3 (소문자 l) 주의 -->
  <script src="<c:url value='/js/Openlayers3/proj4.js'/>"></script>
  <script src="<c:url value='/js/Openlayers3/transCoord.js'/>"></script>

  <!-- 외부 API: 한 줄로(개행 넣지 말 것) -->
  <script src="http://www.khoa.go.kr/oceanmap/BASEMAP_RLTM3857	/otmsVectormapApi.do?ServiceKey=F062497BC44DD448FDAE699EE&version=2"></script>

  <!-- 너의 벡터 샘플 스크립트 -->
  <script src="<c:url value='/js/Openlayers3/apiVectorSample(3857).js'/>"></script>
</head>
<body>

  <div id="baseMap" class="baseMap"></div>
  <style>
  .map-toolbar {
    position: absolute; right: 12px; top: 12px; z-index: 1000;
    background: rgba(255,255,255,.92); padding: 8px 10px; border-radius: 10px;
    box-shadow: 0 4px 12px rgba(0,0,0,.12); font-size: 13px;
  }
  .map-toolbar label { display: inline-flex; align-items: center; gap: 6px; margin-right: 10px; }
</style>

<div id="baseMap" style="width:100%; height:600px; position:relative;"></div>
<div class="map-toolbar">
  <label><input type="checkbox" id="chkWms"> WMS</label>
  <label><input type="checkbox" id="chkWfs" checked> WFS</label>
</div>
</body>
</html>
