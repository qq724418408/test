package com.boc.jx.httptools.network.http.task;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.boc.jx.httptools.network.Executors;
import com.boc.jx.httptools.network.http.DConfing;
import com.boc.jx.httptools.network.http.build.FunctionBuild;
import com.boc.jx.httptools.network.http.build.HttpBuild;
import com.boc.jx.httptools.network.http.cnotrol.DyanmicData;
import com.boc.jx.httptools.network.http.cnotrol.HttpQueue;
import com.boc.jx.httptools.network.http.cnotrol.ProgressControl;
import com.boc.jx.httptools.network.util.LogUtil;
import com.boc.jx.httptools.network.util.SharedPreferencesUtil;
import com.boc.jx.tools.LogUtils;


/**
 * Created by XinQingXia on 2017/8/12.
 */

public abstract class BaseTask extends ProgressControl {


    public void getFunctions() {

    }

    public void getTargetUrl() {

    }

    public void postRequest() {

    }

    public void next() {

    }

    public SharedPreferencesUtil getShareprenfrence() {
        return SharedPreferencesUtil.getInstance();
    }

    public Map addCommParam() {
        Executors executors = Executors.getInstance();
        DyanmicData dyanmicData = executors.getDyanmicData();
        Map<String, Object> map = executors.getBuild().getParams()/*new HashMap<>()*/;
        if (dyanmicData.getDynamicToken() == null) {
            dyanmicData.setDynamicToken("");
        }
        if (map == null)
            map = new HashMap<>();
        map.put("DYNAMIC_TOKEN", dyanmicData.getDynamicToken());     
        return map;
    }

    public void callError(String error, int code) {
        Executors executors = Executors.getInstance();
        executors.setRunning(false);
        if (error.contains("Socket closed") || error.contains("Canceled")) {
            LogUtil.e("用户取消请求，继续执行队列任务" + error);
            executors.setBuild(null);
            HttpQueue.getInstance().next();
            return;
        }
        HttpBuild buidler = executors.getBuild();
        HttpQueue.getInstance().removeAllHttp();
        if (executors.getInitBuild().getLogOutId().equals(buidler.getId())) {
        	LogUtil.e("executors.loginFailed();");
            executors.loginFailed();
        }
        if (error.contains(DConfing.NeedReLogin)) {
        	LogUtils.e(DConfing.NeedReLogin);
        	executors.clearDynamicData();
		}
        if (buidler.getCallback() != null) {
            if (executors.isLogining()) {
                buidler.getCallback().loginFailed(error, code);
            } else {
                buidler.getCallback().onError(buidler.toString(), error, code);
            }
        }
        executors.setBuild(null);
    }

    public boolean checkEspecialCode(String msg) {
        Executors executors = Executors.getInstance();
        FunctionBuild initBuild = executors.getInitBuild();
        ArrayList<String> codeList = initBuild.getExceptionCodeList();
        int startX = 0,endX = msg.length();
        if (!msg.contains("rtnCode")) {
			return false;
		}
        startX = msg.indexOf("rtnCode");
        if (!msg.contains("rtnMsg")) {
			return false;
		}
        endX = msg.indexOf("rtnMsg");
        msg = msg.substring(startX, endX);
        if (codeList != null && !codeList.isEmpty()) {
            for (String code : codeList) {
                if (msg.contains(code)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void exception(String response) {
        Executors executors = Executors.getInstance();
        executors.setRunning(false);
        executors.loginFailed();
        HttpQueue.getInstance().removeAllHttp();
        executors.getBuild().getCallback().loginFailed(response, DConfing.ESPECIALCODE);
        executors.setBuild(null);
    }
}
