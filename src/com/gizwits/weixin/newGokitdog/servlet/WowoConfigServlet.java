package com.gizwits.weixin.newGokitdog.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.gizwits.weixin.newGokitdog.bean.UserBindingDevice;
import com.gizwits.weixin.newGokitdog.service.GokitdogServiceImpl;
import com.gizwits.weixin.newGokitdog.service.IGokitdogService;
import com.gizwits.weixin.newGokitdog.service.XCloudNetworkService;
import com.gizwits.weixin.newGokitdog.service.BaseXCloudNetworkService.ResponseObject;
import com.gizwits.weixin.newGokitdog.util.WeixinUtil;

/**
 * Servlet implementation class WowoConfigServlet
 */
@WebServlet("/WowoConfigServlet")
public class WowoConfigServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	XCloudNetworkService netWorkService = new XCloudNetworkService();
	IGokitdogService gokitDogService = new GokitdogServiceImpl();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public WowoConfigServlet() {
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
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String openId = StringUtils.trimToEmpty(request.getParameter("openId"));
		String code = StringUtils.trimToEmpty(request.getParameter("code"));
		String delete = StringUtils.trimToEmpty(request.getParameter("delete"));
		String username = "";
		// deviceId由数据库获取

		// 第二次进入，获取oauth2请求返回的code，根据code获取openId
		if (!StringUtils.isEmpty(code)) {
			openId = WeixinUtil.requestOpenid(code);
			if (openId.equals("-1")) {
				request.getRequestDispatcher("/noDevice.jsp").forward(request,
						response);
			}
		}

		// 第一次进入，openId为空，进行oauth2请求
		if (StringUtils.isEmpty(openId)) {
			System.out.println("openId is empty");
			StringBuffer requestURL = request.getRequestURL();
			String queryString = request.getQueryString();
			if (queryString != null) {
				requestURL.append("?").append(queryString);
			}
			String url = WeixinUtil.requestOauth2(requestURL.toString());
			try {
				response.sendRedirect(url);
			} catch (IOException e) {
				System.out.print(e);
				return;
			}
			return;
		}
		if (!delete.isEmpty() && delete != null && !openId.isEmpty()
				&& openId != null) {
			IGokitdogService gokitService = new GokitdogServiceImpl();
			UserBindingDevice myDevice = gokitService.getUserBindingDevice(
					openId, "");
			if (myDevice != null) {
				unBindDevice(myDevice.getQrCode(), openId);
			}
			return;
		}

		// 搜索用户绑定的设备
		IGokitdogService gokitService = new GokitdogServiceImpl();
		UserBindingDevice myDevice = gokitService.getUserBindingDevice(openId,
				"");
		// 没有设备的情况
		if (null == myDevice || null == myDevice.getOpenId()) {
			request.getRequestDispatcher("/noDevice.jsp").forward(request,
					response);
		}
		try {
			username = WeixinUtil.getNickName(openId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		request.setAttribute("username", username);
		request.setAttribute("name", myDevice.getName());
		request.setAttribute("openId", openId);

		request.getRequestDispatcher("/config.jsp").forward(request, response);
	}

	// 解绑设备
	public ResponseObject unBindDevice(String deviceId, String openId) {
		ResponseObject result = null;

		// 机智云解绑
		String token = netWorkService.createUser(openId);
		result = netWorkService.unBindDeviceGizwits(token,
				Integer.parseInt(deviceId));
		// 本地解绑
		// 先机智云解绑成功后才对数据库操作
		if (result.getResult()) {
			result.setResult(gokitDogService.deleteBindingDevice(openId));
		}
		return result;
	}
}
