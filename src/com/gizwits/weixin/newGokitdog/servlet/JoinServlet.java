package com.gizwits.weixin.newGokitdog.servlet;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.gizwits.weixin.newGokitdog.GlobalConfig;
import com.gizwits.weixin.newGokitdog.bean.Device;
import com.gizwits.weixin.newGokitdog.bean.Groups;
import com.gizwits.weixin.newGokitdog.service.GokitdogServiceImpl;
import com.gizwits.weixin.newGokitdog.service.IGokitdogService;
import com.gizwits.weixin.newGokitdog.service.XCloudNetworkService;
import com.gizwits.weixin.newGokitdog.service.BaseXCloudNetworkService.ResponseObject;
import com.gizwits.weixin.newGokitdog.service.XCloudNetworkService;
import com.gizwits.weixin.newGokitdog.util.WeixinUtil;

/**
 * Servlet implementation class JoinServlet
 */
@WebServlet("/JoinServlet")
public class JoinServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	XCloudNetworkService networkService = new XCloudNetworkService();
	IGokitdogService gokitService = new GokitdogServiceImpl();

	
	public JoinServlet() {
		super();
	}


	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		//获取相应字段
		String openId = StringUtils.trimToEmpty(request.getParameter("openId"));
		String currentOpenId = StringUtils.trimToEmpty(request
				.getParameter("openId"));
		String groupId = StringUtils.trimToEmpty(request
				.getParameter("groupId"));
		String callback = StringUtils.trimToEmpty(request
				.getParameter("callback"));
		String code = StringUtils.trimToEmpty(request.getParameter("code"));
		String method = StringUtils.trimToEmpty(request.getParameter("method"));
		String clickSum = StringUtils.trimToEmpty(request
				.getParameter("clickSum"));

		// 判断点击次数
		int int_clickSum;
		if (clickSum != "") {
			int_clickSum = Integer.parseInt(clickSum);
		} else {
			int_clickSum = 1;
		}
		int_clickSum++;
		// 判断是否跳转页面
		if (StringUtils.equals(method, "join")) {
			// 跳转页面
			IGokitdogService service = new GokitdogServiceImpl();
			ResponseObject<Groups> list = service.getGroup(openId,
					Integer.parseInt(groupId));
			Groups groups = list.getSuccess();
			response.setContentType("type=text/html; charset=UTF-8");
			request.setAttribute("openId", currentOpenId);
			request.setAttribute("groupId", groupId);
			request.setAttribute("groupName", groups.getGroupName());
			request.setAttribute("callback", callback);
			request.setAttribute("clickSum", clickSum);

			request.getRequestDispatcher("join.jsp").forward(request, response);
		} else if (StringUtils.equals(method, "joinin")) {
			// 加入奴星球
			if (callback.equals("1") && !code.equals("")) {
				try {
					currentOpenId = WeixinUtil.requestOpenid(code);
					ResponseObject respone = gokitService.joinGroup(
							currentOpenId, groupId);
				} catch (Exception e) {
					System.out.print(e);
				}

				ResponseObject<Groups> list = gokitService.getGroup(openId,
						Integer.parseInt(groupId));
				Groups groups = list.getSuccess();
				response.setContentType("type=text/html; charset=UTF-8");
				request.setAttribute("openId", openId);
				request.setAttribute("groupId", groupId);
				request.setAttribute("groupName", groups.getGroupName());
				request.setAttribute("callback", callback);
				request.setAttribute("clickSum", int_clickSum);

				request.getRequestDispatcher("join.jsp").forward(request,
						response);
			} else {
				StringBuffer requestURL = request.getRequestURL();
				String queryString = request.getQueryString();
				if (queryString != null) {
					requestURL.append("?").append(queryString);
					requestURL.append("&callback=1");
				}
				String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="
						+ GlobalConfig.WX_APP_ID
						+ "&redirect_uri="
						+ URLEncoder.encode(requestURL.toString(), "utf-8")
						+ "&response_type=code&scope=snsapi_base#wechat_redirect";
				try {
					response.sendRedirect(url);
				} catch (IOException e) {
					System.out.print(e);
				}

				return;
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		StringBuffer requestURL = request.getRequestURL();
		String queryString = request.getQueryString();
		if (queryString != null) {
			requestURL.append("?").append(queryString);
			requestURL.append("&callback=1");
		}

		String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="
				+ GlobalConfig.WX_APP_ID
				+ "&redirect_uri="
				+ URLEncoder.encode(requestURL.toString())
				+ "&response_type=code&scope=snsapi_base#wechat_redirect";

		try {
			response.sendRedirect(url);
		} catch (IOException e) {
			System.out.print(e);
		}

		return;
	}

}
