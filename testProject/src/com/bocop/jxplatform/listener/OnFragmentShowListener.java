package com.bocop.jxplatform.listener;

/**
 * 当ViewPager+Fragment时，切换显示Fragment调用
 * @author user
 *
 */
public interface OnFragmentShowListener {
	/**
	 * 切换时调用showFragment方法
	 * @param position 当前所属ViewPager位置
	 */
	public void showFragment(int position);
}
