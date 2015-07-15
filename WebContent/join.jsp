<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
<title>加入</title>
<link rel="stylesheet" type="text/css" href="css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="css/join.css">
</head>
<body>
	<input type="hidden" name="openId" id="openId" value="${openId}">
	<input type="hidden" name="groupId" id="groupId" value="${groupId}">
	<input type="hidden" name="callback" id="callback" value="${callback}">
	<input type="hidden" name="groupName" id="groupName"
		value="${groupName}">
	<input type="hidden" name="clickSum" id=clickSum value="1">
	<div class="box box1">
		<p class="shark">点击右上角直接分享给你的朋友</p>
		<p class="join_introduction">我建立了“${groupName}”，欢迎喵奴们一起加入，我们可以一起帮忙看护喵星人，有空可以一起约出来溜个喵哦。</p>
		<img alt="" src="images/joinBtn.png" class="joinBtn">
	</div>
	<div class="ajaxloading"><p><img alt="" src="images/loading.png" class="loadImg rotationAnimate">正在加载...</p></div>
	<script type="text/javascript" src="js/jquery-1.8.3.min.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			if($('#callback').val()=='1'){
				alert('加入成功');
			}
			
			$('.joinBtn').click(function() {
				window.location.href = "/Gokitdog/JoinServlet?method=joinin&openId="+ $('#openId').val() +"&groupId=" + $('#groupId').val() + "&clickSum=" + $("#clickSum").val();
			});
		});
	</script>
</body>
</html>