package com.boc.jx.tools;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

public class DisplayUtil {

	private static float fontScale;
	private static float textSize;
	private static DisplayMetrics metrics = new DisplayMetrics();
	
	/**
	 * 获取屏幕宽
	 */
	public static int getWidth(Context context){
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
		return metrics.widthPixels;
	}
	
	/**
	 * 获取屏幕高
	 */
	public static int getHeight(Context context){
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
		return metrics.heightPixels;
	}


	/**
	 * 适配列表文字大小
	 */
	public static int getTextSize(Context context, int size) {
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
		textSize = (float) metrics.widthPixels / (float) 1280;
		return px2sp(context, (int) (textSize * size));
	}

	/**
	 * 将px值转换为dip或dp值，保证尺寸大小不变
	 *
	 * @param pxValue
	 * @param scale
	 *            （DisplayMetrics类中属性density）
	 * @return
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 将dip或dp值转换为px值，保证尺寸大小不变
	 *
	 * @param dipValue
	 * @param scale
	 *            （DisplayMetrics类中属性density）
	 * @return
	 */
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * 将px值转换为sp值，保证文字大小不变
	 *
	 * @param pxValue
	 * @param fontScale
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int px2sp(Context context, float pxValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	 * 将sp值转换为px值，保证文字大小不变
	 *
	 * @param spValue
	 * @param fontScale
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}
}
