package com.bocop.zyyr.fragment;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.boc.jx.base.BaseFragment;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.boc.jx.tools.DialogUtil;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.config.BocSdkConfig;
import com.bocop.jxplatform.util.CspUtil;
import com.bocop.jxplatform.util.CspUtil.CallBack;
import com.bocop.jxplatform.util.LoginUtil;
import com.bocop.zyyr.activity.AuthentInfoActivity;
import com.bocop.zyyr.bean.AuthenInfo;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.RequestBody;

/**
 * 个人信息
 * 
 * @author rd
 * 
 */
public class InfoCheckFragment extends BaseFragment {

	@ViewInject(R.id.tvProfIdentity)
	private TextView tvProfIdentity;// 职业身份
	@ViewInject(R.id.tvPhoneNum)
	private TextView tvPhoneNum;// 电话号码
	@ViewInject(R.id.tvCompanyType)
	private TextView tvCompanyType;// 公司类型
	@ViewInject(R.id.tvHouseProp)
	private TextView tvHouseProp;// 房产类型
	@ViewInject(R.id.tvLocalFund)
	private TextView tvLocalFund;// 本地公积金
	@ViewInject(R.id.tvLocalSocInsur)
	private TextView tvLocalSocInsur;// 社保
	@ViewInject(R.id.tvCar)
	private TextView tvCar;// 车
	@ViewInject(R.id.tvWorkYear)
	private TextView tvWorkYear;// 工龄
	@ViewInject(R.id.tvMonthPay)
	private TextView tvMonthPay;// 月薪
	@ViewInject(R.id.tvMonthIncome)
	private TextView tvMonthIncome;// 月现金收入
	@ViewInject(R.id.tvReserFundTime)
	private TextView tvReserFundTime;// 公积金缴存时间
	@ViewInject(R.id.tvSocInsurTime)
	private TextView tvSocInsurTime;// 社保缴纳时间
	@ViewInject(R.id.tvYourCredit)
	private TextView tvYourCredit;// 信用情况
	@ViewInject(R.id.tvHouseVa11lue)
	private TextView tvHouseVa11lue;// 房产估值

	private String hasCar;
	private String hasFund;
	private String hasInsure;
	private AuthenInfo authentInfo = new AuthenInfo();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = initView(R.layout.zyyr_fragment_check_info);
		return view;
	}

	@Override
	protected void initView() {

		super.initView();
		showInfo();// 显示用户填写信息
	}

	/**
	 * 显示用户填写信息
	 */
	private void showInfo() {
		authentInfo = ((AuthentInfoActivity) baseActivity).authenInfos;
		tvProfIdentity.setText(authentInfo.getCareerVal());
		tvPhoneNum.setText(authentInfo.getPhone());
		tvCompanyType.setText(authentInfo.getCorpTpVal());
		tvWorkYear.setText(authentInfo.getWorkingYear());
		tvMonthPay.setText(authentInfo.getSalary());
		tvMonthIncome.setText(authentInfo.getCashIncome());
		tvHouseProp.setText(authentInfo.getHouseTpVal());
		tvHouseVa11lue.setText(authentInfo.getHouseVal());
		tvSocInsurTime.setText(authentInfo.getSecurityPeriod());
		tvReserFundTime.setText(authentInfo.getGjjPeriod());
		tvYourCredit.setText(authentInfo.getCreditStaVal());
		tvCar.setText(authentInfo.getHasCar());
		tvLocalFund.setText(authentInfo.getHasFund());
		tvLocalSocInsur.setText(authentInfo.getHasInsure());
		if (tvCar.equals(getString(R.string.yes))) {
			hasCar = "0";
		} else {
			hasCar = "1";
		}
		if (tvLocalFund.equals(getString(R.string.yes))) {
			hasFund = "0";
		} else {
			hasFund = "1";
		}
		if (tvLocalSocInsur.equals(getString(R.string.yes))) {
			hasInsure = "0";
		} else {
			hasInsure = "1";
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		initView();
	}
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if(!hidden){
			initView();
		}
	}

	@OnClick({ R.id.btnLast, R.id.btnEnsure })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnLast:
			((AuthentInfoActivity)baseActivity).showFragment(1);
			break;
		case R.id.btnEnsure:
			requestAuthenInfoSubmit();// 请求提交认证资料
			break;

		}
	}

	/**
	 * 请求提交认证资料
	 */
	private void requestAuthenInfoSubmit() {
		RequestBody formBody = new FormEncodingBuilder()
				.add("userId", LoginUtil.getUserId(getActivity()))
				.add("career", authentInfo.getCareer())
//				.add("CAREER_VAL", authentInfo.getCareerVal())
				.add("phone", tvPhoneNum.getText().toString())
				.add("corpType", authentInfo.getCorpType())
//				.add("CORP_TYPE_VAL", authentInfo.getCorpTpVal())
				.add("houseTp", authentInfo.getHouseType())
//				.add("HOUSE_TP_VAL", authentInfo.getHouseTpVal())
				.add("hasFund", hasFund).add("hasInsure", hasInsure)
				.add("hasCar", hasCar)
				.add("workingYears", tvWorkYear.getText().toString())
				.add("salary", tvMonthPay.getText().toString())
				.add("cashIncome", tvMonthIncome.getText().toString())
				.add("gjjPeriod", tvReserFundTime.getText().toString())
				.add("securityPeriod", tvSocInsurTime.getText().toString())
				.add("houseVal", tvHouseVa11lue.getText().toString())
				.add("creditStatus", authentInfo.getCreditStatus())
//				.add("CREDIT_STATUS_VAL ", authentInfo.getCreditStaVal())
				.build();
		CspUtil cspUtil = new CspUtil(getActivity());
		cspUtil.postCspNoLogin(BocSdkConfig.ZYYR_MODIFY_USERINFO, formBody,
				true, new CallBack() {
					@Override
					public void onSuccess(String responStr) {
						DialogUtil.showWithOneBtn(getActivity(), "资料提交成功",
								new OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										getActivity().finish();

									}
								});
					}

					@Override
					public void onFinish() {

					}

					@Override
					public void onFailure(String responStr) {
						CspUtil.onFailure(getActivity(), responStr);
					}
				});

	}
}
