package com.boc.jx.baseUtil.util;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;


/**
 * 常用问题解决工具类
 * 
 * @author 庄海滨
 */
public class MyUtils {
	/**
	 * 递归将视图布局里面的界面VIEW回收
	 * 
	 * @param root
	 */
	public static void recycle(ViewGroup root) {
		int childCount = root.getChildCount();
		for (int i = 0; i < childCount; ++i) {
			View v = root.getChildAt(i);
			if (v instanceof ViewGroup) {
				recycle((ViewGroup) v);
			} else {
				v = null;
			}
		}
	}

	/**
	 * 以最省内存的方式读取本地资源的图片
	 * 
	 * @param context
	 * @param resId
	 * @return
	 */
	public static Bitmap readBitMap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	/*** 关闭虚拟键盘 */
	public static void closeInputMethodWindow(Context context, View view) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null && view != null) {
			view.setFocusable(false);// 若设成true，键盘收了，但是出现焦点问题会导致键盘事件无法传递
			view.setFocusableInTouchMode(false);
			view.requestFocus();
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}

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

	// /**取消线程后可能不会调用前台的刷新方法了，而且也不能打算inbackground里面执行的代码，使用时要注意场合。
	// * 取消线程工具方法
	// * @param task
	// */
	// public static void cancelTask(AsyncTask<?, ?, ?> task)
	// {
	// if (task != null && task.getStatus() != AsyncTask.Status.FINISHED)
	// {
	// task.cancel(true);
	// task = null;
	// }
	// }

	/**
	 * 用第三方工具打开文件，支持doc、pdf、png、jpg/jpeg、txt
	 * 
	 * @param path
	 * @param ct
	 */
	public static void openFile(String path, Context ct) {
		String str = path.substring(path.lastIndexOf(".") + 1);
		str = str.toLowerCase();
		if (str.equals("doc")) {
			openWordFile(path, ct);
		} else if (str.equals("pdf")) {
			openPdfFile(path, ct);
		} else if (str.equals("png")) {
			openPngFile(path, ct);
		} else if (str.equals("jpeg") || str.equals("jpg")) {
			openJpegFile(path, ct);
		} else if (str.equals("txt")) {
			openTextFile(path, false, ct);
		} else {
			new AlertDialog.Builder(ct).setTitle("提示").setMessage("请安装阅读软件后查看附件信息.")
					.setPositiveButton("确定", new OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					}).show();
		}
	}

	// 用于打开Word文件
	public static void openWordFile(String path, Context context) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(path));
		intent.setDataAndType(uri, "application/msword");
		context.startActivity(intent);
	}

	// 用于打开JPEG文件
	public static void openJpegFile(String path, Context context) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(path));
		intent.setDataAndType(uri, "image/jpeg");
		context.startActivity(intent);
	}

	// 用于打开PNG文件
	public static void openPngFile(String path, Context context) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(path));
		intent.setDataAndType(uri, "image/png");
		context.startActivity(intent);
	}

	// 用于打开TXT文件
	public static void openTextFile(String path, boolean paramBoolean, Context context) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (paramBoolean) {
			Uri uri1 = Uri.parse(path);
			intent.setDataAndType(uri1, "text/plain");
		} else {
			Uri uri2 = Uri.fromFile(new File(path));
			intent.setDataAndType(uri2, "text/plain");
		}
		context.startActivity(intent);
	}

	// android获取一个用于打开PDF文件的intent
	public static void openPdfFile(String path, Context context) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(path));
		intent.setDataAndType(uri, "application/pdf");
		context.startActivity(intent);
	}

	/**
	 * 能进入此方法的前提是sd卡已经加载 注：调用前需判断SD卡是否存在
	 * 
	 * @return sd卡剩余空间
	 */
	public static long getSdRemainRoom() {
		File path = Environment.getExternalStorageDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return availableBlocks * blockSize;

	}

	/**
	 * 从字符串中截取连续6位数字 用于从短信中获取动态密码
	 * 
	 * @param str
	 *            短信内容
	 * @return 截取得到的6位动态密码
	 */
	public static String getDynamicPassword(String str) {
		Pattern continuousNumberPattern = Pattern.compile("[0-9\\.]+");
		Matcher m = continuousNumberPattern.matcher(str);
		String dynamicPassword = "";
		while (m.find()) {
			if (m.group().length() == 6) {
				System.out.print(m.group());
				dynamicPassword = m.group();
			}
		}

		return dynamicPassword;
	}

	/**
	 * 为网络请求加载共有的参数
	 * 
	 * @param paramList
	 *            请求参数集合
	 * @param imei
	 *            设备号
	 * @param lon
	 *            经度
	 * @param lat
	 *            纬度
	 * @param city
	 *            城市
	 * @param deviceToken
	 *            TOKEN号
	 */
	public static void setPublicRequest(List<NameValuePair> paramList, String imei, String lon,
			String lat, String city, String deviceToken) {
		paramList.add(new BasicNameValuePair("imei", imei));
		paramList.add(new BasicNameValuePair("lon", lon));
		paramList.add(new BasicNameValuePair("lat", lat));
		paramList.add(new BasicNameValuePair("city", city));
		paramList.add(new BasicNameValuePair("deviceToken", deviceToken));
	}

	/**
	 * 获取手机imei号
	 * 
	 * @param context
	 * @return
	 */

//	public static String getPhoneIMEI(Context context) {
//		String imei = BaseValue.DEVICE_ID;
//		try {
//			TelephonyManager manager = (TelephonyManager) context
//					.getSystemService(Context.TELEPHONY_SERVICE);
//			if (manager.getDeviceId() != null) {
//				return manager.getDeviceId();
//			} else {
//				WifiManager wifiManager = (WifiManager) context
//						.getSystemService(Context.WIFI_SERVICE);
//				if (wifiManager.getConnectionInfo().getMacAddress() != null) {
//					return wifiManager.getConnectionInfo().getMacAddress();
//				} else {
//					return manager.getSubscriberId();
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return imei;
//	}

}
