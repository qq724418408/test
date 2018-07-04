package com.bocop.yfx.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * 消息列表
 * 
 * @author lh
 * 
 */
public class Message {

	@XStreamAlias("LOAN_MESSAGE_ID")
	private String msgId;
	@XStreamAlias("WLS_TYPE")
	private String msgType;
	@XStreamAlias("WLS_MESSAGE")
	private String msg;

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
