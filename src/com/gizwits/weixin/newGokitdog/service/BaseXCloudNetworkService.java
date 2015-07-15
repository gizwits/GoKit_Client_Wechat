package com.gizwits.weixin.newGokitdog.service;

import java.util.ArrayList;
import java.util.List;

import com.gizwits.weixin.newGokitdog.GlobalConfig;
import com.gizwits.weixin.newGokitdog.util.XCloudNetworkUtils;
import com.gizwits.weixin.newGokitdog.util.XCloudNetworkUtils.RequestParams;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Xcloud 网络服务基础类
 * 
 * @author ads
 * 
 */
public class BaseXCloudNetworkService {

	String Tag = this.getClass().getSimpleName();

	static final String OBJECTID_KEY = "objectId";
	static final String ERROR_CODE_KEY = "error_code";
	static final String USER_OBJECTID_KEY = "userObjectId";
	static final String USER_ID_KEY = "userId";
	static final String DISH_ID_KEY = "dishId";
	static final String TOPIC_ID_KEY = "topicId";
	static final String RESULTS_KEY = "data";
	static final String GET_DEVICE_LATEST_DATA_RESULTS_KEY = "attr";
	static final String URL_KEY = "url";
	static final String CREATEDAT_KEY = "createdAt";
	static final String UPDATEDAT_KEY = "updated_at";
	static final String WHERE_KEY = "where";
	static final String EMAIL_KEY = "email";
	static final String IS_AVAILABLE_KEY = "isAvailable";
	static final String TYPE_KEY = "type";

	/**
	 * XCloud API名
	 * 
	 * @author ads
	 * 
	 */
	public enum XCloudApi {
		app, dev
	}

	/**
	 * XCloud 存储表名
	 * 
	 * @author ads
	 * 
	 */
	public enum XCloudClassName {
		users, devdata, login, bindings,devices
	}

	/**
	 * XCloud 查询操作符号
	 * 
	 * @author ads
	 * 
	 */
	public enum XCloudOperationSymbol {
		$or, $and
	}

	/**
	 * XCloud 查询比较符号
	 * 
	 * @author ads
	 * 
	 */
	public enum XCloudCompareSymbol {
		/**
		 * <=
		 */
		$lte,
		/**
		 * >=
		 */
		$gte,
		/**
		 * >
		 */
		$gt,

		$lt
	}

	/**
	 * 菜谱数量统计类型
	 * 
	 * @author ads
	 * 
	 */
	protected enum DishAmountType {
		Collected, Discussion, Demo
	}

	/**
	 * 用于updateDishAmount, plus = +1, minus = -1
	 * 
	 */
	protected enum DishExtraUpdateType {
		plus, minus;
	}

	/**
	 * 文章(作品、评论) 类型
	 * 
	 * @author ads
	 * 
	 */
	public enum PostsType {
		/**
		 * 作品
		 */
		Posts,
		/**
		 * 菜谱评论(回复)
		 */
		DishesReply,
		/**
		 * 菜谱参照作品
		 */
		DishesPosts,

		/**
		 * 作品评论(回复)
		 */
		PostsReply
	}

	public BaseXCloudNetworkService() {
	}

	protected XCloudNetworkUtils getNetworkUtils(String token) {
		return new XCloudNetworkUtils(GlobalConfig.SERVER_URL, token);
	}

	protected XCloudNetworkUtils getNetworkUtils(String serverUrl, String token) {
		return new XCloudNetworkUtils(serverUrl, token);
	}

	/**
	 * 服务器返回结果基础类
	 * 
	 * @author ads
	 */
	public static class ResponseBase {
		protected boolean result = false;
		protected ErrorMessage failure;
		protected String updated_at;

		public boolean getResult() {
			return result;
		}
		
		public void setResult(boolean result){
			this.result = result;
		}

		public ErrorMessage getFailure() {
			return failure;
		}

		public class ErrorMessage {

			private String error_code;

			public String getErrorCode() {
				return error_code;
			}
		}

	}

	/**
	 * 服务器返回单一结果-对象
	 * 
	 * @author ads
	 * 
	 * @param <T>
	 */
	public static class ResponseObject<T> extends ResponseBase {
		protected T success;

		public T getSuccess() {
			return success;
		}
	}

	/**
	 * 服务器返回多个结果-列表
	 * 
	 * @author ads
	 * 
	 * @param <T>
	 */
	public static class ResponseList<T> extends ResponseBase {
		protected List<T> success;

		public List<T> getSuccess() {
			return success;
		}
	}

