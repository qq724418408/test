package com.bocop.xfjr.activity.customer;

import java.util.ArrayList;
import java.util.List;

import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.bocop.jxplatform.R;
import com.bocop.xfjr.XfjrMain;
import com.bocop.xfjr.adapter.BusinessIndicatorAcapter;
import com.bocop.xfjr.adapter.MyBusinessPagerAdapter;
import com.bocop.xfjr.argument.ArgumentUtil;
import com.bocop.xfjr.argument.Subscribe;
import com.bocop.xfjr.base.XfjrBaseActivity;
import com.bocop.xfjr.bean.TypeBean;
import com.bocop.xfjr.fragment.customer.MyBusinessFragment0;
import com.bocop.xfjr.fragment.customer.MyBusinessFragment1;
import com.bocop.xfjr.fragment.customer.MyBusinessFragment2;
import com.bocop.xfjr.fragment.customer.MyBusinessFragment3;
import com.bocop.xfjr.fragment.customer.MyBusinessFragment4;
import com.bocop.xfjr.fragment.customer.MyBusinessFragment5;
import com.bocop.xfjr.view.NoScrollViewPager;
import com.bocop.xfjr.view.indicator.TrackIndicatorView;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

/**
 * 我的业务 待预审0、待决策1、待审批2、待放款3、已放款4、已拒绝5
 * @author wujunliu
 *
 */
@ContentView(R.layout.xfjr_activity_customer_application)
public class XFJRMyBusinessActivity extends XfjrBaseActivity {

	@ViewInject(R.id.tvHomePage)
	private TextView tvHomePage;
	@ViewInject(R.id.tvTitle)
	private TextView tvTitle;
	@ViewInject(R.id.tvReset)
	private TextView tvReset;
	@ViewInject(R.id.tvSave)
	private TextView tvSave;
	@ViewInject(R.id.vpApplication)
	private ViewPager vpApplication;
	@ViewInject(R.id.indicatorView)
	private TrackIndicatorView indicatorView;
	@ViewInject(R.id.vpMyApplication)
	private NoScrollViewPager vpMyApplication;
	private BusinessIndicatorAcapter businessIndicatorAcapter;
	private MyBusinessPagerAdapter applicationPagerAdapter;
	private List<TypeBean> mTabNames = new ArrayList<>();
	private List<Fragment> fragments = new ArrayList<>();
	private List<Integer> resSelectId = new ArrayList<>();
	private List<Integer> resDefaultId = new ArrayList<>();
	private int status;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ArgumentUtil.get().register(this);
		initView();
		initData();
	}

	protected void initView() {
		hideResetAndSave();
	}

	protected void initData() {
		tvTitle.setText(R.string.my_business);
		status = getIntent().getIntExtra("status", 0);
		bindIndicator(status, true);
	}

	@Subscribe
	public void refreshState(int stat){
		status = stat;
		bindIndicator(status, false);
	}
	
	public void bindIndicator(int status, boolean isFirst) {
		mTabNames.clear();
		fragments.clear();
		fragments.add(MyBusinessFragment0.getInstance());
		fragments.add(MyBusinessFragment1.getInstance());
		fragments.add(MyBusinessFragment2.getInstance());
		fragments.add(MyBusinessFragment3.getInstance());
		fragments.add(MyBusinessFragment4.getInstance());
		fragments.add(MyBusinessFragment5.getInstance());
		resSelectId.add(R.drawable.xfjr_ic_pre_trial);
		resDefaultId.add(R.drawable.xfjr_ic_pre_trial_default);
		resSelectId.add(R.drawable.xfjr_ic_decided);
		resDefaultId.add(R.drawable.xfjr_ic_decided_default);
		resSelectId.add(R.drawable.xfjr_ic_pending);
		resDefaultId.add(R.drawable.xfjr_ic_pending_default);
		resSelectId.add(R.drawable.xfjr_ic_lent);
		resDefaultId.add(R.drawable.xfjr_ic_lent_default);
		resSelectId.add(R.drawable.xfjr_ic_hasbeenlent);
		resDefaultId.add(R.drawable.xfjr_ic_hasbeenlent_default);
		resSelectId.add(R.drawable.xfjr_ic_rejected);
		resDefaultId.add(R.drawable.xfjr_ic_rejected_default);
		mTabNames.addAll(XfjrMain.TYPES);
		businessIndicatorAcapter = new BusinessIndicatorAcapter(mTabNames, resSelectId, resDefaultId, this);
		indicatorView.setAdapter(vpMyApplication, false, businessIndicatorAcapter);
		applicationPagerAdapter = new MyBusinessPagerAdapter(getSupportFragmentManager(), mTabNames, fragments);
		vpMyApplication.setAdapter(applicationPagerAdapter);
		vpMyApplication.setNoScroll(true); // 禁止滑动
		vpMyApplication.setOffscreenPageLimit(5);
		vpMyApplication.setCurrentItem(status);
		indicatorView.setHightLightPosition(status);
		if (isFirst) {
		}
	}

	private void hideResetAndSave() {
		tvReset.setVisibility(View.GONE);
		tvSave.setVisibility(View.GONE);
	}

	@OnClick(R.id.tvHomePage)
	private void clickHomePage(View view) {
		finish();
	}
	
	private boolean isFirst = true;
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus && isFirst) {
			isFirst = false;
			indicatorView.setHightLightPosition(status);
		}
	}

	@Override
	public void onDestroy() {
		ArgumentUtil.get().unRegister(this);
		super.onDestroy();
	}
	
}
