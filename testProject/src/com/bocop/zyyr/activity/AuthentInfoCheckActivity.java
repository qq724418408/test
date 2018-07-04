package com.bocop.zyyr.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.boc.jx.tools.DialogUtil;
import com.boc.jx.view.LoadingView;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.config.BocSdkConfig;
import com.bocop.jxplatform.util.CspUtil;
import com.bocop.jxplatform.util.CspUtil.CallBack;
import com.bocop.jxplatform.util.LoginUtil;
import com.bocop.xms.bean.ConstHead;
import com.bocop.xms.utils.XStreamUtils;
import com.bocop.yfx.utils.CheckoutUtil;
import com.bocop.yfx.utils.ToastUtils;
import com.bocop.zyyr.bean.AuthenInfo;
import com.bocop.zyyr.bean.KeyAndValue;
import com.bocop.zyyr.bean.ListParaResponse;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.RequestBody;
import com.thoughtworks.xstream.io.StreamException;

/**
 * 认证资料/修改
 * 
 * @author rd
 * 
 */
@ContentView(R.layout.zyyr_activity_authent_info_check)
public class AuthentInfoCheckActivity extends BaseActivity {
	@ViewInject(R.id.tv_titleName)
	private TextView tvTitle;
	@ViewInject(R.id.llSocietySafeTime)
	private LinearLayout llSocietySafeTime;// 缴纳社保时间框
	@ViewInject(R.id.llReserfundTime)
	private LinearLayout llReserfundTime;// 缴存公积金时间框

	@ViewInject(R.id.slShow)
	private ScrollView slShow;
	@ViewInject(R.id.slEdit)
	private ScrollView slEdit;

	// 文本显示框
	@ViewInject(R.id.tvProfIdentity)
	private TextView tvProfIdentity;// 职业身份文本框
	@ViewInject(R.id.tvPhoneNum)
	private TextView tvPhoneNum;// 手机号文本框
	@ViewInject(R.id.tvCompanyType)
	private TextView tvCompanyType;// 就职公司文本框
	@ViewInject(R.id.tvHouseProp)
	private TextView tvHouseProp;// 房产类型文本框
	@ViewInject(R.id.tvLocalFund)
	private TextView tvLocalFund;// 是否有本地基金文本框
	@ViewInject(R.id.tvLocalSocInsur)
	private TextView tvLocalSocInsur;// 是否有社保文本框
	@ViewInject(R.id.tvCar)
	private TextView tvCar;// 是否有车文本框
	@ViewInject(R.id.tvWorkYear)
	private TextView tvWorkYear;// 工龄文本框
	@ViewInject(R.id.tvMonthPay)
	private TextView tvMonthPay;// 月薪文本框
	@ViewInject(R.id.tvMonthIncome)
	private TextView tvMonthIncome;// 月现金收入文本框
	@ViewInject(R.id.tvReserFundTime)
	private TextView tvReserFundTime;// 缴纳本地基金时间文本框
	@ViewInject(R.id.tvSocInsurTime)
	private TextView tvSocInsurTime;// 缴纳社保文本框
	@ViewInject(R.id.tvHouseVa11lue)
	private TextView tvHouseVa11lue;// 房产估值文本框
	@ViewInject(R.id.tvYourCredit)
	private TextView tvYourCredit;// 信用情况文本框
	// 编辑框
	@ViewInject(R.id.tvProfIdentityC)
	private TextView tvProfIdentityC;// 职业身份选择框
	@ViewInject(R.id.etPhoneNum)
	private EditText etPhoneNum;// 手机号编辑框
	@ViewInject(R.id.tvCompanyTypeC)
	private TextView tvCompanyTypeC;// 就职公司类型选择框
	@ViewInject(R.id.tvHousePropC)
	private TextView tvHousePropC;// 名下房产类型选择框
	@ViewInject(R.id.etWorkYear)
	private EditText etWorkYear;// 工龄编辑框
	@ViewInject(R.id.etMonthPay)
	private EditText etMonthPay;// 月薪编辑框
	@ViewInject(R.id.etMonthIncome)
	private EditText etMonthIncome;// 月现金收入编辑框

