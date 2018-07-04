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
import com.boc.jx.constants.Constants;
import com.bocop.jxplatform.R;
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

@ContentView(R.layout.xms_activity_first)
public class XmsFirstActivity extends BaseActivity implements ILoginListener {

	@ViewInject(R.id.tv_titleName)
	private TextView tv_titleName;
	@ViewInject(R.id.iv_imageLeft)
	private BackButton backBtn;
	@ViewInject(R.id.lvadvice)
	private ListView traListView;

	@ViewInject(R.id.lvSignMamager)
	private ListView lvSingManager;

	@ViewInject(R.id.lvMessage)
	private ListView lvMessage;

	private DialogCostType costType;

	private BaseApplication baseApplication = BaseApplication.getInstance();
	List<PerFunctionBean> traDates = new ArrayList<PerFunctionBean>();
	List<PerFunctionBean> singManagerDates = new ArrayList<PerFunctionBean>();
	List<PerFunctionBean> messageDates = new ArrayList<PerFunctionBean>();
	TrafficMainAdapter traAdapter;
	TrafficMainAdapter signManagerAdapter;
	TrafficMainAdapter messageAdapter;

	private static final int USER_LIST_SUCCESS = 0;
	private static final int USER_FAILED = 1;
	
	private int flag;

	String strCusName;
	String strIdNo;
	String strCusid;

