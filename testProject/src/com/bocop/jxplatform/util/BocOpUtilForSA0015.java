package com.bocop.jxplatform.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.base.BaseApplication;
import com.boc.jx.baseUtil.asynchttpclient.AsyncHttpClient;
import com.boc.jx.baseUtil.asynchttpclient.AsyncHttpResponseHandler;
import com.boc.jx.baseUtil.asynchttpclient.RequestHandle;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.config.BocSdkConfig;
import com.bocop.jxplatform.util.LoginUtil.ILoginListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

/**
 * @author luoyang
 * @version 创建时间：2015-6-23 下午5:46:43 分行CSP通用接口类，需要验证 修改用户授权信息（刷新令牌）
 */

@SuppressLint("SimpleDateFormat")
public class BocOpUtilForSA0015 implements ILoginListener {

	String msgcode;
	String rtnmsg;

	/**
	 * 用户信息
	 */
	public BaseApplication baseApplication = BaseApplication.getInstance();

	/*
	 * http报文头信息
	 */
	public Context mContext;

	public BocOpUtilForSA0015(Context context) {
		this.mContext = context;
	}

	@SuppressLint("SimpleDateFormat")
	public Header[] getHeaders() {
		Header clientid = new BasicHeader("clentid", BocSdkConfig.CONSUMER_KEY);
		Header userid = new BasicHeader("userid", LoginUtil.getUserId(mContext));
		Header action = new BasicHeader("acton", LoginUtil.getToken(mContext));
		Header type = new BasicHeader("type", "1");
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMDD");
		// 获取当前时间
		String nowData = format.format(new Date(System.currentTimeMillis()));
		Header trandt = new BasicHeader("trandt", nowData);

		SimpleDateFormat formatTime = new SimpleDateFormat("HHmmss");
		// 获取当前时间
		String nowTime = formatTime
				.format(new Date(System.currentTimeMillis()));

		Header trantm = new BasicHeader("trantm", nowTime);
		return new Header[] { clientid, type, userid, action, trandt, trantm };
	}

	/**
	 * 
	 * @param context
	 *            :上下文
	 * @param url
	 *            ：中银开放平台CSP通用接口地址(需要验证)
	 * @param mcis
	 *            ：开放平台报文
	 * @param myCallback
	 *            ：回调
	 */

	public void postOpboc(String strGson, String strUrl, CallBackBoc3 callBack) {

		if (baseApplication.isNetStat()) {
//			post(BocSdkConfig.CONSUMER_URL + ":" + BocSdkConfig.CONSUMER_PORT
//					+ strUrl, getHeaders(), strGson, callBack);
			post(BocSdkConfig.CONSUMER_URL +  strUrl, getHeaders(), strGson, callBack);
		} else {
			CustomProgressDialog.showBocNetworkSetDialog(mContext);
		}
	}

	/**
	 * 
	 * @param context
	 *            :上下文
	 * @param url
	 *            ：中银开放平台CSP通用接口地址
	 * @param mcis
	 *            ：开放平台报文
	 * @param myCallback
	 *            ：回调 罗阳：于20150703封装加载框，并监听DIALOG的取消时间
	 */
	private RequestHandle post(String url, Header[] headers, String strGson,
			final CallBackBoc3 callBack) {
		final String contentType = "application/json; charset=UTF-8";
		AsyncHttpClient client = new AsyncHttpClient();
		client.addHeader("Content-Type", contentType);
		client.addHeader("Cache-Control", "no-cache");
		client.addHeader("Accept-Charset", "UTF-8");
		client.setTimeout(30 * 1000);
		final RequestHandle handle;
		StringEntity strEntity = null;
		try {
			strEntity = new StringEntity(strGson);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		final CustomProgressDialog dialog = new CustomProgressDialog(mContext,
//				"...正在加载...", R.anim.frame);
//		dialog.show();
		// handle = client.post(mContext, url, headers, strEntity, contentType,
		// new AsyncHttpResponseHandler()
		handle = client.get(mContext, url, headers, null,
				new AsyncHttpResponseHandler() {

					@Override
					public void onStart() {
						super.onStart();
						Log.i("tag", "onStart");
						callBack.onStart();
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] responseBody) {
						String strResponseBody = null;
						Log.i("tag", "onSuccess:" + String.valueOf(statusCode));
						try {
							strResponseBody = new String(responseBody, "UTF-8");
							Log.i("tag",
									"APP的公共报头:"
											+ URLDecoder.decode(
													headers[0].getValue(),
													"UTF-8")
											+ "|||"
											+ URLDecoder.decode(
													headers[1].getValue(),
													"UTF-8")
											+ "|||"
											+ URLDecoder.decode(
													headers[2].getValue(),
													"UTF-8")
											+ "|||"
											+ URLDecoder.decode(
													headers[3].getValue(),
													"UTF-8")
											+ "|||"
											+ URLDecoder.decode(
													headers[4].getValue(),
													"UTF-8")
											+ "|||"
											+ URLDecoder.decode(
													headers[5].getValue(),
													"UTF-8"));
							Log.i("tag", "onSuccess:"
									+ new String(responseBody, "UTF-8"));
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
						if (statusCode == 200) {
							if (!strResponseBody.contains("invalid_grant")) {
								callBack.onSuccess(strResponseBody);
							} else {
								LoginUtil.logoutWithoutCallback((Activity)mContext);
								callBack.onFailure(strResponseBody);
							}
						} else {
							callBack.onFailure(String.valueOf(statusCode));
						}

					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] responseBody, Throwable error) {
						try {
							Log.i("tag", "onFailure statusCode:" + String.valueOf(statusCode));
							if(responseBody != null){
								callBack.onFailure(String.valueOf(statusCode) + " "
										+ new String(responseBody, "UTF-8"));
							}else{
								if(statusCode == 0){
									callBack.onFailure(String.valueOf(statusCode));
//									Toast.makeText(mContext, R.string.onFailure, Toast.LENGTH_SHORT).show();
								}
								else{
									callBack.onFailure(String.valueOf(statusCode));
								}
							}
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFinish() {
						super.onFinish();
						callBack.onFinish();
//						dialog.cancel();
					}

				});
		
//		dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//
//			@Override
//			public void onCancel(DialogInterface dialog) {
//				handle.cancel(true);
//			}
//		});
//		dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
//			
//			@Override
//			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//				if(keyCode == KeyEvent.KEYCODE_BACK){
//					dialog.cancel();
//					Toast.makeText(mContext, "您取消了加载", Toast.LENGTH_SHORT).show();
//					try{
//						if(handle.cancel(true)){
//							Log.i("tag1", "onCancel 取消 handle成功");
//						}else{
//							Log.i("tag1", "onCancel 取消handle失败");
//						}
//					}
//					catch(Exception e){
//						Log.i("tag", "异常");
//					}
//					
//				}
//				return true;
//			}
//		});
		return handle;
	}

	public interface CallBackBoc3 {
		public void onStart();

		public void onSuccess(String responStr);

		public void onFailure(String responStr);

		public void onFinish();
	}

	@Override
	public void onLogin() {
		Toast.makeText(mContext, "登陆成功", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onCancle() {
		Toast.makeText(mContext, "请重新登陆，登录后才能办理相关业务", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onError() {
		Toast.makeText(mContext, "登陆错误", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onException() {
		Toast.makeText(mContext, "登陆异常", Toast.LENGTH_LONG).show();
	}

}
