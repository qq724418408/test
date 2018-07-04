package com.boc.jx.tools;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.telephony.TelephonyManager;
import android.util.Log;


/**
 * 设备信息
 * <p>
 *     1.获取设备号：<br/>
 *     DeviceInfo info = new DeviceInfo(this);<br/>
 *     String deviceId = info.getDeviceId();<br/>
 *     2.获取应用信息：<br/>
 *     DeviceInfo info = new DeviceInfo(this);<br/>
 *     DeviceInfo.AppInfo appInfo = info.getAppInfo();<br/>
 *     String version = appInfo.getVersion();// 版本号<br/>
 *     String appName = appInfo.getAppName();// 应用名称<br/>
 *     Drawable appIcon = appInfo.getAppIcon();// 应用图标
 * </p>
 *
 * @author kejia
 */
public class DeviceInfo {
	
	private Context context;
	private SimpleDateFormat sdf;

	@SuppressLint("SimpleDateFormat")
	public DeviceInfo(Context context) {
		this.context = context;
		sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-sss");
	}

    /**
     * 获取设备号
     * <p>
     *     1.获取成功则使用该设备号<br/>
     *     2.获取失败则使用”年月日时分秒毫秒＋8位随机数”规则生成
     * </p>
     *
     * @return
     */
	public String getDeviceId() {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = tm.getDeviceId();
		if (imei == null || imei.length() < 1) {
			Date date = new Date();
			String time = sdf.format(date);
			Random rd = new Random();
			int r = rd.nextInt(89999999) + 10000000;
			imei = time + "-" + r;
		}
		Log.i("DeviceInfo", "DEVICE_ID: " + imei);
		
		return imei;
	}

    /**
     * 获取应用信息
     *
     * @return
     */
    public AppInfo getAppInfo() {
        try {
            PackageInfo mPackageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            AppInfo info = new AppInfo();
            info.setVersion(mPackageInfo.versionName);
            info.setAppName(mPackageInfo.applicationInfo.loadLabel(context.getPackageManager()).toString());
            info.setAppIcon(mPackageInfo.applicationInfo.loadIcon(context.getPackageManager()));

            return info;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 应用信息实体类
     */
    public class AppInfo {

        private String version;
        private String appName;
        private Drawable appIcon;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public Drawable getAppIcon() {
            return appIcon;
        }

        public void setAppIcon(Drawable appIcon) {
            this.appIcon = appIcon;
        }

    }
	
}
