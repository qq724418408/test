package com.bocop.cft.action;

import java.util.HashMap;

import com.boc.jx.tools.LogUtils;
import com.bocop.cft.activity.CftActivity;
import com.bocop.gm.utils.HybridCallBack;
import com.bocop.gm.utils.HybridUtil;
import com.bocop.jxplatform.util.LoginUtil;

import android.webkit.WebView;

public class CftAction {
	private CftActivity mActivity;

	public CftAction(CftActivity cftActivity) {
		this.mActivity = cftActivity;
	}

	private HybridCallBack callBack = new HybridCallBack() {

		@Override
		public void errorMsg(Exception e) {
			LogUtils.e(e.getMessage());
		}
	};

	public void getLoginViewResultCall() {
		mActivity.upLoadMessage();
	}

	public void uploadMessage(WebView wvWebView, String userId) {
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("userId", userId);
		HybridUtil.getInstance().handleJsRequest(wvWebView, "getLoginViewResultCall", hashMap, callBack);
	}
}
