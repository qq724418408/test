package com.bocop.kht.intercept;

import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.webkit.WebView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import com.boc.jx.tools.LogUtils;
import com.bocop.gm.utils.HybridCallBack;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;

public class KhtHybridUtil {

    private Object action;
    private String actionName;
    private String param;
    private Method[] methods;

    private static KhtHybridUtil hybridUtil;
    private static Handler handler = null;

    public static KhtHybridUtil getInstance() {
        if (hybridUtil == null) {
            synchronized (KhtHybridUtil.class) {
                if (hybridUtil == null) {
                    hybridUtil = new KhtHybridUtil();
                    handler = new Handler(Looper.getMainLooper());
                }
            }
        }
        return hybridUtil;
    }

   /* public void handleAppRequest(final String url, Object action, final HybridCallBack callBack) {
        this.action = action;
        if (action == null || callBack == null) {
            throw new NullPointerException("All params Can't be empty");
        }
        callBack.getUrl(url);
        Uri uri = Uri.parse(url);
        actionName = uri.getQueryParameter("actionName");
        callBack.getActionName(actionName);
        param = uri.getQueryParameter("param");
        if (TextUtils.isEmpty(actionName)) {
            callBack.errorMsg(new NullPointerException("The method name cannot be empty"));
            return;
        }
        methods = action.getClass().getDeclaredMethods();
        if (methods == null || methods.length == 0) {
            callBack.errorMsg(new NullPointerException("action error"));
            return;
        }
        handler.post(new Runnable() {

            @Override
            public void run() {
                try {
                    initData(callBack, url);
                } catch (Exception e) {
//                    LogUtil.e(e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }*/

