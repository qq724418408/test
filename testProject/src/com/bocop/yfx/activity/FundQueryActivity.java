package com.bocop.yfx.activity;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.constants.Constants;
import com.bocop.gm.utils.HybridCallBack;
import com.bocop.gm.utils.HybridUtil;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.util.LoginUtil;
import com.bocop.yfx.action.GJJAction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

/**
 * 公积金查询
 * 
 * @author ftl
 * 
 */
@ContentView(R.layout.cpf_activity_main)
public class FundQueryActivity extends BaseActivity {
	
	@ViewInject(R.id.wbMain)
	private WebView wbMain;
	@ViewInject(R.id.loadingProgressBar)
	private ProgressBar loadingProgressBar;
	
	private Pattern pattern;
	private GJJAction action;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		initData();
		initWebView();
		loadURL();
	}
	
	private void initData(){
		action = new GJJAction(this);
	}
	
	private void loadURL() {
		String userId = LoginUtil.getUserId(this);
		String actoken = LoginUtil.getToken(this);
		if (!TextUtils.isEmpty(userId) && !TextUtils.isEmpty(actoken)) {
			//获取5位随机数
			int rannum = new Random().nextInt(90000) + 10000;
			//userid的2位长度
			String useridLength = getLength(userId);
			//token的2位长度
			String actokenLength = getLength(actoken);
			//token反序
			StringBuffer tokenReverse = new StringBuffer(actoken).reverse();
			StringBuffer url = new StringBuffer(Constants.chtUrlForGjj);
			url.append(rannum);
			url.append(useridLength);
			url.append(actokenLength);
			url.append(userId);
			url.append(tokenReverse);
			url.append("JXBOCAOS");
			wbMain.loadUrl(url.toString());
		}
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	private void initWebView(){
		WebSettings settings = wbMain.getSettings();
		settings.setJavaScriptEnabled(true);// 可以运行javaScript
		settings.setUseWideViewPort(false);// 设置自适应屏幕大小
		settings.setLoadWithOverviewMode(false);
		settings.setAppCacheEnabled(false); // 退出清除缓存
		settings.setDomStorageEnabled(true);
		settings.setSupportZoom(false);// 是否支持缩放
		settings.setBuiltInZoomControls(false);// 是否显示缩放按钮
		settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		
		wbMain.clearCache(true);
		wbMain.setWebViewClient(new WebViewClient(){
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
					HybridUtil.getInstance().handleAppRequest(url, action, new HybridCallBack() {

						@Override
						public void errorMsg(Exception e) {

						}
					});
					return true;
				}
				wbMain.loadUrl(url);
				return true;
			}
			
			@SuppressWarnings("deprecation")
			@Override
			public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
				if (pattern == null) {
					pattern = Pattern.compile("apps.callbackapps", Pattern.CASE_INSENSITIVE);
				}
				Matcher matcher = pattern.matcher(url);
				if (matcher != null && matcher.find()) {
					// mHandler.sendEmptyMessage(CASE_TIMEOUT);
					HybridUtil.getInstance().handleAppRequest(url, action, new HybridCallBack() {

						@Override
						public void errorMsg(Exception e) {

						}
					});
				}
				return super.shouldInterceptRequest(view, url);
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				if (loadingProgressBar != null) {
					loadingProgressBar.setVisibility(View.VISIBLE);
					Log.i("TAG", "进度显示");
				}
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				if (loadingProgressBar != null) {
					loadingProgressBar.setVisibility(View.GONE);
					Log.i("TAG", "进度隐藏");
				}
			}

			@Override
			public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
				// super.onReceivedSslError(view, handler, error);
				handler.proceed();
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		if (wbMain != null) {
			wbMain.stopLoading();
			wbMain.clearCache(true);
			wbMain.destroy();
			wbMain = null;
		}
		super.onDestroy();
	}

	/**
	 * 上传用户信息
	 */
	public void getUserInfoCall() {
		//每次调用去除 tipsFlas
		SharedPreferences sp = getSharedPreferences("tipsFlag", Context.MODE_PRIVATE);
		final String tipsFlag = sp.getString("flagKey", "");
		runOnUiThread(new Runnable() {

			@Override
			public void run() {

				Map<String, Object> map = new HashMap<>();
				map.put("userId", LoginUtil.getUserId(FundQueryActivity.this));
				map.put("actoken", LoginUtil.getToken(FundQueryActivity.this)); 
				map.put("client", "Android");
				map.put("tipsFlag", tipsFlag);
				if(!TextUtils.isEmpty(cacheBean.getCount())){
					map.put("count", cacheBean.getCount());
				}else{
					map.put("count", "0");
				}
				HybridUtil.getInstance().handleJsRequest(wbMain, "getUserInfo", map, new HybridCallBack() {

					@Override
					public void errorMsg(Exception e) {
						
					}
				});

			}
		});
	}
	
	private String getLength(String content) {
		int length = content.length();
		if (length > 9){
			return String.valueOf(length);
		} else {
			return "0" + length;
		}
	}
}
