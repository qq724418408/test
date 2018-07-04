package com.bocop.xfjr.fragment.qzzc.bandcard;

import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.boc.jx.httptools.http.HttpUtils;
import com.boc.jx.httptools.http.callback.IHttpCallback;
import com.boc.jx.tools.LogUtils;
import com.bocop.jxplatform.R;
import com.bocop.xfjr.XfjrMain;
import com.bocop.xfjr.activity.XfjrIndexActivity;
import com.bocop.xfjr.activity.customer.XfjrMainActivity;
import com.bocop.xfjr.base.BaseCheckProcessFragment;
import com.bocop.xfjr.config.HttpRequest;
import com.bocop.xfjr.config.UrlConfig;
import com.bocop.xfjr.constant.XFJRConstant;
import com.bocop.xfjr.helper.BankCardHelper;
import com.bocop.xfjr.helper.ThrowableHelper;
import com.bocop.xfjr.observer.StepObserver;
import com.bocop.xfjr.observer.StepSubject;
import com.bocop.xfjr.util.PatternUtils;
import com.bocop.xfjr.util.PreferencesUtil;
import com.bocop.xfjr.util.XFJRUtil;
import com.bocop.xfjr.view.XFJRClearEditText;
import com.bocop.yfx.utils.ToastUtils;
import com.megvii.demo.util.Util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * description： 欺诈侦测第5个界面 银联信用卡验证
 * <p/>
 * Created by TIAN FENG on 2017年8月28日 QQ：27674569 Email: 27674569@qq.com
 * Version：1.0
 */
public class FraudDetectionFragment5 extends BaseCheckProcessFragment implements StepSubject {
	private static final int SCAN_CODE = 100;

	private StepObserver mObserver;
	@ViewInject(R.id.etBankCard)
	private XFJRClearEditText etBankCard;// 银行卡卡号
	@ViewInject(R.id.etPhonenumber)
	private XFJRClearEditText etPhonenumber;// 卡号相关的手机号码
	@ViewInject(R.id.etCheckNum)
	private EditText etCheckNum;// 验证码
	@ViewInject(R.id.tvUserName)
	private TextView tvUserName;// 姓名
	@ViewInject(R.id.tvUserId)
	private TextView tvUserId;// 身份证
	@ViewInject(R.id.ivScanBankCard)
	private ImageView ivScanBankCard;// 扫描信用卡验证
	@ViewInject(R.id.btnGetCheckNum)
	private Button btnGetCheckNum;// 发送验证码
	@ViewInject(R.id.btnCheckNum)
	private View btnCheckNum;// 检测合法性
	@ViewInject(R.id.btnLeft)
	private View btnLeft;// 上一步
	@ViewInject(R.id.btnRight)
	private Button btnRight;// 下一步
	@ViewInject(R.id.tvBiref)
	private TextView tvBiref;

