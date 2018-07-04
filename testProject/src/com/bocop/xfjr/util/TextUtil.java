package com.bocop.xfjr.util;

import java.text.DecimalFormat;

import com.bocop.xfjr.bean.TypeBean;
import com.bocop.xfjr.view.XFJRMoneyClearEditText;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.widget.EditText;

public class TextUtil {

	/**
	 * 设置同一个字符串前面n个字和后面字体不同大小
	 * 
	 * @param str 字符串
	 * @param bigStart 开始位置
	 * @param end 前面字体大小结束位置也是后面字体大小开始位置
	 * @param frontSize 前面字体大小像素
	 * @param behindSize 后面字体大小像素
	 * @return
	 */
	public static SpannableString setDiffSizeText(String str, int end, int frontSize, int behindSize) {
		SpannableString msp = new SpannableString(str);
		msp.setSpan(new AbsoluteSizeSpan(frontSize), 0, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		msp.setSpan(new AbsoluteSizeSpan(behindSize), end, msp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return msp;
	}
	
	/**
	 * 设置括号里面的字体大小，包括括号本身
	 * 
	 * @param str 字符串
	 * @param frontSize 前面字体大小
	 * @param behindSize 后面字体大小
	 * @return
	 */
	public static SpannableString setBracketsTextSmall(String str, int frontSize, int behindSize) {
		int end = 0; // 前面字体大小结束位置也是后面字体大小开始位置
		try {
			if (str.contains("(")){ // 英文括号
				end = str.indexOf("(");
				return setDiffSizeText(str, end, frontSize, behindSize);
			}
			if (str.contains("（")){ // 中文括号
				end = str.indexOf("（");
				return setDiffSizeText(str, end, frontSize, behindSize);
			}
		} catch (Exception e) {
			return setDiffSizeText(str, 0, frontSize, behindSize);
		}
		return setDiffSizeText(str, end, frontSize, frontSize); // 如果没有括号，字体大小由前面决定
	}
	
	/**
	 * 根据输入的数字显示金额格式
	 * 
	 * @return
	 */
	public static String moneyFormat(String money) {
		if (TextUtils.isEmpty(money)) {
			return "0.00";
		} else if ("0".equals(money) || "0.00".equals(money)) {
			return "0.00";
		} else {
			money = money.replaceAll(",", "").replaceAll("￥", "");
			double d = Double.parseDouble(money);
			if (d < 1) {
				return String.valueOf(d);
			}
			DecimalFormat df = new DecimalFormat("#,###.00");
			String m = df.format(d);
			return m;
		}
	}
	
	/**
	 * 根据输入的数字显示金额格式，前面加￥
	 * 
	 * @return
	 */
	public static String money$Format(String money) {
		if (TextUtils.isEmpty(money)) {
			return "￥0.00";
		} else if ("0".equals(money) || "0.00".equals(money)) {
			return "￥0.00";
		} else {
			money = money.replaceAll(",", "").replaceAll("￥", "");
			double d = Double.parseDouble(money);
			if (d < 1) {
				return "￥" + String.valueOf(d);
			}
			DecimalFormat df = new DecimalFormat("#,###.00");
			String m = df.format(d);
			return "￥" + m;
		}
	}
	
	/**
	 * 数量99+显示
	 * @param number
	 * @return
	 */
	public static String more100(String number) {
		int n = Integer.parseInt(number);
		if (n > 99) {
			return "99+";
		}
		return number;
	}

	/**
	 * 拼接我的业务tab标题
	 * @param b
	 * @return
	 */
	public static String MyBusinessTabName(TypeBean b) {
		return b.getTypeName() + "(" + b.getNumber() + ")";
	}
	
	/**
	 * 获取编辑框提示语
	 * 
	 * @param et
	 * @return
	 */
	public static String getHintText(EditText et){
		return et.getHint().toString();
	}
	
	/**
	 * 金额补.00
	 */
	public static void suffix00(XFJRMoneyClearEditText et) {
		String result = et.getTextString();
		if (!TextUtils.isEmpty(result)) {
			result = new DecimalFormat("0.00").format(Double.parseDouble(result));
			et.setText(result);
		}
	}
	
	public static void main(String[] args) {
		
	}

}
