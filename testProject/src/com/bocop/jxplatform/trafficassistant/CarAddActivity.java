package com.bocop.jxplatform.trafficassistant;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.bean.CarInfoBean;
import com.bocop.jxplatform.bean.CspRecHeaderBean;
import com.bocop.jxplatform.config.TransactionValue;
import com.bocop.jxplatform.util.BocOpUtil;
import com.bocop.jxplatform.util.BocOpUtil.CallBackBoc;
import com.bocop.jxplatform.util.BocOpUtilWithoutDia;
import com.bocop.jxplatform.util.BocOpUtilWithoutDia.CallBackBoc2;
import com.bocop.jxplatform.util.CspUtil;
import com.bocop.jxplatform.util.CspUtil.CallBack;
import com.bocop.jxplatform.util.CustomProgressDialog;
import com.bocop.jxplatform.util.JsonUtils;
import com.bocop.jxplatform.util.LoginUtil;
import com.bocop.jxplatform.util.Mcis;
import com.bocop.jxplatform.util.RegularCheck;
import com.bocop.jxplatform.view.BackButton;
import com.bocop.jxplatform.view.MyProgressBar;
import com.bocop.jxplatform.xml.CspRecHeader;
import com.bocop.jxplatform.xml.CspXmlAPJJ01;
import com.google.gson.Gson;

/** 
 * @author luoyang  
 * @version 创建时间：2015-6-18 下午3:57:31 
 * 添加车辆绑定信息
 */

@ContentView(R.layout.activity_trafficcaradd)
public class CarAddActivity extends BaseActivity implements OnClickListener{

	@ViewInject(R.id.tv_titleName)
	private TextView  tv_titleName;
	@ViewInject(R.id.iv_imageLeft)
	private BackButton backBtn; 
	
	@ViewInject(R.id.edlicensenum_add)
	EditText edlicensenum;
	@ViewInject(R.id.splicensetype_add)
	TextView spsplicensetype;
	@ViewInject(R.id.edvehiclenum_add)
	EditText edvehiclenum;
	@ViewInject(R.id.tv_addtel)		//手机
	EditText edTel;
	@ViewInject(R.id.et_telverify_code)
	EditText edTelVerifyCode;
	
	@ViewInject(R.id.tv_telsend_msg)	// 倒计时 发送验证码
	TextView tvSendMsg;
	@ViewInject(R.id.llt_telsend_msg)
	private LinearLayout lltSendMsg; // 点击获取验证码
	@ViewInject(R.id.rlt_telloading)	// 点击验证码加载框
	RelativeLayout rltLoading;
	
	
	/** 短信状态 */
	private static final int LAST_TIME = 1;
	private static final int LESS_TIME = 2;
	private static final int FINISH_TIME = 3;
	private long startTime;// 开始计时时间
	private long endTime;// 当前时间
	private int currentTime = 59;
	private MyProgressBar myProgressBar;
	
	@ViewInject(R.id.btcaradd)
	Button btCarAdd;
	
	String strLicenseNum;
	String strLicenseType;
	String strVehicleNum;
	String strTel = "";
	String strTel1 = "";
	String strTelVerifyCode;
	String strFlag = "0";			//判断是否已经获取验证码
	
