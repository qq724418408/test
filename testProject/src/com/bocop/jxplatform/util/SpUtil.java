/**
 * 
 */
package com.bocop.jxplatform.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.boc.jx.baseUtil.cache.CacheBean;

/** 
 * @author luoyang  E-mail: luoyang8714@163.com
 * @version 创建时间：2017-4-19 上午9:08:03 
 * 类说明 
 */
/**
 * @author zhongye
 *
 */
public class SpUtil {

	/**
	 * 
	 */
	public SpUtil(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
	}
	
	private String strMainDialog = "1";
	private Context mContext;
	public boolean useFirstForMainDialog(){
		try{
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd" + strMainDialog);
			Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
			String str = formatter.format(curDate);
			SharedPreferences sp = mContext.getSharedPreferences(
					CacheBean.AD_HASSHOW, Context.MODE_PRIVATE);
			Editor mEditor = sp.edit();
			mEditor.putBoolean(str, true);
			mEditor.commit();
			return true;
		}catch(Exception ex){
			Log.i("tag", "useFirstForMainDialog:" + ex.getMessage().toString());
			return false;
		}
		
	}
	
	public boolean isFirstForMainDialog(){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate) + strMainDialog;
		SharedPreferences sp = mContext.getSharedPreferences(
				CacheBean.AD_HASSHOW, Context.MODE_PRIVATE);
		boolean hasShowed = sp.getBoolean(str, false);
		if (!hasShowed) {
			Log.i("tag", "isFirstForMainDialog");
			return true;
		} else {
			Log.i("tag", "isFirstForMainDialog no");
			return false;
		}
	}

}
