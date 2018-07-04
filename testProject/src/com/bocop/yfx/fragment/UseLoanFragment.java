package com.bocop.yfx.fragment;

import java.io.UnsupportedEncodingException;

import com.boc.jx.base.BaseFragment;
import com.boc.jx.baseUtil.cache.CacheBean;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.boc.jx.tools.DialogUtil;
import com.boc.jx.view.LoadingView;
import com.boc.jx.view.LoadingView.OnRetryListener;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.config.TransactionValue;
import com.bocop.jxplatform.util.CspUtil;
import com.bocop.jxplatform.util.CspUtil.CallBack;
import com.bocop.jxplatform.util.LoginUtil;
import com.bocop.jxplatform.util.Mcis;
import com.bocop.xms.bean.ConstHead;
import com.bocop.xms.utils.XStreamUtils;
import com.bocop.yfx.activity.LoanMainActivity;
import com.bocop.yfx.activity.loanprodetail.PersonalInfoActivity;
import com.bocop.yfx.activity.loanprodetail.PreviewAuthentInfoActivity;
import com.bocop.yfx.activity.loanprodetail.RealNameAuthentActivity;
import com.bocop.yfx.activity.myloan.RemainingSumPickUpActivity;
import com.bocop.yfx.bean.RemainningSumResponse;
import com.bocop.yfx.utils.DataFormatUtil;
import com.bocop.yfx.xml.CspXmlYfxCom;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * 用款
 * 
 * @author lh
 * 
 */
public class UseLoanFragment extends BaseFragment {

	@ViewInject(R.id.llApply)
	private LinearLayout llApply;
	@ViewInject(R.id.btnPickUp)
	private Button btnPickUp;
	@ViewInject(R.id.tvTotalSum)
	private TextView tvTotalSum;
	@ViewInject(R.id.tvRemainningSum)
	private TextView tvRemainningSum;
	@ViewInject(R.id.llRemainning)
	private LinearLayout llRemainning;
	@ViewInject(R.id.tvStatus)
	private TextView tvStatus;
	// @ViewInject(R.id.tv1)
	// private TextView tv1;
	@ViewInject(R.id.loadingView)
	private LoadingView loadingView;
	private String status;
	private String totalSum;
	private String remainSum;
	private String amtStatus = "";

