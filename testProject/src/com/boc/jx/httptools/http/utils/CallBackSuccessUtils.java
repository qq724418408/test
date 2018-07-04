package com.boc.jx.httptools.http.utils;

import com.boc.jx.httptools.http.callback.IHttpCallback;
import com.boc.jx.tools.LogUtils;




/**
 * Description :  网络请求成功回调带解析泛型（Gson解析）
 * <p/>
 * Created : TIAN FENG
 * Date : 2017/8/14
 * Email : 27674569@qq.com
 * Version : 1.0
 */

public class CallBackSuccessUtils {

    // 成功后的解析
    public static <T> void onSuccess(IHttpCallback<T> callback, String json, String url) {
        if (callback == null) {
            return;
        }
        // 解析结果
        T result = null;
        // 泛型class
        Class<T> clazz = (Class<T>) HttpEnginUtils.analysisInterfaceInfo(callback,IHttpCallback.class.getName());
        
//        T object = JSON.parseObject(json, clazz);
//        callback.onSuccess(url, object);
        LogUtils.e("clazz = "+clazz);
        try {
            // 解析为list是否出错
            result = (T) GsonUtils.jsonToArrayList(json, clazz);
        } catch (Throwable e) {
            // 出错后解析正常对象
            // 判断class是否为String
            if (clazz.getName().equals(String.class.getName())) {
                result = (T) json;
            } else {
                result = GsonUtils.getInstanceByJson(clazz, json);
            }
        } finally {
        	 LogUtils.e("CallBackSuccessUtils json = "+result);
            callback.onSuccess(url, result);
        }
    }
}
