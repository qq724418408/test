package com.bocop.xms.activity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.base.BaseApplication;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.boc.jx.constants.Constants;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.activity.WebActivity;
import com.bocop.jxplatform.activity.WebViewActivity;
import com.bocop.jxplatform.adapter.TrafficMainAdapter;
import com.bocop.jxplatform.bean.PerFunctionBean;
import com.bocop.jxplatform.config.TransactionValue;
import com.bocop.jxplatform.fragment.HomeFragment;
import com.bocop.jxplatform.util.BocOpUtil;
import com.bocop.jxplatform.util.CspUtil;
import com.bocop.jxplatform.util.CustomProgressDialog;
import com.bocop.jxplatform.util.JsonUtils;
import com.bocop.jxplatform.util.LoginUtil;
import com.bocop.jxplatform.util.LoginUtilAnother;
import com.bocop.jxplatform.util.Mcis;
import com.bocop.jxplatform.util.BocOpUtil.CallBackBoc;
import com.bocop.jxplatform.util.CspUtil.CallBack;
import com.bocop.jxplatform.util.LoginUtilAnother.ILoginListener;
import com.bocop.jxplatform.view.BackButton;
import com.bocop.xms.bean.DialogCostType;
import com.bocop.xms.bean.MessageList;
import com.bocop.xms.bean.MessageResponse;
import com.bocop.xms.bean.UserResponse;
import com.bocop.xms.utils.XStreamUtils;
import com.bocop.xms.view.CostTypeDialog;
import com.bocop.xms.view.CostTypeDialog.CostTypeOnClickListener;
import com.bocop.xms.xml.CspXmlXms004;
import com.google.gson.Gson;

@ContentView(R.layout.activity_zqt)
public class ZqtActivity extends BaseActivity {

	@ViewInject(R.id.tv_titleName)
	private TextView tv_titleName;
//	@ViewInject(R.id.iv_imageLeft)
//	private BackButton backBtn;
	@ViewInject(R.id.lvadvice)
	private ListView traListView;


	private BaseApplication baseApplication = BaseApplication.getInstance();
	List<PerFunctionBean> traDates = new ArrayList<PerFunctionBean>();
	List<PerFunctionBean> messageDates = new ArrayList<PerFunctionBean>();
	TrafficMainAdapter traAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		tv_titleName.setText("证券通");
		initView();
		initEvent();
	}
	
	@OnClick(R.id.iv_imageLeft)
	public void back(View v) {
			finish();
	}

	private void initView() {

		initTraDates();

		traAdapter = new TrafficMainAdapter(ZqtActivity.this, traDates,
				R.layout.item_quickpay);
		traListView.setAdapter(traAdapter);
	}

	/*
	 * 初始化列表
	 */
	//证券行情、财经资讯、证券开户、证券交易和中国红商城 
	private void initTraDates() {
		PerFunctionBean funBean1 = new PerFunctionBean("flight",
				R.drawable.icon_xms_market, "证券行情");
		PerFunctionBean funBean2 = new PerFunctionBean("flight",
				R.drawable.icon_xms_consult, "财经资讯");
		PerFunctionBean funBean3 = new PerFunctionBean("exchange",
				R.drawable.zqt_icon_open, "证券开户");
		PerFunctionBean funBean4 = new PerFunctionBean("rate",
				R.drawable.zqt_icon_etrade, "证券交易");
		PerFunctionBean funBean5 = new PerFunctionBean("rate",
				R.drawable.zqt_icon_shop, "中国红商城");
		traDates.add(funBean1);
		traDates.add(funBean2);
		traDates.add(funBean3);
		traDates.add(funBean4);
		traDates.add(funBean5);
	}

	private void initEvent() {
		traListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				if (!baseApplication.isNetStat()) {
					CustomProgressDialog
					.showBocNetworkSetDialog(ZqtActivity.this);
				}
				else{
					if (arg2 == 0) {
						Bundle bundle = new Bundle();
						bundle.putString("url", Constants.xmsUrlForMarket);
						bundle.putString("name", "证券行情");
						Intent intent = new Intent(ZqtActivity.this,
								WebViewActivity.class);
						intent.putExtras(bundle);
						startActivity(intent);
					}
					
					if (arg2 == 1) {
						Bundle bundle = new Bundle();
						bundle.putString("url", Constants.xmsUrlForConsult);
						bundle.putString("name", "财经咨询");
						Intent intent = new Intent(ZqtActivity.this,
								WebViewActivity.class);
						intent.putExtras(bundle);
						startActivity(intent);
					}
					if (arg2 == 2) {
						Bundle bundle = new Bundle();
						bundle.putString("url", Constants.qztUrlForOpen);
						bundle.putString("name", "证券开户");
						Intent intent = new Intent(ZqtActivity.this,
								WebActivity.class);
						intent.putExtras(bundle);
						startActivity(intent);
					}
					
					if (arg2 == 3) {
						Bundle bundle = new Bundle();
						bundle.putString("url", Constants.qztUrlForEtrade);
						bundle.putString("name", "证券交易");
						Intent intent = new Intent(ZqtActivity.this,
								WebActivity.class);
						intent.putExtras(bundle);
						startActivity(intent);
					}
					
					if (arg2 ==4) {
						Bundle bundle = new Bundle();
						bundle.putString("url", Constants.xmsUrlForEshop);
						bundle.putString("name", "中国红商城");
						Intent intent = new Intent(ZqtActivity.this,
								WebActivity.class);
						intent.putExtras(bundle);
						startActivity(intent);
					}
					
				}
				
			}
		});
	}
}
