package com.bocop.xms.push.bean;

import java.io.Serializable;

public class Extension implements Serializable {
	private static final long serialVersionUID = 1L;

	private String msgId;
	private String msgType;

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

	@Override
	public String toString() {
		return "Extension [msgId=" + msgId + ", msgType=" + msgType + "]";
	}

}
