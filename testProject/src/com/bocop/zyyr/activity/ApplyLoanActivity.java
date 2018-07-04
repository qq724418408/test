package com.bocop.zyyr.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.boc.jx.tools.DialogUtil;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.config.TransactionValue;
import com.bocop.jxplatform.util.BocOpUtil;
import com.bocop.jxplatform.util.BocOpUtil.CallBackBoc;
import com.bocop.jxplatform.util.BocOpUtilWithoutDia;
import com.bocop.jxplatform.util.BocOpUtilWithoutDia.CallBackBoc2;
import com.bocop.jxplatform.util.CspUtil;
import com.bocop.jxplatform.util.JsonUtils;
import com.bocop.jxplatform.util.LoginUtil;
import com.bocop.xms.utils.KeyboardUtils;
import com.bocop.yfx.bean.MsgCode;
import com.bocop.yfx.utils.DataFormatUtil;
import com.bocop.yfx.utils.TimeCountUtil;
import com.bocop.zyyr.bean.ProductDetails;
import com.google.gson.Gson;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * 申请贷款
 * 
 * @author lh
 * 
 */
@ContentView(R.layout.zyyr_activity_apply_loan)
public class ApplyLoanActivity extends BaseActivity {

	@ViewInject(R.id.tv_titleName)
	private TextView tvTitle;
	@ViewInject(R.id.etLoanSum)
	private EditText etLoanSum;
	@ViewInject(R.id.tv2)
	private TextView tv2;
	@ViewInject(R.id.etCheckCode)
	private EditText etCheckCode;
	@ViewInject(R.id.tvLoanSum)
	private TextView tvLoanSum;
	@ViewInject(R.id.tvDeadLine)
	private TextView tvDeadLine;
	@ViewInject(R.id.tvArea)
	private TextView tvArea;//区域
	@ViewInject(R.id.tvLoan)
	private TextView tvLoan;
	@ViewInject(R.id.tvInterest)
	private TextView tvInterest;
	@ViewInject(R.id.tvRefundPM)
	private TextView tvRefundPM;
	@ViewInject(R.id.tvDeadLineChoser)
	private TextView tvDeadLineChoser;
	@ViewInject(R.id.tvGetCheckCode)
	private TextView tvGetCheckCode;

	private ProductDetails details;
	private List<String> periodList = new ArrayList<>();
	private String phone;
	private double totalInterest = 0;
	private double refundPM = 0;
	private int position;
	
	private String[] areaKeyArray;// 区域键
	private String[] areaValueArray;// 区域值
	private String userCity = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		tvTitle.setText("申请贷款");

