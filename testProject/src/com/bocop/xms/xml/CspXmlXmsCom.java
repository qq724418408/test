package com.bocop.xms.xml;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.xmlpull.v1.XmlSerializer;

import com.bocop.jxplatform.config.TransactionValue;

import android.util.Log;
import android.util.Xml;

public class CspXmlXmsCom {

	private String USER_ID;// 中银用户号
	private String TX_CODE;

	private String date = "";// 查询的日期，例：2016-12-12
	private String eventId = "";// 事件id
	private String time = "";// 时间，例：2016-12-12 09:30
	private String content = "";// 内容
	private String remind = "";// 是否提醒，0(不提醒)，1(提醒)
	private String pageNo = "";// 页码，从0开始
	private String pageSize = "";// 每页条数，例：10
	private String remindTime = "";// 提醒时间
	private String endTime = "";// 结束时间
	private String type = "";// 循环类型
	private String orderDate = "";// 月提醒日
	private String sysId;
	private String addressId;
	private String userCode;
	private String repeatValue;

	public CspXmlXmsCom(String strUserId, String txCode) {
		USER_ID = strUserId;
		TX_CODE = txCode;
	}

	private static String REQUEST_TYPE = "0240";
	private static String REQUEST_MERCH = "BOCOP000";
	private static String AGENT_CODE = "05079101";
	private static String TRN_CODE = TransactionValue.CSPSZF;
	private static String FRONT_TRACENO = "000000000000000";

	private String FRONT_DATE;
	private String FRONT_TIME;

