package com.bocop.xfjr.fragment.qzzc.three;

import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.boc.jx.httptools.http.callback.IHttpCallback;
import com.boc.jx.tools.LogUtils;
import com.bocop.jxplatform.R;
import com.bocop.xfjr.XfjrMain;
import com.bocop.xfjr.activity.customer.XfjrMainActivity;
import com.bocop.xfjr.base.BaseCheckProcessFragment;
import com.bocop.xfjr.bean.AsyncResultBean;
import com.bocop.xfjr.bean.CheckRiskBean;
import com.bocop.xfjr.config.HttpRequest;
import com.bocop.xfjr.constant.XFJRConstant;
import com.bocop.xfjr.helper.ThrowableHelper;
import com.bocop.xfjr.observer.StepObserver;
import com.bocop.xfjr.observer.StepSubject;
import com.bocop.xfjr.util.boc.BOCSPUtil;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * description： 欺诈侦测第6个界面 第三方征信验证
 * <p/>
 * Created by TIAN FENG on 2017年8月28日 QQ：27674569 Email: 27674569@qq.com
 * Version：1.0
 */
public class FraudDetectionFragment6 extends BaseCheckProcessFragment implements StepSubject, IHttpCallback<String> {

	private static final int START_CUTDOWN_WHAT = 1;

	// 事件观察者
	private StepObserver mObserver;

	@ViewInject(R.id.tvCutDown)
	private TextView tvCutDown;// 倒计时文本
	@ViewInject(R.id.tvPrompt)
	private TextView tvPrompt;// 提示每隔10秒刷新
	@ViewInject(R.id.btnRefresh)
	private Button btnRefresh;// 刷新按钮
	@ViewInject(R.id.successView)
	private View successView;// 成功后的布局
	@ViewInject(R.id.ivSuccess)
	private ImageView ivSuccess;// 成功后的图片显示
	@ViewInject(R.id.tvCheck)
	private TextView tvCheck;// 是否有风险
	@ViewInject(R.id.tvErrMsg)
	private TextView tvErrMsg;// 失败原因
	@ViewInject(R.id.tvTitle)
	private TextView tvTitle;// 标题名称
	@ViewInject(R.id.btnRight)
	private Button btnRight;
	@ViewInject(R.id.btnLeft)
	private Button btnLeft;

	private int mCountDown = 10;

	/**
	 * 延时消息
	 */
	private Handler mHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			mCountDown--;
			if (mCountDown > 0) {
				startRefresh();
			} else {
				asyncRequset();
			}
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.xfjr_fragment_fraud_detection4, container, false);
	}

	@Override
	protected void initView() {
		// 上一步按钮固定不显示
		btnLeft.setVisibility(View.GONE);
		// 下一步按钮成功后显示
		btnRight.setVisibility(View.GONE);
		// 重置保存不显示
		setVisibility(tvReset, View.GONE);
		setVisibility(tvSave, View.GONE);
		tvTitle.setText("第三方征信验证");
	}

	/**
	 * 初始化数据，第一次进来发送一次请求
	 */
	@Override
	protected void initData() {
		// 请求网络时文本显示正在刷新
		tvCutDown.setText("正在刷新");
		// 直接发送人行征信验证
//		HttpRequest.checkOther(getActivity(), this);
	}

	/**
	 * 注册观察者
	 */
	@Override
	public void register(StepObserver observer) {
		mObserver = observer;
	}

	/**
	 * 下一步按钮点击
	 */
	@OnClick(R.id.btnRight)
	private void clickNext(View view) {
		if (btnRight.getText().toString().equals("下一步")) {
			if (mObserver != null) {
				mObserver.pushBackStack();
			}
		} else {
			getActivity().finish();
		}
		((XfjrMainActivity) getActivity()).sendBR(0);
	}

	/**
	 * 手动调用刷新
	 */
	@OnClick(R.id.btnRefresh)
	private void btnRefresh(View view) {
		mIsClick = true;
		mCountDown = 10;
		mHandler.removeMessages(START_CUTDOWN_WHAT);
		initData();
	}

	private boolean mIsClick = false;

	/**
	 * 开始计时刷新
	 */
	private void startRefresh() {
		tvCutDown.setText(mCountDown + "    秒后刷新成功");
		mHandler.sendEmptyMessageDelayed(START_CUTDOWN_WHAT, 1000);
	}

	/**
	 * 根据返回字段操作是否成功失败
	 */
	@Override
	public void onSuccess(String url, String data) {
		// 发起成功 倒计时调取一步结果查询
		mCountDown = 10;
		startRefresh();
		// asyncRequset();
		// }
	}

	@Override
	public void onError(String url, Throwable e) {
//		// 关闭dialog会进入这里并抛出 Throwable("http cancle")
//		LogUtils.e("e ->" + e.getMessage());
//		onSuccess(false);
	}

	@Override
	public void onFinal(String url) {
//		if (!XfjrMain.isNet) {
//			if (mIsClick) {
//				onSuccess(true);
//			} else {
//				mCountDown = 10;
//				startRefresh();
//			}
//		}
	}

	
	/**
	 * 异步获取结果
	 */
	private void asyncRequset() {
		HttpRequest.getAsyncResult(getActivity(), "0", BOCSPUtil.getUserId(), BOCSPUtil.getActoken(), new IHttpCallback<AsyncResultBean>() {

			@Override
			public void onSuccess(String url, AsyncResultBean result) {
				FraudDetectionFragment6.this.onSuccess(true);
			}

			@Override
			public void onError(String url, Throwable e) {
				LogUtils.e("e ->" + e.getMessage());
				ThrowableHelper.setMsgByUrlThrowable(e, tvErrMsg);
				FraudDetectionFragment6.this.onSuccess(false);
			}

			@Override
			public void onFinal(String url) {
				if (!XfjrMain.isNet) {
					if (mIsClick) {
						FraudDetectionFragment6.this.onSuccess(true);
					} else {
						mCountDown = 10;
						startRefresh();
					}
				}
			}
		});
		

	}
	
	
	/**
	 * 检测成功
	 */
	private void onSuccess(boolean isSuccess) {
		// 成功页面显示
		successView.setVisibility(View.VISIBLE);
		// 下一步按钮显示
		btnRight.setVisibility(View.VISIBLE);
		btnRight.setText(isSuccess ? "下一步" : "结束申请");
		// 图片显示成功或者失败
		ivSuccess.setImageResource(isSuccess ? R.drawable.xfjr_check_success : R.drawable.xfjr_check_faile);
		// 倒计时文本隐藏
		tvCutDown.setVisibility(View.GONE);
		// 提示文字隐藏
		tvPrompt.setVisibility(View.GONE);
		// 刷新按钮隐藏
		btnRefresh.setVisibility(View.GONE);
		// 是否有风险
		tvCheck.setText(isSuccess ? "无风险，验证通过" : "有风险，验证失败");
		// 是否显示失败原因
		tvErrMsg.setVisibility(isSuccess ? View.GONE : View.VISIBLE);
	}

}