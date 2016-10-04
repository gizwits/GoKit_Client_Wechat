var wsHost = "ws://m2m.gizwits.com:8080/ws/app/v1";
var websocket;
var xmlHttpRequest;
var heartbeatTimerId;
var _appId;
var _openId;
// == send functions ===================================================
function sendJson(json) 
{
    var data = JSON.stringify(json);
    return sendData(data);
};

function sendData(data) 
{
    if(websocket.readyState == websocket.OPEN){
        websocket.send(data);
        return true;
    } else {
        alert("连接断开，请刷新后重试");
        return false;
    };
};
var websocketConnect = function()
{
	if(websocket != undefined && websocket.readyState == websocket.OPEN){
		disconnect();
		connect();
	}else{
		connect();
	}
}; 

function connect(){
	 websocket = new WebSocket(wsHost);
	    
	 websocket.onopen = function(evt) { onOpen(evt); }; 
     websocket.onclose = function(evt) { onClose(evt); }; 
     websocket.onmessage = function(evt) { onMessage(evt); }; 
     websocket.onerror = function(evt) { onError(evt);};
}

function disconnect() 
{
    websocket.close();
}; 
// == websocket callbacks =============================================
function onOpen(evt) 
{
	console.info("onOpen");
    websocketConnectedCallback();
};

function onClose(evt) 
{
    stopPing();
};

function onMessage(evt) 
{
	console.info("onMessage begin");
    var res = JSON.parse(evt.data);
    switch(res.cmd)
    {
        case "pong":
            break;
        case "login_res":
            var data = res.data;
            login_callback(data);
            break;
        case "s2c_noti":
            var data = res.data;
            noti_callback(data);
        break;
        case "s2c_raw":
//        	var data = res.data;
//            dataraw_callback(data);
            break;
    }
    console.info("onMessage end");
};

function onError(evt) 
{
    alert("连接失败，请重新点击窝窝");
    stopPing();
};
// == operation functions =============================================
function opToggleConn()
{
    if(websocket != undefined && websocket.readyState == websocket.OPEN){
        disconnect();
    } else {
        connect();
    };
};

var onLogin = function(AppId,openId)
{
//    $.cookie('appid', AppId);
//    $.cookie('openId', openId);
	console.info("onLogin beigin");
	_appId = AppId;
	_openId = openId;
    var url = "http://api.gizwits.com/app/users";
    $.ajax(url, {
        type: "POST",
        headers: {"X-Gizwits-Application-Id": AppId},
        data: "{\"phone_id\":\"" + openId + "\"}"
        // data: "{\"username\":\"" + UserName + "\",\"password\":\"" + PassWord + "\"}",
    })
    .done(function(result) {
        var reponseJson=JSON.parse(result);
        var Json = {
            cmd: "login_req",
            data: {
                appid: AppId,
                uid: reponseJson.uid,
                token: reponseJson.token,
                p0_type: "attrs_v4",
                heartbeat_interval: 180
            }
        };
        if (!sendJson(Json))
        {
           alert("连接设备失败，请重新试试 ");
           websocketConnect();
        }
    })
    .fail(function(evt) {
         window.setTimeout(onLogin(_appId,_openId),5000);
    });
    console.info("onLogin end");
};

function opPing()
{
    if (sendJson({cmd: "ping"}))
    {
       console.info("ping");
    }
    else
    {
       console.info("ping error");
    };
};

function opRunAll()
{
    if(websocket != undefined && websocket.readyState == websocket.OPEN)
    {
        disconnect();
        connect();
    }
    else
    {
        connect();
    };
    window.setTimeout(onLogin, 3000);
    window.setTimeout(onPing, 6000);
}
function getDevStatus(did,callback){
	console.info("onGetDev beigin");
    var Json = {
        cmd : "c2s_read",
        data : 
        {
            did : did
        }
    };
    if (sendJson(Json))
    {
       console.info("get dev Status!!");
    }
    else
    {
        websocketConnect();
    };
    //请求状态超时超时

    callback();
    console.info("onGetDev end");
}
var windSpeed = function setWindSpeed(did,speed)
{
    var Json = {
        cmd : "c2s_write",
        data :
        {
            did : did,
            attrs : 
            {
                Motor_Speed : speed
            }
        } 
    };
    if (sendJson(Json))
        {
            $('#result').html('logining...');
        }
        else
        {
        	websocketConnect();
        };
};

function setDehumidifierStatus(did){
    windSpeed(did,-5);
}
function setBlowing(did){
     windSpeed(did,5);
}
function setResetStatus(did){
    windSpeed(did,0);
}
// == helper functions =============================================
function startPing()
{
    var heartbeat = 90;
    heartbeatTimerId = window.setInterval(opPing, heartbeat*1000);
};

function stopPing()
{
    window.clearInterval(heartbeatTimerId);
};
