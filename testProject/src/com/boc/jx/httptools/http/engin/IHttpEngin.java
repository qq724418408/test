package com.boc.jx.httptools.http.engin;

import java.util.Map;

import com.boc.jx.httptools.http.callback.IProgressCallback;
import com.boc.jx.httptools.http.callback.IStringCallaBack;
import com.boc.jx.httptools.http.callback.IUpLoadCallback;

import android.content.Context;

/**
 * description： 引擎基类 封装第三方库时实现
 * <p/>
 * Created by TIAN FENG on 2017/8/13 20:39
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public abstract class IHttpEngin {

    protected IHttpEngin httpEngin;

    public IHttpEngin() {

    }

    /**
     * 处理装饰对象
     * @param engin
     */
    public IHttpEngin(IHttpEngin engin) {
        addDecortorEngin(engin);
    }

    /**
     * 装饰时提供空构造函数时使用
     */
    public void addDecortorEngin(IHttpEngin engin) {
        if (httpEngin != null) {
            engin.addDecortorEngin(httpEngin);
        }
        httpEngin = engin;
    }

    /**
     * 取消请求 被装饰引擎需重写
     */
    public void cancle(String url){
    	httpEngin.cancle(url);
    };

    public abstract void get(Context context, String url, Map<String, Object> params, IStringCallaBack callback);

    public abstract void post(Context context, String url, Map<String, Object> params, IStringCallaBack callback);

    public abstract void downLoad(Context context, String url, Map<String, Object> params, String fileName, IProgressCallback callback);

    public abstract void upLoad(Context context, String url, Map<String, Object> params, IUpLoadCallback callback);

}
