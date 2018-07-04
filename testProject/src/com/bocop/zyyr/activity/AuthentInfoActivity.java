package com.bocop.zyyr.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.ImageView;
import android.widget.TextView;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.base.BaseFragment;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.bocop.jxplatform.R;
import com.bocop.zyyr.bean.AuthenInfo;
import com.bocop.zyyr.bean.KeyAndValue;
import com.bocop.zyyr.fragment.InfoBaseFragment;
import com.bocop.zyyr.fragment.InfoCheckFragment;
import com.bocop.zyyr.fragment.InfoCompleteFragment;

@ContentView(R.layout.zyyr_activity_authent_info)
public class AuthentInfoActivity extends BaseActivity {
	@ViewInject(R.id.tv_titleName)
	private TextView tvTitle;
	@ViewInject(R.id.ivProgress)
	public ImageView ivProgress;

	public AuthenInfo authenInfos = new AuthenInfo();
	public List<KeyAndValue> creditData = new ArrayList<KeyAndValue>();// 信用数组
	private List<BaseFragment> fragmentList = new ArrayList<BaseFragment>();
	private FragmentManager fragmentManager;
	private int currentIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tvTitle.setText(getString(R.string.authentInfo));

		InfoBaseFragment infoBaseFragment = new InfoBaseFragment();
		InfoCompleteFragment infoCompleteFragment = new InfoCompleteFragment();
		InfoCheckFragment infoCheckFragment = new InfoCheckFragment();
		fragmentList.add(infoBaseFragment);
		fragmentList.add(infoCompleteFragment);
		fragmentList.add(infoCheckFragment);

		fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		for (int i = 0; i < fragmentList.size(); i++) {
			transaction.add(R.id.llFragment, fragmentList.get(i), fragmentList.get(i).getClass().getSimpleName());
		}
		transaction.commit();
		showFragment(0);
	}

	/**
	 * 显示指定Fragment，并隐藏其他Fragment
	 * 
	 * @param currentIndex
	 */
	public void showFragment(int currentIndex) {
		for (int i = 0; i < fragmentList.size(); i++) {
			BaseFragment fragment = fragmentList.get(i);
			if (!fragment.isHidden()) {
				fragmentManager.beginTransaction().hide(fragment).commit();
			}
		}
		fragmentManager.beginTransaction().show(fragmentList.get(currentIndex)).commit();
		this.currentIndex = currentIndex;
		if (0 == currentIndex) {
			ivProgress.setImageResource(R.drawable.zyyr_info1);
		} else if (1 == currentIndex) {
			ivProgress.setImageResource(R.drawable.zyyr_info2);

		} else if (2 == currentIndex) {
			ivProgress.setImageResource(R.drawable.zyyr_info3);

		}
	}

	@Override
	public void onBackPressed() {
		if (0 == currentIndex) {
			finish();
		} else if (1 == currentIndex) {
			showFragment(0);
		} else if (2 == currentIndex) {
			showFragment(1);
		}
	}
}
