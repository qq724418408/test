package com.bocop.xms.push.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.bocop.xms.push.service.OnlineService;
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
		
		Intent startSrv = new Intent(context, OnlineService.class);
		startSrv.putExtra(OnlineService.START_FROM, this.getClass().getName());
		startSrv.putExtra(OnlineService.NAME_CMD, OnlineService.VALUE_TICK);
		context.startService(startSrv);
	}

}
