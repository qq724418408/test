package com.bocop.xms.service;

import java.util.List;

import com.bocop.jxplatform.util.LoginUtil;
import com.bocop.xms.utils.SharedPreferencesUtils;
import com.bocop.xms.xml.remind.EventBean;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

public class AlarmServiceManager {
	
	private static AlarmServiceManager serviceManager;
	
	public static AlarmServiceManager getInstance() {
		if (serviceManager == null) {
			serviceManager = new AlarmServiceManager();
		}
		return serviceManager;
	}
	
	/**
	 * 开启闹铃服务
	 */
	@SuppressWarnings("unchecked")
	public void startAlarmService(Context context) {
		String userId = LoginUtil.getUserId(context);
		if (!TextUtils.isEmpty(userId)) {
			SharedPreferencesUtils spf = new SharedPreferencesUtils(context, AlarmService.ALARM_SER);
			List<EventBean> list = (List<EventBean>) spf.getObject(userId, EventBean.class);
			if (list != null) {
				Intent service = new Intent();
				service.setClass(context, AlarmService.class);
				context.startService(service);
			}
		}
	}

}
