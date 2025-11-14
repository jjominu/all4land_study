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
	
		
  <style>
/* 화면 전체 레이아웃 */
#map-wrapper {
    display: flex;
    width: 100%;
    height: calc(100vh - 60px);   /* 상단 header 공간 제외한 높이 */
    margin-top: 60px;             /* header 실제 높이만큼 밀기 */
    overflow: hidden;
}

/* 왼쪽 패널 */
#left-panel {
    width: 350px;
    background: #fff;
    border-right: 1px solid #ddd;
    overflow-y: auto;
    padding: 15px;
}

/* 오른쪽 지도 영역 */
#right-map-area {
    flex: 1;
    position: relative;
}

#baseMap {
    width: 100%;
    height: 100%;
}

/* 레이어 스위치 UI */
#layer-switcher {
    position: absolute;
    top: 15px;
    right: 15px;
    background: white;
    padding: 8px;
    border-radius: 6px;
    box-shadow: 0 2px 8px rgba(0,0,0,0.25);
}

.layer-btn {
    display: block;
    padding: 6px 10px;
    margin-bottom: 5px;
    border: 1px solid #ccc;
    background: white;
    cursor: pointer;
    border-radius: 4px;
}
.layer-btn:hover {
    background: #f2f2f2;
}

/* 팝업 스타일 */
.ol-popup {
  position: absolute;
  background-color: white;
  padding: 10px;
  border-radius: 4px;
  border: 1px solid #cccccc;
  bottom: 12px;
  left: -50px;
  min-width: 200px;
}
</style>

</head>

<body>

<!-- ⭐ 여기부터 레이아웃 시작 -->
<div id="map-wrapper">

    <!-- 왼쪽 목록 -->
    <div id="left-panel">
        <h4>반려견 놀이터 목록</h4>
        <div id="dog-list"></div>
    </div>

    <!-- 오른쪽 지도 -->
    <div id="right-map-area">
        <div id="baseMap"></div>
<div id="layer-switcher">
    <label class="chk-label">
        <input type="checkbox" id="chkDog">
        🐶 반려견 놀이터
    </label>
</div>
</div>

        <div id="popup" class="ol-popup">
            <a href="#" id="popup-closer" class="ol-popup-closer"></a>
            <div id="popup-content"></div>
        </div>
    </div>

</div>
<!-- ⭐ 여기까지 전체 지도 UI -->
</body>
</html>
