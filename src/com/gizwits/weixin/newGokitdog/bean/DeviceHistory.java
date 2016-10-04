package com.gizwits.weixin.newGokitdog.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 设备历史数据
 * 
 * @author ads
 * 
 */
@Entity
public class DeviceHistory {

	public enum Type {
		/**
		 * 电机状态
		 */
		Motor("5"),
		
		/**
		 * 温度
		 */
		Temperature("7"), 
		
		/**
		 * 湿度
		 */
		Humidity("8") ,
		
		/**
		 * 是否在家
		 */
		Home("6");
		String code;

		Type(String code) {
			this.code = code;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

	}

	private String id;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	private String time;
	private Type type;
	private String value;

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
