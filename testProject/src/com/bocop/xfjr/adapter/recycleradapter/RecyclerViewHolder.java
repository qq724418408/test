package com.bocop.xfjr.adapter.recycleradapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Description : 适配器Holder
 * <p/>
 * Created : TIAN FENG
 * Date : 2017/6/29
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    // 用来存放子View减少findViewById的次数 SparseArray 存放键值对为int->T类型 效率高于map
    private SparseArray<View> mViews;
    // 自定义图片加载器
    private static HolderImageLoader mImageLoader;
    
    /**
     * 设置图片加载器 可以全局使用application初始化
     */
    public static void setImageLoader(HolderImageLoader imageLoader){
        mImageLoader = imageLoader;
    }


    public RecyclerViewHolder(View itemView) {
        super(itemView);
        mViews = new SparseArray<>();
    }

    /**
     * 设置TextView文本
     */
    public RecyclerViewHolder setText(int viewId, CharSequence text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }
    public RecyclerViewHolder setOnClick(int viewId, OnClickListener listener) {
       getView(viewId).setOnClickListener(listener);
        return this;
    }

    /**
     * 通过id获取view
     */
    @SuppressWarnings("unchecked")
	public <T extends View> T getView(int viewId) {
        // 先从缓存中找
        View view = mViews.get(viewId);
        if (view == null) {
            // 直接从ItemView中找
            view = itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 设置View的Visibility
     */
    public RecyclerViewHolder setViewVisibility(int viewId, int visibility) {
        getView(viewId).setVisibility(visibility);
        return this;
    }

    /**
     * 设置ImageView的资源
     */
    public RecyclerViewHolder setImageResource(int viewId, int resourceId) {
        ImageView imageView = getView(viewId);
        imageView.setImageResource(resourceId);
        return this;
    }

    /**
     * 设置条目点击事件
     */
    public void setOnIntemClickListener(View.OnClickListener listener) {
        itemView.setOnClickListener(listener);
    }

    /**
     * 设置条目长按事件
     */
    public void setOnIntemLongClickListener(View.OnLongClickListener listener) {
        itemView.setOnLongClickListener(listener);
    }

    /**
     * 加载网络图片
     */
    public RecyclerViewHolder setImageByUrl(int viewId,String url) {
        ImageView imageView = getView(viewId);
        if (mImageLoader == null) {
            throw new NullPointerException("imageLoader is null,please setImageLoader(HolderImageLoader) in application!");
        }
        mImageLoader.displayImage(imageView.getContext(), imageView, url);
        return this;
    }

    /**
     * 设置图片通过路径，因为考虑加载图片的第三方可能不太一样
     * 也可以直接写死
     */
    public RecyclerViewHolder setImageByUrl(int viewId,String path, HolderImageLoader imageLoader) {
        ImageView imageView = getView(viewId);
        if (imageLoader == null) {
            throw new NullPointerException("imageLoader is null!");
        }
        imageLoader.displayImage(imageView.getContext(), imageView, path);
        return this;
    }


    /**
     * 考虑加载图片的第三方可能不太一样
     */
    public interface HolderImageLoader {

        void displayImage(Context context, ImageView imageView, String imagePath);
    }
}