	String strOwnerName;
	String strIdNo;
	
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
		super.onCreate(savedInstanceState);
		tv_titleName.setText("添加车辆");
		btCarAdd.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		bindCar();
	}

	private void bindCar() {
		strLicenseNum = edlicensenum.getText().toString().trim();
		strLicenseType = "02";		//小型车
		strVehicleNum = edvehiclenum.getText().toString().trim();
		strTel1 = edTel.getText().toString().trim();
		strTelVerifyCode = edTelVerifyCode.getText().toString().trim();
		if(strLicenseNum.length()!=6){
			Toast.makeText(this, "请输入正确的车牌号码", Toast.LENGTH_SHORT).show();
			return;
		} 
		if(strVehicleNum.length()!=6){
			Toast.makeText(this, "请输入正确的车架号后6位", Toast.LENGTH_SHORT).show();
			return;
		}
		if(strFlag.equals("0")){
			Toast.makeText(this, "请获取验证码", Toast.LENGTH_SHORT).show();
			return;
		}
		if(strTelVerifyCode.length() != 6){
			Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
			return;
		}
		if(!strTel.equals(strTel1)){
			Toast.makeText(this, "手机号前后不一致，请您确认后重新获取验证码并绑定车辆", Toast.LENGTH_SHORT).show();
			return;
		}
		requestBocopForCheckMsg();
	}
	
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
							strOwnerName = map.get("cusname").toString();
							strIdNo = map.get("idno");
							Log.i("tag","名字：" + strOwnerName + "，身份证好：" + strIdNo);
							if (strOwnerName.length()>0 && strIdNo.length()>10) {
//								strOwnerName = "罗阳";
//								strIdNo = "362202198702140010";
								requestCspForCarAdd();
//								CustomProgressDialog.showBocRegisterSetDialog(CarAddActivity.this);
							} else {
								CustomProgressDialog.showBocRegisterSetDialog(CarAddActivity.this);
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
						CspUtil.onFailure(CarAddActivity.this, responStr);
					}
				});
	}

	private void requestCspForCarAdd(){
		try {
			CarInfoBean carInfoBean = new CarInfoBean(LoginUtil.getUserId(this), strLicenseType, "赣" + strLicenseNum, strOwnerName, strTel, strVehicleNum, strIdNo,"2");
			//生成CSP XML报文
			CspXmlAPJJ01 cspXmlAPJJ01 = new CspXmlAPJJ01(carInfoBean);
			String strXml = cspXmlAPJJ01.getCspXml();
			//生成MCIS报文
			Mcis mcis = new Mcis(strXml,TransactionValue.APJJ01);
			final byte[] byteMessage = mcis.getMcis();
			//发送报文
			CspUtil cspUtil = new CspUtil(this);
			Log.i("tag", "发送报文： " + new String(byteMessage, "GBK"));
			cspUtil.postCspLogin( byteMessage, new CallBack() {
				
				@Override
				public void onSuccess(String responStr) {
					Log.i("tag", responStr);
					try {
						CspRecHeaderBean cspRecHeaderBean = CspRecHeader.readStringXml(responStr);
						if(cspRecHeaderBean.getErrorcode().equals("00")){
							Toast.makeText(CarAddActivity.this, "绑定成功", Toast.LENGTH_LONG).show();
							setResult(RESULT_OK);
							finish();
						}
						else{
							if(cspRecHeaderBean.getErrormsg().contains("手机号码与机动车登记手机号码不匹配")){
								Toast.makeText(CarAddActivity.this, "绑定失败，手机号码与车辆登记手机号码不匹配，请到车管所核实信息并进行修改", Toast.LENGTH_LONG).show();
							}else if(cspRecHeaderBean.getErrormsg().contains("姓名与机动车登记的所有人姓名不匹配")){
								Toast.makeText(CarAddActivity.this, "绑定失败，姓名与机动车登记的所有人姓名不匹配，请到车管所核实信息并进行修改", Toast.LENGTH_LONG).show();
							}
							else{
								Toast.makeText(CarAddActivity.this, cspRecHeaderBean.getErrormsg(), Toast.LENGTH_LONG).show();
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
					if(responStr.equals("0")){
						Toast.makeText(CarAddActivity.this, "服务器错误，请稍候再试", Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(CarAddActivity.this, responStr, Toast.LENGTH_SHORT).show();
					}
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
				map.put("mobcheck", strTelVerifyCode);
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
//								Toast.makeText(CarAddActivity.this, "短信验证码输入有误", Toast.LENGTH_LONG).show();
//							}
//						} catch (Exception e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
					}
					
					@Override
					public void onStart() {
						Log.i("tag", "发送GSON数据：" + strGson);
					}
					
					@Override
					public void onFinish() {
//						requestBocopForUseridQuery();	//用户附加信息查询
					}
					
					@Override
					public void onFailure(String responStr) {
						CspUtil.onFailure(CarAddActivity.this, responStr);
					}
				});
			}
	/**
	 * 发送验证码
	 * 
	 * @param v
	 */
	@OnClick(R.id.llt_telsend_msg)
	public void SendMsg(View v) {
		strTel = edTel.getText().toString().trim();
		if(!RegularCheck.isMobile(strTel)){
			Toast.makeText(this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
			return;
		} 
		strFlag = "1";
		lltSendMsg.setClickable(false);
		requestBocForTelMsg();
		
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
						Toast.makeText(CarAddActivity.this, "短信已发送，请查收", Toast.LENGTH_LONG).show();
						tvSendMsg.setText("59秒后重新获取");
						lltSendMsg.setClickable(false);
						handler.sendEmptyMessage(LAST_TIME);
						myProgressBar.removeView();
						rltLoading.setVisibility(View.GONE);
						Toast.makeText(CarAddActivity.this, "短信验证码已发送！", Toast.LENGTH_SHORT);

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
						Log.i("tag", "cartel onFailure");
						tvSendMsg.setText("获取验证码");
						lltSendMsg.setClickable(true);
						lltSendMsg.setBackgroundResource(R.drawable.send_btn_selector);
						tvSendMsg.setTextColor(getResources().getColor(R.color.white));
						currentTime = 59;
						rltLoading.setVisibility(View.GONE);
						if(responStr.equals("0") || responStr.equals("1") ){
							Toast.makeText(CarAddActivity.this, R.string.onFailure, Toast.LENGTH_SHORT).show();
						}
						else{
								Toast.makeText(CarAddActivity.this,responStr , Toast.LENGTH_SHORT).show();
						}
					}
					@Override
					public void onStart() {
						Log.i("tag", "发送GSON数据：" + strGson);
					}
				});
	}
}
