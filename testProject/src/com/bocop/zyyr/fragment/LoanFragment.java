package com.bocop.zyyr.fragment;

import java.util.ArrayList;
import java.util.List;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.base.BaseAdapter;
import com.boc.jx.base.BaseFragment;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.tools.DialogUtil;
import com.boc.jx.tools.ImageViewUtil;
import com.boc.jx.view.LoadingView;
import com.boc.jx.view.LoadingView.OnRetryListener;
import com.boc.jx.view.swipemenulistview.SwipeMenu;
import com.boc.jx.view.swipemenulistview.SwipeMenuCreator;
import com.boc.jx.view.swipemenulistview.SwipeMenuItem;
import com.boc.jx.view.swipemenulistview.SwipeMenuListView;
import com.boc.jx.view.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.config.BocSdkConfig;
import com.bocop.jxplatform.util.CspUtil;
import com.bocop.jxplatform.util.CspUtil.CallBack;
import com.bocop.jxplatform.util.FormsUtil;
import com.bocop.jxplatform.util.LoginUtil;
import com.bocop.xms.bean.ConstHead;
import com.bocop.xms.tools.ViewHolder;
import com.bocop.xms.utils.XStreamUtils;
import com.bocop.yfx.utils.ToastUtils;
import com.bocop.zyyr.activity.LoanDetailsActivity;
import com.bocop.zyyr.activity.MyLoanActivity;
import com.bocop.zyyr.bean.CommonResponse;
import com.bocop.zyyr.bean.LoanListDetails;
import com.bocop.zyyr.bean.LoanResponse;
import com.bocop.zyyr.bean.StatusResponse;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.RequestBody;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 *
 * 贷款列表
 *
 * @author lh
 *
 */
public class LoanFragment extends BaseFragment implements OnScrollListener {

	@ViewInject(R.id.lvLoan)
	private SwipeMenuListView lvLoan;
	@ViewInject(R.id.loadingView)
	private LoadingView loadingView;

	List<LoanListDetails> list = new ArrayList<>();
	BaseAdapter<LoanListDetails> adapter;
	private View footerView;
	private LinearLayout llLoading;
	private TextView tvTips;
	private int vItemCount;
	private int pageIndex = 1;
	private boolean canLoadMore = true;
	private int deletePosition;

	/** 用于区分不同列表的标志位 */
	private String status;

	private boolean HIDDEN_FLAG = false;

