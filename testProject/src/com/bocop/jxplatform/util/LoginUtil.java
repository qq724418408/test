package com.bocop.jxplatform.util;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.boc.bocop.sdk.BOCOPPayApi;
import com.boc.bocop.sdk.api.bean.ResponseBean;
import com.boc.bocop.sdk.api.bean.oauth.BOCOPOAuthInfo;
import com.boc.bocop.sdk.api.bean.oauth.RegisterResponse;
import com.boc.bocop.sdk.api.event.ResponseListener;
import com.boc.jx.base.BaseActivity;
import com.boc.jx.base.BaseApplication;
import com.boc.jx.baseUtil.cache.CacheBean;
import com.boc.jx.common.util.ContentUtils;
import com.boc.jx.constants.Constants;
import com.bocop.jxplatform.config.BocSdkConfig;
import com.bocop.jxplatform.config.TransactionValue;
import com.bocop.jxplatform.fragment.HomeFragment;
import com.bocop.jxplatform.gesture.util.LocusPassWordUtil;
import com.bocop.jxplatform.receive.LoginBroadcast;
import com.bocop.jxplatform.util.BocOpUtil.CallBackBoc;
import com.bocop.jxplatform.util.CspUtil.CallBack;
import com.bocop.xms.push.service.OnlineService;
import com.bocop.xms.service.AlarmService;
import com.bocop.xms.service.AlarmServiceManager;
import com.bocop.xms.utils.SharedPreferencesUtils;
import com.bocop.xms.xml.CspXmlXmsCom;
import com.bocop.xms.xml.remind.EventBean;
import com.bocop.xms.xml.remind.EventListResp;
import com.bocop.xms.xml.remind.EventListXmlBean;
import com.google.gson.Gson;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class LoginUtil {

	static CacheBean cacheBean;
	// private static SharedPreferences sp;

	public static final String SP_NAME = "config";
	public static final String PATTERN_NAME = "pattern";
	public static final String TIME_NAME = "time";

	private static Editor editor;
	private static ILoginListener myCallback;

	private static HashMap<String, OnMsgServiceOkListener> mListeners = new HashMap<String, OnMsgServiceOkListener>();
	private static final String TAG = "LoginUtil";
	private static Context context;
	public static OnlineService onlineService = null;

	public interface ILoginListener {
		void onLogin();

		void onCancle();

		void onError();

		void onException();

	}

	public interface ILogoutListener {
		void onLogout();

	}

	private static Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
//			CustomInfo.deleteCustomInfo(base);
			
			if (myCallback != null) {
				if (msg.what == 0) {
					Context ctx = (Context) msg.obj;
//					SharedPreferences sp = context.getSharedPreferences(
//							LoginUtil.SP_NAME, Context.MODE_PRIVATE);
//					Log.i("tag", "sp");
//					SharedPreferences spcus = context.getSharedPreferences(
//							Constants.CUSTOM_PREFERENCE_NAME, Context.MODE_PRIVATE);
//					Log.i("tag", "spcus");
//					if (sp.getString(CacheBean.CUST_ID, null) == null || spcus.getString(Constants.CUSTOM_CUS_ID, null) == null) {
//						try{
//						requestBocopForCustid(context, false);}
//						catch(Exception ex){
//							Log.i("exception", ex.getMessage());
//						}
//					}else{Log.i("tag33", "no requestBocopForCustid");}
					Log.i("LoginUtil", "isExistCustomInfo");
					if(!CustomInfo.isExistCustomInfo(ctx)){
						Log.i("LoginUtil", "requestBocopForCustid");
						CustomInfo.requestBocopForCustid(ctx, false);
						CustomInfo.postNoCerLogWithLogin(ctx,Constants.login);
					}else{
						Log.i("LoginUtil", "postIdForXms");
						CustomInfo.postIdForXms(ctx);
					}
					
					
					myCallback.onLogin();
				} else if (msg.what == 1) {
					myCallback.onCancle();
				} else if (msg.what == 2) {
					myCallback.onError();
				} else if (msg.what == 3) {
					myCallback.onException();
				} else if (msg.what == 6) {
					Context ctx = (Context) msg.obj;
					
					Log.i("LoginUtil", "isExistCustomInfo");
					if(!CustomInfo.isExistCustomInfo(ctx)){
						Log.i("LoginUtil", "requestBocopForCustid");
						CustomInfo.requestBocopForCustid(ctx, false);
					}else{
						Log.i("LoginUtil", "postIdForXms");
						CustomInfo.postIdForXms(ctx);
					}
					
					myCallback.onLogin();
//					Intent intent = null;
//					if (LocusPassWordUtil.getHandPassword(context)) {
//						intent = new Intent(context,
//								GuideGesturePasswordActivity.class);
//					} else
//						intent = new Intent(context,
//								CreateGesturePasswordActivity.class);
//					int what = (Integer) msg.obj;
//					intent.putExtra("isSetting", true);
//					intent.putExtra("what", what);
//					intent.putExtra("page", 1);
//					context.startActivity(intent);
				} else if (msg.what == 7) {
					authorize(context, myCallback);
				}
			}
		};
	};

	// private static void init(Context cxt) {
	// if (cacheBean == null) {
	// cacheBean = CacheBean.getInstance();
	// }
	// if (editor == null) {
	// sp = cxt.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
	// editor = sp.edit();
	// }
	// }

	/**
	 * 用于调用第三方sdk
	 */

	public static void authorize(final Context cxt, ILoginListener myCallback) {
		
		Log.i("tag", "authorize");
		//删除闹钟数据
		CustomInfo.deleteAlarmInfo(cxt);
		//删除SP_NAME 信息
		CustomInfo.deleteConfigInfo(cxt);
		//删除信息
		CustomInfo.deleteCustomInfo(cxt);
		
		final SharedPreferences sp = cxt.getSharedPreferences(
				LoginUtil.SP_NAME, Context.MODE_PRIVATE);
		editor = sp.edit();

		LoginUtil.myCallback = myCallback;
		BOCOPPayApi bocopSDKApi = BOCOPPayApi.getInstance(cxt,
				BocSdkConfig.CONSUMER_KEY, BocSdkConfig.CONSUMER_SECRET);
		bocopSDKApi.initURLIPPort(cxt, BocSdkConfig.CONSUMER_URL,
				BocSdkConfig.CONSUMER_PORT, BocSdkConfig.CONSUMER_IS_REGISTER,
				BocSdkConfig.CONSUMER_REGISTER);
		// bocopSDKApi.authorize(context, listener);
		bocopSDKApi.authorize(cxt, new ResponseListener() {
			// bocopSDKApi.authorizeAsr(cxt,true, new ResponseListener() {
			@Override
			public void onException(Exception arg0) {
				mHandler.sendEmptyMessage(3);
			}

			@Override
			public void onError(Error arg0) {
				mHandler.sendEmptyMessage(2);
			}

			@Override
			public void onComplete(ResponseBean response) {
				// com.boc.bocop.sdk.api.bean.oauth.RegisterResponse
				String className = response.getClass().getName();
				Message msg = new Message();
				msg.obj = cxt;
				msg.what = 0;
				if (className.contains("BOCOPOAuthInfo")) {
					BOCOPOAuthInfo info = (BOCOPOAuthInfo) response;
					String access_token = info.getAccess_token();
					String refresh_token = info.getRefresh_token();
					String userid = info.getUserId();
					IApplication.userid = userid;
					if (access_token != null && access_token.length() > 0) {// 登录成功后将token和useid存储到本地
						editor.putString(CacheBean.ACCESS_TOKEN, access_token);
						editor.putString(CacheBean.USER_ID, userid);
						editor.putString(CacheBean.DZP_USER_ID, userid);
						editor.putString(CacheBean.REFRESH_TOKEN, refresh_token);
						editor.commit();
						
						//发送登陆成功消息
						Log.i("tag", "开始发送广播");
						LoginBroadcast.sendLoginBroadcast(cxt,"loginOn");
						
						success(cxt, 0);
						
						

						// Intent startSrv = new Intent(cxt,
						// OnlineService.class);
						// startSrv.putExtra(OnlineService.START_FROM, this
						// .getClass().getName());
						// startSrv.putExtra(OnlineService.NAME_CMD,
						// OnlineService.VALUE_RESET);
						// cxt.startService(startSrv);

						// 开启推送服务
						BaseApplication.startGoPush(userid);
						Log.i("tag23", "startGoPush");
						requestRemindList(cxt, userid);
						
						HomeFragment.gotoDZP(cxt);
						//开始同送信息
					}
				} else if (className.contains("RegisterResponse")) {
					RegisterResponse regInfo = (RegisterResponse) response;
					String access_token = regInfo.getAccess_token();
					String userid = regInfo.getUserid();
					IApplication.userid = userid;
					editor.putString(CacheBean.ACCESS_TOKEN, access_token);
					editor.putString(CacheBean.USER_ID, userid);
					editor.putString(CacheBean.DZP_USER_ID, userid);

					editor.commit();
					
					
					success(cxt, 0);
					
					// 开启推送服务
					BaseApplication.startGoPush(userid);
					requestRemindList(cxt, userid);
					
					HomeFragment.gotoDZP(cxt);
				}
			}

			@Override
			public void onCancel() {
				mHandler.sendEmptyMessage(1);
			}

		});
	}

		
	/**
	 * 成功回调
	 */
	private static void success(Context cxt, int what) {
		context = cxt;
		if (LocusPassWordUtil.isShowGesture(cxt)) {
			Log.i("log", "isShowGesture");
			mHandler.sendEmptyMessage(what);
		} else {
			Log.i("log", "no isShowGesture");
			Message msg = mHandler.obtainMessage();
			msg.obj = cxt;
			msg.what = 6;
			mHandler.sendMessage(msg);
		}
	}

	public static void startHandler(int what) {
		if (what == 0 && what == 5)
			return;
		mHandler.sendEmptyMessage(what);
	}

	/**
	 * 查询客户id
	 * 
	 * @param cxt
	 */
	public static void requestBocopForCustid(final Context cxt, boolean isShowDialog) {
		requestBocopForCustid(cxt, isShowDialog, null);
	}

	public static void requestBocopForCustid(final Context cxt, boolean isShowDialog,
			final OnRequestCustCallBack requestCustCallBack) {
		Gson gson = new Gson();
		Map<String, String> map = new HashMap<String, String>();
		map.put("USRID", getUserId(cxt));
		final String strGson = gson.toJson(map);
		Log.i("taggg", "requestBocopForCustid");

		BocOpUtil bocOpUtil = new BocOpUtil(cxt);
		bocOpUtil.postOpboc(strGson, TransactionValue.SA0053, isShowDialog, new CallBackBoc() {

			@Override
			public void onSuccess(String responStr) {
				Log.i("tag", responStr);
				try {

					Map<String, String> map;
					map = JsonUtils.getMapStr(responStr);
					String cusid = map.get("cusid").toString();
					CacheBean.getInstance().put(CacheBean.CUST_ID, cusid);
					editor.putString(CacheBean.CUST_ID, cusid);
					editor.commit();
					// 存储客户信息
					Log.i("tag", "start");
					ContentUtils.putSharePre(cxt, Constants.CUSTOM_PREFERENCE_NAME, Constants.CUSTOM_ID_NO,
							map.get("idno"));
					ContentUtils.putSharePre(cxt, Constants.CUSTOM_PREFERENCE_NAME, Constants.CUSTOM_MOBILE_NO,
							map.get("mobileno"));
					ContentUtils.putSharePre(cxt, Constants.CUSTOM_PREFERENCE_NAME, Constants.CUSTOM_USER_NAME,
							map.get("cusname").toString());
					ContentUtils.putSharePre(cxt, Constants.CUSTOM_PREFERENCE_NAME, Constants.CUSTOM_CUS_ID,
							map.get("cusid"));
					ContentUtils.putSharePre(cxt, Constants.CUSTOM_PREFERENCE_NAME, Constants.CUSTOM_FLAG, "1");
					CustomInfo.postIdForXms(cxt);
//					if (requestCustCallBack != null) {
//						Log.i("tag", "onSuccess");
//						requestCustCallBack.onSuccess();
//					}
					Log.i("tag", "客户id：" + cusid);
				} catch (Exception e) {
					e.printStackTrace();
				}finally {
					if (requestCustCallBack != null) {
						requestCustCallBack.onSuccess();
					}
					
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
				CspUtil.onFailure(cxt, responStr);
			}
		});
	}

	public interface OnRequestCustCallBack {
		void onSuccess();
	}

	public static void postIdForXms() {
		Gson gson = new Gson();
		Map<String, String> map = new HashMap<String, String>();
		String cardId = ContentUtils.getStringFromSharedPreference(context,
				Constants.SHARED_PREFERENCE_NAME, Constants.CUSTOM_ID_NO);
		String userId = LoginUtil.getUserId(context);
		Log.i("tag22", cardId);
		Log.i("tag22", userId);
		map.put("userId", userId);
		map.put("cardId", cardId);
		final String strGson = gson.toJson(map);
		QztRequestWithJsonAndHead qztRequestWithJsonAndHead = new QztRequestWithJsonAndHead(
				context);
		qztRequestWithJsonAndHead
				.postOpbocNoDialog(
						strGson,
						BocSdkConfig.qztPostForXmsUrl,
						new com.bocop.jxplatform.util.QztRequestWithJsonAndHead.CallBackBoc() {
							@Override
							public void onSuccess(String responStr) {
								Log.i("tag22", responStr);
								try {
									ContentUtils.putSharePre(context,
											Constants.SHARED_PREFERENCE_NAME,
											Constants.CUSTOM_PUT_ALREADY, "1");
									Log.i("tag22",
											ContentUtils
													.getStringFromSharedPreference(
															context,
															Constants.SHARED_PREFERENCE_NAME,
															Constants.CUSTOM_PUT_ALREADY));
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
	// public static void authorize(Context context, ILoginListener myCallback)
	// {
	// // init(context);
	// LoginUtil.myCallback = myCallback;
	// BOCOPPayApi bocopSDKApi = BOCOPPayApi.getInstance(context,
	// BocSdkConfig.CONSUMER_KEY, BocSdkConfig.CONSUMER_SECRET);
	// bocopSDKApi.initURLIPPort(context, BocSdkConfig.CONSUMER_URL,
	// BocSdkConfig.CONSUMER_PORT, BocSdkConfig.CONSUMER_IS_REGISTER,
	// BocSdkConfig.CONSUMER_REGISTER);
	// bocopSDKApi.authorizeAsr(context, true, new ResponseListener() {
	//
	// @Override
	// public void onException(Exception arg0) {
	//
	// }
	//
	// @Override
	// public void onError(Error arg0) {
	//
	// }
	//
	// @Override
	// public void onComplete(ResponseBean response) {
	// Logger.d("testOAuthAsr 测试成功" + response.toString());
	// if (response instanceof BOCOPOAuthInfo) {
	// BOCOPOAuthInfo info = (BOCOPOAuthInfo) response;
	// String access_token = info.getAccess_token();
	// String userid = info.getUserId();
	// if (access_token != null && access_token.length() > 0) {
	// LoginUserInfo userInfo = new LoginUserInfo();
	// userInfo.setAccessToken(access_token);
	// userInfo.setUserId(userid);
	// BaseApplication.getInstance().setUserInfo(userInfo);
	// mHandler.sendEmptyMessage(0);
	// }
	//
	// }
	// else if (response instanceof RegisterResponse) { //注册返回后自动登录
	// RegisterResponse info = (RegisterResponse) response;
	// String access_token = info.getAccess_token();
	// String userid = info.getUserid();
	// if (access_token != null && access_token.length() > 0) {
	// LoginUserInfo userInfo = new LoginUserInfo();
	// userInfo.setAccessToken(access_token);
	// userInfo.setUserId(userid);
	// BaseApplication.getInstance().setUserInfo(userInfo);
	// mHandler.sendEmptyMessage(0);
	// }
	// }
	// }
	//
	// @Override
	// public void onCancel() {
	//
	// }
	// });
	//
	// }

	/**
	 * 退出登录对话框
	 * 
	 * 1、删除授权,本地保存的token和userid清除 2、删除用户缓存的卡列表信息 3、不删除用户收藏的产品
	 */
	public static void showLogoutAppDialog(final Activity cxt,
			final ILogoutListener myOutCallback) {
		BocopDialog dialog = new BocopDialog(cxt, "提示", "确定要退出登录吗？");
		dialog.setPositiveListener(new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				logout(cxt, myOutCallback);
				dialog.dismiss();
			}
		});
		dialog.setNegativeButton(new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		}, "取消");
		dialog.show();

		// AlertDialog.Builder builder = new Builder(cxt);
		// builder.setTitle("提示");
		// builder.setMessage("确定要退出登录吗？");
		// builder.setPositiveButton("退出", new DialogInterface.OnClickListener()
		// {
		// public void onClick(DialogInterface dialog, int which) {
		// logout(cxt, myOutCallback);
		// dialog.dismiss();
		// }
		// });
		// builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
		// {
		//
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// dialog.dismiss();
		// }
		// });
		// if (!(cxt).isFinishing()) {
		// builder.show();
		// }
	}

	/**
	 * 退出登录对话框
	 * 
	 * 1、删除授权,本地保存的token和userid清除 2、删除用户缓存的卡列表信息
	 * 
	 */
	public static void logout(Activity cxt, ILogoutListener myOutCallback) {

		//移除本地用户提醒列表数据
//		SharedPreferencesUtils spf = new SharedPreferencesUtils(cxt, AlarmService.ALARM_SER);
//		String userId = getUserId(cxt);
//		Log.i("logout", "移除" + userId + "数据");
//		spf.delete(userId);
		
		
		
		
//		SharedPreferences sp = cxt.getSharedPreferences(LoginUtil.SP_NAME,
//				Context.MODE_PRIVATE);
//		editor = sp.edit();
//		IApplication.userid = "";
//		BOCOPPayApi bocopSDKApi = BOCOPPayApi.getInstance(cxt,
//				BocSdkConfig.CONSUMER_KEY, BocSdkConfig.CONSUMER_SECRET);
//		bocopSDKApi.delOAuthorize(cxt);
//		editor.putString(CacheBean.ACCESS_TOKEN, null);
//		editor.putString(CacheBean.USER_ID, null);
//		editor.putString(CacheBean.REFRESH_TOKEN, null);
//		editor.putString(CacheBean.CUST_ID, null);
//		editor.commit();
		
		try{
			//删除闹钟数据
			CustomInfo.deleteAlarmInfo(cxt);
			//删除SP_NAME 信息
			CustomInfo.deleteConfigInfo(cxt);
			//删除信息
			CustomInfo.deleteCustomInfo(cxt);
			
			Log.i("tag", "开始发送广播");
			LoginBroadcast.sendLoginBroadcast(cxt,"loginOut");
			
		}catch (Exception ex){
			
		}
		

		// SharedPreferences spf = cxt.getSharedPreferences(
		// OnlineService.SECRETARY_MSG, Context.MODE_PRIVATE);
		// Editor mEditor = spf.edit();
		// mEditor.remove(BocSdkConfig.REGIST_DEVICE_KEY);
		// mEditor.commit();

		// 关闭推送服务
//		BaseApplication.stopGoPush();
		CacheBean.getInstance().clearCacheMap();
		if (myOutCallback != null) {
			myOutCallback.onLogout();
		}
	}

	public static void logoutWithoutCallback(Context cxt) {

		
		
		try{
//			SharedPreferences sp = cxt.getSharedPreferences(LoginUtil.SP_NAME,
//					Context.MODE_PRIVATE);
//			editor = sp.edit();
////			IApplication.userid = "";
////			Log.i("tag8", sp.getString(CacheBean.CUST_ID,null));
//			BOCOPPayApi bocopSDKApi = BOCOPPayApi.getInstance(cxt,
//					BocSdkConfig.CONSUMER_KEY, BocSdkConfig.CONSUMER_SECRET);
//			bocopSDKApi.delOAuthorize(cxt);
//			editor.putString(CacheBean.ACCESS_TOKEN, null);
//			editor.putString(CacheBean.USER_ID, null);
//			editor.putString(CacheBean.REFRESH_TOKEN, null);
//			editor.putString(CacheBean.CUST_ID, null);
////			Log.i("tag8", sp.getString(CacheBean.CUST_ID,null));
//			editor.commit();
//			CacheBean.getInstance().clearCacheMap();
			
			
			try{
				//删除闹钟数据
				CustomInfo.deleteAlarmInfo(cxt);
				//删除SP_NAME 信息
				CustomInfo.deleteConfigInfo(cxt);
				//删除信息
				CustomInfo.deleteCustomInfo(cxt);
				
				Log.i("tag", "开始发送广播");
				LoginBroadcast.sendLoginBroadcast(cxt,"loginOut");
				
			}catch (Exception ex){
				
			}
		}catch (Exception ex){
			
		}
	}

	// private static Oauth2AccessToken accessToken = null;
	// public static void logout(Context cxt, ILoginoutListener
	// myOutCallback,final BaseApplication application) {
	//
	// SharedPreferences sp = cxt.getSharedPreferences(LoginUtil.SP_NAME,
	// Context.MODE_PRIVATE);
	// editor = sp.edit();
	//
	// BOCOPPayApi bocopSDKApi = BOCOPPayApi.getInstance(cxt,
	// BocSdkConfig.CONSUMER_KEY,
	// BocSdkConfig.CONSUMER_SECRET);
	// bocopSDKApi.delOAuthorize(cxt);
	// editor.putString(CacheBean.ACCESS_TOKEN, null);
	// editor.putString(CacheBean.USER_ID, null);
	// editor.commit();
	// CacheBean.getInstance().clearCacheMap();
	// // RequestUtil.resetSession();
	// if (myOutCallback != null) {
	// myOutCallback.onLogout();
	// }

	// LoginUtil.myOutCallback = myOutCallback;
	// // BOCOPPayApi bocopSDKApi = BOCOPPayApi.getInstance(context,
	// BocSdkConfig.CONSUMER_KEY, BocSdkConfig.CONSUMER_SECRET);
	// // bocopSDKApi.delOAuthorize(context);
	// application.setUserInfo(null);
	// mHandler.sendEmptyMessage(1);
	// }

	public static boolean isLog(Context cxt) {
		SharedPreferences sp = cxt.getSharedPreferences(LoginUtil.SP_NAME,
				Context.MODE_PRIVATE);
		editor = sp.edit();

		String userId = sp.getString(CacheBean.USER_ID, "");
		String token = sp.getString(CacheBean.ACCESS_TOKEN, "");
		if (userId != null && !"".equals(userId) && token != null
				&& !"".equals(token)) {
			return true;
		}
		return false;
	}

	public static String getUserId(Context cxt) {
		SharedPreferences sp = cxt.getSharedPreferences(LoginUtil.SP_NAME,
				Context.MODE_PRIVATE);
		editor = sp.edit();

		String userId = sp.getString(CacheBean.USER_ID, "");
		if (userId != null && !"".equals(userId)) {
			return userId;
		}
		return "";
	}

	public static String getToken(Context cxt) {
		SharedPreferences sp = cxt.getSharedPreferences(LoginUtil.SP_NAME,
				Context.MODE_PRIVATE);
		editor = sp.edit();

		String token = sp.getString(CacheBean.ACCESS_TOKEN, "");
		if (token != null && !"".equals(token)) {
			return token;
		}
		return "";
	}

	public static String getRefreshToken(Context cxt) {
		SharedPreferences sp = cxt.getSharedPreferences(LoginUtil.SP_NAME,
				Context.MODE_PRIVATE);
		editor = sp.edit();

		String token = sp.getString(CacheBean.REFRESH_TOKEN, "");
		if (token != null && !"".equals(token)) {
			return token;
		}
		return "";
	}

	public static String getPattern(Activity cxt) {
		SharedPreferences sp = cxt.getSharedPreferences(LoginUtil.SP_NAME,
				Context.MODE_PRIVATE);
		String userId = sp.getString(CacheBean.USER_ID, "");
		String patternString = "";
		if (userId != null && !"".equals(userId)) {
			patternString = sp.getString(userId + PATTERN_NAME, "");
		}
		return patternString;
	}

	public static void setPattern(Activity cxt, String value) {
		SharedPreferences sp = cxt.getSharedPreferences(LoginUtil.SP_NAME,
				Context.MODE_PRIVATE);
		String userId = sp.getString(CacheBean.USER_ID, "");
		if (userId != null && !"".equals(userId)) {
			sp.edit().putString(userId + PATTERN_NAME, value).commit();
		}
	}

	public static long getUserLong(Activity cxt) {
		SharedPreferences sp = cxt.getSharedPreferences(LoginUtil.SP_NAME,
				Context.MODE_PRIVATE);
		String userId = sp.getString(CacheBean.USER_ID, "");
		long now = 0;
		if (userId != null && !"".equals(userId)) {
			now = sp.getLong(userId + TIME_NAME, System.currentTimeMillis());
			// Toast.makeText(cxt, userId + "time" + ":" + now,
			// Toast.LENGTH_SHORT).show();
		}
		return now;
	}

	public static void setUserLong(Activity cxt) {
		SharedPreferences sp = cxt.getSharedPreferences(LoginUtil.SP_NAME,
				Context.MODE_PRIVATE);
		String userId = sp.getString(CacheBean.USER_ID, "");
		if (userId != null && !"".equals(userId)) {
			sp.edit().putLong(userId + TIME_NAME, System.currentTimeMillis())
					.commit();
		}
	}

	public static boolean getOnoff(Activity cxt) {
		SharedPreferences sp = cxt.getSharedPreferences(LoginUtil.SP_NAME,
				Context.MODE_PRIVATE);
		String userId = sp.getString(CacheBean.USER_ID, "");
		boolean onoff = false;

		if (userId != null && !"".equals(userId)) {
			String patternString = sp.getString(userId + PATTERN_NAME, "");
			if (patternString != null && !"".equals(patternString)) {
				onoff = true;
			}
		}
		return onoff;
	}

	public static void turnOff(Activity cxt) {
		SharedPreferences sp = cxt.getSharedPreferences(LoginUtil.SP_NAME,
				Context.MODE_PRIVATE);
		String userId = sp.getString(CacheBean.USER_ID, "");
		if (userId != null && !"".equals(userId)) {
			sp.edit().remove(userId + PATTERN_NAME).commit();
		}
	}

	/**
	 * <p>
	 * 设置后台服务的引用，当后台服务启动或重启完成时将会调用此方法
	 * </p>
	 * <b><i>注意：此方法只能后台服务调用，其他任何页面不可调用</i></b>
	 * 
	 * @param onlineService
	 *            后台服务
	 */
	public static void setOnlineService(OnlineService onlineService) {
		Log.i(TAG, "set OnlineService" + System.currentTimeMillis());
		LoginUtil.onlineService = onlineService;
		for (OnMsgServiceOkListener listener : mListeners.values()) {
			listener.msgServiceOk();
		}
	}

	/**
	 * 注册后台服务启动或重启的监听器
	 * 
	 * @param msgServiceOkListener
	 */
	public static void registerMsgService(
			OnMsgServiceOkListener msgServiceOkListener) {
		Log.i(TAG, "注册service成功" + msgServiceOkListener.getMsgListenerName());
		LoginUtil.mListeners.put(msgServiceOkListener.getMsgListenerName(),
				msgServiceOkListener);
	}

	/**
	 * 后台服务启动或重新监听器
	 * 
	 * @author hch
	 * 
	 */
	public interface OnMsgServiceOkListener {
		/**
		 * 当后台服务已经启动完成，触发此方法
		 */
		void msgServiceOk();

		String getMsgListenerName();

	}
	
	/**
	 * 请求提醒列表
	 * @param context
	 * @param userId
	 */
	public static void requestRemindList(final Context context, final String userId) {
		try {
			// 生成CSP XML报文
			CspXmlXmsCom cspXmlXmsCom = new CspXmlXmsCom(userId, "MS002010");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			String currentDateTime = sdf.format(new Date());
			cspXmlXmsCom.setDate(currentDateTime);
			String strXml = cspXmlXmsCom.getCspXml();
			Log.i("tag", "getCspXml");
			// 生成MCIS报文
			Mcis mcis = new Mcis(strXml, TransactionValue.CSPSZF);
			Log.i("tag", "Mcis");
			final byte[] byteMessage = mcis.getMcis();
			// 发送报文
			CspUtil cspUtil = new CspUtil(context);
			Log.i("tag", "发送报文： " + new String(byteMessage, "GBK"));
//			cspUtil.setTest(true);
			cspUtil.postCspLogin(new String(byteMessage, "GBK"), new CallBack() {
				
				@SuppressWarnings("unchecked")
				@Override
				public void onSuccess(String responStr) {
					EventListXmlBean eventListXmlBean = EventListResp.readStringXml(responStr);
					if ("00".equals(eventListXmlBean.getErrorcode())) {
						final List<EventBean> eventList = eventListXmlBean.getEventList();
						if (eventList != null && eventList.size() > 0) {
							new AsyncTask<String, Integer, String>(){

								@Override
								protected String doInBackground(String... params) {
									SharedPreferencesUtils spf = new SharedPreferencesUtils(context, AlarmService.ALARM_SER);
									List<EventBean> list = (List<EventBean>) spf.getObject(userId, EventBean.class);
									if (list == null) {
										list = new ArrayList<EventBean>();
									}
									list.addAll(eventList);
									spf.setObject(userId, list);
									return null;
								}
								@Override
								protected void onPostExecute(String result) {
									super.onPostExecute(result);
									// 开始闹铃服务
									AlarmServiceManager.getInstance().startAlarmService(context);
							}}.execute("");
						}
					}
				}

				@Override
				public void onFinish() {

				}
				@Override
				public void onFailure(String responStr) {
					
				}
			}, false);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

}
