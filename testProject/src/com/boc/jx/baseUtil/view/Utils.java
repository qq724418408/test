package com.boc.jx.baseUtil.view;

import android.view.View;

public class Utils {
	// 两次点击按钮之间的点击间隔不能少于1000毫秒
    private static final int MIN_CLICK_DELAY_TIME = 500;
    private static long lastClickTime;
    private static int viewId = -1;
    
    private static String sTag = "";


    public static boolean isFirstClick(int id) {

        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME||id!=viewId) {
            flag = true;
        }
        lastClickTime = curClickTime;
        viewId = id;
        return flag;
    }
    
    
    public static boolean isFirstClick(String tag) {

        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME||!sTag.equals(tag)) {
            flag = true;
        }
        lastClickTime = curClickTime;
        sTag = tag;
        return flag;
    }
}
