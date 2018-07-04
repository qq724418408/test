package com.bocop.xfjr.activity;

import com.boc.jx.base.BaseApplication;
import com.boc.jx.baseUtil.view.annotation.CheckNet;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.boc.jx.httptools.http.callback.IHttpCallback;
import com.boc.jx.tools.LogUtils;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.activity.MainActivity;
import com.bocop.xfjr.XfjrMain;
import com.bocop.xfjr.base.XfjrBaseActivity;
import com.bocop.xfjr.bean.login.LoginBean;
import com.bocop.xfjr.bean.login.LoginBean.CustomerInfoBean;
import com.bocop.xfjr.config.HttpRequest;
import com.bocop.xfjr.config.UrlConfig;
import com.bocop.xfjr.constant.XFJRConstant;
import com.bocop.xfjr.helper.ShakeHelper;
import com.bocop.xfjr.util.PatternUtils;
import com.bocop.xfjr.util.PreferencesUtil;
import com.bocop.xfjr.util.TextUtil;
import com.bocop.xfjr.util.XFJRUtil;
import com.bocop.xfjr.view.XFJRClearEditText;
import com.bocop.yfx.utils.ToastUtils;
import com.tencent.bugly.crashreport.CrashReport;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

/**
 * description： 登录
 * <p/>
 * Version：1.0
 */
@ContentView(R.layout.xfjr_activity_login)
public class XFJRLoginActivity extends XfjrBaseActivity {

	private static int message_time = 1;
	private int mTimer = XfjrMain.reqSmsCodeTime;
	@ViewInject(R.id.etPhone)
	private XFJRClearEditText etPhone;
	@ViewInject(R.id.etCode)
	private XFJRClearEditText etCode;
	@ViewInject(R.id.btnGetCode)
	private Button btnGetCode;
	@ViewInject(R.id.btnLogin)
	private Button btnLogin;
	@ViewInject(R.id.ivLogin)
	private View ivLogin;
	private ShakeHelper shakeHelper;
	private short codeLength = 6;
	private boolean isCorrectPhone = false;

