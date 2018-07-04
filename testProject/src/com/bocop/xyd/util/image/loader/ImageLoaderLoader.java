package com.bocop.xyd.util.image.loader;

import java.io.File;

import com.bocop.xfjr.util.image.modle.IImgLoader;
import com.bocop.xfjr.util.image.modle.ImageOption;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.utils.StorageUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

/**
 * description：
 * <p/>
 * Created by TIAN FENG on 2017/8/30
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public class ImageLoaderLoader implements IImgLoader {
    @Override
    public void load(Context context, String url, ImageView view, final ImageOption option) {
        ImageLoader.getInstance().displayImage(url, view, getOption(option), new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
                if (option != null&& option.getListener()!=null) {
                    option.getListener().onError();
                }
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                if (option != null&& option.getListener()!=null) {
                    option.getListener().onSuccess();
                }
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        }, new ImageLoadingProgressListener() {
            @Override
            public void onProgressUpdate(String s, View view, int current, int total) {
                if (option != null&& option.getListener()!=null) {
                    option.getListener().onProgress(current, total);
                }
            }
        }); // imageUrl代表图片的URL地址，imageView代表承载图片的IMAGEVIEW控件
    }

    public static void initImageLoader(Context context, String cachePath) {
        File cacheDir = StorageUtils.getOwnCacheDirectory(context,
                cachePath);// 获取到缓存的目录地址
        Log.e("cacheDir", cacheDir.getPath());
        // 创建配置ImageLoader(所有的选项都是可选的,只使用那些你真的想定制)，这个可以设定在APPLACATION里面，设置为全局的配置参数
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context)
                // max width, max height，即保存的每个缓存文件的最大长宽
                .memoryCacheExtraOptions(480, 800)
                .threadPoolSize(3)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                //硬盘缓存50MB
                .diskCacheSize(50 * 1024 * 1024)
                //将保存的时候的URI名称用MD5
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                // 加密
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())//将保存的时候的URI名称用HASHCODE加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .diskCacheFileCount(100) //缓存的File数量
                .diskCache(new UnlimitedDiskCache(cacheDir))// 自定义缓存路径
                // .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                // .imageDownloader(new BaseImageDownloader(context, 5 * 1000,
                // 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
                .build();
        ImageLoader.getInstance().init(config);// 全局初始化此配置
    }

    private DisplayImageOptions getOption(ImageOption option) {
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
        if (option != null && option.getLoadingImg() != 0) {
            builder.showImageOnLoading(option.getLoadingImg());//设置图片在下载期间显示的图片
        }
        if (option != null && option.getPlaceholder() != 0) {
            builder.showImageForEmptyUri(option.getPlaceholder());//设置图片Uri为空或是错误的时候显示的图片
        }
        if (option != null && option.getErrImg() != 0) {
            builder.showImageOnFail(option.getErrImg()); //设置图片加载/解码过程中错误时候显示的图片
        }

        builder.cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
//                .decodingOptions(android.graphics.BitmapFactory.Options decodingOptions)//设置图片的解码配置
                //.delayBeforeLoading(int delayInMillis)//int delayInMillis为你设置的下载前的延迟时间
                //设置图片加入缓存前，对bitmap进行设置
                //.preProcessor(BitmapProcessor preProcessor)
                .resetViewBeforeLoading(true);//设置图片在下载前是否重置，复位
        if (option != null && option.getRadius() != 0) {
            builder.displayer(new RoundedBitmapDisplayer(option.getRadius()));//是否设置为圆角，弧度为多少
        }
//                .displayer(new FadeInBitmapDisplayer(100));//是否图片加载好后渐入的动画时间
        return builder.build();

    }
}
