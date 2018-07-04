package com.bocop.jxplatform.xml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.bocop.jxplatform.bean.CarListXmlBean;

import android.util.Log;

/**
 * 根据用户ID查询 车牌列表
 * 
 * @author zhongye
 * 
 */
public class CspRecForCarList {
	public CspRecForCarList() {
	}

	/**
	 * 将XML字符串解析，转换对象
	 * @param cspXML
	 * @return
	 */
	public static CarListXmlBean readStringXml(String cspXML) {
		DocumentBuilderFactory factory = null;
		DocumentBuilder builder = null;
		Document document = null;
		// Log.i("tag","000000000");
		// Log.i("tag",cspXML);
		byte[] binaryData = cspXML.getBytes();
		InputStream inStream = new ByteArrayInputStream(binaryData);
		// 获取DOM解析的工厂
		// Log.i("tag",binaryData.toString());
		factory = DocumentBuilderFactory.newInstance();
		CarListXmlBean info = new CarListXmlBean();
		try {
			builder = factory.newDocumentBuilder();
			document = builder.parse(inStream); // 获取解析器
			Element root = document.getDocumentElement();
			// 获取CONST_HEAD节点，判断电费查询是否成功。
			NodeList nodeh = root.getElementsByTagName("CONST_HEAD");
			Element dfheadElement = (Element) (nodeh.item(0));
			// Log.i("tag","888888888");
			// 获取ERR_CODE节点
			Element err_code = (Element) dfheadElement.getElementsByTagName(
					"ERR_CODE").item(0);
			// Log.i("tag","999999999");
			info.setErrorcode(err_code.getFirstChild().getNodeValue());

			Log.i("tag", "CSP查询返回码：" + info.getErrorcode());

			if (!info.getErrorcode().toString().equals("00")) {
				// 获取ERR_CODE节点
				Log.i("tag", "CSP返回错误码");
				Element err_msg = (Element) dfheadElement.getElementsByTagName(
						"ERR_MSG").item(0);
				info.setErrormsg(err_msg.getFirstChild().getNodeValue());
				Log.i("tag", info.getErrormsg());
			} else {
				// Log.i("tag","1313");
				// 获取根节点下的DATA_AREA节点
				NodeList nodes = root.getElementsByTagName("DATA_AREA");
				// 获取DATA_AREA元素节点
				Element infoElement = (Element) (nodes.item(0));
				// 获取USER_CODE
				Element noList = (Element) infoElement.getElementsByTagName(
						"CarNum").item(0);
				int num = Integer.parseInt(noList.getFirstChild()
						.getNodeValue());
				Log.i("tag", String.valueOf(num));
				String[] licenseNumList = new String[num];
//				NodeList dataList = root.getElementsByTagName("DATA_LIST");
//				Element licenseNum = (Element) dataList.item(0);
//				NodeList licenseNums = licenseNum.getElementsByTagName("LicenseNum");
//				Log.i("tag", String.valueOf(dataList.getLength()));
//				Log.i("tag",String.valueOf(licenseNums.getLength()) );
				NodeList dataList = root.getElementsByTagName("DATA_LIST");
				for (int i = 0; i < num; i++) {
					Log.i("tag","i:" + String.valueOf(i));
					Element licenseNum = (Element) dataList.item(i);
					NodeList licenseNums = licenseNum.getElementsByTagName("LicenseNum");
					Element dateElement = (Element) (licenseNums.item(0));
//					Log.i("tag",String.valueOf(i) +"1");
//					licenseNumList[i] = dateElement.getFirstChild()
//							.getNodeValue();
					licenseNumList[i] = dateElement.getFirstChild().getNodeValue();
					Log.i("tag",String.valueOf(i));
				}
				info.setLicenseNumList(licenseNumList);
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
