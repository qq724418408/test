package com.bocop.jxplatform.activity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.boc.jx.common.util.ContentUtils;
import com.boc.jx.constants.Constants;
import com.boc.jx.httpUnits.NetworkUtils;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.config.BocSdkConfig;
import com.bocop.jxplatform.http.RestTemplateJxBank;
import com.bocop.jxplatform.util.CustomProgressDialog;
import com.bocop.jxplatform.util.LoginUtil;
import com.bocsoft.ofa.http.asynchttpclient.JsonHttpResponseHandler;
import com.bocsoft.ofa.http.asynchttpclient.expand.JsonRequestParams;

@SuppressLint("SetJavaScriptEnabled")
@ContentView(R.layout.activity_bmjf)
public class JIEHUIActivity extends BaseActivity {

	@ViewInject(R.id.tv_titleName)
	private TextView tv_titleName;
	@ViewInject(R.id.webView)
	private WebView webView;
	private String newAccessToken;
	private String refreshToken;
	ProgressDialog progressDialog;
	
	@ViewInject(R.id.ll_network_closed)
	private LinearLayout llNetworkClosed;
	@ViewInject(R.id.ll_network_load)
	private LinearLayout llNetworkLoad;
	
	String strNewAccessToken;
	String strRefreshToken;
	private String currentUrl;
	private String flag;
	private String strTitle;
	String url;
	
	private Boolean loadError ;

	@SuppressLint("JavascriptInterface")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		flag = getIntent().getExtras().getString("flag");
		
		loadError  = false;
		llNetworkLoad.setVisibility(View.INVISIBLE);
		webView.setVisibility(View.VISIBLE);
		llNetworkClosed.setVisibility(View.INVISIBLE);
		
