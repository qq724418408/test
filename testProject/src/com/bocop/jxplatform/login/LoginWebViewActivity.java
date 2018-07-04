package com.bocop.jxplatform.login;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.JavascriptInterface;

import android.content.Intent;
import android.widget.Toast;

import com.boc.jx.baseUtil.logger.Logger;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.bean.AppInfoH5;
import com.bocop.jxplatform.config.BocSdkConfig;
import com.google.gson.Gson;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by bocop on 15/12/24.
 * 支持BOCOP APS H5登录功能的交互
 */
public class LoginWebViewActivity extends Activity {

    private WebView mWebView;

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setContentView(R.layout.login_webview_layout);
        //获取需要加载的登录地址
        final String url = getIntent().getExtras().get("url").toString();

        //初始化页面加载所需要的webview
        mWebView = (WebView) super.findViewById(R.id.loginActivity_Webview);

        //设置webview的基本参数，允许JS访问localStorage
        mWebView.getSettings().setDomStorageEnabled(true);
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        mWebView.getSettings().setAppCachePath(appCachePath);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.addJavascriptInterface(new JS(), "android");
        reflectWebView(url);
    }

    /**
     * 加载地址为URL的页面
     */
    @SuppressLint("SetJavaScriptEnabled")
    private final void reflectWebView(String url) {
        //设置webview的基本参数
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSaveFormData(true);
        settings.setBuiltInZoomControls(true);
        settings.setAppCacheEnabled(true);
        if (Build.VERSION.SDK_INT >= 16) {
//            mWebView.getSettings().setAllowFileAccessFromFileURLs(true);
//            mWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        }
        try {
            if (Build.VERSION.SDK_INT >= 16) {
                Class<?> clazz = mWebView.getSettings().getClass();
                Method method = clazz.getMethod(
                        "setAllowUniversalAccessFromFileURLs", boolean.class);
                if (method != null) {
                    method.invoke(mWebView.getSettings(), true);
                }
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

//        CookieManager.setAcceptFileSchemeCookies(true);
        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);

        CookieSyncManager.getInstance().sync();
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            @Override
            public void onReceivedSslError(WebView view,
                                           SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });
        mWebView.loadUrl(url);
    }


    /**
     * 用于Native App和BOCOP APS H5 页面进行js回调的类
     *
     * @author woyawei
     */
    public class JS {
        /**
         * 上传Native App基本信息给H5登录页面
         * @return
         */
        @JavascriptInterface
        public String getAppInfo() {//
            AppInfo info = new AppInfo();
            info.clientId = BocSdkConfig.CONSUMER_KEY;//
            info.client_secret = BocSdkConfig.CONSUMER_SECRET;
            info.appVersion = "234";
            info.responseType = "token";
            info.hideBackArrow = "false";
            Log.i("getAppInfo",  new Gson().toJson(info));
            return new Gson().toJson(info);
        }

        /**
         * 接收H5登录成功以后返回给Native App登录信息
         * @param res
         */
        @JavascriptInterface
        public void waplogin(String res) {
            Logger.e("response=" + res);

            Gson gson = new Gson();
            LoginResponse info = gson.fromJson(res, LoginResponse.class);

//            IApplication.userid = info.userId;
//            IApplication.accessToken = info.access_token;
//            IApplication.isLogin = true;
            finish();
        }

        /**
         * 用于和H5登录页面左上角的返回按钮进行交互
         * */
        @JavascriptInterface
        public void exitlogin() {
            finish();
        }

        /**
         * 上传Native App基本信息给H5个人注册页面
         * @return
         */
        @JavascriptInterface
        public String appInfoForRegister() {
            AppInfoH5 appInfoH5 = new AppInfoH5();
//            appInfoH5.setClientId(IApplication.CLIENTID);
//            appInfoH5.setClient_secret(IApplication.CLIENTSECRET);
            appInfoH5.setAppVersion("234");
            appInfoH5.setResponseType("token");
            appInfoH5.setHasHeaderBar("1");
            return new Gson().toJson(appInfoH5);
        }

        /**
         * 接收H5注册成功以后返回给Native App登录信息
         * @param RegisterInfo
         */
        @JavascriptInterface
        public void webAppRegister(String RegisterInfo) {
            Logger.e("response=" + RegisterInfo);
            Gson gson = new Gson();
            RegisterResponse info = gson.fromJson(RegisterInfo, RegisterResponse.class);
//            IApplication.isLogin = true;
//            IApplication.userid = info.userId;
//            IApplication.accessToken = info.access_token;
//            IApplication.code = info.code;
            Toast toast = Toast.makeText(LoginWebViewActivity.this, "注册成功", Toast.LENGTH_LONG);
            toast.show();
//            Intent intent = new Intent(LoginWebViewActivity.this, myAccountActivity.class);
//            startActivity(intent);
        }
    }
}
