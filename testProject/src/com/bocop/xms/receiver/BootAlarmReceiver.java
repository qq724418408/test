package com.bocop.xms.receiver;

import com.bocop.xms.service.AlarmServiceManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
/**
 * 系统启动的广播接收者
 * @author hch
 *
 */
public class BootAlarmReceiver extends BroadcastReceiver {

	public BootAlarmReceiver() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// 开始闹铃服务
		AlarmServiceManager.getInstance().startAlarmService(context);
	}

}
