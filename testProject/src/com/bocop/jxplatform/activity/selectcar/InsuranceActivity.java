package com.bocop.jxplatform.activity.selectcar;

import com.bocop.jxplatform.R;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class InsuranceActivity extends Activity {
//	private TextView tv_titleName;
	private Button iv_title_left;
	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_insurance);
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
				// 这部分是判断返回时是否返回接入的app，此处是根据title来判断
//				if ("我要选车".equals(tv_titleName.getText().toString())
//						|| "".equals(tv_titleName.getText().toString())) {
//					finish();
//				} else {
//					webView.loadUrl("javascript:commBusiness.nativeAppBack();");
//				}
//				if(webView.canGoBack()){
//					webView.goBack();
//				}else{
//					finish();
//				}
				finish();
			}
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 默认点回退键，会退出Activity，需监听按键操作，使回退在WebView内发生
		// 按物理返回键处理，这里与app中接入的返回键的逻辑一致
//		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
//			if ("我要选车".equals(tv_titleName.getText().toString())) {
//				finish();
//			} else {
//				webView.loadUrl("javascript:commBusiness.nativeAppBack();");
//			}
//			return true;
//		}
		if ((keyCode == KeyEvent.KEYCODE_BACK)){
//			if(webView.canGoBack()){
//				webView.goBack();
//			}else{
				finish();
			//}
			return true;
		}
		
		return super.onKeyDown(keyCode, event);
	}


	/**
	 * 初始化webview
	 */
	private void initWebView() {
//		tv_titleName = (TextView) findViewById(R.id.tv_titleName);
		iv_title_left = (Button) findViewById(R.id.iv_title_left);
		iv_title_left.setBackgroundColor(Color.TRANSPARENT);
		webView = (WebView) findViewById(R.id.wv_common);
		WebSettings ws = webView.getSettings();
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
		});
		webView.setWebChromeClient(new WebChromeClient() {
			// 用于获取title
			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
			//	tv_titleName.setText(title);
			}

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

		webView.loadUrl("http://m.bocins.com/#index?mediaSource=bocyihuitong.html");
	}
}
