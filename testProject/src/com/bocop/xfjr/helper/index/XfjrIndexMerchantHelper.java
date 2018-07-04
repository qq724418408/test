package com.bocop.xfjr.helper.index;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.baseUtil.view.ViewUtils;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.boc.jx.httptools.http.callback.IHttpCallback;
import com.bocop.jxplatform.R;
import com.bocop.xfjr.XfjrMain;
import com.bocop.xfjr.activity.XfjrIndexActivity;
import com.bocop.xfjr.activity.merchant.XFJRFilterActivity;
import com.bocop.xfjr.argument.ArgumentUtil;
import com.bocop.xfjr.argument.Subscribe;
import com.bocop.xfjr.bean.TypeDataBean;
import com.bocop.xfjr.bean.UserInfoBean;
import com.bocop.xfjr.bean.UserInfoBean.YearBean;
import com.bocop.xfjr.bean.login.LoginBean;
import com.bocop.xfjr.config.HttpRequest;
import com.bocop.xfjr.config.UrlConfig;
import com.bocop.xfjr.constant.XFJRConstant;
import com.bocop.xfjr.helper.index.callback.UserInfoBeanCallback;
import com.bocop.xfjr.util.PreferencesUtil;
import com.bocop.xfjr.util.XFJRUtil;
import com.bocop.xfjr.view.brokelineview.BrokenLineChartView;
import com.bocop.xfjr.view.brokelineview.DefaultShowRule;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * 商户页面
 */
public class XfjrIndexMerchantHelper implements IIndexHelper {

	// 首页Activity
	private BaseActivity mActivity;
	// 商户布局
	private View mView;
	// 商户名称
	@ViewInject(R.id.tvCustomUsername)
	private TextView tvCustomUsername;
	// 银行地址
	@ViewInject(R.id.tvCustomUserAddr)
	private TextView tvCustomUserAddr;
	// 折线图
	@ViewInject(R.id.areachartView)
	private BrokenLineChartView areachartView;
	// 左边年份按钮
	@ViewInject(R.id.rbChangeToLastYear)
	private RadioButton leftBtn;
	// 右边年份按钮
	@ViewInject(R.id.rbChangeToThisYear)
	private RadioButton rightBtn;
	// 处理中
	@ViewInject(R.id.tvDealing)
	private TextView tvDealing;
	// 处理中金额
	@ViewInject(R.id.tvDealingMoney)
	private TextView tvDealingMoney;
	// 已通過
	@ViewInject(R.id.tvHadPass)
	private TextView tvHadPass;
	// 已通過
	@ViewInject(R.id.tvHadPassMoney)
	private TextView tvHadPassMoney;
	// 已拒絕
	@ViewInject(R.id.tvHadRefused)
	private TextView tvHadRefused;
	// 已拒絕
	@ViewInject(R.id.tvHadRefusedMoney)
	private TextView tvHadRefusedMoney;
	// 2017
	@ViewInject(R.id.rbChangeToThisYear)
	private View rbChangeToThisYear;

	// 请求到的列表数据
	private UserInfoBean mUserInfoData;

	public XfjrIndexMerchantHelper(BaseActivity activity, View view) {
		super();
		ArgumentUtil.get().register(this);
		this.mActivity = activity;
		this.mView = view;
		ViewUtils.inject(this, mView);
	}

	@Subscribe
	public void receive(LoginBean loginBean) {
		// 设置商戶名称
		tvCustomUsername.setText(loginBean.getMerchantInfo().getMerchantName());
		// 设置地址详情
		tvCustomUserAddr.setText(loginBean.getMerchantInfo().getMerchantAddress());
	}

