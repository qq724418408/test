package com.bocop.jxplatform.util;

import com.bocop.jxplatform.R;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


/**
 * 自定义dialog
 * @author my
 */
public class BocopDialog extends Dialog {
	private Context mContext;
	private View mContentView;
	private TextView ui_bocop_dialog_tv_title,ui_bocop_dialog_tv_text;
	private Button ui_bocop_dialog_btn_cancel,ui_bocop_dialog_btn_confirm;
	private OnClickListener mNegativeListener;
	private OnClickListener mPositiveListener;
	private boolean lock;
	//public static final int DIALOG_STYLE_ONLYONE_BTN = 1;
	//public static final int DIALOG_STYLE_DOUBLE_BTN = 2;
	
	public BocopDialog(Context context) {
		super(context,R.style.Dialog_bocop);
		mContext = context;
		mContentView = View.inflate(context, R.layout.ui_bocop_dialog, null);
		ui_bocop_dialog_tv_title = (TextView) mContentView.findViewById(R.id.ui_bocop_dialog_tv_title);
		ui_bocop_dialog_tv_text = (TextView) mContentView.findViewById(R.id.ui_bocop_dialog_tv_text);
		ui_bocop_dialog_btn_cancel = (Button) mContentView.findViewById(R.id.ui_bocop_dialog_btn_cancel);
		ui_bocop_dialog_btn_confirm = (Button) mContentView.findViewById(R.id.ui_bocop_dialog_btn_confirm);
		mContentView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!lock) {
					dismiss();
				}
			}
		});
		ui_bocop_dialog_btn_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mNegativeListener != null) {
					mNegativeListener.onClick(BocopDialog.this, R.id.ui_bocop_dialog_btn_cancel);
				}
				dismiss();
			}
		});
		ui_bocop_dialog_btn_confirm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mPositiveListener != null) {
					mPositiveListener.onClick(BocopDialog.this, R.id.ui_bocop_dialog_btn_confirm);
				}
				dismiss();
			}
		});
		setContentView(mContentView);
		getWindow().setWindowAnimations(R.anim.alpha_in);
	}
	/**
	 * 构造出指定标题和提示内容的dialog
	 * @param context
	 * @param title 标题文字
	 * @param message 提示文字
	 */
	public BocopDialog(Context context,String title,String message) {
		this(context);
		setTitle(title);
		setMessage(message);
	}
	/**
	 * 使用资源id，构造出指定标题和提示内容的dialog
	 * @param context
	 * @param titleId 标题文字id
	 * @param messageId 提示文字id
	 */
	public BocopDialog(Context context,int titleId,int messageId) {
		this(context,context.getString(titleId),context.getString(messageId));
	}
	
	/**
	 * 设置dialog标题文字
	 */
	@Override
	public void setTitle(CharSequence title) {
		ui_bocop_dialog_tv_title.setText(title);
	}
	/**
	 * 设置dialog标题文字
	 */
	@Override
	public void setTitle(int titleId) {
		setTitle(mContext.getString(titleId));
	}
	/**
	 * 设置dialog的文字内容
	 * @param message
	 */
	public void setMessage(String message) {
		ui_bocop_dialog_tv_text.setText(message);
	}
	/**
	 * 设置dialog的文字内容
	 * @param message
	 */
	public void setMessage(int resId) {
		setMessage(mContext.getString(resId));
	}
	/**
	 * 设置取消按钮的监听与文字
	 */
	public void setNegativeButton(DialogInterface.OnClickListener onNegativeListener,String text) {		
		ui_bocop_dialog_btn_cancel.setText(text);
		this.mNegativeListener = onNegativeListener;
	}
	/**
	 * 设置取消按钮的监听与文字的资源id
	 */
	public void setNegativeButton(DialogInterface.OnClickListener onNegativeListener,int textId) {
		setNegativeButton(onNegativeListener,mContext.getString(textId));
	}
	/**
	 * 设置取消监听
	 */
	public void setNegativeListener(DialogInterface.OnClickListener onNegativeListener) {
		setNegativeButton(onNegativeListener, "取消");		
	}
	/**
	 * 设置确定监听与文字
	 */
	public void setPositiveButton(DialogInterface.OnClickListener onPositiveListener,String text) {
		ui_bocop_dialog_btn_confirm.setText(text);
		this.mPositiveListener = onPositiveListener;
	}
	/**
	 * 设置确定监听与文字的资源id
	 */
	public void setPositiveButton(DialogInterface.OnClickListener onPositiveListener,int textId) {
		setPositiveButton(onPositiveListener, mContext.getString(textId));
	}
	/**  
	 * 设置确定监听
	 */
	public void setPositiveListener(DialogInterface.OnClickListener onPositiveListener) {
		setPositiveButton(onPositiveListener,"确定");
	}
	/**
	 * 隐藏取消按钮用于显示一个提示框
	 */
	public void hideNegativeButton() {
		ui_bocop_dialog_btn_cancel.setVisibility(View.GONE);
	}
	/**
	 * 锁定dialog，阻止返回事件，以及触摸屏幕dialog自动消失功能，锁定后只有按下"确定"或者"取消"时dialog才会消失
	 */
	public void lock() {
		this.lock = true;
	}
	/**
	 * 解除锁定
	 */
	public void unlock() {
		this.lock = false;
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (lock && keyCode == event.KEYCODE_BACK) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
