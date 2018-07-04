package com.bocop.gm.action;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.bocop.gm.GoldManagerActivity;

public class GMAction {
	
	private  GoldManagerActivity mActivity;
	
	
	public GMAction(GoldManagerActivity gmActivity){
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
	
	public void getContactsInfo(String param){
//		mActivity.getContactsInfo();
		JSONObject object;
		try {
			object = new JSONObject(param);
			mActivity.getContactsInfo(object.getInt("param"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void shareCall(){
		mActivity.doShare();
	}
	
	
	public void finishThis(){
		mActivity.finish();
	}
}
