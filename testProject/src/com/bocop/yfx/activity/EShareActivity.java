package com.bocop.yfx.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.boc.jx.baseUtil.cache.CacheBean;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.boc.jx.tools.ImageViewUtil;
import com.boc.jx.view.indicator.CirclePageIndicator;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.activity.ZYEDActivity;
import com.bocop.jxplatform.adapter.LoopViewPagerAdapter;
import com.bocop.jxplatform.bean.Advertisement;
import com.bocop.jxplatform.config.BocSdkConfig;
import com.bocop.jxplatform.util.CspUtil;
import com.bocop.jxplatform.util.CustomInfo;
import com.bocop.jxplatform.util.CspUtil.CallBack;
import com.bocop.jxplatform.util.LoginUtil.OnRequestCustCallBack;
import com.bocop.jxplatform.util.LoginUtil;
import com.bocop.xms.bean.ConstHead;
import com.bocop.xms.utils.XStreamUtils;
import com.bocop.yfx.base.EShareBaseActivity;
import com.bocop.yfx.bean.ImgUrl;
import com.bocop.yfx.bean.ImgUrlListResponse;
import com.bocop.yfx.bean.InfoStatusResponse;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.RequestBody;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * 易分享
 * 
 * @author lh
 * 
 */
@ContentView(R.layout.yfx_activity_eshare2)
public class EShareActivity extends EShareBaseActivity {

	@ViewInject(R.id.iv_imageLeft)
	private ImageView ivLeft;
	@ViewInject(R.id.tv_titleName)
	private TextView tvTitle;
	// @ViewInject(R.id.ivSingleImage)
	private ImageView ivSingleImage;
	// @ViewInject(R.id.rltAd)
	private RelativeLayout rltAd;
	// @ViewInject(R.id.vpAd)
	private ViewPager vpAd;
	// @ViewInject(R.id.indicator)
	private CirclePageIndicator indicator;
	
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

	InfoStatusResponse statusResponse;
	private List<ImgUrl> imgList = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initView();
		// CacheBean.getInstance().put(CacheBean.CUST_ID, "001");// 测试
	}

	/**
	 * 工薪贷点击事件
	 * 
	 * @param v
	 */
	@OnClick({ R.id.llGXD, R.id.llXJFQ, R.id.llJZFP, R.id.ivGXD, R.id.ivXJFQ, R.id.ivJZFP })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.llGXD:
		case R.id.llJZFP:
			goToZZYD();	
			break;

		/** 图片按钮，点击事件与所属区域保持一致 */
		case R.id.ivGXD:
		case R.id.ivJZFP:
			goToZZYD();			
			break;
		case R.id.ivXJFQ:
			Intent intent2 = new Intent(this, ZYEDActivity.class);
			startActivity(intent2);
			break;
		case R.id.llXJFQ:
			Intent intent = new Intent(this, ZYEDActivity.class);
			startActivity(intent);
			break;
		}
	}
	
	/**
	 *跳转到中银E贷界面
	 */
	private void goToZZYD() {
		
		if (!BocSdkConfig.isTest) {
			if (null !=  CustomInfo.getCustomId(this)) {
				if (checkTime()) {
					Bundle bundle = new Bundle();
					bundle.putInt("PRO_FLAG", 0);
					callMe(LoanMainActivity.class,bundle);
				} else {
					Toast.makeText(this, "温馨提示：每日 07:00  --  21:00 提供服务。", Toast.LENGTH_SHORT).show();
				}
			} else {
				LoginUtil.requestBocopForCustid(this, true, new OnRequestCustCallBack() {
					
					@Override
					public void onSuccess() {
						Bundle bundle = new Bundle();
						bundle.putInt("PRO_FLAG", 0);
						callMe(LoanMainActivity.class,bundle);
					}
				});
			}
		} else {
			CacheBean.getInstance().put(CacheBean.CUST_ID, "");
			Bundle bundle = new Bundle();
			bundle.putInt("PRO_FLAG", 0);
			callMe(LoanMainActivity.class,bundle);
		}
	}
	
	
	/**
	 * 检验时间是否在规定区间内
	 * 
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	private boolean checkTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");

		int nowTime = Integer.parseInt(sdf.format(new Date()));
		if (nowTime >= 70000 && nowTime <= 210000) {
			return true;
		}

		return false;
	}

	private void initView() {
		tvTitle.setText("中银E贷");
		// requestRollImageList();
	}

	// @OnTouch(R.id.vpAd)
	// public boolean onTouch(View v, MotionEvent event) { // 根据触摸情况，判断轮播图触摸事件
	// switch (event.getAction()) {
	// case MotionEvent.ACTION_DOWN:
	// isTouch = true;
	// preX = event.getX();
	// preY = event.getY();
	// break;
	// case MotionEvent.ACTION_UP:
	// isTouch = false;
	// nowX = event.getX();
	// nowY = event.getY();
	// if (nowX == preX && nowY == preY) {
	// clickAdPic(vpAd.getCurrentItem());
	// }
	// break;
	//
	// case MotionEvent.ACTION_CANCEL:
	// isTouch = false;
	// break;
	// }
	// return false;
	// }
	

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
				getBaseApp().getImageLoader().displayImage(advertisement.getImageUrl(), iv, ImageViewUtil.getOption());
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

	/**
	 * 请求轮播图
	 */
	private void requestRollImageList() {
		RequestBody formBody = new FormEncodingBuilder().build();
		CspUtil cspUtil = new CspUtil(this);
		cspUtil.postCspNoLogin(BocSdkConfig.YFX_IMG_LIST, formBody, true, new CallBack() {
			@Override
			public void onSuccess(String responStr) {
				ImgUrlListResponse imgUrlListResponse = XStreamUtils.getFromXML(responStr, ImgUrlListResponse.class);
				ConstHead constHead = imgUrlListResponse.getConstHead();
				if (null != constHead && "00".equals(constHead.getErrCode())) {
					imgList = imgUrlListResponse.getImgUrlList().getList();
					if (imgList.size() > 0) {
						mAdvList.clear();
						for (int i = 0; i < imgList.size(); i++) {
							Advertisement advertisement = new Advertisement();
							advertisement.setImageUrl(imgList.get(i).getImgUrl());
							mAdvList.add(advertisement);
						}
					}
					notifyForAdPic();
				}
			}

			@Override
			public void onFinish() {
				LoginUtil.requestBocopForCustid(EShareActivity.this, true);
			}

			@Override
			public void onFailure(String responStr) {
				CspUtil.onFailure(EShareActivity.this, responStr);
			}
		});
	}

}
