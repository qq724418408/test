package com.bocop.xfjr.fragment;

import java.util.Map;

import com.boc.jx.tools.LogUtils;
import com.bocop.jxplatform.R;
import com.bocop.xfjr.base.BaseCheckProcessFragment;
import com.bocop.xfjr.callback.CommunicationCallBack;
import com.bocop.xfjr.constant.XFJRConstant;
import com.bocop.xfjr.observer.StepObserver;
import com.bocop.xfjr.observer.StepSubject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * description： 风险预授信
 * <p/>
 * Version：1.1
 */
public class InstalmentPretrialFragment extends BaseCheckProcessFragment implements StepSubject, CommunicationCallBack {

	private StepObserver mObserver;
	private FragmentManager manager;
	private PretrialBasicInfoFragment basicInfoFragment;
	private PretrialCustomTypeInfoFragment pretrialCustomTypeInfoFragment;
	private PretrialResultFragment pretrialResultFragment;

	@Override
	protected boolean resetClick(View view) {
		LogUtils.e("InstalmentPretrialFragment---resetClick");
		if (basicInfoFragment != null && basicInfoFragment.isVisible()) {
			basicInfoFragment.resetClick(view);
			return true;
		} else if (pretrialCustomTypeInfoFragment != null && pretrialCustomTypeInfoFragment.isVisible()) {
			pretrialCustomTypeInfoFragment.resetClick(view);
			return true;
		}
		return false;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return view = inflater.inflate(R.layout.xfjr_fragment_pretrial, container, false);
	}

	@Override
	public void register(StepObserver observer) {
		mObserver = observer;
	}

	@Override
	protected void initData() {
		super.initData();
		openFragment(1, null);
	}

	@Override
	public boolean onBackClick() {
//		super.onBackClick();
		return backCallBack();
	}

	private void openFragment(int step, Map<String, Object> map) {
		if (manager == null)
			manager = getChildFragmentManager();
		if (step == 1) {
			if (pretrialCustomTypeInfoFragment != null && pretrialCustomTypeInfoFragment.isVisible()) {
				manager.beginTransaction().hide(pretrialCustomTypeInfoFragment).commit();
				//pretrialCustomTypeInfoFragment = null;
			}
			if (basicInfoFragment == null) {
				basicInfoFragment = new PretrialBasicInfoFragment();
				manager.beginTransaction().add(R.id.rlContain, basicInfoFragment).commit();
			} else {
				manager.beginTransaction().show(basicInfoFragment).commit();
			}

		} else if (step == 2) {
			if (basicInfoFragment != null && basicInfoFragment.isVisible()) {
				manager.beginTransaction().hide(basicInfoFragment).commit();
			}
			if (pretrialResultFragment != null && pretrialResultFragment.isVisible()) {
				manager.beginTransaction().remove(pretrialResultFragment).commit();
				pretrialResultFragment = null;
			}
			if (pretrialCustomTypeInfoFragment == null) {
				pretrialCustomTypeInfoFragment = new PretrialCustomTypeInfoFragment();
				if (map != null) {
					Bundle bundle = new Bundle();
					bundle.putBoolean(XFJRConstant.KEY_MARRIED_STATUS, (boolean) map.get(XFJRConstant.KEY_MARRIED_STATUS));
					pretrialCustomTypeInfoFragment.setArguments(bundle);
				}
				manager.beginTransaction().add(R.id.rlContain, pretrialCustomTypeInfoFragment).commit();
			} else {
				manager.beginTransaction().show(pretrialCustomTypeInfoFragment).commit();
			}

		} else {
			if (basicInfoFragment != null && basicInfoFragment.isVisible()) {
				manager.beginTransaction().hide(basicInfoFragment).commit();
			}
			if (pretrialCustomTypeInfoFragment != null && pretrialCustomTypeInfoFragment.isVisible()) {
				manager.beginTransaction().hide(pretrialCustomTypeInfoFragment).commit();
			}
			if (pretrialResultFragment == null) {
				pretrialResultFragment = new PretrialResultFragment();
				if (map != null) {
					Bundle bundle = new Bundle();
					boolean isPass = (boolean) map.get(XFJRConstant.KEY_PRETRIAL_RESULT);
					bundle.putBoolean(XFJRConstant.KEY_PRETRIAL_RESULT, isPass);
					if (isPass) {
						bundle.putString(XFJRConstant.KEY_CREDIT_LINE, (String) map.get(XFJRConstant.KEY_CREDIT_LINE));
					}
					pretrialResultFragment.setArguments(bundle);
				}
				manager.beginTransaction().add(R.id.rlContain, pretrialResultFragment).commit();
			} else {
				manager.beginTransaction().show(pretrialResultFragment).commit();
			}

		}
	}

	private boolean backDeal() {
		if (pretrialResultFragment != null && pretrialResultFragment.isVisible()) {
//			openFragment(2, null);
			getActivity().findViewById(R.id.tvHomePage).performClick();
			return true;
		}
		if (pretrialCustomTypeInfoFragment != null && pretrialCustomTypeInfoFragment.isVisible()) {
			openFragment(1, null);
			return true;
		}
		if (basicInfoFragment != null && basicInfoFragment.isVisible()) {
			getActivity().findViewById(R.id.tvHomePage).performClick();
			return true;
		}
		return false;
	}

	@Override
	public boolean backCallBack() {
		LogUtils.e("后退....");
		return backDeal();
	}

	@Override
	public void nextCallBack(Map<String, Object> map, String fxModel) {
		if (basicInfoFragment != null && basicInfoFragment.isVisible())
			if (fxModel.equals("3")) {
				openFragment(3, map);
			} else {
				openFragment(2, map);
			}
		else if (pretrialCustomTypeInfoFragment != null && pretrialCustomTypeInfoFragment.isVisible())
			openFragment(3, map);
		else {
			if (mObserver != null) {
				mObserver.pushBackStack();
			}
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		try {
			basicInfoFragment.onActivityResult(requestCode & 0xffff, resultCode, data);
		} catch (Exception e) {
		}
	}

}