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
<div class="container">
        <div class="card-body">
            <table class="table table-bordered">
                <tbody>
                    <tr>
                        <th style="width: 15%">제목</th>
                        <td>${detail.title}</td>
                    </tr>
                    <tr>
                        <th>내용</th>
                        <td>${fn:replace(detail.content,replaceChar,"<br/>")}</td>
                    </tr>
                    <tr>
                        <th>조회수</th>
                        <td>${detail.view}</td>
                    </tr>
                    <tr>
                        <th>첨부파일</th>
                        <td>
                            <c:if test="${empty result}">
                                <span>첨부파일이 없습니다.</span>
                            </c:if>
                            <c:if test="${not empty result}">
                                <c:forEach items="${result}" var="result">
                                    <img src='${result}' class="img-thumbnail" style="max-width: 20%; min-height: 20px;">
                                </c:forEach>
                            </c:if>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>


 
    <div class="mt-3">
        <button onclick="window.history.back();" class="btn btn">돌아가기</button>
        
        <form action="delete.do?boardId=${detail.boardId}" method="post" class="d-inline">
            <input type="hidden" name="boardId" value="${detail.boardId}">
            <button type="submit" class="btn">삭제하기</button>
        </form>
        
        <form action="callBoardUpdate.do" method="post" class="d-inline">
            <input type="hidden" name="boardId" value="${detail.boardId}">
            <button type="submit" class="btn">수정하기</button>
        </form>
    </div>

</div>

</body>
<script>

</script>
</html>