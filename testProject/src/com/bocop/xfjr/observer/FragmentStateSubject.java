package com.bocop.xfjr.observer;

/**
 * description： 保存按钮的状态改变  被观察者
 * <p/>
 * Created by TIAN FENG on 2017年8月30日
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */
public interface FragmentStateSubject {
	
	void registObserver(IFragmentState observer);
}
