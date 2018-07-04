package com.bocop.jxplatform.activity;


import java.text.SimpleDateFormat;
import java.util.Date;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.baseUtil.cache.CacheBean;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.config.BocSdkConfig;
import com.bocop.jxplatform.util.LoginUtil;
import com.bocop.jxplatform.util.LoginUtil.OnRequestCustCallBack;
import com.bocop.yfx.activity.LoanMainActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


@ContentView(R.layout.gjt_zyed_activity)
public class GjtZyedActivity extends BaseActivity{
	
	@ViewInject(R.id.tv_titleName)
	TextView tvTitle;
	@ViewInject(R.id.tvSubTitle)
	TextView tvSubTitle;
	
	private int FLAG = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tvTitle.setText("中银江西公积贷");
	}
	
	@OnClick({R.id.btnGoApply})
	public void onClick(View v){
		switch (v.getId()) {
		case R.id.btnGoApply:
			Intent intent = new Intent(GjtZyedActivity.this,ZYEDActivity.class);
			startActivity(intent);
			break;

		}
	}
}
