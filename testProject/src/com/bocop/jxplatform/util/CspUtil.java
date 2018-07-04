package com.bocop.jxplatform.util;

import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;
import com.boc.jx.base.BaseApplication;
import com.boc.jx.baseUtil.asynchttpclient.AsyncHttpClient;
import com.boc.jx.baseUtil.asynchttpclient.AsyncHttpResponseHandler;
import com.boc.jx.baseUtil.asynchttpclient.RequestHandle;
import com.boc.jx.tools.DialogUtil;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.bean.BocRecComBodyBean;
import com.bocop.jxplatform.config.BocSdkConfig;
import com.bocop.jxplatform.trafficassistant.TrafficAssistantMainActivity;
import com.bocop.jxplatform.util.LoginUtil.ILoginListener;
import com.bocop.yfx.activity.loanprodetail.WorkUnitInfoActivity;
import com.bocop.zyyr.xml.TestData;
import com.squareup.okhttp.*;
import org.apache.http.Header;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.message.BasicHeader;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;


/**
 * @author luoyang
 * @version 创建时间：2015-6-23 下午5:46:43 分行CSP通用接口类，需要验证
 */

public class CspUtil implements ILoginListener {

    /**
     * 用户信息
     */
    public BaseApplication baseApplication = BaseApplication.getInstance();
    private static final int REQUEST_SUCCESS = 0;
    private static final int REQUEST_FAILURE = 1;
    private static final int REQUEST_FINISH = 2;
    String msgcode;
    String rtnmsg;
    private String txCode;
    private boolean FLAG_YFX_CSP = false;

    /*
     * http报文头信息
     */
    public Context mContext;
    private CustomProgressDialog dialog;
    private static IMyLoginListener imyCallback;

    public interface IMyLoginListener {
        void onLogin();

        void onCancle();

        void onError();

        void onException();

    }

    public CspUtil(Context context) {
        this.mContext = context;
    }

    public String getTxCode() {
        return txCode;
    }

    public void setTxCode(String txCode) {
        this.txCode = txCode;
    }

    public boolean isFLAG_YFX_CSP() {
        return FLAG_YFX_CSP;
    }

    public void setFLAG_YFX_CSP(boolean fLAG_YFX_CSP) {
        FLAG_YFX_CSP = fLAG_YFX_CSP;
    }

    public class UIhandler extends Handler {

        public CallBack callBack;

