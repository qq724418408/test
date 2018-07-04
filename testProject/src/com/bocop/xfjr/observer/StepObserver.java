package com.bocop.xfjr.observer;

/**
 * description： 进度观察者
 * <p/>
 * Created by TIAN FENG on 2017年8月28日
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */
public interface StepObserver {
	
	/**
	 * 将下一个fragment push到返回栈
	 */
	void pushBackStack();
	
	/**
	 * 将栈顶的fragment pop出返回栈
	 */
	void popBackStack();
}
