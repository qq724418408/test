package com.bocop.yfx.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * 常用输入信息的校验类
 * 
 * @author lh
 * 
 */
public class CheckoutUtil {

	/**
	 * 判空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String... str) {

		for (String string : str) {
			if (TextUtils.isEmpty(string)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * 校验手机号
	 * 
	 * @param context
	 * @param mobile
	 * @return
	 */
	public static boolean isMobileNo(Context context, String mobile) {

		if (!isEmpty(mobile)) {
			Pattern p = Pattern
					.compile("^((13[0-9])|(15[^4,\\D])|(14[5,7])|(170)|(18[0-9]))\\d{8}$");
			Matcher m = p.matcher(mobile);
			if (m.matches()) {
				return true;
			} else {
				ToastUtils.show(context, "请输入正确的手机号", Toast.LENGTH_SHORT);
				return false;
			}
		} else {
			ToastUtils.show(context, "输入不能为空", Toast.LENGTH_SHORT);
			return false;
		}
	}

	public static boolean isMobileNo(String mobile) {

		if (!isEmpty(mobile)) {
			Pattern p = Pattern
					.compile("^((13[0-9])|(15[^4,\\D])|(14[5,7])|(170)|(18[0-9]))\\d{8}$");
			Matcher m = p.matcher(mobile);
			if (m.matches()) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * 校验字符串长度的合法性
	 * 
	 * @param context
	 * @param start
	 * @param end
	 * @param str
	 * @return
	 */
	public static boolean between(Context context, int start, int end,
			String str) {

		if (!isEmpty(str)) {
			if (str.length() >= start && str.length() <= end) {
				return true;
			} else {
				ToastUtils.showError(context,
						"请输入" + start + "到" + end + "位字符", Toast.LENGTH_SHORT);
				return false;
			}
		} else {
			ToastUtils.showError(context, "输入不能为空", Toast.LENGTH_SHORT);
			return false;
		}

	}

	/**
	 * 校验字符串是否包含字母和数字
	 * 
	 * @param context
	 * @param str
	 * @return
	 */
	public static boolean isNumAndLetter(Context context, String str) {

		if (!isEmpty(str)) {
			Pattern p = Pattern
					.compile("^([A-Za-z]+[0-9]+)|([0-9]+[A-Za-z]+)$");
			Matcher m = p.matcher(str);
			if (m.matches()) {
				return true;
			} else {
				ToastUtils
						.showError(context, "密码必须含有字母和数字", Toast.LENGTH_SHORT);
				return false;
			}
		} else {
			ToastUtils.showError(context, "输入不能为空", Toast.LENGTH_SHORT);
			return false;
		}

	}

	/**
	 * 校验名字不含特殊符号
	 * 
	 * @param name
	 * @return
	 */
	public static boolean isNameNoSymbol(Context context, String name) {

		if (!isEmpty(name)) {
			Pattern numPattern = Pattern.compile(".*\\d+.*");
			Pattern charPattern = Pattern
					.compile("!|！|@|◎|#|＃|(\\$)|￥|%|％|(\\^)|……|(\\&)|※|(\\*)|×|(\\()|（|(\\))|）|_|——|(\\+)|＋|(\\|)|§|\\|(\\{)|(\\})|(\\[)|(\\])|(\")|(\')|(\\;)|(\\:)|(\\,)|(\\.)|(\\<)|(\\>)|(\\/)|(\\?)|？|、|，|。|‘|“|”|；|：|-");
			boolean flag = numPattern.matcher(name).matches()
					|| charPattern.matcher(name).find();
			if (flag) {
				ToastUtils
						.showError(context, "不能包含数字和特殊符号", Toast.LENGTH_SHORT);
				return false;
			} else {
				return true;
			}
		} else {
			ToastUtils.showError(context, "输入不能为空", Toast.LENGTH_SHORT);
			return false;
		}
	}

	/**
	 * 校验邮箱
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isEmail(Context context, String email) {

		if (!isEmpty(email)) {
			Pattern emailPattern = Pattern
					.compile("^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");
			boolean flag = emailPattern.matcher(email).matches();
			if (flag) {
				return true;
			} else {
				ToastUtils.showError(context, "请输入正确的邮箱", Toast.LENGTH_SHORT);
				return false;
			}
		} else {
			ToastUtils.showError(context, "输入不能为空", Toast.LENGTH_SHORT);
			return false;
		}
	}

	/**
	 * 校验中文
	 * 
	 * @param chi
	 * @return
	 */
	public static boolean isChinese(Context context, String chi) {
		if (!isEmpty(chi)) {
			Pattern chiPattern = Pattern.compile("^[u4E00-u9FA5]+$");
			boolean flag = chiPattern.matcher(chi).matches();
			if (flag) {
				return true;
			} else {
				return false;
			}
		} else {
			ToastUtils.showError(context, "输入不能为空", Toast.LENGTH_SHORT);
			return false;
		}
	}

	/**
	 * 
	 * 校验身份证号
	 * 
	 * @param IDString
	 * @param isNew
	 *            是否是新版身份证
	 * @see IdentityCheckHelper
	 * @return
	 */
	public static boolean isIdentity(Context context, String IDString,
			boolean isNew) {
		if (!isEmpty(IDString)) {
			boolean flag = IdentityCheckHelper.checkIDCard(IDString, isNew);
			if (flag) {
				return true;
			} else {
				ToastUtils.show(context, IdentityCheckHelper.getMessage(),
						Toast.LENGTH_SHORT);
				return false;
			}
		} else {
			ToastUtils.show(context, "输入不能为空", Toast.LENGTH_SHORT);
			return false;
		}
	}

	/**
	 * 校验银行卡号
	 * 
	 * @param cardID
	 * @return
	 */
	public static boolean isBankCard(Context context, String cardID) {
		String str = cardID.replaceAll(" ", "");
		if (!isEmpty(str)) {
			char bit = getBankCardCheckCode(str.substring(0, str.length() - 1));
			boolean flag = str.charAt(str.length() - 1) == bit;
			if (flag) {
				return true;
			} else {
				ToastUtils.showError(context, "请输入正确的银行卡号", Toast.LENGTH_SHORT);
				return false;
			}
		} else {
			ToastUtils.showError(context, "输入不能为空", Toast.LENGTH_SHORT);
			return false;
		}
	}

	/**
	 * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验码
	 * 
	 * @param nonCheckCodeCardId
	 * @return
	 */
	private static char getBankCardCheckCode(String nonCheckCodeCardId) {
		if (nonCheckCodeCardId == null
				|| nonCheckCodeCardId.trim().length() == 0
				|| !nonCheckCodeCardId.matches("\\d+")) {
			throw new IllegalArgumentException("Bank card code must be number!");
		}
		char[] chs = nonCheckCodeCardId.trim().toCharArray();
		int luhmSum = 0;
		for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
			int k = chs[i] - '0';
			if (j % 2 == 0) {
				k *= 2;
				k = k / 10 + k % 10;
			}
			luhmSum += k;
		}
		return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
	}

}
