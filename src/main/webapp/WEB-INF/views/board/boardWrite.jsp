<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판 작성</title>
</head>
<%@ include file="../include/style.jsp"%>
<jsp:include page="../include/header.jsp"></jsp:include>
<jsp:include page="../include/footer.jsp"></jsp:include>
<body>
<div class="container">
	<form id="form1" class="form-group"action="upload.do" method="post" enctype="multipart/form-data" >
	 	<th scope="row">제목</th>
	 
	 	<input class="form-control" type="text" id ="title" name="title" required maxlength="20">
	 
	 	<td scope="row">내용</td>
	 	<p>
		<textarea  class="form-control" rows="5" cols="50" name="content" required maxlength="1000"></textarea>
	
		
		
		<div class="form-group" id="file-list">
        	<button class="btn btn-primary" type="button" onclick="addFile()" >파일추가</button>
        	
	        <div class="file-group"><input class="form-control" type="file" name="file"><a href='#this' name='file-delete'>삭제</a></div>
    	</div>
		<p>
		<input class="form-control" type="submit"  value="작성하기">
	</form>
	<p>
		<button class="btn btn-primary" onclick="window.history.back();">돌아가기</button>
</div>
</body>

<script type="text/javascript">
  var i = document.querySelectorAll('#file-list .file-group').length || 0;
  var max = 5;

  $(document).on('click', '.btn-delete-file, a[name="file-delete"]', function (e) {
    e.preventDefault();
    var $group = $(this).closest('.file-group');
    if ($group.length) {
      $group.remove();
      if (i > 0) i--;
    }
  });

  function addFile() {
    var str = "<div class='file-group'><input class='form-control' type='file' name='file'><a href='#this' name='file-delete'>삭제</a></div>";
    if (i < max) {
      $("#file-list").append(str);
      i++;
    }
  }
</script>

</html>