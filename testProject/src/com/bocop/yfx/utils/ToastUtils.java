package com.bocop.yfx.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bocop.jxplatform.R;


/**
 * Toast工具类
 * 
 * @author hkj
 * 
 */
public class ToastUtils {

	private static Toast toast;// 原生Toast
	private static Toast iconToast;// 自定义带图标的Toast

	public final static int ICON_NONE = 0;
	public final static int ICON_INFO = 1;
	public final static int ICON_SUCCESS = 2;
	public final static int ICON_FAILED = 3;
	public final static int ICON_ERROR = 4;

	public static void show(Context context, String text, int duration) {
		if (toast == null) {
			toast = Toast.makeText(context, text, duration);
		}
		toast.setText(text);
		toast.setDuration(duration);
//		toast.setGravity(Gravity.BOTTOM, 0, 0);
		toast.show();
//		toast = null;
	}

	public static void showText(Context context, String text, int duration) {
		show(context, text, ICON_NONE, Gravity.CENTER, duration);
	}
	
	public static void showInfo(Context context, String text, int duration) {
		show(context, text, ICON_INFO, Gravity.CENTER, duration);
	}
	
	public static void showSuccess(Context context, String text, int duration) {
		show(context, text, ICON_SUCCESS, Gravity.CENTER, duration);
	}
	
	public static void showFailed(Context context, String text, int duration) {
		show(context, text, ICON_FAILED, Gravity.CENTER, duration);
	}
	
	public static void showError(Context context, String text, int duration) {
		show(context, text, ICON_ERROR, Gravity.CENTER, duration);
	}

	/**
	 * 显示自定义吐司
	 * 
	 * @param context 上下文
	 * @param text 文本
	 * @param iconTypeOrId 图标
	 * @param gravity 位置
	 * @param duration 时长
	 */
	public static void show(Context context, String text, int iconTypeOrId, int gravity,
			int duration) {
		View view = LayoutInflater.from(context).inflate(R.layout.common_layout_toast, null);
		TextView tv = (TextView) view.findViewById(R.id.tv_toast);
		ImageView iv = (ImageView) view.findViewById(R.id.iv_toast);
		switch (iconTypeOrId) {
		case ICON_NONE:
			iv.setVisibility(View.GONE);
			break;
		case ICON_INFO:
			iv.setBackgroundResource(R.drawable.icon_toast_info);
			break;
		case ICON_SUCCESS:
			iv.setBackgroundResource(R.drawable.icon_toast_success);
			break;
		case ICON_FAILED:
			iv.setBackgroundResource(R.drawable.icon_toast_failed);
			break;
		case ICON_ERROR:
			iv.setBackgroundResource(R.drawable.icon_toast_error);
			break;
		default:
			iv.setBackgroundResource(iconTypeOrId);
			break;
		}
		tv.setText(text);

		if (iconToast == null) {
			iconToast = new Toast(context);
		}
		iconToast.setView(view);
		iconToast.setDuration(duration);
		iconToast.setGravity(gravity, 0, 0);
		iconToast.show();
//		iconToast = null;
	}

}
