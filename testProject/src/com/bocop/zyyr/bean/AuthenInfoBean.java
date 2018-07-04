package com.bocop.zyyr.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class AuthenInfoBean {
	
	@XStreamAlias("INFO")
	private AuthenInfo authenInfo;

	public AuthenInfo getAuthenInfo() {
		return authenInfo;
	}

	public void setAuthenInfo(AuthenInfo authenInfo) {
		this.authenInfo = authenInfo;
	}

}
