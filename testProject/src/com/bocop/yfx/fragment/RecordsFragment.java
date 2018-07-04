package com.bocop.yfx.fragment;

import java.util.ArrayList;
import java.util.List;

import com.boc.jx.base.BaseAdapter;
import com.boc.jx.base.BaseFragment;
import com.boc.jx.baseUtil.cache.CacheBean;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.boc.jx.tools.DialogUtil;
import com.boc.jx.view.LoadingView;
import com.boc.jx.view.LoadingView.OnRetryListener;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.config.TransactionValue;
import com.bocop.jxplatform.util.CspUtil;
import com.bocop.jxplatform.util.CspUtil.CallBack;
import com.bocop.jxplatform.util.FormsUtil;
import com.bocop.jxplatform.util.Mcis;
import com.bocop.jxplatform.view.MyListView;
import com.bocop.xms.bean.ConstHead;
import com.bocop.xms.tools.ViewHolder;
import com.bocop.xms.utils.XStreamUtils;
import com.bocop.yfx.activity.myloan.RecordsListActivity;
import com.bocop.yfx.bean.LoanDetail;
import com.bocop.yfx.bean.RecordsResponse;
import com.bocop.yfx.bean.RepaymentRecords;
import com.bocop.yfx.xml.CspXmlYfx012;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * 
 * 历史记录
 * 
 * @author lh
 * 
 */
public class RecordsFragment extends BaseFragment {

	@ViewInject(R.id.lvUseLoan)
	private MyListView lvUseLoan;
	@ViewInject(R.id.lvRepay)
	private MyListView lvRepay;
//	@ViewInject(R.id.lvApply)
//	private MyListView lvApply;
	@ViewInject(R.id.ivMoreUse)
	private ImageView ivMoreUse;
	@ViewInject(R.id.ivMoreRefund)
	private ImageView ivMoreRefund;
//	@ViewInject(R.id.ivMoreRecords)
//	private ImageView ivMoreRecords;
	@ViewInject(R.id.lLayout)
	private LinearLayout lLayout;
	@ViewInject(R.id.loadingView)
	private LoadingView loadingView;
	@ViewInject(R.id.scrollView)
	private ScrollView scrollView;

	private List<LoanDetail> ldList = new ArrayList<>();
	private List<RepaymentRecords> reList = new ArrayList<>();
//	private List<ApplyHistory> ahList = new ArrayList<>();
	private List<LoanDetail> ldVisibilityList = new ArrayList<>();
	private List<RepaymentRecords> reVisibilityList = new ArrayList<>();
//	private List<ApplyHistory> ahVisibilityList = new ArrayList<>();