		if(flag.equals("0")){
			strTitle = "个人售汇";
			url = BocSdkConfig.JIEHUI;
		}else{
			strTitle = "个人购汇";
			url = BocSdkConfig.GOUHUI;
		}
		tv_titleName.setText(strTitle);
		getFinances(this);

	}
	


	@OnClick(R.id.iv_back)
	public void back(View v) {
		// 这部分是判断返回时是否返回接入的app，此处是根据title来判断
		if (webView.canGoBack()) {
			webView.goBack();
		} else {
			finish();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
			if (webView.canGoBack()) {
				webView.goBack();
			} else {
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 初始化webView
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint({ "JavascriptInterface", "NewApi" })
	public void initWebView(String accessToken) {
		WebSettings ws = webView.getSettings();
		webView.getSettings().setSavePassword(false);
		ws.setJavaScriptEnabled(true);
		
		if (Build.VERSION.SDK_INT >= 14) {
			ws.setAllowFileAccessFromFileURLs(true);
			ws.setAllowUniversalAccessFromFileURLs(true);
			// webSettings.setSaveFormData(true);
		}
		ws.setAppCacheMaxSize(1024 * 1024 * 8);
		// String appCachePath =
		// getApplicationContext().getCacheDir().getAbsolutePath();
		// webSettings.setAppCachePath(appCachePath);
		// webSettings.setAppCacheEnabled(true);

		ws.setJavaScriptEnabled(true);
		ws.setRenderPriority(RenderPriority.HIGH);// 提高渲染的优先级
		// webSettings.setDatabaseEnabled(true);
		ws.setDomStorageEnabled(true);
		ws.setAllowFileAccess(true);
		ws.setGeolocationEnabled(true);
		webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		ws.setDefaultTextEncodingName("UTF-8");
		webView.setBackgroundColor(getResources().getColor(R.color.white));
		// webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
		// 支持跨域请求
		if (Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN)
			ws.setAllowUniversalAccessFromFileURLs(true);

		webView.addJavascriptInterface(new JS(), "app_afs");
		
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onLoadResource(WebView view, String url) {
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				currentUrl = url;
				if (url.endsWith("bindcard.html")) {
					Toast.makeText(JIEHUIActivity.this, "请去绑卡", Toast.LENGTH_SHORT).show();
					finish();
				}
				if (url.endsWith("goback.html")) {
					finish();
				} else if (url.endsWith("login.html")) {
//					LoginUtil.isLog(JIEHUIActivity.this);
//					IApplication.isLogin = false;
//					Intent intent = new Intent(JIEHUIActivity.this, LoginActivity.class);
//					intent.putExtra("url", url);
//					startActivityForResult(intent, 1000);
				} else
					view.loadUrl(url);
				return true;
			}
			
			/* (non-Javadoc)
			 * @see android.webkit.WebViewClient#onPageStarted(android.webkit.WebView, java.lang.String, android.graphics.Bitmap)
			 */
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				if (NetworkUtils.isNetworkConnected(JIEHUIActivity.this)) {
					llNetworkLoad.setVisibility(View.INVISIBLE);
					webView.setVisibility(View.VISIBLE);
					llNetworkClosed.setVisibility(View.INVISIBLE);
				} else {
					view.stopLoading();
					CustomProgressDialog.showBocNetworkSetDialog(JIEHUIActivity.this);
					if(webView.canGoBack()){
						webView.goBack();
					}
				}
				
			}
			
			/* (non-Javadoc)
			 * @see android.webkit.WebViewClient#onPageFinished(android.webkit.WebView, java.lang.String)
			 */
			@Override
			public void onPageFinished(WebView view, String url) {
				 if (!loadError) {//当网页加载成功的时候判断是否加载成功
					 	llNetworkLoad.setVisibility(View.INVISIBLE);//加载成功的话，则隐藏掉显示正在加载的视图，显示加载了网页内容的WebView
					 	webView.setVisibility(View.VISIBLE);
					 	llNetworkClosed.setVisibility(View.INVISIBLE);
		            } else { //加载失败的话，初始化页面加载失败的图，然后替换正在加载的视图页面
		            	llNetworkLoad.setVisibility(View.INVISIBLE);//加载成功的话，则隐藏掉显示正在加载的视图，显示加载了网页内容的WebView
		            	webView.setVisibility(View.INVISIBLE);
					 	llNetworkClosed.setVisibility(View.VISIBLE);
		            }
			}
			
			/* (non-Javadoc)
			 * @see android.webkit.WebViewClient#onPageFinished(android.webkit.WebView, java.lang.String)
			 */
			@SuppressWarnings("deprecation")
			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
				Log.i("tag", "errorCode:" + errorCode + "description:" + description + "failingUrl:" + failingUrl);
				loadError = true;
			}
			
		});
		webView.setWebChromeClient(new WebChromeClient() {
			


			// 设置网页加载的进度条
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				JIEHUIActivity.this.getWindow().setFeatureInt(
						Window.FEATURE_PROGRESS, newProgress);
				super.onProgressChanged(view, newProgress);
			}

			// 处理javascript中的alert
			@Override
			public boolean onJsAlert(WebView view, String url, String message,
					JsResult result) {
				return super.onJsAlert(view, url, message, result);
			}
		});
		String userid = LoginUtil.getUserId(JIEHUIActivity.this);
		System.out.println("你好======" + userid);
		/**
		 * 生产地址
		 */
		webView.loadUrl(url);
		
