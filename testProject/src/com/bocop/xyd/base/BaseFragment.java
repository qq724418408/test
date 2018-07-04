package com.bocop.xyd.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import com.boc.jx.base.BaseApplication;
import com.boc.jx.baseUtil.cache.CacheBean;
import com.boc.jx.baseUtil.view.ViewUtils;
import com.boc.jx.tools.LogUtils;

/**
 * Created by user on 14-9-11.
 */
public abstract class BaseFragment extends Fragment {
	public View mRootView;// 视图

	public ArrayList<Fragment> mFragments = new ArrayList<Fragment>();

	public Context mContext;
	private boolean isPrepare;
	protected boolean isVisible;
	private boolean isFirst = true;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	/*
	 * @Override public void setUserVisibleHint(boolean isVisibleToUser) {
	 * super.setUserVisibleHint(isVisibleToUser); if (getUserVisibleHint()){
	 * isVisible = true; lazyLoad(); }else { isVisible = false; onInVisible(); }
	 * }
	 */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
		setHasOptionsMenu(true);
		mFragments.add(this);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (mRootView == null) {
			mRootView = inflater.inflate(getLayout(), container, false);

		}
		ViewGroup parent = (ViewGroup) mRootView.getParent();
		if (parent != null) {
			parent.removeView(mRootView);
		}

		return mRootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ViewUtils.inject(this, mRootView);
		initView();
		initEvent();
		initData();
		/*
		 * isPrepare = true; lazyLoad();
		 */
	}

	protected void lazyLoad() {
		if (!isPrepare || !isVisible || !isFirst) {
			return;
		}
		initData();
		isFirst = false;
	}

	/* 不可见时 */
	protected void onInVisible() {

	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	public void startActivity(Class<? extends Activity> target) {
		Intent intent = new Intent(getActivity(), target);
		startActivity(intent);
	}

	public void startActivityNoAnim(Class<? extends Activity> target) {
		Intent intent = new Intent(getActivity(), target);
		startActivity(intent);
		getActivity().overridePendingTransition(0, 0);
	}

	public void closeActivity() {
		getActivity().finish();
	}

	public void closeActivityNoAnim() {
		getActivity().finish();
		getActivity().overridePendingTransition(0, 0);
	}

	public void addFragmentTo(FragmentManager manager, int containerId, Fragment fragment) {
		manager.beginTransaction().add(containerId, fragment).commit();
	}

	public void replaceTo(FragmentManager manager, int container, Fragment fragment) {
		manager.beginTransaction().replace(container, fragment).commit();

	}

	/**
	 * 返回布局id
	 */
	public abstract int getLayout();

	/**
	 * 数据初始化
	 */
	public abstract void initData();

	/**
	 * 视图初始化
	 */
	public abstract void initView();

	/**
	 * 事件初始化
	 */
	public abstract void initEvent();
}
