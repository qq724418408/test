package com.boc.jx.baseUtil.net;

/**
 * Created by hwt on 15/1/19.
 * 请求过程监视
 */
public abstract class ResponseObserver {
    public final static int TYPE_REQUEST = 1; //单个请求
    public final static int TYPE_TRANSACTION = 2; //并发请求
    public final static int TYPE_TRANSACTION_2 = 20; //并发请求
    public final static int TYPE_QUEUE = 3;//顺序请求
    public final static int TYPE_UPUDATE = 4;//上传
    public final static int TYPE_DOWNLOAD = 5;//下载

    public int type = TYPE_REQUEST;

    protected abstract void start();//请求开始

    protected abstract void start(boolean isShow);//请求开始

    protected abstract void start(boolean isShow, boolean isCancelable);//请求开始

    protected abstract void progress(int bytesWritten, int totalSize);//请求进度

    protected abstract void finish(RetCode retCode);//请求结束

    protected abstract void finish(ResponseObserver observer, RetCode retCode);//请求结束

    protected abstract void loginIllegal();//登录超时处理

    protected abstract void onCancel(Http request);

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public abstract String getUrl();
}
