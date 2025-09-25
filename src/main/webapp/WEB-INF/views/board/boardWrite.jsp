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
<div class="mb-3">
	<form id="form1" class="form-group"action="upload.do" method="post" enctype="multipart/form-data" >
	 	<th scope="row">제목</th>
	 
	 	<input class="form-control" type="text" id ="TITLE" name="title" required>
	 
	 	<td scope="row">내용</td>
	 	<p>
		<textarea  class="form-control" rows="5" cols="50" name="content" required></textarea>
	
		
		
		<div class="form-group" id="file-list">
        	<a href="#this" onclick="addFile()">파일추가</a>
	        <div class="file-group">
	            <input class="form-control" type="file" name="file"><a href='#this' name='file-delete'>삭제</a>
	        </div>
    	</div>
		<p>
		<input type="submit" value="작성하기">
	</form>
	<p>
		<button class="btn btn-primary" onclick="window.history.back();">돌아가기</button>

 </body>
 <script type="text/javascript">
 var i = 1;
    $(document).ready(function() {
        $("a[name='file-delete']").on("click", function(e) {
            e.preventDefault();
            deleteFile($(this));
        });
    })
 
    function addFile() {
    	
        var str = "<div class='file-group'><input type='file' name='file'><a href='#this' name='file-delete'>삭제</a></div>";
        if(i<5){
	        $("#file-list").append(str);
	        i++;
        }
        $("a[name='file-delete']").on("click", function(e) {
        	
            e.preventDefault();
            deleteFile($(this));
            if(i>0)i--;
        	
            
        });
    }
 
    function deleteFile(obj) {
        obj.parent().remove();
    }
</script>
 
</html>