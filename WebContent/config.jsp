<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
<title>设置</title>
<link rel="stylesheet" type="text/css" href="css/config.css">
</head>
<body>
	<input type="hidden" value="<%=request.getAttribute("openId")%>"
		name="openId" id="openId">
	<div class="box" style="overflow: hidden;">
		<div class="box box2">
			<img alt="" src="images/homeTop.png" class="home_top">
			<ul>
				<li id="updatedName"><img alt="" src="images/1.png">
					<p class="user">
						<span class="font user_name">${username}家</span> <span
							class="font pet_name">${name}</span>
					</p></li>
				<img src="images/button_name.png"
					style="position: relative; width: 15%; left: 40%; margin-top: -38%;" id="updateNameBtn">

				<li id="qrcodePage">
					<p class="user">
						<span class="font user_name">设备二维码</span>
					</p> <img alt="" src="images/right.png">
				</li>
			</ul>
			<%--<p class="font cancelBind">解除绑定</p>--%>
			<img class='cancelBind' alt="" src="images/button_delete.png" />
		</div>

		<div id="changeNameDiv" class="box box3">
			<ul>
				<li class="changeName">
					<p>您宠物的名字</p>
					<div>
						<input type="text" class="name_input" id="nameText"
							value="${name}">
					</div>
					<div class="controle_btn">
						<img id="cancelUpdate" alt="" src="images/box_button_cancel.png">
						<img class="createBtn" alt="" src="images/box_button_confirm.png">
					</div>
				</li>
			</ul>
		</div>

		<div id="deleteDevDiv" class="box box4">
			<ul>
				<li class="changeName">
					<p>是否删除设备？</p>
					<div class="controle_btn">
						<img id="cancelDelete" alt=""
							src="images/box_button_cancel.png" alt="取消"> <img class="deleteBtn"
							alt="" src="images/box_button_confirm.png" alt="确定">
					</div>
				</li>
			</ul>
		</div>
	</div>
	<div class="ajaxloading">
		<p>
			<img alt="" src="images/loading.png" class="loadImg rotationAnimate">加载中
		</p>
	</div>
	<script type="text/javascript" src="js/jquery-1.8.3.min.js"></script>
	<script type="text/javascript" src="js/knockout-3.0.0.js"></script>
	<script type="text/javascript" src="js/config.js"></script>
</body>
</html>