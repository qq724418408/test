package com.bocop.zyyr.xml;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.xmlpull.v1.XmlSerializer;

import android.annotation.SuppressLint;
import android.util.Xml;

import com.bocop.jxplatform.config.TransactionValue;

/**
 * 生产XML代码，修改、添加认证资料
 * 
 * @author rd
 * 
 */
public class CspXmlZyyr002 {

	private static String REQUEST_TYPE = "0240";
	private static String REQUEST_MERCH = "BOCOP000";
	private static String AGENT_CODE = "05079101";
	private static String TRN_CODE = TransactionValue.CSPSZF;
	private static String FRONT_TRACENO = "000000000000000";

	private String FRONT_DATE;
	private String FRONT_TIME;
	private String USER_ID;

	private String CAREER;// 职业身份Key
	private String CAREER_VAL;// 职业身份值

	private String PHONE;// 手机号

	private String CORP_TYPE;// 就职公司类型key
	private String CORP_TYPE_VAL;// 就职公司类型值

	private String HOUSE_TP;// 名下房产类型key
	private String HOUSE_TP_VAL;// 名下房产类型值

	private String HAS_FUND;// 是否有本地基金
	private String HAS_INSURE; // 是否有本地社保
	private String HAS_CAR;// 是否有车
	private String WORKING_YEARS;// 工龄
	private String SALARY; // 发放工资
	private String CASH_INCOME; // 现金收入
	private String GJJ_PERIOD;// 公积金缴存时间
	private String SECURITY_PERIOD; // 社保缴纳时间
	private String HOUSE_VAL;// 房产估值
	private String CREDIT_STATUS; // 信用情况key
	private String CREDIT_STATUS_VAL; // 信用情况值

	public CspXmlZyyr002(String userId, String career, String careerVal,
			String phone, String corpType, String corpTpVal, String houseType,
			String houseTpVal, String hasFund, String hasInsure, String hasCar,
			String workingYear, String salary, String cashIncome,
			String gjjPeriod, String securityPeriod, String houseVal,
			String creditStatus, String creditVal) {
		USER_ID = userId;
		CAREER = career;
		CAREER_VAL = careerVal;
		PHONE = phone;
		CORP_TYPE = corpType;
		CORP_TYPE_VAL = corpTpVal;
		HOUSE_TP = houseType;
		HOUSE_TP_VAL = houseTpVal;
		HAS_FUND = hasFund;
		HAS_INSURE = hasInsure;
		HAS_CAR = hasCar;
		WORKING_YEARS = workingYear;
		SALARY = salary;
		CASH_INCOME = cashIncome;
		GJJ_PERIOD = gjjPeriod;
		SECURITY_PERIOD = securityPeriod;
		HOUSE_VAL = houseVal;
		CREDIT_STATUS = creditStatus;
		CREDIT_STATUS_VAL = creditVal;

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
			serializer.text(" 00020002");
			serializer.endTag(null, "TX_CODE");

			serializer.startTag(null, "USER_ID");
			serializer.text(USER_ID);
			serializer.endTag(null, "USER_ID");

			serializer.startTag(null, "CAREER");
			serializer.text(CAREER);
			serializer.endTag(null, "CAREER");

			serializer.startTag(null, "CAREER_VAL");
			serializer.text(CAREER_VAL);
			serializer.endTag(null, "CAREER_VAL");

			serializer.startTag(null, "PHONE");
			serializer.text(PHONE);
			serializer.endTag(null, "PHONE");

			serializer.startTag(null, "CORP_TYPE");
			serializer.text(CORP_TYPE);
			serializer.endTag(null, "CORP_TYPE");

			serializer.startTag(null, "CORP_TYPE_VAL");
			serializer.text(CORP_TYPE_VAL);
			serializer.endTag(null, "CORP_TYPE_VAL");

			serializer.startTag(null, "HOUSE_TP");
			serializer.text(HOUSE_TP);
			serializer.endTag(null, "HOUSE_TP");

			serializer.startTag(null, "HOUSE_TP_VAL");
			serializer.text(HOUSE_TP_VAL);
			serializer.endTag(null, "HOUSE_TP_VAL");

			serializer.startTag(null, "HAS_FUND");
			serializer.text(HAS_FUND);
			serializer.endTag(null, "HAS_FUND");

			serializer.startTag(null, "HAS_INSURE");
			serializer.text(HAS_INSURE);
			serializer.endTag(null, "HAS_INSURE");

			serializer.startTag(null, "HAS_CAR");
			serializer.text(HAS_CAR);
			serializer.endTag(null, "HAS_CAR");

			serializer.startTag(null, "WORKING_YEARS");
			serializer.text(WORKING_YEARS);
			serializer.endTag(null, "WORKING_YEARS");

			serializer.startTag(null, "SALARY");
			serializer.text(SALARY);
			serializer.endTag(null, "SALARY");

			serializer.startTag(null, "CASH_INCOME");
			serializer.text(CASH_INCOME);
			serializer.endTag(null, "CASH_INCOME");

			serializer.startTag(null, "GJJ_PERIOD");
			serializer.text(GJJ_PERIOD);
			serializer.endTag(null, "GJJ_PERIOD");

			serializer.startTag(null, "SECURITY_PERIOD");
			serializer.text(SECURITY_PERIOD);
			serializer.endTag(null, "SECURITY_PERIOD");

			serializer.startTag(null, "HOUSE_VAL");
			serializer.text(HOUSE_VAL);
			serializer.endTag(null, "HOUSE_VAL");

			serializer.startTag(null, "CREDIT_STATUS");
			serializer.text(CREDIT_STATUS);
			serializer.endTag(null, "CREDIT_STATUS");

			serializer.startTag(null, "CREDIT_STATUS_VAL");
			serializer.text(CREDIT_STATUS_VAL);
			serializer.endTag(null, "CREDIT_STATUS_VAL");

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
