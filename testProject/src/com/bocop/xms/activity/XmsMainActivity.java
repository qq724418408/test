package com.bocop.xms.activity;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnItemClick;
import com.boc.jx.constants.Constants;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.activity.CreditCardActivity;
import com.bocop.jxplatform.activity.WebActivity;
import com.bocop.jxplatform.activity.WebViewActivity;
import com.bocop.jxplatform.adapter.XmsItemAdapter;
import com.bocop.jxplatform.bean.app.AppInfo;
import com.bocop.jxplatform.config.TransactionValue;
import com.bocop.jxplatform.util.BocopDialog;
import com.bocop.jxplatform.util.CspUtil;
import com.bocop.jxplatform.util.CspUtil.CallBack;
import com.bocop.jxplatform.util.CustomProgressDialog;
import com.bocop.jxplatform.util.LoginUtil;
import com.bocop.jxplatform.util.LoginUtilAnother;
import com.bocop.jxplatform.util.LoginUtilAnother.ILoginListener;
import com.bocop.jxplatform.util.Mcis;
import com.bocop.jxplatform.view.BackButton;
import com.bocop.xms.service.SoundService;
import com.bocop.xms.utils.FormsUtil;
import com.bocop.xms.utils.IdcardInfoExtractor;
import com.bocop.xms.utils.ServiceWork;
import com.bocop.xms.xml.CspXmlXmsCom;
import com.bocop.xms.xml.message.MessageBean;
import com.bocop.xms.xml.message.MessageListResp;
import com.bocop.xms.xml.message.MessageListXmlBean;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * 秘书通主界面
 * 
 * @author ftl
 * 
 */
@ContentView(R.layout.xms_activity_xms_main)
public class XmsMainActivity extends BaseActivity implements ILoginListener {

	@ViewInject(R.id.llXmsMain)
	private LinearLayout llXmsMain;
	@ViewInject(R.id.iv_imageLeft)
	private BackButton ivImageLeft;
	@ViewInject(R.id.ivBack)
	private ImageView ivBack;
	@ViewInject(R.id.tv_titleName)
	private TextView tvTitle;
	@ViewInject(R.id.ivRefresh)
	private ImageView ivRefresh;
	@ViewInject(R.id.llRemind)
	private LinearLayout llRemind;
	@ViewInject(R.id.tvRemind)
	private TextView tvRemind;// 提醒内容

	@ViewInject(R.id.gvRemind)
	private GridView gvRemind;// 提醒秘书
	@ViewInject(R.id.gvJRService)
	private GridView gvJRService;// 金融秘书
	@ViewInject(R.id.gvDailyLife)
	private GridView gvDailyLife;// 生活秘书
	@ViewInject(R.id.gvNecessary)
	private GridView gvNecessary;// 出行秘书

	private XmsItemAdapter gvRemindAdapter;
	private XmsItemAdapter gvJRAdapter;
	private XmsItemAdapter dailyLifeAdapter;
	private XmsItemAdapter necessaryAdapter;
	private int flag = 0;
	private BocopDialog dialog;// 免责申明
	private List<MessageBean> msgData;

	private static final int MY_MESSAGE_CODE = 101;
	private static final int FINANCE_MESSAGE_CODE = 102;
	private static final int PAY_REMIND_CODE = 103;
	private static final int CUSTOM_REMIND_CODE = 104;
	private static final int CREDIT_POINT_CODE = 105;

	private Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			if (LoginUtil.isLog(XmsMainActivity.this) && isShowDialog() && isBirthday()) {
				Log.i("tagg", "显示生日提醒");
				showAdDialog(XmsMainActivity.this.llXmsMain);
			} else {
				Log.i("tagg", "不显示生日提醒");
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initTitle();
		initData();
		initListener();
		mHandler.sendEmptyMessageDelayed(0, 200);
	}

	private void initTitle() {
		tvTitle.setText("秘书通");
		ivImageLeft.setVisibility(View.GONE);
		ivBack.setVisibility(View.VISIBLE);
	}

