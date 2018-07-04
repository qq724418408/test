package com.bocop.xfjr.util.image.modle;

/**
 * description：
 * <p/>
 * Created by TIAN FENG on 2017/8/30
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public interface LoaderListener {

    void onSuccess();

    void onError();

    void onProgress(int current,int total);
}
