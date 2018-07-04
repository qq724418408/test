package com.bocop.yfx.activity.stageprodetail;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.boc.jx.tools.DialogUtil;
import com.bocop.jxplatform.R;

/**
 * 
 * 绑卡
 * 
 * @author lh
 * 
 */
@ContentView(R.layout.yfx_activity_bind_card)
public class BindCardActivity extends BaseActivity {

	@ViewInject(R.id.tv_titleName)
	private TextView tvTitle;
	@ViewInject(R.id.tvName)
	private TextView tvName;
	@ViewInject(R.id.tvChooseCard)
	private TextView tvChooseCard;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initData();
	}

	private void initData() {
		tvTitle.setText("绑卡");
	}

	@OnClick({ R.id.tvChooseCard, R.id.btnAffirm })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tvChooseCard:
			final String[] cardString = {};
			DialogUtil.showToSelect(this, "", cardString, new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					tvChooseCard.setText(cardString[which]);
				}
			});
			break;

		case R.id.btnAffirm:
			callMe(CashApplyActivity.class);
			break;
		}
	}
}
