package com.bocop.jxplatform.activity.way.pattern;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.base.BaseApplication;
import com.boc.jx.baseUtil.cache.CacheBean;
import com.boc.jx.constants.Constants;
import com.bocop.jxplatform.R;

//import cc.bxjr.bxapp.activity.LoginActivity;
import com.bocop.jxplatform.activity.MainActivity;
//import cc.bxjr.bxapp.activity.account.WayPwdActivity;
import com.bocop.jxplatform.activity.way.view.LockPatternUtils;
import com.bocop.jxplatform.activity.way.view.LockPatternView;
import com.bocop.jxplatform.fragment.PersonalFragment;
import com.bocop.jxplatform.util.LoginUtil;
import com.bocop.jxplatform.util.LoginUtil.ILogoutListener;

/**
 * 手势密码验证
 */
public class UnlockGesturePasswordActivity extends BaseActivity {
	private LockPatternView mLockPatternView;
	private int mFailedPatternAttemptsSinceLastTimeout = 0;
	private CountDownTimer mCountdownTimer = null;
	private Handler mHandler = new Handler();
	private TextView mHeadTextView;
	private Animation mShakeAnim;
	private Toast mToast;
	private String wayid, update;
	public TextView wjpwd;
	public TextView changeAccount;
	// private CircleImageView iv_head;
	private TextView tv_name;
	private Context context;
	private String username;
	private String usericon;

	private long exitTime = 0;

	private BaseApplication baseApp;

	private void showToast(CharSequence message) {
		if (null == mToast) {
			mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
			mToast.setGravity(Gravity.CENTER, 0, 0);
		} else {
			mToast.setText(message);
		}
		mToast.show();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gesturepassword_unlock);
		context = this;
		mLockPatternView = (LockPatternView) this
				.findViewById(R.id.gesturepwd_unlock_lockview);
		mLockPatternView.setOnPatternListener(mChooseNewLockPatternListener);
		mLockPatternView.setTactileFeedbackEnabled(true);
		mHeadTextView = (TextView) findViewById(R.id.gesturepwd_unlock_text);
		mShakeAnim = AnimationUtils.loadAnimation(this, R.anim.shake_x);

		// 获取从主页传过来的参数
		final Intent intent = this.getIntent();
		wayid = intent.getStringExtra("wayid");
		update = intent.getStringExtra("update");

		wjpwd = (TextView) this.findViewById(R.id.gesturepwd_unlock_forget);
		changeAccount = (TextView) this.findViewById(R.id.changeAccount);

