package com.bocop.yfx.activity.loanprodetail;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.bocop.jxplatform.R;
import com.bocop.yfx.activity.LoanMainActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * 资料提交结果
 * 
 * @author rd
 * 
 */
@ContentView(R.layout.yfx_activity_info_submit_result)
public class InfoSubmitResultActivity extends BaseActivity {
	@ViewInject(R.id.tv_titleName)
	private TextView tvTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		tvTitle.setText(getString(R.string.submitResult));

	}

	@OnClick({ R.id.btnBackSalaryLoan, R.id.iv_imageLeft })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_imageLeft:
			finish();
			break;
		case R.id.btnBackSalaryLoan:
			getActivityManager().finishAllWithoutActivity(LoanMainActivity.class);
			break;
		}
	}

	@Override
	public void onBackPressed() {
		getActivityManager().finishAllWithoutActivity(LoanMainActivity.class);
		super.onBackPressed();
	}
}
