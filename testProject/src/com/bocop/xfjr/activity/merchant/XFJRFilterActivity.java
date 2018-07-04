package com.bocop.xfjr.activity.merchant;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.boc.jx.httptools.http.callback.IHttpCallback;
import com.boc.jx.tools.LogUtils;
import com.bocop.jxplatform.R;
import com.bocop.xfjr.activity.XFJRSearchActivity;
import com.bocop.xfjr.adapter.XfjrCommonSearchAdapter;
import com.bocop.xfjr.base.XfjrBaseActivity;
import com.bocop.xfjr.bean.BusinessBean;
import com.bocop.xfjr.bean.ErrorBean;
import com.bocop.xfjr.bean.MerchantBusiness;
import com.bocop.xfjr.config.HttpRequest;
import com.bocop.xfjr.config.UrlConfig;
import com.bocop.xfjr.util.XFJRUtil;
import com.bocop.xfjr.util.dialog.MaxDatePickerDialog;
import com.bocop.xfjr.util.dialog.MaxDatePickerDialog.TimePickerDialogInterface;
import com.google.gson.Gson;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

/**
 * 
 * 业务查询-筛选
 * 
 * @author TIAN FENG
 *
 */
@ContentView(R.layout.xfjr_activity_filter)
public class XFJRFilterActivity extends XfjrBaseActivity {
	private final String mTypeItems[] = { "处理中", "已通过", "已拒绝" };
	// 日期辅助工具
	private Calendar mCalendar, mCalendar2;

	@ViewInject(R.id.recyclerView)
	private RecyclerView recyclerView;// 列表
	@ViewInject(R.id.tvStartDate)
	private TextView tvStartDate;// 起始时间
	@ViewInject(R.id.tvEndDate)
	private TextView tvEndDate;// 结束时间 tvScType
	@ViewInject(R.id.tvScType)
	private TextView tvScType;// 类型
	// 是否自动加载
	private boolean mIsLoad = true;

	// 列表数据
	private List<BusinessBean> mData = new ArrayList<>();
	private Map<String, Object> mParams = new HashMap<>();
	@SuppressWarnings("rawtypes")
	private RecyclerView.Adapter mAdapter;
	private LinearLayoutManager mLayoutManager;
	private int mPage = 0;
	private MaxDatePickerDialog mTimePickerDialog;
	private String startDate, endDate; // yyyy/MM/dd

//	@Override
//	protected int getLoyoutId() {
//		return R.layout.xfjr_activity_filter;
//	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();
		initData();
	}

//	@Override
	protected void initView() {
		mLayoutManager = new LinearLayoutManager(this);
		// mLayoutManager.
		recyclerView.setLayoutManager(mLayoutManager);
	}

