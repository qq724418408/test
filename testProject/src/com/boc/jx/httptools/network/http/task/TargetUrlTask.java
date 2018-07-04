package com.boc.jx.httptools.network.http.task;

import com.alibaba.fastjson.JSON;
import com.boc.jx.httptools.network.Executors;
import com.boc.jx.httptools.network.base.DynamicContent;
import com.boc.jx.httptools.network.base.TMessage;
import com.boc.jx.httptools.network.callback.HttpUtilsCallBak;
import com.boc.jx.httptools.network.http.DConfing;
import com.boc.jx.httptools.network.http.HttpUtils;
import com.boc.jx.httptools.network.http.build.HttpBuild;
import com.boc.jx.tools.LogUtils;


/**
 * Created by XinQingXia on 2017/8/13.
 */

public class TargetUrlTask extends BaseTask {
    /**
     * 请求目标URL
     */
    @Override
    public void getTargetUrl() {
        super.getTargetUrl();
        Executors executors = Executors.getInstance();
        HttpBuild build = executors.getBuild();
        HttpUtils.sendRequest(build.getContext(),executors.getInitBuild().getServerUrl() + executors.getDyanmicData().getEntry(),
                build.getId(), addCommParam(), new HttpUtilsCallBak() {
                    @Override
                    public void onSuccess(String response) {
                        LogUtils.e("获取目标url---" + response);
                        boolean especialCode = checkEspecialCode(response);
                        if (especialCode) {
                            exception(response);
                            return;
                        }
                        try {
                            DynamicContent baseResponse = JSON.parseObject(response, DynamicContent.class);
                            String result = baseResponse.getResult();
                            if ("success".equals(result)) {
                                Executors instance = Executors.getInstance();
                                instance.getDyanmicData().setDynamicToken(baseResponse.getDYNAMIC_TOKEN());
                                TMessage message = JSON.parseObject(baseResponse.getMessage(), TMessage.class);
                                String target = message.getTarget();
                                String self = message.getSelf();
                                instance.getDyanmicData().setEntry(self);
                                instance.getBuild().setUrlKey(target);
                                instance.getBuild().getExecutors().postRequest();
                            } else {
                            	LogUtils.e("Failed to get the target url");
                            	callError(DConfing.NeedReLogin, DConfing.SERVERERROR);
//                                callError("Failed to get the target url", DConfing.SERVERERROR);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            LogUtils.e(e.getMessage());
                            LogUtils.e("Exception---> JSON.parseObject(response, DynamicContent.class)：" + e.getMessage());
                            callError(DConfing.NeedReLogin, DConfing.SERVERERROR);
//                            callError(DConfing.ErrorFromServer, DConfing.SERVERERROR);
//                            callError("Failed to get the target url", DConfing.SERVERERROR);
                        }
                    }

                    @Override
                    public void onError(String errorMsg, String e, int code) {
                    	 LogUtils.e("errorMsg--->" + errorMsg+ "---e--->" + e + "---code--->" +  code);
//                    	callError("账号异常，请重新登录", code);
                        callError(errorMsg, code);
                    }
                });
    }
}
