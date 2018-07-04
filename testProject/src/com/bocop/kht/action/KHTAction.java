package com.bocop.kht.action;

import android.R.integer;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.boc.jx.tools.LogUtils;
import com.bocop.gm.utils.HybridCallBack;
import com.bocop.kht.activity.KhtActivity;
import com.bocop.kht.intercept.KhtHybridUtil;
import com.bocop.xfjr.bean.FaceIdCardBean;
import com.bocop.xfjr.bean.FaceIdCardBean.BirthdayBean;

public class KHTAction {

	private KhtActivity mainActivity;

	private HybridCallBack callBack = new HybridCallBack() {

		@Override
		public void errorMsg(Exception e) {
			LogUtils.e(e.getMessage());
		}
	};

	public KHTAction(KhtActivity mainActivity) {
		this.mainActivity = mainActivity;
	}

	public void fileUpload_getImageFromPhone(String js) {
		if (mainActivity != null) {
			mainActivity.fileUpload_getImageFromPhone(js);
		}
	}

	public void uploadPic(WebView wvWebView, String imgBase64, String state, String imgExtension,
			FaceIdCardBean result) {
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("method", "fileUpload_getImageFromPhone");
		hashMap.put("state", state);
		hashMap.put("errorMessage", "");
		hashMap.put("imgBase64", imgBase64);
		hashMap.put("imgExtension", imgExtension);
		Map<String, String> userMap = null;
		if (result != null) {
			userMap = new HashMap<>();
			if ("front".equals(result.getSide())) {
				userMap.put("name", result.getName());
				userMap.put("gender", result.getGender());
				userMap.put("race", result.getRace());
				BirthdayBean birthday = result.getBirthday();
				String bir = formatBirthDay(birthday.getYear(), Integer.parseInt(birthday.getMonth()),
						Integer.parseInt(birthday.getDay()));
				userMap.put("birthday", bir);
				userMap.put("address", result.getAddress());
				userMap.put("idCardNumber", result.getId_card_number());
			} else if ("back".equals(result.getSide())) {
				// 反面
				userMap.put("issuedBy", result.getIssued_by());
				userMap.put("validDate", result.getValid_date());
			} else {
				// 活体检测
				if (!TextUtils.isEmpty(result.getConfidence())) {
					userMap.put("confidence", result.getConfidence());
				}
				if (!TextUtils.isEmpty(result.getReason())) {
					userMap.put("reason", result.getReason());
				}
			}
		}
		if (userMap != null) {
			hashMap.put("returnInfo", userMap);
		}
		KhtHybridUtil.getInstance().handleJsRequest(wvWebView, "fsNativeCallBack", hashMap, callBack);
//		hashMap.put("imgBase64", "");
//		org.json.JSONObject object = new org.json.JSONObject(hashMap);
//		String t1 = object.toString();
//		Log.d("t1", t1);
	}

	public void getLoginViewResultCall() {
		this.mainActivity.getLoginViewResultCall();
	}

	public String formatBirthDay(String year, int month, int day) {
		return year + String.format("%02d", month) + String.format("%02d", day);
	}

}
