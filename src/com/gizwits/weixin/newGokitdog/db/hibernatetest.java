package com.gizwits.weixin.newGokitdog.db;

import com.gizwits.weixin.newGokitdog.bean.DeviceBindInfo;

public class hibernatetest {

	public static void main(String[] args) {

		new hibernatetest().initDeciceBindInfo();
	}

	public void initDeciceBindInfo() {
		HibernateModel.getInstance().save(
				new DeviceBindInfo("oQn2EUDgiBjoEYPttvY8Ui", "gokit",
						"112233445566"));
		HibernateModel.getInstance().save(
				new DeviceBindInfo("74yrPjyC6EXHk8eRSJqe3Y", "gokit",
						"112233445567"));
		HibernateModel.getInstance().save(
				new DeviceBindInfo("eJjre3dxctEvQEZXiVWUWQ", "gokit",
						"112233445568"));
		HibernateModel.getInstance().save(
				new DeviceBindInfo("Es7cVP5YdgfHduauHgdQSj", "gokit",
						"112233445569"));
		HibernateModel.getInstance().save(
				new DeviceBindInfo("gdGn7PzAYf4VrhnVag5x8D", "gokit",
						"11223344556a"));
	}
}
