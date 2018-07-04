package com.bocop.xfjr.helper.carsh;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Description : 读取手机信息
 * Created : TIAN FENG
 * Date : 2017/3/31
 * Email : 27674569@qq.com
 * Version : 1.0
 */
class CarshHelper {
    public enum SAVE_TYPE {
        DB, FILE
    }

    private Context mContext;
    private Throwable mEx;
    //用来存储设备信息和异常信息
    private List<String> mInfos;

    public CarshHelper(Context context, Throwable ex) {
        this.mContext = context;
        this.mEx = ex;
        mInfos = new ArrayList<>();
    }

    /**
     * 处理信息
     *
     * @return 是否处理成功
     */
    public boolean handleException(SAVE_TYPE type) {
        if (mEx == null) {
            // 没有处理
            return false;
        }
        return collectErrorMessage(type);
    }

    /**
     * 收集参数信息
     *
     * @return 是否收集成功
     */
    private boolean collectErrorMessage(SAVE_TYPE type) {
        try {
            // 收集app信息
            collectAppInfo();
            // 手机信息
            collenctMobileInfo();
            // 错误信息
            collenctErrorInfo();
            if (type == SAVE_TYPE.FILE) {
                // 保存文件
                saveErrorMessageToFile();
            } else {
                saveErrorMessageToDB();
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }


    /**
     * 收集 设备信息
     */
    private void collectAppInfo() throws Exception {
        PackageManager mPackageManager = mContext.getPackageManager();
        PackageInfo mPackageInfo = mPackageManager.getPackageInfo(
                mContext.getPackageName(), PackageManager.GET_ACTIVITIES);

        mInfos.add("------app信息------");
        mInfos.add("\n");
        mInfos.add("版本名称 : ");
        mInfos.add(mPackageInfo.versionName);
        mInfos.add("\n");
        mInfos.add("版本号 : ");
        mInfos.add("" + mPackageInfo.versionCode);
        mInfos.add("\n");
    }

    /**
     * 收集手机信息
     */
    private void collenctMobileInfo() throws Exception {
        mInfos.add("------设备信息------");
        mInfos.add("\n");
        // 利用反射获取 Build 的所有属性
        Class<?> clazz = Build.class;
        // 手机厂商
        Field manufacturer = clazz.getDeclaredField("MANUFACTURER");
        manufacturer.setAccessible(true);
        String valueManu = manufacturer.get(null).toString();
        mInfos.add("手机厂商:");
        mInfos.add(valueManu);
        mInfos.add("\n");
        // 手机型号
        Field model = clazz.getDeclaredField("MODEL");
        model.setAccessible(true);
        String valuemodel = model.get(null).toString();
        mInfos.add("手机型号:");
        mInfos.add(valuemodel);
        mInfos.add("\n");
        // CPU
        Field CPU_ABI = clazz.getDeclaredField("CPU_ABI");
        CPU_ABI.setAccessible(true);
        String CPU = CPU_ABI.get(null).toString();
        mInfos.add("CPU:");
        mInfos.add(CPU);
        mInfos.add("\n");

        // 设备
        Field DEVICE = clazz.getDeclaredField("DEVICE");
        DEVICE.setAccessible(true);
        String dee = DEVICE.get(null).toString();
        mInfos.add("设备:");
        mInfos.add(dee);
        mInfos.add("\n");
    }

    /**
     * 收集异常信息
     */
    private void collenctErrorInfo() {
        mInfos.add("------主要错误信息------");
        mInfos.add("\n");
        StackTraceElement element = mEx.getStackTrace()[0];
        mInfos.add("类名 : ");
        mInfos.add(element.getClassName());
        mInfos.add("\n");
        mInfos.add("方法名 : ");
        mInfos.add(element.getMethodName());
        mInfos.add("\n");
        mInfos.add("行数 : ");
        mInfos.add(element.getLineNumber() + "行");
        mInfos.add("\n");
        mInfos.add("错误原因 : ");
        mInfos.add(mEx.getMessage());
        mInfos.add("\n");
    }

    /**
     * 保存错误信息到文件
     */
    private void saveErrorMessageToFile() throws IOException {
        String message = getErrorMessage();
        SaveCarshUtils.saveToFile(message);
    }


    /**
     * 保存错误信息到数据库
     */
    private void saveErrorMessageToDB() {
        // 保存到数据库时 复写此方法
        //String message = getErrorMessage();
    }

    /**
     * 获取错误字符串
     */
    private String getErrorMessage() {
        StringBuilder sb = new StringBuilder();
        for (String string : mInfos) {
            sb.append(string);
        }
        sb.append("----详细错误信息----").append("\n");
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        mEx.printStackTrace(printWriter);
        Throwable cause = mEx.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result).append("\n");
        return sb.toString();
    }
}
