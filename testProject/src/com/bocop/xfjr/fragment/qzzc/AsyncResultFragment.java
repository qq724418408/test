package com.bocop.xfjr.fragment.qzzc;

import com.boc.jx.base.BaseFragment;
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
import com.bocop.xfjr.bean.AsyncResultBean;
import com.bocop.xfjr.config.HttpRequest;
import com.bocop.xfjr.config.UrlConfig;
import com.bocop.xfjr.util.XFJRUtil;
import com.bocop.xfjr.util.boc.BOCSPUtil;

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
 * 多合1异步结果请求查询
 */
public class AsyncResultFragment extends BaseFragment {

	public static int BANK_CARD = 1;
	public static int THREE_CHECK = 2;

	// 操作下一步的对象
	private BaseSwich mRootFragment;
	private int mType;
	
	public AsyncResultFragment(){}
	
	public AsyncResultFragment(BaseSwich baseSwith, int typeCode) {
		this.mRootFragment = baseSwith;
		this.mType = typeCode;
	}

	/*********************************************************************************************************************/
	private static final int START_CUTDOWN_WHAT = 0;

	@ViewInject(R.id.tvTitle)
	private TextView tvTitle;// title
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
		LogUtils.e("AsyncResultFragment----->onCreateView");
		return inflater.inflate(R.layout.xfjr_fragment_fraud_detection4, container, false);
	}

	@Override
	protected void initView() {
		tvTitle.setText(mType == BANK_CARD ? "银联信用卡验证" : "第三方征信验证");
		btnLeft.setVisibility(View.GONE);
		// 下一步隐藏
		btnRight.setVisibility(View.GONE);
		
		btnRight.setText(mType == BANK_CARD ? "下一步" : "开始预授信");

		// 开始倒计时
		startRefresh();
	}

	/**
	 * 手动调用刷新
	 */
	@OnClick(R.id.btnRefresh)
	private void btnRefresh(View view) {
		if(!XfjrMain.isNet){
			onCheckSuccess(true);
			return ;
		}
		mCountDown = 10;
		mHandler.removeMessages(START_CUTDOWN_WHAT);
		asyncRequset();
	}

	/**
	 * 开始计时刷新
	 */
	private void startRefresh() {
		tvCutDown.setText(mCountDown + "    秒后刷新成功");
		mHandler.sendEmptyMessageDelayed(START_CUTDOWN_WHAT, 1000);
	}

	/**
	 * 异步获取结果
	 */
	private void asyncRequset() {
		HttpRequest.getAsyncResult(getActivity(), mType == BANK_CARD ? "2" : "0", BOCSPUtil.getUserId(),
				BOCSPUtil.getActoken(), new IHttpCallback<AsyncResultBean>() {

					@Override
					public void onSuccess(String url, AsyncResultBean result) {
						if (result.status == 0) {// 成功
							onCheckSuccess(true);
						} else if (result.status == 2) {// 拒绝
							onCheckSuccess(false);
							// 是否显示失败原因
							if (mType == BANK_CARD) {
//								tvErrMsg.setVisibility(View.VISIBLE);
//								tvErrMsg.setText("服务器暂未给失败原因");
							}
						} else {// 验证中
							mCountDown = 10;
							startRefresh();
						}
					}

					@Override
					public void onError(String url, Throwable e) {
						/*mCountDown = 10;
						startRefresh();*/
						XFJRUtil.show10001Error(getActivity(),e);
					}

					@Override
					public void onFinal(String url) {
						
					}
				});

	}

	/**
	 * 检测成功
	 */
	private void onCheckSuccess(boolean isSuccess) {
		if (isAdded()) {
			// 成功页面显示
			successView.setVisibility(View.VISIBLE);
			// 下一步按钮显示
			btnRight.setVisibility(View.VISIBLE);
			if (mType == BANK_CARD) {
				btnRight.setText(isSuccess ? getString(R.string.next) : getString(R.string.over));
			} else {
				btnRight.setText(isSuccess ? "开始预授信" : getString(R.string.over));
			}
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
			// 不显示原因 现在全部gone掉
			tvErrMsg.setVisibility(/*isSuccess?*/View.GONE/*:View.VISIBLE*/);
		}
	}

	/**
	 * 下一步按钮点击
	 */
	@OnClick(R.id.btnRight)
	@CheckNet
	@Duplicate("AsyncResultFragment.java")
	private void clickNext(View view) {
		if (btnRight.getText().toString().equals(getString(R.string.next))||btnRight.getText().toString().equals("开始预授信")) {
			mRootFragment.push();
		} else if (btnRight.getText().toString().equals(getString(R.string.over))) {
			HttpRequest.over(getActivity(), mType == BANK_CARD ? "05" : "06", new IHttpCallback<String>() {

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
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		LogUtils.e("AsyncResultFragment----->onDestroy取消请求"+UrlConfig.ASYNC_RESULT);
		mHandler.removeMessages(START_CUTDOWN_WHAT);
		super.onDestroy();
	}
}
