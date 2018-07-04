package com.bocop.yfx.activity.stageprodetail;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.bocop.jxplatform.R;

/**
 * 
 * 订单确认
 * 
 * @author lh
 * 
 */
@ContentView(R.layout.yfx_activity_order_affirm)
public class OrderAffirmActivity extends BaseActivity {

	@ViewInject(R.id.tv_titleName)
	private TextView tvTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		tvTitle.setText("订单确认");
	}

	@OnClick({ R.id.btnAffirm })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnAffirm:
			callMe(ApplySuccessActivity.class);
			break;

		default:
			break;
		}
	}
}
