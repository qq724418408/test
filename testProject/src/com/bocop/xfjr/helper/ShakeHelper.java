package com.bocop.xfjr.helper;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

/**
 * 摇一摇
 */
public class ShakeHelper implements SensorEventListener {

	private Context mContext;
	// 传感器管理器
	private SensorManager mSensorManager;
	// 传感器
	private Sensor mSensor;
	// 速度阀值
	private int mSpeed = 3000;
	// 时间间隔
	private int mInterval = 50;
	// 上一次摇晃的时间
	private long LastTime;
	// 上一次的x、y、z坐标
	private float LastX, LastY, LastZ;
	private Vibrator mVibrator;
	public ShakeHelper(Context context) {
		this.mContext = context;
		 mVibrator = (Vibrator) context.getSystemService(Activity.VIBRATOR_SERVICE);
		start();
	}

	public void start() {
		mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
		if (mSensorManager != null) {
			mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		}
		if (mSensor != null) {
			mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_GAME);
		}
	}

	public void stop() {
		mSensorManager.unregisterListener(this);
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {

	}

	@Override
	public void onSensorChanged(SensorEvent Event) {
		long NowTime = System.currentTimeMillis();
		if ((NowTime - LastTime) < mInterval)
			return;
		// 将NowTime赋给LastTime
		LastTime = NowTime;
		// 获取x,y,z
		float NowX = Event.values[0];
		float NowY = Event.values[1];
		float NowZ = Event.values[2];
		// 计算x,y,z变化量
		float DeltaX = NowX - LastX;
		float DeltaY = NowY - LastY;
		float DeltaZ = NowZ - LastZ;
		// 赋值
		LastX = NowX;
		LastY = NowY;
		LastZ = NowZ;
		// 计算
		double NowSpeed = Math.sqrt(DeltaX * DeltaX + DeltaY * DeltaY + DeltaZ * DeltaZ) / mInterval * 10000;
		// 判断
		if (NowSpeed >= mSpeed) {
			mVibrator.vibrate(300);
//			Toast.makeText(mContext, getAppVersion(), Toast.LENGTH_SHORT).show();
//			Toast.makeText(mContext, "版本号：1.0.0", Toast.LENGTH_SHORT).show(); //TODO 2017年11月16日 更新
			Toast.makeText(mContext, "版本号：1.0.1", Toast.LENGTH_SHORT).show(); //TODO 2017年12月04日 更新
		}
	}
	
	/** 
	 * 返回当前程序版本名 
	 */  
	public String getAppVersion() {  
	    String versionName = ""; 
	    int versioncode = 0;
	    try {  
	        // ---get the package info---  
	        PackageManager pm = mContext.getPackageManager();  
	        PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), 0);  
	        versionName = pi.versionName;  
	        versioncode = pi.versionCode;
	        if (versionName == null || versionName.length() <= 0) {  
	            return "";  
	        }  
	    } catch (Exception e) {  
	        Log.e("VersionInfo", "Exception", e);  
	    }  
	    return "版本："+versionName+"\n版本号："+versioncode;  
	} 
}
