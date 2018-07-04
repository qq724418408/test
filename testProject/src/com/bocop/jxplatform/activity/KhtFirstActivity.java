/**
 * 
 */
package com.bocop.jxplatform.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.boc.jx.ab.view.sliding.AbSlidingPlayView;
import com.boc.jx.base.BaseActivity;
import com.boc.jx.base.BaseApplication;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.boc.jx.common.util.AbImageUtil;
import com.boc.jx.constants.Constants;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.bean.Advertisement;
import com.bocop.jxplatform.bean.SchoolBean;
import com.bocop.jxplatform.fragment.HomeFragment;
import com.bocop.jxplatform.util.CustomProgressDialog;
import com.bocop.jxplatform.util.LoginUtil;
import com.bocop.jxplatform.util.LoginUtilAnother;
import com.bocop.jxplatform.util.LoginUtilAnother.ILoginListener;
import com.bocop.kht.activity.KhtActivity;
/** 
 * @author luoyang  E-mail: luoyang8714@163.com
 * @version 创建时间：2017-1-11 上午8:57:44 
 * 类说明 
 */
/**
 * @author zhongye
 *
 */


/**
 * 签证通首页
 * 
 * @author luoyang
 * 
 */
@ContentView(R.layout.activity_khtfrist)
public class KhtFirstActivity extends BaseActivity implements ILoginListener {
	// txm
	private ArrayList<SchoolBean> fristList = new ArrayList<SchoolBean>();
	private TextView tv_titleName;
	private ImageView iv_title_left;
	protected BaseActivity baseActivity;
	private List<Advertisement> mAdvList = new ArrayList<Advertisement>();
	public BaseApplication baseApplication = BaseApplication.getInstance();