	/**
	 * 判断余额是否发生变化
	 */
	public static boolean LOAN_CHANGE_FLAG = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = initView(R.layout.yfx_fragment_useloan);
		return view;
	}

	@Override
	protected void initData() {
		super.initData();
		status = ((LoanMainActivity) baseActivity).status;
		if (!"6".equals(status) && !"5".equals(status) && !"3".equals(status)) {
//			llApply.setVisibility(View.VISIBLE);
			btnPickUp.setText("我要申请");
			btnPickUp.setVisibility(View.VISIBLE);
			llRemainning.setVisibility(View.GONE);
			// tv1.setVisibility(View.INVISIBLE);
		} else {
			requestRemainningSum();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (LOAN_CHANGE_FLAG) {// 如果余额发生变化，重新请求
			LOAN_CHANGE_FLAG = false;
			initData();
		}
	}

	@OnClick({ R.id.btnPickUp, R.id.ivIntro })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnPickUp:
			if ("5".equals(status)) {
				if ("1".equals(amtStatus)) {
					baseActivity.callMe(RemainingSumPickUpActivity.class);
				}
			} else if ("0".equals(status)) {
				baseActivity.callMe(RealNameAuthentActivity.class);
			} else if ("1".equals(status)) {
				baseActivity.callMe(PersonalInfoActivity.class);
			} else if ("2".equals(status)) {
				baseActivity.callMe(PreviewAuthentInfoActivity.class);
			} else if ("3".equals(status)) {
				Toast.makeText(baseActivity, R.string.applying, Toast.LENGTH_SHORT).show();
			} else if ("4".equals(status)) {
//				DialogUtil.showWithTwoBtn(baseActivity, "是否需要重新评估额度", "确定", "取消",
//						new DialogInterface.OnClickListener() {
//							@Override
//							public void onClick(DialogInterface dialog, int which) {
//								baseActivity.callMe(PreviewAuthentInfoActivity.class);
//							}
//						}, new DialogInterface.OnClickListener() {
//							@Override
//							public void onClick(DialogInterface dialog, int which) {
//								dialog.dismiss();
//							}
//						});
				baseActivity.callMe(PreviewAuthentInfoActivity.class);
			} else {
				((LoanMainActivity) baseActivity).radioProDetail.setChecked(true);
			}
			break;
		case R.id.ivIntro:
			String statusStr = tvStatus.getText().toString();
			String message = "";
			if ("已激活".equals(statusStr)) {
				message = "额度已成功激活，赶紧来体验“中银E贷”的快捷便利吧";
			} else if ("未激活".equals(statusStr)) {
				message = "您还未完成面签，赶紧就近前往您周边的中行营业网点吧";
			} else if ("已过期".equals(statusStr)) {
				message = "您的额度已到期，请重新发起额度申请，看看您的额度吧";
			} else if ("已冻结".equals(statusStr)) {
				message = "您的状态有异常，看看是不是忘记还款了，别影响用款了";
			}
			DialogUtil.showWithOneBtn(baseActivity, tvStatus.getText().toString(), message,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			break;

		}

	}

	/**
	 * 剩余可用额度查询
	 */
	private void requestRemainningSum() {
		try {
			CspXmlYfxCom cXmlYfxCom = new CspXmlYfxCom("WL002007", getCacheBean().get(CacheBean.CUST_ID).toString(), LoginUtil.getUserId(baseActivity));
			String strXml = cXmlYfxCom.getCspXml();
			// 生成MCIS报文
			Mcis mcis = new Mcis(strXml, TransactionValue.CSPSZF);
			byte[] byteMessage = {};
			byteMessage = mcis.getMcis();
			// 发送报文
			CspUtil cspUtil = new CspUtil(baseActivity);
			cspUtil.setFLAG_YFX_CSP(true);
			cspUtil.setTxCode("001007");
			cspUtil.postCspLogin(new String(byteMessage, "GBK"), new CallBack() {

				@Override
				public void onSuccess(String responStr) {
					onGetRemainningSuccess(responStr);
				}

				@Override
				public void onFinish() {

				}

				@Override
				public void onFailure(String responStr) {
					onGetRemainningFailure(responStr);
				}
			});
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 请求余额成功后
	 * 
	 * @param responStr
	 */
	private void onGetRemainningSuccess(String responStr) {
		RemainningSumResponse remainningSum = XStreamUtils.getFromXML(responStr, RemainningSumResponse.class);
		ConstHead constHead = remainningSum.getConstHead();
		if (constHead != null) {
			if ("00".equals(constHead.getErrCode())) {
				loadingView.setVisibility(View.GONE);
				llApply.setVisibility(View.GONE);
				btnPickUp.setText("提取");
				btnPickUp.setVisibility(View.VISIBLE);
				llRemainning.setVisibility(View.VISIBLE);
				amtStatus = remainningSum.getRemainningSum().getAmtStatus();
				String status = "";
				if ("1".equals(amtStatus)) {
					status = "已激活";
				} else if ("2".equals(amtStatus)) {
					status = "未激活";
				} else if ("3".equals(amtStatus)) {
					status = "已过期";
				} else if ("4".equals(amtStatus)) {
					status = "已冻结";
				}
				tvStatus.setText(status);
				totalSum = remainningSum.getRemainningSum().getTotalSum();
				remainSum = remainningSum.getRemainningSum().getRemainSum();
				if (!TextUtils.isEmpty(totalSum)) {
					ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, Float.parseFloat(totalSum));
					valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
	
						@Override
						public void onAnimationUpdate(ValueAnimator animation) {
							tvTotalSum.setText(DataFormatUtil.moneyStringFormat(animation.getAnimatedValue() + ""));
						}
					});
					valueAnimator.setDuration(1000);
					valueAnimator.start();
				} else {
					tvTotalSum.setText(DataFormatUtil.moneyStringFormat("0"));
				}
				if (!TextUtils.isEmpty(remainSum)) {
					ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, Float.parseFloat(remainSum));
					valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
	
						@Override
						public void onAnimationUpdate(ValueAnimator animation) {
							tvRemainningSum.setText(DataFormatUtil.moneyStringFormat(animation.getAnimatedValue() + ""));
						}
					});
					valueAnimator.setDuration(1000);
					valueAnimator.start();
				} else {
					tvRemainningSum.setText(DataFormatUtil.moneyStringFormat("0"));
				}
			} else if ("50".equals(constHead.getErrCode())) {
				DialogUtil.showWithToMain(baseActivity, constHead.getErrMsg());
			} else {
	//			if ("01".equals(constHead.getErrCode())) {
	//				Toast.makeText(baseActivity, "查无数据", Toast.LENGTH_SHORT).show();
	//			}
				onGetRemainningFailure(constHead.getErrMsg());
			}
		}
	}

	private void onGetRemainningFailure(String err_msg) {
		CspUtil.onFailure(baseActivity, err_msg);
		loadingView.setVisibility(View.VISIBLE);
		// tv1.setVisibility(View.GONE);
		llRemainning.setVisibility(View.GONE);
		btnPickUp.setVisibility(View.GONE);
		llApply.setVisibility(View.GONE);
		loadingView.setmOnRetryListener(new OnRetryListener() {

			@Override
			public void retry() {
				requestRemainningSum();
			}
		});
	}

}