    @SuppressWarnings("unchecked")
    public void handleJsRequest(WebView webview, String actionName, Object params, HybridCallBack callBack) {
        if (webview == null || TextUtils.isEmpty(actionName) || callBack == null) {
            throw new NullPointerException("The webview or actionName or callBack is empty");
        }
        if (params == null) {
            handleJsRequestWithParams(webview, actionName, "", callBack);
            return;
        }
        if (params instanceof String) {
            String string = (String) params;
            if (string.contains("javascript:")) {
                if (string.contains("(") && string.contains(")") && !string.contains(":(")) {
                    webview.loadUrl(string);
                    return;
                } else {
                    callBack.errorMsg(new NullPointerException("The url is not correct"));
                    return;
                }
            }
            handleJsRequestWithParams(webview, actionName, string, callBack);
            return;
        }
        if (params instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) params;
            handleJsRequestWithParams(webview, actionName, map, callBack);
            return;
        }
        if (params instanceof ArrayList) {
            ArrayList<String> list = (ArrayList<String>) params;
            handleJsRequestWithParams(webview, actionName, list, callBack);
            return;
        }
        callBack.errorMsg(
                new NullPointerException("The type parameter must be a String or a map or list,Please check it"));
    }

   /* private void performAction(HybridCallBack callBack) {
        if (methods != null) {
            for (Method method : methods) {
                if (method.getName().startsWith(actionName)) {
                    try {
                        method.invoke(action);
                        return;
                    } catch (Exception e) {
                        callBack.errorMsg(e);
                    }
                }
            }
            callBack.errorMsg(new NullPointerException("The method name was not found"));
        }
    }*/

    private void callJsRequest(final WebView view, final String jsParams) {
        handler.post(new Runnable() {

            @Override
            public void run() {
                view.loadUrl(jsParams);
            }
        });
    }

    private void handleJsRequestWithParams(WebView webview, @NonNull String actionName, ArrayList<String> listParams,
                                           HybridCallBack callBack) {
        if (webview == null || TextUtils.isEmpty(actionName) || callBack == null) {
            throw new NullPointerException("The webview or actionName or callback is empty");
        }
        String callUrl = null;
        if (listParams == null || listParams.size() == 0) {
            callUrl = "javascript:" + actionName + "()";
        } else if (listParams.size() == 1) {
            callUrl = "javascript:" + actionName + "(" + listParams.get(0) + ")";
        } else if (listParams.size() > 1) {
            String actionParam = "";
            for (int i = 0; i < listParams.size(); i++) {
                if (i != listParams.size() - 1) {
                    actionParam += listParams.get(i) + "','";
                } else {
                    actionParam += listParams.get(i);
                }
            }
            callUrl = "javascript:" + actionName + "(" + actionParam + ")";
        }
        callJsRequest(webview, callUrl);
    }

    private void handleJsRequestWithParams(WebView webview, String actionName, String params, HybridCallBack callBack) {
        if (webview == null || TextUtils.isEmpty(actionName) || callBack == null) {
            throw new NullPointerException("The webview or actionName or callback is empty");
        }
        String callUrl;
        if (TextUtils.isEmpty(params)) {
            callUrl = "javascript:" + actionName + "()";
        } else {
            callUrl = "javascript:" + actionName + "(" + params + ")";
        }
        callJsRequest(webview, callUrl);
    }

    private void handleJsRequestWithParams(WebView webview, String actionName, Map<String, Object> params,
                                           HybridCallBack callBack) {
        if (webview == null || TextUtils.isEmpty(actionName)) {
            callBack.errorMsg(new NullPointerException("The webview or actionName is empty"));
            return;
        }
        String callUrl;
        if (params == null || params.size() == 0) {
            callUrl = "javascript:" + actionName + "()";
        } else {
            JSONObject object = new JSONObject(params);
            callUrl = "javascript:" + actionName + "(" + object.toString() + ")";
        }
        callJsRequest(webview, callUrl);
    }

  /*  private void initData(HybridCallBack callBack, String url) throws Exception {
        if (!TextUtils.isEmpty(param) && checkJson(param)) {
            callBack.getOldParams(param);
            callBack.getNewParams(param);
            for (Method method : methods) {
                if (method.getName().startsWith(actionName) && method.getParameterTypes().length == 1) {
                    method.invoke(action, param);
                    return;
                }
            }
            callBack.errorMsg(new NullPointerException("The method name was not found"));
            return;
        }
        if (!url.contains("&")) {
            callBack.getOldParams("");
            callBack.getNewParams("");
            performAction(callBack);
            return;
        }
        int totalLength = url.length();
        int subLength = url.indexOf("&") + 1;
        if (totalLength == subLength || totalLength < subLength) {
            callBack.getOldParams("");
            callBack.getNewParams("");
            performAction(callBack);
            return;
        }
        String substring = url.substring(subLength, totalLength);
        JSONObject object = new JSONObject();
        callBack.getOldParams(substring);
        if (substring.contains("{") && substring.contains("}")) {
            int leftIndex = substring.indexOf("=");
            int rightIndex = substring.indexOf("}");
            String key = substring.substring(0, leftIndex);
            String value = substring.substring(leftIndex + 1, substring.length());
            object.put(key, value);
        } else {
            String[] split = substring.split("&");
            if (split.length == 0) {
                callBack.getOldParams("");
                callBack.getNewParams("");
                performAction(callBack);
                return;
            }
            for (String string : split) {
                String[] strings = string.split("=");
                String key = strings[0];
                if (TextUtils.isEmpty(key)) {
                    callBack.errorMsg(new NullPointerException("The json key can't be empty"));
                    return;
                }
                object.put(key, strings[1]);
            }
        }

        callBack.getNewParams(object.toString());
        dispatch(object.toString(), callBack);
    }
*/
  /*  private void dispatch(String params, HybridCallBack callBack) throws Exception {
        for (Method method : methods) {
            if (method.getName().startsWith(actionName) && method.getParameterTypes().length == 1) {
                method.invoke(action, params);
                return;
            }
        }
        callBack.errorMsg(new NullPointerException("The method name was not found"));
    }*/

   /* private boolean checkJson(String value) {
        try {
            new JSONObject(value);
        } catch (JSONException e) {
            LogUtils.e(e.getMessage());
            return false;
        }
        return true;
    }*/
}
