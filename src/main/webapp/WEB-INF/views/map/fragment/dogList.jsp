<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

<c:if test="${empty dogList}">
    <div class="alert alert-info">검색 결과가 없습니다.</div>
</c:if>

<c:forEach var="dog" items="${dogList}">
    <div class="card mb-3">
        <div class="card-body d-flex justify-content-between align-items-center">
            <div>
                <h6 class="card-title">${dog.parkNm}</h6>
                <p class="card-text">${dog.addr}</p>
            </div>
            <button class="btn" onclick="focusDogOnMap('dog_park.${dog.id}')">➜</button>
        </div>
    </div>
</c:forEach>

</body>
</html>