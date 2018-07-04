package com.bocop.xfjr.view.stepview;

import android.graphics.Bitmap;

/**
 * description：进度适配器
 * <p/>
 * Created by TIAN FENG on 2017/8/23
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public abstract class StepAdapter {

    /**
     * 条目数
     */
    public abstract int getCount();

    /**
     * 2级进度个数 0 不绘制
     */
    public int getInnerStepCount() {
        return 0;
    }

    /**
     * 2级分类在一级分类的位置 -1 没有分类
     */
    public int getInnerStepPosition() {
        return -1;
    }

    /**
     * 默认图
     */
    public abstract Bitmap getDefaultBitmap(int position);

    /**
     * 选中图
     */
    public abstract Bitmap getHighLightBitmap(int position);

    /**
     * 文字介绍
     */
    public abstract String getDescription(int position);


}
