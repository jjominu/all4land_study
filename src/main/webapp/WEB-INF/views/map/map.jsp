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

<!-- ⭐ 여기부터 레이아웃 시작 -->
<div id="map-wrapper">
<!-- 왼쪽 목록 -->
<div id="left-panel">
    <h4>반려견 놀이터 목록</h4>

    <div id="dog-list">
    
        <c:if test="${not empty dogList}">
            <c:forEach var="dog" items="${dogList}">
                
                <div class="card mb-3">
                    <div class="card-body d-flex justify-content-between align-items-center">

                        <!-- 왼쪽 텍스트 -->
                        <div>
                            <h6 class="card-title">
                                <c:out value="${dog.parkNm}" />
                            </h6>
                            <p class="card-text">
                                <c:out value="${dog.addr}" />
                            </p>
                        </div>

                        <!-- 오른쪽 화살표 버튼 -->
                        <button class="btn"
                                onclick="focusDogOnMap('dog_park.${dog.id}')">
                            ➜
                        </button>

                    </div>
                </div>

            </c:forEach>
        </c:if>

      
   
</div>
</div>

    <!-- 오른쪽 지도 -->
<div id="right-map-area">
    <div id="baseMap"></div>

    <!-- ⭐ 레이어 스위처 카드 -->
    <div id="layer-switcher" class="card shadow-sm p-2"
         style="position:absolute; top:15px; right:15px; z-index:999;">
        
        <!-- 체크박스: 반려견 놀이터 on/off -->
        <div class="form-check mb-2">
            <input class="form-check-input" type="checkbox" id="chkDog" checked>
            <label class="form-check-label" for="chkDog">
                🐶 반려견 놀이터
            </label>
        </div>

        <hr class="my-2"/>

        <!-- 베이스맵 선택 버튼 -->
        <div class="btn-group-vertical" role="group" aria-label="베이스맵 선택">
            <button type="button"
                    class="btn btn-sm  layer-btn mb-1"
                    data-type="base">
                기본 지도
            </button>
            <button type="button"
                    class="btn btn-sm  layer-btn mb-1"
                    data-type="sat">
                위성 지도
            </button>
        </div>
    </div>
</div>

        <div id="popup" class="ol-popup">
            <a href="#" id="popup-closer" class="ol-popup-closer" style="">X</a>
            <div id="popup-content"></div>
        </div>
    </div>

</div>
<!-- ⭐ 여기까지 전체 지도 UI -->
</body>
</html>
