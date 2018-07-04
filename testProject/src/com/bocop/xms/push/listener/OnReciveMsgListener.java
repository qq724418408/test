package com.bocop.xms.push.listener;

import org.ddpush.im.v1.client.appuser.Message;


/**
 * 消息接收的监听
 * 
 * @author hch
 *
 */
public interface OnReciveMsgListener {
	/**
	 * 接收的消息
	 * 
	 * @param message
	 *            原始消息格式的消息内容
	 */
	void onReceive(Message message);
	
	String getRegisterName();
}
