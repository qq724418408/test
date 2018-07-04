package com.bocop.user.app;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;




/**
 * @author wangwx
 * 
 */
@SuppressLint("SdCardPath")
public class IApplication extends Application {
	public static final boolean debugOn = true;// 日志打印开关
	private static Context mContext;
	public static int displayWidth;
	public static int displayHeight;
	public static int slidingMenuRightOffset;
	public static String cookie = "";// 用户本次登录后的cookie
	public static String userid = "";// 用户本次登录后的ID
	public static String accessToken = "";// 用户本次登录后的ID
	public static String passwd = "";// 用户本次登陆后的密码
	public static boolean isSavePassword = false;// 本地是否成功保存过手势密码
	public static boolean isHome = false;// 是否点击了HOME退出键
	public static boolean isLogin = false;// 是否点击了HOME退出键
	public static boolean paypwdflag;//用户是否已设置支付密码
	public static boolean paypwd;//用户是否已设置支付密码


	public static String userName = "";
	public static String code="";
	public static String tests="";

	public static String usrid_s="";
	public static String idnum_s="";
	public static String usrnam_s="";
	public static String idtype="";
	public static String idtypeName="";
	public static String usrtel_s="";
	public static String usrtel="";
	public static final String CLIENTID="151";
	public static final String CLIENTSECRET="292989428bdb86fa330d4c8d3aa57964ede05ee5b20b0ba05d";
//	public final static String APSURL="http://22.189.22.196";//local
	public static String APSURL="http://22.188.12.104";//x64
//	public  static String APSURL="http://180.168.146.78";//U1

//	public static MineInfo mineInfo = new MineInfo();// 用户个人信息
	// 手势密码锁定时间
	public static final long LOCKED_TIME = 5 * 60 * 1000;
	/**
	 * 记录用户附加信息姓名
	 */
	public static String username;

	// etoken是否绑定 默认未绑定
	public static boolean isBindEtoken = false;

	// 购汇
	public static int paddingLeft;
	public static int paddingTop;
	public static int margin;
	public static LinkedList<Activity> activitiyList = new LinkedList<Activity>();
	public static LinkedList<Activity> activities = new LinkedList<Activity>();

	/**
	 * baidu map
	 */

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = getApplicationContext();
		// 程序异常信息报错
		DisplayMetrics dm = getApplicationContext().getResources()
				.getDisplayMetrics();
		displayWidth = dm.widthPixels;
		displayHeight = dm.heightPixels;
		slidingMenuRightOffset = (int) (displayWidth * 0.6);
	}

	public static void destoryActivity() {
		for (Activity a : activities) {
			a.finish();
		}
	}

	public static void AddActivity(Activity activity) {
		activitiyList.add(activity);
	}

	public static void exitApp() {
		for (Activity activity : activitiyList) {
			activity.finish();
		}
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);
	}

	/**
	 * 判断网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkinfo = manager.getActiveNetworkInfo();
		if (networkinfo == null)
			return false;
		return networkinfo.isAvailable() && networkinfo.isConnected();
	}
	/**
	 * 判断是否登出
	 * 
	 * @param result
	 * @return
	 */
	public static boolean isLoginOut(String result) {
		return "ASR-000003".contains(result) || "ASR-000004".equals(result)
				|| "ASR-000005".equals(result) || "ASR-000006".equals(result)
				|| "invalid_token".equals(result)
				|| "invalid_grant".equals(result);
	}

	/**
	 * 解绑etoken
	 */
	public static void unBindEtoken() {
		isBindEtoken = false;
	}

	/**
	 * 绑定etoken
	 */
	public static void bindEtoken() {
		isBindEtoken = true;
	}

	/**
	 * 获取Context对象
	 * 
	 * @return
	 */
	public static Context getContext() {
		return mContext;
	}
}
