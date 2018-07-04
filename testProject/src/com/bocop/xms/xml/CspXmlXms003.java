package com.bocop.xms.xml;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.xmlpull.v1.XmlSerializer;

import android.util.Xml;

import com.bocop.jxplatform.config.TransactionValue;

/**
 * 生产XML代码，用户取消协议
 * @author ftl
 *
 */
public class CspXmlXms003 {
	
	public CspXmlXms003(String strUserId, String userCode, String sysId, String addressId,String type){
		USER_ID = strUserId;
		USER_CODE = userCode;
		SYS_ID = sysId;
		ADDRESS_ID = addressId;
		TYPE = type;
	}
	private static String REQUEST_TYPE = "0240";
	private static String REQUEST_MERCH = "BOCOP000";
	private static String AGENT_CODE = "05079101";
	private static String TRN_CODE = TransactionValue.CSPSZF;
	private static String FRONT_TRACENO = "000000000000000";
	
	private String FRONT_DATE;
	private String FRONT_TIME;
	private String USER_ID;
	private String USER_CODE;
	private String HANDLE_TYPE = "D";
	private String SYS_ID;
	private String ADDRESS_ID;
	private String TYPE;
	
	
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
            
            serializer.startTag(null, "HANDLE_TYPE");
            serializer.text(HANDLE_TYPE);
            serializer.endTag(null, "HANDLE_TYPE");
            
            
            //end CONST_HEAD
            serializer.endTag(null, "CONST_HEAD");
            
            //start DATA_AREA
            serializer.startTag(null, "DATA_AREA");
            
            serializer.startTag(null, "TX_CODE");
            serializer.text("00010003");
            serializer.endTag(null, "TX_CODE");
            
            serializer.startTag(null, "USER_ID");
            serializer.text(USER_ID);
            serializer.endTag(null, "USER_ID");
            
            serializer.startTag(null, "USER_CODE");
            serializer.text(USER_CODE);
            serializer.endTag(null, "USER_CODE");
            
            serializer.startTag(null, "TYPE");
            serializer.text(TYPE);
            serializer.endTag(null, "TYPE");
            
            serializer.startTag(null, "SYS_ID");
            serializer.text(SYS_ID);
            serializer.endTag(null, "SYS_ID");
            
            serializer.startTag(null, "ADDRESS_ID");
            serializer.text(ADDRESS_ID);
            serializer.endTag(null, "ADDRESS_ID");
            
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
