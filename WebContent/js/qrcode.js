var JsonResult="";
$(function(){
	$("#BatchCreateQrcode").bind("click",function(){
		$("#uploadfile").modal("show");
		/*隐藏文件信息，显示上传按钮，隐藏提示框，将按钮禁用解除*/
		$("#fileInfo").hide();
		$("#openFile").show();
		$("#msg").empty();
		
		var file = $("#fileToUpload"); 
		file.after(file.clone().val("")); 
		file.remove(); 
		
		$("#FileUploadBtn").removeClass("disabled");
		/*清空JsonResult字符串*/
		JsonResult="";
	});
});
//上传文件
function uploadFile(){	
	if($("#fileToUpload").val()==""){
		alert("请先选择文件!","系统信息");
	}else{
		$(".ajaxloading").show();
		$.ajaxFileUpload
		({
				url:"UploadServlet",
				secureuri:false,
				fileElementId:'fileToUpload',
				dataType: 'JSON',
				success: function (data, status)
				{
					$(".ajaxloading").hide();
					JsonResult=data;
					var json=$.parseJSON(data);
					if(json.flag){
						$("#msg").html("<span style=\"color:green\">上传成功</span>");	
						$("#FileUploadBtn").addClass("disabled");
						$("#CreateQrcodeInfoBtn").show();
					}else{
						$("#msg").html("<span style=\"color:red\">"+json.msg+"</span>");
						$("#openFile").show();
						$("#fileInfo").hide();
						//$("#msg").empty();
						//$("#FileUploadBtn").removeClass("disabled");
					}								
				},
				error: function (data, status, e)
				{
					$(".ajaxloading").hide();
					alert("上传文件出现问题!","系统信息");
				}
			});
	}
	return false;
}

function BatchCreateQrcodeByExcel(){
	var Json;	

	if(JsonResult==""){
		alert("请先上传文件","系统提示");
		return false;
	}else{
		Json=$.parseJSON(JsonResult);
	}		
	if(Json.flag){
		var file_name=Json.filename;	
		
		window.open("UploadServlet?method=download&filename="+file_name);
		
	}else{
		alert("请先上传文件!","系统信息");
	}	
}

function file_change(value){
	var fileName=value.substring(value.lastIndexOf("\\")+1,value.length);
	$("#fileName").text(fileName);
	$("#openFile").hide();
	$("#fileInfo").show();
}