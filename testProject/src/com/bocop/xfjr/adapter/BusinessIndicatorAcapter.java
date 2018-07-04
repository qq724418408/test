package com.bocop.xfjr.adapter;

import java.util.ArrayList;
import java.util.List;

import com.bocop.jxplatform.R;
import com.bocop.xfjr.bean.TypeBean;
import com.bocop.xfjr.constant.XFJRConstant;
import com.bocop.xfjr.util.PreferencesUtil;
import com.bocop.xfjr.util.ScreenUtils;
import com.bocop.xfjr.util.TextUtil;
import com.bocop.xfjr.view.indicator.IndicatorAdapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * description： 我的业务页面，指示器的适配器
 * <p/>
 * Created by TIAN FENG on 2017年8月23日 QQ：27674569 Email: 27674569@qq.com
 * Version：1.0
 */
public class BusinessIndicatorAcapter extends IndicatorAdapter<View> {

	// 指示器高亮
	private int mHighLightColor = Color.RED;
	// 指示器默认颜色
	private int mRestoreColor = Color.GRAY;
	// tab 名称集合
	private List<TypeBean> mTabNames;
	private List<Integer> resSelectId;
	private List<Integer> resDefaultId;
	// 上下文
	private Context mContext;
	private List<View> itemView = new ArrayList<>();
	private boolean isRead;

	/**
	 * @param tabNames
	 *            tab 名称集合
	 * @param context
	 *            上下文
	 */
	public BusinessIndicatorAcapter(List<TypeBean> tabNames, List<Integer> resSelectId, List<Integer> resDefaultId,
			Context context) {
		super();
		this.mTabNames = tabNames;
		this.resSelectId = resSelectId;
		this.resDefaultId = resDefaultId;
		this.mContext = context;
		mHighLightColor = context.getResources().getColor(R.color.xfjr_red);
		mRestoreColor = context.getResources().getColor(R.color.xfjr_gray);
	}

	/**
	 * 多少个item
	 */
	@Override
	public int getCount() {
		return mTabNames == null ? 0 : mTabNames.size();
	}

	/**
	 * 当前item的view
	 */
	@Override
	public View getView(int position, ViewGroup parent) {
		// 注册tab布局
		View tabView = View.inflate(parent.getContext(), R.layout.xfjr_tab_indicator, null);
		// tabName
		TextView tabNameTv = (TextView) tabView.findViewById(R.id.tvStatus);
		ImageView ivTab = (ImageView) tabView.findViewById(R.id.ivStatus);
		ivTab.setImageResource(resDefaultId.get(position));
		// 设置tab名括号里面的字体大小不一样
		int frontSize = ScreenUtils.dip2px(mContext, 14);
		int behindSize = ScreenUtils.dip2px(mContext, 11);
		String tabName = TextUtil.MyBusinessTabName(mTabNames.get(position));
		tabNameTv.setText(TextUtil.setBracketsTextSmall(tabName, frontSize, behindSize));
		// 设置默认颜色
		tabNameTv.setTextColor(mRestoreColor);
		View vRedDot = tabView.findViewById(R.id.ivRedDot);
		isRead = PreferencesUtil.get(XFJRConstant.KEY_IS_READ + position, false);
		if (isRead || mTabNames.get(position).getNumber().equals("0")) { // 如果没有新数据
			vRedDot.setVisibility(View.GONE);
		}
		itemView.add(tabView);
		return tabView;
	}

	public List<View> getItemView() {
		return itemView;
	}

	/**
	 * 高亮状态
	 */
	@Override
	public void highLightIndicator(int position, View view) {
		TextView tabNameTv = (TextView) view.findViewById(R.id.tvStatus);
		tabNameTv.setTextColor(mHighLightColor);
		View vRedDot = view.findViewById(R.id.ivRedDot);
		vRedDot.setVisibility(View.GONE);
		ImageView ivTab = (ImageView) view.findViewById(R.id.ivStatus);
		ivTab.setImageResource(resSelectId.get(position));
		isRead = true; // 已读红点消失
		PreferencesUtil.put(XFJRConstant.KEY_IS_READ + position, isRead);
	}

	/**
	 * 重置默认状态
	 */
	@Override
	public void restoreIndicator(int position, View view) {
		TextView tabNameTv = (TextView) view.findViewById(R.id.tvStatus);
		tabNameTv.setTextColor(mRestoreColor);
		ImageView ivTab = (ImageView) view.findViewById(R.id.ivStatus);
		ivTab.setImageResource(resDefaultId.get(position));
	}

	/**
	 * 底部追踪器
	 */
	@Override
	public View getBottomTrackView(int itemWidth) {
		View view = new View(mContext);
		// 设置指示器宽度为tab宽，高度为xx像素
		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(itemWidth, 8);
		view.setLayoutParams(params);
		view.setBackgroundColor(mHighLightColor);
		return view;
	}

}
