package com.bocop.jxplatform.util;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;

import com.boc.jx.base.BaseApplication;
import com.boc.jx.baseUtil.asynchttpclient.AsyncHttpClient;
import com.boc.jx.baseUtil.asynchttpclient.AsyncHttpResponseHandler;
import com.boc.jx.baseUtil.asynchttpclient.RequestHandle;
import com.bocop.jxplatform.config.BocSdkConfig;

import android.annotation.SuppressLint;
import android.content.Context;

/**
 * @author luoyang
 * @version 创建时间：2015-6-23 下午5:46:43 分行CSP通用接口类，需要验证
 */

@SuppressLint("SimpleDateFormat")
public class BocOpVersionUtil {

	/**
	 * 用户信息
	 */
	public BaseApplication baseApplication = BaseApplication.getInstance();

	/*
	 * http报文头信息
	 */
	public Context mContext;

	public BocOpVersionUtil(Context context) {
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

	public void postCspLogin(String strGson, AsyncHttpResponseHandler responseHandler) {

		if (baseApplication.isNetStat()) {
			post("http://open.boc.cn/interFace" + "/getAppUpdate.php", getHeaders(), strGson, responseHandler);
		} else {
			CustomProgressDialog.showBocNetworkSetDialog(mContext);
		}
	}

	private void post(String url, Header[] headers, String strGson,
			AsyncHttpResponseHandler responseHandler) {
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
		client.post(mContext, url, headers, strEntity, contentType,
				responseHandler);
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
//	public post(String url, Header[] headers, String strGson,
//			AsyncHttpResponseHandler responseHandler) {
//		final String contentType = "application/json; charset=UTF-8";
//		AsyncHttpClient client = new AsyncHttpClient();
//		client.addHeader("Content-Type", contentType);
//		client.addHeader("Cache-Control", "no-cache");
//		client.addHeader("Accept-Charset", "UTF-8");
//		client.setTimeout(30 * 1000);
//		final RequestHandle handle;
//		StringEntity strEntity = null;
//		try {
//			strEntity = new StringEntity(strGson);
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		handle = client.post(mContext, url, headers, strEntity, contentType,
//				responseHandler);
//	}

}
