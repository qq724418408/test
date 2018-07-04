package com.bocop.zyyr.activity;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.boc.jx.tools.DialogUtil;
import com.boc.jx.view.LoadingView;
import com.boc.jx.view.LoadingView.OnRetryListener;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.config.BocSdkConfig;
import com.bocop.jxplatform.util.CspUtil;
import com.bocop.jxplatform.util.CspUtil.CallBack;
import com.bocop.xms.bean.ConstHead;
import com.bocop.xms.utils.XStreamUtils;
import com.bocop.yfx.utils.DataFormatUtil;
import com.bocop.yfx.utils.ToastUtils;
import com.bocop.zyyr.bean.LoanDetails;
import com.bocop.zyyr.bean.LoanDetailsResponse;
import com.bocop.zyyr.bean.StatusResponse;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.RequestBody;
import com.thoughtworks.xstream.io.StreamException;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * 贷款详情
 * 
 * @author lh
 * 
 */
@ContentView(R.layout.zyyr_activity_loan_details)
public class LoanDetailsActivity extends BaseActivity {

	@ViewInject(R.id.tv_titleName)
	private TextView tvTitle;
	@ViewInject(R.id.tvLoanName)
	private TextView tvLoanName;
	@ViewInject(R.id.tvLoanSum)
	private TextView tvLoanSum;
	@ViewInject(R.id.tvLoanTime)
	private TextView tvLoanTime;
	@ViewInject(R.id.tvLoanDeadLine)
	private TextView tvLoanDeadLine;
	@ViewInject(R.id.tvLoanInterest)
	private TextView tvLoanInterest;
	@ViewInject(R.id.tvRefundPM)
	private TextView tvRefundPM;
	@ViewInject(R.id.tvLeftTextTitle)
	private TextView tvLeftTextTitle;
	@ViewInject(R.id.tvCenterTextTitle)
	private TextView tvCenterTextTitle;
	@ViewInject(R.id.tvRightTextTitle)
	private TextView tvRightTextTitle;
	@ViewInject(R.id.loadingView)
	private LoadingView loadingView;
	@ViewInject(R.id.lLayout)
	private LinearLayout lLayout;
	@ViewInject(R.id.btnDelete)
	private Button btnDelete;

	private LoanDetails details;
	private String appId;
	private boolean FLAG_STATUS = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		tvTitle.setText("贷款详情");

