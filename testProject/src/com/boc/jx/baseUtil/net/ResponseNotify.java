package com.boc.jx.baseUtil.net;

import android.text.TextUtils;
import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.boc.jx.baseUtil.logger.Logger;

import org.apache.http.Header;

import java.lang.reflect.Type;

/**
 * Created by hwt on 2/2/15.
 * <p><strong> 网络请求响应解析实现。</strong></p>
 * <p>通用 JSON 格式解析</p>
 */
public abstract class ResponseNotify<T> extends Notify {
    private Type type;

    /**
     * 目标数据类型描述
     *
     * @param typeReference 目标数据类型
     */
    public ResponseNotify(TypeReference<RespBean<T>> typeReference) {
        type = typeReference.getType();
    }

    /**
     * 响应解析处理
     *
     * @param observer    请求监视工具
     * @param headers     请求头信息
     * @param responseStr 请求响应体数据
     */
    @Override
    protected void parseResponse(ResponseObserver observer, String requestUrl, Header[] headers, String responseStr) {
        StringBuffer headerInfo = new StringBuffer();
        if (headers != null) {
            for (Header header : headers) {
                headerInfo.append("\n" + header.getName() + "=" + header.getValue());
            }
        }
        Logger.d("-----------------header info-----------------" +
                headerInfo.toString() +
                "\n" +
                "requestUrl==>" + requestUrl);
        
//        RespBean<T> respBean = JSON.parseObject(responseStr, type);
//        if (respBean != null) {
//            RetCode retCode = HttpConfig.getReturnCode(respBean.getRtnCode());
//            if (retCode == RetCode.success || retCode == RetCode.noData) {
//                onResponse(retCode, respBean);
//            } else if (retCode == RetCode.loginIllegal) {
//                observer.loginIllegal();
//            } else {
//                onFailed(retCode);
//            }
//            observer.finish(observer, retCode);
//        } else {
//            Logger.d("响应数据解析异常...");
//        }

        if (!TextUtils.isEmpty(responseStr) && responseStr.trim().length() > 0) {
            if ((responseStr.startsWith("{") && responseStr.endsWith("}")) ||
                    (responseStr.startsWith("[") && responseStr.endsWith("]"))) {
                Logger.json(responseStr);
            } else {
                Logger.d(responseStr);
            }
            RetCode retCode = null;
            /*当响应结果为HTLM页面内容时，作加载失败处理*/
            if (responseStr.contains("html")
                    && responseStr.contains("head")
                    && responseStr.contains("body")) {
                retCode = RetCode.unKnow;
                onFailed(retCode);
                observer.finish(observer, retCode);
                return;
            }
            /*成功响应处理*/
            RespBean<String> temp = JSON.parseObject(
                    responseStr, new TypeReference<RespBean<String>>() {
                    }
                            .getType()
            );
            retCode = HttpConfig.getReturnCode(temp.getRtnCode());
            if (retCode == RetCode.success || retCode == RetCode.noData) {
                RespBean<T> respBean = JSON.parseObject(responseStr, type);
                if (respBean != null) {
                    onResponse(retCode, respBean);
                } else {
                    Logger.d("响应数据解析异常...");
                }
            } else if (retCode == RetCode.loginIllegal) {
                observer.loginIllegal();
            } else {
                onFailed(retCode);
            }
            observer.finish(observer, retCode);
        } else {
            observer.finish(observer, RetCode.unKnow);
        }
    }

    @Override
    protected void parseFailed(ResponseObserver observer, String requestUrl, Header[] headers, Throwable throwable) {
        StringBuffer headerInfo = new StringBuffer();
        if (headers != null) {
            for (Header header : headers) {
                headerInfo.append("\n" + header.getName() + "=" + header.getValue());
            }
        }
        Logger.d("-----------------header info-----------------" +
                headerInfo.toString() +
                "\n" +
                "requestUrl==>" + requestUrl);
        RetCode retCode = parseFailedInfo(throwable);
        onFailed(retCode);
        observer.finish(observer, retCode);
    }

    /**
     * 请求成功处理
     *
     * @param retCode  响应码
     * @param response 响应解析结果
     */
    public abstract void onResponse(RetCode retCode, RespBean<T> response);

    /**
     * 请求失败处理
     *
     * @param retCode 响应码
     */
    public abstract void onFailed(RetCode retCode);

}
