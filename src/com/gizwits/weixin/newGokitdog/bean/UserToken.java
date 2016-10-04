package com.gizwits.weixin.newGokitdog.bean;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name="usertoken")
public class UserToken {

	String uid;
	String token;
	String expire_at;
	String openId;

	@Id
	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getExpire_at() {
		return expire_at;
	}

	public void setExpire_at(String expireAt) {
		expire_at = expireAt;
	}
}
