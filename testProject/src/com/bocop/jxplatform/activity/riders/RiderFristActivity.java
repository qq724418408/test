package com.bocop.jxplatform.activity.riders;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.base.BaseApplication;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.common.util.ContentUtils;
import com.boc.jx.constants.Constants;
import com.bocop.gm.utils.HybridCallBack;
import com.bocop.gm.utils.HybridUtil;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.action.CHTAction;
import com.bocop.jxplatform.http.RestTemplateJxBank;
import com.bocop.jxplatform.trafficassistant.TrafficAssistantMainActivity;
import com.bocop.jxplatform.util.CustomProgressDialog;
import com.bocop.jxplatform.util.LoginUtil;
import com.bocop.jxplatform.util.LoginUtilAnother;
import com.bocop.jxplatform.util.LoginUtilAnother.ILoginListener;
import com.bocop.jxplatform.view.BackButton;
import com.bocsoft.ofa.http.asynchttpclient.JsonHttpResponseHandler;
import com.bocsoft.ofa.http.asynchttpclient.expand.JsonRequestParams;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * 车惠通首页
 * 
 * @author xmtang
 * 
 */
@ContentView(R.layout.activity_riderfrist)
public class RiderFristActivity extends BaseActivity implements ILoginListener {
	
	@ViewInject(R.id.iv_imageLeft)
	private BackButton ivImageLeft;
	@ViewInject(R.id.ivBack)
	private ImageView ivBack;
	@ViewInject(R.id.tv_titleName)
	private TextView tvTitle;
	@ViewInject(R.id.ivRefresh)
	private ImageView ivRefresh;
	@ViewInject(R.id.wbMain)
	private WebView wbMain;
	@ViewInject(R.id.loadingProgressBar)
	private ProgressBar loadingProgressBar;
	
	private Pattern pattern;
	private CHTAction action;
	
