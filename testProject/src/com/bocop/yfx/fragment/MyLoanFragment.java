package com.bocop.yfx.fragment;

import com.boc.jx.base.BaseFragment;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.bocop.jxplatform.R;
import com.bocop.yfx.activity.LoanMainActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * 
 * 我的贷款
 * 
 * @author lh
 * 
 */
public class MyLoanFragment extends BaseFragment implements android.widget.CompoundButton.OnCheckedChangeListener {

	@ViewInject(R.id.group)
	private RadioGroup group;
	@ViewInject(R.id.radioUseLoan)
	private RadioButton radioUseLoan;
	@ViewInject(R.id.radioReFund)
	private RadioButton radioReFund;
	@ViewInject(R.id.radioRecords)
	private RadioButton radioRecords;
	private String status;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = initView(R.layout.yfx_fragment_myloan);
		return view;
	}

	@Override
	protected void initData() {
		super.initData();
		initListener();
	}

	@Override
	protected void initView() {
		super.initView();
		radioUseLoan.setChecked(true);
		status = ((LoanMainActivity) baseActivity).status;
		if (!"6".equals(status) && !"5".equals(status) && !"3".equals(status)&& !"4".equals(status)) {
			radioReFund.setClickable(false);
			radioRecords.setClickable(false);
		} else {
			radioReFund.setClickable(true);
			radioRecords.setClickable(true);
		}
	}

	private void initListener() {
		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.radioUseLoan:
//					((LoanMainActivity) baseActivity).tvTitle.setText("我的贷款");
					baseActivity.replaceFragment(R.id.rlMyFragment, UseLoanFragment.class);
					break;
				case R.id.radioReFund:
//					((LoanMainActivity) baseActivity).tvTitle.setText("还款");
					baseActivity.replaceFragment(R.id.rlMyFragment, RefundFragment.class);
					break;
				case R.id.radioRecords:
//					((LoanMainActivity) baseActivity).tvTitle.setText("历史记录");
					baseActivity.replaceFragment(R.id.rlMyFragment, RecordsFragment.class);
					break;
				}
			}
		});
		radioUseLoan.setOnCheckedChangeListener(this);
		radioReFund.setOnCheckedChangeListener(this);
		radioRecords.setOnCheckedChangeListener(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		if (LoanMainActivity.REFUND_FLAG) {// 成功还款后，显示余额
			radioUseLoan.setChecked(true);
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			buttonView.setTextColor(baseActivity.getResources().getColor(R.color.white));
		} else {
			buttonView.setTextColor(baseActivity.getResources().getColor(R.color.black));
		}
	}

}
