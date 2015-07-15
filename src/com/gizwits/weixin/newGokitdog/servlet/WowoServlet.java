package com.gizwits.weixin.newGokitdog.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.gizwits.weixin.newGokitdog.bean.Device;
import com.gizwits.weixin.newGokitdog.bean.DeviceBindInfo;
import com.gizwits.weixin.newGokitdog.bean.DeviceStateCache;
import com.gizwits.weixin.newGokitdog.bean.UserBindingDevice;
import com.gizwits.weixin.newGokitdog.db.HibernateModel;
import com.gizwits.weixin.newGokitdog.service.BaseXCloudNetworkService.ResponseObject;
import com.gizwits.weixin.newGokitdog.service.GokitdogServiceImpl;
import com.gizwits.weixin.newGokitdog.service.IGokitdogService;
import com.gizwits.weixin.newGokitdog.service.XCloudNetworkService;
import com.gizwits.weixin.newGokitdog.util.WeixinUtil;

/**
 * Servlet implementation class WowoServlet
 */
@WebServlet("/WowoServlet")
public class WowoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(WowoServlet.class);

	IGokitdogService gokitService = new GokitdogServiceImpl();
	XCloudNetworkService networkService = new XCloudNetworkService();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public WowoServlet() {
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
		/*
		 * 获取参数
		 */
		String openId = StringUtils.trimToEmpty(request.getParameter("openId"));
		// String openId ="oUQfbsw-qYoLTlMko75o7YKYNg1g";
		String flag = StringUtils.trimToEmpty(request.getParameter("flag"));
		String method = StringUtils.trimToEmpty(request.getParameter("method"));
		String code = StringUtils.trimToEmpty(request.getParameter("code"));

		String name = "小白";
		// 第二次进入，获取oauth2请求返回的code，根据code获取openId
		if (!StringUtils.isEmpty(code)) {
			logger.info("get OpenId begin");
			openId = WeixinUtil.requestOpenid(code);
			logger.info("get OpenId end");
		}

		// 第一次进入，openId为空，进行oauth2请求
		if (StringUtils.isEmpty(openId)) {
			logger.info("get oauth2 begin");
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
			}
			logger.info("get oauth2 end");
			return;
		} else {
			System.out.println("openId is " + openId);
		}

		// deviceId由数据库获取

		UserBindingDevice myDevice = null;
		myDevice = gokitService.getUserBindingDevice(openId, "");

		if (myDevice == null) {
			response.setContentType("type=text/html; charset=UTF-8");
			request.setAttribute("openId", openId);

			// 传去前端
			request.getRequestDispatcher("wowo.jsp").forward(request, response);
			return;
		}

		String deviceId = myDevice.getQrCode();

		if (!StringUtils.equals(method, "history")) {
			// 获取窝窝的状态

			Device device = new Device();
			ResponseObject<Device> list = networkService.getDevice(openId);
			if (!list.getResult()) {
				return;
			}
			device = networkService.getDevice(openId).getSuccess();
			response.setContentType("type=text/html; charset=UTF-8");
			String did = "";
			if (null == deviceId || deviceId.isEmpty()) {
				deviceId = "-1";
			}
			DeviceBindInfo deviceBindInfo = (DeviceBindInfo) HibernateModel
					.getInstance().get(DeviceBindInfo.class,
							Integer.parseInt(deviceId));
			if (null == deviceBindInfo) {
				response.setContentType("type=text/html; charset=UTF-8");
				request.setAttribute("openId", openId);
				// 传去前端
				request.getRequestDispatcher("wowo.jsp").forward(request,
						response);
				return;
			}
			name = gokitService.getPetName(openId);
			did = deviceBindInfo.getDid();
			// 传去前端
			request.setAttribute("did", did);
			request.setAttribute("openId", openId);
			request.setAttribute("deviceId", device.getDeviceId());
			request.setAttribute("petName", name);
			request.getRequestDispatcher("wowo.jsp").forward(request, response);
		}
		// 获取历史数据
		else if (StringUtils.equals(method, "history")) {
			String index = flag.equals("") ? "0" : flag;

			Date date = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.DATE, Integer.parseInt(index));
			date = cal.getTime();
			SimpleDateFormat sdf = new SimpleDateFormat("MM / dd");
			String currentDate = sdf.format(date);

			// 获取窝窝的历史状态数据

			String token = networkService.createUser(openId);
			List<DeviceStateCache> list = HibernateModel.getInstance().getList(
					"select * from DeviceStateCache where deviceId ="
							+ deviceId, DeviceStateCache.class);
			ArrayList<String> tempList = new ArrayList<String>();
			ArrayList<String> humidityList = new ArrayList<String>();

			try {
				SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
				Date dt = s.parse(s.format(new Date()));
				Long time = dt.getTime() + Integer.parseInt(index) * 24 * 60
						* 60 * 1000;
				int[] range = { 0, 4, 8, 12, 16, 20, 24 };
				// 温度折线处理
				// 获取时间的中值
				for (int i = 0; i < 6; i++) {
					if (list != null) {
						Integer temp = 0;
						int sum = 0;
						for (int j = 0; j < list.size(); j++) {
							Long tempTime = list.get(j).getUpdatTime()
									.getTime();
							if (tempTime >= (time + range[i] * 60 * 60 * 1000)
									&& tempTime < (time + range[i + 1] * 60 * 60 * 1000)) {
								if (list.get(j).getTemperature() != null) {
									temp += (int) Double.parseDouble(list
											.get(j).getTemperature());
									sum++;
								}
							}
						}
						if (sum > 0) {
							tempList.add(String.valueOf(temp / sum));
						} else {
							tempList.add("0");
						}
					} else {
						tempList.add("0");
					}
					// 湿度折线处理
					if (list != null) {
						Integer humidity = 0;
						int sum = 0;
						for (int j = 0; j < list.size(); j++) {
							Long tempTime = list.get(j).getUpdatTime()
									.getTime();
							if (tempTime >= (time + range[i] * 60 * 60 * 1000)
									&& tempTime < (time + range[i + 1] * 60 * 60 * 1000)) {
								if (list.get(j).getHumidity() != null) {
									humidity += (int) Double.parseDouble(list
											.get(j).getHumidity());
									sum++;
								}
							}
						}
						if (sum > 0) {
							humidityList.add(String.valueOf(humidity / sum));
						} else {
							humidityList.add("0");
						}
					} else {
						humidityList.add("0");
					}
				}
			} catch (ParseException e) {
				for (int i = 0; i < 6; i++) {
					tempList.add("0");
					humidityList.add("0");
				}
				e.printStackTrace();
			}

			JSONObject result = new JSONObject();
			result.put("Temperature", tempList);
			result.put("Humidity", humidityList);
			result.put("Date", currentDate);

			response.setContentType("type = text/json ; charset = utf-8");
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result.toString());
			out.flush();
			out.close();
		}
	}

}
