package com.boc.jx.baseUtil.cache;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.boc.jx.base.UserInfo;

import android.os.Handler;
/**
 * 公共缓存对象
 */
public class CacheBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private static CacheBean cacheBean;

	public static CacheBean getInstance() {
		if (cacheBean == null) {
			cacheBean = new CacheBean();
			// initCityMap();
			return cacheBean;
		}
		return cacheBean;
	}

	/** 保存一些缓存数据缓存标记 */
	private Map<String, Object> cacheMap = new HashMap<String, Object>();

//	/** 保存任务栈中的FragmentBean */
//	public List<FragmentBean> fragmentList = new ArrayList<FragmentBean>();

	/** 基类Handler */
	private Handler handler;

	public final static String USER_TEL_LOGIN = "user_tel_login";
	public final static String USER_ID = "user_id";
	public final static String DZP_USER_ID = "dzp_user_id";
	public final static String ACCESS_TOKEN = "access_token";
	public final static String REFRESH_TOKEN = "refresh_token";
	public final static String EXPIRES_IN  = "expires_in";
	public final static String CUST_ID = "cust_id";
	public final static String AD_HASSHOW = "ad_hasshow";
	public final static String DZP_HASSHOW = "dzp_hasshow";

	/************************** 以上属于不可清除缓存 **************************/

	/************************** 以下属于登出后必须清除缓存 **************************/

	/**
	 * 保存数据到缓存
	 */
	public void put(String key, Object value) {
		if (value == null) {
			cacheMap.remove(key);
			return;
		}
		cacheMap.put(key, value);
	}

	/**
	 * 从缓存中取出数据
	 */
	public Object get(String key) {
		return cacheMap.get(key);
	}

	/**
	 * 清除所有缓存
	 */
	public void clearCacheMap() {
		cacheMap.clear();
	}

	/**
	 * 清理cacheBean
	 */
	public void clearCache() {
		clearCacheMap();
		cacheBean = null;
	}


	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	/**
	 * 获取初始Fragment
	 * 
	 * @return
	 */
//	public FragmentBean getAttachFragment() {
//		return (FragmentBean) get("attach_fragment");
//	}

	/**
	 * 设置初始Fragment
	 * 
	 * @param bean
	 */
//	public void setAttachFragment(FragmentBean bean) {
//		put("attach_fragment", bean);
//	}

	/**
	 * 获取用户信息
	 * 
	 * @return
	 */
	public UserInfo getUserInfo() {
		if (get("user_info") == null) {
			UserInfo info = new UserInfo();
			put("user_info", info);
		}
		return (UserInfo) get("user_info");
	}

	/**
	 * 设置用户信息
	 * 
	 * @param info
	 */
	public void setUserInfo(UserInfo info) {
		put("user_info", info);
	}

	/**
	 * 设置红包ID
	 * @param redBagid
	 */
	public void setCount(String count){
		put("COUNT", count);
	}
	
	public String getCount(){
		if (get("COUNT") == null) {
			put("COUNT", "");
		}
		return (String) get("COUNT");
	}
	
	public void clearUserCache() {
		cacheBean.clearCache();
	}

}