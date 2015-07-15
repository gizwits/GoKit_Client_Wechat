<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
<title>生产二维码</title>
<link rel="stylesheet" type="text/css" href="css/bootstrap.min.css">
<link rel="stylesheet" href="css/qrcodemgr.css" />
<!-- <script type="text/javascript" src="js/jquery-1.8.3.min.js"></script> -->
<script src="http://cdn.bootcss.com/jquery/1.11.2/jquery.min.js"></script>
<script src="http://cdn.bootcss.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>
<script type="text/javascript" src="js/qrcode.js"></script>
<script type="text/javascript" src="js/ajaxfileupload.js"></script>
</head>
<body style="text-align:center;">
<button type="button" class="btn btn-primary qr_text" id="BatchCreateQrcode" style="margin-top:50px;">批量生成</button>

<div class="modal fade" id="uploadfile" data-backdrop="false" data-backdrop="static">
		<div class="modal-dialog" style="margin-top: 50px;">
			<div class="modal-content">
				<!-- header -->
				<div class="modal-header" style="background-color:#2A333D;">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h5 class="modal-title">
						<span style="font-family:Verdana,Microsoft YaHei;font-weight:bold;color:#fff;">批量生成二维码</span>
					</h5>
				</div>
				<!-- body -->
				<div class="modal-body" style="text-align:center;">
					<form>
						<button id="openFile" type="button" class="btn btn-default btn-success" style="width:200px;"  data-toggle="button" onclick="javascript:$('#fileToUpload').trigger('click');" ><span style="font-family:Verdana,Microsoft YaHei;">选择文件</span></button>
						<!-- 表单文件域 -->
						<input id="fileToUpload" type="file" size="45" name="fileToUpload" class="input" onchange="file_change(this.value)" style="display:none;">
						<!-- <span id="success-msg" style="display:none;"></span> -->
					</form>
					<table id="fileInfo" class="table table-bordered " style="display:none;">
						<tbody>
							<tr>
								<th style="font-family:Verdana,Microsoft YaHei; font-size:14px;background-color:#F9F9F9;width:100px;"><a href="javascript:void(0)">文件名称</a></th>
								<td style="font-family:Verdana,Microsoft YaHei; font-size:14px;"><span id="fileName"></span></td>
							</tr>
						</tbody>
					</table>
					<span id="msg" style="color:red; font-size:14px;"></span>					
				</div>
				<div class="modal-footer">
					<button id="FileUploadBtn" type="button" class="btn btn-primary"  data-toggle="button" onclick="uploadFile()"><span style="font-family:Verdana,Microsoft YaHei;">文件上传</span></button>
					<button id="CreateQrcodeInfoBtn" type="button" class="btn btn-primary"  data-toggle="button" onclick="BatchCreateQrcodeByExcel()" style="display:none;"><span style="font-family:Verdana,Microsoft YaHei;">下载二维码</span></button>				
				</div>
				</div>
		</div>
	</div>
	
	<div class="ajaxloading">
		<p>
			<img alt="" src="images/loading.png" class="loadImg rotationAnimate">正在加载...
		</p>
	</div>
</body>
</html>