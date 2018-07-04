package com.bocop.zyyr.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.media.tv.TvView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.base.BaseAdapter;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.boc.jx.baseUtil.view.annotation.event.OnTouch;
import com.boc.jx.tools.ImageViewUtil;
import com.boc.jx.view.LoadingView;
import com.boc.jx.view.LoadingView.OnRetryListener;
import com.boc.jx.view.indicator.CirclePageIndicator;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.adapter.LoopViewPagerAdapter;
import com.bocop.jxplatform.bean.Advertisement;
import com.bocop.jxplatform.config.BocSdkConfig;
import com.bocop.jxplatform.util.CspUtil;
import com.bocop.jxplatform.util.CspUtil.CallBack;
import com.bocop.jxplatform.util.CustomProgressDialog;
import com.bocop.jxplatform.util.FormsUtil;
import com.bocop.jxplatform.util.LoginUtil;
import com.bocop.jxplatform.util.LoginUtil.ILoginListener;
import com.bocop.xms.bean.ConstHead;
import com.bocop.xms.tools.ViewHolder;
import com.bocop.xms.utils.XStreamUtils;
import com.bocop.yfx.utils.ToastUtils;
import com.bocop.zyyr.bean.CommonResponse;
import com.bocop.zyyr.bean.ProductInfo;
import com.bocop.zyyr.bean.ProductListResponse;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.RequestBody;
import com.thoughtworks.xstream.io.StreamException;

/**
 * 
 * 中银易融
 * 
 * @author lh
 * 
 */
@ContentView(R.layout.zyyr_activity_fina_main)
public class FinanceMainActivity extends BaseActivity implements OnScrollListener, ILoginListener {

	@ViewInject(R.id.tv_titleName)
	private TextView tvTitle;
	@ViewInject(R.id.lvProList)
	private ListView lvProList;
	@ViewInject(R.id.loadingView)
	private LoadingView loadingView;
	@ViewInject(R.id.ivItem)
	private ImageView ivItem;
	@ViewInject(R.id.tvItem)
	private TextView tvItem;

	private List<ProductInfo> list = new ArrayList<ProductInfo>();
	private BaseAdapter<ProductInfo> adapter;
	private int pageIndex = 1;
	private View footerView;
	private LinearLayout llLoading;
	private TextView tvTips;
	private int vItemCount;
	private boolean canLoadMore = true;

	// 轮播图
	@ViewInject(R.id.ivSingleImage)
	private ImageView ivSingleImage;
	@ViewInject(R.id.rltAd)
	private RelativeLayout rltAd;
	@ViewInject(R.id.vpAd)
	private ViewPager vpAd;
	@ViewInject(R.id.indicator)
	private CirclePageIndicator indicator;
	@ViewInject(R.id.rlAd)
	private RelativeLayout rlAd;

	private float preX = 0;
	private float preY = 0;
	private float nowX = 0;
	private float nowY = 0;
	private boolean isTouch = false;// 是否正在拖动轮播图

	private List<Advertisement> mAdvList = new ArrayList<Advertisement>();
	private List<View> views = new ArrayList<View>();

	private Handler adHandler = new Handler() { // 启动广告页面自动播放

		@Override
		public void handleMessage(Message msg) {
			if (!isTouch) {
				if (vpAd.getCurrentItem() == views.size() - 1) {
					vpAd.setCurrentItem(0, false);
				} else {
					vpAd.setCurrentItem(vpAd.getCurrentItem() + 1);
				}
				adHandler.sendEmptyMessageDelayed(0, 3000);
			} else {
				adHandler.sendEmptyMessageDelayed(0, 3000);
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initView();
		initDate();
		initListener();
		initAdViews();
		requestProductList(true);
	}

	private void initDate() {
		lvProList.setAdapter(adapter = new BaseAdapter<ProductInfo>(this, list, R.layout.zyyr_item_pro_list) {

			@Override
			public void viewHandler(int position, final ProductInfo t, View convertView) {
				TextView tvProName = ViewHolder.get(convertView, R.id.tvProName);
				TextView tvProDesc = ViewHolder.get(convertView, R.id.tvProDesc);
				TextView tvRate = ViewHolder.get(convertView, R.id.tvRate);
				ImageView ivProIcon = ViewHolder.get(convertView, R.id.ivProIcon);

				if (null != t) {
					tvProName.setText(t.getProName());
					tvProDesc.setText(t.getProDesc());
					tvRate.setText((Double.parseDouble(t.getRate().replaceAll(" ", "")) * 100) + "%");
					getBaseApp().getImageLoader().displayImage(t.getImgUrl(), ivProIcon, ImageViewUtil.getOption());
				}
			}
		});
	}

	private void initView() {
		tvTitle.setText("创业通");
		ivItem.setVisibility(View.GONE);
		tvItem.setVisibility(View.VISIBLE);

		footerView = LayoutInflater.from(this).inflate(R.layout.common_layout_listview_footer, null);
		llLoading = (LinearLayout) footerView.findViewById(R.id.llLoading);
		tvTips = (TextView) footerView.findViewById(R.id.tvTips);
		footerView.setVisibility(View.GONE);
		lvProList.addFooterView(footerView, null, false);
	}

	private void initListener() {

		lvProList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(FinanceMainActivity.this, ProductDetailsActivity.class);
				intent.putExtra("PRO_ID", list.get(position).getProId());
				startActivity(intent);
			}
		});

