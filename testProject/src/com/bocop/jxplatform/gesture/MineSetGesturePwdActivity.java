package com.bocop.jxplatform.gesture;

import com.boc.jx.base.BaseActivity;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.gesture.util.LocusPassWordUtil;
import com.bocop.jxplatform.gesture.view.LocusPassWordView;
import com.bocop.jxplatform.gesture.view.LocusPassWordView.OnCompareListener;
import com.bocop.jxplatform.gesture.view.LocusPassWordView.OnCompleteListener;
import com.bocop.jxplatform.gesture.view.LocusPassWordView.onCheckListener;
import com.bocop.jxplatform.util.IApplication;
import com.bocop.jxplatform.util.LoginUtilAnother;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 设置手势界面的密码
 * 
 * @author huwei
 */
public class MineSetGesturePwdActivity extends BaseActivity {
	// 手势密码的开关状态
	public static boolean stateFlag = true;
	private SharedPreferences sp;
	// 九宫格显示布局，文本框提示布局，九宫格设置布局的总体
	private LinearLayout passwordLinearLayout;
	// 九宫格设置布局改变时，提示文本框
	private TextView showTextView;
	// 九宫格设置布局
	private LocusPassWordView lpwv;
	// 九宫格显示布局，0为灰色，1为亮色
	private String[] gridLists = { "0", "0", "0", "0", "0", "0", "0", "0", "0" };
	private String activityName = "";
	public static boolean isForgetPassword = false;
	/**
	 * defaultPassword-原始密码，resultPassword-新密码 忘记手势密码吗？
	 * 如果没有设置成功（点返回键时），设置原密码为最终密码，否则设置新密码为最终密码
	 */
	private String defaultPassword = "", resultPassword = "";
	// 判断是否是忘记密码后清空手势密码，保存手势密码的
	private boolean cleared = false;
	private boolean isUpdate = false;
	private Activity mActivity;
	private boolean isSetting;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = this;
		getIntentData(savedInstanceState);
		loadXml();
		init();
		setListener();
		setData();
	}

	public void loadXml() {
		setContentView(R.layout.set_new_locus_password_activity);
		sp = getSharedPreferences("slipswitch", MODE_PRIVATE);
		defaultPassword = LocusPassWordUtil.getPassword(mActivity);
		// 进入该页面，更改手势密码状态为false，区分开刚设置还是以前设置过忘记密码进来的情况
		LocusPassWordUtil.resetPasswordStatus(mActivity);
	}

	public void getIntentData(Bundle savedInstanceState) {
		activityName = getIntent().getStringExtra("activityName");
		isUpdate = getIntent().getBooleanExtra("isUpdate", false);
		isSetting = getIntent().getBooleanExtra("isSetting", false);
	}

	public void init() {
		passwordLinearLayout = (LinearLayout) findViewById(R.id.passowordLinearLayout);
		showTextView = (TextView) findViewById(R.id.showTextView);
		// toggle.setSwitchState(flag);
		stateFlag = sp.getBoolean("state", true);
		stateFlag = true;
		if (!LocusPassWordUtil.getHandPassword(mActivity) && !TextUtils.isEmpty(IApplication.userid) || isSetting) {
			findViewById(R.id.rl_title).setVisibility(View.GONE);
		}
		lpwv = (LocusPassWordView) this.findViewById(R.id.mLocusPassWordView);
		// 控制 手势密码的控件
		if (!stateFlag) {// 忘记密码，登录后，即使为开启状态也要改为关闭状态
			passwordLinearLayout.setVisibility(View.GONE);
		} else {
			passwordLinearLayout.setVisibility(View.VISIBLE);
			if (!"".equals(defaultPassword)) {
				showTextView.setText(R.string.inputDefaultPassword);
			} else {
				showTextView.setText(R.string.inputNewPassword);
			}
			showTextView.setTextColor(getResources().getColor(R.color.black));
		}

	}

	public void setListener() {
		lpwv.setOnCompareListener(lpwvOnCompareListener);

		lpwv.setOnCheckListener(lpwvOnCheckListener);
		lpwv.setOnCompleteListener(lpwvOnCompleteListener);
	}

	public void setData() {
		// TODO Auto-generated method stub
		// 原始密码为空，直接输入新密码，否则判断原始密码是否正确，点击home键再次进来时，接着上次的运行，特殊情况除外
		if ("".equals(defaultPassword)) {
			lpwv.setFirst(false);
			lpwv.setSecond(true);
		} else {
			lpwv.setFirst(true);
		}
	}

	/**
	 * 将手势密码的开关状态判断，由onCreate移到onResume，在极端情况下：
	 * 当前页面是修改手势密码且开关状态为打开状态，点击home键，再次进入该系统却忘记密码，重新登陆后手势密码清空，将开关状态更改为关闭状态
	 * modified by liuweina，2013/10/14
	 */
	@Override
	protected void onResume() {
		super.onResume();
		cleared = LocusPassWordUtil.getPasswordStatus(mActivity);
		if (cleared) {// 从手势密码界面，点击home，忘记密码再次进入时，更改其状态为关闭状态
			passwordLinearLayout.setVisibility(View.GONE);
			// 手势密码清空，下面开关的状态改变时，影响判断
			defaultPassword = LocusPassWordUtil.getPassword(mActivity);
			// 进入该页面，更改手势密码状态为false，区分开刚设置还是以前设置过忘记密码进来的情况
			LocusPassWordUtil.resetPasswordStatus(mActivity);
		}
	}

	/**
	 * 将显示密码归为全0
	 * 
	 * @param passStr
	 */
	private void refreshPassStr() {
		for (int i = 0; i < 9; i++) {
			gridLists[i] = "0";
		}
	}

	/**
	 * 原始密码不为空时，输入原始密码后，比较原始密码输入正确与否 modified by liuweina，2013/10/14
	 */
	private OnCompareListener lpwvOnCompareListener = new OnCompareListener() {

		@Override
		public void onCompare(String password) {
			if (password.contains(",")) {// password为本次输入的手势密码值
				refreshPassStr();
				String[] passStr = password.split(",");
				for (int i = 0; i < passStr.length; i++) {
					gridLists[Integer.parseInt(passStr[i])] = "1";
				}
				if (lpwv.verifyPassword(password)) {// 输入原始密码正确
					defaultPassword = password;
					showTextView.setText(R.string.inputNewPassword);
					showTextView.setTextColor(Color.BLACK);
					// 修改判断条件，下一步可输入新密码
					lpwv.setFirst(false);
					lpwv.setSecond(true);
				} else {
					showTextView.setText(R.string.originalPasswordInputError);
					showTextView.setTextColor(getResources().getColor(R.color.hand_less_error));
				}
			} else if (getResources().getString(R.string.passwordistooshortinputagain).equals(password)) {
				showTextView.setText(R.string.handpasswordless);
				showTextView.setTextColor(getResources().getColor(R.color.hand_less_error));
			}
		}
	};

	/**
	 * 第一次输入新密码后，更新上方的小显示框
	 */
	private onCheckListener lpwvOnCheckListener = new onCheckListener() {

		@Override
		public void onCheck(String password) {
			if (password.contains(",")) {
				refreshPassStr();
				String[] passStr = password.split(",");
				for (int i = 0; i < passStr.length; i++) {
					gridLists[Integer.parseInt(passStr[i])] = "1";
				}
				showTextView.setText(R.string.resetInputNewPassword);
				showTextView.setTextColor(Color.BLACK);
				// 第一次输入新密码后，更改状态为判断两次新密码是否正确
				lpwv.setFirst(false);
				lpwv.setSecond(false);
				lpwv.setThird(true);
			}
		}
	};

	/**
	 * 第二次输入新密码后，比较两次密码是否一致
	 */
	private OnCompleteListener lpwvOnCompleteListener = new OnCompleteListener() {

		@Override
		public void onComplete(String firstPassword, String secondPassword) {
			if (firstPassword.equals(secondPassword)) {
				showTextView.setText(R.string.setPasswordSuccess);
				showTextView.setTextColor(Color.BLACK);
				resultPassword = secondPassword;
				LocusPassWordUtil.resetPassWord(mActivity, secondPassword, false);
				LocusPassWordUtil.setHandPassword(mActivity, true);
				Class<?> nextActivityName;
				try {
					if (TextUtils.isEmpty(activityName)) {
						int what = getIntent().getIntExtra("what", -1);
						LoginUtilAnother.startHandler(what);
						mActivity.finish();
					} else {
						nextActivityName = Class.forName(activityName);
						Intent intent = new Intent(mActivity, nextActivityName);
						startActivity(intent);
						overridePendingTransition(R.anim.next_in, R.anim.next_out);
						mActivity.finish();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				showTextView.setText(R.string.newPasswordInputError);
				showTextView.setTextColor(getResources().getColor(R.color.hand_less_error));
				lpwv.setFirst(false);
				lpwv.setSecond(true);
				lpwv.setThird(false);
			}
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 点返回键，先判断手势密码是否为空，若为空，保持开关状态为关；否则，开关状态不变
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ("".equals(defaultPassword)) {// 初始密码为空，手势密码未设置成功过，保持关闭状态
				sp.edit().putBoolean("state", false).commit();
			}
			if (!LocusPassWordUtil.getHandPassword(mActivity) && !TextUtils.isEmpty(IApplication.userid))
				return false;
			mActivity.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

}
