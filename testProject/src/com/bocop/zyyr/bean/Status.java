package com.bocop.zyyr.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * 申请贷款状态
 * 
 * @author lh
 * 
 */
public class Status {

	@XStreamAlias("STATUS")
	private String status;
	@XStreamAlias("PHONE")
	private String phone;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
