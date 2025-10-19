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


<div class="container">

		
	<div class="d-flex mb-3" role="search">
	  	<select class="form-select " name="searchType" style="max-width: 100px;">
		    <option value="title" <c:if test="${bsrVO.searchType=='title'}">selected</c:if>>제목</option>
		    <option value="content" <c:if test="${bsrVO.searchType=='content'}">selected</c:if>>내용</option>
		    <!-- <option value="title_text">제목+내용</option>
		    <option value="nick">작성자</option> -->
	 	 </select>
		 <input type="text" name="keyword" value="<c:out value='${bsrVO.keyword}' escapeXml='true'/>" class="form-control" placeholder="검색어 입력"/>
		 <button type="button" id="searchBtn" class="btn btn-primary"  style="min-width: 100px;">검색</button>
	</div>

	<c:if test="${empty list}"><p>데이터가 없어요.</p></c:if>
	<c:if test="${not empty list}">
		<table border=3 class="table" >
			<thead>
				<tr  style="text-align: center;">
				    <th >번호</th>
					<th >제목</th>
					<th>조회수</th>
					<th>생성일</th>
				</tr>
			</thead>
		<tbody>
	
	 
	 
		<c:forEach items="${list}" var="list"  varStatus="st">
			<tr>
			<td >
		      ${pageUtil.totalCount - (pageUtil.cri.pageStart + st.index)}
		    </td>
				<td>
					<form action="getDetail.do" method="post" class="d-inline">
			            <input type="hidden" name="boardId" value="${list.boardId}">
			            <button type="submit" class="btn">${list.title}</button>
		        	</form>
	        	</td>
				<td style="text-align: center;">${list.view}</td>
      			<td style="text-align: center;"><fmt:formatDate pattern="yyyy-MM-dd  HH시mm분" value="${list.createTimestamp}"/></td>
			</tr>
		</c:forEach>
		 
		
	</tbody>
	
</table>
</c:if>


<button type="button" onclick="location.href='callBoardWrite.do'" class="btn btn-primary">글 쓰기</button>

  <ul class="btn-group pagination">
  	<c:if test="${empty bsrVO }">
  <nav aria-label="Page navigation example">
  <ul class="pagination">
    <c:if test="${pageUtil.prev}">
	    <li>
	        <a class="page-link"href='<c:url value="/board/list.do?page=${pageUtil.startPage-1 }"/>'><i class="fa ">이전</i></a>
	    </li>
    </c:if>
    <c:forEach begin="${pageUtil.startPage }" end="${pageUtil.endPage }" var="pageNum">
	    <li>
	        <a class="page-link"href='<c:url value="/board/list.do?page=${pageNum }"/>'><i class="fa">${pageNum }</i></a>
	    </li>
    </c:forEach>
    <c:if test="${pageUtil.next && pageUtil.endPage >0 }">
	    <li>
	        <a class="page-link" href='<c:url value="/board/list.do?page=${pageUtil.endPage+1 }"/>'><i class="fa ">다음</i></a>
	    </li>
    </c:if>
    </ul>
        </nav>
    </c:if>

    
    
    <c:if test="${not empty bsrVO }">
    <nav aria-label="Page navigation example">
  	<ul class="pagination">
     <c:if test="${pageUtil.prev}">
     <li>
	        <a class="page-link" href='<c:url value="/board/search.do?searchType=${bsrVO.searchType}&keyword=${bsrVO.keyword}&page=${pageUtil.startPage-1 }"/>'><i class="fa ">이전</i></a>
	    </li>
    </c:if>
    <c:forEach begin="${pageUtil.startPage }" end="${pageUtil.endPage }" var="pageNum">
	    <li>
	        <a class="page-link" href='<c:url value="/board/search.do?searchType=${bsrVO.searchType}&keyword=${bsrVO.keyword}&page=${pageNum }"/>'><i class="fa">${pageNum }</i></a>
	    </li>
    </c:forEach>
    <c:if test="${pageUtil.next && pageUtil.endPage >0 }">
	    <li>
	        <a class="page-link" href='<c:url value="/board/search.do?searchType=${bsrVO.searchType}&keyword=${bsrVO.keyword}&page=${pageUtil.endPage+1 }"/>'><i class="fa ">다음</i></a>
	    </li>
    </c:if>
    </ul>
    </nav>
    </c:if>
</ul>
</div>
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