package com.gizwits.weixin.newGokitdog;


/**
 * 全局配置类
 * @author ads
 *
 */
public class GlobalConfig {

	public static final String DomainName = "";
	/**
	 * 机智云的AppId
	 */
	public static final String XCLOUD_APP_ID 	= "";	//xpg 云端 app id
	/**
	 * 机智云的产品识别码
	 */
	public static final String PRODUCT_KEY = "";
	
	/**
	 * 机智云的服务地址
	 */
	public static final String SERVER_URL = "http://api.gizwits.com";
	
	/**
	 * 机智云的服务地址
	 */
	public static final String M2M_SERVER_URL = "";
	
	/**
	 * 机智云的请求授
	 */
	public static final String M2M_REQUEST_AUTHORIZATION = "";
	

	public static final int REFRESH_DID_HOUR = 2;
	
	//TODO：测试请填上APP_ID 和 WX_APP_SECRECT
	public static final String WX_APP_ID = "";
	public static final String WX_APP_SECRECT = "";

	/**
	 * 服务器超时时间
	 */
	public static final int SERVER_TIME_OUT = 10000;//10s
	
	/**
	 * 服务器超时重连次数
	 */
	public static final int SERVER_RECONNECT_TIMES = 1;
	
	public static String signature = "";

	public static  String timestamp = "";

	public static  String nonce = "";
	
	public static  String echostr = "";
}
