package com.bocop.xfjr.helper.index;

import java.util.ArrayList;
import java.util.List;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.base.BaseApplication;
import com.boc.jx.baseUtil.view.ViewUtils;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.boc.jx.httptools.http.callback.IHttpCallback;
import com.boc.jx.tools.LogUtils;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.activity.MainActivity;
import com.bocop.xfjr.XfjrMain;
import com.bocop.xfjr.activity.XFJRSearchActivity;
import com.bocop.xfjr.activity.customer.XFJRMyBusinessActivity;
import com.bocop.xfjr.activity.customer.XfjrMainActivity;
import com.bocop.xfjr.argument.ArgumentUtil;
import com.bocop.xfjr.argument.Subscribe;
import com.bocop.xfjr.bean.BusinessInfoBean;
import com.bocop.xfjr.bean.ErrorBean;
import com.bocop.xfjr.bean.SystemBasicInfo;
import com.bocop.xfjr.bean.login.LoginBean;
import com.bocop.xfjr.config.HttpRequest;
import com.bocop.xfjr.config.UrlConfig;
import com.bocop.xfjr.constant.XFJRConstant;
import com.bocop.xfjr.util.PreferencesUtil;
import com.bocop.xfjr.util.dialog.XFJRDialogUtil;
import com.bocop.xfjr.util.dialog.XFJRDialogUtil.DialogClick;
import com.bocop.xfjr.util.dialog.XfjrDialog;
import com.bocop.xfjr.util.file.SystemBasicJSONWRUtil;
import com.bocop.xfjr.view.MyTaskView;
import com.google.gson.Gson;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

/**
 * 首页客服经理
 */
public class XfjrIndexManagerHelper implements IIndexHelper, IHttpCallback<List<BusinessInfoBean>> {

	// 首页Activity
	private BaseActivity mActivity;
	// 客服经理布局
	private View mView;
	// 客户经理名称
	@ViewInject(R.id.tvManagerUsername)
	private TextView tvManagerUsername;
	// 银行地址
	@ViewInject(R.id.tvManagerUserAddr)
	private TextView tvManagerUserAddr;
	// 客户经理类型
	@ViewInject(R.id.tvManagerUserDuty)
	private TextView tvManagerUserDuty;
	// 待预审
	@ViewInject(R.id.tvWaitingCheck)
	private MyTaskView tvWaitingCheck;
	// 待决策
	@ViewInject(R.id.tvWaitingDecision)
	private MyTaskView tvWaitingDecision;
	// 待审批
	@ViewInject(R.id.tvWaitingApprove)
	private MyTaskView tvWaitingApprove;
	// 待方款
	@ViewInject(R.id.tvWaitingLoan)
	private MyTaskView tvWaitingLoan;
	// 已方款
	@ViewInject(R.id.tvHadLoan)
	private MyTaskView tvHadLoan;
	// 已拒绝
	@ViewInject(R.id.tvHadReject)
	private MyTaskView tvHadReject;

	public XfjrIndexManagerHelper(BaseActivity activity, View view) {
		super();
		ArgumentUtil.get().register(this);
		this.mActivity = activity;
		this.mView = view;
		ViewUtils.inject(this, mView);
	}

	@Subscribe
	public void receive(LoginBean loginBean) {
		// 设置商戶名称
		tvManagerUsername.setText(loginBean.getCustomerInfo().getName());
		// 设置地址详情
		tvManagerUserAddr.setText(loginBean.getCustomerInfo().getOrganName().trim().replace(" ", ""));
	}

	
	@Subscribe
	public void refreshData(String type){
		switch (type) {
		case XFJRConstant.C_STATUS_5_STR:
			initData(false);
			break;

		default:
			break;
		}
	}
	
	@Override
	public void onCreat() {
//		if (XfjrMain.isNet) {
//			initData(true);
//		} else {
//			localData();
//		}
//		updateDataBroadcastReceiver = new UpdateDataBroadcastReceiver();
//		mActivity.registerReceiver(updateDataBroadcastReceiver, new IntentFilter(XFJRConstant.ACTION_UPDATE_DATA));
	}

	@Override
	public void onResume() {
		if (XfjrMain.isNet) {
			initData(true);
		} else {
			localData();
		}
	}

