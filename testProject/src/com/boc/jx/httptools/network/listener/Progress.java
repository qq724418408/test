package com.boc.jx.httptools.network.listener;

/**
 * Created by xiaxinqing on 2017/4/28.
 */

public interface Progress {
    /**
     * 网络失败
     *
     * @param string
     */
//    void netWorkFaild(String string);

    /**
     * 其他错误（server/json）
     */
    void faild(int errorCode);//关闭view

    /**
     * 无数据
     */
    void dataEmpty();//关闭view

    /**
     * show
     */
    void show();//显示view

    /**
     * dismiss
     */
    void dismiss();//关闭view

    /**
     * 添加重试监听
     *
     * @param retry
     */
    void addRetryListener(DynamicRetry retry);

    /**
     * 获取重试监听
     *
     * @return
     */
    DynamicRetry getRetryListener();

    /**
     * 设置tag 这里用于取消网络
     * 控制器中调用
     *
     * @param tag
     */
    void setTag(Object tag);

    /**
     * tag
     *
     * @return
     */
    Object getTag();
}