	private boolean FIRST_REQUEST_FLAG = true;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = initView(R.layout.zyyr_fragment_loan_all);
		return view;
	}

	@Override
	protected void initData() {
		super.initData();
		lvLoan.setAdapter(adapter = new LoanAdapter(baseActivity, list));
	}

	@Override
	protected void initView() {
		super.initView();

		footerView = LayoutInflater.from(baseActivity).inflate(R.layout.common_layout_listview_footer, null);
		llLoading = (LinearLayout) footerView.findViewById(R.id.llLoading);
		tvTips = (TextView) footerView.findViewById(R.id.tvTips);
		footerView.setVisibility(View.GONE);
		lvLoan.addFooterView(footerView, null, false);

		SwipeMenuCreator menuCreator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				/** 添加侧滑选项 */
				SwipeMenuItem deleteItem = new SwipeMenuItem(baseActivity);
				deleteItem.setBackground(R.color.redLight);
				deleteItem.setWidth(FormsUtil.dip2px(90));
				deleteItem.setIcon(R.drawable.ic_delete);
				menu.addMenuItem(deleteItem);
			}
		};

		lvLoan.setMenuCreator(menuCreator);

		initListener();
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if (!hidden) {
			/** 只有“全部”和“申请中”有删除操作，有删除操作时，重新请求列表 */
			if (MyLoanActivity.DELETE_FLAG) {
				if ("0".equals(status) || "1".equals(status) || "3".equals(status)) {
					pageIndex = 1;
					requestLoanList(true);
				}
			} else {
				if (HIDDEN_FLAG && FIRST_REQUEST_FLAG) {
					FIRST_REQUEST_FLAG = false;
					requestLoanList(true);
				}
			}
		} else {
			HIDDEN_FLAG = true;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		/** 只有“全部”和“申请中”有删除操作，有删除操作时，重新请求列表 */
		if ("0".equals(status) || "1".equals(status) || "3".equals(status)) {
			if (MyLoanActivity.DELETE_FLAG) {
				pageIndex = 1;
				requestLoanList(true);
			}
		}
	}

	private void initListener() {
		lvLoan.setOnScrollListener(this);

		lvLoan.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {

				deletePosition = position;
				switch (index) {
				case 0:// 删除
					DialogUtil.showWithTwoBtn(baseActivity, "确定要取消申请吗？", "确定", "取消",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
									String appId = list.get(position).getAppID();
									requestDeleteLoan(appId);
								}
							}, new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
								}
							});
					break;
				}
				return false;
			}
		});

		lvLoan.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (position != lvLoan.getCount() - 1) {
					Intent intent = new Intent(baseActivity, LoanDetailsActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("APP_ID", list.get(position).getAppID());
					bundle.putString("STATUS", list.get(position).getAppStatus());
					intent.putExtras(bundle);
					startActivity(intent);
				}
			}
		});
	}

	/**
	 * 请求贷款列表
	 *
	 * @param isShowDialog
	 *            是否显示Dialog
	 */
	private void requestLoanList(final boolean isShowDialog) {
		RequestBody formBody = new FormEncodingBuilder().add("userId", LoginUtil.getUserId(baseActivity))
				.add("appStatus", status).add("page", String.valueOf(pageIndex)).build();
		CspUtil cspUtil = new CspUtil(baseActivity);
		cspUtil.postCspNoLogin(BocSdkConfig.ZYYR_LOAN_LIST, formBody, isShowDialog, new CallBack() {
			@Override
			public void onSuccess(String responStr) {
				onGetListSuccess(responStr);
			}

			@Override
			public void onFinish() {

			}

			@Override
			public void onFailure(String responStr) {
				loadingView.setVisibility(View.VISIBLE);
				lvLoan.setVisibility(View.GONE);
				loadingView.setmOnRetryListener(new OnRetryListener() {

					@Override
					public void retry() {
						requestLoanList(isShowDialog);
					}
				});
			}
		});
	}

	/**
	 * 取消申请
	 *
	 * @param appId
	 *            申请流水号
	 */
	private void requestDeleteLoan(String appId) {
		RequestBody formBody = new FormEncodingBuilder().add("appId", appId).build();
		CspUtil cspUtil = new CspUtil(baseActivity);
		cspUtil.postCspNoLogin(BocSdkConfig.ZYYR_DELETE_LOAN, formBody, true, new CallBack() {
			@Override
			public void onSuccess(String responStr) {
				StatusResponse statusResponse = XStreamUtils.getFromXML(responStr, StatusResponse.class);
				ConstHead constHead = statusResponse.getConstHead();
				if (null != constHead && "00".equals(constHead.getErrCode())) {
					MyLoanActivity.DELETE_FLAG = true;
					list.remove(deletePosition);
					adapter.notifyDataSetChanged();
				} else {
					Toast.makeText(baseActivity, R.string.applyFailure, Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFinish() {

			}

			@Override
			public void onFailure(String responStr) {
				CspUtil.onFailure(baseActivity, responStr);
			}
		});
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * 请求列表成功后
	 *
	 * @param responStr
	 */
	private void onGetListSuccess(String responStr) {
		loadingView.setVisibility(View.GONE);
		lvLoan.setVisibility(View.VISIBLE);
		LoanResponse loanResponse = XStreamUtils.getFromXML(responStr, LoanResponse.class);
		ConstHead constHead = loanResponse.getConstHead();
		if (null != constHead && "00".equals(constHead.getErrCode())) {
			CommonResponse commonResponse = loanResponse.getList().getCommonResponse();
			pageIndex = Integer.parseInt(commonResponse.getCurrentPage());
			if (pageIndex == 1) {
				list.clear();
			}
			if (pageIndex == Integer.parseInt(commonResponse.getTotalPages())) {
				canLoadMore = false;
			}
			list.addAll(loanResponse.getList().getList());
			adapter.notifyDataSetChanged();
		} else if ("01".equals(constHead.getErrCode())) {
			ToastUtils.showInfo(baseActivity, "暂无数据", Toast.LENGTH_SHORT);
		}
		if (!canLoadMore) {
			footerView.setVisibility(View.VISIBLE);
			llLoading.setVisibility(View.GONE);
			tvTips.setVisibility(View.VISIBLE);
		} else {
			footerView.setVisibility(View.GONE);
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		int lastItem = adapter.getCount();
		if (scrollState == 0) {
			// 当前可见的item和每一页的item条数相同时
			if (vItemCount == lastItem) {
				if (canLoadMore) {
					pageIndex++;
					footerView.setVisibility(View.VISIBLE);
					llLoading.setVisibility(View.VISIBLE);
					tvTips.setVisibility(View.GONE);
					requestLoanList(false);
				}
			}
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		vItemCount = firstVisibleItem + visibleItemCount - 1;
	}

	private class LoanAdapter extends BaseAdapter<LoanListDetails> {

		private float preX = 0, preY = 0, nowX = 0, nowY = 0;

		public LoanAdapter(BaseActivity activity, List<LoanListDetails> tList, int layoutResId) {
			super(activity, tList, layoutResId);
			// TODO Auto-generated constructor stub
		}

		public LoanAdapter(BaseActivity activity, List<LoanListDetails> tList) {
			this(activity, tList, R.layout.zyyr_item_my_loan);
		}

		@Override
		public void viewHandler(int position, final LoanListDetails t, View convertView) {
			TextView tvProType = ViewHolder.get(convertView, R.id.tvProType);
			TextView tvProIntro = ViewHolder.get(convertView, R.id.tvProIntro);
			TextView tvStatus = ViewHolder.get(convertView, R.id.tvStatus);
			ImageView ivProIcon = ViewHolder.get(convertView, R.id.ivProIcon);
			final RelativeLayout rlItem = ViewHolder.get(convertView, R.id.rlItem);

			if (null != t) {
				tvProType.setText(t.getProName());
				tvProIntro.setText(t.getProDesc());
				tvStatus.setText(t.getAppStatus());

				if ("1".equals(t.getAppStatus())) {// 申请中
					tvStatus.setText("申请中");
					tvStatus.setTextColor(getResources().getColor(R.color.TextColor_applying));
				} else if ("2".equals(t.getAppStatus())) {// 已受理
					tvStatus.setText("已受理");
					tvStatus.setTextColor(getResources().getColor(R.color.TextColor_finished));
				} else if ("3".equals(t.getAppStatus())) {// 已取消
					tvStatus.setText("已取消");
					tvStatus.setTextColor(getResources().getColor(R.color.TextColor_cancel));
				}

				baseActivity.getBaseApp().getImageLoader().displayImage(t.getProLogo(), ivProIcon,
						ImageViewUtil.getOption());
			}

			rlItem.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					lvLoan.smoothCloseMenu();
					if ("1".equals(t.getAppStatus())) {
						/** 设置为true，则有侧滑操作 */
						lvLoan.setFLAG(true);
						return false;
					} else {
						/** 设置为false，则取消侧滑操作，此时需重写触摸事件 */
						lvLoan.setFLAG(false);
						switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN:
							preX = event.getX();
							preY = event.getY();
							rlItem.setBackgroundColor(getResources().getColor(R.color.gray));
							break;
						case MotionEvent.ACTION_UP:
							nowX = event.getX();
							nowY = event.getY();

							if ((Math.abs(nowX - preX) < 10) && (Math.abs(nowY - preY) < 10)) {
								Intent intent = new Intent(baseActivity, LoanDetailsActivity.class);
								intent.putExtra("APP_ID", t.getAppID());
								startActivity(intent);
							}
							rlItem.setBackgroundColor(getResources().getColor(R.color.white));
							break;
						case MotionEvent.ACTION_CANCEL:
							rlItem.setBackgroundColor(getResources().getColor(R.color.white));
							break;
						}
						return true;
					}
				}
			});
		}
	}
}
