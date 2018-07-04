package com.boc.jx.baseUtil.net;

import android.app.Dialog;
import android.content.Context;

/**
 * Created by hwt on 14/11/4.
 */
public class LoadingDailog extends Dialog {

    public LoadingDailog(Context context) {
        super(context);
    }

    public LoadingDailog(Context context, int theme) {
        super(context, theme);
    }

    protected LoadingDailog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
}
