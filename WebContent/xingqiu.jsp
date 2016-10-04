<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
<title>奴星球</title>
<link rel="stylesheet" type="text/css" href="css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="css/xingqiu.css">

</head>
<body>
	<input type="hidden" name="openId" id="groupId" value="${groupId}">
	<input type="hidden" name="devices" id="devices" value="${devices}">
	<div class="box" id="petList">
		<div class="box box1">
			<img alt="" src="images/homeTop.png" class="home_top">
			
			<p class="font invitation">邀请更多朋友</p>
			<ul data-bind="foreach:petList">
				<li data-bind="click:function(){drawLineData(openId,deviceId);}"><img alt="" src="images/headPic.png">
					<p class="user">
						<span class="font user_name" data-bind="text:username"><!-- 家家爱 --></span><span class="font pet_name" data-bind="text:petname">野狼</span>
					</p>
					<p class="temp_wet">
						<span class="font temp" data-bind="text:temp+'℃'"><!-- 36℃ --></span><span class="font wet" data-bind="text:humidity+'%'">35%</span>
					</p></li>
			</ul>
			
		</div>
		<div class="box box3">
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
	<div class="ajaxloading"><p><img alt="" src="images/loading.png" class="loadImg rotationAnimate">正在加载...</p></div>
	<script type="text/javascript" src="js/jquery-1.8.3.min.js"></script>
	<script type="text/javascript" src="js/knockout-3.0.0.js"></script>
	<script type="text/javascript" src="js/model.js"></script>
	<script type="text/javascript" src="js/Chart.js"></script>
	<script type="text/javascript" src="js/line.js"></script>
	<script type="text/javascript">
		var device;
		$(document).ready(function(e){
			device = ${devices};
		});
	</script>
	<script type="text/javascript" src="js/xingqiu.js"> </script>
</body>
</html>