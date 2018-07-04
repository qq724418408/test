package com.boc.jx.baseUtil.net;

import android.content.Context;

import com.boc.jx.baseUtil.asynchttpclient.AsyncHttpClient;
import com.boc.jx.baseUtil.asynchttpclient.RequestHandle;

/**
 * Created by hwt on 15/1/19.
 */
public class HttpRequest extends Http {

	private RequestHandle requestHandle;
	private boolean isFinished = false;

	private HttpRequest(Context context, HttpConfig httpConfig, AsyncHttpClient httpClient) {
		super(context, httpConfig, httpClient);
	}

	public static HttpRequest getNewHttpRequest(Context context, HttpConfig httpConfig, AsyncHttpClient httpClient) {
		HttpRequest request = new HttpRequest(context, httpConfig, httpClient);
		request.type = TYPE_REQUEST;
		return request;
	}

	/**
	 * 开始发送网络请求
	 */
	@Override
	public void sendRequest() {
		requestHandle = request(this);
	}

	@Override
	protected void retryRequest() {
		requestHandle = request(this);
	}

	@Override
	public void progress(int bytesWritten, int totalSize) {

	}

	/**
	 * @param retCode
	 */
	@Override
	public void finish(RetCode retCode) {
		isFinished = true;
		hideLoadingView(retCode);

	}

	/**
	 * 当前网络请求是否完成
	 *
	 * @return
	 */
	@Override
	public boolean isFinished() {
		return isFinished;
	}

	/**
	 * 中断请求时，取消当前网络请求
	 */
	@Override
	public void interupt() {
		requestHandle.cancel(true);
	}

	public Http getRequest() {
		return getNewHttpRequest(getContext(), getHttpConfig(), getHttpClient());
	}

}
