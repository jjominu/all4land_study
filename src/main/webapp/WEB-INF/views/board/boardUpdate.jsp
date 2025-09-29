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
	<form action="updateBoard.do" method="post" enctype="multipart/form-data" class="form-group">
	
	<input  type="hidden" id="boardId" name="boardId" value="${detail.boardId}">
	<p>
	<th scope="row">제목</th>
	<p>
	<input class="form-control" type="text" id="title" name="title" value="${detail.title}">
	<p>
	<tr scope="row">내용</tr>
	<p>
	<textarea class="form-control" rows="10" cols="50" name="content">${detail.content}</textarea>
	<p>
	<th scope="row">파일</th>
	<div class="form-group" id="file-list">
		<button class="btn btn-primary" type="button" onclick="addFile()" >파일추가</button>
		<c:forEach items="${fileVO}" var="fileVO">
		    <p>${fileVO.originFileName} 
		       <button type="button" id="deleteBtn" class="btn" data-file-id="${fileVO.fileId}">삭제</button>
		    </p>
		</c:forEach>
	</div>
	<button class="btn btn-primary" type="submit" value="작성하기">작성하기</button>
	<button class="btn btn-primary" onclick="window.history.back();">돌아가기</button>


</form>
</div>
</body>
<script type="text/javascript">
	var i = 0;
	
	$(document).ready(function() {
	    $("a[name='file-delete']").on("click", function(e) {
	        e.preventDefault();
	        deleteFile($(this));
	    });
	    
	    $(document).on("click", "#deleteBtn", function() {
	        var fileId = $(this).data('file-id');
	        var $button = $(this);
	        
	        $.ajax({
	            url: "deleteFile.do",
	            type: "POST",
	            data: { fileId: fileId },
	            success: function(result) {
	                console.log("성공:", result);
	                $button.parent().remove(); 
	            },
	            error: function() {
	                
	            }
	        });
	    });
	});
	
	function addFile() {
		var str="<div class='file-group'><input class='form-control' type='file' name='file'><a href='#this' name='file-delete'>삭제</a></div>"
	    if(i < 5) {
	        $("#file-list").append(str);
	        i++;
	    }
	    
	    $("a[name='file-delete']").on("click", function(e) {
	        e.preventDefault();
	        deleteFile($(this));
	        if(i > 0) i--;
	    });
	}
	
	function deleteFile(obj) {
	    obj.parent().remove();
	}
</script>
</html>