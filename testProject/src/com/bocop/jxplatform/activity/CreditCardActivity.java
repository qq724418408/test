package com.bocop.jxplatform.activity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
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
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

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

@ContentView(R.layout.activity_bmjf)
public class CreditCardActivity extends BaseActivity {

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
	@ViewInject(R.id.lliv_network_closed)
	private LinearLayout llivNetworkClosed;

	private Boolean loadError ;
	
	@SuppressLint("JavascriptInterface")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		loadError  = false;
		llNetworkLoad.setVisibility(View.INVISIBLE);
		webView.setVisibility(View.VISIBLE);
		llNetworkClosed.setVisibility(View.INVISIBLE);
		llivNetworkClosed.setVisibility(View.INVISIBLE);
		
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
		// 默认点回退键，会退出Activity，需监听按键操作，使回退在WebView内发生
		// 按物理返回键处理，这里与app中接入的返回键的逻辑一致
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			if (webView.canGoBack()) {

				webView.goBack();
			} else {
				finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
	@OnClick(R.id.btnNetworkClose)
	public void button(View v) {
		Log.i("tag", "reload");
		llNetworkLoad.setVisibility(View.INVISIBLE);
		webView.setVisibility(View.INVISIBLE);
		llNetworkClosed.setVisibility(View.INVISIBLE);
		llivNetworkClosed.setVisibility(View.INVISIBLE);
		loadError  = false;
		getFinances(this);
//		webView.reload();
	}

	/**
	 * 初始化webView
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("JavascriptInterface")
	public void initWebView(String accessToken) {
		WebSettings ws = webView.getSettings();
		ws.setSavePassword(false);
		ws.setJavaScriptEnabled(true);
		ws.setBuiltInZoomControls(true);
		ws.setUseWideViewPort(false);
		ws.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
		ws.setLoadWithOverviewMode(true);
		ws.setSupportZoom(false);
		ws.setAppCacheEnabled(false);
		webView.addJavascriptInterface(this, "nativeApp");
		webView.getSettings().setSavePassword(false);
		// webView.addJavascriptInterface(new JsInterface(), "nativeApp");
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onLoadResource(WebView view, String url) {
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return false;
			}
			
			/* (non-Javadoc)
			 * @see android.webkit.WebViewClient#onPageStarted(android.webkit.WebView, java.lang.String, android.graphics.Bitmap)
			 */
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				if (NetworkUtils.isNetworkConnected(CreditCardActivity.this)) {
					llNetworkLoad.setVisibility(View.INVISIBLE);
					webView.setVisibility(View.VISIBLE);
					llNetworkClosed.setVisibility(View.INVISIBLE);
					llivNetworkClosed.setVisibility(View.INVISIBLE);
				} else {
					view.stopLoading();
					CustomProgressDialog.showBocNetworkSetDialog(CreditCardActivity.this);
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
					 	llivNetworkClosed.setVisibility(View.INVISIBLE);
		            } else { //加载失败的话，初始化页面加载失败的图，然后替换正在加载的视图页面
		            	llNetworkLoad.setVisibility(View.INVISIBLE);//加载成功的话，则隐藏掉显示正在加载的视图，显示加载了网页内容的WebView
		            	webView.setVisibility(View.INVISIBLE);
					 	llNetworkClosed.setVisibility(View.VISIBLE);
					 	llivNetworkClosed.setVisibility(View.VISIBLE);
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
			// 用于获取title
			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
				tv_titleName.setText(title);
			}

			// 设置网页加载的进度条
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				CreditCardActivity.this.getWindow().setFeatureInt(
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
		// String token =
		// ContentUtils.getSharePreStr(PersonjiaofeiActivity.this,
		// Constants.SHARED_PREFERENCE_NAME, Constants.ACCESS_TOKEN);
		String userid = LoginUtil.getUserId(CreditCardActivity.this);
		System.out.println("你好======" + userid);
		/**
		 * 生产地址
		 */
		webView.loadUrl("http://219.141.191.126:80/conPayH5/?" + "&userId="
				+ userid + "&accessToken=" + accessToken
				+ "&channel=jxeht&serviceName=point");
		// webView.loadUrl("http://219.141.191.126:80/conPayH5/?channel=jxeht"
		// + "&userId=" + userid + "&accessToken=" + accessToken +
		// "channel=jxeht&serviceName=point");
		// http://219.141.191.126:80/conPayH5/?userId=***&accessToken=***channel=xxx&serviceName=point

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
		params.put("client_id", "205");
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
						initWebView("");
					}
				});
	}

}
