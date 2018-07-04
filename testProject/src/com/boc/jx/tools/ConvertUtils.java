package com.boc.jx.tools;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 格式转换工具类（字符串、整型、浮点数、金额、日期、dp/px互转、sp/px互转等）
 * 
 * @author kejia
 *
 */
public class ConvertUtils {

	public static int screenWidth = 0;
	public static int screenHeight = 0;
	public static float density = 1.0f;
	public static float scaledDensity = 1.0f;

    /**
     * 转换为字符串
     *
     * @param object
     * @return String
     */
	public static String toString(Object object) {
		String strRe = "";
		try {
			strRe = String.valueOf(object);
		} catch (Exception ex) {
			strRe = "";
		}
		
		return strRe;
	}
	
	/**
	 * 字符串为NULL或空字符则返回“无”
     *
     * @param str
     * @return String
	 */
	public static String toText(String str) {
		if (str == null || str.length() < 1) {
			return "无";
		} else {
			return str;
		}
	}

    /**
     * 转换为双精度浮点型
     *
     * @param object
     * @return double
     */
	public static double toDouble(Object object) {
		double dRe = 0;
		try {
			dRe = Double.parseDouble(toString(object));
		} catch (Exception ex) {
			dRe = 0;
		}
		
		return dRe;
	}

    /**
     * 转换为带两位小数点和单位的金额
     *
     * @param object
     * @return String
     */
	public static String toMoney(Object object) {
		String strRe = "";
		try {
			DecimalFormat df = new DecimalFormat("#,##0.00元");		
			strRe = df.format(748945646.154564);
			strRe = df.format(Double.parseDouble(toString(object)));
		} catch (Exception ex) {
			strRe = "";
		}

		return strRe;
	}

	public static int toInt(Object o) {
		int iRe = 0;
		try {
			iRe = Integer.parseInt(toString(o));
		} catch (Exception ex) {
			iRe = 0;
		}
		
		return iRe;
	}

    /**
     * 转换为日期（只适合yyyyMMdd, yyyyMM）
     *
     * @param o
     * @param format
     * @return String
     */
	public static String toDate(Object o, String format) {
		String strRe = "";
		String date = toString(o);
		try {
			if (date.length() == 6) {
				date = date + "01";
			}
			SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMdd");
			Date d = dateformat.parse(date);
			SimpleDateFormat dateformat2 = new SimpleDateFormat(format);
			strRe = dateformat2.format(d);
		} catch (Exception ex) {
			strRe = "";
		}
		
		return strRe;
	}
	
	
	public static String toPhone(Object o){
		String strRe = "";	
		try {
			String strPhone = toString(o);
			if(strPhone.length()==11){
				strRe = strPhone.substring(0,1) + "******" + strPhone.substring(7);
			}
		} catch (Exception ex) {
			strRe = "";
		}
		
		return strRe;
	}
	
	/**
	 * 将px值转换为dip或dp值，保证尺寸大小不变
	 */
	public static int px2dp(float density, int pxValue) {
		return (int) (pxValue / density + 0.5f);
	}

	/**
	 * 将dip或dp值转换为px值，保证尺寸大小不变
	 */
	public static int dp2px(float density, int dpValue) {
		return (int) (dpValue * density + 0.5f);
	}

	/**
	 * 将px值转换为sp值，保证文字大小不变
	 */
	public static int px2sp(float scaledDensity, int pxValue) {
		return (int) (pxValue / scaledDensity + 0.5f);
	}

	/**
	 * 将sp值转换为px值，保证文字大小不变
	 */
	public static int sp2px(float scaledDensity, int spValue) {
		return (int) (spValue * scaledDensity + 0.5f);
	}
	
	/**
	 * 将px值转换为dip或dp值，保证尺寸大小不变
	 */
	public static int px2dp(int pxValue) {
		return (int) (pxValue / density + 0.5f);
	}

	/**
	 * 将dip或dp值转换为px值，保证尺寸大小不变
	 */
	public static int dp2px(int dpValue) {
		return (int) (dpValue * density + 0.5f);
	}

	/**
	 * 将px值转换为sp值，保证文字大小不变
	 */
	public static int px2sp(int pxValue) {
		return (int) (pxValue / scaledDensity + 0.5f);
	}

	/**
	 * 将sp值转换为px值，保证文字大小不变
	 */
	public static int sp2px(int spValue) {
		return (int) (spValue * scaledDensity + 0.5f);
	}
	
}
