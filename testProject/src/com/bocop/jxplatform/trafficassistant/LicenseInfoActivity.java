package com.bocop.jxplatform.trafficassistant;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.bean.CspRecHeaderBean;
import com.bocop.jxplatform.bean.LicenseInfoBean;
import com.bocop.jxplatform.config.TransactionValue;
import com.bocop.jxplatform.util.BocOpUtil;
import com.bocop.jxplatform.util.BocOpUtil.CallBackBoc;
import com.bocop.jxplatform.util.BocOpUtilWithoutDia;
import com.bocop.jxplatform.util.BocOpUtilWithoutDia.CallBackBoc2;
import com.bocop.jxplatform.util.BocopDialog;
import com.bocop.jxplatform.util.CspUtil;
import com.bocop.jxplatform.util.CspUtil.CallBack;
import com.bocop.jxplatform.util.CustomProgressDialog;
import com.bocop.jxplatform.util.JsonUtils;
import com.bocop.jxplatform.util.LoginUtil;
import com.bocop.jxplatform.util.Mcis;
import com.bocop.jxplatform.view.MyProgressBar;
import com.bocop.jxplatform.xml.CspRecForLicenseInfo;
import com.bocop.jxplatform.xml.CspRecHeader;
import com.bocop.jxplatform.xml.CspXmlAPJJ05;
import com.bocop.jxplatform.xml.CspXmlAPJJ06;
import com.bocop.jxplatform.xml.CspXmlAPJJ07;
import com.bocop.qzt.QztApplyActivity;
import com.bocop.qzt.QztProcessActivity;
import com.google.gson.Gson;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author luoyang
 * @version 创建时间：2015-6-19 上午10:26:34 类说明
 */

@ContentView(R.layout.activity_trafficlicinfo)
public class LicenseInfoActivity extends BaseActivity {

	/**
	 * 标题栏
	 */
	@ViewInject(R.id.tv_titleName)
	private TextView tv_titleName;
	/**
	 * 对驾驶证信息页面进行初始化
	 */
	@ViewInject(R.id.ll_traffic_info)
	View ll_traffic_info;
	@ViewInject(R.id.tvdrivenum_licquery)
	TextView tvdrivenum_licquery;
	@ViewInject(R.id.tvfilenum_licquery)
	TextView tvfilenum_licquery;
	@ViewInject(R.id.tvownername_licquery)
	TextView tvownername_licquery;
	@ViewInject(R.id.tvtel_licquery)
	TextView tvtel_licquery;
	@ViewInject(R.id.tvquasidriving_licquery)
	TextView tvquasidriving_licquery;
	@ViewInject(R.id.tvpenaltyscore_licquery)
	TextView tvpenaltyscore_licquery;
	@ViewInject(R.id.tvstate_licquery)
	TextView tvstate_licquery;
	@ViewInject(R.id.tvreplacementdate_licquery)
	TextView tvreplacementdate_licquery;
//	@ViewInject(R.id.bt_remove_license)
//	Button btLicenseRemove;

	/**
	 * 对添加驾驶证页面进行初始化
	 */
	@ViewInject(R.id.ll_traffic_add)
	View ll_traffic_add;
	@ViewInject(R.id.eddrivenum_add)
	EditText eddrivenum_add;
	@ViewInject(R.id.edfilenum_add)
	EditText edfilenum_add;
	@ViewInject(R.id.btlicenseadd)
	Button btlicenseadd;
	@ViewInject(R.id.tv_addlicetel)
	EditText edTel;
	
	@ViewInject(R.id.edzhengxin)
	EditText edzhengxin;

	String strFileNumAdd;
	String strTel = "";
	String strTel1 = "";
	String strVerifyCode;
	String strFlag = "0";			//判断是否已经获取验证码
	String strZhengxin;    			//证芯编号
	
