package com.boc.jx.httptools.network;

import java.util.Map;

import com.boc.jx.httptools.http.callback.IProgressCallback;
import com.boc.jx.httptools.http.callback.IStringCallaBack;
import com.boc.jx.httptools.http.callback.IUpLoadCallback;
import com.boc.jx.httptools.http.engin.IHttpEngin;
import com.boc.jx.httptools.network.callback.StringCallBack;
import com.boc.jx.httptools.network.http.DConfing;
import com.boc.jx.httptools.network.http.build.HttpBuild;
import com.boc.jx.tools.LogUtils;
import com.boc.jx.tools.NetworkUtil;

import android.content.Context;

/**
 * 功能说明
 * 说明：详细说明
 *
 * @author formssi
 */

public class DDecortor extends IHttpEngin {

    public DDecortor(){

    }

    @Override
    public void get(Context context, String url, Map<String, Object> params, IStringCallaBack callback) {

    }

    @Override
    public void post(Context context, final String url, Map<String, Object> params, final IStringCallaBack callback) {
    	
    	if (!NetworkUtil.isNetworkAvailable(context)) {
    		String errMsg = "{\"msg\":\"" + "当前无网络"+"\",\"code\":\""+(-1)+"\"}";
    		callback.onError(url, new Throwable(errMsg));
    		callback.onFinal(url);
			return;
		}
    	
        LogUtils.e("网络请求开始post ");
    	Executors.simpleBuilder(context)
                .url(url)
                .Mehod(HttpBuild.Method.POST)
                .addParams(params)
                .callBack(new StringCallBack() {
                	
                	@Override
                	public void onError(String request, String msg, int code) {
                		String errMsg = "{\"msg\":\"" + msg+"\",\"code\":\""+code+"\"}";
                        callback.onError(url,new Exception(errMsg));
                        if (code!=DConfing.TaskBusyCode) {
                            callback.onFinal(url);
						}
                        LogUtils.e("网络请求结束onError  msg = " + msg + "  , code = "+ code +" , request" + request);
                    }

                    @Override
                    public void onResponse(String response) {
                    	LogUtils.e("网络请求结束onResponse  response" + response);
                        callback.onSuccess(url,response);
                        callback.onFinal(url);
                    }

                    @Override
                    public void onEmpty(String msg) {
                        super.onEmpty(msg);
                        String errMsg = "{\"msg\":\"" + msg+"\",\"code\":\""+10013+"\"}";
                        LogUtils.e("网络请求结束onEmpty msg = "+ msg);
                        callback.onError(url,new Exception(errMsg));
                        callback.onFinal(url);
                    }
                    
                    @Override
                    public void loginFailed(String msg, int code) {//需要accesstoken 登录，并且登录失败
                    	LogUtils.e("网络请求结束loginFailed  msg = " + msg + "  , code = "+ code);
                    	String errMsg = "{\"msg\":\"" + msg+"\",\"code\":\""+code+"\"}";
                    	callback.onError(url,new Exception(errMsg));
                        callback.onFinal(url);
                    }
                   
                    @Override
                    public void onEspecialCode(String code, String response, String msg) {
                    	LogUtils.e("网络请求结束onEspecialCode  msg = " + msg + "  , code = "+ code +" , response" + response);
                    	super.onEspecialCode(code, response, msg);
                    	String errMsg = "{\"msg\":\"" + msg+"\",\"code\":\""+code+"\"}";
                    	callback.onError(url,new Exception(errMsg));
                        callback.onFinal(url);
                    }
                    
				})
                .build().send();
    }

    @Override
    public void downLoad(Context context, String url, Map<String, Object> params, String fileName, IProgressCallback callback) {

    }

    @Override
    public void upLoad(Context context, final String url, Map<String, Object> params, final IUpLoadCallback callback) {
        Executors.simpleBuilder(context)
                .url(url)
                .addParams(params)
                .Mehod(HttpBuild.Method.UPLOAD)
                .callBack(new StringCallBack() {
                    @Override
                    public void inProgress(double progress) {
                        super.inProgress(progress);
                        callback.onProgress(100, (long) (progress*100));
                    }

                    @Override
                    public void onError(String msg, String e, int code) {
                        super.onError(msg, e, code);
                        callback.onError(new Exception(msg));
                        callback.onFinal();
                    }

                    @Override
                    public void onResponse(String response) {
                        super.onResponse(response);
                        callback.onSuccess(response);
                        callback.onFinal();
                    }

                    @Override
                    public void onEmpty(String msg) {
                        super.onEmpty(msg);
                        callback.onSuccess("");
                        callback.onFinal();
                    }
                }).build().send();
    }

    @Override
    public void cancle(String url) {
//        super.cancle(url);
        Executors.getInstance().cancelThis(url);
    }
}
