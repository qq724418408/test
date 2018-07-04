package com.bocop.xfjr.observer;

import android.view.View;

/**
 * description： 保存按钮的状态改变 观察者
 * <p/>
 * Created by TIAN FENG on 2017年8月30日
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */
public interface IFragmentState {
	
	
	/**
	 * 重置按钮的点击
	 */
	void onResetClick(View view);
	
	/**
	 * 保存按钮的点击
	 */
	void onSaveClick(View view);
	
	/**
	 * 物理返回键
	 */
	boolean onBackClick();
	
	/**
	 * 错误页面点击
	 */
	void errorClick();
	
}
