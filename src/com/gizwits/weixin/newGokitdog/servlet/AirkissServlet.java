package com.gizwits.weixin.newGokitdog.servlet;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;

import com.gizwits.weixin.newGokitdog.GlobalConfig;
import com.gizwits.weixin.newGokitdog.bean.Ticket;
import com.gizwits.weixin.newGokitdog.util.EncryptUtil;
import com.gizwits.weixin.newGokitdog.util.HttpUtil;
import com.gizwits.weixin.newGokitdog.util.WeixinUtil;
import com.google.gson.Gson;

/**
 * Servlet implementation class AirkissServlet
 */
@WebServlet("/AirkissServlet")
public class AirkissServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(AirkissServlet.class);
	Ticket ticket = null;
	String pageUrl;
	String jsapiTicket = "";
	String timestamp = "";
	String nonestr = "";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AirkissServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	//post 方式传进jsp
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		pageUrl = "http://" + GlobalConfig.DomainName + request.getContextPath() +"/AirkissServlet";
		String signture = getSignature(pageUrl);
		request.setAttribute("appId", GlobalConfig.WX_APP_ID);
		request.setAttribute("signture", signture);
		request.setAttribute("nonceStr", nonestr);
		request.setAttribute("timestamp", timestamp);
		request.getRequestDispatcher("/airkiss.jsp").forward(request, response);

	}

	// 处理signature，并且获取ticket
	private String getTicket() {
		boolean first = false;
		HttpUtil httpUtil = null;
		try {
			httpUtil = new HttpUtil(WeixinUtil.getAccessToken());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (ticket == null) {
			ticket = new Ticket();
			first = true;
		}
		long nowMillis = Calendar.getInstance().getTimeInMillis();
		if (nowMillis - ticket.getTime() > 6000000 || first) {
			String access_token = "";
			try {
				access_token = WeixinUtil.getAccessToken();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String url1 = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="
					+ access_token + "&type=jsapi";
			String requstStr = httpUtil.get(url1);
			Map<String, Object> infoMap = extracted(requstStr);
			String sticket = (String) infoMap.get("ticket");
			ticket.setValue(sticket);
			ticket.setTime(Calendar.getInstance().getTimeInMillis());
		}
		return ticket.getValue();
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> extracted(String requstStr) {
		return new Gson().fromJson(requstStr, Map.class);
	}

	// 获取jssdk所需签名
	private String getSignature(String url) {
		nonestr = createNoneStr();
		timestamp = createTimeStamp();
		String ticket = getTicket();
		String signature = "";
		if (ticket != null) {
			String string1 = "jsapi_ticket=" + ticket + "&noncestr=" + nonestr
					+ "&timestamp=" + timestamp + "&url=" + url;
			signature = EncryptUtil.encrypt(string1);
		}

		return signature;
	}
	//生成时间戳
	private String createTimeStamp() {
		return new Date().getTime() / 1000 + "";
	}
	//生成随机字符
	private String createNoneStr() {
		return RandomStringUtils.randomAlphanumeric(36);
	}
}
