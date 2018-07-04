package com.boc.jx.httptools.network;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.boc.jx.httptools.network.base.BaseResponse;
import com.boc.jx.httptools.network.base.ContentResponse;
import com.boc.jx.httptools.network.base.Function;
import com.boc.jx.httptools.network.callback.CallBack;
import com.boc.jx.httptools.network.callback.HttpCallBack;
import com.boc.jx.httptools.network.callback.HttpUtilsCallBak;
import com.boc.jx.httptools.network.callback.StringCallBack;
import com.boc.jx.httptools.network.http.DConfing;
import com.boc.jx.httptools.network.http.HttpUtils;
import com.boc.jx.httptools.network.http.build.FileBuilder;
import com.boc.jx.httptools.network.http.build.FileTypeBuilder;
import com.boc.jx.httptools.network.http.build.FunctionBuild;
import com.boc.jx.httptools.network.http.build.HttpBuild;
import com.boc.jx.httptools.network.http.build.SimpleBuilder;
import com.boc.jx.httptools.network.http.cnotrol.DyanmicData;
import com.boc.jx.httptools.network.http.cnotrol.HttpQueue;
import com.boc.jx.httptools.network.http.task.FunctionsTask;
import com.boc.jx.httptools.network.listener.HttpQueueListener;
import com.boc.jx.httptools.network.util.LogUtil;
import com.boc.jx.tools.LogUtils;
import com.bocop.xfjr.config.UrlConfig;
import com.bocop.xfjr.util.PreferencesUtil;
import com.bocop.xfjr.util.enrypt.RSA;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.internal.http.OkHeaders;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

/**
 * Created by XinQingXia on 2017/8/12.
 */

public class Executors extends FunctionsTask {

	/**
	 * 基础配置参数
	 */
	private static FunctionBuild initBuild;
	private static DyanmicData dyanmicData = new DyanmicData();
	private static Application app;
	private volatile boolean isRunning = false;// 当前是否有任务正在执行
	private boolean logining = false;// 是否正在执行登录操作
	private HttpQueueListener queueListener;
	private String urlID;
	private HttpBuild build;

	private static final Executors instance = new Executors();

	private Executors() {
	}

	public static Executors getInstance() {
		return instance;
	}

	public static SimpleBuilder simpleBuilder(Context ctx) {
		initState();
		return new SimpleBuilder(ctx, instance);
	}

	public static FileBuilder fileBuilder(Context ctx) {
		initState();
		return new FileBuilder(ctx, instance);
	}

	public static FileTypeBuilder fileTypeBuilder(Context ctx) {
		initState();
		return new FileTypeBuilder(ctx, instance);
	}

	public static FunctionBuild init(Application application) {
		Executors.app = application;
		return new FunctionBuild(instance);
	}

	private static void initState() {
		if (initBuild == null)
			throw new NullPointerException("");
	}

	/**
	 * 网络请求
	 *
	 * @param build
	 *            参数
	 */
	public synchronized void postAsync(HttpBuild build) {
		if (!hasNetwork(build.getContext())) {
			callError(DConfing.ErrorFromNet, DConfing.NOINTNET);
			return;
		}
		if (isRunning) {
			LogUtils.e("任务繁忙--添加至请求队列" + build.getId());
			HttpQueue.getInstance().addRequest(build);
//			build.getCallback().onError("", "", DConfing.TaskBusyCode);
			return;
		}
		initHttp(build);
	}

