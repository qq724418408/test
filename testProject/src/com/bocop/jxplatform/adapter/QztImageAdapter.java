package com.bocop.jxplatform.adapter;

import java.util.ArrayList;

import com.bocop.jxplatform.R;
import com.bocop.jxplatform.bean.app.AppInfo;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author luoyang
 * 
 *         描述：首页gridview适配器
 */
public class QztImageAdapter extends BaseAdapter {

	// 上下文
	private Context mContext;
	private String flag = "";
	// 填充器
	private LayoutInflater mInflater;

	private ArrayList<AppInfo> mData = new ArrayList<AppInfo>();
//	
	private static int[] imageIcons = new int[] { R.drawable.icon_qzt_cou_jiudian,R.drawable.icon_qzt_cou_wifi };

	private static String[] imageBigText = new String[] { "", "" };
	private static String[] imageSmallText = new String[] { "","",  };
	private static String[] methodName = new String[] { "startZhbbm", "pay" };

	public QztImageAdapter(Context context) {
		this.mContext = context;
		mInflater = LayoutInflater.from(mContext);
		buildData();
	}

	private void buildData() {
		for (int i = 0; i < imageIcons.length; i++) {
			AppInfo info = new AppInfo();
			info.iconId = imageIcons[i];
			info.name = imageBigText[i];
			info.otherName = imageSmallText[i];
			info.methodName = methodName[i];
			mData.add(info);
			Log.i("tag", flag);
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mData != null ? mData.size() : 0;
	}

	@Override
	public AppInfo getItem(int position) {
		return mData != null ? mData.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public ArrayList<AppInfo> getData() {
		return mData;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_home_image, null);
			holder.appIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
			holder.appName = (TextView) convertView.findViewById(R.id.tvbig);
			holder.otherName = (TextView) convertView.findViewById(R.id.tvsmall);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		if(position < 4){
			holder.appName.setTextColor(Color.rgb(0, 0, 0)); 
			holder.otherName.setTextColor(Color.rgb(0, 0, 0)); 
		}
		
		AppInfo info = (AppInfo) getItem(position);
		holder.appIcon.setImageResource(info.iconId);
		holder.appName.setText(info.name);
		holder.otherName.setText(info.otherName);

		return convertView;
	}

	class ViewHolder {
		ImageView appIcon;
		TextView appName;
		TextView otherName;
	}
}
