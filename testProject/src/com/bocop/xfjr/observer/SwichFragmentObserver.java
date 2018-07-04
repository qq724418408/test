package com.bocop.xfjr.observer;

/**
 * description： 点击下一步的观察者
 * <p/>
 * Created by TIAN FENG on 2017年8月24日
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */
public interface SwichFragmentObserver {
	
	/**
	 * 点击后位置切换
	 * @param position 位置 -1时返回
	 */
	void onSwichChange(int position);
}
