package com.bocop.jxplatform.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;

import com.boc.jx.base.BaseApplication;
import com.boc.jx.baseUtil.asynchttpclient.AsyncHttpClient;
import com.boc.jx.baseUtil.asynchttpclient.AsyncHttpResponseHandler;
import com.boc.jx.baseUtil.asynchttpclient.RequestHandle;
import com.bocop.jxplatform.bean.BocRecComBodyBean;
import com.bocop.jxplatform.config.BocSdkConfig;
import com.bocop.jxplatform.util.LoginUtil.ILoginListener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * @author luoyang
 * @version 创建时间：2015-6-23 下午5:46:43 分行CSP通用接口类，需要验证
 */

@SuppressLint("SimpleDateFormat")
public class BocOpUtilWithoutDia implements ILoginListener {

	/**
	 * 用户信息
	 */
	public BaseApplication baseApplication = BaseApplication.getInstance();

	/*
	 * http报文头信息
	 */
	public Context mContext;
	
	 String msgcode;
	 String rtnmsg;

	public BocOpUtilWithoutDia(Context context) {
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

	public void postOpboc(String strGson,String strUrl, CallBackBoc2 callBack) {

		if (baseApplication.isNetStat()) {
//			post(BocSdkConfig.CONSUMER_URL + ":" + BocSdkConfig.CONSUMER_PORT + strUrl, getHeaders(), strGson, callBack);
			post(BocSdkConfig.CONSUMER_URL +  strUrl, getHeaders(), strGson, callBack);
		} else {
			callBack.onFailure("1");			//网络不通 
			CustomProgressDialog.showBocNetworkSetDialog(mContext);
		}
	}
//************4-1yuxinhan************
	public void postOpbocNolog(String strGson,String strUrl, CallBackBoc2 callBack) {

		if (baseApplication.isNetStat()) {
//			post(BocSdkConfig.CONSUMER_URL + ":" + BocSdkConfig.CONSUMER_PORT + strUrl, getHeaders(), strGson, callBack);
			post(BocSdkConfig.CONSUMER_URL +  strUrl, getHeadersNolog(), strGson, callBack);
		} else {
			callBack.onFailure("1");			//网络不通
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
			final CallBackBoc2 callBack) {
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
							Log.i("tag2","APP的公共报头:" +URLDecoder.decode(headers[3].getValue(), "UTF-8") + "|||" + URLDecoder.decode(headers[4].getValue(), "UTF-8") + "|||" + URLDecoder.decode(headers[5].getValue(), "UTF-8"));	
							Log.i("tag", "onSuccess:" + new String(responseBody,"UTF-8"));
							Log.i("tag", "onSuccess:" + new String(responseBody));
							Log.i("tag", "onSuccess:" + new String(responseBody,"GBK"));
							Log.i("tag", "onSuccess:" + new String(responseBody,"GB2312"));
							Log.i("tag", "onSuccess:" + new String(responseBody,"UTF-16"));
							Log.i("tag", "onSuccess:" + new String(responseBody,"ISO-8859-1"));
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
						if(statusCode == 200){
							try {
								msgcode = URLDecoder.decode(headers[3].getValue(), "UTF-8");
								rtnmsg = URLDecoder.decode(headers[4].getValue(), "UTF-8");
								if(msgcode.equals("0000000"))
								{
									callBack.onSuccess(new String(responseBody,"UTF-8"));
								}
								else{
									Log.i("tag", "BocOpUtil call onFailure msgcde ");
									BocRecComBodyBean bocRecComBodyBean = null;
									try {
										bocRecComBodyBean = JsonUtils.getObject(strResponseBody, BocRecComBodyBean.class);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									if(bocRecComBodyBean.getMsgcde().equals("invalid_token")){
										Toast.makeText(mContext, "请重新登陆", Toast.LENGTH_LONG).show();
										LoginUtil.authorize(mContext, BocOpUtilWithoutDia.this);
									}else{
										Toast.makeText(mContext, bocRecComBodyBean.getRtnmsg(), Toast.LENGTH_LONG).show();
									}
									Log.i("tag", "Success:" +  bocRecComBodyBean.getRtnmsg());
									Log.i("tag", "Success:" + new String(bocRecComBodyBean.getRtnmsg().getBytes("ISO-8859-1"),"utf-8"));
									Log.i("tag", "Success:" + new String(bocRecComBodyBean.getRtnmsg().getBytes("ISO-8859-1"),"GB2312"));
									Log.i("tag", "Success:" + new String(bocRecComBodyBean.getRtnmsg().getBytes("ISO-8859-1"),"GBK"));
									callBack.onFailure(new String(bocRecComBodyBean.getMsgcde() + bocRecComBodyBean.getRtnmsg()));
//									callBack.onFailure("错误信息：" + msgcode + rtnmsg);
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
							Log.i("tag", "超时: " + String.valueOf(statusCode));
							if(responseBody != null){
								callBack.onFailure(String.valueOf(statusCode) + " "
										+ new String(responseBody, "UTF-8"));
							}else{
								callBack.onFailure(String.valueOf(statusCode));
							}
						} catch (UnsupportedEncodingException e) {
							callBack.onFailure(e.getMessage());
						}
					}

					@Override
					public void onFinish() {
						// TODO Auto-generated method stub
						super.onFinish();
						callBack.onFinish();
						Log.i("tag", "onfinish");
					}

				});
		return handle;
	}

	public interface CallBackBoc2 {
		public void onStart();
		
		public void onSuccess(String responStr);

		public void onFailure(String responStr);

		public void onFinish();
	}

	@Override
	public void onLogin() {
		// TODO Auto-generated method stub
		Toast.makeText(mContext, "登陆成功", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onCancle() {
		// TODO Auto-generated method stub
		Toast.makeText(mContext, "请重新登陆，登录后才能办理相关业务", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onError() {
		// TODO Auto-generated method stub
		Toast.makeText(mContext, "登陆错误", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onException() {
		// TODO Auto-generated method stub
		Toast.makeText(mContext, "登陆异常", Toast.LENGTH_LONG).show();
	}

}
