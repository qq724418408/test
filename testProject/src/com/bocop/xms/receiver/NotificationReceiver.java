package com.bocop.xms.receiver;

import com.bocop.jxplatform.activity.MainActivity;
import com.bocop.xms.utils.SystemUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotificationReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// 判断app进程是否存活
		if (SystemUtils.isAppAlive(context, "com.bocop.jxplatform")) {
			Intent mainIntent = new Intent(context, MainActivity.class);
			mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			context.startActivity(mainIntent);
		} else {
			// 如果app进程已经被杀死，先重新启动app，将DetailActivity的启动参数传入Intent中，参数经过
			// SplashActivity传入MainActivity，此时app的初始化已经完成，在MainActivity中就可以根据传入
			// //参数跳转到DetailActivity中去了
			Log.i("NotificationReceiver", "the app process is dead");
			Intent launchIntent = context.getPackageManager()
					.getLaunchIntentForPackage("com.bocop.jxplatform");
			launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
			context.startActivity(launchIntent);
		}
	}
}