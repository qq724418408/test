package com.bocop.xfjr.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;



public class CrashHandler implements UncaughtExceptionHandler {

    private static final String TAG = CrashHandler.class.getSimpleName();
    private static CrashHandler crashHandler;
    private UncaughtExceptionHandler mDefaultHandler;
    private CrashInterface crashInterface;
    private String crashDirPath;
    private boolean isInit = false;
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss", Locale.CHINA);

    public static CrashHandler getInstance() {
        if (crashHandler == null) {
            synchronized (CrashHandler.class) {
                if (crashHandler == null) {
                    crashHandler = new CrashHandler();
                }
            }
        }
        return crashHandler;
    }

    public void init(Context context, CrashInterface crashInterface) {
        if (isInit)
            return;
        isInit = true;
        this.crashInterface = crashInterface;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        if (isSdCardExist()) {
            crashDirPath = getStorageDirectory(context.getPackageName()).getAbsolutePath();
        } else {
            crashDirPath = context.getFilesDir().getAbsolutePath();
        }
        File crashDirFile = new File(crashDirPath);
        if (!crashDirFile.exists() || crashDirFile.isDirectory()) {
            crashDirFile.mkdirs();
        }
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (mDefaultHandler != null) {
            handleException(ex);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mDefaultHandler.uncaughtException(thread, ex);
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    private File getStorageDirectory(String directoryPath) {
        File file = getStorageFilePath();
        if (file != null) {
            File fileDirectory = new File(file.getAbsolutePath() + File.separator + directoryPath);
            if (!fileDirectory.exists()) {
                if (fileDirectory.mkdirs())
                    return fileDirectory;
            } else {
                return fileDirectory;
            }
        }
        return null;
    }

    private File getStorageFilePath() {
        if (isSdCardExist()) {
            return Environment.getExternalStorageDirectory();
        } else {

        }
        return null;
    }

    private boolean isSdCardExist() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        if (crashInterface != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Looper.prepare();
                    crashInterface.showCrashToast();
                    Looper.loop();
                }
            }).start();
            saveCatchInfoToFile(ex, crashInterface.getDeviceInfo());
        } else {
            saveCatchInfoToFile(ex, null);
        }
        return true;
    }

    public Map<String, String> collectDeviceInfo(Context ctx) {
        Map<String, String> deviceInfo = new HashMap<>();
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                deviceInfo.put("versionName", versionName);
                deviceInfo.put("versionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                deviceInfo.put(field.getName(), field.get(null).toString());
            } catch (Exception e) {
            }
        }
        return deviceInfo;
    }

    private void saveCatchInfoToFile(Throwable ex, Map<String, String> deviceInfo) {
        StringBuilder sb = new StringBuilder();
        if (!deviceInfo.isEmpty())
            for (Map.Entry<String, String> entry : deviceInfo.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                sb.append(key).append("=").append(value).append("\n");
            }
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        try {
            String time = formatter.format(new Date());
            String fileName = "crash-" + time + ".txt";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File dir = new File(crashDirPath);
                if (dir.exists() || dir.mkdirs()) {
                    FileOutputStream fos = new FileOutputStream(crashDirPath + File.separator + fileName);
                    fos.write(sb.toString().getBytes());
                    fos.close();
                } else {
                }
            }
        } catch (Exception e) {
        }
    }

    public interface CrashInterface {
        Map<String, String> getDeviceInfo();

        void showCrashToast();
    }
}