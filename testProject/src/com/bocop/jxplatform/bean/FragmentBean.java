package com.bocop.jxplatform.bean;

import java.io.Serializable;

import android.support.v4.app.Fragment;

/**
 * Fragment封装类
 * 
 * @author kejia
 *
 */
public class FragmentBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Fragment fragment;// Fragment
	private String tag;// 标签
	private String title;// 标题

	public FragmentBean(Fragment fragment, String title) {
		this.fragment = fragment;
		this.tag = fragment.getClass().getSimpleName();
		this.title = title;
	}
	
	public Fragment getFragment() {
		return fragment;
	}

	public void setFragment(Fragment fragment) {
		this.fragment = fragment;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
