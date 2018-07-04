package com.boc.jx.baseUtil.net;

import com.boc.jx.baseUtil.asynchttpclient.TextHttpResponseHandler;
import com.boc.jx.baseUtil.logger.Logger;
import org.apache.http.Header;

/**
 * Created by hwt on 14/11/3.
 * 网络请求响应封装
 */
public class HybridTextResponse<RESPONSE_TYPE> extends TextHttpResponseHandler {
    private int mRequestType = 0;//请求类型
    private ResponseNotify<RESPONSE_TYPE> mResponseNotify;//网络请求响应通知
    private Http mRequest;//网络请求工具
    private ResponseObserver observer;//响应过程监听
    private Object tag;

    public final static int JSON_TYPE = 0;
    public final static int XML_TYPE = 1;

    /**
     * 当前进度
     *
     * @param bytesWritten 已响应字节数
     * @param totalSize    总字节数
     */
    public void notifyProgress(int bytesWritten, int totalSize) {
        if (observer != null) {
            observer.progress(bytesWritten, totalSize);
        }
    }

    /**
     * 请求被取消
     */
    private void notifyCancel() {
        if (observer != null) {
            observer.onCancel(mRequest);
        }
    }

    /**
     * 构造方法
     *
     * @param requestType    响应内容类型JSON_TYPE、XML_TYPE
     * @param responseNotify 请求响应
     * @param encoding       响应数据编码
     */
    @SuppressWarnings("unchecked")
    private HybridTextResponse(int requestType, ResponseNotify<RESPONSE_TYPE> responseNotify, String encoding) {
        super(encoding);
        mRequestType = requestType;
        mResponseNotify = responseNotify;
    }

    /**
     * 创建请求响应解析
     *
     * @param responseNotify 请求响应
     * @param encoding       响应数据编码
     * @param <T>            响应 BEAN 类型
     * @return
     */
    public static <T> HybridTextResponse<T> getJSONResponse(
            ResponseNotify<T> responseNotify,
            String encoding) {
        return new HybridTextResponse<T>(JSON_TYPE, responseNotify, encoding);
    }

    /**
     * 创建请求响应解析
     *
     * @param responseNotify 请求响应
     * @param encoding       响应数据编码
     * @param <T>            响应 BEAN 类型
     * @return
     */
    public static <T> HybridTextResponse<T> getXMLResponse(
            ResponseNotify<T> responseNotify,
            String encoding) {
        return new HybridTextResponse<T>(XML_TYPE, responseNotify, encoding);
    }

    /**
     * 网络加载失败
     *
     * @param statusCode   HTTP响应码
     * @param headers      请求头信息
     * @param responseBody 请求响应数据
     * @param throwable
     */
    @Override
    public void onFailure(final int statusCode, final Header[] headers, String responseBody, Throwable throwable) {
        if (mResponseNotify != null)
            mResponseNotify.parseFailed(observer, getRequestURI().toString(), headers, throwable);
    }

    /**
     * 成功响应
     *
     * @param statusCode   HTTP响应码
     * @param headers      请求头信息
     * @param responseBody 请求响应数据
     */
    @Override
    @SuppressWarnings("unchecked")
    public void onSuccess(int statusCode, Header[] headers, String responseBody) {
        switch (mRequestType) {
            case JSON_TYPE:
                /*设置响应处理方式*/
                if (mResponseNotify != null)
                    mResponseNotify.parseResponse(observer, getRequestURI().toString(), headers, responseBody);
                break;
            case XML_TYPE:
                break;
        }
    }

    @Override
    public void onCancel() {
        super.onCancel();
        notifyCancel();
        Logger.d("取消请求 request URL=s%", getRequestURI());
    }

    @Override
    public void onProgress(int bytesWritten, int totalSize) {
        notifyProgress(bytesWritten, totalSize);
        super.onProgress(bytesWritten, totalSize);
    }

    public Object getTag() {
        return tag;
    }

    public HybridTextResponse<RESPONSE_TYPE> setTag(Object tag) {
        this.tag = tag;
        return this;
    }

    public Object getHttpTag() {
        return mRequest.getTag();
    }

    public void setHttp(Http request) {
        this.mRequest = request;
    }

    public void setObserver(ResponseObserver observer) {
        this.observer = observer;
    }
}
