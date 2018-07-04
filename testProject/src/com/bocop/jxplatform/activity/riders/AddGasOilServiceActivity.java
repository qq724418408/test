package com.bocop.jxplatform.activity.riders;

import java.net.URLEncoder;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.boc.jx.common.util.AesUtils;
import com.boc.jx.common.util.ContentUtils;
import com.boc.jx.common.util.MD5Utils;
import com.boc.jx.constants.Constants;
import com.bocop.jxplatform.R;

/**
 * 加油服务
 * 
 * @author xmtang
 * 
 */
@ContentView(R.layout.activity_addgasoil)
public class AddGasOilServiceActivity extends BaseActivity {
	@ViewInject(R.id.wv_common)
	private WebView webView;
	@ViewInject(R.id.tv_titleName)
	private TextView tv_titleName;
//	private ImageView iv_title_left;
	private String phone; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_addgasoil);
		initView();
		initWebView();
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
//		tv_titleName = (TextView) findViewById(R.id.tv_titleName);
		tv_titleName.setText("加油服务");
//		iv_title_left = (ImageView) findViewById(R.id.iv_title_left);
//		iv_title_left.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				finish();
//			}
//		});
	}
	
	@OnClick(R.id.iv_title_left)
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
		// 默认点回退键，会退出Activity，需监听按键操作，使回退在WebView内发生
		// 按物理返回键处理，这里与app中接入的返回键的逻辑一致
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			if(webView.canGoBack()){
				webView.goBack();
			}
			else{
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 初始化webview
	 */
	private void initWebView() {
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
				// Tv_Title.setText(title);
			}

			// 设置网页加载的进度条
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				// PersonjiaofeiActivity.this.getWindow().setFeatureInt(Window.FEATURE_PROGRESS,webView
				// newProgress);
				super.onProgressChanged(view, newProgress);
			}

			// 处理javascript中的alert
			@Override
			public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
				return super.onJsAlert(view, url, message, result);
			}
		});
		
		String apiKey = "8c0d67757d8846deb386e62a081e2e06";// 文档中券列表的Apikey
		String apiSecret = "67bd84a92b79455ea7ff7082e936655c";// 文档中声明加密用的值
		String AESPhone = "";
		int apiST = getCurrentTime();// 当前时间戳
		String mobilephone = ContentUtils.getStringFromSharedPreference(
				AddGasOilServiceActivity.this, Constants.SHARED_PREFERENCE_NAME,
				Constants.MOBILENO);
		
		Log.i("tag","加油：" +  mobilephone);
		if(!TextUtils.isEmpty(mobilephone)){
			phone = mobilephone;
			AESPhone = AesUtils.encrypt(phone, apiSecret);
		}else{
			phone = "18611111111";
		}
		
		if(AESPhone.contains("+")){
			AESPhone = AESPhone.replace("%2B", "+");
		}
		Log.i("tag","加油AESPhone：" +  AESPhone);
		webView.loadUrl("http://open.chediandian.com/FuelCard/Index?ApiKey=8c0d67757d8846deb386e62a081e2e06&ApiST="
				+ apiST
				+ "&ApiSign="
				+ MD5Utils.md5(apiKey + apiSecret + apiST)
				+ "&Mobile="
				+ URLEncoder.encode(AESPhone));
	}
	
	/**
	 * 获取当前时间戳
	 * 
	 * @return
	 */
	private int getCurrentTime() {
		return (int) (System.currentTimeMillis() / 1000);
	}
}
