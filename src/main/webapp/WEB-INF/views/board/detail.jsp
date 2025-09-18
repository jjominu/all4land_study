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

 
    
 <h2>Detail page</h2>
    <table border="1">
        <th> 정보 </th>
        <th> 데이터 </th>
        <tr>
            
            
            </td>
        </tr>
        <tr>
            <td>id</td><td>${detail.id}</td>
        </tr>
        <tr>
            <td>제목</td><td>${detail.title}</td>
        </tr>
        <tr>
            <td>내용</td><td>${detail.content}</td>
        </tr>
       
        <tr>
            <td>조회수</td><td>${detail.view}</td>
        </tr>
    </table>
    		<a href="list.do">돌아가기</a>
    		<a href="delete.do?id=${detail.id}">삭제하기</a>
</body>
</html>