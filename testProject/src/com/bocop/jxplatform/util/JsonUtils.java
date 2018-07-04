package com.bocop.jxplatform.util;


import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

/**
 * 功能：json数据的解析工具类
 * 
 * 作者：
 * 
 * 日期：2013 年 01 月 09 日
 * 
 * 引用JAR包：fastjson-1.1.22.jar【阿里巴巴封装的json处理包】
 * 
 * 说明文档名字及位置：
 * 
 */
public class JsonUtils {
	
	public JsonUtils() {
	}
	
	/**
	 * 使用JSON工具把数据转换成json对象
	 * @param value 是对解析的集合的类型
	 */
	public static String createJsonString(Object value)throws Exception {
		return JSON.toJSONString(value);
	}

	/**
	 * 对单个javabean进行解析
	 * @param <T>
	 * @param json	要解析的json字符串
	 * @param cls	实体bean的字节码对象
	 * @return		
	 */
	public static <T>T getObject(String json,Class<T> cls)throws Exception{
		return JSON.parseObject(json,cls);
	}
	
	/**
	 * 对list类型进行解析
	 * @param <T>
	 * @param json	要解析的json字符串
	 * @param cls	实体bean的字节码对象
	 * @return
	 */
	public static <T> List<T> getListObject(String json,Class<T> cls)throws Exception{
		return JSON.parseArray(json, cls);
	}
	
	/**
	 * 对MapString类型数据进行解析
	 * @param json	要解析的json字符串
	 * @return
	 */
	public static Map<String, String> getMapStr(String json)throws Exception{
		return JSON.parseObject(json, new TypeReference<Map<String, String>>(){});
	}
	
	/**
	 * 对MapObject类型数据进行解析
	 * @param json	要解析的json字符串
	 * @return
	 */
	public static Map<String, Object> getMapObj(String json)throws Exception{
		return JSON.parseObject(json, new TypeReference<Map<String, Object>>(){});
	}
	
	/**
	 * 对listmap类型进行解析
	 * @param json	要解析的json字符串
	 * @return
	 */
	public static List<Map<String, Object>> getListMap(String json)throws Exception{
		return JSON.parseObject(json,new TypeReference<List<Map<String, Object>>>(){});
	}

	public static Map<String, Object> getMapObjByTag(String json, String tag)throws Exception{
		Map<String, Object> map = getMapObj(json);
		if(map!=null){
			String strTag = String.valueOf(map.get(tag));
			return getMapObj(strTag);
		}
		return null;
	}
}
