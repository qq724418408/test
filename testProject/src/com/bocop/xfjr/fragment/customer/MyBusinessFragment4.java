package com.bocop.xfjr.fragment.customer;

import com.boc.jx.tools.LogUtils;
import com.bocop.jxplatform.R;
import com.bocop.xfjr.base.MyBusinessBaseFragment;
import com.bocop.xfjr.constant.XFJRConstant;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 单个状态的业务列表：已放款
 */
public class MyBusinessFragment4 extends MyBusinessBaseFragment {

	private int status = XFJRConstant.C_STATUS_4_INT; // 业务状态
	private static MyBusinessFragment4 fragment;

	@Override
	public void onResume() {
		LogUtils.e("MyBusinessFragment--->onResume--isVisible--" + isVisible);
		if (request == 0 && isVisible) {
			request++;
			super.loadNetworkData(status);
		}
		super.onResume();
	}
	
	@Override
	public void onPause() {
		request = 0;
		LogUtils.e("MyBusinessFragment--->onPause--isVisible--" + isVisible);
		super.onPause();
	}

	@Override
	protected void lazyLoad() {
		LogUtils.e("请求已放款业务");
		super.loadNetworkData(status);
	}

	public static MyBusinessFragment4 getInstance() {
		if (fragment == null) {
			fragment = new MyBusinessFragment4();
		}
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		view = inflater.inflate(R.layout.xfjr_my_application_fragment, container, false);
		return view;
	}

}