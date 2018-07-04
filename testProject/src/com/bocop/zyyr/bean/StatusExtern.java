package com.bocop.zyyr.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class StatusExtern {
	@XStreamAlias("INFO")
	private Status status;

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
