package com.bocop.xfjr.fragment.qzzc;

import com.boc.jx.baseUtil.view.annotation.CheckNet;
import com.boc.jx.baseUtil.view.annotation.Duplicate;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.bocop.jxplatform.R;
import com.bocop.xfjr.XfjrMain;
import com.bocop.xfjr.activity.customer.XfjrMainActivity;
import com.bocop.xfjr.base.BaseCheckProcessFragment;
import com.bocop.xfjr.observer.StepObserver;
import com.bocop.xfjr.observer.StepSubject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * description： 欺诈侦测第7个界面
 * 				侦测完成
 * <p/>
 * Created by TIAN FENG on 2017年8月28日
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */
public class FraudDetectionFragment7  extends BaseCheckProcessFragment implements StepSubject {
	
	// 事件观察者
	private StepObserver mObserver;
	@ViewInject(R.id.btnRight)
	private Button btnRight;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.xfjr_fragment_fraud_detection4, container, false);
	}

	@OnClick(R.id.btnRight)
	@CheckNet
	@Duplicate("FraudDetectionFragment7")
	private void clickNext(View view) {
		if (!XfjrMain.isNet) {
			if (mObserver != null) {
				mObserver.pushBackStack();
				btnRight.setEnabled(false);
			}
			return ;
		}
		if (mObserver != null) {
			// 欺诈侦测->第一个界面
			mObserver.pushBackStack();
			btnRight.setEnabled(false);
		}
		((XfjrMainActivity)getActivity()).sendBR(0);
	}

	@OnClick(R.id.btnLeft)
	private void clickStep(View view) {
		if (mObserver != null) {
			mObserver.popBackStack();
		}
	}

	@Override
	protected void initView() {
		setVisibility(tvReset, View.GONE);
		//setVisibility(tvSave, View.VISIBLE);
	}
	
	@Override
	public void register(StepObserver observer) {
		mObserver = observer;
	}
}