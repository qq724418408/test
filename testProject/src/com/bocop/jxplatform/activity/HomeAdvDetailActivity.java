package com.bocop.jxplatform.activity;

import com.bocop.jxplatform.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.DownloadListener;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeAdvDetailActivity extends Activity{
	
	private WebView wv;
	private TextView tvTitle;
	private ImageView ivLeft;
	private String url;
	private Button btBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_advdetail);
		initView();
		initWebView();
		setListener();
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		wv = (WebView) findViewById(R.id.wv);
		btBack = (Button) findViewById(R.id.bt_back);
		url = getIntent().getStringExtra("url");
	}

	@SuppressWarnings("deprecation")
	private void initWebView() {
		WebSettings ws = wv.getSettings();
		wv.getSettings().setSavePassword(false);
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
		
         wv.setDownloadListener(new DownloadListener() {
			
			@Override
			public void onDownloadStart(String url, String userAgent,
					String contentDisposition, String mimetype, long contentLength) {
				// 监听下载功能，当用户点击下载链接的时候，直接调用系统的浏览器来下载  
                Uri uri = Uri.parse(url);  
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);  
                startActivity(intent);  
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

	private void setListener() {
		btBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
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