		if (null != getIntent().getSerializableExtra("DETAILS")) {
			details = (ProductDetails) getIntent().getSerializableExtra("DETAILS");
		}
		if (null != getIntent().getStringArrayListExtra("PERIOD")) {
			periodList.addAll(getIntent().getStringArrayListExtra("PERIOD"));
			tvDeadLineChoser.setText(periodList.get(0) + "期");
		}
		if (null != getIntent().getStringExtra("PHONE")) {
			phone = getIntent().getStringExtra("PHONE");
		}
		initListener();
		String[] areaArray = getResources().getStringArray(R.array.zyyr_area_array);
		areaKeyArray = new String[areaArray.length];
		areaValueArray = new String[areaArray.length];
		for (int i = 0; i < areaArray.length; i++) {
			if (areaArray[i].contains(",")) {
				areaKeyArray[i] = areaArray[i].split(",")[0];
				areaValueArray[i] = areaArray[i].split(",")[1];
			}
		}
		userCity = areaKeyArray[0];
		tvArea.setText(areaValueArray[0]);
	}

	private void initListener() {
		etLoanSum.addTextChangedListener(new TextChangeWatcher(etLoanSum));
		etLoanSum.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					checkLoanSum();
				}
			}
		});
	}

	@OnClick({ R.id.tvDeadLineChoser, R.id.tvArea, R.id.tvGetCheckCode, R.id.btnNext })
	public void onClick(View v) {
		if (checkLoanSum()) {
			switch (v.getId()) {
			case R.id.tvDeadLineChoser:
				KeyboardUtils.closeInput(this, tvDeadLineChoser);
				final String[] deadLineString = new String[periodList.size()];
				for (int i = 0; i < deadLineString.length; i++) {
					deadLineString[i] = periodList.get(i) + "期";
				}
				DialogUtil.showToSelect(this, "", deadLineString, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						position = which;
						tvDeadLineChoser.setText(deadLineString[which]);
						calculateAndSet();
					}
				});
				break;
			case R.id.tvArea:
				DialogUtil.showToSelect(this, "", areaValueArray, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						userCity = areaKeyArray[which];
						tvArea.setText(areaValueArray[which]);
					}
				});
				break;
			case R.id.tvGetCheckCode:
				KeyboardUtils.closeInput(this, tvGetCheckCode);
				tvGetCheckCode.setBackgroundResource(R.drawable.yfx_shape_count_check_code);
				TimeCountUtil time = new TimeCountUtil(tvGetCheckCode, 60);
				time.countTime();
				requestBocForTelMsg();
				break;

			case R.id.btnNext:
				if (checkData()) {
					requestBocopForCheckMsg();
					// onCheckSuccess();
				}
				break;
			}
		}
	}

	/**
	 * 发送验证码
	 */
	private void requestBocForTelMsg() {

		BocOpUtilWithoutDia bocOpUtil = new BocOpUtilWithoutDia(this);

		Gson gson = new Gson();
		Map<String, String> map = new HashMap<String, String>();
		map.put("usrid", LoginUtil.getUserId(this));
		map.put("usrtel", phone);
		map.put("randtrantype", TransactionValue.messageType);
		final String strGson = gson.toJson(map);

		bocOpUtil.postOpboc(strGson, TransactionValue.SA7114, new CallBackBoc2() {

			MsgCode msgCode;

			@Override
			public void onSuccess(String responStr) {
				Log.i("tag", responStr);
				try {
					msgCode = JsonUtils.getObject(responStr, MsgCode.class);
					Log.i("tag0", msgCode.getCode());
					if (msgCode.getCode().equals("0")) {
						Log.i("tag1", msgCode.getCode());
						Toast.makeText(ApplyLoanActivity.this, "已发送至" + msgCode.getPhoneNo() + "，请查收",
								Toast.LENGTH_LONG).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFinish() {

			}

			@Override
			public void onFailure(String responStr) {
				if (responStr.equals("0") || responStr.equals("1")) {
					Toast.makeText(ApplyLoanActivity.this, R.string.onFailure, Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(ApplyLoanActivity.this, responStr, Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onStart() {

			}
		});
	}

	/**
	 * 验证短信验证码
	 */
	private void requestBocopForCheckMsg() {
		Gson gson = new Gson();
		// List<Map<String,String>> list =new ArrayList<Map<String,String>>();
		Map<String, String> map = new HashMap<String, String>();
		map.put("usrid", LoginUtil.getUserId(this));
		// TODO
		map.put("usrtel", phone);
		map.put("randtrantype", TransactionValue.messageType);
		map.put("mobcheck", etCheckCode.getText().toString());
		final String strGson = gson.toJson(map);

		BocOpUtil bocOpUtil = new BocOpUtil(this);
		bocOpUtil.postOpboc(strGson, TransactionValue.SA7115, new CallBackBoc() {

			@Override
			public void onSuccess(String responStr) {
				onCheckSuccess();
				Log.i("tag", responStr);
			}

			@Override
			public void onStart() {
				Log.i("tag", "发送GSON数据：" + strGson);
			}

			@Override
			public void onFinish() {
			}

			@Override
			public void onFailure(String responStr) {
				// onCheckSuccess();
				CspUtil.onFailure(ApplyLoanActivity.this, responStr);
			}
		});
	}

	/**
	 * 短信验证成功后
	 */
	private void onCheckSuccess() {
		Intent intent = new Intent(this, ApplyAffirmActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("PRO_ID", details.getProId());
		bundle.putString("APP_AMT", etLoanSum.getText().toString());
		bundle.putString("APP_PERIOD", tvDeadLineChoser.getText().toString());
		bundle.putString("LOAN", tvLoan.getText().toString());
		bundle.putString("TOTAL_INTEREST", tvInterest.getText().toString());
		bundle.putString("REFUND_PM", tvRefundPM.getText().toString());
		bundle.putString("USER_CITY", userCity);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	private boolean checkData() {
		double loanSum = 0.0;
		double minAmt = 0.0;
		double maxAmt = 0.0;
		if (!TextUtils.isEmpty(details.getMinAmt())) {
			minAmt = Double.parseDouble(details.getMinAmt());
		}
		if (!TextUtils.isEmpty(details.getMaxAmt())) {
			maxAmt = Double.parseDouble(details.getMaxAmt());
		}
		if (TextUtils.isEmpty(etLoanSum.getText().toString())) {
			Toast.makeText(this, R.string.inputLoanAmount, Toast.LENGTH_SHORT).show();
			return false;
		} else if ("请选择".equals(tvDeadLineChoser.getText().toString())) {
			Toast.makeText(this, R.string.chooseLoanPeriod, Toast.LENGTH_SHORT).show();
			return false;
		} else if (TextUtils.isEmpty(etCheckCode.getText().toString())) {
			Toast.makeText(this, R.string.inputCheckCode, Toast.LENGTH_SHORT).show();
			return false;
		} else {
			loanSum = Double.parseDouble(etLoanSum.getText().toString()) * 10000;
			if (loanSum < minAmt || loanSum > maxAmt) {
				// Toast.makeText(this,
				// "贷款金额不可小于" + DataFormatUtil.moneyStringFormat(minAmt / 10000
				// + "") + "万元，或大于"
				// + DataFormatUtil.moneyStringFormat(maxAmt / 10000 + "") +
				// "万元",
				// Toast.LENGTH_SHORT).show();

				DialogUtil
						.showWithOneBtn(this,
								"贷款金额不可小于" + DataFormatUtil.moneyStringFormat(minAmt / 10000 + "") + "万元，或大于"
										+ DataFormatUtil.moneyStringFormat(maxAmt / 10000 + "") + "万元",
								new OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog, int which) {
										dialog.dismiss();
									}
								});
				return false;
			} else {
				return true;
			}
		}
	}

	private boolean checkLoanSum() {
		double loanSum = 0.0;
		double minAmt = 0.0;
		double maxAmt = 0.0;
		minAmt = Double.parseDouble(details.getMinAmt().replace(" ", ""));
		maxAmt = Double.parseDouble(details.getMaxAmt().replace(" ", ""));
		if (!TextUtils.isEmpty(etLoanSum.getText().toString())) {
			loanSum = Double.parseDouble(etLoanSum.getText().toString()) * 10000;
			if (loanSum < minAmt || loanSum > maxAmt) {

				DialogUtil
						.showWithOneBtn(this,
								"贷款金额不可小于" + DataFormatUtil.moneyStringFormat(minAmt / 10000 + "") + "万元，或大于"
										+ DataFormatUtil.moneyStringFormat(maxAmt / 10000 + "") + "万元",
								new OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog, int which) {
										dialog.dismiss();
									}
								});
				return false;
			} else {
				return true;
			}
		} else {
			DialogUtil.showWithOneBtn(this, "贷款金额不可小于" + DataFormatUtil.moneyStringFormat(minAmt / 10000 + "")
					+ "万元，或大于" + DataFormatUtil.moneyStringFormat(maxAmt / 10000 + "") + "万元", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			return false;
		}
	}

	/**
	 * 计算并设置总利息、月还款
	 */
	private void calculateAndSet() {
		double loanAmount = 0;
		if (!TextUtils.isEmpty(etLoanSum.getText().toString())) {
			loanAmount = Double.parseDouble(etLoanSum.getText().toString());
		}
		double rate = 0;
		if (details.getRate().contains("%")) {
			rate = Double.parseDouble(details.getRate().replaceAll("%", "")) / 100;
		} else {
			rate = Double.parseDouble(details.getRate());
		}
		double period = 0;
		if (!TextUtils.isEmpty(etLoanSum.getText().toString())) {
			period = Double.parseDouble(tvDeadLineChoser.getText().toString().replaceAll("期", ""));
			tvLoan.setText(details.getProLoanTime() + "天");
			tvDeadLine.setText(periodList.get(position) + "期");
			//贷款金额
			loanAmount = loanAmount * 10000;
			//月平均还款
			refundPM = loanAmount * rate * Math.pow((1 + rate), period) / (Math.pow((1 + rate), period) - 1);
			//总利息
			totalInterest = refundPM * period - loanAmount;
//			refundPM = (loanAmount + totalInterest) / period;
			tvInterest.setText(DataFormatUtil.moneyStringFormat(totalInterest + "") + "元");
			tvRefundPM.setText(DataFormatUtil.moneyStringFormat(refundPM + "") + "元");
		}
	}

	/**
	 * 限制输入小数点后两位
	 * 
	 */
	private class TextChangeWatcher implements TextWatcher {

		private EditText editText;
		private DecimalFormat format = new DecimalFormat(",##0.00");

		public TextChangeWatcher(EditText editText) {
			super();
			this.editText = editText;
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {

		}

		@Override
		public void afterTextChanged(Editable s) {
			String str = s.toString();
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
			if (!TextUtils.isEmpty(str)) {
				tvLoanSum.setText(format.format(Double.parseDouble(str)) + "万元");
				calculateAndSet();
			} else {
				tvLoanSum.setText(format.format(Double.parseDouble("0")) + "万元");
				tvInterest.setText(format.format(Double.parseDouble("0")) + "元");
				tvRefundPM.setText(format.format(Double.parseDouble("0")) + "元");
			}
		}
	}
}