//		webView.loadUrl("http://219.141.191.126:80/conPayH5/?channel=jxeht"
//				+ "&userId=" + userid + "&accessToken=" + accessToken);
//		http://219.141.191.126:80/FM/index.jsp#/yd/userId/accessToken/channel/key

	}
	
	@OnClick(R.id.btnNetworkClose)
	public void button(View v) {
		Log.i("tag", "reload");
		llNetworkLoad.setVisibility(View.INVISIBLE);
		webView.setVisibility(View.INVISIBLE);
		llNetworkClosed.setVisibility(View.INVISIBLE);
		loadError  = false;
		getFinances(this);
//		webView.reload();
	}

	public void getFinances(final Context context) {
		RestTemplateJxBank restTemplate = new RestTemplateJxBank(context);
		JsonRequestParams params = new JsonRequestParams();
		String action = LoginUtil.getToken(context);
		String userid = LoginUtil.getUserId(context);
		// 11-14 14:20:20.832: I/System.out(10366):
		// //
		// https://openapi.boc.cn/bocop/oauth/token?acton=6eb33fd1-c8ee-44f4-9381-08cd5d4fc98e&client_id=205&password=&grant_type=implicit&user_id=arekas&enctyp=
		params.put("enctyp", "");
		params.put("password", "");
		params.put("grant_type", "implicit");
		params.put("user_id", userid);
		// params.put("client_secret", MainActivity.CONSUMER_SECRET);
		params.put("client_id", "374");
		// params.put("client_id", "357");
		params.put("acton", action);
		// https://openapi.boc.cn/bocop/oauth/token
		restTemplate.postGetType("https://openapi.boc.cn/oauth/token", params,
				new JsonHttpResponseHandler("UTF-8") {
					@Override
					public void onStart() {
						super.onStart();
						progressDialog = new ProgressDialog(context);
						progressDialog.setMessage("正在努力加载中...");
						progressDialog.setCanceledOnTouchOutside(false);
						progressDialog.show();
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						progressDialog.dismiss();
						System.out.println("便民缴费===========》"
								+ response.toString());
						try {
							newAccessToken = response.getString("access_token");
							refreshToken = response.getString("refresh_token");
							strNewAccessToken = newAccessToken;
							strRefreshToken = refreshToken;
						} catch (JSONException e) {
							newAccessToken = "";
							refreshToken = "";
							e.printStackTrace();
						} finally {
							initWebView(newAccessToken);
							// initWebView();
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						if (progressDialog != null) {
							progressDialog.dismiss();
						}
						llNetworkClosed.setVisibility(View.VISIBLE);
					}
				});
	}
	
	public class JS {
		/**
		 * @return access_token
		 */
		@JavascriptInterface
		public String getAccessToken() {
			Log.i("tag8", "getAccessToken:" + strNewAccessToken );
			return strNewAccessToken;
			
		}

		/**
		 * @return refresh_token
		 */
		@JavascriptInterface
		public String getRefreshToken() {
			Log.i("tag8", "getRefreshToken:" + strRefreshToken);
			return strRefreshToken;
		}

		@JavascriptInterface
		public String getUserName() {
			Log.i("tag8", "getUserName:" + LoginUtil.getUserId(JIEHUIActivity.this));
			return LoginUtil.getUserId(JIEHUIActivity.this);
		}

		/**
		 * 返回已登录用户名
		 */
		@JavascriptInterface
		public String getUserId() {
			Log.i("tag8", "getUserId:" + LoginUtil.getUserId(JIEHUIActivity.this));
			return LoginUtil.getUserId(JIEHUIActivity.this);
		}

		/**
		 * @return 0为不需要title，1为需要
		 */
		@JavascriptInterface
		public int getHeader() {
			Log.i("tag8", "getHeader");
//			if (isDBS()) {
//				return 1;
//			}
			return 0;
		}

		@JavascriptInterface
		public String isIndex() {
			Log.i("tag8", "isIndex");
			return "1";
		}

		@JavascriptInterface
		public String getUid() {
			Log.i("tag8", "getUid:" + LoginUtil.getUserId(JIEHUIActivity.this));
			return LoginUtil.getUserId(JIEHUIActivity.this);
		}

		/**
		 * @return
		 */
		@JavascriptInterface
		public int isPointLogin() {
			Log.i("tag8", "isPointLogin");
			return 1;
		}
	}
	
//	private boolean isDBS() {
//		boolean result = false;
//		int client_id = Integer.parseInt(BocSdkConfig.CONSUMER_KEY);
//		switch (client_id) {
//		case 386:
//			result = true;
//			break;
//		}
//		return result;
//	}

}
