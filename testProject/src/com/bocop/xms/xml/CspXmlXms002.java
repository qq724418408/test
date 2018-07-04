package com.bocop.xms.xml;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.xmlpull.v1.XmlSerializer;

import android.util.Log;
import android.util.Xml;

import com.bocop.jxplatform.config.TransactionValue;

/**
 * 生产XML代码，用户签约保存/姓名获取
 * @author ftl
 *
 */
public class CspXmlXms002 {
	
	public CspXmlXms002(String type, String strUserId,  String userName, String phone, String address, 
			String agentCode, String userCode, String orderDate, String sysId, String trnCode, 
			String addressId, String txCode){
		TYPE = type;
		USER_ID = strUserId;
		USER_NAME = userName;
		PHONE = phone;
		ADDRESS = address;
		AGENTCODE = agentCode;
		USER_CODE = userCode;
		ORDER_DATE = orderDate;
		SYS_ID = sysId;
		TRNCODE = trnCode;
		ADDRESS_ID = addressId;
		TX_CODE = txCode;
	}
	private static String REQUEST_TYPE = "0240";
	private static String REQUEST_MERCH = "BOCOP000";
	private static String AGENT_CODE = "05079101";
	private static String TRN_CODE = TransactionValue.CSPSZF;
	private static String FRONT_TRACENO = "000000000000000";
	
	private String FRONT_DATE;
	private String FRONT_TIME;
	private String TYPE;//区分是哪一种缴费，水费01，电费02，煤气费03，有线电视04，移动通讯05
	private String USER_ID;//中银用户号
	private String USER_NAME;//中银用户名
	private String PHONE;//用户手机号
	private String ADDRESS;//缴费地区
	private String AGENTCODE;//缴费单位
	private String USER_CODE;//用户号码
	private String ORDER_DATE;//1到28
	private String SYS_ID;//请求缴费单位时会有对应sysId
	private String HANDLE_TYPE = "A";//签约请求的类型：判断保存还是删除   内容："A"-保存  ,"D" - 删除
	private String TRNCODE;
	private String ADDRESS_ID;
	private String TX_CODE;
	
