package com.boc.jx.tools;

import com.bocop.jxplatform.R;
import com.bocop.jxplatform.activity.MainActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListAdapter;
import android.widget.TextView;

/**
 * 对话框工具类
 *
 * @author kejia
 */
public class DialogUtil {
	// 进度对话框
	private static ProgressDialog dialog;
	// 小达人专用对话框

	/**
	 * 进度对话框
	 *
	 * @param context
	 * @param title
	 * @param message
	 * @param isCancelable
	 * @return
	 */
	public static ProgressDialog showWithProgress(Context context, String title, String message, boolean isCancelable) {
		return showProgress(context, title, message, isCancelable, null);
	}

	/**
	 * 可取消的加载框
	 *
	 * @param context
	 *            应用上下文
	 * @param title
	 *            标题
	 * @param message
	 *            内容
	 * @param cancelListener
	 *            监听取消操作
	 * @return
	 */
	public static ProgressDialog showCancelableProgress(Context context, String title, String message,
			DialogInterface.OnCancelListener cancelListener) {
		return showProgress(context, title, message, true, cancelListener);
	}

	/**
	 * 不可取消的加载框
	 *
	 * @param context
	 *            应用上下文
	 * @param title
	 *            标题
	 * @param message
	 *            内容
	 * @return
	 */
	public static ProgressDialog showNoCancelProgress(Context context, String title, String message) {
		return showProgress(context, title, message, false, null);
	}

	/**
	 * 弹出加载框
	 *
	 * @param context
	 *            应用上下文
	 * @param title
	 *            标题
	 * @param message
	 *            内容
	 * @param isCancelable
	 *            是否可被取消
	 * @param cancelListener
	 *            监听取消操作
	 * @return
	 */
	public static ProgressDialog showProgress(Context context, String title, String message, boolean isCancelable,
			DialogInterface.OnCancelListener cancelListener) {
		if (dialog == null) {
			dialog = new ProgressDialog(context);
		} else if (dialog.isShowing()) {
			dialog.dismiss();
		}
		dialog.getWindow().setBackgroundDrawable(new BitmapDrawable());
		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.setCancelable(isCancelable);
		dialog.setCanceledOnTouchOutside(false);
		if (isCancelable && cancelListener != null) {
			dialog.setOnCancelListener(cancelListener);
		}
		dialog.show();
		return dialog;
	}

	/**
	 * 关闭对话框
	 */
	public static void closeWithProgress() {
		if (dialog != null) {
			dialog.dismiss();
			dialog = null;
		}
	}

