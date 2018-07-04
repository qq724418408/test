/**
 * 
 */
package com.bocop.jxplatform.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.boc.jx.constants.Constants;
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
@ContentView(R.layout.activity_market_code_add)
public class marketCodeAddActivity extends BaseActivity {

	@ViewInject(R.id.tv_titleName)
	private TextView tv_titleName;
	@ViewInject(R.id.iv_imageLeft)
	private BackButton backBtn;

	@ViewInject(R.id.et_org)
	private EditText etOrg;
	@ViewInject(R.id.et_UserMarketCode)
	private EditText etUserMarketCode;
	@ViewInject(R.id.btn_Apply)
	private Button btnApply;

	String strOrg;
	String strMarketCode;
	String strGson;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.boc.jx.base.BaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		tv_titleName.setText("营销代码");
	}

	@OnClick(R.id.btn_Apply)
	public void onClick(View v){
		requestBocopForMarketCodeAdd();
	}
	private void requestBocopForMarketCodeAdd() {
		strOrg = etOrg.getText().toString();
		strMarketCode = etUserMarketCode.getText().toString();
		
		String cardId;// 证件号码
		String customerId;//客户号
		final SharedPreferences sp = getSharedPreferences(
				Constants.CUSTOM_PREFERENCE_NAME, Context.MODE_PRIVATE);
		cardId = sp.getString(Constants.CUSTOM_ID_NO, "anonymous");
		customerId = sp.getString(Constants.CUSTOM_CUS_ID, "anonymous");
		if(strOrg.length() != 5){
			Toast.makeText(marketCodeAddActivity.this, "亲，请输入五位的机构号码", Toast.LENGTH_SHORT).show();
			return;
		}
		Gson gson = new Gson();
		Map<String, String> map = new HashMap<String, String>();
		map.put("appId", BocSdkConfig.CONSUMER_KEY);
		map.put("userId", LoginUtil.getUserId(marketCodeAddActivity.this));
		map.put("orgId", strOrg);
		map.put("code", strMarketCode);
		map.put("cardId", cardId);
		map.put("customerId", customerId);
		strGson = gson.toJson(map);
		try {
			strGson = URLDecoder.decode(gson.toJson(map), "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		QztRequestWithJsonAndHead qztRequestWithJson = new QztRequestWithJsonAndHead(
				this);
		qztRequestWithJson
				.postOpboc(
						strGson,
						BocSdkConfig.marketAddUrl,
						new com.bocop.jxplatform.util.QztRequestWithJsonAndHead.CallBackBoc() {

							@Override
							public void onSuccess(String responStr) {
								Log.i("tag22", responStr);
								try {
								} catch (Exception e) {
									e.printStackTrace();
								}
								Toast.makeText(marketCodeAddActivity.this,
										responStr, Toast.LENGTH_LONG).show();
								finish();
							}

							@Override
							public void onStart() {
								// TODO Auto-generated method stub
								Log.i("tag", "发送JSON报文" + strGson);
							}

							@Override
							public void onFinish() {
								// TODO Auto-generated method stub

							}

							@Override
							public void onFailure(String responStr) {
								Toast.makeText(marketCodeAddActivity.this,
										responStr, Toast.LENGTH_LONG).show();

							}
						});
	}

}
