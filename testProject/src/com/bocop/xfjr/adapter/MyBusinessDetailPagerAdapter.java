package com.bocop.xfjr.adapter;

import java.util.Map;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyBusinessDetailPagerAdapter extends FragmentPagerAdapter {
	
	private Map<Integer,Fragment> mFragments;

	public MyBusinessDetailPagerAdapter(FragmentManager fm,Map<Integer,Fragment> mFragments) {
		super(fm);
		this.mFragments = mFragments;
	}

	@Override
	public Fragment getItem(int position) {
		return  mFragments.get(position);
	}

	@Override
	public int getCount() {
		return mFragments.size();
	}

}