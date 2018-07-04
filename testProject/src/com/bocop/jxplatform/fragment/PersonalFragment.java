package com.bocop.jxplatform.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.boc.jx.base.BaseApplication;
import com.boc.jx.base.BaseFragment;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.boc.jx.constants.Constants;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.activity.FindPasswordActivity;
import com.bocop.jxplatform.activity.MarketCodeInfoActivity;
import com.bocop.jxplatform.activity.WebViewActivity;
import com.bocop.jxplatform.activity.marketCodeAddActivity;
import com.bocop.jxplatform.activity.way.pattern.GuideGesturePasswordActivity;
import com.bocop.jxplatform.activity.way.pattern.UnlockGesturePasswordActivity;
import com.bocop.jxplatform.adapter.PerFunctionAdapter;
import com.bocop.jxplatform.bean.PerFunctionBean;
import com.bocop.jxplatform.bean.QztAttentionBean;
import com.bocop.jxplatform.bean.QztAttentionListBean;
import com.bocop.jxplatform.bean.QztHead;
import com.bocop.jxplatform.bean.QztHeadSuper;
import com.bocop.jxplatform.config.BocSdkConfig;
import com.bocop.jxplatform.config.TransactionValue;
import com.bocop.jxplatform.trafficassistant.AbountActivity;
import com.bocop.jxplatform.trafficassistant.DynamicPswdActivity;
import com.bocop.jxplatform.trafficassistant.BocOpWebActivity;
import com.bocop.jxplatform.trafficassistant.DownloadService;
import com.bocop.jxplatform.trafficassistant.Help;
import com.bocop.jxplatform.util.BocOpUtilVersion;
import com.bocop.jxplatform.util.BocOpUtilVersion.CallBackBoc;
import com.bocop.jxplatform.util.CustomInfo.IIsCustom;
import com.bocop.jxplatform.util.CspUtil;
import com.bocop.jxplatform.util.CustomInfo;
import com.bocop.jxplatform.util.CustomProgressDialog;
import com.bocop.jxplatform.util.JsonUtils;
import com.bocop.jxplatform.util.LoginUtil;
import com.bocop.jxplatform.util.QztRequestWithJsonAll;
import com.bocop.jxplatform.util.LoginUtil.ILoginListener;
import com.bocop.jxplatform.util.LoginUtil.ILogoutListener;
import com.bocop.jxplatform.view.MyListView;
import com.bocop.qzt.QztApplyActivity;
import com.bocop.qzt.QztAttentionActivity;
import com.bocop.user.WebViewUserActivity;
import com.google.gson.Gson;

