<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
<title>窝窝</title>
<link rel="stylesheet" type="text/css" href="css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="css/wowo.css">
</head>
<body>
	<input type="hidden" name="openId" id="openId" value="${openId}">
	<input type="hidden" name="deviceId" id="deviceId" value="${deviceId}">
	<input type="hidden" name="did" id="did" value="${did}">
	<input type="hidden" name="des" id="tips" value="${des}">
	<input type="hidden" name="temp" id="temp" value="1">
	<input type="hidden" name="humidity" id="humidity" value="${humidity}">
	<input type="hidden" name="tempDes" id="tempComfort" value="${tempDes}">
	<input type="hidden" name="humidityDes" id="wetComfort" value="${humidityDes}">
	<input type="hidden" name="feng" id="air" value="${feng}">
	<input type="hidden" name="shi" id="dehumidifier" value="${shi}">
	<input type="hidden" name="petName" id="petName" value="${petName}">

	<div class="box" style="overflow: hidden;" id="pet">
		<div class="box box1">
			<img alt="" src="images/homeTop.png" class="home_top">
			<div class="pet_info">
				<img alt="" src="images/headPic.png">
				<p class="font" data-bind="text:tips"></p>
			</div>
			<div class="home_temp">
				<img alt="" src="images/temp.png"> <span class="font temp" data-bind="text:temp"></span>
				<span class="font feeling" data-bind="text:tempComfort"></span>
			</div>
			<div class="home_wet">
				<img alt="" src="images/wet.png"> <span class="font wet" data-bind="text:wet"></span>
				<span class="font feeling" data-bind="text:wetComfort"></span>
			</div>
			<div class="controle_btn">
				<img id='btn1' alt="" src="images/airBtn.png"  data-bind="visible:air()=='false',click:function(){setCommand1(true);}">
				<img id='btn2' alt="" src="images/airBtnActive.png"  data-bind="visible:air()=='true',click:function(){setCommand1(false);}"> 
				<img id='btn3' alt="" src="images/dehumidifierBtn.png"  data-bind="visible:dehumidifier()=='false',click:function(){setCommand2(true);}">
				<img id='btn4' alt="" src="images/dehumidifierBtnActive.png"  data-bind="visible:dehumidifier()=='true',click:function(){setCommand2(false);}">
			</div>
			<img alt="" src="images/next.png" class="sliderDown">
		</div>
		<div class="box box2">
			<img alt="" src="images/homeTop.png" class="home_top"> <img
				alt="" src="images/pre.png" class="sliderUp">
			<div class="line">
				<canvas id="temp_wet_line" width="310px" height="260"></canvas>
			</div>
			<div class="here">
				<img alt="" src="images/here1.png" style="margin-left: 2%;"> <img
					alt="" src="images/here2.png" style="margin-left: 50%;">
			</div>
			<div class="pre_next">
				<img alt="" src="images/left.png" class="pre">
				<p class="font pages">
					<span class="dateTime"></span>
				</p>
				<img alt="" src="images/right.png" class="next">
			</div>
		</div>
	</div>
	<%--<div class="box tips">--%>
		<%--<img alt="" src="images/no_device.jpg" class="device_img">--%>
		<%--<p >尚未绑定设备<br>请先用微信扫一扫说明书上的二维码</p>--%>
	<%--</div>--%>
	<div class="ajaxloading"><p><img alt="" src="images/loading.png" class="loadImg rotationAnimate">正在加载...</p></div>
	<script type="text/javascript" src="js/jquery-1.8.3.min.js"></script>
	<script type="text/javascript" src="js/knockout-3.0.0.js"></script>
	<script type="text/javascript" src="js/gokitWebsocket.js"></script>
	<script type="text/javascript" src="js/model.js"></script>
	<script type="text/javascript" src="js/Chart.js"></script>
	<script type="text/javascript" src="js/line.js"></script>
	<script type="text/javascript" src="js/wowo.js"></script>

</body>
</html>