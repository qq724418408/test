package com.bocop.jxplatform.activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.baseUtil.cache.CacheBean;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.config.BocSdkConfig;
import com.bocop.jxplatform.util.LoginUtil;
import com.bocop.jxplatform.util.LoginUtilAnother;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

@ContentView(R.layout.activity_exchange)
public class DZPActivity extends BaseActivity {

	@ViewInject(R.id.tv_titleName)
	private TextView tv_titleName;
	@ViewInject(R.id.webView)
	private WebView wbMain;

	@ViewInject(R.id.ll_network_closed)
	private LinearLayout llNetworkClosed;
	@ViewInject(R.id.ll_network_load)
	private LinearLayout llNetworkLoad;
	@ViewInject(R.id.exchangeProgressBar)
	private ProgressBar bar;

	private Pattern pattern;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initView();
		initWebView();
	};

	private void initView() {
		bar.setVisibility(View.GONE);
		llNetworkLoad.setVisibility(View.INVISIBLE);
		wbMain.setVisibility(View.VISIBLE);
	 	llNetworkClosed.setVisibility(View.INVISIBLE);
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initWebView() {
		WebSettings settings = wbMain.getSettings();
		settings.setJavaScriptEnabled(true);// 可以运行javaScript

		settings.setUseWideViewPort(false);// 设置自适应屏幕大小
		settings.setLoadWithOverviewMode(false);
		settings.setAppCacheEnabled(false); // 退出清除缓存

		settings.setDomStorageEnabled(true);
		settings.setSupportZoom(false);// 是否支持缩放
		settings.setBuiltInZoomControls(false);// 是否显示缩放按钮

//		settings.setCacheMode(WebSettings.LOAD_NO_CACHE);

//		wbMain.clearCache(true);
		wbMain.setWebViewClient(new WebViewClient() {
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
					doShare();
					return true;
				}
				return super.shouldOverrideUrlLoading(view, url);
			}

			@SuppressWarnings("deprecation")
			@Override
			public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
				if (pattern == null) {
					pattern = Pattern.compile("apps.callbackapps", Pattern.CASE_INSENSITIVE);
				}
				Matcher matcher = pattern.matcher(url);
				if (matcher != null && matcher.find()) {
					doShare();
				}
				return super.shouldInterceptRequest(view, url);
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);

			}

			@Override
			public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
				handler.proceed();
			}
		});

		wbMain.setWebChromeClient(new WebChromeClient() {

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
			}

			// 用于获取title
			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
				Log.i("tag", title);
				tv_titleName.setText(title);
			}

		});
		SharedPreferences sp = getSharedPreferences(LoginUtilAnother.SP_NAME, Context.MODE_PRIVATE);
		String dzpUserId = sp.getString(CacheBean.DZP_USER_ID, "");
		if (!TextUtils.isEmpty(dzpUserId)) {
			String url = BocSdkConfig.DZP_URL + "?ud=" + dzpUserId;
			Log.i("tag", "DZP_URL-->" + url);
			wbMain.loadUrl(url);
		}else{
			Log.i("tag", "DZP_URL-->");
		}
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
	
	@OnClick(R.id.iv_back)
	public void back(View v) {
		if(wbMain.canGoBack()){
			wbMain.goBack();
		}
		else{
			finish();
		}
	}

	/**
	 * 按键事件
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		super.onKeyDown(keyCode, event);
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			finish();
			break;
		case KeyEvent.KEYCODE_MENU:
			break;
		}
		return false;
	}

	@SuppressLint("SdCardPath")
	public void doShare() {
		String userId = "";
		String url = "http://open.boc.cn/appstore/#/app/appDetail/38201";
		if(LoginUtil.isLog(this)){
			userId = LoginUtil.getUserId(this);
			url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxd5944788c1a9ce15&redirect_uri=http://jxboc.uni-infinity.com/yhtsharecount/servlet/DealRestResourceForWeixin/" + userId + "/&response_type=code&scope=snsapi_userinfo&state=a123#wechat_redirect";
		}
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		//关闭sso授权
		oks.disableSSOWhenAuthorize();
		 
		// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
//		oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle("易惠通");
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
//		oks.setTitleUrl("http://www.boc.cn");
		// text是分享文本，所有平台都需要这个字段
		oks.setText("易惠通,百事通");
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//		oks.setImagePath("/sdcard/bocjx.png");//确保SDcard下面存在此张图片
		oks.setImagePath(getResources().getResourcePackageName(R.drawable.bocjx));
		// url仅在微信（包括好友和朋友圈）中使用
		
		oks.setImageUrl(url);
		oks.setUrl(url);
		
//		oks.setImageUrl("http://open.boc.cn/appstore/#/app/appDetail/38201");
//		oks.setUrl("http://open.boc.cn/appstore/#/app/appDetail/38201");
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
//		oks.setComment("我是测试评论文本");
		// site是分享此内容的网站名称，仅在QQ空间使用
//		oks.setSite(getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
//		oks.setSiteUrl("http://sharesdk.cn");
		 
		// 启动分享GUI
		Log.i("tag", url);
		oks.show(this);
		Log.i("tag", "show");
	}
}
