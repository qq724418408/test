package com.bocop.xms.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.bocop.jxplatform.R;

@ContentView(R.layout.xms_activity_sign_contract_complete)
public class SignContractCompleteActivity extends BaseActivity {

	@ViewInject(R.id.tv_titleName)
	private TextView tvTitleName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		tvTitleName.setText("签约成功");

	}

	@OnClick({ R.id.iv_imageLeft, R.id.btn_Back })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_imageLeft:
			finish();
			break;
		case R.id.btn_Back:
			getActivityManager().finishAllWithoutMain();
			break;
		}
	}

}
