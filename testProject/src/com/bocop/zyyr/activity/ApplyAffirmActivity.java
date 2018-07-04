package com.bocop.zyyr.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.config.BocSdkConfig;
import com.bocop.jxplatform.util.CspUtil;
import com.bocop.jxplatform.util.CspUtil.CallBack;
import com.bocop.jxplatform.util.LoginUtil;
import com.bocop.xms.bean.ConstHead;
import com.bocop.xms.utils.XStreamUtils;
import com.bocop.yfx.utils.ToastUtils;
import com.bocop.zyyr.bean.StatusResponse;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.RequestBody;
import com.thoughtworks.xstream.io.StreamException;

/**
 * 
 * 申请贷款02
 * 
 * @author lh
 * 
 */

@ContentView(R.layout.zyyr_activity_apply_affirm)
public class ApplyAffirmActivity extends BaseActivity {

	@ViewInject(R.id.tv_titleName)
	private TextView tvTitle;
	@ViewInject(R.id.tvLoanSum)
	private TextView tvLoanSum;
	@ViewInject(R.id.tvDeadLine)
	private TextView tvDeadLine;
	@ViewInject(R.id.tvLoan)
	private TextView tvLoan;
	@ViewInject(R.id.tvInterest)
	private TextView tvInterest;
	@ViewInject(R.id.tvRefundPM)
	private TextView tvRefundPM;

	private String proId;
	private String appAmt;
	private String appPeriod;
	private String userCity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		tvTitle.setText("申请贷款");

		if (null != getIntent().getExtras()) {
			Bundle bundle = getIntent().getExtras();
			proId = bundle.getString("PRO_ID");
			appAmt = (Double.parseDouble(bundle.getString("APP_AMT")) * 10000) + "";
			appPeriod = bundle.getString("APP_PERIOD");
			userCity = bundle.getString("USER_CITY");

			tvLoanSum.setText(appAmt);
			tvDeadLine.setText(appPeriod);
			tvLoan.setText(bundle.getString("LOAN"));
			tvInterest.setText(bundle.getString("TOTAL_INTEREST"));
			tvRefundPM.setText(bundle.getString("REFUND_PM"));
		}
	}

	@OnClick({ R.id.btnAffirm })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnAffirm:
			requestApplyLoan();
			break;

		}
	}

	/**
	 * 申请贷款
	 */
	private void requestApplyLoan() {
		RequestBody formBody = new FormEncodingBuilder()
				.add("userId", LoginUtil.getUserId(this))
				.add("pdtId", proId)
				.add("appAmt", (Double.parseDouble(appAmt) / 10000) + "")
				.add("appPeriod", appPeriod.replaceAll("期", ""))
				.add("userCity", userCity).build();
		CspUtil cspUtil = new CspUtil(this);
		cspUtil.postCspNoLogin(BocSdkConfig.ZYYR_APPLY_LOAN, formBody, true, new CallBack() {
			@Override
			public void onSuccess(String responStr) {
				onApplySuccess(responStr);
			}

			@Override
			public void onFinish() {

			}

			@Override
			public void onFailure(String responStr) {
				CspUtil.onFailure(ApplyAffirmActivity.this, responStr);
			}
		});
	}

	/**
	 * 申请贷款成功后
	 * 
	 * @param responStr
	 */
	private void onApplySuccess(String responStr) {
		try {
			StatusResponse statusResponse = XStreamUtils.getFromXML(responStr, StatusResponse.class);
			ConstHead constHead = statusResponse.getConstHead();
			if (null != constHead && "00".equals(constHead.getErrCode())) {
				getActivityManager().finishAllWithoutActivity(FinanceMainActivity.class);
			} else {
				Toast.makeText(this, R.string.applyFailure, Toast.LENGTH_SHORT).show();
			}

		} catch (StreamException e) {
			ToastUtils.showError(this, "后台数据异常", Toast.LENGTH_SHORT);
		}
	}

}
