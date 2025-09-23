<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판</title>
</head>
<%@ include file="../include/style.jsp"%>
<jsp:include page="../include/header.jsp"></jsp:include>
<jsp:include page="../include/footer.jsp"></jsp:include>
<body>
	<form id="form1" action="upload.do" method="post" enctype="multipart/form-data" >
	 	<th scope="row">제목</th>
	 	<p>
	 	<input type="text" id ="TITLE" name="title" required>
	 	<p>
	 	<td scope="row">내용
	 	<p>
		<textarea rows="5" cols="50" name="content" required></textarea>
		<p>
		<input type="file" name="file">
		<p>
		<input type="submit" value="작성하기">
	</form>
	<p>
		<a href="list.do">돌아가기</a>
 </body>
</html>