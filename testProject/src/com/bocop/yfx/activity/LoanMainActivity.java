package com.bocop.yfx.activity;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.bocop.jxplatform.R;
import com.bocop.yfx.base.EShareBaseActivity;
import com.bocop.yfx.fragment.LoanProDetailFragment;
import com.bocop.yfx.fragment.MyLoanFragment;
import com.bocop.yfx.fragment.UnreadMessageFragment;

/**
 * 我的贷款主界面
 * 
 * @author lh
 * 
 */
@ContentView(R.layout.yfx_activity_loan_main)
public class LoanMainActivity extends EShareBaseActivity
		implements android.widget.CompoundButton.OnCheckedChangeListener {

	@ViewInject(R.id.tv_titleName)
	private TextView tvTitle;
	@ViewInject(R.id.llFragment)
	private LinearLayout llFragment;
	@ViewInject(R.id.group)
	private RadioGroup group;
	@ViewInject(R.id.radioProDetail)
	public RadioButton radioProDetail;
	@ViewInject(R.id.radioMyLoan)
	public RadioButton radioMyLoan;
	@ViewInject(R.id.radioMessage)
	private RadioButton radioMessage;

	public String status;
	public static boolean REFUND_FLAG = false;
	public static int PRO_FLAG = 0;
	private String title = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initListener();
		radioProDetail.setChecked(true);
		initTitle();
	}
	
	private void initTitle(){
		PRO_FLAG = getIntent().getExtras().getInt("PRO_FLAG");
		switch (PRO_FLAG) {
		case 0:
			title = "中银E贷·关爱专属";
			break;
		case 1:
			title = "扶贫通";
			break;
		case 2:
			title = "扶农通";
			break;
		case 3:
			title = "公积贷";
			break;
		case 8:
			title = "个人扶贫";
			break;
		case 9:
			title = "旅游扶贫";
			break;
		}
		tvTitle.setText(title);
	}

	private void initListener() {
		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.radioProDetail:
//					tvTitle.setText(title);
					replaceFragment(R.id.llFragment, LoanProDetailFragment.class);
					break;
				case R.id.radioMyLoan:
//					tvTitle.setText("我的贷款");
					replaceFragment(R.id.llFragment, MyLoanFragment.class);
					break;
				case R.id.radioMessage:
//					tvTitle.setText("未读消息");
					replaceFragment(R.id.llFragment, UnreadMessageFragment.class);
					break;

				}
			}
		});

		radioProDetail.setOnCheckedChangeListener(this);
		radioMyLoan.setOnCheckedChangeListener(this);
		radioMessage.setOnCheckedChangeListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (REFUND_FLAG) {//成功还款后，回到我的贷款页面
			radioMyLoan.setChecked(true);
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			buttonView.setTextColor(getResources().getColor(R.color.white));
		} else {
			buttonView.setTextColor(getResources().getColor(R.color.colorContentText));
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		REFUND_FLAG = false;
	}

}
