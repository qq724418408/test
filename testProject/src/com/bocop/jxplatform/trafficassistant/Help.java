package com.bocop.jxplatform.trafficassistant;

import android.os.Bundle;
import android.widget.TextView;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.view.BackButton;

@ContentView(R.layout.activity_help)
public class Help extends BaseActivity {
	
	@ViewInject(R.id.tv_titleName)
	private TextView  tv_titleName;
	@ViewInject(R.id.iv_imageLeft)
	private BackButton backBtn; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		tv_titleName.setText("使用帮助");
	}
}
