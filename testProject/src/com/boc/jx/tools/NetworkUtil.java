package com.boc.jx.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by llc on 14-9-12.
 */
public class NetworkUtil {

	private static final int WIFI = 1;
	private static final int CMWAP = 2;
	private static final int CMNET = 3;

	/***
	 * 判断用户是否连接网络(移动网络和WIFI)
	 *
	 * @param ctx
	 * @return
	 */
	public static boolean isNetworkAvailable(Context ctx) {
        ConnectivityManager manager = null;
        try {
        	manager = (ConnectivityManager) ctx.getApplicationContext().getSystemService(
                    Context.CONNECTIVITY_SERVICE);
		} catch (Exception e) {
		}
        if (manager == null) {
            return false;
        }
        NetworkInfo networkinfo = manager.getActiveNetworkInfo();
        if (networkinfo == null || !networkinfo.isAvailable()) {
            return false;
        }
        return true;
	}

	/**
	 * 判断是否有网络连接
	 *
	 * @param context
	 * @return
	 */
	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			try {
				ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
				if (mNetworkInfo != null) {
					return mNetworkInfo.isAvailable();
				} else {
					return false;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断WIFI网络是否可用
	 *
	 * @param context
	 * @return
	 */
	public static boolean isWifiConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (mWiFiNetworkInfo != null) {
				return mWiFiNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 判断MOBILE网络是否可用
	 *
	 * @param context
	 * @return
	 */
	public static boolean isMobileConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mMobileNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (mMobileNetworkInfo != null) {
				return mMobileNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 获取当前网络连接的类型信息
	 *
	 * @param context
	 * @return
	 */
	public static int getConnectedType(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
				return mNetworkInfo.getType();
			}
		}
		return -1;
	}

	/**
	 * 获取当前的网络状态 -1：没有网络 1：WIFI网络2：wap网络3：net网络
	 *
	 * @param context
	 * @return
	 */
	public static int getAPNType(Context context) {
		int netType = -1;
		ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo == null) {
			return netType;
		}
		int nType = networkInfo.getType();
		if (nType == ConnectivityManager.TYPE_MOBILE) {
			Log.e("networkInfo.getExtraInfo()", "networkInfo.getExtraInfo() is " + networkInfo.getExtraInfo());
			if (networkInfo.getExtraInfo().toLowerCase().equals("cmnet")) {
				netType = CMNET;
			} else {
				netType = CMWAP;
			}
		} else if (nType == ConnectivityManager.TYPE_WIFI) {
			netType = WIFI;
		}
		return netType;
	}

	/***
	 * 获取当前网速
	 *
	 * @return
	 */
	public static long getNetworkSpeed() {
		ProcessBuilder cmd;
		long readBytes = 0;
		BufferedReader rd = null;
		try {
			String[] args = { "/system/bin/cat", "/proc/net/dev" };
			cmd = new ProcessBuilder(args);
			Process process = cmd.start();
			rd = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
				if (line.contains("wlan0") || line.contains("eth0")) {
					// Log.i("获取流量整条字符串", line);
					String[] delim = line.split(":");
					if (delim.length >= 2) {
						String[] numbers = delim[1].trim().split(" ");// 提取数据
						readBytes = Long.parseLong(numbers[0].trim());//
						break;
					}
				}
			}
			rd.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (rd != null) {
				try {
					rd.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return readBytes;
	}

    /**
     * 判断所给地址是否为HTTPS请求地址
     */
    public static boolean isHTTPs(String url) {
        return (url.startsWith("https://") || url.startsWith("HTTPS://")) ? true : false;
    }
}
