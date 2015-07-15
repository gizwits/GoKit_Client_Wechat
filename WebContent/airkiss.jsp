<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
<title>机智云Airkiss配置</title>
<link rel="stylesheet" type="text/css" href="css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="css/airkiss.css">
</head>
<body>
	<input type="hidden" name="signture" id="signture" value="${signture}">
	<input type="hidden" name="timestamp" id="timestamp"
		value="${timestamp}">
	<input type="hidden" name="nonceStr" id="nonceStr" value="${nonceStr}">
	<input type="hidden" name="appId" id="appId" value="${appId}">
	<div class="box" style="overflow: hidden;">
		<img alt="" src="images/gitwislogo.png" class="logo">

		<div style="margin-top: 10%">此操作将为你的设备配置WiFi网络,</div>
		<div>请将手机接入可用WiFi并在下一步操作中</div>
		<div>输入此WiFi密码</div>
	
		<img alt="" src="images/wifibtn.png" class="connectWifi">
		
	</div>
	<script type="text/javascript" src="js/jquery-1.8.3.min.js"></script>
	<script type="text/javascript"
		src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
	<script type="text/javascript" src="js/airkiss.js"></script>
</body>
</html>