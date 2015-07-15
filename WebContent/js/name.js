// JavaScript Document
var viewModel = new Model();
var deviceId;
var current = 1;
/**
* @synopsis  页面DOM结构绘制完毕后
* @returns
*/
$(document).ready(function(e) {

	onCreateName();
	onLeftPage();
	onRightPage();
});
/**
* @synopsis  请求服务器改变名字
*
* @param openId   微信用户识别标识
* @param name     名字
* @param deviceid 微信设备识别标识
* @returns
*/
var postChangeName = function(openId,name,deviceId){
	var json = {
		openId : openId,
		name : name,
		deviceId : deviceId
	};
	$(".ajaxloading").show();
	$.post("NameServlet", json, function(data) {
		$(".ajaxloading").hide();
		if(data.flag){
			alert("修改成功！");
		}else{
			alert("修改失败，请重试！");
		}
		
		WeixinJSBridge.call('closeWindow');
	});
};
/**
* @synopsis  点击改变名字按钮
* @returns
*/
var onCreateName = function(){
	$(".createBtn").click(function(e) {
		var name = $(".name_input").val();
		var deviceId = $("#deviceId").val();
		var openId = $("#openId").val();
		if (name.trim() == "") {
			alert("名字不能为空！");
			return false;
		}
		if(name.length > 6){
			alert("名字不能大于6位");
			return false;
		}
		postChangeName(openId,name,deviceId);
	});
}
/**
* @synopsis  向左翻页
* @returns
*/
var onLeftPage = function(){
	$('.left').click(function(e) {
		if (current == 1)
			return;
		current--;
		$('.headPic').attr('src', "images/" + current + ".png");
	});
};
/**
* @synopsis  向右翻页
* @returns
*/
var onRightPage = function(){
	$('.right').click(function(e) {
		if (current == 6)
			return;
		current++;
		$('.headPic').attr('src', "images/" + current + ".png");
	});
};

String.prototype.trim = function() {
	  return this.replace(/^\s\s*/, '').replace(/\s\s*$/, '');
};
