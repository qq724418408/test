/**
 * 
 */
package com.bocop.jxplatform.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** 
 * @author luoyang  E-mail: luoyang8714@163.com
 * @version 创建时间：2016-10-13 上午10:35:20 
 * 类说明 
 */
/**
 * @author zhongye
 *
 */
public class RegularCheck {

	/**
	 * 
	 */
	public RegularCheck() {
		// TODO Auto-generated constructor stub
	}
	
	public static boolean isMobileNO(String mobiles) {   
        Pattern p = Pattern   
                .compile("^((13[0-9])|(15[^4,//D])|(18[0,5-9]))//d{8}$");   
        Matcher m = p.matcher(mobiles);   
        return m.matches();   
    } 
	
	/**
     * 验证手机格式
     */
    public static boolean isMobile(String number) {
    /*
	    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
	    联通：130、131、132、152、155、156、185、186
	    电信：133、153、180、189、（1349卫通）
	    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
    	String num = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
//    	String num = "^(\\d{14}|\\d{17})(\\d|[xX])$";
            //matches():字符串是否在给定的正则表达式匹配
    	return number.matches(num);

    }
    
//    \d{15}|\d{18}
	
	public static boolean isEmail(String strEmail) {    
//      String strPattern = "^[a-zA-Z][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$"; 
        String strPattern = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";   
        Pattern p = Pattern.compile(strPattern);   
        Matcher m = p.matcher(strEmail);   
        return m.matches();   
    }  
	
	public static boolean isId(String strId) {    
        String strPattern = "^(\\d{14}|\\d{17})(\\d|[xX])$";   
        Pattern p = Pattern.compile(strPattern);   
        Matcher m = p.matcher(strId);   
        return m.matches();   
    }  
	
	public static boolean isName(String strName) {    
        String strPattern = "^[\u4e00-\u9fa5]{2,10}$";   
        Pattern p = Pattern.compile(strPattern);   
        Matcher m = p.matcher(strName);   
        return m.matches();   
    }  

}
