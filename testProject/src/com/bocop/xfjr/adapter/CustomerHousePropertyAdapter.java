package com.bocop.xfjr.adapter;

import java.util.List;

import com.bocop.jxplatform.R;
import com.bocop.xfjr.adapter.recycleradapter.CommonRecyclerAdapter;
import com.bocop.xfjr.adapter.recycleradapter.RecyclerViewHolder;
import com.bocop.xfjr.bean.detail.HouseBean;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

/**
 * 客户房产信息
 * 
 * @author wujunliu
 *
 */
public class CustomerHousePropertyAdapter extends CommonRecyclerAdapter<HouseBean> {

	private Context context;

	public CustomerHousePropertyAdapter(Context context, List<HouseBean> data) {
		super(context, data, R.layout.xfjr_item_customer_property_info);
		this.context = context;
	}

	@Override
	public void convert(RecyclerViewHolder viewHolder, final HouseBean bean, final int position) {
		String roprietorTitle = context.getResources().getString(R.string.house_proprietor);
		String areaTitle = context.getResources().getString(R.string.house_area);
		String typeTitle = context.getResources().getString(R.string.house_type);
		String districtTitle = context.getResources().getString(R.string.house_district);
		TextView tvProprietorTitle = viewHolder.getView(R.id.tvProprietorTitle);
		TextView tvProprietorContent = viewHolder.getView(R.id.tvProprietorContent);
		TextView tvAreaTitle = viewHolder.getView(R.id.tvAreaTitle);
		TextView tvTypeTitle = viewHolder.getView(R.id.tvTypeTitle);
		TextView tvDistrictTitle = viewHolder.getView(R.id.tvDistrictTitle);
		View vLine = viewHolder.getView(R.id.vLine);
		tvProprietorTitle.setText(String.format(roprietorTitle, position + 1));
		tvAreaTitle.setText(String.format(areaTitle, position + 1));
		tvTypeTitle.setText(String.format(typeTitle, position + 1));
		tvDistrictTitle.setText(String.format(districtTitle, position + 1));
		tvProprietorContent.setText(bean.getHousesOwner());
		if (getItemCount() - 1 == position) {
			vLine.setVisibility(View.GONE);
		}
	}
}