package com.bocop.jxplatform.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

@ContentView(R.layout.activity_jket)
public class JKETActivity extends BaseActivity{
	
	@ViewInject(R.id.jket_titleName)
	private TextView jket_titleName;
	@ViewInject(R.id.jket_webView)
	private WebView jket_webView;
	
	@ViewInject(R.id.ll_network_closed)
	private LinearLayout llNetworkClosed;
	@ViewInject(R.id.ll_network_load)
	private LinearLayout llNetworkLoad;
	private Boolean loadError ;
	private Pattern pattern;
	int height;
	String userId="";
	String token="";
	private List<String> titles = new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		loadError  = false;
		llNetworkLoad.setVisibility(View.VISIBLE);
		jket_webView.setVisibility(View.INVISIBLE);
		llNetworkClosed.setVisibility(View.INVISIBLE);
		
		WebSettings settings = jket_webView.getSettings();
		settings.setDatabaseEnabled(true);
		settings.setAllowFileAccess(true);
		String dir=this.getApplicationContext().getDir("database",Context.MODE_PRIVATE).getPath();
		settings.setGeolocationEnabled(true);
		settings.setGeolocationDatabasePath(dir);
    	settings.setJavaScriptEnabled(true);//可以运行javaScript
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setDomStorageEnabled(true);
        settings.setSupportZoom(false);//是否支持缩放
        settings.setBuiltInZoomControls(false);//是否显示缩放按钮
        settings.setAppCacheEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
     // 这句话必须，在HTML5版便民中，会调用setLoginStatus()方法进行登入状态的app与HTML5的同步
        jket_webView.setWebChromeClient(new WebChromeClient(){
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
				jket_titleName.setText(title);
				titles.add(title);
			}
		});
        
        jket_webView.setWebViewClient(new WebViewClient(){
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
					// TODO
					HybridUtil.getInstance().handleAppRequest(url, JKETActivity.this, new HybridCallBack() {

						@Override
						public void errorMsg(Exception e) {

						}
					});
					return true;
				}
        	view.loadUrl(url); //在当前的webview中跳转到新的url

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
					// TODO
					// mHandler.sendEmptyMessage(CASE_TIMEOUT);
					HybridUtil.getInstance().handleAppRequest(url, JKETActivity.this, new HybridCallBack() {

						@Override
						public void errorMsg(Exception e) {

						}
					});
				}
				return super.shouldInterceptRequest(view, url);
			}
        	/* (non-Javadoc)
			 * @see android.webkit.WebViewClient#onPageStarted(android.webkit.WebView, java.lang.String, android.graphics.Bitmap)
			 */
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				if (NetworkUtils.isNetworkConnected(JKETActivity.this)) {
					llNetworkLoad.setVisibility(View.INVISIBLE);
					jket_webView.setVisibility(View.VISIBLE);
					llNetworkClosed.setVisibility(View.INVISIBLE);
				} else {
					view.stopLoading();
					CustomProgressDialog.showBocNetworkSetDialog(JKETActivity.this);
					if(jket_webView.canGoBack()){
						if(titles.get(titles.size() - 1).equals("健康通")||"健康通"==titles.get(titles.size() - 1)){
							jket_webView.loadUrl("javascript:selhos();");
							return;
						}else if(titles.get(titles.size() - 1).equals("选择医院")){
							jket_webView.loadUrl("javascript:selcity();");
							return;
						}else if(titles.get(titles.size() - 1).equals("选择城市")){
							finish();
						}
						jket_webView.goBack();
						if(titles.size()>0){
							titles.remove(titles.size() - 1);
							jket_titleName.setText(titles.get(titles.size() - 1));
						}
					}
					else{
						finish();
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
					 	jket_webView.setVisibility(View.VISIBLE);
					 	llNetworkClosed.setVisibility(View.INVISIBLE);
		            } else { //加载失败的话，初始化页面加载失败的图，然后替换正在加载的视图页面
		            	llNetworkLoad.setVisibility(View.INVISIBLE);//加载成功的话，则隐藏掉显示正在加载的视图，显示加载了网页内容的WebView
		            	jket_webView.setVisibility(View.INVISIBLE);
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
		if(bundle != null) {
			userId = bundle.getString("userId");
			token = bundle.getString("token");
			if(userId==null||userId==""||"".equals(userId)){
				userId="";
			}
			if(token==null||token==""||"".equals(token)){
				token="";
			}
			jket_webView.loadUrl(BocSdkConfig.jketurl + "?userId=" + userId + "&accessToken=" + token+"&mobiletype=android");
		}else {
			jket_webView.loadUrl(BocSdkConfig.jketurl + "?userId=" + userId + "&accessToken=" + token+"&mobiletype=android");
		}
		//设置webView需要请求的URL，其中，如果在app中已登入，请传入userId与accessToken
				
	}	
	
	@OnClick(R.id.btnNetworkClose)
	public void button(View v) {
		Log.i("tag", "reload");
		llNetworkLoad.setVisibility(View.VISIBLE);
		jket_webView.setVisibility(View.INVISIBLE);
		llNetworkClosed.setVisibility(View.INVISIBLE);
		loadError  = false;
		jket_webView.reload();
	}
	
	@OnClick(R.id.jket_back)
	public void back(View v){
		// 这部分是判断返回时是否返回接入的app，此处是根据title来判断
		if(jket_webView.canGoBack()){
			if(titles.get(titles.size() - 1).equals("健康通")||"健康通"==titles.get(titles.size() - 1)){
				jket_webView.loadUrl("javascript:selhos();");
				return;
			}else if(titles.get(titles.size() - 1).equals("选择医院")){
				jket_webView.loadUrl("javascript:selcity();");
				return;
			}else if(titles.get(titles.size() - 1).equals("选择城市")){
				finish();
			}
			jket_webView.goBack();
			if(titles.size()>0){
				titles.remove(titles.size() - 1);
				jket_titleName.setText(titles.get(titles.size() - 1));
			}
		}
		else{
			finish();
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	// 默认点回退键，会退出Activity，需监听按键操作，使回退在WebView内发生
		// 按物理返回键处理，这里与app中接入的返回键的逻辑一致
		if ((keyCode == KeyEvent.KEYCODE_BACK) && jket_webView.canGoBack()) {
			if(titles.get(titles.size() - 1).equals("健康通")||"健康通"==titles.get(titles.size() - 1)){
				jket_webView.loadUrl("javascript:selhos();");
				return true;
			}else if(titles.get(titles.size() - 1).equals("选择医院")){
				jket_webView.loadUrl("javascript:selcity();");
				return true;
			}else if(titles.get(titles.size() - 1).equals("选择城市")){
				finish();
			}
			jket_webView.goBack();
			if(titles.size()>0){
				titles.remove(titles.size() - 1);
				jket_titleName.setText(titles.get(titles.size() - 1));
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void getLoginViewResultCall(){


		runOnUiThread(new Runnable() {
			@Override
			public void run() {

				String userId = LoginUtil.getUserId(JKETActivity.this);

				if(!TextUtils.isEmpty(userId)){

					Map<String, Object> map = new HashMap<>();
					map.put("userId", LoginUtil.getUserId(JKETActivity.this));
					map.put("actoken", LoginUtil.getToken(JKETActivity.this));
					HybridUtil.getInstance().handleJsRequest(jket_webView, "getLoginViewResultCall", map,
							new HybridCallBack() {

								@Override
								public void errorMsg(Exception e) {
									// TODO Auto-generated method
									// stub

								}
							});

					return;
				}

				LoginUtil.authorize(JKETActivity.this, new LoginUtil.ILoginListener() {
					@Override
					public void onLogin() {
						Map<String, Object> map = new HashMap<>();
						map.put("userId", LoginUtil.getUserId(JKETActivity.this));
						map.put("actoken", LoginUtil.getToken(JKETActivity.this));
						HybridUtil.getInstance().handleJsRequest(jket_webView, "getLoginViewResultCall", map,
								new HybridCallBack() {

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