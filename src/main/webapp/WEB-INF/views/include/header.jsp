<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

<script src="https://accounts.google.com/gsi/client" async defer></script>

<style>
    /* 네비게이션 링크 호버 효과 */
    .nav-link {
        color: #555;
        transition: color 0.2s ease-in-out;
        font-weight: 500;
    }
    .nav-link:hover {
        color: #0d6efd !important;
    }
    .nav-link.active {
        color: #0d6efd !important;
        font-weight: 700;
    }
    
    /* 로고 스타일 */
    .brand-logo {
        font-family: 'Noto Sans KR', sans-serif;
        font-weight: 800;
        letter-spacing: -0.5px;
        color: #222;
    }
    
    /* 프로필 이미지 스타일 */
    .profile-img {
        width: 32px; 
        height: 32px; 
        border-radius: 50%; 
        object-fit: cover;
        border: 1px solid #eee;
    }
</style>

<nav class="navbar navbar-expand-lg navbar-light bg-white border-bottom shadow-sm fixed-top" style="height: 70px;">
    <div class="container-fluid px-4">

        <a class="navbar-brand brand-logo fs-4" href="<c:url value='/home'/>">
            <span class="text-primary"><i class="bi bi-geo-alt-fill"></i> 반려견 놀이터</span> Map
        </a>

        <button class="navbar-toggler border-0" type="button" data-bs-toggle="collapse"
            data-bs-target="#navbarNav" aria-controls="navbarNav"
            aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse justify-content-end" id="navbarNav">
            <ul class="navbar-nav gap-3 align-items-center">
                
                <li class="nav-item">
                    <a class="nav-link" aria-current="page" href="<c:url value='/home'/>">
                        <i class="bi bi-house-door"></i> 홈
                    </a>
                </li>
                
                <li class="nav-item">
                    <a class="nav-link" href="<c:url value='/board/list.do'/>">
                        <i class="bi bi-journal-text"></i> 게시판
                    </a>
                </li>
                
                <li class="nav-item">
                    <a class="nav-link active text-primary" href="<c:url value='/map/map.do'/>">
                        <i class="bi bi-map-fill"></i> 지도
                    </a>
                </li>

                <li class="nav-item d-none d-lg-block">
                    <div style="width: 1px; height: 24px; background-color: #ddd;"></div>
                </li>

                <c:choose>
                    <c:when test="${sessionScope.memName == null}">
                        <li class="nav-item">
                            <div id="g_id_onload"
                                 data-client_id="403841398487-ihqi6utppasg7n7o8k724sjoosl9s1g1.apps.googleusercontent.com"
                                 data-callback="handleCredentialResponse"
                                 data-auto_prompt="false">
                            </div>
                            <div class="g_id_signin" 
                                 data-type="standard" 
                                 data-size="medium" 
                                 data-theme="outline"
                                 data-text="signin_with"
                                 data-shape="pill"
                                 data-logo_alignment="left">
                            </div>
                        </li>
                    </c:when>

                    <c:otherwise>
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle d-flex align-items-center gap-2" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                                <img src="${sessionScope.memImg}" class="profile-img" onerror="this.src='/uploads/user_default.png'">
                                <span>${sessionScope.memName}님</span>
                            </a>
                            <ul class="dropdown-menu dropdown-menu-end shadow-sm border-0" aria-labelledby="navbarDropdown">
                                <li><a class="dropdown-item" href="#"><i class="bi bi-person-gear me-2"></i>마이페이지</a></li>
                                <li><hr class="dropdown-divider"></li>
                                <li>
                                    <a class="dropdown-item text-danger" href="<c:url value='/login/logout.do'/>">
                                        <i class="bi bi-box-arrow-right me-2"></i>로그아웃
                                    </a>
                                </li>
                            </ul>
                        </li>
                    </c:otherwise>
                </c:choose>

            </ul>
        </div>
    </div>
</nav>

<div style="height: 70px;"></div>

<script>
    function handleCredentialResponse(response) {
        const idToken = response.credential; 

        $.ajax({
            url: "<c:url value='/login/googleProc.do'/>",
            type: 'POST',
            data: { token: idToken },
            success: function(res) {
                if(res === "ok") {
                   
                    location.reload(); 
                } else {
                    alert("로그인 처리에 실패했습니다.");
                }
            },
            error: function(err) {
                console.error("Google Login Error:", err);
                alert("서버 통신 중 오류가 발생했습니다.");
            }
        });
    }
</script>