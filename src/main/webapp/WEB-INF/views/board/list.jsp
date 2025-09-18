<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
   <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> <!-- c라는 거 쓸려면 이거 필수 ! -->>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<table border=1>
	<thead>
		<tr>
			<th>id</th>
			<th>제목</th>
			<th>조회수</th>
		</tr>
	</thead>
		
	<tbody>
		<c:forEach items="${list}" var="list">
			<tr>
				<td>${list.id}</td>
				<td><a href="getDetail.do?id=${list.id}">${list.title}</a></td>
				<td>${list.view}</td>
				
			</tr>
		</c:forEach>
		
	</tbody>
</table>
<a href="callBoardWrite.do">작성</a>
</body>
</html>