public class PersonalFragment extends BaseFragment implements ILoginListener,
		ILogoutListener,IIsCustom {

	@ViewInject(R.id.listView)
	private MyListView listView;
	@ViewInject(R.id.listView2)
	private MyListView listView2;
	@ViewInject(R.id.listView3)
	private MyListView listView3;

	@ViewInject(R.id.iv_login)
	private static ImageView iv_login;

	@ViewInject(R.id.tv_login)
	private static TextView tv_login;

	@ViewInject(R.id.rl_logout)
	private static RelativeLayout rl_logout;
	@ViewInject(R.id.tv_logout)
	private Button tv_logout;
	
	private List<PerFunctionBean> funcList;
	private List<PerFunctionBean> funcList2;
	private List<PerFunctionBean> funcList3;
	private PerFunctionAdapter adapter;
	private PerFunctionAdapter adapter2;
	private PerFunctionAdapter adapter3;

	private String appUrl; // 地址
	private String isNeedUpdate; // 强制更新
	private String version; // 版本号
	private String updateContent; // 更新内容

	/**
	 * service
	 */
	private boolean isBinded;
	private boolean isDestroy = true;
	// private DownloadBinder binder;
	protected Context Context;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = initView(R.layout.fragment_personal);
		return view;
	}

	@Override
	protected void initData() {
		super.initData();
		init();
	}

	@Override
	protected void initView() {
		super.initView();
		Log.i("tag", "initView1");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (LoginUtil.isLog(baseActivity)) {
			tv_login.setText(LoginUtil.getUserId(baseActivity));
			iv_login.setImageResource(R.drawable.pc_ic_head_login);
			rl_logout.setVisibility(View.VISIBLE);
		} else {
			iv_login.setImageResource(R.drawable.pc_ic_head_logout);
			tv_login.setText("登录");
			cacheBean.setUserInfo(null);
			rl_logout.setVisibility(View.GONE);
		}
		// Log.i("tag", "onResume");
	}
	
	public static class LoginReceive extends BroadcastReceiver {

		/* (non-Javadoc)
		 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
		 */
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String loginStatus = intent.getExtras().getString("loginStatus");
			Log.i("Recevier1", "接收到:"+loginStatus);  
			if(loginStatus.equals("loginOn")){
				tv_login.setText(LoginUtil.getUserId(context));
				iv_login.setImageResource(R.drawable.pc_ic_head_login);
				rl_logout.setVisibility(View.VISIBLE);
			}else{
				iv_login.setImageResource(R.drawable.pc_ic_head_logout);
				tv_login.setText("登录");
				cacheBean.setUserInfo(null);
				rl_logout.setVisibility(View.GONE);
			}
			
		}
		
	}

	private void init() {
		System.out.println("init...........personal");
		funcList = new ArrayList<PerFunctionBean>();
		funcList2 = new ArrayList<PerFunctionBean>();
		funcList3 = new ArrayList<PerFunctionBean>();

		// 注册新用户
		PerFunctionBean user = new PerFunctionBean();
		user.setTitle("注册用户");
		user.setImageRes(R.drawable.register);
		user.setFunctionId("myVersion");
		funcList.add(user);

		// 银行卡管理
		PerFunctionBean yhkgl = new PerFunctionBean();
		yhkgl.setTitle(getString(R.string.lv_cardmanager));
		yhkgl.setImageRes(R.drawable.ic1);
		yhkgl.setFunctionId("yhkgl");
		funcList.add(yhkgl);

		// 好友分享
		PerFunctionBean share = new PerFunctionBean();
		share.setTitle("好友分享");
		share.setImageRes(R.drawable.ic4);
		share.setFunctionId("myVersion");
		funcList.add(share);

		// 动态口令
		PerFunctionBean dynamicPswd = new PerFunctionBean();
		dynamicPswd.setTitle("动态口令");
		dynamicPswd.setImageRes(R.drawable.dtkl);
		dynamicPswd.setFunctionId("myVersion");
		funcList2.add(dynamicPswd);

		// 手势管理
		PerFunctionBean ssgl = new PerFunctionBean();
		ssgl.setTitle("手势管理");
		ssgl.setImageRes(R.drawable.icon_ssgl);
		ssgl.setFunctionId("ssgl");
		funcList2.add(ssgl);

		// 修改登录密码
		PerFunctionBean mmgl = new PerFunctionBean();
		mmgl.setTitle("修改登录密码");
		mmgl.setImageRes(R.drawable.pc_icon_mmgl);
		mmgl.setFunctionId("mmgl");
		funcList2.add(mmgl);

		// 找回登录密码
		PerFunctionBean zhmm = new PerFunctionBean();
		zhmm.setTitle("找回登录密码");
		zhmm.setImageRes(R.drawable.icon_zhmm);
		zhmm.setFunctionId("zhmm");
		funcList2.add(zhmm);

		// 支付密码管理
		PerFunctionBean zfmmgl = new PerFunctionBean();
		zfmmgl.setTitle("支付密码管理");
		zfmmgl.setImageRes(R.drawable.icon_zfmmgl);
		zfmmgl.setFunctionId("zfmmgl");
		funcList2.add(zfmmgl);

		// 营销代码
		PerFunctionBean yxdm = new PerFunctionBean();
		yxdm.setTitle("营销代码（营销人员填写）");
		yxdm.setImageRes(R.drawable.iconyxdm);
		yxdm.setFunctionId("yxdm");
		funcList3.add(yxdm);
		
		// 意见反馈
		PerFunctionBean yjfk = new PerFunctionBean();
		yjfk.setTitle("意见反馈");
		yjfk.setImageRes(R.drawable.icon_yjfk);
		yjfk.setFunctionId("yjfk");
		funcList3.add(yjfk);

		// 使用帮助
		PerFunctionBean help = new PerFunctionBean();
		help.setTitle("使用帮助");
		help.setImageRes(R.drawable.ic3);
		help.setFunctionId("myVersion");
		funcList3.add(help);

		// 关于
		PerFunctionBean about = new PerFunctionBean();
		about.setTitle("关于");
		about.setImageRes(R.drawable.ic8);
		about.setFunctionId("myVersion");
		funcList3.add(about);

		adapter = new PerFunctionAdapter(funcList, getActivity());
		listView.setAdapter(adapter);

		adapter2 = new PerFunctionAdapter(funcList2, getActivity());
		listView2.setAdapter(adapter2);

		adapter3 = new PerFunctionAdapter(funcList3, getActivity());
		listView3.setAdapter(adapter3);

		//
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getActivity(),
						BocOpWebActivity.class);
				Bundle bundle = new Bundle();
				if (position == 0) {
					bundle.putString("title", "用户注册");
					bundle.putString("type", "userregister");
					intent.putExtras(bundle);
					startActivity(intent);
//					Intent intentRes = new Intent(getActivity(),
//							 WebViewUserActivity.class);
//					intentRes.putExtra("url", BocSdkConfig.RES_URL);
//					 startActivity(intentRes);
				} else if (position == 2) {
					// bundle.putString("title", "密码管理");
					// bundle.putString("type", "mmgl");
					// intent.putExtras(bundle);
					// startActivity(intent);
					showShare();
				} else if (LoginUtil.isLog(baseActivity)) {
					if (position == 1) {
//						bundle.putString("title", "银行卡管理");
//						bundle.putString("type", "yhkgl");
//						intent.putExtras(bundle);
//						startActivity(intent);
						
						 Intent intentYhk = new Intent(getActivity(),
								 WebViewUserActivity.class);
						 intentYhk.putExtra("url", BocSdkConfig.YHKGL_URL);
						 startActivity(intentYhk);

					}
				} else {
					LoginUtil.authorize(getActivity(), PersonalFragment.this);
				}

			}
		});

		listView2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				BaseApplication application = (BaseApplication) baseActivity
						.getApplication();
				if(application.isNetStat()){
					//找回登陆密码
					if (arg2 == 3) {
						Intent intent = new Intent(getActivity(),
								FindPasswordActivity.class); //
						startActivity(intent);
						return;
					}
					if(LoginUtil.isLog(baseActivity)){
						//手势管理
						if (arg2 == 1) {
									// 如果已经设置了手势密码，则先输入旧的手势密码，验证后，再修改新的手势密码
									if (BaseApplication
											.getInstance()
											.getLockPatternUtils()
											.savedPatternExists(
													LoginUtil.getUserId(baseActivity))) {
										// 输入手势密码
										Intent intent = new Intent();
										intent.putExtra("update", "update");
										intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
										intent.setClass(getActivity(),
												UnlockGesturePasswordActivity.class);
										startActivity(intent);
									} else {// 如果没有设置过手势密码，则直接新建手势密码
										Intent intent = new Intent(baseActivity,
												GuideGesturePasswordActivity.class);
										startActivity(intent);
									}
						}
						//修改登录密码
						if(arg2 == 2){
							Intent intent = new Intent(getActivity(),
									BocOpWebActivity.class);
							Bundle bundle = new Bundle();
							bundle.putString("title", "修改登录密码");
							bundle.putString("type", "mmgl");
							intent.putExtras(bundle);
							startActivity(intent);
							// showShare();
						}
						
						//支付密码管理
						if (arg2 == 4) {
							String url = Constants.UrlForZfmmgl + "clientid=" + BocSdkConfig.CONSUMER_KEY + "&userid=" + LoginUtil.getUserId(getActivity()) + "&accesstoken=" + LoginUtil.getToken(getActivity()) + "&headerbar=0";
							Log.i("tag", url);
							Bundle bundle = new Bundle();
							bundle.putString("url", url);
							bundle.putString("name", "支付密码管理");
							Intent intent = new Intent(baseActivity, WebViewActivity.class);
							intent.putExtras(bundle);
							startActivity(intent);
						}
						//动态口令
						if (arg2 == 0) {
							if (LoginUtil.isLog(baseActivity)) {
								Intent intent = new Intent(getActivity(),
										DynamicPswdActivity.class); // 动态密码
								startActivity(intent);
							} else {
								LoginUtil.authorize(getActivity(),
										PersonalFragment.this);
							}
						}
					}else{
						LoginUtil.authorize(getActivity(),
								PersonalFragment.this);
					}
				}else{
					CustomProgressDialog
					.showBocNetworkSetDialog(baseActivity);
				}
			}
		});
		
		listView3.setOnItemClickListener(new OnItemClickListener() {
			/* (non-Javadoc)
			 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
			 */
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//营销代码
				if(position == 0){
					
					if (LoginUtil.isLog(getActivity())) {
						if(CustomInfo.isExistCustomInfo(baseActivity)){
							requestBocopForMarketInfo();		//查询营销信息
						}else{
							CustomInfo.requestBocopForCustidCallBack(getActivity(),true,PersonalFragment.this);
						}
						
					}else{
						LoginUtil.authorize(getActivity(),PersonalFragment.this);
					}
				}
				
				if(position == 1){
					Uri uri = Uri.parse("mailto:service_boc@163.com");
					String[] email = { "" };
					Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
					intent.putExtra(Intent.EXTRA_CC, email);
					intent.putExtra(Intent.EXTRA_SUBJECT, "");
					intent.putExtra(Intent.EXTRA_TEXT, "请写下您的建议:");
					startActivity(Intent.createChooser(intent, "请选择邮件发送软件"));
				}
				if(position == 2){
					Intent intent = new Intent(getActivity(), Help.class); // 帮助
					startActivity(intent);
				}
				if(position == 3){
					Intent intent = new Intent(getActivity(),
							AbountActivity.class); // 关于
					startActivity(intent);
				}
			}
		});
	}

	protected void requestVersionFromBocop() {
		Gson gson = new Gson();
		Map<String, String> map = new HashMap<String, String>();
		map.put("clientid", BocSdkConfig.CONSUMER_KEY);// 应用KEY
		String strGson = gson.toJson(map);
		Log.i("tag", "检测干本JSON数据：：" + strGson);
		if (strGson != null && strGson.length() > 0) {
			BocOpUtilVersion util = new BocOpUtilVersion(getActivity());
			util.postOpboc(strGson, TransactionValue.SA0000, new CallBackBoc() {

				@Override
				public void onSuccess(String responStr) {
					try {
						Log.i("tag", responStr);
						Map<String, String> map;
						map = JsonUtils.getMapStr(responStr);
						isNeedUpdate = map.get("need_update") + "";// 是否强制更新
						appUrl = map.get("appurl") + ""; // 下载地址
						version = map.get("version") + "";// 最新版本号
						updateContent = map.get("new_function") + "";// 更新内容
						Log.i("tag", isNeedUpdate + "," + appUrl + ","
								+ version + updateContent);
						if (isUpdate(BocSdkConfig.APP_VERSION, version)) {
							showUpdate(); // 显示更新对话框
						} else {
							// Toast.makeText(baseActivity, "已经是最新版本",
							// Toast.LENGTH_LONG).show();
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				@Override
				public void onStart() {

				}

				@Override
				public void onFinish() {

				}

				@Override
				public void onFailure(String responStr) {
					CspUtil.onFailure(getActivity(), responStr);
				}
			});
		}
	}

	protected void showUpdate() {
		Dialog dialog;
		if ("1".equals(isNeedUpdate)) {
			dialog = new AlertDialog.Builder(getActivity())
					.setTitle("发现新版本")
					.setMessage(updateContent)
					// 设置内容
					.setPositiveButton("更新",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// network.getBigFile(activity, appUrl,
									// Environment.getExternalStorageDirectory()
									// + BaseValue.APK_DIR,
									// BaseValue.APK_NAME, downLoadHandler);
									dialog.cancel();
								}
							}).create();
			// 显示对话框
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		} else {// 设置内容
			dialog = new AlertDialog.Builder(getActivity())
					.setTitle("发现新版本")
					.setMessage(updateContent)
					.setPositiveButton("以后再说",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
								}
							})
					.setNeutralButton("立即更新",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Intent updateIntent = new Intent(
											getActivity(),
											DownloadService.class);
									updateIntent.putExtra("url", appUrl);
									getActivity().startService(updateIntent);
									Log.i("tag", "startService");
									// getActivity().bindService(updateIntent,
									// conn, Context.BIND_AUTO_CREATE);
									// network.getBigFile(activity,
									// SettingMainFragment.this, appUrl,
									// Environment.getExternalStorageDirectory()
									// + BaseValue.APK_DIR,
									// BaseValue.APK_NAME, downLoadHandler);
									// HttpDownloader httpDownLoader = new
									// HttpDownloader();
									// Log.i("tag", "httpDownLoader");
									// appUrl =
									// "http://b.hiphotos.baidu.com/image/pic/item/9e3df8dcd100baa1dcabdd6e4310b912c9fc2e5b.jpg";
									// int result = httpDownLoader.downfile(
									// appUrl, "/jxboca/", "jxbocop.apk");
									// if (result == 0) {
									// Toast.makeText(getActivity(), "下载成功！",
									// Toast.LENGTH_SHORT).show();
									// } else if (result == 1) {
									// Toast.makeText(getActivity(), "已有文件！",
									// Toast.LENGTH_SHORT).show();
									// } else if (result == -1) {
									// Toast.makeText(getActivity(), "下载失败！",
									// Toast.LENGTH_SHORT).show();
									// }
									// dialog.cancel();
								}
							}).create();// 创建
			// 显示对话框
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}
	}

	/**
	 * 比较版本号，判断是否需要更新
	 * 
	 * @param oldVersion
	 * @param newVersion
	 * @return
	 */
	public Boolean isUpdate(String oldVersion, String newVersion) {
		String oldVer = "";
		String newVer = "";
		String oldVerArrary[] = oldVersion.split("\\.");
		String newVerArrary[] = newVersion.split("\\.");
		for (int i = 0; i < oldVerArrary.length; i++) {
			oldVer += oldVerArrary[i];
		}
		for (int j = 0; j < newVerArrary.length; j++) {
			newVer += newVerArrary[j];
		}
		if (Integer.parseInt(newVer) > Integer.parseInt(oldVer)) {
			return true;
		} else {
			return false;
		}
	}

	private void showShare() {
		String userId = "";
		String url = "http://open.boc.cn/appstore/#/app/appDetail/38201";
		if (LoginUtil.isLog(baseActivity)) {
			userId = LoginUtil.getUserId(baseActivity);
			url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxd5944788c1a9ce15&redirect_uri=http://jxboc.uni-infinity.com/yhtsharecount/servlet/DealRestResourceForWeixin/"
					+ userId
					+ "/&response_type=code&scope=snsapi_userinfo&state=a123#wechat_redirect";
		}
		ShareSDK.initSDK(getActivity());
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();

		// 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
		// oks.setNotification(R.drawable.ic_launcher,
		// getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle("易惠通");
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		// oks.setTitleUrl("http://www.boc.cn");
		// text是分享文本，所有平台都需要这个字段
		oks.setText("易惠通,百事通");
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		// oks.setImagePath("/sdcard/bocjx.png");//确保SDcard下面存在此张图片
		oks.setImagePath(getResources()
				.getResourcePackageName(R.drawable.bocjx));
		// url仅在微信（包括好友和朋友圈）中使用

		oks.setImageUrl(url);
		oks.setUrl(url);

		// oks.setImageUrl("http://open.boc.cn/appstore/#/app/appDetail/38201");
		// oks.setUrl("http://open.boc.cn/appstore/#/app/appDetail/38201");
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		// oks.setComment("我是测试评论文本");
		// site是分享此内容的网站名称，仅在QQ空间使用
		// oks.setSite(getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		// oks.setSiteUrl("http://sharesdk.cn");

		// 启动分享GUI
		Log.i("tag", url);
		oks.show(getActivity());
		Log.i("tag", "show");
	}
	
	private void requestBocopForMarketInfo(){
		Gson gson = new Gson();
		Map<String,String> map = new HashMap<String,String>();
		map.put("userId", LoginUtil.getUserId(baseActivity));
		final String strGson = gson.toJson(map);
		QztRequestWithJsonAll qztRequestWithJson = new QztRequestWithJsonAll(baseActivity);
		qztRequestWithJson.postOpboc(strGson, BocSdkConfig.marketQueryUrl, new com.bocop.jxplatform.util.QztRequestWithJsonAll.CallBackBoc() {
			
			@Override
			public void onSuccess(String responStr) {
				Log.i("taggonSuccess", responStr);
				Map<String,String> map;
				Map<String,String> mapHead;
				Map<String,String> mapBody;
				String strHead;
				String strStat;
				String strResult;
				String strBody;
				try {
					map = JsonUtils.getMapStr(responStr);
					strHead = map.get("head");
					Log.i("taggonSuccess", strHead);
					
					mapHead = JsonUtils.getMapStr(strHead);
					strStat = mapHead.get("stat");
					strResult = mapHead.get("result");
					Log.i("taggonSuccess", strStat + strResult);
					if(strStat.equals("00")){
						strBody = map.get("body");
						mapBody = JsonUtils.getMapStr(strBody);
						
						String code = mapBody.get("code");
						String orgAddress = mapBody.get("orgAddress");
						String orgId = mapBody.get("orgId");
						String orgName = mapBody.get("orgName");
						String orgNumber = mapBody.get("orgNumber");
						String userId = mapBody.get("userId");
						Log.i("tag", code + orgAddress + orgId + orgName + orgNumber + userId);
						
						Bundle bundle = new Bundle();
						bundle.putString("code", code);
						bundle.putString("orgAddress", orgAddress);
						bundle.putString("orgId", orgId);
						bundle.putString("orgName", orgName);
						bundle.putString("orgNumber", orgNumber);
						bundle.putString("userId", userId);
						Intent intent = new Intent(baseActivity,MarketCodeInfoActivity.class);
						intent.putExtras(bundle);
						startActivity(intent);
						
						
						
					}else if(strStat.equals("01")){
						Intent intent = new Intent(baseActivity, marketCodeAddActivity.class);
						startActivity(intent);
					}
					else{
						Toast.makeText(getActivity(), strResult, Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
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
				Log.i("tagonFinish", "onFinish");
			}
			
			@Override
			public void onFailure(String responStr) {
				Toast.makeText(baseActivity, responStr, Toast.LENGTH_LONG).show();
				
			}
		});
	}

	@OnClick(value = { R.id.iv_login, R.id.tv_login })
	public void loginClick(View view) {
		if (LoginUtil.isLog(baseActivity)) {
			return;
		} else {
			LoginUtil.authorize(getActivity(), this);
		}
	}

	@OnClick(R.id.tv_logout)
	public void logoutClick(View view) {
		LoginUtil.showLogoutAppDialog(baseActivity, PersonalFragment.this);
		// LoginUtil.logout(baseActivity, this,baseActivity.getBaseApp());
	}

	@Override
	public void onLogin() {
		iv_login.setImageResource(R.drawable.pc_ic_head_login);
		tv_login.setText(LoginUtil.getUserId(baseActivity));
		rl_logout.setVisibility(View.VISIBLE);
		// Editor editor;
		// SharedPreferences sp =
		// getActivity().getSharedPreferences(LoginUtil.SP_NAME,
		// Context.MODE_PRIVATE);
		// String userId = sp.getString(CacheBean.USER_ID, "");
		// editor = sp.edit();
		// editor.putString(CacheBean.DZP_USER_ID, userId);
		// editor.commit();
		Toast.makeText(baseActivity, "登录成功", Toast.LENGTH_SHORT).show();
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

	@Override
	public void onLogout() {
		// TODO Auto-generated method stub
		iv_login.setImageResource(R.drawable.pc_ic_head_logout);
		tv_login.setText("登录");
		cacheBean.setUserInfo(null);
		rl_logout.setVisibility(View.GONE);
		Toast.makeText(baseActivity, "退出登录", Toast.LENGTH_SHORT).show();
		
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	/* (non-Javadoc)
	 * @see com.bocop.jxplatform.util.CustomInfo.IIsCustom#onCertification()
	 */
	@Override
	public void onCertification() {
		// TODO Auto-generated method stub
		Log.i("tag", "onCertification");
		requestBocopForMarketInfo();
	}

	/* (non-Javadoc)
	 * @see com.bocop.jxplatform.util.CustomInfo.IIsCustom#noCertification()
	 */
	@Override
	public void noCertification() {
		// TODO Auto-generated method stub
		Log.i("tag", "noCertification");
		Toast.makeText(baseActivity, "亲，请先在“银行卡管理”中绑定卡片噢。", Toast.LENGTH_SHORT).show();
	}

	/* (non-Javadoc)
	 * @see com.bocop.jxplatform.util.CustomInfo.IIsCustom#onCerError()
	 */
	@Override
	public void onCerError() {
		
		
	}

	/* (non-Javadoc)
	 * @see com.bocop.jxplatform.util.CustomInfo.IIsCustom#onCerException()
	 */
	@Override
	public void onCerException() {
		// TODO Auto-generated method stub
		
	}

}
