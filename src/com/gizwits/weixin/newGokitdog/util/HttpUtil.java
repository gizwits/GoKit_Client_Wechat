package com.gizwits.weixin.newGokitdog.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;

import com.gizwits.weixin.newGokitdog.GlobalConfig;

public class HttpUtil {

	String token;

	public HttpUtil(String token) {
		this.token = token;
	}

	enum RequestType {
		GET, POST,DELETE
	}

	public String get(String urlStr) {
		return request(RequestType.GET, urlStr, null);
	}

	public String post(String urlStr, String data) {
		return request(RequestType.POST, urlStr, data);
	}
	
	public String delete(String urlStr, String data) {
		return request(RequestType.DELETE, urlStr, data);
	}

	/**
	 * 设置 head 云端服务器需要的参数
	 * 
	 * @param connection
	 * @throws ProtocolException
	 */
	private HttpURLConnection getConnection(RequestType type, String urlStr)
			throws Exception {
		URL url = new URL(urlStr);

		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		connection.setDoInput(true);
		if (type != RequestType.GET) {
			connection.setDoOutput(true);
		}
		connection.setRequestMethod(type.name());

		connection.addRequestProperty("X-Gizwits-Application-Id",GlobalConfig.XCLOUD_APP_ID);
		connection.addRequestProperty("X-Gizwits-User-token", token);
		connection.setRequestProperty("Accept-Charset", "utf-8");
		connection.setRequestProperty("Accept", "application/json");
		connection.setRequestProperty("Content-Length", "3");
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setRequestProperty("Authorization", "Basic " + GlobalConfig.M2M_REQUEST_AUTHORIZATION);
		connection.setConnectTimeout(GlobalConfig.SERVER_TIME_OUT);
		connection.setReadTimeout(GlobalConfig.SERVER_TIME_OUT);
		
		int count = GlobalConfig.SERVER_RECONNECT_TIMES;
		while(count>=0){
			try{
			
				connection.connect();
				break;
			}catch(SocketTimeoutException e){
				count--;	
			}
		}
		
		return connection;
	}

	/**
	 * http 请求
	 * 
	 * @param urlStr
	 * @return
	 */
	private String request(RequestType type, String urlStr, String data) {
		System.out.println("request: " + urlStr);
		
		StringBuffer result = null;
		HttpURLConnection connection = null;
		BufferedReader reader = null;
		try {
			connection = getConnection(type, urlStr);
			if (type != RequestType.GET) {
				writeData(connection, data);
			}
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String lines;
			result = new StringBuffer();
			while ((lines = reader.readLine()) != null) {
				lines = new String(lines.getBytes(), "utf-8");
				result.append(lines);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if( reader != null ){
					reader.close();
				}
				if( connection != null ){
					connection.disconnect();
				}
			} catch (IOException e) {
			}
		}

		return result != null ? result.toString() : null;
	}

	/**
	 * 写入 http post 数据
	 * 
	 * @param connection
	 * @param data
	 * @throws IOException
	 */
	private void writeData(HttpURLConnection connection, String data)
			throws IOException {
		DataOutputStream out = new DataOutputStream(connection
				.getOutputStream());
		out.writeBytes(data);
		out.flush();
		out.close();
	}
}
