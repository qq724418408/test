package com.boc.jx.baseUtil.net;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.widget.Toast;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.baseUtil.asynchttpclient.*;
import com.boc.jx.tools.NetworkUtil;

import org.apache.http.Header;
import org.apache.http.HttpEntity;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by hwt on 1/22/15.
 * <p/>
 * 基础网络请求工具
 */
public abstract class Http extends ResponseObserver {
    public final static int GET = 0;
    public final static int POST = 1;
    public final static int PUT = 2;
    public final static int DELETE = 3;
    public final static int HEAD = 4;

    private int httpType;
    private String url;
    private Header[] headers;
    private RequestParams params;
    private HttpEntity entity;
    private String contentType;
    private HybridTextResponse<?> responseHandler;
    private AsyncHttpClient mHttpClient;
    private Context mContext;
    private HttpConfig mHttpConfig;
    private Object tag;
    private boolean isShowDialog = true;
    private boolean isCancelable = true;
    private Dialog progress;
    private ViewLoading loadingView;
    private PersistentCookieStore mCookieStore;

    /**
     * @param context    应用上下文
     * @param httpConfig
     * @param httpClient 网络加载工具
     */
    protected Http(Context context, HttpConfig httpConfig, AsyncHttpClient httpClient) {
        mContext = context;
        mHttpClient = httpClient;
        mHttpConfig = httpConfig;
        mHttpClient.setTimeout(httpConfig.getHttpTimeout());
        mHttpClient.setMaxRetriesAndTimeout(httpConfig.getHttpRetry(), httpConfig.getHttpTimeout());
        progress = httpConfig.getProgressDialog();
    }

    /**
     * @param httpType        请求类型
     * @param url             请求路径
     * @param headers         请求头信息
     * @param entity          请求参数
     * @param contentType     数据类型
     * @param responseHandler 请求响应
     * @return
     */
    public Http createRequest(int httpType,
                              String url,
                              Header[] headers,
                              HttpEntity entity,
                              String contentType,
                              HybridTextResponse<?> responseHandler) {
        this.httpType = httpType;
        this.url = url;
        this.headers = headers;
        this.entity = entity;
        this.contentType = contentType;
        this.responseHandler = responseHandler;
        this.responseHandler.setHttp(this);
        this.tag = UUID.randomUUID().toString();
        this.mCookieStore = new PersistentCookieStore(getContext());
        this.mHttpClient.setCookieStore(mCookieStore);
        return this;
    }

    /**
     * @param httpType        请求响应
     * @param url             请求响应
     * @param headers         请求响应
     * @param params          请求响应
     * @param contentType     数据类型
     * @param responseHandler 请求响应
     * @return
     */
    public Http createRequest(int httpType,
                              String url,
                              Header[] headers,
                              RequestParams params,
                              String contentType,
                              HybridTextResponse<?> responseHandler) {

        this.httpType = httpType;
        this.url = url;
        this.headers = headers;
        this.params = params;
        this.contentType = contentType;
        this.responseHandler = responseHandler;
        this.responseHandler.setHttp(this);
        this.tag = UUID.randomUUID().toString();
        this.mCookieStore = new PersistentCookieStore(getContext());
        this.mHttpClient.setCookieStore(mCookieStore);
        return this;
    }

    /**
     * @param httpType        请求类型 POST GET...
     * @param url             请求路径
     * @param headers         请求头信息
     * @param params          请求参数
     * @param responseHandler 请求响应
     * @return
     */
    public Http createRequest(int httpType,
                              String url,
                              Header[] headers,
                              RequestParams params,
                              HybridTextResponse<?> responseHandler) {
        this.httpType = httpType;
        this.url = url;
        this.headers = headers;
        this.params = params;
        this.responseHandler = responseHandler;
        this.responseHandler.setHttp(this);
        this.tag = UUID.randomUUID().toString();
        this.mCookieStore = new PersistentCookieStore(getContext());
        this.mHttpClient.setCookieStore(mCookieStore);
        return this;
    }

    public PersistentCookieStore getCookieStore() {
        return mCookieStore;
    }

    public void setCookieStore(PersistentCookieStore cookieStore) {
        mCookieStore = cookieStore;
        mHttpClient.setCookieStore(mCookieStore);
    }

