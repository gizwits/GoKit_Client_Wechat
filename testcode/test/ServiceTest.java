//package test;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import com.xpg.gokitdog.bean.Groups;
//import com.xpg.gokitdog.bean.UserBindingDevice;
//import com.xpg.gokitdog.bean.DeviceHistory.Type;
//import com.xpg.gokitdog.db.HibernateModel;
//import com.xpg.gokitdog.service.XCloudNetworkService;
//import com.xpg.gokitdog.service.BaseXCloudNetworkService.ResponseList;
//import com.xpg.gokitdog.service.BaseXCloudNetworkService.ResponseObject;
//
//public class ServiceTest {
//	public static void main(String[] args) {
//		// new ServiceTest().bindingDevicesTest();
//		// new ServiceTest().creatGroupTest();
//		// new ServiceTest().joinGroupTest();
//		//
//		// new ServiceTest().getDeviceHistoryDataTest();
//		// new ServiceTest().createUserTest();
//		 new ServiceTest().getDeviceTest();
//		// new ServiceTest().getGroupInfoTest();
//		//new ServiceTest().getGroupTest();
//	}
//
//	public void bindingDevicesTest() {
//		/*
//		 * ArrayList<UserBindingDevice> list = new
//		 * ArrayList<UserBindingDevice>(); for (int i = 0; i < 100; i++) {
//		 * UserBindingDevice u = new UserBindingDevice(); u.setOpenId("OpenId" +
//		 * 100); u.setQrCode("QrCode"); list.add(u); }
//		 * HibernateModel.getInstance().saveList(list);
//		 */
//
//		UserBindingDevice u = new UserBindingDevice();
//		u.setOpenId("123");
//		u.setQrCode("5");
//		HibernateModel.getInstance().save(u);
//		new XCloudNetworkService().bindingDevices(u, "123");
//	}
//
//	public void creatGroupTest() {
//		ResponseObject result = new XCloudNetworkService().createGroup("123",
//				"testgroup");
//		System.out.println("result: " + result.getSuccess());
//	}
//
//	public void joinGroupTest() {
//		for (int i = 0; i < 1; i++) {
//			new XCloudNetworkService().joinGroup("123", i + "");
//		}
//	}
//
//	public void getDeviceHistoryDataTest() {
//		Date date = new Date(Long.parseLong("1410796800000"));
//
//		new XCloudNetworkService().getDeviceHistoryData(null, Type.Temperature,
//				5, "996f59c1b600448b87e93f4a0ea7720d");
//	}
//
//	public void createUserTest() {
//		new XCloudNetworkService().createUser("123");
//	}
//
//	public void loginTest() {
//		new XCloudNetworkService().login("bob", "123456");
//	}
//
//	public void getDeviceTest() {
//		new XCloudNetworkService().getDevices("123");
//	}
//
//	public void getGroupInfoTest() {
//		new XCloudNetworkService().getGroupInfo("123", "0");
//	}
//
//	public void getGroupTest() {
//		ResponseList<Groups> list = new XCloudNetworkService().getGroup("123");
//
//		System.out.println("size: " + list.getSuccess().size());
//	}
//
//}
