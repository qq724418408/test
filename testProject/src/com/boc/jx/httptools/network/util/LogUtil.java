package com.boc.jx.httptools.network.util;//package test.com.web.xinqing.dynamicurldemo.network.util;

import android.util.Log;

/**
 * Created by XinQingXia on 2017/8/12.
 */

public class LogUtil {

    private static boolean defaultLog;
    private static boolean autoLog;
    private static final String TAG = "HttpLog";

    public static void setDefaultLog(boolean defaultLog) {
        LogUtil.defaultLog = defaultLog;
    }

    public static void setAutoLog(boolean autoLog) {
        LogUtil.autoLog = autoLog;
    }

    public static void e(String msg) {
        if (defaultLog) {
            logD(msg);
        } else if (autoLog) {
            logA(msg);
        }

    }

    private static void logD(String str) {
        StackTraceElement element = getCallerStackTraceElement();
        Log.e(TAG, getMsg(element, str));
    }

    private static void logA(String str) {
        StackTraceElement element = getCallerStackTraceElement();
        Log.e(getTag(element), getMsg(element, str));
    }

    private static String getMsg(StackTraceElement element, String str) {
        return getPosition(element) + str;
    }

    private static StackTraceElement getCallerStackTraceElement() {
        return Thread.currentThread().getStackTrace()[4];
    }

    private static String getPosition(StackTraceElement caller) {
        String tag = "%s.%s(Line:%d)"; // 占位符
        String callerClazzName = caller.getClassName(); // 获取到类名
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        tag = String.format(tag, callerClazzName, caller.getMethodName(), caller.getLineNumber()); // 替换
        return tag;
    }

    private static String getTag(StackTraceElement caller) {
        String callerClazzName = caller.getClassName(); // 获取到类名
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        return callerClazzName;
    }

    private static int getLine(StackTraceElement caller) {
        return caller.getLineNumber();
    }
}
