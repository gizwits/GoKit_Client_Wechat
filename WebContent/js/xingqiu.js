// JavaScript Document
var viewModel = new Model();
var lineElementId = "#temp_wet_line";
var openId;
var deviceId;
var method;
/**
* @synopsis  翻页 上一页  下一页
*
* @param openid 微信唯一的身份识别
* @param deviceid 微信的机器识别
* @param method 传递给wowoservlet的标识
* @returns
*/
var pages = {
	current_page : 0,
	prepage : function(openId,deviceId,method){
		this.current_page --;
		if(this.current_page < -2){
			this.current_page = -2;
			return;
		}
		$(".ajaxloading").show();
		onPostHistory(openId,deviceId,method,this.current_page,function(data){
			onDrawChart(data);
		});
	},
	nextpage : function(openId,deviceId,method){
		if(this.current_page < 0){
			this.current_page ++;
			$(".ajaxloading").show();
			onPostHistory(openId,deviceId,method,this.current_page,function(data){
				onDrawChart(data);
			});
		}
	}
};
function drawLineData(openId,deviceId){
	console.info("openId:"+openId);
	console.info("deviceId:"+deviceId);
	pages.current_page = 0;
	var historyJSON = {
			openId : openId,
			method : "history",
			deviceId : deviceId
		};
	$(".ajaxloading").show();
	$.post("WowoServlet",historyJSON,function(data){
		drawLine([ "00:00", "04:00", "08:00", "12:00", "16:00", "20:00" ],
				data.Temperature, data.Humidity,
				function(data) {
					lineDapte(lineElementId);
					drawingGraphs(lineElementId, data);
					$(".ajaxloading").hide();
					$(".box1").animate({
						left : "-100%"
					}, "slow");
					$(".box3").animate({
						left : "0"
					}, "slow");
				});
		$(".dateTime").text(data.Date);
	});
}
/**
* @synopsis  页面DOM结构绘制完毕后
* @returns
*/
$(document).ready(
		function(e) {
	ko.applyBindings(viewModel.petNest, document.getElementById('petList'));
	method = 'history';
	viewModel.petNest.petList(device);
	openId = device[0].openId;
	deviceId =  device[0].deviceId;
	onInvitation();
	onSliderUp();
	onNextPage();
	onPrePage();
	
});
var onInvitation = function(){
	$(".invitation").click(function(e) {
		window.location.href = "/Gokitdog/JoinServlet?method=join&openId="+ device[0].openId +"&groupId=" + $("#groupId").val() + "&clickSum=1";
	});
}

/**
* @synopsis  点击滚动上拉
*
* @returns
*/
var onSliderUp = function() {
	$(".sliderUp").click(function(){
		$(".box1").animate({
			left : "0"
		}, "slow");
		$(".box3").animate({
			left : "100%"
		}, "slow");
	});
};
/**
* @synopsis  请求历史数据
*
* @param openId 微信用户识别标识
* @param deviceid 微信设备识别标识
* @param method 传递到后台的方法标识
* @param method 页数
* @param method 回调方法
* @returns
*/
var onPostHistory = function (openId,deviceId,method,flag,callback){
	var historyJSON = {
			openId : openId,
			method : method,
			deviceId : deviceId,
			flag : flag
	};
	$.post("WowoServlet",historyJSON,function(data){
		dev_list = data;
		callback(data);
	});
};
/**
* @synopsis  画图表
*
* @param data 回调的数据
* @returns
*/
var onDrawChart = function (data){
 	drawLine(["00:00", "04:00", "08:00", "12:00", "16:00", "20:00"],
 			data.Temperature, data.Humidity,
 			function(data){
 				lineDapte(lineElementId);
 				drawingGraphs(lineElementId, data);
 	});
 	$(".dateTime").text(data.Date);
 	$(".ajaxloading").hide();
};
/**
* @synopsis  点击到下一页
*
* @returns
*/
var onNextPage = function() {
	$(".next").click(function(){
			pages.nextpage(openId,deviceId,method);
	});
};
/**
* @synopsis  点击到上一页
*
* @returns
*/
var onPrePage = function() {
	$(".pre").click(function(){
			pages.prepage(openId,deviceId,method);
			
	});
};
