package com.bocop.jxplatform.gesture;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.activity.MainActivity;
import com.bocop.jxplatform.gesture.util.LocusPassWordUtil;
import com.bocop.jxplatform.gesture.view.LocusPassWordView;
import com.bocop.jxplatform.gesture.view.LocusPassWordView.OnCompareListener;
import com.bocop.jxplatform.util.IApplication;
import com.bocop.jxplatform.util.LoginUtil;
import com.bocop.jxplatform.util.LoginUtil.ILogoutListener;
import com.bocop.jxplatform.util.LoginUtilAnother;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * HomeKey 手势解锁登陆Activity
 * 
 * @author zy 判断是否是1>点击home键，手势密码开启且手势密码不为空时才进入到该页面 也有可能是2>刚打开该软件，modified by
 *         liuweina 若是情况1，值为true；若是情况2，值为false；若是true，则直接关闭该页面，否则，跳转到“我的最爱”界面
 */
@ContentView(R.layout.login_locus_activity)
public class MineGesturePwdActivity extends BaseActivity implements
		OnClickListener {
	private LocusPassWordView lpwv;
	public static final int OAUTHERROR = 210;
	public static int displayWidth;
	private TextView text_findpassword, noSetPassword;
	private int errroNumber = 0;
	private String activityName = "";
	private boolean isReComeIn = false;
	private String[] gridLists = { "0", "0", "0", "0", "0", "0", "0", "0", "0" };
	private Activity mActivity;
	public static final int PASSWORDERROR = 1200;
	private int page;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		page = getIntent().getIntExtra("page", 0);
		displayWidth = IApplication.displayWidth;
		this.mActivity = this;
		init();
		setListener();
	}

	/**
	 * 当前页面是HomeLoginActivity，此时再点击home键，将不跳转到该页面，即只有一个输入手势密码进入系统的界面
	 */
	public void init() {
		lpwv = (LocusPassWordView) findViewById(R.id.mLocusPassWordView);
		lpwv.setFirst(true);//
		noSetPassword = (TextView) findViewById(R.id.tvNoSetPassword);
		text_findpassword = (TextView) findViewById(R.id.tvForgetPassword);
		activityName = getIntent().getStringExtra("activityName");
		isReComeIn = getIntent().getBooleanExtra("isReComeIn", false);
		if (5 - LocusPassWordUtil.getErrorTime(mActivity) <= 0) {
			callback(7);
			finish();
		}
	}

	public void setListener() {
		lpwv.setOnCompareListener(new OnCompareListener() {

			@Override
			public void onCompare(String password) {
				errroNumber = LocusPassWordUtil.getErrorTime(mActivity);
				errroNumber++;
				for (int i = 0; i < gridLists.length; i++)
					gridLists[i] = "0";
				if (!TextUtils.isEmpty(password)
						&& !getResources().getString(
								R.string.passwordistooshortinputagain).equals(
								password)) {
					String[] results = password.split(",");
					for (String r : results)
						gridLists[Integer.valueOf(r)] = "1";
				} else if (getResources().getString(
						R.string.passwordistooshortinputagain).equals(password)) {
					LocusPassWordUtil.setErrorTime(mActivity, errroNumber);
				}
				if (lpwv.verifyPassword(password)) {
					noSetPassword.setText("解锁成功");
					noSetPassword.setTextColor(Color.WHITE);
					LocusPassWordUtil.setErrorTime(mActivity, 0);
					overridePendingTransition(R.anim.next_in, R.anim.next_out);
					if (isReComeIn) {
						mActivity.finish();
						return;
					}
					Class<?> nextActivityName;
					try {
						Intent intent = null;
						if (TextUtils.isEmpty(activityName)) {
							int what = getIntent().getIntExtra("what", -1);
							callback(what);
							finish();
							return;
						}
						nextActivityName = Class.forName(activityName);
						intent = new Intent(mActivity, nextActivityName);
						startActivity(intent);
						overridePendingTransition(R.anim.next_in,
								R.anim.next_out);
						intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
						startActivity(intent);
						finish();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				} else {
					LocusPassWordUtil.setErrorTime(mActivity, errroNumber);
					if (5 - LocusPassWordUtil.getErrorTime(mActivity) <= 0) {
						callback(7);
						finish();
					} else {
						noSetPassword.setTextColor(Color.WHITE);
						Spanned data = Html
								.fromHtml("<font color=\"#fc7101\">密码错误，</font>您还可以输入"
										+ String.valueOf(5 - Integer
												.valueOf(errroNumber)) + "次");
						noSetPassword.setText(data);
						LocusPassWordUtil.setErrorTime(mActivity, errroNumber);
						lpwv.clearPassword();
					}
				}
			}
		});

		text_findpassword.setOnClickListener(this);
	}

	public void setData() {
		setTitle("系统登陆");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == PASSWORDERROR) {
			userLogin();
		}
	}

	@Override
	public void onClick(View v) {
		userLogin();
	}

	private void userLogin() {
		MineSetGesturePwdActivity.isForgetPassword = true;
		LocusPassWordUtil.setHandPassword(mActivity, false);
		lpwv.clearPassword();
		LocusPassWordUtil.clearPassWork(mActivity);
		LoginUtil.logout(mActivity, new ILogoutListener() {

			@Override
			public void onLogout() {
				callback(7);
				Intent intent = new Intent(mActivity, MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

	private void callback(int what) {
		if (page == 0)
			LoginUtilAnother.startHandler(what);
		else
			LoginUtil.startHandler(what);
	}
}
