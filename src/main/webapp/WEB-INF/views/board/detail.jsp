<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
       <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> <!-- c라는 거 쓸려면 이거 필수 ! -->
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<%@ include file="../include/style.jsp"%>
<jsp:include page="../include/header.jsp"></jsp:include>
<jsp:include page="../include/footer.jsp"></jsp:include>
<body>

 <h2>상세페이지</h2>
    <table border="1">
        <tr>
            <td>id</td>
            <td>${detail.boardId}</td>
        </tr>
        <tr>
            <td>제목</td>
            <td>${detail.title}</td>
        </tr>
       
        <tr>
            <td>내용</td>
            <td>${detail.content}</td>
        </tr>
       
        <tr>
            <td>조회수</td>
            <td>${detail.view}</td>
        </tr>
         <tr>
            <td>사진</td>
          <c:if test="${empty detail.imgName}">
	          <td>
	          	사진이 없습니다.
	          </td>
          </c:if>
            <c:if test="${not empty detail.imgName}">
	            <td>
	            	<img src='${detail.imgName}'style="max-width:50%; min-height:50px">
	            </td>
            </c:if>
            
        </tr>
        
        
    </table>
     
    		<a href="list.do">돌아가기</a>
    		<a href="delete.do?boardId=${detail.boardId}">삭제하기</a>
    		<a href="callBoardUpdate.do?boardId=${detail.boardId}">수정하기</a>
</body>
<script>

</script>
</html>