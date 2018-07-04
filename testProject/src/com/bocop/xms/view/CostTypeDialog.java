package com.bocop.xms.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.base.BaseAdapter;
import com.bocop.jxplatform.R;
import com.bocop.xms.bean.DialogCostType;
import com.bocop.xms.tools.ViewHolder;

public class CostTypeDialog extends Dialog {

	private BaseActivity baseActivity;
	private GridView gvCostType;
	private List<DialogCostType> costTypeList = new ArrayList<DialogCostType>();

	public CostTypeDialog(BaseActivity baseActivity) {
		super(baseActivity);
		this.baseActivity = baseActivity;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.xms_layout_cost_type_dialog);
		gvCostType = (GridView) findViewById(R.id.gv_CostType);
		initList();
	}

	private void initList() {
		DialogCostType dialogCostType1 = new DialogCostType();
		DialogCostType dialogCostType2 = new DialogCostType();
		DialogCostType dialogCostType3 = new DialogCostType();
		DialogCostType dialogCostType4 = new DialogCostType();
		DialogCostType dialogCostType5 = new DialogCostType();
		DialogCostType dialogCostType6 = new DialogCostType();

		dialogCostType1.setCostName("水费");
		dialogCostType1.setCostIcon(R.drawable.icon_secretary_sf);
		dialogCostType1.setTypeCode("01");
		dialogCostType2.setCostName("电费");
		dialogCostType2.setCostIcon(R.drawable.icon_secretary_df);
		dialogCostType2.setTypeCode("02");
		dialogCostType3.setCostName("燃气费");
		dialogCostType3.setCostIcon(R.drawable.icon_secretary_mqf);
		dialogCostType3.setTypeCode("03");
		dialogCostType4.setCostName("有线电视");
		dialogCostType4.setCostIcon(R.drawable.icon_secretary_yxds);
		dialogCostType4.setTypeCode("04");
		dialogCostType5.setCostName("移动通讯");
		dialogCostType5.setCostIcon(R.drawable.icon_secretary_ydtx);
		dialogCostType5.setTypeCode("05");
		dialogCostType6.setCostName("存款理财");
		dialogCostType6.setCostIcon(R.drawable.icon_secretary_fin);
		dialogCostType6.setTypeCode("07");

		costTypeList.add(dialogCostType1);
		costTypeList.add(dialogCostType2);
		costTypeList.add(dialogCostType3);
		costTypeList.add(dialogCostType4);
		costTypeList.add(dialogCostType5);
		costTypeList.add(dialogCostType6);
	}

	public void show(final CostTypeOnClickListener costTypeOnClickListener) {

		this.show();
		gvCostType.setAdapter(new BaseAdapter<DialogCostType>(baseActivity, costTypeList, R.layout.xms_item_cost_grid) {

			@Override
			public void viewHandler(int position, DialogCostType t, View convertView) {
				ImageView ivCostType = ViewHolder.get(convertView, R.id.iv_CostType);
				TextView tvCostType = ViewHolder.get(convertView, R.id.tv_CostType);

				if (null != t) {
					ivCostType.setImageResource(t.getCostIcon());
					tvCostType.setText(t.getCostName());
				}
			}
		});

		gvCostType.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				costTypeOnClickListener.OnCostTypeClick(costTypeList.get(position));
			}
		});
	}

	public interface CostTypeOnClickListener {
		public void OnCostTypeClick(DialogCostType costType);
	}

}
