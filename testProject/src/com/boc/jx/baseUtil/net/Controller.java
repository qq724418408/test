package com.boc.jx.baseUtil.net;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.base.BaseApplication;
import com.boc.jx.baseUtil.asynchttpclient.AsyncHttpClient;
import com.boc.jx.baseUtil.logger.Logger;

import java.util.Map;

/**
 * Created by hwt on 15/1/19.
 * <p/>
 * 创建网络请求工具
 */
public class Controller {
    private static Controller controller;
    private HttpConfig httpConfig;//网络请求参数配置
    private AsyncHttpClient httpClient; //网络请求工具

    private Controller() {}



    /**
     * 创建 controller
     *
     * @return
     */
    public static Controller getInstance() {
        if (controller == null) {
            controller = new Controller();
            controller.httpClient = HttpUtil.getInstance().getClient();
            controller.httpClient.setMaxConnections(5);
        }
        return controller;
    }

    /**
     * 创建 controller
     *
     * @param httpConfig
     * @return
     */
    public static Controller getInstance(HttpConfig httpConfig) {
        if (httpConfig == null) return getInstance();
        if (controller == null) {
            controller = new Controller();
            controller.httpConfig = httpConfig;
            controller.httpClient = HttpUtil.getInstance(controller.httpConfig).getClient();
            controller.httpClient.setMaxConnections(5);
        }
        return controller;
    }

    /**
     * 创建并初始化 action
     *
     * @param activity
     * @param aClass
     * @return
     */
    public <T> T getTargetAction(BaseActivity activity,
                                 Class<T> aClass) {
        return getTargetAction(activity, activity, aClass);
    }

    /**
     * 创建并初始化 action
     *
     * @param activity
     * @param uiObject
     * @param aClass
     * @return
     */
    public <T> T getTargetAction(BaseActivity activity,
                                 UiObject uiObject,
                                 Class<T> aClass) {
        HttpRequest request = HttpRequest.getNewHttpRequest(activity, httpConfig, httpClient);
        Request.registerHttp(request);
        return createTargetAction(aClass, uiObject, (BaseApplication) activity.getApplication());
    }

    /**
     * @param aClass
     * @param uiObject
     * @param baseApplication
     * @param <T>
     * @return
     */
    private static <T> T createTargetAction(Class<T> aClass,
                                            UiObject uiObject,
                                            BaseApplication baseApplication) {
        if (aClass != null) {
            try {
                Action action = null;
                if (Action.class.isAssignableFrom(aClass)) {
                    action = ((Action) aClass.newInstance()).init(uiObject, baseApplication);
                }
                uiObject.addAction(action);
                return aClass.cast(action);
            } catch (Exception e) {
                Logger.d(e.getMessage(), e);
            }
        }
        return null;
    }

    /**
     * 创建并初始化 action
     *
     * @param activity
     * @param aClass
     * @return
     */
    public Action getAction(BaseActivity activity,
                            Class<? extends Action> aClass) {
        return getAction(activity, (BaseApplication) activity.getApplication(), activity, aClass);
    }

    /**
     * 创建并初始化 action
     *
     * @param activity
     * @param uiObject
     * @param aClass
     * @return
     */
    public Action getAction(BaseActivity activity,
                            UiObject uiObject,
                            Class<? extends Action> aClass) {
        return getAction(activity, (BaseApplication) activity.getApplication(), uiObject, aClass);
    }

    /**
     * 创建并初始化 action
     *
     * @param context
     * @param application
     * @param uiObject
     * @param aClass
     * @return
     */
    public Action getAction(BaseActivity context,
                            BaseApplication application,
                            UiObject uiObject,
                            Class<? extends Action> aClass) {
        HttpRequest request = HttpRequest.getNewHttpRequest(context, httpConfig, httpClient);
        Request.registerHttp(request);
        return createAction(aClass, uiObject, application);
    }

    /**
     * 创建并初始化 action
     *
     * @param aClass
     * @param uiObject
     * @param baseApplication
     * @return
     */
    private static Action createAction(Class<? extends Action> aClass,
                                       UiObject uiObject,
                                       BaseApplication baseApplication) {
        if (aClass != null) {
            try {
                Action action = aClass.newInstance().init(uiObject, baseApplication);
                uiObject.addAction(action);
                return action;
            } catch (Exception e) {
                Logger.d(e.getMessage(), e);
            }
        }
        return null;
    }

    /**
     * 创建请求队列工具
     *
     * @param context
     * @return
     */
    public HttpQueue getQueue(BaseActivity context) {
        return HttpQueue.getNewHttpQueue(context, httpConfig, httpClient);
    }

    /**
     * 创建网络请求并发工具
     *
     * @param context
     * @return
     */
    public HttpTrans getTrans(BaseActivity context) {
        return HttpTrans.getNewHttpTrans(context, httpConfig, httpClient);
    }


    /**
     * 返回 HTTP 请求工具的配置项
     *
     * @return
     */
    public HttpConfig getHttpConfig() {
        return httpConfig;
    }

    public AsyncHttpClient getHttpClient() {
        return httpClient;
    }
}