	@Override
	public void onPause() {

	}

	@Override
	public void onDestory() {
		ArgumentUtil.get().unRegister(this);
//		mActivity.unregisterReceiver(updateDataBroadcastReceiver);
	}
	
	
	private void reqSysBasicInfo(final boolean isShowDialog) {
		//btnLogin.setEnabled(false);
		HttpRequest.reqSysBasicInfo(mActivity, true, new IHttpCallback<SystemBasicInfo>() {
			
			@Override
			public void onSuccess(String url, SystemBasicInfo result) {
				LogUtils.e(result.toString());
				if (isEmptyList(result.getCityList()) 
						|| isEmptyList(result.getIndustryList())
						|| isEmptyList(result.getJobLevelList())
						|| isEmptyList(result.getPayMethodsList()) 
						|| isEmptyList(result.getPeriodList())) {
					reTryDialog();
				} else {
					SystemBasicJSONWRUtil.writeSystemBasicInfo(mActivity, result);
					//ToastUtils.show(XFJRLoginActivity.this, getString(R.string.xfjr_login_success), 0);
					HttpRequest.reqBusinessInfo(mActivity, XfjrIndexManagerHelper.this, isShowDialog);
				}
			}
			
			@Override
			public void onFinal(String url) {
				// TODO Auto-generated method stub
				//btnLogin.setEnabled(true);
			}
			
			@Override
			public void onError(String url, Throwable e) {
				String json = e.getMessage();
				LogUtils.e("error Json = "+json);
				ErrorBean b = new Gson().fromJson(json, ErrorBean.class);
				if (b.code==Integer.parseInt(UrlConfig.emptyCode)) {
					reTryDialog();
					return ;
				}
				
				UrlConfig.showErrorTips(mActivity, e, true);
			}
		});
	}
	
	@SuppressWarnings("rawtypes")
	private boolean isEmptyList(List list) {
		return (null == list) || (list.size() <= 0);
	}
	
	private XfjrDialog reTryDialog() {
		XfjrDialog dialog = XFJRDialogUtil.confirmDialog(mActivity, "获取系统基本信息失败，请重试", new DialogClick() {
			
			@Override
			public void onOkClick(View view, XfjrDialog dialog) {
				reqSysBasicInfo(true);
				dialog.cancel();
			}

			@Override
			public void onCancelClick(View view, XfjrDialog dialog) {
				BaseApplication app = (BaseApplication)XfjrMain.mApp;
				app.getActivityManager().finishAllWithoutActivity(MainActivity.class);
				dialog.cancel();
			}
		});
		TextView tvOk = dialog.getView(R.id.tvOk);
		TextView tvCancel = dialog.getView(R.id.tvCancel);
		tvOk.setText("重试"); // 到登录页，重新登录
		tvCancel.setText("退出"); // 退出消费金融预审批系统，回到易惠通首页
		dialog.setCancelable(false);
		dialog.show();
		return dialog;
	}

	/**
	 * 请求网络数据
	 * 
	 * @param isShowDialog
	 *            是否需要dialog
	 */
	private void initData(final boolean isShowDialog) {
		 reqSysBasicInfo(isShowDialog);
		
	}

