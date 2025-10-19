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
    <!-- ===== [수정] form에 id="boardForm" 추가: JS에서 hidden을 append하기 쉬움 ===== -->
    <form id="boardForm" action="updateBoard.do" method="post" enctype="multipart/form-data" class="form-group">
    <!-- ===================================================================== -->

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
                <!-- ===== [수정] 삭제 버튼 id 고정값 제거, 공통 클래스 부여(.btn-delete-file) ===== -->
                <button type="button" class="btn btn-delete-file" data-file-id="${fileVO.fileId}">삭제</button>
                <!-- ====================================================================== -->
            </p>
        </c:forEach>
    </div>

    <!-- ===== [수정] 제출 시 서버로 보낼 hidden inputs를 모아둘 버킷 ===== -->
    <div id="delete-bucket"></div>
    <!-- ================================================================= -->

    <button class="btn btn-primary" type="submit" value="작성하기">작성하기</button>
    <button class="btn btn-primary" type="button" onclick="window.history.back();">돌아가기</button>

    </form>
</div>
</body>

<script type="text/javascript">
    var i = 0;

    // ===== [수정] 즉시 AJAX 삭제 로직 제거 =====
    // 기존의 $(document).ready 내 $.ajax(...) 삭제
    // ===================================================

    // ===== [수정] 삭제 버튼 클릭 시: hidden 추가 + 화면에서만 제거 =====
    document.addEventListener('click', function (e) {
        if (e.target && e.target.classList.contains('btn-delete-file')) {
            var fileId = e.target.getAttribute('data-file-id');
            // 제출 시 함께 전달될 hidden input 추가 (name=deleteFileIds)
            var hidden = document.createElement('input');
            hidden.type = 'hidden';
            hidden.name = 'deleteFileIds';
            hidden.value = fileId;

            // delete-bucket이 있으면 그쪽에, 없으면 form에 직접 추가
            var bucket = document.getElementById('delete-bucket') || document.getElementById('boardForm');
            bucket.appendChild(hidden);

            // 화면에서 해당 파일 행만 제거 (사용자에게 '지울 예정' 표시)
            var p = e.target.closest('p');
            if (p) p.remove();
        }
    });
    // ===================================================

    function addFile() {
        // 기존 로직 유지
        var str = "<div class='file-group'><input class='form-control' type='file' name='file'><a href='#this' name='file-delete'>삭제</a></div>";
        if (i < 5) {
            document.getElementById("file-list").insertAdjacentHTML('beforeend', str);
            i++;
        }
        // 동적 추가된 a[name=file-delete]에 대한 삭제(화면상 제거)
        setTimeout(function () {
            var anchors = document.querySelectorAll("a[name='file-delete']");
            anchors.forEach(function (a) {
                a.onclick = function (e) {
                    e.preventDefault();
                    deleteFile(a);
                    if (i > 0) i--;
                }
            });
        }, 0);
    }

    function deleteFile(obj) {
        obj.parentElement.remove();
    }
</script>
</html>
