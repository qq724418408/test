package com.bocop.xms.activity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.base.BaseAdapter;
import com.boc.jx.base.BaseApplication;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.boc.jx.constants.Constants;
import com.boc.jx.tools.DialogUtil;
import com.boc.jx.view.swipemenulistview.SwipeMenu;
import com.boc.jx.view.swipemenulistview.SwipeMenuCreator;
import com.boc.jx.view.swipemenulistview.SwipeMenuItem;
import com.boc.jx.view.swipemenulistview.SwipeMenuListView;
import com.boc.jx.view.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.activity.BMJFActivity;
import com.bocop.jxplatform.activity.CreditCardActivity;
import com.bocop.jxplatform.activity.WebActivity;
import com.bocop.jxplatform.adapter.XmsItemAdapter;
import com.bocop.jxplatform.config.BocSdkConfig;
import com.bocop.jxplatform.config.TransactionValue;
import com.bocop.jxplatform.util.BocOpUtil;
import com.bocop.jxplatform.util.BocOpUtil.CallBackBoc;
import com.bocop.jxplatform.util.BocopDialog;
import com.bocop.jxplatform.util.CspUtil;
import com.bocop.jxplatform.util.CspUtil.CallBack;
import com.bocop.jxplatform.util.CustomProgressDialog;
import com.bocop.jxplatform.util.JsonUtils;
import com.bocop.jxplatform.util.LoginUtil;
import com.bocop.jxplatform.util.LoginUtilAnother;
import com.bocop.jxplatform.util.LoginUtilAnother.ILoginListener;
import com.bocop.jxplatform.util.Mcis;
import com.bocop.xms.bean.DialogCostType;
import com.bocop.xms.bean.MessageCostType;
import com.bocop.xms.bean.MessageList;
import com.bocop.xms.bean.MessageResponse;
import com.bocop.xms.bean.UserResponse;
import com.bocop.xms.tools.ViewHolder;
import com.bocop.xms.utils.FormsUtil;
import com.bocop.xms.utils.SharedPreferencesUtils;
import com.bocop.xms.utils.XStreamUtils;
import com.bocop.xms.view.CostTypeDialog;
import com.bocop.xms.view.CostTypeDialog.CostTypeOnClickListener;
import com.bocop.xms.xml.CspXmlXms004;
import com.bocop.xms.xml.CspXmlXms005;
import com.google.gson.Gson;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@ContentView(R.layout.xms_activity_message)
public class MessageActivity extends BaseActivity implements ILoginListener
// implements OnScrollListener
{

	@ViewInject(R.id.tv_titleName)
	private TextView tvTitleName;

	@ViewInject(R.id.tvMessageError)
	private TextView tvError;

	@ViewInject(R.id.lv_Message)
	private SwipeMenuListView lvMessage;

	@ViewInject(R.id.vLine)
	private View vLine;
	
	
	@ViewInject(R.id.gvSign)
	private GridView gvSign;
	@ViewInject(R.id.gvBankSer)
	private GridView gvBankSer;
	@ViewInject(R.id.gvOtherSer)
	private GridView gvOtherSer;
	
	private BaseApplication baseApplication = BaseApplication.getInstance();
	
//	@ViewInject(R.id.lvadvice)
//	private ListView traListView;
//	
//	TrafficMainAdapter adviceAdapter;

	private BaseAdapter<MessageCostType> adapter;
	private List<MessageCostType> list = new ArrayList<>();
//	List<PerFunctionBean> traDates = new ArrayList<PerFunctionBean>();
//	TrafficMainAdapter traAdapter;

	/** 当前请求页码 */
	private int pageIndex = 0;

	private int flag;
	
	String strCusName;
	String strIdNo;
	String strCusid;
	private static final int MSG_SUCCESS = 0;
	private static final int MSG_FAILED = 1;
	private static final int USER_LIST_SUCCESS = 2;
	private static final int USER_FAILED = 3;
	private DialogCostType costType;
	
	private XmsItemAdapter signAdapter;
	private XmsItemAdapter bankAdapter;
	private XmsItemAdapter otherAdapter;
	
	
	private PopupWindow guidingView;
	private ImageView ivRefreshIntro;
	private Button btnKnown;
	private View vPerch;
	private SharedPreferencesUtils sp = new SharedPreferencesUtils(this, BocSdkConfig.FLAG_PROGRESS);
	
	private Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_SUCCESS:
				// TODO 上拉加载
				// footerView.setVisibility(View.GONE);
				String content = (String) msg.obj;
				MessageResponse messageResponse = XStreamUtils.getFromXML(content, MessageResponse.class);
				if (messageResponse != null) {
					if ("01".equals(messageResponse.getConstHead().getErrCode())) {
						lvMessage.setVisibility(View.GONE);
						tvError.setVisibility(View.VISIBLE);
						tvError.setText(messageResponse.getConstHead().getErrMsg());
					} else {
						if ("0".equals(getFlagProgress())) {
							vPerch.setVisibility(View.VISIBLE);
							ivRefreshIntro.setImageResource(R.drawable.xms_jftx_refresh);
							guidingView.showAsDropDown(vLine);
						} else if ("1".equals(getFlagProgress())) {
							vPerch.setVisibility(View.GONE);
							ivRefreshIntro.setImageResource(R.drawable.xms_jftx_sign);
							guidingView.showAsDropDown(vLine);
						}
						lvMessage.setVisibility(View.VISIBLE);
						tvError.setVisibility(View.GONE);
						MessageList messageList = messageResponse.getMessageList();
						if (messageList != null && messageList.getList() != null) {
							if (pageIndex == 0) {
								list.clear();
							}
							list.addAll(messageList.getList());
							adapter.notifyDataSetChanged();
						} else {
							Toast.makeText(MessageActivity.this, "数据异常", Toast.LENGTH_SHORT).show();
						}
					}
				} else {
					Toast.makeText(MessageActivity.this, "数据异常", Toast.LENGTH_SHORT).show();
				}
				break;
			case MSG_FAILED:
				// TODO 上拉加载
				// footerView.setVisibility(View.GONE);
				String responStr = (String) msg.obj;
				CspUtil.onFailure(MessageActivity.this, responStr);
				break;

			case USER_LIST_SUCCESS:
				content = (String) msg.obj;
				UserResponse userResponse = XStreamUtils.getFromXML(content, UserResponse.class);
				//该用户没有签约
				if ("01".equals(userResponse.getConstHead().getErrCode())) {
					//07:金融资产到期提醒
					Log.i("tag", "没有签约协议");
					if("07".equals(costType.getTypeCode())){
						requestBocopForUseridQuery();
					}
					//水电煤气到期提醒
					else{
						Intent intent = new Intent(MessageActivity.this, SignContractActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("TITLE", "签约");
						bundle.putString("COST_TYPE", costType.getCostName());
						bundle.putString("TYPE_CODE", costType.getTypeCode());
						intent.putExtra("BUNDLE", bundle);
						startActivity(intent);
					}
					//用户已经签约，列表显示
				} else {
					Log.i("tag", "有签约协议" + costType.getTypeCode());
					Intent intent = new Intent(MessageActivity.this, UserManagerActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("COST_TYPE", costType.getCostName());
					bundle.putString("TYPE_CODE", costType.getTypeCode());
					bundle.putString("USER_LIST", (String) msg.obj);
					intent.putExtra("BUNDLE", bundle);
					startActivity(intent);
				}
				break;

			case USER_FAILED:
				responStr = (String) msg.obj;
				CspUtil.onFailure(MessageActivity.this, responStr);
				break;
			}
		};
	};
	//获取 用户 附加 信息（客户 号 、身份 证）
	private void requestBocopForUseridQuery() {
		// TODO Auto-generated method stub
				Gson gson = new Gson();
				Map<String,String> map = new HashMap<String,String>();
				map.put("USRID", LoginUtil.getUserId(this));
				final String strGson = gson.toJson(map);
				
				BocOpUtil bocOpUtil = new BocOpUtil(this);
				bocOpUtil.postOpboc(strGson,TransactionValue.SA0053, new CallBackBoc() {
					
					@Override
					public void onSuccess(String responStr) {
						Log.i("tag1", responStr);
						try {
							
							Map<String,String> map;
							map = JsonUtils.getMapStr(responStr);
							strCusName = map.get("cusname").toString();
							strIdNo = map.get("idno");
							strCusid = map.get("cusid");
							Log.i("tag","名字：" + strCusName + "，身份证好：" + strIdNo + "客户 号 ：" + strCusid);
							if (strCusName.length()>0 && strIdNo.length()>10) {
//								strOwnerName = "罗阳";
//								strIdNo = "362202198702140010";
								Intent intent = new Intent(MessageActivity.this, FinanceSignContractActivity.class);
								Bundle bundle = new Bundle();
								bundle.putString("TITLE", "签约");
								bundle.putString("COST_TYPE", costType.getCostName());
								bundle.putString("TYPE_CODE", costType.getTypeCode());
								bundle.putString("CUS_NAME", strCusName);
								bundle.putString("ID_NO", strIdNo);
								bundle.putString("CUS_ID", strCusid);
								intent.putExtra("BUNDLE", bundle);
								startActivity(intent);
//								CustomProgressDialog.showBocRegisterSetDialog(CarAddActivity.this);
							} else {
								CustomProgressDialog.showBocRegisterSetDialog(MessageActivity.this);
//								Toast.makeText(CarAddActivity.this, "请前往中银开发平台实名认证", Toast.LENGTH_LONG).show();
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
						CspUtil.onFailure(MessageActivity.this, responStr);
					}
				});
	}
	// TODO 上拉加载
	// private int vItemCount = 0;
	// private View footerView;

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		tvTitleName.setText("秘书通");
		flag = 0;

		initView();
		initData();
//		initEvent();
		initListener();
		if(LoginUtil.isLog(this)){
			requestMessage(true);
		}else{
			lvMessage.setVisibility(View.GONE);
			tvError.setVisibility(View.VISIBLE);
			tvError.setText("请登陆后查看消息");
			LoginUtilAnother.authorizeAnother(
					MessageActivity.this, MessageActivity.this,
					1);
		}
		
	}

	@Override
	protected void onResume() {
		super.onResume();
//		requestMessage(true);
	}

	@OnClick({ R.id.iv_imageLeft, R.id.iv_refresh, R.id.iv_Item,R.id.tvMessageError })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_refresh:
			if(LoginUtil.isLog(this)){
				requestMessage(true);
			}else{
				LoginUtilAnother.authorizeAnother(
						MessageActivity.this, MessageActivity.this,
						1);
			}
			
			break;
		case R.id.iv_imageLeft:
			finish();
			break;
		case R.id.iv_Item:
//			if(LoginUtil.isLog(MessageActivity.this)){
//				showCostDialog();
//			}else{
//				LoginUtilAnother.authorizeAnother(
//						MessageActivity.this, MessageActivity.this,
//						11);
//			}
		case R.id.tvMessageError:
			if(tvError.getText().toString().equals("请登陆后查看消息")){
				LoginUtilAnother.authorizeAnother(
						MessageActivity.this, MessageActivity.this,
						1);
			}
			
//			final CostTypeDialog costDialog = new CostTypeDialog(MessageActivity.this);
//			costDialog.show(new CostTypeOnClickListener() {
//
//				@Override
//				public void OnCostTypeClick(DialogCostType costType) {
//					costDialog.dismiss();
//					Log.i("tag", "签约类型" +  costType.getTypeCode());
//					MessageActivity.this.costType = costType;
//					if("07".equals(costType)){
//						requestBocopForUseridQuery();
//					}
//					else{
//						requestCspForUser(costType.getTypeCode());
//					}
//				}
//			});
			break;

		default:
			break;
		}
	}

	private void initView() {


		View guidingView = getLayoutInflater().inflate(R.layout.xms_popwindow_guidingview, null);
		RelativeLayout reGuidingView = (RelativeLayout) guidingView.findViewById(R.id.reGuidingView);

		ivRefreshIntro = (ImageView) guidingView.findViewById(R.id.ivRefreshIntro);
		btnKnown = (Button) guidingView.findViewById(R.id.btnKnown);
		vPerch = guidingView.findViewById(R.id.vPerch);
		initPopListener();
		reGuidingView.getBackground().setAlpha(80);
		FormsUtil.getDisplayMetrics(this);
		this.guidingView = new PopupWindow(guidingView, FormsUtil.SCREEN_WIDTH, FormsUtil.SCREEN_HEIGHT);

		SwipeMenuCreator menuCreator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
				deleteItem.setBackground(R.color.redLight);
				deleteItem.setWidth(dp2px(90));
				deleteItem.setIcon(R.drawable.ic_delete);
				menu.addMenuItem(deleteItem);

			}
		};

		lvMessage.setMenuCreator(menuCreator);

	}
	

	private void initData() {
		if (TextUtils.isEmpty(getFlagProgress())) {
			setFlagProgress("0");
		}

		lvMessage.setAdapter(
				adapter = new BaseAdapter<MessageCostType>(MessageActivity.this, list, R.layout.xms_item_message) {

					@Override
					public void viewHandler(int position, MessageCostType t, View convertView) {
						ImageView ivType = ViewHolder.get(convertView, R.id.ivType);
						TextView tvText = ViewHolder.get(convertView, R.id.tvText);
						LinearLayout llMessage = ViewHolder.get(convertView, R.id.llMessage);

						if (null != t) {
							String costType = "";
							if ("01".equals(t.getType())) {
								ivType.setImageResource(R.drawable.icon_secretary_message_sf);
								costType = "水费";
							} else if ("02".equals(t.getType())) {
								ivType.setImageResource(R.drawable.icon_secretary_message_df);
								costType = "电费";
							} else if ("03".equals(t.getType())) {
								ivType.setImageResource(R.drawable.icon_secretary_message_mqf);
								costType = "燃气费";
							} else if ("04".equals(t.getType())) {
								ivType.setImageResource(R.drawable.icon_secretary_message_yxds);
								costType = "有线电视费";
							} else if ("05".equals(t.getType())) {
								ivType.setImageResource(R.drawable.icon_secretary_message_ydtx);
								costType = "移动通讯";
							} else if ("06".equals(t.getType())) {
								ivType.setImageResource(R.drawable.icon_secretary_message_jt);
								costType = "交通缴费";
							}else if ("07".equals(t.getType())) {
								ivType.setImageResource(R.drawable.icon_secretary_message_fin);
								costType = "存款 理财";
							}
							
							tvText.setText(t.getPush_text());
//							String cost = t.getCost();
//							if (!"交通缴费".equals(costType)) {
//								tvText.setText("您需要缴纳" + costType + cost + "元，点击进行缴纳。");
//								// tvCostType.setText(costType);
//								// tvCost.setText(t.getCost());
//							} else {
//								if (cost.contains(".")) {
//									cost = cost.substring(0, cost.indexOf("."));
//								}
//								tvText.setText("您有" + cost + "笔交通违法记录，请到交通助手中处理。");
//								llMessage.setClickable(false);
//							}
						}
					}
				});
	}
	
	private void otherSwitch(int position){
	switch(position){
	//股市行情
	case 0:
		Bundle bundleConsult = new Bundle();
		bundleConsult.putString("url", Constants.xmsUrlForMarket);
		bundleConsult.putString("name", "股市行情");
		Intent intentConsult = new Intent(MessageActivity.this,
				WebActivity.class);
		intentConsult.putExtras(bundleConsult);
		startActivity(intentConsult);
		break;
	//和讯财经
	case 1:
		Bundle bundleHx = new Bundle();
		bundleHx.putString("url", Constants.xmsUrlForHx);
		bundleHx.putString("name", "和讯财经");
		Intent intentHx = new Intent(MessageActivity.this,
				WebActivity.class);
		intentHx.putExtras(bundleHx);
		startActivity(intentHx);
		break;
	//天气预报
	case 2:
		Bundle bundleWeather = new Bundle();
		bundleWeather.putString("url", Constants.xmsUrlForWeather);
		bundleWeather.putString("name", "天气预报");
		Intent intentWeather = new Intent(MessageActivity.this,
				WebActivity.class);
		intentWeather.putExtras(bundleWeather);
		startActivity(intentWeather);
		break;
	//美食团购
	case 3:
		Bundle bundleDzdp= new Bundle();
		bundleDzdp.putString("url", Constants.xmsUrlForDzdp);
		bundleDzdp.putString("name", "美食团购");
		Intent intentDzdp = new Intent(MessageActivity.this,
				WebActivity.class);
		intentDzdp.putExtras(bundleDzdp);
		startActivity(intentDzdp);
		break;	
	//打车服务
	case 4:
		Bundle bundleDidi= new Bundle();
		bundleDidi.putString("url", Constants.xmsUrlForDidi);
		bundleDidi.putString("name", "打车服务");
		Intent intentDidi = new Intent(MessageActivity.this,
				WebActivity.class);
		intentDidi.putExtras(bundleDidi);
		startActivity(intentDidi);
		break;
	//购机票 
	case 5:
		
		Bundle bundleFlight= new Bundle();
		bundleFlight.putString("url", Constants.xmsUrlForFight);
		bundleFlight.putString("name", "购机票");
		Intent intentFlight = new Intent(MessageActivity.this,
				WebActivity.class);
		intentFlight.putExtras(bundleFlight);
		startActivity(intentFlight);
		break;
	//购火车票
	case 6:
		Bundle bundleTrain= new Bundle();
		bundleTrain.putString("url", Constants.xmsUrlForTrain);
		bundleTrain.putString("name", "购火车票");
		Intent intentTrain = new Intent(MessageActivity.this,
				WebActivity.class);
		intentTrain.putExtras(bundleTrain);
		startActivity(intentTrain);
		break;
		
	//购汽车票
	case 7:
		Bundle bundle = new Bundle();
		bundle.putString("url", Constants.xmsUrlForLt100);
		bundle.putString("name", "购汽车票");
		Intent intent = new Intent(MessageActivity.this,
				WebActivity.class);
		intent.putExtras(bundle);
		startActivity(intent);
		break;
	
	//地图服务
	case 8:
		Bundle bundleBaidu= new Bundle();
		bundleBaidu.putString("url", Constants.xmsUrlForBaidu);
		bundleBaidu.putString("name", "地图服务");
		Intent intentBaidu = new Intent(MessageActivity.this,
				WebActivity.class);
		intentBaidu.putExtras(bundleBaidu);
		startActivity(intentBaidu);
		break;
	case 9:
		Bundle bundle58Home= new Bundle();
		bundle58Home.putString("url", Constants.xmsUrlFor58Home);
		bundle58Home.putString("name", "家政服务");
		Intent intent58Home = new Intent(MessageActivity.this,
				WebActivity.class);
		intent58Home.putExtras(bundle58Home);
		startActivity(intent58Home);
		break;
		//电影票
	case 10:
		Bundle bundleSpider= new Bundle();
		bundleSpider.putString("url", Constants.xmsUrlForSpider);
		bundleSpider.putString("name", "电影票");
		Intent intentSpider = new Intent(MessageActivity.this,
				WebActivity.class);
		intentSpider.putExtras(bundleSpider);
		startActivity(intentSpider);
		break;
	}
	}

	private void initListener() {

		signAdapter = new XmsItemAdapter(this, R.array.xmssign, "0");
		bankAdapter = new XmsItemAdapter(this, R.array.xmsbankser,
				"1");
		otherAdapter = new XmsItemAdapter(this, R.array.xmsotherser, "2");
		
		gvSign.setAdapter(signAdapter);
		gvBankSer.setAdapter(bankAdapter);
		gvOtherSer.setAdapter(otherAdapter);
		
		gvSign.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(baseApplication.isNetStat()){
					switch(position){
					//增加签约管理 
					case 0:
						if(LoginUtil.isLog(MessageActivity.this)){
							showCostDialog();
						}else{
							LoginUtilAnother.authorizeAnother(
									MessageActivity.this, MessageActivity.this,
									11);
						}
						
						break;
					//签约一览
					case 1:
						if(LoginUtil.isLog(MessageActivity.this)){
							Intent intent = new Intent(MessageActivity.this,
									SignOverViewActivity.class);
							startActivity(intent);
						}else{
							LoginUtilAnother.authorizeAnother(
									MessageActivity.this, MessageActivity.this,
									12);
						}
						
						break;
					}
				}else{
					CustomProgressDialog
					.showBocNetworkSetDialog(MessageActivity.this);
				}
			}
			
		});
		
		gvBankSer.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(baseApplication.isNetStat()){
					switch(position){
					//ATM分布 
					case 0:
						Bundle bundleATM = new Bundle();
						bundleATM.putString("url", Constants.xmsUrlForATM);
						bundleATM.putString("name", "ATM分布");
						Intent intentATM = new Intent(MessageActivity.this,
								WebActivity.class);
						intentATM.putExtras(bundleATM);
						startActivity(intentATM);
						break;
					//网点查询
					case 1:
						
						Bundle bundleOrg = new Bundle();
						bundleOrg.putString("url", Constants.xmsUrlForOrg);
						bundleOrg.putString("name", "网点查询");
						Intent intentOrg = new Intent(MessageActivity.this,
								WebActivity.class);
						intentOrg.putExtras(bundleOrg);
						startActivity(intentOrg);
						break;
					//网点预约
					case 2:
						Bundle bundle = new Bundle();
						bundle.putString("url", Constants.xmsUrlForDotbooking);
						bundle.putString("name", "网点预约");
						Intent intentDotbook = new Intent(MessageActivity.this,
								WebActivity.class);
						intentDotbook.putExtras(bundle);
						startActivity(intentDotbook);
						break;
					//信用卡积分
					case 3:
						Intent intentDebitCard = new Intent(MessageActivity.this,
								CreditCardActivity.class);
						startActivity(intentDebitCard);
						break;	
					//存贷款利率
					case 4:
						Intent intentRate = new Intent(MessageActivity.this,
								RateActivity.class);
						startActivity(intentRate);
						break;
					//外币牌价
					case 5:
						Intent intentExchange = new Intent(MessageActivity.this,
								ExchangeActivity.class);
						startActivity(intentExchange);
						break;
					//基金净值
					case 6:
						Intent intentFun = new Intent(MessageActivity.this,
								FundActivity.class);
						startActivity(intentFun);
						break;
						
					//中银咨询
					case 7:
						Bundle bundleMarket = new Bundle();
						bundleMarket.putString("url", Constants.xmsUrlForConsult);
						bundleMarket.putString("name", "中银咨询");
						Intent intentMarket = new Intent(MessageActivity.this,
								WebActivity.class);
						intentMarket.putExtras(bundleMarket);
						startActivity(intentMarket);
						break;
					}
					
					}else{
					CustomProgressDialog
					.showBocNetworkSetDialog(MessageActivity.this);
				}
			}
			
		});
		
		gvOtherSer.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				final int position2 = position;
				// TODO Auto-generated method stub
				if(baseApplication.isNetStat()){
					
					//免责申明
					
					BocopDialog dialog = new BocopDialog(MessageActivity.this, "免责声明","\t\t秘书通引用的第三方服务网址、商户及商品信息，意在为客户提供方便快捷的服务链接，所有服务均由第三方服务商提供，相关服务和责任将由第三方承担，如在使用服务的过程中有任何问题和纠纷，请您直接与第三方服务商沟通解决。");
					
					if(flag == 1){
						otherSwitch(position2);
					}else{
						dialog.setPositiveButton(new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								flag = 1;
								otherSwitch(position2);
								dialog.cancel();
							}
						}, "同意");
						dialog.setNegativeButton(new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();
							}
						}, "不同意");
						if (!MessageActivity.this.isFinishing()) {
							dialog.show();
						}
					}
					}else{
					CustomProgressDialog
					.showBocNetworkSetDialog(MessageActivity.this);
				}
			}
			
		});
		// TODO 上拉加载
		// lvMessage.setOnScrollListener(this);

		lvMessage.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (getBaseApp().isNetStat()) {
					if (!"06".equals(list.get(position).getType()) && !"07".equals(list.get(position).getType())) {
						Bundle bundle = new Bundle();
						bundle.putString("userId", LoginUtil.getUserId(MessageActivity.this));
						bundle.putString("token", LoginUtil.getToken(MessageActivity.this));
						Intent intent = new Intent(MessageActivity.this, BMJFActivity.class);
						intent.putExtras(bundle);
						startActivity(intent);
					}
				} else {
					CustomProgressDialog.showBocNetworkSetDialog(MessageActivity.this);
				}
			}
		});

		lvMessage.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {

				switch (index) {
				case 0:// 删除
					DialogUtil.showWithTwoBtn(MessageActivity.this, "确定要删除吗？", "确定", "取消",
							new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							list.remove(position);
							if (list.size() == 0) {
								lvMessage.setVisibility(View.GONE);
								tvError.setVisibility(View.VISIBLE);
							} else {
								lvMessage.setVisibility(View.VISIBLE);
								tvError.setVisibility(View.GONE);
								adapter.notifyDataSetChanged();
							}
						}
					}, new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
					break;
				}
				return false;
			}
		});

	}

	private void initPopListener() {
		btnKnown.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ("0".equals(getFlagProgress())) {
					setFlagProgress("1");
					vPerch.setVisibility(View.GONE);
					ivRefreshIntro.setImageResource(R.drawable.xms_jftx_sign);
					guidingView.dismiss();
				} else if ("1".equals(getFlagProgress())) {
					setFlagProgress("2");
					vPerch.setVisibility(View.VISIBLE);
					ivRefreshIntro.setImageResource(R.drawable.xms_jftx_refresh);
					guidingView.dismiss();
				}
			}
		});
	}

	private String getFlagProgress() {
		if (null != sp.getValue(BocSdkConfig.FLAG_PROGRESS, String.class)) {
			return sp.getValue(BocSdkConfig.FLAG_PROGRESS, String.class);
		} else {
			return "";
		}
	}

	private void setFlagProgress(String progress) {
		sp.setValue(BocSdkConfig.FLAG_PROGRESS, progress);
	}

	/**
	 * 请求消息列表
	 */
	private void requestMessage(boolean isRefresh) {
		try {
			if (isRefresh) {
				pageIndex = 0;
			}
			CspXmlXms005 cspXmlXms005 = new CspXmlXms005(LoginUtil.getUserId(this)
			// "developer"
			, String.valueOf(pageIndex));
			String strXml = cspXmlXms005.getCspXml();
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
					msg.what = MSG_SUCCESS;
					msg.obj = responStr;
					mHandler.sendMessage(msg);
				}

				@Override
				public void onFinish() {

				}

				@Override
				public void onFailure(String responStr) {
					Message msg = new Message();
					msg.what = MSG_FAILED;
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
				this);
		costDialog.show(new CostTypeOnClickListener() {

			@Override
			public void OnCostTypeClick(DialogCostType costType) {
				costDialog.dismiss();
				Log.i("tag", "签约类型" +  costType.getTypeCode());
				MessageActivity.this.costType = costType;
				if("07".equals(costType)){
					requestBocopForUseridQuery();
				}
				else{
					requestCspForUser(costType.getTypeCode());
				}
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
			CspXmlXms004 cspXmlXms004 = new CspXmlXms004(LoginUtil.getUserId(this), typeCode);
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

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
	}



	/* (non-Javadoc)
	 * @see com.bocop.jxplatform.util.LoginUtilAnother.ILoginListener#onLogin(int)
	 */
	@Override
	public void onLogin(int position) {
		// TODO Auto-generated method stub
		if(position == 1){
			requestMessage(true);
		}
		if(position == 11){
			showCostDialog();
		}
		if(position == 12){
			Intent intent = new Intent(MessageActivity.this,
					SignOverViewActivity.class);
			startActivity(intent);
		}
	}



	/* (non-Javadoc)
	 * @see com.bocop.jxplatform.util.LoginUtilAnother.ILoginListener#onLogin()
	 */
	@Override
	public void onLogin() {
		// TODO Auto-generated method stub
		
	}



	/* (non-Javadoc)
	 * @see com.bocop.jxplatform.util.LoginUtilAnother.ILoginListener#onCancle()
	 */
	@Override
	public void onCancle() {
		// TODO Auto-generated method stub
		
	}



	/* (non-Javadoc)
	 * @see com.bocop.jxplatform.util.LoginUtilAnother.ILoginListener#onError()
	 */
	@Override
	public void onError() {
		// TODO Auto-generated method stub
		
	}



	/* (non-Javadoc)
	 * @see com.bocop.jxplatform.util.LoginUtilAnother.ILoginListener#onException()
	 */
	@Override
	public void onException() {
		// TODO Auto-generated method stub
		
	}

}
