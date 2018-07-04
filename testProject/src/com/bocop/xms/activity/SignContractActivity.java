package com.bocop.xms.activity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.boc.jx.tools.DialogUtil;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.config.TransactionValue;
import com.bocop.jxplatform.util.CspUtil;
import com.bocop.jxplatform.util.CspUtil.CallBack;
import com.bocop.jxplatform.util.LoginUtil;
import com.bocop.jxplatform.util.Mcis;
import com.bocop.xms.bean.AddressDetail;
import com.bocop.xms.bean.AddressList;
import com.bocop.xms.bean.AddressResponse;
import com.bocop.xms.bean.AreaResponse;
import com.bocop.xms.bean.ConstHead;
import com.bocop.xms.bean.SignContractInfo;
import com.bocop.xms.bean.SignResponse;
import com.bocop.xms.bean.UserInfoResponse;
import com.bocop.xms.utils.KeyboardUtils;
import com.bocop.xms.utils.XStreamUtils;
import com.bocop.xms.view.SignContractInfoDialog;
import com.bocop.xms.view.SignContractInfoDialog.NegativeOnClickListener;
import com.bocop.xms.view.SignContractInfoDialog.PositiveOnClickListener;
import com.bocop.xms.xml.CspXmlXms002;
import com.bocop.xms.xml.CspXmlXms006;
import com.bocop.xms.xml.CspXmlXmsCom;
import com.bocop.xms.xml.remind.EventComResp;
import com.bocop.xms.xml.remind.EventComXmlBean;

@ContentView(R.layout.xms_activity_sign_contract)
public class SignContractActivity extends BaseActivity {

	@ViewInject(R.id.tv_titleName)
	private TextView tv_titleName;
	@ViewInject(R.id.iv_imageLeft)
	private ImageView iv_imageLeft;

