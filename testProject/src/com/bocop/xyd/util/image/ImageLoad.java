package com.bocop.xyd.util.image;

import com.bocop.xfjr.util.image.modle.IImgLoader;
import com.bocop.xfjr.util.image.modle.ImageOption;

import android.content.Context;
import android.widget.ImageView;

/**
 * description：图片加载器
 * <p/>
 * Created by TIAN FENG on 2017/8/30
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public class ImageLoad {
    private static IImgLoader mLoader;

    private ImageLoad() {
    }

    /**
     * 全局加载器
     */
    public static void init(IImgLoader loader){
        mLoader = loader;
    }

    /**
     * 图片加载
     */
    public static void loadImage(Context context,String url, ImageView view){
        mLoader.load(context,url,view, null);
    }

    /**
     * 图片加载
     */
    public static void loadImage(Context context,String url, ImageView view, ImageOption option){
        mLoader.load(context,url,view, option);
    }
}