        public UIhandler(CallBack callBack) {
            super(Looper.getMainLooper());
            this.callBack = callBack;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REQUEST_SUCCESS:
                    callBack.onSuccess((String) msg.obj);
                    break;
                case REQUEST_FAILURE:
                    callBack.onFailure((String) msg.obj);
                    break;
                case REQUEST_FINISH:
                    callBack.onFinish();
                    break;
            }
            ;
        }
    }

    public Header[] getHeaders() {
        Header clientid = new BasicHeader("clentid", BocSdkConfig.CONSUMER_KEY);
        Header userid = new BasicHeader("userid", LoginUtil.getUserId(mContext));
        Header action = new BasicHeader("acton", LoginUtil.getToken(mContext));
        Header type = new BasicHeader("type", "1");
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMDD");
        // 获取当前时间
        String nowData = format.format(new Date(System.currentTimeMillis()));
        Header trandt = new BasicHeader("trandt", nowData);

        SimpleDateFormat formatTime = new SimpleDateFormat("HHmmss");
        // 获取当前时间
        String nowTime = formatTime.format(new Date(System.currentTimeMillis()));

        Header trantm = new BasicHeader("trantm", nowTime);
        // return new Header[] { clientid, type, userid, action};
        return new Header[]{clientid, type, userid, action, trandt, trantm};
    }

    /**
     * @param context    :上下文
     * @param url        ：中银开放平台CSP通用接口地址(需要验证)
     * @param mcis       ：开放平台报文
     * @param myCallback ：回调
     */

    public void postCspLogin(byte[] mcis, CallBack callBack) {

        if (baseApplication.isNetStat()) {
            if (FLAG_YFX_CSP) {
                post(BocSdkConfig.YFX_LOGIN_MCISCSP, getHeaders(), mcis, callBack);
            }
            else {
                post(BocSdkConfig.LOGIN_MCISCSP, getHeaders(), mcis, callBack);
            }
        }
        else {
            CustomProgressDialog.showBocNetworkSetDialog(mContext);
        }
    }

    public void postCspNoLogin(String url, RequestBody body, boolean isShowDialog, final CallBack callBack) {
        if (baseApplication.isNetStat()) {
            final OkHttpClient mOkHttpClient = new OkHttpClient();
            final Request request = new Request.Builder().url(url)
                    .header("Content-Type", "application/octet-stream; charset=UTF-8")
                    .addHeader("Cache-Control", "no-cache").addHeader("Accept-Charset", "UTF-8").post(body).build();
            postOkHttpRequest(mOkHttpClient, request, callBack, false, isShowDialog);
        }
        else {
            if (!(mContext instanceof Service)) {
                CustomProgressDialog.showBocNetworkSetDialog(mContext);
            }
        }
    }
    
    /**
     * 开发平台未登录接口
     * @param content
     * @param callBack
     * @param isShowDailog
     */
    public void postCspNoLogin(String content, final CallBack callBack, boolean isShowDailog) {
        if (baseApplication.isNetStat()) {
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMDD");
            SimpleDateFormat formatTime = new SimpleDateFormat("HHmmss");
            // 获取当前日期
            String nowData = format.format(new Date(System.currentTimeMillis()));
            // 获取当前时间
            String nowTime = formatTime.format(new Date(System.currentTimeMillis()));

            OkHttpClient mOkHttpClient = new OkHttpClient();
            mOkHttpClient.setConnectTimeout(30, TimeUnit.SECONDS);
            MediaType text = MediaType.parse("application/text; charset=utf-8");
            RequestBody body = RequestBody.create(text, content);
            String url = BocSdkConfig.UNLOGIN_MCISCSP;
            Request request = new Request.Builder().url(url)
                    .header("Content-Type", "application/octet-stream; charset=UTF-8")
                    .addHeader("Cache-Control", "no-cache").addHeader("Accept-Charset", "UTF-8")
                    .addHeader("clentid", BocSdkConfig.CONSUMER_KEY).addHeader("userid", "1234567")
                    .addHeader("acton", "1234567").addHeader("type", "1")
                    .addHeader("trandt", nowData).addHeader("trantm", nowTime).post(body).build();
            postOkHttpRequest(mOkHttpClient, request, callBack, true, isShowDailog);
        }
        else {
            CustomProgressDialog.showBocNetworkSetDialog(mContext);
        }
    }

    public void postCspLogin(String content, final CallBack callBack) {
        this.postCspLogin(content, callBack, true);
    }

    public void postCspLogin(String content, final CallBack callBack, boolean isShowDailog) {
        if (baseApplication.isNetStat()) {
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMDD");
            SimpleDateFormat formatTime = new SimpleDateFormat("HHmmss");
            // 获取当前日期
            String nowData = format.format(new Date(System.currentTimeMillis()));
            // 获取当前时间
            String nowTime = formatTime.format(new Date(System.currentTimeMillis()));

            OkHttpClient mOkHttpClient = new OkHttpClient();
            mOkHttpClient.setConnectTimeout(30, TimeUnit.SECONDS);
            MediaType text = MediaType.parse("application/text; charset=utf-8");
            RequestBody body = RequestBody.create(text, content);
            String url;
            if (FLAG_YFX_CSP) {
                if (BocSdkConfig.isTest) {
                    url = BocSdkConfig.YFX_LOGIN_MCISCSP_TEST;
                   // url="http://192.168.22.171:8080/jxxms/mciscsp";
                }
                else {
                    url = BocSdkConfig.YFX_LOGIN_MCISCSP;
                }
            }
            else {
                url = BocSdkConfig.LOGIN_MCISCSP;
            }
            Request request = new Request.Builder().url(url)
                    .header("Content-Type", "application/octet-stream; charset=UTF-8")
                    .addHeader("Cache-Control", "no-cache").addHeader("Accept-Charset", "UTF-8")
                    .addHeader("clentid", BocSdkConfig.CONSUMER_KEY).addHeader("userid", LoginUtil.getUserId(mContext))
                    .addHeader("acton", LoginUtil.getToken(mContext)).addHeader("type", "1")
                    .addHeader("trandt", nowData).addHeader("trantm", nowTime).post(body).build();
            

            postOkHttpRequest(mOkHttpClient, request, callBack, true, isShowDailog);
        }
        else {
            CustomProgressDialog.showBocNetworkSetDialog(mContext);
        }
    }

    private void postOkHttpRequest(final OkHttpClient mOkHttpClient, final Request request, final CallBack callBack,
                                   final boolean isCspLogin, final boolean isShowDialog) {
        if (isShowDialog) {
            dialog = new CustomProgressDialog(mContext, "...正在加载...", R.anim.frame);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
        final UIhandler mHandler = new UIhandler(callBack);
        final Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                if (isShowDialog) {
                    dialog.cancel();
                }
                Message msg = new Message();
                // TODO
                if (BocSdkConfig.isTest) {
                    msg.what = REQUEST_SUCCESS;
                    if (!TextUtils.isEmpty(txCode)) {
                        msg.obj = TestData.getYfxTestData(txCode);
                    }
                    else {
                        msg.obj = TestData.getYrTestData(request.urlString());
                    }
                }
                else {
                    msg.what = REQUEST_FAILURE;

                    if (e.getMessage() == null) {
                        msg.obj = "0";
                    }
                    else {
                        if (e.getMessage().contains("failed to connect to")) {
                            msg.obj = "0";
                        }
                        else if ("Canceled".equals(e.getMessage()) || "Socket closed".equals(e.getMessage())) {
                            msg.obj = "您取消了加载";
                        }
                        else {
                            msg.obj = e.getMessage();
                        }
                    }
                }
                mHandler.sendMessage(msg);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    if (isShowDialog) {
                        dialog.cancel();
                    }
                    byte[] responseBody = response.body().bytes();
                    String strResponseBody = new String(responseBody, "UTF-8");
                    Log.i("tag", "strResponseBody || " + strResponseBody);
                    Message msg = new Message();
                    if (isCspLogin) {
                        if (strResponseBody.contains("UTILITY_PAYMENT")) {
                            Mcis mcis = new Mcis();
                            Log.i("tag", "开始解析MCIS");
                            String strMcis = mcis.recData(responseBody);
                            Log.i("tag", "mcis解析后：" + strMcis);
                            msg.what = REQUEST_SUCCESS;
                            msg.obj = strMcis;
                        }
                        else if (strResponseBody.contains("msgcde")) {
                            try {
                                BocRecComBodyBean bocRecComBodyBean = null;
                                bocRecComBodyBean = JsonUtils.getObject(strResponseBody, BocRecComBodyBean.class);
                                if (bocRecComBodyBean.getRtnmsg().contains("ccess")
                                        || bocRecComBodyBean.getRtnmsg().contains("expired")) {
                                    LoginUtil.authorize(mContext, CspUtil.this);
                                    msg.what = REQUEST_FAILURE;
                                    msg.obj = "温馨提示：登陆超时，请重新登陆。";
                                    LoginUtil.logoutWithoutCallback(mContext);
                                }
                                else {
                                    msg.what = REQUEST_FAILURE;
                                    msg.obj = bocRecComBodyBean.getRtnmsg();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        else if (strResponseBody.length() == 0) {
                            msg.what = REQUEST_FAILURE;
                            msg.obj = "服务器错误，请稍候再试";
                        }
                    }
                    else {
                        msg.what = REQUEST_SUCCESS;
                        msg.obj = strResponseBody;
                    }
                    mHandler.sendMessage(msg);
                }
            }
        });
        if (isShowDialog) {
            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        dialog.cancel();
                        try {
                            Log.i("tag", "onCancel 取消 handle成功");
                            call.cancel();
                        } catch (Exception e) {
                            Log.i("tag", "异常");
                        }
                    }
                    return true;
                }
            });
        }
    }

    /**
     * @param context    :上下文
     * @param url        ：中银开放平台CSP通用接口地址
     * @param mcis       ：开放平台报文 RequestHandle
     * @param myCallback ：回调 罗阳：于20150703封装加载框，并监听DIALOG的取消时间
     */
    private RequestHandle post(String url, final Header[] headers, byte[] mcis, final CallBack callBack) {
        final String contentType = "application/octet-stream; charset=UTF-8";
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Content-Type", contentType);
        client.addHeader("Cache-Control", "no-cache");
        client.addHeader("Accept-Charset", "UTF-8");
        // client.setTimeout(30 * 1000);
        Log.i("tag", "timeout:" + String.valueOf(client.getTimeout()));
        // client.setMaxRetriesAndTimeout(2, 30 * 1000);
         client.setTimeout(30 * 1000);
//        client.setMaxRetriesAndTimeout(3, 10000);
        Log.i("tag", "timeout:" + String.valueOf(client.getTimeout()));
        final RequestHandle handle;

        ByteArrayEntity entity = new ByteArrayEntity(mcis);
        final CustomProgressDialog dialog = new CustomProgressDialog(mContext, "...正在加载...", R.anim.frame);
        dialog.show();
        final String strUrl = url;
        handle = client.post(mContext, url, headers, entity, contentType, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // TODO Auto-generated method stub
                Log.i("tag", "onStart:" + strUrl);
            }

            @Override
            public void onSuccess(int statusCode, Header[] i, byte[] responseBody) {
                Log.i("tag", "onStart");
                Log.i("tag", "onSuccess" + statusCode);
                String strResponseBody;
                // 获取APP的公共报头
                if (statusCode == 200 && headers != null) {
                    try {
//                        msgcode = URLDecoder.decode(headers[0].getValue(), "UTF-8") + "|||"
//                                + URLDecoder.decode(headers[1].getValue(), "UTF-8") + "|||"
//                                + URLDecoder.decode(headers[2].getValue(), "UTF-8") + "|||"
//                                + URLDecoder.decode(headers[3].getValue(), "UTF-8") + "|||"
//                                + URLDecoder.decode(headers[4].getValue(), "UTF-8") + "|||"
//                                + URLDecoder.decode(headers[5].getValue(), "UTF-8");
//                        rtnmsg = URLDecoder.decode(headers[5].getValue(), "UTF-8");
//                        Log.i("tag2", "APP的公共报头:" + msgcode + "|||" + rtnmsg);
//                        Log.i("tag2", "APP的公共体" + new String(responseBody, "UTF-8") + responseBody.length);
                        // if(msgcode.equals("0000000") && responseBody
                        // != null){
                        strResponseBody = new String(responseBody, "UTF-8");
                        if (strResponseBody.contains("UTILITY_PAYMENT")) {
                            Mcis mcis = new Mcis();
                            Log.i("tag", "开始解析MCIS");
                            String strMcis = mcis.recData(responseBody);
                            Log.i("tag", "mcis解析后：" + strMcis);
                            callBack.onSuccess(strMcis);
                        }
                        else if (strResponseBody.contains("msgcde")) {
                            try {
                                BocRecComBodyBean bocRecComBodyBean = null;
                                bocRecComBodyBean = JsonUtils.getObject(strResponseBody, BocRecComBodyBean.class);
                                if (bocRecComBodyBean.getRtnmsg().contains("ccess")
                                        || bocRecComBodyBean.getRtnmsg().contains("expired")) {
//                                    LoginUtil.authorize(mContext, CspUtil.this);
                                    Toast.makeText(mContext, "温馨提示：登陆超时，请重新登陆。", Toast.LENGTH_LONG).show();
                                    LoginUtil.logoutWithoutCallback(mContext);
                                    callBack.onFinish();
                                }
                                else {
                                    Toast.makeText(mContext, bocRecComBodyBean.getRtnmsg(), Toast.LENGTH_LONG).show();
                                    callBack.onFinish();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        else if (strResponseBody.length() == 0) {
                            Toast.makeText(mContext, "服务器错误，请稍候再试", Toast.LENGTH_LONG).show();
                            callBack.onFailure("back");
                        }
                        else {
                            callBack.onFailure(String.valueOf(statusCode) + msgcode + rtnmsg);
                        }
                    } catch (UnsupportedEncodingException e) {
                        callBack.onFailure(e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                try {
                    if (responseBody != null) {
                        callBack.onFailure(String.valueOf(statusCode) + new String(responseBody, "UTF-8"));
                    }
                    else {
                        callBack.onFailure(String.valueOf(statusCode));
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                callBack.onFinish();
                Log.i("tag", "onCancel 自动调用取消 进度条 ");
                dialog.cancel();
            }

        });
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
            	try {
                    if (handle.cancel(true)) {
                        Log.i("tag1", "onCancel 取消 handle成功");
                    }
                    else {
                        Log.i("tag1", "onCancel 取消handle失败");
                    }
                } catch (Exception e) {
                    Log.i("tag", "异常");
                }
            }
        });
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.cancel();
                    Toast.makeText(mContext, "您取消了加载", Toast.LENGTH_SHORT).show();
                    try {
                        if (handle.cancel(true)) {
                            Log.i("tag1", "onCancel 取消 handle成功");
                        }
                        else {
                            Log.i("tag1", "onCancel 取消handle失败");
                        }
                    } catch (Exception e) {
                        Log.i("tag", "异常");
                    }

                }
//                mContext
//                if(keyCode == mContext.)
                return true;
            }
        });

        return handle;
    }

    public interface CallBack {
        public void onSuccess(String responStr);

        public void onFailure(String responStr);

        public void onFinish();
    }

    public static void onFailure(Context mContext, String responStr) {
        if (responStr.equals("0")) {
            Toast.makeText(mContext, R.string.onFailure, Toast.LENGTH_SHORT).show();
        }
        else {
        	DialogUtil.showWithOneBtn(mContext, responStr, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
//            Toast.makeText(mContext, responStr, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLogin() {
//        Toast.makeText(mContext, "登陆成功", Toast.LENGTH_LONG).show();
//        if (mContext instanceof TrafficAssistantMainActivity) {
//            ((TrafficAssistantMainActivity) mContext).requestCspForCarList();
//        }
    }

    @Override
    public void onCancle() {
        Toast.makeText(mContext, "请重新登陆，登录后才能办理相关业务", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onError() {
        Toast.makeText(mContext, "登陆错误", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onException() {
        Toast.makeText(mContext, "登陆异常", Toast.LENGTH_LONG).show();
    }

}
