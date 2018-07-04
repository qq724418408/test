package com.bocop.yfx.activity.myloan;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.baseUtil.cache.CacheBean;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.boc.jx.tools.DialogUtil;
import com.boc.jx.view.LoadingView;
import com.boc.jx.view.LoadingView.OnRetryListener;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.config.TransactionValue;
import com.bocop.jxplatform.util.BocOpUtil;
import com.bocop.jxplatform.util.BocOpUtil.CallBackBoc;
import com.bocop.jxplatform.util.CspUtil;
import com.bocop.jxplatform.util.CspUtil.CallBack;
import com.bocop.jxplatform.util.JsonUtils;
import com.bocop.jxplatform.util.LoginUtil;
import com.bocop.jxplatform.util.Mcis;
import com.bocop.xms.bean.ConstHead;
import com.bocop.xms.utils.KeyboardUtils;
import com.bocop.xms.utils.XStreamUtils;
import com.bocop.yfx.bean.CommonResponse;
import com.bocop.yfx.bean.RefundData;
import com.bocop.yfx.bean.RefundDataResponse;
import com.bocop.yfx.view.RefundPlanDialog;
import com.bocop.yfx.view.SlideSwitch;
import com.bocop.yfx.view.SlideSwitch.SlideListener;
import com.bocop.yfx.xml.CspXmlYfx011;
import com.bocop.yfx.xml.CspXmlYfx014;
import com.bocop.yfx.xml.CspXmlYfx017;
import com.bocop.yfx.xml.repayment.RepaymentBean;
import com.bocop.yfx.xml.repayment.RepaymentListResp;
import com.bocop.yfx.xml.repayment.RepaymentListXmlBean;
import com.google.gson.Gson;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 提前还款
 *
 * @author lh
 */
@ContentView(R.layout.yfx_activity_refund_in_advance)
public class RefundInAdvanceActivity extends BaseActivity {

	@ViewInject(R.id.tv_titleName)
	private TextView tvTitle;
	@ViewInject(R.id.loadingView)
	private LoadingView loadingView;
	@ViewInject(R.id.scrollView)
	private ScrollView scrollView;
	/**
	 * 剩余还款金额
	 */
	@ViewInject(R.id.tvRemainToRefund)
	private TextView tvRemainToRefund;
	/**
	 * 贷款总额
	 */
	@ViewInject(R.id.tvLoanAmount)
	private TextView tvLoanAmount;
	/**
	 * 未还本金
	 */
	@ViewInject(R.id.tvPrincipal)
	private TextView tvPrincipal;
	/**
	 * 逾期金额
	 */
	@ViewInject(R.id.tvOverdueAmt)
	private TextView tvOverdueAmt;
	/**
	 * 逾期金额单位
	 */
	@ViewInject(R.id.tvOverdueTab)
	private TextView tvOverdueTab;
	/**
	 * 本期利息
	 */
	@ViewInject(R.id.tvInterest)
	private TextView tvInterest;
	/**
	 * 本期应还
	 */
	@ViewInject(R.id.tvRefund)
	private TextView tvRefund;
	/**
	 * 还款金额
	 */
	@ViewInject(R.id.etRefundSum)
	private EditText etRefundSum;
	/**
	 * 还款卡号
	 */
	@ViewInject(R.id.tvRefundCard)
	private TextView tvRefundCard;
	/**
	 * 账号余额
	 */
	@ViewInject(R.id.tvAccBalance)
	private TextView tvAccBalance;
	
	@ViewInject(R.id.btnApply)
	private Button btnApply;
	// @ViewInject(R.id.tvRefundAll)
	// private TextView tvRefundAll;
	@ViewInject(R.id.swRefundAll)
	private SlideSwitch swRefundAll;
	@ViewInject(R.id.tvExplain)
	private TextView tvExplain;

