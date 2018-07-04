package com.bocop.xms.fragment;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.boc.jx.base.BaseFragment;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.constants.Constants;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.activity.CreditCardActivity;
import com.bocop.jxplatform.activity.WebActivity;
import com.bocop.jxplatform.adapter.XmsItemAdapter;
import com.bocop.jxplatform.config.TransactionValue;
import com.bocop.jxplatform.util.BocOpUtil;
import com.bocop.jxplatform.util.BocOpUtil.CallBackBoc;
import com.bocop.jxplatform.util.CspUtil;
import com.bocop.jxplatform.util.CspUtil.CallBack;
import com.bocop.jxplatform.util.CustomProgressDialog;
import com.bocop.jxplatform.util.JsonUtils;
import com.bocop.jxplatform.util.LoginUtil;
import com.bocop.jxplatform.util.Mcis;
import com.bocop.xms.activity.ExchangeActivity;
import com.bocop.xms.activity.FinanceSignContractActivity;
import com.bocop.xms.activity.FundActivity;
import com.bocop.xms.activity.RateActivity;
import com.bocop.xms.activity.SignContractActivity;
import com.bocop.xms.activity.UserManagerActivity;
import com.bocop.xms.activity.XmsMainActivity;
import com.bocop.xms.bean.UserResponse;
import com.bocop.xms.utils.XStreamUtils;
import com.bocop.xms.xml.CspXmlXms004;
import com.bocop.xms.xml.CspXmlXmsCom;
import com.bocop.xms.xml.message.MessageBean;
import com.bocop.xms.xml.message.MessageListResp;
import com.bocop.xms.xml.message.MessageListXmlBean;
import com.bocop.xms.xml.sign.SignBean;
import com.bocop.xms.xml.sign.SignListResp;
import com.bocop.xms.xml.sign.SignListXmlBean;
import com.google.gson.Gson;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

/**
 * 
 * 金融服务
 * 
 * @author ftl
 * 
 */
public class BankServiceFragment extends BaseFragment {
	
	@ViewInject(R.id.gvBankSer)
	private GridView gvBankSer;
	
	private XmsItemAdapter bankAdapter;
	private String costName = "";// 缴费类型名称
	private String typeCode = "";//缴费类型编号
	
