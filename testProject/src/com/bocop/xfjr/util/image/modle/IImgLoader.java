package com.bocop.xfjr.util.image.modle;

import android.content.Context;
import android.widget.ImageView;

/**
 * description：加载器模板
 * <p/>
 * Created by TIAN FENG on 2017/8/30
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */
public interface IImgLoader {
    void load(Context context,String url, ImageView view, ImageOption option);
}
