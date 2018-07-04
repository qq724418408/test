package com.bocop.yfx.xml.identitycheck;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.util.Log;

/**
 * 身份验证响应
 * 
 */
public class IdentityCheckResp {
	
	public IdentityCheckResp() {
		
	}

	public static IdentityCheckBean readStringXml(String cspXML) {
		DocumentBuilderFactory factory = null;
		DocumentBuilder builder = null;
		Document document = null;
		byte[] binaryData = cspXML.getBytes();
		InputStream inStream = new ByteArrayInputStream(binaryData);
		// 获取DOM解析的工厂
		// Log.i("tag",binaryData.toString());
		factory = DocumentBuilderFactory.newInstance();
		IdentityCheckBean info = new IdentityCheckBean();
		try {
			builder = factory.newDocumentBuilder();
			document = builder.parse(inStream); // 获取解析器
			Element root = document.getDocumentElement();
			NodeList nodeh = root.getElementsByTagName("CONST_HEAD");
			Element dfheadElement = (Element) (nodeh.item(0));
			// 获取ERR_CODE节点
			Element err_code = (Element) dfheadElement.getElementsByTagName("ERR_CODE").item(0);
			info.setErrorcode(err_code.getFirstChild().getNodeValue());
			Log.i("tag", "CSP查询返回码：" + info.getErrorcode());

			if (!info.getErrorcode().toString().equals("00")) {
				// 获取ERR_CODE节点
				Log.i("tag", "CSP返回错误码");
				Element err_msg = (Element) dfheadElement.getElementsByTagName("ERR_MSG").item(0);
				info.setErrormsg(err_msg.getFirstChild().getNodeValue());
				Log.i("tag", info.getErrormsg());
			} else {
				// 获取根节点下的DATA_AREA节点
				NodeList nodes = root.getElementsByTagName("DATA_AREA");
				// 获取DATA_AREA元素节点
				Element infoElement = (Element) (nodes.item(0));
				Node userIdNode = infoElement.getElementsByTagName("WLS_ID_CARD").item(0).getFirstChild();
				Node custTypeNode = infoElement.getElementsByTagName("WLS_CUST_TYPE").item(0).getFirstChild();
				String userId = userIdNode == null ? "" : userIdNode.getNodeValue();
				String custType = custTypeNode == null ? "" : custTypeNode.getNodeValue();
				info.setUserId(userId);
				info.setCustType(custType);
			}
			Log.i("tag","返回");
			return info;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return null;
	}
}
