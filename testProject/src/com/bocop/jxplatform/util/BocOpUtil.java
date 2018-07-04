package com.bocop.jxplatform.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.boc.jx.base.BaseApplication;
import com.boc.jx.baseUtil.asynchttpclient.AsyncHttpClient;
import com.boc.jx.baseUtil.asynchttpclient.AsyncHttpResponseHandler;
import com.boc.jx.baseUtil.asynchttpclient.RequestHandle;
import com.bocop.jxplatform.bean.BocRecComBodyBean;
import com.bocop.jxplatform.config.BocSdkConfig;
import com.bocop.jxplatform.util.LoginUtil.ILoginListener;
import com.bocop.jxplatform.R;

/**
 * @author luoyang
 * @version 创建时间：2015-6-23 下午5:46:43 分行CSP通用接口类，需要验证
 */

@SuppressLint("SimpleDateFormat")
public class BocOpUtil  {
	
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

	public BocOpUtil(Context context) {
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


	//*********4-1yuxinhan************
	@SuppressLint("SimpleDateFormat")
	public Header[] getHeadersNolog() {
		Header clientid = new BasicHeader("clentid", BocSdkConfig.CONSUMER_KEY);
		Header userid = new BasicHeader("userid", "1234567");
		Header action = new BasicHeader("acton", "1234567");
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

	public void postOpboc(String strGson,String strUrl, CallBackBoc callBack) {
		postOpboc(strGson, strUrl, true, callBack);
	}
//********yuxinhan4-1*****
	public void postOpbocNolog(String strGson,String strUrl, CallBackBoc callBack) {
		postOpbocNolog(strGson, strUrl, true, callBack);
	}

	public void postOpbocNoDialog(String strGson,String strUrl, CallBackBoc callBack) {
		postOpboc(strGson, strUrl, false, callBack);
	}
	
	public void postOpboc(String strGson, String strUrl, boolean isShowDialog, CallBackBoc callBack) {

		if (baseApplication.isNetStat()) {
			post(BocSdkConfig.CONSUMER_URL + ":" + BocSdkConfig.CONSUMER_PORT + strUrl, getHeaders(), strGson,isShowDialog, callBack);
		} else {
			CustomProgressDialog.showBocNetworkSetDialog(mContext);
		}
	}


	//*******yuxinhan4-1*******
	public void postOpbocNolog(String strGson, String strUrl, boolean isShowDialog, CallBackBoc callBack) {

		if (baseApplication.isNetStat()) {
			postNolog(BocSdkConfig.CONSUMER_URL + ":" + BocSdkConfig.CONSUMER_PORT + strUrl, getHeadersNolog(), strGson,isShowDialog, callBack);
//			post(BocSdkConfig.CONSUMER_URL +  strUrl, getHeaders(), strGson, isShowDialog, callBack);
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
			boolean isShowDialog, final CallBackBoc callBack) {
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
		final CustomProgressDialog dialog = new CustomProgressDialog(mContext,
				"...正在加载...", R.anim.frame);
		
		if (isShowDialog) {
			dialog.show();
		}
		
		handle = client.post(mContext, url, headers, strEntity, contentType,
				new AsyncHttpResponseHandler() {

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
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
							Log.i("tag","APP的公共报头:" +URLDecoder.decode(headers[3].getValue(), "UTF-8") + "|||" + URLDecoder.decode(headers[4].getValue(), "UTF-8") + "|||" + URLDecoder.decode(headers[5].getValue(), "UTF-8"));	
							Log.i("tag", "onSuccess:" + new String(responseBody,"UTF-8"));
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if(statusCode == 200){
							try {
								msgcode = URLDecoder.decode(headers[3].getValue(), "UTF-8");
								rtnmsg = URLDecoder.decode(headers[4].getValue(), "UTF-8");
								if(msgcode.equals("0000000")){
								callBack.onSuccess(new String(responseBody,"UTF-8"));
								}
								else{
									Log.i("tag", "BocOpUtil call onFailure msgcde ");
									BocRecComBodyBean bocRecComBodyBean = null;
									try {
										bocRecComBodyBean = JsonUtils.getObject(strResponseBody, BocRecComBodyBean.class);
										if(bocRecComBodyBean.getRtnmsg().contains("ccess")|| bocRecComBodyBean.getRtnmsg().contains("expired")){
											Toast.makeText(mContext, "温馨提示：登陆超时，请重新登陆。", Toast.LENGTH_LONG).show();
		                                    LoginUtil.logoutWithoutCallback(mContext);
										}else{
											Toast.makeText(mContext, bocRecComBodyBean.getRtnmsg(), Toast.LENGTH_LONG).show();
											callBack.onFailure(bocRecComBodyBean.getMsgcde());
										}
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							} catch (UnsupportedEncodingException e) {
								callBack.onFailure(e.getMessage().toString());
							}
						}
						else{
							callBack.onFailure(String.valueOf(statusCode));
						}
						
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] responseBody, Throwable error) {
						try {
							Log.i("tag", "onFailure statusCode:" + String.valueOf(statusCode));
//							if(statusCode == 0){
//								Toast.makeText(mContext, R.string.onFailure, Toast.LENGTH_SHORT).show();
//							}
							if(responseBody != null){
								callBack.onFailure(String.valueOf(statusCode) + " "
										+ new String(responseBody, "UTF-8"));
							}else{
								callBack.onFailure(String.valueOf(statusCode));
							}
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFinish() {
						// TODO Auto-generated method stub
						super.onFinish();
						callBack.onFinish();
						dialog.cancel();
					}

				});
		
		if (isShowDialog) {
			dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
				
				@Override
				public void onCancel(DialogInterface dialog) {
					try {
	                    if (handle.cancel(true)) {
	                        Log.i("tag1", "onCancel 取消 handle成功");
	                    }
	                    else {
	                        Log.i("tag1", "onCancel 取消handle失败");
	                    }
	                } catch (Exception e) {
	                    Log.i("tag", "异常");
	                }
				}
			});
			dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
				
				@Override
				public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
					if(keyCode == KeyEvent.KEYCODE_BACK){
						dialog.cancel();
						Toast.makeText(mContext, "您取消了加载", Toast.LENGTH_SHORT).show();
						try{
							if(handle.cancel(true)){
								Log.i("tag1", "onCancel 取消 handle成功");
							}else{
								Log.i("tag1", "onCancel 取消handle失败");
							}
						}
						catch(Exception e){
							Log.i("tag", "异常");
						}
						
					}
					return true;
				}
			});
		}
		return handle;
	}

//******yuxinhan4-1*******
	private RequestHandle postNolog(String url, Header[] headers, String strGson,
							   boolean isShowDialog, final CallBackBoc callBack) {
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
		final CustomProgressDialog dialog = new CustomProgressDialog(mContext,
				"...正在加载...", R.anim.frame);

		if (isShowDialog) {
			dialog.show();
		}

		handle = client.post(mContext, url, headers, strEntity, contentType,
				new AsyncHttpResponseHandler() {

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
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
							Log.i("tag","APP的公共报头:" +URLDecoder.decode(headers[3].getValue(), "UTF-8") + "|||" + URLDecoder.decode(headers[4].getValue(), "UTF-8") + "|||" + URLDecoder.decode(headers[5].getValue(), "UTF-8"));
							Log.i("tag", "onSuccess:" + new String(responseBody,"UTF-8"));
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if(statusCode == 200){
							try {
								msgcode = URLDecoder.decode(headers[3].getValue(), "UTF-8");
								rtnmsg = URLDecoder.decode(headers[4].getValue(), "UTF-8");
								if(msgcode.equals("0000000")){
									callBack.onSuccess(new String(responseBody,"UTF-8"));
									Toast.makeText(mContext, "验证成功", Toast.LENGTH_LONG).show();
								}
								else{
									Log.i("tag", "BocOpUtil call onFailure msgcde ");
									BocRecComBodyBean bocRecComBodyBean = null;
									try {
										bocRecComBodyBean = JsonUtils.getObject(strResponseBody, BocRecComBodyBean.class);
										if(bocRecComBodyBean.getRtnmsg().contains("ccess")|| bocRecComBodyBean.getRtnmsg().contains("expired")){
											Toast.makeText(mContext, "温馨提示：登陆超时，请重新登陆。", Toast.LENGTH_LONG).show();
											LoginUtil.logoutWithoutCallback(mContext);
										}else{
											Toast.makeText(mContext, bocRecComBodyBean.getRtnmsg(), Toast.LENGTH_LONG).show();
											callBack.onFailure(bocRecComBodyBean.getMsgcde());
										}
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							} catch (UnsupportedEncodingException e) {
								callBack.onFailure(e.getMessage().toString());
							}
						}
						else{
							callBack.onFailure(String.valueOf(statusCode));
						}

					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
										  byte[] responseBody, Throwable error) {
						try {
							Log.i("tag", "onFailure statusCode:" + String.valueOf(statusCode));
//							if(statusCode == 0){
//								Toast.makeText(mContext, R.string.onFailure, Toast.LENGTH_SHORT).show();
//							}
							if(responseBody != null){
								callBack.onFailure(String.valueOf(statusCode) + " "
										+ new String(responseBody, "UTF-8"));
							}else{
								callBack.onFailure(String.valueOf(statusCode));
							}
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFinish() {
						// TODO Auto-generated method stub
						super.onFinish();
						callBack.onFinish();
						dialog.cancel();
					}

				});

		if (isShowDialog) {
			dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					try {
						if (handle.cancel(true)) {
							Log.i("tag1", "onCancel 取消 handle成功");
						}
						else {
							Log.i("tag1", "onCancel 取消handle失败");
						}
					} catch (Exception e) {
						Log.i("tag", "异常");
					}
				}
			});
			dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

				@Override
				public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
					if(keyCode == KeyEvent.KEYCODE_BACK){
						dialog.cancel();
						Toast.makeText(mContext, "您取消了加载", Toast.LENGTH_SHORT).show();
						try{
							if(handle.cancel(true)){
								Log.i("tag1", "onCancel 取消 handle成功");
							}else{
								Log.i("tag1", "onCancel 取消handle失败");
							}
						}
						catch(Exception e){
							Log.i("tag", "异常");
						}

					}
					return true;
				}
			});
		}
		return handle;
	}



	public interface CallBackBoc {
		public void onStart();
		
		public void onSuccess(String responStr);

		public void onFailure(String responStr);

		public void onFinish();
	}


}
