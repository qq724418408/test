package com.boc.jx.httptools.network.listener;

import com.boc.jx.httptools.network.http.cnotrol.HttpQueue;
import com.boc.jx.httptools.network.util.LogUtil;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;


/**
 * Created by XinQingXia on 2017/8/17.
 */

public final class HttpQueueListener implements Application.ActivityLifecycleCallbacks {

    private static HttpQueueListener queueListener;

    private HttpQueueListener() {
    }

    public static HttpQueueListener getIntance() {
        synchronized (HttpQueueListener.class) {
            if (queueListener == null) {
                queueListener = new HttpQueueListener();
            }
        }
        return queueListener;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        LogUtil.e("onActivityDestroyed");
        HttpQueue.getInstance().deleteHttp(activity);
    }
}
