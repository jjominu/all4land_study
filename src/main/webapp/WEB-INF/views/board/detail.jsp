<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
       <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> <!-- c라는 거 쓸려면 이거 필수 ! -->
       <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
       <% pageContext.setAttribute("replaceChar","\n"); %> <!-- "\n" 이라는 객체 생성 -->
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판 상세</title>
</head>
<%@ include file="../include/style.jsp"%>
<jsp:include page="../include/header.jsp"></jsp:include>
<jsp:include page="../include/footer.jsp"></jsp:include>
<body>

 <h2>상세페이지</h2>
 

    <table border="1">
        <tr>
            <td>제목</td>
            <td>${detail.title}</td>
        </tr>
       
        <tr>
            <td>내용</td>
            <td>${fn:replace(detail.content,replaceChar,"<br/>")}</td> <!-- <br/>을 <\n>올 변환 -->
        </tr>
       
        <tr>
            <td>조회수</td>
            <td>${detail.view}</td>
        </tr>
         <tr>
            <td>사진</td>
          <c:if test="${empty result}">
	          <td>
	          	사진이 없습니다.
	          </td>
          </c:if>
            <c:if test="${not empty result}">
	            <td>
	             <c:forEach items="${result}" var="result">
				   
				        <img src='${result}'style="max-width:20%; min-height:20px "border=2>
				   
    			</c:forEach>
	            	
	            </td>
            </c:if>
            
        </tr>
        
        
    </table>
     
    		
    		<button onclick="window.history.back();">돌아가기</button>
    		<form action="delete.do?boardId=${detail.boardId}" method="post">
		   		<input type="hidden" name="boardId" value="${detail.boardId}">
		    <button type="submit" class="link-button">삭제하기</button>
		    </form>
		    
    	 	<form action="callBoardUpdate.do" method="post">
		   		<input type="hidden" name="boardId" value="${detail.boardId}">
		    <button type="submit" class="link-button">수정하기</button>
</form>
</body>
<script>

</script>
</html>