	@ViewInject(R.id.btlicenseadd)
	Button btLicenseAdd;
	@ViewInject(R.id.tv_tellicesend_msg)	// 倒计时 发送验证码
	TextView tvSendMsg;
	@ViewInject(R.id.llt_tellicesend_msg)
	private LinearLayout lltSendMsg; // 点击获取验证码
	@ViewInject(R.id.rlt_telliceloading)	// 点击验证码加载框
	RelativeLayout rltLoading;
	@ViewInject(R.id.et_telliceverify_code)
	EditText edVerifyCode;
	
	/** 短信状态 */
	private static final int LAST_TIME = 1;
	private static final int LESS_TIME = 2;
	private static final int FINISH_TIME = 3;
	private long startTime;// 开始计时时间
	private long endTime;// 当前时间
	private int currentTime = 59;
	private MyProgressBar myProgressBar;
	
	String strOwnerName;
	String strIdNo;
	LicenseInfoBean licenseInfoBean;
	/**
	 * 定义进度条
	 */

	LicenseInfoBean bean = new LicenseInfoBean();

	/**
	 * 定义标志位，模拟已经绑定驾驶证和没绑定的情况
	 */
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case LAST_TIME:
				break;
			case LESS_TIME:
				currentTime--;
				tvSendMsg.setText(currentTime + "秒后重新获取");
				break;
			case FINISH_TIME:
				tvSendMsg.setText("获取验证码");
				lltSendMsg.setClickable(true);
				lltSendMsg.setBackgroundResource(R.drawable.send_btn_selector);
				tvSendMsg.setTextColor(getResources().getColor(R.color.white));

