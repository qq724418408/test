package com.bocop.xms.xml.remind;

import java.io.Serializable;

public class EventBean implements Serializable {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	private String eventId;//事件id
	private String remindtime;//提醒时间
	private String content;//内容
	private String endtime;//结束时间
	private String type;//重复类型
	private String repeatValue;//重复值
	
	public EventBean() {
		super();
	}
	public EventBean(String eventId, String remindtime, String content, String endtime,String type) {
		super();
		this.eventId = eventId;
		this.remindtime = remindtime;
		this.content = content;
		this.endtime = endtime;
		this.type=type;
	}
	public String getEventId() {
		return eventId;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	public String getRemindtime() {
		return remindtime;
	}
	public void setRemindtime(String remindtime) {
		this.remindtime = remindtime;
	}
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getRepeatValue() {
		return repeatValue;
	}
	public void setRepeatValue(String repeatValue) {
		this.repeatValue = repeatValue;
	}
	
	
}