	private BankCardHelper mBankCardHelper;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.xfjr_fragment_fraud_detection5, container, false);
	}

	@Override
	protected void initData() {
		mBankCardHelper = new BankCardHelper();
		mBankCardHelper.bankCardNumAddSpace(etBankCard, ' ');
	}

	/**
	 * 检测输入信息是否有误
	 */
	private boolean checkInfo(boolean isNext) {
		// 银行卡长度判断
		if (TextUtils.isEmpty(etBankCard.getText().toString())) {
			ToastUtils.show(getActivity(), getString(R.string.please_input_bank_num), 0);
			return false;
		}
		// 手机号码正则判断
		if (!PatternUtils.isMobile(etPhonenumber.getText().toString())) {
			ToastUtils.show(getActivity(), getString(R.string.please_input_phone_num), 0);
			return false;
		}
		// 验证码长度判断
		if (etCheckNum.getText().length() != 6) {
			ToastUtils.show(getActivity(), getString(R.string.error_check_num), 0);
			return false;
		}

		if (mObserver != null && !XfjrMain.isNet && isNext) {
			mObserver.pushBackStack();
		}
		return true;
	}

	@Override
	protected void initView() {

		btnLeft.setVisibility(View.GONE);
		btnRight.setVisibility(View.GONE);
		// 电话号码姓名身份证读取
		String userName = PreferencesUtil.get(XFJRConstant.KEY_USER_NAME, "");
		String userId = PreferencesUtil.get(XFJRConstant.KEY_USER_IDCARD, "");
		String userPhone = PreferencesUtil.get(XFJRConstant.KEY_CLIENT_PHONE, "");
		tvUserName.setText(userName);
		tvUserId.setText(userId);
		etPhonenumber.setText(userPhone);
	}

	@Override
	public void register(StepObserver observer) {
		mObserver = observer;
	}

	/**
	 * 扫描信用卡
	 */
	@OnClick(R.id.ivScanBankCard)
	private void scanBankCard(View view) {
		Intent intent = new Intent(getActivity(), com.megvii.demo.BankCardScanActivity.class);
		intent.putExtra(Util.KEY_ISDEBUGE, false);// 是否调试
		intent.putExtra(Util.KEY_ISALLCARD, false);// 是否扫描全卡
		startActivityForResult(intent, SCAN_CODE);
	}

	/**
	 * 发送验证码倒计时
	 */
	@OnClick(R.id.btnGetCheckNum)
	private void cutDown(View btn) {
		if (!PatternUtils.isMobile(etPhonenumber.getText().toString())) {
			ToastUtils.show(getActivity(), getString(R.string.please_input_phone_num), 0);
			return;
		}
		HttpUtils.with(getActivity()).url(UrlConfig.REQ_LOGIN_CODE).addParams("busiNo", XfjrMain.businessId)
				.addParams("telephone", etPhonenumber.getText().toString()).post(new IHttpCallback<String>() {

					@Override
					public void onSuccess(String url, String result) {
						// 开启倒计时
						mBankCardHelper.cutDown(btnGetCheckNum);
						ToastUtils.show(getActivity(), getString(R.string.get_check_num_success), 0);
					}

					@Override
					public void onError(String url, Throwable e) {
						LogUtils.e("e ->" + e);
						ToastUtils.show(getActivity(), getString(R.string.get_check_num_err), 0);
					}

					@Override
					public void onFinal(String url) {
					}
				});

	}

	/**
	 * 验证按钮点击
	 */
	@OnClick(R.id.btnCheckNum)
	private void btnCheckNum(View view) {
		if (checkInfo(false)) {
//			HttpRequest.checkBankCard(getActivity(), etBankCard.getText().toString(),
//					etPhonenumber.getText().toString(), etCheckNum.getText().toString(), new IHttpCallback<String>() {
//
//						@Override
//						public void onSuccess(String url, String result) {
//							tvBiref.setText("");
//							/**
//							 * 成功之后才能点击下一步
//							 */
//							btnRight.setOnClickListener(new OnClickListener() {
//
//								@Override
//								public void onClick(View v) {
//									checkInfo(true);
//								}
//							});
//						}
//
//						@Override
//						public void onError(String url, Throwable e) {
//							tvBiref.setText("信用卡信息与申请人信息不符");
//							/**
//							 * 失败之后结束申请 点击下一步
//							 */
//							btnRight.setText("结束申请");
//							btnRight.setOnClickListener(new OnClickListener() {
//
//								@Override
//								public void onClick(View v) {
//									onOverFinish();
////									getActivity().finish();
//								}
//
//							});
//						}
//
//						@Override
//						public void onFinal(String url) {
//							btnRight.setVisibility(View.VISIBLE);
//						}
//					});
		}
	}
	
	/**
	 * 结束申请
	 */
	private void onOverFinish() {
		HttpRequest.over(getActivity(), "05",  new IHttpCallback<String>() {

			@Override
			public void onSuccess(String url, String result) {
				((XfjrMainActivity)getActivity()).sendBR(0);
				// 结束申请
//				getActivity().finish();
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

	/**
	 * 跳转结果回调
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			onActivitySuccess(requestCode, data);
		}
	}

	/**
	 * 执行成功
	 */
	private void onActivitySuccess(int requestCode, Intent data) {
		if (data == null) {
			return;
		}
		switch (requestCode) {
		case SCAN_CODE:
			// 将银行卡号的结果显示到EditText
			String bankNum = data.getStringExtra("bankNum").replace(" ", "");
			etBankCard.setText(bankNum);
			etBankCard.setSelection(etBankCard.getText().length());
			break;
		default:
			break;
		}
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

}