package com.bocop.xfjr.adapter;

import java.util.List;

import com.bocop.jxplatform.R;
import com.bocop.xfjr.adapter.recycleradapter.CommonRecyclerAdapter;
import com.bocop.xfjr.adapter.recycleradapter.RecyclerViewHolder;
import com.bocop.xfjr.bean.MyBusinessBean;
import com.bocop.xfjr.constant.XFJRConstant;
import com.bocop.xfjr.util.ScreenUtils;
import com.bocop.xfjr.util.TextUtil;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutParams;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyBusinessListAdapter extends CommonRecyclerAdapter<MyBusinessBean> {

	private OnClickAddDataListener onClickAddDataListener;
	private OnClickMoreDataListener onClickMoreDataListener;

	public void setOnClickAddDataListener(OnClickAddDataListener onClickAddDataListener) {
		this.onClickAddDataListener = onClickAddDataListener;
	}

	public void setOnClickMoreDataListener(OnClickMoreDataListener onClickMoreDataListener) {
		this.onClickMoreDataListener = onClickMoreDataListener;
	}

	public MyBusinessListAdapter(Context context, List<MyBusinessBean> data) {
		super(context, data, R.layout.xfjr_item_my_applicaton_list);
	}

	/**
	 * // 待预审，没有预审金额，继续申请，更多详情
	 * // 待决策，没有预审金额，补充资料(已上传要变成灰色不可点击)，更多详情
	 * // 待审批，有预审金额，更多详情
	 * // 待放款，有预审金额，补充资料(已上传要变成灰色不可点击)，更多详情
	 * // 已放款，有放款金额，更多详情
	 * // 已拒绝，没有有预审金额，更多详情
	 */
	@Override
	public void convert(RecyclerViewHolder viewHolder, final MyBusinessBean bean, final int position) {
		boolean fileCommitted = getFileCommitted(bean);
		LinearLayout lltPreAmount = viewHolder.getView(R.id.lltPreAmount);
		TextView tvMerchantName = viewHolder.getView(R.id.tvMerchantName);
		TextView productType = viewHolder.getView(R.id.tvCategory);
		TextView tvApplyMoney = viewHolder.getView(R.id.tvApplyMoney);
		TextView tvAproveMoney = viewHolder.getView(R.id.tvAproveMoney);
		TextView tvAproveHint = viewHolder.getView(R.id.tvAproveHint);
		TextView tvCustomerName = viewHolder.getView(R.id.tvCustomerName);
		TextView tvBusinessId = viewHolder.getView(R.id.tvBusinessId);
		TextView tvFurtherInfo = viewHolder.getView(R.id.tvFurtherInfo); // 补充资料
		View vLine = viewHolder.getView(R.id.vLine);
		TextView tvMore = viewHolder.getView(R.id.tvMore); // 更多详情 右边按钮
		ImageView ivFlag = viewHolder.getView(R.id.ivFlag);
		String commercialNameStr = bean.getMerchantName();
		int frontSize = ScreenUtils.dip2px(mContext, 16);
		int behindSize = ScreenUtils.dip2px(mContext, 13);
		tvMerchantName.setText(TextUtil.setBracketsTextSmall(commercialNameStr, frontSize, behindSize));
		productType.setText(bean.getProductName());
		tvApplyMoney.setText(TextUtil.money$Format(bean.getApplyMoney()));
		tvCustomerName.setText(bean.getCustomerName());
		tvBusinessId.setText(bean.getBusinessId());
		int status = Integer.parseInt(bean.getStatus());
		switch (status) {
		case XFJRConstant.C_STATUS_0_INT: // 待预审，没有预审金额，继续申请，更多详情  
			lltPreAmount.setVisibility(View.GONE);
			tvFurtherInfo.setVisibility(View.VISIBLE);// 左边按钮
//			vLine.setVisibility(View.VISIBLE);
			ivFlag.setVisibility(View.GONE);
			tvFurtherInfo.setText("继续申请");
			break;
		case XFJRConstant.C_STATUS_1_INT: // 待决策，没有预审金额，补充资料，更多详情
			lltPreAmount.setVisibility(View.GONE);
			tvFurtherInfo.setVisibility(View.VISIBLE);
			vLine.setVisibility(View.VISIBLE);
			ivFlag.setVisibility(View.GONE);
			tvFurtherInfo.setText(R.string.further_info);
			break;
		case XFJRConstant.C_STATUS_2_INT: // 待审批，有预审金额，更多详情
			tvAproveHint.setText("预审金额");
			tvAproveMoney.setText(TextUtil.money$Format(bean.getApproveMoney()));
			lltPreAmount.setVisibility(View.VISIBLE);
			tvFurtherInfo.setVisibility(View.GONE);
			vLine.setVisibility(View.GONE);
			ivFlag.setVisibility(View.GONE);
			break;
		case XFJRConstant.C_STATUS_3_INT: // 待放款，有预审金额，补充资料(分期申请书)，更多详情
			tvAproveHint.setText("预审金额");
			tvAproveMoney.setText(TextUtil.money$Format(bean.getApproveMoney()));
			lltPreAmount.setVisibility(View.VISIBLE);
			tvFurtherInfo.setVisibility(View.VISIBLE);// 左边按钮
			vLine.setVisibility(View.VISIBLE);
			tvFurtherInfo.setText(R.string.further_info);
			break;
		case XFJRConstant.C_STATUS_4_INT: // 已放款，有放款金额，更多详情
			tvAproveHint.setText("放款金额");
			tvAproveMoney.setText(TextUtil.money$Format(bean.getTariffMoney()));
			lltPreAmount.setVisibility(View.VISIBLE);
			tvFurtherInfo.setVisibility(View.GONE);
			vLine.setVisibility(View.GONE);
			ivFlag.setVisibility(View.VISIBLE);
			//ivFlag已放款的图标
			ivFlag.setImageResource(R.drawable.xfjr_tag_loans);
			break;
		case XFJRConstant.C_STATUS_5_INT: // 已拒绝，没有有预审金额，更多详情
			lltPreAmount.setVisibility(View.GONE);
			tvFurtherInfo.setVisibility(View.GONE);
			vLine.setVisibility(View.GONE);
			ivFlag.setVisibility(View.VISIBLE);
			//ivFlag已拒绝的图标
			ivFlag.setImageResource(R.drawable.xfjr_ic_refuse_status);
			break;
		}
		if (fileCommitted) { // 文件已提交
			tvFurtherInfo.setTextColor(mContext.getResources().getColor(R.color.xfjr_gray));
			tvFurtherInfo.setClickable(false);
		} else {
			tvFurtherInfo.setClickable(true);
			tvFurtherInfo.setTextColor(mContext.getResources().getColor(R.color.xfjr_red));
			tvFurtherInfo.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (onClickAddDataListener != null) {
						onClickAddDataListener.onClickAddData(bean, position);
					}
				}
			});
		}
		tvMore.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (onClickMoreDataListener != null) {
					onClickMoreDataListener.onClickMoreData(bean, position);
				}
			}
		});
		if(getItemCount() - 1 == position){
			RecyclerView.LayoutParams params = (LayoutParams) viewHolder.itemView.getLayoutParams();
			params.bottomMargin = ScreenUtils.px2dip(mContext, 60);
			viewHolder.itemView.setLayoutParams(params);
			
		}
	}
	
	/**
	 * 根据补充资料是否已上传文件判断按钮是否可以点击
	 * 
	 * @param bean
	 * @return
	 */
	private boolean getFileCommitted(MyBusinessBean bean){
		int status = Integer.parseInt(bean.getStatus());
		switch (status) {
		case XFJRConstant.C_STATUS_1_INT:
			String financeCertify = bean.getFinanceCertify();
			String incomeCertify = bean.getIncomeCertify();
			if (financeCertify.equals("Y") && incomeCertify.equals("Y")) {
				return true;
			} else if (financeCertify.equals("N") || incomeCertify.equals("N")) {
				return false;
			} 
			break;

		case XFJRConstant.C_STATUS_3_INT:
			String instalmentApply = bean.getInstalmentApply();
			if (instalmentApply.equals("Y")) { // 已上传分期申请书
				return true;
			} else if (instalmentApply.equals("N")) { // 未上传分期申请书
				return false;
			}
			break;
		}
		return false;
	}

	public interface OnClickAddDataListener {
		void onClickAddData(MyBusinessBean bean, int position); // 补充资料
	}

	public interface OnClickMoreDataListener {
		void onClickMoreData(MyBusinessBean bean, int position); // 更多详情
	}
}