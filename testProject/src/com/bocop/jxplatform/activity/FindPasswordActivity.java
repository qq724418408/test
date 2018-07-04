package com.bocop.jxplatform.activity;

import java.util.regex.Pattern;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.boc.jx.base.BaseActivity;
import com.bocop.gm.action.GMAction;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.bean.AppInfoH5;
import com.bocop.jxplatform.config.BocSdkConfig;
import com.google.gson.Gson;

public class FindPasswordActivity extends BaseActivity {
	private TextView tv_titleName;
	private ImageView iv_title_left;
	private WebView webView;
	private Pattern pattern;
	private GMAction action;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_findpassword);
		initWebView();
		setListener();
	}

	/**
	 * 设置监听
	 */
	private void setListener() {
		iv_title_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (webView.canGoBack()) {
					webView.goBack();
				} else {
					finish();
				}
			}
		});
	}

	public class JS {
		@JavascriptInterface
		public String appInfoForGetPwd() {
			AppInfoH5 appInfoH5 = new AppInfoH5();
			appInfoH5.setClientId(BocSdkConfig.CONSUMER_KEY);
			appInfoH5.setClient_secret(BocSdkConfig.CONSUMER_SECRET);
			appInfoH5.setHideBackArrow("false");
			appInfoH5.setRegUrl("");
			appInfoH5.setThemePath("123");
			appInfoH5.setAppVersion(BocSdkConfig.APP_VERSION);
			appInfoH5.setResponseType("12");
			appInfoH5.setHasHeaderBar("1");
			return new Gson().toJson(appInfoH5);
		}

		@JavascriptInterface
		public void webAppGetPwd(String data) {
			Log.i("tag", "response=" + data);
			Toast toast = Toast.makeText(FindPasswordActivity.this, "重置成功",
					Toast.LENGTH_LONG);
			toast.show();
			finish();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 默认点回退键，会退出Activity，需监听按键操作，使回退在WebView内发生
		// 按物理返回键处理，这里与app中接入的返回键的逻辑一致
		// if ((keyCode == KeyEvent.KEYCODE_BACK)) {
		// if ("我要选车".equals(tv_titleName.getText().toString())) {
		// finish();
		// } else {
		// webView.loadUrl("javascript:commBusiness.nativeAppBack();");
		// }
		// return true;
		// }
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

	/**
	 * 初始化webview
	 */
	@SuppressWarnings("deprecation")
	private void initWebView() {
		tv_titleName = (TextView) findViewById(R.id.tv_titleName);
		tv_titleName.setText("找回登陆密码");
		iv_title_left = (ImageView) findViewById(R.id.iv_title_left);
		webView = (WebView) findViewById(R.id.wv_common);
		webView.getSettings().setSavePassword(false);
		webView.addJavascriptInterface(new JS(), "android");
		WebSettings ws = webView.getSettings();
		this.getApplicationContext().getDir("database", Context.MODE_PRIVATE)
				.getPath();
		// 启用地理定位
		ws.setGeolocationEnabled(true);
		// 设置定位的数据库路径
		// String dir;
		// ws.setGeolocationDatabasePath(dir);

		// 最重要的方法，一定要设置，这就是出不来的主要原因
		ws.setSavePassword(false);
		ws.setDomStorageEnabled(true);
		ws.setDatabaseEnabled(true);
		ws.setJavaScriptEnabled(true);
		ws.setBuiltInZoomControls(false);
		ws.setUseWideViewPort(false);
		ws.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
		ws.setLoadWithOverviewMode(true);
		ws.setSupportZoom(false);
		ws.setDomStorageEnabled(true);
		// webView.addJavascriptInterface(this, "nativeApp");
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onLoadResource(WebView view, String url) {
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return false;
			}

			// @SuppressWarnings("deprecation")
			// @Override
			// public WebResourceResponse shouldInterceptRequest(WebView view,
			// String url) {
			// Log.i("tag", "shouldInterceptRequest:" + url);
			//
			// if (pattern == null) {
			// pattern = Pattern.compile("https://open.boc.cn/wap/login",
			// Pattern.CASE_INSENSITIVE);
			// }
			// Matcher matcher = pattern.matcher(url);
			// if (matcher != null && matcher.find()) {
			// // TODO
			// // mHandler.sendEmptyMessage(CASE_TIMEOUT);
			// HybridUtil.getInstance().handleAppRequest(url, null, new
			// HybridCallBack() {
			//
			// @Override
			// public void errorMsg(Exception e) {
			//
			// }
			// });
			// }
			// return super.shouldInterceptRequest(view, url);
			// }

		});
		webView.setWebChromeClient(new WebChromeClient() {

			public void onGeolocationPermissionsShowPrompt(String origin,
					GeolocationPermissions.Callback callback) {
				callback.invoke(origin, true, false);
				super.onGeolocationPermissionsShowPrompt(origin, callback);
			}

			// 用于获取title
			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
				tv_titleName.setText("找回密码");
			}

			// public boolean shouldOverrideUrlLoading(WebView view, String url)
			// {
			// Log.i("tag", "shouldOverrideUrlLoading");
			// if (url.indexOf("https://open.boc.cn/bocop/#/app/login") != -1) {
			// // 调用系统默认浏览器处理url
			// Log.i("tag", "shouldOverrideUrlLoading find");
			// webView.stopLoading();
			// finish();
			// return true;
			// }
			// return false;
			// }

			// 设置网页加载的进度条
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
			}

			// 处理javascript中的alert
			@Override
			public boolean onJsAlert(WebView view, String url, String message,
					JsResult result) {
				return super.onJsAlert(view, url, message, result);
			}
		});

		webView.loadUrl("https://open.boc.cn/bocop/#/app/getPwd/");
	}
}
