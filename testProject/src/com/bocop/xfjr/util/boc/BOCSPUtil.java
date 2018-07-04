package com.bocop.xfjr.util.boc;

import com.boc.jx.base.BaseApplication;
import com.boc.jx.baseUtil.cache.CacheBean;
import com.bocop.jxplatform.activity.MainActivity;
import com.bocop.jxplatform.util.LoginUtil;
import com.bocop.xfjr.XfjrMain;
import com.bocop.xfjr.util.PreferencesUtil;
import com.bocop.xfjr.util.dialog.XFJRDialogUtil;
import com.bocop.xfjr.util.dialog.XFJRDialogUtil.DialogClick;
import com.bocop.xfjr.util.dialog.XfjrDialog;

import android.content.Context;
import android.view.View;

public class BOCSPUtil {

	// editor.putString(CacheBean.ACCESS_TOKEN, access_token);
	// editor.putString(CacheBean.USER_ID, userid);
	public static void initBOCSPUtil(Context context) {
		PreferencesUtil.init(context, LoginUtil.SP_NAME);
	}

	public static String getUserId() {
		return PreferencesUtil.get(CacheBean.USER_ID, "");
	}

	public static String getActoken() {
		return PreferencesUtil.get(CacheBean.ACCESS_TOKEN, "");
	}
	
	public static boolean isLogin(Context context) {
		return LoginUtil.isLog(context);
	}
	
	/**
	 * 判断易惠通是否登录,如果未登录回到易惠通首页重新登录
	 * 
	 * @param cxt
	 * @return
	 */
	public static void reLogin(Context context) {
		XfjrDialog dialog = XFJRDialogUtil.tipsDialog(context, "检测到易惠通未登录，请重新登录", new DialogClick() {
			
			@Override
			public void onOkClick(View view, XfjrDialog dialog) {
				BaseApplication app = (BaseApplication)XfjrMain.mApp;
				app.getActivityManager().finishAllWithoutActivity(MainActivity.class);
				dialog.cancel();
			}
			
			@Override
			public void onCancelClick(View view, XfjrDialog dialog) {
				BaseApplication app = (BaseApplication)XfjrMain.mApp;
				app.getActivityManager().finishAllWithoutActivity(MainActivity.class);
				dialog.cancel();
			}
		});
		dialog.show();
	}
	

}
