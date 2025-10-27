<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>게시판 작성</title>
  <%@ include file="../include/style.jsp"%>
</head>

<jsp:include page="../include/header.jsp"></jsp:include>
<jsp:include page="../include/footer.jsp"></jsp:include>

<body>
  <c:if test="${not empty errorMessages}">
    <ul id="error-messages" style="display:none;">
      <c:forEach var="msg" items="${errorMessages}">
        <li><c:out value="${msg}"/></li>
      </c:forEach>
    </ul>
  </c:if>

  <div class="container">
    <form id="form1" class="form-group" action="<c:url value='/board/upload.do'/>" method="post" enctype="multipart/form-data">
      <div class="form-group">
        <label for="title">제목</label>
        <input class="form-control" type="text" id="title" name="title" maxlength="20" required />
      </div>

      <div class="form-group">
        <label for="content">내용</label>
        <textarea class="form-control" id="content" name="content" rows="5" cols="50" maxlength="1000" required></textarea>
      </div>

      <div class="form-group" id="file-list">
        <label>파일</label>
        <div class="mb-2">
          <button class="btn btn-primary" type="button" id="btnAddFile">파일추가</button>
        </div>
        <div class="file-group">
          <input class="form-control" type="file" name="file" />
          <button type="button" class="btn btn-delete-file">삭제</button>
        </div>
      </div>

      <div class="mt-3">
        <input class="btn btn-primary" type="submit" value="작성하기" />
        <button class="btn btn-secondary" type="button" onclick="window.history.back();">돌아가기</button>
      </div>
    </form>
  </div>

  <script src="<c:url value='/js/board/board-write.js'/>"></script>
</body>
</html>
