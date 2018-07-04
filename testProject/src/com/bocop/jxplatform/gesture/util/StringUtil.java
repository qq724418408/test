package com.bocop.jxplatform.gesture.util;

import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.text.TextUtils;

/**
 * 字符串处理类
 * 
 */
@SuppressLint({ "DefaultLocale", "SimpleDateFormat" })
public class StringUtil {
	public final static Pattern emailer = Pattern
			.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
	public final static SimpleDateFormat dateFormater = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	public final static SimpleDateFormat dateFormater2 = new SimpleDateFormat(
			"yyyy-MM-dd");
	public static int BUFFER_SIZE = 512;

	/**
	 * 去掉负号，截取负号后面的String
	 * 
	 * @param str
	 * @return
	 */
	public static String removeSign(String str) {
		if (str.contains("-")) {
			return str.substring(str.indexOf("-"), str.length());
		} else {
			return str;
		}
	}

	public static boolean isNullOrEmpty(String str) {
		if ((str == null) || "".equals(str) || "null".equals(str)
				|| "NULL".equals(str) || TextUtils.isEmpty(str)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isNotEmpty(String s) {
		return s != null && !"".equals(s.trim());
	}

	/**
	 * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
	 * 
	 * @param input
	 * @return boolean
	 */
	public static boolean isEmpty(String input) {
		if (input == null || "".equals(input))
			return true;

		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
				return false;
			}
		}
		return true;
	}
}
