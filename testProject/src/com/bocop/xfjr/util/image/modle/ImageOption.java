package com.bocop.xfjr.util.image.modle;

import android.widget.ImageView.ScaleType;

/**
 * description：图片加载的参数
 * <p/>
 * Created by TIAN FENG on 2017/8/30
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */
public class ImageOption {
    private Builder builder;
	private ScaleType scaleTYpe = ScaleType.CENTER;

    public ScaleType getScaleTYpe() {
		return scaleTYpe;
	}

	public void setScaleTYpe(ScaleType scaleTYpe) {
		this.scaleTYpe = scaleTYpe;
	}

	private ImageOption(Builder builder) {
        this.builder = builder;
    }

    public int getErrImg() {
        return builder.errImg;
    }

    public int getPlaceholder() {
        return builder.placeholder;
    }

    public int getRadius() {
        return builder.radius;
    }

    public int getLoadingImg() {
        return builder.loadingImg;
    }

    public LoaderListener getListener() {
        return builder.listener;
    }

    public static class Builder {
        private int errImg;
        private int placeholder;
        private int radius;
        private int loadingImg;
        private LoaderListener listener;


        public Builder setListener(LoaderListener listener) {
            this.listener = listener;
            return this;
        }

        public Builder setLoadingImg(int loadingImg) {
            this.loadingImg = loadingImg;
            return this;
        }

        public Builder setRadius(int radius) {
            this.radius = radius;
            return this;
        }

        public Builder setErrImg(int errImg) {
            this.errImg = errImg;
            return this;
        }

        public Builder setPlaceholder(int placeholder) {
            this.placeholder = placeholder;
            return this;
        }

        public ImageOption build() {
            return new ImageOption(this);
        }

    }
}
