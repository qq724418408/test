/**
 * 
 */
package com.bocop.jxplatform.receive;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

/** 
 * @author luoyang  E-mail: luoyang8714@163.com
 * @version 创建时间：2017-3-8 下午4:33:58 
 * 类说明 
 */
/**
 * @author zhongye
 *
 */
public class LoginBroadcast {

	/**
	 * 
	 */
	public LoginBroadcast() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * loginStatus:loginOn;loginOut
	 * @param cxt
	 * @param loginStatus
	 */
	public static void sendLoginBroadcast(Context cxt,String loginStatus){
		Intent intent = new Intent();
		intent.setAction("com.bocop.jxplatform.login");
		intent.putExtra("loginStatus", loginStatus);
		cxt.sendBroadcast(intent);
		Log.i("tag", "发送广播成功");
	}

}
