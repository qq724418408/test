package com.boc.jx.baseUtil.net;

import com.boc.jx.baseUtil.asynchttpclient.RequestParams;

import java.util.UUID;

import org.apache.http.Header;

/**
 * Created by hwt on 15/1/19.
 * <p/>
 * 后台对应接口 API
 */
public class Request {
    /**
     * 网络请求工具
     */
    public static HttpRequest httpRequest;
    /**
     * 网络请求数据编码格式
     */
    public static String charset = "utf-8";

    /**
     * HTTP 请求工具参数配置
     */
    private static HttpConfig httpConfig = Controller.getInstance().getHttpConfig();

    public static void registerHttp(HttpRequest request) {
        httpRequest = request;
    }

    public static Http getHttp() {
        return httpRequest.getRequest();
    }

    public static Http queryUserName(RequestParams params, ResponseNotify<String> responseNotify){
    	String requesturl = "http://sdfsdf/queryusername";
    	return getHttp().createRequest(Http.POST, requesturl, null, params, null,getResponseHandler(responseNotify));
    }
    
    /**
     * 通用网络请求（表单提交）
     *
     * @param requestURL     请求路径
     * @param params         请求参数
     * @param responseNotify 请求响应回调
     * @param <T>            响应内容数据对象
     */
    public static <T> Http request(String requestURL, RequestParams params,
                                   ResponseNotify<T> responseNotify) {
        return getHttp().createRequest(Http.POST, requestURL, null, params, null, getResponseHandler(responseNotify));
    }

    /**
     * 上传文件
     *
     * @param uploadUrl
     * @param fileParams
     * @param responseNotify
     * @param <T>
     * @return
     */
    public static <T> Http upload(String uploadUrl, RequestParams fileParams, ResponseNotify<T> responseNotify) {
        return getHttp().createRequest(Http.PUT, uploadUrl, null, fileParams, null, getResponseHandler(responseNotify));
    }

    /**
     * 下载文件
     *
     * @param uploadUrl
     * @param fileParams
     * @param responseNotify
     * @param <T>
     * @return
     */
    public static <T> Http download(String uploadUrl, RequestParams fileParams, ResponseNotify<T> responseNotify) {
        return getHttp().createRequest(Http.GET, uploadUrl, null, fileParams, null, getResponseHandler(responseNotify));
    }



    /**
     * 请求响应设置
     *
     * @param responseNotify
     * @return
     */
    public static <T> HybridTextResponse<T> getResponseHandler(ResponseNotify<T> responseNotify) {
        String uuid = UUID.randomUUID().toString();
        return HybridTextResponse.getJSONResponse(responseNotify, charset).setTag(uuid);
    }


}
