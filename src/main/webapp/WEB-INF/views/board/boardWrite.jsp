<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판</title>
</head>
<body>
	<form action="insertBoard.do">
	<table>
		<colgroup>
			<col width="50%">
			<col width="*">
	</colgroup>
			<caption>게시글작성</caption>
			<tbody>
				<tr>
					<th scope="row">id</th>
					<td><input type="text" id ="TITLE" name="id"></td>
					<th scope="row">제목</th>
					<td><input type="text" id ="TITLE" name="title"></td>
			</tbody>
			<tbody>
				<tr>
					<td scope="row">내용
					 	<textarea rows="10" cols="50" name="content"></textarea>
					 </td>
				</tr>
			</tbody>
	</table>
		<button type="submit">작성</button>
	</form>
		<a href="list.do">돌아가기</a>
</body>
</html>