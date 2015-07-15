<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title>没有设备</title>
    <style>
        *{ padding:0; margin:0; }
        .font {
            font-family:"微软雅黑";
            text-align:center;
            font-weight:bolder;
            margin-top:10px;
            font-size: 5em;
        }
        .box{
            position:absolute;
            width:100%;
            height:100%;
            background-color:#fff2e5;
            text-align:center;
        }
        .tips{
            background-color:gray;

        }

        .device_img{
            width: 100%;
            padding: 10px;
        }

        .tips p{
            margin-top:50px;
        }

    </style>
</head>
<body>
<div class="box tips">
    <img alt="" src="images/no_device.jpg" class="device_img">

    <p class="font">尚未绑定设备<br>请先用微信扫一扫说明书上的二维码</p>
</div>

</body>
</html>