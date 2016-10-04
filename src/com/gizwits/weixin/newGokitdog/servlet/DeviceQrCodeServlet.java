package com.gizwits.weixin.newGokitdog.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.gizwits.weixin.newGokitdog.service.GokitdogServiceImpl;
import com.gizwits.weixin.newGokitdog.service.IGokitdogService;

/**
 * Servlet implementation class DeviceQrCode
 */
@WebServlet("/DeviceQrCodeServlet")
public class DeviceQrCodeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeviceQrCodeServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String openId = StringUtils.trimToEmpty(request.getParameter("openId"));
		IGokitdogService gokitService = new GokitdogServiceImpl();
		String macAddr = gokitService.getMacAddr(openId, "");
		String qrcodeLink = gokitService.getQrCodeAddr(macAddr, "");
		request.setAttribute("macAddr", macAddr);
		request.setAttribute("qrcodeLink", qrcodeLink);
		request.getRequestDispatcher("/deviceQrcode.jsp").forward(request, response);	
	}

}
