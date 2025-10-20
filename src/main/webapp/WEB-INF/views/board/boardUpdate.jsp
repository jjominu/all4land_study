<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판 수정</title>
</head>
<%@ include file="../include/style.jsp"%>
<jsp:include page="../include/header.jsp"></jsp:include>
<jsp:include page="../include/footer.jsp"></jsp:include>
<body>
<div class="container">
    <form id="boardForm" action="updateBoard.do" method="post" enctype="multipart/form-data" class="form-group">
    <input type="hidden" id="boardId" name="boardId" value="${detail.boardId}">
    <p>
    <th scope="row">제목</th>
    <p>
    <input class="form-control" type="text" id="title" name="title" value="${detail.title}" required maxlength="20">
    <p>
    <tr scope="row">내용</tr>
    <p>
    <textarea class="form-control" rows="10" cols="50" name="content" required maxlength="1000">${detail.content}</textarea>
    <p>
    <th scope="row">파일</th>

    <div class="form-group" id="file-list">
        <button class="btn btn-primary" type="button" onclick="addFile()">파일추가</button>

        <c:forEach items="${fileVO}" var="fileVO">
            <p>
                ${fileVO.originFileName}
                <button type="button" class="btn btn-delete-file" data-file-id="${fileVO.fileId}">삭제</button>
            </p>
        </c:forEach>
    </div>

    <div id="delete-bucket"></div>

    <button class="btn btn-primary" type="submit" value="작성하기">작성하기</button>
    <button class="btn btn-primary" type="button" onclick="window.history.back();">돌아가기</button>

    </form>
</div>
</body>

<script type="text/javascript">
  var i = document.querySelectorAll('#file-list .file-group').length || 0;
  console.log(i);
  var max = 5;

  document.addEventListener('click', function (e) {
    if (e.target && e.target.classList.contains('btn-delete-file')) {
      var fileId = e.target.getAttribute('data-file-id');
      var hidden = document.createElement('input');
      hidden.type = 'hidden';
      hidden.name = 'deleteFileIds';
      hidden.value = fileId;

      var bucket = document.getElementById('delete-bucket') || document.getElementById('boardForm');
      bucket.appendChild(hidden);

      var p = e.target.closest('p');
      if (p) p.remove();
    }
  });

  document.addEventListener('click', function (e) {
    var target = e.target;
    if (target && target.matches('a[name="file-delete"]')) {
      e.preventDefault();
      var group = target.closest('.file-group');
      if (group) {
        group.remove();
        if (i > 0) i--;
      }
    }
  });

  function addFile() {
    var str = "<div class='file-group'><input class='form-control' type='file' name='file'><a href='#this' name='file-delete'>삭제</a></div>";
    if (i < max) {
      document.getElementById("file-list").insertAdjacentHTML('beforeend', str);
      i++;
    } else {
      alert('파일은 최대 ' + max + '개까지 추가할 수 있습니다.');
    }
  }

  function deleteFile(obj) {
    obj.parentElement.remove();
    if (i > 0) i--;
  }
</script>

</html>
