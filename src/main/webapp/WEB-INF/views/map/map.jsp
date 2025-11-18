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

<div id="map-wrapper">
<!--리스트-->
<div id="left-panel">

<div class="card p-3 mb-3">
    <form id="parkSearchForm" method="post">

        <div class="mb-2">
            <label class="form-label">놀이터 이름 검색</label>
            <input type="text"
                   class="form-control"
                   name="parkNm"
                   placeholder="예) 어린이대공원"
                   value="${parkNm}">
        </div>

        <button type="submit" class="btn btn-primary w-100">검색</button>
    </form>
</div>	


    <h4>반려견 놀이터 목록</h4>
    <div id="dog-list">
        <c:if test="${not empty dogList}">
            <c:forEach var="dog" items="${dogList}">
                <div class="card mb-3">
                    <div class="card-body">
                        <div>
                            <h6 class="card-title">
                                <c:out value="${dog.parkNm}" />
                            </h6>
                            <p class="card-text">
                                <c:out value="${dog.addr}" />
                            </p>
                        </div>
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

<div id="right-map-area">
    <div id="baseMap"></div>

    <div id="layer-switcher" class="card "
         style="position:absolute; top:15px; right:15px; z-index:999;">
        
        <!-- 체크박스: 반려견 놀이터 on/off -->
        <div class="form-check">
            <input class="form-check-input" type="checkbox" id="chkDog" checked>
            <label class="form-check-label" for="chkDog">
                🐶 반려견 놀이터
            </label>
        </div>

        <hr class="my-2"/>

        <div class="btn" role="group" aria-label="베이스맵 선택">
            <button type="button"
                     class="btn btn-sm  layer-btn "
                    data-type="base">
                기본 지도
            </button>
            <button type="button"
                     class="btn btn-sm  layer-btn"
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
</body>
</html>
