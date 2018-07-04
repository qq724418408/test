package com.bocop.jxplatform.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.boc.jx.httpUnits.NetworkUtils;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.config.BocSdkConfig;
import com.bocop.jxplatform.util.CustomProgressDialog;
import com.bocop.jxplatform.util.LoginUtil;

@ContentView(R.layout.activity_htzq)
public class WEIHUIActivity extends BaseActivity {

	@ViewInject(R.id.htzq_titleName)
	private TextView htzq_titleName;
	@ViewInject(R.id.htzq_webView)
	private WebView htzq_webView;
	
	@ViewInject(R.id.ll_network_closed)
	private LinearLayout llNetworkClosed;
	@ViewInject(R.id.ll_network_load)
	private LinearLayout llNetworkLoad;
	
	int height;
	String userId = "";
	String token = "";
	private List<String> titles = new ArrayList<String>();
	
	private Boolean loadError ;

	@SuppressWarnings("deprecation")
	@SuppressLint({ "JavascriptInterface", "SetJavaScriptEnabled" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		loadError  = false;
		llNetworkLoad.setVisibility(View.INVISIBLE);
		htzq_webView.setVisibility(View.VISIBLE);
		llNetworkClosed.setVisibility(View.INVISIBLE);
		
		final ProgressBar bar = (ProgressBar) findViewById(R.id.myProgressBar);
		htzq_webView.getSettings().setSavePassword(false);
		WebSettings settings = htzq_webView.getSettings();
		settings.setSavePassword(false);
		settings.setJavaScriptEnabled(true);// 可以运行javaScript
		settings.setUseWideViewPort(true);
		settings.setLoadWithOverviewMode(true);
		settings.setDomStorageEnabled(true);
		settings.setSupportZoom(true);// 是否支持缩放
		settings.setBuiltInZoomControls(true);// 是否显示缩放按钮
		settings.setAppCacheEnabled(true);
		settings.setJavaScriptCanOpenWindowsAutomatically(true);
		settings.setGeolocationEnabled(true);
		htzq_webView.setWebChromeClient(new WebChromeClient() {

			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
				if (title.length() > 10) {
					htzq_titleName.setText(title.substring(0, 10));
				} else {
					htzq_titleName.setText(title);
				}
				
				 if(!TextUtils.isEmpty(title)&&(title.toLowerCase().contains("error") || title.toLowerCase().contains("失败") || title.toLowerCase().contains("找不"))){
		        	  Log.i("tag88", title);
		              loadError = true;
		          }

			}

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
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
		htzq_webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {

				view.loadUrl(url); // 在当前的webview中跳转到新的url

				return true;
			}
			
			/* (non-Javadoc)
			 * @see android.webkit.WebViewClient#onPageStarted(android.webkit.WebView, java.lang.String, android.graphics.Bitmap)
			 */
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				if (NetworkUtils.isNetworkConnected(WEIHUIActivity.this)) {
					llNetworkLoad.setVisibility(View.INVISIBLE);
					htzq_webView.setVisibility(View.VISIBLE);
					llNetworkClosed.setVisibility(View.INVISIBLE);
				} else {
					view.stopLoading();
					CustomProgressDialog.showBocNetworkSetDialog(WEIHUIActivity.this);
					if(htzq_webView.canGoBack()){
						htzq_webView.goBack();
					}
				}
				
			}
			
			/* (non-Javadoc)
			 * @see android.webkit.WebViewClient#onPageFinished(android.webkit.WebView, java.lang.String)
			 */
			@Override
			public void onPageFinished(WebView view, String url) {
				 if (!loadError) {//当网页加载成功的时候判断是否加载成功
					 	llNetworkLoad.setVisibility(View.INVISIBLE);//加载成功的话，则隐藏掉显示正在加载的视图，显示加载了网页内容的WebView
					 	htzq_webView.setVisibility(View.VISIBLE);
					 	llNetworkClosed.setVisibility(View.INVISIBLE);
		            } else { //加载失败的话，初始化页面加载失败的图，然后替换正在加载的视图页面
		            	llNetworkLoad.setVisibility(View.INVISIBLE);//加载成功的话，则隐藏掉显示正在加载的视图，显示加载了网页内容的WebView
					 	htzq_webView.setVisibility(View.INVISIBLE);
					 	llNetworkClosed.setVisibility(View.VISIBLE);
		            }
			}
			
			/* (non-Javadoc)
			 * @see android.webkit.WebViewClient#onPageFinished(android.webkit.WebView, java.lang.String)
			 */
			@SuppressWarnings("deprecation")
			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
				Log.i("tag", "errorCode:" + errorCode + "description:" + description + "failingUrl:" + failingUrl);
				loadError = true;
			}
			
		});
		
		
		
		Bundle bundle = getIntent().getExtras();

		htzq_webView.loadUrl(BocSdkConfig.WEIHUI + LoginUtil.getUserId(this));
		// 设置webView需要请求的URL，其中，如果在app中已登入，请传入userId与accessToken

	}

	@JavascriptInterface
	public void startFunction(final int str) {
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}

	@OnClick(R.id.htzq_back)
	public void back(View v) {
		// 这部分是判断返回时是否返回接入的app，此处是根据title来判断

		try {
			if (htzq_webView.canGoBack()) {
				htzq_webView.goBack();
			} else {
				finish();
			}
		} catch (Exception ex) {
			Log.i("tag", "Exception" + ex.getMessage().toString());
			finish();
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 默认点回退键，会退出Activity，需监听按键操作，使回退在WebView内发生
		// 按物理返回键处理，这里与app中接入的返回键的逻辑一致
		try {
			if ((keyCode == KeyEvent.KEYCODE_BACK)) {
				if (htzq_webView.canGoBack()) {
					htzq_webView.goBack();
				} else {
					finish();
				}
				return true;
			}
			return super.onKeyDown(keyCode, event);
		} catch (Exception ex) {
			Log.i("tag", "Exception" + ex.getMessage().toString());
			finish();
			return true;
		}
	}
	
	@OnClick(R.id.btnNetworkClose)
	public void button(View v) {
		Log.i("tag", "reload");
		llNetworkLoad.setVisibility(View.INVISIBLE);
		htzq_webView.setVisibility(View.INVISIBLE);
		llNetworkClosed.setVisibility(View.INVISIBLE);
		loadError  = false;
		htzq_webView.reload();
//		initView();
	}

}
