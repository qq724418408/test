package com.bocop.yfx.activity;

import java.util.ArrayList;
import java.util.List;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.boc.jx.view.LoadingView;
import com.boc.jx.view.indicator.CirclePageIndicator;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.activity.ZYEDActivity;
import com.bocop.jxplatform.adapter.LoopViewPagerAdapter;
import com.bocop.jxplatform.bean.Advertisement;
import com.bocop.jxplatform.view.BackButton;
import com.bocop.xms.utils.FormsUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * 产品详情
 * 
 * @author rd
 */

@ContentView(R.layout.activity_gdt_forzh)
public class LoanActivity extends BaseActivity {
	@ViewInject(R.id.tv_titleName)
	private TextView tv_titleName;
	@ViewInject(R.id.iv_imageLeft)
	private BackButton backBtn;
	
	@ViewInject(R.id.reLayout)
	private RelativeLayout reLayout;
	@ViewInject(R.id.llCompleteInfo)
	private LinearLayout llCompleteInfo;// 完善信息框
	@ViewInject(R.id.loadingView)
	private LoadingView loadingView;
	@ViewInject(R.id.scrollView)
	private ScrollView scrollView;
	@ViewInject(R.id.btnApply)
	private Button btnApply;

	@ViewInject(R.id.reLayout1)
	private RelativeLayout reLayout1;
	@ViewInject(R.id.svNewUI)
	private ScrollView svNewUI;
	@ViewInject(R.id.llGAZS)
	private LinearLayout llGAZS;
	@ViewInject(R.id.llFPT)
	private LinearLayout llFPT;
	@ViewInject(R.id.llFNT)
	private LinearLayout llFNT;
	@ViewInject(R.id.llBG)
	private LinearLayout llBG;
	@ViewInject(R.id.ivSteps)
	private ImageView ivSteps;
	@ViewInject(R.id.rlBG)
	private RelativeLayout rlBG;

	private int PRO_FLAG;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("tag", "start initText");
		initText();
	}

	private void initText() {
		FormsUtil.getDisplayMetrics(LoanActivity.this);
		tv_titleName.setText("中银E贷·代发薪专属");
		PRO_FLAG = getIntent().getExtras().getInt("PRO_FLAG");
		switch (PRO_FLAG) {
		case 0:
			Log.i("tag", "initText case 0");
			setAdHeight(0);
			initAdList(0);
			notifyForAdPic();
			rlBG.setBackgroundResource(R.drawable.yfx_bg_gazs);
			break;
		case 1:
			setAdHeight(1);
			initAdList(1);
			notifyForAdPic();
			rlBG.setBackgroundResource(R.drawable.yfx_bg_fpt);
			break;
		case 2:
			setAdHeight(2);
			initAdList(2);
			notifyForAdPic();
			rlBG.setBackgroundResource(R.drawable.yfx_bg_fnt);
			llFNT.setVisibility(View.GONE);
			break;

		case 3:
			setAdHeight(3);
			initAdList(3);
			notifyForAdPic();
			rlBG.setBackgroundResource(R.drawable.icon_gjtbackground);
			break;
		}
	}

	@OnClick({ R.id.btnApply })
	public void onClick(View v) {
		switch (PRO_FLAG) {
		case 0:
			Intent intent2 = new Intent(LoanActivity.this, ZYEDActivity.class);
			startActivity(intent2);
			break;
		}
	}

	/** 轮播图功能模块 */
	@ViewInject(R.id.ivSingleImage)
	private ImageView ivSingleImage;
	@ViewInject(R.id.rltAd)
	private RelativeLayout rltAd;
	@ViewInject(R.id.vpAd)
	private ViewPager vpAd;
	@ViewInject(R.id.rlAdRoot)
	private RelativeLayout rlAdRoot;
	@ViewInject(R.id.indicator)
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

		Log.i("tag", "start notifyForAdPic");
		if (mAdvList != null && mAdvList.size() > 1) {// 多张图片
			views.clear();
			rltAd.setVisibility(View.VISIBLE);
			ivSingleImage.setVisibility(View.GONE);
			Log.i("tag", "多张图片");
			for (Advertisement advertisement : mAdvList) {
				View view = LayoutInflater.from(LoanActivity.this).inflate(
						R.layout.page_ad, null);
				ImageView iv = (ImageView) view.findViewById(R.id.ivAd);
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
			Log.i("tag", "start 单张图片");
			rlBG.setVisibility(View.VISIBLE);
			rltAd.setVisibility(View.GONE);
			ivSingleImage.setVisibility(View.VISIBLE);
			ivSingleImage.setImageResource(mAdvList.get(0).getImageRes());
//			ivSingleImage.setImageResource(R.drawable.yfx_icon_gdtzh);
		}

	}

	private void initAdList(int flag) {
		mAdvList.clear();

		switch (flag) {
		case 0:
			Log.i("tag", "start initAdList 0");
			Advertisement gdt1 = new Advertisement();
			gdt1.setImageRes(R.drawable.yfx_icon_gdtzh);
			mAdvList.add(gdt1);
			ivSteps.setImageResource(R.drawable.yfx_trains_steps);
			break;
		}
	}

	private void setAdHeight(int flag) {
//		switch (flag) {
//		case 3:
//			double height1 = FormsUtil.SCREEN_WIDTH * 0.3;
//			LayoutParams layoutParams1 = new LayoutParams(
//					LayoutParams.MATCH_PARENT, (int) height1);
//			rlAdRoot.setLayoutParams(layoutParams1);
//			break;
//
//		default:
			double height2 = FormsUtil.SCREEN_WIDTH * 0.3;
			LayoutParams layoutParams2 = new LayoutParams(
					LayoutParams.MATCH_PARENT, (int) height2);
			rlAdRoot.setLayoutParams(layoutParams2);
//			break;
//		}
	}
}