				currentTime = 59;
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		repuestCspForLicenseDates();
		ininEvent();
	}

	@OnClick(R.id.tvzhengxin)
	public void onLicenseZhengxin(View v){
		Intent intent = new Intent(LicenseInfoActivity.this,
				TrafficZhengxinActivity.class);
		startActivity(intent);
	}
	
	@OnClick(R.id.btlicenseadd)
	public void onLicenseAddClick(View v){
		strFileNumAdd = edfilenum_add.getText().toString();
		strTel1 = edTel.getText().toString();
		strVerifyCode = edVerifyCode.getText().toString();
		strZhengxin = edzhengxin.getText().toString();
		if(strFileNumAdd.length()!=12){
			Toast.makeText(this, "请输入12位档案编号", Toast.LENGTH_SHORT).show();
			return;
		} 
		if(strZhengxin.length()!=6){
			Toast.makeText(this, "请输入6位证芯编号", Toast.LENGTH_SHORT).show();
			return;
		} 
		
		if(strFlag.equals("0")){
			Toast.makeText(this, "请获取验证码", Toast.LENGTH_SHORT).show();
			return;
		}
		if(strVerifyCode.length() != 6){
			Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
			return;
		}
		if(!strTel.equals(strTel1)){
			Toast.makeText(this, "手机号前后不一致，请您确认后重新获取验证码并绑定车辆", Toast.LENGTH_SHORT).show();
			return;
		}
//		if(strTel.length()!=11){
//			Toast.makeText(this, "请输入11位手机号码", Toast.LENGTH_SHORT).show();
//			return;
//		}
//		if(strVerifyCode.length() != 6){
//			Toast.makeText(this, "请输入6位验证码", Toast.LENGTH_SHORT).show();
//			return;
//		}
//		strOwnerName = "罗阳";
//		strIdNo = "362202198702140010";
//		requestCspForLicenseAdd();
		requestBocopForCheckMsg();			//验证短信验证码
		
	}
	private void requestBocopForUseridQuery() {
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
							strOwnerName = map.get("cusname").toString();
							strIdNo = map.get("idno");
							Log.i("tag","名字：" + strOwnerName + "，身份证好：" + strIdNo);
							if (strOwnerName.length()>0 && strIdNo.length()>10) {
//								strOwnerName = "罗阳";
//								strIdNo = "362202198702140010";
								requestCspForLicenseAdd();
//								CustomProgressDialog.showBocRegisterSetDialog(LicenseInfoActivity.this);
							} else {
								CustomProgressDialog.showBocRegisterSetDialog(LicenseInfoActivity.this);
//								Toast.makeText(LicenseInfoActivity.this, "请前往中银开发平台实名认证", Toast.LENGTH_LONG).show();
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
						CspUtil.onFailure(LicenseInfoActivity.this, responStr);
					}
				});
	}
	private void requestCspForLicenseAdd() {
		try {
			Log.i("tag", "start");
			Log.i("tag", strZhengxin);
			LicenseInfoBean Info = new LicenseInfoBean(strIdNo,LoginUtil.getUserId(this), strFileNumAdd, strTel,strZhengxin);
			Log.i("tag", "LicenseInfoBean");
			//生成CSP XML报文
			CspXmlAPJJ05 cspXmlAPJJ05 = new CspXmlAPJJ05(Info);
			Log.i("tag", "CspXmlAPJJ05");
			String strXml = cspXmlAPJJ05.getCspXml();
			Log.i("tag", "cspXmlAPJJ05");
			//生成MCIS报文
			Mcis mcis = new Mcis(strXml,TransactionValue.APJJ19);
			final byte[] byteMessage = mcis.getMcis();
			Log.i("tag", "byteMessage");
			//发送报文
			CspUtil cspUtil = new CspUtil(this);
			Log.i("tag", "发送报文： " + new String(byteMessage, "GBK"));
			Log.i("tag", "CspUtil");
			cspUtil.postCspLogin( byteMessage, new CallBack() {
				
				@Override
				public void onSuccess(String responStr) {
					Log.i("tag", responStr);
					try {
						CspRecHeaderBean cspRecHeaderBean = CspRecHeader.readStringXml(responStr);
						if(cspRecHeaderBean.getErrorcode().equals("00")){
							Toast.makeText(LicenseInfoActivity.this, "绑定成功", Toast.LENGTH_LONG).show();
							setResult(RESULT_OK);
							finish();
						}
						else{
							if(cspRecHeaderBean.getErrormsg().contains("手机号码与驾驶证登记手机号码不匹配")){
								Toast.makeText(LicenseInfoActivity.this, "绑定失败，手机号码与驾驶证登记手机号码不匹配，请到交警大队核实信息并进行修改", Toast.LENGTH_LONG).show();
							}else if(cspRecHeaderBean.getErrormsg().contains("姓名与驾驶证登记的所有人姓名不匹配")){
								Toast.makeText(LicenseInfoActivity.this, "绑定失败，姓名与机动车登记的所有人姓名不匹配，请到车管所核实信息并进行修改", Toast.LENGTH_LONG).show();
							}else{
								Toast.makeText(LicenseInfoActivity.this, cspRecHeaderBean.getErrormsg(), Toast.LENGTH_LONG).show();
							}
							
						}
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
				
				@Override
				public void onFinish() {
				}
				
				@Override
				public void onFailure(String responStr) {
					CspUtil.onFailure(LicenseInfoActivity.this, responStr);
				}
			});
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private void ininEvent() {
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if(bundle != null){
			if(bundle.getString("errorCode").equals("00")){
				Log.i("tag", "bundle != null");
				ll_traffic_info.setVisibility(View.VISIBLE);
				ll_traffic_add.setVisibility(View.GONE);
				tv_titleName.setText("驾驶证信息");
				tvownername_licquery.setText(bundle.getString("Ownername"));
				tvdrivenum_licquery.setText(bundle.getString("Drivenum"));
				tvfilenum_licquery.setText(bundle.getString("Filenum"));
				tvtel_licquery.setText(bundle.getString("Tel"));
				tvquasidriving_licquery.setText(bundle.getString("Quasidriving"));
				tvpenaltyscore_licquery.setText(bundle.getString("Penaltyscore"));
				tvstate_licquery.setText(bundle.getString("State"));
				tvreplacementdate_licquery.setText(bundle.getString("Replacementdate"));
			}else{
				String tmpDriverNum = bundle.getString("Drivenum");
				Log.i("tag", bundle.getString("errorMsg"));
				String tmpErrorMsg = bundle.getString("errorMsg");
				tvdrivenum_licquery.setText(tmpDriverNum);
				
				BocopDialog dialog = new BocopDialog(this, "提示", tmpErrorMsg);
				dialog.setPositiveButton(new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						onClickRemoveLicense(null);
						dialog.dismiss();
					}
				}, "解除绑定");
				dialog.setNegativeButton(new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						finish();
					}
				}, "取消");
				dialog.show();
				
			}
		}else{
			Log.i("tag", "bundle = null");
			ll_traffic_info.setVisibility(View.GONE);
			ll_traffic_add.setVisibility(View.VISIBLE);
			tv_titleName.setText("绑定驾驶证");
		}
	}

	/**
	 * 发送验证码
	 * 
	 * @param v
	 */
	@OnClick(R.id.llt_tellicesend_msg)
	public void SendMsg(View v) {
		strTel = edTel.getText().toString().trim();
		if(strTel.length() != 11){
			Toast.makeText(this, "请输入正确手机号", Toast.LENGTH_SHORT).show();
			return;
		}
		strFlag = "1";
		requestBocForTelMsg();
	}
	
	@OnClick(R.id.bt_remove_license)
	public void onClickRemoveLicense(View v){
		try {
			//生成CSP XML报文
			CspXmlAPJJ07 cspXmlAPJJ07 = new CspXmlAPJJ07(tvdrivenum_licquery.getText().toString());
			String strXml = cspXmlAPJJ07.getCspXml();
			//生成MCIS报文
			Mcis mcis = new Mcis(strXml,TransactionValue.APJJ07);
			final byte[] byteMessage = mcis.getMcis();
			//发送报文
			Log.i("tag", "发送报文： " + new String(byteMessage, "GBK"));
			CspUtil cspUtil = new CspUtil(this);
			cspUtil.postCspLogin(byteMessage, new CallBack() {
				
				@Override
				public void onSuccess(String responStr) {
					Log.i("tag", responStr);
					try {
						CspRecHeaderBean cspRecHeaderBean = CspRecHeader.readStringXml(responStr);
						if(cspRecHeaderBean.getErrorcode().equals("00")){
							Toast.makeText(LicenseInfoActivity.this, "解除绑定成功", Toast.LENGTH_LONG).show();
							setResult(RESULT_OK);
							finish();
						}
						else{
							Toast.makeText(LicenseInfoActivity.this, cspRecHeaderBean.getErrormsg(), Toast.LENGTH_LONG).show();
						}
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
				
				@Override
				public void onFinish() {
				}
				
				@Override
				public void onFailure(String responStr) {
					CspUtil.onFailure(LicenseInfoActivity.this, responStr);
				}
			});
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	private void requestBocForTelMsg() {
		//点击短信发送按钮后的加载框
				rltLoading.setVisibility(View.VISIBLE);
				myProgressBar = new MyProgressBar(this, rltLoading);
				myProgressBar.addView();
				tvSendMsg.setText("正在努力...");
				tvSendMsg.setTextColor(getResources().getColor(R.color.pay_send_msg1));
				BocOpUtilWithoutDia bocOpUtil = new BocOpUtilWithoutDia(this);
				
				Gson gson = new Gson();
				Map<String,String> map = new HashMap<String,String>();
				map.put("usrid", LoginUtil.getUserId(this));
				map.put("usrtel", strTel);
				map.put("randtrantype", TransactionValue.messageType);
				final String strGson = gson.toJson(map);
				
				bocOpUtil.postOpboc(strGson,TransactionValue.SA7114, new CallBackBoc2() {
//					SA0052 sa0052;
					@Override
					public void onSuccess(String responStr) {
						Log.i("tag", responStr);
//						try {
//							sa0052 = JsonUtils.getObject(responStr, SA0052.class);
//							Log.i("tag0", sa0052.getResult());
//							if(sa0052.getResult().equals("0")){
//								Log.i("tag1", sa0052.getResult());
//								Toast.makeText(LicenseInfoActivity.this, "短信已发送，请查收", Toast.LENGTH_LONG).show();
//							}
//						} catch (Exception e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
						Toast.makeText(LicenseInfoActivity.this, "短信已发送，请查收", Toast.LENGTH_LONG).show();
						tvSendMsg.setText("59秒后重新获取");
						lltSendMsg.setClickable(false);
						handler.sendEmptyMessage(LAST_TIME);
						myProgressBar.removeView();
						rltLoading.setVisibility(View.GONE);
						Toast.makeText(LicenseInfoActivity.this, "短信验证码已发送！", Toast.LENGTH_SHORT);

						new Thread() {
							public void run() {
								startTime = System.currentTimeMillis();// 获取当前时间
								while (true) {
									endTime = System.currentTimeMillis();// 获取当前时间
									if ((endTime - startTime) > 1000) {
										startTime = System.currentTimeMillis();
										if (currentTime > 0) {
											handler.sendEmptyMessage(LESS_TIME);
										} else {
											handler.sendEmptyMessage(FINISH_TIME);
											break;
										}
									}
								}
							};
						}.start();
						
					}
					
					@Override
					public void onFinish() {
						
					}
					
					@Override
					public void onFailure(String responStr) {
						tvSendMsg.setText("获取验证码");
						lltSendMsg.setClickable(true);
						lltSendMsg.setBackgroundResource(R.drawable.send_btn_selector);
						tvSendMsg.setTextColor(getResources().getColor(R.color.white));
						currentTime = 59;
						rltLoading.setVisibility(View.GONE);
						if(responStr.equals("0") || responStr.equals("1") ){
							Toast.makeText(LicenseInfoActivity.this, R.string.onFailure, Toast.LENGTH_SHORT).show();
						}
						else{
							Toast.makeText(LicenseInfoActivity.this, responStr, Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						Log.i("tag", "发送GSON数据：" + strGson);
					}
				});
	}
	/**
	 * 解除绑定
	 */
	protected void requestCspForDriveDatesRemove() {
		// TODO Auto-generated method stub
		try {
			CspUtil cspUtil = new CspUtil(this);
			final byte[] mcis = "00E1091500010400020".getBytes("GBK");
			cspUtil.postCspLogin(mcis, new CallBack() {

				@Override
				public void onSuccess(String responStr) {
					// TODO Auto-generated method stub
					// 解除绑定成功
					setResult(RESULT_OK);
					finish();
				}

				@Override
				public void onFinish() {
					// TODO Auto-generated method stub

				}

				@Override
				public void onFailure(String responStr) {
					CspUtil.onFailure(LicenseInfoActivity.this, responStr);
				}
			});
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 验证短信验证码
	 */
			private void requestBocopForCheckMsg() {
				// TODO Auto-generated method stub
				Gson gson = new Gson();
//				List<Map<String,String>> list =new ArrayList<Map<String,String>>();
				Map<String,String> map = new HashMap<String,String>();
				map.put("usrid", LoginUtil.getUserId(this));
				map.put("usrtel", strTel);
				map.put("randtrantype", TransactionValue.messageType);
				map.put("mobcheck", strVerifyCode);
				
				final String strGson = gson.toJson(map);
				
				BocOpUtil bocOpUtil = new BocOpUtil(this);
				bocOpUtil.postOpboc(strGson,TransactionValue.SA7115, new CallBackBoc() {
					
					@Override
					public void onSuccess(String responStr) {
						Log.i("tag", responStr);
						requestBocopForUseridQuery();	//用户附加信息查询
//						try {
//							sa0052 = JsonUtils.getObject(responStr, SA0052.class);
//							Log.i("tag0", sa0052.getResult());
//							if(sa0052.getResult().equals("0")){
//								Log.i("tag1", sa0052.getResult() + " 短信验证通过，想CSP发送报文，缴纳罚款");
//								requestBocopForUseridQuery();	//用户附加信息查询
//							}
//							else{
//								Toast.makeText(LicenseInfoActivity.this, "短信验证码输入有误", Toast.LENGTH_LONG).show();
//							}
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
					}
					
					@Override
					public void onStart() {
						Log.i("tag", "发送GSON数据：" + strGson);
					}
					
					@Override
					public void onFinish() {
//						 requestBocopForUseridQuery();
					}
					
					@Override
					public void onFailure(String responStr) {
						if(responStr.equals("0")){
							Toast.makeText(LicenseInfoActivity.this, R.string.onFailure, Toast.LENGTH_SHORT).show();
						}
//						else{
//							Toast.makeText(LicenseInfoActivity.this, responStr, Toast.LENGTH_SHORT).show();
//						}
					}
				});
			}

	private void repuestCspForLicenseDates() {
		try {
			//生成CSP XML报文
			CspXmlAPJJ06 cspXmlForCarList = new CspXmlAPJJ06(LoginUtil.getUserId(this));
			String strXml = cspXmlForCarList.getCspXml();
			//生成MCIS报文
			Mcis mcis = new Mcis(strXml,TransactionValue.APJJ06);
			final byte[] byteMessage = mcis.getMcis();
			//发送报文
			CspUtil cspUtil = new CspUtil(this);
			Log.i("tag", "发送报文： " + new String(byteMessage, "GBK"));
			cspUtil.postCspLogin(byteMessage, new CallBack() {
				@Override
				public void onSuccess(String responStr) {
					try {
						
						licenseInfoBean = CspRecForLicenseInfo.readStringXml(responStr);
						if(!licenseInfoBean.getErrorcode().equals("00")){
							Toast.makeText(LicenseInfoActivity.this,licenseInfoBean.getErrormsg(), Toast.LENGTH_SHORT).show();
							ll_traffic_info.setVisibility(View.GONE);
							ll_traffic_add.setVisibility(View.VISIBLE);
							tv_titleName.setText("绑定驾驶证");
						}
						else{
							Log.i("tag", "initCarDates");
							ll_traffic_info.setVisibility(View.VISIBLE);
							ll_traffic_add.setVisibility(View.GONE);
							tv_titleName.setText("驾驶证信息");
							setLicenseInfoText();
						}
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onFinish() {

				}

				@Override
				public void onFailure(String responStr) {
					CspUtil.onFailure(LicenseInfoActivity.this, responStr);
					Log.i("tag", "onFailure:"+responStr);
					ll_traffic_info.setVisibility(View.GONE);
					ll_traffic_add.setVisibility(View.VISIBLE);
					tv_titleName.setText("绑定驾驶证");
				}
			});
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	protected void setLicenseInfoText() {
		tvownername_licquery.setText(licenseInfoBean.getOwnername());
		tvdrivenum_licquery.setText(licenseInfoBean.getDrivenum());
		tvfilenum_licquery.setText(licenseInfoBean.getFilenum());
		tvtel_licquery.setText(licenseInfoBean.getTel());
		tvquasidriving_licquery.setText(licenseInfoBean.getQuasidriving());
		tvpenaltyscore_licquery.setText(licenseInfoBean.getPenaltyscore());
		tvstate_licquery.setText(licenseInfoBean.getState());
		tvreplacementdate_licquery.setText(licenseInfoBean.getReplacementdate());
	}
}
