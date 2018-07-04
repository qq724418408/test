package com.bocop.jxplatform.gesture.util;

import java.util.Date;

import com.bocop.jxplatform.util.IApplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.util.Base64;

public class LocusPassWordUtil {
	/**
	 * 取得手势密码
	 * 
	 * @return
	 */
	public static String getPassword(Context context) {
		SharedPreferences settings = context.getSharedPreferences(
				"GusturePassword", 0);
		String password = settings.getString("password", ""); // ,
																// "0,1,2,3,4,5,6,7,8"
		return password;
	}

	/**
	 * 取得手势密码的状态，是否是忘记密码后清空手势密码保存的
	 * 
	 * @param context
	 * @return
	 */
	public static Boolean getPasswordStatus(Context context) {
		SharedPreferences settings = context.getSharedPreferences(
				"GusturePassword", 0);
		Boolean status = settings.getBoolean("cleared", false); // ,
																// "0,1,2,3,4,5,6,7,8"
		return status;
	}

	/**
	 * 更新手势密码的状态，若进入手势密码界面，手势密码不为空，将此状态更改为false
	 * 
	 * @param context
	 */
	public static void resetPasswordStatus(Context context) {
		SharedPreferences settings = context.getSharedPreferences(
				"GusturePassword", 0);
		settings.edit().putBoolean("cleared", false).commit(); // ,
																// "0,1,2,3,4,5,6,7,8"
	}

	/**
	 * 重新设置密码，重置密码成功时，更改缓存中的值
	 * 
	 * @param context
	 *            上下文
	 * @param password
	 *            新的手势密码
	 * @param clear
	 *            清空手势密码，忘记密码时-
	 */
	public static void resetPassWord(Context context, String password,
			boolean clear) {
		SharedPreferences settings = context.getSharedPreferences(
				"GusturePassword", 0);
		Editor editor = settings.edit();
		editor.putString("password", password);
		editor.putBoolean("cleared", clear);
		editor.commit();
	}

	/**
	 * 清空密码，当login的user改变时，以及关闭密码时
	 * 
	 * @param context
	 */
	public static void clearPassWork(Context context) {
		resetPassWord(context, "", false);
	}

	/**
	 * 清空用户信息
	 * 
	 * @param context
	 */
	public static void clearUserData(Context context) {
		IApplication.userid = "";
		IApplication.cookie = "";
		IApplication.passwd = "";
		setUserData(context);
	}

	/**
	 * 加密缓存用户信息
	 * 
	 * @param context
	 */
	public static void setUserData(Context context) {
		String userid = getEncode(IApplication.userid);
		String passwd = getEncode(IApplication.passwd);
		String cookie = getEncode(IApplication.cookie);
		SharedPreferences settings = context.getSharedPreferences(
				"GusturePassword", 0);
		Editor editor = settings.edit();
		editor.putString("userid", userid);
		editor.putString("passwd", passwd);
		editor.putString("cookie", cookie);
		editor.commit();
	}

	/**
	 * 进行Base64加密
	 * 
	 * @param content
	 * @return
	 */
	private static String getEncode(String content) {
		return TextUtils.isEmpty(content) ? "" : new String(Base64.encode(
				content.getBytes(), Base64.DEFAULT));
	}

	/**
	 * 获取解密用户信息
	 * 
	 * @param context
	 */
	public static void getUserData(Context context) {
		SharedPreferences settings = context.getSharedPreferences(
				"GusturePassword", 0);
		IApplication.userid = getDecode(settings.getString("userid", ""));
		IApplication.passwd = getDecode(settings.getString("passwd", ""));
		IApplication.cookie = getDecode(settings.getString("cookie", ""));
	}

	/**
	 * 进行Base64解密
	 * 
	 * @param content
	 * @return
	 */
	private static String getDecode(String content) {
		return TextUtils.isEmpty(content) ? "" : new String(Base64.decode(
				content.getBytes(), Base64.DEFAULT));
	}

	/**
	 * 设置手势密码错误次数
	 * 
	 * @param context
	 * @param time
	 */
	public static void setErrorTime(Context context, int time) {
		SharedPreferences settings = context.getSharedPreferences(
				"GusturePassword", 0);
		Editor editor = settings.edit();
		editor.putInt("errorTime", time);
		editor.commit();
	}

	/**
	 * 获取手势密码错误次数
	 * 
	 * @param context
	 * @return
	 */
	public static int getErrorTime(Context context) {
		return context.getSharedPreferences("GusturePassword", 0).getInt(
				"errorTime", 0);
	}

	/**
	 * 设置通过Home键或者其他方式使系统后台运行的时间
	 * 
	 * @param context
	 */
	public static void setBackTime(Context context) {
		SharedPreferences settings = context.getSharedPreferences(
				"GusturePassword", 0);
		Editor editor = settings.edit();
		editor.putLong("lastTime", new Date().getTime());
		editor.commit();
	}

	/**
	 * 获取通过Home键或者其他方式使系统后台运行的时间
	 * 
	 * @param context
	 * @return
	 */
	public static long getBackTime(Context context) {
		return context.getSharedPreferences("GusturePassword", 0).getLong(
				"lastTime", new Date().getTime());
	}

	/** 手势密码保存 */
	public static void setHandPassword(Context context, boolean isSave) {
		SharedPreferences settings = context.getSharedPreferences(
				"GusturePassword", 0);
		Editor editor = settings.edit();
		editor.putBoolean("isSave", isSave);
		editor.commit();
	}

	/**
	 * 获取手势密码
	 * 
	 * @param context
	 * @return
	 */
	public static boolean getHandPassword(Context context) {
		return context.getSharedPreferences("GusturePassword", 0).getBoolean(
				"isSave", false);
	}
	/**
	 * 判断是否需要显示手势密码
	 * 
	 * @return
	 */
	public static boolean isShowGesture(Context context) {
		return StringUtil.isNotEmpty(IApplication.userid)
				&& getHandPassword(context);
	}
}
