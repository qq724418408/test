package com.bocop.kht.utils;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.boc.jx.baseUtil.cache.CacheBean;
import com.boc.jx.constants.Constants;
import com.bocop.jxplatform.util.LoginUtil;
import com.bocop.xfjr.util.PreferencesUtil;

import android.content.Context;
import android.text.TextUtils;

public class InfoUtil {

	public static String getIdCard(Context context) {
		PreferencesUtil.init(context, Constants.CUSTOM_PREFERENCE_NAME);
		String idCard = PreferencesUtil.get(Constants.CUSTOM_ID_NO, "");
		return idCard;
	}

	public static String getCusId(Context context) {
		PreferencesUtil.init(context, Constants.CUSTOM_PREFERENCE_NAME);
		String cusId = PreferencesUtil.get(Constants.CUSTOM_CUS_ID, "");
		return cusId;
	}

	public static String updateInfo(Context context) {
		String idCardNumber = getIdCard(context);
		Map<String, String> hashMap = new HashMap<>();
		String info;
		if (TextUtils.isEmpty(idCardNumber)) {
			hashMap.put("state", "01");
			JSONObject jsonObject = new JSONObject(hashMap);
			info = jsonObject.toString();
		} else {
			String userId = LoginUtil.getUserId(context);
			String accessToken = LoginUtil.getToken(context);
			String cusId = getCusId(context);
			hashMap.put("state", "00");
			hashMap.put("client", "aos");
			hashMap.put("idCardNumber", idCardNumber);
			hashMap.put("userId", userId);
			hashMap.put("cusId", cusId);
			hashMap.put("accessToken", accessToken);
			JSONObject jsonObject = new JSONObject(hashMap);
			info = jsonObject.toString();
		}
		return info;
	}

}
