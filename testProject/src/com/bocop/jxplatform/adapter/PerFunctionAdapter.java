package com.bocop.jxplatform.adapter;

import java.util.List;

import com.bocop.jxplatform.R;
import com.bocop.jxplatform.bean.PerFunctionBean;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PerFunctionAdapter extends BaseAdapter {
	
	private List<PerFunctionBean> list;
	private Context context;

	public PerFunctionAdapter(List<PerFunctionBean> list, Context context) {
		this.list = list;
		this.context = context;
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
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.personal_function_item, null);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.iv_function);
            viewHolder.titleTv = (TextView) convertView.findViewById(R.id.tv_title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
		PerFunctionBean pf = list.get(position);
		viewHolder.imageView.setImageResource(pf.getImageRes());
		viewHolder.titleTv.setText(pf.getTitle());
		
//		ItemListener itemListener = new ItemListener(position); //监听器记录了所在行，于是绑定到各个控件后能够返回具体的行，以及触发的控件  
//		ItemListenera itemListenera = new ItemListenera(position);
//		viewHolder.imageView.setOnClickListener(itemListener);
//		viewHolder.titleTv.setOnClickListener(itemListenera);
		return convertView;
	}
	
	static class ViewHolder {
		ImageView imageView;
		TextView titleTv;
	}
	
	/*
	 * listview内部监听事件
	 */
//    class ItemListener implements OnClickListener {  
//        private int m_position;  
//
//        ItemListener(int pos) {  
//            m_position = pos;  
//        }  
//          
//        @Override  
//        public void onClick(View v) {  
//            Log.v("MyListView-click", "line:" + m_position + ":"+ v.getTag());  
//        }  
//    } 
//    
//    class ItemListenera implements OnClickListener {  
//        private int m_position;  
//
//        ItemListenera(int pos) {  
//            m_position = pos;  
//        }  
//          
//        @Override  
//        public void onClick(View v) {  
//            Log.v("MyListView-clicka", "line:" + m_position + ":"+ v.getTag());  
//        }  
//    } 

}