//	@Override
	protected void initData() {
		// 设置列表适配器
		recyclerView.setAdapter(mAdapter = new XfjrCommonSearchAdapter(this, mData));
		// 智能加载
		recyclerView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);
				if (newState == RecyclerView.SCROLL_STATE_IDLE) {
					int lastItemPosition = mLayoutManager.findLastVisibleItemPosition();
					// 倒数第四个开始加载
					if (lastItemPosition >= mLayoutManager.getItemCount() - 1) {
						if (mData.size() != 0 && mIsLoad) {
							getNetWork(++mPage);
						}
					}
				}
			}
		});
	}

	/**
	 * 点击首页按钮
	 * 
	 * @param view
	 */
	@OnClick(R.id.tvHomePage)
	private void clickHomePager(View view) {
		finish();
	}

	/**
	 * 点击搜索放大镜按钮 btnQuery
	 * 
	 * @param view
	 */
	@OnClick(R.id.ivSearch)
	private void clickSearch(View view) {
		XFJRSearchActivity.goMe(this, XFJRSearchActivity.MERCHANT_SEARCH);
	}

	/**
	 * 点击查询按钮 tvScType
	 * 
	 * @param view
	 */
	@OnClick(R.id.btnQuery)
	private void clickQuery(View view) {
		// 所有为空必须选择一个跳转
		if (TextUtils.isEmpty(tvScType.getText().toString())
				&& TextUtils.isEmpty(tvStartDate.getText().toString())
				&& TextUtils.isEmpty(tvEndDate.getText().toString())) {
			showToast("请至少选择一个条件");
			return;
		}
		if (TextUtils.isEmpty(tvScType.getText().toString())
				&& TextUtils.isEmpty(tvStartDate.getText().toString())
				&& !TextUtils.isEmpty(tvEndDate.getText().toString())) {
			showToast("请选择起始时间");
			return;
		}
		if (/*TextUtils.isEmpty(tvScType.getText().toString())
				&& */!TextUtils.isEmpty(tvStartDate.getText().toString())
				&& TextUtils.isEmpty(tvEndDate.getText().toString())) {
			showToast("请选择结束时间");
			return;
		}
		int c = -1; // 开始时间小于结束时间 = -1
		if (!TextUtils.isEmpty(startDate) && !TextUtils.isEmpty(endDate)) {
			c = XFJRUtil.compareDate(startDate, endDate, "yyyy/MM/dd");
		}
		if (c < 0) {
			mData.clear();
			getNetWork(mPage = 0);
		} else {
			showToast("开始时间必须早过结束时间");
		}
	}

	private void getNetWork(final int page) {
		mParams.put("type", getTypeByItem(tvScType.getText().toString()));
		// 修改显示格式为所需要的格式
		mParams.put("startTime",
				tvStartDate.getText().toString().replace("/", "-").replace(" ", ""));
		mParams.put("endTime", tvEndDate.getText().toString().replace("/", "-").replace(" ", ""));
		// 请求网络数据
		HttpRequest.queryMerchantBusiness(this, page, mParams,
				new IHttpCallback<MerchantBusiness>() {

					@Override
					public void onSuccess(String url, MerchantBusiness result) {
						// 成功后刷新适配器
						onRequestSuccess(result);
					}

					@Override
					public void onError(String url, Throwable e) {
						LogUtils.e("error ：" + e);
						if (page == 0) {
							String json = e.getMessage();
							ErrorBean error = new Gson().fromJson(json, ErrorBean.class);
							if (UrlConfig.emptyCode.equals("" + error.code)) {
								mData.clear();
								mAdapter.notifyDataSetChanged();
								showToast(error.msg);
								return;
							}

							UrlConfig.showErrorTips(XFJRFilterActivity.this, e, true);
						}
					}

					@Override
					public void onFinal(String url) {

					}
				});
	}

	/**
	 * 根据String得到type
	 */
	private String getTypeByItem(String string) {
		// 获取对应的type
		for (int i = 0; i < mTypeItems.length; i++) {
			if (mTypeItems[i].equals(tvScType.getText().toString())) {
				return i + "";
			}
		}
		return "";
	}

	/**
	 * 请求结果
	 * 
	 * @param result
	 *            结果
	 * @param isLoad
	 *            是否是加载
	 */
	private void onRequestSuccess(MerchantBusiness result) {
		if (result != null && result.getBusiness() != null) {
			mData.addAll(result.getBusiness());
			// 标识自动加载 数据对象为空不加载，没有数据不加载
			mIsLoad = result.getBusiness() != null && result.getBusiness().size() != 0;
			mAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * 状态选择
	 * 
	 * @param view
	 */
	@OnClick(R.id.tvScType)
	private void clickScType(final TextView view) {
		/**
		 * 弹出dialog 选择查询的类型 直接调用系统的，懒得写布局
		 */
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(" 选择类型").setItems(mTypeItems, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// 显示文字 可以定义全局变量type 然后在这里赋值
				view.setText(mTypeItems[which]);
			}
		});
		builder.create().show();
	}

	/**
	 * 起始日期
	 * 
	 * @param view
	 */
	@OnClick(R.id.tvStartDate)
	private void clickStartDate(TextView view) {
		if (mCalendar == null) {
			mCalendar = Calendar.getInstance();
		}
//	 结束日期为所选日期 如若没有选则为 当前日期
		getDateDialog(mCalendar, view);
//		getDateDialog(Calendar.getInstance(), view).show();

	}

	/**
	 * 结束日期
	 * 
	 * @param view
	 */
	@OnClick(R.id.tvEndDate)
	private void clickEndDate(TextView view) {
		if (mCalendar2 == null) {
			mCalendar2 = Calendar.getInstance();
		}
//		 结束日期为所选日期 如若没有选则为 当前日期
		getDateDialog(mCalendar2, view);
//		getDateDialog(Calendar.getInstance(), view).show();
	}
	
	/**
	 * 获取系统日期选择器
	 * 
	 * @param c
	 * @param textView
	 * @return
	 */
	private MaxDatePickerDialog getDateDialog(Calendar calendar, final TextView textView) {
		if (textView.getId() == R.id.tvStartDate) {
			mCalendar = calendar;
		} else {
			mCalendar2 = calendar;
		}
		mTimePickerDialog = new MaxDatePickerDialog(this, new TimePickerDialogInterface() {
			
			@Override
			public void positiveListener() {
				int y = mTimePickerDialog.getYear();
				int m = mTimePickerDialog.getMonth();
				int d = mTimePickerDialog.getDay();
				String time = y + "/" + m + "/" + d + " ";
				if (textView.getId() == R.id.tvStartDate) {
					startDate = time;
					mCalendar.set(y, m - 1, d);
				} else {
					endDate = time;
					mCalendar2.set(y, m - 1, d);
				}
				textView.setText(time);
			}
			
			@Override
			public void negativeListener() {
			}
		}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
		mTimePickerDialog.showDatePickerDialog();
//		/*DatePickerDialog*/ mTimePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
//
//			@Override
//			public void onDateSet(DatePicker arg0, int y, int m, int d) {
//				
//				if (textView.getId() == R.id.tvStartDate) {
//					mCalendar.set(y, m, d);
//					startDate = y + "/" + (m + 1) + "/" + d + " ";
//				}else {
//					endDate = y + "/" + (m + 1) + "/" + d + " ";
//					mCalendar2.set(y, m, d);
//				}
//				textView.setText(y + "/" + (m + 1) + "/" + d + " ");
//			}
//
//		}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
//		mTimePickerDialog.setTitle("请选择日期");
//		mTimePickerDialog.show();
		return mTimePickerDialog;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (null != mTimePickerDialog) {
			mTimePickerDialog = null;
		}
	}
	
}