	/**
	 * 判断列表长度是否超过XX条
	 */
	private boolean LD_SIZE_FLAG = false;
	private boolean RE_SIZE_FLAG = false;
//	private boolean AH_SIZE_FLAG = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = initView(R.layout.yfx_fragment_records);
		return view;
	}

	@Override
	protected void initData() {
		super.initData();

	}

	@Override
	protected void initView() {
		super.initView();
	}

	@OnClick({ R.id.ivMoreUse, R.id.ivMoreRefund})
	public void onClick(View v) {
		Intent intent = new Intent(baseActivity, RecordsListActivity.class);
		Bundle bundle = new Bundle();
		switch (v.getId()) {
		case R.id.ivMoreUse:
			bundle.putString("TITLE", "用款历史");
			bundle.putString("TYPE", "0");
			break;
		case R.id.ivMoreRefund:
			bundle.putString("TITLE", "还款历史");
			bundle.putString("TYPE", "1");
			break;
		}
		intent.putExtras(bundle);
		startActivity(intent);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		requestList();
	}

	/**
	 * 如果列表长度大于XX条，则只显示XX条
	 */
	private void setAdapter() {

		List<LoanDetail> loanDetailList = new ArrayList<>();
		if (LD_SIZE_FLAG) {
			loanDetailList.addAll(ldVisibilityList);
		} else {
			loanDetailList.addAll(ldList);
		}
		lvUseLoan.setAdapter(new BaseAdapter<LoanDetail>(baseActivity, loanDetailList, R.layout.yfx_item_records) {

			@Override
			public void viewHandler(int position, LoanDetail t, View convertView) {
				TextView tvDrawDate = ViewHolder.get(convertView, R.id.tvDrawDate);
				TextView tvDrawMoney = ViewHolder.get(convertView, R.id.tvDrawMoney);
				TextView tvUseDeadline = ViewHolder.get(convertView, R.id.tvUseDeadline);
				TextView tvExpireDate = ViewHolder.get(convertView, R.id.tvExpireDate);
				TextView tvLoanStatus = ViewHolder.get(convertView, R.id.tvLoanStatus);
				if (null != t) {
					FormsUtil.setTextViewTxt(tvDrawDate, t.getDrawDate());
					FormsUtil.setTextViewTxt(tvDrawMoney, t.getDrawMoney());
					FormsUtil.setTextViewTxt(tvUseDeadline, t.getUseDeadline());
					FormsUtil.setTextViewTxt(tvExpireDate, t.getExpireDate());
					setTvLoanStatus(tvLoanStatus, t.getLoanStatus());
				}
			}
		});

		List<RepaymentRecords> repaymentRecordsList = new ArrayList<>();
		if (RE_SIZE_FLAG) {
			repaymentRecordsList.addAll(reVisibilityList);
		} else {
			repaymentRecordsList.addAll(reList);
		}
		lvRepay.setAdapter(new BaseAdapter<RepaymentRecords>(baseActivity, repaymentRecordsList, R.layout.yfx_item_records) {

					@Override
					public void viewHandler(int position, final RepaymentRecords t, View convertView) {
						LinearLayout llRecords = ViewHolder.get(convertView, R.id.llRecords);
						TextView tvDrawDate = ViewHolder.get(convertView, R.id.tvDrawDate);
						TextView tvDrawMoney = ViewHolder.get(convertView, R.id.tvDrawMoney);
						TextView tvUseDeadline = ViewHolder.get(convertView, R.id.tvUseDeadline);
						TextView tvExpireDate = ViewHolder.get(convertView, R.id.tvExpireDate);
						TextView tvLoanStatus = ViewHolder.get(convertView, R.id.tvLoanStatus);
						if (null != t) {
							FormsUtil.setTextViewTxt(tvDrawDate, t.getDrawDate());
							FormsUtil.setTextViewTxt(tvDrawMoney, t.getDrawMoney());
							FormsUtil.setTextViewTxt(tvUseDeadline, t.getUseDeadline());
							FormsUtil.setTextViewTxt(tvExpireDate, t.getExpireDate());
							setTvLoanStatus(tvLoanStatus, t.getLoanStatus());
							llRecords.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
								    Intent intent = new Intent(baseActivity, RecordsListActivity.class);
									Bundle bundle = new Bundle();
									bundle.putString("TITLE", "还款清单");
									bundle.putString("TYPE", "2");
									bundle.putString("LoanNo", t.getLoanNo());
									intent.putExtras(bundle);
									startActivity(intent);
								}
							});
						}
					}
				});

