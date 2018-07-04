package com.bocop.yfx.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * 还款方式
 * 
 * @author lh
 * 
 */
public class Method {

	@XStreamAlias("WLS_REPAYMENT_METHOD")
	private String methodString;
	@XStreamAlias("WLS_REPAYMENT_METHOD_ID")
	private String methodID;

	public String getMethodID() {
		return methodID;
	}

	public void setMethodID(String methodID) {
		this.methodID = methodID;
	}

	public String getMethodString() {
		return methodString;
	}

	public void setMethodString(String methodString) {
		this.methodString = methodString;
	}

}
