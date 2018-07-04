package com.boc.jx.baseUtil.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求签名校验-2016年11月3日17:05:21
 * @author qinwenqiang
 * 用于安全URL调用
 */
public class SignRequestVerify {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.out.println("签名检验："+SignRequestVerify.md5SignRequestVerify("13707887274", "yht", "aa37228#$%@@11!!@"));

	}
	
	//请求签名校验
	public static String md5SignRequestVerify(String openId,String authApp,String key) {
		
		//格式-【应用编码+日期时间+openId+密钥】
		
		//1.利用目前分到的authApp，这个统一使用authApp---yht;
		//2.利用目前分到的密钥，这个统一使用
		
	    StringBuffer wsb = new StringBuffer();
	    Date dt = new Date();
	    //SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm"); 可以把时间调至分钟来校验
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
	    String sDate = sdf.format(dt);
	    wsb.append(authApp);
	    wsb.append(sDate);
	    wsb.append(openId);
	    wsb.append(key);
	    
		String signVerify = Md5Encrypt.MD55(wsb.toString());
		
		return signVerify;
		
	}

}
