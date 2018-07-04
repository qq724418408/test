package com.bocop.user.login;

public class DataKeeper {

	private static DataKeeper mKeeper;

	public static DataKeeper getInstance() {
		if (mKeeper == null) {
			mKeeper = new DataKeeper();
		}
		return mKeeper;
	}
	private static LoginInfo sLoginInfo;

	public boolean isLogin() {
		return sLoginInfo != null && sLoginInfo.access_token != null && !sLoginInfo.access_token.isEmpty();
	}

	public String getToken() {
		return sLoginInfo == null ? "" : sLoginInfo.access_token;
	}

	public String getUserid() {
		return sLoginInfo == null ? "" : sLoginInfo.userid;
	}

	public String getClientid() {
		return sLoginInfo == null ? "" : sLoginInfo.client_id;
	}

	public void setLoginInfo(LoginInfo info) {
		sLoginInfo = info;
	}

	public void clearLoginInfo() {
		sLoginInfo = null;
	}
}
