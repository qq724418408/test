package com.bocop.xms.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 用于MessageActivity
 * 
 * @author luoyang
 *
 */
public class MessageCostType {

	@XStreamAlias("TYPE")
	private String type;//签约类型
	@XStreamAlias("USER_ID")//用户编号
	private String userId;
	@XStreamAlias("PUSH_TEXT")
	private String push_text;//推送文本
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPush_text() {
		return push_text;
	}
	public void setPush_text(String push_text) {
		this.push_text = push_text;
	}

	
}
