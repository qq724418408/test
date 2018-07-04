package com.bocop.xms.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("UTILITY_PAYMENT")
public class MessageResponse {
	
	@XStreamAlias("CONST_HEAD")
	private ConstHead constHead;
	@XStreamAlias("DATA_AREA")
	private MessageList messageList;
	
	public ConstHead getConstHead() {
		return constHead;
	}
	public void setConstHead(ConstHead constHead) {
		this.constHead = constHead;
	}
	public MessageList getMessageList() {
		return messageList;
	}
	public void setMessageList(MessageList messageList) {
		this.messageList = messageList;
	}

}
