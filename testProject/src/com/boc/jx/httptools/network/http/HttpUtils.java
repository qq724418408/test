package com.boc.jx.httptools.network.http;

import java.util.HashMap;
import java.util.Map;

import com.boc.jx.httptools.http.callback.IStringCallaBack;
import com.boc.jx.httptools.http.callback.IUpLoadCallback;
import com.boc.jx.httptools.http.engin.IHttpEngin;
import com.boc.jx.httptools.http.engin.okhttp2engin.OkHttp2Engin;
import com.boc.jx.httptools.network.Executors;
import com.boc.jx.httptools.network.callback.HttpUtilsCallBak;
import com.boc.jx.tools.LogUtils;
import com.bocop.xfjr.util.enrypt.SignVerify;

import android.content.Context;
import android.text.TextUtils;


/**
 * http动态url
 * 网络解耦和
 *
 * @author 夏新庆
 */
public class HttpUtils {

    private static IHttpEngin  mCallEngin = new OkHttp2Engin();

    
    public static void init(IHttpEngin engin){
    	mCallEngin = engin;
    }
    
    public static void sendRequest(Context context,String url, Object httpTag, Map<String, Object> params, final HttpUtilsCallBak callBack) {
        LogUtils.e("called with:  url = [" + url + "], httpTag = [" + httpTag + "], params = [" + params + "]");
        Map<String, String> heardMap = new HashMap<>();
        heardMap.put("Accept", "application/vnd.captech-v1.0+json");
        heardMap.put("content-type", "charset=utf-8");
        heardMap.put("accessToken", SignVerify.sha1Sign());
		heardMap.put("time", SignVerify.time);
        heardMap.put("ACCESS-TOKEN", Executors.getInstance().getDyanmicData().getHeardToken());
        postParam(context,url,  params, callBack, heardMap);
    }

    private static void postParam(Context context,String url, Map<String, Object> params, final HttpUtilsCallBak callBack, Map<String, String> heardMap) {
        if(heardMap!=null){
            if(params==null){
                params=new HashMap<>();
            }
            params.putAll(heardMap);
        }
        LogUtils.e("url->"+url);

        mCallEngin.post(context, url, params, new IStringCallaBack() {
			
			@Override
			public void onSuccess(String url, String result) {
				 if (!TextUtils.isEmpty(result))
	                    callBack.onSuccess(result);//成功
	                else
	                    callBack.onError(DConfing.ErrorFromNoData, result, DConfing.SERVERERROR);
			}
			
			@Override
			public void onFinal(String url) {
				
			}
			
			@Override
			public void onError(String url, Throwable e) {
				callBack.onError(DConfing.ErrorFromServer, e==null?"":e.getMessage(), DConfing.SERVERERROR);
			}
		});
    }

