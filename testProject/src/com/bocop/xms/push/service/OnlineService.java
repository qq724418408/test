package com.bocop.xms.push.service;

import org.ddpush.im.util.PropertyUtil;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.text.TextUtils;
import android.widget.Toast;

import com.boc.jx.base.BaseApplication;
import com.boc.jx.tools.LogUtils;
import com.bocop.jxplatform.activity.MainActivity;
import com.bocop.jxplatform.config.BocSdkConfig;
import com.bocop.jxplatform.util.CspUtil;
import com.bocop.jxplatform.util.CspUtil.CallBack;
import com.bocop.jxplatform.util.LoginUtil;
import com.bocop.xms.push.impl.MsgImpl;
import com.bocop.xms.push.listener.OnReciveMsgListener;
import com.bocop.xms.push.receiver.TickAlarmReceiver;
import com.bocop.xms.push.runnable.UdpClientImpl;
import com.bocop.xms.push.util.SimpleNotifation;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.RequestBody;

/**
 * 接收消息的后台服务
 * 
 * @author hch
 *
 */
public class OnlineService extends Service {

	public final static String NAME_CMD = "CMD";
	public final static String VALUE_TEXT = "TEXT";
	public final static String VALUE_TICK = "TICK";
	public final static String VALUE_RESET = "RESET";
	public final static String VALUE_TOAST = "TOAST";
	public final static String START_FROM = "startFrom.key";
	public static final String START_FRAGMENT_KEY = "startFragment.key";
	public static final String START_ACTIVITY_KEY = "startActivity.key";
	public static final String SECRETARY_MSG = "secretary.msg";
	
	private static final String TAG = "OnlineService";
	private static final String APP_ID = "JX001001";
	private PendingIntent tickPendIntent;
	private UdpClientImpl clientImpl;
	private WakeLock wakeLock;
	private MsgImpl msgImpl;
	private BaseApplication baseApp;
	private String UUID;
	private SharedPreferences spf;

	public OnlineService() {
	}

	@Override
	public void onCreate() {
		super.onCreate();
		LogUtils.d("OnlineService :onCreate");
		baseApp = (BaseApplication) getApplication();
		this.setTickAlarm();
		msgImpl = new MsgImpl(baseApp);
		PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
		wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
		spf = getSharedPreferences(SECRETARY_MSG, Context.MODE_PRIVATE);
		LogUtils.d("oncreate");

	}

	@Override
	public int onStartCommand(Intent param, int flags, int startId) {
		if (param == null) {
			LogUtils.d("onStartCommand :intent is null");
			return START_STICKY;
		}
		// LogUtils.d("startFrom:" + param.getStringExtra(START_FROM));
		String cmd = param.getStringExtra(NAME_CMD);
		if (cmd == null) {
			cmd = "";
		}
		if (cmd.equals(VALUE_TICK)) {
			if (wakeLock != null && wakeLock.isHeld() == false) {
				wakeLock.acquire();
				LogUtils.d("check 'clientImpl' is 'null' or not");
				if (clientImpl == null) {
					if (checkUUIDExist()) {
						resetClient();
					} else {
						requestUUID();
					}
					if (!checkRegisterDevice()) {
						requestRegiestDevice();
					}
				}
			}
		}
		if (cmd.equals(VALUE_RESET)) {
			if (wakeLock != null && wakeLock.isHeld() == false) {
				wakeLock.acquire();
			}
			LogUtils.d("onStartCommand :start command with value RESET");
			if (checkUUIDExist()) {
				resetClient();
			} else {
				requestUUID();
			}
			if (!checkRegisterDevice()) {
				requestRegiestDevice();
			}
		}
		if (cmd.equals(VALUE_TOAST)) {
			String text = param.getStringExtra(VALUE_TEXT);
			if (text != null && text.trim().length() != 0) {
				Toast.makeText(this, text, Toast.LENGTH_LONG).show();
				LogUtils.d("onStartCommand :start command with value TEXT:" + text);
			}
		}
		if (clientImpl != null) {
			clientImpl.setPkgsInfo();
		}
		// 测试消息
		// notifyRunning();
		return Service.START_STICKY;
	}

