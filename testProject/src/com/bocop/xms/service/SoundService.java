/**
 * 
 */
package com.bocop.xms.service;

import com.bocop.jxplatform.R;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

/** 
 * @author luoyang  E-mail: luoyang8714@163.com
 * @version 创建时间：2016-12-19 上午10:33:55 
 * 类说明 
 */
/**
 * @author zhongye
 * 
 */
public class SoundService extends Service {

	private MediaPlayer mp;

	@Override
	public void onCreate() {
		super.onCreate();
		mp = MediaPlayer.create(this, R.raw.birthday);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onDestroy()
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		mp.release();
		stopSelf();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		boolean playing = intent.getBooleanExtra("playing", false);
		if (playing) {
			Log.i("tag", "true playing");
			mp.start();
		} else {
			if(mp != null){
				Log.i("tag", "pause playing");
				mp.pause();
			}
			
		}
		return super.onStartCommand(intent, flags, startId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