	/**
	 * 网络请求成功
	 */
	@Override
	public void onSuccess(String url, List<BusinessInfoBean> result) {
		for (BusinessInfoBean infoBean : result) {
			if (infoBean.getType() == 0) {
				String n = PreferencesUtil.get(XFJRConstant.KEY_B_NUMBER0, "0");
				if(!n.equals(infoBean.getNumber())){
					PreferencesUtil.put(XFJRConstant.KEY_B_NUMBER0, infoBean.getNumber());
					PreferencesUtil.put(XFJRConstant.KEY_IS_READ0, false);
					ArgumentUtil.get().post(0);// 5代表已拒绝
				}
				tvWaitingCheck.setTaskNum(infoBean.getNumber());
				XfjrMain.TYPES.get(0).setNumber(infoBean.getNumber());
			} else if (infoBean.getType() == 1) {
				String n = PreferencesUtil.get(XFJRConstant.KEY_B_NUMBER1, "0");
				if(!n.equals(infoBean.getNumber())){
					PreferencesUtil.put(XFJRConstant.KEY_B_NUMBER1, infoBean.getNumber());
					PreferencesUtil.put(XFJRConstant.KEY_IS_READ1, false);
					ArgumentUtil.get().post(1);// 5代表已拒绝
				}
				tvWaitingDecision.setTaskNum(infoBean.getNumber());
				XfjrMain.TYPES.get(1).setNumber(infoBean.getNumber());
			} else if (infoBean.getType() == 2) {
				String n = PreferencesUtil.get(XFJRConstant.KEY_B_NUMBER2, "0");
				if(!n.equals(infoBean.getNumber())){
					PreferencesUtil.put(XFJRConstant.KEY_B_NUMBER2, infoBean.getNumber());
					PreferencesUtil.put(XFJRConstant.KEY_IS_READ2, false);
					ArgumentUtil.get().post(2);// 5代表已拒绝
				}
				tvWaitingApprove.setTaskNum(infoBean.getNumber());
				XfjrMain.TYPES.get(2).setNumber(infoBean.getNumber());
			} else if (infoBean.getType() == 3) {
				String n = PreferencesUtil.get(XFJRConstant.KEY_B_NUMBER3, "0");
				if(!n.equals(infoBean.getNumber())){
					PreferencesUtil.put(XFJRConstant.KEY_B_NUMBER3, infoBean.getNumber());
					PreferencesUtil.put(XFJRConstant.KEY_IS_READ3, false);
					ArgumentUtil.get().post(3);// 5代表已拒绝
				}
				tvWaitingLoan.setTaskNum(infoBean.getNumber());
				XfjrMain.TYPES.get(3).setNumber(infoBean.getNumber());
			} else if (infoBean.getType() == 4) {
				String n = PreferencesUtil.get(XFJRConstant.KEY_B_NUMBER4, "0");
				if(!n.equals(infoBean.getNumber())){
					PreferencesUtil.put(XFJRConstant.KEY_B_NUMBER4, infoBean.getNumber());
					PreferencesUtil.put(XFJRConstant.KEY_IS_READ4, false);
					ArgumentUtil.get().post(4);// 5代表已拒绝
				}
				tvHadLoan.setTaskNum(infoBean.getNumber());
				XfjrMain.TYPES.get(4).setNumber(infoBean.getNumber());
			} else if (infoBean.getType() == 5) {
				String n = PreferencesUtil.get(XFJRConstant.KEY_B_NUMBER5, "0");
				if(!n.equals(infoBean.getNumber())){
					PreferencesUtil.put(XFJRConstant.KEY_B_NUMBER5, infoBean.getNumber());
					PreferencesUtil.put(XFJRConstant.KEY_IS_READ5, false);
					ArgumentUtil.get().post(5);// 5代表已拒绝
				}
				tvHadReject.setTaskNum(infoBean.getNumber());
				XfjrMain.TYPES.get(5).setNumber(infoBean.getNumber());
			}
		}
		// 发送广播通知业务列表更新指示器(发现没有效果)
		Intent intent = new Intent(XFJRConstant.ACTION_UPDATE_DATA);
		intent.putExtra(XFJRConstant.ACTION_FLAG_INT, 99);
		intent.putExtra(XFJRConstant.KEY_BUSINESS_STATUS, 2);
		mActivity.sendBroadcast(intent);
		
	}

	/**
	 * 网络请求失败
	 */
	@Override
	public void onError(String url, Throwable e) {
		LogUtils.e("164 ->" +e.getMessage());
		UrlConfig.showErrorTips(mActivity, e, true);
	}

	/**
	 * 网络请求一定进入
	 */
	@Override
	public void onFinal(String url) {
		LogUtils.e("164 -> onFinal");
	}

	/**
	 * 退出登录
	 * 
	 * @param view
	 */
	@OnClick(R.id.ivGoLogin1)
	private void signOut(View view) {
		HttpRequest.signOutLogin(mActivity);
		XfjrMain.startXFJR(mActivity);
		mActivity.finish();
	}
	
