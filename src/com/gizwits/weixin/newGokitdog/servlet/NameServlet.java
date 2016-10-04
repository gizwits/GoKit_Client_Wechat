package com.gizwits.weixin.newGokitdog.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.gizwits.weixin.newGokitdog.bean.UserBindingDevice;
import com.gizwits.weixin.newGokitdog.db.HibernateModel;
import com.gizwits.weixin.newGokitdog.service.GokitdogServiceImpl;
import com.gizwits.weixin.newGokitdog.service.IGokitdogService;
import com.gizwits.weixin.newGokitdog.service.XCloudNetworkService;

/**
 * Servlet implementation class CreateServlet
 */
@WebServlet("/NameServlet")
public class NameServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public NameServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		//当get 请求过来的时候跳转到name.JSP的页面
		String openId = StringUtils.trimToEmpty(request.getParameter("openId"));
		String deviceId = StringUtils.trimToEmpty(request.getParameter("deviceId"));
		request.setAttribute("openId", openId);
		request.setAttribute("deviceId", deviceId);
		request.getRequestDispatcher("name.jsp").forward(request, response);
	}
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		//post 请求的时候修改姓名
		JSONObject result = new JSONObject();
		try {
			String openId = StringUtils.trimToEmpty(request
					.getParameter("openId"));
			String name = StringUtils.trimToEmpty(request.getParameter("name"));
			IGokitdogService gokitService = new GokitdogServiceImpl();
			gokitService.updateUserDeviceName(openId,"",name);
			result.put("flag", true);
		} catch (Exception e) {
			result.put("flag", false);
			e.printStackTrace();
		}
		request.setCharacterEncoding("UTF-8");
		response.setContentType("type=text/json; charset=UTF-8");

		PrintWriter out = response.getWriter();
		out.print(result.toString());
		out.flush();
		out.close();
	}

}
