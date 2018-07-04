package com.bocop.jxplatform.adapter;

import java.util.ArrayList;
import java.util.List;

import com.bocop.jxplatform.R;
import com.bocop.jxplatform.bean.app.AppInfo;
import com.bocop.xms.xml.sign.SignBean;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author wangzhongcai
 * 
 *         描述：首页gridview适配器
 */
public class XmsItemAdapter extends BaseAdapter {

	// 上下文
	private Context mContext;
	private String flag = "";
	private List<SignBean> list;

	// 缴费提醒
	private static int[] bankIcons = new int[] { R.drawable.icon_xms_sf, 
		R.drawable.icon_xms_df, R.drawable.icon_xms_rqf, R.drawable.icon_xms_yxds,
		R.drawable.icon_xms_ydtx, R.drawable.icon_xms_lc, R.drawable.blank, R.drawable.blank};
	// 提醒秘书
	private static int[] remindIcons = new int[] { R.drawable.xms_icon_my_message_pre,
			R.drawable.xms_icon_finance_message, R.drawable.xms_icon_bank_service_pre,
			R.drawable.xms_icon_remind_service_pre};
	// 金融秘书
	private static int[] jrServiceIcons = new int[]{R.drawable.icon_xms_deposit, 
			R.drawable.icon_xms_rate,R.drawable.icon_xms_fund, R.drawable.icon_xms_hx};
	// 生活秘书
	private static int[] otherIcons = new int[] { R.drawable.icon_xms_weather,R.drawable.icon_xms_dzdp,
		R.drawable.icon_xms_58home,R.drawable.icon_xms_spider,R.drawable.icon_xms_express,
		R.drawable.icon_xms_tranlate,R.drawable.icon_xms_toutiao,R.drawable.icon_xms_boce};
	// 出行秘书
	private static int[] necessaryIcons = new int[] { R.drawable.icon_xms_didi, 
			R.drawable.icon_xms_fight,R.drawable.icon_xms_train,R.drawable.icon_xms_lv100,
			R.drawable.icon_xms_baidu,R.drawable.icon_xms_jiudian,R.drawable.blank,R.drawable.blank};

	// app名称集合
	private  String[] sAppNames;

	// 填充器
	private LayoutInflater mInflater;

	private ArrayList<AppInfo> mData = new ArrayList<AppInfo>();

	public XmsItemAdapter(Context context) {
		this.mContext = context;
		mInflater = LayoutInflater.from(mContext);
		sAppNames = mContext.getResources().getStringArray(
				R.array.appname_array);
		buildData();
	}
	
	public XmsItemAdapter(Context context, int appNamesID, String flag, List<SignBean> list){
		this.mContext = context;
		this.mInflater = LayoutInflater.from(mContext);
		this.sAppNames = mContext.getResources().getStringArray(appNamesID);
		this.flag = flag;
		this.list=list;
		buildData();
	}

	public XmsItemAdapter(Context context, int appNamesID, String flag) {
		this.mContext = context;
		this.mInflater = LayoutInflater.from(mContext);
		this.sAppNames = mContext.getResources().getStringArray(appNamesID);
		this.flag = flag;
		buildData();
	}

	private void buildData() {
		if ("0".equals(flag)) {
			for (int i = 0; i < sAppNames.length; i++) {
				AppInfo info = new AppInfo();
				info.iconId = bankIcons[i];
				info.name = sAppNames[i];
				mData.add(info);
				Log.i("tag", flag);
			}
		} else if ("1".equals(flag)) {
			for (int i = 0; i < sAppNames.length; i++) {
				AppInfo info = new AppInfo();
				info.iconId = remindIcons[i];
				info.name = sAppNames[i];
				mData.add(info);
				Log.i("tag", flag);
			}

		} else if ("2".equals(flag)) {
			for (int i = 0; i < sAppNames.length; i++) {
				AppInfo info = new AppInfo();
				info.iconId = jrServiceIcons[i];;
				info.name = sAppNames[i];
				mData.add(info);
				Log.i("tag", flag);
			}
		} else if ("3".equals(flag)) {
			for (int i = 0; i < sAppNames.length; i++) {
				AppInfo info = new AppInfo();
				info.iconId = otherIcons[i];
				info.name = sAppNames[i];
				mData.add(info);
				Log.i("tag", flag);
			}
		}else if("4".equals(flag)){
			for (int i = 0; i < sAppNames.length; i++) {
				AppInfo info = new AppInfo();
				info.iconId = necessaryIcons[i];
				info.name = sAppNames[i];
				mData.add(info);
				Log.i("tag", flag);
			}
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

	public void setList(List<SignBean> list) {
		this.list = list;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_home, null);
			holder.appIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
			holder.appName = (TextView) convertView.findViewById(R.id.tv_name);
			holder.ivState=(ImageView)convertView.findViewById(R.id.ivSignState);
			holder.tvMsgCount = (TextView) convertView.findViewById(R.id.tvMsgCount);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		AppInfo info = (AppInfo) getItem(position);
		holder.appIcon.setImageResource(info.iconId);
		
		if("0".equals(flag)){
			String type = "";
			if (position == 0) {
				type = "01";
			} else if (position == 1) {
				type = "02";
			} else if (position == 2) {
				type = "03";
			} else if (position == 3) {
				type = "04";
			} else if (position == 4) {
				type = "05";
			} else if (position == 5) {
				type = "07";
			}
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					if(type.equals(list.get(i).getType())){
						if("0".equals(list.get(i).getIsSigned())){//未签约
							holder.ivState.setVisibility(View.VISIBLE);
							holder.ivState.setImageResource(R.drawable.xms_icon_nosign);
						}else if("1".equals(list.get(i).getIsSigned())){
							holder.ivState.setVisibility(View.VISIBLE);
							holder.ivState.setImageResource(R.drawable.xms_icon_sign);
						}else if("2".equals(list.get(i).getIsSigned())){
							holder.ivState.setVisibility(View.GONE);
						}
						break;
						
					}
				}
			}
		}
		holder.appName.setText(info.name);
		if (info.getMsgCount() != 0){
			String msgCount = info.getMsgCount() > 9 ? "9+" :String.valueOf(info.getMsgCount());
			holder.tvMsgCount.setText(msgCount);
			holder.tvMsgCount.setVisibility(View.VISIBLE);
		} else {
			holder.tvMsgCount.setVisibility(View.INVISIBLE);
		}
		return convertView;
	}

	class ViewHolder {
		ImageView appIcon;
		TextView appName;
		ImageView ivState;
		TextView tvMsgCount;
	}
	
}
