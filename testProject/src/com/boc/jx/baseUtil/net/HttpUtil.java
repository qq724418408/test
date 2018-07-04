package com.boc.jx.baseUtil.net;

import com.boc.jx.baseUtil.asynchttpclient.AsyncHttpClient;

/**
 * Created by hwt on 15/1/19.
 * HTTP请求创建工具
 */
public class HttpUtil {
    private static HttpUtil httpUtil = null;
    private AsyncHttpClient client = null;    //实例话对象

    /**
     * 初始化请求端口数据
     *
     * @param httpConfig
     * @return
     */
    public HttpUtil init(HttpConfig httpConfig) {
        if (client == null) {
            if (httpConfig != null) {
                if ("https".equalsIgnoreCase(httpConfig.getProtocal())) {
                    client = new AsyncHttpClient(80, httpConfig.getPort());
                } else if ("http".equalsIgnoreCase(httpConfig.getProtocal())) {
                    client = new AsyncHttpClient(httpConfig.getPort());
                }
                client.setTimeout(httpConfig.getHttpTimeout());
            } else {
                client = new AsyncHttpClient();
            }
        }
        return this;
    }

    /**
     * 获取实例
     *
     * @return
     */
    public static HttpUtil getInstance() {
        if (httpUtil == null) {
            httpUtil = new HttpUtil().init(null);
        }
        return httpUtil;
    }

    /**
     * 获取实例
     *
     * @param httpConfig
     * @return
     */
    public static HttpUtil getInstance(HttpConfig httpConfig) {
        if (httpUtil == null) {
            httpUtil = new HttpUtil().init(httpConfig);
        }
        return httpUtil;
    }

    /**
     * 返回网络请求工具
     *
     * @return
     */
    public AsyncHttpClient getClient() {
        return httpUtil.client;
    }


}