	/**
	 * 搜索数据
	 * 
	 * @param className
	 * @param objectId
	 * @param clazz
	 * @return
	 */
	protected <T> ResponseList<T> searchData(XCloudClassName className,
			RequestParams params, String arg, String token, final Class<T> clazz) {
		final ResponseList<T> result = new ResponseList<T>();

		String request = getNetworkUtils(token).get(XCloudApi.app, className,
				params, arg);

		if (request != null) {
			result.result = true;
			List<T> list = new ArrayList<T>();
			System.out.println("request: " + request);
			JsonArray jsonArray = new JsonParser().parse(request)
					.getAsJsonObject().getAsJsonArray(RESULTS_KEY);
			if (jsonArray != null) {
				for (int cou = 0; cou < jsonArray.size(); cou++) {
					list.add(new Gson().fromJson(jsonArray.get(cou).toString(),
							clazz));
				}
			}
			result.success = list;
		}

		return result;
	}

	/**
	 * 通用创建对象方法
	 * 
	 * @param xCloudApi
	 * @param className
	 * @param obj
	 * @param clazz
	 * @return
	 */
	protected <T> ResponseObject<T> createObject(XCloudApi xCloudApi,
			XCloudClassName className, Object obj, final Class<T> clazz) {
		final ResponseObject<T> result = new ResponseObject<T>();

		String json = new Gson().toJson(obj);
		System.out.println("json: " + json);
		if (xCloudApi == null) {
			xCloudApi = XCloudApi.app;
		}

		String request = getNetworkUtils(null).post(xCloudApi, className, json);
		if (request != null) {
			result.result = true;
			result.success = new Gson().fromJson(request, clazz);
		}

		return result;
	}

	/**
	 * 通用POST请求方法
	 * 
	 * @param xCloudApi
	 * @param className
	 * @param obj
	 * @param token
	 * @param clazz
	 * @return
	 */
	protected <T> ResponseObject<T> post(XCloudApi xCloudApi,
			XCloudClassName className, Object obj, String token,
			final Class<T> clazz) {
		final ResponseObject<T> result = new ResponseObject<T>();

		String json = new Gson().toJson(obj);
		System.out.println("json: " + json);
		if (xCloudApi == null) {
			xCloudApi = XCloudApi.app;
		}
		String request = getNetworkUtils(token)
				.post(xCloudApi, className, json);
		System.out.println("result： " + request);
		if (request != null) {
			result.result = true;
			result.success = new Gson().fromJson(request, clazz);
		}
		return result;
	}

	/**
	 * 通用GET请求方法
	 * 
	 * @param xCloudApi
	 * @param className
	 * @param obj
	 * @param token
	 * @param clazz
	 * @return
	 */
	protected <T> ResponseObject<T> get(XCloudApi xCloudApi,
			XCloudClassName className, String path, final Class<T> clazz) {
		final ResponseObject<T> result = new ResponseObject<T>();

		if (xCloudApi == null) {
			xCloudApi = XCloudApi.app;
		}

		String request = getNetworkUtils("").get(xCloudApi, className, path);
		System.out.println("result： " + request);

		if (request != null) {
			JsonObject respond=new JsonParser().parse(request)
					.getAsJsonObject();
			if(null == respond.getAsJsonPrimitive(UPDATEDAT_KEY) ){
				result.updated_at = "";
			}else{
				result.updated_at=respond.getAsJsonPrimitive(UPDATEDAT_KEY).toString();
			}
			
			result.result = true;
			JsonObject jsonArray = respond
					.getAsJsonObject(GET_DEVICE_LATEST_DATA_RESULTS_KEY);
			if(null == jsonArray ){
				result.success = null;
			}else{
				result.success=new Gson().fromJson(jsonArray.toString(), clazz);
			}
		}
		return result;
	}
	
	/**
	 * 通用DELETE请求方法
	 * 
	 * @param xCloudApi
	 * @param className
	 * @param obj
	 * @param token
	 * @param clazz
	 * @return
	 */
	protected <T> ResponseObject<T> delete(XCloudApi xCloudApi,
			XCloudClassName className, Object obj, String token,
			final Class<T> clazz) {
		final ResponseObject<T> result = new ResponseObject<T>();

		String json = new Gson().toJson(obj);
		System.out.println("json: " + json);
		if (xCloudApi == null) {
			xCloudApi = XCloudApi.app;
		}
		String request = getNetworkUtils(token)
				.delete(xCloudApi, className, json);
		System.out.println("result： " + request);
		if (request != null) {
			result.result = true;
			result.success = new Gson().fromJson(request, clazz);
		}
		return result;
	}
}
