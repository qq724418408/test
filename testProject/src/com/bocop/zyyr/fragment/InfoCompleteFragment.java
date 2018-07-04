package com.bocop.zyyr.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.boc.jx.base.BaseFragment;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.boc.jx.tools.DialogUtil;
import com.bocop.jxplatform.R;
import com.bocop.yfx.utils.CheckoutUtil;
import com.bocop.zyyr.activity.AuthentInfoActivity;
import com.bocop.zyyr.bean.KeyAndValue;

/**
 * 完善个人信息
 * 
 * @author rd
 * 
 */
public class InfoCompleteFragment extends BaseFragment {

	@ViewInject(R.id.llSocietySafeTime)
	private LinearLayout llSocietySafeTime;// 社保缴纳时间框
	@ViewInject(R.id.llReserfundTime)
	private LinearLayout llReserfundTime;// 公积金缴存时间框
	@ViewInject(R.id.cbFunds)
	private CheckBox cbFunds;// 本地社保选择框
	@ViewInject(R.id.cbSocSafe)
	private CheckBox cbSocSafe;// 本地基金选择框
	@ViewInject(R.id.cbCar)
	private CheckBox cbCar;// 车选择框

	@ViewInject(R.id.viewLine1)
	private View viewLine1;// 分割线
	@ViewInject(R.id.viewLine2)
	private View viewLine2;
	@ViewInject(R.id.etSocInsurTime)
	private EditText etSocInsurTime;// 社保缴纳时间
	@ViewInject(R.id.etReserfundTime)
	private EditText etReserfundTime;// 公积金缴存时间
	@ViewInject(R.id.tvCredit)
	private TextView tvCredit;// 信用情况

	private String hasFund;// 是否有本地基金 0是 1否
	private String hasInsure;
	private String hasCar;

	private List<KeyAndValue> creditStatus = new ArrayList<KeyAndValue>();// 信用情况列表

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = initView(R.layout.zyyr_fragment_complete_info);
		return view;
	}

	@Override
	protected void initView() {
		super.initView();
		hasFund = getString(R.string.no);
		hasInsure = getString(R.string.no);
		hasCar = getString(R.string.no);

		creditStatus.clear();
		creditStatus.addAll(((AuthentInfoActivity) baseActivity).creditData);
		// 设置选择框默认值
		if (!creditStatus.isEmpty()) {
			tvCredit.setText(creditStatus.get(0).getParaValue());
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

	@OnClick({ R.id.btnNext, R.id.btnLast, R.id.cbFunds, R.id.cbSocSafe, R.id.cbCar, R.id.tvCredit })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnNext:
			String socInsurTime = etSocInsurTime.getText().toString();
			String reserfundTime = etReserfundTime.getText().toString();
			String credit = tvCredit.getText().toString();
			if (CheckoutUtil.isEmpty(credit)) {
				Toast.makeText(getActivity(), R.string.inputCredit, Toast.LENGTH_SHORT).show();
			} else if (cbFunds.isChecked() && CheckoutUtil.isEmpty(reserfundTime)) {
				Toast.makeText(getActivity(), R.string.inputGjjTime, Toast.LENGTH_SHORT).show();
			} else if (cbSocSafe.isChecked() && CheckoutUtil.isEmpty(socInsurTime)) {
				Toast.makeText(getActivity(), R.string.inputInsurTime, Toast.LENGTH_SHORT).show();
			} else {
				storeInfo(socInsurTime, reserfundTime, credit);// 保存用户填写信息
				((AuthentInfoActivity) baseActivity).showFragment(2);

			}
			break;
		case R.id.btnLast:
			((AuthentInfoActivity) baseActivity).showFragment(0);
			break;
		case R.id.cbFunds:
			if (cbFunds.isChecked()) {
				cbFunds.setBackgroundResource(R.drawable.zyyr_btn_open);
				llReserfundTime.setVisibility(View.VISIBLE);
				viewLine1.setVisibility(View.VISIBLE);
				hasFund = getString(R.string.yes);
			} else {
				cbFunds.setBackgroundResource(R.drawable.zyyr_btn_close);
				llReserfundTime.setVisibility(View.GONE);
				viewLine1.setVisibility(View.GONE);
				hasFund = getString(R.string.no);
			}

			break;

		case R.id.cbSocSafe:
			if (cbSocSafe.isChecked()) {
				cbSocSafe.setBackgroundResource(R.drawable.zyyr_btn_open);
				llSocietySafeTime.setVisibility(View.VISIBLE);
				viewLine2.setVisibility(View.VISIBLE);
				hasInsure = getString(R.string.yes);
			} else {
				cbSocSafe.setBackgroundResource(R.drawable.zyyr_btn_close);
				llSocietySafeTime.setVisibility(View.GONE);
				viewLine2.setVisibility(View.GONE);
				hasInsure = getString(R.string.no);
			}

			break;

		case R.id.cbCar:
			if (cbCar.isChecked()) {
				cbCar.setBackgroundResource(R.drawable.zyyr_btn_open);
				hasCar = getString(R.string.yes);
			} else {
				cbCar.setBackgroundResource(R.drawable.zyyr_btn_close);
				hasCar = getString(R.string.no);
			}
			break;
		case R.id.tvCredit:

			final String[] creditStr = new String[creditStatus.size()];
			for (int i = 0; i < creditStatus.size(); i++) {
				creditStr[i] = creditStatus.get(i).getParaValue();
			}

			DialogUtil.showToSelect(getActivity(), "", creditStr, new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					tvCredit.setText(creditStr[which]);
					((AuthentInfoActivity) baseActivity).authenInfos
							.setCreditStatus(creditStatus.get(which).getParaKey());
				}
			});
			break;

		}
	}

	/**
	 * 保存用户填写信息
	 * 
	 * @param credit
	 * @param reserfundTime
	 * @param socInsurTime
	 */
	private void storeInfo(String socInsurTime, String reserfundTime, String credit) {
		((AuthentInfoActivity) baseActivity).authenInfos.setCreditStaVal(credit);
		((AuthentInfoActivity) baseActivity).authenInfos.setHasCar(hasCar);
		((AuthentInfoActivity) baseActivity).authenInfos.setHasFund(hasFund);
		((AuthentInfoActivity) baseActivity).authenInfos.setHasInsure(hasInsure);
		if (null == socInsurTime || "".equals(socInsurTime)) {
			((AuthentInfoActivity) baseActivity).authenInfos.setSecurityPeriod("0");
		} else {
			((AuthentInfoActivity) baseActivity).authenInfos.setSecurityPeriod(socInsurTime);
		}
		if (null == reserfundTime || "".equals(reserfundTime)) {
			((AuthentInfoActivity) baseActivity).authenInfos.setGjjPeriod("0");
		} else {
			((AuthentInfoActivity) baseActivity).authenInfos.setGjjPeriod(reserfundTime);
		}
	}
}
