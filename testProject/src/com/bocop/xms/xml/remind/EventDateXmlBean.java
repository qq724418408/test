package com.bocop.xms.xml.remind;

import java.util.List;

public class EventDateXmlBean {

	private String errorcode;
	private String errormsg;
	private List<String> timeList;
	
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
	public List<String> getTimeList() {
		return timeList;
	}
	public void setTimeList(List<String> timeList) {
		this.timeList = timeList;
	}
	
}