	private void initData() {
		gvRemindAdapter = new XmsItemAdapter(this, R.array.xmsremind, "1");
		gvRemind.setAdapter(gvRemindAdapter);

		gvJRAdapter = new XmsItemAdapter(this, R.array.xmsjrservice, "2");
		gvJRService.setAdapter(gvJRAdapter);

		dailyLifeAdapter = new XmsItemAdapter(this, R.array.xmsotherser, "3");
		gvDailyLife.setAdapter(dailyLifeAdapter);

		necessaryAdapter = new XmsItemAdapter(this, R.array.xmsnecessary, "4");
		gvNecessary.setAdapter(necessaryAdapter);

		dialog = new BocopDialog(this, "免责声明",
				"\t\t秘书通引用的第三方服务网址、商户及商品信息，意在为客户提供方便快捷的服务链接，所有服务均由第三方服务商提供，相关服务和责任将由第三方承担，如在使用服务的过程中有任何问题和纠纷，请您直接与第三方服务商沟通解决。");

	}

	private void initListener() {
		ivBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@OnItemClick({ R.id.gvRemind, R.id.gvJRService, R.id.gvDailyLife, R.id.gvNecessary })
	public void setOnItemClickListener(AdapterView<?> parent, View view, final int position, long id) {
		switch (parent.getId()) {
		case R.id.gvRemind:
			handleEvent(R.id.gvRemind, 1, gvRemindAdapter.getItem(position).getName(), position);
			break;
		case R.id.gvJRService:
			handleEvent(R.id.gvJRService, 1, gvJRAdapter.getItem(position).getName(), position);
			break;
		case R.id.gvDailyLife:
			handleEvent(R.id.gvDailyLife, flag, dailyLifeAdapter.getItem(position).getName(), position);
			break;
		case R.id.gvNecessary:
			handleEvent(R.id.gvNecessary, flag, necessaryAdapter.getItem(position).getName(), position);
			break;
		}
	}

	private void handleEvent(final int id, int flagParam, String name, final int position) {
		if (this.getBaseApp().isNetStat()) {
			if (!"".equals(name)) {
				if (flagParam == 1) {
					handleJump(id, position);
				} else {
					dialog.setPositiveButton(new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							flag = 1;
							handleJump(id, position);
							dialog.cancel();
						}
					}, "同意");
					dialog.setNegativeButton(new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					}, "不同意");
					if (!this.isFinishing()) {
						dialog.show();
					}
				}
			}
		} else {
			CustomProgressDialog.showBocNetworkSetDialog(this);
		}
	}

	private void handleJump(int id, int position) {
		switch (id) {
		case R.id.gvRemind:
			switchRemind(position);
			break;
		case R.id.gvJRService:
			switchJRService(position);
			break;
		case R.id.gvDailyLife:
			switchDailyLife(position);
			break;
		case R.id.gvNecessary:
			switchNecessary(position);
			break;
		}
	}

	/**
	 * 提醒秘书
	 * 
	 * @param position
	 */
	private void switchRemind(int position) {
		int code = position == 0 ? MY_MESSAGE_CODE : FINANCE_MESSAGE_CODE;
		switch (position) {
		// 我的消息
		case 0:
			if (LoginUtil.isLog(this)) {
				Intent intentMsg = new Intent(this, MyMessageActivity.class);
				intentMsg.putExtra("type", position);
				startActivity(intentMsg);
			} else {
				LoginUtilAnother.authorizeAnother(this, this, code);
			}
			break;
		// 金融信息
		case 1:
			Intent intentMsg = new Intent(this, MyMessageActivity.class);
			intentMsg.putExtra("type", position);
			startActivity(intentMsg);
			break;
		// 缴费提醒
		case 2:
			if (LoginUtil.isLog(this)) {
				Intent intentpPay = new Intent(this, PayRemindActivity.class);
				startActivity(intentpPay);
			} else {
				LoginUtilAnother.authorizeAnother(this, this, PAY_REMIND_CODE);
			}
			break;
		// 自定义提醒
		case 3:
			if (LoginUtil.isLog(this)) {
				Intent intentpRemind = new Intent(this, CustomRemindActivity.class);
				startActivity(intentpRemind);
			} else {
				LoginUtilAnother.authorizeAnother(this, this, CUSTOM_REMIND_CODE);
			}
			break;
		}
	}

	/**
	 * 金融秘书
	 * 
	 * @param position
	 */
	private void switchJRService(int position) {
		switch (position) {
		// 存贷款利率
		case 0:
			Bundle bundleRate = new Bundle();
			bundleRate.putString("url", Constants.xmsUrlForRate);
			bundleRate.putString("name", "存贷款利率");
			Intent intentRate = new Intent(this, WebActivity.class);
			intentRate.putExtras(bundleRate);
			startActivity(intentRate);
			break;
		// 外汇牌价
		case 1:
			Bundle bundleExchange = new Bundle();
			bundleExchange.putString("url", Constants.xmsUrlForExchange);
			bundleExchange.putString("name", "外汇牌价");
			Intent intentExchange = new Intent(this, WebActivity.class);
			intentExchange.putExtras(bundleExchange);
			startActivity(intentExchange);
			break;
		// 信用卡积分
		case 2:
			if (LoginUtil.isLog(this)) {
				Intent intentDebitCard = new Intent(this, CreditCardActivity.class);
				startActivity(intentDebitCard);
			} else {
				LoginUtilAnother.authorizeAnother(this, this, CREDIT_POINT_CODE);
			}
			break;
		// 和讯财经
		case 3:
			Bundle bundleHx = new Bundle();
			bundleHx.putString("url", Constants.xmsUrlForHx);
			bundleHx.putString("name", "和讯财经");
			Intent intentHx = new Intent(this, WebViewActivity.class);
			intentHx.putExtras(bundleHx);
			startActivity(intentHx);
			break;
		}
	}

	/**
	 * 生活秘书
	 * 
	 * @param position
	 */
	private void switchDailyLife(int position) {
		switch (position) {
		// 天气预报
		case 0:
			Bundle bundleWeather = new Bundle();
			bundleWeather.putString("url", Constants.xmsUrlForWeather);
			bundleWeather.putString("name", "天气预报");
			Intent intentWeather = new Intent(this, WebViewActivity.class);
			intentWeather.putExtras(bundleWeather);
			startActivity(intentWeather);
			break;
		// 美食团购
		case 1:
			Bundle bundleDzdp = new Bundle();
			bundleDzdp.putString("url", Constants.xmsUrlForDzdp);
			bundleDzdp.putString("name", "美食团购");
			Intent intentDzdp = new Intent(this, WebViewActivity.class);
			intentDzdp.putExtras(bundleDzdp);
			startActivity(intentDzdp);
			break;
		// 家政服务
		case 2:
			Bundle bundle58Home = new Bundle();
			bundle58Home.putString("url", Constants.xmsUrlFor58Home);
			bundle58Home.putString("name", "家政服务");
			Intent intent58Home = new Intent(this, WebViewActivity.class);
			intent58Home.putExtras(bundle58Home);
			startActivity(intent58Home);
			break;
		// 电影票
		case 3:
			Bundle bundleSpider = new Bundle();
			bundleSpider.putString("url", Constants.xmsUrlForSpider);
			bundleSpider.putString("name", "江西电影网");
			Intent intentSpider = new Intent(this, WebViewActivity.class);
			intentSpider.putExtras(bundleSpider);
			startActivity(intentSpider);
			break;
		// 快递100
		case 4:
			Bundle bundleExpress = new Bundle();
			bundleExpress.putString("url", Constants.xmsUrlForExpress);
			bundleExpress.putString("name", "快递查询");
			Intent intentExpress = new Intent(this, WebViewActivity.class);
			intentExpress.putExtras(bundleExpress);
			startActivity(intentExpress);
			break;
		// 在线翻译
		case 5:
			Bundle bundleTranlate = new Bundle();
			bundleTranlate.putString("url", Constants.xmsUrlForTranlate);
			bundleTranlate.putString("name", "在线翻译");
			Intent intentTranlate = new Intent(this, WebViewActivity.class);
			intentTranlate.putExtras(bundleTranlate);
			startActivity(intentTranlate);
			break;
		// 头条新闻
		case 6:
			Bundle bundleJiudian = new Bundle();
			bundleJiudian.putString("url", Constants.xmsUrlForToutiao);
			bundleJiudian.putString("name", "头条新闻");
			Intent intentJiudian = new Intent(this, WebViewActivity.class);
			intentJiudian.putExtras(bundleJiudian);
			startActivity(intentJiudian);
			break;
		// 中银E商
		case 7:
			Bundle bundleBoce = new Bundle();
			bundleBoce.putString("url", Constants.xmsUrlForBoce);
			bundleBoce.putString("name", "中银e商");
			Intent intentBoce = new Intent(this, WebViewActivity.class);
			intentBoce.putExtras(bundleBoce);
			startActivity(intentBoce);
			break;

		}
	}

	/**
	 * 出行秘书
	 * 
	 * @param position
	 */
	private void switchNecessary(int position) {
		switch (position) {
		// 打车服务
		case 0:
			Bundle bundleDidi = new Bundle();
			bundleDidi.putString("url", Constants.xmsUrlForDidi);
			bundleDidi.putString("name", "打车服务");
			Intent intentDidi = new Intent(this, WebViewActivity.class);
			intentDidi.putExtras(bundleDidi);
			startActivity(intentDidi);
			break;
		// 购飞机票
		case 1:
			Bundle bundleFlight = new Bundle();
			bundleFlight.putString("url", Constants.xmsUrlForFight);
			bundleFlight.putString("name", "购飞机票");
			Intent intentFlight = new Intent(this, WebViewActivity.class);
			intentFlight.putExtras(bundleFlight);
			startActivity(intentFlight);
			break;
		// 购火车票
		case 2:
			Bundle bundleTrain = new Bundle();
			bundleTrain.putString("url", Constants.xmsUrlForTrain);
			bundleTrain.putString("name", "购火车票");
			Intent intentTrain = new Intent(this, WebViewActivity.class);
			intentTrain.putExtras(bundleTrain);
			startActivity(intentTrain);
			break;
		// 购汽车票
		case 3:
			Bundle bundle = new Bundle();
			bundle.putString("url", Constants.xmsUrlForLt100);
			bundle.putString("name", "购汽车票");
			Intent intent = new Intent(this, WebViewActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		// 地图服务
		case 4:
			Bundle bundleBaidu = new Bundle();
			bundleBaidu.putString("url", Constants.xmsUrlForBaidu);
			bundleBaidu.putString("name", "地图服务");
			Intent intentBaidu = new Intent(this, WebViewActivity.class);
			intentBaidu.putExtras(bundleBaidu);
			startActivity(intentBaidu);
			break;
		// 酒店服务
		case 5:
			Bundle bundleToutiao = new Bundle();
			bundleToutiao.putString("url", Constants.xmsUrlForJiudian);
			bundleToutiao.putString("name", "酒店服务");
			Intent intentToutiao = new Intent(this, WebViewActivity.class);
			intentToutiao.putExtras(bundleToutiao);
			startActivity(intentToutiao);
			break;
		}

	}

	/**
	 * 显示广告
	 */
	private void showAdDialog(final View rootView) {
		Log.i("tag", "getLayoutInflater");
		View view = getLayoutInflater().inflate(R.layout.xms_layout_dialog_ad, null);
		Log.i("tag", "PopupWindow");
		final PopupWindow adWindow = new PopupWindow(view, FormsUtil.SCREEN_WIDTH, FormsUtil.SCREEN_HEIGHT);
		Log.i("tag", "showAtLocation");
		adWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0);
		Log.i("tag", "findViewById");
		ImageView ivClose = (ImageView) view.findViewById(R.id.ivClose);
		ivClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				adWindow.dismiss();

				stopPlayBirthday();
			}
		});

		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		String year = str.substring(0, 4);
		Log.i("showAdDialog当前年份：", year);
		final SharedPreferences sp = getSharedPreferences(Constants.CUSTOM_PREFERENCE_NAME, Context.MODE_PRIVATE);
		Editor mEditor = sp.edit();
		mEditor.putBoolean(year, true);
		mEditor.commit();

		playBirthday();
	}

	private Boolean isShowDialog() {

		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		String year = str.substring(0, 4);
		Log.i("当前年份：", year);
		final SharedPreferences sp = this.getSharedPreferences(Constants.CUSTOM_PREFERENCE_NAME, Context.MODE_PRIVATE);
		boolean hasShowed = sp.getBoolean(year, false);
		if (!hasShowed) {
			Log.i("tag", "当年还没有生日提醒，返回true,需要进行生日提醒");
			return true;
		} else {
			Log.i("tag", "当年提交进行过生日提醒，返回false,不需要进行生日提醒");
			return false;
		}
	}

	private Boolean isBirthday() {
		try {
			final SharedPreferences sp = this.getSharedPreferences(Constants.CUSTOM_PREFERENCE_NAME,
					Context.MODE_PRIVATE);
			String cardId = sp.getString(Constants.CUSTOM_ID_NO, null);
			if (cardId != null) {
				Log.i("tag", "获取身份证" + cardId);
				IdcardInfoExtractor idcardInfoExtractor = new IdcardInfoExtractor(cardId);
				if (idcardInfoExtractor.getIsBirthday()) {
					return true;
				} else {
					return false;
				}
			} else {
				Log.i("tag", "没有身份证");
				return false;
			}
		} catch (Exception e) {
			return false;
		}

	}

	private void playBirthday() {
		Intent intent = new Intent(XmsMainActivity.this, SoundService.class);
		intent.putExtra("playing", true);
		startService(intent);
	}

	private void stopPlayBirthday() {
		Intent intent = new Intent(XmsMainActivity.this, SoundService.class);
		intent.putExtra("playing", false);
		startService(intent);
	}

	@Override
	public void onResume() {
		super.onResume();
		requestMessagePre();
	}

	/**
	 * 请求消息准备
	 */
	public void requestMessagePre() {
		if (LoginUtil.isLog(this)) {
			requestMessage(0);
		} else {
			requestMessage(1);
		}
	}

	/**
	 * 请求消息
	 */
	private void requestMessage(final int type) {
		try {
			// 生成CSP XML报文
			String txCode;
			CspXmlXmsCom cspXmlXmsCom = null;
			String tranCode = "";
			if (type == 0) {
				txCode = "MS002005";
				tranCode = TransactionValue.CSPSZF;
				cspXmlXmsCom = new CspXmlXmsCom(LoginUtil.getUserId(this), txCode);
				cspXmlXmsCom.setPageNo("");
			} else if (type == 1) {
				txCode = "MS002012";
				tranCode = TransactionValue.GETMSG;
				cspXmlXmsCom = new CspXmlXmsCom("",txCode);
			}
			String strXml = cspXmlXmsCom.getCspXml();
			Log.i("tag", "getMSgXml");
			// 生成MCIS报文
			Mcis mcis = new Mcis(strXml, tranCode);
			Log.i("tag", "Mcis");
			final byte[] byteMessage = mcis.getMcis();
			// 发送报文
			CspUtil cspUtil = new CspUtil(this);
			Log.i("tag", "发送报文： " + new String(byteMessage, "GBK"));
			// cspUtil.setTest(true);
			CallBack callBack=new CallBack() {
				
				@Override
				public void onSuccess(String responStr) {
					// TODO Auto-generated method stub
					if (responStr != null) {
						MessageListXmlBean msgListXmlBean = MessageListResp.readStringXml(responStr);
						if (msgListXmlBean!=null&&msgListXmlBean.getErrorcode().equals("00")) {
							msgData = msgListXmlBean.getMessageList();
							changeMsgCount();
						}
					}
					
				}
				
				@Override
				public void onFinish() {
				}
				
				@Override
				public void onFailure(String responStr) {
				}
			};
			if (type == 0) {
				cspUtil.postCspLogin(new String(byteMessage, "GBK"), callBack, false);
			} else if (type == 1) {
				cspUtil.postCspNoLogin(new String(byteMessage, "GBK"), callBack, false);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 改变显示消息数量
	 */
	@SuppressWarnings("unchecked")
	private void changeMsgCount() {
		if (msgData != null && msgData.size() > 0) {
			SharedPreferences sp = getSharedPreferences(MyMessageActivity.SELECT_ROLE, Context.MODE_PRIVATE);
			Map<String, Object> map = (Map<String, Object>) sp.getAll();
			Set<String> keyset = map.keySet();
			List<String> list = new ArrayList<String>(keyset);
			list.remove(MyMessageActivity.FIRST_SELECT);
			list.remove(MyMessageActivity.CURRENT_ROLE);
			// 我的消息数量
			int myMsgCount = 0;
			// 我的消息已读数量
			int myReadCount = 0;
			// 金融消息已读数量
			int finReadCount = 0;
			for (int i = 0; i < msgData.size(); i++) {
				if (!"20".equals(msgData.get(i).getType())) {
					myMsgCount++;
				}
			}
			for (int i = 0; i < list.size(); i++) {
				String key = list.get(i);
				int flag = 0;// 标志位
				for (int j = 0; j < msgData.size(); j++) {
					if (key.equals(msgData.get(j).getMessageId())) {
						flag = 1;
						if (!"20".equals(msgData.get(j).getType())) {
							myReadCount++;
						} else {
							finReadCount++;
						}
					}
				}
				if (flag == 0) {
					sp.edit().remove(key).commit();
				}
			}
			AppInfo myMsgInfo = gvRemindAdapter.getItem(0);
			AppInfo finMsgInfo = gvRemindAdapter.getItem(1);
			if (myMsgInfo != null) {
				myMsgInfo.setMsgCount(myMsgCount - myReadCount);
			}
			if (finMsgInfo != null) {
				finMsgInfo.setMsgCount(msgData.size() - myMsgCount - finReadCount);
			}
			gvRemindAdapter.notifyDataSetChanged();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (ServiceWork.isServiceWork(XmsMainActivity.this, "com.bocop.xms.service.SoundService"))
			;
		{
			stopPlayBirthday();
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (ServiceWork.isServiceWork(XmsMainActivity.this, "com.bocop.xms.service.SoundService"))
			;
		{
			stopPlayBirthday();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (ServiceWork.isServiceWork(XmsMainActivity.this, "com.bocop.xms.service.SoundService"))
			;
		{
			stopPlayBirthday();
		}
	}

	@Override
	public void onLogin(int position) {
		requestMessage(0);
		switch (position) {
		case MY_MESSAGE_CODE:
			int type = position == MY_MESSAGE_CODE ? 0 : 1;
			Intent intentMsg = new Intent(this, MyMessageActivity.class);
			intentMsg.putExtra("type", type);
			startActivity(intentMsg);
			break;
		case PAY_REMIND_CODE:
			Intent intentpPay = new Intent(this, PayRemindActivity.class);
			startActivity(intentpPay);
			break;
		case CUSTOM_REMIND_CODE:
			Intent intentpRemind = new Intent(this, CustomRemindActivity.class);
			startActivity(intentpRemind);
			break;
		case CREDIT_POINT_CODE:
			Intent intentDebitCard = new Intent(this, CreditCardActivity.class);
			startActivity(intentDebitCard);
			break;
		}
	}

	@Override
	public void onLogin() {

	}

	@Override
	public void onCancle() {

	}

	@Override
	public void onError() {

	}

	@Override
	public void onException() {

	}
}
