package com.bocop.xms.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;

import com.bocop.jxplatform.util.LoginUtil;
import com.bocop.xms.push.receiver.TickAlarmReceiver;
import com.bocop.xms.push.util.SimpleNotifation;
import com.bocop.xms.receiver.NotificationReceiver;
import com.bocop.xms.utils.SharedPreferencesUtils;
import com.bocop.xms.xml.remind.EventBean;

/**
 * 闹钟后台服务
 * 
 * @author ftl
 *
 */
public class AlarmService extends Service {

	public static final String ALARM_SER = "alarm.service";
	public static final String TAG = "AlarmService";
	private PendingIntent tickPendIntent;
	private WakeLock wakeLock;
	private SharedPreferencesUtils spf;
	private MyReceiver myReceiver;

	public AlarmService() {
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "onCreate");
		this.setTickAlarm();
		PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
		wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
		spf = new SharedPreferencesUtils(this, AlarmService.ALARM_SER);
		initReceiver();
	}

	@Override
	public int onStartCommand(Intent param, int flags, int startId) {
		Log.i(TAG, "onStartCommand");
		return Service.START_STICKY;
	}
	
	/**
	 * 初始化广播接收
	 */
	public void initReceiver() {
		myReceiver = new MyReceiver();
		IntentFilter filter = new IntentFilter();  
        filter.addAction(Intent.ACTION_TIME_TICK);  
        registerReceiver(myReceiver, filter);  
	}
	
	public class MyReceiver extends BroadcastReceiver {
		
		@SuppressWarnings("unchecked")
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i(TAG, "onReceive");
			Log.i(TAG, "userId:" + LoginUtil.getUserId(context));
			String action = intent.getAction();
			if (Intent.ACTION_TIME_TICK.equals(action)) {
				List<EventBean> list = (List<EventBean>) spf.getObject(LoginUtil.getUserId(context), EventBean.class);
				if (list != null) {
					for (int i = 0; i < list.size(); i++) {
						EventBean eventBean = list.get(i);
						if (eventBean != null) {
							if ("0".equals(eventBean.getType())) {//永不重复
								if (getCurrentDateTime().equals(eventBean.getRepeatValue())) {
									notifyRunning(eventBean.getContent());
								}
							} else if ("1".equals(eventBean.getType())) {//每天
								String time = eventBean.getRepeatValue();
								Log.i(TAG, "每天:" + time);
								if (getCurrentTime().equals(time)) {
									notifyRunning(eventBean.getContent());
								}
							} else if ("2".equals(eventBean.getType())) {//每周
								Calendar cal = Calendar.getInstance();
								//获取星期
								int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;	
								String saveWeekDay = eventBean.getRepeatValue();
								Log.i(TAG, "保存星期:" + saveWeekDay);
								Log.i(TAG, "当前星期:" + currentWeekDay);
								if (String.valueOf(currentWeekDay).equals(saveWeekDay)){
									if (getCurrentTime().equals(eventBean.getRemindtime())) {
										notifyRunning(eventBean.getContent());
									}
								}
							} else if ("3".equals(eventBean.getType())) {//每月
								String saveDay = eventBean.getRepeatValue();
								String currentDay = getCurrentDateTime().substring(8, 10);
								Log.i(TAG, "保存号数:" + saveDay);
								Log.i(TAG, "当前号数:" + currentDay);
								if (saveDay.equals(currentDay)) {
									if (getCurrentTime().equals(eventBean.getRemindtime())) {
										notifyRunning(eventBean.getContent());
									}
								}
							}
						}
					}
				} 
	        }
		}
		
	}
	
	/**
	 * 获取当前日期时间
	 * @return
	 */
	protected String getCurrentDateTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		return sdf.format(new Date());
	}
	
	/**
	 * 获取当前日期
	 * @return
	 */
	protected String getCurrentDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(new Date());
	}
	
	/**
	 * 获取当前时间
	 * @return
	 */
	protected String getCurrentTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		return sdf.format(new Date());
	}
	
	/**
	 * 获取保存日期
	 * @return
	 */
	protected String getSaveDate(String time) {
		if (time != null && time.length() == 16) {
			time = time.substring(0, 10);
		}
		return time;
	}
	
	/**
	 * 获取保存时间
	 * @return
	 */
	protected String getSaveTime(String time) {
		if (time != null && time.length() == 16) {
			time = time.substring(11, 16);
		}
		return time;
	}
	
	protected void setTickAlarm() {
		AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(this, TickAlarmReceiver.class);
		int requestCode = 0;
		tickPendIntent = PendingIntent.getBroadcast(this, requestCode, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		// 小米2s的MIUI操作系统，目前最短广播间隔为5分钟，少于5分钟的alarm会等到5分钟再触发！2014-04-28
		long triggerAtTime = System.currentTimeMillis();
		int interval = 300 * 1000;
		// alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, triggerAtTime,
		// AlarmManager.INTERVAL_FIFTEEN_MINUTES, tickPendIntent);
		alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, triggerAtTime, interval,
				tickPendIntent);
	}

	protected void cancelTickAlarm() {
		AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		alarmMgr.cancel(tickPendIntent);
	}

	protected void notifyRunning(String content) {
		Intent broadcastIntent = new Intent(this, NotificationReceiver.class);
		broadcastIntent.putExtra(TAG, TAG);
        PendingIntent pendingIntent = PendingIntent.
                getBroadcast(this, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		SimpleNotifation.getInstance(this).notifyMsg(0, "中国银行 易惠通", content, "", pendingIntent);

	}

	protected void cancelNotifyRunning() {
		NotificationManager notificationManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(0);
	}

	@Override
	public void onDestroy() {
		Log.i(TAG, "onDestroy");
		unregisterReceiver(myReceiver);
		super.onDestroy();
		// this.cancelTickAlarm();
		cancelNotifyRunning();
		tryReleaseWakeLock();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	protected void tryReleaseWakeLock() {
		if (wakeLock != null && wakeLock.isHeld() == true) {
			wakeLock.release();
		}
	}

}
