package com.boc.jx.httptools.network.callback;

/**
 * 动态url 解耦合专用
 *
 * @author 夏新庆
 */
public abstract class HttpUtilsCallBak {

    public abstract void onSuccess(String response);

    public abstract void onError(String errorMsg, String e, int code);

}
