/**
 * 
 */
package com.bocop.qzt;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.jx.base.BaseApplication;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.bean.CarListBean;
import com.bocop.jxplatform.bean.QztContactBean;
import com.bocop.jxplatform.trafficassistant.CarInfoActivity;
import com.bocop.jxplatform.trafficassistant.CarPeccancyActivity;
import com.bocop.jxplatform.util.ActivityForResultCode;
import com.bocop.jxplatform.util.CustomProgressDialog;
import com.nhaarman.listviewanimations.ArrayAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.UndoAdapter;

/** 
 * @author luoyang  E-mail: luoyang8714@163.com
 * @version 创建时间：2016-11-14 上午11:37:56 
 * 类说明 
 */
/**
 * @author zhongye
 * 
 */
public class QztContactListAdapter extends ArrayAdapter<QztContactBean> implements
		UndoAdapter {

	private Context mContext;
	private List<QztContactBean> qztContactListDate;
	public BaseApplication baseApplication = BaseApplication.getInstance();
	private int resultCode = 0;  

	/**
	 * 
	 */
	public QztContactListAdapter(Context mContext,
			List<QztContactBean> qztContactListDate) {
		super();
		this.mContext = mContext;
		this.qztContactListDate = qztContactListDate;

		for (int i = 0; i < qztContactListDate.size(); i++) {
			add(qztContactListDate.get(i));
		}
	}

	public Context getmContext() {
		return mContext;
	}

	public void setmContext(Context mContext) {
		this.mContext = mContext;
	}

	public List<QztContactBean> getQztContactListDate() {
		return qztContactListDate;
	}

	public void setQztContactListDate(List<QztContactBean> qztContactListDate) {
		this.qztContactListDate = qztContactListDate;
	}

	public BaseApplication getBaseApplication() {
		return baseApplication;
	}

	public void setBaseApplication(BaseApplication baseApplication) {
		this.baseApplication = baseApplication;
	}

	/**
	 * @param objects
	 */
	public QztContactListAdapter(@Nullable List<String> objects) {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public long getItemId(final int position) {
		return getItem(position).hashCode();
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(final int position, final View convertView,
			final ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			view = LayoutInflater.from(mContext).inflate(
					R.layout.qzt_contact_listview, parent, false);
		}

		LinearLayout llQztInfo = new LinearLayout(mContext);
		LinearLayout llQztInfoEdit = new LinearLayout(mContext);
		llQztInfo = (LinearLayout)view.findViewById(R.id.ll_qzt_list);
		llQztInfoEdit = (LinearLayout)view.findViewById(R.id.ll_qzt_infoedit);
		llQztInfo.setTag(getItem(position));
		llQztInfoEdit.setTag(getItem(position));
		
		TextView tvCardId;
		TextView tvName;
		TextView tvPhone;
		TextView tvMail;
		TextView tvAdress;
		tvCardId = (TextView)view.findViewById(R.id.tv_qzt_cardId);
		tvName = (TextView)view.findViewById(R.id.tv_qzt_name);
		tvPhone = (TextView)view.findViewById(R.id.tv_qzt_phone);
		tvMail = (TextView)view.findViewById(R.id.tv_qzt_mail);
		tvAdress = (TextView)view.findViewById(R.id.tv_qzt_adress);
		
		tvCardId.setText(getItem(position).getCardId());
		tvName.setText(getItem(position).getName());
		tvPhone.setText(getItem(position).getPhone());
		tvMail.setText(getItem(position).getMail());
		tvAdress.setText(getItem(position).getAddress());
		/**
		 * 查看车辆信息
		 */
		
		llQztInfo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//将车牌号信息传至下一个ACTIVITY
				if (baseApplication.isNetStat()){
					Bundle bundle = new Bundle();
					QztContactBean contactBean = (QztContactBean)v.getTag();
					bundle.putString("cardId",contactBean.getCardId());
					bundle.putString("name",contactBean.getName());
					bundle.putString("phone",contactBean.getPhone());
					bundle.putString("mail",contactBean.getMail());
					bundle.putString("address",contactBean.getAddress());
					Intent intent = new Intent();
					intent.putExtras(bundle);
					((Activity) mContext).setResult(resultCode, intent); 
					((Activity) mContext).finish();
				}else{
					CustomProgressDialog.showBocNetworkSetDialog(mContext);
				}
			}
		});
		/**
		 * 查看车辆违章信息
		 */
		llQztInfoEdit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//将车牌号信息传至下一个ACTIVITY
				if (baseApplication.isNetStat()){
					Bundle bundle = new Bundle();
					QztContactBean contactBean = (QztContactBean)v.getTag();
					bundle.putString("cardId",contactBean.getCardId());
					bundle.putString("name",contactBean.getName());
					bundle.putString("phone",contactBean.getPhone());
					bundle.putString("mail",contactBean.getMail());
					bundle.putString("address",contactBean.getAddress());
					Intent intent = new Intent(mContext,
							QztContactInfoActivity.class);
					intent.putExtras(bundle);
					((Activity) mContext).startActivityForResult(intent,ActivityForResultCode.CodeForQztContactEdit);
//					mContext.startActivity(intent);
				}else{
					CustomProgressDialog.showBocNetworkSetDialog(mContext);
				}
				
			}
		});

		return view;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.
	 * UndoAdapter#getUndoClickView(android.view.View)
	 */
	@Override
	@NonNull
	public View getUndoClickView(@NonNull View arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.
	 * UndoAdapter#getUndoView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	@NonNull
	public View getUndoView(int arg0, @Nullable View arg1,
			@NonNull ViewGroup arg2) {
		// TODO Auto-generated method stub
		return null;
	}

}
