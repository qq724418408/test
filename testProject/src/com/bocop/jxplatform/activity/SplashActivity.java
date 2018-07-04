package com.bocop.jxplatform.activity;

import java.util.HashMap;
import java.util.Map;

import com.boc.bocop.sdk.util.AccessTokenKeeper;
import com.boc.bocop.sdk.util.Oauth2AccessToken;
import com.boc.jx.base.BaseApplication;
import com.boc.jx.baseUtil.cache.CacheBean;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.config.BocSdkConfig;
import com.bocop.jxplatform.config.TransactionValue;
import com.bocop.jxplatform.util.BocOpUtilForSA0015;
import com.bocop.jxplatform.util.BocOpUtilForSA0015.CallBackBoc3;
import com.bocop.jxplatform.util.CustomInfo;
import com.bocop.jxplatform.util.JsonUtils;
import com.bocop.jxplatform.util.LoginUtil;
import com.bocop.xfjr.XfjrMain;
import com.bocop.yfx.utils.ToastUtils;
import com.google.gson.Gson;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.Toast;

public class SplashActivity extends Activity {

	private  Editor editor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// x86下的设备不让安装
//		if(!"ARM".equals(XfjrMain.getCpuName())){
////			ToastUtils.show(this, "抱歉,您的设备不支持此应用.", Toast.LENGTH_LONG);
//			
//			new AlertDialog.Builder(this)
//			.setCancelable(false)
//			.setTitle("警告")
//	        .setMessage("抱歉,您的设备不支持此应用.")
//	        .setPositiveButton("退出", 
//	            new DialogInterface.OnClickListener() {
//	            @Override
//	            public void onClick(DialogInterface dialog, int which) {
//	               finish();
//	            }
//	        }).show();
//			
////			new Thread(new Runnable() {
////				
////				@Override
////				public void run() {
////					try {
////						Thread.sleep(3000);
////					} catch (InterruptedException e) {
////						e.printStackTrace();
////					}
////					android.os.Process.killProcess(android.os.Process.myPid()); //获取PID，目前获取自己的也只有该API，否则从/proc中自己的枚举其他进程吧，不过要说明的是，结束其他进程不一定有权限，不然就乱套了。// 建议使用
////					System.exit(0); //常规java、c#的标准退出法，返回值为0代表正常退出  // 不建议使用
////				}
////			}).start();
//			return ;
//		}
		
