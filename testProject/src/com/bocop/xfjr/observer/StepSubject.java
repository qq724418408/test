package com.bocop.xfjr.observer;

/**
 * description： 进度被观察者
 * <p/>
 * Created by TIAN FENG on 2017年8月28日
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */
public interface StepSubject {
	void register(StepObserver observer);
}
