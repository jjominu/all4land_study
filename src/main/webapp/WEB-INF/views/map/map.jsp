<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ include file="../include/style.jsp"%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>반려견 놀이터 지도</title>

    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@400;500;700&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">

    <jsp:include page="../include/header.jsp"></jsp:include>

    <script src="<c:url value='/js/Openlayers3/ol.js'/>"></script>
    <script src="<c:url value='/js/Openlayers3/proj4.js'/>"></script>
    <script src="<c:url value='/js/Openlayers3/transCoord.js'/>"></script>
    <script src="<c:url value='/js/Openlayers3/study.js'/>"></script>

   
</head>

<body>

    <div id="map-wrapper">
        
        <div id="left-panel">
            <div class="search-container">
                <h5 class="fw-bold mb-3"><i class="bi bi-search me-2"></i>놀이터 검색</h5>
                <form id="parkSearchForm" method="post" action="<c:url value='/map/getParkByParkName.do'/>">
                    
                    <div class="row g-2 mb-2">
                        <div class="col-6">
                            <select class="form-select form-select-sm" id="sdNm" name="sdNm">
                                <option value="">시도 선택</option>
                                <option value="서울">서울특별시</option>
                                <option value="인천">인천광역시</option>
                                <option value="경기">경기도</option>
                            </select>
                        </div>
                        <div class="col-6">
                            <select class="form-select form-select-sm" id="sggNm" name="sggNm">
                                <option value="">시군구 선택</option>
                            </select>
                        </div>
                    </div>

                    <div class="row g-2 mb-2">
                        <div class="col-6">
    <input type="hidden" id="realOperTm" name="operTm" value="">
    
    <div class="input-group input-group-sm">
        <select class="form-select px-1 text-center" id="operTmStart" onchange="updateOperTm()">
            <option value="">시작</option>
            <c:forEach var="i" begin="0" end="24">
                <c:set var="timeStr" value="${i < 10 ? '0' : ''}${i}:00"/>
                <option value="${timeStr}">${timeStr}</option>
            </c:forEach>
        </select>
        <span class="input-group-text px-1">~</span>
        <select class="form-select px-1 text-center" id="operTmEnd" onchange="updateOperTm()">
            <option value="">종료</option>
            <c:forEach var="i" begin="0" end="24">
                <c:set var="timeStr" value="${i < 10 ? '0' : ''}${i}:00"/>
                <option value="${timeStr}">${timeStr}</option>
            </c:forEach>
        </select>
    </div>
</div>
                        <div class="col-6">
                            <select class="form-select form-select-sm" name="useAmt">
                                <option value="">요금 전체</option>
                                <option value="무료">무료</option>
                                <option value="유료">유료</option>
                            </select>
                        </div>
                    </div>

                    <div class="mb-2">
                        <input type="text" class="form-control form-control-sm" name="fcs" placeholder="시설 (예: 펜스, 음수대)">
                    </div>

                    <div class="input-group mb-3">
                        <input type="text" class="form-control" name="parkNm" placeholder="놀이터 이름 검색">
<div class="d-flex gap-2">
    <button type="button" id="btnReset" class="btn btn-outline-secondary" title="필터 초기화">
        <i class="bi bi-arrow-counterclockwise"></i> 초기화
    </button>
    
    <button type="submit" class="btn btn-primary flex-grow-1">
        <i class="bi bi-search"></i> 검색
    </button>
    
</div>                    </div>
                </form>

                <div class="d-flex justify-content-between align-items-center mt-3">
                    <span class="text-muted small">검색 결과 <strong>${dogList != null ? dogList.size() : 0}</strong>건</span> 
                    <button type="button" id="btnFilterBookmark" class="btn btn-warning text-white">
    <i class="bi bi-star-fill"></i> 즐겨찾기 모아보기
</button>
                    </div>
            </div>

            <div id="dog-list">
                <c:if test="${not empty dogList}">
                    <c:forEach var="dog" items="${dogList}">
                        <div class="place-card" onclick="focusDogOnMap('dog_park.${dog.id}')">
                            <div class="place-title"><c:out value="${dog.parkNm}" /></div>
                            <p class="place-addr text-truncate"><i class="bi bi-geo-alt-fill me-1 text-danger"></i><c:out value="${dog.addr}" /></p>
                            <div class="d-flex gap-2 mt-2">
                                <span class="badge bg-light text-dark border">반려견</span>
                                <c:if test="${not empty dog.operTm}">
                                    <span class="badge bg-light text-dark border text-truncate" style="max-width: 100px;"><c:out value="${dog.operTm}"/></span>
                                </c:if>
                            </div>
                            <button class="btn-focus" title="지도에서 보기">
                                <i class="bi bi-arrow-right"></i>
                            </button>
                        </div>
                    </c:forEach>
                </c:if>
                <c:if test="${empty dogList}">
                    <div class="text-center py-5 text-muted">
                        <i class="bi bi-info-circle display-4"></i>
                        <p class="mt-3">검색 결과가 없습니다.</p>
                    </div>
                </c:if>
            </div>
        </div>

        <div id="right-map-area">
            <div id="baseMap"></div>

            <div id="layer-switcher">
                <div class="d-flex align-items-center justify-content-between mb-2">
                    <label class="form-check-label fw-bold small" for="chkDog">
                        <i class="bi bi-geo-alt text-primary"></i> 반려견 놀이터
                    </label>
                    <div class="form-check form-switch">
                        <input class="form-check-input" type="checkbox" id="chkDog" checked>
                    </div>
                </div>

                <hr class="my-2 text-muted"/>

                <div class="btn-group w-100" role="group">
                    <button type="button" class="btn btn-outline-secondary btn-sm layer-btn active" data-type="base">기본</button>
                    <button type="button" class="btn btn-outline-secondary btn-sm layer-btn" data-type="sat">위성</button>
                </div>
            </div>

            <div id="popup" class="ol-popup">
                <a href="#" id="popup-closer" class="ol-popup-closer"></a>
                <div id="popup-content"></div>
            </div>
        </div>

    </div>

<jsp:include page="../include/footer.jsp"></jsp:include>
<script>
    function updateOperTm() {
        var start = document.getElementById('operTmStart').value;
        var end = document.getElementById('operTmEnd').value;
        var realInput = document.getElementById('realOperTm');

        if (start && end) {
            realInput.value = start + " ~ " + end; 
        } else {
            realInput.value = "";
        }
    }

    document.getElementById('btnReset').addEventListener('click', function() {
        document.getElementById('parkSearchForm').reset();
        document.getElementById('operTmStart').value = "";
        document.getElementById('operTmEnd').value = "";
        document.getElementById('realOperTm').value = "";
    });
</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>