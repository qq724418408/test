package com.boc.jx.httptools.network.http.build;

import java.util.HashMap;
import java.util.Map;

import com.boc.jx.httptools.network.Executors;
import com.boc.jx.httptools.network.callback.CallBack;
import com.boc.jx.httptools.network.http.task.SimpleTask;
import com.boc.jx.httptools.network.listener.Progress;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;


/**
 * Created by XinQingXia on 2017/8/13.
 */

public class HttpBuild {

    private Progress progress;
    private Map<String, Object> params;
    private String id;
    private CallBack callback;
    private Context context;
    private boolean checkLoginState;//是否检测登录状态
    private Executors executors;
    public enum Method{
        GET,POST,UPLOAD
    }
    private Method method=Method.POST;


    public HttpBuild(@NonNull Context context, Executors executors) {
        this.context = context;
        this.executors = executors;
    }

    public SimpleTask build() {
        /*SimpleTask simpleTask = new SimpleTask(this);
        simpleTask.send();
        return simpleTask;*/

        return new SimpleTask(this);
    }
    public HttpBuild Mehod(Method method){
        this.method=method;
        return  this;
    }

    public Method getMethod() {
        return method;
    }

    public HttpBuild url(String url) {
        this.id = url;
        return this;
    }

    public HttpBuild progress(Progress progress) {
        this.progress = progress;
        return this;
    }

    public HttpBuild callBack(CallBack httpCallBack) {
        this.callback = httpCallBack;
        return this;
    }

    public HttpBuild addParams(String key, String value) {
        if (params == null)
            params = new HashMap<>();
        if (!TextUtils.isEmpty(key))
            if (value != null)
                params.put(key, value);
        return this;
    }

    public HttpBuild addParams(Map<String, Object> params) {
        if (this.params == null) {
            this.params = params;
        } else {
            this.params.putAll(params);
        }
        return this;
    }

    public HttpBuild checkLogin(boolean check) {
        this.checkLoginState = check;
        return this;
    }

    public boolean isCheckLoginState() {
        return checkLoginState;
    }


    public Map<String, Object> getParams() {
        if (params == null)
            params = new HashMap<>();
        return params;
    }

    public String getId() {
        return id;
    }

    public CallBack getCallback() {
        return callback;
    }

    public Context getContext() {
        return context;
    }

    public Executors getExecutors() {
        return executors;
    }


    /****
     * External calls are not allowed
     * @param urlKey
     */
    @Deprecated
    public void setUrlKey(String urlKey) {
        addParams("key", urlKey).addParams("t", "d");
    }
}
