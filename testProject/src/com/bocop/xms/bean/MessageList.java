package com.bocop.xms.bean;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

public class MessageList {
	
	@XStreamImplicit(itemFieldName="pushInfo")
	private List<MessageCostType> list;

	public List<MessageCostType> getList() {
		return list;
	}

	public void setList(List<MessageCostType> list) {
		this.list = list;
	}

}
