package com.bocop.xfjr.base;

import com.boc.jx.base.BaseFragment;
import com.bocop.jxplatform.R;
import com.bocop.xfjr.activity.customer.XfjrMainActivity;
import com.bocop.xfjr.bean.add.ProductBean;
import com.bocop.xfjr.observer.IFragmentState;
import com.bocop.xfjr.util.file.FileUtil;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * description： 检测流程的基类 showError 函数和 errorClick函数配合使用
 * 
 * <p/>
 * Created by TIAN FENG on 2017年8月30日 QQ：27674569 Email: 27674569@qq.com
 * Version：1.0
 */
public class BaseCheckProcessFragment extends BaseFragment implements IFragmentState {
	/**
	 * 是否显示重置和保存
	 */
	public boolean isShowReset = false;
	public boolean isShowSave = false;
	protected boolean isPush;
	// 保存
	protected TextView tvReset;
	// 重置
	protected TextView tvSave;
	// 是否取消申请的dialog
//	protected Dialog cancleDialog;
	
	public ProductBean productBean;

//	private CancleClickLinstener mListener = new CancleClickLinstener();
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		tvReset = (TextView) getActivity().findViewById(R.id.tvReset);
		tvSave = (TextView) getActivity().findViewById(R.id.tvSave);
		super.onViewCreated(view, savedInstanceState);
//		cancleDialog = new XfjrDialog.Builder(getActivity())
//				.setContentView(R.layout.xfjr_dialog_cancle_sc)
//				.setWidthAndHeight(ScreenUtils.dip2px(getActivity(), 250), ViewGroup.LayoutParams.WRAP_CONTENT)
//				.setOnClickListener(R.id.tvOk, mListener)
//				.setOnClickListener(R.id.tvCancel, mListener).create();
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		isPush = ((XfjrMainActivity)getActivity()).isPush;
		productBean = XfjrMainActivity.productBean;
	}
	
	/**
	 * 设置控件是否可见 主要用于显示和隐藏 保存和重置按钮
	 */
	protected void setVisibility(View view, int visibility) {
		switch (view.getId()) {
		case R.id.tvReset:
			isShowReset = visibility == View.VISIBLE;
			break;
		case R.id.tvSave:
			isShowSave = visibility == View.VISIBLE;
			break;
		}
		view.setVisibility(visibility);
	}

	/**
	 * 重置点击监听 不要复写此方法 复写resetClick
	 */
	@Override
	public void onResetClick(View view) {
		isShowReset = resetClick(view);
	}

	/**
	 * 保存点击监听 不要复写此方法 复写saveClick
	 */
	@Override
	public void onSaveClick(View view) {
		isShowSave = saveClick(view);
	}

	/**
	 * 物理返回键是否拦截事件
	 */
	@Override
	public boolean onBackClick() {
//		if (cancleDialog!=null) {
//			cancleDialog.show();
//		}	
		getActivity().findViewById(R.id.tvHomePage).performClick();
		return true;
	}

	/**
	 * 重置点击监听
	 */
	protected boolean resetClick(View view) {
		return true;
	}

	/**
	 * 保存点击监听
	 */
	protected boolean saveClick(View view) {
		return true;
	}

	/**
	 * 显示错误页面1
	 */
	protected void showError1() {
		((XfjrMainActivity) getActivity()).getHelper().showError1();
	}

	/**
	 * 显示错误页面2
	 */
	protected void showError2() {
		((XfjrMainActivity) getActivity()).getHelper().showError2();
	}

	/**
	 * 隐藏错误页面
	 */
	protected void hideError() {
		((XfjrMainActivity) getActivity()).getHelper().hide();
	}

	/**
	 * 点击错误页面后的回调 子类复写
	 */
	@Override
	public void errorClick() {

	}

	/**
	 * 选择之后黑色、加粗
	 * 
	 * @param view
	 * @param text
	 */
	protected void selected(TextView view, String text) {
		view.setText(text);
		view.setTextColor(getActivity().getResources().getColor(R.color.xfjr_black));
		//view.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
	}

	/**
	 * 未选择之前灰色不加粗
	 * 
	 * @param view
	 */
	protected void unSelected(TextView view, String text) {
		view.setText(text);
		view.setTextColor(getActivity().getResources().getColor(R.color.xfjr_selector_hint));
		//view.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		FileUtil.delAllImages(getActivity()); // 删除所有图片缓存
	}	
	
	/**
	 * dialog的控制监听
	 */
//	private class CancleClickLinstener implements View.OnClickListener{
//		/**
//		 * 控制
//		 */
//		@Override
//		public void onClick(View v) {
//			switch (v.getId()) {
//			
//			case R.id.tvOk:
//				cancleDialog.dismiss();
//				getActivity().finish();
//				break;
//			case R.id.tvCancel:
//				cancleDialog.dismiss();
//				break;
//			}
//		}
//	}
}
