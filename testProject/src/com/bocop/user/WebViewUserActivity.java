package com.bocop.user;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.activity.MainActivity;
import com.bocop.jxplatform.config.BocSdkConfig;
import com.bocop.jxplatform.util.LoginUtil;
import com.bocop.user.app.IApplication;
import com.bocop.user.login.LoginTestResponse;
import com.google.gson.Gson;
//import java.util.logging.Handler;

/**
 * Created by luoyang on 15/9/29.
 */
public class WebViewUserActivity extends Activity {

	private WebView mWebView;
	// private EditText urlEdit;
	private int mState;

	private ProgressBar bar;
	
	private TextView tv_titleName;

	private ImageView ivBack;
	@SuppressLint({ "JavascriptInterface", "SetJavaScriptEnabled" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		setContentView(R.layout.webview_layout);
		final String url = getIntent().getExtras().get("url").toString();
		mWebView = (WebView) super.findViewById(R.id.mainActivity_Webview);

		mWebView.getSettings().setDomStorageEnabled(true);
		String appCachePath = getApplicationContext().getCacheDir()
				.getAbsolutePath();
		mWebView.getSettings().setAppCachePath(appCachePath);
		mWebView.getSettings().setAllowFileAccess(true);
		mWebView.getSettings().setAppCacheEnabled(true);
		mWebView.addJavascriptInterface(new AppAndroid(), "android");

		bar = (ProgressBar) super.findViewById(R.id.exchangeProgressBar);
		tv_titleName = (TextView) super.findViewById(R.id.tv_titleName);
		ivBack = (ImageView) super.findViewById(R.id.iv_back);
		
		ivBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mWebView.canGoBack()){
					mWebView.goBack();
				}
				else{
					finish();
				}
			}
		});
		// urlEdit = (EditText)super.findViewById(R.id.url_Edit);
		// urlEdit.setText(url);
		// Button bt_Reflect = (Button)super.findViewById(R.id.Bt_reflect);
		// bt_Reflect.setOnClickListener(new View.OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// urlEdit.setText(url);
		// reflectWebView(url);
		// }
		// });

		reflectWebView(url);
	}
	
