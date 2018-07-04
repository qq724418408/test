package com.bocop.xfjr.adapter;

import java.util.List;

import com.bocop.jxplatform.R;
import com.bocop.xfjr.view.stepview.StepAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * description： 欺诈侦测整个模块的进度指示适配器
 * <p/>
 * Created by TIAN FENG on 2017年8月28日
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */
public class MainActivityStepAdapter extends StepAdapter{

	private List<String> mList;
	private Context mContext;
	public final int INNER_POSITION = 1;
	public final int INNER_STEP_COUNT = 5;
	
	
	public MainActivityStepAdapter(List<String> list,Context context) {
		super();
		this.mList = list;
		this.mContext = context;
	}

	@Override
	public int getCount() {
		return mList==null?0:mList.size();
	}

	@Override
	public Bitmap getDefaultBitmap(int position) {
		// TODO 图片还没有确定
		return BitmapFactory.decodeResource(mContext.getResources(), R.drawable.xfjr_step_undone);
	}

	@Override
	public Bitmap getHighLightBitmap(int position) {
		// TODO 图片还没有确定
		return BitmapFactory.decodeResource(mContext.getResources(), R.drawable.xfjr_step_completed);
	}

	@Override
	public String getDescription(int position) {
		return mList==null?"":mList.get(position);
	}
	
	 /**
     * 2级进度个数  0->不绘制
     */
	@Override
    public int getInnerStepCount() {
        return 0;
    }

    /**
     * 2级分类在一级分类的位置   -1->没有2级分类
     */
	@Override
    public int getInnerStepPosition() {
        return -1;
    }

	
}
