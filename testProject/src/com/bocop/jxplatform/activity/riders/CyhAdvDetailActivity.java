package com.bocop.jxplatform.activity.riders;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bocop.jxplatform.R;

public class CyhAdvDetailActivity extends Activity {
	private WebView wv;
	private TextView tvTitle;
	private ImageView ivLeft;
	private String url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cyh_advdetail);
		initView();
		initWebView();
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		wv = (WebView) findViewById(R.id.wv);
		url = getIntent().getStringExtra("url");
	}

	private void initWebView() {
		WebSettings ws = wv.getSettings();
		ws.setJavaScriptEnabled(true);
		ws.setSupportMultipleWindows(true);
		ws.setBuiltInZoomControls(true);
		ws.setSupportZoom(true);
		ws.setCacheMode(WebSettings.LOAD_NORMAL);
		// webView.setFitsSystemWindows(true);
		ws.setJavaScriptCanOpenWindowsAutomatically(true);
		String dir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath(); 
		//设置定位的数据库路径  
		ws.setGeolocationDatabasePath(dir);  
		ws.setDatabaseEnabled(true);
		// webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
		ws.setDomStorageEnabled(true);
		//启用地理定位  
		ws.setGeolocationEnabled(true);  
	    ws.setDefaultZoom(ZoomDensity.FAR);// 屏幕自适应网页
		wv.setWebViewClient(new WebViewClient() {
			@Override
			public void onLoadResource(WebView view, String url) {
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
         
		wv.setWebChromeClient(new WebChromeClient() {
			// 用于获取title
			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
			}

			// 设置网页加载的进度条
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
			}

			// 处理javascript中的alert
//			@Override
//			public boolean onJsAlert(WebView view, String url, String message,
//					JsResult result) {
//				return super.onJsAlert(view, url, message, result);
//			}
		});

		// 加载网页
		wv.loadUrl(url);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 默认点回退键，会退出Activity，需监听按键操作，使回退在WebView内发生
		// 按物理返回键处理，这里与app中接入的返回键的逻辑一致
		if ((keyCode == KeyEvent.KEYCODE_BACK && wv.canGoBack())) {
			wv.goBack();
			return true;
		} else {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
