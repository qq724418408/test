package com.bocop.xms.adapter;

import java.util.ArrayList;

import com.bocop.jxplatform.R;
import com.bocop.jxplatform.activity.BMJFActivity;
import com.bocop.jxplatform.activity.WebActivity;
import com.bocop.jxplatform.util.CustomProgressDialog;
import com.bocop.jxplatform.util.LoginUtil;
import com.bocop.xms.activity.GuideActivity;
import com.bocop.xms.activity.MyMessageActivity;
import com.bocop.xms.activity.XmsWebActivity;
import com.bocop.xms.xml.message.MessageBean;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 我的消息adapter
 * 
 * @author hooyangr
 *
 */
public class MyMessageAdapter extends BaseAdapter {

	private ArrayList<MessageBean> list;
	private MyMessageActivity activity;
	private SharedPreferences sp;
	private Editor editor;
	private int typeID;

	public MyMessageAdapter(MyMessageActivity activity, ArrayList<MessageBean> list, int type) {
		this.activity = activity;
		this.list = list;
		this.typeID = type;
		sp = activity.getSharedPreferences(MyMessageActivity.SELECT_ROLE, Context.MODE_PRIVATE);
		editor = sp.edit();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(activity).inflate(R.layout.xms_item_my_message, null);
			holder = new ViewHolder();
			holder.llContent = (LinearLayout) convertView.findViewById(R.id.llContent);
			holder.ivRole = (ImageView) convertView.findViewById(R.id.ivRole);
			holder.ivIcon = (ImageView) convertView.findViewById(R.id.ivAccount);
			holder.ivDot = (ImageView) convertView.findViewById(R.id.ivDot);
			holder.tvContent = (TextView) convertView.findViewById(R.id.tvContent);
			holder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
			holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tvContent.setText(list.get(position).getContent());
		holder.tvTime.setText(list.get(position).getDate());

		final MessageBean messageBean = list.get(position);
		if (messageBean != null) {
			final String type = messageBean.getType();
			String date = messageBean.getDate();
			if (date != null && date.length() == 8) {
				date = date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8);
			}
			holder.tvTime.setText(date);
			holder.tvContent.setText(messageBean.getContent());
			if ("01".equals(type)) {
				holder.ivIcon.setImageResource(R.drawable.xms_icon_sf_small);
			} else if ("02".equals(type)) {
				holder.ivIcon.setImageResource(R.drawable.xms_icon_df_small);
			} else if ("03".equals(type)) {
				holder.ivIcon.setImageResource(R.drawable.xms_icon_rqf_small);
			} else if ("04".equals(type)) {
				holder.ivIcon.setImageResource(R.drawable.xms_icon_yxf_small);
			} else if ("05".equals(type)) {
				holder.ivIcon.setImageResource(R.drawable.xms_icon_ydf_small);
			} else if ("06".equals(type)) {
				holder.ivIcon.setImageResource(R.drawable.icon_secretary_message_jt);
			} else if ("07".equals(type)) {
				holder.ivIcon.setImageResource(R.drawable.xms_icon_lc_small);
			} else if ("20".equals(type)) {
				holder.ivIcon.setImageResource(R.drawable.xms_icon_gg_small);
			}
			if (sp.getBoolean(messageBean.getMessageId(), false)) {
				holder.ivDot.setVisibility(View.INVISIBLE);
			} else {
				holder.ivDot.setVisibility(View.VISIBLE);
			}
			if ("man".equals(activity.getCurrentRole())) {
				holder.ivRole.setImageResource(R.drawable.xms_icon_man);
			} else {
				holder.ivRole.setImageResource(R.drawable.xms_icon_woman);
			}
			holder.ivRole.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(activity, GuideActivity.class);
					activity.startActivity(intent);
					new Handler().postDelayed(new Runnable() {

						@Override
						public void run() {
							activity.showRoleDialog();
						}
					}, 200);
				}
			});
			holder.llContent.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					editor.putBoolean(messageBean.getMessageId(), true);
					editor.commit();
					notifyDataSetChanged();
					if (activity.getBaseApp().isNetStat()) {
						if (typeID == 0) {
							if (!"06".equals(type) && !"07".equals(type) && !"20".equals(type)) {
								Bundle bundle = new Bundle();
								bundle.putString("userId", LoginUtil.getUserId(activity));
								bundle.putString("token", LoginUtil.getToken(activity));
								Intent intent = new Intent(activity, BMJFActivity.class);
								intent.putExtras(bundle);
								activity.startActivity(intent);
							}
						} else if (typeID == 1 && messageBean.getUrl() != null) {
							Bundle bundle = new Bundle();
							bundle.putString("url", messageBean.getUrl());
							bundle.putString("name", "");
							Intent intent = new Intent(activity, WebActivity.class);
							intent.putExtras(bundle);
							activity.startActivity(intent);
						}

					} else {
						CustomProgressDialog.showBocNetworkSetDialog(activity);
					}
				}
			});
		}

		return convertView;
	}

	class ViewHolder {
		LinearLayout llContent;
		ImageView ivRole;
		ImageView ivIcon;// 图标
		ImageView ivDot;// 小红点
		TextView tvTitle;// 标题
		TextView tvTime;// 时间
		TextView tvContent;// 内容
	}

}
