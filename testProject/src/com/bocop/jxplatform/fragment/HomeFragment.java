package com.bocop.jxplatform.fragment;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.boc.jx.base.BaseApplication;
import com.boc.jx.base.BaseFragment;
import com.boc.jx.baseUtil.asynchttpclient.AsyncHttpResponseHandler;
import com.boc.jx.baseUtil.asynchttpclient.RequestParams;
import com.boc.jx.baseUtil.cache.CacheBean;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnTouch;
import com.boc.jx.common.util.ContentUtils;
import com.boc.jx.constants.Constants;
import com.boc.jx.view.indicator.CirclePageIndicator;
import com.bocop.cft.activity.CftActivity;
import com.bocop.gm.GoldManagerActivity;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.activity.BMJFActivity;
import com.bocop.jxplatform.activity.DZPActivity;
import com.bocop.jxplatform.activity.EXYActivity;
import com.bocop.jxplatform.activity.GjtFirstActivity;
import com.bocop.jxplatform.activity.HDTActivity;
import com.bocop.jxplatform.activity.HomeAdvDetailActivity;
import com.bocop.jxplatform.activity.JFSCActivity;
import com.bocop.jxplatform.activity.JIEHUIActivity;
import com.bocop.jxplatform.activity.JKETActivity;
import com.bocop.jxplatform.activity.KhtFirstActivity;
import com.bocop.jxplatform.activity.LYTActivity;
import com.bocop.jxplatform.activity.WEIHUIActivity;
import com.bocop.jxplatform.activity.WebActivity;
import com.bocop.jxplatform.activity.WebDialogActivity;
import com.bocop.jxplatform.activity.WebForZytActivity;
import com.bocop.jxplatform.activity.WebViewActivity;
import com.bocop.jxplatform.activity.WebViewForGjsActivity;
import com.bocop.jxplatform.activity.YPTActivity;
import com.bocop.jxplatform.activity.ZYEDActivity;
import com.bocop.jxplatform.activity.riders.AddGasOilServiceActivity;
import com.bocop.jxplatform.activity.riders.MoneySelectWebView;
import com.bocop.jxplatform.activity.riders.RiderFristActivity;
import com.bocop.jxplatform.adapter.HomeImageAdapter;
import com.bocop.jxplatform.adapter.HomeItemAdapter;
import com.bocop.jxplatform.adapter.LoopViewPagerAdapter;
import com.bocop.jxplatform.bean.Advertisement;
import com.bocop.jxplatform.bean.MainDialogBean;
import com.bocop.jxplatform.bean.MainDialogListBean;
import com.bocop.jxplatform.bean.app.AppInfo;
import com.bocop.jxplatform.config.BocSdkConfig;
import com.bocop.jxplatform.config.TransactionValue;
import com.bocop.jxplatform.http.RestTemplate;
import com.bocop.jxplatform.http.RestTemplateJxBank;
import com.bocop.jxplatform.trafficassistant.TrafficAssistantMainActivity;
import com.bocop.jxplatform.util.BocopDialog;
import com.bocop.jxplatform.util.BocopDialog2;
import com.bocop.jxplatform.util.CspUtil;
import com.bocop.jxplatform.util.CspUtil.CallBack;
import com.bocop.jxplatform.util.CustomInfo;
import com.bocop.jxplatform.util.CustomProgressDialog;
import com.bocop.jxplatform.util.FormsUtil;
import com.bocop.jxplatform.util.JsonUtils;
import com.bocop.jxplatform.util.LoginUtil;
import com.bocop.jxplatform.util.LoginUtilAnother;
import com.bocop.jxplatform.util.LoginUtilAnother.ILoginListener;
import com.bocop.jxplatform.util.Mcis;
import com.bocop.jxplatform.util.QztRequestWithJsonAll;
import com.bocop.jxplatform.util.QztRequestWithJsonAndHead;
import com.bocop.jxplatform.util.SpUtil;
import com.bocop.jxplatform.util.Update;
import com.bocop.jxplatform.util.Update.DialogShowingListener;
import com.bocop.kht.activity.KhtActivity;
import com.bocop.qzt.QztMainActivity;
import com.bocop.xfjr.XfjrMain;
import com.bocop.xms.activity.MyMessageActivity;
import com.bocop.xms.activity.XmsMainActivity;
import com.bocop.xms.xml.CspXmlXmsCom;
import com.bocop.xms.xml.message.MessageBean;
import com.bocop.xms.xml.message.MessageListResp;
import com.bocop.xms.xml.message.MessageListXmlBean;
import com.bocop.yfx.activity.TrainsActivity;
import com.bocop.yfx.activity.myloan.RemainingSumPickUpActivity;
import com.bocsoft.ofa.http.asynchttpclient.JsonHttpResponseHandler;
import com.bocsoft.ofa.http.asynchttpclient.expand.JsonRequestParams;
import com.google.gson.Gson;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.RequestBody;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HomeFragment extends BaseFragment implements ILoginListener {

	@ViewInject(R.id.ll_helper)
	private LinearLayout llHelper;
	@ViewInject(R.id.ll_financial)
	private LinearLayout llFinancial;
	@ViewInject(R.id.ll_secretary)
	private LinearLayout llSecretary;
	@ViewInject(R.id.ll_illegals)
	private LinearLayout llIllegals;
	@ViewInject(R.id.ivSingleImage)
	private ImageView ivSingleImage;
	@ViewInject(R.id.rltAd)
	private RelativeLayout rltAd;
	@ViewInject(R.id.vpAd)
	private ViewPager vpAd;
	@ViewInject(R.id.indicator)
	private CirclePageIndicator indicator;

	private HomeItemAdapter lifeAdapter;
	private HomeItemAdapter financeAdapter;
	private HomeItemAdapter financeFpAdapter;
	private HomeItemAdapter shopAdapter;
	private HomeItemAdapter facilityAdapter;

	@ViewInject(R.id.gvLifeSer)
	private GridView gvLifeSer;
	@ViewInject(R.id.gvFinanceSer)
	private GridView gvFinanceSer;
	@ViewInject(R.id.gvFinanceFpSer)
	private GridView gvFinanceFpSer;
	@ViewInject(R.id.gvFacilitySer)
	private GridView gvFacilitySer;
	@ViewInject(R.id.gvShopping)
	private GridView gvShopping;
	@ViewInject(R.id.gvImage)
	private GridView gvImage;

	private List<MessageBean> msgData;
	private HomeImageAdapter imageAdapter;

	public BaseApplication baseApplication = BaseApplication.getInstance();
	public ProgressDialog progressDialog;
	public int id;
	private float preX = 0;
	private float preY = 0;
	private float nowX = 0;
	private float nowY = 0;
	private boolean isTouch = false;// 是否正在拖动轮播图
	private List<Advertisement> mAdvList = new ArrayList<Advertisement>();
	private List<View> views = new ArrayList<View>();
	private static String[] homePageLogEvent;
	private static String[] homeLifeLogEvent;
	private static String[] homeFinaceLogEvent;
	private static String[] homeFpFinaceLogEvent;
	private static String[] homeFacilityLogEvent;
	private static String[] homeShoppingLogEvent;

	private static String[] homePageImageLogEvent;
	private Handler adHandler = new Handler() { // 启动广告页面自动播放
		@Override
		public void handleMessage(Message msg) {
			if (!isTouch) {
				if (vpAd.getCurrentItem() == views.size() - 1) {
					vpAd.setCurrentItem(0, false);
				} else {
					vpAd.setCurrentItem(vpAd.getCurrentItem() + 1);
				}
				adHandler.sendEmptyMessageDelayed(0, 3000);
			} else {
				adHandler.sendEmptyMessageDelayed(0, 3000);
			}
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = initView(R.layout.fragment_home);

		// homePageLogEvent = baseActivity.getResources().getStringArray(
		// R.array.homepagelog_array);
		homePageImageLogEvent = baseActivity.getResources().getStringArray(R.array.homepageimagelog_array);
		homeLifeLogEvent = baseActivity.getResources().getStringArray(R.array.homelifelog_array);
		homeFinaceLogEvent = baseActivity.getResources().getStringArray(R.array.homefinacelog_array);
		homeFpFinaceLogEvent = baseActivity.getResources().getStringArray(R.array.homeFpfinacelog_array);
		homeFacilityLogEvent = baseActivity.getResources().getStringArray(R.array.homefacilitylog_array);
		homeShoppingLogEvent = baseActivity.getResources().getStringArray(R.array.homeshoppinglog_array);
		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setListener();
	}

	/**
	 * 设置监听事件
	 */
	private void setListener() {
		lifeAdapter = new HomeItemAdapter(getActivity(), R.array.life, "0");
		financeAdapter = new HomeItemAdapter(getActivity(), R.array.finance, "1");
		shopAdapter = new HomeItemAdapter(getActivity(), R.array.shopping, "2");
		facilityAdapter = new HomeItemAdapter(getActivity(), R.array.facility, "3");
		financeFpAdapter = new HomeItemAdapter(getActivity(), R.array.fpFinance, "4");
		imageAdapter = new HomeImageAdapter(getActivity());

		gvLifeSer.setAdapter(lifeAdapter);
		gvFinanceSer.setAdapter(financeAdapter);
		gvFinanceFpSer.setAdapter(financeFpAdapter);
		gvShopping.setAdapter(shopAdapter);
		gvFacilitySer.setAdapter(facilityAdapter);
		gvImage.setAdapter(imageAdapter);

		// 快捷连接
		gvImage.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				requestCustidAndLog(homePageImageLogEvent[position]);
				invokeMethod(imageAdapter.getData(), position);
			}
		});

		// 生活服务
		gvLifeSer.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				requestCustidAndLog(homeLifeLogEvent[position]);
				invokeMethod(lifeAdapter.getData(), position);
			}
		});

		// 金融服务
		gvFinanceSer.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				requestCustidAndLog(homeFinaceLogEvent[position]);
				invokeMethod(financeAdapter.getData(), position);
			}
		});

		// 金融精准扶贫
		gvFinanceFpSer.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				requestCustidAndLog(homeFpFinaceLogEvent[position]);
				invokeMethod(financeFpAdapter.getData(), position);
			}
		});
		// 授信服务
		gvFacilitySer.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				requestCustidAndLog(homeFacilityLogEvent[position]);
				invokeMethod(facilityAdapter.getData(), position);
			}
		});

		gvShopping.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// if (!NetworkUtils.isNetworkConnected(baseActivity)) {
				// return;
				// }
				requestCustidAndLog(homeShoppingLogEvent[position]);
				invokeMethod(shopAdapter.getData(), position);
			}
		});

		/**
		 * 违章处理
		 */
		llIllegals.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				BaseApplication application = (BaseApplication) baseActivity.getApplication();
				if (application.isNetStat()) {
					if (LoginUtil.isLog(baseActivity)) {
						Intent intent = new Intent(baseActivity, TrafficAssistantMainActivity.class);
						startActivity(intent);
					} else {
						LoginUtilAnother.authorizeAnother(getActivity(), HomeFragment.this, 5);
					}
				} else {
					CustomProgressDialog.showBocNetworkSetDialog(baseActivity);
				}
			}
		});
		/**
		 * 小秘书
		 */
		llSecretary.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				BaseApplication application = (BaseApplication) baseActivity.getApplication();
				if (application.isNetStat()) {
					// if (LoginUtil.isLog(baseActivity)) {
					Intent intent = new Intent(baseActivity, XmsMainActivity.class);
					startActivity(intent);
					// } else {
					// LoginUtilAnother.authorizeAnother(getActivity(),
					// HomeFragment.this, 1);
					// }
				} else {
					CustomProgressDialog.showBocNetworkSetDialog(baseActivity);
				}
			}
		});
		/**
		 * 理财通
		 */
		llFinancial.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				BaseApplication application = (BaseApplication) baseActivity.getApplication();
				if (application.isNetStat()) {
					if (LoginUtil.isLog(baseActivity)) {
						getFinances(getActivity());
					} else {
						LoginUtilAnother.authorizeAnother(getActivity(), HomeFragment.this, 2);
					}
				} else {
					CustomProgressDialog.showBocNetworkSetDialog(baseActivity);
				}
			}
		});
		/**
		 * 加油服务
		 */
		llHelper.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				BaseApplication application = (BaseApplication) baseActivity.getApplication();
				if (application.isNetStat()) {
					if (LoginUtil.isLog(baseActivity)) {
						// ((com.bocop.jxplatform.activity.riders.RiderFristActivity)
						// getActivity()).postUserInfo(id);
						Intent intentAddOil = new Intent(getActivity(), AddGasOilServiceActivity.class);
						startActivity(intentAddOil);
					} else {
						LoginUtilAnother.authorizeAnother(getActivity(), HomeFragment.this, 5);
					}
				} else {
					CustomProgressDialog.showBocNetworkSetDialog(baseActivity);
				}
			}
		});
	}

	/**
	 * 请求客户id和上送日志
	 * 
	 * @param eventId
	 */
	private void requestCustidAndLog(String eventId) {
		if (LoginUtil.isLog(getActivity())) {
			if (!CustomInfo.isExistCustomInfo(baseActivity)) {
				Log.i("LoginUtil", "requestBocopForCustid");
				CustomInfo.requestBocopForCustid(baseActivity, false);
			}
		}
		Log.i("LoginUtil", "postIdForXms");
		CustomInfo.postLog(baseActivity, eventId);
	}

	/**
	 * 动态调用方法
	 * 
	 * @param mData
	 * @param position
	 */
	private void invokeMethod(List<AppInfo> mData, int position) {
		if (mData != null) {
			AppInfo appInfo = mData.get(position);
			if (appInfo != null && !"".equals(appInfo.getMethodName())) {
				Object[] parameter = appInfo.getParameter();
				try {
					if (parameter == null) {
						HomeFragment.this.getClass().getMethod(appInfo.getMethodName()).invoke(HomeFragment.this);
					} else {
						Class<?>[] classType = new Class[parameter.length];
						for (int i = 0; i < parameter.length; i++) {
							if (parameter[i] instanceof String) {
								classType[i] = String.class;
							} else if (parameter[i] instanceof Integer) {
								classType[i] = int.class;
							} else if (parameter[i] instanceof Boolean) {
								classType[i] = boolean.class;
							}
						}
						HomeFragment.this.getClass().getMethod(appInfo.getMethodName(), classType)
								.invoke(HomeFragment.this, parameter);
					}
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				}
			}
		}
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
		if (LoginUtil.isLog(baseActivity)) {
			requestMessage(0);
		} else {
			requestMessage(1);
		}
	}

	/**
	 * 隐藏消息
	 */
	public void hiddenMessage() {
		msgData = null;
		lifeAdapter.getMstAppInfo().setMsgCount(0);
		lifeAdapter.notifyDataSetChanged();
	}

	@Override
	protected void initData() {
		super.initData();
		init();
		gotoDZP(baseActivity);
		// 查询是否有新版本
		try {
			freshOauth();
		} catch (Exception e) {
			Log.i("tag", "freshOauth 发送异常");
		}

	}

	/**
	 * 请求消息
	 */
	private void requestMessage(final int type) {
		try {
			// 生成CSP XML报文
			String txCode;
			String tranCode = "";
			CspXmlXmsCom cspXmlXmsCom = null;
			if (type == 0) {
				txCode = "MS002005";
				tranCode = TransactionValue.CSPSZF;
				cspXmlXmsCom = new CspXmlXmsCom(LoginUtil.getUserId(baseActivity), txCode);
				cspXmlXmsCom.setPageNo("");
			} else if (type == 1) {
				txCode = "MS002012";
				tranCode = TransactionValue.GETMSG;
				cspXmlXmsCom = new CspXmlXmsCom("", txCode);
			}
			String strXml = cspXmlXmsCom.getCspXml();
			Log.i("tag", "getMSgXml");
			// 生成MCIS报文
			Mcis mcis = new Mcis(strXml, tranCode);
			Log.i("tag", "Mcis");
			final byte[] byteMessage = mcis.getMcis();
			// 发送报文
			CspUtil cspUtil = new CspUtil(baseActivity);
			Log.i("tag", "发送报文： " + new String(byteMessage, "GBK"));
			CallBack callBack = new CallBack() {

				@Override
				public void onSuccess(String responStr) {
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
					Toast.makeText(baseActivity, responStr, Toast.LENGTH_SHORT).show();
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
		if (msgData != null) {
			SharedPreferences sp = baseActivity.getSharedPreferences(MyMessageActivity.SELECT_ROLE,
					Context.MODE_PRIVATE);
			Map<String, Object> map = (Map<String, Object>) sp.getAll();
			Set<String> keyset = map.keySet();
			List<String> list = new ArrayList<String>(keyset);
			int removeCount = 0;
			for (int i = 0; i < list.size(); i++) {
				String key = list.get(i);
				int flag = 0;// 标志位
				if (!key.equals(MyMessageActivity.FIRST_SELECT) && !key.equals(MyMessageActivity.CURRENT_ROLE)) {
					for (int j = 0; j < msgData.size(); j++) {
						if (key.equals(msgData.get(j).getMessageId())) {
							flag = 1;
						}
					}
					if (flag == 0) {
						removeCount++;
						sp.edit().remove(key).commit();
					}
				}
			}
			list.remove(MyMessageActivity.FIRST_SELECT);
			list.remove(MyMessageActivity.CURRENT_ROLE);
			int msgCount = msgData.size() - (list.size() + removeCount);
			if (msgCount < 0) {
				msgCount = 0;
			}
			lifeAdapter.getMstAppInfo().setMsgCount(msgCount);
			lifeAdapter.notifyDataSetChanged();
		}
	}

	private void showMainDialog(String title, final String url, String isJump, String info, String buttonTitle) {
		Log.i("tag", title + url + isJump + info + buttonTitle);
		BocopDialog2 dialog = null;
		dialog = new BocopDialog2(baseActivity, title, info);
		dialog.setPositiveButton(new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				showAdDialog(view);
				Bundle bundle = new Bundle();
				bundle.putString("url", url);
				bundle.putString("name", "");
				Intent intent = new Intent(baseActivity, WebViewActivity.class);
				intent.putExtras(bundle);
				baseActivity.startActivity(intent);

				dialog.cancel();
			}
		}, buttonTitle);
		dialog.setNegativeButton(new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				showAdDialog(view);
			}
		}, "下次再说");
		Log.i("tag", "show");
		dialog.show();
		SpUtil spUtil = new SpUtil(baseActivity);
		spUtil.useFirstForMainDialog();
	}

	private void showMainDialog2(String title, final String url, String isJump, String info, String buttonTitle) {
		Log.i("tag", "showMainDialog2");
		BocopDialog2 dialog = null;
		dialog = new BocopDialog2(baseActivity, title, info);
		dialog.setPositiveButton(new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				showAdDialog(view);
			}
		}, buttonTitle);
		Log.i("tag", "show");
		dialog.show();
	}

	private void requestBocopForDialog() {

		Log.i("tag", "requestBocopForDialog");
		Gson gson = new Gson();
		Map<String, String> map = new HashMap<String, String>();
		map.put("clientid", "386");
		final String strGson = gson.toJson(map);
		QztRequestWithJsonAll qztRequestWithJson = new QztRequestWithJsonAll(baseActivity);
		qztRequestWithJson.postOpbocNoDialog(strGson, BocSdkConfig.DialogUrl,
				new com.bocop.jxplatform.util.QztRequestWithJsonAll.CallBackBoc() {

					@Override
					public void onSuccess(String responStr) {
						Log.i("tagg1", responStr);
						if (responStr.contains("stat") && responStr.contains("00")) {
							try {
								Log.i("tag", "sucessss");
								MainDialogListBean mainDialogListBean;
								mainDialogListBean = JsonUtils.getObject(responStr, MainDialogListBean.class);
								Log.i("tag", "JsonUtils");
								if (mainDialogListBean != null) {
									if (mainDialogListBean.getBody() != null) {
										MainDialogBean qztAttentionBean = mainDialogListBean.getBody();
										String title = qztAttentionBean.getTitle();
										String url = qztAttentionBean.getUrl();
										String isJump = qztAttentionBean.getIsJump();
										String info = qztAttentionBean.getInfo();
										String buttonTitle = qztAttentionBean.getButtonTitle();
										if (isJump.equals("1")) {
											showMainDialog(title, url, isJump, info, buttonTitle);
										} else if (isJump.equals("0")) {
											showMainDialog2(title, url, isJump, info, buttonTitle);
										}

										// if(isJump.equals(""))
									} else {
										Log.i("tag", "no date");
									}
								} else {
								}

							} catch (Exception e) {
								e.printStackTrace();
							}

							return;
						} else {
						}
					}

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						Log.i("tag", "发送JSON报文" + strGson);
					}

					@Override
					public void onFinish() {
						// TODO Auto-generated method stub

					}

					@Override
					public void onFailure(String responStr) {
						// Toast.makeText(QztApplyActivity.this,
						// responStr, Toast.LENGTH_LONG).show();
						Log.i("tag", "qzt" + responStr);
						showAdDialog(view);
					}
				});
	}

	private void freshOauth() {
		Log.i("tag", "freshOauth");
		try {
			if (baseApplication.isNetStat()) {
				Update update = new Update(getActivity());
				update.requestVersionFromBocop(new DialogShowingListener() {

					@Override
					public void show(boolean isShowing) {

						if (!isShowing) {
							SpUtil spUtil = new SpUtil(baseActivity);
							if (spUtil.isFirstForMainDialog()) {
								requestBocopForDialog();
							}

						}

						// 显示鸿运通财神，此处注释，由积分商城抽金币替换
						// if (!isShowing) {
						// showAdDialog(view);
						// }
					}

					@Override
					public void showAdDia() {
						// TODO Auto-generated method stub
						showAdDialog(view);
					}
				});
			} else {
				CustomProgressDialog.showBocNetworkSetDialog(getActivity());
			}
		} catch (Exception ex) {
			Log.i("tag", "更新版本发生异常");
		}
	}

	private void init() {
		System.out.println("home.....init");
		mAdvList.clear();

		Advertisement adv0 = new Advertisement();
		adv0.setImageRes(R.drawable.hyt);

		// 养老宝
		Advertisement adv1 = new Advertisement();
		adv1.setImageRes(R.drawable.ylb);
		// adv1.setContent("http://open.boc.cn/mobile#/search2Detail/12599"); //
		// 网页url

		// 出国金融
		Advertisement adv2 = new Advertisement();
		adv2.setImageRes(R.drawable.cgjr);
		// adv2.setContent("http://open.boc.cn/mobile#/search2Detail/13621");
		// 易社区
		Advertisement adv3 = new Advertisement();
		adv3.setImageRes(R.drawable.ysq);
		// adv3.setContent("http://open.boc.cn/mobile#/search2Detail/11574");
		// 易商app
		Advertisement adv4 = new Advertisement();
		adv4.setImageRes(R.drawable.zr);

		mAdvList.add(adv0);
		mAdvList.add(adv1);
		mAdvList.add(adv2);
		mAdvList.add(adv3);
		mAdvList.add(adv4);
		notifyForAdPic();
	}

	/**
	 * 响应轮播图请求
	 * 
	 * @param retCode
	 * @param response
	 */
	public void notifyForAdPic() {
		if (mAdvList != null && mAdvList.size() > 1) {// 多张图片
			views.clear();
			rltAd.setVisibility(View.VISIBLE);
			ivSingleImage.setVisibility(View.GONE);
			for (Advertisement advertisement : mAdvList) {
				View view = LayoutInflater.from(baseActivity).inflate(R.layout.page_ad, null);
				ImageView iv = (ImageView) view.findViewById(R.id.ivAd);
				iv.setImageResource(advertisement.getImageRes());
				views.add(view);
			}
			LoopViewPagerAdapter adapter = new LoopViewPagerAdapter(views);
			vpAd.setAdapter(adapter);
			indicator.setViewPager(vpAd);
			// 启动轮播图
			if (adHandler.hasMessages(0)) {
				adHandler.removeMessages(0);
			}
			adHandler.sendEmptyMessageDelayed(0, 3000);
		} else if (mAdvList != null && mAdvList.size() == 1) {// 单张图片
			rltAd.setVisibility(View.GONE);
			ivSingleImage.setVisibility(View.VISIBLE);
			ivSingleImage.setImageResource(mAdvList.get(0).getImageRes());
		}
	}

	@OnTouch(R.id.vpAd)
	public boolean onTouch(View v, MotionEvent event) { // 根据触摸情况，判断轮播图触摸事件
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			isTouch = true;
			preX = event.getX();
			preY = event.getY();
			break;
		case MotionEvent.ACTION_UP:
			isTouch = false;
			nowX = event.getX();
			nowY = event.getY();
			if (nowX == preX && nowY == preY) {
				// 点击跳转事件
				// clickAdPic(vpAd.getCurrentItem());
			}
			break;
		case MotionEvent.ACTION_CANCEL:
			isTouch = false;
			break;
		}
		return false;
	}

	/**
	 * 轮播图点击事件
	 * 
	 * @param index
	 */
	public void clickAdPic(int index) {
		if (mAdvList != null && mAdvList.size() != 0) {
			// if (mAdvList.size() == index + 1) {
			// vpAd.setCurrentItem(0, false);
			// }
			Advertisement adv = mAdvList.get(index);
			String url = adv.getContent();
			Intent intent = new Intent(getActivity(), HomeAdvDetailActivity.class);
			intent.putExtra("url", url);
			startActivity(intent);
		}
	}

	@Override
	public void onLogin() {
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

	/**
	 * 获取用户信息，主要是获取手机号为洗车服务做准备
	 */
	private void postUserInfo() {
		RestTemplate restTemplate = new RestTemplate(getActivity());
		RequestParams params = new RequestParams();
		restTemplate.post_nobody("https://openapi.boc.cn/app/useridquery", params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				String string = new String(responseBody);
				System.out.println("查询用户信息------" + string);
				try {
					JSONObject jsonObject = new JSONObject(string);
					String idnoString = jsonObject.optString("idno");
					String mobileNo = jsonObject.optString("mobileno");
					String username = jsonObject.optString("cusname");
					ContentUtils.putSharePre(getActivity(), Constants.SHARED_PREFERENCE_NAME, Constants.ID_NO,
							idnoString);
					ContentUtils.putSharePre(getActivity(), Constants.SHARED_PREFERENCE_NAME, Constants.MOBILENO,
							mobileNo);
					ContentUtils.putSharePre(getActivity(), Constants.SHARED_PREFERENCE_NAME, Constants.USER_NAME,
							username);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

			}
		});
	}

	@Override
	public void onLogin(int position) {
		Log.i("taggg", String.valueOf(position));
		// requestBocopForCustid(getBaseActivity(), false);

		if (position == 201) {
			getFinances(getActivity());
		}

		if (position == 102) {
			Intent intent = new Intent(baseActivity, XmsMainActivity.class);
			startActivity(intent);
		}
		if (position == 103) {
			Intent intent = new Intent(baseActivity, BMJFActivity.class);
			startActivity(intent);
		}
		// 健康通
		if (position == 104) {
			Bundle bundle = new Bundle();
			bundle.putString("userId", LoginUtil.getUserId(getActivity()));
			bundle.putString("token", LoginUtil.getToken(getActivity()));
			Intent intent = new Intent(baseActivity, JKETActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
		}
		// 鸿运通
		if (position == 105) {
			Intent intent = new Intent(baseActivity, GoldManagerActivity.class);
			startActivity(intent);
		}

		if (position == 301) {
			Intent intent = new Intent(baseActivity, WEIHUIActivity.class);
			startActivity(intent);
		}

		// 售汇通
		if (position == 206) {
			Bundle bundle = new Bundle();
			bundle.putString("flag", "0");
			Intent intent = new Intent(baseActivity, JIEHUIActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
		}
		// 购汇通
		if (position == 207) {
			Bundle bundle = new Bundle();
			bundle.putString("flag", "1");
			Intent intent = new Intent(baseActivity, JIEHUIActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
		}
		// 公积通
		if (position == 209) {
			Intent intent = new Intent(baseActivity, GjtFirstActivity.class);
			startActivity(intent);
		}

		if (position == 205) {
			startFntWithoutLogin(5);
		}
		if (position == 212) {
			startFntWithoutLogin(1);
		}
		if (position == 213) {
			startFntWithoutLogin(2);
		}
		if (position == 214) {
			startFntWithoutLogin(3);
		}

		if (position == 501) {
			startGrfpd();
		}
		if (position == 503) {
			startLvfpd();
		}
		if (position == 504) {
			startFpscLogin();
		}

		if (position == 901) {
			Intent intent = new Intent(baseActivity, TrafficAssistantMainActivity.class);
			startActivity(intent);
		}
		if (position == 909) {
			Intent intent = new Intent(baseActivity, DZPActivity.class);
			baseActivity.startActivity(intent);
		}
		// 企贷通
		if (position == 801) {
			Bundle bundleHx = new Bundle();
			bundleHx.putString("url", Constants.UrlForMainQdt);
			bundleHx.putString("type", "qdt");
			bundleHx.putString("name", "企贷通");
			Intent intentHx = new Intent(baseActivity, WebForZytActivity.class);
			intentHx.putExtras(bundleHx);
			startActivity(intentHx);
		}
		// 保函通
		if (position == 803) {
			Bundle bundleHx = new Bundle();
			bundleHx.putString("url", Constants.UrlForMainDzt);
			bundleHx.putString("type", "dzt");
			bundleHx.putString("name", "单证通");
			Intent intentHx = new Intent(baseActivity, WebForZytActivity.class);
			intentHx.putExtras(bundleHx);
			startActivity(intentHx);
		}
		// 单证通
		if (position == 802) {
			Bundle bundleHx = new Bundle();
			bundleHx.putString("url", Constants.UrlForMainBht);
			bundleHx.putString("type", "bht");
			bundleHx.putString("name", "保函通");
			Intent intentHx = new Intent(baseActivity, WebForZytActivity.class);
			intentHx.putExtras(bundleHx);
			startActivity(intentHx);
		}
		// 消费金融
		if (position == 508) {
			XfjrMain.startXFJR(baseActivity);
		}
		Toast.makeText(getActivity(), "登录成功", Toast.LENGTH_LONG).show();

	}

	public void postIdForXms() {
		final Context contextXms = getBaseActivity();
		Gson gson = new Gson();
		Map<String, String> map = new HashMap<String, String>();
		String cardId = ContentUtils.getStringFromSharedPreference(contextXms, Constants.SHARED_PREFERENCE_NAME,
				Constants.CUSTOM_ID_NO);
		String userId = LoginUtil.getUserId(contextXms);
		Log.i("tag22", cardId);
		Log.i("tag22", userId);
		map.put("userId", userId);
		map.put("cardId", cardId);
		final String strGson = gson.toJson(map);
		QztRequestWithJsonAndHead qztRequestWithJsonAndHead = new QztRequestWithJsonAndHead(contextXms);
		qztRequestWithJsonAndHead.postOpbocNoDialog(strGson, BocSdkConfig.qztPostForXmsUrl,
				new com.bocop.jxplatform.util.QztRequestWithJsonAndHead.CallBackBoc() {
					@Override
					public void onSuccess(String responStr) {
						Log.i("tag22", responStr);
						try {
							ContentUtils.putSharePre(contextXms, Constants.SHARED_PREFERENCE_NAME,
									Constants.CUSTOM_PUT_ALREADY, "1");
							Log.i("tag22", ContentUtils.getStringFromSharedPreference(contextXms,
									Constants.SHARED_PREFERENCE_NAME, Constants.CUSTOM_PUT_ALREADY));
						} catch (Exception e) {
							e.printStackTrace();
						}

					}

					@Override
					public void onStart() {
					}

					@Override
					public void onFailure(String responStr) {
						Log.i("tag33", responStr);
					}

					@Override
					public void onFinish() {
					}
				});
	}

	private void getFinances(final Context context) {
		RestTemplateJxBank restTemplate = new RestTemplateJxBank(context);
		JsonRequestParams params = new JsonRequestParams();
		String action = LoginUtil.getToken(context);
		String userid = LoginUtil.getUserId(context);
		params.put("enctyp", "");
		params.put("password", "");
		params.put("grant_type", "implicit");
		params.put("user_id", userid);
		// params.put("client_secret", MainActivity.CONSUMER_SECRET);
		params.put("client_id", "357");
		params.put("acton", action);
		// https://openapi.boc.cn/bocop/oauth/token
		restTemplate.postGetType("https://openapi.boc.cn/oauth/token", params, new JsonHttpResponseHandler("UTF-8") {
			@Override
			public void onStart() {
				super.onStart();
				progressDialog = new ProgressDialog(context);
				progressDialog.setMessage("正在努力加载中...");
				progressDialog.setCanceledOnTouchOutside(false);
				progressDialog.show();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				System.out.println("nihao======" + response.toString());
				progressDialog.dismiss();
				Intent intent = new Intent(context, MoneySelectWebView.class);
				intent.putExtra("url",
						"https:/openapi.boc.cn/ezdb/mobileHtml/html/userCenter/index.html?channel=android");
				try {
					intent.putExtra("access_token", response.getString("access_token"));
					intent.putExtra("refresh_token", response.getString("refresh_token"));
					intent.putExtra("user_id", response.getString("user_id"));
					context.startActivity(intent);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
			}
		});
	}

	/**
	 * 小秘书
	 * 
	 * @param view
	 */
	public void smallSecretary() {
		if (baseApplication.isNetStat()) {
			// if (LoginUtil.isLog(baseActivity)) {
			Intent intent = new Intent(baseActivity, XmsMainActivity.class);
			startActivity(intent);
			// } else {
			// LoginUtilAnother.authorizeAnother(getActivity(),
			// HomeFragment.this, 102);
			// }
		} else {
			CustomProgressDialog.showBocNetworkSetDialog(getActivity());
		}

	}

	/**
	 * 金币管理
	 */
	public void gm() {
		if (baseApplication.isNetStat()) {
			if (LoginUtil.isLog(baseActivity)) {
				Intent intent = new Intent(baseActivity, GoldManagerActivity.class);
				startActivity(intent);
			} else {
				LoginUtilAnother.authorizeAnother(getActivity(), HomeFragment.this, 105);
			}
		} else {
			CustomProgressDialog.showBocNetworkSetDialog(getActivity());
		}

	}

	/**
	 * 显示财神，原生页面，鸿运通抽取财神
	 */
	// private void showAdDialog(final View rootView) {
	// if (checkTime() && canShowAd()) {
	// View view = baseActivity.getLayoutInflater().inflate(
	// R.layout.gm_layout_dialog_ad, null);
	// final PopupWindow adWindow = new PopupWindow(view,
	// FormsUtil.SCREEN_WIDTH, FormsUtil.SCREEN_HEIGHT);
	//
	// adWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0);
	// ImageView ivClose = (ImageView) view.findViewById(R.id.ivClose);
	// ImageView ivPacket = (ImageView) view.findViewById(R.id.ivPacket);
	// ivClose.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// adWindow.dismiss();
	// }
	// });
	//
	// ivPacket.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// adWindow.dismiss();
	// requestForPacket(rootView);
	// }
	// });
	//
	// SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
	// Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
	// String str = formatter.format(curDate);
	// SharedPreferences sp = getActivity().getSharedPreferences(
	// CacheBean.AD_HASSHOW, Context.MODE_PRIVATE);
	// Editor mEditor = sp.edit();
	// mEditor.putBoolean(str, true);
	// mEditor.commit();
	// }
	// }

	/**
	 * 显示财神，H5页面，积分商城的财神
	 */
	private void showAdDialog(final View rootView) {
		if (checkTime() && canShowAd()) {
			// Log.i("tag", "showAdDialog jk");
			// Bundle bundle = new Bundle();
			// bundle.putString("url", BocSdkConfig.jfscCsurl);
			//// bundle.putString("name", "存贷款利率");
			// Intent intent = new Intent(baseActivity,
			// SoftwareWebActivity.class);
			// intent.putExtras(bundle);
			// startActivity(intent);
			// Log.i("tag", "showAdDialog mk");
			// SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
			// Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
			// String str = formatter.format(curDate);
			// SharedPreferences sp = getActivity().getSharedPreferences(
			// CacheBean.AD_HASSHOW, Context.MODE_PRIVATE);
			// Editor mEditor = sp.edit();
			// mEditor.putBoolean(str, true);
			// mEditor.commit();

			Intent intent = new Intent(baseActivity, WebDialogActivity.class);
			startActivity(intent);
		}
	}

	/**
	 * 显示打开红包页面
	 * 
	 * @param rootView
	 * @param count
	 */
	private void showPacketDialog(final View rootView, final String count) {
		View view = baseActivity.getLayoutInflater().inflate(R.layout.gm_layout_dialog_packet, null);
		final PopupWindow packetWindow = new PopupWindow(view, FormsUtil.SCREEN_WIDTH, FormsUtil.SCREEN_HEIGHT);

		packetWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0);
		ImageView ivClose = (ImageView) view.findViewById(R.id.ivClose);
		ImageView ivClickToDraw = (ImageView) view.findViewById(R.id.ivClickToDraw);
		TextView tvAmt = (TextView) view.findViewById(R.id.tvAmt);

		tvAmt.setText(count);
		ivClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				packetWindow.dismiss();
			}
		});

		ivClickToDraw.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				packetWindow.dismiss();
				// showADDialog2(rootView, count);
				cacheBean.setCount(count);
				gm();
			}
		});
	}

	/**
	 * 请求红包数量
	 * 
	 * @param rootView
	 */
	private void requestForPacket(final View rootView) {
		RequestBody formBody = new FormEncodingBuilder().build();
		CspUtil cspUtil = new CspUtil(baseActivity);
		cspUtil.postCspNoLogin(BocSdkConfig.RED_PACKET
		// "http://172.23.16.34:8080/dfb_app/common/user/getRedbagInfo.do"
				, formBody, true, new CallBack() {
					@Override
					public void onSuccess(String responStr) {
						JSONObject object;
						String isSuccess = "";
						String count = "";
						try {
							object = new JSONObject(responStr);
							isSuccess = object.getString("result");
							object = new JSONObject(object.getString("message"));
							count = object.getString("redbagcount");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if ("success".equals(isSuccess)) {
							showPacketDialog(rootView, count);
						}
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
	 * 判断是否过0点
	 * 
	 * @return
	 */
	private boolean checkTime() {
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);

		int minuteOfDay = hour * 60 + minute;
		if (minuteOfDay >= 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断当天是否显示过广告
	 * 
	 * @return
	 */
	private boolean canShowAd() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		SharedPreferences sp = getActivity().getSharedPreferences(CacheBean.AD_HASSHOW, Context.MODE_PRIVATE);
		boolean hasShowed = sp.getBoolean(str, false);
		if (!hasShowed) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 理财通
	 */
	public void liCai() {
		if (baseApplication.isNetStat()) {
			if (LoginUtil.isLog(baseActivity)) {
				getFinances(getActivity());
			} else {
				LoginUtilAnother.authorizeAnother(getActivity(), HomeFragment.this, 201);
			}
		} else {
			CustomProgressDialog.showBocNetworkSetDialog(getActivity());
		}
	}

	/**
	 * 缴费通
	 */
	public void pay() {
		BaseApplication baseApplication = (BaseApplication) baseActivity.getApplication();
		if (baseApplication.isNetStat()) {
			if (LoginUtil.isLog(baseActivity)) {
				Intent intent = new Intent(baseActivity, BMJFActivity.class);
				startActivity(intent);
			} else {
				LoginUtilAnother.authorizeAnother(getActivity(), HomeFragment.this, 103);
			}
		} else {
			CustomProgressDialog.showBocNetworkSetDialog(baseActivity);
		}
	}

	/**
	 * 汇兑通
	 */
	public void huiduitong() {
		BaseApplication baseApplication = (BaseApplication) baseActivity.getApplication();
		if (baseApplication.isNetStat()) {
			if (LoginUtil.isLog(baseActivity)) {
				Intent intent = new Intent(baseActivity, HDTActivity.class);
				startActivity(intent);
			} else {
				LoginUtilAnother.authorizeAnother(getActivity(), HomeFragment.this, 103);
			}
		} else {
			CustomProgressDialog.showBocNetworkSetDialog(baseActivity);
		}
	}

	/**
	 * 个贷通
	 */
	public void boced() {
		BaseApplication baseApplication = (BaseApplication) baseActivity.getApplication();
		if (baseApplication.isNetStat()) {
			if (LoginUtil.isLog(baseActivity)) {
				Intent intent = new Intent(baseActivity, ZYEDActivity.class);
				startActivity(intent);
			} else {
				LoginUtilAnother.authorizeAnother(getActivity(), HomeFragment.this, 6);
			}
		} else {
			CustomProgressDialog.showBocNetworkSetDialog(baseActivity);
		}
	}

	/**
	 * 健康通
	 */
	public void health() {
		if (baseApplication.isNetStat()) {
			// if (LoginUtil.isLog(baseActivity)) {
			Bundle bundle = new Bundle();
			bundle.putString("userId", LoginUtil.getUserId(getActivity()));
			bundle.putString("token", LoginUtil.getToken(getActivity()));
			Intent intent = new Intent(baseActivity, JKETActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
			// } else {
			// LoginUtilAnother.authorizeAnother(getActivity(),
			// HomeFragment.this, 104);
			// }
		} else {
			CustomProgressDialog.showBocNetworkSetDialog(getActivity());
		}
	}

	/**
	 * 积分商城
	 */
	public void startJfsc() {
		if (baseApplication.isNetStat()) {
			// Bundle bundle = new Bundle();
			Intent intent = new Intent(baseActivity, JFSCActivity.class);
			// if (canShowAd()) { // 每天第一次启动积分商城，则跳转财神页面
			// Log.i("tag", "canShowAd");
			// bundle.putString("flag", "first");
			// intent.putExtras(bundle);
			// }
			startActivity(intent);
		} else {
			CustomProgressDialog.showBocNetworkSetDialog(getActivity());
		}
	}

	/*
	 * 流量红包
	 */
	public void redpaper() {
		Toast.makeText(baseActivity, "敬请期待 ", Toast.LENGTH_SHORT).show();
	}

	/**
	 * 违章处理
	 */
	public void llIllegals() {
		BaseApplication application = (BaseApplication) baseActivity.getApplication();

		if (application.isNetStat()) {
			if (LoginUtil.isLog(baseActivity)) {
				Intent intent = new Intent(baseActivity, TrafficAssistantMainActivity.class);
				startActivity(intent);
			} else {
				LoginUtilAnother.authorizeAnother(getActivity(), HomeFragment.this, 5);
			}
		} else {
			CustomProgressDialog.showBocNetworkSetDialog(baseActivity);
		}
	}

	/**
	 * 交通助手
	 */
	public void transHelper() {
		if (baseApplication.isNetStat()) {
			if (LoginUtil.isLog(baseActivity)) {
				Intent intent = new Intent(baseActivity, TrafficAssistantMainActivity.class);
				startActivity(intent);
			} else {
				LoginUtilAnother.authorizeAnother(getActivity(), HomeFragment.this, 901);
			}
		} else {
			CustomProgressDialog.showBocNetworkSetDialog(getActivity());
		}
	}

	/**
	 * 外购通
	 */
	public void htzq() {
		if (baseApplication.isNetStat()) {
			CustomProgressDialog.showBocFengxianDialog(baseActivity, "免责声明",
					"\t\t中国银行信用卡仅作为客户用于境外网上商户的支付结算工具，对于商品质量或商户服务无法负责；本网站引用的网址、商户及商品信息，意在为持卡人境外购物行为提供便利，并解决境外购物所遇常见问题，对境外商户网站合法性、安全性、准确性无法负责；注意网购有风险，如您在境外购物过程中发生商品质量、转运服务、货物丢失、延迟收货等购物纠纷，请您直接与购物网站或转运服务商沟通解决；境外购物需遵守国家相关法律、法规。 ",
					"com.bocop.jxplatform.activity.HTZQActivity");

		} else {
			CustomProgressDialog.showBocNetworkSetDialog(getActivity());
		}
	}

	/**
	 * 淘宝网
	 */
	public void tbw() {
		if (baseApplication.isNetStat()) {
			CustomProgressDialog.showBocFengxianDialog(baseActivity, "免责声明",
					"\t\t易惠通引用的第三方服务网址、商户及商品信息，意在为客户提供方便快捷的服务链接，所有服务均由第三方服务商提供，相关服务和责任将由第三方承担，如在使用服务的过程中有任何问题和纠纷，请您直接与第三方服务商沟通解决。 ",
					"tbw");

		} else {
			CustomProgressDialog.showBocNetworkSetDialog(getActivity());
		}
	}

	/**
	 * 京东
	 */
	public void jd() {
		if (baseApplication.isNetStat()) {
			CustomProgressDialog.showBocFengxianDialog(baseActivity, "免责声明",
					"\t\t易惠通引用的第三方服务网址、商户及商品信息，意在为客户提供方便快捷的服务链接，所有服务均由第三方服务商提供，相关服务和责任将由第三方承担，如在使用服务的过程中有任何问题和纠纷，请您直接与第三方服务商沟通解决。 ",
					"jd");

		} else {
			CustomProgressDialog.showBocNetworkSetDialog(getActivity());
		}
	}

	/**
	 * 美团外卖
	 */
	public void mtwm() {
		if (baseApplication.isNetStat()) {
			// CustomProgressDialog
			// .showBocFengxianDialog(
			// baseActivity,
			// "免责声明",
			// "\t\t易惠通引用的第三方服务网址、商户及商品信息，意在为客户提供方便快捷的服务链接，所有服务均由第三方服务商提供，相关服务和责任将由第三方承担，如在使用服务的过程中有任何问题和纠纷，请您直接与第三方服务商沟通解决。
			// ","mtwm");

			BocopDialog dialog = null;
			dialog = new BocopDialog(getActivity(), "免责声明",
					"\t\t易惠通引用的第三方服务网址、商户及商品信息，意在为客户提供方便快捷的服务链接，所有服务均由第三方服务商提供，相关服务和责任将由第三方承担，如在使用服务的过程中有任何问题和纠纷，请您直接与第三方服务商沟通解决。 ");
			dialog.setPositiveButton(new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = null;
					Bundle bundle = new Bundle();
					bundle.putString("url", Constants.UrlForMainMtwm);
					bundle.putString("name", "美团外卖");
					intent = new Intent(getActivity(), WebViewActivity.class);
					intent.putExtras(bundle);
					startActivity(intent);
					dialog.cancel();
				}
			}, "同意");
			dialog.setNegativeButton(new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			}, "不同意");
			dialog.show();
		} else {
			CustomProgressDialog.showBocNetworkSetDialog(getActivity());
		}
	}

	/**
	 * 百度外卖
	 */
	public void bdwm() {
		if (baseApplication.isNetStat()) {
			CustomProgressDialog.showBocFengxianDialog(baseActivity, "免责声明",
					"\t\t易惠通引用的第三方服务网址、商户及商品信息，意在为客户提供方便快捷的服务链接，所有服务均由第三方服务商提供，相关服务和责任将由第三方承担，如在使用服务的过程中有任何问题和纠纷，请您直接与第三方服务商沟通解决。 ",
					"bdwm");

		} else {
			CustomProgressDialog.showBocNetworkSetDialog(getActivity());
		}
	}

	/**
	 * 银保通
	 */
	public void startYbt() {
		if (baseApplication.isNetStat()) {
			CustomProgressDialog.showBocFengxianDialog(baseActivity, "免责声明",
					"\t\t中国银行江西省分行“易惠通”项目通过链接合作保险公司网址，为客户提供高效、便捷的多元化服务需求。客户遵照国家相关法律法规在网站购买保险产品等服务，要注意不同保险产品具备不同的保险特性。保险公司对网站的合法性、安全性、准确性负责，并解决客户购买保险需求所遇的常见问题。如您在购买保险公司产品的过程中发生任务售前、售中、售后等服务纠纷，请您直接与相关保险公司沟通解决。 ",
					"YBTActivity");

		} else {
			CustomProgressDialog.showBocNetworkSetDialog(getActivity());
		}
		//
		// Intent intent = new Intent(getBaseActivity(), YbtActivity.class);
		// startActivity(intent);
	}

	/**
	 * 外购通
	 */
	public void bocexy() {
		Intent intent = new Intent(baseActivity, EXYActivity.class);
		startActivity(intent);
	}

	/**
	 * 跳转到车友会的方法
	 */
	public void startCyh() {
		Intent intent = new Intent(baseActivity, RiderFristActivity.class);
		startActivity(intent);
	}

	/**
	 * 跳转扶微通、扶农通的方法
	 */
	public void startFnt(int FLAG) {
		if (baseApplication.isNetStat()) {
			// if (LoginUtil.isLog(baseActivity)) {
			goToZZYD(FLAG);

			// } else {
			// // 扶贫
			// if (FLAG == 1) {
			// LoginUtilAnother.authorizeAnother(getActivity(),
			// HomeFragment.this, 212);
			// }
			// if (FLAG == 2) {
			// LoginUtilAnother.authorizeAnother(getActivity(),
			// HomeFragment.this, 213);
			// }
			// }

		} else {
			CustomProgressDialog.showBocNetworkSetDialog(getActivity());
		}
	}

	private void startFntWithoutLogin(int FLAG) {
		Bundle bundle = new Bundle();
		bundle.putInt("PRO_FLAG", FLAG);
		// Intent intent = new Intent(baseActivity, LoanMainActivity.class);
		Intent intent = new Intent(baseActivity, TrainsActivity.class);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	/**
	 * 跳转到中银E贷界面
	 */
	private void goToZZYD(final int FLAG) {
		startFntWithoutLogin(FLAG);

	}

	/**
	 * 跳转到签证通的方法
	 */
	public void startQzt() {
		if (baseApplication.isNetStat()) {
			Intent intent = new Intent(baseActivity, QztMainActivity.class);
			// Intent intent = new Intent(baseActivity, QztFristActivity.class);
			startActivity(intent);
		} else {
			CustomProgressDialog.showBocNetworkSetDialog(getActivity());
		}
	}

	/**
	 * 跳转到优品通的方法
	 */
	public void youpingtong() {
		Intent intent = new Intent(baseActivity, YPTActivity.class);
		startActivity(intent);
	}

	/**
	 * 跳转到卡惠通的方法
	 */
	public void startKht() {
		if (LoginUtil.isLog(baseActivity)) {
			Intent intent = new Intent(baseActivity, WEIHUIActivity.class);
			startActivity(intent);
		} else {
			LoginUtilAnother.authorizeAnother(getActivity(), HomeFragment.this, 301);
		}
	}

	/**
	 * 跳转企贷通的方法
	 */
	public void startQdt() {
		Bundle bundleHx = new Bundle();
		bundleHx.putString("url", Constants.UrlForMainQdt);
		bundleHx.putString("type", "qdt");
		bundleHx.putString("name", "企贷通");
		Intent intentHx = new Intent(baseActivity, WebForZytActivity.class);
		intentHx.putExtras(bundleHx);
		startActivity(intentHx);
	}

	/**
	 * 跳转保函通的方法
	 */
	public void startBht() {
		Bundle bundleHx = new Bundle();
		bundleHx.putString("url", Constants.UrlForMainBht);
		bundleHx.putString("type", "bht");
		bundleHx.putString("name", "保函通");
		Intent intentHx = new Intent(baseActivity, WebForZytActivity.class);
		intentHx.putExtras(bundleHx);
		startActivity(intentHx);
	}

	/**
	 * 跳转单证通的方法
	 */
	public void startDzt() {
		Bundle bundleHx = new Bundle();
		bundleHx.putString("url", Constants.UrlForMainDzt);
		bundleHx.putString("type", "xzt");
		bundleHx.putString("name", "单证通");
		Intent intentHx = new Intent(baseActivity, WebForZytActivity.class);
		intentHx.putExtras(bundleHx);
		startActivity(intentHx);
	}

	/**
	 * 跳转到旅游通的方法
	 */
	public void startLyt() {
		if (baseApplication.isNetStat()) {
			Intent intent = new Intent(baseActivity, LYTActivity.class);
			startActivity(intent);
		} else {
			CustomProgressDialog.showBocNetworkSetDialog(getActivity());
		}
	}

	/**
	 * 购汇通
	 */
	public void startGouhui() {
		if (baseApplication.isNetStat()) {
			if (LoginUtil.isLog(baseActivity)) {
				Bundle bundle = new Bundle();
				bundle.putString("flag", "1");
				Intent intent = new Intent(baseActivity, JIEHUIActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
			} else {
				LoginUtilAnother.authorizeAnother(getActivity(), HomeFragment.this, 207);
			}
		} else {
			CustomProgressDialog.showBocNetworkSetDialog(getActivity());
		}
	}

	public void xiaodaitong() {
		if (baseApplication.isNetStat()) {
			// if (LoginUtil.isLog(baseActivity)) {
			startFntWithoutLogin(5);
			// } else {
			// LoginUtilAnother.authorizeAnother(getActivity(),
			// HomeFragment.this, 205);
			// }
		} else {
			CustomProgressDialog.showBocNetworkSetDialog(getActivity());
		}
	}

	/**
	 * 结汇
	 */
	public void startJiehui() {
		if (baseApplication.isNetStat()) {
			if (LoginUtil.isLog(baseActivity)) {
				Bundle bundle = new Bundle();
				bundle.putString("flag", "0");
				Intent intent = new Intent(baseActivity, JIEHUIActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
			} else {
				LoginUtilAnother.authorizeAnother(getActivity(), HomeFragment.this, 206);
			}
		} else {
			CustomProgressDialog.showBocNetworkSetDialog(getActivity());
		}
	}

	/**
	 * 跳转到车友会的方法
	 */
	public void startCft() {
		// CustomProgressDialog.showBocFengxianDialog(baseActivity,"免责声明","中国银行信用卡仅作为客户于境外网上商户的支付结算工具，对于商品质量或商户服务无法负责；
		// 本网站引用的网址、商户及商品信息，意在为持卡人海淘行为提供便利，并解决境外海淘所遇常见问题，对境外商户网站合法性、安全性、准确性无法负责；注意网购有风险，如您在海淘过程中如发生商品质量、转运服务、货物丢失、延迟收货等购物纠纷，请您直接与购物网站或转运服务商沟通解决；境外购物需遵守国家相关法律、法规。
		// ");
		if (baseApplication.isNetStat()) {
			Bundle bundle = new Bundle();
			bundle.putString("url", BocSdkConfig.CFT);
			bundle.putString("name", "财富通");
			Intent intent = new Intent(baseActivity, CftActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
			//
			// Intent intent = new Intent(baseActivity, CFTActivity.class);
			// startActivity(intent);
		} else {
			CustomProgressDialog.showBocNetworkSetDialog(getActivity());
		}
	}

	public void startCard() {
		// CustomProgressDialog.showBocFengxianDialog(baseActivity,"免责声明","中国银行信用卡仅作为客户于境外网上商户的支付结算工具，对于商品质量或商户服务无法负责；
		// 本网站引用的网址、商户及商品信息，意在为持卡人海淘行为提供便利，并解决境外海淘所遇常见问题，对境外商户网站合法性、安全性、准确性无法负责；注意网购有风险，如您在海淘过程中如发生商品质量、转运服务、货物丢失、延迟收货等购物纠纷，请您直接与购物网站或转运服务商沟通解决；境外购物需遵守国家相关法律、法规。
		// ");

		Bundle bundle = new Bundle();
		bundle.putString("url", BocSdkConfig.CARD);
		bundle.putString("name", "办卡通");
		Intent intent = new Intent(baseActivity, WebActivity.class);
		intent.putExtras(bundle);
		startActivity(intent);

		// Intent intent = new Intent(baseActivity, CardActivity.class);
		// startActivity(intent);
	}

	public void startZdt() {
		if (baseApplication.isNetStat()) {
			startFntWithoutLogin(6);
			// Intent intent = new Intent(baseActivity,
			// FinanceMainActivity.class);
			// startActivity(intent);
		} else {
			CustomProgressDialog.showBocNetworkSetDialog(getActivity());
		}
	}

	// 证券通
	public void startZqt() {

		if (baseApplication.isNetStat()) {
			CustomProgressDialog.showBocFengxianDialog(baseActivity, "免责声明",
					"\t\t证券通是我行为了方便您进行证券投资而为您与券商之间搭建的开户渠道，不代表我行主动引导您进行证券交易，并对您的交易盈亏负责。请您充分了解股市投资风险，谨慎入市。本服务项下所有链接和二维码均由证券公司提供，如在下载、安装app过程中产生问题，请详询证券公司。 ",
					"ZqtFirstActivity");

		} else {
			CustomProgressDialog.showBocNetworkSetDialog(getActivity());
		}

		// if (baseApplication.isNetStat()) {
		// Intent intent = new Intent(baseActivity, ZqtFirstActivity.class);
		// startActivity(intent);
		// } else {
		// CustomProgressDialog.showBocNetworkSetDialog(getActivity());
		// }
	}

	/*
	 * 开户通
	 */
	public void startKhtcard() {
		if (baseApplication.isNetStat()) {
//			Intent intent = new Intent(baseActivity, KhtFirstActivity.class);
//			startActivity(intent);
			Bundle bundleHx = new Bundle();
			bundleHx.putString("url",BocSdkConfig.KHT);
			bundleHx.putString("name", "开户通");
			Intent intentHx = new Intent(baseActivity, KhtActivity.class);
			intentHx.putExtras(bundleHx);
			startActivity(intentHx);
		} else {
			CustomProgressDialog.showBocNetworkSetDialog(getActivity());
		}
	}

	/*
	 * 易贷通
	 */

	public void startYdt() {
		if (baseApplication.isNetStat()) {
			Bundle bundle = new Bundle();
			bundle.putString("url", Constants.UrlForMainYdt);
			bundle.putString("name", "中小通");
			Intent intent = new Intent(baseActivity, WebActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
		} else {
			CustomProgressDialog.showBocNetworkSetDialog(getActivity());
		}
	}

	/*
	 * 公基通
	 */

	public void startGjt() {
		BaseApplication baseApplication = (BaseApplication) baseActivity.getApplication();
		if (baseApplication.isNetStat()) {
			// if (LoginUtil.isLog(baseActivity)) {
			startFntWithoutLogin(3);
			// } else {
			// LoginUtilAnother.authorizeAnother(getActivity(),
			// HomeFragment.this, 209);
			// }
		} else {
			CustomProgressDialog.showBocNetworkSetDialog(baseActivity);
		}

		// Intent intent = new Intent(baseActivity,GjtFirstActivity.class);
		// startActivity(intent);

		// Toast.makeText(baseActivity, "敬请期待 ", Toast.LENGTH_SHORT).show();
		// if (baseApplication.isNetStat()) {
		// Bundle bundle = new Bundle();
		// bundle.putString("url", Constants.UrlForMainKhtcard);
		// bundle.putString("name", "公积通");
		// Intent intent = new Intent(baseActivity,
		// XmsWebActivity.class);
		// intent.putExtras(bundle);
		// startActivity(intent);
		// }else {
		// CustomProgressDialog.showBocNetworkSetDialog(getActivity());
		// }
	}

	/*
	 * 校园通
	 */
	public void startXyt() {
		if (baseApplication.isNetStat()) {
			Bundle bundle = new Bundle();
			bundle.putString("url", Constants.UrlForMainXyt);
			bundle.putString("name", "校园通");
			Intent intent = new Intent(baseActivity, WebActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
		} else {
			CustomProgressDialog.showBocNetworkSetDialog(getActivity());
		}
	}

	/*
	 * 个人扶贫贷
	 */

	public void startGrfpd() {
		// if (LoginUtil.isLog(baseActivity)) {
		startFntWithoutLogin(8);
		// } else {
		// LoginUtilAnother.authorizeAnother(getActivity(), HomeFragment.this,
		// 501);
		// }
	}

	/*
	 * 企业扶贫贷
	 */
	public void startQyfpd() {
		Bundle bundle = new Bundle();
		bundle.putString("url", Constants.fptUrlForqyfp);
		bundle.putString("name", "企业扶贫贷");
		Intent intentDotbook = new Intent(getActivity(), WebActivity.class);
		intentDotbook.putExtras(bundle);
		startActivity(intentDotbook);
	}

	/*
	 * 旅游扶贫贷
	 */
	public void startLvfpd() {
		// if (LoginUtil.isLog(baseActivity)) {
		startFntWithoutLogin(9);
		// } else {
		// LoginUtilAnother.authorizeAnother(getActivity(), HomeFragment.this,
		// 503);
		// }
	}

	/*
	 * 扶贫商城
	 */
	public void startFpsc() {
		if (baseApplication.isNetStat()) {
			// if (LoginUtil.isLog(baseActivity)) {
			startFpscLogin();
			// } else {
			// LoginUtilAnother.authorizeAnother(getActivity(),
			// HomeFragment.this,
			// 504);
			// }

		} else {
			CustomProgressDialog.showBocNetworkSetDialog(getActivity());
		}
	}

	private void startFpscLogin() {
		Bundle bundle = new Bundle();
		bundle.putString("url", BocSdkConfig.fpsc);
		bundle.putString("name", "扶贫商城");
		Intent intent = new Intent(baseActivity, WebViewForGjsActivity.class);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	// 中银易商
	public void startZyys() {
		if (baseApplication.isNetStat()) {
			doStartApplicationWithPackageName(baseActivity, "com.boc.bocop.container");
		} else {
			CustomProgressDialog.showBocNetworkSetDialog(getActivity());
		}
	}

	/**
	 * 启动第三方 app
	 * 
	 * @param mcontext
	 * @param packagename
	 */
	public static void doStartApplicationWithPackageName(Context mcontext, String packagename) {

		// 通过包名获取此 APP 详细信息，包括 Activities、 services 、versioncode 、 name等等
		PackageInfo packageinfo = null;
		try {
			packageinfo = mcontext.getPackageManager().getPackageInfo(packagename, 0);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		if (packageinfo == null) {
			// Toast.makeText(mcontext, "亲，", Toast.LENGTH_LONG).show();
			Bundle bundle = new Bundle();
			bundle.putString("url", Constants.UrlForZyys);
			bundle.putString("name", "中银易商");
			Intent intent = new Intent(mcontext, WebActivity.class);
			intent.putExtras(bundle);
			mcontext.startActivity(intent);
		}

		// 创建一个类别为 CATEGORY_LAUNCHER 的该包名的 Intent
		Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
		resolveIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		resolveIntent.setPackage(packageinfo.packageName);

		// 通过 getPackageManager()的 queryIntentActivities 方法遍历
		List<ResolveInfo> resolveinfoList = mcontext.getPackageManager().queryIntentActivities(resolveIntent, 0);

		ResolveInfo resolveinfo = resolveinfoList.iterator().next();
		if (resolveinfo != null) {
			// packagename = 参数 packname
			String packageName = resolveinfo.activityInfo.packageName;
			// 这个就是我们要找的该 APP 的LAUNCHER 的 Activity[组织形式：
			// packagename.mainActivityname]
			String className = resolveinfo.activityInfo.name;
			// LAUNCHER Intent
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);

			// 设置 ComponentName参数 1:packagename 参数2:MainActivity 路径
			ComponentName cn = new ComponentName(packageName, className);

			intent.setComponent(cn);
			mcontext.startActivity(intent);
		} else {
			Intent intent = new Intent(mcontext, EXYActivity.class);
			mcontext.startActivity(intent);
		}
	}

	/**
	 * 进入大转盘
	 */
	public static void gotoDZP(Context context) {
		SharedPreferences sp = context.getSharedPreferences(LoginUtilAnother.SP_NAME, Context.MODE_PRIVATE);
		String dzpUserId = sp.getString(CacheBean.DZP_USER_ID, "");
		if (!TextUtils.isEmpty(dzpUserId)) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
			Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
			String str = formatter.format(curDate);
			int currentDate = Integer.parseInt(str);
			if (currentDate >= 20170124 && currentDate <= 20170211) {// 为有效时间
				SharedPreferences spf = context.getSharedPreferences(CacheBean.DZP_HASSHOW, Context.MODE_PRIVATE);
				// 是否进入过大转盘
				boolean flag = spf.getBoolean(str, false);
				if (!flag) {
					Editor mEditor = spf.edit();
					mEditor.putBoolean(str, true);
					mEditor.commit();
					Intent intent = new Intent(context, DZPActivity.class);
					context.startActivity(intent);
				}
			}
		}
	}

	public void startHyzzz() {
		if (baseApplication.isNetStat()) {
			// SharedPreferences sp =
			// getActivity().getSharedPreferences(LoginUtilAnother.SP_NAME,
			// Context.MODE_PRIVATE);
			// String dzpUserId = sp.getString(CacheBean.DZP_USER_ID, "");
			// !TextUtils.isEmpty(dzpUserId)
			if (LoginUtil.isLog(baseActivity)) {
				Intent intent = new Intent(baseActivity, DZPActivity.class);
				baseActivity.startActivity(intent);
			} else {
				LoginUtilAnother.authorizeAnother(getActivity(), HomeFragment.this, 909);
			}
		} else {
			CustomProgressDialog.showBocNetworkSetDialog(getActivity());
		}
	}

	public void startZhbbm() {
		if (baseApplication.isNetStat()) {
			Bundle bundle = new Bundle();
			bundle.putString("url", BocSdkConfig.urlForZhbbm);
			bundle.putString("name", "");
			Intent intent = new Intent(baseActivity, WebViewActivity.class);
			intent.putExtras(bundle);
			baseActivity.startActivity(intent);
		} else {
			CustomProgressDialog.showBocNetworkSetDialog(getActivity());
		}
	}
	
	/**
	 * 跳到消费金融页面 
	 * 20170907新增by wujunliu
	 * 20171023修改by wujunliu
	 */
	public void startXFJR() {
		if (baseApplication.isNetStat()) {
			if (XfjrMain.isNet) {
				if (LoginUtil.isLog(baseActivity)) {
					XfjrMain.startXFJR(baseActivity);
				} else {
					LoginUtilAnother.authorizeAnother(getActivity(), HomeFragment.this, 508);
				}
			} else {
				XfjrMain.startXFJR(baseActivity);
			}
		} else {
			CustomProgressDialog.showBocNetworkSetDialog(getActivity());
		}
	}
}
