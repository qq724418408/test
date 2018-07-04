package com.bocop.xfjr.fragment.merchant;

import com.bocop.jxplatform.R;
import com.bocop.xfjr.base.MyBusinessBaseFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 2已拒绝
 */
public class MyBusinessFragment2 extends MyBusinessBaseFragment {

	private int status = 2; // 业务状态:2已拒绝
	private static MyBusinessFragment2 fragment;
	private boolean isPrepared; // 已经初始化完成。

	@Override
	protected void lazyLoad() {
		if (isPrepared && isVisible) {
			super.loadNetworkData(status);
			isPrepared = false;
			isVisible = false;
		}
	}

	public static MyBusinessFragment2 getInstance() {
		if(fragment == null){
			fragment = new MyBusinessFragment2();
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