package com.bocop.jxplatform.adapter;

import java.util.ArrayList;

import com.bocop.jxplatform.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class QztListAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private ArrayList<ArrayList<String>> lists;
	
	public QztListAdapter(Context context, ArrayList<ArrayList<String>> lists) {
		super();
		this.context = context;
		this.lists = lists;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lists.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int index, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ArrayList<String> list = lists.get(index);
		if(view == null){
			view = inflater.inflate(R.layout.qzt_list_item, null);
		}
		view.setBackgroundColor(Color.WHITE);
		TextView textView1 = (TextView) view.findViewById(R.id.text1);
		TextView textView2 = (TextView) view.findViewById(R.id.text2);
		TextView textView3 = (TextView) view.findViewById(R.id.text3);
		TextView textView4 = (TextView) view.findViewById(R.id.text4);
		textView1.setTextColor(Color.BLACK);
		textView2.setTextColor(Color.BLACK);
		textView3.setTextColor(Color.BLACK);
		textView4.setTextColor(Color.BLACK);
		textView1.setText(list.get(0));
		textView2.setText(list.get(1));
		textView3.setText(list.get(2));
		textView4.setText(list.get(3));
		if(index == 0){
			view.setBackgroundResource(R.color.head_bg);
			textView1.setTextColor(Color.WHITE);
			textView2.setTextColor(Color.WHITE);
			textView3.setTextColor(Color.WHITE);
			textView4.setTextColor(Color.WHITE);
		}else{
			if(index%2 != 0){
				view.setBackgroundColor(Color.argb(250 ,  255 ,  255 ,  255 )); 
			}else{
				view.setBackgroundColor(Color.argb(250 ,  224 ,  243 ,  250 ));    
			}
		}
		
		return view;
	}

}
