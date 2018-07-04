package com.bocop.jxplatform.activity;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.base.BaseApplication;
import com.boc.jx.baseUtil.cache.CacheBean;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.boc.jx.tools.LogUtils;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.adapter.IconPagerAdapter;
import com.bocop.jxplatform.fragment.HomeFragment;
import com.bocop.jxplatform.fragment.PersonalFragment;
import com.bocop.jxplatform.util.LoginUtil;
import com.bocop.jxplatform.util.LoginUtil.ILoginListener;
import com.bocop.jxplatform.util.LoginUtil.ILogoutListener;
import com.bocop.jxplatform.view.BackButton;
import com.bocop.jxplatform.view.TabButton;
import com.bocop.jxplatform.view.TabLayout;
import com.bocop.jxplatform.view.riders.NoScrollViewPager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity implements ILoginListener, ILogoutListener {

	@ViewInject(R.id.tv_titleName)
	private TextView tv_titleName;
	@ViewInject(R.id.iv_imageLeft)
	private BackButton backBtn;

	@ViewInject(R.id.tv_Item)
	private static TextView tvItemState;

	@ViewInject(R.id.btn_home)
	private TabButton btn_home;
	@ViewInject(R.id.btn_activity)
	private TabButton btn_activity;
	@ViewInject(R.id.btn_personal)
	private TabButton btn_personal;
	@ViewInject(R.id.vpFragment)
	private NoScrollViewPager vpFragment;

	private TabLayout tabLayout;

	private long exitTime = 0;

	// public String func[] = { "首页", "活动", "个人"};
	public String func[] = { "首页", "个人" };
	// private Class fragmentArray[] = { HomeFragment.class,
	// ActivityFragment.class, PersonalFragment.class};
	private Class fragmentArray[] = { HomeFragment.class, PersonalFragment.class };
	private static IconPagerAdapter pageAdapter;
	public static int as = 0;

	private static SharedPreferences.Editor editor;
	public BaseApplication baseApplication = BaseApplication.getInstance();
	protected BaseActivity baseActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		init();
		// login();
		// initToken();
	}

	@OnClick(value = { R.id.tv_Item })
	public void loginClick(View view) {
//		callMe(XFJRApplicationActivity.class); // TODO 测试代码
		if (LoginUtil.isLog(this)) {
			LoginUtil.showLogoutAppDialog(this, this);
		} else {
			LoginUtil.authorize(MainActivity.this, MainActivity.this);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.boc.jx.base.BaseActivity#onResume()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (LoginUtil.isLog(this)) {
			tvItemState.setText("已登录");
		} else {
			tvItemState.setText("未登录");
		}
	}

	public static class LoginReceive extends BroadcastReceiver {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.content.BroadcastReceiver#onReceive(android.content.Context,
		 * android.content.Intent)
		 */
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String loginStatus = intent.getExtras().getString("loginStatus");
			Log.i("Recevier1", "接收到:" + loginStatus);
			HomeFragment homeFragment = pageAdapter.getHomeFragment();
			if (loginStatus.equals("loginOn")) {
				tvItemState.setText("已登录");
				if (homeFragment != null) {
					homeFragment.requestMessagePre();
				}
			} else {
				tvItemState.setText("未登录");
				if (homeFragment != null) {
					homeFragment.hiddenMessage();
					homeFragment.requestMessagePre();
				}
			}

		}

	}

	// public void initToken() {
	// Oauth2AccessToken token = AccessTokenKeeper.readAccessToken(this);
	// if(token != null && token.isSessionValid()) {
	// LoginUserInfo userInfo = new LoginUserInfo();
	// userInfo.setUserId(token.getUserId());
	// userInfo.setAccessToken(token.getToken());
	// getBaseApp().setUserInfo(userInfo);
	// }
	// }

	// *******************yuxinhan4-5
	// public void login() {
	// baseActivity = (BaseActivity) MainActivity.this;
	// BaseApplication application = (BaseApplication) baseActivity
	// .getApplication();
	// if (!LoginUtil.isLog(baseActivity) && !getTel()) {
	// Intent intent = new Intent(baseActivity, InformalLoginActivity.class);
	// startActivity(intent);
	// }
	//
	// }

	// public boolean getTel (){
	// SharedPreferences sp = this.getSharedPreferences(LoginUtil.SP_NAME,
	// Context.MODE_PRIVATE);
	// editor = sp.edit();
	// String userTel = sp.getString(CacheBean.USER_TEL_LOGIN, "");
	// if (userTel != null && !"".equals(userTel)) {
	// return true;
	// }
	// return false;
	// }

	private void init() {
		// 初始化Tab项
		// tabLayout = new TabLayout(R.color.white,
		// R.color.white).addBtn(btn_home, btn_activity,
		// btn_personal);
		tabLayout = new TabLayout(R.color.white, R.color.white).addBtn(btn_home, btn_personal);
		btn_home.init(R.drawable.sy_default, R.drawable.sy_select, "首页", true, this);
		// btn_activity.init(R.drawable.hd_default, R.drawable.hd_select, "活动",
		// false,
		// this);
		btn_personal.init(R.drawable.user_default, R.drawable.user_select, "个人", false, this);

		pageAdapter = new IconPagerAdapter(this, fragmentArray);
		vpFragment.setAdapter(pageAdapter);
		;
		// 设置初始Tap
		tabLayout.selectBtn(R.id.btn_home);
		tv_titleName.setText("首页");
		backBtn.setVisibility(View.GONE);

		// if(LoginUtil.isLog(this)){
		// tvItemState.setText("已登陆");
		// }else{
		// tvItemState.setText("未登陆");
		// }

		vpFragment.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				LogUtils.i("setOnPageChangeListener：" + arg0);
				setSelectButton(arg0);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

		btn_home.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				vpFragment.setCurrentItem(0, false);
			}
		});
		// btn_activity.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// vpFragment.setCurrentItem(1, false);
		// }
		// });
		btn_personal.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				vpFragment.setCurrentItem(1, false);
			}
		});
	}

	// public void setSelectButton(int index) {
	// switch (index) {
	// case 0:
	// tabLayout.selectBtn(R.id.btn_home);
	// tv_titleName.setText("首页");
	// break;
	// case 1:
	// tabLayout.selectBtn(R.id.btn_activity);
	// tv_titleName.setText("活动");
	// break;
	// case 2:
	// tabLayout.selectBtn(R.id.btn_personal);
	// tv_titleName.setText("个人");
	// break;
	//
	// }
	// // fragmentList.get(index).onSelected();
	// }
	public void setSelectButton(int index) {
		switch (index) {
		case 0:
			tabLayout.selectBtn(R.id.btn_home);
			tv_titleName.setText("首页");
			as = 0;
			break;
		case 1:
			tabLayout.selectBtn(R.id.btn_personal);
			tv_titleName.setText("个人");
			as = 1;
			break;

		}
		// fragmentList.get(index).onSelected();
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
				exitApp();
			}
			return true;
		}
		return super.dispatchKeyEvent(event);
	}

	private void exitApp() {
		if ((System.currentTimeMillis() - exitTime) > 2000) {
			Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_LONG).show();
			exitTime = System.currentTimeMillis();
		} else {

			try {
				CacheBean.getInstance().clearCacheMap();
				// Log.i("tag", "logoutWithoutCallback");
				// LoginUtil.logoutWithoutCallback(MainActivity.this);
				finish();
				getBaseApp().exit();
				// System.exit(0);
			} catch (Exception ex) {
				Log.i("tag", ex.getMessage().toString());
			}

		}

	}

	// @Override
	// public void onBackPressed() {
	//
	// BocopDialog dialog = new BocopDialog(this, "提示", "确定要退出应用吗？");
	// dialog.setPositiveListener(new DialogInterface.OnClickListener() {
	//
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	// // TODO Auto-generated method stub
	// CacheBean.getInstance().clearCacheMap();
	// getBaseApp().exit();
	// dialog.dismiss();
	// }
	// });
	// dialog.setNegativeButton(new DialogInterface.OnClickListener() {
	//
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	// // TODO Auto-generated method stub
	// dialog.dismiss();
	// }
	// }, "取消");
	// dialog.show();
	// }
	// DialogUtil.showWithTwoBtn(this, "提示", "确定退出应用吗？", "确定", "取消", new
	// DialogInterface.OnClickListener() {
	//
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	// getBaseApp().exit();
	// }
	// }, new DialogInterface.OnClickListener() {
	//
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	// dialog.dismiss();
	// }
	// });
	// }

	@Override
	public void onLogout() {
		// TODO Auto-generated method stub
		Toast.makeText(this, "退出登录", Toast.LENGTH_SHORT).show();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bocop.jxplatform.util.LoginUtil.ILoginListener#onLogin()
	 */
	@Override
	public void onLogin() {
		// TODO Auto-generated method stub
		Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bocop.jxplatform.util.LoginUtil.ILoginListener#onCancle()
	 */
	@Override
	public void onCancle() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bocop.jxplatform.util.LoginUtil.ILoginListener#onError()
	 */
	@Override
	public void onError() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bocop.jxplatform.util.LoginUtil.ILoginListener#onException()
	 */
	@Override
	public void onException() {
		// TODO Auto-generated method stub

	}

}