//		List<ApplyHistory> applyHistoryList = new ArrayList<>();
//		if (AH_SIZE_FLAG) {
//			applyHistoryList.addAll(ahVisibilityList);
//		} else {
//			applyHistoryList.addAll(ahList);
//		}
//		if (applyHistoryList.size() > 0) {
//			View headerView = LayoutInflater.from(baseActivity).inflate(R.layout.yfx_item_repayment_inventory, null);
//			lvApply.addHeaderView(headerView);
//		}
//		lvApply.setAdapter(new BaseAdapter<ApplyHistory>(baseActivity, applyHistoryList, R.layout.yfx_item_repayment_inventory) {
//
//			@Override
//			public void viewHandler(int position, ApplyHistory t, View convertView) {
//				TextView tvSerialNumber = ViewHolder.get(convertView, R.id.tvSerialNumber);
//				TextView tvRepaymentDate = ViewHolder.get(convertView, R.id.tvRepaymentDate);
//				TextView tvPrincipal = ViewHolder.get(convertView, R.id.tvPrincipal);
//				TextView tvInterest = ViewHolder.get(convertView, R.id.tvInterest);
//				TextView tvInterestTotal = ViewHolder.get(convertView, R.id.tvInterestTotal);
//				if (null != t) {
//					FormsUtil.setTextViewTxt(tvSerialNumber, t.getSerialNumber());
//					FormsUtil.setTextViewTxt(tvRepaymentDate, t.getRepaymentDate());
//					FormsUtil.setTextViewTxt(tvPrincipal, t.getPrincipal());
//					FormsUtil.setTextViewTxt(tvInterest, t.getInterest());
//					FormsUtil.setTextViewTxt(tvInterestTotal, t.getInterestTotal());
//				}
//			}
//		});
	}

	/**
	 * 请求历史记录列表
	 */
	private void requestList() {
		try {
			CspXmlYfx012 cspXmlYfx012 = new CspXmlYfx012(baseActivity.getCacheBean().get(CacheBean.CUST_ID).toString(),
					"3", "10", "1");
			String strXml = cspXmlYfx012.getCspXml();
			// 生成MCIS报文
			Mcis mcis = new Mcis(strXml, TransactionValue.CSPSZF);
			byte[] byteMessage = {};
			byteMessage = mcis.getMcis();
			// 发送报文
			CspUtil cspUtil = new CspUtil(baseActivity);
			cspUtil.setFLAG_YFX_CSP(true);
			cspUtil.setTxCode("001012");
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
	 * 
	 * 请求列表成功后
	 * 
	 * @param responStr
	 */
	private void onGetListSuccess(String responStr) {
		RecordsResponse recordsResponse = XStreamUtils.getFromXML(responStr, RecordsResponse.class);
		ConstHead constHead = recordsResponse.getConstHead();
		if (constHead != null) {
			if ("00".equals(constHead.getErrCode())) {
				scrollView.setVisibility(View.VISIBLE);
				loadingView.setVisibility(View.GONE);
				if (recordsResponse.getContainer() != null) {
					if (recordsResponse.getContainer().getLoanList() != null) {
						ldList.clear();
						ldList.addAll(recordsResponse.getContainer().getLoanList());
					}
					if (recordsResponse.getContainer().getReList() != null) {
						reList.clear();
						reList.addAll(recordsResponse.getContainer().getReList());
					}
	//				ahList.addAll(recordsResponse.getContainer().getHiList());
					if (ldList.size() >= 10) {
						for (int i = 0; i < 10; i++) {
							ldVisibilityList.add(ldList.get(i));
						}
						LD_SIZE_FLAG = true;
						ivMoreUse.setVisibility(View.VISIBLE);
					}
					if (reList.size() >= 10) {
						for (int i = 0; i < 10; i++) {
							reVisibilityList.add(reList.get(i));
						}
						RE_SIZE_FLAG = true;
						ivMoreRefund.setVisibility(View.VISIBLE);
					}
	//				if (ahList.size() > 10) {
	//					for (int i = 0; i < 10; i++) {
	//						ahVisibilityList.add(ahList.get(i));
	//					}
	//					AH_SIZE_FLAG = true;
	//					ivMoreRecords.setVisibility(View.VISIBLE);
	//				}
					setAdapter();
				}
			} else if ("50".equals(constHead.getErrCode())) {
				DialogUtil.showWithToMain(baseActivity, constHead.getErrMsg());
			} else {
				onGetListFailure(constHead.getErrMsg());
			}
		}
	}
	
	private void onGetListFailure(String err_msg){
		CspUtil.onFailure(baseActivity, err_msg);
		scrollView.setVisibility(View.GONE);
		loadingView.setVisibility(View.VISIBLE);
		loadingView.setmOnRetryListener(new OnRetryListener() {

			@Override
			public void retry() {
				requestList();
			}
		});
	}
	
	private void setTvLoanStatus(TextView tvLoanStatus, String loanStatus){
		if ("50".equals(loanStatus)) {
			loanStatus = "逾期";
			tvLoanStatus.setTextColor(getResources().getColor(R.color.redLight));
		}else if("40".equals(loanStatus)){
			loanStatus = "结清";
			tvLoanStatus.setTextColor(getResources().getColor(R.color.greenDark));
		} else {
			loanStatus = "正常";
			tvLoanStatus.setTextColor(getResources().getColor(R.color.blueDark));
		}
		FormsUtil.setTextViewTxt(tvLoanStatus, loanStatus);
	}
}
