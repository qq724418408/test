package com.bocop.xyd.activity;

import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.bocop.jxplatform.R;
import com.bocop.xyd.base.XydBaseActivity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


public class Xyd_IndexActivity extends XydBaseActivity{
	private final static int  HASSTATUS=1;
	private final static int HASNOSTATUS_GOAPPLY=0;
	private final static int HASNOSTATUS_REGRET=-1;
	private int  status=HASSTATUS;
	
	@ViewInject(R.id.ll_hasNoStatus_ui)
	LinearLayout llHasNoStatusUi;
	@ViewInject(R.id.ll_hasStatus_limit)
	LinearLayout llHasStatusLimit;
	@ViewInject(R.id.tv_hasNoStatus_goapply)
	TextView tvHasNoStatusGoapply;
	@ViewInject(R.id.tv_hasNoStatus_regret)
	TextView tvHasNoStatusRegret;
	
	@ViewInject(R.id.hasStatus)
	LinearLayout hasStatus;
	@ViewInject(R.id.hasNoStatus)
	LinearLayout hasNoStatus;
	@Override
	protected void initView() {
		
	}

	@Override
	protected void initData() {
		changeStatusAndUi();
	}
	
	private void changeStatusAndUi() {
		if(status==HASSTATUS){
			hasStatus.setVisibility(View.VISIBLE);
			llHasStatusLimit.setVisibility(View.VISIBLE);
			hasNoStatus.setVisibility(View.GONE);
			llHasNoStatusUi.setVisibility(View.GONE);
		}else{
			llHasStatusLimit.setVisibility(View.GONE);
			hasStatus.setVisibility(View.GONE);
			hasNoStatus.setVisibility(View.VISIBLE);
			llHasNoStatusUi.setVisibility(View.VISIBLE);
			if(status==HASNOSTATUS_GOAPPLY){
				tvHasNoStatusGoapply.setVisibility(View.VISIBLE);
				tvHasNoStatusRegret.setVisibility(View.GONE);
			}else{
				tvHasNoStatusGoapply.setVisibility(View.GONE);
				tvHasNoStatusRegret.setVisibility(View.VISIBLE);
			}
		}
	}
	
	@Override
	protected int getLoyoutId() {
		return R.layout.xyd_activity_index;
	}
	@OnClick({R.id.ll_index_back,R.id.ll_index_applying,R.id.ll_index_nowrepay,R.id.ll_index_record,R.id.tv_hasNoStatus_goapply})
	public void back(View view){
		switch (view.getId()) {
		case R.id.ll_index_back:
			this.finish();
			break;
		case R.id.ll_index_applying:
			Xyd_ApplyingActivity.StartThisActivity(this);
			break;
		case R.id.ll_index_nowrepay:
			Xyd_RepayActivity_New.StartThisActivity(this,1);
			break;
		case R.id.ll_index_record:
			Xyd_RepayActivity_New.StartThisActivity(this,2);
			break;
		case R.id.tv_hasNoStatus_goapply:
			Xyd_ActivateActivity.StartThisActivity(this);
			break;
		default:
			break;
		}
	}

}
