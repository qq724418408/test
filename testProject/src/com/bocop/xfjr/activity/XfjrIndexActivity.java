package com.bocop.xfjr.activity;

import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.httptools.network.Executors;
import com.bocop.jxplatform.R;
import com.bocop.xfjr.argument.ArgumentUtil;
import com.bocop.xfjr.base.XfjrBaseActivity;
import com.bocop.xfjr.bean.login.LoginBean;
import com.bocop.xfjr.constant.XFJRConstant;
import com.bocop.xfjr.helper.index.IIndexHelper;
import com.bocop.xfjr.helper.index.XfjrIndexManagerHelper;
import com.bocop.xfjr.helper.index.XfjrIndexMerchantHelper;
import com.bocop.xfjr.util.PreferencesUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;

/**
 * 首页
 * 
 * BY TIAN FENG
 */
@ContentView(R.layout.xfjr_activity_index)
public class XfjrIndexActivity extends XfjrBaseActivity {

	private static LoginBean bean;

	/**
	 * 跳转此页面
	 * 
	 * @param context
	 *            上下文
	 * @param type
	 *            客户类型 1 ->客户经理 0->商户
	 */
	public static void goMe(Context context, LoginBean loginData) {
		Intent intent = new Intent(context, XfjrIndexActivity.class);
		intent.putExtra("TYPE_", loginData);
		bean = loginData;
		context.startActivity(intent);
	}

	/************************************************************************************************/

	// 辅助类
	private IIndexHelper mIndexHelper;
	// 客户经理
	@ViewInject(R.id.viewstubManager)
	private ViewStub mManagerStub;
	// 商户
	@ViewInject(R.id.viewstubCustom)
	private ViewStub mMerchantStub;
	// 客户类型
	private LoginBean loginData = null;
	// 某个界面的 type
	private String mType=XFJRConstant.C_ROLE;

	private View mManagerView, mMerchantView;

//	@Override
//	protected int getLoyoutId() {
//		return R.layout.xfjr_activity_index;
//	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// TODO Auto-generated method stub
		initView();
		initData();
	}
	
//	@Override
	protected void initView() {
		loginData = (LoginBean) getIntent().getSerializableExtra("TYPE_");
		mType = loginData.getRole();
		// 客户类型不一致异常提醒
		if (!mType.equals(XFJRConstant.M_ROLE) && !mType.equals(XFJRConstant.C_ROLE)) {
			throw new IllegalArgumentException("loginData String role argument must be 0 or 1.");
		}
	}

//	@Override
	protected void initData() {

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		if(bean!=null){
			mType = bean.getRole();
		}
		// 根据type加载不同页面
		switch (mType) {
		case XFJRConstant.C_ROLE:
			if (mManagerView == null) {
				if(mMerchantView!=null){
					mMerchantView.setVisibility(View.GONE);
				}
				mManagerView = mManagerStub.inflate();
				mIndexHelper = new XfjrIndexManagerHelper(this, mManagerView);
				mIndexHelper.onCreat();
			}
			break;
		case XFJRConstant.M_ROLE:
			if (mMerchantView == null) {
				if(mManagerView!=null){
					mManagerView.setVisibility(View.GONE);
				}
				mMerchantView = mMerchantStub.inflate();
				mIndexHelper = new XfjrIndexMerchantHelper(this, mMerchantView);
				mIndexHelper.onCreat();
			}
			break;
		}

		super.onResume();
		ArgumentUtil.get().post(bean);
		mIndexHelper.onResume();
	}

	/**
	 * 切換视图 没有网时处理
	 * 
	 * @param isManager
	 *            是否是客服经理
	 */
	public void swichView(boolean isManager) {
		if (isManager) {
			mManagerView.setVisibility(View.GONE);
			if (mMerchantView == null) {
				mType = XFJRConstant.M_ROLE;
				initData();
			}
			mMerchantView.setVisibility(View.VISIBLE);
		} else {
			mMerchantView.setVisibility(View.GONE);
			if (mManagerView == null) {
				mType = XFJRConstant.C_ROLE;
				initData();
			}
			mManagerView.setVisibility(View.VISIBLE);

		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		PreferencesUtil.put(XFJRConstant.KEY_IS_LOGIN, false); // 待fixed
		Executors.getInstance().clearDynamicData();
		if (mIndexHelper != null) {
			mIndexHelper.onDestory();
		}
	}

}
