// JavaScript Document
var timeOutFlag = true;
$(document).ready(function(e) {
	var openId = $("#openId").val();
	var groupId = null;
	$(".createBtn").click(function(e) {
		var groupName = $(".name_input").val();
		if( groupName.trim() == ""){
			alert("名字不能为空哦！");
			return false;
		}
		if(groupName.length > 6){
			alert("名字不能大于6位");
			return false;
		}
		var json = {
			openId : openId,
			groupName : groupName
		};
		$(".ajaxloading").show();
		setTimeout(function(){
			if(timeOutFlag){
				alert("网络状况不太好，请重试");
				$(".ajaxloading").hide();
			}
		}, 10000);
		$.post("CreateServlet",json,function(data){
			    timeOutFlag = false;
				$(".ajaxloading").hide();
				groupId = data;
				$(".createname").text(groupName);
				$(".ajaxloading").hide();
				$(".box1").animate({
					left : "-100%"
				}, "slow");
				$(".box2").animate({
					left : "0"
				}, "slow");
		});
	});
	$(".invitation").click(function(e) {
		window.location.href = "/Gokitdog/JoinServlet?method=join&openId="+ openId +"&groupId=" + groupId;
	});
});

String.prototype.trim = function() {
	  return this.replace(/^\s\s*/, '').replace(/\s\s*$/, '');
};