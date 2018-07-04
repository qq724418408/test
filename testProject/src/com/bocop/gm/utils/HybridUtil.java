package com.bocop.gm.utils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;

/**
 * Js interactive tools
 * 
 * @author Xia Xinqing
 *
 *         On July 5, 2016
 */
public class HybridUtil {

	private Object action;
	private String actionName;
	private String param;
	private Method[] methods;
	// private String type;

	private static HybridUtil hybridUtil;
	private static Handler handler = null;

	private HybridUtil() {

	}

	/**
	 * instantiation
	 * 
	 * @return
	 */
	public static HybridUtil getInstance() {
		if (hybridUtil == null) {
			synchronized (HybridUtil.class) {
				if (hybridUtil == null) {
					hybridUtil = new HybridUtil();
					handler = new Handler(Looper.getMainLooper());
				}
			}
		}
		return hybridUtil;
	}

	/**
	 * 
	 * @param url
	 *            url
	 * @param action
	 *            Call methods of a class instance
	 * 
	 * @param callBack
	 *            Error correction
	 */
	public void handleAppRequest(String url, Object action, HybridCallBack callBack) {
		this.action = action;
		if (action == null || callBack == null) {
			throw new NullPointerException("All params Can't be empty");
		}
		callBack.getUrl(url);
		Log.i("TAG", "handleJSRequest");
		Uri uri = Uri.parse(url);
		actionName = uri.getQueryParameter("actionName");
		callBack.getActionName(actionName);
		if ("shareCall".equals(actionName) && url.contains("param")) {
			try {
				param = URLDecoder.decode(url.substring(url.indexOf("param=")+6), "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} else {
			param = uri.getQueryParameter("param");
		}
		Log.i("TAG", "actionName-->" + actionName);
		Log.i("TAG", "params-->" + param);
		if (TextUtils.isEmpty(actionName)) {
			callBack.errorMsg(new NullPointerException("The method name cannot be empty"));
			return;
		}
		methods = action.getClass().getDeclaredMethods();
		if (methods == null || methods.length == 0) {
			callBack.errorMsg(new NullPointerException("action error"));
			return;
		}
		// if (TextUtils.isEmpty(param)) {
		// performAction(callBack);
		// return;
		// }
		// type = callBack.paramType();
		// if (TextUtils.isEmpty(type)) {
		// callBack.errorMsg(new NullPointerException("Please call configuration
		// parameter types"));
		// return;
		// }
		try {
			initData(callBack, url);
		} catch (Exception e) {
			e.printStackTrace();
			callBack.errorMsg(e);
		}
	}

	/**
	 * Call HTML method
	 * 
	 * @param webview
	 * @param actionName
	 * @param params
	 *            1.Directly assembles a good pass json string / js url
	 *            <p/>
	 *            2.Map<String,String>type Key/value pair, by the framework
	 *            assembled json
	 *            <p/>
	 *            3.ArrayList<String>type,Call multi-parameter js method, it is
	 *            necessary to note that the sequence data
	 * @param callBack
	 */
	@SuppressWarnings("unchecked")
	public synchronized void handleJsRequest(WebView webview, String actionName, Object params, HybridCallBack callBack) {

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
			Map<String, String> map = (Map<String, String>) params;
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

	private void performAction(HybridCallBack callBack) {
		if (methods != null) {
			for (Method method : methods) {
				if (method.getName().startsWith(actionName)) {
					try {
						method.invoke(action);
						return;
					} catch (Exception e) {
						e.printStackTrace();
						callBack.errorMsg(e);
					}
				}
			}
			callBack.errorMsg(new NullPointerException("The method name was not found"));

		}
	}

	private void callJsRequest(final WebView view, final String jsParams) {
		handler.post(new Runnable() {

			@Override
			public void run() {
				view.loadUrl(jsParams);
			}
		});
	}

	private void handleJsRequestWithParams(@NonNull WebView webview, @NonNull String actionName,
			ArrayList<String> listParams, @NonNull HybridCallBack callBack) {

		// String callUrl = "javascript:" + actionName + "('" + actionParam +
		// "')";
		if (webview == null || TextUtils.isEmpty(actionName) || callBack == null) {
			throw new NullPointerException("The webview or actionName or callback is empty");
		}
		String callUrl = null;
		if (listParams == null || listParams.size() == 0) {
			callUrl = "javascript:" + actionName + "()";
		} else if (listParams != null && listParams.size() == 1) {
			callUrl = "javascript:" + actionName + "('" + listParams.get(0) + "')";
		} else if (listParams != null && listParams.size() > 1) {
			String actionParam = "";
			for (int i = 0; i < listParams.size(); i++) {
				if (i != listParams.size() - 1) {
					actionParam += listParams.get(i) + "','";
				} else {
					actionParam += listParams.get(i);
				}
			}
			callUrl = "javascript:" + actionName + "('" + actionParam + "')";
		}
		Log.i("TAG", callUrl);
		callJsRequest(webview, callUrl);
	}

	private void handleJsRequestWithParams(WebView webview, String actionName, String params, HybridCallBack callBack) {
		if (webview == null || TextUtils.isEmpty(actionName) || callBack == null) {
			throw new NullPointerException("The webview or actionName or callback is empty");
		}
		String callUrl = null;
		if (TextUtils.isEmpty(params)) {
			callUrl = "javascript:" + actionName + "()";
		} else {
			callUrl = "javascript:" + actionName + "('" + params + "')";
		}
		System.out.println("calUrl:-----"+callUrl);
		callJsRequest(webview, callUrl);
	}

	private void handleJsRequestWithParams(WebView webview, String actionName, Map<String, String> params,
			HybridCallBack callBack) {
		if (webview == null || TextUtils.isEmpty(actionName)) {
			callBack.errorMsg(new NullPointerException("The webview or actionName is empty"));
			return;
		}
		String callUrl = null;
		if (params == null || params.size() == 0) {
			callUrl = "javascript:" + actionName + "()";
		} else {
			JSONObject object = new JSONObject();
			Set<Entry<String, String>> entrySet = params.entrySet();
			for (Entry<String, String> entry : entrySet) {
				try {
					if (TextUtils.isEmpty(entry.getKey())) {
						callBack.errorMsg(new NullPointerException("The json key can't be empty"));
						return;
					}
					object.put(entry.getKey(), entry.getValue());
				} catch (JSONException e) {
					callBack.errorMsg(e);
					e.printStackTrace();
				}
			}
			callUrl = "javascript:" + actionName + "('" + object.toString() + "')";
			System.out.println(callUrl);
		}
		callJsRequest(webview, callUrl);
	}

	private void initData(HybridCallBack callBack, String url) throws Exception {

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
		// ArrayList<Object> list = callBack.moreParams(actionName, param);
		// if (list == null || list.size() == 0) {
		// // performAction(callBack);
		// callBack.errorMsg(new NullPointerException("Parameters are
		// uninitialized"));
		// return;
		// }

		String[] split = substring.split("&");
		if (split == null || split.length == 0) {
			callBack.getOldParams("");
			callBack.getNewParams("");
			performAction(callBack);
			return;
		}
		callBack.getOldParams(substring);
		JSONObject object = new JSONObject();
		for (String string : split) {
			String[] strings = string.split("=");
			String key = strings[0];
			if (TextUtils.isEmpty(key)) {
				callBack.errorMsg(new NullPointerException("The json key can't be empty"));
				return;
			}
			object.put(key, strings[1]);
		}
		callBack.getNewParams(object.toString());
		Log.i("TAG", object.toString());
		dispatch(object.toString(), callBack);
	}

	private void dispatch(String params, HybridCallBack callBack) throws Exception {
		for (Method method : methods) {
			if (method.getName().startsWith(actionName) && method.getParameterTypes().length == 1) {
				method.invoke(action, params);
				return;
			}
		}
		callBack.errorMsg(new NullPointerException("The method name was not found"));
	}

	private boolean checkJson(String value) {
		try {
			new JSONObject(value);
		} catch (JSONException e) {
			return false;
		}
		return true;
	}

}
