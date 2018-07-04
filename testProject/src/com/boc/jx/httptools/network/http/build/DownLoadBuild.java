package com.boc.jx.httptools.network.http.build;//package test.com.web.xinqing.dynamicurldemo.network.http.build;
//
//import android.content.Context;
//import android.support.annotation.NonNull;
//import android.util.Log;
//
//import com.forms.okhttplibrary.callback.DownloadFileCallback;
//import com.forms.okhttplibrary.util.HttpUtil;
//
//import java.io.File;
//import java.util.HashMap;
//import java.util.Map;
//
//import okhttp3.Call;
//import okhttp3.Headers;
//import test.com.web.xinqing.dynamicurldemo.network.Executors;
//import test.com.web.xinqing.dynamicurldemo.network.callback.FileCallBack;
//import test.com.web.xinqing.dynamicurldemo.network.http.DConfing;
//import test.com.web.xinqing.dynamicurldemo.network.listener.Progress;
//
///**
// * Created by XinQingXia on 2017/8/17.
// */
//
//public class DownLoadBuild {
//
//    private String url;
//    private String fileDir;
//    private String fileName;
//    private Progress progress;
//    private FileCallBack callBack;
//    private Map<String, String> headMap;
//
//    public DownLoadBuild url(String url) {
//        this.url = url;
//        return this;
//    }
//
//    public DownLoadBuild fileDir(String fileDir) {
//        this.fileDir = fileDir;
//        return this;
//    }
//
//    public DownLoadBuild fileName(String fileName) {
//        this.fileName = fileName;
//        return this;
//    }
//
//    public DownLoadBuild progress(Progress progress) {
//        this.progress = progress;
//        return this;
//    }
//
//    public DownLoadBuild callBack(FileCallBack callBack) {
//        this.callBack = callBack;
//        return this;
//    }
//
//    public void downloadFileBase() {
//        if (progress != null)
//            progress.show();
//        if (headMap == null && headMap.isEmpty())
//            headMap = new HashMap<>();
//        headMap.put("Accept", "application/vnd.captech-v1.0+json");
//        headMap.put("content-type", "charset=utf-8");
//        headMap.put("ACCESS-TOKEN", Executors.getInstance().getDyanmicData().getHeardToken());
//        HttpUtil.getInstance().downloadFileRequest(url, headMap, url, new DownloadFileCallback(fileDir, fileName) {
//            @Override
//            public void onSuccess(Call call, File file) {
//                if (progress != null)
//                    progress.dismiss();
//                if (file != null)
//                    callBack.onSuccessed(file);
//                else
//                    callBack.onError(call.request().toString(), null, DConfing.SERVERERROR);
//            }
//
//            @Override
//            public void onError(Call call, String s) {
//                if (progress != null)
//                    progress.faild(DConfing.SERVERERROR);
//                callBack.onError(call.request().toString(), s, DConfing.SERVERERROR);
//            }
//
//            @Override
//            public void getHeader(Headers headers) {
//                String accessToken = headers.get("ACCESS-TOKEN");
//                Log.e("TAG", "TOKEN--123456789" + accessToken);
//                if (accessToken != null)
//                    Executors.getInstance().getDyanmicData().setHeardToken(accessToken);
//            }
//
//            @Override
//            public void onProgress(int progress) {
//                super.onProgress(progress);
//                callBack.inProgress(progress);
//            }
//
//            @Override
//            public void onFailed(Call call, String s) {
//                if (progress != null)
//                    progress.faild(DConfing.SERVERERROR);
//                callBack.onError(call.request().toString(), s, DConfing.TIMEOUT);
//            }
//        });
//    }
//
//}
