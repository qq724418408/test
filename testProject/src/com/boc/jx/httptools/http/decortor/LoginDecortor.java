package com.boc.jx.httptools.http.decortor;

import java.util.Map;

import com.boc.jx.httptools.http.callback.IProgressCallback;
import com.boc.jx.httptools.http.callback.IStringCallaBack;
import com.boc.jx.httptools.http.callback.IUpLoadCallback;
import com.boc.jx.httptools.http.engin.IHttpEngin;
import com.boc.jx.httptools.network.http.DConfing;

import android.content.Context;

public class LoginDecortor extends IHttpEngin {

	private LoginEnginCallBack mEnginCallBack;

	public LoginDecortor() {
	}

	public LoginDecortor(IHttpEngin engin) {
		super(engin);
	}

	@Override
	public void get(Context context, String url, Map<String, Object> params,
			IStringCallaBack callback) {
		mEnginCallBack = new LoginEnginCallBack(callback);
		httpEngin.get(context, url, params, mEnginCallBack);
	}

	@Override
	public void post(Context context, String url, Map<String, Object> params,
			IStringCallaBack callback) {
		mEnginCallBack = new LoginEnginCallBack(callback);
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

	public class LoginEnginCallBack implements IStringCallaBack {

		private IStringCallaBack mCallBack;

		public LoginEnginCallBack(IStringCallaBack callback) {
			this.mCallBack = callback;
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
				e = new Throwable(e.getMessage().replace("66666", DConfing.ErrorFromServer)
						.replace(DConfing.NeedReLogin, DConfing.ErrorFromServer));
			}
			mCallBack.onError(url, e);
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
