<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
<title>修改窝窝名字</title>
<link rel="stylesheet" type="text/css" href="css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="css/name.css">
</head>
<body>
	<input type="hidden" name="openId" id="openId" value="${openId}">
	<input type="hidden" name="deviceId" id="deviceId" value="${deviceId}">
	<div class="box" style="overflow: hidden;" id="deviceList">
		<div class="box box1">
			<img alt="" src="images/homeTop.png" class="home_top">
			<div class="pre_next">
				 <img alt="" src="images/1.png" class="headPic">
			</div>
			<p class="font createTitle">窝窝名称</p>
			<input type="text" class="name_input"> 
			<img alt="" src="images/createBtn.png" class="createBtn">
		</div>
	</div>
	<div class="ajaxloading">
		<p>
			<img alt="" src="images/loading.png" class="loadImg rotationAnimate">正在加载...
		</p>
	</div>
	<script type="text/javascript" src="js/jquery-1.8.3.min.js"></script>
	<script type="text/javascript" src="js/knockout-3.0.0.js"></script>
	<script type="text/javascript" src="js/model.js"></script>
	<script type="text/javascript" src="js/name.js"></script>
</body>
</html>