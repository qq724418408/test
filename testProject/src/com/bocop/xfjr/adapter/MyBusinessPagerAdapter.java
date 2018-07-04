package com.bocop.xfjr.adapter;

import java.util.List;

import com.bocop.xfjr.bean.TypeBean;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyBusinessPagerAdapter extends FragmentPagerAdapter {
	
	private List<TypeBean> tabTitles = null;
	private List<Fragment> fragments;

	public MyBusinessPagerAdapter(FragmentManager fm, List<TypeBean> arrTabTitles, List<Fragment> fragments) {
		super(fm);
		this.tabTitles = arrTabTitles;
		this.fragments = fragments;
	}

	@Override
	public Fragment getItem(int position) {
		return  fragments.get(position);
	}

	@Override
	public int getCount() {
		return tabTitles.size();
	}

}