	/**
	 * 关闭页面
	 * 
	 * @param view
	 */
	@OnClick(R.id.ivFinish1)
	private void clickFinish(View view){
//		XfjrMain.clearAllSP();
		PreferencesUtil.put(XFJRConstant.KEY_IS_LOGIN, false);
		mActivity.finish();
	}

	/**
	 * 刷新按钮
	 */
	@OnClick(R.id.iv_manager_refresh)
	private void refresh(View view) {
		// 是否有開關
		if (XfjrMain.isNet) {
			initData(true);
		} else {
			//((XfjrIndexActivity) mActivity).swichView(true);
		}
	}

	/**
	 * 搜索按钮
	 */
	@OnClick(R.id.iv_manager_search)
	private void search(View view) {
		XFJRSearchActivity.goMe(mActivity, XFJRSearchActivity.MANAGER_SEARCH);
	}

	/**
	 * 新建业务按钮
	 */
	@OnClick(R.id.tvNewOrder)
	private void newOrder(View view) {
		XfjrMainActivity.callMe(mActivity, 0);
	}

	/**
	 * 待预审
	 * 
	 * @param view
	 */
	@OnClick(R.id.tvWaitingCheck)
	private void tvWaitingCheck(View view) {
//		if ("0".equals(tvWaitingCheck.getTaskNum())) {
//			ToastUtils.show(mActivity, "当前无待预审业务", 0);
//			return ;
//		}
		goDetailList(0);
	}

	/**
	 * 待决策
	 * 
	 * @param view
	 */
	@OnClick(R.id.tvWaitingDecision)
	private void tvWaitingDecision(View view) {
//		if ("0".equals(tvWaitingDecision.getTaskNum())) {
//			ToastUtils.show(mActivity, "当前无待决策业务", 0);
//			return ;
//		}
		goDetailList(1);
	}

	/**
	 * 待审批
	 * 
	 * @param view
	 */
	@OnClick(R.id.tvWaitingApprove)
	private void tvWaitingApprove(View view) {
//		if ("0".equals(tvWaitingApprove.getTaskNum())) {
//			ToastUtils.show(mActivity, "当前无待审批业务", 0);
//			return ;
//		}
		goDetailList(2);
	}

	/**
	 * 待放款
	 * 
	 * @param view
	 */
	@OnClick(R.id.tvWaitingLoan)
	private void tvWaitingLoan(View view) {
//		if ("0".equals(tvWaitingLoan.getTaskNum())) {
//			ToastUtils.show(mActivity, "当前无待放款业务", 0);
//			return ;
//		}
		goDetailList(3);
	}

	/**
	 * 已放款
	 * 
	 * @param view
	 */
	@OnClick(R.id.tvHadLoan)
	private void tvHadLoan(View view) {
//		if ("0".equals(tvHadLoan.getTaskNum())) {
//			ToastUtils.show(mActivity, "当前无已放款业务", 0);
//			return ;
//		}
		goDetailList(4);
	}

	/**
	 * 已拒绝
	 * 
	 * @param view
	 */
	@OnClick(R.id.tvHadReject)
	private void tvHadReject(View view) {
//		if ("0".equals(tvHadLoan.getTaskNum())) {
//			ToastUtils.show(mActivity, "当前无已拒绝业务", 0);
//			return ;
//		}
		goDetailList(5);
	}

	/**
	 * 根据状态跳转
	 */
	private void goDetailList(int status) {
		Intent intent = new Intent(mActivity, XFJRMyBusinessActivity.class).putExtra("status", status);
		mActivity.startActivity(intent);
		mActivity.overridePendingTransition(0, 0);
	}
	
