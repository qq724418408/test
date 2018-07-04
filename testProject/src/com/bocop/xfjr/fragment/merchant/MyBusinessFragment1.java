package com.bocop.xfjr.fragment.merchant;

import com.bocop.jxplatform.R;
import com.bocop.xfjr.base.MyBusinessBaseFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 1已通过
 */
public class MyBusinessFragment1 extends MyBusinessBaseFragment {

	private int status = 1; // 业务状态:已通过
	private static MyBusinessFragment1 fragment;
	private boolean isPrepared; // 已经初始化完成。

	@Override
	protected void lazyLoad() {
		if (isPrepared && isVisible) {
			super.loadNetworkData(status);
			isPrepared = false;
			isVisible = false;
		}
	}

	public static MyBusinessFragment1 getInstance() {
		if (fragment == null) {
			fragment = new MyBusinessFragment1();
		}
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.xfjr_my_application_fragment, container, false);
		isPrepared = true;
		lazyLoad();
		return view;
	}

}