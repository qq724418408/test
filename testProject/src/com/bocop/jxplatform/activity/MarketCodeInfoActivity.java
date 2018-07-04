/**
 * 
 */
package com.bocop.jxplatform.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.config.BocSdkConfig;
import com.bocop.jxplatform.util.JsonUtils;
import com.bocop.jxplatform.util.LoginUtil;
import com.bocop.jxplatform.util.QztRequestWithJsonAndBody;
import com.bocop.jxplatform.util.QztRequestWithJsonAndHead;
import com.bocop.jxplatform.view.BackButton;
import com.bocop.qzt.QztApplyActivity;
import com.bocop.qzt.QztOrderSureActivity;
import com.google.gson.Gson;

/** 
 * @author luoyang  E-mail: luoyang8714@163.com
 * @version 创建时间：2017-3-7 下午3:18:12 
 * 类说明 
 */
/**
 * @author zhongye
 * 
 */
@ContentView(R.layout.activity_market_code_info)
public class MarketCodeInfoActivity extends BaseActivity {

	@ViewInject(R.id.tv_titleName)
	private TextView tv_titleName;
	@ViewInject(R.id.iv_imageLeft)
	private BackButton backBtn;

	@ViewInject(R.id.et_org)
	private EditText etOrg;
	@ViewInject(R.id.et_UserMarketCode)
	private EditText etUserMarketCode;
	
	@ViewInject(R.id.et_userid)
	private EditText etUserid;
	@ViewInject(R.id.et_orgname)
	private EditText etOrgname;
	@ViewInject(R.id.et_orgtel)
	private EditText etOrgtel;
	@ViewInject(R.id.et_orgadd)
	private EditText etOrgadd;
	
	
	@ViewInject(R.id.btn_Apply)
	private Button btnApply;


	String code;
	String orgAddress;
	String orgId;
	String orgName;
	String orgNumber;
	String userId;
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.boc.jx.base.BaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		tv_titleName.setText("营销信息");
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if(bundle != null){
			code = bundle.getString("code");
			orgAddress = bundle.getString("orgAddress");
			orgId = bundle.getString("orgId");
			orgName = bundle.getString("orgName");
			orgNumber = bundle.getString("orgNumber");
			userId = bundle.getString("userId");
			
			etUserid.setText(userId);
			etOrg.setText(orgId);
			etOrgname.setText(orgName);
			etOrgtel.setText(orgNumber);
			etOrgadd.setText(orgAddress);
			etUserMarketCode.setText(code);
		}
	}

	@OnClick(R.id.btn_Apply)
	public void onClick(View v){
		finish();
	}

}
