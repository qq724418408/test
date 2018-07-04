package com.bocop.jxplatform.activity.riders;

import com.bocop.jxplatform.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 违章处理流程 html 页面
 * 
 * @author wangzhongcai
 * 
 */
public class IllegalprocessActivity extends Activity {
	private WebView wv;
	private TextView tvTitle;
	private ImageView ivLeft;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_illegalprocess);
		initView();
		initWebView();
		setListener();
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		wv = (WebView)this.findViewById(R.id.wv);
		tvTitle = (TextView)this.findViewById(R.id.tv_titleName);
		ivLeft = (ImageView)this.findViewById(R.id.iv_title_left);
		tvTitle.setText("违章处理流程");
	}

	private void initWebView() {
		WebSettings ws = wv.getSettings();
		ws.setJavaScriptEnabled(true);
		ws.setBuiltInZoomControls(false);
		ws.setUseWideViewPort(false);
		ws.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
		ws.setLoadWithOverviewMode(true);
		ws.setSupportZoom(false);
		ws.setDomStorageEnabled(true);
		wv.setWebViewClient(new WebViewClient() {
			@Override
			public void onLoadResource(WebView view, String url) {
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return false;
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
			@Override
			public boolean onJsAlert(WebView view, String url, String message,
					JsResult result) {
				return super.onJsAlert(view, url, message, result);
			}
		});

		// 加载网页
		wv.loadUrl("http://123.124.191.179/cyh/workflow.html");
		
	}

	private void setListener() {
		ivLeft.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}
}
