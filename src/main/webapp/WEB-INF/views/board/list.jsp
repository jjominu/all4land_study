<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
   <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> <!-- c라는 거 쓸려면 이거 필수 ! -->
   <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
   
   
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판</title>
</head>
<body>
<%@ include file="../include/style.jsp"%>
<jsp:include page="../include/header.jsp"></jsp:include>
<jsp:include page="../include/footer.jsp"></jsp:include>
<div  >
		<select name="searchType" >
			<option value="title" <c:if test="${bsrVO.searchType=='title'}">selected</c:if>>제목</option>
			<option value="content" <c:if test="${bsrVO.searchType=='content'}">selected</c:if>>내용</option>
		<!--  	<option value="title_text" <c:if test="'}">selected</c:if>>제목+내용</option>
			<option value="nick" <c:if test="">selected</c:if>>작성자</option>-->
		</select>
		<input type="text" name="keyword" value="${bsrVO.keyword }"/>
		<button type="button" id="searchBtn">검색</button>
		
		
</div>
<div>
 <c:if test="${empty list}">
 <p>데이터카 없어요.</p>
	 </c:if>
 <c:if test="${not empty list}">
<table border=3 >
	<thead>
		<tr  style="text-align: center;">
			<th>제목</th>
			<th>조회수</th>
			<th>생성일</th>
		</tr>
	</thead>
		
	<tbody>
	
	 
	 
		<c:forEach items="${list}" var="list">
			<tr >
				<td><a href="getDetail.do?boardId=${list.boardId}">${list.title}</a></td>
				<td>${list.view}</td>
      			<td><fmt:formatDate pattern="yyyy-MM-dd  HH시mm분" value="${list.createTimestamp }"/></td>
			</tr>
		</c:forEach>
		 
		
	</tbody>
	
</table>
</c:if>
</div>

<a href="callBoardWrite.do">작성</a>

  <ul class="btn-group pagination">
  	<c:if test="${empty bsrVO }">
  
    <c:if test="${pageUtil.prev}">
	    <li>
	        <a href='<c:url value="/board/list.do?page=${pageUtil.startPage-1 }"/>'><i class="fa ">이전</i></a>
	    </li>
    </c:if>
    <c:forEach begin="${pageUtil.startPage }" end="${pageUtil.endPage }" var="pageNum">
	    <li>
	        <a href='<c:url value="/board/list.do?page=${pageNum }"/>'><i class="fa">${pageNum }</i></a>
	    </li>
    </c:forEach>
    <c:if test="${pageUtil.next && pageUtil.endPage >0 }">
	    <li>
	        <a href='<c:url value="/board/list.do?page=${pageUtil.endPage+1 }"/>'><i class="fa ">다음</i></a>
	    </li>
    </c:if>
    </c:if>
    
    
    
    <c:if test="${not empty bsrVO }">
     <c:if test="${pageUtil.prev}">
     <li>
	        <a href='<c:url value="/board/search.do?searchType=${bsrVO.searchType}&keyword=${bsrVO.keyword}&page=${pageUtil.startPage-1 }"/>'><i class="fa ">이전</i></a>
	    </li>
    </c:if>
    <c:forEach begin="${pageUtil.startPage }" end="${pageUtil.endPage }" var="pageNum">
	    <li>
	        <a href='<c:url value="/board/search.do?searchType=${bsrVO.searchType}&keyword=${bsrVO.keyword}&page=${pageNum }"/>'><i class="fa">${pageNum }</i></a>
	    </li>
    </c:forEach>
    <c:if test="${pageUtil.next && pageUtil.endPage >0 }">
	    <li>
	        <a href='<c:url value="/board/search.do?searchType=${bsrVO.searchType}&keyword=${bsrVO.keyword}&page=${pageUtil.endPage+1 }"/>'><i class="fa ">다음</i></a>
	    </li>
    </c:if>
    </c:if>
</ul>

</body>
<script>
	
		
		document.getElementById("searchBtn").onclick = function () {
		    let searchType = document.getElementsByName("searchType")[0].value;
		    let keyword = document.getElementsByName("keyword")[0].value;

		    let url = "/board-test/board/search.do?searchType=" + searchType + "&keyword=" + keyword ;
		    location.href = encodeURI(url);
		};
		
		
</script>


</html>