	private static final int USER_LIST_SUCCESS = 0;
	private static final int USER_FAILED = 1;
	private List<SignBean> signList = new ArrayList<SignBean>();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = initView(R.layout.xms_fragment_bank_service);
		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		requestIsSign();
	}

	@Override
	protected void initData() {
		super.initData();
		bankAdapter = new XmsItemAdapter(baseActivity, R.array.xmsbankser, "1",signList);
		gvBankSer.setAdapter(bankAdapter);
	}

	@Override
	protected void initView() {
		super.initView();
		gvBankSer.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(baseActivity.getBaseApp().isNetStat()){
					switch(position){
					case 0:
						costName = "水费";
						typeCode = "01";
						requestCspForUser(typeCode);
						break;
					case 1:
						costName = "电费";
						typeCode = "02";
						requestCspForUser(typeCode);
						break;
					case 2:
						costName = "燃气费";
						typeCode = "03";
						requestCspForUser(typeCode);
						break;
					case 3:
						costName = "有线电视";
						typeCode = "04";
						requestCspForUser(typeCode);
						break;
					case 4:
						costName = "移动通讯";
						typeCode = "05";
						requestCspForUser(typeCode);
						break;
					case 5:
						costName = "存款理财";
						typeCode = "07";
						requestCspForUser(typeCode);
						break;
					}
				}else{
					CustomProgressDialog.showBocNetworkSetDialog(baseActivity);
				}
			}
			
		});
	}
	
	private Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case USER_LIST_SUCCESS:
				String content = (String) msg.obj;
				UserResponse userResponse = XStreamUtils.getFromXML(content, UserResponse.class);
				//该用户没有签约
				if ("01".equals(userResponse.getConstHead().getErrCode())) {
					//07:金融资产到期提醒
					Log.i("tag", "没有签约协议");
					if("07".equals(typeCode)){
						requestBocopForUseridQuery();
					}
					//水电煤气到期提醒
					else{
						Intent intent = new Intent(baseActivity, SignContractActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("TITLE", "签约");
						bundle.putString("COST_TYPE", costName);
						bundle.putString("TYPE_CODE", typeCode);
						intent.putExtra("BUNDLE", bundle);
						startActivity(intent);
					}
					//用户已经签约，列表显示
				} else {
					Log.i("tag", "有签约协议" + typeCode);
					Intent intent = new Intent(baseActivity, UserManagerActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("COST_TYPE", costName);
					bundle.putString("TYPE_CODE", typeCode);
					bundle.putString("USER_LIST", (String) msg.obj);
					intent.putExtra("BUNDLE", bundle);
					startActivity(intent);
				}
				break;

			case USER_FAILED:
				String responStr = (String) msg.obj;
				CspUtil.onFailure(baseActivity, responStr);
				break;
			}
		};
	};
	
	//获取 用户 附加 信息（客户 号 、身份 证）
	private void requestBocopForUseridQuery() {
		Gson gson = new Gson();
		Map<String,String> map = new HashMap<String,String>();
		map.put("USRID", LoginUtil.getUserId(baseActivity));
		final String strGson = gson.toJson(map);
		BocOpUtil bocOpUtil = new BocOpUtil(baseActivity);
		bocOpUtil.postOpboc(strGson,TransactionValue.SA0053, new CallBackBoc() {
			@Override
			public void onSuccess(String responStr) {
				Log.i("tag1", responStr);
				try {
					Map<String,String> map = JsonUtils.getMapStr(responStr);
					String strCusName = map.get("cusname").toString();
					String strIdNo = map.get("idno");
					String strCusid = map.get("cusid");
					Log.i("tag","名字：" + strCusName + "，身份证好：" + strIdNo + "客户 号 ：" + strCusid);
					if (strCusName.length()>0 && strIdNo.length()>10) {
						Intent intent = new Intent(baseActivity, FinanceSignContractActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("TITLE", "签约");
						bundle.putString("COST_TYPE", costName);
						bundle.putString("TYPE_CODE", typeCode);
						bundle.putString("CUS_NAME", strCusName);
						bundle.putString("ID_NO", strIdNo);
						bundle.putString("CUS_ID", strCusid);
						intent.putExtra("BUNDLE", bundle);
						startActivity(intent);
					} else {
						CustomProgressDialog.showBocRegisterSetDialog(baseActivity);
					}
					
				} catch (Exception e) {
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
				CspUtil.onFailure(baseActivity, responStr);
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
			CspXmlXms004 cspXmlXms004 = new CspXmlXms004(LoginUtil.getUserId(baseActivity), typeCode);
			String strXml = cspXmlXms004.getCspXml();
			// 生成MCIS报文
			Mcis mcis = new Mcis(strXml, TransactionValue.CSPSZF);
			final byte[] byteMessage = mcis.getMcis();
			// 发送报文
			CspUtil cspUtil = new CspUtil(baseActivity);
			Log.i("tag", "请求用户列表发送报文： " + new String(byteMessage, "GBK"));
//			cspUtil.setTest(true);
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
	
	/**
	 * 请求用户是否签约
	 */
	private void requestIsSign(){
		try {
			// 生成CSP XML报文
			String txCode = "MS002008";
			CspXmlXmsCom cspXmlXmsCom = new CspXmlXmsCom(LoginUtil.getUserId(baseActivity), txCode);
			String strXml = cspXmlXmsCom.getCspXml();
			// 生成MCIS报文
			Mcis mcis = new Mcis(strXml, TransactionValue.CSPSZF);
			Log.i("tag", "Mcis");
			final byte[] byteMessage = mcis.getMcis();
			// 发送报文
			CspUtil cspUtil = new CspUtil(baseActivity);
			Log.i("tag", "发送报文： " + new String(byteMessage, "GBK"));
//			cspUtil.setTest(true);
			cspUtil.postCspLogin(new String(byteMessage, "GBK"), new CallBack() {
				
				@Override
				public void onSuccess(String responStr) {

					if(responStr!=null){
						SignListXmlBean signListXmlBean = SignListResp.readStringXml(responStr);
						if(!"00".equals(signListXmlBean.getErrorcode())){
							if (signListXmlBean.getErrorcode().equals("01")) {
								
							}
						}else{
							if(signListXmlBean.getSignListBean()!=null){
								signList.clear();
								signList.addAll(signListXmlBean.getSignListBean());
								if(signList.size()==6){
									signList.add(new SignBean());
									signList.add(new SignBean());
									bankAdapter.setList(signList);
									bankAdapter.notifyDataSetChanged();
									System.out.println("---->>>"+signList);
								}
						
							}
							
						}
					}
				}

				@Override
				public void onFinish() {

				}
				@Override
				public void onFailure(String responStr) {
					Toast.makeText(baseActivity, responStr, Toast.LENGTH_SHORT).show();
				}
			
			});
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	

}
