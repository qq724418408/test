package com.bocop.xms.push.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

/**
 * 通知的简单实现类
 * 
 * @author hucanhua
 * 
 */
public class SimpleNotifation {
	private static SimpleNotifation simpleNotifation;
	NotificationManager notificationManager;
	private int messageNum = 0;
	private int appIconId;
	private Context context;
	Bitmap bmAppIcon;
	private final static int RESIDENT_ONE_ID = Integer.MAX_VALUE;

	private SimpleNotifation(Context context) {
		this.context = context;
		notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		appIconId = context.getApplicationInfo().icon;
		bmAppIcon = BitmapFactory.decodeResource(context.getResources(), appIconId);
	}

	public static SimpleNotifation getInstance(Context context) {
		if (simpleNotifation == null) {
			simpleNotifation = new SimpleNotifation(context);
		}
		return simpleNotifation;
	}

	private int notifyMsg(int id, NotificationCompat.Builder builder) {
		Notification notification = builder.build();
		notificationManager.notify(id, notification);
		return id;
	}

	/**
	 * 发送通知
	 * 
	 * @param id
	 * @param contentTitle
	 * @param contentText
	 * @param tickerText
	 * @return
	 */
	public int notifyMsg(int id, String contentTitle, String contentText,
			String tickerText, PendingIntent pendingIntent) {
		clearNotify(id);
		Bitmap BmAppIcBm = BitmapFactory
				.decodeResource(context.getResources(), appIconId);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
				.setLargeIcon(BmAppIcBm).setSmallIcon(appIconId, 0).setTicker(tickerText)
				.setContentText(contentText).setContentTitle(contentTitle)
				.setWhen(System.currentTimeMillis()).setAutoCancel(true);
//		if (Util.isTopActivity(context)) {
//			builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS);
//		} else {
//			builder.setDefaults(Notification.DEFAULT_ALL);
//		}
		builder.setDefaults(Notification.DEFAULT_ALL
				| Notification.DEFAULT_VIBRATE); // 设置铃声为默认+震动
		if (pendingIntent != null) {
			builder.setContentIntent(pendingIntent);
		}
		messageNum++;
		return notifyMsg(id, builder);
	}

	/**
	 * 发送常驻通知
	 * 
	 * @param contentTitle
	 * @param contentText
	 * @param tickerText
	 * @return
	 */
	public int notifyResidentMsg(String contentTitle, String contentText,
			String tickerText, PendingIntent pendingIntent) {
		Bitmap BmAppIcBm = BitmapFactory
				.decodeResource(context.getResources(), appIconId);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
				.setLargeIcon(BmAppIcBm).setSmallIcon(appIconId, 0).setTicker(tickerText)
				.setContentText(contentText).setContentTitle(contentTitle)
				.setOngoing(true).setAutoCancel(false);
		if (pendingIntent != null) {
			builder.setContentIntent(pendingIntent);
		}
		return notifyMsg(RESIDENT_ONE_ID, builder);
	}

//	public int notifyCustomMsg() {
//		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
//				R.layout.layout_notification);
//		Intent intent = new Intent(context, MainActivity.class);
//		Intent intent1 = new Intent(context, InnerBroadcastReceiver.class);
//		Intent intent2 = new Intent(context, InnerBroadcastReceiver.class);
//		intent2.setAction(ThirdActivity.class.getName());
//		intent1.setAction(SecondActivity.class.getName());
//		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
//				PendingIntent.FLAG_CANCEL_CURRENT);
//		PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context, 0, intent2, 0);
//		PendingIntent pendingIntent1 = PendingIntent.getBroadcast(context, 0, intent1, 0);
//		remoteViews.setOnClickPendingIntent(R.id.button2, pendingIntent2);
//		remoteViews.setOnClickPendingIntent(R.id.button1, pendingIntent1);
//		NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
//				.setContent(remoteViews).setSmallIcon(R.drawable.ic_launcher)
//				.setLargeIcon(bmAppIcon).setAutoCancel(true)
//				.setContentIntent(pendingIntent);
//		return notifyMsg(RESIDENT_ONE_ID, builder);
//	}

	public int getMessageNum() {
		return messageNum;
	}

	public void clearNotify(int id) {
		notificationManager.cancel(id);
	}

}
