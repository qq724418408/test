package com.boc.jx.baseUtil.net;

import android.content.Context;

import com.boc.jx.baseUtil.asynchttpclient.AsyncHttpClient;
import com.boc.jx.baseUtil.asynchttpclient.RequestHandle;
import com.boc.jx.baseUtil.logger.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by hwt on 15/1/19.
 */
public class HttpTrans extends Http {
    private Map<Object, Http> httpList = new HashMap<Object, Http>();
    private Map<Object, RequestHandle> requestHandle = new HashMap<Object, RequestHandle>();
    private int requestHandlerNums = 0;
    private boolean isFinished = false;
    private OnFinshListener finshListener;

    /**
     * @param context    应用上下文
     * @param httpConfig
     * @param httpClient 网络加载工具
     */
    private HttpTrans(Context context, HttpConfig httpConfig, AsyncHttpClient httpClient) {
        super(context, httpConfig, httpClient);
    }

    public static HttpTrans getNewHttpTrans(Context context, HttpConfig httpConfig, AsyncHttpClient httpClient) {
        HttpTrans httpTrans = new HttpTrans(context, httpConfig, httpClient);
        httpTrans.type = ResponseObserver.TYPE_TRANSACTION;
        return httpTrans;
    }

    public HttpTrans addRequest(Http request) {
        httpList.put(request.getTag(), request);
        return this;
    }

    /**
     * 开始发送网络请求
     */
    @Override
    protected void sendRequest() {
        requestHandlerNums = httpList.size();
        Set<Map.Entry<Object, Http>> entrySet = httpList.entrySet();
        for (Map.Entry<Object, Http> httpEntry : entrySet) {
            Http http = httpEntry.getValue();
            requestHandle.put(http.getTag(), http.request(this));
        }
    }

    /**
     * 失败重试，重新发送网络请求，注意：些方法是从上次请求失败的位置重新开始。
     */
    @Override
    protected void retryRequest() {
        Set<Map.Entry<Object, RequestHandle>> entrySet = requestHandle.entrySet();
        requestHandlerNums = entrySet.size();
        for (Map.Entry<Object, RequestHandle> httpEntry : entrySet) {
            httpList.get(httpEntry.getKey()).request(this);
        }
    }

    @Override
    public void finish(ResponseObserver observer, RetCode retCode) {
        if (type == ResponseObserver.TYPE_TRANSACTION_2) {
            requestHandle.remove(((Http) observer).getTag());
            requestHandlerNums--;
        } else {
            if (retCode == RetCode.success || retCode == RetCode.noData) {
                requestHandle.remove(((Http) observer).getTag());
                requestHandlerNums--;
            } else {
                requestHandlerNums = 0;
            }
        }
        if (requestHandlerNums == 0) {
            transComplete(retCode);
        }
    }

    /**
     * 網絡請求結束
     *
     * @param retCode
     */
    private void transComplete(RetCode retCode) {
        isFinished = true;
        hideLoadingView(retCode);
        if (finshListener != null) {
            finshListener.onFinish();
        }
    }


    /**
     * 当前网络请求队列是否全部完成
     *
     * @return
     */
    @Override
    public boolean isFinished() {
        return isFinished;
    }

    /**
     * 中断时取消剩余网络请求
     */
    @Override
    public void interupt() {
        Set<Map.Entry<Object, RequestHandle>> entrySet = requestHandle.entrySet();
        for (Map.Entry<Object, RequestHandle> httpEntry : entrySet) {
            httpEntry.getValue().cancel(true);
            Logger.d("httpEntry.key=s%", httpEntry.getKey());
        }
    }


    public interface OnFinshListener {
        void onFinish();
    }

    public void setFinshListener(OnFinshListener finshListener) {
        this.finshListener = finshListener;
    }


}
