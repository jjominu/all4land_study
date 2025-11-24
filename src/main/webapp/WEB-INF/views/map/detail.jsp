<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>${park.parkNm} - 상세정보</title>

    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@400;500;700&display=swap" rel="stylesheet">
    
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">
    
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

    <style>
        /* 기본 설정 (지도 페이지와 다름) */
        body {
            background-color: #fafafa; /* 인스타 배경색 */
            font-family: 'Noto Sans KR', sans-serif;
            overflow-y: auto; /* 스크롤 허용 (중요!) */
        }

        /* 헤더에 가려지지 않게 여백 추가 */
        .main-content {
            margin-top: 80px; 
            padding-bottom: 50px;
        }

        /* --- 1. 프로필 영역 --- */
        .profile-container {
            max-width: 935px;
            margin: 0 auto;
            padding: 0 20px 30px;
            border-bottom: 1px solid #dbdbdb;
        }

        .profile-header {
            display: flex;
            align-items: center;
            margin-bottom: 20px;
        }

        .profile-img-wrapper {
            flex-grow: 1;
            margin-right: 30px;
            display: flex;
            justify-content: center;
        }

        /* 프사 테두리 & 크기 */
        .profile-img-border {
            width: 160px;
            height: 160px;
            border-radius: 50%;
            padding: 4px;
            border: 1px solid #dbdbdb;
            display: flex;
            align-items: center;
            justify-content: center;
            background-color: white;
        }

        .profile-img {
            width: 100%;
            height: 100%;
            border-radius: 50%;
            object-fit: cover; /* 이미지 비율 유지하며 꽉 채우기 */
        }

        .profile-info {
            flex-grow: 2;
            flex-basis: 0;
        }

        .profile-title-row {
            display: flex;
            align-items: center;
            margin-bottom: 20px;
        }

        .park-name {
            font-size: 28px;
            font-weight: 400;
            margin-right: 20px;
        }

        .profile-stats {
            display: flex;
            list-style: none;
            padding: 0;
            margin-bottom: 20px;
            font-size: 16px;
        }

        .profile-stats li { margin-right: 40px; }
        .stat-count { font-weight: 700; }

        .profile-bio { font-size: 16px; line-height: 1.5; }
        .profile-bio .category { color: #8e8e8e; font-weight: 600; }
        .profile-bio a { color: #00376b; text-decoration: none; font-weight: 600; }

        /* --- 2. 탭 메뉴 --- */
        .gallery-nav {
            max-width: 935px;
            margin: 0 auto;
            display: flex;
            justify-content: center;
            border-top: 1px solid #dbdbdb;
        }

    

       
        /* --- 3. 그리드 갤러리 --- */
        .gallery-grid {
            display: grid;
            grid-template-columns: repeat(3, 1fr);
            gap: 28px;
            max-width: 935px;
            margin: 0 auto;
        }

        .gallery-item {
            position: relative;
            width: 100%;
            padding-bottom: 100%; /* 1:1 정방형 유지 */
            overflow: hidden;
            background-color: #efefef;
            cursor: pointer;
        }

        .gallery-image {
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            object-fit: cover;
        }

        /* 호버 효과 */
        .gallery-item:hover .gallery-overlay { display: flex; }
        .gallery-overlay {
            display: none;
            position: absolute;
            top: 0; left: 0; width: 100%; height: 100%;
            background-color: rgba(0, 0, 0, 0.3);
            justify-content: center; align-items: center;
            color: white; font-weight: bold; font-size: 18px;
        }
        .overlay-icon { margin-right: 7px; }
        .overlay-stat { margin-right: 20px; }

      /* 모달 커스텀 */
.modal-content { border: none; }
.cursor-pointer { cursor: pointer; }

/* 스크롤바 얇게 */
.overflow-auto::-webkit-scrollbar { width: 6px; }
.overflow-auto::-webkit-scrollbar-thumb { background-color: #dbdbdb; border-radius: 3px; }

/* 모바일에서 모달 레이아웃 변경 */
@media (max-width: 768px) {
    .modal-dialog { margin: 0; max-width: 100%; height: 100%; }
    .modal-content { height: 100%; border-radius: 0; max-height: none !important; }
    .col-md-7 { height: 40vh; min-height: auto !important; } /* 이미지 영역 줄임 */
    .col-md-5 { height: 60vh; }
}
    </style>
</head>
<body>

    <jsp:include page="../include/header.jsp"></jsp:include>

    <div class="main-content">
        
        <div class="profile-container">
            <div class="profile-header">
                <div class="profile-img-wrapper">
                    <div class="profile-img-border">
                        <img src="/uploads/${park.parkImg}" 
                             class="profile-img" 
                             alt="${park.parkNm}"
                             onerror="this.src='<c:url value="/images/park_default.png"/>'">
                    </div>
                </div>

                <div class="profile-info">
                    <div class="profile-title-row">
                        <h2 class="park-name">${park.parkNm}</h2>
                        
                        <c:if test="${sessionScope.memId != null}">
                            <div class="d-flex gap-2">
                                <button class="btn btn-primary btn-sm fw-bold px-3" onclick="openReviewModal()">리뷰 쓰기</button>
                                <button class="btn btn-outline-secondary btn-sm" onclick="toggleBookmark(${park.id})">
                                    <i class="bi ${isBookmarked ? 'bi-bookmark-fill text-warning' : 'bi-bookmark'}" id="detailBmIcon"></i>
                                </button>
                            </div>
                        </c:if>
                    </div>

                    <ul class="profile-stats">
                        <li>게시물 <span class="stat-count">${fn:length(reviewList)}</span></li>
                        <li>조회수 <span class="stat-count">${park.viewCount}</span></li>
                        <li>즐겨찾기 <span class="stat-count">0</span></li> </ul>

                    <div class="profile-bio">
                        <div class="category">반려견 놀이터 · 공원</div>
                        <div><i class="bi bi-geo-alt-fill text-danger me-1"></i>${park.addr}</div>
                        <div><i class="bi bi-clock me-1"></i>${park.operTm} / ${park.useAmt}</div>
                        <div class="text-muted small mt-1">${park.fcs}</div>
                        <a href="#" class="mt-1 d-block">www.dogpark.go.kr</a>
                    </div>
                </div>
            </div>
        </div>

        <div class="gallery-nav">
            <div class="nav-item active"><i class="bi bi-grid-3x3"></i> 게시물</div>
            <div class="nav-item"><i class="bi bi-bookmark"></i> 태그됨</div>
        </div>

       <div class="gallery-grid">
            <c:forEach var="review" items="${reviewList}">
                
                <c:set var="thumbImg" value="" />
                <c:if test="${not empty review.photoList and fn:length(review.photoList) > 0}">
                    <c:set var="thumbImg" value="${review.photoList[0].filePath}" />
                </c:if>
                
                <div class="gallery-item" onclick="openFeedModal(${review.reviewId})">
                    <c:choose>
                        <c:when test="${not empty thumbImg}">
                            <img src="/uploads/${thumbImg}" 
                                 class="gallery-image" 
                                 alt="Review Thumbnail"
                                 onerror="this.src='<c:url value="/images/park_default.png"/>'">
                        </c:when>
                        <c:otherwise>
                            <div class="gallery-image d-flex align-items-center justify-content-center bg-light text-muted border">
                                <div class="text-center">
                                    <i class="bi bi-card-text display-6"></i><br>
                                    <span style="font-size:0.8rem;">글만 있음</span>
                                </div>
                            </div>
                        </c:otherwise>
                    </c:choose>

                    <div class="gallery-overlay">
                        <span class="overlay-stat"><i class="bi bi-heart-fill overlay-icon"></i>${review.likeCount}</span>
                        <span class="overlay-stat"><i class="bi bi-chat-fill overlay-icon"></i>0</span>
                    </div>
                    
                    <c:if test="${not empty review.photoList and fn:length(review.photoList) > 1}">
                        <i class="bi bi-collection-fill text-white position-absolute top-0 end-0 m-2" style="font-size: 14px; text-shadow: 0 0 3px rgba(0,0,0,0.5);"></i>
                    </c:if>
                </div>
            </c:forEach>
            
            <c:if test="${empty reviewList}">
                <div class="text-center py-5 text-muted w-100" style="grid-column: 1 / -1;">
                    <i class="bi bi-camera display-4"></i>
                    <p class="mt-3">아직 게시물이 없습니다.<br>첫 번째 사진을 공유해보세요!</p>
                </div>
            </c:if>
        </div>

    <div class="modal fade" id="writeModal" tabindex="-1">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title fw-bold mx-auto">새 게시물 만들기</h5>
                    <button type="button" class="btn-close position-absolute end-0 me-3" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <form id="reviewForm" enctype="multipart/form-data">
                        <input type="hidden" name="parkId" value="${park.id}">
                        <div class="mb-3 text-center">
                            <label class="form-label d-block fw-bold mb-2">평점</label>
                            <div class="btn-group" role="group">
                                <c:forEach begin="1" end="5" var="i">
                                    <input type="radio" class="btn-check" name="rating" id="r${6-i}" value="${6-i}" ${i==1 ? 'checked' : ''}>
                                    <label class="btn btn-outline-warning" for="r${6-i}">${6-i}점</label>
                                </c:forEach>
                            </div>
                        </div>
                        <div class="mb-3">
    <label class="form-label d-block fw-bold mb-2">사진 추가</label>
    <input type="file" class="form-control mb-2" name="uploadFiles" id="fileInput" multiple accept="image/*" onchange="previewImages()">
    
    <div id="imagePreviewContainer" class="d-flex gap-2 overflow-auto" style="white-space: nowrap; padding-bottom: 5px;">
        </div>
</div>
                        <textarea class="form-control border-0" name="reviewContent" rows="5" placeholder="문구 입력..." style="resize:none;"></textarea>
                    </form>
                </div>
                <div class="modal-footer p-1">
                    <button type="button" class="btn btn-link text-primary fw-bold w-100 text-decoration-none" onclick="submitReview()">공유하기</button>
                </div>
            </div>
        </div>
    </div>
<div class="modal fade" id="feedModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-xl"> <div class="modal-content overflow-hidden rounded-0" style="max-height: 85vh;">
            <div class="row g-0 h-100">
                
                <div class="col-md-7 bg-black d-flex align-items-center justify-content-center" style="min-height: 400px;">
                    
                    <div id="feedCarousel" class="carousel slide w-100" data-bs-interval="false">
                        <div class="carousel-inner" id="modalCarouselInner">
                            </div>
                        
                        <button class="carousel-control-prev" type="button" data-bs-target="#feedCarousel" data-bs-slide="prev">
                            <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                        </button>
                        <button class="carousel-control-next" type="button" data-bs-target="#feedCarousel" data-bs-slide="next">
                            <span class="carousel-control-next-icon" aria-hidden="true"></span>
                        </button>
                    </div>

                </div>

                <div class="col-md-5 d-flex flex-column bg-white" style="max-height: 85vh;">
                    
                    <div class="p-3 border-bottom d-flex align-items-center">
                        <img id="modalUserImg" src="" class="rounded-circle border me-3" style="width: 32px; height: 32px; object-fit: cover;">
                        <span id="modalUserName" class="fw-bold text-dark text-decoration-none fs-6"></span>
                        <button type="button" class="btn-close ms-auto" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>

                    <div class="p-3 flex-grow-1 overflow-auto" style="scrollbar-width: thin;">
                        <div class="d-flex mb-3">
                            <img id="modalUserImgSmall" src="" class="rounded-circle border me-3" style="width: 32px; height: 32px; object-fit: cover;">
                            <div>
                                <span id="modalUserNameSmall" class="fw-bold me-2"></span>
                                <span id="modalContent" style="white-space: pre-wrap; font-size: 0.9rem;"></span>
                                <div class="mt-2 text-muted" style="font-size: 0.75rem;" id="modalDate"></div>
                            </div>
                        </div>
                        
                        <div class="mt-4 pt-3 border-top">
                            <p class="text-center text-muted small">아직 댓글이 없습니다.</p>
                        </div>
                    </div>

                    <div class="p-3 border-top bg-white">
                        <div class="d-flex justify-content-between mb-2 fs-4">
                            <div>
                                <i class="bi bi-heart me-3 cursor-pointer"></i>
                                <i class="bi bi-chat me-3 cursor-pointer"></i>
                                <i class="bi bi-send cursor-pointer"></i>
                            </div>
                            <div>
                                <i class="bi bi-bookmark cursor-pointer"></i>
                            </div>
                        </div>
                        <div class="fw-bold mb-1 text-dark">좋아요 <span id="modalLikeCount">0</span>개</div>
                        <div class="text-warning small" id="modalRating"></div>
                    </div>
                    
                    <div class="p-3 border-top">
                        <div class="input-group">
                            <input type="text" class="form-control border-0" placeholder="댓글 달기..." style="box-shadow: none;">
                            <button class="btn text-primary fw-bold" type="button">게시</button>
                        </div>
                    </div>

                </div>
            </div>
        </div>
    </div>
</div>
    <script>
 // 이미지 미리보기 함수
 
 // 피드 상세 보기 모달 열기
function openFeedModal(reviewId) {
    
    $.ajax({
        url: "/test/review/detail.do", // 컨트롤러 URL 확인
        type: "POST",
        data: { reviewId: reviewId },
        dataType: "json",
        success: function(data) {
            // 1. 데이터 바인딩
            
            // 작성자 정보
            var userImg = data.memImg ? data.memImg : '/images/user_default.png';
            $("#modalUserImg").attr("src", userImg);
            $("#modalUserImgSmall").attr("src", userImg);
            $("#modalUserName").text(data.memName);
            $("#modalUserNameSmall").text(data.memName);
            
            // 내용 및 날짜
            $("#modalContent").text(data.reviewContent);
            // 날짜 포맷팅 (yyyy-MM-dd) - timestamp로 올 경우 변환 필요
            var date = new Date(data.createdAt);
            $("#modalDate").text(date.getFullYear() + "-" + (date.getMonth()+1) + "-" + date.getDate());
            
            $("#modalLikeCount").text(data.likeCount);
            
            // 평점 별 표시
            var stars = "";
            for(var i=0; i<data.rating; i++) stars += "★";
            $("#modalRating").text(stars);

            // 2. 이미지 슬라이드(Carousel) 구성
            var html = "";
            var photos = data.photoList;
            
            if(photos && photos.length > 0) {
                for(var i=0; i<photos.length; i++) {
                    var active = (i === 0) ? "active" : "";
                    html += `
                        <div class="carousel-item ${active} h-100" style="background:#000;">
                            <div class="d-flex align-items-center justify-content-center" style="height: 600px;">
                                <img src="/uploads/${photos[i].filePath}" class="d-block" style="max-width:100%; max-height:100%; object-fit:contain;" alt="Review Image">
                            </div>
                        </div>
                    `;
                }
            } else {
                // 사진 없을 때
                html = `
                    <div class="carousel-item active h-100">
                        <div class="d-flex align-items-center justify-content-center text-white" style="height: 600px;">
                            <span>사진 없음</span>
                        </div>
                    </div>
                `;
            }
            
            $("#modalCarouselInner").html(html);
            
            // 화살표 표시 여부 (사진 1장이면 숨김)
            if(photos && photos.length > 1) {
                $(".carousel-control-prev, .carousel-control-next").show();
            } else {
                $(".carousel-control-prev, .carousel-control-next").hide();
            }

            // 3. 모달 띄우기
            var myModal = new bootstrap.Modal(document.getElementById('feedModal'));
            myModal.show();
        },
        error: function(err) {
            console.error(err);
            alert("정보를 불러오는데 실패했습니다.");
        }
    });
}
    function previewImages() {
        var previewContainer = document.getElementById("imagePreviewContainer");
        var fileInput = document.getElementById("fileInput");
        var files = fileInput.files;

        // 초기화
        previewContainer.innerHTML = "";

        if (files.length > 5) {
            alert("사진은 최대 5장까지만 업로드 가능합니다.");
            fileInput.value = ""; // 선택 취소
            return;
        }

        for (var i = 0; i < files.length; i++) {
            var file = files[i];
            var reader = new FileReader();

            reader.onload = function(e) {
                var imgDiv = document.createElement("div");
                imgDiv.style.width = "80px";
                imgDiv.style.height = "80px";
                imgDiv.style.flexShrink = "0"; // 크기 줄어듦 방지
                imgDiv.style.backgroundImage = "url('" + e.target.result + "')";
                imgDiv.style.backgroundSize = "cover";
                imgDiv.style.backgroundPosition = "center";
                imgDiv.style.borderRadius = "4px";
                imgDiv.style.border = "1px solid #ddd";
                
                previewContainer.appendChild(imgDiv);
            }
            reader.readAsDataURL(file);
        }
    }
        function openReviewModal() {
            $('#writeModal').modal('show');
        }

        function submitReview() {
            var form = $('#reviewForm')[0];
            var formData = new FormData(form);

            $.ajax({
                url: "<c:url value='/review/add.do'/>",
                type: 'POST',
                data: formData,
                processData: false,
                contentType: false,
                success: function(res) {
                    if(res === "ok") {
                        alert("등록되었습니다.");
                        location.reload();
                    } else {
                        alert("등록 실패: " + res);
                    }
                },
                error: function() {
                    alert("오류 발생");
                }
            });
        }

        function toggleBookmark(id) {
            $.ajax({
                url: "<c:url value='/api/bookmark/toggle.do'/>",
                type: "POST",
                data: { parkId: id },
                success: function(res) {
                    var icon = $("#detailBmIcon");
                    if(res === 'inserted') icon.removeClass('bi-bookmark').addClass('bi-bookmark-fill text-warning');
                    else if(res === 'login_required') alert("로그인이 필요합니다.");
                    else icon.removeClass('bi-bookmark-fill text-warning').addClass('bi-bookmark');
                }
            });
        }
        
     
    </script>

</body>
</html>