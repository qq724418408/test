package com.bocop.xyd.fragment;


import java.util.ArrayList;
import java.util.List;

import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnChildClick;
import com.bocop.jxplatform.R;
import com.bocop.xyd.activity.Xyd_NowRepayingActivity;
import com.bocop.xyd.activity.Xyd_RepayActivity;
import com.bocop.xyd.activity.Xyd_UseMoneyDetailActivity;
import com.bocop.xyd.adapter.recycleradapter.CommonRecyclerAdapter;
import com.bocop.xyd.adapter.recycleradapter.RecyclerViewHolder;
import com.bocop.xyd.base.BaseFragment;
import com.bocop.xyd.util.ScreenUtils;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class RepayFragment extends BaseFragment{
	private boolean isPrepared; // 已经初始化完成。
	private int tag;
	
	@ViewInject(R.id.recyclerView)
	RecyclerView recyclerView;
	
	private List<String> listDate=new ArrayList<>();
	private CommonRecyclerAdapter<String> mAdpter;
	

	@Override
	public int getLayout() {
		return R.layout.xyd_item_repay_viewpager;
	}

	@Override
	public void initData() {
		tag=getArguments().getInt("style");
		initListData();
	}

	@Override
	public void initView() {
		
	}

	@Override
	public void initEvent() {
		// TODO Auto-generated method stub
		
	}

	private void initListData() {
		for (int i = 0; i < 5; i++) {
			listDate.add(i+"");
		}
		initListView();
	}
	private void initListView(){
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		mAdpter=new CommonRecyclerAdapter<String>(getActivity(),listDate,R.layout.xyd_item_repay) {
			
			@Override
			public void convert(RecyclerViewHolder holder, String itemData, int position) {
				holder.setViewVisibility(R.id.xyd_item_repay_bottomhint, position==listDate.size()-1&&Xyd_RepayActivity.SELECT_NOW==tag?View.VISIBLE:View.GONE);
				if(Xyd_RepayActivity.SELECT_NOW==tag){
					holder.setText(R.id.xyd_item_repay_date, "2017-05-10");
				}else if(Xyd_RepayActivity.SELECT_RECORD==tag){
					holder.setText(R.id.xyd_item_repay_date, getResources().getString(R.string.xyd_repay_data_demo));
				}
				if(position==0){
					LinearLayout linear=holder.getView(R.id.ll_item_out);
					LinearLayout.LayoutParams lp=(LayoutParams) linear.getLayoutParams();
					lp.topMargin=ScreenUtils.dip2px(getActivity(), 10);
					linear.setLayoutParams(lp);
					
				}/*else{
					LinearLayout linear=holder.getView(R.id.ll_item_out);
					LinearLayout.LayoutParams lp=(LayoutParams) linear.getLayoutParams();
					lp.topMargin=ScreenUtils.dip2px(getActivity(), 0);
					linear.setLayoutParams(lp);
				}*/
				holder.setOnClick(R.id.xyd_repay_left_arrow, new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						if(Xyd_RepayActivity.SELECT_RECORD==tag){
							Xyd_UseMoneyDetailActivity.StartThisActivity(getActivity());
						}else if(Xyd_RepayActivity.SELECT_NOW==tag){
							Xyd_NowRepayingActivity.StartThisActivity(getActivity());
						}
					}
				});
			}
		};
		recyclerView.setAdapter(mAdpter);
	}
}
