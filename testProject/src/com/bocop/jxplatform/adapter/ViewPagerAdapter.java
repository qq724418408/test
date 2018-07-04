package com.bocop.jxplatform.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * ViewPager数据适配器
 * 
 * @author hkj
 * 
 */
public class ViewPagerAdapter extends PagerAdapter {

	private List<View> views = new ArrayList<View>();

	public ViewPagerAdapter(List<View> list) {
		this.views = list;
	}

	@Override
	public void destroyItem(View container, int position, Object object) {
		((ViewPager) container).removeView(views.get(position));
	}

	@Override
	public int getCount() {
		return views.size();
	}

	@Override
	public Object instantiateItem(View container, int position) {
		((ViewPager) container).addView(views.get(position), 0);
		return views.get(position);
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

}
