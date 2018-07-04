package com.bocop.xms.push.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager.WakeLock;

import com.bocop.xms.push.service.OnlineService;
import com.bocop.xms.push.util.Util;

/**
 * 接收定时器的接收者
 * @author hch
 *
 */
public class TickAlarmReceiver extends BroadcastReceiver {

	WakeLock wakeLock;
	
	public TickAlarmReceiver() {
		
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if(Util.hasNetwork(context) == false){
			return;
		}
		Intent startSrv = new Intent(context, OnlineService.class);
		startSrv.putExtra(OnlineService.START_FROM, this.getClass().getName());
		startSrv.putExtra(OnlineService.NAME_CMD, OnlineService.VALUE_TICK);
		context.startService(startSrv);
	}

}
