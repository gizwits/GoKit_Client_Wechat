package com.gizwits.weixin.newGokitdog.bean;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name="device")
public class Device {
	private String openId;
	private String deviceId; // qrcode
	private String name;
	private String temperature;
	private String humidity;
	private String status;
	/**
	 * 电机状态
	 */
	private String motor;
	/**
	 * 是否在家
	 */
	private String home;
	private String homeTime;
	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getHomeTime() {
		return homeTime;
	}

	public void setHomeTime(String homeTime) {
		this.homeTime = homeTime;
	}

	public String getMotor() {
		return motor;
	}

	public void setMotor(String motor) {
		this.motor = motor;
	}

	public String getHome() {
		return home;
	}

	public void setHome(String home) {
		this.home = home;
	}

	@Id
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	public String getHumidity() {
		return humidity;
	}

	public void setHumidity(String humidity) {
		this.humidity = humidity;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
