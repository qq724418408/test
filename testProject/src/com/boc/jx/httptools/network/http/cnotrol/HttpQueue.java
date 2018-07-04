package com.boc.jx.httptools.network.http.cnotrol;

import java.util.ArrayList;

import com.boc.jx.httptools.network.Executors;
import com.boc.jx.httptools.network.http.DConfing;
import com.boc.jx.httptools.network.http.HttpUtils;
import com.boc.jx.httptools.network.http.build.HttpBuild;
import com.boc.jx.httptools.network.listener.Progress;
import com.boc.jx.httptools.network.util.LogUtil;
import com.boc.jx.tools.LogUtils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;



/**
 * Created by xiaxinqing on 2017/6/27.
 * 动态流程队列管理
 */

public class HttpQueue {


    private ArrayList<HttpBuild> httpList;
    private static HttpQueue dmanager;

    private HttpQueue() {
        httpList = new ArrayList<>();
    }

    public static HttpQueue getInstance() {
        synchronized (HttpQueue.class) {
            if (dmanager == null)
                dmanager = new HttpQueue();
        }
        return dmanager;
    }

    public void addRequest(HttpBuild build) {
//        if (httpList.size() > 0) {
//            boolean canAdd = true;
//            for (int i = 0; i < httpList.size(); i++) {
//                HttpBuild https = httpList.get(i);
//                if (https.getId().equals(build.getId()))
//                    canAdd = false;
//            }
//            if (canAdd) {
//                httpList.add(0, build);
//                LogUtil.e("addRequest() called with: http = [" + build + "]" + "--" + httpList.size());
//            }
//        } else {
        httpList.add(0, build);//删除校验方法，允许多个相同id 同时进行操作
        LogUtils.e("addRequest() called with: http = [" + build + "]" + "--" + httpList.size());
//        }
    }

    /**
     * 继续执行下一个
     *
     * @return true 继续下一个，false 队列执行完毕
     */
    public boolean next() {
        LogUtil.e("开始检查队列是否存在未执行请求");
        if (canNext()) {
            HttpBuild build = httpList.remove(0);
            Executors.getInstance().postAsync(build);
            return true;
        }
        return false;
    }

    /**
     * 继续执行下一个
     *
     * @return true 继续下一个，false 队列执行完毕|未找到改ID
     */
    public boolean next(String urlID) {
        LogUtil.e("执行-->" + urlID);
        if (canNext()) {
            for (int i = 0; i < httpList.size(); i++) {
                HttpBuild https = httpList.get(i);
                if (https.getId().equals(urlID)) {
                    Executors.getInstance().postAsync(https);
                    httpList.remove(https);
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    /**
     * 取消该请求，并且显示失败/重试框（或者隐藏）
     */
    public void removeHttp(String urlId) {
        LogUtil.e("清除指定请求-->" + urlId);
        Executors.getInstance().cancelThis(urlId);
        if (httpList.size() > 0) {
            for (int i = 0; i < httpList.size(); i++) {
                HttpBuild https = httpList.get(i);
                if (https.getId().equals(urlId)) {
                    /*if (https.getProgress() != null) {
                        showOnFailed(https.getProgress(), DConfing.CANCEL);
                        https.getProgress().setTag(null);
                    }*/
                    httpList.remove(https);
                    LogUtil.e("清除-->" + urlId);
                    cancelRequest(urlId);
                    break;
                }
            }
        }
    }

    /**
     * 取消队列不显示失败框
     *
     * @param urlID
     */
    public void deleteHttp(String... urlID) {
        LogUtil.e("清除指定请求-->" + urlID.toString());
        for (int i = 0; i < urlID.length; i++) {
            String url = urlID[i];
            Executors.getInstance().cancelThis(url);
            Log.e("TAG", "delete--start--" + url);
            if (httpList.size() > 0) {
                outer:
                for (int j = 0; j < httpList.size(); j++) {
                    HttpBuild https = httpList.get(j);
                    if (https.getId().equals(url)) {
//                            showOnFailed(https.getProgress());
                     /*   if (https.getProgress() != null)
                            https.getProgress().setTag(null);*/
                        httpList.remove(https);
                        cancelRequest(url);
                        LogUtil.e("delete--end--" + url);
                        break outer;
                    }
                }
            }
        }
    }

    /**
     * 取消所有队列请求，并且显示失败/重试框（或者隐藏）
     */
    public void deleteAllHttp() {
        LogUtil.e("开始清空队列，并且取消当前请求");
        Executors.getInstance().cancelThis();
        if (httpList.size() > 0) {
            for (int i = 0; i < httpList.size(); i++) {
                HttpBuild https = httpList.get(i);
              /*  if (https.getProgress() != null)
                    https.getProgress().setTag(null);*/
                cancelRequest(https.getId());
            }
            httpList.clear();
            LogUtil.e("清除队列" + httpList.size());
        }
    }

    public synchronized void deleteHttp(Activity activity) {
        if (httpList != null && !httpList.isEmpty()) {
            for (int i = 0; i < httpList.size(); i++) {
                HttpBuild build = httpList.get(i);
                Context buildContext = build.getContext();
                if (buildContext instanceof Activity) {
                    Activity mActivity = (Activity) buildContext;
                    if (activity == mActivity) {
                        LogUtil.e(build.getId() + "--移除");
                        httpList.remove(build);
                        --i;
                    }
                }
            }
        }
        HttpBuild httpBuild = Executors.getInstance().getBuild();
        if (httpBuild == null)
            return;
        Context ctx = httpBuild.getContext();
        if (ctx instanceof Activity) {
            Activity mActivity = (Activity) ctx;
            if (activity == mActivity) {
                LogUtil.e("取消正在执行的任务");
                Executors.getInstance().cancelThis();
            }
        }
    }

    /**
     * 取消所有队列请求，并且显示失败/重试框（或者隐藏）
     */

    public void removeAllHttp() {
        LogUtil.e("开始清空队列，并且取消当前请求");
        Executors.getInstance().cancelThis();
        if (httpList.size() > 0) {
            for (int i = 0; i < httpList.size(); i++) {
                HttpBuild https = httpList.get(i);
               /* if (https.getProgress() != null) {
                    showOnFailed(https.getProgress(), DConfing.CANCEL);
                    https.getProgress().setTag(null);
                }*/
                cancelRequest(https.getId());
            }
            httpList.clear();
        }
    }

    private void showOnFailed(Progress progress, int code) {
        if (progress != null)
            progress.faild(code);
    }

    private boolean canNext() {
        if (httpList != null && httpList.size() > 0) {
            LogUtil.e("队列剩余--" + (httpList.size() - 1));
            return true;
        }
        Executors.getInstance().setBuild(null);
        LogUtil.e("执行完毕");
        return false;
    }

    public int getHttpNum() {
        return httpList.size() > 0 ? httpList.size() : 0;
    }

    private void cancelRequest(String urlID) {
    	for (HttpBuild httpBuild : httpList) {
    		httpBuild.getCallback().onError(DConfing.ErrorFromServer, DConfing.ErrorFromServer, DConfing.CANCEL);
		}
        HttpUtils.cancelRequest(urlID);
    }
}
