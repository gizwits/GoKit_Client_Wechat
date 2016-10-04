package com.gizwits.weixin.newGokitdog.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.gizwits.weixin.newGokitdog.bean.Device;
import com.gizwits.weixin.newGokitdog.bean.GroupmMember;
import com.gizwits.weixin.newGokitdog.db.HibernateModel;
import com.gizwits.weixin.newGokitdog.service.GokitdogServiceImpl;
import com.gizwits.weixin.newGokitdog.service.IGokitdogService;
import com.gizwits.weixin.newGokitdog.service.XCloudNetworkService;
import com.gizwits.weixin.newGokitdog.service.BaseXCloudNetworkService.ResponseList;
import com.gizwits.weixin.newGokitdog.service.BaseXCloudNetworkService.ResponseObject;
import com.gizwits.weixin.newGokitdog.util.WeixinUtil;

/**
 * Servlet implementation class XingqiuServlet
 */
@WebServlet("/XingqiuServlet")
public class XingqiuServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(XingqiuServlet.class);  
	IGokitdogService gokitService = new GokitdogServiceImpl();
	XCloudNetworkService networkService = new XCloudNetworkService();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public XingqiuServlet() {
		super();
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
	// 奴星球
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String groupId = StringUtils.trimToEmpty(request
				.getParameter("groupId"));
		String openId = StringUtils.trimToEmpty(request.getParameter("openId"));

		// 获取组的状态
		List<Device> list = getGroupInfo(openId, groupId);
		JSONArray devices = new JSONArray();

		for (int i = 0; i < list.size(); i++) {

			String temp = list.get(i).getTemperature() == "" ? "25" : list.get(
					i).getTemperature();
			String humidity = list.get(i).getHumidity() == "" ? "70" : list
					.get(i).getHumidity();

			String username = "";
			if(null == list || null == list.get(i).getOpenId() || list.get(i).getOpenId().isEmpty()){
				logger.info("list is null or openId is empty");
				continue;
			}
			try {
				username = WeixinUtil.getNickName(list.get(i).getOpenId());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(username.isEmpty() || username == null){
				username = "未知";
			}
			JSONObject object = new JSONObject();
			object.put("deviceId", list.get(i).getDeviceId());
			object.put("temp", temp);
			object.put("humidity", humidity);
			object.put("username", username + "家");
			object.put("petname", list.get(i).getName());
			object.put("openId", list.get(i).getOpenId());

			devices.add(object);
		}

		response.setContentType("type=text/html; charset=UTF-8");
		request.setAttribute("groupId", groupId);
		request.setAttribute("devices", devices);
		request.getRequestDispatcher("xingqiu.jsp").forward(request, response);
	}

	// 获取组的信息
	public List<Device> getGroupInfo(String openId, String groupId) {
		
		ArrayList<GroupmMember> list = (ArrayList<GroupmMember>) gokitService
				.getGroupMemberList(openId, groupId).getSuccess();

		ArrayList<Device> devicelist = new ArrayList<Device>();
		for (int i = 0; i < list.size(); i++) {

			ResponseObject<Device> devicesList = networkService.getDevice(list
					.get(i).getUserId());
			if (!devicesList.getResult()) {
				continue;
			}
			Device temp = devicesList.getSuccess();

			temp.setOpenId(list.get(i).getUserId());
			devicelist.add(temp);
		}
		return devicelist;
	}

}
