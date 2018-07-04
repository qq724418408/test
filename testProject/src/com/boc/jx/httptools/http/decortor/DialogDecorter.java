package com.boc.jx.httptools.http.decortor;

import java.util.Map;

import com.boc.jx.httptools.http.callback.IProgressCallback;
import com.boc.jx.httptools.http.callback.IStringCallaBack;
import com.boc.jx.httptools.http.callback.IUpLoadCallback;
import com.boc.jx.httptools.http.engin.IHttpEngin;
import com.boc.jx.httptools.network.http.DConfing;
import com.boc.jx.tools.LogUtils;
import com.bocop.jxplatform.R;
import com.bocop.xfjr.util.dialog.XfjrDialog;

import android.app.Activity;
import android.app.Application.ActivityLifecycleCallbacks;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;

/**
 * Description : dialog 装饰 dismiss后请滞空避免内存泄露
 * <p/>
 * Created : TIAN FENG Date : 2017/8/14 Email : 27674569@qq.com Version : 1.0
 */

public class DialogDecorter extends IHttpEngin implements ActivityLifecycleCallbacks {

	private Dialog mDialog;
	private IStringCallaBack mEnginCallBack;
	private Activity mActivity;

	public DialogDecorter() {
	}

	public DialogDecorter(IHttpEngin engin) {
		super(engin);
	}

	@Override
	public void get(Context context, String url, Map<String, Object> params, IStringCallaBack callback) {
		mEnginCallBack = new DialogEnginCallBack(callback);
		getDialog(context, url);
		httpEngin.get(context, url, params, mEnginCallBack);
	}

	@Override
	public void post(Context context, String url, Map<String, Object> params, IStringCallaBack callback) {
		try {
			mActivity = (Activity) context;
			mActivity.getApplication().registerActivityLifecycleCallbacks(this);
		} catch (Exception e) {
		}  
		// 创建dialog并且显示
		if (callback != null) {
			mEnginCallBack = new DialogEnginCallBack(callback);
			getDialog(context, url);
		}else{
			mEnginCallBack = callback;
		}
		httpEngin.post(context, url, params, mEnginCallBack);

	}

	@Override
	public void downLoad(Context context, String url, Map<String, Object> params, String path,
			IProgressCallback callback) {
		getDialog(context, url);
		httpEngin.downLoad(context, url, params, path, callback);
	}

	@Override
	public void upLoad(Context context, String url, Map<String, Object> params, IUpLoadCallback callback) {
		getDialog(context, url);
		httpEngin.upLoad(context, url, params, callback);
	}

	/**
	 * 获取dialog
	 * 
	 * @param context
	 * @param url
	 * @return
	 */
	public Dialog getDialog(Context context, String url) {
		if (mDialog == null) {
			creatDialog(context, url);
		} else if (!mDialog.isShowing()) {
			mDialog.show();
		}
		return mDialog;
	}

	/**
	 * 根据情况创建dialog
	 * 
	 * @param context
	 */
	private void creatDialog(Context context, final String url) {
		if (mDialog != null && mDialog.isShowing()) {
			LogUtils.e("2不创建dialog isShowing------>");
			return;
		}
		mDialog = new XfjrDialog.Builder(context).setContentView(R.layout.xfjr_loading_dialog).setCancelable(false)// 点击空白不能取消
				.show();
		LogUtils.e("1创建好了dialog------>" + mDialog);
		if (mDialog == null) {
			return;
		}
		mDialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
					LogUtils.e("Dialog isShowing按了返回键------>" + mDialog);
					return true;
				}
				return false;
			}
		});
	}

	public class DialogEnginCallBack implements IStringCallaBack {

		private IStringCallaBack mCallBack;

		public DialogEnginCallBack(IStringCallaBack callback) {
			this.mCallBack = callback;
		}

		/**
		 * 成功
		 */
		@Override
		public void onSuccess(String url, String result) {
			if (mDialog != null && mDialog.isShowing()) {
				LogUtils.e("onSuccess dialog取消------>" + mDialog);
				mDialog.dismiss();
				// mDialog = null;
			}
			mCallBack.onSuccess(url, result);
		}

		/**
		 * 失败
		 */
		@Override
		public void onError(String url, Throwable e) {

			if (mDialog != null && mDialog.isShowing()) {
				LogUtils.e("onError dialog取消------>" + mDialog);
				mDialog.dismiss();
			}
			if (!e.getMessage().contains(DConfing.TaskBusyCode + "")) {
				mCallBack.onError(url, e);
			}
			
		}

		/**
		 * 一定进入
		 */
		@Override
		public void onFinal(String url) {
			if (mDialog != null && mDialog.isShowing()) {
				LogUtils.e("onFinal dialog取消------>" + mDialog);
				mDialog.dismiss();
				// mDialog = null;
			}
			mCallBack.onFinal(url);
		}
	}

	@Override
	public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
		
	}

	@Override
	public void onActivityStarted(Activity activity) {
		
	}

	@Override
	public void onActivityResumed(Activity activity) {
		
	}

	@Override
	public void onActivityPaused(Activity activity) {
//		dismiss(activity);
	}

	@Override
	public void onActivityStopped(Activity activity) {
		dismiss(activity);
	}

	@Override
	public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
		dismiss(activity);
	}

	@Override
	public void onActivityDestroyed(Activity activity) {
		dismiss(activity);
	}
	
	void dismiss(Activity activity){
		if(activity==mActivity){
			if (mDialog != null && mDialog.isShowing()) {
				mDialog.dismiss();
			}
		}
	}

}
