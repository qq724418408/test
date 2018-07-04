package com.boc.jx.httptools.http.decortor;

import java.util.Map;

import com.boc.jx.httptools.http.HttpUtils;
import com.boc.jx.httptools.http.callback.IHttpCallback;
import com.boc.jx.httptools.http.callback.IProgressCallback;
import com.boc.jx.httptools.http.callback.IStringCallaBack;
import com.boc.jx.httptools.http.callback.IUpLoadCallback;
import com.boc.jx.httptools.http.engin.IHttpEngin;
import com.boc.jx.httptools.network.DDecortor;
import com.boc.jx.httptools.network.http.DConfing;
import com.boc.jx.tools.LogUtils;
import com.bocop.xfjr.config.UrlConfig;
import com.bocop.xfjr.util.PreferencesUtil;

import android.content.Context;

public class DErrorDecortor extends IHttpEngin {

	private DErrorDecortorCallBack mEnginCallBack;

	public DErrorDecortor() {
	}

	public DErrorDecortor(IHttpEngin engin) {
		super(engin);
	}

	@Override
	public void get(Context context, String url, Map<String, Object> params,
			IStringCallaBack callback) {
		mEnginCallBack = new DErrorDecortorCallBack(callback, context, url, params);
		httpEngin.get(context, url, params, mEnginCallBack);
	}

	@Override
	public void post(Context context, String url, Map<String, Object> params,
			IStringCallaBack callback) {
		mEnginCallBack = new DErrorDecortorCallBack(callback, context, url, params);
		httpEngin.post(context, url, params, mEnginCallBack);

	}

	@Override
	public void downLoad(Context context, String url, Map<String, Object> params, String path,
			IProgressCallback callback) {
		httpEngin.downLoad(context, url, params, path, callback);
	}

	@Override
	public void upLoad(Context context, String url, Map<String, Object> params,
			IUpLoadCallback callback) {
		httpEngin.upLoad(context, url, params, callback);
	}

	public class DErrorDecortorCallBack implements IStringCallaBack {

		private IStringCallaBack mCallBack;
		Context context;
		String url;
		Map<String, Object> params;

		public DErrorDecortorCallBack(IStringCallaBack callback,Context context,String url, Map<String, Object> params) {
			this.mCallBack = callback;
			this.context = context;
			this.url = url;
			this.params = params;
		}

		/**
		 * 成功
		 */
		@Override
		public void onSuccess(String url, String result) {
			mCallBack.onSuccess(url, result);
		}

		/**
		 * 失败
		 */
		@Override
		public void onError(String url, Throwable e) {
			boolean flag = e.getMessage().contains("66666")
					|| e.getMessage().contains(DConfing.NeedReLogin);
			if (flag) {
				LogUtils.e("账号异常，请重新登录---ACCESST_TOKEN_LOGIN");
				String accessToken = PreferencesUtil.get(DConfing.ACCESS_TOKEN, "");
//				String publicKey = PreferencesUtil.get(DConfing.PUBLICK_KEY, "");
//		        accessToken = RSASecurity.decryptByPublic(accessToken, publicKey);
				HttpUtils.with(context)
				.url(UrlConfig.ACCESST_TOKEN_LOGIN)
				.addParams("accessToken", accessToken)
				.post(new IHttpCallback<String>() {

					@Override
					public void onSuccess(String url, String result) {
						LogUtils.e("账号异常，请重新登录---ACCESST_TOKEN_LOGIN--onSuccess");
						HttpUtils utils = HttpUtils.with(context);
						DDecortor dDecortor = new DDecortor();
						dDecortor.addDecortorEngin(utils.getEngin());
						dDecortor.post(context, DErrorDecortorCallBack.this.url, params, mCallBack);
					}

					@Override
					public void onError(String url, Throwable e) {
						LogUtils.e("账号异常，请重新登录---ACCESST_TOKEN_LOGIN--onError");
						mCallBack.onError(url, e);
					}

					@Override
					public void onFinal(String url) {
						LogUtils.e("账号异常，请重新登录---ACCESST_TOKEN_LOGIN--onFinal");
						mCallBack.onFinal(url);
					}
				});
			} else {
				LogUtils.e("DErrorDecortor---66666---onError");
				mCallBack.onError(url, e);
			}
			LogUtils.e("DErrorDecortor---onError");
//			mCallBack.onError(url, e);
			mCallBack.onFinal(url);
		}

		/**
		 * 一定进入
		 */
		@Override
		public void onFinal(String url) {
			mCallBack.onFinal(url);
		}
	}

}
