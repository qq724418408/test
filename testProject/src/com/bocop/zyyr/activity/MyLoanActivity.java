package com.bocop.zyyr.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.base.BaseFragment;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.bocop.jxplatform.R;
import com.bocop.zyyr.fragment.LoanFragment;

/**
 * 
 * 我的贷款
 * 
 * @author lh
 * 
 */
@ContentView(R.layout.zyyr_activity_my_loan)
public class MyLoanActivity extends BaseActivity {

	@ViewInject(R.id.tv_titleName)
	private TextView tvTitle;
	@ViewInject(R.id.group)
	private RadioGroup group;
	@ViewInject(R.id.radioAll)
	private RadioButton radioAll;
	@ViewInject(R.id.radioApplying)
	private RadioButton radioApplying;
	@ViewInject(R.id.radioAccepted)
	private RadioButton radioAccepted;
	@ViewInject(R.id.radioCanceled)
	private RadioButton radioCanceled;

	private LoanFragment allFragment = new LoanFragment();
	private LoanFragment applyingFragment = new LoanFragment();
	private LoanFragment acceptedFragment = new LoanFragment();
	private LoanFragment canceledFragment = new LoanFragment();
	private List<BaseFragment> fragmentList = new ArrayList<BaseFragment>();
	private FragmentManager fragmentManager;
	/**
	 * 判断是否有删除操作，即列表发生改变
	 */
	public static boolean DELETE_FLAG = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initData();
		initView();
		initListener();
	}

	private void initData() {
		/** 设置不同的标志位 */
		allFragment.setStatus("0");
		applyingFragment.setStatus("1");
		acceptedFragment.setStatus("2");
		canceledFragment.setStatus("3");

		fragmentList.add(allFragment);
		fragmentList.add(applyingFragment);
		fragmentList.add(acceptedFragment);
		fragmentList.add(canceledFragment);
		fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		for (int i = 0; i < fragmentList.size(); i++) {
			transaction.add(R.id.llFragment, fragmentList.get(i), fragmentList
					.get(i).getClass().getSimpleName());
		}
		transaction.commit();
		showFragment(0);
	}

	private void initView() {
		tvTitle.setText("我的贷款");
	}

	private void initListener() {

		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.radioAll:
					showFragment(0);
					break;
				case R.id.radioApplying:
					showFragment(1);
					break;
				case R.id.radioAccepted:
					showFragment(2);
					break;
				case R.id.radioCanceled:
					showFragment(3);
					break;

				}
			}
		});

		radioAll.setOnCheckedChangeListener(new CheckChangeListener());
		radioApplying.setOnCheckedChangeListener(new CheckChangeListener());
		radioAccepted.setOnCheckedChangeListener(new CheckChangeListener());
		radioCanceled.setOnCheckedChangeListener(new CheckChangeListener());
		radioAll.setChecked(true);
	}

	private class CheckChangeListener implements
			CompoundButton.OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (isChecked) {
				buttonView.setTextColor(getResources().getColor(R.color.white));
			} else {
				buttonView.setTextColor(getResources().getColor(R.color.black));
			}
		}
	}

	/**
	 * 显示指定Fragment，并隐藏其他Fragment
	 * 
	 * @param currentIndex
	 */
	public void showFragment(int currentIndex) {
		for (int i = 0; i < fragmentList.size(); i++) {
			BaseFragment fragment = fragmentList.get(i);
			if (!fragment.isHidden()) {
				fragmentManager.beginTransaction().hide(fragment).commit();
			}
		}
		fragmentManager.beginTransaction().show(fragmentList.get(currentIndex))
				.commit();
	}

}
