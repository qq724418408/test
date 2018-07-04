package com.bocop.jxplatform.adapter;

import java.util.ArrayList;

import com.bocop.jxplatform.R;
import com.bocop.jxplatform.bean.app.AppInfo;

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
public class HomeItemAdapter extends BaseAdapter {

	// 上下文
	private Context mContext;
	private String flag = "";

	// app图标资源id
	private static int[] sAppIcons = new int[] { R.drawable.icon_cyh,
			R.drawable.icon_secretary, R.drawable.icon_yslc,
			R.drawable.icon_bmjf, R.drawable.icon_jket, R.drawable.icon_htzq,
			R.drawable.icon_boced, R.drawable.icon_qzt, R.drawable.icon_cft,
			R.drawable.icon_card, R.drawable.icon_weihui, R.drawable.icon_edai,
			R.drawable.icon_jiehui, R.drawable.icon_gouhui,
			R.drawable.icon_xdt, R.drawable.icon_ypt };
	// R.drawable.icon_bmjf,
	// R.drawable.icon_jket,R.drawable.icon_htzq,R.drawable.icon_boced,R.drawable.icon_qzt,R.drawable.icon_cft,R.drawable.icon_card,R.drawable.icon_weihui,R.drawable.icon_edai,R.drawable.icon_jiehui,R.drawable.icon_gouhui,R.drawable.icon_exy};
	//生活服务
//	private static int[] sLifeIcons = new int[] { R.drawable.icon_main_llhb,R.drawable.icon_main_mst,
//			R.drawable.icon_main_cht,R.drawable.icon_main_lyt, R.drawable.icon_main_qzt,
//			R.drawable.icon_main_jkt,R.drawable.icon_main_xyt,R.drawable.blank};
	//生活服务  20170221 删除缴费通
	private static int[] sLifeIcons = new int[] { R.drawable.icon_main_llhb,R.drawable.icon_main_mst,
		R.drawable.icon_main_cht,R.drawable.icon_main_lyt, R.drawable.icon_main_jft,R.drawable.icon_main_qzt,
		R.drawable.icon_main_jkt,R.drawable.icon_main_xyt};
	//金融服务
	private static int[] sFinanceIcons = new int[] {
		R.drawable.icon_main_khtcard, R.drawable.icon_main_bkt,R.drawable.icon_main_lct,R.drawable.icon_main_cft,
		R.drawable.icon_main_sht,R.drawable.icon_main_ght,R.drawable.icon_main_dzt,R.drawable.icon_main_bht,
		R.drawable.icon_main_ybt,R.drawable.icon_main_zqt,R.drawable.icon_main_yzf,R.drawable.blank};
	
//	R.drawable.icon_main_bkt, 办卡通
	//金融精准扶贫
	private static int[] sFpFinanceIcons = new int[] {
		R.drawable.icon_fpt_grfp,R.drawable.icon_fpt_qyfp,R.drawable.icon_fpt_lvfp,R.drawable.icon_lbfpsc};
	//授信服务
	private static int[] sFacilityIcons = new int[] {
		R.drawable.icon_main_qdt,R.drawable.icon_main_ydt,
		R.drawable.icon_main_fnt,R.drawable.icon_main_gdt, R.drawable.icon_main_gjt,R.drawable.icon_main_cyt
		,R.drawable.blank,R.drawable.blank};
	//消费购物
//	private static int[] sShoppimgIcons = new int[] { R.drawable.icon_main_ypt,R.drawable.icon_main_kht,
//			R.drawable.icon_main_wgt,R.drawable.blank };
	private static int[] sShoppimgIcons = new int[] {R.drawable.icon_main_kht, R.drawable.icon_main_ypt,
		R.drawable.icon_main_wgt,R.drawable.icon_main_tbw,R.drawable.icon_main_jd,R.drawable.icon_main_mtwm,
		//R.drawable.icon_main_jfsc,R.drawable.blank };//icon_main_jfsc 20170907 12:52注释
		R.drawable.icon_main_jfsc,R.drawable.xfjr_ic_logo };//20170907 12:52新增消费金融图标
	
