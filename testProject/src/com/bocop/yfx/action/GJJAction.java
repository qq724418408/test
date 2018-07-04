package com.bocop.yfx.action;

import org.json.JSONException;
import org.json.JSONObject;

import com.bocop.yfx.activity.FundQueryActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
/**
 * 公积金查询action
 * @author ftl
 *
 */
public class GJJAction {
	
	private  FundQueryActivity mActivity;
	
	public GJJAction(FundQueryActivity gmActivity){
		this.mActivity = gmActivity;
	}
	
	/**
	 * 获取用户信息
	 */
	public void getUserInfoCall(){
		mActivity.getUserInfoCall();
	}
	
	/**
	 * 结束当前页
	 */
	public void finishThis(){
		mActivity.finish();
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
}
