package com.bocop.xms.activity;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.boc.jx.tools.CalendarUtils;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.config.TransactionValue;
import com.bocop.jxplatform.util.CspUtil;
import com.bocop.jxplatform.util.CspUtil.CallBack;
import com.bocop.jxplatform.util.LoginUtil;
import com.bocop.jxplatform.util.Mcis;
import com.bocop.xms.service.AlarmService;
import com.bocop.xms.service.AlarmServiceManager;
import com.bocop.xms.utils.SharedPreferencesUtils;
import com.bocop.xms.widget.OnWheelChangedListener;
import com.bocop.xms.widget.WheelView;
import com.bocop.xms.widget.adapter.ArrayWheelAdapter;
import com.bocop.xms.xml.CspXmlXmsCom;
import com.bocop.xms.xml.remind.EventBean;
import com.bocop.xms.xml.remind.EventComResp;
import com.bocop.xms.xml.remind.EventComXmlBean;
import com.bocop.xms.xml.remind.EventSaveResp;
import com.bocop.xms.xml.remind.EventSaveXmlBean;
import com.bocop.yfx.utils.ToastUtils;

/**
 * 提醒管理界面
 * 
 * @author ftl
 * 
 */
@ContentView(R.layout.xms_activity_remind_manage)
public class RemindManageActivity extends BaseActivity {

	@ViewInject(R.id.tvTitle)
	private TextView tvTitle;
	@ViewInject(R.id.etContent)
	private EditText etContent;
	@ViewInject(R.id.tvNoticeTime)
	private TextView tvNoticeTime;
	@ViewInject(R.id.tvNum)
	private TextView tvNum;
	@ViewInject(R.id.tvTYPE)
	private TextView tvTYPE;

