/**
 * 
 */
package com.bocop.jxplatform.util;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.boc.bocop.sdk.BOCOPPayApi;
import com.boc.jx.base.BaseApplication;
import com.boc.jx.baseUtil.cache.CacheBean;
import com.boc.jx.common.util.ContentUtils;
import com.boc.jx.constants.Constants;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.activity.SplashActivity;
import com.bocop.jxplatform.config.BocSdkConfig;
import com.bocop.jxplatform.config.TransactionValue;
import com.bocop.jxplatform.util.BocOpUtil.CallBackBoc;
import com.bocop.jxplatform.util.LoginUtil.ILoginListener;
import com.bocop.xms.service.AlarmService;
import com.bocop.xms.utils.SharedPreferencesUtils;
import com.google.gson.Gson;

/** 
 * @author luoyang  E-mail: luoyang8714@163.com
 * @version 创建时间：2016-6-13 上午10:55:44 
 * 类说明 
 */
/**
 * @author zhongye
 * 
 */
public class CustomInfo {

	/**
	 * 
	 */
	public CustomInfo() {
		// TODO Auto-generated constructor stub
	}
	
	private static IIsCustom myCallback;
	public interface IIsCustom {
		void onCertification();

		void noCertification();

		void onCerError();

		void onCerException();

	}
	
