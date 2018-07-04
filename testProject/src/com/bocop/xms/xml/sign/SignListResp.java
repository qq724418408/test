package com.bocop.xms.xml.sign;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.bocop.xms.xml.remind.EventBean;

import android.util.Log;


public class SignListResp {
	public SignListResp(){
		
	}
	
    public static SignListXmlBean readStringXml(String cspXML) {
    	DocumentBuilderFactory factory = null;
		DocumentBuilder builder = null;
		Document document = null;
		byte[] binaryData = cspXML.getBytes();
		InputStream inStream = new ByteArrayInputStream(binaryData);
		// 获取DOM解析的工厂
		// Log.i("tag",binaryData.toString());
		factory = DocumentBuilderFactory.newInstance();
    	SignListXmlBean signInfo=new SignListXmlBean();
    	
    	try {
			builder = factory.newDocumentBuilder();
			document = builder.parse(inStream); // 获取解析器
			Element root = document.getDocumentElement();
			NodeList nodeh = root.getElementsByTagName("CONST_HEAD");
			Element dfheadElement = (Element) (nodeh.item(0));
			// 获取ERR_CODE节点
			Element err_code = (Element) dfheadElement.getElementsByTagName("ERR_CODE").item(0);
			signInfo.setErrorcode(err_code.getFirstChild().getNodeValue());
			Log.i("tag", "CSP查询返回码：" + signInfo.getErrorcode());

			if (!signInfo.getErrorcode().toString().equals("00")) {
				// 获取ERR_CODE节点
				Log.i("tag", "CSP返回错误码");
				Element err_msg = (Element) dfheadElement.getElementsByTagName("ERR_MSG").item(0);
				signInfo.setErrormsg(err_msg.getFirstChild().getNodeValue());
				Log.i("tag", signInfo.getErrormsg());
			} else {
				// 获取根节点下的DATA_AREA节点
				NodeList nodes = root.getElementsByTagName("DATA_AREA");
				// 获取DATA_AREA元素节点
				Element infoElement = (Element) (nodes.item(0));
				NodeList dataList = infoElement.getElementsByTagName("LIST");
				List<SignBean> signList = new ArrayList<SignBean>();
				for (int i = 0; i < dataList.getLength(); i++) {
					SignBean signBean=new SignBean();
					Element eventElement = (Element) dataList.item(i);
					String type=eventElement.getElementsByTagName("TYPE").item(0).
							getFirstChild().getNodeValue();
					String isSigned=eventElement.getElementsByTagName("ISSIGNED").item(0).
							getFirstChild().getNodeValue();
					signBean.setType(type);
					signBean.setIsSigned(isSigned);
					signList.add(signBean);
				}
				signInfo.setSignListBean(signList);
			}
			
			return signInfo;
			
	    	
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    	
    	return null;
	
    }

}
