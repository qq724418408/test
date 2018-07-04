package com.bocop.yfx.activity.stageprodetail;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.bocop.jxplatform.R;
import com.bocop.yfx.activity.StageMainActivity;

/**
 * 
 * 申请成功
 * 
 * @author lh
 * 
 */
@ContentView(R.layout.yfx_activity_apply_success)
public class ApplySuccessActivity extends BaseActivity {

	@ViewInject(R.id.tv_titleName)
	private TextView tvTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		tvTitle.setText("申请成功");
	}

	@OnClick(R.id.btnBack)
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBack:
			StageMainActivity.BACK_FLAG = true;
			getActivityManager().finishAllWithoutActivity(StageMainActivity.class);
			break;

		default:
			break;
		}
	}
}
