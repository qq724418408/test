package com.bocop.cft.activity;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.baseUtil.util.SignRequestVerify;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.boc.jx.httpUnits.NetworkUtils;
import com.bocop.cft.action.CftAction;
import com.bocop.gm.utils.HybridCallBack;
import com.bocop.gm.utils.HybridUtil;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.activity.riders.RiderFristActivity;
import com.bocop.jxplatform.util.CustomProgressDialog;
import com.bocop.jxplatform.util.LoginUtil;
import com.bocop.jxplatform.util.LoginUtilAnother;
import com.bocop.jxplatform.util.LoginUtil.ILoginListener;

@ContentView(R.layout.activity_exchange)
public class CftActivity extends BaseActivity implements ILoginListener {

	@ViewInject(R.id.tv_titleName)
	private TextView tv_titleName;
	@ViewInject(R.id.webView)
	private WebView webView;

	@ViewInject(R.id.ll_network_closed)
	private LinearLayout llNetworkClosed;
	@ViewInject(R.id.ll_network_load)
	private LinearLayout llNetworkLoad;

	private Boolean loadError;

	ProgressDialog progressDialog;

	// @ViewInject(R.id.exchangeProgressBar)
	// private WebView exchange_webView;

	@ViewInject(R.id.exchangeProgressBar)
	private ProgressBar bar;

	String strurl;
	String titleName;
	private CftAction cftAction;
	private Pattern pattern;
	String userId = "";

