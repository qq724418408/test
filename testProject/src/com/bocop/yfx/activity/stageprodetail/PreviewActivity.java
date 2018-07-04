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
 * 订单预览
 * 
 * @author lh
 * 
 */
@ContentView(R.layout.yfx_activity_preview)
public class PreviewActivity extends BaseActivity {

	@ViewInject(R.id.tv_titleName)
	private TextView tvTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		tvTitle.setText("订单预览");
	}

	@OnClick({ R.id.btnApply })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnApply:
			callMe(OrderAffirmActivity.class);
			break;

		default:
			break;
		}
	}

}