//	@OnClick(R.id.iv_back)
//	public void back(View v) {
//		if(mWebView.canGoBack()){
//			mWebView.goBack();
//		}
//		else{
//			finish();
//		}
//	}

	public class AppAndroid{

		@JavascriptInterface
		public void BackToSuperView(String data) {
			;
			Toast toast = Toast.makeText(WebViewUserActivity.this, "重置成功",
					Toast.LENGTH_LONG);
			toast.show();
			finish();
		}

		@JavascriptInterface
		public void webAppEditPwd(String data) {
			;
			Toast toast = Toast.makeText(WebViewUserActivity.this, "重置成功",
					Toast.LENGTH_LONG);
			toast.show();
			finish();
		}

		@JavascriptInterface
		public String appInfoForGetPwd() {
			AppInfoH5 appInfoH5 = new AppInfoH5();
			// appInfoH5.setClientId(IApplication.CLIENTID);
			appInfoH5.setClientId(BocSdkConfig.CONSUMER_KEY);
			// appInfoH5.setClient_secret(IApplication.CLIENTSECRET);
			appInfoH5.setClient_secret(BocSdkConfig.CONSUMER_SECRET);
			appInfoH5.setHideBackArrow("false");
			appInfoH5.setRegUrl("");
			appInfoH5.setThemePath("123");
			appInfoH5.setAppVersion("2");
			appInfoH5.setResponseType(BocSdkConfig.responseType);
			// appInfoH5.setResponseType();
			appInfoH5.setHasHeaderBar("1");
			return new Gson().toJson(appInfoH5);
		}

		@JavascriptInterface
		public String appInfoForEditPwd() {
			AppInfoH5 appInfoH5 = new AppInfoH5();
			appInfoH5.setClientId(BocSdkConfig.CONSUMER_KEY);
			appInfoH5.setClient_secret(BocSdkConfig.CONSUMER_SECRET);
			appInfoH5.setAccesssToken(LoginUtil
					.getToken(WebViewUserActivity.this));
			appInfoH5.setUserId(LoginUtil.getUserId(WebViewUserActivity.this));
			return new Gson().toJson(appInfoH5);
		}

		@JavascriptInterface
		public String appInfoForRegister() {
			Log.i("tag", "appInfoForRegister");
			AppInfoH5 appInfoH5 = new AppInfoH5();
			appInfoH5.setClientId(BocSdkConfig.CONSUMER_KEY);
			appInfoH5.setClient_secret(BocSdkConfig.CONSUMER_SECRET);
			appInfoH5.setHasHeaderBar("0");
			appInfoH5.setAppVersion(BocSdkConfig.APP_VERSION);
			appInfoH5.setResponseType(BocSdkConfig.responseType);
			return new Gson().toJson(appInfoH5);
		}

		@JavascriptInterface
		public String getAppInfo() {
			AppInfoH5 appInfoH5 = new AppInfoH5();
			appInfoH5.setClientId(BocSdkConfig.CONSUMER_KEY);
			appInfoH5.setClient_secret(BocSdkConfig.CONSUMER_SECRET);
			appInfoH5.setAsrUrl("");
			appInfoH5.setSapUrl("");
			appInfoH5.setHideBackArrow("false");
			appInfoH5.setRegUrl("");
			appInfoH5.setThemePath("123");
			appInfoH5.setAppVersion("234");
			appInfoH5.setResponseType(BocSdkConfig.responseType);
			return new Gson().toJson(appInfoH5);
		}

		@JavascriptInterface
		public void getdata(String ti, String token) {

			Logger.getLogger("ti+token--------" + ti + token);
		}

		@JavascriptInterface
		public void wapRegisterCode(String res) {
			Log.i("tag", "response=" + res);
			Gson gson = new Gson();
			RegisterResponse info = gson.fromJson(res, RegisterResponse.class);
			IApplication.isLogin = true;
			IApplication.userid = info.userId;
			IApplication.accessToken = info.access_token;
			Toast toast = Toast.makeText(WebViewUserActivity.this, "注册成功",
					Toast.LENGTH_LONG);
			toast.show();
			finish();
		}

		@JavascriptInterface
		public void TestLogin(String res) {
			Log.i("tag", "response=" + res);
			Gson gson = new Gson();
			LoginTestResponse info = gson
					.fromJson(res, LoginTestResponse.class);

			IApplication.isLogin = true;

			IApplication.userid = info.userName;
			IApplication.idnum_s = info.idnum_s;
			IApplication.idtype = info.idtype;
			IApplication.usrid_s = info.usrid_s;
			IApplication.usrnam_s = info.usrnam_s;
			IApplication.usrtel_s = info.usrtel_s;
			IApplication.accessToken = info.accessToken;
			Map tt = new HashMap();
			tt.put("01", "居民身份证");
			tt.put("02", "临时身份证");
			tt.put("03", "护照");
			tt.put("04", "户口本");
			tt.put("05", "军人身份证");
			tt.put("06", "武装警察身份证");
			tt.put("07", "港澳台居民通行证");
			tt.put("08", "外交人员身份证");
			tt.put("09", "外国人居留许可证");
			tt.put("10", "边民出入境通行证");
			tt.put("47", "港澳居民通行证（香港）");
			tt.put("48", "台湾居民来往大陆通行证");
			tt.put("11", "其它");

			IApplication.idtypeName = (String) tt.get(info.idtype);

			finish();
		}

		@JavascriptInterface
		public void bindCardDone(String state) {
			mState = Integer.valueOf(state.trim());
			Toast toast = Toast.makeText(WebViewUserActivity.this, "绑卡成功",
					Toast.LENGTH_LONG);
			toast.show();
			finish();
		}

		@JavascriptInterface
		public void TestLoginExit() {
			if (IApplication.isLogin)
				IApplication.isLogin = false;
			finish();
		}

		@JavascriptInterface
		public void exitCardLists() {
			finish();
		}

		@JavascriptInterface
		public String getAppLoginInfo() {
			AppLoginInfo appLoginInfo = new AppLoginInfo();
			appLoginInfo.setClientId(BocSdkConfig.CONSUMER_KEY);
			appLoginInfo.setHideBackArrow("false");
			appLoginInfo.setUserId(LoginUtil
					.getUserId(WebViewUserActivity.this));
			appLoginInfo.setAccessToken(LoginUtil
					.getToken(WebViewUserActivity.this));
			return new Gson().toJson(appLoginInfo);
		}

		@JavascriptInterface
		public void exitBindCard() {
			finish();
		}

		@JavascriptInterface
		public void exitGetPwd() {
			finish();
		}

		@JavascriptInterface
		public void exitRegister() {
			finish();
		}

		@JavascriptInterface
		public void webAppGetPwd(String data) {
			Log.i("tag", "response=" + data);
			Toast toast = Toast.makeText(WebViewUserActivity.this, "重置成功",
					Toast.LENGTH_LONG);
			toast.show();
			finish();
		}

		@JavascriptInterface
		public void exitEditPwd() {
			finish();
		}

		@JavascriptInterface
		public void exitlogin() {
			finish();
		}

		@JavascriptInterface
		public void webAppRegister(String RegisterInfo) {
			Log.i("tag", "response=" + RegisterInfo);
			Gson gson = new Gson();
			RegisterResponse info = gson.fromJson(RegisterInfo,
					RegisterResponse.class);
			// IApplication.isLogin = true;
			// IApplication.userid = info.userId;
			// IApplication.accessToken = info.access_token;
			// IApplication.code = info.code;
			Toast toast = Toast.makeText(WebViewUserActivity.this, "注册成功",
					Toast.LENGTH_LONG);
			toast.show();
			Intent intent = new Intent(WebViewUserActivity.this,
					MainActivity.class);
			startActivity(intent);
		}

		@JavascriptInterface
		public String appInfoForBindCard() {
			Log.i("tag", "appInfoForBindCard");
			AppInfoH5 appInfoH5 = new AppInfoH5();
			appInfoH5.setClientId(BocSdkConfig.CONSUMER_KEY);
			appInfoH5.setClient_secret(BocSdkConfig.CONSUMER_SECRET);
			appInfoH5.setAccesssToken(LoginUtil
					.getToken(WebViewUserActivity.this));
			appInfoH5.setUserId(LoginUtil.getUserId(WebViewUserActivity.this));
			return new Gson().toJson(appInfoH5);
		}

		@JavascriptInterface
		public String appInfoForCardLists() {
			Log.i("tag", "appInfoForCardLists");
			AppInfoH5 appInfoH5 = new AppInfoH5();
			appInfoH5.setClientId(BocSdkConfig.CONSUMER_KEY);
			appInfoH5.setUserId(LoginUtil.getUserId(WebViewUserActivity.this));
			appInfoH5.setAccesssToken(LoginUtil
					.getToken(WebViewUserActivity.this));
			appInfoH5.setCardForm("allCard");
			return new Gson().toJson(appInfoH5);
		}

		// 手机号认证
		@JavascriptInterface
		public void exitAuth() {
			finish();
		}
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@SuppressLint({ "SetJavaScriptEnabled", "NewApi" })
	private final void reflectWebView(String url) {
		WebSettings settings = mWebView.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setSaveFormData(true);
		settings.setBuiltInZoomControls(true);
		settings.setAppCacheEnabled(true);
		if (Build.VERSION.SDK_INT >= 16) {
			mWebView.getSettings().setAllowFileAccessFromFileURLs(true);
			mWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
		}
		try {
			if (Build.VERSION.SDK_INT >= 16) {
				Class<?> clazz = mWebView.getSettings().getClass();
				Method method = clazz.getMethod(
						"setAllowUniversalAccessFromFileURLs", boolean.class);
				if (method != null) {
					method.invoke(mWebView.getSettings(), true);
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		CookieManager.setAcceptFileSchemeCookies(true);
		CookieSyncManager.createInstance(this);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.setAcceptCookie(true);

		CookieSyncManager.getInstance().sync();
		mWebView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
			}

			@Override
			public void onReceivedSslError(WebView view,
					SslErrorHandler handler, SslError error) {
				handler.proceed();
			}
		});

		mWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
				if(title != null){
					tv_titleName.setText(title);
				}
			}

			// 设置网页加载的进度条
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if (newProgress == 100) {
					bar.setVisibility(View.INVISIBLE);
				} else {
					if (View.INVISIBLE == bar.getVisibility()) {
						bar.setVisibility(View.VISIBLE);
					}
					bar.setProgress(newProgress);
				}
				super.onProgressChanged(view, newProgress);
			}
		});
		mWebView.loadUrl(url);
	}

	// Handler mHandler = new Handler() {
	// public void handleMessage(android.os.Message msg){
	// switch (msg.what){
	// case 1:
	// if (mWebView.canGoBack()){
	// mWebView.goBack();
	// }else {
	// finish();
	// }
	// break;
	// default:
	// break;
	// }
	//
	// }
	// };

}
