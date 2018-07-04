package com.bocop.xfjr.adapter;

import java.util.ArrayList;
import java.util.List;

import com.boc.jx.tools.LogUtils;
import com.bocop.jxplatform.R;
import com.bocop.xfjr.bean.pretrial.CustomType;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomerTypeListAdapter extends BaseAdapter {

	private List<CustomType> customTypes;
	private List<CustomType> listData;
	private OnClickDelDataListener onClickDelDataListener;
	private Context context;

	public CustomerTypeListAdapter(Context context, List<CustomType> listData) {
		super();
		this.listData = listData;
		this.context = context;
	}

	@Override
	public int getCount() {
		customTypes = new ArrayList<>();
		for (CustomType customType : listData) {
			if (customType.getResid() == R.layout.xfjr_item_pretrial_common_new) {
				LogUtils.e("getItemCount add:");
				customTypes.add(customType);
			}
		}
		for (CustomType customType : customTypes) {
			LogUtils.e(customType.toString());
		}
		LogUtils.e("getItemCount size:" + customTypes.size());
		return customTypes.size();
	}

	@Override
	public CustomType getItem(int position) {
		return customTypes.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.xfjr_item_lv_common_head_view, null);
			holder.textView = (TextView) convertView.findViewById(R.id.tvName);
			holder.ivDel = (ImageView) convertView.findViewById(R.id.ivDel);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		CustomType item = getItem(position);
		holder.textView.setText(item.gettName());
		holder.ivDel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onClickDelDataListener.onClickDelData(position);
			}
		});
		return convertView;
	}

	public List<CustomType> getDataSets() {
		return customTypes;
	}
	
	public void setOnClickDelDataListener(OnClickDelDataListener onClickDelDataListener) {
		this.onClickDelDataListener = onClickDelDataListener;
	}
	
	public interface OnClickDelDataListener {
		void onClickDelData(int position);
	}
	
	class ViewHolder {
		TextView textView;
		ImageView ivDel;
	}
}