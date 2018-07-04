package com.bocop.jxplatform.trafficassistant;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.config.BocSdkConfig;
import com.bocop.jxplatform.util.LoginUtil;
import com.bocop.jxplatform.view.BackButton;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 绑定卡界面
 * 
 * @author luoyang
 * 
 */
@ContentView(R.layout.activity_webview)
public class BocOpWebActivity extends BaseActivity {

	@ViewInject(R.id.ivLine)
	private ImageView ivLine;
	@ViewInject(R.id.pbLoad)
	private ProgressBar pbLoad;
	@ViewInject(R.id.wvMain)
	private WebView wvMain;


	@ViewInject(R.id.tv_titleName)
	private TextView tv_titleName;
	String type;
	String title;

	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i("tag", "onCreate");
		super.onCreate(savedInstanceState);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			type = bundle.getString("type");
			title = bundle.getString("title");
		}
		Log.i("tag", "bundle");

		tv_titleName.setText(title);
		mContext = this;
		Log.i("tag", "initData");
		Log.i("tag", "initData");
		initData();
	}
	
	@OnClick(R.id.iv_title_left)
	public void back(View v) {
		if(wvMain.canGoBack()){
			wvMain.goBack();
		}
		else{
			finish();
		}
	}

	private void initData() {
		Log.i("tag", "pbLoad");
		pbLoad.setMax(100);
		Log.i("tag", "pbLoad1");
		WebSettings settings = wvMain.getSettings();
		settings.setJavaScriptEnabled(true);// 可以运行javaScript
		settings.setUseWideViewPort(true);
		settings.setLoadWithOverviewMode(true);
		settings.setDomStorageEnabled(true);
		settings.setSupportZoom(false);// 是否支持缩放
		settings.setBuiltInZoomControls(true);// 是否显示缩放按钮
		settings.setAppCacheEnabled(true);
		settings.setGeolocationEnabled(true);
		Log.i("tag", "bindCard");
		if (type.equals("bindCard")) {
			wvMain.loadUrl(BocSdkConfig.HTTP_URL
					+ "/cardmange.php?act=viewAddCard&clientid="
					+ BocSdkConfig.CONSUMER_KEY + "&accesstoken="
					+ LoginUtil.getToken(this) + "&userid="
					+ LoginUtil.getUserId(this) + "&themeid=1&devicetype=1");
		} else if (type.equals("yhkgl")) {
			wvMain.loadUrl(BocSdkConfig.HTTP_URL + "/cardmange.php?clientid="
					+ BocSdkConfig.CONSUMER_KEY + "&accesstoken="
					+ LoginUtil.getToken(this) + "&userid="
					+ LoginUtil.getUserId(this) + "&cardForm=allCard&themeid=1&devicetype=1");
		} else if (type.equals("mmgl")) {
			wvMain.loadUrl(BocSdkConfig.HTTP_URL + "/pwdedit.php?clientid="
					+ BocSdkConfig.CONSUMER_KEY + "&accesstoken="
					+ LoginUtil.getToken(this) + "&userid="
					+ LoginUtil.getUserId(this) + "&themeid=1&devicetype=1");
		}else if (type.equals("userregister")) {
			Log.i("tag", BocSdkConfig.HTTP_URL + "/register.php?clientid="
					+ BocSdkConfig.CONSUMER_KEY + "&themeid=1&devicetype=1");
			wvMain.loadUrl(BocSdkConfig.HTTP_URL + "/register.php?clientid="
					+ BocSdkConfig.CONSUMER_KEY + "&themeid=1&devicetype=2");
		}
		wvMain.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				Log.i("tag", "shouldOverrideUrlLoading");
				wvMain.loadUrl(url);
				pbLoad.setVisibility(View.VISIBLE);// 再次请求网页，显示进度条
				ivLine.setVisibility(View.GONE);
				Log.i("tag", "shouldOverrideUrlLoading2");
				return true;
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				// 打开错误，隐藏进度条，并置零
				pbLoad.setVisibility(View.GONE);
				ivLine.setVisibility(View.VISIBLE);
				pbLoad.setProgress(0);
				Toast.makeText(mContext, "网络繁忙，请稍候重试", Toast.LENGTH_LONG)
						.show();
			}
		});

		wvMain.setWebChromeClient(new WebChromeClient() {

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				Log.i("tag", "setWebChromeClient");
				pbLoad.setProgress(newProgress);
				if (100 == newProgress) {// 加载完成页面
					pbLoad.setVisibility(View.GONE);
					ivLine.setVisibility(View.VISIBLE);
					pbLoad.setProgress(0);
					Log.i("tag", "setWebChromeClient2");
				}
			}

		});
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {// 默认点回退键，会退出Activity，需监听按键操作，使回退在WebView内发生
		if ((keyCode == KeyEvent.KEYCODE_BACK) && wvMain.canGoBack()) {
			wvMain.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
