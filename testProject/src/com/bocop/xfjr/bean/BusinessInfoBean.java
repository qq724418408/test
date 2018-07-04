package com.bocop.xfjr.bean;

import com.boc.jx.httptools.network.base.RootRsp;

public class BusinessInfoBean extends RootRsp {
	
	private int type;
	private String number;

	public BusinessInfoBean() {
		super();
	}
	
	public BusinessInfoBean(int type, String number) {
		super();
		this.type = type;
		this.number = number;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

}
