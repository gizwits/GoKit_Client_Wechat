package com.gizwits.weixin.newGokitdog.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.gizwits.weixin.newGokitdog.GlobalConfig;
import com.gizwits.weixin.newGokitdog.bean.Article;
import com.gizwits.weixin.newGokitdog.bean.Device;
import com.gizwits.weixin.newGokitdog.bean.Groups;
import com.gizwits.weixin.newGokitdog.bean.UserBindingDevice;
import com.gizwits.weixin.newGokitdog.service.BaseXCloudNetworkService.ResponseObject;
import com.gizwits.weixin.newGokitdog.service.GokitdogServiceImpl;
import com.gizwits.weixin.newGokitdog.service.IGokitdogService;
import com.gizwits.weixin.newGokitdog.service.SystemCache;
import com.gizwits.weixin.newGokitdog.service.BaseXCloudNetworkService.ResponseList;
import com.gizwits.weixin.newGokitdog.service.XCloudNetworkService;
import com.gizwits.weixin.newGokitdog.servlet.XingqiuServlet;
import com.gizwits.weixin.newGokitdog.util.BindingThread.BindingCallback;

/**
 *
 * 处理微信POST请求消息类
 *
 *
 */

public class WeixinUtil {
	public enum WeixinRequstType{
		qrCodeScan,Xinqiu,Wowo
	};
	private static Logger logger = Logger.getLogger(WeixinUtil.class);  
	
	public static WeixinRequest WeixinRequsetAnalysis( final HttpServletRequest request) throws Exception {
		WeixinRequest mWeixinRequst =new WeixinRequest();
	
		final String url = "http://" + request.getServerName() + ":"
				+ request.getServerPort() + request.getContextPath();
		
		//解释请求信息
		StringBuffer readBuf = new StringBuffer();
		String line;
		BufferedReader reader = request.getReader();
		while ((line = reader.readLine()) != null) {
			readBuf.append(line);
		}
		String body = new String(readBuf.toString().getBytes("iso-8859-1"),
				"utf-8");
		Map<String, String> map = null;
		map = XmlUtil.xml2Map(body);
		final String msgType = map.get("xml.MsgType");
		final String openId = map.get("xml.FromUserName");
		
		
		mWeixinRequst.setBody(map);
		mWeixinRequst.setOpenId(openId);
		mWeixinRequst.setUrl(url);
		
		//微信请求后的事件
		if (StringUtils.equalsIgnoreCase(msgType, "event")) {

			final String eventKey = map.get("xml.EventKey");
			String event = map.get("xml.Event");

			if (StringUtils.equalsIgnoreCase(event, "subscribe")) {

				if (map.get("xml.EventKey") != null
						&& map.get("xml.EventKey").length() > 0) {
					mWeixinRequst.setRequstType(WeixinRequstType.qrCodeScan);
				}
			} else if (StringUtils.equalsIgnoreCase(event, "unsubscribe")) {

			}
			//扫描后的事件
			else if (StringUtils.equalsIgnoreCase(event, "scan")) {
				mWeixinRequst.setRequstType(WeixinRequstType.qrCodeScan);

			} else if (StringUtils.equalsIgnoreCase(event, "click")) {
				//点击窝窝
				if (eventKey.equalsIgnoreCase("wowo")) {
					// 点击窝窝
					mWeixinRequst.setRequstType(WeixinRequstType.Wowo);
				}
				//点击奴星球
				else if (eventKey.equalsIgnoreCase("xingqiu")) {
					mWeixinRequst.setRequstType(WeixinRequstType.Xinqiu);
				}
			} else if (StringUtils.equalsIgnoreCase(event, "location")) {

			}
		} else if (StringUtils.equalsIgnoreCase(msgType, "text")
				|| StringUtils.equalsIgnoreCase(msgType, "voice")) {

		}

		return mWeixinRequst;
	}
	
	public static void DoQrCodeSan(WeixinRequest wr,BindingCallback listener){
		IGokitdogService gokitSevice = new GokitdogServiceImpl();
		String qrCode = wr.getBody().get("xml.EventKey");
		if(qrCode.contains("qrscene")){
			qrCode = qrCode.split("_")[1];
		}
		
		logger.info("qrCode is "+qrCode);
		
		UserBindingDevice data = new UserBindingDevice();
		data.setOpenId(wr.getOpenId());
		data.setQrCode(qrCode);
		
		data.setIsUsed(true);
		
		AsynTaskQueue.shared().addTask(new BindingThread( data, listener, wr));
		
	}
	
