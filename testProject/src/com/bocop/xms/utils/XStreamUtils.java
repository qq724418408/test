package com.bocop.xms.utils;

import com.thoughtworks.xstream.XStream;

public class XStreamUtils {

	 /**
     * xml转对象
     * @param xmlStr
     * @param tclass
     * @param <T>
     * @return
     */
    public static <T> T getFromXML(String xmlStr, Class<T> tclass) {
        XStream xStream = getXStream();
        xStream.processAnnotations(tclass);
        xStream.useAttributeFor(tclass);
        T t = tclass.cast(xStream.fromXML(xmlStr));
        return t;
    }

    /**
     * XML处理器初始化
     * @return
     */
    public static XStream getXStream() {
        XStream xStream = new XStream();
        xStream.autodetectAnnotations(true);
        return xStream;
    }
}
