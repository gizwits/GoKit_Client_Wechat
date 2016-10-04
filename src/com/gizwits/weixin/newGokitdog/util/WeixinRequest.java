package com.gizwits.weixin.newGokitdog.util;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.gizwits.weixin.newGokitdog.util.WeixinUtil.WeixinRequstType;

public class WeixinRequest {
	private String url;
	private String openId;
	private WeixinRequstType requstType;
	private Map<String, String> body;

	public WeixinRequest() {
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public WeixinRequstType getRequstType() {
		return requstType;
	}

	public void setRequstType(WeixinRequstType requstType) {
		this.requstType = requstType;
	}

	public Map<String, String> getBody() {
		return body;
	}

	public void setBody(Map<String, String> body) {
		this.body = body;
	}

	
	
}
