package com.gizwits.weixin.newGokitdog.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.gizwits.weixin.newGokitdog.GlobalConfig;
import com.gizwits.weixin.newGokitdog.util.EncryptUtil;
import com.gizwits.weixin.newGokitdog.util.WeixinRequest;
import com.gizwits.weixin.newGokitdog.util.WeixinUtil;
import com.gizwits.weixin.newGokitdog.util.BindingThread.BindingCallback;

/**
 * Servlet implementation class WeixinServlet
 */
@WebServlet("/WeixinServlet")
public class WeixinServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String Token = "gokitdog";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public WeixinServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		GlobalConfig.signature = request.getParameter("signature");
		GlobalConfig.timestamp = request.getParameter("timestamp");
		GlobalConfig.nonce = request.getParameter("nonce");
		GlobalConfig.echostr = request.getParameter("echostr");
		if (StringUtils.isBlank(GlobalConfig.signature)
				|| StringUtils.isBlank(GlobalConfig.timestamp)
				|| StringUtils.isBlank(GlobalConfig.nonce)
				|| StringUtils.isBlank(GlobalConfig.echostr)) {
			return;
		}

		String[] ArrTmp = { Token, GlobalConfig.timestamp, GlobalConfig.nonce };
		Arrays.sort(ArrTmp);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < ArrTmp.length; i++) {
			sb.append(ArrTmp[i]);
		}
		String pwd = EncryptUtil.encrypt(sb.toString());

		if (pwd.equals(GlobalConfig.signature)) {
			if (!"".equals(GlobalConfig.echostr)
					&& GlobalConfig.echostr != null) {
				response.getWriter().print(GlobalConfig.echostr);
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		try {
			// 微信接入处理
			
			WeixinRequest wrt = WeixinUtil.WeixinRequsetAnalysis(request);
			if(wrt != null && null != wrt.getRequstType()){
				switch (wrt.getRequstType()) {
				case Wowo:
					break;
				case Xinqiu:
					WeixinUtil.DoXinqiu(wrt);
					break;
				case qrCodeScan:
					WeixinUtil.DoQrCodeSan(wrt,new mBindingResutl());
					break;
				}
			}
			
			//回复空字符给微信，表示接收成功，避免他重复请求
			response.setContentType("application/xml");
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			out.print("");
			out.flush();
			out.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
//判断是否显示使用说明
	class mBindingResutl implements BindingCallback {

		@Override
		public void BindingResult(boolean isSuccess, WeixinRequest wr) {
			String sb = "";

			if (isSuccess) {
				sb ="欢迎使用“窝窝”智能宠物屋，点击“窝窝”查看或者控制你的窝窝，点击“奴星球”查看或创建你的星球并邀请朋友一起互相看护X星人们";
			} else {
				sb="扫描绑定失败，请重试。";
			}
			try {
				WeixinUtil.SendCustomTextMsg( wr.getOpenId(), sb);

			} catch (Exception e) {

			}

		}

	}

}
