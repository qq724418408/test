package com.boc.jx.base.bean;

import com.boc.jx.base.BaseActivity;

/**
 * 存储对象
 * @author llc
 *
 */
public class AcitivtyBean {
	
	private String activityName;
	
	private BaseActivity activity;
	
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	public BaseActivity getActivity() {
		return activity;
	}
	public void setActivity(BaseActivity activity) {
		this.activity = activity;
	}
	
	
}
