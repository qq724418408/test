package com.bocop.qzt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.adapter.QztOrderAdapter;
import com.bocop.jxplatform.bean.CarListBean;
import com.bocop.jxplatform.bean.QztOrderBean;
import com.bocop.jxplatform.bean.QztOrderListBean;
import com.bocop.jxplatform.config.BocSdkConfig;
import com.bocop.jxplatform.trafficassistant.MyPeccancyActivity;
import com.bocop.jxplatform.trafficassistant.TrafficAssistantMainActivity;
import com.bocop.jxplatform.trafficassistant.TrafficPayActivity;
import com.bocop.jxplatform.trafficassistant.TrafficQuickPayActivity;
import com.bocop.jxplatform.util.ActivityForResultCode;
import com.bocop.jxplatform.util.BocopDialog;
import com.bocop.jxplatform.util.CustomProgressDialog;
import com.bocop.jxplatform.util.JsonUtils;
import com.bocop.jxplatform.util.LoginUtil;
import com.bocop.jxplatform.util.QztRequestWithJsonAll;
import com.bocop.jxplatform.util.QztRequestWithJsonAndBody;
import com.bocop.jxplatform.util.QztRequestWithJsonAndHead;
import com.bocop.jxplatform.view.BackButton;
import com.google.gson.Gson;

@ContentView(R.layout.qzt_activity_order_sure)
public class QztOrderSureActivity extends BaseActivity {

	@ViewInject(R.id.tv_titleName)
	private TextView tv_titleName;
	@ViewInject(R.id.iv_imageLeft)
	private BackButton backBtn;
	
	@ViewInject(R.id.qztOrder)
	private TextView qztOrder;
	@ViewInject(R.id.qztAmt)
	private TextView qztAmt;
	@ViewInject(R.id.qztCountry)
	private TextView qztCountry;
	@ViewInject(R.id.qztDes)
	private TextView qztDes;
	@ViewInject(R.id.qztType)
	private TextView qztType;
	@ViewInject(R.id.qztName)
	private TextView qztName;
	@ViewInject(R.id.qztId)
	private TextView qztId;
	@ViewInject(R.id.qztTel)
	private TextView qztTel;
	@ViewInject(R.id.qztMail)
	private TextView qztMail;
	@ViewInject(R.id.qztAdress)
	private TextView qztAdress;
	
	@ViewInject(R.id.qztApply)
	private Button btQzt;
	
	private String orderNum;
	private String atm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		tv_titleName.setText("订单");
		ininData();
	}
	
	@OnClick(R.id.qztApply)
	public void onClick(View v){
		Bundle bundle = new Bundle();
		bundle.putString("orderNum", orderNum);
		bundle.putString("amt", atm);
		Intent intent = new Intent(QztOrderSureActivity.this,
				QztPayActivity.class);
		intent.putExtras(bundle);
		startActivity(intent);
	}
	

	private void ininData() {
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if(bundle != null){
			Log.i("tag", "bundle != null");
			orderNum = bundle.getString("orderNum");
			atm = bundle.getString("amt");
			qztOrder.setText(orderNum);
			qztAmt.setText(atm);
			qztCountry.setText(bundle.getString("strCountry"));
			qztDes.setText(bundle.getString("strPuopose"));
			qztType.setText(bundle.getString("strCrowdId"));
			qztName.setText(bundle.getString("strQztName"));
			qztId.setText(bundle.getString("strQztId"));
			qztTel.setText(bundle.getString("strQztTel"));
			qztMail.setText(bundle.getString("strQztMail"));
			qztAdress.setText(bundle.getString("strQztAdress"));
		}else{
			Log.i("tag", "bundle = null");
			btQzt.setEnabled(false);
			tv_titleName.setText("");
		}
	}
	
	

}