		ininInfo();
		isOauthToken();
	}
	
	private void ininInfo(){
		if(!LoginUtil.isLog(SplashActivity.this)){
			Log.i("tag", "启动页删除信息");
			//删除闹钟数据
			CustomInfo.deleteAlarmInfo(SplashActivity.this);
			//删除SP_NAME 信息
			CustomInfo.deleteConfigInfo(SplashActivity.this);
			//删除信息
			CustomInfo.deleteCustomInfo(SplashActivity.this);
		}
	}
	private void isOauthToken() {
		String token = getIntent().getStringExtra("accesstoken");
		String refreshtoken = getIntent().getStringExtra("refreshtoken");
		long expiresTime = getIntent().getLongExtra("expiresTime", 0);
		String user = getIntent().getStringExtra("user");

		/*
		 * 目前判断了token值是否为空，使用时可根据需求增加其他判断条件
		 */
		if (null != token) {
			Editor editor;
			SharedPreferences sp = getSharedPreferences(LoginUtil.SP_NAME, Context.MODE_PRIVATE);
			editor = sp.edit();
			Oauth2AccessToken accessToken = new Oauth2AccessToken();
			accessToken.setToken(token);
			accessToken.setRefreshToken(refreshtoken);
			accessToken.setExpiresIn(String.valueOf(expiresTime));
			accessToken.setUserId(user);

			editor.putString(CacheBean.ACCESS_TOKEN, accessToken.getToken());
			editor.putString(CacheBean.USER_ID, accessToken.getUserId());
			editor.putString(CacheBean.REFRESH_TOKEN, accessToken.getRefreshToken());
			editor.commit();

			/*
			 * 保存传入的token等值，跳转到对应的下一页面
			 */
			AccessTokenKeeper.keepAccessToken(SplashActivity.this, accessToken);
			initView();

		} else {
			initView();
		}
	}

	private void initView() {
		
		if(LoginUtil.getToken(SplashActivity.this) != null && LoginUtil.getToken(SplashActivity.this).length()>10)
		{
			
			try{
				requestBocopForRefreshToken();
			}
			catch(Exception e){
				Log.i("tag", "requestBocopForRefreshToken 刷新TOKEN发生异常");
			}
		}
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		setContentView(R.layout.activity_splash);
		ImageView logoView = (ImageView) this.findViewById(R.id.slash_img);
		AlphaAnimation alphaAnimation = new AlphaAnimation(0.1f, 1.0f);
		alphaAnimation.setDuration(3000);
		logoView.startAnimation(alphaAnimation);
		alphaAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				Log.i("tag", "onAnimationStart");
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				Log.i("tag", "onAnimationRepeat");
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				Log.i("tag", "onAnimationEnd");
				
				if (!LoginUtil.isLog(SplashActivity.this) && !getTel()) {
				      Intent intent = new Intent(SplashActivity.this, InformalLoginActivity.class);
				      startActivity(intent);
				   }else{
					   Intent intent = new Intent(SplashActivity.this, MainActivity.class);
						startActivity(intent);
				   }
				SplashActivity.this.finish();
//				if (LoginUtil.isLog(SplashActivity.this)) {                            	                            	
//            		if (LockPatternUtils.savedPatternExists()) {
//                        //输入手势密码
//                        Intent intent1 = new Intent();
//                        intent1.putExtra("wayid", "wayid");
//                        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
//                        intent1.setClass(SplashActivity.this, UnlockGesturePasswordActivity.class);
//                        startActivity(intent1);                        
//                    }
//            	}	
				
//				Log.i("tag", "onAnimationEnd");
//				if(LocusPassWordUtil.getHandPassword(getApplicationContext())!=false){
//					Intent intent = new Intent(SplashActivity.this, GuideGesturePasswordActivity.class);
//					intent.putExtra("activityName", "com.bocop.jxplatform.activity.MainActivity");
//					startActivity(intent);
//					SplashActivity.this.finish();
//				}else{
//					Intent intent = new Intent(SplashActivity.this, MainActivity.class);
//					startActivity(intent);
//					SplashActivity.this.finish();
//				}
				
			}
		});
	}

	public boolean getTel (){
		SharedPreferences sp = this.getSharedPreferences(LoginUtil.SP_NAME,
				Context.MODE_PRIVATE);
		editor = sp.edit();
		String userTel = sp.getString(CacheBean.USER_TEL_LOGIN, "");
		if (userTel != null && !"".equals(userTel)) {
			return true;
		}
		return false;
	}
	
	private void requestBocopForRefreshToken() {
		BocOpUtilForSA0015 bocOpUtil = new BocOpUtilForSA0015(SplashActivity.this);
		Log.i("tag", "access_token:" + LoginUtil.getToken(SplashActivity.this));
		SharedPreferences sp = SplashActivity.this.getSharedPreferences(
				LoginUtil.SP_NAME, Context.MODE_PRIVATE);
		editor = sp.edit();
		Gson gson = new Gson();
		Map<String, String> map = new HashMap<String, String>();
		map.put("client_id", BocSdkConfig.CONSUMER_KEY);
		// map.put("client_secret", BocSdkConfig.CONSUMER_SECRET);
		// map.put("refresh_token", LoginUtil.getRefreshToken(getActivity()));
		// map.put("grant_type", "1");
		Log.i("tag", "client_id:" + BocSdkConfig.CONSUMER_KEY
				+ "client_secret:" + BocSdkConfig.CONSUMER_SECRET
				+ "refresh_token:" + LoginUtil.getRefreshToken(SplashActivity.this)
				+ "grant_type:" + "1");
		final String strGson = gson.toJson(map);
		Log.i("tag", "postOpboc");
		String url = TransactionValue.SA0015
				+ "?grant_type=refresh_token&client_id="
				+ BocSdkConfig.CONSUMER_KEY + "&client_secret="
				+ BocSdkConfig.CONSUMER_SECRET + "&refresh_token="
				+ LoginUtil.getRefreshToken(SplashActivity.this);
		Log.i("tag", "url:" + url);
		bocOpUtil.postOpboc(strGson, url, new CallBackBoc3() {
			@Override
			public void onSuccess(String responStr) {
				Log.i("tag", responStr);
				try {
					Map<String, String> mapRec = null;
					mapRec = JsonUtils.getMapStr(responStr);
					Log.i("tag1", mapRec.toString());
					String access_token = mapRec.get("access_token");
					Log.i("tag0",
							"yuan_Access_token:"
									+ LoginUtil.getToken(SplashActivity.this));
					if (access_token != null && access_token.length() > 0) {// 登录成功后将token和useid存储到本地
						editor.putString(CacheBean.ACCESS_TOKEN, access_token);
						editor.commit();
					}else{
						 LoginUtil.logoutWithoutCallback(SplashActivity.this);
					}
					Log.i("tag0",
							"getAccess_token:"
									+ LoginUtil.getToken(SplashActivity.this));
					Log.i("tag0", "Access_token:" + access_token);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFinish() {
				try{
					SharedPreferences sp = getSharedPreferences(LoginUtil.SP_NAME, Context.MODE_PRIVATE);
					String userId = sp.getString(CacheBean.USER_ID, "");
					editor = sp.edit();
					editor.putString(CacheBean.DZP_USER_ID, userId);
					editor.commit();
					if (!TextUtils.isEmpty(userId)) {
						
						
						//上送日志
						try{
							// 开启推送服务
							Log.i("tag", "开启推送服务");
							BaseApplication.startGoPush(userId);
							if(!CustomInfo.isExistCustomInfo(SplashActivity.this)){
								Log.i("LoginUtil", "requestBocopForCustid");
								CustomInfo.requestBocopForCustid(SplashActivity.this, false);
							}else{
								Log.i("LoginUtil", "postIdForXms");
								CustomInfo.postIdForXms(SplashActivity.this);
							}
						}
						catch(Exception ex){
							
						}
					}
				}
				catch(Exception e){
					Log.i("tag", "SplashActivity onFinish 发生异常");
				}
			}

			@Override
			public void onFailure(String responStr) {
			}

			@Override
			public void onStart() {
			}
		});
	}
}
