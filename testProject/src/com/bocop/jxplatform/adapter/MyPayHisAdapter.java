
package com.bocop.jxplatform.adapter;

import java.util.List;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bocop.jxplatform.bean.CarPeccancyBean;
import com.bocop.jxplatform.R;
import com.nhaarman.listviewanimations.itemmanipulation.expandablelistitem.ExpandableListItemAdapter;

public class MyPayHisAdapter extends
		ExpandableListItemAdapter<Integer> {

	public final Context mContext;
	public List<CarPeccancyBean> mlistDates;

	/**
	 * Creates a new ExpandableListItemAdapter with the specified list, or an
	 * empty list if items == null.
	 */
	public MyPayHisAdapter(final Context context,
			List<CarPeccancyBean> listDates) {
		super(context, R.layout.item_myhispeccancy,
				R.id.fl_myhispeccancytitle,
				R.id.fl_myhispeccancycontext);
		mContext = context;
		mlistDates = listDates;
		Log.i("tag","adapter:" +  mlistDates.size());
		for (int i = 0; i < mlistDates.size(); i++) {
			add(i);
			Log.i("tag","adapter:" + i);
		}
	}

	@NonNull
	@Override
	public View getTitleView(final int position, View convertView,
			@NonNull final ViewGroup parent) {
		LinearLayout ly = (LinearLayout) convertView;
		if (ly == null) {
			ly = new LinearLayout(mContext);
			ly = (LinearLayout) LayoutInflater.from(mContext).inflate(
					R.layout.item_myhispeccancy_list_title, parent, false);
		}
		((TextView)ly.findViewById(R.id.tv_myhispeccancy_num)).setText(mlistDates.get(position).getPeccancyNum());
		((TextView)ly.findViewById(R.id.tv_myhispeccancy_paytime)).setText(mlistDates.get(position).getPapTime());
		((TextView)ly.findViewById(R.id.tv_myhispeccancy_factmoney)).setText(mlistDates.get(position).getFactMoney());
		Log.i("tag", "getTitleView");
		return ly;

	}

	@NonNull
	@Override
	public View getContentView(final int position, View convertView,
			@NonNull final ViewGroup parent) {
		Log.d("tag", "content_start");
		View ly = (LinearLayout) convertView;

		if (ly == null) {
			ly = new LinearLayout(mContext);
			ly = (LinearLayout) LayoutInflater.from(mContext).inflate(
					R.layout.item_myhispeccancy_list_context, parent, false);
		}
		Log.i("tag", "getContentView 0");
		((TextView)ly.findViewById(R.id.tv_myhispeccancy_licensenum)).setText(mlistDates.get(position).getPeccancyLicenseNum());
		((TextView)ly.findViewById(R.id.tv_myhispeccancy_drivenum)).setText(mlistDates.get(position).getDriveNum());
		((TextView)ly.findViewById(R.id.tv_myhispeccancy_time)).setText(mlistDates.get(position).getPeccancyTime());
		Log.i("tag", "getContentView 1");
		((TextView)ly.findViewById(R.id.tv_myhispeccancy_act)).setText(mlistDates.get(position).getPeccancyAct());
		((TextView)ly.findViewById(R.id.tv_myhispeccancy_peccancymoney)).setText(mlistDates.get(position).getPeccancyMoney());
		((TextView)ly.findViewById(R.id.tv_myhispeccancy_othermoney)).setText(mlistDates.get(position).getOtherMoney());
		Log.i("tag", "getContentView");
		return ly;
	}

}