package com.bocop.yfx.fragment;

import java.util.ArrayList;
import java.util.List;

import com.boc.jx.base.BaseAdapter;
import com.boc.jx.base.BaseFragment;
import com.boc.jx.baseUtil.cache.CacheBean;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.tools.DialogUtil;
import com.boc.jx.view.LoadingView;
import com.boc.jx.view.LoadingView.OnRetryListener;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.config.TransactionValue;
import com.bocop.jxplatform.util.CspUtil;
import com.bocop.jxplatform.util.CspUtil.CallBack;
import com.bocop.jxplatform.util.Mcis;
import com.bocop.xms.bean.ConstHead;
import com.bocop.xms.tools.ViewHolder;
import com.bocop.xms.utils.XStreamUtils;
import com.bocop.yfx.activity.LoanMainActivity;
import com.bocop.yfx.activity.myloan.RefundInAdvanceActivity;
import com.bocop.yfx.bean.Repayment;
import com.bocop.yfx.bean.RepaymentList;
import com.bocop.yfx.bean.RepaymentListResponse;
import com.bocop.yfx.xml.CspXmlYfxCom;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 还款
 *
 * @author lh
 */
public class RefundFragment extends BaseFragment {

	@ViewInject(R.id.lvRefund)
	private ListView lvRefund;
	@ViewInject(R.id.loadingView)
	private LoadingView loadingView;

	private BaseAdapter<Repayment> adapter;
	private List<Repayment> repayList = new ArrayList<>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = initView(R.layout.yfx_fragment_refund);
		return view;
	}

	@Override
	protected void initData() {
		super.initData();
		requestRefundList();
	}

	@Override
	protected void initView() {
		super.initView();

		// initListener();
	}

	private void setAdapter() {
		lvRefund.setAdapter(adapter = new BaseAdapter<Repayment>(baseActivity, repayList, R.layout.yfx_item_records) {

			@Override
			public void viewHandler(final int position, Repayment t, View convertView) {
				TextView tvDrawDate = ViewHolder.get(convertView, R.id.tvDrawDate);
				TextView tvDrawMoney = ViewHolder.get(convertView, R.id.tvDrawMoney);
				TextView tvUseDeadline = ViewHolder.get(convertView, R.id.tvUseDeadline);
				TextView tvExpireDate = ViewHolder.get(convertView, R.id.tvExpireDate);
				TextView tvLoanStatus = ViewHolder.get(convertView, R.id.tvLoanStatus);
				TextView tvRefund = ViewHolder.get(convertView, R.id.tvRefund);

				tvRefund.setVisibility(View.VISIBLE);

				if (null != t) {
					tvDrawDate.setText(t.getDataTK());
					tvDrawMoney.setText(t.getAmtTK());
					tvUseDeadline.setText(t.getTimeYK());
					tvExpireDate.setText(t.getDataDQ());
					if ("50".equals(t.getLoanState())) {
						tvLoanStatus.setText("逾期");
						tvLoanStatus.setTextColor(getResources().getColor(R.color.redLight));
					} else if ("40".equals(t.getLoanState())) {
						tvLoanStatus.setText("结清");
						tvLoanStatus.setTextColor(getResources().getColor(R.color.greenDark));
					} else {
						tvLoanStatus.setText("正常");
						tvLoanStatus.setTextColor(getResources().getColor(R.color.blueDark));
					}
				}

				tvRefund.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(baseActivity, RefundInAdvanceActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("ACCT_NO", repayList.get(position).getAccNo());
						bundle.putString("REPAYMENT_AMOUNT", repayList.get(position).getAmtTK());
						intent.putExtra("BUNDLE", bundle);
						startActivity(intent);

					}
				});
			}
		});

	}

	private void initListener() {

		lvRefund.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Intent intent = new Intent(baseActivity, RefundInAdvanceActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("ACCT_NO", repayList.get(arg2).getAccNo());
				bundle.putString("REPAYMENT_AMOUNT", repayList.get(arg2).getAmtTK());
				intent.putExtra("BUNDLE", bundle);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		if (LoanMainActivity.REFUND_FLAG) {
			LoanMainActivity.REFUND_FLAG = false;
			requestRefundList();
		}
	}

	/**
	 * 请求还款列表
	 */
	private void requestRefundList() {
		try {
			CspXmlYfxCom cXmlYfxCom = new CspXmlYfxCom("WL001010",
					baseActivity.getCacheBean().get(CacheBean.CUST_ID).toString());
			String strXml = cXmlYfxCom.getCspXml();
			// 生成MCIS报文
			Mcis mcis = new Mcis(strXml, TransactionValue.CSPSZF);
			byte[] byteMessage = {};
			byteMessage = mcis.getMcis();
			// 发送报文
			CspUtil cspUtil = new CspUtil(baseActivity);
			cspUtil.setFLAG_YFX_CSP(true);
			cspUtil.setTxCode("001010");
			cspUtil.postCspLogin(new String(byteMessage, "GBK"), new CallBack() {

				@Override
				public void onSuccess(String responStr) {
					onGetListSuccess(responStr);
				}

				@Override
				public void onFinish() {
					// TODO Auto-generated method stub

				}

				@Override
				public void onFailure(String responStr) {
					onGetListFailure(responStr);
				}
			});
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 请求还款列表成功后
	 *
	 * @param responStr
	 */
	private void onGetListSuccess(String responStr) {
		RepaymentListResponse listResponse = XStreamUtils.getFromXML(responStr, RepaymentListResponse.class);
		ConstHead constHead = listResponse.getConstHead();
		if (constHead != null) {
			if ("00".equals(constHead.getErrCode())) {
				lvRefund.setVisibility(View.VISIBLE);
				loadingView.setVisibility(View.GONE);
				RepaymentList repaymentList = listResponse.getList();
				if (repaymentList != null && repaymentList.getList() != null) {
					repayList.clear();
					repayList.addAll(repaymentList.getList());
					setAdapter();
				}
			} else if ("50".equals(constHead.getErrCode())) {
				DialogUtil.showWithToMain(baseActivity, constHead.getErrMsg());
			} else {
				onGetListFailure(constHead.getErrMsg());
			}
		}
	}

	private void onGetListFailure(String err_msg) {
		CspUtil.onFailure(baseActivity, err_msg);
		lvRefund.setVisibility(View.GONE);
		loadingView.setVisibility(View.VISIBLE);
		loadingView.setmOnRetryListener(new OnRetryListener() {

			@Override
			public void retry() {
				requestRefundList();
			}
		});
	}
}