		if (null != getIntent().getExtras()) {
			String status = getIntent().getExtras().getString("STATUS");
			if ("1".equals(status)) {
				FLAG_STATUS = true;
			}
			if (FLAG_STATUS) {
				btnDelete.setVisibility(View.VISIBLE);
			}

			appId = getIntent().getExtras().getString("APP_ID");
			if (!TextUtils.isEmpty(appId)) {
				requestLoanDetails();
			}
		}

	}

	private void initDate() {
		tvLoanName.setText(details.getProName().replaceAll(" ", ""));
		tvLoanSum.setText(DataFormatUtil
				.moneyStringFormat(Double.parseDouble(details.getAppAmount().replaceAll(" ", "")) / 10000 + ""));
		StringBuffer buffer = new StringBuffer(details.getAppDate().replaceAll(" ", ""));
		buffer.insert(4, "-");
		buffer.insert(7, "-");
		tvLoanTime.setText(buffer.toString());
		tvLoanDeadLine.setText(details.getAppPeriod().replaceAll(" ", "") + "期");
		tvLoanInterest.setText(DataFormatUtil.moneyStringFormat(details.getAppInterest().replaceAll(" ", "")) + "元");
		tvRefundPM.setText(DataFormatUtil.moneyStringFormat(details.getPayPM().replaceAll(" ", "")) + "元");
		tvLeftTextTitle.setText(details.getAppPeriod().replaceAll(" ", "") + "期");
		tvCenterTextTitle.setText(DataFormatUtil
				.moneyStringFormat(Double.parseDouble(details.getAppAmount().replaceAll(" ", "")) / 10000 + "") + "万");
		// tvRightTextTitle.setText(DataFormatUtil.moneyStringFormat(details.getPayPM().replaceAll("
		// ", "")) + "元");
		tvRightTextTitle.setText(
				DataFormatUtil.moneyStringFormat(Double.parseDouble(details.getAppInterest().replaceAll(" ", ""))
						/ Double.parseDouble(details.getAppPeriod().replaceAll(" ", ""))+"") + "元");

	}

	@OnClick({ R.id.btnDelete })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnDelete:
			DialogUtil.showWithTwoBtn(this, "确定要取消申请吗？", "确定", "取消", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					requestDeleteLoan();
				}
			}, new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			break;
		}
	}

	/**
	 * 请求贷款详情
	 * 
	 * @param appId
	 *            申请流水号
	 */
	private void requestLoanDetails() {
		RequestBody formBody = new FormEncodingBuilder().add("appId", appId).build();
		CspUtil cspUtil = new CspUtil(this);
		cspUtil.postCspNoLogin(BocSdkConfig.ZYYR_LOAN_DETAILS, formBody, true, new CallBack() {
			@Override
			public void onSuccess(String responStr) {
				onGetInfoSuccess(responStr);
			}

			@Override
			public void onFinish() {

			}

			@Override
			public void onFailure(String responStr) {
				loadingView.setVisibility(View.VISIBLE);
				lLayout.setVisibility(View.GONE);
				loadingView.setmOnRetryListener(new OnRetryListener() {

					@Override
					public void retry() {
						requestLoanDetails();
					}
				});
			}
		});
	}

	/**
	 * 请求详情成功后
	 * 
	 * @param responStr
	 */
	private void onGetInfoSuccess(String responStr) {
		try {
			loadingView.setVisibility(View.GONE);
			lLayout.setVisibility(View.VISIBLE);
			LoanDetailsResponse detailsResponse = XStreamUtils.getFromXML(responStr, LoanDetailsResponse.class);
			ConstHead constHead = detailsResponse.getConstHead();
			if (null != constHead && "00".equals(constHead.getErrCode())) {
				details = detailsResponse.getDetailsExtern().getDetails();
				initDate();
			} else {
				loadingView.setVisibility(View.VISIBLE);
				lLayout.setVisibility(View.GONE);
				loadingView.setmOnRetryListener(new OnRetryListener() {

					@Override
					public void retry() {
						requestLoanDetails();
					}
				});
			}
		} catch (StreamException e) {
			loadingView.setVisibility(View.VISIBLE);
			lLayout.setVisibility(View.GONE);
			loadingView.setmOnRetryListener(new OnRetryListener() {

				@Override
				public void retry() {
					requestLoanDetails();
				}
			});
			ToastUtils.showError(this, "后台数据异常", Toast.LENGTH_SHORT);
		}
	}

	/**
	 * 取消申请
	 *
	 * @param appId
	 *            申请流水号
	 */
	private void requestDeleteLoan() {
		RequestBody formBody = new FormEncodingBuilder().add("appId", appId).build();
		CspUtil cspUtil = new CspUtil(this);
		cspUtil.postCspNoLogin(BocSdkConfig.ZYYR_DELETE_LOAN, formBody, true, new CallBack() {
			@Override
			public void onSuccess(String responStr) {
				try {
					StatusResponse statusResponse = XStreamUtils.getFromXML(responStr, StatusResponse.class);
					ConstHead constHead = statusResponse.getConstHead();
					if (null != constHead && "00".equals(constHead.getErrCode())) {
						MyLoanActivity.DELETE_FLAG = true;
						finish();
					} else {
						Toast.makeText(LoanDetailsActivity.this, R.string.applyFailure, Toast.LENGTH_SHORT).show();
					}
				} catch (StreamException e) {
					ToastUtils.showError(LoanDetailsActivity.this, "后台数据异常", Toast.LENGTH_SHORT);
				}
			}

			@Override
			public void onFinish() {

			}

			@Override
			public void onFailure(String responStr) {
				CspUtil.onFailure(LoanDetailsActivity.this, responStr);
			}
		});
	}
}
