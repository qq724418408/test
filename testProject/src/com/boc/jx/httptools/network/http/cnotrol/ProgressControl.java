package com.boc.jx.httptools.network.http.cnotrol;


import com.boc.jx.httptools.network.listener.Progress;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;



/**
 * Created by XinQingXia on 2017/8/12
 */

public class ProgressControl {

    protected void show(Progress progress) {
        if (isNotEmpty(progress))
            progress.show();
    }

    protected void showNoData(Progress progress) {
        if (isNotEmpty(progress))
            progress.dataEmpty();
    }

    protected void showError(Progress progress, int code) {
        if (isNotEmpty(progress))
            progress.faild(code);
    }

    protected void dismiss(Progress progress) {
        if (isNotEmpty(progress))
            progress.dismiss();
    }

    private boolean isNotEmpty(Progress progress) {
        if (progress == null)
            return false;
        return true;
    }


    protected boolean hasNetwork(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        }
        return false;
    }

    protected void logMsg(String msg) {

    }
}
