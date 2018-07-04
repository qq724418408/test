package com.bocop.xms.receiver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.boc.jx.tools.FileUtils;
import com.bocop.gopushlibrary.bean.PushMessage;
import com.bocop.gopushlibrary.bean.PushMessageMap;
import com.bocop.gopushlibrary.service.GoPushService;
import com.bocop.gopushlibrary.utils.Logger;
import com.bocop.jxplatform.activity.MainActivity;
import com.bocop.jxplatform.config.BocSdkConfig;
import com.bocop.xms.push.util.DateTimeUtil;
import com.bocop.xms.push.util.SimpleNotifation;

/**
 * 自定义接收器
 * 
 * 
 */
public class MyReceiver extends BroadcastReceiver {
	
	@SuppressWarnings("unchecked")
	@Override
	public void onReceive(Context context, Intent intent) {
		// 将接受的数据发送到需要的地方（使用广播）
		Logger.i("GoPush", "MyReceiver==>数据传递中");
		
		if (intent.getAction().equals(GoPushService.ACTION_GET_ONLINE_MSG)) {
			PushMessage pushMessage = (PushMessage) intent.getExtras().get(
					GoPushService.ONLINE_Extra);
			saveToFile(DateTimeUtil.getCurDateTime() + "  " + pushMessage.toString());

			try {
				if(pushMessage != null){
					JSONObject jot = new JSONObject(pushMessage.msg);
					if(jot != null){
						String msg = jot.getString("msg") == null ? "" : jot.getString("msg");
						notificUI(context, msg);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			};
		} else if (intent.getAction().equals(GoPushService.ACTION_GET_OFFLINE_MSG)) {
			ArrayList<PushMessageMap> pushMessageMaps = (ArrayList<PushMessageMap>) intent
					.getExtras().get(GoPushService.OFFLINE_Extra);
			if (pushMessageMaps.size() > 0) {
				String msg = "您有" + pushMessageMaps.size() + "离线交易消息";
				notificUI(context, msg);
			}
		}
	}
	
	private void notificUI(Context context, String msg){
		int number = 0;
		//设置点击通知栏的动作为启动另外一个广播
        Intent broadcastIntent = new Intent(context, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.
                getBroadcast(context, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		SimpleNotifation.getInstance(context).notifyMsg(number++, "", msg,
				"您有一条新的信息", pendingIntent);
	}

	private void saveToFile(String text){
		try {
			File file = new File(FileUtils.getStorageDerectory(BocSdkConfig.APP_PUSH_LOG_PATH) + "/push.log");
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true));
			bufferedWriter.write(text + "\n");
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
