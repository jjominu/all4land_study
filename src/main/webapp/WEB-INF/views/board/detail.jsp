<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%
  pageContext.setAttribute("replaceChar", "\n");
%>

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>게시판 상세</title>
  <%@ include file="../include/style.jsp"%>
</head>

<jsp:include page="../include/header.jsp"></jsp:include>
<jsp:include page="../include/footer.jsp"></jsp:include>

<body>
<div class="container">
  <div class="card-body">
    <table class="table table-bordered">
      <tbody>
        <tr>
          <th>제목</th>
          <td>${detail.title}</td>
        </tr>
        <tr>
          <th>내용</th>
          <td>${fn:replace(detail.content, replaceChar, "<br/>")}</td>
        </tr>
        <tr>
          <th>조회수</th>
          <td>${detail.view}</td>
        </tr>
        <tr>
          <th>첨부파일</th>
          <td>
            <c:if test="${empty result and empty files}">
              <span>첨부파일이 없습니다.</span>
            </c:if>

            <c:if test="${not empty result}">
              <div class="d-flex flex-wrap gap-2">
	                <c:forEach items="${result}" var="imgSrc">
		                <c:if test="${not empty imgSrc}">
		                  <img src='${imgSrc}' class="img-thumbnail bd-img" style="max-width:20%; min-height:20px;" alt="첨부 이미지 미리보기">
	                  </c:if>
	                </c:forEach>
              </div>
            </c:if>

            <c:if test="${not empty files}">
              <ul class="mt-2">
                <c:forEach items="${files}" var="f">
                  <li>
                    <a class="bd-download"
                       href="<c:url value='/board/download.do'><c:param name='fileId' value='${f.fileId}'/></c:url>"
                       data-filename="${f.originFileName}">
                      ${f.originFileName}
                    </a>
                  </li>
                </c:forEach>
              </ul>
            </c:if>
          </td>
        </tr>
      </tbody>
    </table>
  </div>

  <div class="mt-3">
    <button id="btnBack" class="btn">돌아가기</button>

    <form id="deleteForm" action="<c:url value='/board/delete.do'/>?boardId=${detail.boardId}" method="post" class="d-inline">
      <input type="hidden" name="boardId" value="${detail.boardId}">
      <button type="submit" class="btn">삭제하기</button>
    </form>

    <form id="editForm" action="<c:url value='/board/callBoardUpdate.do'/>" method="post" class="d-inline">
      <input type="hidden" name="boardId" value="${detail.boardId}">
      <button type="submit" class="btn">수정하기</button>
    </form>
  </div>

  <table class="table mt-5">
    <thead>
      <tr style="text-align:center;"></tr>
    </thead>
    <tbody>
      <c:if test="${not empty nextBoard}">
        <tr>
          <td>다음글</td>
          <td>
            <form action="<c:url value='/board/getDetail.do'/>" method="post" class="d-inline">
              <input type="hidden" name="boardId" value="${nextBoard.boardId}">
              <button type="submit" class="btn btn-link">${nextBoard.title}</button>
            </form>
          </td>
          <td style="text-align:center;">${nextBoard.view}</td>
          <td style="text-align:center;">
            <fmt:formatDate pattern="yyyy-MM-dd  HH시mm분" value="${nextBoard.createTimestamp}"/>
          </td>
        </tr>
      </c:if>

      <c:if test="${not empty previousBoard}">
        <tr>
          <td>이전글</td>
          <td>
            <form action="<c:url value='/board/getDetail.do'/>" method="post" class="d-inline">
              <input type="hidden" name="boardId" value="${previousBoard.boardId}">
              <button type="submit" class="btn btn-link">${previousBoard.title}</button>
            </form>
          </td>
          <td style="text-align:center;">${previousBoard.view}</td>
          <td style="text-align:center;">
            <fmt:formatDate pattern="yyyy-MM-dd  HH시mm분" value="${previousBoard.createTimestamp}"/>
          </td>
        </tr>
      </c:if>
    </tbody>
  </table>
</div>

<script src="<c:url value='/js/board/board-detail.js'/>"></script>
</body>
</html>
