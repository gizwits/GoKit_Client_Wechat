var timeOutFlag = true;
/**
 * Created by chaoso on 15/6/12.
 */
$(document).ready(function(e) {
    onDeviceQrcode();
    onChangeName();
    onCreateName();
    onCancel();
    onDelete();
    onDeleteBound(); 
});
function onDeviceQrcode() {
    var openId = $('#openId').val();
    $("#qrcodePage")
        .click(
        function() {
            window.location.href = "/Gokitdog/DeviceQrCodeServlet?openId="
                + openId;
        });
}
function onChangeName() {
	
    $("#updatedName").click(function() {
        document.getElementById("changeNameDiv").style.display = 'block';
    });
    $("#updateNameBtn").click(function() {
        document.getElementById("changeNameDiv").style.display = 'block';
    });
    
}
function onCancel() {
    $("#cancelUpdate").click(function() {
        document.getElementById("changeNameDiv").style.display = 'none';
        document.getElementById("deleteDevDiv").style.display = 'none';
    });
    $("#cancelDelete").click(function() {
        document.getElementById("changeNameDiv").style.display = 'none';
        document.getElementById("deleteDevDiv").style.display = 'none';
    });
    
}
/**
 * @synopsis  请求服务器改变名字
 *
 * @param openId   微信用户识别标识
 * @param name     名字
 * @param deviceid 微信设备识别标识
 * @returns
 */
var postChangeName = function(openId, name) {
    var json = {
        openId : openId,
        name : name
    };
    
//    alert(JSON.stringify(json));
    $(".ajaxloading").show();
    timeoutMsg(6000,timeOutFlag);
    $.post("NameServlet", json, function(data) {
    	timeOutFlag = false;
        $(".ajaxloading").hide();
        if (data.flag) {
            alert("修改成功！");
        } else {
            alert("修改失败，请重试！");
        }
        WeixinJSBridge.call('closeWindow');
    });
};
/**
 * @synopsis  点击改变名字按钮
 * @returns
 */
var onCreateName = function() {
    $(".createBtn").click(function(e) {
    	timeOutFlag = true;
        var name = $("#nameText").val();
        var openId = $("#openId").val();
        if (name.trim().length <= 0) {
            alert("名字不能为空！");
            return false;
        }
        if (name.length > 6) {
            alert("名字不能大于6位");
            return false;
        }
        postChangeName(openId, name);
    });
};
var onDelete = function(){
    $(".cancelBind").click(function() {
        document.getElementById("deleteDevDiv").style.display = 'block';
    });
};

var onDeleteBound = function(){
	var json = {
		"delete" : "delete",
		"openId" : $("#openId").val()
	};
	$(".deleteBtn").click(function(){
		timeOutFlag = true;
		
		 $(".ajaxloading").show();
		 timeoutMsg(6000,timeOutFlag);
		 $.post("WowoConfigServlet", json, function(data) {
		        $(".ajaxloading").hide();
		        alert("删除成功");
                WeixinJSBridge.call('closeWindow');
		    });
	});
};
function timeoutMsg(timestamp,timeout){
	setTimeout(function(){
		if(timeout){
			alert("网络状况不太好，请重试");
			$(".ajaxloading").hide();
		}
	}, timestamp);
}

String.prototype.trim = function() {
	  return this.replace(/^\s\s*/, '').replace(/\s\s*$/, '');
};