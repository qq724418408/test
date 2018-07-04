package com.boc.jx.httptools.http.decortor;

import java.net.ConnectException;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.boc.jx.httptools.http.callback.IProgressCallback;
import com.boc.jx.httptools.http.callback.IStringCallaBack;
import com.boc.jx.httptools.http.callback.IUpLoadCallback;
import com.boc.jx.httptools.http.engin.IHttpEngin;
import com.boc.jx.tools.LogUtils;
import com.bocop.xfjr.config.FaceConfig;

import android.content.Context;

/**
 * description： 剥壳装饰器
 * <p/>
 * Created by TIAN FENG on 2017年9月7日 QQ：27674569 Email: 27674569@qq.com
 * Version：1.0
 */
public class ContentDecorter extends IHttpEngin {

	public ContentDecorter() {

	}

	public ContentDecorter(IHttpEngin engin) {
		super(engin);
	}

	@Override
	public void get(Context context, String url, Map<String, Object> params, IStringCallaBack callback) {
		httpEngin.get(context, url, params, new ContentCallback(callback));
	}

	@Override
	public void post(Context context, String url, Map<String, Object> params, IStringCallaBack callback) {
		httpEngin.post(context, url, params, new ContentCallback(callback));
	}

	@Override
	public void downLoad(Context context, String url, Map<String, Object> params, String path,
			IProgressCallback callback) {
		httpEngin.downLoad(context, url, params, path, callback);
	}

	@Override
	public void upLoad(Context context, String url, Map<String, Object> params, IUpLoadCallback callback) {
		httpEngin.upLoad(context, url, params,
				/* new ContentIUpLoadCallback(callback) */callback);
	}

	private class ContentCallback implements IStringCallaBack {
		private IStringCallaBack callback;
		
		public ContentCallback(IStringCallaBack callback) {
			super();
			this.callback = callback;
		}

		@Override
		public void onSuccess(String url, String result) {
			LogUtils.e("66 json = "+result);
			if (url.equals(FaceConfig.GET_CONTRAST_URL)||url.equals(FaceConfig.GET_FICE_ID_URL)) {
				callback.onSuccess(url, result);
				return ;
			}
			try {
				JSONObject jsonObject = new JSONObject(result);
				jsonObject = new JSONObject(jsonObject.optString("message"));
				if (jsonObject.getString("rtnCode").equals("10000")) {
					result = jsonObject.getString("content");
					callback.onSuccess(url, result);
				} else {
					callback.onError(url, new ConnectException(result));
				}
				LogUtils.e("80 json = "+result);
			} catch (Throwable e) {
				e.printStackTrace();
				LogUtils.e("83 "+e.getMessage());
				callback.onError(url, e);
			}
		}

		@Override
		public void onError(String url, Throwable e) {
			callback.onError(url, e);
		}

		@Override
		public void onFinal(String url) {
			callback.onFinal(url);
		}

	}

	private class ContentIUpLoadCallback implements IUpLoadCallback {

		private IUpLoadCallback callback;
		

		public ContentIUpLoadCallback(IUpLoadCallback callback) {
			super();
			this.callback = callback;
		}

		@Override
		public void onProgress(long total, long current) {
			callback.onProgress(total, current);
		}

		@Override
		public void onError(Throwable e) {
			callback.onError(e);
		}

		@Override
		public void onFinal() {
			callback.onFinal();
		}
		@Override
		public void onSuccess(String result) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (jsonObject.getString("rtnCode").equals("10000")) {
					result = jsonObject.getString("content");
					callback.onSuccess(result);
				} else {
					callback.onError(new ConnectException(result));
				}

			} catch (JSONException e) {
				e.printStackTrace();
				callback.onError(e);
			}
		}

	}
}
