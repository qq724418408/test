package com.bocop.xms.xml.message;

import java.util.List;

public class MessageListXmlBean {

	private String errorcode;
	private String errormsg;
	private List<MessageBean> messageList;
	//private String isLast;//是否最后一页，0(否)，1(是)
	
	public String getErrorcode() {
		return errorcode;
	}
	public void setErrorcode(String errorcode) {
		this.errorcode = errorcode;
	}
	public String getErrormsg() {
		return errormsg;
	}
	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}
	public List<MessageBean> getMessageList() {
		return messageList;
	}
	public void setMessageList(List<MessageBean> messageList) {
		this.messageList = messageList;
	}
/*	public String getIsLast() {
		return isLast;
	}
	public void setIsLast(String isLast) {
		this.isLast = isLast;
	}*/
	
}
