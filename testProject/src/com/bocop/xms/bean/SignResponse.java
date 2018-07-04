package com.bocop.xms.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * 签约信息响应
 * @author ftl
 *
 */
@XStreamAlias("UTILITY_PAYMENT")
public class SignResponse {
	
	@XStreamAlias("CONST_HEAD")
	private ConstHead constHead;

	
	public ConstHead getConstHead() {
		return constHead;
	}
	public void setConstHead(ConstHead constHead) {
		this.constHead = constHead;
	}

}
