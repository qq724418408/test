package com.bocop.jxplatform.util;

import android.app.Application;
import android.util.DisplayMetrics;

public class IApplication extends Application {
	public static int displayWidth;
	public static int displayHeight;
	public static String userid = "";
	public static String cookie = "";
	public static String passwd = "";

	public static boolean isHomeBack = false;

	@Override
	public void onCreate() {
		DisplayMetrics dm = getApplicationContext().getResources()
				.getDisplayMetrics();
		displayWidth = dm.widthPixels;
		displayHeight = dm.heightPixels;
	}
}
