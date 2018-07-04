package com.bocop.jxplatform.gesture.util;


/**
 * 
 * @author cindy
 * 数据格式工具类
 */
public class MathUtil{
	public static double distance(double x1,double y1,double x2,double y2){
		return Math.sqrt(Math.abs(x1-x2)*Math.abs(x1-x2)+Math.abs(y1-y2)*Math.abs(y1-y2));
	}

	public static double pointTotoDegrees(double x,double y){
		return Math.toDegrees(Math.atan2(x,y));
	}
	
	public static boolean isAccountRight(String strAccount1, double account2) {
		if (!StringUtil.isNullOrEmpty(strAccount1)) {
			if(account2 > conversePoint(strAccount1)){
				return false;
			}
		}
		return true;
	}
	
	
	public static double conversePoint(String number){
		//如果有负号，先去掉负号
		number = StringUtil.removeSign(number);
		String pointNumber=number.substring(number.length()-2, number.length());
		String beforNumber=number.substring(0, number.length()-2);
		String targetNumber=beforNumber+"."+pointNumber;
		return Double.parseDouble(targetNumber);
	}
	
	public static long converse(String number){
		return Long.parseLong(number);
	}
}
