package com.bocop.yfx.view;

import java.util.List;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.base.BaseAdapter;
import com.bocop.jxplatform.R;
import com.bocop.xms.tools.ViewHolder;
import com.bocop.yfx.xml.repayment.RepaymentBean;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
/**
 * 剩余还款计划
 * @author ftl
 *
 */
public class RefundPlanDialog extends ProgressDialog {

	private BaseActivity baseActivity;
	private TextView tvTitle;
	private ListView lvResult;

	public RefundPlanDialog(Context context) {
		super(context);
		baseActivity = (BaseActivity) context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.yfx_dialog_show_result);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		lvResult = (ListView) findViewById(R.id.lvResult);
		tvTitle.setText("剩余还款计划");
	}

	public void show(List<RepaymentBean> list) {
		this.show();
		setAdapter(list);
	}

	private void setAdapter(List<RepaymentBean> list) {
		lvResult.setAdapter(new BaseAdapter<RepaymentBean>(baseActivity, list, R.layout.yfx_item_repayment_inventory) {

				@Override
				public void viewHandler(int position, RepaymentBean t, View convertView) {
					TextView tvRepaymentDate = ViewHolder.get(convertView, R.id.tvRepaymentDate);
					TextView tvPrincipal = ViewHolder.get(convertView, R.id.tvPrincipal);
					TextView tvInterest = ViewHolder.get(convertView, R.id.tvInterest);
					TextView tvInterestTotal = ViewHolder.get(convertView, R.id.tvInterestTotal);

					if (null != t) {
						tvRepaymentDate.setText(t.getRepayDate());
						tvRepaymentDate.setTextSize(14);
						tvPrincipal.setText(t.getRepayCpt());
						tvPrincipal.setTextSize(14);
						tvInterest.setText(t.getRepayIst());
						tvInterest.setTextSize(14);
						tvInterestTotal.setText(t.getRepayAmt());
						tvInterestTotal.setTextSize(14);
					}
				}
			});
	}

}
