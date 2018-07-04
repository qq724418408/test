package com.boc.jx.httptools.network.callback;

import java.io.File;

/**
 * Created by XinQingXia on 2017/8/16.
 */

public abstract class FileCallBack implements CallBack {
    @Override
    public void inProgress(double progress) {

    }

    @Override
    public void onError(String msg, String e, int code) {

    }

    public abstract void onSuccessed(File file);

    @Deprecated
    @Override
    public void loginSuccessed() {

    }

    @Deprecated
    @Override
    public void loginFailed(String msg, int code) {

    }

    @Deprecated
    @Override
    public void onEspecialCode(String code, String response, String msg) {

    }
}
