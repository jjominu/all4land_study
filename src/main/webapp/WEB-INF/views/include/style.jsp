<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<head>
<meta charset="UTF-8">

    <!-- Bootstrap : CSS only -->
    <link
        href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css"
        rel="stylesheet"
        integrity="sha384-GLhlTQ8iRABdZLl6O3oVMWSktQOp6b7In1Zl3/Jr59b6EGGoI1aFkw7cmDA6j6gD"
        crossorigin="anonymous">
    <script
        src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-w76AqPfDkMBDXo30jS1Sgez6pr3x5MlQ1ZAGC+nuZB+EYdgRZgiwxhTBTkF7CXvN"
        crossorigin="anonymous"></script>

    <!-- Bootstrap : JavaScript Bundle with Popper -->
    <script
        src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"
        integrity="sha384-oBqDVmMz9ATKxIep9tiCxS/Z9fNfEXiDAYTujMAeBAsjFuCZSmKbSSUnQlmh/jp3"
        crossorigin="anonymous"></script>
    <script
        src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.min.js"
        integrity="sha384-mQ93GR66B00ZXjt0YO5KlohRA5SY2XofN4zfuZxLkoj1gXtW8ANNCe9d5Y3eG5eD"
        crossorigin="anonymous"></script>

    <!-- JQuery -->	
    <script src=https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.3/jquery.min.js></script>
    
  <style>
       /* 네이버 지도 스타일 커스텀 CSS */
        body {
            font-family: 'Noto Sans KR', sans-serif;
            overflow: hidden; /* 전체 스크롤 방지 */
            height: 100vh;
            display: flex;
            flex-direction: column;
        }

        /* 메인 컨테이너: 헤더/푸터 제외한 나머지 공간 꽉 채우기 */
        #map-wrapper {
            display: flex;
            flex: 1;
            position: relative;
            height: calc(100vh - 120px); /* 헤더/푸터 높이에 따라 조정 필요 */
            overflow: hidden;
        }

        /* [왼쪽] 사이드바 패널 */
        #left-panel {
            width: 400px;
            min-width: 400px;
            height: 100%;
            background: #fff;
            box-shadow: 2px 0 10px rgba(0,0,0,0.1);
            z-index: 1000;
            display: flex;
            flex-direction: column;
            border-right: 1px solid #e0e0e0;
        }

        /* 사이드바 - 검색 영역 */
        .search-container {
            padding: 20px;
            background-color: #fff;
            border-bottom: 1px solid #f0f0f0;
        }

        .form-label {
            font-weight: 700;
            font-size: 0.85rem;
            color: #333;
            margin-bottom: 5px;
        }

        .form-control, .form-select {
            border-radius: 8px;
            border: 1px solid #ddd;
            font-size: 0.9rem;
            padding: 10px;
        }
        
        .form-control:focus, .form-select:focus {
            box-shadow: 0 0 0 3px rgba(13, 110, 253, 0.15);
            border-color: #0d6efd;
        }

        /* 사이드바 - 리스트 영역 (스크롤 가능) */
        #dog-list {
            flex: 1;
            overflow-y: auto;
            background-color: #f8f9fa;
            padding: 15px;
        }
        
        /* 스크롤바 디자인 */
        #dog-list::-webkit-scrollbar { width: 6px; }
        #dog-list::-webkit-scrollbar-thumb { background-color: #ccc; border-radius: 3px; }
        #dog-list::-webkit-scrollbar-track { background-color: #f8f9fa; }

        /* 카드 디자인 */
        .place-card {
            background: #fff;
            border: 1px solid #eee;
            border-radius: 12px;
            padding: 15px;
            margin-bottom: 12px;
            transition: all 0.2s ease;
            cursor: pointer;
            position: relative;
        }

        .place-card:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(0,0,0,0.08);
            border-color: #0d6efd;
        }

        .place-title {
            font-size: 1.1rem;
            font-weight: 700;
            color: #222;
            margin-bottom: 4px;
        }

        .place-addr {
            font-size: 0.85rem;
            color: #666;
            margin-bottom: 0;
        }

        .btn-focus {
            position: absolute;
            top: 15px;
            right: 15px;
            width: 32px;
            height: 32px;
            border-radius: 50%;
            background-color: #f1f3f5;
            border: none;
            color: #0d6efd;
            display: flex;
            align-items: center;
            justify-content: center;
            transition: background 0.2s;
        }
        .btn-focus:hover { background-color: #e2e6ea; }

        /* [오른쪽] 지도 영역 */
        #right-map-area {
            flex: 1;
            position: relative;
            background-color: #e9ecef; /* 지도 로딩 전 배경색 */
        }

        #baseMap {
            width: 100%;
            height: 100%;
        }

        /* 지도 위 플로팅 컨트롤 (레이어 스위처) */
        #layer-switcher {
            position: absolute;
            top: 20px;
            right: 20px;
            z-index: 999;
            background: rgba(255, 255, 255, 0.95);
            padding: 12px;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.15);
            backdrop-filter: blur(5px);
            min-width: 160px;
        }

        .layer-btn {
            font-size: 0.8rem;
            font-weight: 600;
        }

        /* OpenLayers Popup 커스텀 */
        .ol-popup {
            position: absolute;
            background-color: white;
            box-shadow: 0 5px 15px rgba(0,0,0,0.2);
            padding: 15px;
            border-radius: 10px;
            border: 1px solid #cccccc;
            bottom: 12px;
            left: -50px;
            min-width: 200px;
        }
        .ol-popup:after, .ol-popup:before {
            top: 100%;
            border: solid transparent;
            content: " ";
            height: 0;
            width: 0;
            position: absolute;
            pointer-events: none;
        }
        .ol-popup:after {
            border-top-color: white;
            border-width: 10px;
            left: 48px;
            margin-left: -10px;
        }
        .ol-popup-closer {
            text-decoration: none;
            position: absolute;
            top: 5px;
            right: 10px;
            color: #999;
        }
        .ol-popup-closer:after { content: "✖"; }
</style>
</head>