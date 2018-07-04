package com.bocop.jxplatform.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.bocop.jxplatform.R;

@ContentView(R.layout.activity_software_web)
public class SoftwareWebActivity extends BaseActivity {

	@ViewInject(R.id.tv_titleName)
	private TextView tv_titleName;
	@ViewInject(R.id.webView)
	private WebView webView;
	
	@ViewInject(R.id.exchangeProgressBar)
	private ProgressBar bar;

	String strurl;
	String titleName;
	
	@SuppressLint("JavascriptInterface")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		bar = (ProgressBar)findViewById(R.id.exchangeProgressBar);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		Log.i("log", "onCreate");
		if(bundle != null){
			strurl= bundle.getString("url");
			titleName = bundle.getString("name");
			Log.i("log", strurl);
		}
		initWebView();

	}

	@OnClick(R.id.iv_back)
	public void back(View v) {
		if(webView.canGoBack()){
			webView.goBack();
		}
		else{
			finish();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(webView.canGoBack()){
			webView.goBack();
		}
		else{
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
	/**
	 * 初始化webView
	 */
	@SuppressLint("JavascriptInterface")
	public void initWebView() {
		Log.i("log", "initWebView");
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
		Log.i("log", "initWebView2");
//		webView.setDownloadListener(new MyWebViewDownLoadListener());
//		webView.addJavascriptInterface(this, "nativeApp");
		// webView.addJavascriptInterface(new JsInterface(), "nativeApp");
		
		Log.i("log", "WebSettings");
		webView.setBackgroundColor(0);
		webView.getBackground().setAlpha(2); // 设置填充透明度 范围：0-255  
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
				Log.i("log", "onReceivedTitle");
				super.onReceivedTitle(view, title);
//					tv_titleName.setText(title);
			}

			// 设置网页加载的进度条
			@Override
	          public void onProgressChanged(WebView view, int newProgress) {
				Log.i("log", "onProgressChanged");
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
		/**
		 * 生产地址
		 */
		Log.i("log", "loadUrl");
		webView.loadUrl(strurl);

	}
	
//	
//	private class MyWebViewDownLoadListener implements DownloadListener{   
//		  
//        @Override  
//        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,   
//                                    long contentLength) {              
//            Log.i("tag", "url="+url);              
//            Log.i("tag", "userAgent="+userAgent);   
//            Log.i("tag", "contentDisposition="+contentDisposition);            
//            Log.i("tag", "mimetype="+mimetype);   
//            Log.i("tag", "contentLength="+contentLength);   
//            Uri uri = Uri.parse(url);   
//            Intent intent = new Intent(Intent.ACTION_VIEW, uri);   
//            startActivity(intent);              
//        }   
//    } 
}
