package com.bocop.xyd.activity;

import java.util.ArrayList;
import java.util.List;

import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.bocop.jxplatform.R;
import com.bocop.xyd.adapter.ViewPageAdapter;
import com.bocop.xyd.adapter.recycleradapter.CommonRecyclerAdapter;
import com.bocop.xyd.adapter.recycleradapter.RecyclerViewHolder;
import com.bocop.xyd.base.XydBaseActivity;
import com.bocop.xyd.fragment.RepayFragment;
import com.bocop.xyd.util.ScreenUtils;
import com.bocop.yfx.utils.ToastUtils;

import android.R.bool;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

/**
 * 还款记录以及用款记录列表
 * 
 * @author formssi
 *
 */
public class Xyd_RepayActivity_New extends XydBaseActivity {

	
	@ViewInject(R.id.recyclerView)
	RecyclerView recyclerView;
	
	@ViewInject(R.id.title)
	TextView title;
	
	public static final int SELECT_NOW = 1;
	public static final int SELECT_RECORD = 2;
	private int tag = SELECT_NOW;

	private List<String> listDate=new ArrayList<>();
	private CommonRecyclerAdapter<String> mAdpter;

	@Override
	protected int getLoyoutId() {
		return R.layout.xyd_activity_repay_new;
	}

	@Override
	protected void initView() {

	}

	@Override
	protected void initData() {
		
		tag=getIntent().getIntExtra("STYLE", SELECT_NOW);
		Log.e("TGA=======>>", tag+"");
		if(tag==SELECT_NOW){
			title.setText("当前还款");
		}else  if(tag==SELECT_RECORD){
			title.setText("用款记录");
		}
		initListData();
		
	}




	
	@OnClick(R.id.ll_repay_back)
	public void back(View view) {
		switch (view.getId()) {
		case R.id.ll_repay_back:
			this.finish();
			break;
		default:
			break;
		}
	}
	
	private void initListData() {
		for (int i = 0; i < 5; i++) {
			listDate.add(i+"");
		}
		initListView();
	}
	private void initListView(){
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		mAdpter=new CommonRecyclerAdapter<String>(this,listDate,R.layout.xyd_item_repay) {
			
			@Override
			public void convert(RecyclerViewHolder holder, String itemData, int position) {
				holder.setViewVisibility(R.id.xyd_item_repay_bottomhint, position==listDate.size()-1&&SELECT_NOW==tag?View.VISIBLE:View.GONE);
				if(SELECT_NOW==tag){
					holder.setText(R.id.xyd_item_repay_date, "2017-05-10");
				}else if(SELECT_RECORD==tag){
					holder.setText(R.id.xyd_item_repay_date, getResources().getString(R.string.xyd_repay_data_demo));
				}
				if(position==0){
					LinearLayout linear=holder.getView(R.id.ll_item_out);
					LinearLayout.LayoutParams lp=(LayoutParams) linear.getLayoutParams();
					lp.topMargin=ScreenUtils.dip2px(Xyd_RepayActivity_New.this, 10);
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
						if(SELECT_RECORD==tag){
							Xyd_UseMoneyDetailActivity.StartThisActivity(Xyd_RepayActivity_New.this);
						}else if(SELECT_NOW==tag){
							Xyd_NowRepayingActivity.StartThisActivity(Xyd_RepayActivity_New.this);
						}
					}
				});
			}
		};
		recyclerView.setAdapter(mAdpter);
	}
	
	public static void  StartThisActivity(Context context,int style) {
		Intent i=new Intent(context,Xyd_RepayActivity_New.class);
		i.putExtra("STYLE", style);
		context.startActivity(i);
	}
}
