package com.bocop.xms.xml.remind;

import java.util.List;

public class EventListXmlBean {

	private String errorcode;
	private String errormsg;
	private List<EventBean> eventList;
	
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
	public List<EventBean> getEventList() {
		return eventList;
	}
	public void setEventList(List<EventBean> eventList) {
		this.eventList = eventList;
	}
	
}
