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
               url: "/test/review/add.do",
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

     
        
        // 즐겨찾기 토글 함수 (숫자 카운팅 기능 추가)
function toggleBookmark(id) {
    $.ajax({
        url: "/test/api/bookmark/toggle.do",
        type: "POST",
        data: { parkId: id },
        success: function(res) {
            var icon = $("#detailBmIcon");
            var countSpan = $("#bookmarkCount"); // ★ ID 추가 필요 (HTML 수정 참조)
            var currentCount = parseInt(countSpan.text()) || 0;

            if(res === 'inserted') {
                // 추가됨: 아이콘 채우고 숫자 +1
                icon.removeClass('bi-bookmark').addClass('bi-bookmark-fill text-warning');
                countSpan.text(currentCount + 1);
            } else if(res === 'login_required') {
                alert("로그인이 필요합니다.");
            } else {
                // 취소됨: 아이콘 비우고 숫자 -1
                icon.removeClass('bi-bookmark-fill text-warning').addClass('bi-bookmark');
                countSpan.text(currentCount > 0 ? currentCount - 1 : 0);
            }
        },
        error: function(err) {
            console.error(err);
            alert("오류가 발생했습니다.");
        }
    });
}
        
     