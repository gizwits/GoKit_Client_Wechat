package com.gizwits.weixin.newGokitdog.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.gizwits.weixin.newGokitdog.GlobalConfig;
import com.gizwits.weixin.newGokitdog.bean.Device;
import com.gizwits.weixin.newGokitdog.bean.DeviceBindInfo;
import com.gizwits.weixin.newGokitdog.bean.GroupmMember;
import com.gizwits.weixin.newGokitdog.bean.Groups;
import com.gizwits.weixin.newGokitdog.bean.UserBindingDevice;
import com.gizwits.weixin.newGokitdog.bean.UserToken;
import com.gizwits.weixin.newGokitdog.db.HibernateModel;
import com.gizwits.weixin.newGokitdog.dto.DtoBean.BindDevices_result;
import com.gizwits.weixin.newGokitdog.dto.DtoBean.DeviceBindInfo_dto;
import com.gizwits.weixin.newGokitdog.dto.DtoBean.DeviceBindInfo_dto_item;
import com.gizwits.weixin.newGokitdog.dto.DtoBean.DeviceDataInfo;
import com.gizwits.weixin.newGokitdog.dto.DtoBean.LoginInfo;
import com.gizwits.weixin.newGokitdog.dto.DtoBean.regeditInfo;
import com.gizwits.weixin.newGokitdog.service.BaseXCloudNetworkService.ResponseObject;
import com.gizwits.weixin.newGokitdog.util.HttpUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@SuppressWarnings("rawtypes")
public class XCloudNetworkService extends BaseXCloudNetworkService implements
		INetworkService {

	/**
	 * 绑定设备
	 */
	@Override
	public ResponseObject<?> bindingDevices(UserBindingDevice data,
			String openId) {
		ResponseObject result = new ResponseObject();

		try {
			
			result.result = getDidNPasscode(Integer.parseInt(data.getQrCode()));
			if (result.result == true) {
				// 云端绑定
				String token = createUser(openId);
				result = bindDevicesForNet(
						token, Integer.parseInt(data.getQrCode()));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
	
	/**
	 *  绑定设备
	 */
	@Override
	public ResponseObject<?> bindingDeviceByToken(UserBindingDevice data,
			String token) {
		ResponseObject result = new ResponseObject();

		try {
			result.result = getDidNPasscode(Integer.parseInt(data.getQrCode()));
			if (result.result == true) {
				// 云端绑定
				result = bindDevicesForNet(
						token, Integer.parseInt(data.getQrCode()));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 
	 * 从XPG云端获取设备信息
	 * 
	 * */
	public Device getNetDevice(String openId) {
		IGokitdogService service = new GokitdogServiceImpl();
		UserBindingDevice userBindingDevice = service.getUserBindingDevice(
				openId, "");

		Device device = new Device();
		device.setName("小白");
		device.setHumidity("25");
		device.setTemperature("70");
		device.setHome("0");
		Long time = Long.parseLong("0");
		device.setHomeTime("0");
		device.setMotor("0");

		device.setDeviceId(userBindingDevice.getQrCode());
		String token = createUser(openId);

		if (userBindingDevice == null)
			return device;

		if (StringUtils.isEmpty(userBindingDevice.getName())) {
			device.setName("小白");
		} else {
			device.setName(userBindingDevice.getName());
		}
		DeviceDataInfo dataInfo = getDeviceLatestData(userBindingDevice
				.getQrCode());
		if (dataInfo != null) {
			device.setHumidity(dataInfo.getHumidity());
			device.setTemperature(dataInfo.getTemperature());
			device.setHome(dataInfo.getInfrared());
			device.setHomeTime(dataInfo.getUpdated_at());
			device.setMotor(dataInfo.getMotor_Speed());
		}
		return device;
	}

	

	/**
	 * 向机智云云端绑定设备
	 */
	private ResponseObject<BindDevices_result> bindDevicesForNet(String token,
			int qrcode) {
		// start_ts&end_ts&entity&attr&limit&skip

		DeviceBindInfo deviceBindInfo = (DeviceBindInfo) HibernateModel
				.getInstance().get(DeviceBindInfo.class, qrcode);
		ArrayList<DeviceBindInfo_dto_item> devices = new ArrayList<DeviceBindInfo_dto_item>();
		devices.add(new DeviceBindInfo_dto_item(deviceBindInfo.getDid(),
				deviceBindInfo.getPasscode()));
		DeviceBindInfo_dto deviceBindInfoDto = new DeviceBindInfo_dto();
		deviceBindInfoDto.setDevices(devices);
		ResponseObject<BindDevices_result> result = post(XCloudApi.app,
				XCloudClassName.bindings, deviceBindInfoDto, token,
				BindDevices_result.class);
		return result;
	}

	

	/**
	 *  创建用户
	 */
	public String createUser(String phoneId) {
		if(null == phoneId || phoneId.isEmpty()){
			return "";
		}
		String expire_at = "";
		String token = "";
		Date date = new Date();
		long time = date.getTime();
		if (StringUtils.isEmpty(token)
				|| StringUtils.isEmpty(expire_at)
				|| (time - 6 * 24 * 60 * 60 * 1000) > (Long
						.parseLong(expire_at) * 1000)) {
			ResponseObject<UserToken> result = createObject(XCloudApi.app,
					XCloudClassName.users, new regeditInfo(phoneId),
					UserToken.class);
			UserToken userToken = (UserToken) result.getSuccess();
			userToken.setOpenId(phoneId);
			if (userToken != null) {
				HibernateModel.getInstance().saveOrUpdate(userToken);
				token = userToken.getToken();
			}
		}
		return token;
	}

	/**
	 * 登录
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject login(String username, String password) {
		ResponseObject<UserToken> result = createObject(XCloudApi.app,
				XCloudClassName.login, new LoginInfo(username, password),
				UserToken.class);
		UserToken userToken = (UserToken) result.getSuccess();
		if (userToken != null) {
			HibernateModel.getInstance().update(userToken);
		}
		System.out.println("token: " + userToken.getToken());
		return result;
	}

	/**
	 * 发送控制设备指令
	 */
	@Override
	public ResponseObject sendControlCommands(int deviceId, Command cmd,
			String token) {
		ResponseObject result = new ResponseObject();
		try {
			DeviceBindInfo deviceBindInfo = (DeviceBindInfo) HibernateModel
					.getInstance().get(DeviceBindInfo.class, deviceId);

			JsonObject json = new JsonObject();
			json.addProperty("cmd", "write");

			JsonObject dataJson = new JsonObject();
			dataJson.addProperty("did", deviceBindInfo.getDid());
			dataJson.add("raw",
					new JsonParser().parse("[" + cmd.getCode() + "]"));
			json.add("data", dataJson);

			getNetworkUtils(GlobalConfig.M2M_SERVER_URL, token).post(
					"api/v1/rc/app2dev", json.toString());

			result.result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
	
	/**
	 * 发送控制设备指令
	 */
	public ResponseObject sendV4ControlCommands(int deviceId, String json,
			String token) {
		ResponseObject result = new ResponseObject();
		try {
			DeviceBindInfo deviceBindInfo = (DeviceBindInfo) HibernateModel
					.getInstance().get(DeviceBindInfo.class, deviceId);
			
			getNetworkUtils(GlobalConfig.SERVER_URL, token).post(
					"app/control/"+deviceBindInfo.getDid(), json.toString());
			result.result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 获取最新数据
	 */
	public DeviceDataInfo getDeviceLatestData(String deviceId) {

		String did = "";
		
		if(deviceId == null || deviceId.isEmpty() ){
			deviceId = "-1";
		}
		DeviceBindInfo deviceBindInfo = (DeviceBindInfo) HibernateModel
				.getInstance().get(DeviceBindInfo.class,
						Integer.parseInt(deviceId));
		if(deviceBindInfo == null){
			return null;
		}
		did = deviceBindInfo.getDid();
		if (StringUtils.isEmpty(did))
			return null;

		did = did + "/latest";
		ResponseObject<DeviceDataInfo> respond = get(XCloudApi.app,
				XCloudClassName.devdata, did, DeviceDataInfo.class);
		DeviceDataInfo info = respond.success;
		if (info != null) {
			info.setUpdated_at(new Date().getTime() + "");
		}

		return info;

	}

	/**
	 * 主动获取设备的did和passcode信息
	 * 
	 * @param deviceId
	 * @return
	 */
	private boolean getDidNPasscode(int deviceId) {
		DeviceBindInfo deviceBindInfo = (DeviceBindInfo) HibernateModel
				.getInstance().get(DeviceBindInfo.class, deviceId);
		if (deviceBindInfo != null) {
			String json = queryDevice(GlobalConfig.PRODUCT_KEY,
					deviceBindInfo.getMac());
			if (json == null) {
				return false;
			}
			JSONObject jsonObject = JSONObject.fromObject(json);
			try {
				String did = jsonObject.getString("did");
				String passcode = jsonObject.getString("passcode");
				deviceBindInfo.setDid(did);
				deviceBindInfo.setPasscode(passcode);
				deviceBindInfo.setLastDate(new Date());
				HibernateModel.getInstance().saveOrUpdate(deviceBindInfo);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}

		return false;

	}

	/**
	 * 查询数据
	 */ 
	private String queryDevice(String productKey, String mac) {
		String url = "http://api.gizwits.com/app/devices?product_key="
				+ productKey + "&mac=" + mac;
		HttpUtil httpUtil = new HttpUtil("");
		return httpUtil.get(url);
	}

	/**
	 *  向机智云云端解除绑定设备
	 */
	@Override
	public ResponseObject<BindDevices_result> unBindDeviceGizwits(String token,
			int qrcode) {

		DeviceBindInfo deviceBindInfo = (DeviceBindInfo) HibernateModel
				.getInstance().get(DeviceBindInfo.class, qrcode);
		if (deviceBindInfo == null)
			return null;

		ArrayList<DeviceBindInfo_dto_item> devices = new ArrayList<DeviceBindInfo_dto_item>();
		devices.add(new DeviceBindInfo_dto_item(deviceBindInfo.getDid(),
				deviceBindInfo.getPasscode()));
		DeviceBindInfo_dto deviceBindInfoDto = new DeviceBindInfo_dto();
		deviceBindInfoDto.setDevices(devices);
		ResponseObject<BindDevices_result> result = delete(XCloudApi.app,
				XCloudClassName.bindings, deviceBindInfoDto, token,
				BindDevices_result.class);
		return result;
	}

	/** 
	 * 获取设备
	 * 
	 */
	@Override
	public ResponseObject<Device> getDevice(String openId) {
		ResponseObject<Device> result = new ResponseObject<Device>();
		XCloudNetworkService networkService = new XCloudNetworkService();

		Device device = networkService.getNetDevice(openId);
		if (device != null) {
			result.result = true;
			result.success = device;
		}

		return result;
	}

}
