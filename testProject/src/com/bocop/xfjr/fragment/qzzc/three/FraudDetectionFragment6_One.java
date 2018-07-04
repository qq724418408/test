package com.bocop.xfjr.fragment.qzzc.three;

import com.boc.jx.base.BaseFragment;
import com.boc.jx.baseUtil.view.annotation.Duplicate;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.boc.jx.httptools.http.callback.IHttpCallback;
import com.bocop.jxplatform.R;
import com.bocop.xfjr.XfjrMain;
import com.bocop.xfjr.bean.add.ProductBean;
import com.bocop.xfjr.config.HttpRequest;
import com.bocop.xfjr.config.UrlConfig;
import com.bocop.xfjr.fragment.qzzc.BaseSwich;
import com.bocop.xfjr.helper.BankCardHelper;
import com.bocop.xfjr.util.PatternUtils;
import com.bocop.xfjr.util.XFJRUtil;
import com.bocop.xfjr.view.XFJRClearEditText;
import com.bocop.yfx.utils.ToastUtils;
import com.megvii.demo.util.Util;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FraudDetectionFragment6_One extends BaseFragment {

	// 操作下一步的对象
	private BaseSwich mRootFragment;
	private ProductBean mProductBean;
	
	public FraudDetectionFragment6_One(){}
	
	public FraudDetectionFragment6_One(BaseSwich fragment_new5) {
		this.mRootFragment = fragment_new5;
		mProductBean = ((FraudDetectionFragment_new6)mRootFragment).productBean;
	}

	/*********************************************************************************************************************************/
	private static final int SCAN_CODE = 10011;
	@ViewInject(R.id.etBankCard)
	private XFJRClearEditText etBankCard;// 银行卡卡号
	@ViewInject(R.id.etPhonenumber)
	private XFJRClearEditText etPhonenumber;// 卡号相关的手机号码
	@ViewInject(R.id.etCheckNum)
	private EditText etCheckNum;// 验证码
	@ViewInject(R.id.tvUserId)
	private TextView tvUserId;// 身份证
	@ViewInject(R.id.ivScanBankCard)
	private ImageView ivScanBankCard;// 扫描信用卡验证
	@ViewInject(R.id.btnGetCheckNum)
	private Button btnGetCheckNum;// 发送验证码
	@ViewInject(R.id.btnCheckNum)
	private View btnCheckNum;// 检测合法性
	@ViewInject(R.id.btnLeft)
	private Button btnLeft;// 上一步
	@ViewInject(R.id.btnRight)
	private Button btnRight;// 下一步
	@ViewInject(R.id.tvBiref)
	private TextView tvBiref;
	@ViewInject(R.id.llMessag)
	private LinearLayout llMessag;// 短信验证整个布局
	@ViewInject(R.id.tvMessg)
	private View tvMessg;
	@ViewInject(R.id.tvMessgLine)
	private View tvMessgLine;
	@ViewInject(R.id.tvTitle)
	private TextView tvTitle;
	@ViewInject(R.id.tvBankPhone)
	private TextView tvBankPhone;
	@ViewInject(R.id.tvBankCardMsg)
	private TextView tvBankCardMsg;

	private BankCardHelper mBankCardHelper;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.xfjr_fragment_fraud_detection5, container, false);
	}

	@Override
	protected void initData() {
		 new BankCardHelper().bankCardNumAddSpace(etBankCard, ' ');
		 llMessag.setVisibility(View.GONE);
		 btnLeft.setVisibility(View.GONE);
		 tvMessg.setVisibility(View.GONE);
		 tvMessgLine.setVisibility(View.GONE);
		 tvBankPhone.setText("绑定手机号码");
//		 tvBankPhone.setVisibility(View.VISIBLE);
		 tvTitle.setText("第三方征信验证");
		 tvBankCardMsg.setText("请确认信用卡或银联卡信息");
	}
	
	@Override
	protected void initView() {
		mBankCardHelper = new BankCardHelper();
		btnRight.setText("下一步");
		// 手机号读取
		String userPhone = mProductBean.getTelephone();
		etPhonenumber.setText(userPhone);
		
		// 补全银行卡
		if(!TextUtils.isEmpty(mProductBean.getCustCardId())){
			etBankCard.setText(mProductBean.getCustCardId());
		}else if (!TextUtils.isEmpty(mProductBean.getCustCardIdThird())) {
			etBankCard.setText(mProductBean.getCustCardIdThird());
		}
	}

	/**
	 * 无银联卡点击
	 */
	@OnClick(R.id.btnLeft)
	private void liftClick(View view) {
		mRootFragment.push();
	}

	/**
	 * 提交点击
	 */
	@OnClick(R.id.btnRight)
	@Duplicate("FraudDetectionFragment6_One.java")
	private void rightClick(View view) {
		// 银行卡长度判断
		if (TextUtils.isEmpty(etBankCard.getText().toString())) {
			ToastUtils.show(getActivity(), getString(R.string.please_input_bank_num), 0);
			return ;
		}
		
		int bankLength = etBankCard.getText().toString().replace(" ", "").length();
		if(bankLength!=16&&bankLength!=19){
			ToastUtils.show(getActivity(), getString(R.string.please_input_bank_lenght), 0);
			return ;
		}
		
		mProductBean.setCustCardIdThird(etBankCard.getText().toString());
		// 手机号码正则判断
		if (!PatternUtils.isMobile(etPhonenumber.getText().toString())) {
			ToastUtils.show(getActivity(), getString(R.string.please_input_phone_num), 0);
			return ;
		}
//		mProductBean.setTelephone(etPhonenumber.getText().toString());
		if (!XfjrMain.isNet) {
			mRootFragment.swichAsync();
			return ;
		}else{
			// 提交验证
			sunmit();
		}
	}

	// 提交验证
	private void sunmit() {
		HttpRequest.checkOther(getActivity(), 
				etBankCard.getText().toString(), 
				etPhonenumber.getText().toString(), 
				mProductBean.getUserName(), 
				mProductBean.getIdCard(), 
				new IHttpCallback<String>() {

			@Override
			public void onSuccess(String url, String result) {
				mRootFragment.swichAsync();
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
	 * 扫描信用卡
	 */
	@OnClick(R.id.ivScanBankCard)
	private void scanBankCard(View view) {
		Intent intent = new Intent(getActivity(), com.megvii.demo.BankCardScanActivity.class);
		intent.putExtra(Util.KEY_ISDEBUGE, false);// 是否调试
		intent.putExtra(Util.KEY_ISALLCARD, false);// 是否扫描全卡
		if (XFJRUtil.cameraIsCanUse()) {
			getActivity().startActivityForResult(intent, SCAN_CODE);
		} else {
			ToastUtils.show(getActivity(), getString(R.string.are_you_sure_camera_can_use), 0);
		}
//		try {
//			getActivity().startActivityForResult(intent, SCAN_CODE);
//		} catch (Exception e) {
//			ToastUtils.show(getActivity(), "请确认是否开启相机权限", 0);
//		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
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
}