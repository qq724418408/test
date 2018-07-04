package com.boc.jx.tools;

import android.graphics.Bitmap;

import com.bocop.jxplatform.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class ImageViewUtil {

    public static DisplayImageOptions getOption() {
        // 使用DisplayImageOptions.Builder()创建DisplayImageOptions
        return new DisplayImageOptions.Builder()
//              	.showImageOnLoading(R.drawable.zyyr_logo)// 设置图片下载期间显示的图片
//                .showImageForEmptyUri(R.drawable.zyyr_logo) // 设置图片Uri为空或是错误的时候显示的图片
//                .showImageOnFail(R.drawable.zyyr_logo) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(false) // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
                .bitmapConfig(Bitmap.Config.RGB_565) //设置图片的质量
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT) //设置图片的缩放类型，该方法可以有效减少内存的占用
                .build();
    }
//    
//    /**
//     * 广告轮播图
//     * @return
//     */
//    public static DisplayImageOptions getOptionAd() {
//        return new DisplayImageOptions.Builder()
//              	.showImageOnLoading(R.drawable.image_fail)
//                .showImageForEmptyUri(R.drawable.image_fail)
//                .showImageOnFail(R.drawable.image_fail)
//                .cacheInMemory(false)
//                .cacheOnDisk(true)
//                .bitmapConfig(Bitmap.Config.RGB_565)
//                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
//                .build();
//    }
//    
//    
//    /**
//     * 图片选择
//     * @return
//     */
//    public static DisplayImageOptions getOptionImg() {
//    	return new DisplayImageOptions.Builder()
//    	.showImageOnLoading(R.drawable.image_default_90)
//    	.showImageForEmptyUri(R.drawable.image_default_90)
//    	.showImageOnFail(R.drawable.image_default_90)
//    	.cacheInMemory(false)
//    	.cacheOnDisk(true)
//    	.bitmapConfig(Bitmap.Config.RGB_565)
//    	.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
//    	.build();
//    }

}
