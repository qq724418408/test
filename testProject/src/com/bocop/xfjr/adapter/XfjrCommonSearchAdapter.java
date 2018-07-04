package com.bocop.xfjr.adapter;

import java.util.List;

import com.bocop.jxplatform.R;
import com.bocop.xfjr.activity.XFJRSearchResultActivity;
import com.bocop.xfjr.adapter.recycleradapter.CommonRecyclerAdapter;
import com.bocop.xfjr.adapter.recycleradapter.RecyclerViewHolder;
import com.bocop.xfjr.bean.BusinessBean;
import com.bocop.xfjr.util.TextUtil;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
/**
 * 搜索界面 首页搜索
 * @author TIAN FENG
 *
 */
public class XfjrCommonSearchAdapter extends CommonRecyclerAdapter<BusinessBean>{

	private Context mContext;
	public XfjrCommonSearchAdapter(Context context, List<BusinessBean> data) {
		super(context, data, R.layout.xfjr_item_search);
		mContext = context;
	}

	@Override
	public void convert(RecyclerViewHolder holder, final BusinessBean itemData, int position) {
		LinearLayout llLayoutParams = holder.getView(R.id.lllayouparams);
		/*if (position == getItemCount() - 1) {
			llLayoutParams.setPadding(llLayoutParams.getPaddingLeft(), llLayoutParams.getPaddingTop(),
					llLayoutParams.getPaddingRight(), 60);
		}*/
		holder.setText(R.id.tvSearchName, itemData.getCustomerName());
		holder.setText(R.id.tvSearchCategory, itemData.getCategory());
		holder.setText(R.id.tvSearchMoney, TextUtil.money$Format(itemData.getApplyMoney()));
		llLayoutParams.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				XFJRSearchResultActivity.startActivity(mContext,itemData.getBusinessId(), false);
			}
		});
	}

	
	/**  之前肖魁的代码
	 mAdapter = new CommonRecyclerAdapter<BusinessBean>(this, listData, new MultiTypeSupport<BusinessBean>() {

			@Override
			public int getLayoutId(BusinessBean item, int position) {
//				if (position == 0) {
//					return R.layout.xfjr_item_search_header;
//				}
				return R.layout.xfjr_item_search;
			}
		}) {//
			public void convert(final RecyclerViewHolder holder, final BusinessBean itemData, int position) {
//				if (0 == position) {// 头部
//					holder.setText(R.id.tv_search_totalmoney, "总金额："+(listData.size()==1?"0.00":TextUtil.moneyFormat(getTotalMoney())));
//					holder.setText(R.id.tv_search_num, "已放款"+(listData.size()-1)+"笔");
//					
//					TextView fromData = holder.getView(R.id.tv_search_fromdata);
//					fromData.setText(itemData.getCustomerName());
//					setTimeCall(fromData,1);
//					TextView toData = holder.getView(R.id.tv_search_todata);
//					toData.setText(itemData.getApplyMoney());
//					setTimeCall(toData,0);
//					final TextView tView = holder.getView(R.id.tv_search_status);
//					tView.setText(itemData.getCategory());
//					tView.setOnClickListener(XFJRSearchActivity.this);
//				} else {// 列表
					LinearLayout llLayoutParams = holder.getView(R.id.lllayouparams);
					if (position == getItemCount() - 1) {
						llLayoutParams.setPadding(llLayoutParams.getPaddingLeft(), llLayoutParams.getPaddingTop(),
								llLayoutParams.getPaddingRight(), 60);
					}
					holder.setText(R.id.tvSearchName, itemData.getCustomerName());
					holder.setText(R.id.tvSearchCategory, itemData.getCategory());
					holder.setText(R.id.tvSearchMoney,"￥ "+ TextUtil.moneyFormat(itemData.getApplyMoney()));
					llLayoutParams.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							XFJRSearchResultActivity.StartActivity(XFJRSearchActivity.this, itemData.getBusinessId());
						}
					});
//				}
			}

			private String getTotalMoney() {
				float money=0;
				if(listData.size()>1){
					for(int i=1;i<listData.size();i++){
						money+=Float.valueOf(listData.get(i).getApplyMoney());
					}
				}
				DecimalFormat df = new DecimalFormat("0.00");
				return  df.format(money);
			}
		}; 
	 */
}
