package com.bocop.xfjr.helper.carsh;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.widget.Toast;

import java.io.File;

/**
 * Description : 全局捕获未处理的异常
 * <p/>
 * Created : TIAN FENG
 * Date : 2017/3/31
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public class CarshExceptionHandler implements Thread.UncaughtExceptionHandler {
    // 错误日志保存的文件夹路径文件夹路径
    public static String CARSH_ROOT_DIR = Environment.getExternalStorageDirectory().getPath() + "/crash";
    // CarshExceptionHandler实例对象
    private static CarshExceptionHandler mInstance;
    // 获取系统默认的处理类
    private Thread.UncaughtExceptionHandler mDefaultExceptionHandler;
    private Context mContext;

    private CarshExceptionHandler() {
    }

    /**
     * 单例设计防并发
     */
    public static CarshExceptionHandler getInstance() {
        if (mInstance == null) {
            synchronized (CarshExceptionHandler.class) {
                if (mInstance == null) {
                    mInstance = new CarshExceptionHandler();
                }
            }
        }
        return mInstance;
    }

    /**
     * 绑定
     */
    public void bind(Context context) {
        this.mContext = context;
        //获取系统默认的UncaughtException处理器
        mDefaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 出现异常时进入此方法
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        // toast 提示错误
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, "很抱歉,程序出现异常,即将退出.", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }).start();
        // 判断自己是否处理成功
        if (new CarshHelper(mContext, ex).handleException(CarshHelper.SAVE_TYPE.FILE)) {
            // 自己操作成功
            // 1.1 可以开启一个服务上传错误信息
            startServiceConnectInterNet(getCarshFile());
            // 1.2 退出程序
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            exitApp();
        } else {
            // 操作失败交给系统处理
            mDefaultExceptionHandler.uncaughtException(thread, ex);
        }
    }

    /**
     * 退出App
     */
    private void exitApp() {
        int currentVersion = Build.VERSION.SDK_INT;
        if (currentVersion > Build.VERSION_CODES.ECLAIR_MR1) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        } else {
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    /**
     * 获取carsh文件夹下的文件
     */
    public static File getCarshFile() {
        return SaveCarshUtils.getFile()[0];
    }


    /**
     * 开启一个服务连接网络
     *
     * @param carshFile
     */
    protected void startServiceConnectInterNet(File carshFile) {

    }
}