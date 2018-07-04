package com.bocop.xfjr.fragment;

import com.boc.jx.baseUtil.view.annotation.CheckNet;
import com.boc.jx.baseUtil.view.annotation.Duplicate;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.boc.jx.tools.LogUtils;
import com.bocop.jxplatform.R;
import com.bocop.xfjr.XfjrMain;
import com.bocop.xfjr.activity.XfjrIndexActivity;
import com.bocop.xfjr.base.BaseCheckProcessFragment;
import com.bocop.xfjr.callback.CommunicationCallBack;
import com.bocop.xfjr.constant.XFJRConstant;
import com.bocop.xfjr.util.TextUtil;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * 预授信提交结果页
 * 
 * @author wujunliu
 *
 */
public class PretrialResultFragment extends BaseCheckProcessFragment {

	@ViewInject(R.id.llResultPass)
	private View llResultPass; // 成功
	@ViewInject(R.id.llResultFail)
	private View llResultFail; // 失败
	@ViewInject(R.id.tvCreditLine)
	private TextView tvCreditLine; // 授信额度
	@ViewInject(R.id.tvSuccessTip)
	private TextView tvSuccessTip; // 通过提示语
	@ViewInject(R.id.tvFailTip)
	private TextView tvFailTip; // 不通过提示语
	@ViewInject(R.id.btnResult)
	private Button btnResult;
	private CommunicationCallBack communicationCallBack;
	private boolean result = false;

	@Override
	protected void initView() {
		super.initView();
		setVisibility(tvReset, View.GONE);
		//setVisibility(tvSave, View.VISIBLE);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		LogUtils.e("xfjr_fragment_instalment_pre_trial");
		return view = inflater.inflate(R.layout.xfjr_fragment_pretrial_result, container, false);
	}

	@Override
	protected void initData() {
		super.initData();
		Fragment fragment = getParentFragment();
		if (fragment instanceof CommunicationCallBack) {
			communicationCallBack = (CommunicationCallBack) fragment;
		}
		result = getArguments().getBoolean(XFJRConstant.KEY_PRETRIAL_RESULT);
		if (result) { // 通过
			String creditLine = getArguments().getString(XFJRConstant.KEY_CREDIT_LINE);
			tvCreditLine.setText(TextUtil.moneyFormat(creditLine)); // 授信额度
			//tvSuccessTip.setText("预授信通过\n最终结果以实际审批结果为准"); // 
			llResultPass.setVisibility(View.VISIBLE);
			llResultFail.setVisibility(View.GONE);
			btnResult.setText(R.string.finish_assessing); // 结束评估
		} else { // 不通过
			//tvFailTip.setText("很遗憾，您不符合预授信标准，请补充资料转人工决策！"); // 
//			btnResult.setVisibility(View.GONE);
			llResultPass.setVisibility(View.GONE);
			llResultFail.setVisibility(View.VISIBLE);
			btnResult.setText(R.string.further_info); // 补充资料
		}
	}

	@OnClick(R.id.btnResult)
	@CheckNet
	@Duplicate("PretrialResultFragment.java")
	private void clickNext(View view) {
		if (result) { // 通过,状态：待预审0-->待审批2
			Intent intent = new Intent(getActivity(), XfjrIndexActivity.class);
			getActivity().startActivity(intent);
			//getActivity().finish(); // 结束评估
		} else { // 不通过,状态：待预审0-->转人工1（去补充资料）
			if (communicationCallBack != null) {
				XfjrMain.businessStatus = "1"; // 去补充资料
				communicationCallBack.nextCallBack(null, "");
				System.gc();
			}
		}
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		LogUtils.e("onDestroy()--->System.gc()");
		System.gc();
		super.onDestroy();
	}

}
