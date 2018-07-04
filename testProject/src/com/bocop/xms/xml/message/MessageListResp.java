package com.bocop.xms.xml.message;

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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.util.Log;

/**
 * 我的消息列表
 * 
 * @author ftl
 * 
 */
public class MessageListResp {

	public MessageListResp() {

	}

	/**
	 * 将XML字符串解析，转换对象
	 * 
	 * @param cspXML
	 * @return
	 */
	public static MessageListXmlBean readStringXml(String cspXML) {
		DocumentBuilderFactory factory = null;
		DocumentBuilder builder = null;
		Document document = null;
		byte[] binaryData = cspXML.getBytes();
		InputStream inStream = new ByteArrayInputStream(binaryData);
		// 获取DOM解析的工厂
		// Log.i("tag",binaryData.toString());
		factory = DocumentBuilderFactory.newInstance();
		MessageListXmlBean info = new MessageListXmlBean();
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
				NodeList dataList = infoElement.getElementsByTagName("pushInfo");
				List<MessageBean> messageList = new ArrayList<MessageBean>();
				for (int i = 0; i < dataList.getLength(); i++) {
					Log.i("tag", "i:" + String.valueOf(i));
					MessageBean messageBean = new MessageBean();
					Element messageElement = (Element) dataList.item(i);
					String messageId = messageElement.getElementsByTagName("PUSH_ID").item(0).getFirstChild()
							.getNodeValue();
					String type = messageElement.getElementsByTagName("TYPE").item(0).getFirstChild().getNodeValue();
					String content = messageElement.getElementsByTagName("PUSH_TEXT").item(0).getFirstChild()
							.getNodeValue();
					Node dateNode = messageElement.getElementsByTagName("DATE").item(0);
					Node pushDateNode = messageElement.getElementsByTagName("PUSH_DATE").item(0);
					if (dateNode != null) {
						String date = dateNode.getFirstChild().getNodeValue();
						messageBean.setDate(date);
					}
					if (pushDateNode != null) {
						String date = pushDateNode.getFirstChild().getNodeValue();
						messageBean.setDate(date);
					}
					Node urlNode = messageElement.getElementsByTagName("URL").item(0);
					if (urlNode != null) {
						String url = urlNode.getFirstChild().getNodeValue();
						messageBean.setUrl(url);
					}
					messageBean.setMessageId(messageId);
					messageBean.setType(type);
					messageBean.setContent(content);
					messageList.add(messageBean);
				}
				info.setMessageList(messageList);
			}

			Log.i("tag", "返回");
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
