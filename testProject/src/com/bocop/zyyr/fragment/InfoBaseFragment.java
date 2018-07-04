package com.bocop.zyyr.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.boc.jx.base.BaseFragment;
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
import com.bocop.yfx.utils.CheckoutUtil;
import com.bocop.yfx.utils.ToastUtils;
import com.bocop.zyyr.activity.AuthentInfoActivity;
import com.bocop.zyyr.bean.KeyAndValue;
import com.bocop.zyyr.bean.ListParaResponse;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.RequestBody;
import com.thoughtworks.xstream.io.StreamException;

/**
 * 个人基本信息
 * 
 * @author rd
 * 
 */
public class InfoBaseFragment extends BaseFragment {

	@ViewInject(R.id.tvProfIdentity)
	private TextView tvProfIdentity;// 职业身份
	@ViewInject(R.id.tvCompanyType)
	private TextView tvCompanyType;// 就职公司类型
	@ViewInject(R.id.tvHouseProp)
	private TextView tvHouseProp;// 名下房产类型

	@ViewInject(R.id.etPhoneNum)
	private TextView etPhoneNum;// 手机号
	@ViewInject(R.id.etWorkYear)
	private TextView etWorkYear;// 当前工龄
	@ViewInject(R.id.etMonthPay)
	private TextView etMonthPay;// 月薪
	@ViewInject(R.id.etMonthIncome)
	private TextView etMonthIncome;// 月现金收入
	@ViewInject(R.id.etHouseValue)
	private TextView etHouseValue;// 房产估值

	@ViewInject(R.id.svBaseInfo)
	private ScrollView svBaseInfo;// 个人基本信息界面
	@ViewInject(R.id.btnToInfoCom)
	private Button btnToInfoCom;// 下一步按钮

