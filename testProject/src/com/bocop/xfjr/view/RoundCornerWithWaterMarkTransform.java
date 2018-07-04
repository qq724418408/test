package com.bocop.xfjr.view;


import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;

public class RoundCornerWithWaterMarkTransform extends BitmapTransformation {

    private String waterMarkText;
    private int radius;
    
    public RoundCornerWithWaterMarkTransform(Context context) {
        this(context, 10);
    }

    public RoundCornerWithWaterMarkTransform(Context context, int dp, String waterMarkText) {
        super(context);
        if (dp > 0)
            this.radius = (int) (Resources.getSystem().getDisplayMetrics().density * dp + .5f);
        this.waterMarkText = waterMarkText;
    }

    public RoundCornerWithWaterMarkTransform(Context context, int dp) {
        this(context, dp, null);
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return roundCrop(pool, toTransform, waterMarkText);
    }

    private Bitmap roundCrop(BitmapPool pool, Bitmap source, String waterMarkText) {
        if (source == null) return null;

        int delta = (int) (Resources.getSystem().getDisplayMetrics().density * 2 + .5f);
        Paint textPaint = new Paint();
        boolean drawText = !TextUtils.isEmpty(waterMarkText);
        Rect bounds = new Rect();
        if (drawText) {
            textPaint.setTextSize(14);
            textPaint.setAntiAlias(true);
            textPaint.getTextBounds(waterMarkText, 0, waterMarkText.length(), bounds);
        }
        Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
        canvas.drawRoundRect(rectF, radius, radius, paint);
        if (drawText) {
            paint = new Paint();
            paint.setColor(Color.parseColor("#50f2f2f2"));
            bounds.top = canvas.getHeight() - bounds.height() - radius;
            bounds.bottom = canvas.getHeight() - radius + delta;
            int width = bounds.width();
            bounds.left = radius - delta;
            bounds.right = bounds.left + width + delta * 2;
            canvas.drawRect(bounds, paint);
            canvas.drawText(waterMarkText, radius, canvas.getHeight() - radius, textPaint);
        }
        return result;
    }

    @Override
    public String getId() {
        return getClass().getName() + Math.round(radius);
    }
}