	private String subscriberno = "";//有线编号, 当然有线电视签约,单位是江西广播电视局时,对多一栏 "智能卡号/上网账号"要填  的值 
	private String servId = "";//缴费类型,当缴费单位为：景德镇龙源管道燃气有限公司、九江深燃天然气有限公司 这两个公司时才需要传此参
	private String devTyp = "";//设备类型,10 -电话通讯费（含固话手机） 20-宽带上网费,电信才需要
	private String servicetype = "";//有线收费类型,客户选择按客户编码查询时固定填"02"-数字有线：就是固定"02"
	private String pinpaiN = "";//南昌市广播电视传输公司才需要，内容是 0-普通有线 1-数字有线 2-宽带业务
	
	
	//生产CSPXML报文
	public String getCspXml(){
		
        XmlSerializer serializer = Xml.newSerializer();
        StringWriter stringWriter = new StringWriter();
        
        //获取时间
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		FRONT_DATE = format.format(new Date(System.currentTimeMillis()));
		
		SimpleDateFormat format_h = new SimpleDateFormat("HHmmss");
		FRONT_TIME = format_h.format(new Date(System.currentTimeMillis()));

        try {
            serializer.setOutput(stringWriter);
            serializer.startDocument("utf-8", false);
            // start UTILITY_PAYMENT
            serializer.startTag(null, "UTILITY_PAYMENT");
            
            //start CONST_HEAD
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
            
            //end CONST_HEAD
            serializer.endTag(null, "CONST_HEAD");
            
            //start DATA_AREA
            serializer.startTag(null, "DATA_AREA");
            
            serializer.startTag(null, "TX_CODE");
            serializer.text(TX_CODE);
            serializer.endTag(null, "TX_CODE");
            
            serializer.startTag(null, "AGENT_CODE");
            serializer.text(AGENTCODE);
            serializer.endTag(null, "AGENT_CODE");
            
            Log.i("tag", "AGENT_CODE");
            
            serializer.startTag(null, "TYPE");
            serializer.text(TYPE);
            serializer.endTag(null, "TYPE");
            
            serializer.startTag(null, "USER_ID");
            serializer.text(USER_ID);
            serializer.endTag(null, "USER_ID");
            
            serializer.startTag(null, "USER_NAME");
            serializer.text(USER_NAME);
            serializer.endTag(null, "USER_NAME");
            
            serializer.startTag(null, "PHONE");
            serializer.text(PHONE);
            serializer.endTag(null, "PHONE");
            
            Log.i("tag", "PHONE");
            
            serializer.startTag(null, "ADDRESS");
            serializer.text(ADDRESS);
            serializer.endTag(null, "ADDRESS");
            
            serializer.startTag(null, "USER_CODE");
            serializer.text(USER_CODE);
            serializer.endTag(null, "USER_CODE");
            
            serializer.startTag(null, "ORDER_DATE");
            serializer.text(ORDER_DATE);
            serializer.endTag(null, "ORDER_DATE");
            
            serializer.startTag(null, "SYS_ID");
            serializer.text(SYS_ID);
            serializer.endTag(null, "SYS_ID");
            
            serializer.startTag(null, "HANDLE_TYPE");
            serializer.text(HANDLE_TYPE);
            serializer.endTag(null, "HANDLE_TYPE");
            
            Log.i("tag", "HANDLE_TYPE");
            
            serializer.startTag(null, "TRN_CODE");
            serializer.text(TRNCODE);
            serializer.endTag(null, "TRN_CODE");
            
            Log.i("tag", "TRN_CODE");
            
            serializer.startTag(null, "ADDRESS_ID");
            serializer.text(ADDRESS_ID);
            serializer.endTag(null, "ADDRESS_ID");
            
            Log.i("tag", "ADDRESS_ID");
            
        	serializer.startTag(null, "SUBSCRIBERNO");
            serializer.text(subscriberno);
            serializer.endTag(null, "SUBSCRIBERNO");
            
            Log.i("tag", "SUBSCRIBERNO");

       	  	serializer.startTag(null, "SERV_ID");
            serializer.text(servId);
            serializer.endTag(null, "SERV_ID");
            
            Log.i("tag", "SERV_ID");

       	 	serializer.startTag(null, "DEV_TYP");
            serializer.text(devTyp);
            serializer.endTag(null, "DEV_TYP");

       	 	serializer.startTag(null, "SERVICETYPE");
            serializer.text(servicetype);
            serializer.endTag(null, "SERVICETYPE");

       	 	serializer.startTag(null, "PINPAI_N");
            serializer.text(pinpaiN);
            serializer.endTag(null, "PINPAI_N");
            
            Log.i("tag", "PINPAI_N");
            //end DATA_AREA
            serializer.endTag(null, "DATA_AREA");
            
            // end UTILITY_PAYMENT
            serializer.endTag(null, "UTILITY_PAYMENT");

            serializer.endDocument();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringWriter.toString();
	}


	public String getSubscriberno() {
		return subscriberno;
	}


	public void setSubscriberno(String subscriberno) {
		this.subscriberno = subscriberno;
	}


	public String getServId() {
		return servId;
	}


	public void setServId(String servId) {
		this.servId = servId;
	}


	public String getDevTyp() {
		return devTyp;
	}


	public void setDevTyp(String devTyp) {
		this.devTyp = devTyp;
	}


	public String getServicetype() {
		return servicetype;
	}


	public void setServicetype(String servicetype) {
		this.servicetype = servicetype;
	}


	public String getPinpaiN() {
		return pinpaiN;
	}


	public void setPinpaiN(String pinpaiN) {
		this.pinpaiN = pinpaiN;
	}

}
