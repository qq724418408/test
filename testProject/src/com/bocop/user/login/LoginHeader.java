package com.bocop.user.login;

public class LoginHeader {
	public String clentid;
	public String userid;
	public String cookie;
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "clentid" + clentid + ",userid" + userid + ",cookie" + cookie;
	}
}
