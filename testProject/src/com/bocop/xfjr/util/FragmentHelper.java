package com.bocop.xfjr.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * description： Fragment 返回栈管理
 * <p/>
 * Created by TIAN FENG on 2017年8月28日
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */
public class FragmentHelper {
	
	private FragmentManager mManager;
	private int mContainerViewId;
	
	public FragmentHelper(FragmentManager manager, int containerViewId) {
		super();
		this.mManager = manager;
		this.mContainerViewId = containerViewId;
	}
	
	public void addToBackStack(Fragment fragment){
		FragmentTransaction transaction = mManager.beginTransaction();
		transaction.add(mContainerViewId, fragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}
	
	public void popToBackStack(){
		mManager.popBackStack();
	}
	
}
