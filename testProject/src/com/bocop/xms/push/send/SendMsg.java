package com.bocop.xms.push.send;

import org.ddpush.im.v1.client.appserver.Pusher;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.bocop.jxplatform.config.BocSdkConfig;
import com.bocop.xms.push.service.OnlineService;
import com.bocop.xms.push.util.Util;

/**
 * 发送消息
 * 
 * @author hch
 *
 */
public class SendMsg {
	private static final String TAG = "SendMsg";
	private Context context;
	static SendMsg sendMsg;

	private SendMsg(Context context) {
		this.context = context;
	}

	public SendMsg getInstance(Context context) {
		if (sendMsg == null) {
			sendMsg = new SendMsg(context);
		}
		return sendMsg;
	}

	protected void send0x10(String targetUserName) {
		if (targetUserName.length() == 0) {
			Log.e(TAG, "请输入目标用户名");
			Toast.makeText(context, "请输入目标用户名", Toast.LENGTH_SHORT).show();
			return;
		}
		String serverIp = BocSdkConfig.DDPUSH_SERVER_IP;
		String pushPort = BocSdkConfig.DDPUSH_PUSH_PORT;
		int port;
		try {
			port = Integer.parseInt(pushPort);
		} catch (Exception e) {
			Log.e(TAG, "推送端口格式错误：" + pushPort);
			Toast.makeText(context, "推送端口格式错误：" + pushPort, Toast.LENGTH_SHORT).show();
			return;
		}
		byte[] uuid = null;
		try {
			uuid = Util.md5Byte(targetUserName);
		} catch (Exception e) {
			Log.e(TAG, "错误：" + e.getMessage());
			Toast.makeText(context, "错误：" + e.getMessage(), Toast.LENGTH_SHORT).show();
			return;
		}
		Thread t = new Thread(new send0x10Task(context, serverIp, port, uuid));
		t.start();

	}

	protected void send0x11(String targetUserName, long msg) {
		if (targetUserName.length() == 0) {
			Log.e(TAG, "请输入目标用户名");
			Toast.makeText(context, "请输入目标用户名", Toast.LENGTH_SHORT).show();
			return;
		}

		if (msg == 0) {
			Log.e(TAG, "数字必须非零");
			Toast.makeText(context, "数字必须非零", Toast.LENGTH_SHORT).show();
			return;
		}
		String serverIp = BocSdkConfig.DDPUSH_SERVER_IP;
		String pushPort = BocSdkConfig.DDPUSH_PUSH_PORT;
		int port;
		try {
			port = Integer.parseInt(pushPort);
		} catch (Exception e) {
			Log.e(TAG, "推送端口格式错误：" + pushPort);
			Toast.makeText(context, "推送端口格式错误：" + pushPort, Toast.LENGTH_SHORT).show();
			return;
		}
		byte[] uuid = null;
		try {
			uuid = Util.md5Byte(targetUserName);
		} catch (Exception e) {
			Log.e(TAG, "错误：" + e.getMessage());
			Toast.makeText(context, "错误：" + e.getMessage(), Toast.LENGTH_SHORT).show();
			return;
		}
		Thread t = new Thread(new send0x11Task(context, serverIp, port, uuid, msg));
		t.start();
	}

	protected void send0x20(String targetUserName, String msg) {
		if (targetUserName.length() == 0) {
			Log.e(TAG, "请输入目标用户名");
			Toast.makeText(context, "请输入目标用户名", Toast.LENGTH_SHORT).show();
			return;
		}

		if (msg.length() == 0) {
			Log.e(TAG, "请输入一串文字");
			Toast.makeText(context, "请输入一串文字", Toast.LENGTH_SHORT).show();
			return;
		}

		String serverIp = BocSdkConfig.DDPUSH_SERVER_IP;
		String pushPort = BocSdkConfig.DDPUSH_PUSH_PORT;
		int port;
		try {
			port = Integer.parseInt(pushPort);
		} catch (Exception e) {
			Log.e(TAG, "推送端口格式错误：" + pushPort);
			Toast.makeText(context, "推送端口格式错误：" + pushPort, Toast.LENGTH_SHORT).show();
			return;
		}
		byte[] uuid = null;
		try {
			uuid = Util.md5Byte(targetUserName);
		} catch (Exception e) {
			Log.e(TAG, "错误：" + e.getMessage());
			Toast.makeText(context, "错误：" + e.getMessage(), Toast.LENGTH_SHORT).show();
			return;
		}
		byte[] sendMsg = null;
		try {
			sendMsg = msg.getBytes("UTF-8");
		} catch (Exception e) {
			Log.e(TAG, "错误：" + e.getMessage());
			Toast.makeText(context, "错误：" + e.getMessage(), Toast.LENGTH_SHORT).show();
			return;
		}
		Thread t = new Thread(new send0x20Task(context, serverIp, port, uuid, sendMsg));
		t.start();
	}

