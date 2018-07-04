package com.bocop.xfjr.view.brokelineview;

import java.util.List;

/**
 * 功能说明
 * 说明：详细说明
 *
 * @author formssi
 */

public interface BrokenAdapter {
//    int getList();
    //根据position返回x的值
    int getLands(int position);
    //根据position返回y轴最大值
    float getTotal(int position);
    //根据position返回实际值
    float getActual(int position);

    int getSize();

    void notifyChanged();
}
