package com.bocop.yfx.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * 还款期数
 * 
 * @author lh
 * 
 */
public class Period {

	@XStreamAlias("WLS_REPAYMENT_PERIOD")
	private String periodString;
	@XStreamAlias("WLS_REPAYMENT_PERIOD_ID")
	private String periodID;


	public String getPeriodID() {
		return periodID;
	}

	public void setPeriodID(String periodID) {
		this.periodID = periodID;
	}

	public String getPeriodString() {
		return periodString;
	}

	public void setPeriodString(String periodString) {
		this.periodString = periodString;
	}
}