	@SuppressLint("JavascriptInterface")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// bar = (ProgressBar)findViewById(R.id.exchangeProgressBar);

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			// strurl= bundle.getString("url");
			// strurl = "http://182.106.129.135:9999/Wealth/app.html#/?url=home/index.html";
			strurl = "http://123.124.191.179/Wealth/app.html#/?url=home/index.html";
			titleName = bundle.getString("name");
		}
		if (titleName != null) {
			tv_titleName.setText(titleName);
		}

		initWebView();
		cftAction = new CftAction(this);
	}

	@OnClick(R.id.iv_back)
	public void back(View v) {
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
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@OnClick(R.id.btnNetworkClose)
	public void button(View v) {
		Log.i("tag", "reload");
		llNetworkLoad.setVisibility(View.INVISIBLE);
		webView.setVisibility(View.INVISIBLE);
		llNetworkClosed.setVisibility(View.INVISIBLE);
		loadError = false;
		webView.reload();
	}

	/**
	 * 初始化webView
	 */
	@SuppressLint("JavascriptInterface")
	public void initWebView() {

		loadError = false;
		llNetworkLoad.setVisibility(View.INVISIBLE);
		webView.setVisibility(View.VISIBLE);
		llNetworkClosed.setVisibility(View.INVISIBLE);

		WebSettings ws = webView.getSettings();
		ws.setSavePassword(false);
		ws.setJavaScriptEnabled(true);
		ws.setBuiltInZoomControls(true);
		ws.setUseWideViewPort(false);
		ws.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
		ws.setLoadWithOverviewMode(true);
		ws.setSupportZoom(false);
		ws.setDomStorageEnabled(true);
		webView.clearCache(true);
		// webView.addJavascriptInterface(this, "nativeApp");
		// webView.addJavascriptInterface(new JsInterface(), "nativeApp");
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onLoadResource(WebView view, String url) {
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (TextUtils.isEmpty(url)) {
					return false;
				}
				if (pattern == null) {
					pattern = Pattern.compile("apps.callbackapps", Pattern.CASE_INSENSITIVE);
				}
				Matcher matcher = pattern.matcher(url);
				if (matcher != null && matcher.find()) {
					HybridUtil.getInstance().handleAppRequest(url, cftAction, new HybridCallBack() {

						@Override
						public void errorMsg(Exception e) {

						}
					});
					return true;
				}
				if (url != null && url.contains("vzan.com")) {
					String cURL = modifyUrl(url, userId);
					webView.loadUrl(cURL);
					return false;

				}
				webView.loadUrl(url);
				return true;
			}

			@SuppressWarnings("deprecation")
			@Override
			public WebResourceResponse shouldInterceptRequest(WebView view, final String url) {
				if (pattern == null) {
					pattern = Pattern.compile("apps.callbackapps", Pattern.CASE_INSENSITIVE);
				}
				Matcher matcher = pattern.matcher(url);
				if (matcher != null && matcher.find()) {
					// mHandler.sendEmptyMessage(CASE_TIMEOUT);
					HybridUtil.getInstance().handleAppRequest(url, cftAction, new HybridCallBack() {

						@Override
						public void errorMsg(Exception e) {

						}
					});
				}
				if (url != null && url.contains("vzan.com")) {
					final String cURL = modifyUrl(url, userId);
					return super.shouldInterceptRequest(view, cURL);

				}
				return super.shouldInterceptRequest(view, url);

			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see android.webkit.WebViewClient#onPageStarted(android.webkit. WebView,
			 * java.lang.String, android.graphics.Bitmap)
			 */
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				if (NetworkUtils.isNetworkConnected(CftActivity.this)) {
					llNetworkLoad.setVisibility(View.INVISIBLE);
					webView.setVisibility(View.VISIBLE);
					llNetworkClosed.setVisibility(View.INVISIBLE);
				} else {
					view.stopLoading();
					CustomProgressDialog.showBocNetworkSetDialog(CftActivity.this);
					if (webView.canGoBack()) {
						webView.goBack();
					}
				}
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see android.webkit.WebViewClient#onPageFinished(android.webkit. WebView,
			 * java.lang.String)
			 */
			@Override
			public void onPageFinished(WebView view, String url) {
				if (!loadError) {// 当网页加载成功的时候判断是否加载成功
					llNetworkLoad.setVisibility(View.INVISIBLE);// 加载成功的话，则隐藏掉显示正在加载的视图，显示加载了网页内容的WebView
					webView.setVisibility(View.VISIBLE);
					llNetworkClosed.setVisibility(View.INVISIBLE);
				} else { // 加载失败的话，初始化页面加载失败的图，然后替换正在加载的视图页面
					llNetworkLoad.setVisibility(View.INVISIBLE);// 加载成功的话，则隐藏掉显示正在加载的视图，显示加载了网页内容的WebView
					webView.setVisibility(View.INVISIBLE);
					llNetworkClosed.setVisibility(View.VISIBLE);
				}
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see android.webkit.WebViewClient#onPageFinished(android.webkit. WebView,
			 * java.lang.String)
			 */
			@SuppressWarnings("deprecation")
			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
				Log.i("tag", "errorCode:" + errorCode + "description:" + description + "failingUrl:" + failingUrl);
				loadError = true;
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see android.webkit.WebViewClient#onReceivedSslError(android.webkit. WebView,
			 * android.webkit.SslErrorHandler, android.net.http.SslError)
			 */
			@Override
			public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
				// TODO Auto-generated method stub
				handler.proceed();
				loadError = true;
			}

		});
		webView.setWebChromeClient(new WebChromeClient() {
			// 用于获取title
			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
				Log.i("tag", title);
				if (titleName == null) {
					tv_titleName.setText(title);
				}

				if (!TextUtils.isEmpty(title) && (title.toLowerCase().contains("error")
						|| title.toLowerCase().contains("失败") || title.toLowerCase().contains("找不"))) {
					Log.i("tag88", title);
					loadError = true;
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
				// XmsWebActivity.this.getWindow().setFeatureInt(
				// Window.FEATURE_PROGRESS, newProgress);
				super.onProgressChanged(view, newProgress);
			}

			// 处理javascript中的alert
			@Override
			public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
				return super.onJsAlert(view, url, message, result);
			}
		});
		/**
		 * 生产地址
		 */

		userId = LoginUtil.getUserId(this);
		String sign = SignRequestVerify.md5SignRequestVerify(userId, "yht", "aa37228#$%@@11!!@");
		// strurl = strurl + "openId=" + userId +
		// "&authToken=26fa5fbda9cd476d899432dad4c5a982&authApp=yht&nickName=" +
		// userId + "&sign=" + sign;
		Log.i("strurl:", strurl);
		webView.loadUrl(strurl);

	}

	public void upLoadMessage() {
		if (!LoginUtil.isLog(CftActivity.this)) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					LoginUtil.authorize(CftActivity.this, CftActivity.this);
				}
			});
		} else {
			userId = LoginUtil.getUserId(this);
			cftAction.uploadMessage(webView, userId);
		}

	}

	@Override
	public void onLogin() {
		// TODO Auto-generated method stub
		upLoadMessage();
		Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
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

	public String modifyUrl(String url, String userId) {
		String modifyUserId = modifyUserId(userId);
		if (TextUtils.isEmpty(modifyUserId)) {
			return url;
		} else {
			if (url.contains("userId=")) {
				return url;
			} else {
				StringBuilder stringBuilder = new StringBuilder(url);
				if (url.contains("?")) {
					stringBuilder.append("&userId=").append(modifyUserId);
				} else {
					stringBuilder.append("?userId=").append(modifyUserId);
				}
				return stringBuilder.toString();
			}
		}
	}

	public String modifyUserId(String userId) {
		if (userId != null && userId.length() > 5) {
			StringBuilder sb = new StringBuilder(userId);
			sb.replace(sb.length() - 5, sb.length() - 1, "****");
			return sb.toString();
		} else {
			return userId;
		}
	}
}
