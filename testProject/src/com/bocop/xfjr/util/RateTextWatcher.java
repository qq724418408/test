package com.bocop.xfjr.util;

import java.text.DecimalFormat;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * 费率输入限制TextWatcher
 * 输入范围0.01 - 100（不包括100.00）（两位小数）
 * 
 * @author wujunliu
 *
 */
public class RateTextWatcher implements TextWatcher {

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
	public RateTextWatcher(EditText editText, int iLength, int dLength) {
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
//		LogUtils.e("---origin---" + origin);
//		LogUtils.e("---finalStr---" + finalStr);
		if (!TextUtils.isEmpty(finalStr)) {
			if (finalStr.equals(origin) || TextUtils.isEmpty(finalStr))
				return;
			if (finalStr.startsWith(".")) {
				et.setText(origin);
				return;
			}
			finalStr = finalStr.replaceAll(",", "");
			String regex = "(^[1-9]\\d{0,%d}(\\.\\d{0,%d})?$)|(^0(\\.\\d{0,%d})?$)";
			regex = String.format(regex, iLength - 1, dLenth, dLenth);
			if (finalStr.matches(regex)) {
//				LogUtils.e("matches---finalStr---" + finalStr);
				if (finalStr.endsWith(".")) {
					return;
				}
				StringBuilder stringBuilder = new StringBuilder(
						finalStr.startsWith("0") ? "0" : "#");
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
//				LogUtils.e("---format---" + format);
			} else {
//				LogUtils.e("unmatches---origin---" + origin);
				if (Float.parseFloat(finalStr) == 100f) {
					et.setText("100");
				} else {
					et.setText(origin);
				}
			}
			et.setSelection(et.getText().length());
		}
	}

}
