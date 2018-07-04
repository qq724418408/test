package com.bocop.jxplatform.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

public class MyUtil {
	
	/**
	 * 获取应用版本号
	 * 
	 * @param context
	 * @return
	 */
	public static String getVersionName(Context context) {
		String versionName = "1.0.0";
		try {
			PackageInfo mPackageInfo = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			versionName = mPackageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionName;
	}

}
