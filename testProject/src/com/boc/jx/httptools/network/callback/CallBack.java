package com.boc.jx.httptools.network.callback;

/**
 * Created by XinQingXia on 2017/8/15.
 */

public interface CallBack {

    void inProgress(double progress);

    void onError(String msg, String e, int code);

    void onEmpty(String msg);

    void loginSuccessed();

    void loginFailed(String msg, int code);

    void onEspecialCode(String code, String response, String msg);
}
