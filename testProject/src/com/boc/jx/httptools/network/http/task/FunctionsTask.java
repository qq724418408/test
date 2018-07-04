package com.boc.jx.httptools.network.http.task;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.boc.jx.httptools.http.HttpTimeOuts;
import com.boc.jx.httptools.network.Executors;
import com.boc.jx.httptools.network.base.DMessage;
import com.boc.jx.httptools.network.base.DynamicContent;
import com.boc.jx.httptools.network.base.DynamicUri;
import com.boc.jx.httptools.network.base.Function;
import com.boc.jx.httptools.network.callback.HttpUtilsCallBak;
import com.boc.jx.httptools.network.http.DConfing;
import com.boc.jx.httptools.network.http.HttpUtils;
import com.boc.jx.httptools.network.http.build.FunctionBuild;
import com.boc.jx.httptools.network.http.build.HttpBuild;
import com.boc.jx.httptools.network.http.cnotrol.DyanmicData;
import com.boc.jx.tools.LogUtils;
import com.bocop.xfjr.XfjrMain;
import com.bocop.xfjr.config.UrlConfig;

import android.text.TextUtils;


/**
 * Created by XinQingXia on 2017/8/13.
 */

public class FunctionsTask extends TargetUrlTask {
    /**
     * 获取function
     */
    @SuppressWarnings("unchecked")
	@Override
    public void getFunctions() {
        super.getFunctions();
        HttpBuild build = Executors.getInstance().getBuild();
        FunctionBuild initBuild = build.getExecutors().getInitBuild();
        String url = initBuild.getServerUrl() + initBuild.getServerRootPath();
        if(XfjrMain.isSimulator){ // 模拟器的时候用
        	url = UrlConfig.SERVER;
        }
        HttpUtils.sendRequest(build.getContext(), url, build.getId(), addCommParam(), new HttpUtilsCallBak() {
            @Override
            public void onSuccess(String response) {
                LogUtils.e("获取functions---" + response);
                boolean especialCode = checkEspecialCode(response);
                if (especialCode) {
                    exception(response);
                    return;
                }
                try {
                    DynamicContent baseResponse = JSON.parseObject(response, DynamicContent.class);
                    String result = baseResponse.getResult();
                    if ("success".equals(result)) {
                        Executors executors = Executors.getInstance();
                        DyanmicData dyanmicData = executors.getDyanmicData();
                        dyanmicData.setDynamicToken(baseResponse.getDYNAMIC_TOKEN());
                        DMessage message = JSON.parseObject(baseResponse.getMessage(), DMessage.class);
                        List<Function> functions = message.getFunctions();
                        DynamicUri dynamicUri = message.getDynamicUri();
                        String entry = dynamicUri.getEntry();
                        boolean isOn = dynamicUri.isOn();
                        if (isOn && TextUtils.isEmpty(entry)) {
                        	LogUtils.e("init list failure");
                			callError(DConfing.ErrorFromServer, DConfing.SERVERERROR);
//                            callError("账号异常，请重新登录", DConfing.SERVERERROR);
                            return;
                        }
                        dyanmicData.setDynamicState(isOn);
                        dyanmicData.setFunctions(functions);
                        LogUtils.e("entry:"+entry);
                        dyanmicData.setEntry(entry);
                        executors.next();
                    } else {
                    	LogUtils.e(DConfing.ErrorFromServer);
//                    	callError("账号异常，请重新登录", DConfing.SERVERERROR);
                    	callError(DConfing.ErrorFromServer, DConfing.SERVERERROR);
//                        callError(DConfing.ErrorFromServer + "获取functions---", DConfing.SERVERERROR);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtils.e("Exception：JSON.parseObject(response, DynamicContent.class);------->");
        			callError(DConfing.ErrorFromServer, DConfing.SERVERERROR);
                }
            }

            @Override
            public void onError(String errorMsg, String e, int code) {
                LogUtils.e("获取functions---errorMsg--->" + errorMsg+ "---e--->" + e + "---code--->" +  code);
                if(!TextUtils.isEmpty(e) && e.contains("failed to connect to") && e.contains("after ")){
                	errorMsg = DConfing.ErrorFromServer;
                	LogUtils.e(DConfing.ErrorFromServer);
                }
                callError(errorMsg, code);
            }
        });
    }

    @Override
    public void getTargetUrl() {
        if (Executors.getInstance().getBuild().getCallback() == null)
            return;
        super.getTargetUrl();
    }
}
