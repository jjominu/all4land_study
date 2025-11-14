<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"> 
    <title>baseMapSample</title>  
	<script type="text/javascript" src="js/jquery-3.1.1.min.js"></script>
	<script type="text/javascript" src="js/Openlayers3/ol.js"></script>
	<script type="text/javascript" src="js/Openlayers3/proj4.js"></script>
	<script type="text/javascript" src="js/Openlayers3/transCoord.js"></script>
        <script type="text/javascript" src="https://www.khoa.go.kr/oceanmap/BASEMAP_RLTM3857/otmsVectormapApi.do?ServiceKey=F062497BC44DD448FDAE699EE
&version=2"></script>
        <style>
        .info-popup {
                position: fixed;
                right: 32px;
                top: 96px;
                width: 320px;
                max-width: calc(100% - 40px);
                background: #fff;
                border: 1px solid #dadce0;
                border-radius: 10px;
                box-shadow: 0 10px 25px rgba(0, 0, 0, 0.15);
                display: none;
                z-index: 1000;
        }

        .info-popup.open {
                display: block;
        }

        .info-popup header {
                display: flex;
                align-items: center;
                justify-content: space-between;
                padding: 12px 16px;
                border-bottom: 1px solid #efefef;
                font-weight: 600;
                font-size: 15px;
        }

        .info-popup header button {
                border: none;
                background: transparent;
                font-size: 18px;
                cursor: pointer;
                padding: 0;
                color: #6b7280;
        }

        .info-popup header button:hover {
                color: #111827;
        }

        .info-popup .popup-body {
                padding: 14px 16px 18px 16px;
                font-size: 14px;
                line-height: 1.4;
                color: #1f2937;
                max-height: 300px;
                overflow-y: auto;
        }

        .info-popup .popup-body ul {
                margin: 8px 0 0 12px;
                padding: 0;
        }

        .info-popup .popup-body li {
                margin-bottom: 4px;
        }

        .info-popup .popup-body strong {
                color: #2563eb;
        }

        .info-popup .popup-body .popup-sample {
                margin-top: 12px;
                padding-top: 8px;
                border-top: 1px solid #e5e7eb;
        }

        .info-popup .popup-body .popup-sample p {
                margin: 0 0 6px 0;
                font-weight: 600;
        }
        </style>
        <script type="text/javascript" src="js/Openlayers3/study.js"></script>
  </head>
<body>
	<div id="baseMap" class="baseMap"style="width:100%; height:600px; position:relative;"></div>	
	<div class="map-toolbar">
  <label><input type="checkbox" id="chkWms"> WMS</label>
  <label><input type="checkbox" id="chkWfs" checked> WFS</label>
</div>
<label style="margin-left:12px">
    클릭 타입
    <select id="type">
      <option value="click" selected>Click</option>
      <option value="singleclick">SingleClick</option>
      <option value="pointermove">PointerMove</option>
    </select>
  </label>
</div>	

<p id="featInfo" class="text-start"></p>
<div id="popup" class="info-popup" role="dialog" aria-modal="true" aria-labelledby="popup-title">
        <header>
                <span id="popup-title">WFS 정보</span>
                <button type="button" id="popup-close" aria-label="닫기">×</button>
        </header>
        <div id="popup-content" class="popup-body">
                <p>WFS 레이어 정보가 여기에 표시됩니다.</p>
        </div>
</div>
 </body>
 </html>
