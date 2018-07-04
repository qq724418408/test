package com.bocop.yfx.xml;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.xmlpull.v1.XmlSerializer;

import com.bocop.jxplatform.config.TransactionValue;
import com.bocop.yfx.bean.LinkManInfo;

import android.annotation.SuppressLint;
import android.util.Xml;

/**
 * 生产XML代码，补入个人信息
 * 
 * @author rd
 * 
 */
public class CspXmlYfx004 {

	private static String REQUEST_TYPE = "0240";
	private static String REQUEST_MERCH = "BOCOP000";
	private static String AGENT_CODE = "05079101";
	private static String TRN_CODE = TransactionValue.CSPGXD;
	private static String FRONT_TRACENO = "000000000000000";
	private String FRONT_DATE;
	private String FRONT_TIME;

	private String WLS_USER_ID;
	private String WLS_PHONE = "";
	private String WLS_ADDRESS;
	private List<LinkManInfo> INFORMATION_LIST;
	
	public CspXmlYfx004(String userId, String address,
			List<LinkManInfo> information) {
		WLS_USER_ID = userId;
		WLS_ADDRESS = address;
		INFORMATION_LIST = information;
	}

	public CspXmlYfx004(String userId, String phoneNum, String address,
			List<LinkManInfo> information) {
		WLS_USER_ID = userId;
		WLS_PHONE = phoneNum;
		WLS_ADDRESS = address;
		INFORMATION_LIST = information;
	}

	// 生产CSPXML报文
	@SuppressLint("SimpleDateFormat")
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
			serializer.text("WL001004");
			serializer.endTag(null, "TX_CODE");

			serializer.startTag(null, "WLS_USER_ID");
			serializer.text(WLS_USER_ID);
			serializer.endTag(null, "WLS_USER_ID");
			
			serializer.startTag(null, "WLS_PHONE");
			serializer.text(WLS_PHONE);
			serializer.endTag(null, "WLS_PHONE");

			serializer.startTag(null, "WLS_ADDRESS");
			serializer.text(WLS_ADDRESS);
			serializer.endTag(null, "WLS_ADDRESS");

			for (int i = 0; i < INFORMATION_LIST.size(); i++) {
				// Start List
				serializer.startTag(null, "INFORMATION_LIST");
				serializer.startTag(null, "WLS_TYPE");
				serializer.text(INFORMATION_LIST.get(i).getType());
				serializer.endTag(null, "WLS_TYPE");

				serializer.startTag(null, "WLS_NAME");
				serializer.text(INFORMATION_LIST.get(i).getName());
				serializer.endTag(null, "WLS_NAME");

				serializer.startTag(null, "WLS_PHONE");
				serializer.text(INFORMATION_LIST.get(i).getPhone());
				serializer.endTag(null, "WLS_PHONE");
				// end List
				serializer.endTag(null, "INFORMATION_LIST");
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

}
