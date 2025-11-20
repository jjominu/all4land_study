<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">

<style>
    /* 네비게이션 링크 호버 효과 */
    .nav-link {
        color: #555;
        transition: color 0.2s ease-in-out;
        font-weight: 500; /* 글자 두께 약간 굵게 */
    }
    .nav-link:hover {
        color: #0d6efd !important; /* 호버 시 파란색 */
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
</style>

<nav class="navbar navbar-expand-lg navbar-light bg-white border-bottom shadow-sm fixed-top" style="height: 70px;">
    <div class="container-fluid px-4">

        <a class="navbar-brand brand-logo fs-4" href="<c:url value='/home'/>">
            <span class="text-primary"><i class="bi bi-geo-alt-fill"></i> 조민우</span> Map
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

                </ul>
        </div>
    </div>
</nav>

<div style="height: 70px;"></div>