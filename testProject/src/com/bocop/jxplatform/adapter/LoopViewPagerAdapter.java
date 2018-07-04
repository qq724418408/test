package com.bocop.jxplatform.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

/**
 * 循环ViewPager适配器
 * 
 * @author Hxiaobin 14/11/05
 * 
 */
public class LoopViewPagerAdapter extends PagerAdapter {

	private List<View> views;

	public LoopViewPagerAdapter(List<View> views) {
		this.views = views;
	}

	@Override
	public int getCount() {
		return views.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// 循环ViewPager：需注释，否则拖动切换时会出现黑色区域
		// ((ViewPager)container).removeView(views.get(position));
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		try {
			((ViewPager) container).addView(views.get(position));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return views.get(position);
	}

}
