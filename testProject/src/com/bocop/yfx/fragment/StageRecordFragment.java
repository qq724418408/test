package com.bocop.yfx.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.boc.jx.base.BaseAdapter;
import com.boc.jx.base.BaseFragment;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.bocop.jxplatform.R;
import com.bocop.xms.tools.ViewHolder;
import com.bocop.yfx.bean.CashStateRecord;

/**
 * 分期记录
 * 
 * @author rd
 * 
 */
public class StageRecordFragment extends BaseFragment {
	@ViewInject(R.id.lvRecords)
	private ListView lvRecords;
	private List<CashStateRecord> stateRecords;
	private BaseAdapter<CashStateRecord> adapter;
	private View footerView;// 加载界面
	private LinearLayout llLoading;// 正在加载中
	private TextView tvTips;// 没有更多数据

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = initView(R.layout.yfx_fragment_state_record);
		return view;
	}

	@Override
	protected void initView() {
		// super.initView();
		stateRecords = new ArrayList<CashStateRecord>();
		CashStateRecord record1 = new CashStateRecord("55213698745511111",
				"10.00元", "20", "1.25元", "买个包子");
		CashStateRecord record2 = new CashStateRecord("67783698745511111",
				"1000000.00元", "10", "1852000元", "买座岛");
		stateRecords.add(record1);
		stateRecords.add(record2);
		stateRecords.add(record1);
		stateRecords.add(record2);
		stateRecords.add(record1);
		stateRecords.add(record2);

		footerView = LayoutInflater.from(baseActivity).inflate(
				R.layout.common_layout_listview_footer, null);
		llLoading = (LinearLayout) footerView.findViewById(R.id.llLoading);
		tvTips = (TextView) footerView.findViewById(R.id.tvTips);
		footerView.setVisibility(View.GONE);// 隐藏加载界面
		lvRecords.addFooterView(footerView);// 在listview底部添加footerview

		lvRecords.setAdapter(adapter = new BaseAdapter<CashStateRecord>(
				baseActivity, stateRecords, R.layout.yfx_item_state_records) {

			@Override
			public void viewHandler(int position, CashStateRecord t,
					View convertView) {

				TextView tvCardNum = ViewHolder
						.get(convertView, R.id.tvCardNum);
				TextView tvAmount = ViewHolder.get(convertView, R.id.tvAmount);
				TextView tvCount = ViewHolder.get(convertView, R.id.tvCount);
				TextView tvPerRepayment = ViewHolder.get(convertView,
						R.id.tvPerRepayment);
				TextView tvLoanUse = ViewHolder
						.get(convertView, R.id.tvLoanUse);
				if (null != t) {
					tvCardNum.setText(t.getCardNum());
					tvAmount.setText(t.getamount());
					tvCount.setText(t.getCount());
					tvPerRepayment.setText(t.getPerRepayment());
					tvLoanUse.setText(t.getLoanUse());
				}

			}
		});
		setListener();// 为listview设置监听

	}

	/**
	 * 设置监听
	 */
	private void setListener() {
		lvRecords.setOnScrollListener(new OnScrollListener() {

			private int vItemCount;// 当前可见item数
			private boolean canLoadMore = true;// 是否可以继续加载
			private int pageIndex = 0;

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				int lastItem = adapter.getCount();
				if (scrollState == 0) {
					// 当前可见的item和数据总数相等
					if (vItemCount == lastItem) {
						if (canLoadMore) {
							pageIndex++;
							footerView.setVisibility(View.VISIBLE);
							llLoading.setVisibility(View.VISIBLE);
							tvTips.setVisibility(View.GONE);
							new Handler().postDelayed(new Runnable() {
								@Override
								public void run() {
									loadData();
									if (pageIndex == 3) {
										canLoadMore = false;
										footerView.setVisibility(View.VISIBLE);
										llLoading.setVisibility(View.GONE);
										tvTips.setVisibility(View.VISIBLE);
									} else {
										footerView.setVisibility(View.GONE);
									}
								}
							}, 1500);
						}
					}
				}

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				vItemCount = firstVisibleItem + visibleItemCount - 1;

			}
		});
	}

	private void loadData() {
		CashStateRecord record1 = new CashStateRecord("55213698745511111",
				"88888元", "11", "33元", "开公司");
		CashStateRecord record2 = new CashStateRecord("67783698745511111",
				"666666元", "5", "996元", "投资");
		stateRecords.add(record1);
		stateRecords.add(record2);
		adapter.notifyDataSetChanged();
	}
}
