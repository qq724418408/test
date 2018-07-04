package com.bocop.xms.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bocop.jxplatform.R;
import com.bocop.xms.bean.SignContractInfo;

public class SignContractInfoDialog extends Dialog {

	private Context context;

	private TextView tvName;
	private TextView tvPayArea;
	private TextView tvPayUnit;
	private LinearLayout llUserCode;
	private TextView tvUserCode;
	private LinearLayout llSubscriberno;
	private TextView tvSubscriberno;
	private LinearLayout llExtra;
	private TextView tvExtraTitle;
	private TextView tvExtra;
	private TextView tvOrderDate;
	private Button btnPositive;
	private Button btnNegative;

	public SignContractInfoDialog(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.xms_layout_sign_contract_info_dialog);

		tvName = (TextView) findViewById(R.id.tv_Name);
		tvPayArea = (TextView) findViewById(R.id.tv_PayArea);
		tvPayUnit = (TextView) findViewById(R.id.tv_PayUnit);
		tvUserCode = (TextView) findViewById(R.id.tv_UserCode);
		tvOrderDate = (TextView) findViewById(R.id.tv_OrderDate);
		llSubscriberno = (LinearLayout) findViewById(R.id.ll_subscriberno);
		tvSubscriberno = (TextView) findViewById(R.id.tv_subscriberno);
		llUserCode = (LinearLayout) findViewById(R.id.ll_userCode);
		llExtra = (LinearLayout) findViewById(R.id.ll_Extra);
		tvExtraTitle = (TextView) findViewById(R.id.tv_ExtraTitle);
		tvExtra = (TextView) findViewById(R.id.tv_Extra);
		btnPositive = (Button) findViewById(R.id.btn_Positive);
		btnNegative = (Button) findViewById(R.id.btn_Negative);

	}

	/**
	 * @param info
	 * @param positiveOnClickListener
	 * @param negativeOnClickListener
	 */
	public void show(SignContractInfo info, final PositiveOnClickListener positiveOnClickListener,
			final NegativeOnClickListener negativeOnClickListener) {

		this.show();
		tvName.setText(info.getName());
		tvPayArea.setText(info.getArea());
		tvPayUnit.setText(info.getUnit());
		tvUserCode.setText(info.getUserCode());
		tvOrderDate.setText(info.getOrderDate());

		if (!TextUtils.isEmpty(info.getSubscriberno())) {
			llUserCode.setVisibility(View.GONE);
			llSubscriberno.setVisibility(View.VISIBLE);
			tvSubscriberno.setText(info.getSubscriberno());
		} else if (!TextUtils.isEmpty(info.getPinpaiN())) {
			llExtra.setVisibility(View.VISIBLE);
			tvExtraTitle.setText("缴费项目");
			tvExtra.setText(info.getPinpaiN());
		} else if (!TextUtils.isEmpty(info.getDevTyp())) {
			llExtra.setVisibility(View.VISIBLE);
			tvExtraTitle.setText("设备类型");
			tvExtra.setText(info.getDevTyp());
		}

		btnPositive.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				positiveOnClickListener.positiveOnClick();
			}
		});

		btnNegative.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				negativeOnClickListener.negativeOnClick();
			}
		});

	}

	public interface PositiveOnClickListener {
		public void positiveOnClick();
	}

	public interface NegativeOnClickListener {
		public void negativeOnClick();
	}

}
