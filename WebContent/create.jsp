<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
<title>建立奴星球</title>
<link rel="stylesheet" type="text/css" href="css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="css/create.css">
</head>
<body>
	<input type="hidden" name="openId" id="openId" value="${openId}">
	<input type="hidden" name="openId" id="username" value="${username}">
	<input type="hidden" name="openId" id="petname" value="${petname}">
	<input type="hidden" name="openId" id="temp" value="${temp}">
	<input type="hidden" name="openId" id="humidity" value="${humidity}">
	<div class="box" style="overflow: hidden;">
		<div class="box box1">
			<img alt="" src="images/homeTop.png" class="home_top">
			<p class="font createTitle">给你的奴星球起个名字</p>
			<input type="text" class="name_input">
			<p class="font picTitle">你的窝窝：</p>
			<img alt="" src="images/headPic.png" class="headPic"> <img
				alt="" src="images/createBtn.png" class="createBtn">
		</div>
		<div class="box box2">
			<img alt="" src="images/homeTop.png" class="home_top">
			<p class="font createTips">你的奴星球“<span class="createname"></span>”已经建立，现在只有你一个，快点邀请更多朋友进来吧。</p>
			<ul>
				<li><img alt="" src="images/headPic.png">
					<p class="user">
						<span class="font user_name">${username}</span><span class="font pet_name">${petname}</span>
					</p>
					<p class="temp_wet">
						<span class="font temp">${temp}℃</span><span class="font wet">${humidity}%</span>
					</p></li>
			</ul>
			<p class="font invitation">邀请更多朋友</p>
		</div>
	</div>
	<div class="ajaxloading"><p><img alt="" src="images/loading.png" class="loadImg rotationAnimate">正在加载...</p></div>
	<script type="text/javascript" src="js/jquery-1.8.3.min.js"></script>
	<script type="text/javascript" src="js/create.js"></script>
</body>
</html>