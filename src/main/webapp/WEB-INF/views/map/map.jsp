<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8" />
  <title>baseMapSample</title>

  <script src="<c:url value='/js/jquery-3.1.1.min.js'/>"></script>
    <script src="<c:url value='/js/Openlayers3/proj4.js'/>"></script>
  
  <script src="<c:url value='/js/Openlayers3/ol.js'/>"></script>
  <script src="<c:url value='/js/Openlayers3/transCoord.js'/>"></script>

  <script src="http://www.khoa.go.kr/oceanmap/BASEMAP_RLTMCOAST3857/otmsVectormapApi.do?ServiceKey=F062497BC44DD448FDAE699EE&version=2"></script>

  <script src="<c:url value='/js/Openlayers3/study.js'/>"></script>

  <style>
    html, body {
      margin: 0;
      padding: 0;
      width: 100%;
      height: 100%;
    }
    #baseMap {
      width: 100%;
      height: 600px;
    }
  </style>
</head>

<body>
  <div id="baseMap" class="baseMap"></div>
</body>
</html>
