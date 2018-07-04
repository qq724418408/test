package com.bocop.jxplatform.fragment;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.boc.jx.base.BaseFragment;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.bocop.jxplatform.activity.ShowWebActivity;
import com.bocop.jxplatform.R;

public class ActivityFragment extends BaseFragment {
	
	@ViewInject(R.id.webView)
	private WebView webView;
	
	private boolean isFirst = true;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = initView(R.layout.fragment_activity);
    	return view;
	}
	
	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		super.initData();
		init();
//		webView.loadUrl("http://formss.eicp.net:18888/STO/index.html");
//		webView.loadUrl("file:///android_asset/html/index.html");
	}
	
	@Override
	public void onSelected() {
		if(isFirst){
			System.out.println("onSelected.......activity");
//			webView.loadUrl("http://formss.eicp.net:18888/STO/index.html");
			isFirst = false;
		}
		
	}
	
	private void init() {
		System.out.println("ActivityFragment......init");
		WebSettings settings = webView.getSettings();
		settings.setJavaScriptEnabled(true);// 可以运行javaScript
		settings.setUseWideViewPort(true);
		settings.setLoadWithOverviewMode(true);
		settings.setDomStorageEnabled(true);
		settings.setSupportZoom(false);// 是否支持缩放
		settings.setBuiltInZoomControls(false);// 是否显示缩放按钮
		settings.setAppCacheEnabled(true);
		settings.setGeolocationEnabled(true);
		webView.setInitialScale(100);
        
		webView.setWebChromeClient(new WebChromeClient());
		
        webView.setWebViewClient(new WebViewClient() {
        	@Override
        	public boolean shouldOverrideUrlLoading(WebView view, String url) {
        		Pattern pattern = Pattern.compile("APPS.CALLBACKAPPS", Pattern.CASE_INSENSITIVE);
        		Matcher matcher = pattern.matcher(url);
                if (matcher.find()) {
                	Uri uri = Uri.parse(url);
                    String actionName = uri.getQueryParameter("actionName");
                    String param = uri.getQueryParameter("param");
                    if("openNewPage".equals(actionName)) {
                    	if(param != null && !"".equals(param)){
                    		try {
								JSONObject jsonObject = new JSONObject(URLDecoder.decode(param, "utf-8"));
								String loadUrl = jsonObject.getString("url");
								String title = jsonObject.getString("title");
								
								Bundle bundle = new Bundle();
								bundle.putString("url", loadUrl);
								bundle.putString("title", title);
								
								Intent intent = new Intent(getBaseActivity(), ShowWebActivity.class);
								intent.putExtras(bundle);
								startActivity(intent);
								
							} catch (UnsupportedEncodingException e) {
								e.printStackTrace();
							} catch (JSONException e) {
								e.printStackTrace();
							}
                    	}
                    }
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
        	}
        });
        
	}

}
