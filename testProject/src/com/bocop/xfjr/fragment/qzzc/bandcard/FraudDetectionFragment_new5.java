package com.bocop.xfjr.fragment.qzzc.bandcard;

import com.bocop.jxplatform.R;
import com.bocop.xfjr.base.BaseCheckProcessFragment;
import com.bocop.xfjr.fragment.qzzc.AsyncResultFragment;
import com.bocop.xfjr.fragment.qzzc.BaseSwich;
import com.bocop.xfjr.observer.StepObserver;
import com.bocop.xfjr.observer.StepSubject;
import com.bocop.xfjr.util.FragmentManagerHelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 银联验证的根fragment
 */
public class FraudDetectionFragment_new5 extends BaseCheckProcessFragment implements StepSubject, BaseSwich {

	private StepObserver mObserver;

	private FragmentManagerHelper mHelper;

	private Fragment fratment;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.xfjr_fragment_fraud_detection_new5, container, false);
	}

	@Override
	public void register(StepObserver observer) {
		mObserver = observer;
	}

	@Override
	protected void initData() {
		mHelper = new FragmentManagerHelper(getFragmentManager(), R.id.flRootView);
	}

	@Override
	protected void initView() {
		// 判断是否已经提交过了
		if (!productBean.isBankCardSubmit()) {
			mHelper.add(fratment = new FraudDetectionFragment5_One(this));
		} else {
			mHelper.add(fratment = new AsyncResultFragment(this, AsyncResultFragment.BANK_CARD));
		}
	}

	/**
	 * 跳转一个大步骤
	 */
	@Override
	public void push() {
		if (null != mObserver) {
			mObserver.pushBackStack();
		}
	}

	/**
	 * 跳转异步结果请求
	 */
	@Override
	public void swichAsync() {
		mHelper.add(fratment = new AsyncResultFragment(this, AsyncResultFragment.BANK_CARD));
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		fratment.onActivityResult(requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
	}
}