	private boolean checkRegisterDevice() {
		LogUtils.d("check 'Device' is registered or not");
		String isRegisterDeivce = spf.getString(BocSdkConfig.REGIST_DEVICE_KEY, "");
		if (TextUtils.isEmpty(isRegisterDeivce)) {
			LogUtils.d("'Device' is not registered");
			return false;
		}
		LogUtils.d("'Device' is already registered");
		return true;
	}

	/**
	 * 注册推送设备
	 */
	private void requestRegiestDevice() {
		LogUtils.d("request 'register device' interface");
		CspUtil cspUtil = new CspUtil(this);
		RequestBody formBody = null;
		if (TextUtils.isEmpty(LoginUtil.getUserId(this))) {
			formBody = new FormEncodingBuilder()
				.add("deviceId", BaseApplication.deviceId)
				.add("platform", "0")
				.add("appId", APP_ID)
				.add("userId", "")
				.add("userName", "")
				.build();
		} else {
			formBody = new FormEncodingBuilder()
				.add("deviceId", BaseApplication.deviceId)
				.add("platform", "0")
				.add("appId", APP_ID)
				.add("method", "queryDeviceInfo")
				.add("userId", LoginUtil.getUserId(this))
				.add("userName", "")
				.build();
		} 
		CallBack callBack = new CallBack() {
			
			@Override
			public void onSuccess(String responStr) {
				LogUtils.d("request 'register device' interface success");
				Editor editor = spf.edit();
				editor.putString(BocSdkConfig.REGIST_DEVICE_KEY, "1");
				editor.commit();
			}
			
			@Override
			public void onFinish() {
				
			}
			
			@Override
			public void onFailure(String responStr) {
				LogUtils.d("request 'register device' interface failed：" + responStr);
			}
		};
		if (TextUtils.isEmpty(LoginUtil.getUserId(this))) {
			cspUtil.postCspNoLogin(BocSdkConfig.SECRETARY_REGISTER_DEVICE_PATH, formBody, false, callBack);
		} else {
			cspUtil.postCspNoLogin(BocSdkConfig.SECRETARY_DEVICE_PATH, formBody, false, callBack);
		}
	}

	/**
	 * 获取UUID
	 */
	private void requestUUID() {
		LogUtils.d("request 'UUID' interface");
		RequestBody formBody = new FormEncodingBuilder()
				.add("deviceId", BaseApplication.deviceId)
				.add("platform", "0")
				.add("appId", APP_ID)
				.build();
		CspUtil cspUtil = new CspUtil(this);
		cspUtil.postCspNoLogin(BocSdkConfig.SECRETARY_REQUEST_UUID_PATH, formBody, false, new CallBack() {
			
			@Override
			public void onSuccess(String responStr) {
				LogUtils.d("uuid:" + responStr);
//				UuidResponse uuidResponse = JSON.parseObject(responStr, UuidResponse.class);
//				if (uuidResponse != null) {
					Editor editor = spf.edit();
					editor.putString(BocSdkConfig.UUID_KEY, responStr);
					editor.commit();
					resetClient();
//				}
			}
			
			@Override
			public void onFinish() {
				
			}
			
			@Override
			public void onFailure(String responStr) {
				LogUtils.d("request 'UUID' interface failed:" + responStr);
			}
		});
	}

	private boolean checkUUIDExist() {
		LogUtils.d("check to see 'UUID' is existence or not");
		String uuid = spf.getString(BocSdkConfig.UUID_KEY, "");
		if (TextUtils.isEmpty(uuid)) {
			LogUtils.d("'UUID' is not existence");
			return false;
		}
		LogUtils.d("'UUID' is existence");
		return true;
	}