		wjpwd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showToast("将退出登录，请重新设置手势密码");
				LoginUtil
						.logoutWithoutCallback(UnlockGesturePasswordActivity.this);
				BaseApplication.getInstance().getLockPatternUtils().clearLock();// 关闭手势密码
				finish();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!BaseApplication
				.getInstance()
				.getLockPatternUtils()
				.savedPatternExists(
						LoginUtil.getUserId(UnlockGesturePasswordActivity.this))) {
			if ("wayid".equals(wayid)) {
				// showToast("你已经退出登录了。");
				Intent intent = new Intent();
				intent.putExtra("fromLoginExit", true);
				intent.setClass(this, MainActivity.class);
				startActivity(intent);
				// 关闭Activity
				this.finish();
			} else {
				startActivity(new Intent(this,
						GuideGesturePasswordActivity.class));
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mCountdownTimer != null)
			mCountdownTimer.cancel();
	}

	private Runnable mClearPatternRunnable = new Runnable() {
		public void run() {
			mLockPatternView.clearPattern();
		}
	};

	protected LockPatternView.OnPatternListener mChooseNewLockPatternListener = new LockPatternView.OnPatternListener() {

		public void onPatternStart() {
			mLockPatternView.removeCallbacks(mClearPatternRunnable);
			patternInProgress();
		}

		public void onPatternCleared() {
			mLockPatternView.removeCallbacks(mClearPatternRunnable);
		}

		public void onPatternDetected(List<LockPatternView.Cell> pattern) {
			if (pattern == null)
				return;
			if (BaseApplication.getInstance().getLockPatternUtils()
					.checkPattern(pattern)) {
				mLockPatternView
						.setDisplayMode(LockPatternView.DisplayMode.Correct);
				if ("wayid".equals(wayid)) {
					showToast("解锁成功!");
					Constants.handFlg = true;
					finish();
				} else if ("update".equals(update)) {
					Intent intent = new Intent(
							UnlockGesturePasswordActivity.this,
							CreateGesturePasswordActivity.class);
					intent.putExtra("update", update);
					// 打开新的Activity
					startActivity(intent);
					finish();
				} else {
					Intent intent = new Intent(
							UnlockGesturePasswordActivity.this,
							GuideGesturePasswordActivity.class);
					// 打开新的Activity
					startActivity(intent);
					showToast("解锁成功!");
					Constants.handFlg = true;
					finish();
				}
			} else {
				mLockPatternView
						.setDisplayMode(LockPatternView.DisplayMode.Wrong);
				if (pattern.size() >= LockPatternUtils.MIN_PATTERN_REGISTER_FAIL) {
					mFailedPatternAttemptsSinceLastTimeout++;
					int retry = LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT
							- mFailedPatternAttemptsSinceLastTimeout;
					if (retry >= 0) {
						if (retry == 0)
							showToast("您已5次输错密码，将退出登录");
						mHeadTextView.setText("密码错误，还可以再输入" + retry + "次");
						mHeadTextView.setTextColor(Color.RED);
						mHeadTextView.startAnimation(mShakeAnim);
					}
				} else {
					showToast("输入长度不够，请重试");
				}

				if (mFailedPatternAttemptsSinceLastTimeout >= LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT) {
					LoginUtil
							.logoutWithoutCallback(UnlockGesturePasswordActivity.this);
					BaseApplication.getInstance().getLockPatternUtils()
							.clearLock();// 关闭手势密码
					finish();

					// mHandler.postDelayed(attemptLockout, 2000);
				} else {
					mLockPatternView.postDelayed(mClearPatternRunnable, 2000);
				}
			}
		}

		public void onPatternCellAdded(List<LockPatternView.Cell> pattern) {

		}

		private void patternInProgress() {
		}
	};
	Runnable attemptLockout = new Runnable() {
		@Override
		public void run() {
			mLockPatternView.clearPattern();
			mLockPatternView.setEnabled(false);
			mCountdownTimer = new CountDownTimer(
					LockPatternUtils.FAILED_ATTEMPT_TIMEOUT_MS + 1, 1000) {

				@Override
				public void onTick(long millisUntilFinished) {
					int secondsRemaining = (int) (millisUntilFinished / 1000) - 1;
					if (secondsRemaining > 0) {
						// mHeadTextView.setText(secondsRemaining + " 秒后重试");
						// 退出登录
					} else {
						mHeadTextView.setText("请输入手势密码");
						mHeadTextView.setTextColor(Color.WHITE);
					}

				}

				@Override
				public void onFinish() {
					mLockPatternView.setEnabled(true);
					mFailedPatternAttemptsSinceLastTimeout = 0;
				}
			}.start();
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 这里写你要在用户按下返回键同时执行的动作
			// moveTaskToBack(false); //核心代码：屏蔽返回行为
			// if ("wayid".equals(wayid)) {
			// BaseApplication.getInstance().exit();//退出应用程序
			// }
			// finish();
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(UnlockGesturePasswordActivity.this, "再按一次退出程序",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				BaseApplication.getInstance().exit();// 退出应用程序
				finish();
				 getBaseApp().exit();
				 System.exit(0);
				// CacheBean.getInstance().clearCacheMap();
				// getBaseApp().exit();
				// System.exit(0);
			}

			
		}

		return super.onKeyDown(keyCode, event);

	}

}
