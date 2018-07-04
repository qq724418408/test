package com.bocop.xfjr.fragment.qzzc;

import com.boc.jx.baseUtil.view.annotation.CheckNet;
import com.boc.jx.baseUtil.view.annotation.Duplicate;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.boc.jx.httptools.http.HttpUtils;
import com.boc.jx.httptools.http.callback.IHttpCallback;
import com.boc.jx.httptools.http.decortor.DialogDecorter;
import com.bocop.jxplatform.R;
import com.bocop.xfjr.XfjrMain;
import com.bocop.xfjr.activity.customer.XfjrMainActivity;
import com.bocop.xfjr.base.BaseCheckProcessFragment;
import com.bocop.xfjr.config.HttpRequest;
import com.bocop.xfjr.config.UrlConfig;
import com.bocop.xfjr.observer.StepObserver;
import com.bocop.xfjr.observer.StepSubject;
import com.bocop.xfjr.util.PatternUtils;
import com.bocop.xfjr.util.XFJRUtil;
import com.bocop.xfjr.util.boc.BOCSPUtil;
import com.bocop.yfx.utils.ToastUtils;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * description： 欺诈侦测第2个界面 手机验证
 * <p/>
 * Created by TIAN FENG on 2017年8月28日 QQ：27674569 Email: 27674569@qq.com
 * Version：1.0
 */
public class FraudDetectionFragment2 extends BaseCheckProcessFragment implements StepSubject, IHttpCallback<String> {
	private static int message_time = 1;
	private int mTimer = XfjrMain.reqSmsCodeTime;
	private StepObserver mObserver;
	@ViewInject(R.id.tvPhoneNumber)
	private EditText tvPhoneNumber;// 手机号码
	@ViewInject(R.id.etCheckNum)
	private EditText etCheckNum;// 验证码
	@ViewInject(R.id.btnCheckNum)
	private Button btnCheckNum;// 获取验证码
	@ViewInject(R.id.btnLeft)
	private View btnLeft;// 上一步
	@ViewInject(R.id.btnRight)
	private TextView btnRight;
	private CountDownTimer timer;