	private LoadingView loadingView;// 加载界面
	private LinearLayout llhead;
	private List<KeyAndValue> professionData = new ArrayList<KeyAndValue>();// 职业身份数组
	private List<KeyAndValue> companyData = new ArrayList<KeyAndValue>();// 就职公司数组
	private List<KeyAndValue> houseData = new ArrayList<KeyAndValue>();// 房产数组

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = initView(R.layout.zyyr_fragment_base_info);
		return view;
	}

	@Override
	protected void initView() {
		super.initView();
		loadingView = (LoadingView) getActivity().findViewById(R.id.loadingView);
		llhead = (LinearLayout) getActivity().findViewById(R.id.llhead);
		requestData();// 请求数据

	}

	/**
	 * 请求数据
	 */
	private void requestData() {
		RequestBody formBody = new FormEncodingBuilder().build();
		CspUtil cspUtil = new CspUtil(getActivity());
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
				loadingView.setVisibility(View.VISIBLE);
				loadingView.setmOnRetryListener(new OnRetryListener() {

					@Override
					public void retry() {
						requestData();
					}
				});
				CspUtil.onFailure(getActivity(), responStr);
			}
		});
	}

	/**
	 * 处理返回数据
	 * 
	 * @param responStr
	 */
	protected void dealParaList(String responStr) {
		btnToInfoCom.setVisibility(View.VISIBLE);
		svBaseInfo.setVisibility(View.VISIBLE);
		llhead.setVisibility(View.VISIBLE);
		try {
			ListParaResponse listParaResponse = XStreamUtils.getFromXML(responStr, ListParaResponse.class);
			ConstHead constHead = listParaResponse.getConstHead();
			if (null != constHead && "00".equals(constHead.getErrCode())) {
				professionData = listParaResponse.getListPara().getCareer();
				houseData = listParaResponse.getListPara().getHouseTp();
				companyData = listParaResponse.getListPara().getCompTp();
				((AuthentInfoActivity) baseActivity).creditData.clear();
				((AuthentInfoActivity) baseActivity).creditData
						.addAll(listParaResponse.getListPara().getCreditStatus());
				// 选择框显示默认数据
				tvProfIdentity.setText(professionData.get(0).getParaValue());
				tvCompanyType.setText(companyData.get(0).getParaValue());
				tvHouseProp.setText(houseData.get(0).getParaValue());

				((AuthentInfoActivity) baseActivity).authenInfos.setCareer(professionData.get(0).getParaKey());
				((AuthentInfoActivity) baseActivity).authenInfos.setCorpType(companyData.get(0).getParaKey());
				((AuthentInfoActivity) baseActivity).authenInfos.setHouseType(houseData.get(0).getParaKey());
				((AuthentInfoActivity) baseActivity).authenInfos
						.setCreditStatus(listParaResponse.getListPara().getCreditStatus().get(0).getParaKey());

			} else {
				Toast.makeText(getActivity(), getString(R.string.systemError), Toast.LENGTH_SHORT).show();
			}
		} catch (StreamException e) {
			ToastUtils.showError(baseActivity, "后台数据异常", Toast.LENGTH_SHORT);
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
		if (!hidden) {
			initView();
		}
	}

	@OnClick({ R.id.btnToInfoCom, R.id.tvProfIdentity, R.id.tvCompanyType, R.id.tvHouseProp })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnToInfoCom:
			String phoneNum = etPhoneNum.getText().toString();// 手机号
			String workYear = etWorkYear.getText().toString();// 当前工龄
			String monthPay = etMonthPay.getText().toString();// 月薪
			String monthIncome = etMonthIncome.getText().toString();// 月现金收入
			String houseValue = etHouseValue.getText().toString();// 房产估值
			if (CheckoutUtil.isEmpty(phoneNum, workYear, monthPay, monthIncome)) {
				Toast.makeText(getActivity(), getString(R.string.empty), Toast.LENGTH_SHORT).show();
			} else if (CheckoutUtil.isMobileNo(getActivity(), phoneNum)) {
				if (TextUtils.isEmpty(houseValue)) {
					houseValue = "0";
				}
				storeInfo(phoneNum, workYear, monthPay, monthIncome, houseValue);// 保存用户填写信息
				((AuthentInfoActivity) baseActivity).showFragment(1);
			}

			break;
		case R.id.tvProfIdentity:

			final String[] professions = new String[professionData.size()];
			for (int i = 0; i < professionData.size(); i++) {
				professions[i] = professionData.get(i).getParaValue();
			}
			DialogUtil.showToSelect(getActivity(), "", professions, new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					tvProfIdentity.setText(professions[which]);

					((AuthentInfoActivity) baseActivity).authenInfos.setCareer(professionData.get(which).getParaKey());

				}
			});
			break;
		case R.id.tvCompanyType:
			final String[] companies = new String[companyData.size()];
			for (int i = 0; i < companyData.size(); i++) {
				companies[i] = companyData.get(i).getParaValue();
			}
			DialogUtil.showToSelect(getActivity(), "", companies, new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					tvCompanyType.setText(companies[which]);
					((AuthentInfoActivity) baseActivity).authenInfos.setCorpType(companyData.get(which).getParaKey());

				}
			});
			break;
		case R.id.tvHouseProp:
			final String[] houses = new String[houseData.size()];
			for (int i = 0; i < houseData.size(); i++) {
				houses[i] = houseData.get(i).getParaValue();
			}
			DialogUtil.showToSelect(getActivity(), "", houses, new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					tvHouseProp.setText(houses[which]);
					((AuthentInfoActivity) baseActivity).authenInfos.setHouseType(houseData.get(which).getParaKey());

				}
			});
			break;
		}
	}

	/**
	 * 保存用户填写信息
	 * 
	 * @param houseValue
	 * @param monthIncome
	 * @param monthPay
	 * @param workYear
	 * @param phoneNum
	 */
	private void storeInfo(String phoneNum, String workYear, String monthPay, String monthIncome, String houseValue) {
		((AuthentInfoActivity) baseActivity).authenInfos.setCareerVal(tvProfIdentity.getText().toString());
		((AuthentInfoActivity) baseActivity).authenInfos.setPhone(phoneNum);
		((AuthentInfoActivity) baseActivity).authenInfos.setCorpTpVal(tvCompanyType.getText().toString());
		((AuthentInfoActivity) baseActivity).authenInfos.setWorkingYear(workYear);
		((AuthentInfoActivity) baseActivity).authenInfos.setSalary(monthPay);
		((AuthentInfoActivity) baseActivity).authenInfos.setCashIncome(monthIncome);
		((AuthentInfoActivity) baseActivity).authenInfos.setHouseTpVal(tvHouseProp.getText().toString());
		((AuthentInfoActivity) baseActivity).authenInfos.setHouseVal(houseValue);

	}
}