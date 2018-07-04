package com.bocop.xms.push.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.ddpush.im.v1.client.appuser.Message;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.boc.jx.base.BaseApplication;
import com.boc.jx.tools.FileUtils;
import com.bocop.jxplatform.activity.MainActivity;
import com.bocop.jxplatform.config.BocSdkConfig;
import com.bocop.xms.push.bean.PushMessage;
import com.bocop.xms.push.listener.OnReciveMsgListener;
import com.bocop.xms.push.util.DateTimeUtil;
import com.bocop.xms.push.util.SimpleNotifation;
import com.bocop.xms.push.util.Util;

/**
 * 处理接收的消息并发送通知
 * 
 * @author hch
 *
 */
public class MsgImpl implements OnReciveMsgListener {

	private Context context;
	// public static final String MSG_CONTENT_KEY = "msgContent.key";
	public static final String MSG_ONTENT_OBJECT_KEY = "msgContentObject.key";
	private static final String TAG = "MsgImpl";
	int number = 0;
	private BaseApplication application;

	public MsgImpl(BaseApplication application) {
		this.application = application;
		this.context = application.getApplicationContext();
	}

	@Override
	public void onReceive(Message message) {
		if (message == null) {
			return;
		}
		if (message.getData() == null || message.getData().length == 0) {
			return;
		}
		if (message.getCmd() == 16) {// 0x10 通用推送信息
			notifyUser(16, Util.getApplicationName(context) + "通用信息", "时间："
					+ DateTimeUtil.getCurDateTime(), "您有一条新的信息");
		}
		if (message.getCmd() == 17) {// 0x11 分组推送信息
			long msg = ByteBuffer.wrap(message.getData(), 5, 8).getLong();
			notifyUser(17, Util.getApplicationName(context) + "分组信息", "" + msg,
					"您有一条新的信息");
		}
		if (message.getCmd() == 32) {// 0x20 自定义推送信息
			String str = null;
			try {
				str = new String(message.getData(), 5, message.getContentLength(),
						"UTF-8");
			} catch (Exception e) {
				str = Util.convert(message.getData(), 5, message.getContentLength());
			}
			notifyUser(32, Util.getApplicationName(context) + "信息", str, "您有一条新的信息");
		}
	}

	public void notifyUser(int type, String title, String content, String tickerText) {
		PendingIntent pi = null;
		Log.d(TAG, "收到消息：" + content);
		try {
			if (type == 32) {
				Intent intent = new Intent(context, MainActivity.class);
				PushMessage message = JSON.parseObject(content, PushMessage.class);
				title = message.getAps().getAlert().getTitle();
				content = message.getAps().getAlert().getBody();
				saveToFile(DateTimeUtil.getCurDateTime() + "  " + message.toString());
				pi = PendingIntent.getActivity(context, number, intent,
						PendingIntent.FLAG_UPDATE_CURRENT);
				SimpleNotifation.getInstance(context).notifyMsg(number++, title, content,
						tickerText, pi);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void playSound(Context context) {
		try {
			Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			MediaPlayer player = new MediaPlayer();
			player.setDataSource(context, alert);
			AudioManager audioManager = (AudioManager) context
					.getSystemService(Context.AUDIO_SERVICE);
			if (audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION) != 0) {
				player.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
				player.setLooping(false);
				player.prepare();
				player.start();
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getRegisterName() {
		return MsgImpl.class.getSimpleName();
	}

	private void saveToFile(String text) throws IOException {
		try{
			File file = new File(FileUtils.getStorageDerectory(BocSdkConfig.APP_PUSH_LOG_PATH) + "/push.log");
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true));
			bufferedWriter.write(text + "\n");
			bufferedWriter.close();
		}catch(Exception ex){
			Log.i("tag", ex.getMessage().toString());
		}
		
	}

}