	@ViewInject(R.id.etFundTime)
	private EditText etFundTime;// 缴纳本地基金时间编辑框

	@ViewInject(R.id.etSocInsurTime)
	private EditText etSocInsurTime;// 缴纳社保时间编辑框
	@ViewInject(R.id.etHouseVa11lue)
	private EditText etHouseVa11lue;// 房产估值
	@ViewInject(R.id.etYourCredit)
	private TextView etYourCredit;// 信用情况
	// 选择框
	@ViewInject(R.id.cbCar)
	private CheckBox cbCar;// 车选择框
	@ViewInject(R.id.cbFunds)
	private CheckBox cbFunds;// 本地基金选择框
	@ViewInject(R.id.cbSocSafe)
	private CheckBox cbSocSafe;// 社保选择框
	@ViewInject(R.id.btnEdit)
	private Button btnEdit;// 编辑按钮
	@ViewInject(R.id.loadingView)
	private LoadingView loadingView;

	@ViewInject(R.id.llsocInsur)
	private LinearLayout llsocInsur;
	@ViewInject(R.id.llFundTime)
	private LinearLayout llFundTime;

	private boolean isEdit = true;
	private AuthenInfo authenInfo;// 认证资料信息
	private String hasCar;
	private String hasFund;
	private String hasInsure;
	private List<KeyAndValue> professionData = new ArrayList<KeyAndValue>();// 职业身份数组
	private List<KeyAndValue> companyData = new ArrayList<KeyAndValue>();// 就职公司数组
	private List<KeyAndValue> houseData = new ArrayList<KeyAndValue>();// 房产数组
	private List<KeyAndValue> creditData = new ArrayList<KeyAndValue>();// 房产数组
	protected String careerKey;
	protected String companyTpKey;
	protected String houseTpKey;
	protected String creditKey;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tvTitle.setText(getString(R.string.authentInfo));
		showInfo();// 显示信息
	}

	/**
	 * 显示信息
	 */
	private void showInfo() {
		authenInfo = (AuthenInfo) getIntent().getSerializableExtra("AuthentInfo");
		tvProfIdentity.setText(authenInfo.getCareerVal());
		tvPhoneNum.setText(authenInfo.getPhone());
		tvCompanyType.setText(authenInfo.getCorpTpVal());
		tvHouseProp.setText(authenInfo.getHouseTpVal());
		tvWorkYear.setText(authenInfo.getWorkingYear());
		tvMonthPay.setText(authenInfo.getSalary());
		tvMonthIncome.setText(authenInfo.getCashIncome());

		if ("0".equals(authenInfo.getHasFund())) {
			tvLocalFund.setText(getString(R.string.no));
		} else {
			tvLocalFund.setText(getString(R.string.yes));
		}
		if ("0".equals(authenInfo.getHasInsure())) {
			tvLocalSocInsur.setText(getString(R.string.no));
		} else {
			tvLocalSocInsur.setText(getString(R.string.yes));
		}
		if ("0".equals(authenInfo.getHasCar())) {
			tvCar.setText(getString(R.string.no));
		} else {
			tvCar.setText(getString(R.string.yes));
		}

		if ("否".equals(authenInfo.getHasInsure())) {
			llsocInsur.setVisibility(View.GONE);
		} else {
			tvSocInsurTime.setText(authenInfo.getSecurityPeriod());
		}
		if ("否".equals(authenInfo.getHasFund())) {
			llFundTime.setVisibility(View.GONE);
		} else {
			tvReserFundTime.setText(authenInfo.getGjjPeriod());
		}
		tvHouseVa11lue.setText(authenInfo.getHouseVal());
		tvYourCredit.setText(authenInfo.getCreditStaVal());
	}

	@OnClick({ R.id.btnEdit, R.id.cbFunds, R.id.cbSocSafe, R.id.cbCar, R.id.tvProfIdentityC, R.id.tvCompanyTypeC,
			R.id.tvHousePropC, R.id.etYourCredit })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnEdit:
			if (isEdit) {
				requestData();
			} else {
				String phoneNum = etPhoneNum.getText().toString();
				String workYear = etWorkYear.getText().toString();
				String monthPay = etMonthPay.getText().toString();
				String monthIncome = etMonthIncome.getText().toString();
				String fundTime = etFundTime.getText().toString();
				String socInsurTime = etSocInsurTime.getText().toString();
				String houseVa11lue = etHouseVa11lue.getText().toString();
				String yourCredit = etYourCredit.getText().toString();
				if (CheckoutUtil.isEmpty(phoneNum, workYear, monthPay, monthIncome, houseVa11lue, yourCredit)) {
					ToastUtils.show(this, getString(R.string.empty), Toast.LENGTH_SHORT);
				} else if (cbFunds.isChecked() && CheckoutUtil.isEmpty(fundTime)) {
					ToastUtils.show(this, getString(R.string.inputGjjTime), Toast.LENGTH_SHORT);
				} else if (cbSocSafe.isChecked() && CheckoutUtil.isEmpty(socInsurTime)) {
					ToastUtils.show(this, getString(R.string.inputInsurTime), Toast.LENGTH_SHORT);
				} else if (CheckoutUtil.isMobileNo(this, phoneNum)) {
					AlertDialog dlg=new AlertDialog.Builder(AuthentInfoCheckActivity.this).setTitle("修改提示").setMessage("是否确认修改")
							.setPositiveButton("确认", new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									requestAuthenInfoSubmit();// 请求提交认证资料
								}
							}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
								}
							})
							.create();
							dlg.setCanceledOnTouchOutside(false);
							dlg.show();
				}
			}
			break;
		case R.id.cbFunds:
			if (cbFunds.isChecked()) {
				cbFunds.setBackgroundResource(R.drawable.zyyr_btn_open);
				llReserfundTime.setVisibility(View.VISIBLE);
				hasFund = "0";
			} else {
				cbFunds.setBackgroundResource(R.drawable.zyyr_btn_close);
				llReserfundTime.setVisibility(View.GONE);
				hasFund = "1";
			}

			break;

		case R.id.cbSocSafe:
			if (cbSocSafe.isChecked()) {
				cbSocSafe.setBackgroundResource(R.drawable.zyyr_btn_open);
				llSocietySafeTime.setVisibility(View.VISIBLE);
				hasInsure = "0";
			} else {
				cbSocSafe.setBackgroundResource(R.drawable.zyyr_btn_close);
				llSocietySafeTime.setVisibility(View.GONE);
				hasInsure = "1";
			}
			break;
		case R.id.cbCar:
			if (cbCar.isChecked()) {
				cbCar.setBackgroundResource(R.drawable.zyyr_btn_open);
				hasCar = "0";
			} else {
				cbCar.setBackgroundResource(R.drawable.zyyr_btn_close);
				hasCar = "1";
			}
			break;
		case R.id.tvProfIdentityC:
			final String[] professions = new String[professionData.size()];
			for (int i = 0; i < professionData.size(); i++) {
				professions[i] = professionData.get(i).getParaValue();
			}
			DialogUtil.showToSelect(this, "", professions, new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					tvProfIdentityC.setText(professions[which]);
					careerKey = professionData.get(which).getParaKey();
				}
			});
			break;
		case R.id.tvCompanyTypeC:
			final String[] companies = new String[companyData.size()];
			for (int i = 0; i < companyData.size(); i++) {
				companies[i] = companyData.get(i).getParaValue();
			}
			DialogUtil.showToSelect(this, "", companies, new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					tvCompanyTypeC.setText(companies[which]);
					companyTpKey = companyData.get(which).getParaKey();
				}
			});
			break;
		case R.id.tvHousePropC:
			final String[] houses = new String[houseData.size()];
			for (int i = 0; i < houseData.size(); i++) {
				houses[i] = houseData.get(i).getParaValue();
			}
			DialogUtil.showToSelect(this, "", houses, new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					tvHousePropC.setText(houses[which]);
					houseTpKey = houseData.get(which).getParaKey();

				}
			});
			break;
		case R.id.etYourCredit:
			final String[] credits = new String[creditData.size()];
			for (int i = 0; i < creditData.size(); i++) {
				credits[i] = creditData.get(i).getParaValue();
			}
			DialogUtil.showToSelect(this, "", credits, new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					etYourCredit.setText(credits[which]);
					creditKey = creditData.get(which).getParaKey();
				}
			});
		}
	}

	/**
	 * 请求提交认证资料
	 */
	private void requestAuthenInfoSubmit() {
		RequestBody formBody = new FormEncodingBuilder().add("userId", LoginUtil.getUserId(this))
				.add("career", careerKey)
				// .add("CAREER_VAL", tvProfIdentityC.getText().toString())
				.add("phone", etPhoneNum.getText().toString()).add("corpType", companyTpKey)
				// .add("CORP_TYPE_VAL", tvCompanyTypeC.getText().toString())
				.add("houseTp", houseTpKey)
				// .add("HOUSE_TP_VAL", tvHousePropC.getText().toString())
				.add("hasFund", hasFund).add("hasInsure", hasInsure).add("hasCar", hasCar)
				.add("workingYears", etWorkYear.getText().toString()).add("salary", etMonthPay.getText().toString())
				.add("cashIncome", etMonthIncome.getText().toString()).add("gjjPeriod", etFundTime.getText().toString())
				.add("securityPeriod", etSocInsurTime.getText().toString())
				.add("houseVal", etHouseVa11lue.getText().toString()).add("creditStatus", creditKey)
				// .add("CREDIT_STATUS_VAL ", tvYourCredit.getText().toString())
				.build();
		CspUtil cspUtil = new CspUtil(this);
		cspUtil.postCspNoLogin(BocSdkConfig.ZYYR_MODIFY_USERINFO, formBody, true, new CallBack() {
			@Override
			public void onSuccess(String responStr) {
				DealInfoUpdateSucess();// 完成认证资料修改
			}

			@Override
			public void onFinish() {

			}

			@Override
			public void onFailure(String responStr) {
				CspUtil.onFailure(AuthentInfoCheckActivity.this, responStr);
			}
		});
	}

	/**
	 * 完成认证资料修改
	 */
	protected void DealInfoUpdateSucess() {

		DialogUtil.showWithOneBtn(this, getString(R.string.submitSuccess), new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		slShow.setVisibility(View.VISIBLE);
		slEdit.setVisibility(View.GONE);
		// 设置编辑框默认值
		tvProfIdentity.setText(tvProfIdentityC.getText().toString());
		tvPhoneNum.setText(etPhoneNum.getText().toString());
		tvCompanyType.setText(tvCompanyTypeC.getText().toString());
		tvHouseProp.setText(tvHousePropC.getText().toString());
		// 处理本地基金、社保、车的选择状态
		if (cbFunds.isChecked()) {
			llFundTime.setVisibility(View.VISIBLE);
			tvLocalFund.setText(getString(R.string.yes));
			tvReserFundTime.setText(etFundTime.getText().toString());
		} else {
			llFundTime.setVisibility(View.GONE);
			tvLocalFund.setText(getString(R.string.no));
		}
		if (cbSocSafe.isChecked()) {
			llsocInsur.setVisibility(View.VISIBLE);
			tvLocalSocInsur.setText(getString(R.string.yes));
			tvSocInsurTime.setText(etSocInsurTime.getText().toString());
		} else {
			llsocInsur.setVisibility(View.GONE);
			tvLocalSocInsur.setText(getString(R.string.no));
		}
		if (cbCar.isChecked()) {
			tvCar.setText(getString(R.string.yes));
		} else {
			tvCar.setText(getString(R.string.no));
		}

		tvWorkYear.setText(etWorkYear.getText().toString());
		tvMonthPay.setText(etMonthPay.getText().toString());
		tvMonthIncome.setText(etMonthIncome.getText().toString());
		tvHouseVa11lue.setText(etHouseVa11lue.getText().toString());
		tvYourCredit.setText(etYourCredit.getText().toString());
		isEdit = true;
		btnEdit.setText(getString(R.string.edit));
	}

	/**
	 * 编辑认证资料
	 */
	private void editInfo() {

		slShow.setVisibility(View.GONE);
		slEdit.setVisibility(View.VISIBLE);

		// 设置编辑框默认值
		tvProfIdentityC.setText(tvProfIdentity.getText().toString());
		etPhoneNum.setText(tvPhoneNum.getText().toString());
		tvCompanyTypeC.setText(tvCompanyType.getText().toString());
		tvHousePropC.setText(tvHouseProp.getText().toString());
		// 处理本地基金、社保、车的选择状态
		if (tvLocalFund.getText().equals(getString(R.string.yes))) {
			cbFunds.setBackgroundResource(R.drawable.zyyr_btn_open);
			cbFunds.setChecked(true);
			llReserfundTime.setVisibility(View.VISIBLE);
			etFundTime.setText(tvReserFundTime.getText().toString());
			hasFund = "0";
		} else {
			cbFunds.setBackgroundResource(R.drawable.zyyr_btn_close);
			llReserfundTime.setVisibility(View.GONE);
			cbFunds.setChecked(false);
			hasFund = "1";
		}
		if (tvLocalSocInsur.getText().equals(getString(R.string.yes))) {
			cbSocSafe.setBackgroundResource(R.drawable.zyyr_btn_open);
			llSocietySafeTime.setVisibility(View.VISIBLE);
			etSocInsurTime.setText(tvSocInsurTime.getText().toString());
			cbSocSafe.setChecked(true);
			hasInsure = "0";
		} else {
			cbSocSafe.setBackgroundResource(R.drawable.zyyr_btn_close);
			llSocietySafeTime.setVisibility(View.GONE);
			hasInsure = "1";
			cbSocSafe.setChecked(false);
		}
		if (tvCar.getText().equals(getString(R.string.yes))) {
			cbCar.setBackgroundResource(R.drawable.zyyr_btn_open);
			hasCar = "0";
			cbCar.setChecked(true);
		} else {
			cbCar.setBackgroundResource(R.drawable.zyyr_btn_close);
			hasCar = "1";
			cbCar.setChecked(false);
		}

		etWorkYear.setText(tvWorkYear.getText().toString());
		etMonthPay.setText(tvMonthPay.getText().toString());
		etMonthIncome.setText(tvMonthIncome.getText().toString());
		etHouseVa11lue.setText(tvHouseVa11lue.getText().toString());
		etYourCredit.setText(tvYourCredit.getText().toString());

	}

	/**
	 * 请求数据
	 */
	private void requestData() {
		RequestBody formBody = new FormEncodingBuilder().build();
		CspUtil cspUtil = new CspUtil(this);
		cspUtil.postCspNoLogin(BocSdkConfig.ZYYR_LIST_PARA, formBody, true, new CallBack() {
			@Override
			public void onSuccess(String responStr) {
				dealParaList(responStr);// 处理返回数据
			}

			@Override
			public void onFinish() {

			}

			@Override
			public void onFailure(String responStr) {
				CspUtil.onFailure(AuthentInfoCheckActivity.this, responStr);
			}
		});

	}

	/**
	 * 处理返回数据
	 * 
	 * @param responStr
	 */
	protected void dealParaList(String responStr) {
		try {
			ListParaResponse listParaResponse = XStreamUtils.getFromXML(responStr, ListParaResponse.class);
			ConstHead constHead = listParaResponse.getConstHead();
			if (null != constHead && "00".equals(constHead.getErrCode())) {
				professionData = listParaResponse.getListPara().getCareer();
				houseData = listParaResponse.getListPara().getHouseTp();
				companyData = listParaResponse.getListPara().getCompTp();
				creditData = listParaResponse.getListPara().getCreditStatus();
				// 选择框显示默认数据
				tvProfIdentity.setText(authenInfo.getCareerVal());
				tvCompanyType.setText(authenInfo.getCorpTpVal());
				tvHouseProp.setText(authenInfo.getHouseTpVal());
				tvYourCredit.setText(authenInfo.getCreditStaVal());
				careerKey = authenInfo.getCareer();
				houseTpKey = authenInfo.getHouseType();
				companyTpKey = authenInfo.getCorpType();
				creditKey = authenInfo.getCreditStatus();

				editInfo();
				btnEdit.setText(getString(R.string.complete));
				isEdit = false;
			} else {
				ToastUtils.show(AuthentInfoCheckActivity.this, getString(R.string.systemError), 0);
			}
		} catch (StreamException e) {
			ToastUtils.showError(this, "后台数据异常", Toast.LENGTH_SHORT);
		}
	}
}
