package com.bocop.xms.push.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.bocop.xms.push.service.OnlineService;
import com.bocop.xms.push.util.Util;

/**
 * 网络改变的接收者
 * @author hch
 *
 */
public class ConnectivityAlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		Log.d("ConnectivityAlarmReceiver", "网络改变");
		if(Util.hasNetwork(context) == false){
			return;
		}
		Intent startSrv = new Intent(context, OnlineService.class);
		startSrv.putExtra(OnlineService.START_FROM, this.getClass().getName());
		startSrv.putExtra(OnlineService.NAME_CMD, OnlineService.VALUE_RESET);
		context.startService(startSrv);
	}

}