	/**
	 * 网络设置工具方法
	 *
	 * @param context
	 *            上下文对象
	 */
	public static void showToSetNetwork(final Context context) {
		AlertDialog.Builder builder = getBuilder(context);
		builder.setTitle("提示");
		builder.setMessage("当前网络不可用，是否设置网络？");
		builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent("android.settings.WIFI_SETTINGS");
				context.startActivity(intent);
				dialog.cancel();
			}
		});
		if (!((Activity) context).isFinishing()) {
			builder.setCancelable(true).show();
		}
	}

	/**
	 * 往外弹的警示框(带重试按钮)
	 *
	 * @param message
	 *            内容
	 * @return AlertDialog对象
	 */
	public static AlertDialog showWithRetryBtn(Context context, String message,
			DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener) {
		AlertDialog.Builder builder = getBuilder(context);
		builder.setMessage(message);
		builder.setPositiveButton("重试", positiveListener);
		builder.setNegativeButton("取消", negativeListener);

		return builder.show();
	}

	/**
	 * 往外弹的警示框(带重试按钮)
	 *
	 * @param id
	 *            String Id
	 * @return AlertDialog对象
	 */
	public static AlertDialog showWithRetryBtn(Context context, int id,
			DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener) {
		AlertDialog.Builder builder = getBuilder(context);
		builder.setMessage(id);
		builder.setPositiveButton("重试", positiveListener);
		builder.setNegativeButton("取消", negativeListener);

		return builder.show();
	}

	/**
	 * 往外弹的警示框(有操作)
	 *
	 * @param message
	 *            内容
	 * @return AlertDialog对象
	 */
	public static AlertDialog showWithListener(Context context, String message,
			DialogInterface.OnClickListener positiveListener) {
		AlertDialog.Builder builder = getBuilder(context);
		builder.setMessage(message);
		builder.setPositiveButton("确定", positiveListener);
		builder.setNegativeButton("取消", positiveListener);
		builder.setCancelable(false);

		return builder.show();
	}

	/**
	 * 往外弹的警示框(不做任何操作，仅提示作用)
	 *
	 * @param message
	 *            内容
	 * @return AlertDialog对象
	 */
	public static AlertDialog show(Context context, String message) {
		AlertDialog.Builder builder = getBuilder(context);
		builder.setTitle("提示");
		builder.setMessage(message);
		builder.setPositiveButton("确定", null);
		builder.setCancelable(false);

		return builder.show();
	}

	/**
	 * 往外弹的警示框(不做任何操作，仅提示作用，无标题)
	 *
	 * @param message
	 *            内容
	 * @return AlertDialog对象
	 */
	public static AlertDialog showWithNoTitile(Context context, String message) {
		AlertDialog.Builder builder = getBuilder(context);
		builder.setMessage(message);
		builder.setPositiveButton("确定", null);
		builder.setCancelable(false);
		return builder.show();
	}

	/**
	 * 自定义VIew无标题对话框
	 *
	 * @param message
	 *            内容
	 * @return AlertDialog对象
	 */
	public static AlertDialog showViewDialog(Context context, String message) {
		final AlertDialog.Builder builder = getBuilder(context);
		View view = View.inflate(context, R.layout.dialog_message, null);
		builder.setView(view);
		TextView tv_message = (TextView) view.findViewById(R.id.tv_message);
		Button bt_ok = (Button) view.findViewById(R.id.bt_ok);
		tv_message.setText(message);
		builder.setCancelable(true);
		final AlertDialog mDialog = builder.show();
		bt_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mDialog.dismiss();
			}
		});
		return mDialog;
	}
	
	/**
	 * 往外弹出自定义布局对话框
	 *
	 * @param context
	 * @param view
	 * @return
	 */
	public static AlertDialog showWithView(final Context context, View view) {
		AlertDialog.Builder builder = getBuilder(context);
		builder.setView(view);
		builder.setCancelable(true);
		return builder.show();
	}
	/**
	 * 往外弹出自定义布局对话框
	 *
	 * @param context
	 * @param view
	 * @return
	 */
	public static AlertDialog showWithView(final Context context, View view, String sureBtnName,
			String cancelBtnName, DialogInterface.OnClickListener positiveListener,
			DialogInterface.OnClickListener negativeListener) {
		AlertDialog.Builder builder = getBuilder(context);
		builder.setView(view);
		builder.setPositiveButton(sureBtnName, positiveListener);
		builder.setNegativeButton(cancelBtnName, negativeListener);
		builder.setCancelable(false);
		return builder.show();
	}

	/**
	 * 往外弹出单个确定按钮的对话框
	 *
	 * @param context
	 * @param message
	 * @return
	 */
	public static AlertDialog showWithOneBtn(Context context, String message,
			DialogInterface.OnClickListener positiveListener) {
		AlertDialog.Builder builder = getBuilder(context);
		builder.setMessage(message);
		builder.setPositiveButton("确定", positiveListener);
		builder.setCancelable(false);

		return builder.show();
	}

	public static AlertDialog showWithOneBtn(Context context, String title, String message,
			DialogInterface.OnClickListener positiveListener) {
		AlertDialog.Builder builder = getBuilder(context);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton("确定", positiveListener);
		builder.setCancelable(false);

		return builder.show();
	}

	/**
	 * 往外弹出两个按钮的对话框
	 *
	 * @param context
	 * @param message
	 * @return
	 */
	public static AlertDialog showWithTwoBtn(Context context, String message, String sureBtnName, String cancelBtnName,
			DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener) {
		AlertDialog.Builder builder = getBuilder(context);
		builder.setMessage(message);
		builder.setPositiveButton(sureBtnName, positiveListener);
		builder.setNegativeButton(cancelBtnName, negativeListener);

		return builder.show();
	}

	/**
	 * 往外弹出两个按钮的对话框
	 *
	 * @param context
	 * @param message
	 * @return
	 */
	public static AlertDialog showWithTwoBtn(Context context, String title, String message, String sureBtnName,
			String cancelBtnName, DialogInterface.OnClickListener positiveListener,
			DialogInterface.OnClickListener negativeListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(message);
		builder.setPositiveButton(sureBtnName, positiveListener);
		builder.setNegativeButton(cancelBtnName, negativeListener);
		builder.setTitle(title);
		return builder.show();
	}

	/**
	 * 往外弹出两个按钮的带标题对话框
	 *
	 * @param context
	 * @param message
	 * @return
	 */
	public static AlertDialog showWithTwoBtnAndTitle(Context context, String title, String message, String sureBtnName,
			String cancelBtnName, DialogInterface.OnClickListener positiveListener,
			DialogInterface.OnClickListener negativeListener) {
		AlertDialog.Builder builder = getBuilder(context);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton(sureBtnName, positiveListener);
		builder.setNegativeButton(cancelBtnName, negativeListener);

		return builder.show();
	}
	 /**
	  * 往外弹出确定后回主页对话框
	  *
	  * @param context
	  * @param message
	  * @return
	  */
	 public static AlertDialog showWithToMain(final Context context, String message) {
	  AlertDialog.Builder builder = getBuilder(context);
	  builder.setMessage(message);
	  builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
	   
	   @Override
	   public void onClick(DialogInterface dialog, int which) {
	    Intent intent = new Intent(context, MainActivity.class);
	    context.startActivity(intent);
	   }
	  });
	  builder.setCancelable(false);
	  return builder.show();
	 }
	/**
	 * 往外弹出多个选择项的对话框
	 *
	 * @param context
	 * @param title
	 * @param items
	 * @param onClickListener
	 * @return
	 */
	public static AlertDialog showToSelect(Context context, String title, String[] items,
			DialogInterface.OnClickListener onClickListener) {
		AlertDialog.Builder builder = getBuilder(context);
		builder.setTitle(title);
		builder.setItems(items, onClickListener);

		return builder.show();
	}

	/**
	 * 单选列表对话框
	 *
	 * @param context
	 * @param title
	 * @param listAdapter
	 * @param selectedItem
	 * @param onClickListener
	 * @return
	 */
	public static AlertDialog getListDialog(Context context, String title, ListAdapter listAdapter, int selectedItem,
			Dialog.OnClickListener onClickListener) {
		AlertDialog.Builder builder = getBuilder(context);
		builder.setTitle(title);
		builder.setSingleChoiceItems(listAdapter, selectedItem, onClickListener);
		AlertDialog alertDialog = builder.create();
		return alertDialog;
	}

	/**
	 * 单选列表对话框
	 *
	 * @param context
	 * @param title
	 * @param items
	 * @param selectedItem
	 * @param onClickListener
	 * @return
	 */
	public static AlertDialog getListDialog(Context context, String title, String[] items, int selectedItem,
			Dialog.OnClickListener onClickListener) {
		AlertDialog.Builder builder = getBuilder(context);
		builder.setTitle(title);
		builder.setSingleChoiceItems(items, selectedItem, onClickListener);
		AlertDialog alertDialog = builder.create();
		return alertDialog;
	}

	public static AlertDialog showDateDialog(Context context, String title) {
		AlertDialog.Builder builder = getBuilder(context);
		builder.setTitle(title);
		View view = View.inflate(context, R.layout.layout_date_dialog, null);
		CalendarView calendarView = (CalendarView) view.findViewById(R.id.calendarView);
		return builder.create();
	}

	@SuppressLint("NewApi")
	public static AlertDialog.Builder getBuilder(Context context) {
		return new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
	}
}
