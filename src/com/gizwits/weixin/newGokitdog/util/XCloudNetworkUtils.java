package com.gizwits.weixin.newGokitdog.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.gizwits.weixin.newGokitdog.service.BaseXCloudNetworkService.XCloudApi;
import com.gizwits.weixin.newGokitdog.service.BaseXCloudNetworkService.XCloudClassName;

public class XCloudNetworkUtils {

	public static class RequestParams {
		Map<String, String> paramsMap;

		public RequestParams() {
			paramsMap = new HashMap<String, String>();
		}

		public void add(String key, String value) {
			paramsMap.put(key, value);
		}
		
	}

	private HttpUtil client;
	private String serverUrl;

	public XCloudNetworkUtils(String serverUrl,String token) {
		this.client = new HttpUtil(token);
		this.serverUrl = serverUrl;
	}

	public String post(String url,
			String json) {
		return client.post(getUrl(url), json);
	}
	
	public String post(XCloudApi xCloudApi, XCloudClassName xCloudClassName,
			String json) {
		return client.post(getUrl(xCloudApi, xCloudClassName), json);
	}

	public String get(XCloudApi xCloudApi, XCloudClassName xCloudClassName,
			RequestParams params) {
		return client.get(getUrl(params, xCloudApi, xCloudClassName));
	}
	
	public String get(XCloudApi xCloudApi, XCloudClassName xCloudClassName,
			RequestParams params,String did) {
		return client.get(getUrl(params, xCloudApi, xCloudClassName,did));
	}
	
	public String get(XCloudApi xCloudApi, XCloudClassName xCloudClassName,
			String path) {
		return client.get(getUrl(xCloudApi, xCloudClassName,path));
	}
	
	public String delete(XCloudApi xCloudApi, XCloudClassName xCloudClassName,
			String json) {
	
		return client.delete(getUrl(xCloudApi, xCloudClassName), json);
	}

	private String getUrl(Object... args) {
		StringBuffer strBuf = new StringBuffer(serverUrl);

		for (Object url : args) {
			if (url != null) {
				strBuf.append("/" + url.toString());
			}
		}

		return strBuf.toString();
	}

	private String getUrl(RequestParams params, Object... args) {
		StringBuffer result = new StringBuffer();
		if (params != null && params.paramsMap != null
				&& params.paramsMap.size() > 0) {
			Iterator<String> iter = params.paramsMap.keySet().iterator();
			while (iter.hasNext()) {
				String key = iter.next();
				result.append("&" + key + "=" + params.paramsMap.get(key));
			}
			if (result.length() > 0) {
				result.replace(0, 1, "?");
			}
		}

		return getUrl(args) + result.toString();
	}
	


	
}