	private void localData() {
		List<BusinessInfoBean> result = new ArrayList<>();
		result.add(new BusinessInfoBean(0, "100"));
		result.add(new BusinessInfoBean(1, "10"));
		result.add(new BusinessInfoBean(2, "8"));
		result.add(new BusinessInfoBean(3, "10"));
		result.add(new BusinessInfoBean(4, "12"));
		result.add(new BusinessInfoBean(5, "3"));
		for (BusinessInfoBean infoBean : result) {
			if (infoBean.getType() == 0) {
				String n = PreferencesUtil.get(XFJRConstant.KEY_B_NUMBER0, "0");
				if(!n.equals(infoBean.getNumber())){
					PreferencesUtil.put(XFJRConstant.KEY_B_NUMBER0, infoBean.getNumber());
					PreferencesUtil.put(XFJRConstant.KEY_IS_READ0, false);
				}
				tvWaitingCheck.setTaskNum(infoBean.getNumber());
				XfjrMain.TYPES.get(0).setNumber(infoBean.getNumber());
			} else if (infoBean.getType() == 1) {
				String n = PreferencesUtil.get(XFJRConstant.KEY_B_NUMBER1, "0");
				if(!n.equals(infoBean.getNumber())){
					PreferencesUtil.put(XFJRConstant.KEY_B_NUMBER1, infoBean.getNumber());
					PreferencesUtil.put(XFJRConstant.KEY_IS_READ1, false);
				}
				tvWaitingDecision.setTaskNum(infoBean.getNumber());
				XfjrMain.TYPES.get(1).setNumber(infoBean.getNumber());
			} else if (infoBean.getType() == 2) {
				String n = PreferencesUtil.get(XFJRConstant.KEY_B_NUMBER2, "0");
				if(!n.equals(infoBean.getNumber())){
					PreferencesUtil.put(XFJRConstant.KEY_B_NUMBER2, infoBean.getNumber());
					PreferencesUtil.put(XFJRConstant.KEY_IS_READ2, false);
				}
				tvWaitingApprove.setTaskNum(infoBean.getNumber());
				XfjrMain.TYPES.get(2).setNumber(infoBean.getNumber());
			} else if (infoBean.getType() == 3) {
				String n = PreferencesUtil.get(XFJRConstant.KEY_B_NUMBER3, "0");
				if(!n.equals(infoBean.getNumber())){
					PreferencesUtil.put(XFJRConstant.KEY_B_NUMBER3, infoBean.getNumber());
					PreferencesUtil.put(XFJRConstant.KEY_IS_READ3, false);
				}
				tvWaitingLoan.setTaskNum(infoBean.getNumber());
				XfjrMain.TYPES.get(3).setNumber(infoBean.getNumber());
			} else if (infoBean.getType() == 4) {
				String n = PreferencesUtil.get(XFJRConstant.KEY_B_NUMBER4, "0");
				if(!n.equals(infoBean.getNumber())){
					PreferencesUtil.put(XFJRConstant.KEY_B_NUMBER4, infoBean.getNumber());
					PreferencesUtil.put(XFJRConstant.KEY_IS_READ4, false);
				}
				tvHadLoan.setTaskNum(infoBean.getNumber());
				XfjrMain.TYPES.get(4).setNumber(infoBean.getNumber());
			} else if (infoBean.getType() == 5) {
				String n = PreferencesUtil.get(XFJRConstant.KEY_B_NUMBER5, "0");
				if(!n.equals(infoBean.getNumber())){
					PreferencesUtil.put(XFJRConstant.KEY_B_NUMBER5, infoBean.getNumber());
					PreferencesUtil.put(XFJRConstant.KEY_IS_READ5, false);
				}
				tvHadReject.setTaskNum(infoBean.getNumber());
				XfjrMain.TYPES.get(5).setNumber(infoBean.getNumber());
			}
		}
	}
	
//	private BroadcastReceiver updateDataBroadcastReceiver;
//	
//	class UpdateDataBroadcastReceiver extends BroadcastReceiver {
//
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			if (null != intent) {
//				int res = intent.getIntExtra(XFJRConstant.ACTION_FLAG_INT, -2);
//				int stas = intent.getIntExtra(XFJRConstant.KEY_BUSINESS_STATUS, -1);
//				LogUtils.e("首页收到广播--flag=="+res+"-----status=="+stas);
//				if (res == 0) { // 这是列表继续申请（补充资料）或者详情继续申请（补充资料）完成之后发送广播通知首页更新，再发送广播通知列表或者详情
//					intent.putExtra(XFJRConstant.ACTION_FLAG_INT, res++);
//					mActivity.sendBroadcast(intent);
//					initData(true);
//				} else if (res == -1) {
//					initData(true); // 这是新建业务的时候后续每一步成功了发送的广播通知首页更新数据
//				}
//			}
//		}
//	}
	
}
