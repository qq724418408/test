package com.bocop.yfx.activity.myloan;

import java.util.ArrayList;
import java.util.List;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.base.BaseAdapter;
import com.boc.jx.baseUtil.cache.CacheBean;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.tools.DialogUtil;
import com.boc.jx.view.LoadingView;
import com.boc.jx.view.LoadingView.OnRetryListener;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.config.TransactionValue;
import com.bocop.jxplatform.util.CspUtil;
import com.bocop.jxplatform.util.CspUtil.CallBack;
import com.bocop.jxplatform.util.FormsUtil;
import com.bocop.jxplatform.util.Mcis;
import com.bocop.xms.bean.ConstHead;
import com.bocop.xms.tools.ViewHolder;
import com.bocop.xms.utils.XStreamUtils;
import com.bocop.yfx.bean.ApplyHistory;
import com.bocop.yfx.bean.LoanDetail;
import com.bocop.yfx.bean.RecordsResponse;
import com.bocop.yfx.bean.RepaymentRecords;
import com.bocop.yfx.xml.CspXmlYfx012;
import com.bocop.yfx.xml.CspXmlYfx015;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 
 * 历史记录
 * 
 * @author lh
 * 
 */
@ContentView(R.layout.yfx_activity_records_list)
public class RecordsListActivity extends BaseActivity implements OnScrollListener {

	@ViewInject(R.id.lvRecords)
	private ListView lvRecords;
	@ViewInject(R.id.tv_titleName)
	private TextView tvTitle;
	@ViewInject(R.id.loadingView)
	private LoadingView loadingView;
	@ViewInject(R.id.llInventory)
	private LinearLayout llInventory;

	private List<LoanDetail> ldList = new ArrayList<>();
	private List<RepaymentRecords> reList = new ArrayList<>();
	private List<ApplyHistory> ahList = new ArrayList<>();

	private BaseAdapter<LoanDetail> ldAdapter;
	private BaseAdapter<RepaymentRecords> reAdapter;
	private BaseAdapter<ApplyHistory> ahAdapter;

	private String type;

