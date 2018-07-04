package com.bocop.xms.xml.remind;

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

import android.util.Log;

/**
 * 提醒列表
 * 
 * @author ftl
 * 
 */
public class EventListResp {
	
	public EventListResp() {
		
	}

	/**
	 * 将XML字符串解析，转换对象
	 * @param cspXML
	 * @return
	 */
	public static EventListXmlBean readStringXml(String cspXML) {
		DocumentBuilderFactory factory = null;
		DocumentBuilder builder = null;
		Document document = null;
		byte[] binaryData = cspXML.getBytes();
		InputStream inStream = new ByteArrayInputStream(binaryData);
		// 获取DOM解析的工厂
		// Log.i("tag",binaryData.toString());
		factory = DocumentBuilderFactory.newInstance();
		EventListXmlBean info = new EventListXmlBean();
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

			if (!"00".equals(info.getErrorcode().toString())) {
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
				NodeList dataList = infoElement.getElementsByTagName("EVENT");
				List<EventBean> eventList = new ArrayList<EventBean>();
				for (int i = 0; i < dataList.getLength(); i++) {
					Log.i("tag","i:" + String.valueOf(i));
					EventBean eventBean = new EventBean();
					Element eventElement = (Element) dataList.item(i);
					String eventId = eventElement.getElementsByTagName("EVENT_ID").item(0).
							getFirstChild().getNodeValue();
					String remindtime = eventElement.getElementsByTagName("REMIND_TIME").item(0).
							getFirstChild().getNodeValue();
					String content = eventElement.getElementsByTagName("CONTENT").item(0).
							getFirstChild().getNodeValue();
					/*String overtime = eventElement.getElementsByTagName("OVER_TIME").item(0).
							getFirstChild().getNodeValue();*/
					String type=eventElement.getElementsByTagName("TYPE").item(0).
							getFirstChild().getNodeValue();
					String repeatValue=eventElement.getElementsByTagName("REPEAT_VALUE").item(0).
							getFirstChild().getNodeValue();
					eventBean.setEventId(eventId);
					eventBean.setRemindtime(remindtime);
					eventBean.setContent(content);
					//eventBean.setEndtime(overtime);
					eventBean.setType(type);
					eventBean.setRepeatValue(repeatValue);
					eventList.add(eventBean);
				}
				info.setEventList(eventList);
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
