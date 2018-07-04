package com.bocop.yfx.view;

import java.util.List;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.base.BaseAdapter;
import com.bocop.jxplatform.R;
import com.bocop.xms.tools.ViewHolder;
import com.bocop.yfx.bean.ApplyHistory;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class ResultDialog extends ProgressDialog {

	private BaseActivity baseActivity;
	private ListView lvResult;
	private BaseAdapter<ApplyHistory> adapter;

	public ResultDialog(Context context) {
		super(context);
		baseActivity = (BaseActivity) context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.yfx_dialog_show_result);

		lvResult = (ListView) findViewById(R.id.lvResult);

	}

	public void show(List<ApplyHistory> aList) {
		this.show();
		setAdapter(aList);
	}

	private void setAdapter(List<ApplyHistory> aList) {
		lvResult.setAdapter(
				adapter = new BaseAdapter<ApplyHistory>(baseActivity, aList, R.layout.yfx_item_repayment_inventory) {

					@Override
					public void viewHandler(int position, ApplyHistory t, View convertView) {
						TextView tvRepaymentDate = ViewHolder.get(convertView, R.id.tvRepaymentDate);
						TextView tvPrincipal = ViewHolder.get(convertView, R.id.tvPrincipal);
						TextView tvInterest = ViewHolder.get(convertView, R.id.tvInterest);
						TextView tvInterestTotal = ViewHolder.get(convertView, R.id.tvInterestTotal);

						if (null != t) {
							tvRepaymentDate.setText(t.getRepaymentDate());
							tvRepaymentDate.setTextSize(14);
							tvPrincipal.setText(t.getPrincipal());
							tvPrincipal.setTextSize(14);
							tvInterest.setText(t.getInterest());
							tvInterest.setTextSize(14);
							tvInterestTotal.setText(t.getInterestTotal());
							tvInterestTotal.setTextSize(14);
						}
					}
				});
	}

}
