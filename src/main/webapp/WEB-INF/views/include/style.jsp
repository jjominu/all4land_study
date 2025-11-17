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
#map-wrapper {
    display: flex;
    width: 100%;
    height: calc(100vh - 60px);   
    margin-top: 60px;             
    overflow: hidden;
}

#left-panel {
    width: 350px;
    background: #fff;
    border-right: 1px solid #ddd;
    overflow-y: auto;
    padding: 15px;
}

#right-map-area {
    flex: 1;
    position: relative;
}

#baseMap {
    width: 100%;
    height: 100%;
}

#layer-switcher {
    position: absolute;
    top: 15px;
    right: 15px;
    background: white;
    padding: 8px;
    border-radius: 6px;
    box-shadow: 0 2px 8px rgba(0,0,0,0.25);
}

.layer-btn {
    display: block;
    padding: 6px 10px;
    margin-bottom: 5px;
    border: 1px solid #ccc;
    background: white;
    cursor: pointer;
    border-radius: 4px;
}
.layer-btn:hover {
    background: #f2f2f2;
}

.ol-popup {
  position: absolute;
  background-color: white;
  padding: 10px;
  border-radius: 4px;
  border: 1px solid #cccccc;
  bottom: 50px;
  left: -300px;
  min-width: 500px;
  min-height: 190px;
}
</style>
</head>