package com.bocop.jxplatform.adapter;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.base.BaseFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyPeccancyAdapter extends FragmentPagerAdapter
{
	private Class[] fragments;
	private FragmentManager manager;
	private BaseActivity activity;

	public static String[] TITLES = new String[]
//	{ "处理中"};
	{ "处理成功","处理中", "处理失败"};

	public MyPeccancyAdapter(FragmentActivity activity, Class fragments[]) {
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fragment;
	}

	@Override
	public int getCount()
	{
		return TITLES.length;
	}

	@Override
	public CharSequence getPageTitle(int position)
	{
		return TITLES[position];
	}

}
