package com.bocop.qzt;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.baseUtil.util.MyUtils;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.bean.CspRecHeaderBean;
import com.bocop.jxplatform.bean.SA0052;
import com.bocop.jxplatform.bean.SA0075;
import com.bocop.jxplatform.bean.SA0075Card;
import com.bocop.jxplatform.config.BocSdkConfig;
import com.bocop.jxplatform.config.TransactionValue;
import com.bocop.jxplatform.trafficassistant.BocOpWebActivity;
import com.bocop.jxplatform.util.BocOpUtil;
import com.bocop.jxplatform.util.BocOpUtil.CallBackBoc;
import com.bocop.jxplatform.util.BocOpUtilWithoutDia;
import com.bocop.jxplatform.util.BocOpUtilWithoutDia.CallBackBoc2;
import com.bocop.jxplatform.util.CspUtil;
import com.bocop.jxplatform.util.CspUtil.CallBack;
import com.bocop.jxplatform.util.JsonUtils;
import com.bocop.jxplatform.util.LoginUtil;
import com.bocop.jxplatform.util.Mcis;
import com.bocop.jxplatform.util.QztRequestWithJsonAndHead;
import com.bocop.jxplatform.view.MyProgressBar;
import com.bocop.jxplatform.xml.CspRecHeader;
import com.bocop.jxplatform.xml.CspXmlForPay;
import com.google.gson.Gson;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Selection;
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
 * @version 创建时间：2015-6-30 上午9:13:39 
 * 类说明 
 */
@ContentView(R.layout.qzt_activity_pay)
public class QztPayActivity extends BaseActivity {

	
	@ViewInject(R.id.tv_titleName)
	private TextView  tv_titleName;
	
	@ViewInject(R.id.tv_pay_order)
	TextView tvPayOrder;
	@ViewInject(R.id.tv_pay_qzt_order)
	TextView tvQztOrder;
	
	@ViewInject(R.id.tv_pay_qztAmt)
	TextView tvQztAmt;			// 应缴金额
	@ViewInject(R.id.tv_card_sum_name)
	TextView tvCardSumName;			// 绑定卡“余额”
	@ViewInject(R.id.tv_card_sum)
	TextView tvCardSum;				// 绑定卡余额
	//获取验证码
	@ViewInject(R.id.et_verify_code)
	EditText etVerifyCode;			// 验证码输入框
	@ViewInject(R.id.tv_send_msg)	// 倒计时 发送验证码
	TextView tvSendMsg;
	@ViewInject(R.id.llt_send_msg)
	private LinearLayout lltSendMsg; // 点击获取验证码
	@ViewInject(R.id.rlt_loading)	// 点击验证码加载框
	RelativeLayout rltLoading;
	
	@ViewInject(R.id.llPayMain)
	LinearLayout lltPayMain;
	@ViewInject(R.id.lltBind)
	LinearLayout lltBind;
	
	@ViewInject(R.id.tv_pay_card)
	TextView tvPayCard;
	//支付按钮
	@ViewInject(R.id.bt_pay)
	Button btPay;
	
	String strCardBal;
	String orderNum;
	String amt;
	
	
	
	private String[] cardArray;
	private String[] lmtamtArray;
	private String[] cardArrayStr;// 带有信用卡或者借记卡字样的列表
	private String uniqueCard; // 卡唯一标识
	private String choiseCard; // 卡号
	private Map<String, String> mapSum = new HashMap<String, String>();
	String checkMsg = ""; 			//短信验证码
	
	/** 短信状态 */
	private static final int LAST_TIME = 1;
	private static final int LESS_TIME = 2;
	private static final int FINISH_TIME = 3;
	private long startTime;// 开始计时时间
	private long endTime;// 当前时间
	private int currentTime = 59;
	private MyProgressBar myProgressBar;
	public SmsContent smsContent;
	
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
//				lltSendMsg.setPadding(10, 10, 10, 10);
				tvSendMsg.setTextColor(getResources().getColor(R.color.white));

