package com.bocop.jxplatform.xml;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.xmlpull.v1.XmlSerializer;

import com.bocop.jxplatform.config.TransactionValue;

import android.util.Xml;

/**
 * 交通违章缴费交易
 * @author zhongye
 *处罚决定书编号、CSP交易码、缴费账号、应缴金额
 */
public class CspXmlForPay {
	
	public CspXmlForPay(String strpenaltyNum,String strCardNo,String strSjAmt,String strUserId){
		penaltyNum = strpenaltyNum;
		cardNo = strCardNo;
		sjAmt = strSjAmt;
		USER_ID = strUserId;
	}
	
	private static String REQUEST_TYPE = "0200";
	private static String REQUEST_MERCH = "BOCOP000";
	private static String AGENT_CODE = "05079101";
//	private static String AGENT_CODE = "07079101";
	public static String TRN_CODE = TransactionValue.APJJ13;
	private String FRONT_TRACENO = "000000000000000";
	
	private String cardNo;
	private String penaltyNum;
	private String sjAmt;
	
	private String USER_CODE;
	private String USER_ID;
	private String FRONT_DATE;
	private String FRONT_TIME;
	
	
	//生产CSPXML报文
	public String getCspXml(){
		
        XmlSerializer serializer = Xml.newSerializer();
        StringWriter stringWriter = new StringWriter();
        
        //获取时间
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		FRONT_DATE = format.format(new Date(System.currentTimeMillis()));
		
		SimpleDateFormat format_h = new SimpleDateFormat("HHmmss");
		FRONT_TIME = format_h.format(new Date(System.currentTimeMillis()));
		
		Random random = new Random();
//		random.nextInt(10);
//		FRONT_TRACENO = FRONT_DATE + FRONT_TIME +String.valueOf(1 + (int)Math.random()*9);
		FRONT_TRACENO = FRONT_DATE + FRONT_TIME + random.nextInt(9);

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
            
            //end CONST_HEAD
            serializer.endTag(null, "CONST_HEAD");
            
            //start DATA_AREA
            serializer.startTag(null, "DATA_AREA");
            
            serializer.startTag(null, "USER_CODE");
            serializer.text(penaltyNum);
            serializer.endTag(null, "USER_CODE");
            
            serializer.startTag(null, "SJ_AMT");
            serializer.text(sjAmt);
            serializer.endTag(null, "SJ_AMT");
            
            serializer.startTag(null, "ACCT_NO");
            serializer.text(cardNo);
            serializer.endTag(null, "ACCT_NO");
            
            serializer.startTag(null, "ChannelFlag");
            serializer.text(TransactionValue.CHANNELFLAG);
            serializer.endTag(null, "ChannelFlag");
            
            serializer.startTag(null, "UserId");
            serializer.text(USER_ID);
            serializer.endTag(null, "UserId");
            
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

}
