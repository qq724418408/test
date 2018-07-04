package com.bocop.jxplatform.activity.riders;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.config.BocSdkConfig;

@ContentView(R.layout.activity_htzq)
public class FQGCActivity extends BaseActivity{
	
	@ViewInject(R.id.htzq_titleName)
	private TextView htzq_titleName;
	@ViewInject(R.id.htzq_webView)
	private WebView htzq_webView;
	int height;
	String userId="";
	String token="";
	private List<String> titles = new ArrayList<String>();
	

	@SuppressWarnings("deprecation")
	@SuppressLint({ "JavascriptInterface", "SetJavaScriptEnabled" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final ProgressBar bar = (ProgressBar)findViewById(R.id.myProgressBar);
		htzq_webView.addJavascriptInterface(this, "wst");
		htzq_webView.getSettings().setSavePassword(false);
		WebSettings settings = htzq_webView.getSettings();
    	settings.setJavaScriptEnabled(true);//可以运行javaScript
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setDomStorageEnabled(true);
        settings.setSupportZoom(true);//是否支持缩放
        settings.setBuiltInZoomControls(true);//是否显示缩放按钮
        settings.setAppCacheEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setGeolocationEnabled(true);
        htzq_webView.setWebChromeClient(new WebChromeClient(){
			
			@Override
			public void onReceivedTitle(WebView view, String title) {
				// TODO Auto-generated method stub
				super.onReceivedTitle(view, title);
//				if(title.length() > 16)
//				{
//					htzq_titleName.setText(title.substring(0, 16));
//					titles.add(title.substring(0, 10));
//				}
//				else{
					htzq_titleName.setText(title);
					titles.add(title);
//				}
				
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
        htzq_webView.setWebViewClient(new WebViewClient(){
        	@Override
        	public boolean shouldOverrideUrlLoading(WebView view, String url) {

        	view.loadUrl(url); //在当前的webview中跳转到新的url

        	return true;
        	}
        	});
		
		htzq_webView.loadUrl("http://wx.wevein.com/app/index.php?i=3&c=entry&do=index&m=jy_yht");
		//设置webView需要请求的URL，其中，如果在app中已登入，请传入userId与accessToken
				
	}
	@JavascriptInterface
	public void startFunction(final int str){
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	//	jket_titleName.setText(titles.get(titles.size()- str));
	}
	
	@OnClick(R.id.htzq_back)
	public void back(View v){
		// 这部分是判断返回时是否返回接入的app，此处是根据title来判断
		if(htzq_webView.canGoBack()){
			
			//Toast.makeText(getApplicationContext(), "test2", 1000);
//			jket_webView.loadUrl("javascript:history.go(-1);");
			if(titles.get(titles.size() - 1).equals("外购通")){
//				Toast.makeText(this, titles.get(titles.size() - 1)+titles.get(titles.size() - 1).equals("健康通"), Toast.LENGTH_SHORT).show();
				htzq_webView.loadUrl("javascript:selhos();");
				return;
			}else if(titles.get(titles.size() - 1).equals("外购通")){
				finish();
			}
			htzq_webView.goBack();
			if(titles.size()>0){
				titles.remove(titles.size() - 1);
				htzq_titleName.setText(titles.get(titles.size() - 1));
			}
//			webview.goBack()
		}
		else{
			//jket_webView.loadUrl("javascript:setheight('"+height+"');");
			finish();
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	// 默认点回退键，会退出Activity，需监听按键操作，使回退在WebView内发生
		// 按物理返回键处理，这里与app中接入的返回键的逻辑一致
		if ((keyCode == KeyEvent.KEYCODE_BACK) && htzq_webView.canGoBack()) {
			finish();
//			if ("选择医院".equals(jket_titleName.getText().toString())) {
//				finish();
//			} else {
//				jket_webView.loadUrl("javascript:javascript:aaa();");
//			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
