package com.bocop.yfx.activity.stageprodetail;

import java.util.ArrayList;
import java.util.List;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.base.BaseAdapter;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.boc.jx.tools.DialogUtil;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.view.MyListView;
import com.bocop.xms.tools.ViewHolder;
import com.bocop.yfx.bean.SumPerStage;
import com.bocop.yfx.utils.DataFormatUtil;
import com.bocop.yfx.view.DiscreteSeekBar;
import com.bocop.yfx.view.DiscreteSeekBar.NumericTransformer;

/**
 * 
 * 现金分期申请
 * 
 * @author lh
 * 
 */
@ContentView(R.layout.yfx_activity_cash_apply)
public class CashApplyActivity extends BaseActivity {

	@ViewInject(R.id.tv_titleName)
	private TextView tvTitle;
	@ViewInject(R.id.tvReceiptCard)
	private TextView tvReceiptCard;
	@ViewInject(R.id.tvFundPurpose)
	private TextView tvFundPurpose;
	@ViewInject(R.id.etApplyStages)
	private EditText etApplyStages;
	@ViewInject(R.id.seekDeadline)
	private DiscreteSeekBar seekDeadline;
	@ViewInject(R.id.tvMin)
	private TextView tvMin;
	@ViewInject(R.id.tvMax)
	private TextView tvMax;
	@ViewInject(R.id.lvRefundDetails)
	private MyListView lvRefundDetails;

	private List<SumPerStage> list = new ArrayList<>();
	private BaseAdapter<SumPerStage> adapter;
	private int max = 7800;
	private int min = 300;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initData();
		initListener();
	}

	private void initData() {
		tvTitle.setText("现金分期申请");

		lvRefundDetails
				.setAdapter(adapter = new BaseAdapter<SumPerStage>(this, list, R.layout.yfx_item_refund_details) {

					@Override
					public void viewHandler(int position, SumPerStage t, View convertView) {
						TextView tvStages = ViewHolder.get(convertView, R.id.tvStages);
						TextView tvRefundPerS = ViewHolder.get(convertView, R.id.tvRefundPerS);
						TextView tvhChargePerS = ViewHolder.get(convertView, R.id.tvhChargePerS);
						ImageView ivCheck = ViewHolder.get(convertView, R.id.ivCheck);
						LinearLayout llRfundDetails = ViewHolder.get(convertView, R.id.llRfundDetails);
						if (null != t) {
							tvStages.setText(t.getStage());
							tvRefundPerS.setText(t.getSumPS());
							tvhChargePerS.setText(t.gethChargePS());
							if (t.isClicked()) {
								ivCheck.setVisibility(View.VISIBLE);
							} else {
								ivCheck.setVisibility(View.INVISIBLE);
							}
						}

					}
				});
	}

	private void initListener() {
		seekDeadline.setNumericTransformer(new NumericTransformer() {

			@Override
			public int transform(int value) {
				if (seekDeadline.isPressed()) {
					etApplyStages.setText(value + "");
				}
				calData(value);
				adapter.notifyDataSetChanged();
				return value;
			}
		});

		lvRefundDetails.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				for (int i = 0; i < list.size(); i++) {
					list.get(i).setClicked(false);
				}
				list.get(arg2).setClicked(true);
				adapter.notifyDataSetChanged();
			}
		});

		etApplyStages.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (!seekDeadline.isPressed()) {
					if (!TextUtils.isEmpty(s.toString())) {
						int input = (int) Double.parseDouble(s.toString());
						if (input > max) {
							input = max;
						} else if (input < min) {
							input = min;
						}
						seekDeadline.setProgress(input);
					}
				}
			}
		});
	}

	@OnClick({ R.id.tvReceiptCard, R.id.tvFundPurpose, R.id.btnPreview })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tvReceiptCard:
			final String[] reCardString = {};
			DialogUtil.showToSelect(this, "", reCardString, new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					tvReceiptCard.setText(reCardString[which]);
				}
			});
			break;

		case R.id.tvFundPurpose:
			final String[] purString = {};
			DialogUtil.showToSelect(this, "", purString, new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					tvReceiptCard.setText(purString[which]);
				}
			});
			break;

		case R.id.btnPreview:
			callMe(PreviewActivity.class);
			break;
		}
	}

	private void calData(int value) {

		double sum = value;

		SumPerStage perStage1 = new SumPerStage();
		SumPerStage perStage2 = new SumPerStage();
		SumPerStage perStage3 = new SumPerStage();
		SumPerStage perStage4 = new SumPerStage();
		SumPerStage perStage5 = new SumPerStage();
		SumPerStage perStage6 = new SumPerStage();

		perStage1.setSumPS("￥" + DataFormatUtil.moneyStringFormat(sum / 3 + ""));
		perStage1.setStage("3");
		perStage1.sethChargePS("￥" + "100.00");
		perStage1.setClicked(false);

		perStage2.setSumPS("￥" + DataFormatUtil.moneyStringFormat(sum / 6 + ""));
		perStage2.setStage("6");
		perStage2.sethChargePS("￥" + "100.00");
		perStage2.setClicked(true);

		perStage3.setSumPS("￥" + DataFormatUtil.moneyStringFormat(sum / 10 + ""));
		perStage3.setStage("10");
		perStage3.sethChargePS("￥" + "100.00");
		perStage3.setClicked(false);

		perStage4.setSumPS("￥" + DataFormatUtil.moneyStringFormat(sum / 12 + ""));
		perStage4.setStage("12");
		perStage4.sethChargePS("￥" + "100.00");
		perStage4.setClicked(false);

		perStage5.setSumPS("￥" + DataFormatUtil.moneyStringFormat(sum / 18 + ""));
		perStage5.setStage("18");
		perStage5.sethChargePS("￥" + "100.00");
		perStage5.setClicked(false);

		perStage6.setSumPS("￥" + DataFormatUtil.moneyStringFormat(sum / 24 + ""));
		perStage6.setStage("24");
		perStage6.sethChargePS("￥" + "100.00");
		perStage6.setClicked(false);

		list.clear();
		list.add(perStage1);
		list.add(perStage2);
		list.add(perStage3);
		list.add(perStage4);
		list.add(perStage5);
		list.add(perStage6);
	}
}
