package com.bocop.xfjr.adapter;

import java.util.List;

import com.bocop.jxplatform.R;
import com.bocop.xfjr.view.stepview.StepAdapter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * description： 业务详情页面，进度指示器适配器
 * <p/>
 * Created by TIAN FENG on 2017年8月24日
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */
public class BusinessDetailStepAdapter extends StepAdapter{
	
	// 进度item介绍文本
	private List<String> mDescriptions;
	// 资源
	private Resources mResources;
	
	
	public BusinessDetailStepAdapter(List<String> descriptions,Resources resources) {
		super();
		this.mDescriptions = descriptions;
		this.mResources = resources;
	}

	@Override
	public int getCount() {
		return mDescriptions==null?0:mDescriptions.size();
	}

	@Override
	public Bitmap getDefaultBitmap(int position) {
		// TODO 图片还没有确定
		return BitmapFactory.decodeResource(mResources, R.drawable.zyyr_user);
	}

	@Override
	public Bitmap getHighLightBitmap(int position) {
		// TODO 图片还没有确定
		return BitmapFactory.decodeResource(mResources, R.drawable.icon_main_jd);
	}

	@Override
	public String getDescription(int position) {
		return mDescriptions==null?"":mDescriptions.get(position);
	}

}
