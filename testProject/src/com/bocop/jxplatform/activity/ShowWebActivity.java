package com.bocop.jxplatform.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.bocop.jxplatform.view.BackButton;
import com.bocop.jxplatform.R;

@ContentView(R.layout.activity_show_web)
public class ShowWebActivity extends BaseActivity {

	@ViewInject(R.id.tv_titleName)
	private TextView tv_titleName;
	@ViewInject(R.id.iv_imageLeft)
	private BackButton backBtn;
	@ViewInject(R.id.webView)
	private WebView webView;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getIntent().getExtras();
		String loadUrl = bundle.getString("url");
		String title = bundle.getString("title");
		tv_titleName.setText(title);
		
		WebSettings settings = webView.getSettings();
		settings.setSavePassword(false);
    	settings.setJavaScriptEnabled(true);//可以运行javaScript
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setDomStorageEnabled(true);
        settings.setSupportZoom(false);//是否支持缩放
        settings.setBuiltInZoomControls(false);//是否显示缩放按钮
        settings.setAppCacheEnabled(true);
        settings.setGeolocationEnabled(true);
        webView.setInitialScale(100);
        webView.getSettings().setSavePassword(false);
        webView.loadUrl(loadUrl);
        
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
								
								Intent intent = new Intent(ShowWebActivity.this, ShowWebActivity.class);
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