	//生活服务方法
	private static String[] sLifeMethodName = new String[] { "gm", "smallSecretary",
			"startCyh", "startLyt", "pay", "startQzt", "health", "startXyt"};
	//金融服务方法
	private static String[] sFinanceMethodName = new String[] { "startKhtcard", "startCard", "liCai", "startCft", 
			"startJiehui", "startGouhui", "startDzt", "startBht", "startYbt", "startZqt", "startZyys", ""};//huiduitong
	//金融精准扶贫方法
	private static String[] sFpFinanceMethodName = new String[] { "startGrfpd", "startQyfpd",
			"startLvfpd", "startFpsc"};
	//授信服务方法
	private static String[] sFacilityMethodName = new String[] { "startQdt", "startYdt",
			"startFnt", "xiaodaitong", "startGjt", "startZdt", "", ""};
	//消费购物方法
	private static String[] sShoppimgMethodName = new String[] { "startKht", "youpingtong",
			//"htzq", "tbw", "jd", "mtwm", "startJfsc", ""};//startJfsc 20170907 12:52注释
			"htzq", "tbw", "jd", "mtwm", "startJfsc", "startXFJR"};//startXFJR 20170907 12:52新增消费金融方法
	
//	R.drawable.icon_main_bdwm,  百度外卖
	// app名称集合
	private  String[] sAppNames;

	// 填充器
	private LayoutInflater mInflater;

	private ArrayList<AppInfo> mData = new ArrayList<AppInfo>();
	//秘书通
	private AppInfo mstAppInfo;

	public HomeItemAdapter(Context context) {
		this.mContext = context;
		mInflater = LayoutInflater.from(mContext);
		sAppNames = mContext.getResources().getStringArray(R.array.appname_array);
		buildData();
	}

	public HomeItemAdapter(Context context, int appNamesID, String flag) {
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
				info.iconId = sLifeIcons[i];
				info.name = sAppNames[i];
				info.methodName = sLifeMethodName[i];
				mData.add(info);
				Log.i("tag", flag);
				if (info.iconId == R.drawable.icon_main_mst) {
					mstAppInfo = info;
				}
			}
		} else if ("1".equals(flag)) {
			for (int i = 0; i < sAppNames.length; i++) {
				AppInfo info = new AppInfo();
				info.iconId = sFinanceIcons[i];
				info.name = sAppNames[i];
				info.methodName = sFinanceMethodName[i];
				mData.add(info);
				Log.i("tag", flag);
			}

		} else if ("2".equals(flag)) {
			for (int i = 0; i < sAppNames.length; i++) {
				AppInfo info = new AppInfo();
				info.iconId = sShoppimgIcons[i];
				info.name = sAppNames[i];
				info.methodName = sShoppimgMethodName[i];
				mData.add(info);
				Log.i("tag", flag);
			}
		} else if ("3".equals(flag)) {
			for (int i = 0; i < sAppNames.length; i++) {
				AppInfo info = new AppInfo();
				info.iconId = sFacilityIcons[i];
				info.name = sAppNames[i];
				info.methodName = sFacilityMethodName[i];
				if (info.iconId == R.drawable.icon_main_fnt) {
					info.parameter = new Object[]{2};
				}
				mData.add(info);
				Log.i("tag", flag);
			}
		} else if ("4".equals(flag)) {
			for (int i = 0; i < sAppNames.length; i++) {
				AppInfo info = new AppInfo();
				info.iconId = sFpFinanceIcons[i];
				info.name = sAppNames[i];
				info.methodName = sFpFinanceMethodName[i];
				mData.add(info);
				Log.i("tag", flag);
			}
		}
		else {
			for (int i = 0; i < sAppNames.length; i++) {
				AppInfo info = new AppInfo();
				info.iconId = sAppIcons[i];
				info.name = sAppNames[i];
				mData.add(info);
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
	
	public ArrayList<AppInfo> getData() {
		return mData;
	}
	
	public AppInfo getMstAppInfo() {
		return mstAppInfo;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_home, null);
			holder.appIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
			holder.appName = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tvMsgCount = (TextView) convertView.findViewById(R.id.tvMsgCount);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		AppInfo info = (AppInfo) getItem(position);
		holder.appIcon.setImageResource(info.iconId);
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
		TextView tvMsgCount;
	}
}
