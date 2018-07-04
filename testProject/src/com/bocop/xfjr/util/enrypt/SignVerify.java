package com.bocop.xfjr.util.enrypt;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.boc.jx.httptools.network.http.DConfing;
import com.bocop.xfjr.util.PreferencesUtil;

import android.annotation.SuppressLint;

/**
 * 签名校验：sha1+时间
 * 
 * @author Administrator
 *
 */
public class SignVerify {
	
	public static String time;
	
	/**
	 * 格式-【sha1加密accessToken+日期时间】
	 * 
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String sha1Sign() {
		String accessToken = PreferencesUtil.get(DConfing.ACCESS_TOKEN, "");
		String publicKey = PreferencesUtil.get(DConfing.PUBLICK_KEY, "");
        accessToken = RSASecurity.decryptByPublic(accessToken, publicKey);
		StringBuffer wsb = new StringBuffer();
		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		time = sdf.format(dt);
		wsb.append(accessToken);
		wsb.append(time);
		String signVerify = wsb.toString();
		return SHA1.sha1(signVerify);
	}
}
