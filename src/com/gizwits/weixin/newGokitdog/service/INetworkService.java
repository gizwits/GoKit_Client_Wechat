package com.gizwits.weixin.newGokitdog.service;

import com.gizwits.weixin.newGokitdog.bean.Device;
import com.gizwits.weixin.newGokitdog.bean.Groups;
import com.gizwits.weixin.newGokitdog.bean.UserBindingDevice;
import com.gizwits.weixin.newGokitdog.dto.DtoBean.BindDevices_result;
import com.gizwits.weixin.newGokitdog.dto.DtoBean.DeviceDataInfo;
import com.gizwits.weixin.newGokitdog.service.BaseXCloudNetworkService.ResponseList;
import com.gizwits.weixin.newGokitdog.service.BaseXCloudNetworkService.ResponseObject;

/**
 * 网络服务接口
 * 
 * @author ads
 * 
 */
@SuppressWarnings("rawtypes")
public interface INetworkService {

	/**
	 * 功能命令码枚举
	 * 
	 * @author ads
	 * 
	 */
	public enum Command {
		/**
		 * 送风
		 */
		Air("1,32,0,0,0,0,0,6"),
		/**
		 * 抽湿
		 */
		Dehumidifier("1,32,0,0,0,0,0,4"),
		/**
		 * 关闭
		 */
		ShutDown("1,32,0,0,0,0,0,5"),

		/**
		 * 黄停
		 */
		Yellow_ShutDown("1,62,0,254,70,0,0,5"),

		/**
		 * 绿停
		 */
		Green_ShutDown("1,62,0,0,254,0,0,5"),

		/**
		 * 红+正转
		 */
		RED_AIR("1,62,0,254,0,0,0,6"),

		/**
		 * 红+反转
		 */
		RED_Dehumidifier("1,62,0,254,0,0,0,4");

		private String code;

		Command(String code) {
			this.code = code;
		}

		public String getCode() {
			return code;
		}
	}

	/**
	 * 绑定设备
	 * 
	 * @param data
	 */
	public ResponseObject bindingDevices(UserBindingDevice data, String openId);

	/**
	 * 发送设备控制指令
	 * 
	 * @return
	 */
	public ResponseObject sendControlCommands(int deviceId, Command cmd,
			String token);

	// /**
	// * 获取设备历史数据
	// */
	// public ResponseList<DeviceHistory> getDeviceHistoryData(Date date,
	// Type type, int deviceId, String token);

	public ResponseObject<?> login(String username, String password);

	public String createUser(String phoneId);

	/**
	 * 获取设备最新数据
	 */
	public DeviceDataInfo getDeviceLatestData(String deviceId);

	/**
	 * 解绑设备
	 */
	public ResponseObject<BindDevices_result> unBindDeviceGizwits(String token,
			int qrcode);

	/**
	 * 获取设备信息
	 */
	public ResponseObject<Device> getDevice(String openId);

	/**
	 * 发送V4命令
	 */
	public ResponseObject sendV4ControlCommands(int deviceId, String json,
			String token);

	ResponseObject<?> bindingDeviceByToken(UserBindingDevice data, String token);

}
