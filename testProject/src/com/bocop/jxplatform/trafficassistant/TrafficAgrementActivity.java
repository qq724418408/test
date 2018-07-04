package com.bocop.jxplatform.trafficassistant;

import android.os.Bundle;
import android.widget.TextView;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.bocop.jxplatform.R;

@ContentView(R.layout.activity_trafficagreement)
public class TrafficAgrementActivity extends BaseActivity {
	@ViewInject(R.id.tv_titleName)
	private TextView tv_titleName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tv_titleName.setText("简易违法处罚协议");
	}

}
