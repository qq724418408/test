package com.bocop.jxplatform.adapter;

import java.util.ArrayList;
import java.util.List;

import com.bocop.jxplatform.R;
import com.bocop.jxplatform.bean.QztProgressBean;
import com.bocop.qzt.view.CustomTextView;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout.LayoutParams;

public class QztProgressAdapter extends BaseAdapter {

	private List<QztProgressBean> list;
	private Context context;
	private List<Float> nodeRadiusDistances;

	public QztProgressAdapter(List<QztProgressBean> list, Context context) {
		this.list = list;
		this.context = context;
		this.nodeRadiusDistances = new ArrayList<Float>();
		for (int i = 0; i < list.size() + 2; i++) {
			// 倒数第二个用于装载item顶部内边距,倒数第一个用于装载listview分割线高度
			this.nodeRadiusDistances.add(0.0f);
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		//这里只所以不用复用，是因为使用了复用之后测量出item的高度就不准确了
		convertView = LayoutInflater.from(context).inflate(
				R.layout.qzt_logistics_info_item_line, null);
		CustomTextView info_txt = (CustomTextView) convertView
				.findViewById(R.id.info_txt);
		CustomTextView dateTime_txt = (CustomTextView) convertView
				.findViewById(R.id.dateTime_txt);
		CustomTextView order_txt = (CustomTextView) convertView
				.findViewById(R.id.order_txt);
//		info_txt.setText(list.get(position).getProgress());
//		dateTime_txt.setText(list.get(position).getDate());
//		order_txt.setText(list.get(position).getProgressDetailInfo());
		info_txt.setText(list.get(position).getProgressDetailInfo());
		dateTime_txt.setText(list.get(position).getDate());
		order_txt.setText(list.get(position).getProgress());
//		order_txt.setText("123");
		//监听View的伸展，以便测量高度
//		ViewTreeObserver vto = info_txt.getViewTreeObserver();
//		vto.addOnGlobalLayoutListener(new MyGlobalLayoutListener(info_txt,
//				dateTime_txt, position));
		
		ViewTreeObserver vto = order_txt.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new MyGlobalLayoutListener(order_txt,info_txt,
				dateTime_txt, position));
		
		return convertView;
	}
	
	class ViewHolder{
		CustomTextView order_txt;
		CustomTextView info_txt;
		CustomTextView dateTime_txt;
	}

	public List<Float> getNodeRadiusDistances() {
		return nodeRadiusDistances;
	}

	/**
	 * 监听TextView高度的改变
	 * 
	 * @author zad
	 * 
	 */
	private class MyGlobalLayoutListener implements OnGlobalLayoutListener {

		// private ViewHolder holder;
		private int position;
		private CustomTextView order_txt;
		private CustomTextView info_txt;
		private CustomTextView dateTime_txt;

		public MyGlobalLayoutListener(CustomTextView order_txt,CustomTextView info_txt,
				CustomTextView dateTime_txt, int position) {
			// this.holder = holder;
			this.order_txt = order_txt;
			this.position = position;
			this.info_txt = info_txt;
			this.dateTime_txt = dateTime_txt;
			// 倒数第二个位置装载顶部内边距
			nodeRadiusDistances.set(nodeRadiusDistances.size() - 2,
					(float) info_txt.getParentPaddingTop());
		}

		@Override
		public void onGlobalLayout() {
			// TODO Auto-generated method stub
			/**
			 * 获取子项的真实高度
			 */
			float realHeight = order_txt.getHeight()
					+ order_txt.getParentPaddingTop()
					+ order_txt.getParentPaddingBotton()
					+ info_txt.getTxtHeight()
					+ dateTime_txt.getTxtHeight();
			LayoutParams childparams = (LayoutParams) order_txt
					.getLayoutParams();
			LayoutParams childparams1 = (LayoutParams) info_txt
					.getLayoutParams();
			realHeight += childparams.bottomMargin;
			realHeight += childparams1.bottomMargin;
			if (realHeight > nodeRadiusDistances.get(position)) {
				nodeRadiusDistances.set(position, realHeight);
			}
		}
	}
}
