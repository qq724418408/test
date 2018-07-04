package com.bocop.xms.activity;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.base.BaseAdapter;
import com.boc.jx.baseUtil.cache.CacheBean;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.boc.jx.tools.DialogUtil;
import com.boc.jx.view.swipemenulistview.SwipeMenu;
import com.boc.jx.view.swipemenulistview.SwipeMenuCreator;
import com.boc.jx.view.swipemenulistview.SwipeMenuItem;
import com.boc.jx.view.swipemenulistview.SwipeMenuListView;
import com.boc.jx.view.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.activity.BMJFActivity;
import com.bocop.jxplatform.adapter.QztOrderAdapter;
import com.bocop.jxplatform.adapter.TrafficMainAdapter;
import com.bocop.jxplatform.bean.PerFunctionBean;
import com.bocop.jxplatform.bean.QztOrderBean;
import com.bocop.jxplatform.config.BocSdkConfig;
import com.bocop.jxplatform.config.TransactionValue;
import com.bocop.jxplatform.trafficassistant.AbountActivity;
import com.bocop.jxplatform.trafficassistant.CarAddActivity;
import com.bocop.jxplatform.trafficassistant.MyPeccancyActivity;
import com.bocop.jxplatform.trafficassistant.TrafficAssistantMainActivity;
import com.bocop.jxplatform.trafficassistant.TrafficQuickPayActivity;
import com.bocop.jxplatform.util.CspUtil;
import com.bocop.jxplatform.util.BocOpUtil.CallBackBoc;
import com.bocop.jxplatform.util.CspUtil.CallBack;
import com.bocop.jxplatform.util.ActivityForResultCode;
import com.bocop.jxplatform.util.BocOpUtil;
import com.bocop.jxplatform.util.CustomProgressDialog;
import com.bocop.jxplatform.util.JsonUtils;
import com.bocop.jxplatform.util.LoginUtil;
import com.bocop.jxplatform.util.LoginUtilAnother;
import com.bocop.jxplatform.util.Mcis;
import com.bocop.qzt.QztOrderActivity;
import com.bocop.xms.adapter.XmsOverViewAdapter;
import com.bocop.xms.bean.DialogCostType;
import com.bocop.xms.bean.MessageCostType;
import com.bocop.xms.bean.MessageList;
import com.bocop.xms.bean.MessageResponse;
import com.bocop.xms.bean.OverViewBean;
import com.bocop.xms.bean.OverViewList;
import com.bocop.xms.bean.OverViewResponse;
import com.bocop.xms.bean.OverViewType;
import com.bocop.xms.bean.UserResponse;
import com.bocop.xms.tools.ViewHolder;
import com.bocop.xms.utils.FormsUtil;
import com.bocop.xms.utils.SharedPreferencesUtils;
import com.bocop.xms.utils.XStreamUtils;
import com.bocop.xms.view.CostTypeDialog;
import com.bocop.xms.view.CostTypeDialog.CostTypeOnClickListener;
import com.bocop.xms.xml.CspXmlXms004;
import com.bocop.xms.xml.CspXmlXms005;
import com.bocop.xms.xml.CspXmlXms007;
import com.google.gson.Gson;

@ContentView(R.layout.xms_activity_signoverview)
public class SignOverViewActivity extends BaseActivity
{

	@ViewInject(R.id.tv_titleName)
	private TextView tvTitleName;

	@ViewInject(R.id.tvError)
	private TextView tvError;
	
	@ViewInject(R.id.lv_OverView)
	private ListView xmsOverViewLv;
	
	@ViewInject(R.id.iv_Item)
	private ImageView ivItem;
	
	@ViewInject(R.id.iv_refresh)
	private ImageView ivRefresh;

	@ViewInject(R.id.vLine)
	private View vLine;
	
	
	XmsOverViewAdapter xmsOverViewAdapter;
	List<OverViewType> overViewList;
	List<Map<String,Object>> mapList;
	
	
	private BaseAdapter<MessageCostType> adapter;
	private List<OverViewBean> list = new ArrayList<>();

	/** 当前请求页码 */
	private int pageIndex = 0;

	private static final int OVERVIEW_SUCCESS = 0;
	private static final int OVERVIEW_FAILED = 1;
	
