package com.bocop.jxplatform.activity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.boc.jx.common.util.ContentUtils;
import com.boc.jx.constants.Constants;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.config.BocSdkConfig;
import com.bocop.jxplatform.http.RestTemplateJxBank;
import com.bocop.jxplatform.util.LoginUtil;
import com.bocsoft.ofa.http.asynchttpclient.JsonHttpResponseHandler;
import com.bocsoft.ofa.http.asynchttpclient.expand.JsonRequestParams;

@ContentView(R.layout.activity_bmjf)
public class EXYActivity extends BaseActivity {

	@ViewInject(R.id.tv_titleName)
	private TextView tv_titleName;
	@ViewInject(R.id.webView)
	private WebView webView;
	private String newAccessToken;
	private String refreshToken;
	ProgressDialog progressDialog;

	@SuppressLint("JavascriptInterface")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initWebView();

	}

	@OnClick(R.id.iv_back)
	public void back(View v) {
			finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 默认点回退键，会退出Activity，需监听按键操作，使回退在WebView内发生
		// 按物理返回键处理，这里与app中接入的返回键的逻辑一致
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 初始化webView
	 */
	@SuppressLint("JavascriptInterface")
	public void initWebView() {
		WebSettings ws = webView.getSettings();
		ws.setSavePassword(false);
		ws.setJavaScriptEnabled(true);
		ws.setBuiltInZoomControls(false);
		ws.setUseWideViewPort(false);
		ws.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
		ws.setLoadWithOverviewMode(true);
		ws.setSupportZoom(false);
//		webView.addJavascriptInterface(this, "nativeApp");
		webView.getSettings().setSavePassword(false);
		webView.setDownloadListener(new MyWebViewDownLoadListener());
		// webView.addJavascriptInterface(new JsInterface(), "nativeApp");
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
				tv_titleName.setText(title);
			}

			// 设置网页加载的进度条
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				EXYActivity.this.getWindow().setFeatureInt(
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
		String userid = LoginUtil.getUserId(EXYActivity.this);
		System.out.println("你好======" + userid);
		/**
		 * 生产地址
		 */
		webView.loadUrl("http://open.boc.cn/mobile#/search2Detail/41295");

	}
	
	
	public class MyWebViewDownLoadListener implements DownloadListener{   
		  
        @Override  
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,   
                                    long contentLength) {              
            Log.i("tag", "url="+url);              
            Log.i("tag", "userAgent="+userAgent);   
            Log.i("tag", "contentDisposition="+contentDisposition);            
            Log.i("tag", "mimetype="+mimetype);   
            Log.i("tag", "contentLength="+contentLength);   
            Uri uri = Uri.parse(url);   
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);   
            startActivity(intent);              
        }   
    } 
	

}