				currentTime = 59;
				break;
			}
		};
	};
	
	
	
	@Override
	protected void onResume() {
		super.onResume();
		//加载卡列表
		requestBocopForUserCard();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();
		//加载卡列表
//		requestBocopForUserCard();
	}

	//获取卡余额信息
	private void requestBocopForCardBal() {
		Gson gson = new Gson();
		Map<String,String> map = new HashMap<String,String>();
		map.put("lmtamt", uniqueCard);
		final String strGson = gson.toJson(map);
		
		BocOpUtil bocOpUtil = new BocOpUtil(this);
		bocOpUtil.postOpboc(strGson,TransactionValue.SA0059, new CallBackBoc() {
			
			@Override
			public void onSuccess(String responStr) {
				Log.i("tag2", responStr);
//				SA0059 sa0059 = null;
				Map<String,String> map;
				try {
					map = JsonUtils.getMapStr(responStr);
					strCardBal = map.get("balance").toString();
//					sa0059.setBALANCE(map.get("BALANCE").toString());
					Float fCardBal = Float.parseFloat(strCardBal)/100;
					tvCardSumName.setText("卡余额：");
					tvCardSum.setText(fCardBal.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void onStart() {
				Log.i("tag", "余额发送GSON数据：" + strGson);
			}
			
			@Override
			public void onFinish() {
				
			}
			
			@Override
			public void onFailure(String responStr) {
				Log.i("tag", "tvCardSumName");
				tvCardSumName.setText("卡余额：");
				tvCardSum.setText(strCardBal);
			}
		});
	}

	//获取开放平台用户的卡信息
	private void requestBocopForUserCard() {
		Gson gson = new Gson();
//		List<Map<String,String>> list =new ArrayList<Map<String,String>>();
		Map<String,String> map = new HashMap<String,String>();
		map.put("USRID", LoginUtil.getUserId(this));
		map.put("ifncal", "0");
		map.put("pageno", "1");
		final String strGson = gson.toJson(map);
		
		BocOpUtil bocOpUtil = new BocOpUtil(this);
		bocOpUtil.postOpboc(strGson,TransactionValue.SA0075, new CallBackBoc() {
			
			@Override
			public void onSuccess(String responStr) {
				SA0075 sa0075;
				Log.i("tag1", responStr);
				try {
					sa0075 = JsonUtils.getObject(responStr, SA0075.class);
					Log.i("tag", sa0075.pageno + sa0075.rcdcnt);
					Log.i("tag", String.valueOf(sa0075.getSaplist().size()));
					if(sa0075.getSaplist().size() > 0){
						lltPayMain.setVisibility(View.VISIBLE);
						lltBind.setVisibility(View.GONE);
						cardArray = new String[sa0075.getSaplist().size()];
						lmtamtArray = new String[sa0075.getSaplist().size()];
						for(int i = 0;i<sa0075.getSaplist().size();i++){
							SA0075Card sa0075Card = sa0075.getSaplist().get(i);
							cardArray[i] = sa0075Card.accno;
							lmtamtArray[i] = sa0075Card.lmtamt;
							Log.i("tag","第"+ String.valueOf(i) + sa0075Card.accno );
							Log.i("tag1","第"+ String.valueOf(i) + sa0075Card.lmtamt );
						}
//						cardArray[sa0075.getSaplist().size()] = "6259073676906698";
						//模拟信信用卡，上线前删掉
						choiseCard = cardArray[0];
						uniqueCard = lmtamtArray[0];
						cardArrayStr = new String[cardArray.length];
						for (int i = 0; i < cardArray.length; i++) {
							String num = cardArray[i];
							Log.i("tag",String.valueOf(cardArray.length) );
							if (num.length() == 19) {
								cardArrayStr[i] = cardArray[i].substring(0, 4) + "***********" + cardArray[i].substring(15)  + "(借记卡)";
							} else {
								cardArrayStr[i] = cardArray[i].substring(0, 4) + "***********" + cardArray[i].substring(12)  + "(信用卡)";
							}
							Log.i("tag","第"+ String.valueOf(i) + ":" + "  " + cardArrayStr[i] );
						}
						tvPayCard.setText(cardArrayStr[0]);
						if (cardArrayStr[0].contains("信用卡")) {
//							btPay.setEnabled(false);
							lltSendMsg.setEnabled(false);
							Toast.makeText(QztPayActivity.this, "该缴费暂不支持信用卡支付", Toast.LENGTH_SHORT)
							.show();
							
						} else {
							requestBocopForCardBal();
						}
					}
					else{
						lltPayMain.setVisibility(View.GONE);
						lltBind.setVisibility(View.VISIBLE);
					}
					tvCardSumName.setText("");
					tvCardSum.setText("");
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(QztPayActivity.this, "意外事件发生，请稍候再试", Toast.LENGTH_LONG).show();
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
				Log.i("tag", responStr);
				if(responStr.equals("3800015")){
					lltPayMain.setVisibility(View.GONE);
					lltBind.setVisibility(View.VISIBLE);
//					Toast.makeText(TrafficPayActivity.this, "请绑定借记卡进行缴费。", Toast.LENGTH_LONG).show();
				}
			}
		});
	}
	

	/**
	 * 选择卡
	 * 
	 * @param v
	 */
	@OnClick(R.id.llt_card)
	public void selectCard(View v) {
		Log.i("tag", "选择卡");
		try{
			showCardList();
		}
		catch(Exception e){
			Toast.makeText(QztPayActivity.this, "意外事件发生，请稍候再试", Toast.LENGTH_LONG).show();
		}
	}

	private void showCardList() {
		// TODO Auto-generated method stub
		Dialog alertDialog = new AlertDialog.Builder(this).setTitle("请选择付款账户")
				.setItems(cardArrayStr, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if ((cardArray[which].length() != 19)) {
							Toast.makeText(QztPayActivity.this, "该缴费暂不支持信用卡支付", Toast.LENGTH_SHORT)
									.show();
//							btPay.setEnabled(false);
							lltSendMsg.setEnabled(false);
						} else {
							btPay.setEnabled(true);
							lltSendMsg.setEnabled(true);
						}
						tvPayCard.setText(cardArrayStr[which]);
						choiseCard = cardArray[which];
						uniqueCard = lmtamtArray[which];
						Log.i("tag", choiseCard);
						if (cardArrayStr[which].contains("信用卡")) {
							tvCardSumName.setText("");
							tvCardSum.setText("");
						}
						else{
							requestBocopForCardBal();
						}
					}
				}).create();
		alertDialog.setCanceledOnTouchOutside(true);
		alertDialog.show();
	}

	private void initView() {
		tv_titleName.setText("缴费");
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if(bundle != null){
			
			orderNum = bundle.getString("orderNum");
			amt = bundle.getString("amt");
			tvQztOrder.setText(orderNum);
			tvQztAmt.setText(amt);
//			strPeccancyNum = bundle.getString("peccancyNum");
//			strLicenseNum = bundle.getString("licenseNum");
//			
//			tvPayPeccancyNum.setText(strPeccancyNum);
//			tvLicenseNum.setText(strLicenseNum);
			
		}
			
	}
	
	/**
	 * 发送验证码
	 * 
	 * @param v
	 */
	@OnClick(R.id.llt_send_msg)
	public void SendMsg(View v) {
		requestBocForMsg();
	}
	/**
	 * 去绑定
	 * 
	 * @param v
	 */
	@OnClick(R.id.tvAddBankCard)
	public void btnAddBind(View v) {
		if (LoginUtil.isLog(QztPayActivity.this)){
			Log.i("tag", "BindCardActivity");
			Intent intent = new Intent(QztPayActivity.this, BocOpWebActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("title", "添加银行卡");
			bundle.putString("type", "bindCard");
			intent.putExtras(bundle);
			startActivity(intent);
		}
	}
	/**
	 * 去绑定
	 * 
	 * @param v
	 */
	@OnClick(R.id.btnBind)
	public void btnBind(View v) {
		if (LoginUtil.isLog(QztPayActivity.this)){
			Log.i("tag", "BindCardActivity");
			Intent intent = new Intent(QztPayActivity.this, BocOpWebActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("title", "添加银行卡");
			bundle.putString("type", "bindCard");
			intent.putExtras(bundle);
			startActivity(intent);
		}
	}
	/**
	 * 请求发送短信验证码
	 */
	public void requestBocForMsg() {
		//点击短信发送按钮后的加载框
		rltLoading.setVisibility(View.VISIBLE);
		myProgressBar = new MyProgressBar(this, rltLoading);
		myProgressBar.addView();
		tvSendMsg.setText("正在努力...");
		tvSendMsg.setTextColor(getResources().getColor(R.color.pay_send_msg1));
		BocOpUtilWithoutDia bocOpUtil = new BocOpUtilWithoutDia(this);
		
		Gson gson = new Gson();
		Map<String,String> map = new HashMap<String,String>();
		map.put("userid", LoginUtil.getUserId(this));
		map.put("cardno", uniqueCard);
		final String strGson = gson.toJson(map);
		
		bocOpUtil.postOpboc(strGson,TransactionValue.SA0052, new CallBackBoc2() {
			SA0052 sa0052;
			@Override
			public void onSuccess(String responStr) {
				Log.i("tag", responStr);
				try {
					sa0052 = JsonUtils.getObject(responStr, SA0052.class);
					Log.i("tag0", sa0052.getResult());
					if(sa0052.getResult().equals("0")){
						Log.i("tag1", sa0052.getResult());
						Toast.makeText(QztPayActivity.this, "已发送至" + sa0052.mobleno + "，请查收", Toast.LENGTH_LONG).show();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				tvSendMsg.setText("59秒后重新获取");
				lltSendMsg.setClickable(false);
				handler.sendEmptyMessage(LAST_TIME);
				myProgressBar.removeView();
				rltLoading.setVisibility(View.GONE);
				Toast.makeText(QztPayActivity.this, "短信验证码已发送！", Toast.LENGTH_SHORT);

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
					Toast.makeText(QztPayActivity.this, R.string.onFailure, Toast.LENGTH_SHORT).show();
				}
				else{
//					Toast.makeText(TrafficPayActivity.this, responStr, Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onStart() {
				Log.i("tag", "发送GSON数据：" + strGson);
			}
		});
		
//		List<NameValuePair> paramList = new ArrayList<NameValuePair>();
//		paramList.add(new BasicNameValuePair("txnId",
//				TransactionValue.COMM_SEND_SMS_VERIFY_CODE_TXN));
//
//		paramList.add(new BasicNameValuePair("orderId", tvOrder.getText().toString()));
//		paramList.add(new BasicNameValuePair("uniqCard", uniqueCard));
//		paramList.add(new BasicNameValuePair("bgFlag", TransactionValue.bgFlag));
//
//		sendPostRequest(paramList, this, TransactionValue.COMM_SEND_SMS_VERIFY_CODE,
//				DialogUtils.NO_WAIT_DIALOG);
	}
	
	/**
	 * 缴纳罚款
	 * 1、发送验证码
	 * 2、CSP扣款
	 * @param v
	 */
	@OnClick(R.id.bt_pay)
	public void btPay(View v) {
		Log.i("tag", "start");
		checkMsg = etVerifyCode.getText().toString();
		Log.i("tag", "amt:" + amt + "card:" + strCardBal);
		if(choiseCard.length() < 19){
			Toast.makeText(QztPayActivity.this, "该缴费暂不支持信用卡支付", Toast.LENGTH_SHORT).show();
			return;
		}
		if(amt == null || strCardBal == null){
			Toast.makeText(QztPayActivity.this, "没有获取到卡余额", Toast.LENGTH_LONG).show();
			return;
		}
		if(Float.parseFloat(amt) > Float.parseFloat(strCardBal)/100){
			Toast.makeText(QztPayActivity.this, "您的卡余额不足", Toast.LENGTH_LONG).show();
			return;
		}
		if(checkMsg.length() == 6){
			requestBocopForCheckMsg();
		}
		else{
			Toast.makeText(QztPayActivity.this, "请输入6位短信验证码", Toast.LENGTH_LONG).show();
		}
		
	}
	
	//验证短信验证码
		private void requestBocopForCheckMsg() {
			// TODO Auto-generated method stub
			Gson gson = new Gson();
//			List<Map<String,String>> list =new ArrayList<Map<String,String>>();
			Map<String,String> map = new HashMap<String,String>();
			map.put("USRID", LoginUtil.getUserId(this));
			map.put("chkcode", checkMsg);
			final String strGson = gson.toJson(map);
			
			BocOpUtil bocOpUtil = new BocOpUtil(this);
			bocOpUtil.postOpboc(strGson,TransactionValue.SA0056, new CallBackBoc() {
				
				@Override
				public void onSuccess(String responStr) {
					SA0052 sa0052;;
					Log.i("tag", responStr);
					try {
						sa0052 = JsonUtils.getObject(responStr, SA0052.class);
						Log.i("tag0", sa0052.getResult());
						if(sa0052.getResult().equals("0")){
							Log.i("tag1", sa0052.getResult() + " 短信验证通过，想CSP发送报文，缴纳罚款");
							requestCspForPay();
						}
						else{
							Toast.makeText(QztPayActivity.this, "短信验证码输入有误", Toast.LENGTH_LONG).show();
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
					
				}
			});
		}
		

	/**
	 * CSP缴费交易，验证成功后调用
	 */
	protected void requestCspForPay() {
		Gson gson = new Gson();
		Map<String,String> map = new HashMap<String,String>();
		map.put("userid",LoginUtil.getUserId(QztPayActivity.this));
		map.put("clientid",BocSdkConfig.CONSUMER_KEY);
		map.put("token",LoginUtil.getToken(QztPayActivity.this));
		map.put("order_num",orderNum);
		map.put("tran_amount",amt);
		map.put("card_no",choiseCard);
//		map.put("card_no","4563515005004121414");
		final String strGson = gson.toJson(map);
		Log.i("tag22", strGson);
		QztRequestWithJsonAndHead qztRequestWithJsonAndHead = new QztRequestWithJsonAndHead(QztPayActivity.this);
		qztRequestWithJsonAndHead.postOpboc(strGson, BocSdkConfig.qztpayUrl, new com.bocop.jxplatform.util.QztRequestWithJsonAndHead.CallBackBoc(){

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(String responStr) {
				// TODO Auto-generated method stub
				Bundle bundle = new Bundle();
				bundle.putString("orderNum", orderNum);
				bundle.putString("amt", amt);
				Intent intent = new Intent(QztPayActivity.this,
						QztApplyCompleteActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
			}

			@Override
			public void onFailure(String responStr) {
				// TODO Auto-generated method stub
				Toast.makeText(QztPayActivity.this, responStr, Toast.LENGTH_LONG).show();
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				
			}
			
		});
		}



	/**
	 * 监听短信数据库
	 */
	class SmsContent extends ContentObserver {

		private Cursor cursor = null;

		public SmsContent(Handler handler) {
			super(handler);
		}

		@Override
		public void onChange(boolean selfChange) {

			super.onChange(selfChange);
			// 读取收件箱中指定号码的短信
			cursor = managedQuery(Uri.parse("content://sms/inbox"), new String[] { "_id",
					"address", "read", "body" }, " address=?",
					new String[] { TransactionValue.telephoneNumber }, "date desc");

			if (cursor != null && cursor.getCount() > 0) {
				cursor.moveToNext();
				int smsbodyColumn = cursor.getColumnIndex("body");
				String smsBody = cursor.getString(smsbodyColumn);
				// 在这里把需要的验证码填到编辑框
				etVerifyCode.setText(MyUtils.getDynamicPassword(smsBody));
				Selection.setSelection(etVerifyCode.getText(), etVerifyCode.getText().length());// 移动光标到最后
			}

			// 在用managedQuery的时候，不能主动调用close()方法， 否则在Android 4.0+的系统上， 会发生崩溃
			if (Build.VERSION.SDK_INT < 14) {
				if (cursor != null) {
					cursor.close();
				}
			}
		}
	}
}
