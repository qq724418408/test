package com.bocop.jxplatform.wxapi;

import java.util.HashMap;

import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import android.R.string;
import android.app.Activity;
import android.content.Context;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.statistics.NewAppReceiver;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * 获取微信授权登录的openId
 * @author formssi
 *
 */
public class WeChatOpenID  {
	private static final String BASE_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?";
	public static final String APP_ID = "wx1559c98f3806f393";
	private static final String APP_SECRET = "9a6a1fd9ad771accada91dfb564712e3";
	public static IWXAPI mWxApi;
	public  static void registToWX(Context context) {
		mWxApi = WXAPIFactory.createWXAPI(context, APP_ID,true);
		mWxApi.registerApp(APP_ID);
	}
	public static void weChatLogin(Context context) {
		if (!mWxApi.isWXAppInstalled()) {
	        Toast.makeText(context, "您还未安装微信客户端", Toast.LENGTH_SHORT).show();
	        return;
	    }
	    final SendAuth.Req req = new SendAuth.Req();
	    req.scope = "snsapi_userinfo";
	    req.state = "none";
	    mWxApi.sendReq(req);
	}
	public static String getAccessTokenUrl(String code) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(BASE_URL);
		stringBuilder.append("appid=").append(APP_ID);
		stringBuilder.append("&secret=").append(APP_SECRET);
		stringBuilder.append("&code=").append(code);
		stringBuilder.append("&grant_type=authorization_code");
		return stringBuilder.toString();
	}
}
