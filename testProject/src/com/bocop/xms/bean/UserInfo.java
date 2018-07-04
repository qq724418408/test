package com.bocop.xms.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class UserInfo {
	
	@XStreamAlias("USER_INFO")
	private SignContractInfo signContractInfo;

	public SignContractInfo getSignContractInfo() {
		return signContractInfo;
	}

	public void setSignContractInfo(SignContractInfo signContractInfo) {
		this.signContractInfo = signContractInfo;
	}
	
}
