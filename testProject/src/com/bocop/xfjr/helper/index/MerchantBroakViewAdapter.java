package com.bocop.xfjr.helper.index;


import java.util.List;

import com.bocop.xfjr.bean.UserInfoBean.YearBean;
import com.bocop.xfjr.view.brokelineview.BrokenAdapter;

public class MerchantBroakViewAdapter implements BrokenAdapter {

	private List<YearBean> mBroakData;
	
	public MerchantBroakViewAdapter(List<YearBean> BroakData) {
		mBroakData = BroakData;
	}
	
	@Override
	public synchronized int getLands(int position) {	
		return mBroakData.get(position).getMonth();
	}

	@Override
	public synchronized float getTotal(int position) {
		
		return mBroakData.get(position).getTotal();
	}

	@Override
	public synchronized float getActual(int position) {
		return mBroakData.get(position).getPassed();
	}

	@Override
	public synchronized int getSize() {
		return mBroakData==null?0:mBroakData.size();
	}

	@Override
	public void notifyChanged() {
		
	}

}
