package com.bocop.user.utils;

import android.util.Log;

//import com.boc.bocaf.source.app.IApplication;

/**
 * 日志工具类
 * 
 * @author huwei
 * 
 */

public class Logger {

	private static final String TAG = "Logger";

	public static void d(String sMessage) {
//		if (IApplication.debugOn) {
			d(TAG, sMessage);
//		}
	}

	public static void d(String sTag, String sMessage) {
//		if (IApplication.debugOn) {
			if (null != sMessage) {
				Log.d(sTag, sMessage);
			}
//		}
	}

	// Warning Info
	public static void w(String sTag, String sMessage) {
//		if (IApplication.debugOn) {
			if (null != sMessage) {
				Log.w(sTag, sMessage);
			}
//		}
	}

	// Error Info
	public static void e(String sMessage) {
//		if (IApplication.debugOn) {
			if (null != sMessage) {
				e(TAG, sMessage);
			}
//		}
	}

	public static void e(String sTag, String sMessage) {
//		if (IApplication.debugOn) {
			if (null != sMessage) {
				Log.e(sTag, sMessage);
			}
//		}
	}

}