	public static final int LOGIN_RESPONSE_WZCL = 0;//违章处理
	public static final int LOGIN_RESPONSE_THXC = 1;//特惠洗车
	public static final int LOGIN_RESPONSE_JYCZ = 2;//加油充值
	public static final int LOGIN_RESPONSE_SXC = 3;//新手学车
	public static final int LOGIN_RESPONSE_GRXX = 4;//个人信息
	public static final int LOGIN_RESPONSE_WDCZ = 5;//我的车子

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		initData();
		setWebViewSetting(wbMain);
		initWebView();
		loadURL();
	}
	
	private void initData(){
		action = new CHTAction(this);
		ivImageLeft.setVisibility(View.GONE);
		ivBack.setVisibility(View.VISIBLE);
		ivBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	private void loadURL() {
		Bundle bundle = getIntent().getExtras();
		String url = Constants.chtUrlForCht;
		if (bundle != null) {
			url = bundle.getString("url");
		} else {
			ivRefresh.setVisibility(View.VISIBLE);
			ivRefresh.setImageResource(R.drawable.cht_img_personal_info);
			ivRefresh.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					skipControl(LOGIN_RESPONSE_GRXX);
				}
			});
		}
		wbMain.loadUrl(url);
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	public static WebSettings setWebViewSetting(WebView webView) {
		if (webView == null) {
			Log.i("TAG", "webView cann't null");
			return null;
		}
		WebSettings settings = webView.getSettings();
		settings.setJavaScriptEnabled(true);// 可以运行javaScript

		settings.setUseWideViewPort(false);// 设置自适应屏幕大小
		settings.setLoadWithOverviewMode(false);
		settings.setAppCacheEnabled(false); // 退出清除缓存

		settings.setDomStorageEnabled(true);
		settings.setSupportZoom(false);// 是否支持缩放
		settings.setBuiltInZoomControls(false);// 是否显示缩放按钮

		settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		return settings;
	}
	
	private void initWebView(){
		wbMain.clearCache(true);
		wbMain.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				if (TextUtils.isEmpty(url)) {
					return false;
				}
				if (pattern == null) {
					pattern = Pattern.compile("apps.callbackapps", Pattern.CASE_INSENSITIVE);
				}
				Matcher matcher = pattern.matcher(url);
				if (matcher != null && matcher.find()) {
					// TODO
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
					// TODO
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
		wbMain.setWebChromeClient(new WebChromeClient() {
			// 用于获取title
			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
				Log.i("tag", title);
				tvTitle.setText(title);
			}
		});
	}
	
	/**
	 * 跳转控制
	 * @param position
	 */
	public void skipControl(final int position) {
		BaseApplication application = (BaseApplication) getApplication();
		if (application.isNetStat()) {
			if (LoginUtil.isLog(this)) {
				switch(position) {
					//违章处理
					case LOGIN_RESPONSE_WZCL:
						Intent intent = new Intent(this, TrafficAssistantMainActivity.class);
						startActivity(intent);
						break;
					//特惠洗车  
					case LOGIN_RESPONSE_THXC:
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								postUserInfo(position);
							}
						});
						break;
					//加油充值
					case LOGIN_RESPONSE_JYCZ:
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								postUserInfo(position);
							}
						});
						break;
					//新手学车
					case LOGIN_RESPONSE_SXC:
						Bundle bundleSxc = new Bundle();
						bundleSxc.putString("url", Constants.chtUrlForSxc);
						Intent intentSxc = new Intent(this, RiderFristActivity.class);
						intentSxc.putExtras(bundleSxc);
						startActivity(intentSxc);
						break;
					//个人信息
					case LOGIN_RESPONSE_GRXX:
						Bundle bundleGrxx = new Bundle();
						bundleGrxx.putString("url", Constants.chtUrlForGrxx);
						Intent intentGrxx = new Intent(this, RiderFristActivity.class);
						intentGrxx.putExtras(bundleGrxx);
						startActivity(intentGrxx);
						break;
					//我的车子
					case LOGIN_RESPONSE_WDCZ:
						Bundle bundleWdcz = new Bundle();
						bundleWdcz.putString("url", Constants.chtUrlForWdcz);
						Intent intentWdcz = new Intent(this, RiderFristActivity.class);
						intentWdcz.putExtras(bundleWdcz);
						startActivity(intentWdcz);
						break;
				}
			} else {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						LoginUtilAnother.authorizeAnother(RiderFristActivity.this, 
								RiderFristActivity.this, position);
					}
				});
			}
		}  else {
			CustomProgressDialog.showBocNetworkSetDialog(this);
		}
	}
	
	// 实现登陆的接口
	@Override
	public void onLogin(int position) {
		skipControl(position);
	}

	@Override
	public void onLogin() {

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
	 * 获取用户信息，主要是获取手机号为洗车服务做准备
	 */
	public void postUserInfo(final int i) {
		RestTemplateJxBank restTemplate = new RestTemplateJxBank(this);
		JsonRequestParams params = new JsonRequestParams();
		String userid = LoginUtil.getUserId(this);
		params.put("userid", userid);
		restTemplate.post("https://openapi.boc.cn/app/useridquery",
				params, new JsonHttpResponseHandler("UTF-8") {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						try {
							String idnoString = response.getString("idno");
							String mobileNo = response.getString("mobileno");
							String username = response.getString("cusname");
							ContentUtils.putSharePre(RiderFristActivity.this,
									Constants.SHARED_PREFERENCE_NAME,
									Constants.ID_NO, idnoString);
							ContentUtils.putSharePre(RiderFristActivity.this,
									Constants.SHARED_PREFERENCE_NAME,
									Constants.MOBILENO, mobileNo);
							ContentUtils.putSharePre(RiderFristActivity.this,
									Constants.SHARED_PREFERENCE_NAME,
									Constants.USER_NAME, username);
							skipCarOrRecharge(i);
						} catch (Exception e) {
							e.printStackTrace();
							//获取手机号出现异常 也让他跳转
							skipCarOrRecharge(i);
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						skipCarOrRecharge(i);
					}
				});
	}
	
	/**
	 * 跳转特惠洗车或加油充值
	 * @param position
	 */
	private void skipCarOrRecharge(int position) {
		if (position == LOGIN_RESPONSE_THXC) {
			Intent intentwashCar = new Intent(this, CarWashServiceActivity.class);
			startActivity(intentwashCar);
		} else if (position == LOGIN_RESPONSE_JYCZ) {
			Intent intentAddOil = new Intent(this, AddGasOilServiceActivity.class);
			startActivity(intentAddOil);
		}
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
				map.put("userId", LoginUtil.getUserId(RiderFristActivity.this));
				map.put("actoken", LoginUtil.getToken(RiderFristActivity.this)); 
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

	/**
	 * 设置横竖屏
	 * 
	 * @param param
	 *            0竖屏 1横屏
	 */
	public void screenDirection(String param) {
		if ("0".equals(param)) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		} else if ("1".equals(param)) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
	}
	
	/**
	 * 分享
	 * @param param
	 */
	public void doShare(String param) {
		try {
			JSONObject object = new JSONObject(param);
			String title = object.getString("title");
			String content = object.getString("content");
			String url = object.getString("url");
			Log.i("tag", "title:" + title);
			Log.i("tag", "content:" + content);
			Log.i("tag", "url:" + url);
			ShareSDK.initSDK(this);
			OnekeyShare oks = new OnekeyShare();
			//关闭sso授权
			oks.disableSSOWhenAuthorize();

			oks.setTitle(title);
			oks.setText(content);

			oks.setImagePath(getResources().getResourcePackageName(R.drawable.bocjx));
			// url仅在微信（包括好友和朋友圈）中使用
			oks.setImageUrl(url);
			oks.setUrl(url);
			 
			// 启动分享GUI
			oks.show(this);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
}
