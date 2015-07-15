package com.gizwits.weixin.newGokitdog.util;

import java.io.Serializable;

import com.gizwits.weixin.newGokitdog.bean.UserBindingDevice;
import com.gizwits.weixin.newGokitdog.service.GokitdogServiceImpl;
import com.gizwits.weixin.newGokitdog.service.IGokitdogService;
import com.gizwits.weixin.newGokitdog.service.XCloudNetworkService;
import com.gizwits.weixin.newGokitdog.service.BaseXCloudNetworkService.ResponseObject;

public class BindingThread implements Runnable,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UserBindingDevice data;
	private BindingCallback mBindingCallback;
	private WeixinRequest mWeixinRequest;
	
	public BindingThread(UserBindingDevice data,BindingCallback mbc,WeixinRequest wr){
		this.data=data;
		this.mBindingCallback=mbc;
		this.mWeixinRequest =wr;
	}
	
	@Override
	public void run() {
		XCloudNetworkService service = new XCloudNetworkService();
		ResponseObject result = bindingDeviceFlow(data,mWeixinRequest.getOpenId());
		
		mBindingCallback.BindingResult(result.getResult(),mWeixinRequest);
	}
	
	public interface BindingCallback{
		public void BindingResult(boolean isSuccess,WeixinRequest wr);
	}
	
	private static ResponseObject bindingDeviceFlow(UserBindingDevice data,
			String openId){
		 IGokitdogService gokitService = new GokitdogServiceImpl();
		XCloudNetworkService networkService = new XCloudNetworkService();
		
		ResponseObject result = networkService.bindingDevices(data, openId);
		// 云端绑定成功后本地服务器
		if (result.getResult()) {
			result = gokitService.bindingDevices(data, openId);
		}
		return result; 
	}

}
