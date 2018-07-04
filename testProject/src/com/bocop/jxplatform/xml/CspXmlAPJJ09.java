package com.bocop.jxplatform.xml;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.xmlpull.v1.XmlSerializer;

import com.bocop.jxplatform.config.TransactionValue;

import android.util.Xml;

/**
 * 生产XML代码，绑定车辆
 * @author zhongye
 *
 */
public class CspXmlAPJJ09 {
	
	public CspXmlAPJJ09(String strLicenseNum,String strUserId,String strPeccancySequenceNum){
		LicenseNum = strLicenseNum;
		USER_ID = strUserId;
		PeccancySequenceNum = strPeccancySequenceNum;
	}
	private static String REQUEST_TYPE = "0200";
	private static String REQUEST_MERCH = "BOCOP000";
	private static String AGENT_CODE = "05079101";
	private static String TRN_CODE = TransactionValue.APJJ18;
	private static String FRONT_TRACENO = "000000000000000";
	
	private String USER_ID;
	private String FRONT_DATE;
	private String FRONT_TIME;
	private String LicenseNum;
	private String PeccancySequenceNum;
	
	
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
            
            //end CONST_HEAD
            serializer.endTag(null, "CONST_HEAD");
            
            //start DATA_AREA
            serializer.startTag(null, "DATA_AREA");
            //违法记录号
            serializer.startTag(null, "PeccancySequenceNum");
            serializer.text(PeccancySequenceNum);
            serializer.endTag(null, "PeccancySequenceNum");
            
          //渠道标示
            serializer.startTag(null, "ChannelFlag");
            serializer.text(TransactionValue.CHANNELFLAG);
            serializer.endTag(null, "ChannelFlag");
            
          //用户ID
            serializer.startTag(null, "UserId");
            serializer.text(USER_ID);
            serializer.endTag(null, "UserId");
            
          //违法记录号
            serializer.startTag(null, "LicenseNum");
            serializer.text(LicenseNum);
            serializer.endTag(null, "LicenseNum");
            
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