	private static final int SIGN_LIST_SUCCESS = 3;
	private static final int SIGN_FAILED = 4;
	private DialogCostType costType;
	String strCusName;
	String strIdNo;
	String strCusid;
	private Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case OVERVIEW_SUCCESS:
				String content = (String) msg.obj;
				OverViewResponse overViewResponse = XStreamUtils.getFromXML(content, OverViewResponse.class);
				if (overViewResponse != null) {
					if ("01".equals(overViewResponse.getConstHead().getErrCode())) {
						xmsOverViewLv.setVisibility(View.GONE);
						tvError.setVisibility(View.VISIBLE);
						tvError.setText(overViewResponse.getConstHead().getErrMsg());
					} else {
						tvError.setVisibility(View.GONE);
						overViewList = overViewResponse.getOverViewList().getList();
						if (overViewList != null) {
							xmsOverViewAdapter = new XmsOverViewAdapter(SignOverViewActivity.this, overViewList, R.layout.xms_overview_list);
							xmsOverViewLv.setAdapter(xmsOverViewAdapter);
							Log.i("tag", "xmsOverViewAdapter");
						} else {
							Toast.makeText(SignOverViewActivity.this, "数据异常", Toast.LENGTH_SHORT).show();
						}
					}
				} else {
					Toast.makeText(SignOverViewActivity.this, "数据异常", Toast.LENGTH_SHORT).show();
				}
				break;
			case OVERVIEW_FAILED:
				String responStr = (String) msg.obj;
				CspUtil.onFailure(SignOverViewActivity.this, responStr);
				break;
				
