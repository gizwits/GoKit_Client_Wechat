package com.gizwits.weixin.newGokitdog.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;

import com.gizwits.weixin.newGokitdog.bean.Device;
import com.gizwits.weixin.newGokitdog.bean.DeviceBindInfo;
import com.gizwits.weixin.newGokitdog.bean.UserBindingDevice;
import com.gizwits.weixin.newGokitdog.service.BaseXCloudNetworkService.ResponseObject;
import com.gizwits.weixin.newGokitdog.service.GokitdogServiceImpl;
import com.gizwits.weixin.newGokitdog.service.IGokitdogService;
import com.gizwits.weixin.newGokitdog.service.XCloudNetworkService;
import com.google.gson.Gson;

/**
 * Servlet implementation class DeviceDataServlet
 */
@WebServlet("/device/data")
public class DeviceDataServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(DeviceDataServlet.class);
	private static IGokitdogService gokitService = new GokitdogServiceImpl();
	private static XCloudNetworkService xCloudNetworkService = new XCloudNetworkService();
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DeviceDataServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		Device device = new Device();
		
		Map<String, Object> map = readToMap(request);
		// 查找设备信息
		DeviceBindInfo deviceInfo = ((ResponseObject<DeviceBindInfo>) gokitService
				.getDeviceBindInfoByDid((String) map.get("did"))).getSuccess();

		if (deviceInfo == null) {
			return;
		}
		int deviceId = deviceInfo.getId();
		// 查找绑定的设备
		UserBindingDevice bounddeivce = gokitService
				.getUserBindingDevice(Integer.toString(deviceId));
		if (bounddeivce == null) {
			return;
		}
		String openId = bounddeivce.getOpenId();
		if (null == openId) {
			return;
		}
		
		// 登录机智云
		String token = xCloudNetworkService.createUser(openId);
		if (null == token || token.isEmpty()) {
			return;
		}
		// 绑定设备
		ResponseObject respone = xCloudNetworkService.bindingDeviceByToken(
				bounddeivce, token);
		if (respone.getResult()) {
			Map attrs = (Map) map.get("attrs");
			String homeString = attrs.get("Infrared").toString();

			if (homeString == null) {
				homeString = "";
			}
			device.setOpenId(openId);
			device.setHome(homeString);
			device.setToken(token);
			device.setDeviceId(String.valueOf(deviceId));
			// 控制
			updateDeviceControl(device);
		}
	}

	// 读取数据后转化为map
	private Map<String, Object> readToMap(HttpServletRequest request) {
		// 读取数据
		BufferedReader br;
		Map<String, Object> map = null;
		try {
			br = request.getReader();
			StringBuffer sb = new StringBuffer();
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			logger.info(sb.toString());
			map = new Gson().fromJson(sb.toString(), Map.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}

	// 更新设备状态
	private void updateDeviceControl(Device device) {
		try {
			if (device != null) {

				List commandList = new ArrayList<JSONObject>();

				if (device.getHome() != null && device.getHome().length() > 0) {
					if (Double.parseDouble(device.getHome()) == 0) {
						// 黄灯

						JSONObject json = new JSONObject();
						JSONObject json2 = new JSONObject();
						json.put("LED_R", 250);
						json2.put("attrs", json);

						JSONObject json3 = new JSONObject();
						JSONObject json4 = new JSONObject();
						json3.put("LED_G", 70);
						json4.put("attrs", json3);
						commandList.add(json2);
						commandList.add(json4);

					} else {
						// 绿灯
						JSONObject json = new JSONObject();
						JSONObject json2 = new JSONObject();
						json.put("LED_R", 0);
						json2.put("attrs", json);

						JSONObject json3 = new JSONObject();
						JSONObject json4 = new JSONObject();
						json3.put("LED_G", 254);
						json4.put("attrs", json3);
						commandList.add(json2);
						commandList.add(json4);
					}

					for (int i = 0; i < commandList.size(); i++) {
						new XCloudNetworkService().sendV4ControlCommands(
								Integer.parseInt(device.getDeviceId()),
								commandList.get(i).toString(),
								device.getToken());
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
