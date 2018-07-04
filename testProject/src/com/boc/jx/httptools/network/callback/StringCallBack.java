package com.boc.jx.httptools.network.callback;


/**
 * Created by XinQingXia on 2017/8/14.
 */

public abstract class StringCallBack implements CallBack {

    @Override
    public void inProgress(double progress) {

    }

    @Override
    public void onError(String msg, String e, int code) {

    }

    public void onResponse(String response) {

    }

    @Override
    public void onEmpty(String msg) {

    }

    @Override
    public void loginSuccessed() {
    }
   
    @Override
    public void loginFailed(String msg, int code) {//需要accesstoken 登录，并且登录失败
//        SpreaditApplication application = ContextUtil.getApplication();
//        application.startActivity(new Intent(application, LoginActivity.class).addFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK));
    }

    public void onEspecialCode(String code, String response, String msg) {
    }
}
