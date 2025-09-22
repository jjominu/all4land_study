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
	<form action="updateBoard.do">
			<th scope="row">id</th>
				 	<p>
			
			<input type="text" id ="boardId" name="boardId" value="${detail.boardId}" readonly>
			<p>
			<th scope="row">제목</th>
			<p>
			<input type="text" id ="title" name="title" value="${detail.title}">
			<p>
			<tr scope="row">내용</tr>
			<p>
			<textarea rows="10" cols="50" name="content" >${detail.content}</textarea>
			<button type="submit">수정</button>
	</form>
		<a href="list.do">돌아가기</a>
		
		
</body>
</html>
<script>
document.getElementById("boardId").value = ${detail.title};
</script>