package com.bocop.xfjr.activity.merchant;

import java.util.ArrayList;
import java.util.List;

import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.bocop.jxplatform.R;
import com.bocop.xfjr.XfjrMain;
import com.bocop.xfjr.adapter.BusinessIndicatorAcapter;
import com.bocop.xfjr.adapter.MyBusinessPagerAdapter;
import com.bocop.xfjr.base.XfjrBaseActivity;
import com.bocop.xfjr.bean.TypeBean;
import com.bocop.xfjr.fragment.merchant.MyBusinessFragment0;
import com.bocop.xfjr.fragment.merchant.MyBusinessFragment1;
import com.bocop.xfjr.fragment.merchant.MyBusinessFragment2;
import com.bocop.xfjr.view.NoScrollViewPager;
import com.bocop.xfjr.view.indicator.TrackIndicatorView;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

/**
 * 我的业务 处理中0、已通过1、已拒绝2
 * 
 * @author wujunliu
 *
 */
@ContentView(R.layout.xfjr_activity_merchant_application)
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
		initView();
		initData();
	}

	protected void initView() {
		hideResetAndSave();
	}

	protected void initData() {
		tvTitle.setText(R.string.my_business);
		status = getIntent().getIntExtra("status", 0);
		bindIndicator(status);
	}

	private void bindIndicator(int status) {
		fragments.add(MyBusinessFragment0.getInstance()); // 
		fragments.add(MyBusinessFragment1.getInstance());
		fragments.add(MyBusinessFragment2.getInstance());
		resSelectId.add(R.drawable.xfjr_ic_pre_trial);
		resDefaultId.add(R.drawable.xfjr_ic_pre_trial_default);
		resSelectId.add(R.drawable.xfjr_ic_hasbeenlent);
		resDefaultId.add(R.drawable.xfjr_ic_hasbeenlent_default);
		resSelectId.add(R.drawable.xfjr_ic_rejected);
		resDefaultId.add(R.drawable.xfjr_ic_rejected_default);
		mTabNames.addAll(XfjrMain.M_TYPES);
		businessIndicatorAcapter = new BusinessIndicatorAcapter(mTabNames, resSelectId, resDefaultId, this);
		indicatorView.setAdapter(vpMyApplication, false, businessIndicatorAcapter);
		applicationPagerAdapter = new MyBusinessPagerAdapter(getSupportFragmentManager(), mTabNames, fragments);
		vpMyApplication.setAdapter(applicationPagerAdapter);
		vpMyApplication.setNoScroll(true); // 禁止滑动
		vpMyApplication.setOffscreenPageLimit(2);
		indicatorView.setHightLightPosition(status);
	}

	private void hideResetAndSave() {
		tvReset.setVisibility(View.GONE);
		tvSave.setVisibility(View.GONE);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@OnClick(R.id.tvHomePage)
	private void clickHomePage(View view) {
		finish();
	}

}
