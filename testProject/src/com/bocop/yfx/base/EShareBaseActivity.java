package com.bocop.yfx.base;

import android.os.Bundle;

import com.boc.jx.base.BaseActivity;

/**
 * 
 * 我的贷款基类
 * 
 * @author lh
 * 
 */
public class EShareBaseActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onBackPressed() {
		finish();
	}

}
