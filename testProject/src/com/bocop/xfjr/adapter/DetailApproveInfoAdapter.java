package com.bocop.xfjr.adapter;

import java.util.List;

import com.bocop.jxplatform.R;
import com.bocop.xfjr.adapter.recycleradapter.CommonRecyclerAdapter;
import com.bocop.xfjr.adapter.recycleradapter.RecyclerViewHolder;
import com.bocop.xfjr.bean.detail.BusinessBasicInfoBean.AuditBean;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailApproveInfoAdapter extends CommonRecyclerAdapter<AuditBean> {

	public DetailApproveInfoAdapter(Context context, List<AuditBean> data) {
		super(context, data, R.layout.xfjr_item_business_detail_basic_approve);
	}

	@Override
	public void convert(RecyclerViewHolder viewHolder, AuditBean bean, int position) {
		View ivApproveUpLine = viewHolder.getView(R.id.ivApproveUpLine);
		View ivApproveDownLine = viewHolder.getView(R.id.ivApproveDownLine);
		View grayDot = viewHolder.getView(R.id.grayDot);
		ImageView redDot = viewHolder.getView(R.id.redDot);
		TextView tvApproveMsg = viewHolder.getView(R.id.tvApproveMsg);
		TextView tvApproveTime = viewHolder.getView(R.id.tvApproveTime);
//		LayoutParams params = (LayoutParams) tvApproveMsg.getLayoutParams();
//		params.height = LayoutParams.WRAP_CONTENT;
//		params.width = LayoutParams.WRAP_CONTENT;
//		tvApproveMsg.setLayoutParams(params);
//		tvApproveTime.setLayoutParams(params);
		redDot.setVisibility(View.GONE); // 隐藏灰点
		grayDot.setVisibility(View.VISIBLE); // 显示灰点
		ivApproveDownLine.setVisibility(View.VISIBLE); // 显示上边线
		ivApproveUpLine.setVisibility(View.VISIBLE); // 显示下边线
		// 显示灰点和上边线，其他隐藏
		if (getItemCount() - 1 == position) { // 如果是第一步
			redDot.setVisibility(View.GONE); // 隐藏红点
			grayDot.setVisibility(View.VISIBLE); // 显示灰点
			ivApproveUpLine.setVisibility(View.VISIBLE); // 显示上边线
			ivApproveDownLine.setVisibility(View.INVISIBLE); // 隐藏下边线
		}
		// 隐藏上边线，显示红点和下边线
		if (0 == position) { // 如果是最后一步
			redDot.setVisibility(View.VISIBLE); // 显示红点
			grayDot.setVisibility(View.GONE); // 隐藏灰点
			ivApproveUpLine.setVisibility(View.INVISIBLE); // 隐藏上边线
			ivApproveDownLine.setVisibility(View.VISIBLE); // 显示下边线
		}
		// 只显示一个红点
		if(getItemCount() == 1){ // 如果只有一条信息
			redDot.setVisibility(View.VISIBLE); // 显示红点
			grayDot.setVisibility(View.GONE); // 隐藏灰点
			ivApproveUpLine.setVisibility(View.INVISIBLE); // 隐藏上边线
			ivApproveDownLine.setVisibility(View.INVISIBLE); // 隐藏下边线
		}
		tvApproveMsg.setText(bean.getTips());
		//long time = Long.parseLong(bean.getTime());
		tvApproveTime.setText(bean.getTime());
	}

}