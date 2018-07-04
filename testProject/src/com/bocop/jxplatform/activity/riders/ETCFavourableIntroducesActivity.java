package com.bocop.jxplatform.activity.riders;

import java.net.URLEncoder;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.boc.jx.baseUtil.asynchttpclient.JsonHttpResponseHandler;
import com.boc.jx.baseUtil.asynchttpclient.RequestParams;
import com.boc.jx.common.util.AesUtils;
import com.boc.jx.common.util.MD5Utils;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.http.RestTemplate;

/**
 * ETC优惠政策介绍
 * 
 * @author xmtang
 * 
 */
public class ETCFavourableIntroducesActivity extends Activity {
	private TextView tv_titleName;
	private ImageView iv_title_left;
	private Button btn_sq;
	ProgressDialog progressDialog;
	private WebView wv_common;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favintroduce);
		initView();
		setListener();
		getFavourableIntroduceDetail();
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		tv_titleName = (TextView) findViewById(R.id.tv_titleName);
		iv_title_left = (ImageView) findViewById(R.id.iv_title_left);
		btn_sq = (Button) findViewById(R.id.tv_sq);
		tv_titleName.setText("ETC产品介绍");
		initWebView();   
	}

	/**
	 * 初始化webview
	 */
	private void initWebView() {
		wv_common = (WebView) findViewById(R.id.wv_common);
		WebSettings ws = wv_common.getSettings();
		ws.setJavaScriptEnabled(true);
		ws.setBuiltInZoomControls(false);
		ws.setUseWideViewPort(false);
		ws.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
		ws.setLoadWithOverviewMode(true);
		ws.setSupportZoom(false);
		ws.setDomStorageEnabled(true);
		// webView.addJavascriptInterface(this, "nativeApp");
		wv_common.setWebViewClient(new WebViewClient() {
			@Override
			public void onLoadResource(WebView view, String url) {
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return false;
			}
		});
		wv_common.setWebChromeClient(new WebChromeClient() {
			// 用于获取title
			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
				// Tv_Title.setText(title);
			}

			// 设置网页加载的进度条
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
			}

			// 处理javascript中的alert
			@Override
			public boolean onJsAlert(WebView view, String url, String message,
					JsResult result) {
				return super.onJsAlert(view, url, message, result);
			}
		});

	}

	/**
	 * 设置监听
	 */
	private void setListener() {
		iv_title_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		btn_sq.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(
						ETCFavourableIntroducesActivity.this,
						MapListActivity.class);
//				Intent intent = new Intent(
//						ETCFavourableIntroducesActivity.this,
//						ETCApplyActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
			}
		});
	}

	/**
	 * 获取优惠政策详情
	 */
	private void getFavourableIntroduceDetail() {
		RestTemplate restTemplate = new RestTemplate(
				ETCFavourableIntroducesActivity.this);
		RequestParams params = new RequestParams();
		// params.put(key, file); http://22.220.13.64:8080
		restTemplate.get(
				"http://123.124.191.179/etc/notice/findListByTime",
				params, new JsonHttpResponseHandler("UTF-8") {
					@Override
					public void onStart() {
//						progressDialog = new ProgressDialog(
//								ETCFavourableIntroducesActivity.this);
//						progressDialog.setMessage("正在加载数据");
//						progressDialog.show();
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
//						progressDialog.dismiss();
						try {
							String content = null;
							int status = Integer.parseInt(response
									.getString("errorCode"));
							switch (status) {
							case 10000:
								content = response.getJSONObject("rows")
										.getString("noticeContent");
								break;

							default:
								break;
							}
							System.out.println("政策数据===="
									+ response.getJSONObject("rows").getString(
											"noticeContent"));
							wv_common.loadDataWithBaseURL(null, content,
									"text/html", "utf-8", null);
						} catch (NumberFormatException e) {
							e.printStackTrace();
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}
				});

	}
}
