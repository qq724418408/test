package com.bocop.xfjr.helper;

import com.bocop.jxplatform.R;
import com.bocop.xfjr.XfjrMain;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.NumberKeyListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * 银行卡验证辅助类
 * 
 * TIAN FENG
 */
public class BankCardHelper {

	// 延时获取
	private Handler mHndler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			mCount--;
			if (mCount > 0) {
				send();
			} else {
				mCount = 60;
				mButton.setText(mButton.getResources().getString(R.string.regain_check_num));
				mButton.setBackgroundResource(R.drawable.xfjr_shape_circle_corner_btn_red);
				mButton.setEnabled(true);
			}
		};
	};

	private Button mButton;
	private int mCount =  XfjrMain.reqSmsCodeTime;

	public BankCardHelper() {

	}

	/**
	 * 倒计时按钮
	 */
	public void cutDown(Button button) {
		mButton = button;
		send();
	}

	/**
	 * 自动补全空格 
	 * @param format 补全的字符 空格 ->' ' 横线 ->'-'
	 */
	public void bankCardNumAddSpace(final EditText mEditText,final char format) {
		mEditText.setKeyListener(new NumberKeyListener() {
			// 0无键盘 1英文键盘 2模拟键盘 3数字键盘
			@Override
			public int getInputType() {
				// TODO Auto-generated method stub
				return 3;
			}

			// 返回允许输入的字符
			@Override
			protected char[] getAcceptedChars() {
				// TODO Auto-generated method stub
				char[] c = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', format };
				return c;
			}
		});
		mEditText.addTextChangedListener(new TextWatcher() {
			/*int beforeTextLength = 0;
			int onTextLength = 0;
			boolean isChanged = false;

			int location = 0;// 记录光标的位置
			private char[] tempChar;
			private StringBuffer buffer = new StringBuffer();
			int konggeNumberB = 0;

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				beforeTextLength = s.length();
				if (buffer.length() > 0) {
					buffer.delete(0, buffer.length());
				}
				konggeNumberB = 0;
				for (int i = 0; i < s.length(); i++) {
					if (s.charAt(i) == format) {
						konggeNumberB++;
					}
				}
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				onTextLength = s.length();
				buffer.append(s.toString());
				if (onTextLength == beforeTextLength || onTextLength <= 3 || isChanged) {
					isChanged = false;
					return;
				}
				isChanged = true;
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (isChanged) {
					location = mEditText.getSelectionEnd();
					int index = 0;
					while (index < buffer.length()) {
						if (buffer.charAt(index) == format) {
							buffer.deleteCharAt(index);
						} else {
							index++;
						}
					}

					index = 0;
					int konggeNumberC = 0;
					while (index < buffer.length()) {
						// 银行卡号的话需要改这里
						if ((index % 5 == 4  || index == 9 )) {
							buffer.insert(index, format);
							konggeNumberC++;
						}
						index++;
					}

					if (konggeNumberC > konggeNumberB) {
						location += (konggeNumberC - konggeNumberB);
					}

					tempChar = new char[buffer.length()];
					buffer.getChars(0, buffer.length(), tempChar, 0);
					String str = buffer.toString();
					if (location > str.length()) {
						location = str.length();
					} else if (location < 0) {
						location = 0;
					}

					mEditText.setText(str);
					Editable etable = mEditText.getText();
					Selection.setSelection(etable, location);
					isChanged = false;
				}
			}*/
			 	private boolean isRun = false;
			    private String d = "";
			 	@Override
	            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	            }

	            @Override
	            public void onTextChanged(CharSequence s, int start, int before, int count) {
	                    if(isRun){
	                        isRun=false;
	                        return;
	                    }
	                isRun = true;
	                d = "";
	                String newStr = s.toString();
	                newStr = newStr.replace(" ", "");
	                int index = 0;
	                while ((index + 4) < newStr.length()){
	                    d += (newStr.substring(index, index + 4) + " ");
	                    index += 4;
	                }
	                d += (newStr.substring(index, newStr.length()));
	                int i = mEditText.getSelectionStart();
	                mEditText.setText(d);
	                try {


	                    if (i % 5 == 0 && before == 0) {
	                        if (i + 1 <= d.length()) {
	                        	mEditText.setSelection(i + 1);
	                        } else {
	                        	mEditText.setSelection(d.length());
	                        }
	                    } else if (before == 1 && i < d.length()) {
	                    	mEditText.setSelection(i);
	                    } else if (before == 0 && i < d.length()) {
	                    	mEditText.setSelection(i);
	                    } else
	                        mEditText.setSelection(d.length());


	                }catch (Exception e){

	                }
	            }

	            @Override
	            public void afterTextChanged(Editable s) {

	            }
		});
	}
	
	/**
	 * 开始延时发送
	 */
	private void send() {
		mButton.setText(mCount+"s");
		mButton.setEnabled(false);
		mButton.setBackgroundResource(R.drawable.xfjr_shape_circle_corner_btn_gray);
		mHndler.sendEmptyMessageDelayed(0, 1000);
	}
}
