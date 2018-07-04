package com.bocop.xfjr.fragment.merchant;

import com.bocop.jxplatform.R;
import com.bocop.xfjr.base.MyBusinessBaseFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 0处理中
 */
public class MyBusinessFragment0 extends MyBusinessBaseFragment {

	private int status = 0; // 业务状态:处理中
	private static MyBusinessFragment0 fragment;
	
	@Override
	protected void lazyLoad() {
		super.loadNetworkData(status);
	}
	
	public static MyBusinessFragment0 getInstance() {
		if(fragment == null){
			fragment = new MyBusinessFragment0();
		}
		return fragment;
	}
 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.xfjr_my_application_fragment, container, false);
		return view;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

}