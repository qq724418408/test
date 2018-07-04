package com.bocop.xfjr.util.dialog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.boc.jx.tools.LogUtils;
import com.bocop.jxplatform.R;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TimePicker;

public class MaxDatePickerDialog {

	private Context mContext;
	private AlertDialog.Builder mAlertDialog;
	private int mHour, mMinute;
	private TimePickerDialogInterface timePickerDialogInterface;
	private TimePicker mTimePicker;
	private DatePicker mDatePicker;
	private int mTag = 0;
	private int mYear, mDay, mMonth;

	public MaxDatePickerDialog(Context context, TimePickerDialogInterface i) {
		this(context, i, 0, 0, 0);
	}

	@SuppressWarnings("deprecation")
	public MaxDatePickerDialog(Context context, TimePickerDialogInterface i, int year, int month, int day) {
		super();
		mContext = context;
		timePickerDialogInterface = i;
		this.mYear = year;
		this.mDay = day;
		this.mMonth = month;
		if (mYear == 0 || mMonth == 0 || mDay == 0) {
			Date date = new Date();
			mYear = date.getYear() + 1900;
			mMonth = date.getMonth() + 1;
			mDay = date.getDate();
		}
	}

	OnDateChangedListener mListener = new OnDateChangedListener() {

		@Override
		public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//			Date date = new Date(year, monthOfYear, dayOfMonth);
//			if (mYear == year && mMonth == monthOfYear && mDay == dayOfMonth) {
//				view.init(mYear, 11, mDay, mListener);
//			} else {
//				ToastUtils.show(mContext, "====", 0);
//			}
		}
	};

	@SuppressLint("InflateParams")
	private View initDateAndTimePicker(int... style) {
		View inflate = LayoutInflater.from(mContext).inflate(R.layout.xfjr_dialog_datatime, null);
		mTimePicker = (TimePicker) inflate.findViewById(R.id.dateAndTimePicker_timePicker);
		mDatePicker = (DatePicker) inflate.findViewById(R.id.dateAndTimePicker_datePicker);
		LogUtils.e("参数是：" + style[0]);
		if (style != null && style.length == 1) {
			if (1 == style[0]) {
				mDatePicker.setVisibility(View.GONE);
			} else if (2 == style[0]) {
				mTimePicker.setVisibility(View.GONE);
			}
		}
		mTimePicker.setIs24HourView(true);
		mDatePicker.setMaxDate(System.currentTimeMillis());
		//ToastUtils.show(mContext, mYear + "-" + mMonth + "-" + mDay, 0);
		mDatePicker.init(mYear, mMonth, mDay, mListener);
		resizePikcer(mTimePicker);
		resizePikcer(mDatePicker);
		return inflate;
	}

	/**
	 * 创建dialog
	 *
	 * @param view
	 */
	private void initDialog(View view) {
		mAlertDialog.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();

				if (mTag == 0) {
					getTimePickerValue();
				} else if (mTag == 1) {
					getDatePickerValue();
				} else if (mTag == 2) {
					getDatePickerValue();
					getTimePickerValue();
				}
				timePickerDialogInterface.positiveListener();

			}
		});
		mAlertDialog.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				timePickerDialogInterface.negativeListener();
				dialog.dismiss();
			}
		});
		mAlertDialog.setView(view);
	}

	/**
	 * 显示时间选择器
	 */
	public void showTimePickerDialog() {
		mTag = 0;
		View view = initDateAndTimePicker(1);
		mAlertDialog = new AlertDialog.Builder(mContext);
		mAlertDialog.setTitle("请选择时间");
		initDialog(view);
		mAlertDialog.show();

	}

	/**
	 * 显示日期选择器
	 */
	public void showDatePickerDialog() {
		mTag = 1;
		View view = initDateAndTimePicker(2);
		mAlertDialog = new AlertDialog.Builder(mContext);
		mAlertDialog.setTitle("请选择日期");
		initDialog(view);
		mAlertDialog.show();
	}

	/**
	 * 显示日期选择器
	 */
	public void showDateAndTimePickerDialog() {
		mTag = 2;
		View view = initDateAndTimePicker();
		mAlertDialog = new AlertDialog.Builder(mContext);
		mAlertDialog.setTitle("请选择时间和日期");
		initDialog(view);
		mAlertDialog.show();
	}

	/*
	 * 调整numberpicker大小
	 */
	private void resizeNumberPicker(NumberPicker np) {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(10, 0, 10, 0);
		np.setLayoutParams(params);
	}

	/**
	 * 调整FrameLayout大小
	 *
	 * @param tp
	 */
	private void resizePikcer(FrameLayout tp) {
		List<NumberPicker> npList = findNumberPicker(tp);
		for (NumberPicker np : npList) {
			resizeNumberPicker(np);
		}
	}

	/**
	 * 得到viewGroup里面的numberpicker组件
	 *
	 * @param viewGroup
	 * @return
	 */
	private List<NumberPicker> findNumberPicker(ViewGroup viewGroup) {
		List<NumberPicker> npList = new ArrayList<NumberPicker>();
		View child = null;
		if (null != viewGroup) {
			for (int i = 0; i < viewGroup.getChildCount(); i++) {
				child = viewGroup.getChildAt(i);
				if (child instanceof NumberPicker) {
					npList.add((NumberPicker) child);
				} else if (child instanceof LinearLayout) {
					List<NumberPicker> result = findNumberPicker((ViewGroup) child);
					if (result.size() > 0) {
						return result;
					}
				}
			}
		}
		return npList;
	}

	public int getYear() {
		return mYear;
	}

	public int getDay() {
		return mDay;
	}

	public int getMonth() {
		// 返回的时间是0-11
		return mMonth + 1;
	}

	public int getMinute() {
		return mMinute;
	}

	public int getHour() {
		return mHour;
	}

	/**
	 * 获取日期选择的值
	 */
	private void getDatePickerValue() {
		mYear = mDatePicker.getYear();
		mMonth = mDatePicker.getMonth();
		mDay = mDatePicker.getDayOfMonth();
	}

	/**
	 * 获取时间选择的值
	 */
	private void getTimePickerValue() {
		// api23这两个方法过时
		mHour = mTimePicker.getCurrentHour();// timePicker.getHour();
		mMinute = mTimePicker.getCurrentMinute();// timePicker.getMinute();
	}

	public interface TimePickerDialogInterface {
		public void positiveListener();

		public void negativeListener();
	}

}
