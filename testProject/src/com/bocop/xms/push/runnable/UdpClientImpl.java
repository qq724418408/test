package com.bocop.xms.push.runnable;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import org.ddpush.im.v1.client.appuser.Message;
import org.ddpush.im.v1.client.appuser.UDPClientBase;

import android.content.Context;
import android.os.PowerManager.WakeLock;

import com.boc.jx.tools.LogUtils;
import com.bocop.jxplatform.config.BocSdkConfig;
import com.bocop.xms.push.listener.OnReciveMsgListener;
import com.bocop.xms.push.util.Util;

/**
 * udp的实现
 * 
 * @author hch
 *
 */
public class UdpClientImpl extends UDPClientBase {

	private static final String TAG = "UdpClientImpl";
	private Context context;
	private WakeLock wakeLock;
	private static HashMap<String, OnReciveMsgListener> msgListeners = new HashMap<String, OnReciveMsgListener>();

	public UdpClientImpl(Context context, byte[] uuid, int appid, String serverAddr,
			int serverPort, WakeLock wakeLock) throws Exception {
		super(uuid, appid, serverAddr, serverPort);
		this.context = context;
		this.wakeLock = wakeLock;
	}

	@Override
	public boolean hasNetworkConnection() {
		return Util.hasNetwork(context);
	}

	@Override
	public void trySystemSleep() {
		tryReleaseWakeLock();
	}

	@Override
	public void onPushMessage(Message message) {
		LogUtils.d("收到消息："
				+ message.getData());
		notifyMsgListener(message);
		setPkgsInfo();
	}

	private void notifyMsgListener(Message message) {
		try {
			LogUtils.d("收到消息："
					+ message.getData()
					+ "   "
					+ new String(message.getData(), 5, message.getContentLength(),
							"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		synchronized (msgListeners) {
			for (OnReciveMsgListener reciveMsgListener : msgListeners.values()) {
				reciveMsgListener.onReceive(message);
			}
		}
	}

	public void setPkgsInfo() {

		long sent = this.getSentPackets();
		long received = this.getReceivedPackets();
		BocSdkConfig.SENT_PKGS = "" + sent;
		BocSdkConfig.RECEIVE_PKGS = "" + received;
	}

	protected void tryReleaseWakeLock() {
		if (wakeLock != null && wakeLock.isHeld() == true) {
			wakeLock.release();
		}
	}

	public void registerMsgListener(OnReciveMsgListener reciveMsgListener) {
		synchronized (msgListeners) {
			LogUtils.d("注册消息接收：" + reciveMsgListener.getRegisterName() + "  "
					+ reciveMsgListener.getClass().getSimpleName());
			if (msgListeners.get(reciveMsgListener.getRegisterName()) != null) {
				LogUtils.d("删除" + reciveMsgListener.getRegisterName() + "的监听器");
				msgListeners.remove(reciveMsgListener.getRegisterName());
			}
			msgListeners.put(reciveMsgListener.getRegisterName(), reciveMsgListener);
			LogUtils.d(
					"添加" + reciveMsgListener.getRegisterName() + ",共有"
							+ msgListeners.size() + "个监听器");
		}
	}

	public void unregisterMsgListener(OnReciveMsgListener reciveMsgListener) {
		synchronized (msgListeners) {
			msgListeners.remove(reciveMsgListener);
		}
	}

}