	private Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case USER_LIST_SUCCESS:
				String content = (String) msg.obj;
				UserResponse userResponse = XStreamUtils.getFromXML(content,
						UserResponse.class);
				// 该用户没有签约
				if ("01".equals(userResponse.getConstHead().getErrCode())) {
					// 07:金融资产到期提醒
					Log.i("tag", "没有签约协议");
					if ("07".equals(costType.getTypeCode())) {
						requestBocopForUseridQuery();
					}
					// 水电煤气到期提醒
					else {
						Intent intent = new Intent(XmsFirstActivity.this,
								SignContractActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("TITLE", "签约");
						bundle.putString("COST_TYPE", costType.getCostName());
						bundle.putString("TYPE_CODE", costType.getTypeCode());
						intent.putExtra("BUNDLE", bundle);
						startActivity(intent);
					}
					// 用户已经签约，列表显示
				} else {
					Log.i("tag", "有签约协议" + costType.getTypeCode());
					Intent intent = new Intent(XmsFirstActivity.this,
							UserManagerActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("COST_TYPE", costType.getCostName());
					bundle.putString("TYPE_CODE", costType.getTypeCode());
					bundle.putString("USER_LIST", (String) msg.obj);
					intent.putExtra("BUNDLE", bundle);
					startActivity(intent);
				}
				break;

			case USER_FAILED:
				String responStr = (String) msg.obj;
				CspUtil.onFailure(XmsFirstActivity.this, responStr);
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		tv_titleName.setText("秘书通");
		//初始化 免责申明提示框
		flag = 0;
		initView();
		initEvent();
	}

	private void initView() {

		initTraDates();

		traAdapter = new TrafficMainAdapter(XmsFirstActivity.this, traDates,
				R.layout.item_quickpay);
		traListView.setAdapter(traAdapter);
		signManagerAdapter = new TrafficMainAdapter(XmsFirstActivity.this,
				singManagerDates, R.layout.item_quickpay);
		lvSingManager.setAdapter(signManagerAdapter);
		messageAdapter = new TrafficMainAdapter(XmsFirstActivity.this,
				messageDates, R.layout.item_quickpay);
		lvMessage.setAdapter(messageAdapter);
	}

	/*
	 * 初始化列表
	 */
	private void initTraDates() {
		PerFunctionBean funBean1 = new PerFunctionBean("exchange",
				R.drawable.icon_xms_rate, "外汇牌价");
		PerFunctionBean funBean2 = new PerFunctionBean("rate",
				R.drawable.icon_xms_deposit, "存/贷款利率");
		// PerFunctionBean funBean3 = new PerFunctionBean("rate",
		// R.drawable.icon_xms_metal, "贵金属报价");
		PerFunctionBean funBean4 = new PerFunctionBean("rate",
				R.drawable.icon_xms_org, "网点查询");
		PerFunctionBean funBean5 = new PerFunctionBean("rate",
				R.drawable.icon_xms_invest, "投资攻略");
		PerFunctionBean funBean6 = new PerFunctionBean("rate",
				R.drawable.icon_xms_fund, "基金净值");
		PerFunctionBean funBean7 = new PerFunctionBean("rate",
				R.drawable.icon_xms_atm, "ATM分布");
		PerFunctionBean funBean8 = new PerFunctionBean("train",
				R.drawable.icon_xms_train, "火车票订购");
		PerFunctionBean funBean9 = new PerFunctionBean("flight",
				R.drawable.icon_xms_fight, "飞机票订购");
		PerFunctionBean funBean10 = new PerFunctionBean("flight",
				R.drawable.icon_xms_dotbooking, "网点预约");
		PerFunctionBean funBean11 = new PerFunctionBean("flight",
				R.drawable.icon_xms_consult, "财经资讯");
		PerFunctionBean funBean12 = new PerFunctionBean("flight",
				R.drawable.icon_xms_market, "证券行情");
		PerFunctionBean funBean13 = new PerFunctionBean("flight",
				R.drawable.icon_xms_dzdp, "大众点评");
		PerFunctionBean funBean14 = new PerFunctionBean("flight",
				R.drawable.icon_xms_hx, "和讯财经");
		PerFunctionBean funBean15 = new PerFunctionBean("flight",
				R.drawable.icon_xms_lv100, "旅途100");
		PerFunctionBean funBean16 = new PerFunctionBean("flight",
				R.drawable.icon_xms_shuning, "苏宁");
		PerFunctionBean funBean17 = new PerFunctionBean("flight",
				R.drawable.icon_xms_weather, "天气预报");
		traDates.add(funBean1);
		traDates.add(funBean2);
		// traDates.add(funBean3);
		traDates.add(funBean4);
		traDates.add(funBean5);
		traDates.add(funBean6);
		traDates.add(funBean7);
		traDates.add(funBean8);
		traDates.add(funBean9);
		traDates.add(funBean10);
		traDates.add(funBean11);
		traDates.add(funBean12);
		traDates.add(funBean13);
		traDates.add(funBean14);
		traDates.add(funBean15);
		traDates.add(funBean16);
		traDates.add(funBean17);
		

		PerFunctionBean signManager = new PerFunctionBean("signManager",
				R.drawable.xms_main_manage, "签约");
		singManagerDates.add(signManager);
		PerFunctionBean signOverView = new PerFunctionBean("signOverView",
				R.drawable.xms_main_msglist, "签约一览");
		singManagerDates.add(signOverView);

		PerFunctionBean message = new PerFunctionBean("message",
				R.drawable.xms_main_signedlist, "消息列表");
		messageDates.add(message);
	}

	private void initEvent() {

		lvSingManager.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				if (baseApplication.isNetStat()) {
					if (LoginUtil.isLog(XmsFirstActivity.this)) {
						if (position == 0) {
							showCostDialog();
						}
						if (position == 1) {
							Intent intent = new Intent(XmsFirstActivity.this,
									SignOverViewActivity.class);
							startActivity(intent);
						}
					} else {
						LoginUtilAnother.authorizeAnother(
								XmsFirstActivity.this, XmsFirstActivity.this,
								position);
					}
				} else {
					CustomProgressDialog
							.showBocNetworkSetDialog(XmsFirstActivity.this);
				}
			}
		});

		lvMessage.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 0) {
//					if (baseApplication.isNetStat()) {
//						if (LoginUtil.isLog(XmsFirstActivity.this)) {
//							Intent intent = new Intent(XmsFirstActivity.this,
//									MessageActivity.class);
//							startActivity(intent);
//						} else {
//							LoginUtilAnother.authorizeAnother(
//									XmsFirstActivity.this,
//									XmsFirstActivity.this, 9);
//						}
//					} else {
//						CustomProgressDialog
//								.showBocNetworkSetDialog(XmsFirstActivity.this);
//					}
					
					Intent intent = new Intent(XmsFirstActivity.this,
							MessageActivity.class);
					startActivity(intent);
				}

			}
		});

		traListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				if (!baseApplication.isNetStat()) {
					CustomProgressDialog
					.showBocNetworkSetDialog(XmsFirstActivity.this);
				}
				else{
					// 外汇牌价
					if (arg2 == 0) {
						Intent intent = new Intent(XmsFirstActivity.this,
								ExchangeActivity.class);
						startActivity(intent);
					}
					// 存/贷款利率
					if (arg2 == 1) {
						Intent intent = new Intent(XmsFirstActivity.this,
								RateActivity.class);
						startActivity(intent);
					}
					// 贵金属报价
					if (arg2 == 2) {
						Intent intent = new Intent(XmsFirstActivity.this,
								OrgActivity.class);
						startActivity(intent);
					}
					if (arg2 == 3) {
						Intent intent = new Intent(XmsFirstActivity.this,
								InvestActivity.class);
						startActivity(intent);
					}
					if (arg2 == 4) {
						Intent intent = new Intent(XmsFirstActivity.this,
								FundActivity.class);
						startActivity(intent);
					}
					if (arg2 == 5) {
						Intent intent = new Intent(XmsFirstActivity.this,
								AtmActivity.class);
						startActivity(intent);
					}
					if (arg2 == 6) {
						Intent intent = new Intent(XmsFirstActivity.this,
								TrainActivity.class);
						startActivity(intent);
					}
					if (arg2 == 7) {
						Intent intent = new Intent(XmsFirstActivity.this,
								FlightActivity.class);
						startActivity(intent);
					}
					if (arg2 == 8) {
						Bundle bundle = new Bundle();
						bundle.putString("url", Constants.xmsUrlForDotbooking);
						bundle.putString("name", "网点预约");
						Intent intent = new Intent(XmsFirstActivity.this,
								XmsWebActivity.class);
						intent.putExtras(bundle);
						startActivity(intent);
					}
					if (arg2 == 9) {
						Bundle bundle = new Bundle();
						bundle.putString("url", Constants.xmsUrlForConsult);
						bundle.putString("name", "财经咨询");
						Intent intent = new Intent(XmsFirstActivity.this,
								XmsWebActivity.class);
						intent.putExtras(bundle);
						startActivity(intent);
					}
					if (arg2 == 10) {
						Bundle bundle = new Bundle();
						bundle.putString("url", Constants.xmsUrlForMarket);
						bundle.putString("name", "证券行情");
						Intent intent = new Intent(XmsFirstActivity.this,
								XmsWebActivity.class);
						intent.putExtras(bundle);
						startActivity(intent);
					}
					if (arg2 == 11) {
						Bundle bundle = new Bundle();
						bundle.putString("url", Constants.xmsUrlForDzdp);
						bundle.putString("name", "大众点评");
						Intent intent = new Intent(XmsFirstActivity.this,
								XmsWebActivity.class);
						intent.putExtras(bundle);
						startActivity(intent);
					}
					if (arg2 == 12) {
						Bundle bundle = new Bundle();
						bundle.putString("url", Constants.xmsUrlForHx);
						bundle.putString("name", "和讯财经");
						Intent intent = new Intent(XmsFirstActivity.this,
								XmsWebActivity.class);
						intent.putExtras(bundle);
						startActivity(intent);
					}
					if (arg2 == 13) {
						Bundle bundle = new Bundle();
						bundle.putString("url", Constants.xmsUrlForLt100);
						bundle.putString("name", "旅途100");
						Intent intent = new Intent(XmsFirstActivity.this,
								XmsWebActivity.class);
						intent.putExtras(bundle);
						startActivity(intent);
					}
					if (arg2 == 14) {
						Bundle bundle = new Bundle();
						bundle.putString("url", Constants.xmsUrlForSuning);
						bundle.putString("name", "苏宁");
						Intent intent = new Intent(XmsFirstActivity.this,
								XmsWebActivity.class);
						intent.putExtras(bundle);
						startActivity(intent);
					}
					if (arg2 == 15) {
						Bundle bundle = new Bundle();
						bundle.putString("url", Constants.xmsUrlForWeather);
						bundle.putString("name", "天气预报");
						Intent intent = new Intent(XmsFirstActivity.this,
								XmsWebActivity.class);
						intent.putExtras(bundle);
						startActivity(intent);
					}
				}
				
			}
		});
	}

	@Override
	public void onLogin(int position) {
		// 添加签约关系
		if (position == 9) {
			Intent intent = new Intent(getBaseContext(), MessageActivity.class);
			startActivity(intent);
		}
		// 签约一览
		if (position == 1) {
			Intent intent = new Intent(getBaseContext(),
					SignOverViewActivity.class);
			startActivity(intent);
		}
		/*
		 * 签约管理 登陆成功 回调
		 */
		if (position == 0) {
			showCostDialog();
		}
	}

	@Override
	public void onLogin() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCancle() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onError() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onException() {
		// TODO Auto-generated method stub

	}

	// 获取 用户 附加 信息（客户 号 、身份 证）
	private void requestBocopForUseridQuery() {
		// TODO Auto-generated method stub
		Gson gson = new Gson();
		Map<String, String> map = new HashMap<String, String>();
		map.put("USRID", LoginUtil.getUserId(this));
		final String strGson = gson.toJson(map);

		BocOpUtil bocOpUtil = new BocOpUtil(this);
		bocOpUtil.postOpboc(strGson, TransactionValue.SA0053,
				new CallBackBoc() {

					@Override
					public void onSuccess(String responStr) {
						Log.i("tag1", responStr);
						try {

							Map<String, String> map;
							map = JsonUtils.getMapStr(responStr);
							strCusName = map.get("cusname").toString();
							strIdNo = map.get("idno");
							strCusid = map.get("cusid");
							Log.i("tag", "名字：" + strCusName + "，身份证好："
									+ strIdNo + "客户 号 ：" + strCusid);
							if (strCusName.length() > 0
									&& strIdNo.length() > 10) {
								// strOwnerName = "罗阳";
								// strIdNo = "362202198702140010";
								Intent intent = new Intent(
										XmsFirstActivity.this,
										FinanceSignContractActivity.class);
								Bundle bundle = new Bundle();
								bundle.putString("TITLE", "签约");
								bundle.putString("COST_TYPE",
										costType.getCostName());
								bundle.putString("TYPE_CODE",
										costType.getTypeCode());
								bundle.putString("CUS_NAME", strCusName);
								bundle.putString("ID_NO", strIdNo);
								bundle.putString("CUS_ID", strCusid);
								intent.putExtra("BUNDLE", bundle);
								startActivity(intent);
								// CustomProgressDialog.showBocRegisterSetDialog(CarAddActivity.this);
							} else {
								CustomProgressDialog
										.showBocRegisterSetDialog(XmsFirstActivity.this);
								// Toast.makeText(CarAddActivity.this,
								// "请前往中银开发平台实名认证", Toast.LENGTH_LONG).show();
							}

						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					@Override
					public void onStart() {
						Log.i("tag", "发送GSON数据：" + strGson);
					}

					@Override
					public void onFinish() {

					}

					@Override
					public void onFailure(String responStr) {
						CspUtil.onFailure(XmsFirstActivity.this, responStr);
					}
				});
	}

	/**
	 * 请求用户列表
	 */
	private void requestCspForUser(String typeCode) {
		try {
			// 生成CSP XML报文
			// 水费01，电费02，煤气费03，有线电视04，移动通讯05,金融资产07
			CspXmlXms004 cspXmlXms004 = new CspXmlXms004(
					LoginUtil.getUserId(this), typeCode);
			String strXml = cspXmlXms004.getCspXml();
			// 生成MCIS报文
			Mcis mcis = new Mcis(strXml, TransactionValue.CSPSZF);
			final byte[] byteMessage = mcis.getMcis();
			// 发送报文
			CspUtil cspUtil = new CspUtil(this);
			Log.i("tag", "发送报文： " + new String(byteMessage, "GBK"));
			cspUtil.postCspLogin(byteMessage, new CallBack() {
				@Override
				public void onSuccess(String responStr) {
					Message msg = new Message();
					msg.what = USER_LIST_SUCCESS;
					msg.obj = responStr;
					mHandler.sendMessage(msg);
				}

				@Override
				public void onFinish() {

				}

				@Override
				public void onFailure(String responStr) {
					Message msg = new Message();
					msg.what = USER_FAILED;
					msg.obj = responStr;
					mHandler.sendMessage(msg);
				}
			});
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private void showCostDialog() {
		final CostTypeDialog costDialog = new CostTypeDialog(
				XmsFirstActivity.this);
		costDialog.show(new CostTypeOnClickListener() {

			@Override
			public void OnCostTypeClick(DialogCostType costType) {
				costDialog.dismiss();
				Log.i("tag", "签约类型" + costType.getTypeCode());
				XmsFirstActivity.this.costType = costType;
				if ("07".equals(costType)) {
					requestBocopForUseridQuery();
				} else {
					requestCspForUser(costType.getTypeCode());
				}
			}
		});
	}

}