	public static void DoXinqiu(WeixinRequest wr){
		final String url=wr.getUrl();
		final String openId=wr.getOpenId();
		AsynTaskQueue.shared().addTask(new Runnable() {
			@Override
			public void run() {
				try {
					ArrayList<Article> articles = new ArrayList<Article>();

					Article article = new Article();
					article.setTitle("你是猫奴还是汪奴？");
					article.setDescription("点击大图可以创建新的奴星球");
					article.setPicUrl(url.replace(":80", "") + "/images/gou.jpg");
					article.setUrl(url + "/CreateServlet?openId="
							+ openId);
					articles.add(article);

					// 点击奴星球
					IGokitdogService service = new GokitdogServiceImpl();
					ResponseList<Groups> list = service
							.getGroup(openId);

					List<Groups> groups = list.getSuccess();

					for (int i = 0; i < groups.size(); i++) {
						String title = (i + 1) + "、"
								+ groups.get(i).getGroupName()
								+ "   点击查看大家的窝窝";

						Article article1 = new Article();
						article1.setTitle(title);
						article1.setDescription(title);
						article1.setPicUrl(url.replace(":80", "")
								+ "/images/headPic.png");
						article1.setUrl(url
								+ "/XingqiuServlet?groupId="
								+ groups.get(i).getGroupId()
								+ "&openId=" + openId);
						articles.add(article1);
					}

					SendCustomArticlesMsg(openId, articles);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	public static StringBuffer confTextBuf(Map<String, String> map,
			String content) {
		return confTextBuf(map.get("xml.FromUserName"),
				map.get("xml.ToUserName"), content);
	}

	public static StringBuffer confTextBuf(String to, String from,
			String content) {
		StringBuffer sb = new StringBuffer();
		sb.append("<xml><ToUserName><![CDATA[").append(to)
				.append("]]></ToUserName><FromUserName><![CDATA[").append(from)
				.append("]]></FromUserName><CreateTime>")
				.append(System.currentTimeMillis())
				.append("</CreateTime><MsgType><![CDATA[text]]></MsgType>")
				.append("<Content><![CDATA[").append(content)
				.append("]]></Content></xml>");

		return sb;
	}

	public static StringBuffer confArticleBuf(String to, String from,
			ArrayList<com.gizwits.weixin.newGokitdog.bean.Article> articles) {
		StringBuffer sbBuffer = new StringBuffer();

		int articleCount = articles.size();

		sbBuffer.append("<xml><ToUserName><![CDATA[").append(to)
				.append("]]></ToUserName><FromUserName><![CDATA[").append(from)
				.append("]]></FromUserName><CreateTime>")
				.append(System.currentTimeMillis())
				.append("</CreateTime><MsgType><![CDATA[news]]></MsgType>")
				.append("<ArticleCount>").append(articleCount)
				.append("</ArticleCount><Articles>");

		for (com.gizwits.weixin.newGokitdog.bean.Article article : articles) {
			sbBuffer.append("<item><Title><![CDATA[")
					.append(article.getTitle())
					.append("]]></Title><Description><![CDATA[")
					.append(article.getDescription())
					.append("]]></Description><PicUrl><![CDATA[")
					.append(article.getPicUrl())
					.append("]]></PicUrl><Url><![CDATA[")
					.append(article.getUrl()).append("]]></Url></item>");
		}

		sbBuffer.append("</Articles></xml>");

		return sbBuffer;
	}
//建立微信的图文消息
	public static StringBuffer buildArticle(Article article) {
		StringBuffer sb = new StringBuffer();
		sb.append("{\"title\":\"");
		sb.append(article.getTitle());
		sb.append("\",\"description\":\"");
		sb.append(article.getDescription());
		sb.append("\",\"url\":\"");
		sb.append(article.getUrl());
		sb.append("\",\"picurl\":\"");
		sb.append(article.getPicUrl());
		sb.append("\"}");
		return sb;
	}
//http Get
	public static String httpGet(String url) throws Exception {
		ProtocolSocketFactory fcty = new MySecureProtocolSocketFactory();
		Protocol.registerProtocol("https", new Protocol("https", fcty, 443));
		GetMethod method = new GetMethod(url);// 请求地址
		String response = "";
		try {
			method.setRequestHeader("Content-type", "text/xml; charset=utf-8");
			HttpClient httpclient = new HttpClient();// 创建 HttpClient 的实例
			httpclient.executeMethod(method);
			response = method.getResponseBodyAsString();// 返回的内容
			method.releaseConnection();// 释放连接
			response = new String(response.getBytes("iso-8859-1"), "utf-8");
			if (response.indexOf("40001") > -1) {
				getFreshAccessToken();
			}
		} catch (Exception ex) {

		}
		return response;

	}
//http post
	public static String httpPost(String url, String body) throws Exception {
		ProtocolSocketFactory fcty = new MySecureProtocolSocketFactory();
		Protocol.registerProtocol("https", new Protocol("https", fcty, 443));

		PostMethod post = new PostMethod(url);

		post.setRequestBody(body);// 这里添加xml字符串

		// 指定请求内容的类型
		post.setRequestHeader("Content-type", "application/json; charset=utf-8");
		HttpClient httpclient = new HttpClient();// 创建 HttpClient 的实例

		httpclient.executeMethod(post);
		String res = post.getResponseBodyAsString();

		logger.info(res);
		
		post.releaseConnection();// 释放连接

		if (res.indexOf("40001") > -1) {
			getFreshAccessToken();
		}

		return res;
	}
//获取微信accesstoken
	public static String getAccessToken() throws Exception {

		// SystemCache systemCache = SystemCache.getInstance();
		long curTime = System.currentTimeMillis();
		long lastAccessTime = (long) SystemCache.access_token_time;

		boolean isNotTimeout = curTime - lastAccessTime < (7200L * 1000);
		if (StringUtils.isNotBlank((String) SystemCache.access_token)
				&& isNotTimeout) {
			return (String) SystemCache.access_token;
		}

		return getFreshAccessToken();
	}
//刷新 微信accesstoken
	public static String getFreshAccessToken() throws Exception {
		ProtocolSocketFactory fcty = new MySecureProtocolSocketFactory();
		Protocol.registerProtocol("https", new Protocol("https", fcty, 443));
		// 获取access_token
		String urlStr = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
		+GlobalConfig.WX_APP_ID+"&secret="+GlobalConfig.WX_APP_SECRECT;
		GetMethod method = new GetMethod(urlStr);// 请求地址

		method.setRequestHeader("Content-type", "text/xml; charset=utf-8");
		HttpClient httpclient = new HttpClient();// 创建 HttpClient 的实例
		int result = httpclient.executeMethod(method);
		String responsStr = method.getResponseBodyAsString();// 返回的内容
		method.releaseConnection();// 释放连接
		JSONObject json = JSONObject.fromObject(responsStr);
		Object accessObj = json.get("access_token");
		if (null == accessObj) {

			SystemCache.access_token = "";
			SystemCache.access_token_time = 0L;
		} else {
			SystemCache.access_token = accessObj.toString();
			SystemCache.access_token_time = System.currentTimeMillis();
		}
		return SystemCache.access_token;
	}
//发送图文消息
	public static String SendCustomArticlesMsg(String to, ArrayList<Article> articles)
			throws Exception {
		String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="
				+ getAccessToken();

		StringBuffer sb = new StringBuffer();

		sb.append("[");

		for (Article article : articles) {
			sb.append(buildArticle(article));
			sb.append(",");
		}
		sb.append("]");

		String body = "{\"touser\":\"" + to + "\",\"msgtype\":\"news\""
				+ ",\"news\": {\"articles\":" + sb.toString() + "}}";

		return httpPost(url, body);
	}
	//发送普通文字消息
	public static String SendCustomTextMsg(String openId,String content)
			throws Exception {
		String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="
				+ getAccessToken();

		String body = "{\"touser\":\"" + openId + "\",\"msgtype\":\"text\""
				+ ",\"text\": {\"content\":\"" + content.toString() +"\"}}";

		return httpPost(url, body);
	}
//取得名字
	public static String getNickName(String openId) throws Exception {
		
		String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token="
				+ getAccessToken() + "&openid=" + openId;

		String response = httpGet(url);
		JSONObject jsonObject = JSONObject.fromObject(response);
		return jsonObject.getString("nickname");
	}
	//获取微信openId
	public static String requestOpenid(String code) throws HttpException, IOException {
		StringBuffer sb = new StringBuffer();
		sb.append("https://api.weixin.qq.com/sns/oauth2/access_token");
		sb.append("?grant_type=authorization_code");
		sb.append("&appid="+GlobalConfig.WX_APP_ID);
		sb.append("&secret="+GlobalConfig.WX_APP_SECRECT);
		sb.append("&code=" + code);
		String result = "";
		try {
			JSONObject resultObj = JSONObject.fromObject(WeixinUtil.httpPost(
					sb.toString(), ""));
			result = resultObj.getString("openid");
		} catch (Exception e) {
			e.printStackTrace();
			return "-1";
		}

		return result;
	}
	
	//请求oauth2授权
	public static String requestOauth2(String requestURL) throws UnsupportedEncodingException{
		String result="";
		result = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="
				+ GlobalConfig.WX_APP_ID
				+ "&redirect_uri="
				+ URLEncoder.encode(requestURL, "utf-8")
				+ "&response_type=code&scope=snsapi_base#wechat_redirect";
		
		return result;
	}
	
	

}
