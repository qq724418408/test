package com.bocop.jxplatform.fragment;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.boc.jx.base.BaseFragment;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.adapter.traProcessingListItemAdapter;
import com.bocop.jxplatform.bean.APJJ08ListXmlBean;
import com.bocop.jxplatform.bean.CarPeccancyBean;
import com.bocop.jxplatform.config.TransactionValue;
import com.bocop.jxplatform.util.CspUtil;
import com.bocop.jxplatform.util.CspUtil.CallBack;
import com.bocop.jxplatform.util.LoginUtil;
import com.bocop.jxplatform.util.Mcis;
import com.bocop.jxplatform.xml.CspRecForAPJJ16;
import com.bocop.jxplatform.xml.CspXmlAPJJ16;
import com.bocop.jxplatform.xml.CspXmlAPJJ17;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;

public class TraProcessFailFragment extends BaseFragment {
	@ViewInject(R.id.lv_myhis_peccancy)
	ListView lvMyHisPeccancy;
	// 没有违章，不显示
	@ViewInject(R.id.ll_payhis_nopeccancy)
	View llPayhisNopeccancy;
	// 有违章，显示LISTVIEW
	@ViewInject(R.id.ll_payhis_mypeccancy)
	View llPayhisMypeccancy;

	private static final int INITIAL_DELAY_MILLIS = 500;
	private traProcessingListItemAdapter traProcessingAdapter;
	AlphaInAnimationAdapter alphaInAnimationAdapter;

	List<CarPeccancyBean> dataList = new ArrayList<CarPeccancyBean>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = initView(R.layout.fragment_traproing);
		return view;
	}

	@Override
	protected void initData() {
		super.initData();
		requestCspForTraProcessing();
	}

	@Override
	protected void initView() {
		super.initView();
	}

	private void requestCspForTraProcessing() {
		try {
			// 生成CSP XML报文
			CspXmlAPJJ16 cspXmlAPJJ16 = new CspXmlAPJJ16(
					LoginUtil.getUserId(getActivity()));
			String strXml = cspXmlAPJJ16.getCspXml();
			// 生成MCIS报文
			Mcis mcis = new Mcis(strXml, TransactionValue.APJJ16);
			final byte[] byteMessage = mcis.getMcis();
			// 发送报文
			CspUtil cspUtil = new CspUtil(getActivity());
			Log.i("tag", "发送报文： " + new String(byteMessage, "GBK"));
			cspUtil.postCspLogin(byteMessage, new CallBack() {
				@Override
				public void onSuccess(String responStr) {
					// APJJ16Response aPJJ16Response =
					// XStreamUtils.getFromXML(responStr, APJJ16Response.class);
					Log.i("tag", "开始解析 ");
					APJJ08ListXmlBean listBean = CspRecForAPJJ16
							.readStringXml(responStr);
					Log.i("tag", "解析 成功");
					if (!listBean.getErrorcode().equals("00")) {
//						if (listBean.getCarPeccancyListBean().size() > 0) {
//							llPayhisNopeccancy.setVisibility(View.INVISIBLE);
//							llPayhisMypeccancy.setVisibility(View.VISIBLE);
//							dataList = listBean.getCarPeccancyListBean();
//							setListAdapter();
//						} else {
							llPayhisNopeccancy.setVisibility(View.VISIBLE);
							llPayhisMypeccancy.setVisibility(View.INVISIBLE);
//						}
					} else {
						llPayhisNopeccancy.setVisibility(View.VISIBLE);
						llPayhisMypeccancy.setVisibility(View.INVISIBLE);
					}
				}

				@Override
				public void onFinish() {
					// TODO Auto-generated method stub

				}

				@Override
				public void onFailure(String responStr) {
					CspUtil.onFailure(getActivity(), responStr);
				}
			});
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private void setListAdapter() {
		traProcessingAdapter = new traProcessingListItemAdapter(baseActivity,
				dataList);
		alphaInAnimationAdapter = new AlphaInAnimationAdapter(
				traProcessingAdapter);
		alphaInAnimationAdapter.setAbsListView(lvMyHisPeccancy);
		Log.d("tag", "setAbsListView");
		assert alphaInAnimationAdapter.getViewAnimator() != null;
		alphaInAnimationAdapter.getViewAnimator().setInitialDelayMillis(
				INITIAL_DELAY_MILLIS);
		Log.d("tag", "setInitialDelayMillis");
		lvMyHisPeccancy.setAdapter(alphaInAnimationAdapter);
	}
}
