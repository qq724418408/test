package com.boc.jx.httptools.network.http.build;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.boc.jx.httptools.network.Executors;
import com.boc.jx.httptools.network.http.cnotrol.DyanmicData;

import android.text.TextUtils;



/**
 * Created by XinQingXia on 2017/8/14.
 */

public class FunctionBuild {
    private final Executors instance;
    private ArrayList<String> exceptionCodeList;//出现此异常，直接做登出操作
    private ArrayList<String> otherCodeList;//显示失败页面
    private String logOutId;// 自动登出url
    private String loginTokenId;//自动登录url
    private boolean loginState = false;//登录状态

    private String serverUrl;
    private String serverRootPath;
    private DyanmicData dyanmicData;


    public FunctionBuild(Executors instance) {
        this.instance = instance;
        this.dyanmicData = instance.getDyanmicData();
    }

    public FunctionBuild server(String server, String serverRootPath) {
        this.serverUrl = server;
        this.serverRootPath = serverRootPath;
        dyanmicData.setFunctions(null);
        return this;
    }

    public FunctionBuild defaultLog(boolean logState) {
//        LogUtil.setDefaultLog(logState);
        return this;
    }

    public FunctionBuild autoLog(boolean logState) {
//        LogUtil.setAutoLog(logState);
        return this;
    }

    public FunctionBuild addParam(String key, String value) {
        if (dyanmicData.getfParam() == null)
            dyanmicData.setfParam(new HashMap<String, String>());
        if (!TextUtils.isEmpty(key))
            if (value != null)
                dyanmicData.getfParam().put(key, value);
        return this;
    }

    public FunctionBuild addParams(Map<String, String> params) {
        if (dyanmicData.getfParam() == null) {
            dyanmicData.setfParam(params);
        } else {
            dyanmicData.getfParam().putAll(params);
        }
        return this;
    }

    public FunctionBuild autoLogOut(String logOutId) {
        this.logOutId = logOutId;
        return this;
    }

    public FunctionBuild exceptionCode(String rtnCode) {
        if (this.exceptionCodeList == null)
            this.exceptionCodeList = new ArrayList<String>();
        if (!this.exceptionCodeList.contains(rtnCode))
            this.exceptionCodeList.add(rtnCode);
        return this;
    }

    @Deprecated
    public FunctionBuild otherCode(String rtnCode) {
        if (this.otherCodeList == null)
            this.otherCodeList = new ArrayList<String>();
        if (!this.otherCodeList.contains(rtnCode))
            this.otherCodeList.add(rtnCode);
        return this;
    }

    public FunctionBuild loginState(boolean loginState) {
        this.loginState = loginState;
        return this;
    }

    public FunctionBuild autoLogin(String loginUrlID) {
        this.loginTokenId = loginUrlID;
        return this;
    }

    public FunctionBuild build() {
        instance.setInitBuild(this);
        instance.setQueueListener();
        return this;
    }

    public ArrayList<String> getExceptionCodeList() {
        return exceptionCodeList;
    }

    public ArrayList<String> getOtherCodeList() {
        return otherCodeList;
    }

    public String getLogOutId() {
        return logOutId;
    }

    public String getLoginTokenId() {
        return loginTokenId;
    }

    public boolean getLoginState() {
        return loginState;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public String getServerRootPath() {
        return serverRootPath;
    }
}
