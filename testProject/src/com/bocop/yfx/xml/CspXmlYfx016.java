package com.bocop.yfx.xml;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.xmlpull.v1.XmlSerializer;

import com.bocop.jxplatform.config.TransactionValue;

import android.util.Xml;

/**
 * 
 * 身份认证
 * 
 * @author ftl
 * 
 */
public class CspXmlYfx016 {
	private static String REQUEST_TYPE = "0240";
	private static String REQUEST_MERCH = "BOCOP000";
	private static String AGENT_CODE = "05079101";
	private static String TRN_CODE = TransactionValue.CSPGXD;
	private static String FRONT_TRACENO = "000000000000000";

	private String FRONT_DATE;
	private String FRONT_TIME;
	private String WLS_ID_CARD;//身份证号
	private String WLS_CUST_TYPE;//入口类型，FN-抚农，FP-扶贫，GD-个贷，GJ-公积
	
	
	public CspXmlYfx016(String cardId, String custType) {
		super();
		WLS_ID_CARD = cardId;
		WLS_CUST_TYPE = custType;
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
			//serializer.text("WL001016");
			serializer.text("WL002016");
			serializer.endTag(null, "TX_CODE");

			serializer.startTag(null, "WLS_ID_CARD");
			serializer.text(WLS_ID_CARD);
			serializer.endTag(null, "WLS_ID_CARD");

			serializer.startTag(null, "WLS_CUST_TYPE");
			serializer.text(WLS_CUST_TYPE);
			serializer.endTag(null, "WLS_CUST_TYPE");

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
