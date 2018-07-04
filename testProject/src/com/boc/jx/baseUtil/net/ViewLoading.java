package com.boc.jx.baseUtil.net;

/**
 * Created by hwt on 2/9/15.
 * 网络请求加载过程控件描述。
 */
public interface ViewLoading {
    /**
     * 加载中处理
     * @param progress
     */
    public void loading(int progress);

    /**
     * 载入完成但无数据处理
     * @param noDataMsg
     */
    public void noData(String noDataMsg);

    /**
     * 载入完成但数据加载失败处理
     * @param failedMsg
     */
    public void failed(String failedMsg);

    /**
     * 载入完成
     */
    public void loaded();

    /**
     * 判断是否下在载入处理
     * @return
     */
    public boolean isLoading();

    /**
     * 设置重试操作
     * @param onRetryListener
     */
    public void retry(OnRetryListener onRetryListener);

    /**
     * 判断是否已有重试设置
     * @return
     */
    public boolean isHasRetry();

    /**
     * 重试监听
     */
    public interface OnRetryListener {
        /**
         * 实现在请求重试时的操作
         */
        public void retry();
    }
}
