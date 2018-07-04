package com.bocop.xms.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * 用户列表响应
 * @author ftl
 *
 */
@XStreamAlias("UTILITY_PAYMENT")
public class UserResponse {
	
	@XStreamAlias("CONST_HEAD")
	private ConstHead constHead;
	@XStreamAlias("DATA_AREA")
	private UserList userList;
	
	public ConstHead getConstHead() {
		return constHead;
	}
	public void setConstHead(ConstHead constHead) {
		this.constHead = constHead;
	}
	public UserList getUserList() {
		return userList;
	}
	public void setUserList(UserList userList) {
		this.userList = userList;
	}
	
}