	protected void resetClient() {
		UUID = spf.getString(BocSdkConfig.UUID_KEY, "");
		new Thread() {
			@Override
			public void run() {
				String serverIp = BocSdkConfig.DDPUSH_SERVER_IP;
				String serverPort = BocSdkConfig.DDPUSH_CLIENT_PORT;
				String pushPort = BocSdkConfig.DDPUSH_PUSH_PORT;
				// String serverIp = BaseConfig.PUSH_SERVER_IP;
				// String serverPort = BaseConfig.DDPUSH_CLIENT_PORT;
				// String pushPort = BaseConfig.DDPUSH_PUSH_PORT;
				String uuid = UUID;
				LogUtils.d("serverIp: " + serverIp + "\nserverPort: " + serverPort
						+ "\npushPort: " + pushPort + "\nuuid: " + uuid);
				if (serverIp == null || serverIp.trim().length() == 0
						|| serverPort == null || serverPort.trim().length() == 0
						|| pushPort == null || pushPort.trim().length() == 0
						|| uuid == null || uuid.trim().length() == 0) {
					return;
				}
				if (OnlineService.this.clientImpl != null) {
					try {
						LogUtils.d("it will call close clientImpl");
						clientImpl.stop();
						LogUtils.d("it will call unregisterMsgListener");
						unregisterMsgListener(msgImpl);
					} catch (Exception e) {
					}
				}
				try {
					LogUtils.d("resetClient");
					// clientImpl = new UdpClientImpl(OnlineService.this,
					// Util.md5Byte("hucanhua"), 1, serverIp,
					// Integer.parseInt(serverPort), wakeLock);
					clientImpl = new UdpClientImpl(OnlineService.this,
							hexStringToByteArray(uuid), BocSdkConfig.APP_ID, serverIp,
							Integer.parseInt(serverPort), wakeLock);
					registerMsgListener(msgImpl);
					clientImpl.setHeartbeatInterval(PropertyUtil.getPropertyInt("CLIENT_UDP_HEART_PACKAGE_TIME"));
					clientImpl.start();
					BocSdkConfig.SENT_PKGS = "0";
					BocSdkConfig.RECEIVE_PKGS = "0";
					LoginUtil.setOnlineService(OnlineService.this);
				} catch (Exception e) {
					handler.sendMessage(handler.obtainMessage(0, e));

				}
				LogUtils.d("ddpush：client is reseted");
				handler.sendEmptyMessage(1);
			}
		}.start();

	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				LogUtils.e("操作失败：" + ((Exception) msg.obj).getMessage());
				Toast.makeText(OnlineService.this, "操作失败：" + ((Exception) msg.obj).getMessage(), 
						Toast.LENGTH_SHORT).show();
				break;
			case 1:
				LogUtils.d("ddpush：终端重置");
				// ToastUtils
				// .showError(OnlineService.this, "ddpush：终端重置",
				// Toast.LENGTH_SHORT);
				break;
			}
		}
	};

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

	protected void notifyRunning() {
		Intent intent = new Intent(this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		Bundle bundle = new Bundle();
		intent.putExtras(bundle);
		PendingIntent pi = PendingIntent.getActivity(this, 0, intent,
				PendingIntent.FLAG_CANCEL_CURRENT);
		SimpleNotifation.getInstance(this).notifyMsg(11, "新消息", "", "新消息", pi);
		// SimpleNotifation.getInstance(this).notifyResidentMsg(getApplicationName(),
		// "正在运行", "正在运行", pi);

	}

	protected void cancelNotifyRunning() {
		NotificationManager notificationManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(0);
	}

	@Override
	public void onDestroy() {
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

	public void registerMsgListener(OnReciveMsgListener reciveMsgListener) {
		clientImpl.registerMsgListener(reciveMsgListener);
	}

	public void unregisterMsgListener(OnReciveMsgListener reciveMsgListener) {
		clientImpl.unregisterMsgListener(reciveMsgListener);
	}

	public static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
					.digit(s.charAt(i + 1), 16));
		}
		return data;
	}
}
