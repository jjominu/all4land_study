<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html" charset="UTF-8">
<title></title>
 <%@ include file="../include/style.jsp"%>
<jsp:include page="../include/header.jsp"></jsp:include>
<jsp:include page="../include/footer.jsp"></jsp:include>
</head>
<body>
<h1>Error Page by web.xml : 404</h1>
    <a class="btn" href="<c:url value='/board/list.do'/>">보드 홈으로</a>

</body>
</html>