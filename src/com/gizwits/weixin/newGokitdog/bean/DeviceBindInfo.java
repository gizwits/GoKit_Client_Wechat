package com.gizwits.weixin.newGokitdog.bean;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="devicebindinfo")
public class DeviceBindInfo {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String did;
	private String passcode;
	private String mac;
	private Date lastDate;
	private String ticket;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public DeviceBindInfo() {

	}

	public DeviceBindInfo(String did, String passcode, String mac) {
		super();
		this.did = did;
		this.passcode = passcode;
		this.mac = mac;
	}

	public String getDid() {
		return did;
	}

	public void setDid(String did) {
		this.did = did;
	}

	public String getPasscode() {
		return passcode;
	}

	public void setPasscode(String passcode) {
		this.passcode = passcode;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public Date getLastDate() {
		return lastDate;
	}

	public void setLastDate(Date lastDate) {
		this.lastDate = lastDate;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
}
