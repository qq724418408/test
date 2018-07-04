package com.boc.jx.baseUtil.net;

import android.app.Dialog;
import android.app.ProgressDialog;

import java.util.HashMap;

/**
 * Created by hwt on 15/1/20.
 * HTTP请求相关参数配置
 */
public class HttpConfig {
    private String protocal = HTTP_PROTOCAL; //网络协议
    private String domain ;//服务器地址
    private int port = 80;//网络端口
    private String context;//根目录
    private String charset = "UTF-8";
    private String loading = "加载中...";
    private int httpTimeout = 10000;
    private int httpRetry = 2;
    private int maxConnects = 5;
    private Dialog progressDialog;


    public static HashMap<String, RetCode> RETCODE_MAP = null;
    public final static String HTTP_PROTOCAL = "http";
    public final static String HTTPS_PROTOCAL = "https";

    /**
     * 初始化响应码
     *
     * @param RETCODE_MAP
     */
    public static void initRetCode(HashMap<String, RetCode> RETCODE_MAP) {
        HttpConfig.RETCODE_MAP = RETCODE_MAP;
    }

    public static RetCode getReturnCode(String code) {
        RetCode retCode = RETCODE_MAP.get(code);
        if (retCode == null) return RetCode.unKnow;
        return retCode;
    }

    public String getProtocal() {
        return protocal;
    }

    /**
     * 网络协议
     *
     * @param protocal
     * @return
     */
    public HttpConfig setProtocal(String protocal) {
        this.protocal = protocal;
        return this;
    }

    public String getDomain() {
        return domain;
    }

    /**
     * 服务器地址
     *
     * @param domain
     * @return
     */
    public HttpConfig setDomain(String domain) {
        this.domain = domain;
        return this;
    }

    public int getPort() {
        return port;
    }

    /**
     * 网络端口
     *
     * @param port
     * @return
     */
    public HttpConfig setPort(int port) {
        this.port = port;
        return this;
    }

    /**
     * @return
     */
    public String getContext() {
        return context;
    }

    /**
     * 设置服务器根目录
     *
     * @param context
     */
    public void setContext(String context) {
        this.context = context;
    }

    public String getCharset() {
        return charset;
    }

    public HttpConfig setCharset(String charset) {
        this.charset = charset;
        return this;
    }

    /**
     * 返回服务器路径
     *
     * @return
     */
    public String getHttpURI() {
        return protocal + "://" + domain + ":" + port;
    }

    public void setLoading(String loading) {
        this.loading = loading;
    }

    public String getLoading() {
        return loading;
    }

    public int getHttpTimeout() {
        return httpTimeout;
    }

    public HttpConfig setHttpTimeout(int httpTimeout) {
        this.httpTimeout = httpTimeout;
        return this;
    }

    public int getHttpRetry() {
        return httpRetry;
    }

    public HttpConfig setHttpRetry(int httpRetry) {
        this.httpRetry = httpRetry;
        return this;
    }

    public int getMaxConnects() {
        return maxConnects;
    }

    public void setMaxConnects(int maxConnects) {
        this.maxConnects = maxConnects;
    }

    public Dialog getProgressDialog() {
        return progressDialog;
    }

    public void setProgressDialog(Dialog progressDialog) {
        this.progressDialog = progressDialog;
    }
}
