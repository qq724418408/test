package com.bocop.xfjr.util.gps;

import com.boc.jx.base.BaseActivity;

import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;

public class GPSUtil {

	/**
	 * 打开GPS
	 * 
	 * @param activity
	 */
	public static void openGPS(BaseActivity activity) {
		Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		activity.startActivityForResult(intent, 0);
	}

	/**
	 * 判断gps是否打开
	 * 
	 * @param lm
	 * @return
	 */
	public static boolean isGpsAble(LocationManager lm) {
		return lm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
	}

}
