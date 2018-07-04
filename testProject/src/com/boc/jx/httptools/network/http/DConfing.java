package com.boc.jx.httptools.network.http;

public class DConfing {
//
    public static String ErrorFromNet = "网络错误,请检查网络";//动态修改
    public static String ErrorFromNoData = "暂无数据";//动态修改
    public static String ErrorFromServer = "请求失败,请稍后重试";//动态修改
    public static String NeedReLogin = "账号异常，请重新登录";//NeedReLogin
    public static int TaskBusyCode = 10086;//TaskBusyCode
//    public static String ErrorFromDynamic = "安全认证失败,请稍后重试";// 动态url失败
    public static String FunctionParamKey = "funtions.key";
    public static final String SP_NAME = "DynamicData_sp";
    public static final String ACCESS_TOKEN = "accessToken.key";
    public static final String LOGIN_TOKEN = "loginToken.key";
    public static final String PUBLICK_KEY = "publickKey.key";
    public static final int NOINTNET = 0;//无网络
    public static final int TIMEOUT = 1;//连接超时
    public static final int SERVERERROR = 2;//服务器异常（连接异常，数据解析异常均归为服务器异常）
    public static final int CANCEL = 3;//用户取消
    public static final int ESPECIALCODE = 4;//返回字段里包含配置的特殊码

}
