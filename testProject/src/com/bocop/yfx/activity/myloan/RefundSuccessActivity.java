package com.bocop.yfx.activity.myloan;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.bocop.jxplatform.R;
import com.bocop.yfx.activity.LoanMainActivity;
import com.bocop.yfx.fragment.UseLoanFragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

@ContentView(R.layout.yfx_activity_pick_up_affirm)
public class RefundSuccessActivity extends BaseActivity {

	@ViewInject(R.id.tv_titleName)
	private TextView tvTitle;
	// @ViewInject(R.id.tvStatus)
	// private TextView tvStatus;
	// @ViewInject(R.id.tvStatusIntro)
	// private TextView tvStatusIntro;

	/** PickUpAffirm */
	@ViewInject(R.id.iv_imageLeft)
	private ImageView ivLeft;
	@ViewInject(R.id.tvContent)
	private TextView tvContent;
	@ViewInject(R.id.iv1)
	private ImageView iv1;

	private String ERR_CODE;
	private String ERR_MSG;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initData();
	}

	private void initData() {
		tvTitle.setText("提前还款");
		if (null != getIntent().getExtras()) {
			ERR_CODE = getIntent().getStringExtra("ERR_CODE");
			ERR_MSG = getIntent().getStringExtra("ERR_MSG");
		}
		initView();
	}

	private void initView() {

		if ("00".equals(ERR_CODE)) {
			iv1.setImageResource(R.drawable.yfx_gxd_ok);
		} else {
			iv1.setImageResource(R.drawable.icon_toast_failed);
		}
		tvContent.setText(ERR_MSG);
		// if ("".equals(ERR_CODE)) {
		// tvStatus.setText("还款金额低于应还利息");
		// tvStatusIntro.setText("您的还款金额低于应还利息，请修改并重新还款。");
		// } else if ("".equals(ERR_CODE)) {
		// tvStatus.setText("部分贷款存在逾期未还");
		// tvStatusIntro.setText("您尚有贷款逾期未还，请确认并先行归还该笔贷款。");
		//
		// } else if ("".equals(ERR_CODE)) {
		// tvStatus.setText("放款当天进行还款操作");
		// tvStatusIntro.setText("抱歉，系统尚不支持在提款当天进行还款。");
		//
		// } else if ("1".equals(ERR_CODE)) {
		// tvStatus.setText("系统账务处理未能成功");
		// tvStatusIntro.setText("抱歉，因系统忙碌，您的还款尚未处理，请您在5分钟后再次重试。如仍为成功，请您及时联系中行客服热线95566。");
		//
		// }
	}

	@OnClick({ R.id.iv_imageLeft, R.id.btnBack })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_imageLeft:
		case R.id.btnBack:
			gotoLoanMainActivity();
			break;
		}
	}
	
	@Override
	public void onBackPressed() {
		gotoLoanMainActivity();
	}
	
	/**
	 * 回到贷款主页面
	 */
	private void gotoLoanMainActivity() {
		LoanMainActivity.REFUND_FLAG = true;
		UseLoanFragment.LOAN_CHANGE_FLAG = true;
		getActivityManager().finishAllWithoutActivity(LoanMainActivity.class);
	}
}
