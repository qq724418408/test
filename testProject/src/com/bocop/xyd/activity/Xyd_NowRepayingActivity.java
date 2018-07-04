package com.bocop.xyd.activity;

import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.bocop.jxplatform.R;
import com.bocop.xyd.base.XydBaseActivity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

public class Xyd_NowRepayingActivity extends XydBaseActivity{

	@Override
	protected int getLoyoutId() {
		// TODO Auto-generated method stub
		return R.layout.xyd_activity_nowrepay;
	}

	@Override
	protected void initView() {
		
	}

	@Override
	protected void initData() {
		
	}
	
	@OnClick({R.id.ll_nowrepay_back,R.id.tv_nowrepay_bottom_back,R.id.tv_nowrepay_bottom_gopact,R.id.tv_nowrepay_bottom_repayaheadoftime})
	public void back(View view){
		switch (view.getId()) {
		case R.id.ll_nowrepay_back:
			finish();
			break;
		case R.id.tv_nowrepay_bottom_back:
			finish();
			break;
		case R.id.tv_nowrepay_bottom_gopact:
			Xyd_PactForLoansActivity.StartThisActivity(this);
			break;
		case R.id.tv_nowrepay_bottom_repayaheadoftime:
			Xyd_RepayAheadOfTimeActivity.StartThisActivity(this,0);
			break;
		default:
			break;
		}
	}
	public static void  StartThisActivity(Context context) {
		Intent i=new Intent(context,Xyd_NowRepayingActivity.class);
//		i.putExtra("STYLE", style);
		context.startActivity(i);
	}
}
