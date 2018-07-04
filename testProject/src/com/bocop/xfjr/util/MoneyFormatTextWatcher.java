package com.bocop.xfjr.util;

import java.text.DecimalFormat;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * 金额千分位显示
 * 
 * @author bubbly
 *
 */
public class MoneyFormatTextWatcher implements TextWatcher {

	private int dLenth;
	private int iLength;
	private EditText et;

	/**
	 * 
	 * @param editText
	 * @param iLength
	 *            整数位数
	 * @param dLength
	 *            小数位数
	 */
	public MoneyFormatTextWatcher(EditText editText, int iLength, int dLength) {
		this.et = editText;
		this.iLength = iLength;
		this.dLenth = dLength;
	}

	String origin = "";

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		origin = s.toString();
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}

	@Override
	public void afterTextChanged(Editable s) {
//		LogUtils.e("---s---" + s.toString());
		String finalStr = s.toString();
		if (finalStr.equals("￥"))
			et.setText("");
		if (finalStr.equals(origin) || TextUtils.isEmpty(finalStr))
			return;
		finalStr = finalStr.replaceAll(",", "").replace("￥", "");
		String regex = "^[1-9]\\d{0,%d}(\\.\\d{0,%d})?$";
		regex = String.format(regex, iLength - 1, dLenth);
		if (finalStr.matches(regex)) {
			if (finalStr.endsWith("."))
				return;
			StringBuilder stringBuilder = new StringBuilder("￥,###");
			if (finalStr.contains(".")) {
				int len = finalStr.substring(finalStr.indexOf(".") + 1).length();
				String fix = "^.+\\.0+$";
				if (finalStr.matches(regex)) {
					fix = "0";
				} else {
					fix = "#";
				}
				stringBuilder.append(".");
				for (int i = 0; i < len; i++) {
					stringBuilder.append(fix);
				}
			}
			String format = new DecimalFormat(stringBuilder.toString())
					.format(Double.parseDouble(finalStr));
			et.setText(format);
//			LogUtils.e("---format---" + format);
		} else {
//			LogUtils.e("---origin---" + origin);
			et.setText(origin);
		}
		et.setSelection(et.getText().length());
	}

}
