package com.bocop.xfjr.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * 默认限制只能输入中文
 * 
 * @author bubbly
 *
 */
public class LimitInputTextWatcher implements TextWatcher {
	/**
	 * et
	 */
	private EditText et = null;
	/**
	 * 筛选条件
	 */
	private String regex;
	/**
	 * 默认的筛选条件(正则:只能输入中文)
	 */
	private String DEFAULT_REGEX = "[^\u4E00-\u9FA5]";
	public static String a_zA_Z_0_9_CN_REGEX = "[^a-zA-Z0-9\u4E00-\u9FA5]"; // 只能输入a-zA-Z0-9和中文
	public static String a_zA_Z_CN_REGEX = "[^a-zA-Z\u4E00-\u9FA5]"; // 只能输入a-zA-Z和中文
	public static String a_z_CN_REGEX = "[^a-z\u4E00-\u9FA5]"; // 只能输入a-和中文
	public static String A_Z_CN_REGEX = "[^A-Z\u4E00-\u9FA5]"; // 只能输入A-Z和中文
	public static String _0_9_CN_REGEX = "[^0-9\u4E00-\u9FA5]"; // 只能输入0-9和中文

	/**
	 * 构造方法
	 *
	 * @param et
	 */
	public LimitInputTextWatcher(EditText et) {
		this.et = et;
		this.regex = DEFAULT_REGEX;
	}

	/**
	 * 构造方法
	 *
	 * @param et
	 *            et
	 * @param regex
	 *            筛选条件
	 */
	public LimitInputTextWatcher(EditText et, String regex) {
		this.et = et;
		this.regex = regex;
	}

	@Override
	public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

	}

	@Override
	public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

	}

	@Override
	public void afterTextChanged(Editable editable) {
		String str = editable.toString();
		String inputStr = clearLimitStr(regex, str);
		et.removeTextChangedListener(this);
		// et.setText方法可能会引起键盘变化,所以用editable.replace来显示内容
		editable.replace(0, editable.length(), inputStr.trim());
		et.addTextChangedListener(this);

	}

	/**
	 * 清除不符合条件的内容
	 *
	 * @param regex
	 * @return
	 */
	private String clearLimitStr(String regex, String str) {
		return str.replaceAll(regex, "");
	}
}
