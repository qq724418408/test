package com.boc.jx.httptools.http.engin.okhttp2engin;

import java.io.IOException;

import com.boc.jx.httptools.http.callback.IStringCallaBack;
import com.boc.jx.tools.LogUtils;
import com.bocop.xfjr.XfjrMain;
import com.bocop.xfjr.config.UrlConfig;
import com.bocop.xfjr.constant.XFJRConstant;
import com.bocop.xfjr.util.PreferencesUtil;
import com.bocop.yfx.utils.ToastUtils;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Response;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

/**
 * description：
 * <p/>
 * Created by TIAN FENG on 2017/8/25 QQ：27674569 Email: 27674569@qq.com
 * Version：1.0
 */

public class OkHttp2SuccessRunnable implements Runnable {
	// okhttp 异步请求结果
	private String mResult;
	// 网络请求回调
	private IStringCallaBack mCallback;
	// 链接
	private String mUrl;
	private String c;
	private String currentSsession;
	private Context context;

	public OkHttp2SuccessRunnable(Context context, Response response, IStringCallaBack callback,
			String url) throws IOException {
		this.mCallback = callback;
		this.mUrl = url;
		mResult = response.body().string();
		Headers headers = response.headers();
		c = headers.get("Set-Cookie");
		if (TextUtils.isEmpty(c) && !TextUtils.isEmpty(OkHttp2Engin.ssession)) {
			LogUtils.e("OkHttp2SuccessRunnable 当前的ssession-->" + OkHttp2Engin.ssession);
			currentSsession = OkHttp2Engin.ssession;
		}
		this.context = context;
	}

	@Override
	public void run() {
		LogUtils.e("OkHttp2SuccessRunnable 存的ssession-->" + OkHttp2Engin.ssession);
		LogUtils.e("OkHttp2SuccessRunnable 当前返回的ssession-->" + c);
//		if (XfjrMain.isSit && !TextUtils.isEmpty(OkHttp2Engin.ssession) && !TextUtils.isEmpty(c)
//				&& Executors.getInstance().isLogining() && !OkHttp2Engin.ssession.equals(c)) {
//			ToastUtils.show(context, "ssession变了", 0);
//			
//		}
		if (XfjrMain.isSit) {
			if (!TextUtils.isEmpty(c) && c.contains(UrlConfig.SERVER_ROOT_PATH.replace("/", ""))) { // Path=/Zero-api/
				if (!c.equals(currentSsession)) {
					boolean isLogin = PreferencesUtil.get(XFJRConstant.KEY_IS_LOGIN, false);
					if (isLogin) ToastUtils.show(context, "ssession变了", 0);
				}
			}
		}
		//LogUtils.e("session--->>>" + c);
		if (!TextUtils.isEmpty(c) && c.contains(UrlConfig.SERVER_ROOT_PATH.replace("/", ""))) { // Path=/Zero-api/
			OkHttp2Engin.ssession = c;
		}

		Log.e("43 json = ", mResult);// 11-15 09:57:06.510: E/43 json =(19842): {"message":{"content":"","host":"http://localhost:8080","rtnCode":"10001","rtnMsg":"EF000003 - Function \"API020203\" requires login"},"result":"success","DYNAMIC_TOKEN":"PZ1DDFZDR1WZMNI3KVAPZPMS8RN5MHNG"}

		try {
//			if(mResult.contains("\"rtnCode\":\"10040\",\"rtnMsg\":\"EF00")){
//				throw new Exception(/*"{\"msg\":\"" + "账号异常,请重新登录"+"\",\"code\":\""+"11111"+"\"}"*/",账号异常");
//			}
			mCallback.onSuccess(mUrl, mResult);
		} catch (Exception e) {
			e.printStackTrace();
			mCallback.onError(mUrl, e);
		} finally {
			mCallback.onFinal(mUrl);
		}
	}
}
