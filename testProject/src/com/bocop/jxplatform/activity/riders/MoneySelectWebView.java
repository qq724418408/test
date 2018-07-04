package com.bocop.jxplatform.activity.riders;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bocop.jxplatform.R;
import com.bocop.jxplatform.http.RestTemplateJxBank;
import com.bocop.jxplatform.util.LoginUtil;
import com.bocsoft.ofa.http.asynchttpclient.JsonHttpResponseHandler;
import com.bocsoft.ofa.http.asynchttpclient.expand.JsonRequestParams;

/**
 * 易商理财
 * 
 * @author xmtang
 * 
 */
public class MoneySelectWebView extends Activity {
	private WebView webView;
	private TextView tv_titleName;
	private ImageView iv_title_left;
	private ArrayList<String> indexs = new ArrayList<String>();
	private int pointLogin = 1;
	private String currentUrl = "";// 当前页面地址
	private String url = "";
	// private String url =
	// "https:/openapi.boc.cn/ezdb/mobileHtml/html/userCenter/index.html?channel=android";
	private boolean isNative = false;// 是否是从本地入口进入
	private String channel = "?channel=android";
	private String access_token = "";
	private String refresh_token = "";
	String userid;
	// private TextView iv_close;
	// private TextView tv_title;
	private LinearLayout llTitle;
	ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_finances);
		initView();
		getFinances(this);
		// initWebView();
		setListener();
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		// url = getIntent().getStringExtra("url");
		// access_token = getIntent().getStringExtra("access_token");
		// refresh_token = getIntent().getStringExtra("refresh_token");
		// userid = getIntent().getStringExtra("user_id");
		tv_titleName = (TextView) findViewById(R.id.tv_titleName);
		tv_titleName.setText("易商理财");
		iv_title_left = (ImageView) findViewById(R.id.iv_title_left);
		iv_title_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}

	/**
	 * 初始化webView
	 */
	@SuppressLint({ "JavascriptInterface", "NewApi" })
	void initWebView() {
		indexs.add("https://openapi.boc.cn/ezdb/mobileHtml/html/userCenter/card_list.html?channel=android");// 我的银行卡
		indexs.add("https://openapi.boc.cn/ezdb/mobileHtml/html/login/editPwd.html?channel=android");// 密码管理
		indexs.add("https://openapi.boc.cn/ezdb/mobileHtml/html/product/productList.html?channel=android");// 我要理财
		indexs.add("https://openapi.boc.cn/ezdb/mobileHtml/html/product/fund/myFundsList.html?channel=android");// 我的资产
		indexs.add("https://openapi.boc.cn/ezdb/mobileHtml/html/product/acctDetail/tradingList.html?channel=android");// 交易明细
		indexs.add("https://openapi.boc.cn/ezdb/mobileHtml/html/product/redeemFailList.html?channel=android");// 赎回异常处理
		indexs.add("https://openapi.boc.cn/ezdb/mobileHtml/html/message/messageList.html");// 消息公告
		indexs.add("https://openapi.boc.cn/ezdb/mobileHtml/html/help/index.html");// 帮助中心
		webView = (WebView) findViewById(R.id.webView);
		if (!TextUtils.isEmpty(url)
				&& ("https://openapi.boc.cn/ezdb/mobileHtml/html/message/messageList.html"
						.equals(url) || "https://openapi.boc.cn/ezdb/mobileHtml/html/help/index.html"
						.equals(url))) {// ����Ϣ����Ͱ������Ľ�ȥ֮��Ҫ��������ʶ
			url += channel;
			pointLogin = 0;
		} else {
			pointLogin = 1;
		}
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);// 用于JS回调
		webSettings.setSaveFormData(true);
		webSettings.setRenderPriority(RenderPriority.HIGH);// 提高渲染的优先级
		webSettings.setDatabaseEnabled(true);
		webSettings.setDomStorageEnabled(true);
		String dbPath = this.getApplicationContext()
				.getDir("database", MoneySelectWebView.MODE_PRIVATE).getPath();// 此
		// 抽象路径名的字符串形式
		webSettings.setDatabasePath(dbPath);
		webSettings.setAllowFileAccess(true);
		webSettings.setGeolocationEnabled(true);
		// setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		webSettings.setDefaultTextEncodingName("UTF-8");
		// setBackgroundColor(getResources().getColor(R.color.white));
		webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
		if (Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN)
			webSettings.setAllowUniversalAccessFromFileURLs(true);
		webView.addJavascriptInterface(this, "app_ezdb");
		currentUrl = url;
		webView.loadUrl(url);
		if ("https:/openapi.boc.cn/ezdb/mobileHtml/html/userCenter/index.html?channel=android"
				.equals(url)) {// H5列表首页
			isNative = false;
		} else {
			isNative = true;
		}
		webView.setWebViewClient(new MyWebViewClient());
		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public boolean onJsAlert(WebView view, String url, String message,
					JsResult result) {
				return super.onJsAlert(view, url, message, result);
			}

			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
			}
		});
		// Tv_Close.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View arg0) {
		// finish();
		// }
		// });
	}

	@JavascriptInterface
	public String getAccessToken() {
		System.out.println("nihao00000------" + access_token);
		return access_token;
	}

	/**
	 * @return 0为首页，1为非首页
	 */
	@JavascriptInterface
	public String isIndex() {
		if (url.equals(currentUrl)) {// 入口页面相当于首页
			return "0";// 首页
		} else {
			return "1";
		}
	}

	/**
	 * @return refresh_token
	 */
	@JavascriptInterface
	public String getRefreshToken() {
		System.out.println("nihao00000111111------" + refresh_token);
		return refresh_token;
	}

	/**
	 * 返回已登录用户名
	 */
	@JavascriptInterface
	public String getUserId() {
		System.out.println("nihaoliuzong ------" + userid);
		return userid;
	}

	/**
	 * @return 0为不需要title，1为需要
	 */
	@JavascriptInterface
	public int getHeader() {
		return 0;
	}

	/**
	 * 0为不需要登录，1为需要登录 目前消息公告和帮助中心不需要登录
	 * 
	 * @return
	 */
	@JavascriptInterface
	public int isPointLogin() {
		return pointLogin;
	}

	/**
	 * 用WebView点链接看了很多页以后为了让WebView支持回退功能，
	 * 需要覆盖覆盖Activity类的onKeyDown()方法，如果不做任何处理，点击系统回退剪键，
	 * 整个浏览器会调用finish()而结束自身，而不是回退到上一页面
	 */
	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	//
	// if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
	// webView.goBack(); // goBack()表示返回WebView的上一页面
	// return true;
	// }
	// if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	// finish();
	// return true;
	// }
	// return false;
	// }
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode) {
			if (webView.canGoBack()) {
				if (!TextUtils.isEmpty(webView.getUrl())
						&& webView.getUrl().contains("index.html")) {
					this.finish();
					return true;
				} else if (!TextUtils.isEmpty(webView.getUrl())
						&& webView.getUrl().contains("login.html")) {
					return super.onKeyDown(keyCode, event);
				} else if (isNative
						&& indexs.contains(webView.getUrl())
						|| indexs.contains(webView.getUrl()
								+ "?channel=android")) {
					return super.onKeyDown(keyCode, event);
				}
				webView.goBack();// ����webview�ϼ�ҳ��
				currentUrl = webView.getUrl();
				return true;
			} else {
				return super.onKeyDown(keyCode, event);
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	private class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			webView.addJavascriptInterface(MoneySelectWebView.this, "app_ezdb");// ������һ��Ҫ�Ǳ�activityȫ�֣�����ҳ����ת�����߻ص�����
			// 作用域一定要是本activity全局，否则页面跳转不会走回调方法
			if (!TextUtils.isEmpty(url)
					&& ("https://openapi.boc.cn/ezdb/mobileHtml/html/message/messageList.html"//
					// 消息公告
					.equals(url) || "https://openapi.boc.cn/ezdb/mobileHtml/html/help/index.html"//
					// 帮助中心
					.equals(url))) {// 从我们的渠道进去之后要加渠道标识
				url += channel;
				pointLogin = 0;
			} else {
				pointLogin = 1;
			}
			if (!TextUtils.isEmpty(url) && url.contains("?link=productList")) {// 去掉产品列表标识
				url = url.replace("?link=productList", "");
			}
			currentUrl = url;
			webView.loadUrl(url);
			return true;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			// pb.setVisibility(View.GONE);
			super.onPageFinished(view, url);
		}
	}

	private void setListener() {
		iv_title_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!webView.canGoBack()) {
					finish();
				} else {
					webView.goBack();
				}
			}
		});
	}

	private void getFinances(final Context context) {
		RestTemplateJxBank restTemplate = new RestTemplateJxBank(context);
		JsonRequestParams params = new JsonRequestParams();
		String action = LoginUtil.getToken(context);
		String useid = LoginUtil.getUserId(context);
		params.put("enctyp", "");
		params.put("password", "");
		params.put("grant_type", "implicit");
		params.put("user_id", useid);
		// params.put("client_secret", MainActivity.CONSUMER_SECRET);
		params.put("client_id", "357");
		params.put("acton", action);
		// https://openapi.boc.cn/bocop/oauth/token
		restTemplate.postGetType("https://openapi.boc.cn/oauth/token", params,
				new JsonHttpResponseHandler("UTF-8") {
					@Override
					public void onStart() {
						super.onStart();
						progressDialog = new ProgressDialog(context);
						progressDialog.setMessage("正在努力加载中...");
						progressDialog.setCanceledOnTouchOutside(false);
						progressDialog.show();
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						System.out.println("nihao======" + response.toString());
						progressDialog.dismiss();
						try {
							access_token = response.getString("access_token");
							refresh_token = response.getString("refresh_token");
							userid = response.getString("user_id");
							url = "https:/openapi.boc.cn/ezdb/mobileHtml/html/userCenter/index.html?channel=android";
							initWebView();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						if (progressDialog != null) {
							progressDialog.dismiss();
						}
					}
				});
	}
}