	/*
	 * 判断SP是否已经报存客户信息
	 */
	public static boolean isExistCustomInfo(Context cxt) {
		final SharedPreferences sp = cxt.getSharedPreferences(
				Constants.CUSTOM_PREFERENCE_NAME, Context.MODE_PRIVATE);
		String idNO = sp.getString(Constants.CUSTOM_ID_NO, null);
		if (idNO == null) {
			Log.i("tag", "没有客户信息");
			return false;
		} else {
			Log.i("tag", "客户信息：" + idNO);
			return true;
		}
	}
	
	
	/*
	 * 判断SP是否已经报存客户信息
	 */
	public static void deleteCustomInfo(Context cxt) {
		Log.i("tag", "start deleteCustomInfo");
		final SharedPreferences sp = cxt.getSharedPreferences(
				Constants.CUSTOM_PREFERENCE_NAME, Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString(Constants.CUSTOM_ID_NO, null);
		editor.putString(Constants.CUSTOM_CUS_ID, null);
		editor.putString(Constants.CUSTOM_MOBILE_NO, null);
		editor.putString(Constants.CUSTOM_USER_NAME, null);
		editor.putString(Constants.CUSTOM_FLAG, null);
		editor.putString(Constants.CUSTOM_LOG_FLAG, null);
		editor.commit();
		Log.i("tag", "end deleteCustomInfo");
	}
	
	/*
	 * 删除闹钟信息信息
	 */
	public static void deleteAlarmInfo(Context cxt) {
		Log.i("tag", "start deleteAlarmInfo");
		SharedPreferencesUtils spf = new SharedPreferencesUtils(cxt, AlarmService.ALARM_SER);
		String userId = LoginUtil.getUserId(cxt);
		Log.i("logout", "移除" + userId + "数据");
		spf.delete(userId);
		Log.i("tag", "end deleteAlarmInfo");
	}
	
	public static void deleteConfigInfo(Context cxt){
		Log.i("tag", "start deleteConfigInfo");
		SharedPreferences sp = cxt.getSharedPreferences(LoginUtil.SP_NAME,
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor = sp.edit();
		IApplication.userid = "";
		BOCOPPayApi bocopSDKApi = BOCOPPayApi.getInstance(cxt,
				BocSdkConfig.CONSUMER_KEY, BocSdkConfig.CONSUMER_SECRET);
		bocopSDKApi.delOAuthorize(cxt);
		editor.putString(CacheBean.ACCESS_TOKEN, null);
		editor.putString(CacheBean.USER_ID, null);
		editor.putString(CacheBean.REFRESH_TOKEN, null);
		editor.putString(CacheBean.CUST_ID, null);
		editor.commit();
		Log.i("tag", "end deleteConfigInfo");
	}
	
	/*
	 * 获取客户号
	 */
	
	public static String getCustomId(Context cxt){
		final SharedPreferences sp = cxt.getSharedPreferences(
				Constants.CUSTOM_PREFERENCE_NAME, Context.MODE_PRIVATE);
		String customId = sp.getString(Constants.CUSTOM_CUS_ID, null);
		Log.i("tag","客户号"+ customId);
		return customId;
	}
	/*
	 * 判断SP是否已经报存客户信息
	 */
	public static void LoginOUt(Context cxt) {
		
		SharedPreferences sp = cxt.getSharedPreferences(LoginUtil.SP_NAME,
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		IApplication.userid = "";
		BOCOPPayApi bocopSDKApi = BOCOPPayApi.getInstance(cxt,
				BocSdkConfig.CONSUMER_KEY, BocSdkConfig.CONSUMER_SECRET);
		bocopSDKApi.delOAuthorize(cxt);
		editor.putString(CacheBean.ACCESS_TOKEN, null);
		editor.putString(CacheBean.USER_ID, null);
		editor.putString(CacheBean.REFRESH_TOKEN, null);
		editor.putString(CacheBean.CUST_ID, null);
		editor.commit();
		
		deleteCustomInfo(cxt);
	}

	/**
	 * 查询客户id
	 * 
	 * @param cxt
	 */
	public static void requestBocopForCustid(final Context cxt,
			boolean isShowDialog) {
		if(!LoginUtil.isLog(cxt)){
			return;
		}
		
		Gson gson = new Gson();
		Map<String, String> map = new HashMap<String, String>();
		map.put("USRID", LoginUtil.getUserId(cxt));
		final String strGson = gson.toJson(map);
		Log.i("tag", "请求客户信息");

		BocOpUtil bocOpUtil = new BocOpUtil(cxt);
		bocOpUtil.postOpboc(strGson, TransactionValue.SA0053, isShowDialog,
				new CallBackBoc() {

					@Override
					public void onSuccess(String responStr) {
						Log.i("tag", responStr);
						try {
							final SharedPreferences sp = cxt
									.getSharedPreferences(
											Constants.CUSTOM_PREFERENCE_NAME,
											Context.MODE_PRIVATE);
							Editor editor = sp.edit();

							Map<String, String> map;
							map = JsonUtils.getMapStr(responStr);
							Log.i("tag", "转换身份信息成功");
							if(map.get("idno").length() > 2){
								Log.i("tag", "实名认证客户");
								// 存储信息
								editor.putString(Constants.CUSTOM_ID_NO,
										map.get("idno"));// 身份证
								editor.putString(Constants.CUSTOM_CUS_ID,
										map.get("cusid"));// 客户号
								editor.putString(Constants.CUSTOM_MOBILE_NO,
										map.get("mobileno"));// 手机号
								editor.putString(Constants.CUSTOM_USER_NAME,
										map.get("cusname"));// 用户名
								editor.putString(Constants.CUSTOM_FLAG, "1");// 客户信息标志
																				// 1：已获取信息。2、已经上传信息(FOR
																				// XMS)
								editor.putString(Constants.CUSTOM_LOG_FLAG, "1");// 客户信息标志传送日志
																					// 1：已获取信息。2、已经上传信息(FOR
																					// LOG)
								editor.commit();
								Log.i("tag", "获取客户信息：idno：" + map.get("idno")
										+ "cusid：" + map.get("cusid"));
							}
							
							
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onStart() {
						Log.i("tag", "发送GSON数据：" + strGson);
					}

					@Override
					public void onFinish() {

					}

					@Override
					public void onFailure(String responStr) {
					}
				});
	}

	/*
	 * 传送ID和客户身份证
	 */
	public static void postIdForXms(final Context cxt) {

		final SharedPreferences sp = cxt.getSharedPreferences(
				Constants.CUSTOM_PREFERENCE_NAME, Context.MODE_PRIVATE);
		// 获取传送信息 1：初始标志，2：已经上送信息
		String flag = sp.getString(Constants.CUSTOM_FLAG, null);
		Log.i("tag", "flag;" + flag);
		if (flag == null) {
			Log.i("postIdForXms", "客户标志为空");
			return;
		}
		if (flag.equals("2")) {
			Log.i("postIdForXms", "信息已上传");
			return;
		}
		if (flag.equals("1")) {
			Log.i("postIdForXms", "开始上传信息");
			Gson gson = new Gson();
			Map<String, String> map = new HashMap<String, String>();
			String cardId = sp.getString(Constants.CUSTOM_ID_NO, null);
			Log.i("postIdForXms", "身份信息：" + cardId);
			if (cardId == null) {
				return;
			}
			String userId = LoginUtil.getUserId(cxt);

			map.put("userId", userId);
			map.put("cardId", cardId);
			map.put(Constants.eventId, Constants.login);
//			map.put("systemId", BocSdkConfig.CONSUMER_KEY);
			final String strGson = gson.toJson(map);
			QztRequestWithJsonAndHead qztRequestWithJsonAndHead = new QztRequestWithJsonAndHead(
					cxt);
			qztRequestWithJsonAndHead
					.postOpbocNoDialog(
							strGson,
							BocSdkConfig.qztPostForXmsUrl,
							new com.bocop.jxplatform.util.QztRequestWithJsonAndHead.CallBackBoc() {
								@Override
								public void onSuccess(String responStr) {
									Log.i("tag22", responStr);
									try {
										Editor editor = sp.edit();
										editor.putString(Constants.CUSTOM_FLAG,
												"2");
										editor.commit();
										Log.i("postIdForXms", "已经成功上传XMS信息");
									} catch (Exception e) {
										e.printStackTrace();
									}

								}

								@Override
								public void onStart() {
								}

								@Override
								public void onFailure(String responStr) {
									Log.i("tag33", responStr);
								}

								@Override
								public void onFinish() {
								}
							});
		}

	}

	/*
	 * 传送ID和客户身份证
	 */
	public static void postLog(final Context cxt, String eventId) {
		// 判定是否需要登陆验证
		Log.i("postLog", "事件" + eventId);
		final SharedPreferences sp = cxt.getSharedPreferences(
				Constants.CUSTOM_PREFERENCE_NAME, Context.MODE_PRIVATE);
		String userId;// 用户名
		String cardId;// 证件号码
		String cusNo;//客户号
		if (LoginUtil.isLog(cxt)) {
			userId = LoginUtil.getUserId(cxt);
		} else {
			userId = "anonymous";
		}
		cardId = sp.getString(Constants.CUSTOM_ID_NO, "anonymous");
		cusNo = sp.getString(Constants.CUSTOM_CUS_ID, "anonymous");
		Log.i("tag", "userId:" + userId + "cardId:" + cardId + "cusNo:" + cusNo);
		Log.i("postLog", "开始上传日志");
		
		Gson gson = new Gson();
		Map<String, String> map = new HashMap<String, String>();

		map.put("userId", userId);
		map.put("cardId", cardId);
		map.put(Constants.eventId, eventId);
		map.put("systemId", BocSdkConfig.CONSUMER_KEY);
		map.put("customerId", cusNo);
		
		final String strGson = gson.toJson(map);
		QztRequestWithJsonAndHead qztRequestWithJsonAndHead = new QztRequestWithJsonAndHead(
				cxt);
		qztRequestWithJsonAndHead
				.postOpbocNoDialog(
						strGson,
						BocSdkConfig.postForLog,
						new com.bocop.jxplatform.util.QztRequestWithJsonAndHead.CallBackBoc() {
							@Override
							public void onSuccess(String responStr) {
								Log.i("tag22", responStr);
								Log.i("postLog", "已经成功上传LOG");

							}

							@Override
							public void onStart() {
							}

							@Override
							public void onFailure(String responStr) {
								Log.i("tag33", responStr);
							}

							@Override
							public void onFinish() {
							}
						});

	}
	
	/*
	 * 传送ID和客户身份证
	 */
	public static void postNoCerLogWithLogin(final Context cxt, String eventId) {
		// 判定是否需要登陆验证
		Log.i("postLog", "事件" + eventId);
		String userId;// 用户名
		String cardId;// 证件号码
		String cusNo;//客户号
		if (LoginUtil.isLog(cxt)) {
			userId = LoginUtil.getUserId(cxt);
		} else {
			userId = "anonymous";
		}
		
		Gson gson = new Gson();
		Map<String, String> map = new HashMap<String, String>();

		map.put("userId", userId);
		map.put("cardId", "anonymous");
		map.put(Constants.eventId, eventId);
		map.put("systemId", BocSdkConfig.CONSUMER_KEY);
		map.put("customerId", "anonymous");
		
		final String strGson = gson.toJson(map);
		QztRequestWithJsonAndHead qztRequestWithJsonAndHead = new QztRequestWithJsonAndHead(
				cxt);
		qztRequestWithJsonAndHead
				.postOpbocNoDialog(
						strGson,
						BocSdkConfig.postForLog,
						new com.bocop.jxplatform.util.QztRequestWithJsonAndHead.CallBackBoc() {
							@Override
							public void onSuccess(String responStr) {
								Log.i("tag22", responStr);
								Log.i("postLog", "已经成功上传LOG");

							}

							@Override
							public void onStart() {
							}

							@Override
							public void onFailure(String responStr) {
								Log.i("tag33", responStr);
							}

							@Override
							public void onFinish() {
							}
						});

	}


	
	/**
	 * 查询客户id
	 * 
	 * @param cxt
	 */
	public static void requestBocopForCustidCallBack(final Context cxt,
			boolean isShowDialog,IIsCustom myCallback) {
		
		CustomInfo.myCallback = myCallback;
		if(!LoginUtil.isLog(cxt)){
			return;
		}
		
		Gson gson = new Gson();
		Map<String, String> map = new HashMap<String, String>();
		map.put("USRID", LoginUtil.getUserId(cxt));
		final String strGson = gson.toJson(map);
		Log.i("tag", "请求客户信息");
		
		final Message msg = mHandler.obtainMessage();
		msg.obj = cxt;

		BocOpUtil bocOpUtil = new BocOpUtil(cxt);
		bocOpUtil.postOpboc(strGson, TransactionValue.SA0053, isShowDialog,
				new CallBackBoc() {

					@Override
					public void onSuccess(String responStr) {
						Log.i("tag", responStr);
						try {
							final SharedPreferences sp = cxt
									.getSharedPreferences(
											Constants.CUSTOM_PREFERENCE_NAME,
											Context.MODE_PRIVATE);
							Editor editor = sp.edit();

							Map<String, String> map;
							map = JsonUtils.getMapStr(responStr);
							
							if(map.get("idno").length() > 2){
								// 存储信息
								editor.putString(Constants.CUSTOM_ID_NO,
										map.get("idno"));// 身份证
								editor.putString(Constants.CUSTOM_CUS_ID,
										map.get("cusid"));// 客户号
								editor.putString(Constants.CUSTOM_MOBILE_NO,
										map.get("mobileno"));// 手机号
								editor.putString(Constants.CUSTOM_USER_NAME,
										map.get("cusname"));// 用户名
								editor.putString(Constants.CUSTOM_FLAG, "1");// 客户信息标志
																				// 1：已获取信息。2、已经上传信息(FOR
																				// XMS)
								editor.putString(Constants.CUSTOM_LOG_FLAG, "1");// 客户信息标志传送日志
																					// 1：已获取信息。2、已经上传信息(FOR
																					// LOG)
								editor.commit();
								Log.i("tag", "获取客户信息：idno：" + map.get("idno")
										+ "cusid：" + map.get("cusid"));
								Log.i("tag", "回调函数：实名认证返回成功");
								msg.what = 0;
							}else{
								Log.i("tag", "回调函数：非实名认证返回成功");
								msg.what = 1;
							}
						} catch (Exception e) {
							e.printStackTrace();
							msg.what = 3;
						}
					}

					@Override
					public void onStart() {
						Log.i("tag", "发送GSON数据：" + strGson);
					}

					@Override
					public void onFinish() {
						mHandler.sendMessage(msg);		 //实名认证客户，存在身份证号 
					}

					@Override
					public void onFailure(String responStr) {
						msg.what = 2;
					}
				});
	}
	
	
	private static Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			Log.i("tag", "handleMessage");
			if (myCallback != null) {
				if (msg.what == 0) {
					myCallback.onCertification();
				} else if (msg.what == 1) {
					myCallback.noCertification();
				} else if (msg.what == 2) {
					myCallback.onCerError();
				}
				else if (msg.what == 3) {
					myCallback.onCerException();
				}
			}
		};
	};

	
	/*
	 * 传送ID和客户身份证
	 */
	public  String getTrafficLicPhone(final Context cxt) {

		final SharedPreferences sp = cxt.getSharedPreferences(
				Constants.CUSTOM_PREFERENCE_NAME, Context.MODE_PRIVATE);
		// 获取传送信息 1：初始标志，2：已经上送信息
		String flag = sp.getString(Constants.CUSTOM_FLAG, null);
		Log.i("tag", "flag;" + flag);
		if (flag.equals("1")) {
			Log.i("postIdForXms", "客户信息存在");
			Gson gson = new Gson();
			Map<String, String> map = new HashMap<String, String>();
			String cardId = sp.getString(Constants.CUSTOM_ID_NO, null);
			Log.i("postIdForXms", "身份信息：" + cardId);
			if (cardId == null) {
				return null;
			}
			String userId = LoginUtil.getUserId(cxt);
			
		}
		return null;
	}
}
