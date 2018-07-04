package com.bocop.jxplatform.trafficassistant;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.view.BackButton;
import com.polites.android.GestureImageView;


@ContentView(R.layout.activity_traffic_zhengxin)
public class TrafficZhengxinActivity extends BaseActivity {
	@ViewInject(R.id.tv_titleName)
	private TextView  tv_titleName;
	@ViewInject(R.id.iv_imageLeft)
	private BackButton backBtn; 
	protected GestureImageView view;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tv_titleName.setText("如何查看证芯编号");
		
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        view = new GestureImageView(this);
        view.setImageResource(R.drawable.trafficzhengxin);
        view.setLayoutParams(params);
        
        ViewGroup layout = (ViewGroup) findViewById(R.id.ll_process);

        layout.addView(view);
	}

}