    /**
     * 发送已配置HTTP请求
     *
     * @return
     */
    public RequestHandle request(ResponseObserver observer) {
        responseHandler.setObserver(observer);
        return request(httpType, url, headers, params, entity, contentType, responseHandler);
    }

    /**
     * @param httpType        请求类型
     * @param url             请求路径
     * @param headers         请求头信息
     * @param params          请求参数
     * @param entity          请求参数
     * @param contentType     数据类型
     * @param responseHandler 请求响应
     * @return
     */
    public RequestHandle request(int httpType,
                                 String url,
                                 Header[] headers,
                                 RequestParams params,
                                 HttpEntity entity,
                                 String contentType,
                                 ResponseHandlerInterface responseHandler) {
        if (params != null) {
            entity = paramsToEntity(params, responseHandler);
        }

        switch (httpType) {
            case GET:
                return mHttpClient.get(mContext, url, headers, params, responseHandler);
            case POST:
                if (headers == null) {
                    return mHttpClient.post(mContext, url, entity, contentType, responseHandler);
                } else {
                    return mHttpClient.post(mContext, url, headers, entity, contentType, responseHandler);
                }
            case DELETE:
                return mHttpClient.delete(mContext, url, headers, params, responseHandler);
            case PUT:
                return mHttpClient.put(mContext, url, headers, entity, contentType, responseHandler);
            case HEAD:
                return mHttpClient.head(mContext, url, headers, params, responseHandler);
            default:
                return null;
        }
    }

    /**
     * Returns HttpEntity containing data from RequestParams included with request declaration.
     * Allows also passing progress from upload via provided ResponseHandler
     *
     * @param params          additional request params
     * @param responseHandler ResponseHandlerInterface or its subclass to be notified on progress
     */
    private HttpEntity paramsToEntity(RequestParams params, ResponseHandlerInterface responseHandler) {
        HttpEntity entity = null;

        try {
            if (params != null) {
                entity = params.getEntity(responseHandler);
            }
        } catch (IOException e) {
            if (responseHandler != null) {
                responseHandler.sendFailureMessage(0, null, null, e);
            } else {
                e.printStackTrace();
            }
        }

        return entity;
    }

    /**
     * 开始请求
     */
    @Override
    public void start() {
        this.isShowDialog = true;
        this.isCancelable = true;
        start(true, true);
    }

    /**
     * 开始网络请求
     *
     * @param isShow
     */
    @Override
    public void start(boolean isShow) {
        this.isShowDialog = isShow;
        this.isCancelable = true;
        start(isShow, true);
    }

