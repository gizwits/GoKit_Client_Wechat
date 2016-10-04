package com.gizwits.weixin.newGokitdog.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.gizwits.weixin.newGokitdog.bean.Device;
import com.gizwits.weixin.newGokitdog.bean.UserBindingDevice;
import com.gizwits.weixin.newGokitdog.service.GokitdogServiceImpl;
import com.gizwits.weixin.newGokitdog.service.IGokitdogService;
import com.gizwits.weixin.newGokitdog.service.XCloudNetworkService;
import com.gizwits.weixin.newGokitdog.service.BaseXCloudNetworkService.ResponseList;
import com.gizwits.weixin.newGokitdog.service.BaseXCloudNetworkService.ResponseObject;
import com.gizwits.weixin.newGokitdog.util.WeixinUtil;

/**
 * Servlet implementation class CreateServlet
 */
@WebServlet("/CreateServlet")
public class CreateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	IGokitdogService gokitService = new GokitdogServiceImpl();
	XCloudNetworkService networkService = new XCloudNetworkService();
	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CreateServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String openId = StringUtils.trimToEmpty(request.getParameter("openId"));
		
		
		ResponseObject<Device> list = networkService.getDevice(openId);
		if(!list.getResult()){
			return;
		}
		Device device = list.getSuccess();
		
		String temp = device.getTemperature() == "" ? "25" : device
				.getTemperature();
		String humidity = device.getHumidity() == "" ? "70" : device
				.getHumidity();

		String username = "";
		try {
			username = WeixinUtil.getNickName(openId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String petname = device.getName();

		response.setContentType("type=text/html; charset=UTF-8");
		request.setAttribute("openId", openId);
		request.setAttribute("temp", temp);
		request.setAttribute("humidity", humidity);
		request.setAttribute("username", username + "家");
		request.setAttribute("petname", petname);
		request.getRequestDispatcher("create.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String openId = StringUtils.trimToEmpty(request.getParameter("openId"));
		String groupName = StringUtils.trimToEmpty(request
				.getParameter("groupName"));
		ResponseObject respone = gokitService.createGroup(openId, groupName);

		request.setCharacterEncoding("UTF-8");
		response.setContentType("type=text/json; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(respone.getSuccess());
		out.flush();
		out.close();
	}

}
