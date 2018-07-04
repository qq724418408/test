package com.boc.jx.baseUtil.net;

/**
 * Created by hwt on 14/11/4.
 */
public enum RetCode {
    success("10000", "success"),
    noData("00002","查无数据"),

    netError("F00001", "网络异常"),
    timeOut("F00002", "请求超时"),
    unKnow("F00003","加载失败，请稍后重试"),
    
    oldPswdError("60001","输入的旧密码有误"),
    exist("60011","已存在"),
    authCodeError("60014","输入的验证码有误"),
    loginFailed("60017","用户名或密码错误"),
    checkinError("60020","已经签过到了"),
    loginIllegal("60021","登录失效");

    private String code;
    private String retMsg;

    private RetCode(String code, String retMsg) {
        this.code = code;
        this.retMsg = retMsg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRetMsg() {
        return retMsg;
    }

    public void setRetMsg(String retMsg) {
        this.retMsg = retMsg;
    }
}