		lvProList.setOnScrollListener(this);

	}

	@OnClick({ R.id.tvItem })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tvItem:
			if (!BocSdkConfig.isTest) {
				if (getBaseApp().isNetStat()) {
					if (LoginUtil.isLog(this)) {
						callMe(UserCenterActivity.class);
					} else {
						LoginUtil.authorize(this, FinanceMainActivity.this);
					}
				} else {
					CustomProgressDialog.showBocNetworkSetDialog(this);
				}
			} else {
				callMe(UserCenterActivity.class);
			}
			break;
		}
	}

	/**
	 * 请求产品列表
	 * 
	 * @param isShowDialog
	 *            是否显示Dialog
	 */
	private void requestProductList(final boolean isShowDialog) {
		RequestBody formBody = new FormEncodingBuilder().add("page", String.valueOf(pageIndex)).build();
		CspUtil cspUtil = new CspUtil(this);
		cspUtil.postCspNoLogin(BocSdkConfig.ZYYR_PDT_LIST, formBody, isShowDialog, new CallBack() {
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
				loadingView.setmOnRetryListener(new OnRetryListener() {

					@Override
					public void retry() {
						requestProductList(isShowDialog);
					}
				});
			}
		});
	}

	/**
	 * 请求列表成功
	 * 
	 * @param responStr
	 */
	private void onGetListSuccess(String responStr) {
		loadingView.setVisibility(View.GONE);
		rlAd.setVisibility(View.VISIBLE);
		try {
			ProductListResponse listResponse = XStreamUtils.getFromXML(responStr, ProductListResponse.class);
			ConstHead constHead = listResponse.getConstHead();
			if (null != constHead && "00".equals(constHead.getErrCode())) {
				CommonResponse commonResponse = listResponse.getList().getCommonResponse();
				pageIndex = Integer.parseInt(commonResponse.getCurrentPage());
				if (pageIndex == 1) {
					list.clear();
				}
				if (pageIndex == Integer.parseInt(commonResponse.getTotalPages())) {
					canLoadMore = false;
				}
				list.addAll(listResponse.getList().getList());
				adapter.notifyDataSetChanged();
			} else if ("01".equals(constHead.getErrCode())) {
				ToastUtils.showInfo(this, "暂无数据", Toast.LENGTH_SHORT);
			}
		} catch (StreamException e) {
			loadingView.setVisibility(View.VISIBLE);
			rlAd.setVisibility(View.GONE);
			loadingView.setmOnRetryListener(new OnRetryListener() {

				@Override
				public void retry() {
					requestProductList(true);
				}
			});
			ToastUtils.showError(this, "后台数据异常", Toast.LENGTH_SHORT);
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
					requestProductList(false);
				}
			}
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		vItemCount = firstVisibleItem + visibleItemCount - 1;
	}

	@Override
	public void onLogin() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCancle() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onError() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onException() {
		// TODO Auto-generated method stub

	}

	// 轮播图

	private void initAdViews() {
		mAdvList.clear();
		Advertisement advertisement1 = new Advertisement();
		Advertisement advertisement2 = new Advertisement();
		Advertisement advertisement3 = new Advertisement();
		advertisement1.setImageRes(R.drawable.zyyr_ad_page01);
		advertisement2.setImageRes(R.drawable.zyyr_ad_page02);
		advertisement3.setImageRes(R.drawable.zyyr_ad_page03);

		mAdvList.add(advertisement1);
		mAdvList.add(advertisement2);
		mAdvList.add(advertisement3);

		setAdHeight();
		notifyForAdPic();
	}

	@OnTouch(R.id.vpAd)
	public boolean onTouch(View v, MotionEvent event) { // 根据触摸情况，判断轮播图触摸事件
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			isTouch = true;
			preX = event.getX();
			preY = event.getY();
			break;
		case MotionEvent.ACTION_UP:
			isTouch = false;
			nowX = event.getX();
			nowY = event.getY();
			if (nowX == preX && nowY == preY) {
				clickAdPic(vpAd.getCurrentItem());
			}
			break;

		case MotionEvent.ACTION_CANCEL:
			isTouch = false;
			break;
		}
		return false;
	}

	/**
	 * 轮播图点击事件
	 * 
	 * @param index
	 */
	private void clickAdPic(int index) {
		if (mAdvList != null && mAdvList.size() != 0) {
			if (mAdvList.size() == index + 1) {
				vpAd.setCurrentItem(0, false);
			}
		}
	}

	/**
	 * 响应轮播图请求
	 * 
	 * @param retCode
	 * @param response
	 */
	public void notifyForAdPic() {

		if (mAdvList != null && mAdvList.size() > 1) {// 多张图片
			views.clear();
			rltAd.setVisibility(View.VISIBLE);
			ivSingleImage.setVisibility(View.GONE);
			for (Advertisement advertisement : mAdvList) {
				View view = LayoutInflater.from(this).inflate(R.layout.page_ad, null);
				ImageView iv = (ImageView) view.findViewById(R.id.ivAd);
				// getBaseApp().getImageLoader().displayImage(advertisement.getImageUrl(),
				// iv, ImageViewUtil.getOption());
				iv.setImageResource(advertisement.getImageRes());
				views.add(view);
			}
			LoopViewPagerAdapter adapter = new LoopViewPagerAdapter(views);
			vpAd.setAdapter(adapter);
			indicator.setViewPager(vpAd);
			// 启动轮播图
			if (adHandler.hasMessages(0)) {
				adHandler.removeMessages(0);
			}
			adHandler.sendEmptyMessageDelayed(0, 3000);
		} else if (mAdvList != null && mAdvList.size() == 1) {// 单张图片
			rltAd.setVisibility(View.GONE);
			ivSingleImage.setVisibility(View.VISIBLE);
			getBaseApp().getImageLoader().displayImage(mAdvList.get(0).getImageUrl(), ivSingleImage,
					ImageViewUtil.getOption());
		}

	}

	private void setAdHeight() {
		int height = FormsUtil.SCREEN_WIDTH / 2;
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, height);
		rlAd.setLayoutParams(params);
	}

}
