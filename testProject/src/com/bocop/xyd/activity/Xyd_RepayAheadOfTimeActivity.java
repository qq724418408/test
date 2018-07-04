package com.bocop.xyd.activity;

import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.bocop.jxplatform.R;
import com.bocop.xyd.base.XydBaseActivity;
import com.bocop.xyd.util.dialog.XydDialog;
import com.bocop.xyd.util.dialog.XydDialogUtil;
import com.bocop.xyd.util.dialog.XydDialogUtil.ComfirmDialogClick;
import com.bocop.xyd.util.dialog.XydDialogUtil.PasswordDialogClick;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

public class Xyd_RepayAheadOfTimeActivity  extends XydBaseActivity{

	private static final int FLAG_FULL_PAYMENT=0;
	private static final int FLAG_OVERDUE_PAYMENT=-1;
	private int flag=0;
	
	@ViewInject(R.id.title)
	TextView title;
	
	@ViewInject(R.id.xyd_repay_fullpay_line)
	View xydRepayRullpayLine;
	@ViewInject(R.id.xyd_repay_fullpay_hint)
	TextView xydRepayRullpay;
	
	@Override
	protected int getLoyoutId() {
		return R.layout.xyd_activity_repay_aheadoftime;
	}

	@Override
	protected void initView() {
		
	}

	@Override
	protected void initData() {
		flag=getIntent().getIntExtra("Flag", FLAG_FULL_PAYMENT);
		setUI();
	}
	private void setUI() {
		if(flag==FLAG_FULL_PAYMENT){
			title.setText("提前结清");
			xydRepayRullpayLine.setVisibility(View.VISIBLE);
			xydRepayRullpay.setVisibility(View.VISIBLE);
		}else if(flag==FLAG_OVERDUE_PAYMENT){
			title.setText("逾期还款");
			xydRepayRullpayLine.setVisibility(View.GONE);
			xydRepayRullpay.setVisibility(View.GONE);
		}
	}

	@OnClick({R.id.ll_aheadtime_top_back,R.id.ll_aheadtime_bottom_back,R.id.ll_aheadtime_bottom_submit})
	public void back(View view){
		switch (view.getId()) {
		case R.id.ll_aheadtime_top_back:
			finish();
			break;
		case R.id.ll_aheadtime_bottom_back:
			finish();
			break;
		case R.id.ll_aheadtime_bottom_submit:
			XydDialog dialog=XydDialogUtil.inputPasswordDialog(this, new PasswordDialogClick() {
				
				@Override
				public void onOkClick(View v, Dialog dialog) {
					dialog.dismiss();
					XydDialogUtil.comfirmDialog(Xyd_RepayAheadOfTimeActivity.this, new ComfirmDialogClick(){

						@Override
						public void onOkClick(View v, Dialog dialog) {
							dialog.dismiss();
							
						}});
					
				}
				
				@Override
				public void onCancelClick(View v, Dialog dialog) {
					dialog.dismiss();
				}
			});
			dialog.show();
			break;
		default:
			break;
		}
	}
	public static void  StartThisActivity(Context context,int flag) {
		Intent i=new Intent(context,Xyd_RepayAheadOfTimeActivity.class);
		i.putExtra("Flag", flag);
		context.startActivity(i);
	}

}
