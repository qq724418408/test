/**
 * 
 */
package com.bocop.jxplatform.util;
/** 
 * @author luoyang  E-mail: luoyang8714@163.com
 * @version 创建时间：2017-4-20 下午6:02:26 
 * 类说明 
 */
/**
 * @author zhongye
 *
 */
public class ShuZu {

	/**
	 * 
	 */
	public ShuZu() {
		// TODO Auto-generated constructor stub
	}

	public static int getIndex(String id,String[] idString) {  
	        for (int i = 0; i < idString.length; i++) {  
	            if (idString[i].equals(id)) {  
	                return i;  
	            }  
	        }  
	        return -1;  //若数组中没有则返回-1  
	    } 
}
