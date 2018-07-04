package com.bocop.yfx.xml;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.xmlpull.v1.XmlSerializer;

import android.util.Xml;

import com.bocop.jxplatform.config.TransactionValue;

/**
 * 
 * 确认还款
 * 
 * @author lh
 * 
 */
public class CspXmlYfx011 {
	private static String REQUEST_TYPE = "0240";
	private static String REQUEST_MERCH = "BOCOP000";
	private static String AGENT_CODE = "05079101";
	private static String TRN_CODE = TransactionValue.CSPGXD;
	private static String FRONT_TRACENO = "000000000000000";

	private String FRONT_DATE;
	private String FRONT_TIME;
	private String WLS_CUST_ID;// 客户号
	private String WLS_ACCT_NO;// 贷款账号
	private String WLS_REPAYMENT_AMOUNT;// 本次实际还款金额
	private String WLS_REPAYMENT_INTEREST;// 利息
	
	

	public CspXmlYfx011(String wLS_CUST_ID, String wLS_ACCT_NO, String wLS_REPAYMENT_AMOUNT,String wLS_REPAYMENT_INTEREST) {
		super();
		WLS_CUST_ID = wLS_CUST_ID;
		WLS_ACCT_NO = wLS_ACCT_NO;
		WLS_REPAYMENT_AMOUNT = wLS_REPAYMENT_AMOUNT;
		WLS_REPAYMENT_INTEREST = wLS_REPAYMENT_INTEREST;
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
			serializer.text("WL001011");
			serializer.endTag(null, "TX_CODE");

			serializer.startTag(null, "WLS_CUST_ID");
			serializer.text(WLS_CUST_ID);
			serializer.endTag(null, "WLS_CUST_ID");

			serializer.startTag(null, "WLS_ACCT_NO");
			serializer.text(WLS_ACCT_NO);
			serializer.endTag(null, "WLS_ACCT_NO");

			serializer.startTag(null, "WLS_REPAYMENT_AMOUNT");
			serializer.text(WLS_REPAYMENT_AMOUNT);
			serializer.endTag(null, "WLS_REPAYMENT_AMOUNT");
			
			serializer.startTag(null, "WLS_REPAYMENT_INTEREST");
			serializer.text(WLS_REPAYMENT_INTEREST);
			serializer.endTag(null, "WLS_REPAYMENT_INTEREST");

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