	class send0x10Task implements Runnable {
		private Context context;
		private String serverIp;
		private int port;
		private byte[] uuid;

		public send0x10Task(Context context, String serverIp, int port, byte[] uuid) {
			this.context = context;
			this.serverIp = serverIp;
			this.port = port;
			this.uuid = uuid;
		}

		public void run() {
			Pusher pusher = null;
			Intent startSrv = new Intent(context, OnlineService.class);
			startSrv.putExtra(OnlineService.NAME_CMD, OnlineService.VALUE_TOAST);
			try {
				boolean result;
				pusher = new Pusher(serverIp, port, 1000 * 5);
				result = pusher.push0x10Message(uuid);
				if (result) {
					startSrv.putExtra(OnlineService.VALUE_TEXT, "通用信息发送成功");
				} else {
					startSrv.putExtra(OnlineService.VALUE_TEXT, "发送失败！格式有误");
				}
			} catch (Exception e) {
				e.printStackTrace();
				startSrv.putExtra(OnlineService.VALUE_TEXT, "发送失败！" + e.getMessage());
			} finally {
				if (pusher != null) {
					try {
						pusher.close();
					} catch (Exception e) {
					}
					;
				}
			}
			context.startService(startSrv);
		}
	}

	class send0x11Task implements Runnable {
		private Context context;
		private String serverIp;
		private int port;
		private byte[] uuid;
		private long msg;

		public send0x11Task(Context context, String serverIp, int port, byte[] uuid,
				long msg) {
			this.context = context;
			this.serverIp = serverIp;
			this.port = port;
			this.uuid = uuid;
			this.msg = msg;
		}

		public void run() {
			Pusher pusher = null;
			Intent startSrv = new Intent(context, OnlineService.class);
			startSrv.putExtra(OnlineService.NAME_CMD, OnlineService.VALUE_TOAST);
			try {
				boolean result;
				pusher = new Pusher(serverIp, port, 1000 * 5);
				result = pusher.push0x11Message(uuid, msg);
				if (result) {
					startSrv.putExtra(OnlineService.VALUE_TEXT, "分类信息发送成功");
				} else {
					startSrv.putExtra(OnlineService.VALUE_TEXT, "发送失败！格式有误");
				}
			} catch (Exception e) {
				e.printStackTrace();
				startSrv.putExtra(OnlineService.VALUE_TEXT, "发送失败！" + e.getMessage());
			} finally {
				if (pusher != null) {
					try {
						pusher.close();
					} catch (Exception e) {
					}
					;
				}
			}
			context.startService(startSrv);
		}
	}

	class send0x20Task implements Runnable {
		private Context context;
		private String serverIp;
		private int port;
		private byte[] uuid;
		private byte[] msg;

		public send0x20Task(Context context, String serverIp, int port, byte[] uuid,
				byte[] msg) {
			this.context = context;
			this.serverIp = serverIp;
			this.port = port;
			this.uuid = uuid;
			this.msg = msg;
		}

		public void run() {
			Pusher pusher = null;
			Intent startSrv = new Intent(context, OnlineService.class);
			startSrv.putExtra(OnlineService.NAME_CMD, OnlineService.VALUE_TOAST);
			try {
				boolean result;

				pusher = new Pusher(serverIp, port, 1000 * 5);
				result = pusher.push0x20Message(uuid, msg);
				if (result) {
					startSrv.putExtra(OnlineService.VALUE_TEXT, "自定义信息发送成功");
				} else {
					startSrv.putExtra(OnlineService.VALUE_TEXT, "发送失败！格式有误");
				}
			} catch (Exception e) {
				e.printStackTrace();
				startSrv.putExtra(OnlineService.VALUE_TEXT, "发送失败！" + e.getMessage());
			} finally {
				if (pusher != null) {
					try {
						pusher.close();
					} catch (Exception e) {
					}
					;
				}
			}
			context.startService(startSrv);
		}
	}
}
