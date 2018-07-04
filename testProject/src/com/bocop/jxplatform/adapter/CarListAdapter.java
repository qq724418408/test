package com.bocop.jxplatform.adapter;

import java.util.List;

import com.boc.jx.base.BaseApplication;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.bean.CarListBean;
import com.bocop.jxplatform.trafficassistant.CarInfoActivity;
import com.bocop.jxplatform.trafficassistant.CarPeccancyActivity;
import com.bocop.jxplatform.util.ActivityForResultCode;
import com.bocop.jxplatform.util.CustomProgressDialog;
import com.nhaarman.listviewanimations.ArrayAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.UndoAdapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author luoyang
 * @version 创建时间：2015-6-28 上午11:40:50 类说明
 */

public class CarListAdapter extends ArrayAdapter<String> implements UndoAdapter {

	private Context mContext;
	private List<CarListBean> carListDate;
	public BaseApplication baseApplication = BaseApplication.getInstance();
	
	public CarListAdapter(Context mContext, List<CarListBean> carListDate) {
		super();
		this.mContext = mContext;
		this.carListDate = carListDate;
		
		for (int i=0;i<carListDate.size();i++){
			add(carListDate.get(i).getTbLicenseNumber());
		}
	}

	public Context getmContext() {
		return mContext;
	}

	public void setmContext(Context mContext) {
		this.mContext = mContext;
	}

	public List<CarListBean> getCarListDate() {
		return carListDate;
	}

	public void setCarListDate(List<CarListBean> carListDate) {
		this.carListDate = carListDate;
	}

	@Override
	public long getItemId(final int position) {
		return getItem(position).hashCode();
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getView(final int position, final View convertView,
			final ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			view = LayoutInflater.from(mContext).inflate(
					R.layout.car_list_item, parent, false);
		}

		((TextView) view.findViewById(R.id.tv_licenseNumber))
				.setText(getItem(position));
		//查询车辆信息、车辆违法信息
		Button btCarInfo = new Button(mContext);
		Button btCarPeccancy = new Button(mContext);
		btCarInfo = (Button) view.findViewById(R.id.bt_licenseNumber);
		btCarPeccancy = (Button) view.findViewById(R.id.bt_car_peccancy);
		
		btCarInfo.setTag((String)getItem(position));
		btCarPeccancy.setTag((String)getItem(position));
		/**
		 * 查看车辆信息
		 */
		
		btCarInfo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//将车牌号信息传至下一个ACTIVITY
				if (baseApplication.isNetStat()){
					Bundle bundle = new Bundle();
					String str = v.getTag().toString();
					bundle.putString("licenseNum",str);
					Intent intent = new Intent(mContext,
							CarInfoActivity.class);
					intent.putExtras(bundle);
					((FragmentActivity) mContext).startActivityForResult(intent,ActivityForResultCode.CodeForCarGo);
				}else{
					CustomProgressDialog.showBocNetworkSetDialog(mContext);
				}
			}
		});
		
		/**
		 * 查看车辆违章信息
		 */
		btCarPeccancy.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//将车牌号信息传至下一个ACTIVITY
				if (baseApplication.isNetStat()){
					Bundle bundle = new Bundle();
					String str = v.getTag().toString();
					bundle.putString("licenseNum",str);
					Intent intent = new Intent(mContext,
							CarPeccancyActivity.class);
					intent.putExtras(bundle);
					mContext.startActivity(intent);
				}else{
					CustomProgressDialog.showBocNetworkSetDialog(mContext);
				}
				
			}
		});

		return view;
	}

	@NonNull
	@Override
	public View getUndoView(final int position, final View convertView,
			@NonNull final ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			view = LayoutInflater.from(mContext).inflate(R.layout.car_undo_row,
					parent, false);
		}
		return view;
	}

	@NonNull
	@Override
	public View getUndoClickView(@NonNull final View view) {
		return view.findViewById(R.id.undo_row_undobutton);
	}
	
//	class ViewHolder {
//		Button imageView;
//		Button titleTv;
//	}

}
