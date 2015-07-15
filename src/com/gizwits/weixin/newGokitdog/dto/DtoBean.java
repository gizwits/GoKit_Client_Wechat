package com.gizwits.weixin.newGokitdog.dto;

import java.util.ArrayList;

public class DtoBean {

	public static class regeditInfo {
		String phone_id = "";

		public regeditInfo(String phoneId) {
			phone_id = phoneId;
		}
	}

	public static class LoginInfo {
		String username;
		String password;

		public LoginInfo(String username, String password) {
			super();
			this.username = username;
			this.password = password;
		}
	}

	public static class BindDevices_result {
		ArrayList<String> success;
		ArrayList<String> failed;
	}

	public static class DeviceBindInfo_dto_item {
		String did;
		String passcode;

		public DeviceBindInfo_dto_item(String did, String passcode) {
			super();
			this.did = did;
			this.passcode = passcode;
		}
	}

	public static class DeviceBindInfo_dto {
		public void setDevices(ArrayList<DeviceBindInfo_dto_item> devices) {
			this.devices = devices;
		}

		ArrayList<DeviceBindInfo_dto_item> devices;

	}

	public static class DeviceHistoryInfo {
		String ts;
		String entity;
		String attr;
		String val;
		String did;

		public String getTimestamp() {
			return ts;
		}

		public void setTimestamp(String ts) {
			this.ts = ts;
		}

		public String getDid() {
			return did;
		}

		public void setDid(String did) {
			this.did = did;
		}

		public String getEntity() {
			return entity;
		}

		public void setEntity(String entity) {
			this.entity = entity;
		}

		public String getAttr() {
			return attr;
		}

		public void setAttr(String attr) {
			this.attr = attr;
		}

		public String getVal() {
			return val;
		}

		public void setVal(String val) {
			this.val = val;
		}
	}

	public static class DeviceDataInfo {
		private String LED_OnOff;
		private String Humidity;
		private String LED_R;
		private String LED_Color;
		private String Infrared;
		private String Fault_IR;
		private String Fault_TemHum;
		private String Alert_1;
		private String Alert_2;
		private String LED_B;
		private String Motor_Speed;
		private String Fault_Motor;
		private String Fault_LED;
		private String LED_G;
		private String Temperature;

		private String updated_at;

		public String getLED_OnOff() {
			return LED_OnOff;
		}

		public void setLED_OnOff(String lED_OnOff) {
			LED_OnOff = lED_OnOff;
		}

		public String getHumidity() {
			return Humidity;
		}

		public void setHumidity(String humidity) {
			Humidity = humidity;
		}

		public String getLED_R() {
			return LED_R;
		}

		public void setLED_R(String lED_R) {
			LED_R = lED_R;
		}

		public String getLED_Color() {
			return LED_Color;
		}

		public void setLED_Color(String lED_Color) {
			LED_Color = lED_Color;
		}

		public String getInfrared() {
			return Infrared;
		}

		public void setInfrared(String infrared) {
			Infrared = infrared;
		}

		public String getFault_IR() {
			return Fault_IR;
		}

		public void setFault_IR(String fault_IR) {
			Fault_IR = fault_IR;
		}

		public String getFault_TemHum() {
			return Fault_TemHum;
		}

		public void setFault_TemHum(String fault_TemHum) {
			Fault_TemHum = fault_TemHum;
		}

		public String getAlert_1() {
			return Alert_1;
		}

		public void setAlert_1(String alert_1) {
			Alert_1 = alert_1;
		}

		public String getAlert_2() {
			return Alert_2;
		}

		public void setAlert_2(String alert_2) {
			Alert_2 = alert_2;
		}

		public String getLED_B() {
			return LED_B;
		}

		public void setLED_B(String lED_B) {
			LED_B = lED_B;
		}

		public String getMotor_Speed() {
			return Motor_Speed;
		}

		public void setMotor_Speed(String motor_Speed) {
			Motor_Speed = motor_Speed;
		}

		public String getFault_Motor() {
			return Fault_Motor;
		}

		public void setFault_Motor(String fault_Motor) {
			Fault_Motor = fault_Motor;
		}

		public String getFault_LED() {
			return Fault_LED;
		}

		public void setFault_LED(String fault_LED) {
			Fault_LED = fault_LED;
		}

		public String getLED_G() {
			return LED_G;
		}

		public void setLED_G(String lED_G) {
			LED_G = lED_G;
		}

		public String getTemperature() {
			return Temperature;
		}

		public void setTemperature(String temperature) {
			Temperature = temperature;
		}

		public String getUpdated_at() {
			return updated_at;
		}

		public void setUpdated_at(String updated_at) {
			this.updated_at = updated_at;
		}

	}

}
