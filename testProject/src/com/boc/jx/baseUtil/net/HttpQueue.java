package com.boc.jx.baseUtil.net;

import android.content.Context;

import com.boc.jx.baseUtil.asynchttpclient.AsyncHttpClient;
import com.boc.jx.baseUtil.asynchttpclient.RequestHandle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hwt on 15/1/19.
 */
public class HttpQueue extends Http {

    private List<Http> httpList = new ArrayList<Http>();
    private RequestHandle requestHandle;
    private int mRequestPos = 0;
    private boolean isFinished = false;
    private OnFinshListener finshListener;

    /**
     * @param context    应用上下文
     * @param httpConfig
     * @param httpClient 网络加载工具
     */
    private HttpQueue(Context context, HttpConfig httpConfig, AsyncHttpClient httpClient) {
        super(context, httpConfig, httpClient);
    }

    public static HttpQueue getNewHttpQueue(Context context, HttpConfig httpConfig, AsyncHttpClient httpClient) {
        HttpQueue httpQueue = new HttpQueue(context, httpConfig, httpClient);
        httpQueue.type = TYPE_QUEUE;
        return httpQueue;
    }

    public HttpQueue addRequest(Http request) {
        httpList.add(request);
        return this;
    }

    /**
     * 开始发送网络请求
     */
    @Override
    public void sendRequest() {
        mRequestPos = 0;//重置发送顺序
        if (mRequestPos < httpList.size()) {
            requestHandle = httpList.get(mRequestPos++).request(this);
        }
    }

    /**
     * 失败重试，重新发送网络请求，注意：此方法是从上次请求失败的位置重新开始。
     */
    @Override
    public void retryRequest() {
        if (mRequestPos < httpList.size()) {
            requestHandle = httpList.get(--mRequestPos).request(this);
        }
    }

    @Override
    public void progress(int bytesWritten, int totalSize) {

    }

    @Override
    public void finish(RetCode retCode) {
        if (mRequestPos < httpList.size()) {
            if (retCode == RetCode.success || retCode == RetCode.noData) {
                requestHandle = httpList.get(mRequestPos++).request(this);
            } else {
                requestHandle.cancel(true);
            }
        } else {
            isFinished = true;
            hideLoadingView(retCode);
            if(finshListener!=null){
                finshListener.onFinish();
            }
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
        requestHandle.cancel(true);
    }


    public interface OnFinshListener{
        public void onFinish();
    }

    public void setFinshListener(OnFinshListener finshListener) {
        this.finshListener = finshListener;
    }
}
