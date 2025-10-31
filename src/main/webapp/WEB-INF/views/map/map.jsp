<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
  <%@ include file="../include/style.jsp"%>

<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"> 
    <title>지도</title>  

	<script type="text/javascript" src="https://www.khoa.go.kr/oceanmap/BASEMAP_RLTM3857/otmsVectormapApi.do?ServiceKey=F062497BC44DD448FDAE699EE
&version=2"></script>
<jsp:include page="../include/header.jsp"></jsp:include>
<jsp:include page="../include/footer.jsp"></jsp:include>
	
	<script src="<c:url value='/js/jquery-3.1.1.min.js'/>"></script>
	<script src="<c:url value='/js/Openlayers3/ol.js'/>"></script>
	<script src="<c:url value='/js/Openlayers3/proj4.js'/>"></script>
	<script src="<c:url value='/js/Openlayers3/transCoord.js'/>"></script>
	<script src="<c:url value='/js/Openlayers3/study.js'/>"></script>
	
		
  </head>
<body>
	<div id="baseMap" class="baseMap"style="width:100%; height:700px;"></div>	
	<div class="map-toolbar">
  <label><input type="checkbox" id="chkWms"></input> WMS</label>
  <label><input type="checkbox" id="chkWfs" checked></input>  WFS</label>
  <label><input type="checkbox" id="chkDog" ></input>  DogWFS</label>
</div>
<div>
<label class="label">
    클릭 타입
    <select id="type">
      <option value="singleclick">SingleClick</option>
      <option value="pointermove">PointerMove</option>
    </select>
  </label>
</div>	

<p id="featInfo" class="text-start"></p>
</body>
</html>
