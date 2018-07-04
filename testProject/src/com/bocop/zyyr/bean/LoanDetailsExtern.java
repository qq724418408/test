package com.bocop.zyyr.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class LoanDetailsExtern {
	
	@XStreamAlias("MYPROINFO")
	private LoanDetails details;

	public LoanDetails getDetails() {
		return details;
	}

	public void setDetails(LoanDetails details) {
		this.details = details;
	}

}
