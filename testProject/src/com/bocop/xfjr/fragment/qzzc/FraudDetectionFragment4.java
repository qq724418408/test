package com.bocop.xfjr.fragment.qzzc;

import com.boc.jx.baseUtil.view.annotation.CheckNet;
import com.boc.jx.baseUtil.view.annotation.Duplicate;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.boc.jx.httptools.http.callback.IHttpCallback;
import com.boc.jx.tools.LogUtils;
import com.bocop.jxplatform.R;
import com.bocop.xfjr.XfjrMain;
import com.bocop.xfjr.activity.XfjrIndexActivity;
import com.bocop.xfjr.activity.customer.XfjrMainActivity;
import com.bocop.xfjr.base.BaseCheckProcessFragment;
import com.bocop.xfjr.bean.AsyncResultBean;
import com.bocop.xfjr.config.HttpRequest;
import com.bocop.xfjr.config.UrlConfig;
import com.bocop.xfjr.observer.StepObserver;
import com.bocop.xfjr.observer.StepSubject;
import com.bocop.xfjr.util.XFJRUtil;
import com.bocop.xfjr.util.boc.BOCSPUtil;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * description： 欺诈侦测第4个界面 人行征信风险验证
 * <p/>
 * Created by TIAN FENG on 2017年8月28日 QQ：27674569 Email: 27674569@qq.com
 * Version：1.0
 */
public class FraudDetectionFragment4 extends BaseCheckProcessFragment
		implements StepSubject, IHttpCallback</* CheckRiskBean */String> {
	private static final int START_CUTDOWN_WHAT = 0;
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
	}

	/**
	 * 初始化数据，第一次进来发送一次请求
	 */
	@Override
	protected void initData() {
		// 是否已经提交了人人行征信
		if (productBean.isPersonalSubmit()) {
			startRefresh();
		} else {
			// 请求网络时文本显示正在刷新
			tvCutDown.setText(getString(R.string.on_ref));
			// 发起人行征信验证
			HttpRequest.checkRisk(getActivity(), this);
		}
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
	@CheckNet
	@Duplicate("FraudDetectionFragment4")
	private void clickNext(View view) {
		if (btnRight.getText().toString().equals(getString(R.string.next))) {
			if (mObserver != null) {
				mObserver.pushBackStack();
				btnRight.setEnabled(false);
			}
		} else if (btnRight.getText().toString().equals(getString(R.string.over))) {
			HttpRequest.over(getActivity(), "04", new IHttpCallback<String>() {

				@Override
				public void onSuccess(String url, String result) {
					((XfjrMainActivity) getActivity()).sendBR(0);
					// 结束申请
//					getActivity().finish();
					Intent intent = new Intent(getActivity(),XfjrIndexActivity.class);
					getActivity().startActivity(intent);
				}

				@Override
				public void onError(String url, Throwable e) {
					UrlConfig.showErrorTips(getActivity(), e, true);
				}

				@Override
				public void onFinal(String url) {

				}
			});
		}

	}

	/**
	 * 手动调用刷新
	 */
	@OnClick(R.id.btnRefresh)
	private void btnRefresh(View view) {
		if (!XfjrMain.isNet) {
			onSuccess(true);
			return;
		}
		mIsClick = true;
		mCountDown = 10;
		mHandler.removeMessages(START_CUTDOWN_WHAT);
		asyncRequset();
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
	public void onSuccess(String url, /* CheckRiskBean */String data) {
		// 发起成功 倒计时调取一步结果查询
		mCountDown = 10;
		startRefresh();
		// asyncRequset();
	}

	@Override
	public void onError(String url, Throwable e) {
		/**
		 * TODO 还缺少失败的提示
		 * 
		 * 
		 */
		UrlConfig.showErrorTips(getActivity(), e, true);
		final AlertDialog.Builder normalDialog = new AlertDialog.Builder(getActivity());
		// normalDialog.setIcon(R.drawable.icon_dialog);
		// normalDialog.setTitle("我是一个普通Dialog");
		normalDialog.setMessage("请求失败,是否重试?");
		normalDialog.setCancelable(false);
		normalDialog.setPositiveButton("退出", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				getActivity().finish();
			}
		});
		normalDialog.setNegativeButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				initData();
			}
		});
		// 显示
		normalDialog.show();
	}

	@Override
	public void onFinal(String url) {

	}

	/**
	 * 异步获取结果
	 */
	private void asyncRequset() {
		HttpRequest.getAsyncResult(getActivity(), "1", BOCSPUtil.getUserId(), BOCSPUtil.getActoken(),
				new IHttpCallback<AsyncResultBean>() {

					@Override
					public void onSuccess(String url, AsyncResultBean result) {
						if (result.status == 0) {// 成功
							FraudDetectionFragment4.this.onSuccess(true);
						} else if (result.status == 2) {// 拒绝
							FraudDetectionFragment4.this.onSuccess(false);
							// 是否显示失败原因
						} else {// 验证中
							mCountDown = 10;
							startRefresh();
						}
						
					}

					@Override
					public void onError(String url, Throwable e) {
						LogUtils.e("e ->" + e.getMessage());
						XFJRUtil.show10001Error(getActivity(),e);
					}

					@Override
					public void onFinal(String url) {
						if (!XfjrMain.isNet) {
							if (mIsClick) {
								FraudDetectionFragment4.this.onSuccess(true);
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
		if (isAdded()) {
			// 成功页面显示
			successView.setVisibility(View.VISIBLE);
			// 下一步按钮显示
			btnRight.setVisibility(View.VISIBLE);
			btnRight.setText(isSuccess ? getString(R.string.next) : getString(R.string.over));
			// 图片显示成功或者失败
			ivSuccess.setImageResource(isSuccess ? R.drawable.xfjr_check_success : R.drawable.xfjr_check_faile);
			// 倒计时文本隐藏
			tvCutDown.setVisibility(View.GONE);
			// 提示文字隐藏
			tvPrompt.setVisibility(View.GONE);
			// 刷新按钮隐藏
			btnRefresh.setVisibility(View.GONE);
			// 是否有风险
			tvCheck.setText(isSuccess ? "验证通过" : "验证失败");
			// 是否显示失败原因
			tvErrMsg.setVisibility(
					/* isSuccess ? */View.GONE /* : View.VISIBLE */);
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		mHandler.removeMessages(START_CUTDOWN_WHAT);
		LogUtils.e("FraudDetectionFragment4----->onDestroy取消请求"+UrlConfig.ASYNC_RESULT);
		super.onDestroy();
	}
}