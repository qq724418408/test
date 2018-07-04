package com.bocop.xfjr.bean.pretrial;

import com.bocop.jxplatform.R;

public class CustomType {
	private int tid;
	private String tName;
	private int Resid;

	public int getTid() {
		return tid;
	}

	public void setTid(int tid) {
		this.tid = tid;
	}

	public String gettName() {
		return tName;
	}

	public void settName(String tName) {
		this.tName = tName;
	}

	public int getResid() {
		return Resid;
	}

	public void setResid(int resid) {
		Resid = resid;
	}

	public CustomType(int tid, String tName, int resid) {
		super();
		this.tid = tid;
		this.tName = tName;
		Resid = resid;
	}

	public CustomType() {
		super();
		this.tid = -1;
	}
	
	@Override
	public String toString() {
		return tName;
	}
}