   public static void sendRequestWithFile(Context context,String url,
                                          Map<String, Object> params,
                                          final HttpUtilsCallBak callBack) {
		Map<String, String> heardMap = new HashMap<>();
		heardMap.put("Accept", "application/vnd.captech-v1.0+json");
		heardMap.put("content-type", "charset=utf-8");
		heardMap.put("ACCESS-TOKEN", SignVerify.sha1Sign());
		heardMap.put("accessToken", SignVerify.sha1Sign());
		heardMap.put("time", SignVerify.time);
		params.putAll(heardMap);
       /*com.bocop.xfjr.util.http.HttpUtils.with(context).addDecortor(mCallEngin).addParams(params).url(url).upLoad(new IUpLoadCallback(){
           @Override
           public void onProgress(long total, long current) {

           }

           @Override
           public void onSuccess(String result) {
               if (!TextUtils.isEmpty(result))
                   callBack.onSuccess(result);//成功
               else
                   callBack.onError(DConfing.ErrorFromNoData, result, DConfing.SERVERERROR);
           }

           @Override
           public void onError(Throwable e) {
               callBack.onError(DConfing.ErrorFromServer, e.getMessage(), DConfing.SERVERERROR);
           }

           @Override
           public void onFinal() {

           }
       });
       */
       mCallEngin.upLoad(context, url, params, new IUpLoadCallback() {
		
		@Override
		public void onSuccess(String result) {
			 if (!TextUtils.isEmpty(result))
                 callBack.onSuccess(result);//成功
             else
                 callBack.onError(DConfing.ErrorFromNoData, result, DConfing.SERVERERROR);
		}
		
		@Override
		public void onProgress(long total, long current) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onFinal() {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onError(Throwable e) {
			 callBack.onError(DConfing.ErrorFromServer, e.getMessage(), DConfing.SERVERERROR);
			
		}
	});
    }
 /*
    public static void sendRequestWithFile(String url,
                                           Map<String, String> params, List<PostMultipartBuilder.FileObject> fileObjects, Object tag,
                                           final HttpUtilsCallBak callBack) {
        Map<String, String> heardMap = new HashMap<>();
        heardMap.put("Accept", "application/vnd.captech-v1.0+json");
        heardMap.put("content-type", "charset=utf-8");
        heardMap.put("ACCESS-TOKEN", Executors.getInstance().getDyanmicData().getHeardToken());
        HttpUtil.getInstance().postMultipartRequest(url, heardMap, params, fileObjects, tag, new PostMultipleCallback() {
            @Override
            public void onSuccess(Call call, String s) {
                if (!TextUtils.isEmpty(s))
                    callBack.onSuccess(s);//成功
                else
                    callBack.onError(DConfing.ErrorFromNoData, s, DConfing.SERVERERROR);
            }

            @Override
            public void onError(Call call, String s) {//连上了 ，但是失败了
                callBack.onError(DConfing.ErrorFromServer, s, DConfing.SERVERERROR);
            }

            @Override
            public void getHeader(Headers headers) {
                String accessToken = headers.get("ACCESS-TOKEN");
                Log.e("TAG", "TOKEN--" + accessToken);
                if (accessToken != null)
                    Executors.getInstance().getDyanmicData().setHeardToken(accessToken);
            }

            @Override
            public void onFailed(Call call, String s) {//没连接到服务器
                callBack.onError(DConfing.ErrorFromServer, s, DConfing.TIMEOUT);
            }
        });
    }

    public static void downloadFileBase(String url, String fileDir, String fileName, final Progress progress, final FileCallBack callBack) {
        if (progress != null)
            progress.show();
        Map<String, String> heardMap = new HashMap<>();
        heardMap.put("Accept", "application/vnd.captech-v1.0+json");
        heardMap.put("content-type", "charset=utf-8");
        heardMap.put("ACCESS-TOKEN", Executors.getInstance().getDyanmicData().getHeardToken());
        HttpUtil.getInstance().downloadFileRequest(url, heardMap, url, new DownloadFileCallback(fileDir, fileName) {
            @Override
            public void onSuccess(Call call, File file) {
                if (progress != null)
                    progress.dismiss();
                if (file != null)
                    callBack.onSuccessed(file);
                else
                    callBack.onError(call.request().toString(), null, DConfing.SERVERERROR);
            }

            @Override
            public void onError(Call call, String s) {
                if (progress != null)
                    progress.faild(DConfing.SERVERERROR);
                callBack.onError(call.request().toString(), s, DConfing.SERVERERROR);
            }

            @Override
            public void getHeader(Headers headers) {
                String accessToken = headers.get("ACCESS-TOKEN");
                Log.e("TAG", "TOKEN--123456789" + accessToken);
                if (accessToken != null)
                    Executors.getInstance().getDyanmicData().setHeardToken(accessToken);
            }

            @Override
            public void onProgress(int progress) {
                super.onProgress(progress);
                callBack.inProgress(progress);
            }

            @Override
            public void onFailed(Call call, String s) {
                if (progress != null)
                    progress.faild(DConfing.SERVERERROR);
                callBack.onError(call.request().toString(), s, DConfing.TIMEOUT);
            }
        });
    }*/

    public static void cancelRequest(String urlID) {
        if (!TextUtils.isEmpty(urlID))
            try {
            	mCallEngin.cancle(urlID);
            } catch (Exception e) {
                e.printStackTrace();
            }
        
    }

}