	// 生产CSPXML报文
	public String getCspXml() {

		XmlSerializer serializer = Xml.newSerializer();
		StringWriter stringWriter = new StringWriter();

		// 获取时间
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		FRONT_DATE = format.format(new Date(System.currentTimeMillis()));
		SimpleDateFormat format_h = new SimpleDateFormat("HHmmss");
		FRONT_TIME = format_h.format(new Date(System.currentTimeMillis()));

		try {
			serializer.setOutput(stringWriter);
			serializer.startDocument("utf-8", false);
			// start UTILITY_PAYMENT
			serializer.startTag(null, "UTILITY_PAYMENT");

			// start CONST_HEAD
			serializer.startTag(null, "CONST_HEAD");

			serializer.startTag(null, "REQUEST_TYPE");
			serializer.text(REQUEST_TYPE);
			serializer.endTag(null, "REQUEST_TYPE");

			serializer.startTag(null, "REQUEST_MERCH");
			serializer.text(REQUEST_MERCH);
			serializer.endTag(null, "REQUEST_MERCH");

			serializer.startTag(null, "AGENT_CODE");
			serializer.text(AGENT_CODE);
			serializer.endTag(null, "AGENT_CODE");

			Log.i("tag", "AGENT_CODE");

			serializer.startTag(null, "TRN_CODE");
			serializer.text(TRN_CODE);
			serializer.endTag(null, "TRN_CODE");

			serializer.startTag(null, "FRONT_TRACENO");
			serializer.text(FRONT_TRACENO);
			serializer.endTag(null, "FRONT_TRACENO");

			serializer.startTag(null, "FRONT_DATE");
			serializer.text(FRONT_DATE);
			serializer.endTag(null, "FRONT_DATE");

			Log.i("tag", "FRONT_DATE");

			serializer.startTag(null, "FRONT_TIME");
			serializer.text(FRONT_TIME);
			serializer.endTag(null, "FRONT_TIME");

			// end CONST_HEAD
			serializer.endTag(null, "CONST_HEAD");

			// start DATA_AREA
			serializer.startTag(null, "DATA_AREA");

			serializer.startTag(null, "TX_CODE");
			serializer.text(TX_CODE);
			serializer.endTag(null, "TX_CODE");

			serializer.startTag(null, "USER_ID");
			serializer.text(USER_ID);
			serializer.endTag(null, "USER_ID");

			if ("MS002001".equals(TX_CODE)) {
				serializer.startTag(null, "DATE");
				serializer.text(date);
				serializer.endTag(null, "DATE");
			} else if ("MS002002".equals(TX_CODE)) {
				serializer.startTag(null, "EVENT_ID");
				serializer.text(eventId);
				serializer.endTag(null, "EVENT_ID");
			} else if ("MS002003".equals(TX_CODE)) {
				/*
				 * serializer.startTag(null, "TIME"); serializer.text(time);
				 * serializer.endTag(null, "TIME");
				 */

				serializer.startTag(null, "REMIND_TIME");
				serializer.text(remindTime);
				serializer.endTag(null, "REMIND_TIME");

				serializer.startTag(null, "REPEAT_VALUE");
				serializer.text(repeatValue);
				serializer.endTag(null, "REPEAT_VALUE");

				serializer.startTag(null, "CONTENT");
				serializer.text(content);
				serializer.endTag(null, "CONTENT");

				/*
				 * serializer.startTag(null, "OVER_TIME");
				 * serializer.text(endTime); serializer.endTag(null,
				 * "OVER_TIME");
				 */

				serializer.startTag(null, "TYPE");
				serializer.text(type);
				serializer.endTag(null, "TYPE");

				/*
				 * serializer.startTag(null, "REMIND"); serializer.text(remind);
				 * serializer.endTag(null, "REMIND");
				 */

			} else if ("MS002004".equals(TX_CODE)) {
				serializer.startTag(null, "EVENT_ID");
				serializer.text(eventId);
				serializer.endTag(null, "EVENT_ID");

				serializer.startTag(null, "REMIND_TIME");
				serializer.text(remindTime);
				serializer.endTag(null, "REMIND_TIME");

				serializer.startTag(null, "REPEAT_VALUE");
				serializer.text(repeatValue);
				serializer.endTag(null, "REPEAT_VALUE");

				serializer.startTag(null, "CONTENT");
				serializer.text(content);
				serializer.endTag(null, "CONTENT");

				// serializer.startTag(null, "OVER_TIME");
				// serializer.text(endTime);
				// serializer.endTag(null, "OVER_TIME");

				serializer.startTag(null, "TYPE");
				serializer.text(type);
				serializer.endTag(null, "TYPE");
			} else if ("MS002005".equals(TX_CODE)) {
				serializer.startTag(null, "PAGE_NO");
				serializer.text(pageNo);
				serializer.endTag(null, "PAGE_NO");

				// serializer.startTag(null, "PAGE_SIZE");
				// serializer.text(pageSize);
				// serializer.endTag(null, "PAGE_SIZE");
			} else if ("MS002006".equals(TX_CODE)) {
				// serializer.startTag(null, "PAGE_SIZE");
				// serializer.text(pageSize);
				// serializer.endTag(null, "PAGE_SIZE");

				serializer.startTag(null, "DATE");
				serializer.text(date);
				serializer.endTag(null, "DATE");
			} else if ("MS002007".equals(TX_CODE)) {
				serializer.startTag(null, "SYS_ID");
				serializer.text(sysId);
				serializer.endTag(null, "SYS_ID");
				serializer.startTag(null, "ADDRESS_ID");
				serializer.text(addressId);
				serializer.endTag(null, "ADDRESS_ID");
				serializer.startTag(null, "ORDER_DATE");
				serializer.text(orderDate);
				serializer.endTag(null, "ORDER_DATE");
				serializer.startTag(null, "USER_CODE");
				serializer.text(userCode);
				serializer.endTag(null, "USER_CODE");
			} else if ("MS002010".equals(TX_CODE)) {
				serializer.startTag(null, "DATE");
				serializer.text(date);
				serializer.endTag(null, "DATE");
			}
			// end DATA_AREA
			serializer.endTag(null, "DATA_AREA");
			// end UTILITY_PAYMENT
			serializer.endTag(null, "UTILITY_PAYMENT");
			serializer.endDocument();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return stringWriter.toString();
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getRemind() {
		return remind;
	}

	public void setRemind(String remind) {
		this.remind = remind;
	}

	public String getPageNo() {
		return pageNo;
	}

	public void setPageNo(String pageNo) {
		this.pageNo = pageNo;
	}

	public String getPageSize() {
		return pageSize;
	}

	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}

	public String getRemindTime() {
		return remindTime;
	}

	public void setRemindTime(String remindTime) {
		this.remindTime = remindTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getSysId() {
		return sysId;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
	}

	public String getAddressId() {
		return addressId;
	}

	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getRepeatValue() {
		return repeatValue;
	}

	public void setRepeatValue(String repeatValue) {
		this.repeatValue = repeatValue;
	}

}