	private String date = "";// 传递过来的date
	private String hour = "";
	private String minute = "";
	private String oprate = "1";
	private String dayOfWeek = "";// 星期几
	private EventBean eventBean;
	private String eventId;
	public final static String DATE = "date";
	public final static String OPRATE = "oprate";// 1：新增，2：编辑
	public final static String EVENT = "event";
	public final static String DAY_OF_WEEK = "dayOfWeek";
	private PopupWindow window;
	private String type;// 重复类型
	// private String remindTime;
	// private String endTime;
	private String content;
	private String currentTime;// 当前时间
	private String currentData;// 当前日期
	private String showRemindTime = "";// 提醒时间控件中显示的内容
	private String showType = "";// 提醒时间控件中显示的重复类型
	private String dateOfMonth;// 每月几号
	private String showHour;// 显示的小时
	private String showMinute;// 显示的分钟
	private WheelView wvYear;
	private WheelView wvMonth;
	private WheelView wvDay;
	private WheelView wvHour;
	private WheelView wvMinute;
	private String[] arryYears;
	private String[] arryMonths;
	private String[] arryDays;
	private String[] arryHours;
	private String[] arryMinutes;
	private String[] arryDayOfWeeks;
	private String cYear = "";
	private String cMonth = "";
	private String cDay = "";
	private String cHour = "";
	private String cMinute = "";
	private int cMonthItem;
	private int cDayItem;
	private int cHourItem;
	private int cMinuteItem;
	private int cYearItem;
	private String repeatValue;// 重复值
	private ArrayWheelAdapter<String> mViewYearAdapter;
	private ArrayWheelAdapter<String> mViewMonthAdapter;
	private ArrayWheelAdapter<String> mViewDayAdapter;
	private ArrayWheelAdapter<String> mViewHourAdapter;
	private ArrayWheelAdapter<String> mViewMinuteAdapter;
	private ArrayList<String> yearDataList;
	private ArrayList<String> monthDataList;
	private ArrayList<String> dayDataList;
	private ArrayList<String> weekDataList;
	private boolean isLeap;// 是否位闰年
	private int pickMonths;
	private String never="一次性";
	private String everyDay="每天";
	private String everyWeek="每周";
	private String everyMonth="每月";
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initTitle();
		initView();
	}

	private void initEditView() {

		TextWatcher tw = new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				int num = s.length();
				tvNum.setText(num + "/200");
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		};
		etContent.addTextChangedListener(tw);
	}

	private void initTitle() {
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			date = bundle.getString(DATE);
			dateOfMonth = date.substring(8, 11);

			oprate = bundle.getString(OPRATE);
			eventBean = (EventBean) bundle.getSerializable(EVENT);
			dayOfWeek = bundle.getString(DAY_OF_WEEK);
			if ("1".equals(oprate)) {
				tvTitle.setText("新增事件");

			} else {
				tvTitle.setText("修改事件");
				// dayOfWeek=eventBean.
			}
		} else {
			tvTitle.setText("新增事件");
		}
	}

	private void initView() {
		initEditView();
		Date dateTime = new Date();
		SimpleDateFormat sfDate = new SimpleDateFormat("yyyy年MM月dd日");
		SimpleDateFormat sfTime = new SimpleDateFormat("HH:mm");
		currentData = sfDate.format(dateTime);
		currentTime = sfTime.format(dateTime);
		hour = getSystemTime().get(0);
		minute = getSystemTime().get(1);

		/*
		 * if(date!=null&date.equals(currentData)){ remindTime=date+" "
		 * +currentTime; }else{ remindTime=date+" "+"00:00"; }
		 */
		showRemindTime = currentTime;
		if ("1".equals(oprate)) {
			// 设置为当前时间
			type = never;
			// repeatValue=date+" "+currentTime;
			// remindTime=date+" "+currentTime;
			showHour = hour;
			showMinute = minute;
			// endTime=date+" "+"23:59";
			tvNoticeTime.setText(date + " " + currentTime);
		} else {
			if (eventBean != null) {
				eventId = eventBean.getEventId();
				content = eventBean.getContent();
				repeatValue = eventBean.getRepeatValue();
				if (!TextUtils.isEmpty(content)) {
					etContent.setText(content);
					etContent.setSelection(content.length());
				}
				// remindTime = date+"
				// "+eventBean.getRemindtime().substring(0,5);
				showHour = eventBean.getRemindtime().substring(0, 2);
				showMinute = eventBean.getRemindtime().substring(3, 5);
				if (eventBean.getRemindtime().length() > 5) {
					showType = eventBean.getRemindtime().substring(6, eventBean.getRemindtime().length());
				}
				showRemindTime = eventBean.getRemindtime().substring(0, 5);
				// String dTime=eventBean.getEndtime();
				// String aString=dTime.substring(0, 4) + "年" +
				// dTime.substring(5, 7) + "月" + dTime.substring(8,
				// 10)+"日"+dTime.substring(10, 16);
				// endTime = aString;
				String typeCode = eventBean.getType();
				type = getType(typeCode);
				String remindtime = eventBean.getRemindtime() != null ? eventBean.getRemindtime() : "";
				if (never.equals(type)) {
					tvNoticeTime.setText(date + " " + eventBean.getRemindtime().substring(0, 5));
				} else if (everyDay.equals(type)) {
					tvNoticeTime.setText(remindtime.substring(0, 5));
				} else if (everyWeek.equals(type)) {
					String[] array = remindtime.split(" ");
					if (array != null && array.length == 2) {
						tvNoticeTime.setText(array[1] + " " + array[0]);
					}
				} else if (everyMonth.equals(type)) {
					cDay = dateOfMonth;
					String[] array = remindtime.split(" ");
					if (array != null && array.length == 2) {
						tvNoticeTime.setText(array[1] + " " + array[0]);
					}
				}
			}
		}
		tvTYPE.setText(type);
//		etContent.setText(content);
		// tvNoticeTime.setText(showRemindTime+" "+showType);

	}

	/**
	 * 获取系统时间
	 * 
	 * @return List<String>
	 */
	public List<String> getSystemTime() {
		Date date = new Date();
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH-mm-ss");
		List<String> timeList = new ArrayList<String>();
		timeList.add(timeFormat.format(date).split("-")[0]);
		timeList.add(timeFormat.format(date).split("-")[1]);
		return timeList;
	}

	@OnClick({ R.id.tvCancel, R.id.tvSave, R.id.rlNoticeTime, R.id.rlRecycleType })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tvCancel:
			finish();
			break;
		case R.id.tvSave:
			if (!etContent.getText().toString().equals("")) {
				closeInputMethod();
				requestEvent();
			} else {
				ToastUtils.show(RemindManageActivity.this, "内容不能为空", Toast.LENGTH_SHORT);
			}
			break;
		case R.id.rlNoticeTime:
			WindowManager.LayoutParams lp = getWindow().getAttributes();
			lp.alpha = 0.5f;
			getWindow().setAttributes(lp);
			showPopWindow(R.layout.xms_popwindow_timepick, tvNoticeTime);
			break;
		case R.id.rlRecycleType:
			lp = getWindow().getAttributes();
			lp.alpha = 0.5f;
			getWindow().setAttributes(lp);
			showPopWindow(R.layout.xms_popwindow_recycletype, null);
			break;
		}
	}

	/**
	 * 请求事件
	 */
	private void requestEvent() {
		try {
			// 生成CSP XML报文
			String txCode = TextUtils.isEmpty(eventId) ? "MS002003" : "MS002004";
			CspXmlXmsCom cspXmlXmsCom = new CspXmlXmsCom(LoginUtil.getUserId(this), txCode);
			cspXmlXmsCom.setEventId(eventId);
			cspXmlXmsCom.setTime(date + " " + hour + ":" + minute);
			cspXmlXmsCom.setContent(etContent.getText().toString());
			// String rTime = remindTime.substring(0, 4) + "/" +
			// remindTime.substring(5, 7) + "/" + remindTime.substring(8,
			// 10)+remindTime.substring(11, 17);
			// String overTime=endTime.substring(0, 4) + "/" +
			// endTime.substring(5, 7) + "/" + endTime.substring(8,
			// 10)+endTime.substring(11, 17);
			final String remindTime = getRemindTime(tvTYPE.getText().toString());
			final String repeatValue = getValue(tvTYPE.getText().toString());
			final String type = getTypeCode(tvTYPE.getText().toString());
			cspXmlXmsCom.setRemindTime(remindTime);
			// cspXmlXmsCom.setEndTime(overTime);
			cspXmlXmsCom.setRepeatValue(repeatValue);
			cspXmlXmsCom.setType(type);
			String strXml = cspXmlXmsCom.getCspXml();
			Log.i("tag", "getCspXml");
			// 生成MCIS报文
			Mcis mcis = new Mcis(strXml, TransactionValue.CSPSZF);
			Log.i("tag", "Mcis");
			final byte[] byteMessage = mcis.getMcis();
			// 发送报文
			CspUtil cspUtil = new CspUtil(this);
			Log.i("tag", "发送报文： " + new String(byteMessage, "GBK"));
			// cspUtil.setTest(true);
			cspUtil.postCspLogin(byteMessage, new CallBack() {
				@SuppressWarnings("unchecked")
				@Override
				public void onSuccess(String responStr) {
					SharedPreferencesUtils spf = new SharedPreferencesUtils(RemindManageActivity.this,
							AlarmService.ALARM_SER);
					List<EventBean> list = (List<EventBean>) spf
							.getObject(LoginUtil.getUserId(RemindManageActivity.this), EventBean.class);
					if ("1".equals(oprate)) {
						EventSaveXmlBean eventSaveXmlBean = EventSaveResp.readStringXml(responStr);
						if (!"00".equals(eventSaveXmlBean.getErrorcode())) {
							Toast.makeText(RemindManageActivity.this, eventSaveXmlBean.getErrormsg(),
									Toast.LENGTH_SHORT).show();
						} else {
							EventBean eventBean = new EventBean();
							eventBean.setEventId(eventSaveXmlBean.getEventId());
							eventBean.setRemindtime(remindTime);
							eventBean.setRepeatValue(repeatValue);
							eventBean.setContent(etContent.getText().toString());
							eventBean.setType(type);
							if (list == null) {
								list = new ArrayList<EventBean>();
							}
							list.add(eventBean);
							spf.setObject(LoginUtil.getUserId(RemindManageActivity.this), list);
							// 开始闹铃服务
							AlarmServiceManager.getInstance().startAlarmService(RemindManageActivity.this);
							setResult(Activity.RESULT_OK);
							finish();
						}
					} else {
						EventComXmlBean eventComXmlBean = EventComResp.readStringXml(responStr);
						if (!"00".equals(eventComXmlBean.getErrorcode())) {
							Toast.makeText(RemindManageActivity.this, eventComXmlBean.getErrormsg(), Toast.LENGTH_SHORT)
									.show();
						} else {
							if (list != null) {
								for (int i = 0; i < list.size(); i++) {
									EventBean eventBean = list.get(i);
									if (eventId.equals(eventBean.getEventId())) {
										eventBean.setRemindtime(remindTime);
										eventBean.setRepeatValue(repeatValue);
										eventBean.setContent(etContent.getText().toString());
										eventBean.setType(type);
										break;
									}
								}
								spf.setObject(LoginUtil.getUserId(RemindManageActivity.this), list);
							}
							setResult(Activity.RESULT_OK);
							finish();
						}
					}
				}

				@Override
				public void onFinish() {

				}

				@Override
				public void onFailure(String responStr) {
					Toast.makeText(RemindManageActivity.this, responStr, Toast.LENGTH_SHORT).show();
				}
			});
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private void showPopWindow(int layoutId, TextView tv) {
		closeInputMethod();

		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(layoutId, null);
		initPop(view, layoutId, tv);
		window = new PopupWindow(view);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		window.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
		window.setHeight(dm.heightPixels / 5 * 2);
		window.setFocusable(true);
		// window.setOutsideTouchable(true);
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		window.setBackgroundDrawable(dw);
		// window.setAnimationStyle(R.style.mypopwindow_anim_style);
		window.showAtLocation(RemindManageActivity.this.findViewById(R.id.rlNoticeTime), Gravity.BOTTOM, 0, 0);
		window.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = 1.0f;
				getWindow().setAttributes(lp);
			}
		});
	}

	private void initPop(View view, int layoutId, TextView tv) {
		switch (layoutId) {
		case R.layout.xms_popwindow_timepick:
			initTimeDialogView(view);
			break;
		case R.layout.xms_popwindow_recycletype:
			initTypeDialogView(view);
			break;
		}
	}

	private void initTimeDialogView(View view) {
		wvYear = (WheelView) view.findViewById(R.id.wvYear);
		wvMonth = (WheelView) view.findViewById(R.id.wvMonth);
		wvDay = (WheelView) view.findViewById(R.id.wvDay);
		wvHour = (WheelView) view.findViewById(R.id.wvHour);
		wvMinute = (WheelView) view.findViewById(R.id.wvMinute);
		TextView tvSure = (TextView) view.findViewById(R.id.tvSure);
		TextView tvCancle = (TextView) view.findViewById(R.id.tvCancle);
		/*
		 * List<String> hourList = new ArrayList<String>(); List<String>
		 * minuteList = new ArrayList<String>(); //初始化小时数据 for (int i = 0; i <
		 * 24; i++) { hourList.add(i < 10 ? "0" + i : "" + i); } //初始化分钟数据 for
		 * (int i = 0; i < 60; i++) { minuteList.add(i < 10 ? "0" + i : "" + i);
		 * }
		 */
		if (never.equals(type)) {// 永不
			wvYear.setVisibility(View.VISIBLE);
			wvMonth.setVisibility(View.VISIBLE);
			wvDay.setVisibility(View.VISIBLE);
			wvHour.setVisibility(View.VISIBLE);
			wvMinute.setVisibility(View.VISIBLE);
		} else if (everyDay.equals(type)) {// 每天
			wvYear.setVisibility(View.GONE);
			wvMonth.setVisibility(View.GONE);
			wvDay.setVisibility(View.GONE);
			wvHour.setVisibility(View.VISIBLE);
			wvMinute.setVisibility(View.VISIBLE);
		} else if (everyWeek.equals(type) || everyMonth.equals(type)) {// 每周 每月
			wvYear.setVisibility(View.GONE);
			wvMonth.setVisibility(View.GONE);
			wvDay.setVisibility(View.VISIBLE);
			wvHour.setVisibility(View.VISIBLE);
			wvMinute.setVisibility(View.VISIBLE);
		}
		initPopDatas();
		// String time=tvNoticeTime.getText().toString();
		/*
		 * tvSure.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View arg0) { remindTime=date+" "
		 * +showHour+":"+showMinute; showRemindTime=showHour+":"+showMinute;
		 * tvNoticeTime.setText(showRemindTime+" "+showType); window.dismiss();
		 * } });
		 */
		tvSure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// String pickTime=mViewYear.getItemView(index);
				String pickYear = mViewYearAdapter.getItemText(wvYear.getCurrentItem()).toString();
				String pickMonth = mViewMonthAdapter.getItemText(wvMonth.getCurrentItem()).toString();
				String pickDay = mViewDayAdapter.getItemText(wvDay.getCurrentItem()).toString();
				String pickHour = mViewHourAdapter.getItemText(wvHour.getCurrentItem()).toString().substring(0, 2);
				String pickMinute = mViewMinuteAdapter.getItemText(wvMinute.getCurrentItem()).toString().substring(0,
						2);
				if (never.equals(type)) {
					tvNoticeTime.setText(pickYear + pickMonth + pickDay + " " + pickHour + ":" + pickMinute);
				} else if (everyDay.equals(type)) {
					tvNoticeTime.setText(pickHour + ":" + pickMinute);
				} else if (everyWeek.equals(type)) {
					tvNoticeTime.setText("每" + pickDay + " " + pickHour + ":" + pickMinute);
				} else if (everyMonth.equals(type)) {
					tvNoticeTime.setText("每月" + pickDay + " " + pickHour + ":" + pickMinute);
				}

				window.dismiss();
			}
		});

		tvCancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				window.dismiss();
			}
		});
	}

	private void initTypeDialogView(View view) {
		// PickerView pvType=(PickerView) view.findViewById(R.id.pvType);
		TextView tvSure = (TextView) view.findViewById(R.id.tvSure);
		TextView tvCancle = (TextView) view.findViewById(R.id.tvCancle);
		final RadioButton rbYB = (RadioButton) view.findViewById(R.id.cbYB);
		final RadioButton rbMT = (RadioButton) view.findViewById(R.id.cbMT);
		final RadioButton rbMZ = (RadioButton) view.findViewById(R.id.cbMZ);
		final RadioButton rbMY = (RadioButton) view.findViewById(R.id.cbMY);
		RadioGroup rgGroup = (RadioGroup) view.findViewById(R.id.rgGroup);
		String defautType = tvTYPE.getText().toString();
		if (defautType.equals(never)) {
			rgGroup.check(R.id.cbYB);
		} else if (defautType.equals(everyDay)) {
			rgGroup.check(R.id.cbMT);
		} else if (defautType.equals(everyWeek)) {
			rgGroup.check(R.id.cbMY);
		} else if (defautType.equals(everyMonth)) {
			rgGroup.check(R.id.cbMZ);
		}

		tvSure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int year = Integer.parseInt(date.substring(0, 4)) + 1;
				if (rbYB.isChecked()) {
					type = rbYB.getText().toString();
					showType = "";
					// endTime=date+" "+"23:59";
					tvNoticeTime.setText(date + " " + showRemindTime);
				} else if (rbMT.isChecked()) {
					type = rbMT.getText().toString();
					showType = "每天";
					// endTime=year+date.substring(4,11)+" "+hour+":"+minute;
					tvNoticeTime.setText(showRemindTime);
				} else if (rbMZ.isChecked()) {
					type = rbMZ.getText().toString();
					showType = "每" + dayOfWeek;
					// endTime=year+date.substring(4,11)+" "+hour+":"+minute;
					tvNoticeTime.setText(showType + " " + showRemindTime);
				} else if (rbMY.isChecked()) {
					type = rbMY.getText().toString();
					showType = "每月" + dateOfMonth;
					// endTime=year+date.substring(4,11)+" "+hour+":"+minute;
					tvNoticeTime.setText(showType + " " + showRemindTime);
					cDay = dateOfMonth;
				}
				tvTYPE.setText(type);
				window.dismiss();
			}
		});
		tvCancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				window.dismiss();
			}
		});

	}

	/**
	 * type转typecode
	 * 
	 * @param mtype
	 * @return
	 */
	private String getTypeCode(String mtype) {
		if (mtype.equals(never)) {
			return "0";
		} else if (mtype.equals(everyDay)) {
			return "1";
		} else if (mtype.equals(everyWeek)) {
			return "2";
		} else if (mtype.equals(everyMonth)) {
			return "3";
		}
		return null;
	}

	/**
	 * typeCode转type
	 * 
	 * @param typeCode
	 */
	private String getType(String mtypeCode) {
		if (mtypeCode != null) {
			if (mtypeCode.equals("0")) {
				return never;
			} else if (mtypeCode.equals("1")) {
				return everyDay;
			} else if (mtypeCode.equals("2")) {
				return everyWeek;
			} else if (mtypeCode.equals("3")) {
				return everyMonth;
			}
		}
		return null;

	}

	/**
	 * 初始化时间数据
	 */
	private void initPopDatas() {

		yearDataList = new ArrayList<String>();
		monthDataList = new ArrayList<String>();
		dayDataList = new ArrayList<String>();
		ArrayList<String> hourDataList = new ArrayList<String>();
		ArrayList<String> minuteDataList = new ArrayList<String>();
		weekDataList = new ArrayList<String>();

		for (int i = 1900; i < 3000; i++) {
			yearDataList.add(i + "年");
		}
		for (int i = 1; i < 13; i++) {
			monthDataList.add((i < 10 ? "0" + i : i) + "月");
		}

		for (int i = 0; i < 24; i++) {
			hourDataList.add((i < 10 ? "0" + i : i) + "时");
		}
		for (int i = 0; i < 60; i++) {
			minuteDataList.add((i < 10 ? "0" + i : i) + "分");
		}

		// 初始化周几
		weekDataList.add("周日");
		weekDataList.add("周一");
		weekDataList.add("周二");
		weekDataList.add("周三");
		weekDataList.add("周四");
		weekDataList.add("周五");
		weekDataList.add("周六");

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String showDate = sdf.format(date);
		isLeap = CalendarUtils.isLeapYear(Integer.parseInt(showDate.substring(0, 4)));
		pickMonths = Integer.parseInt(showDate.substring(4, 6));

		if (everyWeek.equals(type)) {
			dayDataList.clear();
			dayDataList.addAll(weekDataList);
		} else {
			dayDataList.clear();
			for (int i = 1; i < CalendarUtils.getDaysOfMonth(isLeap, pickMonths) + 1; i++) {
				dayDataList.add((i < 10 ? "0" + i : i) + "日");
			}
		}

		arryYears = (String[]) yearDataList.toArray(new String[yearDataList.size()]);
		arryMonths = (String[]) monthDataList.toArray(new String[monthDataList.size()]);
		arryDays = (String[]) dayDataList.toArray(new String[dayDataList.size()]);
		arryHours = (String[]) hourDataList.toArray(new String[hourDataList.size()]);
		arryMinutes = (String[]) minuteDataList.toArray(new String[minuteDataList.size()]);
		arryDayOfWeeks = (String[]) weekDataList.toArray(new String[weekDataList.size()]);
		setUpData();
	}

	/**
	 * 时间选择器设置adapter
	 */
	private void setUpData() {
		mViewYearAdapter = new ArrayWheelAdapter<>(RemindManageActivity.this, arryYears);
		mViewMonthAdapter = new ArrayWheelAdapter<>(RemindManageActivity.this, arryMonths);
		mViewDayAdapter = new ArrayWheelAdapter<>(RemindManageActivity.this, arryDays);
		mViewHourAdapter = new ArrayWheelAdapter<>(RemindManageActivity.this, arryHours);
		mViewMinuteAdapter = new ArrayWheelAdapter<>(RemindManageActivity.this, arryMinutes);
		String time = tvNoticeTime.getText().toString();
		if (time.length() >= 17 && never.equals(type)) {// type为永不。显示具体日期
			cYear = time.substring(0, 5);
			cMonth = time.substring(5, 8);
			cDay = time.substring(8, 11);
			cHour = time.substring(12, 14) + "时";
			cMinute = time.substring(15, 17) + "分";

		} else if (time.length() == 5 && everyDay.equals(type)) {// type为每天，显示时分
			cYear = "";
			cMonth = "";
			cDay = "";
			cHour = time.substring(0, 2) + "时";
			cMinute = time.substring(3, 5) + "分";
		} else if (time.length() == 9 && everyWeek.equals(type)) {// type为每周
			cYear = "";
			cMonth = "";
			cDay = time.substring(1, 3);
			cHour = time.substring(4, 6) + "时";
			cMinute = time.substring(7, 9) + "分";
		} else if (time.length() == 11 && everyMonth.equals(type)) {// type为每月
			cYear = "";
			cMonth = "";
			cDay = time.substring(2, 5);
			cHour = time.substring(6, 8) + "时";
			cMinute = time.substring(9, 11) + "分";
		}

		// 初始化adapter的被选中的item
		cYearItem = mViewYearAdapter.setItemText(cYear);
		cMonthItem = mViewMonthAdapter.setItemText(cMonth);
		cDayItem = mViewDayAdapter.setItemText(cDay);
		cHourItem = mViewHourAdapter.setItemText(cHour);
		cMinuteItem = mViewMinuteAdapter.setItemText(cMinute);

		wvYear.setViewAdapter(mViewYearAdapter);
		wvMonth.setViewAdapter(mViewMonthAdapter);
		wvDay.setViewAdapter(mViewDayAdapter);
		wvHour.setViewAdapter(mViewHourAdapter);
		wvMinute.setViewAdapter(mViewMinuteAdapter);

		wvYear.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				// System.out.println("-------oldvalue"+yearDataList.get(newValue).toString());
				// System.out.println("-------newvalue"+newValue);
				String year = yearDataList.get(newValue).toString();
				int pickYears = Integer.parseInt(year.substring(0, 4));
				isLeap = CalendarUtils.isLeapYear(pickYears);
				int daySize = CalendarUtils.getDaysOfMonth(isLeap, pickMonths);
				System.out.println("--------day" + CalendarUtils.getDaysOfMonth(isLeap, pickMonths));
				if (everyWeek.equals(type)) {
					dayDataList.clear();
					dayDataList.addAll(weekDataList);
				} else {
					dayDataList.clear();
					for (int i = 1; i < daySize + 1; i++) {
						dayDataList.add((i < 10 ? "0" + i : i) + "日");
					}
					arryDays = (String[]) dayDataList.toArray(new String[dayDataList.size()]);

					System.out.println("--------->>>" + arryDays);
					// mViewDayAdapter.notify();
					mViewDayAdapter = new ArrayWheelAdapter<>(RemindManageActivity.this, arryDays);
					wvDay.setViewAdapter(mViewDayAdapter);

					System.out.println("------ssss" + mViewDayAdapter.getItemText(wvDay.getCurrentItem()));
					if (mViewDayAdapter.getItemText(wvDay.getCurrentItem()) == null) {
						wvDay.setCurrentItem(mViewDayAdapter.setItemText(dayDataList.get(dayDataList.size() - 1)));
					}
				}
			}
		});

		wvMonth.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				String month = monthDataList.get(newValue).toString();

				if (month.startsWith("0")) {
					pickMonths = Integer.parseInt(month.substring(1, 2));
				} else {
					pickMonths = Integer.parseInt(month.substring(0, 2));
				}

				int daySize = CalendarUtils.getDaysOfMonth(isLeap, pickMonths);
				System.out.println("--------day" + CalendarUtils.getDaysOfMonth(isLeap, pickMonths));
				if (everyWeek.equals(type)) {
					dayDataList.clear();
					dayDataList.addAll(weekDataList);
				} else {
					dayDataList.clear();
					for (int i = 1; i < daySize + 1; i++) {
						dayDataList.add((i < 10 ? "0" + i : i) + "日");
					}
					arryDays = (String[]) dayDataList.toArray(new String[dayDataList.size()]);

					System.out.println("--------->>>" + arryDays);
					// mViewDayAdapter.notify();
					mViewDayAdapter = new ArrayWheelAdapter<>(RemindManageActivity.this, arryDays);
					wvDay.setViewAdapter(mViewDayAdapter);
					if (mViewDayAdapter.getItemText(wvDay.getCurrentItem()) == null) {
						wvDay.setCurrentItem(mViewDayAdapter.setItemText(dayDataList.get(dayDataList.size() - 1)));
					}
				}
			}
		});

		wvYear.setCurrentItem(cYearItem);
		wvMonth.setCurrentItem(cMonthItem);
		wvDay.setCurrentItem(cDayItem);
		wvHour.setCurrentItem(cHourItem);
		wvMinute.setCurrentItem(cMinuteItem);

	}

	/**
	 * type为每周时获取repeatValue
	 * 
	 * @return
	 */
	private String getDayOfWeek(String day) {
		if ("周日".equals(day)) {
			return "0";
		} else if ("周一".equals(day)) {
			return "1";
		} else if ("周二".equals(day)) {
			return "2";
		} else if ("周三".equals(day)) {
			return "3";
		} else if ("周四".equals(day)) {
			return "4";
		} else if ("周五".equals(day)) {
			return "5";
		} else if ("周六".equals(day)) {
			return "6";
		}
		return null;
	}

	/**
	 * 当type为每月时，返回的repeatValue
	 * 
	 * @return
	 */
	private String getDayOfMonth(String mType) {
		String time = tvNoticeTime.getText().toString();
		if (everyMonth.equals(mType)) {
			String day = time.substring(2, 4);
			return day;
		}
		return null;
	}

	private String getValue(String type) {
		String time = tvNoticeTime.getText().toString();
		if (never.equals(type)) {
			String value = time.substring(0, 4) + "/" + time.substring(5, 7) + "/" + time.substring(8, 10)
					+ time.substring(11, 17);
			return value;
		} else if (everyDay.equals(type)) {
			return time;
		} else if (everyWeek.equals(type)) {
			String weekTime = time.substring(1, 3);
			return getDayOfWeek(weekTime);
		} else if (everyMonth.equals(type)) {
			return getDayOfMonth(type);
		}
		return null;

	}

	private String getRemindTime(String type) {
		String time = tvNoticeTime.getText().toString();
		String[] array = time.split(" ");
		if (never.equals(type)) {
			String remindtime = time.substring(12, 17);
			return remindtime;
		} else if (everyDay.equals(type)) {
			return time;
		} else if (everyWeek.equals(type)) {
			return array[1];
		} else if (everyMonth.equals(type)) {
			return array[1];
		}
		return null;
	}

	private void closeInputMethod() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(etContent.getWindowToken(), 0);
	}
}