	// 倒计时处理
	private Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			if (mTimer > 0) {
				startCutTime();
			} else {
				mTimer = 60;
				btnCheckNum.setEnabled(true);
				btnCheckNum.setText("重获");
				btnCheckNum.setBackgroundResource(R.drawable.xfjr_shape_circle_corner_btn_red);
			}
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.xfjr_fragment_fraud_detection2, container, false);
	}

	@OnClick(R.id.btnRight)
	@Duplicate("FraudDetectionFragment2")
	@CheckNet
	private void clickNext(View view) {
		if (!XfjrMain.isNet) {
			if (mObserver != null) {
				mObserver.pushBackStack();
				btnRight.setEnabled(false);
			}
			return;
		}
		if(TextUtils.isEmpty(tvPhoneNumber.getText().toString())){
			ToastUtils.show(getActivity(), getString(R.string.please_input_phone_number), 0);
			return;
		}
		// 判断手机号
		if (!PatternUtils.isMobile(tvPhoneNumber.getText().toString())) {
			ToastUtils.show(getActivity(), getString(R.string.user_phone_number_err), 0);
			return;
		}
		String checkNum = etCheckNum.getText().toString();
		// 判断验证码
		if (checkNum.length() != 6) {
			ToastUtils.show(getActivity(), getString(R.string.error_check_num), 0);
			return;
		}
		// 手机验证
		validateCheckNum();
	}

	@OnClick(R.id.btnLeft)
	private void clickStep(View view) {
		if (mObserver != null) {
			mObserver.popBackStack();
		}
	}

	@Override
	protected void initView() {
		btnRight.setText("验证");
		btnLeft.setVisibility(View.GONE);
		String phoneNum = productBean.getTelephone()/*PreferencesUtil.get(XFJRConstant.KEY_CLIENT_PHONE, "")*/;
		
		tvPhoneNumber.setText(phoneNum);
	}

	@Override
	public void register(StepObserver observer) {
		mObserver = observer;
	}

	/**
	 * 获取验证码
	 * 
	 * @param view
	 */
	@OnClick(R.id.btnCheckNum)
	@CheckNet
	private void btnCheckNum(View view) {
		if(TextUtils.isEmpty(tvPhoneNumber.getText().toString())){
			ToastUtils.show(getActivity(), getString(R.string.please_input_phone_number), 0);
			return;
		}
		if (!PatternUtils.isMobile(tvPhoneNumber.getText().toString())) {
			ToastUtils.show(getActivity(), getString(R.string.user_phone_number_err), 0);
			return;
		}
		HttpRequest.reqSmsCode(getActivity(), tvPhoneNumber.getText().toString(), new IHttpCallback<String>() {

			@Override
			public void onSuccess(String url, String result) {
				startCountDown();
				ToastUtils.show(getActivity(), getString(R.string.get_check_num_success), 0);
			}

			@Override
			public void onError(String url, Throwable e) {
				UrlConfig.showErrorTips(getActivity(), e, true);
			}

			@Override
			public void onFinal(String url) {
			}
		});
//		BOCSPUtil.reLogin(getActivity());
//		// 发送网络请求验证码
//		HttpUtils.with(getActivity()).url(UrlConfig.REQ_LOGIN_CODE)
//				.addParams("busiNo", XfjrMain.businessId)
//				.addParams("userId",BOCSPUtil.getUserId())
//				.addParams("actoken",BOCSPUtil.getActoken())
//				.addParams("client","A")
//				.addDecortor(new DialogDecorter())
//				.addParams("telephone", tvPhoneNumber.getText().toString()).post(new IHttpCallback<String>() {
//
//					@Override
//					public void onSuccess(String url, String result) {
//						// 开启倒计时
//						// startCutTime();
//						startCountDown();
//						ToastUtils.show(getActivity(), getString(R.string.get_check_num_success), 0);
//					}
//
//					@Override
//					public void onError(String url, Throwable e) {
////						LogUtils.e("e ->" + e);
////						ToastUtils.show(getActivity(), getString(R.string.get_check_num_err), 0);
//						UrlConfig.showErrorTips(getActivity(), e);
//					}
//
//					@Override
//					public void onFinal(String url) {
//					}
//				});
	}

	// 开启倒计时 see#startCountDown()
	@Deprecated
	private void startCutTime() {
		mTimer--;
		btnCheckNum.setText(mTimer + "");
		btnCheckNum.setEnabled(false);
		btnCheckNum.setBackgroundResource(R.drawable.xfjr_shape_circle_corner_btn_gray);
		mHandler.sendEmptyMessageDelayed(message_time, 1000);
	}

	// 开启倒计时
	private void startCountDown() {
		getCodeEnable(false);
		timer = new CountDownTimer(mTimer * 1000, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {
				btnCheckNum.setText(millisUntilFinished / 1000 + "");
			}

			@Override
			public void onFinish() {
				getCodeEnable(true);
			}
		};
		timer.start();
	}

	// 获取按钮是否可用
	private void getCodeEnable(boolean enable) {
		btnCheckNum.setEnabled(enable);
		if (enable) {
			btnCheckNum.setText("重获");
			btnCheckNum.setBackgroundResource(R.drawable.xfjr_shape_circle_corner_btn_red);
			releaseTimer();
		} else {
			btnCheckNum.setBackgroundResource(R.drawable.xfjr_shape_circle_corner_btn_gray);
		}
	}

	private void releaseTimer() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	// 手机验证
	private void validateCheckNum() {
		if (!PatternUtils.isMobile(tvPhoneNumber.getText().toString())) {
			ToastUtils.show(getActivity(), getString(R.string.user_phone_number_err), 0);
			return;
		}
		btnRight.setEnabled(false);
		HttpUtils.with(getActivity()).url(UrlConfig.PHONT_CHECK).addDecortor(new DialogDecorter())
				.addParams("phone", tvPhoneNumber.getText().toString())// 手机号码
				.addParams("smscode", etCheckNum.getText().toString())// 短信验证码
				.addParams("userId",BOCSPUtil.getUserId())
				.addParams("actoken",BOCSPUtil.getActoken())
				.addParams("client","A")
				.addParams("businessId", XfjrMain.businessId).post(this);

	}

	@Override
	public void onSuccess(String url, String result) {
		if (mObserver != null) {
			//((XfjrMainActivity)getActivity()).sendBR(0);
			mObserver.pushBackStack();
			btnRight.setEnabled(false);
		}
	}

	@Override
	public void onError(String url, Throwable e) {
//		ToastUtils.show(getActivity(), getString(R.string.check_num_err), 0);
		UrlConfig.showErrorTips(getActivity(), e, true);
		btnRight.setEnabled(true);
	}

	@Override
	public void onFinal(String url) {
		((XfjrMainActivity)getActivity()).sendBR(0);
	}

	/**
	 * 空白处点击事件
	 * 
	 * @param view
	 */
	@OnClick(R.id.lltBlank)
	private void clickBlank(View view) {
		XFJRUtil.hideSoftInput(view);
	}
	
	@Override
	public void onDestroy() {
		releaseTimer();
		super.onDestroy();
	}

}