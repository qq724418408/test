package com.bocop.jxplatform.adapter;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.base.BaseFragment;
import com.bocop.jxplatform.fragment.HomeFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * 
 * @author Hxiaobin 14/11/05
 *
 */
public class IconPagerAdapter extends FragmentPagerAdapter {

	private Class[] fragments;
	private FragmentManager manager;
	private BaseActivity activity;
	private HomeFragment homeFragment;

	public IconPagerAdapter(FragmentActivity activity, Class[] fragments) {
		super(activity.getSupportFragmentManager());
		this.activity = (BaseActivity)activity;
		manager = activity.getSupportFragmentManager();
		this.fragments = fragments;
	}

	@Override
	public Fragment getItem(int position) {
		BaseFragment fragment = null;
		try {
			fragment = (BaseFragment) manager.findFragmentById(position);
			if(fragment == null){
				fragment = (BaseFragment)(fragments[position].newInstance());
				activity.getBaseApp().getFragmentList().put(position, fragment);
			}
			if (fragment instanceof HomeFragment) {
				homeFragment = (HomeFragment) fragment;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fragment;
	}

	@Override
	public int getCount() {
		return fragments.length;
	}

	public HomeFragment getHomeFragment() {
		return homeFragment;
	}
}