package com.boc.jx.baseUtil.net;

/**
 * Created by hwt on 14/11/3.
 * 网络请求 JSON 格式通用 BEAN 类
 */
public class RespBean<T> {

    protected String RtnCode; //响应码
    protected String ReturnDescription; //响应状态描述
    protected T Content; //响应解析数据对象

    public RespBean() {

    }

    public RespBean(String returnCode, String returnDesc) {
        this.RtnCode = returnCode;
        this.ReturnDescription = returnDesc;
    }

    public RespBean(RetCode retCode) {
        this.RtnCode = retCode.getCode();
        this.ReturnDescription = retCode.getRetMsg();
    }

    public String getRtnCode() {
        return RtnCode;
    }

    public void setRtnCode(String rtnCode) {
        RtnCode = rtnCode;
    }

    public String getReturnDescription() {
        return ReturnDescription;
    }

    public void setReturnDescription(String returnDescription) {
        ReturnDescription = returnDescription;
    }

    public T getContent() {
        return Content;
    }

    public void setContent(T content) {
        Content = content;
    }

    @Override
    public String toString() {
        return "RespBean{" +
                "RtnCode='" + RtnCode + '\'' +
                ", ReturnDescription='" + ReturnDescription + '\'' +
                ", Content=" + Content +
                '}';
    }
}
