package com.bocop.yfx.activity.myloan;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.bocop.jxplatform.R;
import com.bocop.yfx.activity.LoanMainActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * 提取确认
 * 
 * @author lh
 * 
 */
@ContentView(R.layout.yfx_activity_pick_up_affirm)
public class PickUpAffirmActivity extends BaseActivity {

	@ViewInject(R.id.iv_imageLeft)
	private ImageView ivLeft;
	@ViewInject(R.id.tv_titleName)
	private TextView tvTitle;
	@ViewInject(R.id.tvContent)
	private TextView tvContent;
	@ViewInject(R.id.iv1)
	private ImageView iv1;

	private String ERR_CODE;
	private String ERR_MSG;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}

	private void initView() {
		tvTitle.setText("提取确认");
		ivLeft.setVisibility(View.GONE);
		if (null != getIntent().getExtras()) {
			ERR_CODE = getIntent().getStringExtra("ERR_CODE");
			ERR_MSG = getIntent().getStringExtra("ERR_MSG");
		}
		if ("00".equals(ERR_CODE)) {
			iv1.setImageResource(R.drawable.yfx_gxd_ok);
			tvContent.setText("提款申请已提交！稍后将以短信通知您提款结果。感谢您再次选择并信任中国银行！");
		} else if ("89".equals(ERR_CODE)) {
			iv1.setImageResource(R.drawable.icon_toast_failed);
			tvContent.setText("系统账务处理未能成功：请检查您的账户状态是否异常，如有疑问，请联系中行客服热线95566！");
		} else {
			iv1.setImageResource(R.drawable.icon_toast_failed);
			tvContent.setText(ERR_MSG);
		}
	}

	@OnClick({ R.id.btnBack })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBack:
			getActivityManager().finishAllWithoutActivity(LoanMainActivity.class);
			break;

		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		getActivityManager().finishAllWithoutActivity(LoanMainActivity.class);
	}
}