	@ViewInject(R.id.tv_PayArea)
	private TextView tvPayArea;
	@ViewInject(R.id.tv_PayUnit)
	private TextView tvPayUnit;
	@ViewInject(R.id.et_UserCode)
	private EditText etUserCode;
	@ViewInject(R.id.btn_Apply)
	private Button btnApply;
	@ViewInject(R.id.tv_OrderDate)
	private TextView tv_OrderDate;
	@ViewInject(R.id.ll_pattern)
	private LinearLayout ll_pattern;// 缴费方式
	@ViewInject(R.id.tv_pattern)
	private TextView tv_pattern;//缴费方式
	@ViewInject(R.id.ll_userCode)
	private LinearLayout ll_userCode;// 用户号码
	@ViewInject(R.id.rl_Extra)
	private RelativeLayout rl_Extra;
	@ViewInject(R.id.tv_Extra)
	private TextView tv_Extra;
	@ViewInject(R.id.et_Extra)
	private EditText et_Extra;
	@ViewInject(R.id.ll_DeviceType)
	private LinearLayout ll_DeviceType;
	@ViewInject(R.id.tv_DeviceType)
	private TextView tv_DeviceType;//设备类型
	@ViewInject(R.id.rl_PinPaiN)
	private RelativeLayout rl_PinPaiN;
	@ViewInject(R.id.tv_PinPaiN)
	private TextView tv_PinPaiN;
	@ViewInject(R.id.ivIcon)
	private ImageView ivIcon;
	@ViewInject(R.id.tvType)
    private TextView tvType;
	private List<AddressList> areaList = new ArrayList<AddressList>();
	private List<AddressDetail> unitList = new ArrayList<AddressDetail>();
	private List<String> dateList = new ArrayList<String>();
	private int datePosition;
	private SignContractInfo contractInfo = new SignContractInfo();
	private boolean COST_TYPE_WIRED_TV = false;
	private String costType;
	private String typeCode;//01
	private String[] pattern = { "用户号码", "智能卡号" };
	private static final int SIGN_AREA_SUCCESS = 0;
	private static final int SIGN_NAME_SUCCESS = 1;
	private static final int SIGN_SAVE_SUCCESS = 2;
	private static final int SIGN_FAILED = 3;
	private Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case SIGN_AREA_SUCCESS:
				String content = (String) msg.obj;
				AreaResponse areaResponse = XStreamUtils.getFromXML(content, AreaResponse.class);
				if (areaResponse != null) {
					AddressResponse addressResponse = areaResponse.getAddressResponse();
					List<AddressList> list = addressResponse.getAddressList();
					if (list != null) {
						areaList.addAll(list);
						for (int i = 0; i < areaList.size(); i++) {
							AddressList addressList = areaList.get(0);
							unitList = addressList.getList();
							tvPayArea.setText(addressList.getCityName());
							List<AddressDetail> detailList = addressList.getList();
							if (detailList != null && detailList.size() > 0) {
								if (detailList.get(0) != null) {
									choosePayUnit(detailList.get(0));
								}
							}
						}
					} else {
						Toast.makeText(SignContractActivity.this, "数据异常", Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(SignContractActivity.this, "数据异常", Toast.LENGTH_SHORT).show();
				}
				break;
			case SIGN_NAME_SUCCESS:
				final SignContractInfo conInfo = (SignContractInfo) msg.obj;
				contractInfo.setName(conInfo.getName());
				final SignContractInfoDialog infoDialog = new SignContractInfoDialog(SignContractActivity.this);
				infoDialog.show(contractInfo, new PositiveOnClickListener() {
					@Override
					public void positiveOnClick() {
						infoDialog.dismiss();
						requestSignSave(conInfo);
					}
				}, new NegativeOnClickListener() {
					@Override 
					public void negativeOnClick() {
						infoDialog.dismiss();
					}
				});
				break;
			case SIGN_SAVE_SUCCESS:
				callMe(SignContractCompleteActivity.class);
				break;
			case SIGN_FAILED:
				String responStr = (String) msg.obj;
				CspUtil.onFailure(SignContractActivity.this, responStr);
				break;
			}
		};
	};
	private SignContractInfo userInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String str = getIntent().getBundleExtra("BUNDLE").getString("TITLE");
		if (!TextUtils.isEmpty(str)) {
			tv_titleName.setText(str);
		}
		
		userInfo = (SignContractInfo) getIntent().getBundleExtra("BUNDLE").getSerializable("USER_INFO");

		costType = getIntent().getBundleExtra("BUNDLE").getString("COST_TYPE");
		typeCode = getIntent().getBundleExtra("BUNDLE").getString("TYPE_CODE");
		tvType.setText(costType);
		initIcon(typeCode);
		System.out.println("-------------->>>costType"+costType);
		System.out.println("-------------->>>typecode"+typeCode);
		if(userInfo!=null){
			tvPayArea.setClickable(false);
			tvPayUnit.setClickable(false);
			etUserCode.setEnabled(false);
			tv_pattern.setClickable(false);
			et_Extra.setEnabled(false);
			tv_DeviceType.setEnabled(false);
			String sysId = userInfo.getSysId();
			//tvPayUnit.setText(agentName);
			if ("08".equals(sysId)) {// 江西省广播电视传输
				ll_pattern.setVisibility(View.VISIBLE);
				if ("用户号码".equals(tv_pattern.getText().toString())) {
					ll_userCode.setVisibility(View.VISIBLE);
					rl_Extra.setVisibility(View.GONE);
				} else if ("智能卡号".equals(tv_pattern.getText().toString())) {
					ll_userCode.setVisibility(View.GONE);
					rl_Extra.setVisibility(View.VISIBLE);
				}
				rl_PinPaiN.setVisibility(View.GONE);
				ll_DeviceType.setVisibility(View.GONE);
			} else if ("09".equals(sysId)) {// 南昌市广播电视传输
				ll_pattern.setVisibility(View.GONE);
				ll_userCode.setVisibility(View.VISIBLE);
				rl_Extra.setVisibility(View.GONE);
				rl_PinPaiN.setVisibility(View.VISIBLE);
				tv_PinPaiN.setText("请选择");
				ll_DeviceType.setVisibility(View.GONE);
			} else if ("02".equals(sysId)) {// 电信
				ll_pattern.setVisibility(View.GONE);
				ll_userCode.setVisibility(View.VISIBLE);
				rl_Extra.setVisibility(View.GONE);
				rl_PinPaiN.setVisibility(View.GONE);
				ll_DeviceType.setVisibility(View.VISIBLE);
				tv_DeviceType.setText("请选择");
			} else {
				ll_pattern.setVisibility(View.GONE);
				ll_userCode.setVisibility(View.VISIBLE);
				rl_Extra.setVisibility(View.GONE);
				rl_PinPaiN.setVisibility(View.GONE);
				ll_DeviceType.setVisibility(View.GONE);
			}
            String orderDate=userInfo.getOrderDate();
            if(orderDate!=null&&orderDate.startsWith("0")){
            	orderDate=orderDate.substring(1,2);
            }
            String devType=userInfo.getDevTyp();
            if("10".equals(devType)){
            	tv_DeviceType.setText("电话通讯费");
            }else if("20".equals(devType)){
            	tv_DeviceType.setText("宽带上网费");
            }
            String subScriberno=userInfo.getSubscriberno();
            if(subScriberno!=null){
            	tv_pattern.setText("智能卡号");
            	ll_userCode.setVisibility(View.GONE);
            	rl_Extra.setVisibility(View.VISIBLE);
            	
            }else{
            	tv_pattern.setText("用户号码");
            	ll_userCode.setVisibility(View.VISIBLE);
            	rl_Extra.setVisibility(View.GONE);
            }
			tvPayArea.setText(userInfo.getArea());
			tvPayUnit.setText(userInfo.getAgentName());
			etUserCode.setText(userInfo.getUserCode());
			tv_OrderDate.setText("每月"+orderDate+"号");
			et_Extra.setText(userInfo.getSubscriberno());
			tv_PinPaiN.setText(userInfo.getPinpaiN());
			
		}else{
			tvPayArea.setClickable(true);
			tvPayUnit.setClickable(true);
			etUserCode.setEnabled(true);
			tv_pattern.setClickable(true);
			tv_DeviceType.setEnabled(true);
			et_Extra.setEnabled(true);

		if (!TextUtils.isEmpty(costType) && "有线电视".equals(costType)) {
			COST_TYPE_WIRED_TV = true;
		}
        
		requestSignArea(typeCode);
		initData();
	
		}
	}
	
	private void initIcon(String type){
		switch (type) {
		case "01":
			ivIcon.setImageResource(R.drawable.xms_icon_sf_small);
			break;
		case "02":
			ivIcon.setImageResource(R.drawable.xms_icon_df_small);
			break;
		case "03":
			ivIcon.setImageResource(R.drawable.xms_icon_rqf_small);
			break;
		case "04":
			ivIcon.setImageResource(R.drawable.xms_icon_yxf_small);
			break;
		case "05":
		    ivIcon.setImageResource(R.drawable.xms_icon_ydf_small);
		    break;
		case "06":
			ivIcon.setImageResource(R.drawable.xms_icon_lc_small);
			break;

		default:
			break;
		}
	}

	private void initData() {
		for (int i = 1; i <= 28; i++) {
			if (i < 10) {
				dateList.add("0" + i);
			} else {
				dateList.add(i + "");
			}
		}
	}

	@OnClick({ R.id.iv_imageLeft, R.id.btn_Apply, R.id.tv_PayArea, R.id.tv_PayUnit, R.id.tv_pattern,
			R.id.tv_DeviceType, R.id.tv_OrderDate, R.id.tv_PinPaiN })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_imageLeft:
			finish();
			break;

		case R.id.tv_PayArea:
			final String[] areaString = new String[areaList.size()];
			for (int i = 0; i < areaString.length; i++) {
				areaString[i] = areaList.get(i).getCityName();
			}
			KeyboardUtils.closeInput(SignContractActivity.this, tvPayArea);
			DialogUtil.showToSelect(SignContractActivity.this, "", areaString, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					tvPayArea.setText(areaString[which]);
					if (areaList.get(which).getList() != unitList) {
						unitList = areaList.get(which).getList();
						tvPayUnit.setText("请选择");
					}
				}
			});
			break;

		case R.id.tv_PayUnit:
			final String[] unitString = new String[unitList.size()];
			for (int i = 0; i < unitString.length; i++) {
				unitString[i] = unitList.get(i).getAgentName();
			}
			KeyboardUtils.closeInput(SignContractActivity.this, tvPayArea);
			DialogUtil.showToSelect(SignContractActivity.this, "", unitString, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					choosePayUnit(unitList.get(which));
				}
			});
			break;

		case R.id.tv_pattern:
			DialogUtil.showToSelect(SignContractActivity.this, "", pattern, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					tv_pattern.setText(pattern[which]);
					if ("用户号码".equals(tv_pattern.getText().toString())) {
						ll_userCode.setVisibility(View.VISIBLE);
						rl_Extra.setVisibility(View.GONE);
					} else if ("智能卡号".equals(tv_pattern.getText().toString())) {
						ll_userCode.setVisibility(View.GONE);
						rl_Extra.setVisibility(View.VISIBLE);
					}
				}
			});

			break;
		case R.id.tv_DeviceType:
			KeyboardUtils.closeInput(SignContractActivity.this, tv_DeviceType);
			final String devString[] = { "电话通讯费", "宽带上网费" };
			DialogUtil.showToSelect(this, "", devString, new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					tv_DeviceType.setText(devString[which]);
				}
			});
			break;

		case R.id.tv_OrderDate:
			KeyboardUtils.closeInput(SignContractActivity.this, tv_OrderDate);
			final String[] dateString = new String[28];
			for (int i = 1; i <= 28; i++) {
				dateString[i - 1] = "每月" + i + "号";
			}
			DialogUtil.showToSelect(this, "", dateString, new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					datePosition = which;
					tv_OrderDate.setText(dateString[which]);
				}
			});
			break;

		case R.id.tv_PinPaiN:
			KeyboardUtils.closeInput(SignContractActivity.this, tv_PinPaiN);
			final String[] pinPaiNString = { "普通有线", "数字有线", "宽带业务" };
			DialogUtil.showToSelect(this, "", pinPaiNString, new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					tv_PinPaiN.setText(pinPaiNString[which]);
				}
			});
			break;

		case R.id.btn_Apply:
			if(userInfo!=null){
				requestSave(userInfo);
			}else{
				if ("请选择".equals(tvPayArea.getText().toString())) {
					Toast.makeText(this, "请选择缴费地区", Toast.LENGTH_SHORT).show();
	
				} else if ("请选择".equals(tvPayUnit.getText().toString())) {
					Toast.makeText(this, "请选择缴费单位", Toast.LENGTH_SHORT).show();
	
				} else if (ll_userCode.getVisibility() == View.VISIBLE
						&& TextUtils.isEmpty(etUserCode.getText().toString())) {
					Toast.makeText(this, "请输入用户号码", Toast.LENGTH_SHORT).show();
	
				} else if (rl_Extra.getVisibility() == View.VISIBLE && TextUtils.isEmpty(et_Extra.getText().toString())) {
					if ("08".equals(contractInfo.getSysId())) {// 江西省广播电视传输
						Toast.makeText(this, "请输入智能卡号/上网账号", Toast.LENGTH_SHORT).show();
					}
				} else if ("请选择".equals(tv_OrderDate.getText().toString())) {
					Toast.makeText(this, "请选择提醒日", Toast.LENGTH_SHORT).show();
	
				} else if (ll_DeviceType.getVisibility() == View.VISIBLE
						&& "请选择".equals(tv_DeviceType.getText().toString())) {
					Toast.makeText(this, "请选择设备类型", Toast.LENGTH_SHORT).show();
	
				} else if (rl_PinPaiN.getVisibility() == View.VISIBLE && "请选择".equals(tv_PinPaiN.getText().toString())) {
					Toast.makeText(this, "请选择缴费项目", Toast.LENGTH_SHORT).show();
	
				} else {
					// contractInfo = new SignContractInfo();
					contractInfo.setArea(tvPayArea.getText().toString());
					// contractInfo.setUnit(tvPayUnit.getText().toString());
					contractInfo.setUserCode(etUserCode.getText().toString());
					contractInfo.setCostType(typeCode);
					// String orderDate = tv_OrderDate.getText().toString();
					contractInfo.setOrderDate(tv_OrderDate.getText().toString());
					contractInfo.setServicetype("");
					if (rl_Extra.getVisibility() == View.VISIBLE) {
						contractInfo.setSubscriberno(et_Extra.getText().toString());
						contractInfo.setUserCode("");
					} else {
						if (COST_TYPE_WIRED_TV) {
							contractInfo.setServicetype("02");
						}
					}
	
					if ("05".equals(contractInfo.getSysId())) {// 景德镇龙源管道燃气,九江深燃天燃气
						contractInfo.setServId("0001");
					}
	
					if (ll_DeviceType.getVisibility() == View.VISIBLE) {
						contractInfo.setDevTyp(tv_DeviceType.getText().toString());
					}
	
					if (rl_PinPaiN.getVisibility() == View.VISIBLE) {
						contractInfo.setPinpaiN(tv_PinPaiN.getText().toString());
					}
					contractInfo.setUserId(LoginUtil.getUserId(this));
	//				final SignContractInfoDialog infoDialog = new SignContractInfoDialog(SignContractActivity.this);
	//				infoDialog.show(contractInfo, new PositiveOnClickListener() {
	//
	//					@Override
	//					public void positiveOnClick() {
	//						infoDialog.dismiss();
	//						requestSignSave(contractInfo);
	//					}
	//				}, new NegativeOnClickListener() {
	//
	//					@Override
	//					public void negativeOnClick() {
	//						infoDialog.dismiss();
	//					}
	//				});
					requestSignUserName(contractInfo);
				}
				break;
			}
		}
	}

	/**
	 * 选择缴费单位
	 * @param addressDetail
	 */
	private void choosePayUnit(AddressDetail addressDetail) {
		String agentName = addressDetail.getAgentName();
		String sysId = addressDetail.getSysId();
		tvPayUnit.setText(agentName);
		if ("08".equals(sysId)) {// 江西省广播电视传输
			ll_pattern.setVisibility(View.VISIBLE);
			if ("用户号码".equals(tv_pattern.getText().toString())) {
				ll_userCode.setVisibility(View.VISIBLE);
				rl_Extra.setVisibility(View.GONE);
			} else if ("智能卡号".equals(tv_pattern.getText().toString())) {
				ll_userCode.setVisibility(View.GONE);
				rl_Extra.setVisibility(View.VISIBLE);
			}
			rl_PinPaiN.setVisibility(View.GONE);
			ll_DeviceType.setVisibility(View.GONE);
		} else if ("09".equals(sysId)) {// 南昌市广播电视传输
			ll_pattern.setVisibility(View.GONE);
			ll_userCode.setVisibility(View.VISIBLE);
			rl_Extra.setVisibility(View.GONE);
			rl_PinPaiN.setVisibility(View.VISIBLE);
			tv_PinPaiN.setText("请选择");
			ll_DeviceType.setVisibility(View.GONE);
		} else if ("02".equals(sysId)) {// 电信
			ll_pattern.setVisibility(View.GONE);
			ll_userCode.setVisibility(View.VISIBLE);
			rl_Extra.setVisibility(View.GONE);
			rl_PinPaiN.setVisibility(View.GONE);
			ll_DeviceType.setVisibility(View.VISIBLE);
			tv_DeviceType.setText("请选择");
		} else {
			ll_pattern.setVisibility(View.GONE);
			ll_userCode.setVisibility(View.VISIBLE);
			rl_Extra.setVisibility(View.GONE);
			rl_PinPaiN.setVisibility(View.GONE);
			ll_DeviceType.setVisibility(View.GONE);
		}

		contractInfo.setTrnCode(addressDetail.getTrnCode());
		contractInfo.setSysId(addressDetail.getSysId());
		contractInfo.setAreaId(addressDetail.getCityId());
		contractInfo.setUnit(addressDetail.getAgentCode());
	}

	/**
	 * 请求签约地区
	 */
	private void requestSignArea(String typeCode) {
		try {
			CspXmlXms006 cspXmlXms006 = new CspXmlXms006(typeCode);
			String strXml = cspXmlXms006.getCspXml();
			// 生成MCIS报文
			Mcis mcis = new Mcis(strXml, TransactionValue.CSPSZF);
			final byte[] byteMessage = mcis.getMcis();
			// 发送报文
			CspUtil cspUtil = new CspUtil(this);
//			cspUtil.setTest(true);
			Log.i("tag", "发送报文： " + new String(byteMessage, "GBK"));
			cspUtil.postCspLogin(byteMessage, new CallBack() {
				@Override
				public void onSuccess(String responStr) {
					Message msg = new Message();
					msg.what = SIGN_AREA_SUCCESS;
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

	/**
	 * 请求签约用户名
	 */
	private void requestSignUserName(SignContractInfo contractInfo) {
		try {
			// 生成CSP XML报文
			CspXmlXms002 cspXmlXms002 = new CspXmlXms002(contractInfo.getCostType(), contractInfo.getUserId(), "", "",
					contractInfo.getArea(), contractInfo.getUnit(), contractInfo.getUserCode(),
					dateList.get(datePosition), contractInfo.getSysId(), contractInfo.getTrnCode(),
					contractInfo.getAreaId(), "00010006");
			cspXmlXms002.setServicetype(contractInfo.getServicetype());
			String sysId = contractInfo.getSysId();
			if ("08".equals(sysId)) {// 江西省广播电视传输
				if ("智能卡号".equals(tv_pattern.getText().toString())) {
					cspXmlXms002.setSubscriberno(contractInfo.getSubscriberno());
				}
			} else if ("09".equals(sysId)) {// 南昌市广播电视传输
				if ("普通有线".equals(contractInfo.getPinpaiN())) {
					cspXmlXms002.setPinpaiN("0");
				} else if ("数字有线".equals(contractInfo.getPinpaiN())) {
					cspXmlXms002.setPinpaiN("1");
				} else if ("宽带业务".equals(contractInfo.getPinpaiN())) {
					cspXmlXms002.setPinpaiN("2");
				}
			} else if ("02".equals(sysId)) {// 电信
				if ("电话通讯费".equals(contractInfo.getDevTyp())) {
					cspXmlXms002.setDevTyp("10");
				} else if ("宽带上网费".equals(contractInfo.getDevTyp())) {
					cspXmlXms002.setDevTyp("20");
				}
			} else if ("05".equals(sysId)) {// 景德镇龙源管道燃气,九江深燃天燃气
				cspXmlXms002.setServId(contractInfo.getServId());
			}
			String strXml = cspXmlXms002.getCspXml();
			// 生成MCIS报文
			Mcis mcis = new Mcis(strXml, TransactionValue.CSPSZF);
			final byte[] byteMessage = mcis.getMcis();
			// 发送报文
			CspUtil cspUtil = new CspUtil(this);
//			cspUtil.setTest(true);
			Log.i("tag", "发送报文： " + new String(byteMessage, "GBK"));
			cspUtil.postCspLogin(byteMessage, new CallBack() {
				@Override
				public void onSuccess(String responStr) {
					UserInfoResponse response = XStreamUtils.getFromXML(responStr, UserInfoResponse.class);
					ConstHead constHead = response.getConstHead();
					if (response.getInfo() != null && response.getInfo().getSignContractInfo() != null) {
						SignContractInfo info = response.getInfo().getSignContractInfo();
						if (constHead != null && !"99".equals(constHead.getErrCode())
								&& !TextUtils.isEmpty(info.getName())) {
							Message msg = new Message();
							msg.what = SIGN_NAME_SUCCESS;
							msg.obj = info;
							mHandler.sendMessage(msg);
						} else {
							Toast.makeText(SignContractActivity.this, "该用户不存在", Toast.LENGTH_SHORT).show();
						}
					} else {
						Toast.makeText(SignContractActivity.this, "数据异常", Toast.LENGTH_SHORT).show();
					}

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

	/**
	 * 请求签约保存
	 */
	private void requestSignSave(SignContractInfo contractInfo) {
		try {

			// 生成CSP XML报文
			CspXmlXms002 cspXmlXms002 = new CspXmlXms002(contractInfo.getCostType(), contractInfo.getUserId(),
					contractInfo.getName(), "", contractInfo.getArea(), contractInfo.getUnit(),
					contractInfo.getUserCode(), dateList.get(datePosition), contractInfo.getSysId(),
					contractInfo.getTrnCode(), contractInfo.getAreaId(), "00010002");
			cspXmlXms002.setServicetype(contractInfo.getServicetype());
			cspXmlXms002.setSubscriberno(contractInfo.getSubscriberno());
			cspXmlXms002.setPinpaiN(contractInfo.getPinpaiN());
			cspXmlXms002.setDevTyp(contractInfo.getDevTyp());
			cspXmlXms002.setServId(contractInfo.getServId());
			String strXml = cspXmlXms002.getCspXml();
			// 生成MCIS报文
			Mcis mcis = new Mcis(strXml, TransactionValue.CSPSZF);
			final byte[] byteMessage = mcis.getMcis();
			// 发送报文
			CspUtil cspUtil = new CspUtil(this);
//			cspUtil.setTest(true);
			Log.i("tag", "发送报文： " + new String(byteMessage, "GBK"));
			cspUtil.postCspLogin(byteMessage, new CallBack() {
				@Override
				public void onSuccess(String responStr) {
					SignResponse response = XStreamUtils.getFromXML(responStr, SignResponse.class);
					if ("50".equals(response.getConstHead().getErrCode())) {
						DialogUtil.showWithOneBtn(SignContractActivity.this, "用户已存在",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog, int which) {
										dialog.dismiss();
									}
								});
					} else {
						mHandler.sendEmptyMessage(SIGN_SAVE_SUCCESS);
					}

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
	
	/**
	 * 编辑请求存储
	 */
	private void requestSave(SignContractInfo contractInfo){
	
		try{
		CspXmlXmsCom cspXmlXmsCom=new CspXmlXmsCom(LoginUtil.getUserId(this), "MS002007");
		cspXmlXmsCom.setAddressId(contractInfo.getAreaId());
		String orderData=tv_OrderDate.getText().toString();
		if(orderData.length()==4){
			cspXmlXmsCom.setOrderDate("0"+orderData.substring(2,3));
		}else if(orderData.length()==5){
			cspXmlXmsCom.setOrderDate(orderData.substring(2,4));
		}
		
		cspXmlXmsCom.setSysId(contractInfo.getSysId());
		cspXmlXmsCom.setUserCode(contractInfo.getUserCode());
		
		String strXml = cspXmlXmsCom.getCspXml();
		Log.i("tag", "getCspXml");
		// 生成MCIS报文
		Mcis mcis = new Mcis(strXml, TransactionValue.CSPSZF);
		Log.i("tag", "Mcis");
		final byte[] byteMessage = mcis.getMcis();
		// 发送报文
		CspUtil cspUtil = new CspUtil(this);
		Log.i("tag", "发送报文： " + new String(byteMessage, "GBK"));
//		cspUtil.setTest(true);
		cspUtil.postCspLogin(byteMessage, new CallBack() {
			@Override
			public void onSuccess(String responStr) {
				EventComXmlBean eventComXmlBean = EventComResp.readStringXml(responStr);
				if (!"00".equals(eventComXmlBean.getErrorcode())) {
					Toast.makeText(SignContractActivity.this, eventComXmlBean.getErrormsg(), Toast.LENGTH_SHORT).show();
				} else{
					finish();
				}
			}

			@Override
			public void onFinish() {

			}

			@Override
			public void onFailure(String responStr) {
				Toast.makeText(SignContractActivity.this, responStr, Toast.LENGTH_SHORT).show();
			}
		});
	}catch (UnsupportedEncodingException e) {
		e.printStackTrace();
	}
}
}
