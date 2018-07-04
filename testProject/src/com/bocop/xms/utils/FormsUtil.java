package com.bocop.xms.utils;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class FormsUtil {

    public static int SCREEN_WIDTH = 0;
    public static int SCREEN_HEIGHT = 0;
    public static float density = 1.0f;
    public static float scaledDensity = 1.0f;

    private static final NumberFormat format;
    private static final String redHtml = "<font color='red'>{0}</font>";

    static {
        format = DecimalFormat.getCurrencyInstance(Locale.CHINA);
        format.setMaximumFractionDigits(2);
    }

    /**
     * 获取屏幕属性
     *
     * @param context
     */
    public static void getDisplayMetrics(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
//		((BaseActivity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        density = dm.density;
        scaledDensity = dm.scaledDensity;
        SCREEN_WIDTH = dm.widthPixels;
        SCREEN_HEIGHT = dm.heightPixels;
    }

    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param pxValue
     * @return
     */
    public static int px2dip(float pxValue) {
        return (int) (pxValue / density + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue
     * @return
     */
    public static int dip2px(float dipValue) {
        return (int) (dipValue * density + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @return
     */
    public static int px2sp(float pxValue) {
        return (int) (pxValue / scaledDensity + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @return
     */
    public static int sp2px(float spValue) {
        return (int) (spValue * scaledDensity + 0.5f);
    }

    /**
     * 设置textView文本值
     *
     * @param textView
     * @param text
     * @param nullText text为空的显示值
     */
    public static void setTextViewTxt(TextView textView, String text, String nullText) {
        nullText = nullText == null ? "" : nullText;
        text = text == null ? nullText : text;
        textView.setText(text);
    }

    /**
     * 设置textView文本值
     *
     * @param textView
     * @param text
     */
    public static void setTextViewTxt(TextView textView, String text) {
        setTextViewTxt(textView, text, null);
    }

    /**
     * 设置textView文本值
     *
     * @param textView
     * @param text
     */
    public static void setTextViewTxts(TextView textView, String text, Object... textValue) {
        if (TextUtils.isEmpty(text)) {
            textView.setText("");
        } else {
            text = MessageFormat.format(text, textValue);
            textView.setText(text);
        }
    }

    /**
     * 设置textView文本值
     *
     * @param textView
     * @param text
     */
    public static void setRedText(TextView textView, String text) {
        text = TextUtils.isEmpty(text) ? "" : text;
        textView.setText(Html.fromHtml(text));
    }

    /**
     * 设置textView文本值
     *
     * @param textView
     * @param text
     */
    public static void setRedText(TextView textView, String text, Object... textValue) {
        if (TextUtils.isEmpty(text)) {
            textView.setText("");
        } else {
            text = MessageFormat.format(text, MessageFormat.format(redHtml, textValue));
            textView.setText(Html.fromHtml(text));
        }
    }

    /**
     * @param textView
     * @param amt
     */
    public static void setCurrencyText(TextView textView, String text, double amt) {
        setRedText(textView, text, getCurrencyStr(amt));
    }

    /**
     * @param context
     * @param textView
     * @param textResId
     * @param nullText
     */
    public static void setTextViewTxt(Context context, TextView textView, int textResId, String nullText) {
        nullText = nullText == null ? "" : nullText;
        String text = context.getString(textResId);
        setTextViewTxt(textView, text, nullText);
    }

    /**
     * @param context
     * @param view
     * @param textResId
     */
    public static void setErrorHtmlTxt(Context context, EditText view, Integer textResId) {
        if (textResId == null || textResId <= 0) {
            return;
        }
        setErrorHtmlTxt(view, context.getString(textResId));
    }

    /**
     * @param view
     * @param text
     */
    public static void setErrorHtmlTxt(EditText view, String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        view.setError(Html.fromHtml(MessageFormat.format("<font color='red'>{0}</font>", text)));
    }

    /**
     * @param context
     * @param view
     * @param textResId
     */
    public static void setError(Context context, EditText view, Integer textResId) {
        if (textResId == null || textResId <= 0) {
            return;
        }
        setError(view, context.getString(textResId));
    }

    /**
     * @param view
     * @param text
     */
    public static void setError(EditText view, String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        view.setError(Html.fromHtml(MessageFormat.format(redHtml, text)));
    }

    /**
     * 金额格式化
     *
     * @param currency
     * @return
     */
    public static String getCurrencyStr(double currency) {
        return format.format(currency);
    }



}