	@Override
	public void onCreat() {
		getNetWorkDateThread();
		leftBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 按钮两边文本颜色交换
				rightBtn.setTextColor(mActivity.getResources().getColor(R.color.xfjr_red));
				leftBtn.setTextColor(Color.WHITE);
				// 数据不为空显示折线图
				if (mUserInfoData != null) {
					showBrokenLine(mUserInfoData.getLastYear());
				}

				setData(mTypeDataBean != null ? mTypeDataBean.getLastYear() : null);

				// 后续删除
				if (!XfjrMain.isNet) {
					showBrokenLine(null);
				}
			}
		});

		rightBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				leftBtn.setTextColor(mActivity.getResources().getColor(R.color.xfjr_red));
				rightBtn.setTextColor(Color.WHITE);
				// 为空点击不显示
				if (mUserInfoData != null) {
					showBrokenLine(mUserInfoData.getThisYear());
				}

				setData(mTypeDataBean != null ? mTypeDataBean.getThisYear() : null);

				// 显示无网数据
				if (!XfjrMain.isNet) {
					showBrokenLine(null);
				}
			}
		});
	}

	@Override
	public void onResume() {
		initData(false);
	}
	
	private void getNetWorkDateThread() {

		new Thread(new Runnable() {

			@Override
			public void run() {
				final String netWorkDateString = XFJRUtil.getNetWorkDateString("yyyy-MM-dd HH:mm:ss");
				mActivity.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						String timeStr =netWorkDateString.split("-")[0];
						int time = Integer.valueOf(timeStr);
						leftBtn.setText((time - 1) + "");
						rightBtn.setText(timeStr);
					}
				});
			}
		}).start();
	}

	@Override
	public void onPause() {

	}

	@Override
	public void onDestory() {
		ArgumentUtil.get().unRegister(this);
	}

	/**
	 * 请求网络数据
	 * 
	 * @param isShowDialog
	 *            是否需要dialog
	 */
	private void initData(final boolean isShowDialog) {
		getNetWorkDateThread();
		/**
		 * 代表商户
		 */
		HttpRequest.reqUserInfo(mActivity, new UserInfoBeanCallback() {
			@Override
			public void onSuccess(String url, UserInfoBean result) {
				super.onSuccess(url, result);
				mUserInfoData = result;
//				showBrokenLine(mUserInfoData == null || mUserInfoData.getLastYear() == null ? null
//						: mUserInfoData.getLastYear());
				setTypeData();
			}
		});

		// 无网显示
		if (!XfjrMain.isNet) {
			showBrokenLine(null);
		}
	}

	private TypeDataBean mTypeDataBean;

	/**
	 * 设置 已通过 已拒绝的数据
	 */
	protected void setTypeData() {
		HttpRequest.reqMerchantData(mActivity, new IHttpCallback<TypeDataBean>() {

			@Override
			public void onSuccess(String url, TypeDataBean result) {
				mTypeDataBean = result;
				rbChangeToThisYear.performClick();
			}

			@Override
			public void onError(String url, Throwable e) {
//				LogUtils.e("error " + e);
				UrlConfig.showErrorTips(mActivity, e, true);
			}

			@Override
			public void onFinal(String url) {

			}
		});
	}

	/**
	 * 显示折线图信息
	 */
	private void showBrokenLine(List<YearBean> showYears) {

		if (!XfjrMain.isNet) {
			// 僞造數據
			List<YearBean> broakLineDatas = new ArrayList<>();
			float total;
			float arc;
			for (int i = 1; i < 13; i++) {
				total = (float) (Math.random() * 200 + 5);
				total = (int) (total * 100);
				total = total / 100;
				arc = (float) (total - 2 * Math.random() - 1);
				arc = (int) (arc * 100);
				arc = arc / 100;
				if (arc < 0) {
					arc = 0;
				}
				broakLineDatas.add(new YearBean(i, total, arc));
			}
			// 显示规则
			areachartView
					.setShowRule(new DefaultShowRule(getMeasureY(getMaxValue(broakLineDatas))));
			areachartView.setAdapter(new MerchantBroakViewAdapter(broakLineDatas));
		} else {
			// 处理空日期如果存在没有11个月份的
			if (showYears.size() < 12) {
				handleData(showYears);
			}
			// 显示规则
			areachartView.setShowRule(new DefaultShowRule(getMeasureY(getMaxValue(showYears))));
			// 網絡數據
			areachartView.setAdapter(new MerchantBroakViewAdapter(showYears));
		}

	}

	/**
	 * 获取月份最大的值
	 */
	private int getMaxValue(List<YearBean> broakLineDatas) {

		int max = (int) broakLineDatas.get(0).getTotal();

		for (int i = 1; i < broakLineDatas.size(); i++) {
			if (max < broakLineDatas.get(i).getTotal()) {
				max = (int) broakLineDatas.get(i).getTotal();
			}
		}
		return max;
	}

	/**
	 * 获取月份最大的值
	 */
	private int getMeasureY(int maxValue) {
		if (maxValue == 0) {
			return 15;
		}

		int value = maxValue / 3;

		for (int i = value; i < Integer.MAX_VALUE; i++) {
			if (i % 5 == 0) {
				value = i;
				break;
			}
		}
		return value * 3;
	}

	/**
	 * 处理日期没有数据
	 */
	private void handleData(List<YearBean> showYears) {
		// 用来存储没有的月份
		Set<Integer> months = new HashSet<>();
		for (int j = 1; j < 13; j++) {
			months.add(j);
		}

		for (int i = 0; i < showYears.size(); i++) {
			months.remove(showYears.get(i).getMonth());
		}
		// 添加没有的数据
		for (int month : months) {
			showYears.add(new YearBean(month, 0, 0));
		}

		// 冒泡排序
		for (int i = 0; i < showYears.size(); i++) {
			for (int j = i; j < showYears.size(); j++) {
				if (showYears.get(i).getMonth() > showYears.get(j).getMonth()) {
					int temp = showYears.get(i).getMonth();
					showYears.get(i).setMonth(showYears.get(j).getMonth());
					showYears.get(j).setMonth(temp);
				}
			}
		}

	}

	/**
	 * 明细查询
	 * 
	 * @param view
	 */
	@OnClick(R.id.tvCustomSearch)
	private void customSearch(View view) {
		// 跳转查询页面，为明细查询页面
		// XFJRSearchActivity.goMe(mActivity,
		// XFJRSearchActivity.DETAILED_SEARCH);
		mActivity.startActivity(new Intent(mActivity, XFJRFilterActivity.class));
	}

	/**
	 * 年份左边按钮点击事件 2016
	 */
	@OnClick(R.id.rbChangeToLastYear)
	private void changeToLastYear(View view) {
		// 按钮两边文本颜色交换
		rightBtn.setTextColor(mActivity.getResources().getColor(R.color.xfjr_red));
		leftBtn.setTextColor(Color.WHITE);
		// 数据不为空显示折线图
		if (mUserInfoData != null) {
			showBrokenLine(mUserInfoData.getLastYear());
		}

		setData(mTypeDataBean != null ? mTypeDataBean.getLastYear() : null);

		// 后续删除
		if (!XfjrMain.isNet) {
			showBrokenLine(null);
		}
	}

	/**
	 * 年份右边按钮点击事件 2017
	 */
	@OnClick(R.id.rbChangeToThisYear)
	private void changeToThisYear(View view) {
		leftBtn.setTextColor(mActivity.getResources().getColor(R.color.xfjr_red));
		rightBtn.setTextColor(Color.WHITE);
		// 为空点击不显示
		if (mUserInfoData != null) {
			showBrokenLine(mUserInfoData.getThisYear());
		}

		setData(mTypeDataBean != null ? mTypeDataBean.getThisYear() : null);

		// 显示无网数据
		if (!XfjrMain.isNet) {
			showBrokenLine(null);
		}
	}

	/**
	 * 设置数量
	 * 
	 * @param yearBean
	 */
	private void setData(List<TypeDataBean.YearBean> yearBean) {
		if (yearBean != null) {
			for (TypeDataBean.YearBean bean : yearBean) {
				switch (bean.getType()) {
				case "0":// 处理中
					tvDealing.setText("处理中       " + bean.getNumber());
					tvDealingMoney.setText(bean.getMoney() + "  元");
					break;
				case "1":
					tvHadPass.setText("已通过       " + bean.getNumber());
					tvHadPassMoney.setText(bean.getMoney() + "  元");
					break;
				case "2":
					tvHadRefused.setText("已拒绝       " + bean.getNumber());
					tvHadRefusedMoney.setText(bean.getMoney() + "  元");
					break;
				}
			}
		}
	}

	/**
	 * 退出登录
	 * 
	 * @param view
	 */
	@OnClick(R.id.ivGoLogin2)
	private void signOut(View view) {
		PreferencesUtil.put(XFJRConstant.KEY_IS_LOGIN, false);
		XfjrMain.startXFJR(mActivity);
		mActivity.finish();
		HttpRequest.signOutLogin(mActivity);
	}

	/**
	 * 关闭页面
	 * 
	 * @param view
	 */
	@OnClick(R.id.ivFinish2)
	private void clickFinish(View view) {
//		XfjrMain.clearAllSP();
		PreferencesUtil.put(XFJRConstant.KEY_IS_LOGIN, false);
		mActivity.finish();
	}

	/**
	 * 刷新按鈕
	 */
	@OnClick(R.id.iv_custom_refresh)
	private void refresh(View view) {
		// 是否有開關
		if (XfjrMain.isNet) {
			initData(true);
		} else {
			((XfjrIndexActivity) mActivity).swichView(false);
		}
	}
}
