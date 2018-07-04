package com.bocop.yfx.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boc.jx.base.BaseFragment;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.bocop.jxplatform.R;
import com.bocop.yfx.activity.stageprodetail.BindCardActivity;

/**
 * 
 * 产品详情
 * 
 * @author lh
 * 
 */
public class StageProDetailFragment extends BaseFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = initView(R.layout.yfx_fragment_stage_pro_detail);
		return view;
	}

	@OnClick({ R.id.btnApply })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnApply:
			baseActivity.callMe(BindCardActivity.class);
			break;

		default:
			break;
		}
	}
}
