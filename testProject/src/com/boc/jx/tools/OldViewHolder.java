package com.boc.jx.tools;

import android.util.SparseArray;
import android.view.View;

/**
 * <p>配合ListView控件使用，省去在创建listAdapter时getView()时重复编写ViewHolder类。</p>
 * <p>可与BaseAdapter配合使用。</p>
 *
 * @see com.boc.jx.base.BaseAdapter
 */
@SuppressWarnings("unchecked")
public class OldViewHolder {

    /**
     * @param view 当前listView itemview的视图
     * @param id   视图中控件ID
     * @param <T>  返回根据提供的控件ID从itemview中找到的控件视图
     * @return ID指向的View
     */
    public static <T extends View> T get(View view, int id) {
        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray<View>();
            view.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }
}