	private View footerView;
	private LinearLayout llLoading;
	private TextView tvTips;
	private int vItemCount;
	private int pageIndex = 1;
	private boolean canLoadMore = true;
	private boolean isShowDialog;
	private int totalPage = 0;
	private String loanNo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initData();
		initListener();
		initView();
	}

	private void initData() {
		if (null != getIntent().getExtras()) {
			tvTitle.setText(getIntent().getExtras().getString("TITLE"));
			type = getIntent().getExtras().getString("TYPE");
			loanNo = getIntent().getExtras().getString("LoanNo");
		}
		requestList(true);
	}

	private void initView() {
		footerView = LayoutInflater.from(this).inflate(R.layout.common_layout_listview_footer, null);
		llLoading = (LinearLayout) footerView.findViewById(R.id.llLoading);
		tvTips = (TextView) footerView.findViewById(R.id.tvTips);
		footerView.setVisibility(View.GONE);
		lvRecords.addFooterView(footerView);
		llInventory.setVisibility(View.GONE);
		setAdapter();
	}

	private void initListener() {
		lvRecords.setOnScrollListener(this);
	}

	private void setAdapter() {
		if ("0".equals(type)) {
			lvRecords.setAdapter(ldAdapter = new BaseAdapter<LoanDetail>(this, ldList, R.layout.yfx_item_records) {

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
		} else if ("1".equals(type)) {
			lvRecords
					.setAdapter(reAdapter = new BaseAdapter<RepaymentRecords>(this, reList, R.layout.yfx_item_records) {

						@Override
						public void viewHandler(int position, RepaymentRecords t, View convertView) {
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
		} else if ("2".equals(type)) {
			lvRecords.setAdapter(
					ahAdapter = new BaseAdapter<ApplyHistory>(this, ahList, R.layout.yfx_item_repayment_inventory) {

						@Override
						public void viewHandler(int position, ApplyHistory t, View convertView) {
							// TextView tvSerialNumber =
							// ViewHolder.get(convertView, R.id.tvSerialNumber);
							TextView tvRepaymentDate = ViewHolder.get(convertView, R.id.tvRepaymentDate);
							TextView tvPrincipal = ViewHolder.get(convertView, R.id.tvPrincipal);
							TextView tvInterest = ViewHolder.get(convertView, R.id.tvInterest);
							TextView tvInterestTotal = ViewHolder.get(convertView, R.id.tvInterestTotal);
							if (null != t) {
								// FormsUtil.setTextViewTxt(tvSerialNumber,
								// t.getSerialNumber());
								FormsUtil.setTextViewTxt(tvRepaymentDate, t.getRepaymentDate());
								FormsUtil.setTextViewTxt(tvPrincipal, t.getPrincipal());
								FormsUtil.setTextViewTxt(tvInterest, t.getInterest());
								FormsUtil.setTextViewTxt(tvInterestTotal, t.getInterestTotal());
							}
						}
					});
		}
	}

	/**
	 * 请求列表
	 * 
	 * @param isShowDialog
	 *            是否显示Dialog
	 */
	private void requestList(final boolean isShowDialog) {
		try {
			this.isShowDialog = isShowDialog;
			String strXml = "";
			if ("0".equals(type) || "1".equals(type)) {
				CspXmlYfx012 cspXmlYfx012 = new CspXmlYfx012(getCacheBean().get(CacheBean.CUST_ID).toString(), type,
						"10", (pageIndex + ""));
				strXml = cspXmlYfx012.getCspXml();
			} else {
				CspXmlYfx015 cspXmlYfx015 = new CspXmlYfx015(getCacheBean().get(CacheBean.CUST_ID).toString(), loanNo);
				strXml = cspXmlYfx015.getCspXml();
			}
			// 生成MCIS报文
			Mcis mcis = new Mcis(strXml, TransactionValue.CSPSZF);
			byte[] byteMessage = {};
			byteMessage = mcis.getMcis();
			// 发送报文
			CspUtil cspUtil = new CspUtil(this);
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
					CspUtil.onFailure(RecordsListActivity.this, responStr);
					loadingView.setVisibility(View.VISIBLE);
					loadingView.setmOnRetryListener(new OnRetryListener() {

						@Override
						public void retry() {
							requestList(isShowDialog);
						}
					});
				}
			}, isShowDialog);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 请求列表成功后
	 * 
	 * @param responStr
	 */
	private void onGetListSuccess(String responStr) {
		loadingView.setVisibility(View.GONE);
		RecordsResponse recordsResponse = XStreamUtils.getFromXML(responStr, RecordsResponse.class);
		ConstHead constHead = recordsResponse.getConstHead();
		if (constHead != null) {
			if ("00".equals(constHead.getErrCode())) {
				if ("0".equals(type) && recordsResponse.getContainer().getLoanList() != null) {
	//				ldList.clear();
					ldList.addAll(recordsResponse.getContainer().getLoanList());
					ldAdapter.notifyDataSetChanged();
				} else if ("1".equals(type) && recordsResponse.getContainer().getReList() != null) {
	//				reList.clear();
					reList.addAll(recordsResponse.getContainer().getReList());
					reAdapter.notifyDataSetChanged();
				} else if ("2".equals(type) && recordsResponse.getContainer().getHiList() != null) {
	//				ahList.clear();
					llInventory.setVisibility(View.VISIBLE);
					ahList.addAll(recordsResponse.getContainer().getHiList());
					ahAdapter.notifyDataSetChanged();
				}
				if (TextUtils.isEmpty(recordsResponse.getContainer().getCurrPage())) {
					pageIndex = 0;
				} else {
					pageIndex = Integer.parseInt(recordsResponse.getContainer().getCurrPage());
				}
				if (!TextUtils.isEmpty(recordsResponse.getContainer().getPageNum())) {
					totalPage = Integer.parseInt(recordsResponse.getContainer().getPageNum());
				}
			} else if ("50".equals(constHead.getErrCode())) {
				DialogUtil.showWithToMain(RecordsListActivity.this, constHead.getErrMsg());
			} else {
				CspUtil.onFailure(this, constHead.getErrMsg());
				loadingView.setVisibility(View.VISIBLE);
				loadingView.setmOnRetryListener(new OnRetryListener() {
	
					@Override
					public void retry() {
						requestList(isShowDialog);
					}
				});
			}
		}
		/** 大于1时，上拉加载 */
		if (pageIndex >= 1) {
			if (!canLoadMore) {
				footerView.setVisibility(View.VISIBLE);
				llLoading.setVisibility(View.GONE);
				tvTips.setVisibility(View.VISIBLE);
			} else {
				footerView.setVisibility(View.GONE);
			}
		} else {
			canLoadMore = false;
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		int lastItem = 0;
		if ("0".equals(type)) {
			lastItem = ldAdapter.getCount();
		} else if ("1".equals(type)) {
			lastItem = reAdapter.getCount();
		} else if ("2".equals(type)) {
			lastItem = ahAdapter.getCount();
		}
		if (scrollState == 0) {
			// 当前可见的item和每一页的item条数相同时
			if (vItemCount == lastItem) {
				if (canLoadMore) {
					pageIndex++;
					if (pageIndex == totalPage) {
						canLoadMore = false;
					}
					footerView.setVisibility(View.VISIBLE);
					llLoading.setVisibility(View.VISIBLE);
					tvTips.setVisibility(View.GONE);
					requestList(false);
				}
			}
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		vItemCount = firstVisibleItem + visibleItemCount - 1;
	}

	private void setTvLoanStatus(TextView tvLoanStatus, String loanStatus) {
		if ("50".equals(loanStatus)) {
			loanStatus = "逾期";
			tvLoanStatus.setTextColor(getResources().getColor(R.color.redLight));
		} else if ("40".equals(loanStatus)) {
			loanStatus = "结清";
			tvLoanStatus.setTextColor(getResources().getColor(R.color.greenDark));
		} else {
			loanStatus = "正常";
			tvLoanStatus.setTextColor(getResources().getColor(R.color.blueDark));
		}
		FormsUtil.setTextViewTxt(tvLoanStatus, loanStatus);
	}

}
