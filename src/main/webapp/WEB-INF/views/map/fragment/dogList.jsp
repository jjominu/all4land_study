<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>놀이터 리스트</title>
    
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@400;500;700&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">

  
</head>
<body>

    <c:if test="${not empty dogList}">
        <c:forEach var="dog" items="${dogList}">
            <div class="place-card" onclick="if(window.parent.focusDogOnMap) window.parent.focusDogOnMap('dog_park.${dog.id}'); else focusDogOnMap('dog_park.${dog.id}');">
                
                <div class="place-title">
                    <c:out value="${dog.parkNm}" />
                </div>

                <p class="place-addr text-truncate">
                    <i class="bi bi-geo-alt-fill text-danger me-1"></i>
                    <c:out value="${dog.addr}" />
                </p>
                

                <div>
                    <span class="info-badge">반려견 놀이터</span>
                    <c:if test="${not empty dog.operTm}">
                         <span class="info-badge"><c:out value="${dog.operTm}" /></span>
                    </c:if>
                </div>

                <button class="btn-focus" type="button" title="지도에서 위치 보기">
                    <i class="bi bi-arrow-right"></i>
                </button>
            </div>
        </c:forEach>
    </c:if>
 
    <c:if test="${empty dogList}">
        <div class="empty-state">
            <div class="mb-3">
                <i class="bi bi-exclamation-circle display-4" style="color: #dee2e6;"></i>
            </div>
            <h6 class="fw-bold text-secondary">검색 결과가 없습니다.</h6>
            <p class="small text-muted">검색 조건을 변경하거나<br>지도를 이동해 보세요.</p>
        </div>
    </c:if>

   

</body>
</html>