    /**
     * 开始网络请求
     *
     * @param isShow       是否显示载入提示控件
     * @param isCancelable 如为 dialog，控制dialog 是否可被取消
     */
    @Override
    public void start(boolean isShow, boolean isCancelable) {
    	this.isShowDialog = isShow;
        this.isCancelable = isCancelable;
        if (NetworkUtil.isNetworkAvailable(getContext())) {
//            getHttpClient().cancelAllRequests(true);
            showLoadingView();
            sendRequest();
        } else {
            Toast.makeText(getContext(), RetCode.netError.getRetMsg(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 重试当前请求
     */
    public void retry() {
        if (NetworkUtil.isNetworkAvailable(getContext())) {
            showLoadingView();
            retryRequest();
        } else {
            Toast.makeText(getContext(), RetCode.netError.getRetMsg(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 加载进度，当 activity stop 时中断网络请求
     *
     * @param bytesWritten
     * @param totalSize
     */
    @Override
    protected void progress(int bytesWritten, int totalSize) {
        if (getContext() instanceof BaseActivity) {
            BaseActivity activity = (BaseActivity) getContext();
            if (activity.getState() == BaseActivity.ON_STOPED ||
                    activity.getState() == BaseActivity.ON_DISTORY) {
                interupt();
            }
        }
    }

    /**
     * 网络请求完成
     *
     * @param retCode
     */
    @Override
    protected void finish(RetCode retCode) {

    }

    /**
     * 网络请求完成
     *
     * @param observer 请求响应监听
     * @param retCode  响应码对象
     */
    @Override
    protected void finish(ResponseObserver observer, RetCode retCode) {
        finish(retCode);
    }

    /**
     * @return
     */
    public Context getContext() {
        return mContext;
    }

    public HttpConfig getHttpConfig() {
        return mHttpConfig;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;

    }

    public AsyncHttpClient getHttpClient() {
        return mHttpClient;
    }

    /**
     * 显示载入提示
     */
    protected void showLoadingView() {
        if (isShowDialog) {
            if (loadingView == null) {
                if (progress != null) {
                    if (isCancelable) {
                        progress.setOnCancelListener(onCancelListener);
                    }
                    progress.show();
                } else {
                    progress = showProgress(getContext(), null, getHttpConfig().getLoading(),
                            isCancelable, onCancelListener);
                }
            } else {
                loadingView.loading(0);
                /**
                 * 设置重试操作
                 */
                if (!loadingView.isHasRetry()) {
                    loadingView.retry(new ViewLoading.OnRetryListener() {
                        @Override
                        public void retry() {
                            Http.this.retry();
                        }
                    });
                }
            }
        }
    }

    /**
     * 加载框取消时监听
     */
    private DialogInterface.OnCancelListener onCancelListener = new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
            interupt();
            dialog.dismiss();
        }
    };

    /**
     * 结束当前加载提示。
     * <br/>
     * 1.实现 loadingView 的特殊加载控件
     * <br/>
     * 2.progressDialog
     *
     * @param retCode
     */
    protected void hideLoadingView(RetCode retCode) {
        if (loadingView != null && loadingView.isLoading()) {
            switch (retCode) {
                case success:
                    loadingView.loaded();
                    break;
                case noData:
                    loadingView.noData(retCode.getRetMsg());
                    break;
                default:
                    loadingView.failed(retCode.getRetMsg());
                    break;
            }
        } else if (progress != null && progress.isShowing()) {
            progress.dismiss();
        }
    }

    public ViewLoading getLoadingView() {
        return loadingView;
    }

    public Http setLoadingView(ViewLoading loadingView) {
        this.loadingView = loadingView;
        return this;
    }

    public Dialog getProgress() {
        return progress;
    }

    public void setProgress(ProgressDialog progress) {
        this.progress = progress;
    }

    /**
     * 弹出加载框
     *
     * @param context        应用上下文
     * @param title          标题
     * @param message        内容
     * @param isCancelable   是否可被取消
     * @param cancelListener 监听取消操作
     * @return
     */
    public static ProgressDialog showProgress(Context context, String title, String message, boolean isCancelable, DialogInterface.OnCancelListener cancelListener) {
        ProgressDialog progress = new ProgressDialog(context);
        progress.getWindow().setBackgroundDrawable(new BitmapDrawable());
        progress.setTitle(title);
        progress.setMessage(message);
        progress.setCancelable(isCancelable);
        progress.setCanceledOnTouchOutside(false);
        if (isCancelable && cancelListener != null) {
            progress.setOnCancelListener(cancelListener);
        }
        progress.show();
        return progress;
    }

    /**
     * 网络请求被取消时隐藏加载控件
     *
     * @param request
     */
    @Override
    protected void onCancel(Http request) {
        if (loadingView != null && loadingView.isLoading()) {
            loadingView.failed("加载失败，请稍后重试。");
        } else if (progress != null && progress.isShowing()) {
            progress.dismiss();
        }


    }

    @Override
    public String getUrl() {
        return url;
    }

    /**
     * 判断是否正在加载中
     *
     * @return
     */
    public boolean isShowLoading() {
        return isShowDialog ||
                (loadingView != null && loadingView.isLoading()) ||
                (progress != null && progress.isShowing());
    }

    /**
     * 判断请求是否已完成
     *
     * @return
     */
    public abstract boolean isFinished();

    /**
     * 子类继承实现，中断所创建的网络请求
     */
    public abstract void interupt();

    /**
     * 子类继承实现，在启动网络请求时调用
     */
    protected abstract void sendRequest();

    /**
     * 子类继承实现，重新发送一次请求
     */
    protected abstract void retryRequest();

    @Override
    protected void loginIllegal() {

    }


}
