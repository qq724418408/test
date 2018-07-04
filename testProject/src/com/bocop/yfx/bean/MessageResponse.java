package com.bocop.yfx.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * 消息列表
 * 
 * @author lh
 * 
 */
@XStreamAlias("UTILITY_PAYMENT")
public class MessageResponse extends BaseResponse {

	@XStreamAlias("DATA_AREA")
	private MessageList list;

	public MessageList getList() {
		return list;
	}

	public void setList(MessageList list) {
		this.list = list;
	}


}
