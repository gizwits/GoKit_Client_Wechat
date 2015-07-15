<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
<title>查看二维码</title>
<link rel="stylesheet" type="text/css" href="css/deviceQrCode.css">
</head>
<body>
	<div class="box" style="overflow: hidden;" id="petList">
		<div class="box box1">
			<img alt="" src="images/homeTop.png" class="home_top">

			<div class="container-fluid">
				
			</div>
			<div class="container-qrcode">
				<img src="${qrcodeLink}" alt="二维码" />
				<p class="macAddr">Mac地址：${macAddr}</p>
			</div>
			<div>
				<span class="font qrcodeTips">
					<span class="font qrcodeTips qrcodeTitle">温馨提示</span><br />
				请长按二维码保存图片,以免丢失
				</span>
			</div>
		</div>
	</div>
	<!--<div class="ajaxloading"><p><img alt="" src="images/loading.png" class="loadImg rotationAnimate">加载中</p></div>-->
	<script type="text/javascript" src="js/jquery-1.8.3.min.js"></script>
</body>
</html>