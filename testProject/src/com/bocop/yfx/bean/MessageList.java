package com.bocop.yfx.bean;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 消息列表
 * 
 * @author lh
 * 
 */
public class MessageList {
	@XStreamImplicit(itemFieldName = "LOAN_MESSAGE_LIST")
	private List<Message> list;

	public List<Message> getList() {
		return list;
	}

	public void setList(List<Message> list) {
		this.list = list;
	}
}
