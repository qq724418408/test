package com.boc.jx.httptools.network.callback;


import com.alibaba.fastjson.TypeReference;

import java.lang.reflect.Type;


/**
 * Created by XinQingXia on 2017/8/14.
 */

public abstract class HttpCallBack<T> extends TypeReference implements CallBack {

    @Override
    public void inProgress(double progress) {
    }

    @Override
    public abstract void onError(String request, String msg, int code);

    public abstract void onResponse(T response);

    @Override
    public void onEmpty(String msg) {

    }

    @Override
    public void onEspecialCode(String code, String response, String msg) {
    }

    @Override
    public void loginFailed(String msg, int code) {

    }

    @Override
    public void loginSuccessed() {

    }

    @Override
    public Type getType() {
        return super.getType();
    }
}