	// BaseApplication application = (BaseApplication) baseActivity
	// .getApplication();
	/**
	 * 集成图片轮播
	 */
	AbSlidingPlayView pv_playview;
	static final int ASPECT_X = 4, ASPECT_Y = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}

	/**
	 * 初始化
	 */
	private void initView() {
		baseActivity = (BaseActivity) KhtFirstActivity.this;
		tv_titleName = (TextView) findViewById(R.id.tv_titleName);
		tv_titleName.setText("开户通");
		iv_title_left = (ImageView) findViewById(R.id.iv_title_left);
		pv_playview = (AbSlidingPlayView) findViewById(R.id.spv_photos);
		initHeaderView();
	}

	@OnClick({R.id.ll_qykh,R.id.ll_grkh,R.id.iv_title_left})
	public void onClick(View v){
		switch(v.getId()){
		case R.id.ll_grkh:
			if (baseApplication.isNetStat()) {
				Bundle bundle = new Bundle();
				bundle.putString("url", Constants.UrlForMainKhtcard);
				bundle.putString("name", "开户通");
				Intent intent = new Intent(baseActivity, KhtActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
			} else {
				CustomProgressDialog.showBocNetworkSetDialog(this);
			}
			break;
		case R.id.ll_qykh:
				Bundle bundleHx = new Bundle();
				bundleHx.putString("url", Constants.UrlForMainQyKhtcard);
				bundleHx.putString("type", "kht");
				bundleHx.putString("name", "开户通");
				Intent intentHx = new Intent(baseActivity,
						WebForZytActivity.class);
				intentHx.putExtras(bundleHx);
				startActivity(intentHx);
			break;
		case R.id.iv_title_left:
			finish();
			break;
		}
	}
	
	private void initHeaderView() {
		pv_playview.setNavHorizontalGravity(Gravity.RIGHT);
		Drawable iv_playviewindex_off = getResources().getDrawable(
				R.drawable.iv_playviewindex_off);
		Drawable iv_playviewindex_on = getResources().getDrawable(
				R.drawable.iv_playviewindex_on);
		pv_playview.setPageLineImage(
				AbImageUtil.drawableToBitmap(iv_playviewindex_on),
				AbImageUtil.drawableToBitmap(iv_playviewindex_off));
		initPlayViewSize();
		initPlayViewContent();
		pv_playview.setPlayDuration(3000);
	}

	private void initPlayViewSize() {
		final ViewTreeObserver vto = pv_playview.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			boolean hasSetted = false;

			@Override
			public void onGlobalLayout() {
				if (hasSetted)
					return;
				hasSetted = true;
				LayoutParams params = pv_playview.getLayoutParams();
				int w = pv_playview.getMeasuredWidth();
				int h = w / ASPECT_X * ASPECT_Y;
				// 宽度放开的话就会根据宽高比进行适配
				// params.height = h;
				pv_playview.setLayoutParams(params);
				try {
					vto.removeGlobalOnLayoutListener(this);
				} catch (Exception e) {
				}
			}
		});
	}

	private void initPlayViewContent() {
		getAdPhotos();
	}

	void getAdPhotos() {
		pv_playview.removeAllViews();
		// 第一张
		Advertisement adv1 = new Advertisement();
		adv1.setImageRes(R.drawable.advkht01);
		// adv1.setContent("http://mp.weixin.qq.com/s?__biz=MjM5NDg0NzIzNA==&mid=401756730&idx=6&sn=ae322c5365773a4abf94f0a90dc5df60&scene=1&srcid=0115ZGwEkPDOdR1gKIke6zUw#rd");
		// // 网页url
		mAdvList.add(adv1);
		// 第二张
		Advertisement adv2 = new Advertisement();
		adv2.setImageRes(R.drawable.advkht02);
		// adv2.setContent("http://mp.weixin.qq.com/s?__biz=MjM5NDg0NzIzNA==&mid=401756730&idx=5&sn=2169aa0f2751322e4d4874b7f3144929&scene=1&srcid=0115QUo3iYtIvU2q9TpYiX8b#rd");
		mAdvList.add(adv2);
		// 第三张
//		Advertisement adv3 = new Advertisement();
//		adv3.setImageRes(R.drawable.visarollimg3);
		// adv3.setContent("http://mp.weixin.qq.com/s?__biz=MjM5NDg0NzIzNA==&mid=401756730&idx=3&sn=32df249221afd8aa3d2d0de8be8f9792&scene=1&srcid=01158TgGCW2ZeSHQNKtiXMzw#rd");
//		mAdvList.add(adv3);
		// 易商app
		// Advertisement adv4 = new Advertisement();
		// adv4.setContent("http://mp.weixin.qq.com/s?__biz=MjM5NDg0NzIzNA==&mid=401756730&idx=4&sn=3060be3f4ed8c4fcb4b6803cb2e2d08b&scene=1&srcid=0115bPM13VwmciqlqsePCPbT#rd");
		// adv4.setImageRes(R.drawable.cyh_adv4);
		// mAdvList.add(adv4);
		if (mAdvList != null && mAdvList.size() > 1) {// 多张图片
			// pv_playview.clear();
			for (Advertisement advertisement : mAdvList) {
				View view = getLayoutInflater().inflate(R.layout.item_adpic,
						null);
				ImageView iv = (ImageView) view.findViewById(R.id.iv_photo);
				iv.setImageResource(advertisement.getImageRes());
				pv_playview.addView(iv);
			}
			pv_playview.startPlay();
		}
	}

	/* (non-Javadoc)
	 * @see com.bocop.jxplatform.util.LoginUtilAnother.ILoginListener#onLogin(int)
	 */
	@Override
	public void onLogin(int position) {
		// TODO Auto-generated method stub
		Bundle bundleHx = new Bundle();
		bundleHx.putString("url", Constants.UrlForMainQyKhtcard);
		bundleHx.putString("type", "kht");
		bundleHx.putString("name", "开户通");
		Intent intentHx = new Intent(baseActivity,
				WebForZytActivity.class);
		intentHx.putExtras(bundleHx);
		startActivity(intentHx);
	}

	/* (non-Javadoc)
	 * @see com.bocop.jxplatform.util.LoginUtilAnother.ILoginListener#onLogin()
	 */
	@Override
	public void onLogin() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.bocop.jxplatform.util.LoginUtilAnother.ILoginListener#onCancle()
	 */
	@Override
	public void onCancle() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.bocop.jxplatform.util.LoginUtilAnother.ILoginListener#onError()
	 */
	@Override
	public void onError() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.bocop.jxplatform.util.LoginUtilAnother.ILoginListener#onException()
	 */
	@Override
	public void onException() {
		// TODO Auto-generated method stub
		
	}
}
