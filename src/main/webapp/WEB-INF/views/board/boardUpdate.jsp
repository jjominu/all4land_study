<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>게시판 수정</title>
  <%@ include file="../include/style.jsp"%>
</head>

<jsp:include page="../include/header.jsp"></jsp:include>
<jsp:include page="../include/footer.jsp"></jsp:include>

<body>
<div class="container">
  <form id="boardForm" action="<c:url value='/board/updateBoard.do'/>" method="post" enctype="multipart/form-data" class="form-group">
    <input type="hidden" id="boardId" name="boardId" value="${detail.boardId}"/>

    <div class="form-group">
      <label for="title">제목</label>
      <input class="form-control" type="text" id="title" name="title" value="${detail.title}" required maxlength="20"/>
    </div>

    <div class="form-group">
      <label for="content">내용</label>
      <textarea class="form-control" id="content" name="content" rows="10" cols="50" required maxlength="1000">${detail.content}</textarea>
    </div>

    <div class="form-group" id="file-list">
      <div class="mb-2">
        <button class="btn btn-primary" type="button" id="btnAddFile">파일추가</button>
      </div>

      <c:forEach items="${fileVO}" var="fileVO">
        <p class="attached-file" data-file-id="${fileVO.fileId}">
          <span class="fname">${fileVO.originFileName}</span>
			<button type="button" class="btn btn-delete-existing" data-file-id="${fileVO.fileId}">삭제</button>
        </p>
      </c:forEach>
    </div>

    <div id="delete-bucket"></div>

    <div class="mt-3">
      <button class="btn btn-primary" type="submit">작성하기</button>
      <button class="btn btn-secondary" type="button" onclick="window.history.back();">돌아가기</button>
    </div>
  </form>
</div>

<!-- 외부 JS -->
<script src="<c:url value='/js/board/board-update.js'/>"></script>
</body>
</html>
