package com.bocop.jxplatform.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.boc.jx.baseUtil.cache.CacheBean;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.boc.jx.base.BaseActivity;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;

import com.bocop.jxplatform.config.BocSdkConfig;
import com.bocop.jxplatform.trafficassistant.BocOpWebActivity;
import com.bocop.jxplatform.util.BocOpUtil;
import com.bocop.jxplatform.util.CspUtil;
import com.bocop.jxplatform.util.LoginUtil;
import com.bocop.jxplatform.util.QztRequestWithJsonAndHead;
import com.bocop.jxplatform.util.RegularCheck;
import com.bocop.jxplatform.util.BocOpUtilWithoutDia;
import com.bocop.jxplatform.util.BocOpUtilWithoutDia.CallBackBoc2;
import com.bocop.jxplatform.util.LoginUtil.ILoginListener;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.config.TransactionValue;
import com.bocop.jxplatform.view.BackButton;
import com.bocop.jxplatform.view.MyProgressBar;
import com.bocop.xfjr.XfjrMain;
import com.bocop.xfjr.activity.customer.XFJRBusinessDetailActivity;
import com.bocop.xyd.XydMain;
import com.google.gson.Gson;

import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


@ContentView(R.layout.activity_informal_login)

public class InformalLoginActivity extends BaseActivity implements OnClickListener,ILoginListener{

	@ViewInject(R.id.tv_titleName)
	private TextView  tv_titleName;
	@ViewInject(R.id.iv_imageLeft)
	private BackButton backBtn;

	@ViewInject(R.id.edlicensenum_add)
	EditText edlicensenum;
	@ViewInject(R.id.splicensetype_add)
	TextView spsplicensetype;
	@ViewInject(R.id.edvehiclenum_add)
	EditText edvehiclenum;
	@ViewInject(R.id.tv_addtel)		//手机
			EditText edTel;
	private EditText input = null;

	@ViewInject(R.id.et_telverify_code)
	EditText edTelVerifyCode;

	@ViewInject(R.id.tv_telsend_msg)	// 倒计时 发送验证码
			TextView tvSendMsg;
	@ViewInject(R.id.llt_telsend_msg)
	private LinearLayout lltSendMsg; // 点击获取验证码
	@ViewInject(R.id.rlt_telloading)	// 点击验证码加载框
			RelativeLayout rltLoading;


	/** 短信状态 */
	private static final int LAST_TIME = 1;
	private static final int LESS_TIME = 2;
	private static final int FINISH_TIME = 3;
	private long startTime;// 开始计时时间
	private long endTime;// 当前时间
	private int currentTime = 59;
	private MyProgressBar myProgressBar;

	@ViewInject(R.id.TelAdd)
	Button usrTelAdd;
	String strGson;
	private static Editor editor;

//	@ViewInject(R.id.fomal_login)
//	Button fomalLogin;

	@ViewInject(R.id.sign_up)
	Button signup;

	private long exitTime = 0;


	String strTel = "";
	String strTelVerifyCode;
	String strFlag = "0";			//判断是否已经获取验证码



	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case LAST_TIME:
					break;
				case LESS_TIME:
					currentTime--;
					tvSendMsg.setText(currentTime + "秒");
					tvSendMsg.setTextSize(12);
					break;
				case FINISH_TIME:
					tvSendMsg.setText("获取验证码");
					tvSendMsg.setTextSize(12);
					lltSendMsg.setClickable(true);
					lltSendMsg.setBackgroundResource(R.drawable.button_press);
					tvSendMsg.setTextColor(getResources().getColor(R.color.white));

