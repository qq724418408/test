package com.bocop.xfjr.base;

import com.boc.jx.baseUtil.view.ViewUtils;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 懒加载的fragment基类
 * 
 * @author wujunliu
 *
 */
public abstract class XFJRLazyFragment extends Fragment {

	protected View view;
	protected boolean isVisible;
	private boolean isCreate;

	/**
	 * 在这里实现Fragment数据的缓加载.
	 * 
	 * @param isVisibleToUser
	 */
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			isVisible = true;
			onVisible();
		} else {
			isVisible = false;
			onInvisible();
		}
	}

	protected void onVisible() {
		if (isCreate && isVisible) {
			lazyLoad();
			isVisible = false;
			isCreate = false;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		isCreate = true;
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// TODO Auto-generated method stub
		ViewUtils.inject(this, view);
		isCreate = true;
		initData();
		onVisible();
	}

	protected void initData() {
		// TODO Auto-generated method stub

	}

	protected abstract void lazyLoad();

	protected void onInvisible() {
	}

	protected void setDrawableTop(TextView tv, int resId) {
		if (!isDetached() && getActivity() != null && !getActivity().isFinishing()) {
			@SuppressWarnings("deprecation")
			Drawable drawable = getResources().getDrawable(resId);
			// drawable.getMinimumWidth() drawable.getMinimumHeight()
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
			tv.setCompoundDrawables(null, drawable, null, null);
		}
	}

}
