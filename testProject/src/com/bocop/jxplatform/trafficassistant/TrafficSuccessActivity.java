package com.bocop.jxplatform.trafficassistant;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bocop.jxplatform.R;
import com.bocop.jxplatform.fragment.HomeFragment;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;

@ContentView(R.layout.activity_trafficsuccess)
public class TrafficSuccessActivity extends BaseActivity {

	@ViewInject(R.id.tv_titleName)
	private TextView  tv_titleName;
	@ViewInject(R.id.tv_tip)
	TextView tvTip;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if(bundle != null){
			tv_titleName.setText(bundle.getString("title"));
			tvTip.setText(bundle.getString("tip"));
//			strPeccancyNum = bundle.getString("peccancyNum");
//			strSjAmt = bundle.getString("sjAmt");
//			tvSuccPeccancyNum.setText(strPeccancyNum);
//			tvSjAmt.setText(strSjAmt);
		}
	}
	
	@OnClick(R.id.btnComplete)
	public void btComplete(View v){
//		Intent intent = new Intent(TrafficPaySuccessActivity.this,HomeFragment.class);
//		startActivity(intent);
		getActivityManager().finishAllWithoutMain();
	}
}
