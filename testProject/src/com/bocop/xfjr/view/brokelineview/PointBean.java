package com.bocop.xfjr.view.brokelineview;

import android.graphics.PointF;

/**
 * 功能说明
 * 说明：详细说明
 *
 * @author formssi
 */

class PointBean {
    public static int THEFIRSTLINE=1;
    public static int THESECONDLINE=2;

    private PointF uppointF;
    private PointF downPoint;
    private int line;
    private int text;
    private  int text2;

    public PointBean() {
    }

    public PointBean(PointF pointF1,PointF pointF2, int line, float text, float text1) {
        this.uppointF = pointF1;
        this.downPoint=pointF2;
        this.line = line;
        this.text = (int) text;
        this.text2=(int) text1;
    }

    public int getText2() {
        return text2;
    }

    public void setText2(float text2) {
        this.text2 = (int) text2;
    }

    public PointF getUppointF() {
        return uppointF;
    }

    public void setUppointF(PointF uppointF) {
        this.uppointF = uppointF;
    }

    public PointF getDownPoint() {
        return downPoint;
    }

    public void setDownPoint(PointF downPoint) {
        this.downPoint = downPoint;
    }


    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getText() {
        return text;
    }

    public void setText(float text) {
        this.text = (int) text;
    }
}
