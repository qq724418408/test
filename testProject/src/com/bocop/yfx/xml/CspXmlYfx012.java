package com.bocop.yfx.xml;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.xmlpull.v1.XmlSerializer;

import android.util.Xml;

import com.bocop.jxplatform.config.TransactionValue;

/**
 * 
 * 历史记录
 * 
 * @author lh
 * 
 */
public class CspXmlYfx012 {
	private static String REQUEST_TYPE = "0240";
	private static String REQUEST_MERCH = "BOCOP000";
	private static String AGENT_CODE = "05079101";
	private static String TRN_CODE = TransactionValue.CSPGXD;
	private static String FRONT_TRACENO = "000000000000000";

	private String FRONT_DATE;
	private String FRONT_TIME;
	private String WLS_CUST_ID;// 客户号
	private String WLS_APP_TYPE;//申请类别
	private String WLS_LIST_NUM;//每页数据条数
	private String WLS_CURR_PAGE;//页码
	
	
	public CspXmlYfx012(String wLS_CUST_ID, String wLS_APP_TYPE, String wLS_LIST_NUM, String wLS_CURR_PAGE) {
		super();
		WLS_CUST_ID = wLS_CUST_ID;
		WLS_APP_TYPE = wLS_APP_TYPE;
		WLS_LIST_NUM = wLS_LIST_NUM;
		WLS_CURR_PAGE = wLS_CURR_PAGE;
	}


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

			serializer.startTag(null, "TRN_CODE");
			serializer.text(TRN_CODE);
			serializer.endTag(null, "TRN_CODE");

			serializer.startTag(null, "FRONT_TRACENO");
			serializer.text(FRONT_TRACENO);
			serializer.endTag(null, "FRONT_TRACENO");

			serializer.startTag(null, "FRONT_DATE");
			serializer.text(FRONT_DATE);
			serializer.endTag(null, "FRONT_DATE");

			serializer.startTag(null, "FRONT_TIME");
			serializer.text(FRONT_TIME);
			serializer.endTag(null, "FRONT_TIME");

			// end CONST_HEAD
			serializer.endTag(null, "CONST_HEAD");

			// start DATA_AREA
			serializer.startTag(null, "DATA_AREA");

			serializer.startTag(null, "TX_CODE");
			serializer.text("WL001012");
			serializer.endTag(null, "TX_CODE");

			serializer.startTag(null, "WLS_CUST_ID");
			serializer.text(WLS_CUST_ID);
			serializer.endTag(null, "WLS_CUST_ID");

			serializer.startTag(null, "WLS_APP_TYPE");
			serializer.text(WLS_APP_TYPE);
			serializer.endTag(null, "WLS_APP_TYPE");

			serializer.startTag(null, "WLS_LIST_NUM");
			serializer.text(WLS_LIST_NUM);
			serializer.endTag(null, "WLS_LIST_NUM");
			
			serializer.startTag(null, "WLS_CURR_PAGE");
			serializer.text(WLS_CURR_PAGE);
			serializer.endTag(null, "WLS_CURR_PAGE");

			// end DATA_AREA
			serializer.endTag(null, "DATA_AREA");

			// end UTILITY_PAYMENT
			serializer.endTag(null, "UTILITY_PAYMENT");

			serializer.endDocument();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stringWriter.toString();
	}
}
