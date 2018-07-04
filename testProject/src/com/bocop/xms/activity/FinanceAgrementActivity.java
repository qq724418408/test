package com.bocop.xms.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.bocop.jxplatform.R;

@ContentView(R.layout.activity_financeagreement)
public class FinanceAgrementActivity extends BaseActivity {
	@ViewInject(R.id.tv_titleName)
	private TextView tv_titleName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tv_titleName.setText("协议");
	}

}