	protected boolean hasNetwork(Context context) {
		try {
			ConnectivityManager connectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivityManager != null) {
				// 获取NetworkInfo对象
				NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
				if (networkInfo != null && networkInfo.length > 0) {
					for (int i = 0; i < networkInfo.length; i++) {
						// 判断当前网络状态是否为连接状态
						if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
							return true;
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	// public static DownLoadBuild downloadFile() {
	// return new DownLoadBuild();
	// }

	private synchronized void initHttp(HttpBuild build) {
		if (build.getCallback() == null) {
			isRunning = false;
			throw new NullPointerException("callBack must not be empty");
		}
		urlID = build.getId();
		this.build = build;
		isRunning = true;
		LogUtils.e("开始请求--" + build.getId());
		if (dyanmicData.getFunctions() == null || dyanmicData.getFunctions().isEmpty()) {
			getFunctions();
		} else {
			next();
		}
	}

	@Override
	public void getFunctions() {
		Map<String, String> map = dyanmicData.getfParam();
		if (map != null && !map.isEmpty())
			build.getParams().putAll(map);
		super.getFunctions();
		removeFParam();
	}

	@Override
	public void next() {
		if (build.isCheckLoginState() && !initBuild.getLoginState()) {
			if (initBuild.getLoginTokenId() == null)
				throw new NullPointerException("If you check the login status , LoginID must not be empty");
			build.url(initBuild.getLoginTokenId()).getParams().clear();
			build.addParams("accessToken", getLoginToken());
			// if (!logining) {
			// OkHttp2Engin.ssession = "";
			// }
			logining = true;
		} else {
			// if (logining) {
			// OkHttp2Engin.ssession = "";
			// }
			logining = false;
		}
		LogUtil.e("下一步---校验登录状态-->" + logining + build.isCheckLoginState() + build.getId());
		String url = null;
		List<Function> functions = dyanmicData.getFunctions();
		if (functions != null && functions.size() > 0) {
			for (Function function : functions) {
				if (build.getId().equals(function.getId())) {
					url = function.getUrl();
					break;
				}
			}
		}
		if (url != null) {
			build.setUrlKey(url);
			if (dyanmicData.isDynamicState()) {
				build.getParams().put("key___",true);	
				getTargetUrl();
				build.getParams().put("key___",false);	
			} else {
				build.getParams().put("key___",false);	
				postRequest();
			}
		} else { // dynamic url exception -- don't find this APP_ID
			callError(UrlConfig.dynamicUrlExceptionCode, DConfing.SERVERERROR);
		}
	}

	@Override
	public void postRequest() {
		LogUtils.e("开始请求真实url");
		String url = initBuild.getServerUrl() + build.getParams().get("key");
		
		/*
		 * if (build instanceof FileBuilder) { filePost(url); } else if (build
		 * instanceof FileTypeBuilder) { fileTypePost(url); } else {
		 * simplePost(url); }
		 */
		if (build.getMethod() == HttpBuild.Method.UPLOAD) {
			filePost(url);
		} else {
			simplePost(url);
		}
	}

	private void filePost(String url) {
		HttpUtils.sendRequestWithFile(build.getContext(), url, build.getParams(), new HttpUtilsCallBak() {
			@Override
			public void onSuccess(String response) {
				success(response);
			}

			@Override
			public void onError(String errorMsg, String e, int code) {
				callError(errorMsg + e, code);
			}
		});
	}

	private void simplePost(String url) {
		build.getParams().putAll(addCommParam());
		HttpUtils.sendRequest(build.getContext(), url, build.getId(), build.getParams(), new HttpUtilsCallBak() {
			@Override
			public void onSuccess(String response) {
				LogUtil.e(response);
				success(response);
			}

			@Override
			public void onError(String errorMsg, String e, int code) {
				LogUtils.e("errorMsg-->"+errorMsg);
				LogUtils.e("e-->"+e);
				LogUtils.e("code-->"+code);
				callError(errorMsg, code);
			}
		});
	}

	private <T> void success(String response) {
		setRunning(false);
		if (build.getId().equals(initBuild.getLogOutId())) {
			HttpQueue.getInstance().removeAllHttp();
			loginFailed();
		}
		boolean especialCode = checkEspecialCode(response);
		if (especialCode) {
			LogUtils.e("特殊码------->");
			Executors executors = build.getExecutors();
			executors.setRunning(false);
			executors.loginFailed();
			HttpQueue.getInstance().removeAllHttp();
			build.getCallback().loginFailed(response, DConfing.ESPECIALCODE);
			setBuild(null);
			return;
		}
		BaseResponse baseResponse = null;
		try {
			baseResponse = JSON.parseObject(response, BaseResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
			LogUtils.e("Exception：JSON.parseObject(response, BaseResponse.class)------->");
			callError(DConfing.NeedReLogin, DConfing.SERVERERROR);
//			callError(DConfing.ErrorFromServer, DConfing.SERVERERROR);
			//callError(e.getMessage(), DConfing.SERVERERROR);
		}
		if (baseResponse != null) {
			if ("success".equals(baseResponse.getResult())) {
				dyanmicData.setDynamicToken(baseResponse.getDYNAMIC_TOKEN());
				CallBack callback = build.getCallback();
				ContentResponse contentResponse = baseResponse.getMessage();
				String rtnCode = contentResponse.getRtnCode();
				if (UrlConfig.successCode.equals(rtnCode)) {
					String publicKey = null;
					String accessToken = null;
					try {
						dyanmicData.setEntry(baseResponse.getMessage().getDynamicUri().getEntry());
						dyanmicData.setFunctions(contentResponse.getFunctions());
						publicKey = contentResponse.getPublicKey();
						accessToken = contentResponse.getAccessToken();
					} catch (Exception e) {
					}
					loginSuccessed(accessToken, publicKey);
					if (callback instanceof HttpCallBack) {
						HttpCallBack httpCallBack = (HttpCallBack) callback;
						T object = JSON.parseObject(baseResponse.getMessage().getContent(), httpCallBack.getType());
						if (logining) {
							httpCallBack.loginSuccessed();
							setBuild(null);
						} else {
							httpCallBack.onResponse(object);
							setBuild(null);
							HttpQueue.getInstance().next();
						}
					} else if (callback instanceof StringCallBack) {
						StringCallBack stringCallBack = (StringCallBack) callback;
						if (logining) {
							stringCallBack.loginSuccessed();
							setBuild(null);
						} else {
							stringCallBack.onResponse(contentResponse.getContent());
							setBuild(null);
							HttpQueue.getInstance().next();
						}
					}
				} else if (UrlConfig.emptyCode.equals(rtnCode)) {
					if (logining) {
						loginFailed();
						HttpQueue.getInstance().removeAllHttp();
						callback.loginFailed(contentResponse.getRtnMsg(), DConfing.SERVERERROR);
						setBuild(null);
					} else {
						callback.onEmpty(contentResponse.getRtnMsg());
						setBuild(null);
						HttpQueue.getInstance().next();
					}
				} else {
					if (logining) {
						loginFailed();
						HttpQueue.getInstance().removeAllHttp();
						callback.loginFailed(contentResponse.getRtnMsg(), DConfing.SERVERERROR);
						setBuild(null);
					} else {
						callback.onEspecialCode(rtnCode, contentResponse.getContent(), contentResponse.getRtnMsg());
						setBuild(null);
						HttpQueue.getInstance().next();
					}
				}
			} else {
				callError(response, DConfing.SERVERERROR);
			}
		}
	}

	private void removeFParam() {
		Map<String, String> fParam = dyanmicData.getfParam();
		Map<String, Object> params = build.getParams();
		if (fParam != null && !fParam.isEmpty() && params != null && !params.isEmpty()) {
			for (String key : fParam.keySet()) {
				if (params.containsKey(key))
					params.remove(key);
			}
		}
	}

	/**
	 * 登录成功修改登录状态
	 *
	 * @param loginToken
	 */
	public void loginSuccessed(String accessToken, String publicKey) {
		if (TextUtils.isEmpty(accessToken))
			return;
		LogUtils.e("--登录成功--");
		initBuild.loginState(true);
		PreferencesUtil.put(DConfing.ACCESS_TOKEN, accessToken); // accessToken是加密了的
		PreferencesUtil.put(DConfing.PUBLICK_KEY, publicKey); // 用于解密
	}

	/**
	 * 清空登录信息修改为未登录状态
	 */
	public void loginFailed() {
		LogUtils.e("--登录状态失效--");
		initBuild.loginState(false);
		clearDynamicData();
		PreferencesUtil.put(DConfing.ACCESS_TOKEN, "");
		PreferencesUtil.put(DConfing.PUBLICK_KEY, "");
	}

	public DyanmicData getDyanmicData() {
		return dyanmicData;
	}

	public void clearDynamicData() {
		dyanmicData.setFunctions(null).setDynamicState(false).setEntry(null).setDynamicToken(null).setHeardToken(null);
	}

	public String getLoginToken() {
		String token = PreferencesUtil.get(DConfing.ACCESS_TOKEN, "");
		String publicKey = PreferencesUtil.get(DConfing.PUBLICK_KEY, "");
		token = RSA.decryptByPublicKey(token, publicKey); // 在这里解密
		LogUtils.e("解密的token：" + token);
		return token == null ? "" : token;
	}

	public void cancelThis() {
		if (TextUtils.isEmpty(urlID))
			return;
		if (dyanmicData.getFunctions() == null) {
			return;
		}
		for (Function function : dyanmicData.getFunctions()) {
			if (urlID.endsWith(function.getId())) {
				urlID = function.getUrl();
				break;
			}
		}
		HttpUtils.cancelRequest(initBuild.getServerUrl() + urlID);
	}

	public void cancelThis(String urlID) {
		if (TextUtils.isEmpty(urlID) || dyanmicData.getFunctions() == null)
			return;
		for (Function function : dyanmicData.getFunctions()) {
			if (urlID.endsWith(function.getId())) {
				urlID = function.getUrl();
				break;
			}
		}
		HttpUtils.cancelRequest(initBuild.getServerUrl() + urlID);
	}

	/**********************************
	 * set/get
	 *********************************/

	public void setRunning(boolean running) {
		isRunning = running;
	}

	public boolean isLogining() {
		return logining;
	}

	public FunctionBuild getInitBuild() {
		return initBuild;
	}

	public void setInitBuild(FunctionBuild initBuild) {
		this.initBuild = initBuild;
	}

	public HttpBuild getBuild() {
		return build;
	}

	public void setBuild(HttpBuild build) {
		if (!isRunning)
			this.build = build;
	}

	@SuppressLint("NewApi")
	public void setQueueListener() {
		if (queueListener == null) {
			queueListener = HttpQueueListener.getIntance();
			app.registerActivityLifecycleCallbacks(queueListener);
		} else {
			LogUtils.e("监听已存在");
		}
	}
}