					currentTime = 59;
					break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tv_titleName.setText("游客体验");
		usrTelAdd.setOnClickListener(this);
//		fomalLogin.setOnClickListener(this);
		signup.setOnClickListener(this);
		input = (EditText) findViewById(R.id.tv_addtel);
	}

	@Override
	public void onClick(View v) {
		Bundle bundle = new Bundle();
		Intent intent = new Intent(InformalLoginActivity.this,
				BocOpWebActivity.class);
		switch (v.getId()) {
			case R.id.TelAdd:
				bindCar();
				break;
			case R.id.sign_up:
				signup();
				break;
			case R.id.iv_imageLeft:
				Intent intentMain = new Intent(InformalLoginActivity.this, MainActivity.class);
				startActivity(intentMain);
			default:
				break;
//		finish();
		}
		if (LoginUtil.isLog(this))
		{
			Intent intentMain = new Intent(InformalLoginActivity.this, MainActivity.class);
			startActivity(intentMain);
		}
	}


	private void signup(){
		Intent intentMain = new Intent(InformalLoginActivity.this, MainActivity.class);
//		Intent intentMain = new Intent(InformalLoginActivity.this, BusinessDetailActivity.class);
		startActivity(XydMain.startXydMainActivity(intentMain));
	}


	private void bindCar() {
//		strLicenseNum = edlicensenum.getText().toString().trim();
//		strLicenseType = "02";		//小型车
//		strVehicleNum = edvehiclenum.getText().toString().trim();
		strTel = edTel.getText().toString().trim();
		Log.i("tag", strTel);
		strTelVerifyCode = edTelVerifyCode.getText().toString().trim();

		if("".equals(edTel.getText().toString().trim())){
			Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
			return;
		}
		if(strFlag.equals("0")){
			Toast.makeText(this, "请获取验证码", Toast.LENGTH_SHORT).show();
			return;
		}
		if(strTelVerifyCode.length() != 6){
			Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
			return;
		}
		requestBocopForCheckMsg();
	}

	/**
	 * 发送验证码
	 *
	 * @param v
	 */
	@OnClick(R.id.llt_telsend_msg)
	public void SendMsg(View v) {
		strTel = edTel.getText().toString().trim();
		if(!RegularCheck.isMobile(strTel)){
			Toast.makeText(this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
			return;
		}
		strFlag = "1";
		lltSendMsg.setClickable(false);
		requestBocForTelMsg();

	}


	//
	private void requestBocForTelMsg() {
		//点击短信发送按钮后的加载框
		rltLoading.setVisibility(View.VISIBLE);
		myProgressBar = new MyProgressBar(this, rltLoading);
		myProgressBar.addView();
		tvSendMsg.setText("正在努力...");
		tvSendMsg.setTextSize(12);
		tvSendMsg.setTextColor(getResources().getColor(R.color.pay_send_msg1));
		BocOpUtilWithoutDia bocOpUtil = new BocOpUtilWithoutDia(this);

		Gson gson = new Gson();
		Map<String,String> map = new HashMap<String,String>();
		map.put("usrid", "logintest");
		map.put("usrtel", strTel);
		map.put("randtrantype", TransactionValue.messageType);
		final String strGson = gson.toJson(map);

		bocOpUtil.postOpbocNolog(strGson,TransactionValue.SA7114, new CallBackBoc2() {
//					SA0052 sa0052;
			@Override
			public void onSuccess(String responStr) {
				Log.i("tag", responStr);
				Toast.makeText(InformalLoginActivity.this, "短信已发送，请查收", Toast.LENGTH_LONG).show();
				tvSendMsg.setText("59秒后重新获取");
				tvSendMsg.setTextSize(12);
				lltSendMsg.setClickable(false);
				handler.sendEmptyMessage(LAST_TIME);
				myProgressBar.removeView();
				rltLoading.setVisibility(View.GONE);
				Toast.makeText(InformalLoginActivity.this, "短信验证码已发送！", Toast.LENGTH_SHORT);

				new Thread() {
					public void run() {
						startTime = System.currentTimeMillis();// 获取当前时间
						while (true) {
							endTime = System.currentTimeMillis();// 获取当前时间
							if ((endTime - startTime) > 1000) {
								startTime = System.currentTimeMillis();
								if (currentTime > 0) {
									handler.sendEmptyMessage(LESS_TIME);
								} else {
									handler.sendEmptyMessage(FINISH_TIME);
									break;
								}
							}
						}
					};
				}.start();


			}

			@Override
			public void onFinish() {

			}

			@Override
			public void onFailure(String responStr) {
				Log.i("tag", "cartel onFailure");
				tvSendMsg.setText("获取验证码");
				tvSendMsg.setTextSize(12);
				lltSendMsg.setClickable(true);
				lltSendMsg.setBackgroundResource(R.drawable.send_btn_selector);
				tvSendMsg.setTextColor(getResources().getColor(R.color.white));
				currentTime = 59;
				rltLoading.setVisibility(View.GONE);
				if(responStr.equals("0") || responStr.equals("1") ){
					Toast.makeText(InformalLoginActivity.this, R.string.onFailure, Toast.LENGTH_SHORT).show();
				}
				else{
					Toast.makeText(InformalLoginActivity.this,responStr , Toast.LENGTH_SHORT).show();
				}
			}
			@Override
			public void onStart() {
				Log.i("tag", "发送GSON数据：" + strGson);
			}
		});
	}

	/**
	 * 验证短信验证码
	 */
	private void requestBocopForCheckMsg() {
		// TODO Auto-generated method stub
		Gson gson = new Gson();
//				List<Map<String,String>> list =new ArrayList<Map<String,String>>();
		Map<String, String> map = new HashMap<String, String>();
		map.put("usrid", "logintest");
		map.put("usrtel", strTel);
		map.put("randtrantype", TransactionValue.messageType);
		map.put("chkcode", strTelVerifyCode);
		final String strGson = gson.toJson(map);

		BocOpUtil bocOpUtil = new BocOpUtil(this);
		bocOpUtil.postOpbocNolog(strGson, TransactionValue.SA7115, new BocOpUtil.CallBackBoc() {

			@Override
			public void onSuccess(String responStr) {
				Log.i("tag", responStr);
				putTel(InformalLoginActivity.this);
				requestBocopForLogin();
			}

			@Override
			public void onStart() {
				Log.i("tag", "发送GSON数据：" + strGson);
			}

			@Override
			public void onFinish() {
//						requestBocopForUseridQuery();	//用户附加信息查询
			}

			@Override
			public void onFailure(String responStr) {
				CspUtil.onFailure(InformalLoginActivity.this, responStr);
			}
		});

	}

	private void requestBocopForLogin() {
		Gson gson = new Gson();
		Map<String, String> map = new HashMap<String, String>();
		String userTel = edTel.getText().toString().trim();

		map.put("userTel", userTel);
		map.put("appId", BocSdkConfig.CONSUMER_KEY);
//		map.put("cardId", strCardId);
		strGson = gson.toJson(map);
		Log.i("tag", strGson);
		try {
			strGson = URLDecoder.decode(gson.toJson(map), "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		QztRequestWithJsonAndHead qztRequestWithJson = new QztRequestWithJsonAndHead(
				this);
		qztRequestWithJson
				.postOpboc(
						strGson,
						BocSdkConfig.infomalLogin,
						new com.bocop.jxplatform.util.QztRequestWithJsonAndHead.CallBackBoc() {

							@Override
							public void onSuccess(String responStr) {
								Log.i("tag22", responStr);
//								Toast.makeText(InformalLoginActivity.this,
//										responStr, Toast.LENGTH_LONG).show();
							}

							@Override
							public void onStart() {
								// TODO Auto-generated method stub
								Log.i("tag", "发送JSON报文" + strGson);
							}

							@Override
							public void onFinish() {
								// TODO Auto-generated method stub
								Intent intent = new Intent(InformalLoginActivity.this, MainActivity.class);
								startActivity(intent);
							}

							@Override
							public void onFailure(String responStr) {
								Toast.makeText(InformalLoginActivity.this,
										responStr, Toast.LENGTH_LONG).show();

							}
						});
	}



//	    public boolean putTel(Context cxt){
//			SharedPreferences sp = cxt.getSharedPreferences(LoginUtil.SP_NAME,
//					Context.MODE_PRIVATE);
//			editor = sp.edit();
//			editor.putString(CacheBean.USER_TEL_LOGIN, edTel.getText().toString().trim());
//
//		}

	public void putTel (Context cxt) {
		Editor editor;
		SharedPreferences sp = InformalLoginActivity.this.getSharedPreferences(LoginUtil.SP_NAME,
				Context.MODE_PRIVATE);
		editor = sp.edit();
		String userTel = edTel.getText().toString().trim();
		Log.i("tag", "userTel----put--" + userTel);
		editor.putString(CacheBean.USER_TEL_LOGIN, userTel);
		Log.i("tag", "userTel----put--" + userTel);
		editor.commit();
	}


		public String getTel (Context cxt){
			SharedPreferences sp = cxt.getSharedPreferences(LoginUtil.SP_NAME,
					Context.MODE_PRIVATE);
			editor = sp.edit();

			String userTel = sp.getString(CacheBean.USER_TEL_LOGIN, "");

//		String token = sp.getString(CacheBean.ACCESS_TOKEN, "");
			if (userTel != null && !"".equals(userTel)
					) {
				return userTel;
			}
			return "";
		}

//	@Override
//	public boolean dispatchKeyEvent(KeyEvent event) {
//		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
//			if (event.getAction() == KeyEvent.ACTION_DOWN
//					&& event.getRepeatCount() == 0) {
//				exitApp();
//			}
//			return true;
//		}
//		return super.dispatchKeyEvent(event);
//	}
//
//	private void exitApp() {
//		if ((System.currentTimeMillis() - exitTime) > 2000) {
//			Toast.makeText(InformalLoginActivity.this, "再按一次退出程序", Toast.LENGTH_LONG)
//					.show();
//			exitTime = System.currentTimeMillis();
//		} else {
//			finish();
//			CacheBean.getInstance().clearCacheMap();
//			// Log.i("tag", "logoutWithoutCallback");
//			// LoginUtil.logoutWithoutCallback(MainActivity.this);
//			// getBaseApp().exit();
////			 System.exit(0);
//		}
//
//	}



	@Override
	public void onLogin() {
		// TODO Auto-generated method stub
		requestBocopForLogin();
		Toast.makeText(InformalLoginActivity.this, "登陆成功", Toast.LENGTH_LONG).show();
		finish();
	}

	@Override
	public void onCancle() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onError() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onException() {
		// TODO Auto-generated method stub

	}

}
