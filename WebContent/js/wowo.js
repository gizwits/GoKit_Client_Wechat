// JavaScript Document
var viewModel = new Model();
//参数
var lineElementId ="#temp_wet_line";
var appId = "";
var did;
var deviceId;
var method;
var openId;
var dev_list;
var times = 0;
var temp_ffeng;
var temp_fhumidity;
var f_feng;
var f_humidity;
var flag;
var timeoutFlag;
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
            $(".ajaxloading").hide();
            flag = 1;
        });
        window.setTimeout(function(){
            $(".ajaxloading").hide();
        }, 3000);
    },
    nextpage : function(openId,deviceId,method){
        if(this.current_page < 0){
            this.current_page ++;
            $(".ajaxloading").show();
            onPostHistory(openId,deviceId,method,this.current_page,function(data){
                onDrawChart(data);
                $(".ajaxloading").hide();
                flag = 1;
            });
            window.setTimeout(function(){
                $(".ajaxloading").hide();

            }, 3000);
        }
    }
};
/**
 * @synopsis  页面DOM结构绘制完毕后
 * @returns
 */
$(document).ready(
		function(e) {
			openId = $("#openId").val();
			deviceId = $("#deviceId").val();
			did = $("#did").val();
		    method = "history";
		    
		    if(deviceId==null||deviceId.length == 0){
		    	alert('没有设备');
                window.location.href = "../Gokitdog/noDevice.jsp";
		    	return;
		    }
			$(".ajaxloading").show();
			flag = 0;
			setTimeout(function(){
				if(flag == 0){
					alert("网络不稳定或者设备不在线");
					(".ajaxloading").hide();
					getDevStatusTimeout();
				}
			},11000);
		    
		    //绑定事件knockout
			ko.applyBindings(viewModel.petNest, document.getElementById('pet'));
			//链接websocket
			websocketConnect();
			//先从服务器上访问历史数据并且绘制图表
			onPostHistory(openId,deviceId,method,0,function(data){
				onDrawChart(data);
			});
			//监听事件
			onSliderUp();
			onSliderDown();
			onNextPage();
			onPrePage();
	});
// ---------------------------- 回调方法 ----------------------------- // 
/**
 * @synopsis  登陆websocket回调
 *
 * @param data 回调的数据
 * @returns
 */
var login_callback = function(data){
    if(data.success == true)
    {
        getDevStatus(did,function(){
            timeoutFlag = true;
            setTimeout(getDevStatusTimeout,7000);
        });
    }
};
/**
 * @synopsis  连接websocket的回调
 *
 * @param data 回调的数据
 * @returns
 */
var websocketConnectedCallback = function(){
	window.setTimeout(function(){
		 onLogin(appId,openId);
	}, 3000);
	window.setTimeout(startPing, 6000);
};

var noti_callback = function(data){
	times=0;
    var statusList = data.attrs;
    if(data.did != did){
    	return;
    }
    var temp = statusList.Temperature;
    var motor = statusList.Motor_Speed;
    var humidity = statusList.Humidity;
    var infrared = statusList.Infrared;
 
    var f_inrared = infrared ? $("#petName").val()+"在窝里" : $("#petName").val()+"走出了窝窝";
    var f_tempDes = temp > 25 ? "热" : "舒适";
    var f_humidityDes = humidity > 70 ? "过湿" : "舒适";

    temp_ffeng = motor > 0  ? true : false;
    temp_fhumidity = motor < 0  ? true : false;

    /**
     * 防抖震
     */
    if(times == 0){
        f_feng =  motor > 0  ? true : false;
        f_humidity = motor < 0  ? true : false;
        times = 1;
        setDeviceStatus(temp,f_tempDes,humidity,f_humidityDes,f_feng,f_humidity,f_inrared);
        viewModel.loadpet();
    }
    if(times == 1){
    	times = 2;
        window.setTimeout(function(){
            if(temp_ffeng == f_feng && temp_fhumidity == f_humidity){
                times = 3;
            }
        },1000);
    }
    if(times == 2){
    	return;
    }
    if(times == 3){
    	setDeviceStatus(temp,f_tempDes,humidity,f_humidityDes,f_feng,f_humidity,f_inrared);
    	viewModel.loadpet();
    }

    


};

// ----------------获取状态超时-------------------//
/**
 * @synopsis  获取设备状态超时
 *
 * @returns
 */
var getDevStatusTimeout = function(){
    if(timeoutFlag){
    	alert("设备不在线");
        $(".ajaxloading").hide();
        var temp = 0;
        var humidity = 0;
        var infrared = false;
        var f_inrared = infrared ? $("#petName").val()+"在窝里" : $("#petName").val()+"走出了窝窝";
        var f_tempDes = temp > 25 ? "热" : "舒适";
        var f_humidityDes = humidity > 70 ? "过湿" : "舒适";
        var f_feng = false;
        var f_humidity = false;

        setDeviceStatus(temp,f_tempDes,humidity,f_humidityDes,f_feng,f_humidity,f_inrared);
        viewModel.loadpet();
        flag = 1;
        timeoutFlag = false;
    }

};

//----------------------- 设备控制 -----------------//
/**
 * @synopsis  指令1 设置风速
 *
 * @param air 风速
 * @returns
 */
function setCommand1(air) {
    var cmd = 0;
    if(air){
        viewModel.petNest.air("true");
        viewModel.petNest.dehumidifier("false");
        setBlowing(did);
    }else{
        viewModel.petNest.air("false");
        setResetStatus(did);
    }
}
/**
 * @synopsis  指令1 设置抽湿
 *
 * @param dehumidifier 抽湿风速
 * @returns
 */
function setCommand2(dehumidifier) {
    if(dehumidifier){
        viewModel.petNest.dehumidifier("true");
        viewModel.petNest.air("false");
        setDehumidifierStatus(did);
    }else{
        viewModel.petNest.dehumidifier("false");
        setResetStatus(did);
    }
}
/**
 * @synopsis  设置页面上的值
 *
 * @returns
 */
var setDeviceStatus = function(temp,tempDes,humidity,humidityDes,air,dehumidifier,infrared){
    $(".ajaxloading").hide();
    flag = 1;
    timeoutFlag = false;
    $("#tips").val(infrared);
    $("#temp").val(temp);
    $("#humidity").val(humidity);
    $("#tempComfort").val(tempDes);
    $("#wetComfort").val(humidityDes);
    $("#air").val(air);
    $("#dehumidifier").val(dehumidifier);
};

//------------------------ 事件 -------------------------//
/**
 * @synopsis  点击滚动下拉
 *
 * @returns
 */
var onSliderDown = function(){
    $(".sliderDown").click(function(e) {
        $(".box1").animate({
            top : "-100%"
        }, "slow");
        $(".box2").animate({
            top : "0"
        }, "slow");
    });
};
/**
 * @synopsis  点击滚动上拉
 *
 * @returns
 */
var onSliderUp = function() {
    $(".sliderUp").click(function(e) {
        $(".box2").animate({
            top : "100%"
        }, "slow");
        $(".box1").animate({
            top : "0"
        }, "slow");
    });
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
	var tempList = data.Temperature;
	var humidityList = data.Humidity;
	var xAxisTimeList = ["00:00", "04:00","08:00","12:00", "16:00", "20:00"];
    drawLine(xAxisTimeList,
    		tempList, humidityList,
        function(data){
            lineDapte(lineElementId);
            drawingGraphs(lineElementId, data);
        });
    $(".dateTime").text(data.Date);
};

function readValWith(key,list){
    return list(key);
}
Array.prototype.insert = function (index, item) {  
	  this.splice(index, 0, item);  
};