package com.bocop.xfjr.activity.customer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.boc.jx.tools.LogUtils;
import com.bocop.jxplatform.R;
import com.bocop.xfjr.adapter.BusinessDetailIndicatorAcapter;
import com.bocop.xfjr.adapter.MyBusinessDetailPagerAdapter;
import com.bocop.xfjr.base.XfjrBaseActivity;
import com.bocop.xfjr.bean.MyBusinessBean;
import com.bocop.xfjr.config.FragmentConfig;
import com.bocop.xfjr.fragment.BusinessDetailFragment0;
import com.bocop.xfjr.observer.SwichFragmentObserver;
import com.bocop.xfjr.observer.SwichFragmentSubject;
import com.bocop.xfjr.view.NoScrollViewPager;
import com.bocop.xfjr.view.indicator.TrackIndicatorView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

/**
 * description： 业务详情
 * <p/>
 * Version：1.0
 */
@ContentView(R.layout.xfjr_activity_business_detail)
public class XFJRBusinessDetailActivity extends XfjrBaseActivity implements SwichFragmentObserver {

	@ViewInject(R.id.tvHomePage)
	private TextView tvHomePage;
	@ViewInject(R.id.tvTitle)
	private TextView tvTitle;
	@ViewInject(R.id.tvReset)
	private TextView tvReset;
	@ViewInject(R.id.tvSave)
	private TextView tvSave;
	@ViewInject(R.id.tvStatus)
	private TextView tvStatus;
	@ViewInject(R.id.tvCustomerName)
	private TextView tvCustomerName;
	@ViewInject(R.id.indicatorView)
	private TrackIndicatorView indicatorView;
	@ViewInject(R.id.vpMyApplicationDetail)
	private NoScrollViewPager vpMyApplicationDetail;
	private BusinessDetailIndicatorAcapter bAcapter;
	private MyBusinessDetailPagerAdapter mDetailPagerAdapter;
	private MyBusinessBean bean;
	// 当前fragment
	private int mCurrentFragment;
	private List<String> mTabNames = new ArrayList<>();
	private Map<Integer, Fragment> mFragments = new HashMap<>();;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		String status = intent.getStringExtra("status");
		Bundle bundle = intent.getBundleExtra("ApplicationBeanBundle");
		bean = (MyBusinessBean) bundle.getSerializable("ApplicationBean");
		tvCustomerName.setText(bean.getCustomerName());
		initData(status);
	}
	
	public void setStatus(String status){
		tvStatus.setText(status);
	}

	private String getStatusString(String status) {
		switch (status) {
		case "0": // 待预审，没有预审金额，继续申请，更多详情99+
			tvStatus.setTextColor(getResources().getColor(R.color.xfjr_gray));
			bAcapter.getItemView().get(2).setEnabled(false);
			TextView tvText2 = (TextView) bAcapter.getItemView().get(2).findViewById(R.id.tvStepName);
			tvText2.setTextColor(getResources().getColor(R.color.xfjr_gray));
			bAcapter.getItemView().get(3).setEnabled(false);
			TextView tvText3 = (TextView) bAcapter.getItemView().get(3).findViewById(R.id.tvStepName);
			tvText3.setTextColor(getResources().getColor(R.color.xfjr_gray));
			return "待提交"; // #999999 灰色
		case "1": // 没有预审金额，补充资料，更多详情10
			tvStatus.setTextColor(getResources().getColor(R.color.xfjr_gray));
			return "转人工"; // #999999 灰色
		case "2": // 待审批，有预审金额，更多详情8
			tvStatus.setTextColor(getResources().getColor(R.color.xfjr_gray));
			bAcapter.getItemView().get(3).setEnabled(false);
			tvText3 = (TextView) bAcapter.getItemView().get(3).findViewById(R.id.tvStepName);
			tvText3.setTextColor(getResources().getColor(R.color.xfjr_gray));
			return "待落实"; // #999999 灰色
		case "3": // 待放款，有预审金额，补充资料，更多详情10
			tvStatus.setTextColor(getResources().getColor(R.color.xfjr_gray));
			return "待放款"; // #999999 灰色
		case "4": // 已放款，有预审金额，更多详情12
			tvStatus.setTextColor(getResources().getColor(R.color.status_pass));
			return "已放款"; // #999999 绿色
		case "5": // 已拒绝，没有有预审金额，更多详情3
			tvStatus.setTextColor(getResources().getColor(R.color.xfjr_red));
			return "已拒绝"; // #999999 红色
		}
		return "未知状态";
	}

	private void hideResetAndSave() {
		tvReset.setVisibility(View.GONE);
		tvSave.setVisibility(View.GONE);
	}

	// 上一页
	@OnClick(R.id.tvHomePage)
	private void clickHomePage(View view) {
		finish();
	}

	private void initData(String status) {
		mTabNames.add(getString(R.string.basic_info));
		mTabNames.add(getString(R.string.fraud_detection_info));
		mTabNames.add(getString(R.string.instalment_pre_trial_info));
		mTabNames.add(getString(R.string.further_info));
		hideResetAndSave();
		putFragment();
		bAcapter = new BusinessDetailIndicatorAcapter(mTabNames, this);
		indicatorView.setAdapter(vpMyApplicationDetail, false, bAcapter);
		mDetailPagerAdapter = new MyBusinessDetailPagerAdapter(getSupportFragmentManager(), mFragments);
		tvHomePage.setText("上一页");
		tvTitle.setText("业务详情");
		vpMyApplicationDetail.setAdapter(mDetailPagerAdapter);
		indicatorView.setHightLightPosition(0);
		vpMyApplicationDetail.setNoScroll(true); // 禁止滑动
		tvStatus.setText(getStatusString(status));
		vpMyApplicationDetail.setOffscreenPageLimit(3);
	}

	private void putFragment() {
		SwichFragmentSubject fragment = BusinessDetailFragment0.getInstance(bean);
		fragment.register(this);
		mFragments.put(FragmentConfig.USER_INFOF_FRAGMENT, (Fragment) fragment);
//		fragment = BusinessDetailFragment1.getInstance(bean);
//		fragment.register(this);
//		mFragments.put(FragmentConfig.DECEIT_CHECKF_FRAGMENT, (Fragment) fragment);
//		fragment = BusinessDetailFragment2.getInstance(bean.getStatus());
//		fragment.register(this);
//		mFragments.put(FragmentConfig.PREJUDICATION_FRAGMENT, (Fragment) fragment);
//		fragment = BusinessDetailFragment3.getInstance(bean.getStatus());
//		fragment.register(this);
//		mFragments.put(FragmentConfig.ADD_DATA_FRAGMENT, (Fragment) fragment);
	}

	/**
	 * 添加fragment
	 */
	private void addFragment(Fragment fragment) {
		SwichFragmentSubject subject = (SwichFragmentSubject) fragment;
		subject.register(this);
	}

	@Override
	public void onSwichChange(int position) {
		switch (position) {
		case FragmentConfig.USER_INFOF_FRAGMENT:
			addFragment(mFragments.get(FragmentConfig.USER_INFOF_FRAGMENT));
			break;
//		case FragmentConfig.DECEIT_CHECKF_FRAGMENT:
//			addFragment(mFragments.get(FragmentConfig.DECEIT_CHECKF_FRAGMENT));
//			break;
//		case FragmentConfig.PREJUDICATION_FRAGMENT:
//			addFragment(mFragments.get(FragmentConfig.PREJUDICATION_FRAGMENT));
//			break;
//		case FragmentConfig.ADD_DATA_FRAGMENT:
//			addFragment(mFragments.get(FragmentConfig.ADD_DATA_FRAGMENT));
//			break;
		}
		if (position > FragmentConfig.BACK) {
			// stepView.setFocusPosition(position);
		} else {
			// TODO 返回事件
			finish();
		}
		mCurrentFragment = position;
		LogUtils.e("position = " + position);
	}

	@Override
	public void onBackPressed() {
		// super.onBackPressed();
		mCurrentFragment--;
		onSwichChange(mCurrentFragment);
	}

}
