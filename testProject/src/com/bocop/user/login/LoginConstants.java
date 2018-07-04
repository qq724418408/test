package com.bocop.user.login;

public class LoginConstants {
	// private static final String IP = "http://22.189.5.181";
	// private static final String IP = "http://22.188.12.104";
//	 private static final String IP = "https://open.boc.cn";// 生产
//	private static final String IP = "http://22.188.12.115";//
//	 private static final String IP = "http://22.188.146.47";//T1
	 private static final String IP = "http://22.188.12.104";
	// private static final String IP = "http://22.188.12.102";
	// private static final String IP = "http://22.188.146.250";//T2 李保辉专用

	public static String getLoginUrl() {
		// return IP + "/public/junmp.html";
		// return IP +
		// "/wap/login/login?logintype=1&setcolor=&devicetype=2&clientid=170&act=register";
		// return IP +
		// "/public/app/#/login?logintype=1&setcolor=&devicetype=1&clientid=549&act=register";
		// return IP + "/public/app/#/login";
		return IP + "/mobile/login";
		// return IP + "/mobile/login#/login";
	}

}