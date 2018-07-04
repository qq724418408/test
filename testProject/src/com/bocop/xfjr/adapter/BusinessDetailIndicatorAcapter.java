package com.bocop.xfjr.adapter;

import java.util.ArrayList;
import java.util.List;

import com.bocop.jxplatform.R;
import com.bocop.xfjr.view.indicator.IndicatorAdapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * description： 我的业务页面，指示器的适配器
 * <p/>
 * Created by TIAN FENG on 2017年8月23日 QQ：27674569 Email: 27674569@qq.com
 * Version：1.0
 */
public class BusinessDetailIndicatorAcapter extends IndicatorAdapter<View> {

	// 指示器高亮
	private int mHighLightColor = Color.RED;
	// 指示器默认颜色
	private int mRestoreColor = Color.GRAY;
	// tab 名称集合
	private List<String> mTabNames;
	// 上下文
	private Context mContext;
	private List<View> itemView = new ArrayList<>();

	/**
	 * @param tabNames
	 *            tab 名称集合
	 * @param context
	 *            上下文
	 */
	public BusinessDetailIndicatorAcapter(List<String> tabNames, Context context) {
		super();
		this.mTabNames = tabNames;
		this.mContext = context;
		mHighLightColor = context.getResources().getColor(R.color.xfjr_red);
		mRestoreColor = context.getResources().getColor(R.color.xfjr_black3);
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
		View tabView = View.inflate(parent.getContext(), R.layout.xfjr_layout_tab_detail_indicator, null);
		// tabName
		TextView tabNameTv = (TextView) tabView.findViewById(R.id.tvStepName);
		// 设置tab名
		tabNameTv.setText(mTabNames.get(position));
		// 设置默认颜色
		tabNameTv.setTextColor(mRestoreColor);
		// TODO 图片没有处理
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
		TextView tabNameTv = (TextView) view.findViewById(R.id.tvStepName);
		tabNameTv.setTextColor(mHighLightColor);
		// TODO 图片没有处理
	}

	/**
	 * 重置默认状态
	 */
	@Override
	public void restoreIndicator(int position, View view) {
		TextView tabNameTv = (TextView) view.findViewById(R.id.tvStepName);
		tabNameTv.setTextColor(mRestoreColor);
		// TODO 图片没有处理
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
