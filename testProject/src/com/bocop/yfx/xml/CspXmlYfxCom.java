package com.bocop.yfx.xml;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.xmlpull.v1.XmlSerializer;

import com.bocop.jxplatform.config.TransactionValue;

import android.util.Xml;

/**
 * 剩余额度、提款数据、还款列表、（贷款、还款、申请记录）、消息列表
 *
 * @author lh
 */
public class CspXmlYfxCom {

	private static String REQUEST_TYPE = "0240";
	private static String REQUEST_MERCH = "BOCOP000";
	private static String AGENT_CODE = "05079101";
	private static String TRN_CODE = TransactionValue.CSPGXD;
	private static String FRONT_TRACENO = "000000000000000";

	private String FRONT_DATE;
	private String FRONT_TIME;
	/**
	 * <p>
	 * 剩余额度：WL001007
	 * </p>
	 * <p>
	 * 提款数据：WL001008
	 * </P>
	 * <p>
	 * 还款列表：WL001010
	 * </p>
	 * <p>
	 * 消息列表：WL001013
	 * </p>
	 */
	private String TX_CODE;// 请求编号
	private String WLS_CUST_ID;// 客户ID
	private String WLS_USER_ID;

	public CspXmlYfxCom(String tX_CODE, String wLS_CUST_ID) {
		super();
		TX_CODE = tX_CODE;
		WLS_CUST_ID = wLS_CUST_ID;
	}

	public CspXmlYfxCom(String tX_CODE, String wLS_CUST_ID, String wLS_USER_ID) {
		super();
		TX_CODE = tX_CODE;
		WLS_CUST_ID = wLS_CUST_ID;
		WLS_USER_ID = wLS_USER_ID;
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
			serializer.text(TX_CODE);
			serializer.endTag(null, "TX_CODE");

			if ("WL001013".equals(TX_CODE)) {
				serializer.startTag(null, "WLS_USER_ID");
				serializer.text(WLS_CUST_ID);
				serializer.endTag(null, "WLS_USER_ID");

			} else if ("WL001007".equals(TX_CODE) || "WL001008".equals(TX_CODE) 
					|| "WL002007".equals(TX_CODE) || "WL002008".equals(TX_CODE)) {
				serializer.startTag(null, "WLS_USER_ID");
				serializer.text(WLS_USER_ID);
				serializer.endTag(null, "WLS_USER_ID");

				serializer.startTag(null, "WLS_CUST_ID");
				serializer.text(WLS_CUST_ID);
				serializer.endTag(null, "WLS_CUST_ID");
			} else {
				serializer.startTag(null, "WLS_CUST_ID");
				serializer.text(WLS_CUST_ID);
				serializer.endTag(null, "WLS_CUST_ID");
			}

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
