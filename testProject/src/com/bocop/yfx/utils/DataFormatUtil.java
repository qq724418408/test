package com.bocop.yfx.utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * 
 * 数据格式化类
 * 
 * @author lh
 * 
 */
public class DataFormatUtil {

	/**
	 * 银行卡号格式化（XXXX XXXX XXXX XXXX XXX）
	 * 
	 * @param bankCardString
	 * @return
	 */
	public static String bankCardFormat(String bankCardString) {
		return bankCardString.replace(" ", "").replaceAll("\\d{4}", "$0 ");
	}

	public static boolean isBankCardFormated(String bankCardString) {
		return Pattern.compile("(\\d{4} {1})*\\d{0,4}").matcher(bankCardString).matches();
	}

	/**
	 * 
	 * 金额格式化（10,000,000.00）
	 * 
	 * @param moneyString
	 * @return
	 */
	public static String moneyStringFormat(String moneyString) {
		String strNoComma = moneyString.replaceAll(",", "");
		double newDouble = Double.parseDouble(strNoComma);
		DecimalFormat decimalFormat = new DecimalFormat(",##0.00");
		return decimalFormat.format(newDouble);
	}

	/**
	 * 
	 * 日期格式化（1969-12-31 <blockquote>1969年12月31日）</blockquote>
	 * 
	 * @param date
	 *            new Date()
	 * @param isChinese
	 *            是：1969年12月31日 否：1969-12-31
	 * @return
	 */
	public static String getDaytimeOnly(Date date, boolean isChinese) {
		if (!isChinese) {
			return new SimpleDateFormat("yyyy-MM-dd").format(date);
		} else {
			return new SimpleDateFormat("yyyy年MM月dd日").format(date);
		}
	}

	/**
	 * 
	 * 日期格式化（1969-12-31 16:00:00 <blockquote>1969年12月31日 16时00分00秒）</blockquote>
	 * 
	 * @param date
	 *            new Date()
	 * @param isChinese
	 *            是：1969年12月31日 16时00分00秒 否：1969-12-31 16:00:00
	 * @return
	 */
	public static String getDateWithTime(Date date, boolean isChinese) {
		if (!isChinese) {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
		} else {
			return new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒").format(date);
		}
	}
}
