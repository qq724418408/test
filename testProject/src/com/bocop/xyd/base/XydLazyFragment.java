package com.bocop.xyd.base;

import com.bocop.xyd.base.BaseFragment;

/**
 * 懒加载的fragment基类
 *
 */
public abstract class XydLazyFragment extends BaseFragment {
	
	protected boolean isVisible;

	/**
	 * 在这里实现Fragment数据的缓加载.
	 * 
	 * @param isVisibleToUser
	 */
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (getUserVisibleHint()) {
			isVisible = true;
			onVisible();
		} else {
			isVisible = false;
			onInvisible();
		}
	}

	protected void onVisible() {
		lazyLoad();
	}

	protected abstract void lazyLoad();
	
	protected void onInvisible() {
	}
	
}
