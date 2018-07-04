package com.bocop.jxplatform.action;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;

import com.boc.jx.constants.Constants;
import com.bocop.cft.activity.CftActivity;
import com.bocop.jxplatform.activity.WebActivity;
import com.bocop.jxplatform.activity.WebTitleActivity;
import com.bocop.jxplatform.activity.riders.ETCHighWayActivity;
import com.bocop.jxplatform.activity.riders.RiderFristActivity;
import com.bocop.jxplatform.util.LoginUtil;
import com.bocop.jxplatform.util.LoginUtil.ILoginListener;
/**
 * 车惠通action
 * @author hooyangr
 *
 */
public class CHTAction{
	
	private  RiderFristActivity mActivity;
	
	public CHTAction(RiderFristActivity gmActivity){
		this.mActivity = gmActivity;
	}
	
	/**
	 * 获取用户信息
	 */
	public void getUserInfoCall(){
		mActivity.getUserInfoCall();
	}
	
	/**
	 *切换横竖屏
	 */
	public void screenDirection(String param){
		JSONObject object;
		try {
			object = new JSONObject(param);
			mActivity.screenDirection(object.getString("param"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@SuppressLint("CommitPrefEdits")
	public void setTipsFlag(String flag) {
		//保存flag到本地, 下次调用取出
		try {
			JSONObject object = new JSONObject(flag);
			SharedPreferences sp = mActivity.getSharedPreferences("tipsFlag", Context.MODE_PRIVATE);
			Editor editor = sp.edit();
			editor.putString("flagKey", object.getString("param"));
			editor.commit();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void finishThis(){
		mActivity.finish();
	}
	
	/**
	 * 违章处理
	 */
	public void chtwzcl(){
		mActivity.skipControl(RiderFristActivity.LOGIN_RESPONSE_WZCL);
	}
	
	/**
	 * 特惠洗车
	 */
	public void chtthxc(){
		mActivity.skipControl(RiderFristActivity.LOGIN_RESPONSE_THXC);
	}
	
	/**
	 * 加油充值
	 */
	public void chtjycz(){
		mActivity.skipControl(RiderFristActivity.LOGIN_RESPONSE_JYCZ);
	}
	
	/**
	 * 新手学车
	 */
	public void chtxsxc(){
		mActivity.skipControl(RiderFristActivity.LOGIN_RESPONSE_SXC);
	}
	
	/**
	 * 贴息购车
	 */
	public void chttxgc(){
		Bundle bundleConsult = new Bundle();
		bundleConsult.putString("url", Constants.chtUrlForTxgc);
		bundleConsult.putString("name", "贴息购车");
		Intent intentConsult = new Intent(mActivity, WebActivity.class);
		intentConsult.putExtras(bundleConsult);
		mActivity.startActivity(intentConsult);
	}
	
	/**
	 * 高速etc
	 */
	public void chtgsetc(){
		Intent intentETC = new Intent(mActivity, ETCHighWayActivity.class);
		mActivity.startActivity(intentETC);
	}
	
	/**
	 * 车险投保
	 */
	public void chtcxtb(){
		Bundle bundleConsult = new Bundle();
		bundleConsult.putString("url", Constants.chtUrlForCxtb);
		bundleConsult.putString("name", "中银车险");
		Intent intentConsult = new Intent(mActivity, WebTitleActivity.class);
		intentConsult.putExtras(bundleConsult);
		mActivity.startActivity(intentConsult);
	}
	
	/**
	 * 团购活动
	 */
	public void chttghd(){
		Bundle bundleConsult = new Bundle();
		bundleConsult.putString("url", Constants.chtUrlForTghd);
		bundleConsult.putString("name", "团购活动");
		Intent intentConsult = new Intent(mActivity, RiderFristActivity.class);
		intentConsult.putExtras(bundleConsult);
		mActivity.startActivity(intentConsult);
	}
	
	/**
	 * 我的车子
	 */
	public void chtwdcz(){
		mActivity.skipControl(RiderFristActivity.LOGIN_RESPONSE_WDCZ);
	}
	
	/**
	 * 个人信息
	 */
	public void chtgrxx(){
		mActivity.skipControl(RiderFristActivity.LOGIN_RESPONSE_GRXX);
	}
	
	/**
	 * 车贷测算
	 */
	public void chtcdcs(){
		Bundle bundleConsult = new Bundle();
		bundleConsult.putString("url", Constants.chtUrlForCdcs);
		bundleConsult.putString("name", "车贷测算");
		Intent intentConsult = new Intent(mActivity, RiderFristActivity.class);
		intentConsult.putExtras(bundleConsult);
		mActivity.startActivity(intentConsult);
	}
	
	/**
	 * 分享
	 * @param param
	 */
	public void shareCall(String param){
		mActivity.doShare(param);
	}

}
