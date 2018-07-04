package com.bocop.xms.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * 用户信息响应
 * @author ftl
 *
 */
@XStreamAlias("UTILITY_PAYMENT")
public class UserInfoResponse {
	
	@XStreamAlias("CONST_HEAD")
	private ConstHead constHead;
	@XStreamAlias("DATA_AREA")
	private UserInfo info;
	
	public ConstHead getConstHead() {
		return constHead;
	}
	public void setConstHead(ConstHead constHead) {
		this.constHead = constHead;
	}
	public UserInfo getInfo() {
		return info;
	}
	public void setInfo(UserInfo info) {
		this.info = info;
	}
}
