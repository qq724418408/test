package com.bocop.xms.xml.sign;

import java.util.List;


public class SignListXmlBean {
	private String errorcode;
	private String errormsg;
	private List<SignBean> signListBean;
	public String getErrorcode() {
		return errorcode;
	}
	public void setErrorcode(String errorcode) {
		this.errorcode = errorcode;
	}
	public String getErrormsg() {
		return errormsg;
	}
	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}
	public List<SignBean> getSignListBean() {
		return signListBean;
	}
	public void setSignListBean(List<SignBean> signListBean) {
		this.signListBean = signListBean;
	}
	@Override
	public String toString() {
		return "SignListXmlBean [errorcode=" + errorcode + ", errormsg="
				+ errormsg + ", signListBean=" + signListBean + "]";
	}
	
	

}