	/**
	 * 剩余还款金额
	 */
	private String reAmount;
	private String accNo;
	/**
	 * 本期利息
	 */
	private double interest;
	/**
	 * 逾期金额
	 */
	private double overdueAmt;
	/**
	 * 还款列表
	 */
	private List<RepaymentBean> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initData();
		requestRefundData();
	}

	private void initData() {
		tvTitle.setText("提前还款");
		if (null != getIntent().getBundleExtra("BUNDLE")) {
			accNo = getIntent().getBundleExtra("BUNDLE").getString("ACCT_NO");
		}
	}

	private void initListener() {
		etRefundSum.addTextChangedListener(new TextChangeWatcher(etRefundSum));
		swRefundAll.setSlideListener(new SlideListener() {

			@Override
			public void open() {
				KeyboardUtils.closeInput(RefundInAdvanceActivity.this, swRefundAll);
				if (!TextUtils.isEmpty(tvRemainToRefund.getText().toString())) {
					etRefundSum.setEnabled(false);
					etRefundSum.setText(
							getDoubleParse(Double.parseDouble(tvRefund.getText().toString().replaceAll(" ", "") + "")));
				}
			}

			@Override
			public void close() {
				KeyboardUtils.closeInput(RefundInAdvanceActivity.this, swRefundAll);
				etRefundSum.setEnabled(true);
				etRefundSum.setText("");
			}
		});
	}

	private boolean checkData() {
		double refund = 0.0;
		if (!TextUtils.isEmpty(etRefundSum.getText().toString())) {
			refund = Double.parseDouble(etRefundSum.getText().toString());
		}
		if (TextUtils.isEmpty(etRefundSum.getText().toString())) {
			Toast.makeText(this, R.string.inputRefundAmount, Toast.LENGTH_SHORT).show();
			return false;
		} else if (interest > refund) {
			Toast.makeText(this, "您的还款金额低于应还利息，请修改并重新还款", Toast.LENGTH_SHORT).show();
			return false;
		} else {
			return true;
		}
	}

	@OnClick({ R.id.btnApply/** , R.id.tvRefundAll */, R.id.tvRefundPlan})
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnApply:
			KeyboardUtils.closeInput(this, btnApply);
			if (checkData()) {
				refundAffirm();
			}
			break;
		// case R.id.tvRefundAll:
		// KeyboardUtils.closeInput(this, tvRefundAll);
		// if (!TextUtils.isEmpty(tvRemainToRefund.getText().toString())) {
		// etRefundSum.setText(Double.parseDouble(tvRemainToRefund.getText().toString().replaceAll("
		// ", "")) + "");
		// }
		// break;
		case R.id.tvRefundPlan:
			if (list == null) {
				requestRefundPlan();
			} else {
				showRefundPlan();
			}
			break;
		}
	}
	
	private void showRefundPlan() {
		RefundPlanDialog dialog = new RefundPlanDialog(this);
		dialog.show(list);
	}
	
	/**
	 * 获取借记卡卡余额 
	 */
	private void requestBocopForCardBal(String cardNo) {
//		cardNo = "6013821800007656785";
		Gson gson = new Gson();
		Map<String,String> map = new HashMap<String,String>();
		//用户id
		map.put("custNo", LoginUtil.getUserId(this));
		//卡号
		map.put("cardNo", cardNo);
		final String strGson = gson.toJson(map);
		
		BocOpUtil bocOpUtil = new BocOpUtil(this);
		bocOpUtil.postOpboc(strGson, TransactionValue.DEBIT_BALANCE, new CallBackBoc() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String responStr) {
				try {
					Map<String, Object> map = JsonUtils.getMapObj(responStr);
					Map<String, String> strMap = (Map<String, String>) map.get("cardServiceDTO");
					if (strMap != null) {
						String cardBal = strMap.get("balance").toString();
						if (cardBal != null) {
							Float fCardBal = Float.parseFloat(cardBal)/100;
							tvAccBalance.setText(fCardBal.toString());
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void onStart() {
				Log.i("tag", "余额发送GSON数据：" + strGson);
			}
			
			@Override
			public void onFinish() {
				
			}
			
			@Override
			public void onFailure(String responStr) {
				
			}
		});
	}

	
	/**
	 * 请求剩余还款计划
	 */
	private void requestRefundPlan() {
		try {
			CspXmlYfx017 cspXmlYfx017 = new CspXmlYfx017(accNo);
			String strXml = cspXmlYfx017.getCspXml();
			// 生成MCIS报文
			Mcis mcis = new Mcis(strXml, TransactionValue.CSPSZF);
			byte[] byteMessage = mcis.getMcis();
			// 发送报文
			CspUtil cspUtil = new CspUtil(this);
			cspUtil.setFLAG_YFX_CSP(true);
			cspUtil.postCspLogin(new String(byteMessage, "GBK"), new CallBack() {

				@Override
				public void onSuccess(String responStr) {
					RepaymentListXmlBean xmlBean = RepaymentListResp.readStringXml(responStr);
					if ("00".equals(xmlBean.getErrorcode())) {
						if (xmlBean.getRepaymentList() != null) {
							list = xmlBean.getRepaymentList();
							showRefundPlan();
						} 
					} else if ("50".equals(xmlBean.getErrorcode())) {
						DialogUtil.showWithToMain(RefundInAdvanceActivity.this, xmlBean.getErrormsg());
					} else {
						CspUtil.onFailure(RefundInAdvanceActivity.this, xmlBean.getErrormsg());
					}
				}

				@Override
				public void onFinish() {

				}

				@Override
				public void onFailure(String responStr) {
					
				}
			});

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 请求还款数据
	 */
	private void requestRefundData() {
		try {
			CspXmlYfx014 cspXmlYfx014 = new CspXmlYfx014(getCacheBean().get(CacheBean.CUST_ID).toString(), accNo);
			String strXml = cspXmlYfx014.getCspXml();
			// 生成MCIS报文
			Mcis mcis = new Mcis(strXml, TransactionValue.CSPSZF);
			byte[] byteMessage = {};
			byteMessage = mcis.getMcis();
			// 发送报文
			CspUtil cspUtil = new CspUtil(this);
			cspUtil.setFLAG_YFX_CSP(true);
			cspUtil.setTxCode("001014");
			cspUtil.postCspLogin(new String(byteMessage, "GBK"), new CallBack() {

				@Override
				public void onSuccess(String responStr) {
					onGetDataSuccess(responStr);
				}

				@Override
				public void onFinish() {
					// TODO Auto-generated method stub

				}

				@Override
				public void onFailure(String responStr) {
					DialogUtil.showWithOneBtn(RefundInAdvanceActivity.this, responStr, new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							finish();
						}
					});
					loadingView.setVisibility(View.VISIBLE);
					loadingView.setmOnRetryListener(new OnRetryListener() {

						@Override
						public void retry() {
							requestRefundData();
						}
					});
				}
			});

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 请求数据成功后
	 */
	private void onGetDataSuccess(String responStr) {
		try {
			RefundDataResponse dataResponse = XStreamUtils.getFromXML(responStr, RefundDataResponse.class);
			ConstHead constHead = dataResponse.getConstHead();
			if (constHead != null) {
				if ("00".equals(constHead.getErrCode())) {
					loadingView.setVisibility(View.GONE);
					scrollView.setVisibility(View.VISIBLE);
					RefundData data = dataResponse.getRefundData();
					tvLoanAmount.setText(getDoubleParse(Double.parseDouble(data.getLoanAmount().replaceAll(" ", ""))));
					tvPrincipal.setText(getDoubleParse(Double.parseDouble(data.getRemainAmt().replaceAll(" ", ""))));
					tvInterest.setText(getDoubleParse(Double.parseDouble(data.getInterest().replaceAll(" ", ""))));
	
					double principal = Double.parseDouble(data.getRemainAmt().replaceAll(" ", ""));
					interest = Double.parseDouble(data.getInterest().replaceAll(" ", ""));
	
					reAmount = data.getRemainAmt().replaceAll(" ", "");
	
					if (!TextUtils.isEmpty(data.getOverdueAmt())) {
						overdueAmt = Double.parseDouble(data.getOverdueAmt().replaceAll(" ", ""));
					}
					tvOverdueAmt.setText(TextUtils.isEmpty(data.getOverdueAmt()) ? "0.00"
							: getDoubleParse(Double.parseDouble(data.getOverdueAmt().replaceAll(" ", ""))));
					tvRefund.setText(getDoubleParse(Double.parseDouble((principal + interest) + "")));
	
					tvRemainToRefund.setText(getDoubleParse(Double.parseDouble(principal + "")));
					tvRefundCard.setText(getHideCard(data.getRepayCard()));
					if (overdueAmt > 0) {
						tvTitle.setText("逾期还款");
						tvOverdueAmt.setTextColor(getResources().getColor(R.color.depth_red));
						tvOverdueTab.setTextColor(getResources().getColor(R.color.depth_red));
						tvExplain.setText("说明：如贷款已逾期，请先处理逾期金额。");
						swRefundAll.setVisibility(View.INVISIBLE);
						etRefundSum.setText(getDoubleParse(overdueAmt));
						etRefundSum.setEnabled(false);
					} else {
						swRefundAll.setVisibility(View.VISIBLE);
						tvExplain.setText("说明：还款金额不可低于应还利息");
						if (swRefundAll.getChecked()) {// 如果是“开”
							etRefundSum.setText(getDoubleParse(Double.parseDouble((principal + interest) + "")));
						} else {
							etRefundSum.setText("");
						}
					}
					initListener();
					requestBocopForCardBal(data.getRepayCard());
				} else if ("50".equals(constHead.getErrCode())) {
					DialogUtil.showWithToMain(RefundInAdvanceActivity.this, constHead.getErrMsg());
				} else {
					DialogUtil.showWithOneBtn(this, constHead.getErrMsg(), new OnClickListener() {
	
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							finish();
						}
					});
					loadingView.setVisibility(View.VISIBLE);
					scrollView.setVisibility(View.GONE);
					loadingView.setmOnRetryListener(new OnRetryListener() {
	
						@Override
						public void retry() {
							requestRefundData();
						}
					});
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 确认还款
	 */
	private void refundAffirm() {
		try {
			String refundSum = etRefundSum.getText().toString().replaceAll(",", "");
			CspXmlYfx011 cXmlYfx011 = new CspXmlYfx011(getCacheBean().get(CacheBean.CUST_ID).toString(), accNo,
					refundSum, interest + "");
			String strXml = cXmlYfx011.getCspXml();
			// 生成MCIS报文
			Mcis mcis = new Mcis(strXml, TransactionValue.CSPSZF);
			byte[] byteMessage = {};
			byteMessage = mcis.getMcis();
			// 发送报文
			CspUtil cspUtil = new CspUtil(this);
			cspUtil.setFLAG_YFX_CSP(true);
			cspUtil.setTxCode("001011");
			cspUtil.postCspLogin(new String(byteMessage, "GBK"), new CallBack() {

				@Override
				public void onSuccess(String responStr) {
					CommonResponse commonResponse = XStreamUtils.getFromXML(responStr, CommonResponse.class);
					ConstHead constHead = commonResponse.getConstHead();
					if (constHead != null) {
						if ("00".equals(constHead.getErrCode())) {
							// getActivityManager().finishAllWithoutActivity(LoanMainActivity.class);
							Intent intent = new Intent(RefundInAdvanceActivity.this, RefundSuccessActivity.class);
							intent.putExtra("ERR_CODE", constHead.getErrCode());
							intent.putExtra("ERR_MSG", constHead.getErrMsg());
							startActivity(intent);
						} else if ("50".equals(constHead.getErrCode())) {
							DialogUtil.showWithToMain(RefundInAdvanceActivity.this, constHead.getErrMsg());
						} else {
							CspUtil.onFailure(RefundInAdvanceActivity.this, constHead.getErrMsg());
						}
					}
				}

				@Override
				public void onFinish() {
					// TODO Auto-generated method stub

				}

				@Override
				public void onFailure(String responStr) {
					// TODO Auto-generated method stub
					CspUtil.onFailure(RefundInAdvanceActivity.this, responStr);

				}
			});
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private String getHideCard(String cardNo) {
		if (cardNo.length() == 19) {
			cardNo = cardNo.substring(0, 4) + "***********" + cardNo.substring(15);
		} else {
			cardNo = cardNo.substring(0, 4) + "***********" + cardNo.substring(12);
		}

		return cardNo;
	}

	/**
	 * 限制输入小数点后两位
	 */
	private class TextChangeWatcher implements TextWatcher {

		private EditText editText;
		double remain = Double.parseDouble(reAmount.replaceAll(",", ""));

		public TextChangeWatcher(EditText editText) {
			super();
			this.editText = editText;
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
		}

		@Override
		public void afterTextChanged(Editable s) {
			String str = s.toString();
			if (!TextUtils.isEmpty(str)) {
				if (str.startsWith(".")) {
					str = "0" + str;
					editText.setText(str);
					editText.setSelection(str.length());
				}
				if (str.contains(".")) {
					if (str.length() - str.lastIndexOf(".") > 3) {
						str = str.substring(0, str.lastIndexOf(".") + 3);
						editText.setText(str);
						editText.setSelection(str.length());
					}
				}
				// if (compare(str)) {
				// str = remain + "";
				// editText.setText(str);
				// editText.setSelection(str.length());
				// }
			}
		}

		private boolean compare(String s) {
			double refund = Double.parseDouble(s.replaceAll(",", ""));
			if (refund > remain) {
				return true;
			} else {
				return false;
			}
		}

	}

	@SuppressLint("DefaultLocale")
	private String getDoubleParse(double d) {
		return String.format("%.2f", d);
	}
}
