package com.bocop.jxplatform.activity;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.boc.jx.httpUnits.NetworkUtils;
import com.bocop.gm.utils.HybridCallBack;
import com.bocop.gm.utils.HybridUtil;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.config.BocSdkConfig;
import com.bocop.jxplatform.util.CustomProgressDialog;
import com.bocop.jxplatform.util.LoginUtil;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.TextView;


@SuppressLint("JavascriptInterface")
@ContentView(R.layout.activity_touming_webview)
public class WebDialogActivity extends BaseActivity {

    @ViewInject(R.id.tv_titleName)
	private TextView tv_titleName;
	@ViewInject(R.id.webView)
	private WebView webView;
	
	private Pattern pattern;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
//        Window window=getWindow();
//        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        WindowManager.LayoutParams wl = window.getAttributes();
//        wl.height = WindowManager.LayoutParams.FLAG_FULLSCREEN;
//        wl.flags=WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
//        wl.alpha=1.0f;//这句就是设置窗口里控件的透明度的．０.０全透明．１.０不透明．
//        wl.x = 0;
//        wl.y = 0;
//        window.setAttributes(wl);
        
       

        
        WebSettings ws = webView.getSettings();
		ws.setSavePassword(false);
		ws.setJavaScriptEnabled(true);
		ws.setBuiltInZoomControls(true);
		ws.setUseWideViewPort(false);
		ws.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
		ws.setLoadWithOverviewMode(true);
		ws.setSupportZoom(false);
		ws.setDomStorageEnabled(true);
        ws.setUseWideViewPort(true); 
        ws.setLoadWithOverviewMode(true); 
        
        webView.setBackgroundColor(0); // 设置背景色 
        
        webView.addJavascriptInterface(this, "app_afs");
        webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onGeolocationPermissionsShowPrompt(String origin,
					android.webkit.GeolocationPermissions.Callback callback) {
				callback.invoke(origin, true, false);
				super.onGeolocationPermissionsShowPrompt(origin, callback);
			}

			@Override
			public void onReceivedTitle(WebView view, String title) {
				// TODO Auto-generated method stub
				super.onReceivedTitle(view, title);
			}
		});
        
        webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (TextUtils.isEmpty(url)) {
					return false;
				}
				if (pattern == null) {

					pattern = Pattern.compile("apps.callbackapps",
							Pattern.CASE_INSENSITIVE);
				}
				Matcher matcher = pattern.matcher(url);
				if (matcher != null && matcher.find()) {
					// TODO
					HybridUtil.getInstance().handleAppRequest(url,
							WebDialogActivity.this, new HybridCallBack() {

								@Override
								public void errorMsg(Exception e) {

								}
							});
					return true;
				}
				view.loadUrl(url); // 在当前的webview中跳转到新的url

				return true;
			}

			@SuppressWarnings("deprecation")
			@Override
			public WebResourceResponse shouldInterceptRequest(WebView view,
					String url) {
				if (pattern == null) {
					pattern = Pattern.compile("apps.callbackapps",
							Pattern.CASE_INSENSITIVE);
				}
				Matcher matcher = pattern.matcher(url);
				if (matcher != null && matcher.find()) {
					// TODO
					// mHandler.sendEmptyMessage(CASE_TIMEOUT);
					HybridUtil.getInstance().handleAppRequest(url,
							WebDialogActivity.this, new HybridCallBack() {

								@Override
								public void errorMsg(Exception e) {

								}
							});
				}
				return super.shouldInterceptRequest(view, url);
			}
		});
        
        
        webView.loadUrl(BocSdkConfig.jfscCsurl);
    }
    
    @OnClick(R.id.iv_caishen_close)
	public void button(View v) {
    	finish();
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
	
	
	public void getLoginViewResultCall() {

		runOnUiThread(new Runnable() {
			@Override
			public void run() {

				String userId = LoginUtil.getUserId(WebDialogActivity.this);

				if (!TextUtils.isEmpty(userId)) {

					Map<String, Object> map = new HashMap<>();
					map.put("userId", LoginUtil.getUserId(WebDialogActivity.this));
					map.put("actoken", LoginUtil.getToken(WebDialogActivity.this));
					HybridUtil.getInstance().handleJsRequest(webView,
							"getLoginViewResultCall", map,
							new HybridCallBack() {

								@Override
								public void errorMsg(Exception e) {
									// TODO Auto-generated method
									// stub

								}
							});

					return;
				}

				LoginUtil.authorize(WebDialogActivity.this,
						new LoginUtil.ILoginListener() {
							@Override
							public void onLogin() {
								Map<String, Object> map = new HashMap<>();
								map.put("userId",
										LoginUtil.getUserId(WebDialogActivity.this));
								map.put("actoken",
										LoginUtil.getToken(WebDialogActivity.this));
								HybridUtil.getInstance().handleJsRequest(
										webView, "getLoginViewResultCall",
										map, new HybridCallBack() {

											@Override
											public void errorMsg(Exception e) {
												// TODO Auto-generated method
												// stub

											}
										});

							}

							@Override
							public void onCancle() {

							}

							@Override
							public void onError() {

							}

							@Override
							public void onException() {

							}
						});
			}
		});
	}
}