			case SIGN_LIST_SUCCESS:
				String contentSign = (String) msg.obj;
				UserResponse userResponse = XStreamUtils.getFromXML(contentSign, UserResponse.class);
				//该用户没有签约
				if ("01".equals(userResponse.getConstHead().getErrCode())) {
					//07:金融资产到期提醒
					Log.i("tag", "没有签约协议");
					if("07".equals(costType.getTypeCode())){
						requestBocopForUseridQuery();
					}
					//水电煤气到期提醒
					else{
						Intent intent = new Intent(SignOverViewActivity.this, SignContractActivity.class);
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
					Intent intent = new Intent(SignOverViewActivity.this, UserManagerActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("COST_TYPE", costType.getCostName());
					bundle.putString("TYPE_CODE", costType.getTypeCode());
					bundle.putString("USER_LIST", (String) msg.obj);
					intent.putExtra("BUNDLE", bundle);
					startActivity(intent);
				}
				break;

			case SIGN_FAILED:
				String strResponseSign = (String) msg.obj;
				CspUtil.onFailure(SignOverViewActivity.this, strResponseSign);
				break;

			}
		};
	};
	private ImageView ivRefreshIntro;
	private Button btnKnown;
	private View vPerch;
	private SharedPreferencesUtils sp = new SharedPreferencesUtils(this, BocSdkConfig.FLAG_PROGRESS);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		tvTitleName.setText("签约一览");
		ivItem.setVisibility(View.INVISIBLE);
		ivRefresh.setVisibility(View.INVISIBLE);
		initData();
		requestOverView(false);
		initEvent();
	}
	
	private String nameToType(String strName){
		if ("水费".equals(strName)) {
			return "01";
		} else if ("电费".equals(strName)) {
			return "02";
		} else if ("燃气费".equals(strName)) {
			return "03";
		} else if ("有线电视".equals(strName)) {
			return "04";
		} else if ("移动通讯".equals(strName)) {
			return "05";
		} else if ("交通缴费".equals(strName)) {
			return "06";
		}else if ("存款到期".equals(strName) || "理财到期".equals(strName) || "存款理财".equals(strName)) {
			return "07";
		}
		return "07";
	}
	private void initEvent(){
		xmsOverViewLv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
//				Log.i("tag22", xmsOverViewLv.getItemAtPosition(position).toString());
//				String strXmsName;
//				String strXmsType;
				DialogCostType clickCostType = new DialogCostType();
				String strXmsUser;
				TextView xmsType = (TextView) view.findViewById(R.id.xms_type);
				TextView xmsUser = (TextView) view.findViewById(R.id.xms_username);
				TextView xmsUserReal = (TextView) view.findViewById(R.id.xms_usernamereal);
//				clickCostType.setCostName(xmsType.getText().toString());
				clickCostType.setCostName(xmsUserReal.getText().toString());
				clickCostType.setTypeCode(nameToType(xmsType.getText().toString()));
				strXmsUser = xmsUser.getText().toString();
				//没签约
				SignOverViewActivity.this.costType = clickCostType;
				if(!strXmsUser.contains("用户号")){
					if("07".equals(costType.getTypeCode())){
						requestBocopForUseridQuery();
					}
					else{
						requestCspForUser(costType.getTypeCode());
					}
				}
				else{
					Log.i("tag", "有签约协议" + costType.getTypeCode());
					Intent intent = new Intent(SignOverViewActivity.this, UserManagerActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("COST_TYPE", costType.getCostName());
					bundle.putString("TYPE_CODE", costType.getTypeCode());
//					bundle.putString("USER_LIST", (String) msg.obj);
					intent.putExtra("BUNDLE", bundle);
					startActivity(intent);
				}
				
//				showCostDialog();
			}
		});
	}
	
	private void showCostDialog(){
		final CostTypeDialog costDialog = new CostTypeDialog(SignOverViewActivity.this);
		costDialog.show(new CostTypeOnClickListener() {

			@Override
			public void OnCostTypeClick(DialogCostType costType) {
				costDialog.dismiss();
				Log.i("tag", "签约类型" +  costType.getTypeCode());
				SignOverViewActivity.this.costType = costType;
				if("07".equals(costType)){
					requestBocopForUseridQuery();
				}
				else{
					requestCspForUser(costType.getTypeCode());
				}
			}
		});
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
//		requestOverView(true);
	}

	@OnClick({ R.id.iv_imageLeft, R.id.iv_refresh, R.id.iv_Item })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_refresh:
			requestOverView(true);
			break;
		case R.id.iv_imageLeft:
			finish();
			break;
		default:
			break;
		}
	}


	private void initData() {
		if (TextUtils.isEmpty(getFlagProgress())) {
			setFlagProgress("0");
		}
	}


	private void initPopListener() {
		btnKnown.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ("0".equals(getFlagProgress())) {
					setFlagProgress("1");
					vPerch.setVisibility(View.GONE);
					ivRefreshIntro.setImageResource(R.drawable.xms_jftx_sign);
				} else if ("1".equals(getFlagProgress())) {
					setFlagProgress("2");
					vPerch.setVisibility(View.VISIBLE);
					ivRefreshIntro.setImageResource(R.drawable.xms_jftx_refresh);
//					guidingView.dismiss();
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
	private void requestOverView(boolean isRefresh) {
		try {
			if (isRefresh) {
				if(overViewList != null)
				{
					overViewList.clear();
				}
			}
			CspXmlXms007 cspXmlXms007 = new CspXmlXms007(LoginUtil.getUserId(this)
			// "developer"
			, String.valueOf(pageIndex));
			String strXml = cspXmlXms007.getCspXml();
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
					msg.what = OVERVIEW_SUCCESS;
					msg.obj = responStr;
					mHandler.sendMessage(msg);
				}

				@Override
				public void onFinish() {

				}

				@Override
				public void onFailure(String responStr) {
					Message msg = new Message();
					msg.what = OVERVIEW_FAILED;
					msg.obj = responStr;
					mHandler.sendMessage(msg);
				}
			});
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
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
					msg.what = SIGN_LIST_SUCCESS;
					msg.obj = responStr;
					mHandler.sendMessage(msg);
				}

				@Override
				public void onFinish() {

				}

				@Override
				public void onFailure(String responStr) {
					Message msg = new Message();
					msg.what = SIGN_FAILED;
					msg.obj = responStr;
					mHandler.sendMessage(msg);
				}
			});
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
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
								Intent intent = new Intent(SignOverViewActivity.this, FinanceSignContractActivity.class);
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
								CustomProgressDialog.showBocRegisterSetDialog(SignOverViewActivity.this);
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
						CspUtil.onFailure(SignOverViewActivity.this, responStr);
					}
				});
	}

}