	// 倒计时处理
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			if (mTimer > 0) {
				startCutTime();
			} else {
				mTimer = XfjrMain.reqSmsCodeTime;
				btnGetCode.setEnabled(true);
				btnGetCode.setText(R.string.regain_check_num);
			}
		};
	};

	// 开启倒计时
	private void startCutTime() {
		mTimer--;
		btnGetCode.setText(mTimer + "s");
		btnGetCode.setEnabled(false);
		mHandler.sendEmptyMessageDelayed(message_time, 1000);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		XfjrMain.clearAllSP();
		if (XfjrMain.isSit) {
			etPhone.setText("17620371182");
			etPhone.setText("17607842058");
			etCode.setText("123456");
		}
		shakeHelper = new ShakeHelper(this);
		PreferencesUtil.put(XFJRConstant.KEY_IS_LOGIN, false); // 进入登录界面登录状态都改为false
		initListener();
	}
	
	@OnClick(R.id.ivBack)
	private void clickBack(View view) {
		onBackPressed();
	}
	
	@Override
	public void onBackPressed() {
//		XfjrMain.clearAllSP();
		BaseApplication app = (BaseApplication)XfjrMain.mApp;
		app.getActivityManager().finishAllWithoutActivity(MainActivity.class);
		PreferencesUtil.put(XFJRConstant.KEY_IS_LOGIN, false); 
//		super.onBackPressed();
//		boolean isFirstLogin = getIntent().getBooleanExtra(XFJRConstant.KEY_IS_FIRST_LOGIN, false);
//		if (isFirstLogin) {
//			finish();
//		} else {
//			XFJRDialogUtil.confirmDialog(this, "是否退出消费金融预审批系统？", new DialogClick() {
//				
//				@Override
//				public void onOkClick(View view, XfjrDialog dialog) {
//					BaseApplication app = (BaseApplication)XfjrMain.mApp;
//					app.getActivityManager().finishAllWithoutActivity(MainActivity.class);
//					dialog.cancel();
//				}
//				
//				@Override
//				public void onCancelClick(View view, XfjrDialog dialog) {
//					dialog.cancel();
//				}
//			}).show();
//		}
	}
	
	@OnClick(R.id.btnGetCode)
	@CheckNet
	private void clickGetCode(View view) {
		String telephone = etPhone.getText().toString().trim(); // 手机号
		if (!checkPhone()) {
			return;
		}
		// 发送网络请求验证码
		if (XfjrMain.isNet) {
			HttpRequest.reqLoginCode(this, telephone, new IHttpCallback<String>() {

				@Override
				public void onSuccess(String url, String result) {
					ToastUtils.show(XFJRLoginActivity.this, getString(R.string.get_check_num_success), 0);
					// 开启倒计时
					startCutTime();
				}

				@Override
				public void onFinal(String url) {
					LogUtils.e(url);
				}

				@Override
				public void onError(String url, Throwable e) {
					UrlConfig.showErrorTips(XFJRLoginActivity.this, e, true);
				}
			});
		} else {
			// 开启倒计时
			startCutTime();
		}
	}

	@OnClick(R.id.btnLogin)
	@CheckNet
	private void clickLogin(View view) {
		String telephone = etPhone.getText().toString().trim(); // 手机号
		String code = etCode.getText().toString().trim(); // 验证码
		if (!checkPhone()) {
			return;
		}
		if (TextUtils.isEmpty(code)) {
			ToastUtils.show(this, TextUtil.getHintText(etCode), 0);
			etCode.requestFocus();
			return;
		} else if (code.length() != codeLength) {
			ToastUtils.show(XFJRLoginActivity.this, getString(R.string.error_check_num), 0);
			etCode.requestFocus();
			return;
		}
		if (XfjrMain.isNet) {
			//btnLogin.setEnabled(false);
			HttpRequest.reqLogin(this, telephone, code, new IHttpCallback<LoginBean>() {
				
				@Override
				public void onSuccess(String url, LoginBean result) {
					XfjrMain.role = result.getRole();
					switch (result.getRole()) {
					case XFJRConstant.M_ROLE:
						if (result.getMerchantInfo() == null) {
							return;
						}
						ToastUtils.show(XFJRLoginActivity.this, getString(R.string.xfjr_login_success), 0);
						gotoLogin(result);
						break;
					case XFJRConstant.C_ROLE:
						if (result.getCustomerInfo() == null) {
							return;
						}
						gotoLogin(result);
						break;
						
					default:
						break;
					}
				}
				
				@Override
				public void onFinal(String url) {
					LogUtils.e(url);
					//btnLogin.setEnabled(true);
				}
				
				@Override
				public void onError(String url, Throwable e) {
					UrlConfig.showErrorTips(XFJRLoginActivity.this, e, true);
				}
			});
		} else {
			CustomerInfoBean c = new CustomerInfoBean();
			c.setEhr("yinlu");
			c.setName("孙八一");
			c.setOrganName("中国银行红角洲支行");
			if(telephone.equals("17607842058")){
				XfjrMain.role = XFJRConstant.C_ROLE;
				gotoLogin(new LoginBean(XfjrMain.role, c));
			} else {
				XfjrMain.role = XFJRConstant.M_ROLE;
				gotoLogin(new LoginBean(XfjrMain.role, c));
			}
		}
	}

	private boolean checkPhone() {
		String telephone = etPhone.getText().toString().trim(); // 手机号
		if (PatternUtils.isMobile(telephone)) {
			isCorrectPhone = true;
		} else {
			isCorrectPhone = false;
		}
		if (TextUtils.isEmpty(telephone)) {
			ToastUtils.show(this, TextUtil.getHintText(etPhone), 0);
			etPhone.requestFocus();
			return false;
		} else if (!isCorrectPhone) {
			ToastUtils.show(this, getString(R.string.please_input_correct_phone_number), 0);
			etPhone.requestFocus();
			etPhone.setSelection(etPhone.getText().length());
			return false;
		} else {
			return true;
		}
	}

	private void initListener() {
//		etPhone.setOnFocusChangeListener(new OnFocusChangeListener() {
//			@Override
//			public void onFocusChange(View v, boolean hasFocus) {
//				etPhone.onFocusChange(v, hasFocus); // 不调这个方法就不会显示delete图标
//				if (hasFocus) {
//					// 此处为得到焦点时的处理内容
//				} else {
//					// 此处为失去焦点时的处理内容
//					if (!TextUtils.isEmpty(etPhone.getText())) {
//						if (PatternUtils.isMobile(etPhone.getText().toString())) {
//							isCorrectPhone = true;
//						} else {
//							isCorrectPhone = false;
//						}
//					}
//				}
//			}
//		});
//		ivLogin.setOnLongClickListener(new OnLongClickListener() {
//			
//			@Override
//			public boolean onLongClick(View v) {
//				if(XfjrMain.isTest){
//					etPhone.setText("17688153915");
//					etCode.setText("888888");
//				}
//				return true;
//			}
//		});
	}

	private void gotoLogin(LoginBean data) {
		String telephone = etPhone.getText().toString().trim(); // 手机号
		CrashReport.setUserId(telephone); // TODO bugly设置userId
		PreferencesUtil.put(XFJRConstant.KEY_USER_PHONE, telephone);
		if (data.getRole().equals(XFJRConstant.C_ROLE)) {
			PreferencesUtil.put(XFJRConstant.KEY_EHR_ID, data.getCustomerInfo().getEhr());
		}
		PreferencesUtil.put(XFJRConstant.KEY_IS_LOGIN, true);
		/**
		 * 跳转请带参数 0 或者 1 0代表客户经理 1代表商户
		 */
		XfjrIndexActivity.goMe(this, data);
		finish();
//		boolean isFirstLogin = getIntent().getBooleanExtra(XFJRConstant.KEY_IS_FIRST_LOGIN, false);
//		if (isFirstLogin) {
//			XfjrIndexActivity.goMe(this, data);
//			finish();
//		} else {
//			finish();
//		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (null != shakeHelper) {
			shakeHelper.stop();
			shakeHelper = null;
		}
	}
	
	@Override
	protected void onResume() {
		if (null != shakeHelper) {
			shakeHelper.start();
		}
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		if (null != shakeHelper) {
			shakeHelper.stop();
		}
		super.onPause();
	}
	
	/**
	 * 空白处点击事件
	 * 
	 * @param view
	 */
	@OnClick(R.id.lltBlank)
	private void clickBlank(View view) {
		XFJRUtil.hideSoftInput(view);
	}
	
	
	
}