package com.boc.jx.base;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * <p>
 * 些类为对ANDROID BaseAdapter的简单封装。主要提供于listview的item布局较为简单时使用，省去在ITEM布局简单而创建一个新的adapter。<br/>
 * 建议与ViewHolder一起使用
 * </p>
 * msgAdapter = new BaseAdapter<MsgBean>(this, msgList, R.layout.layout_msg_center_item) {<br/>
 * public void viewHandler(int position, MsgBean MsgBean, View convertView) {<br/>
 * ImageView ivMsgIcon = ViewHolder.get(convertView, R.id.ivMsgIcon);<br/>
 * TextView tvMsgTitle = ViewHolder.get(convertView, R.id.tvMsgTitle);<br/>
 * TextView tvMsgTime = ViewHolder.get(convertView, R.id.tvMsgTime);<br/>
 * if (MsgBean.isRead()) {<br/>
 * tvMsgTitle.setTextColor(getResources().getColor(R.color.msg_time_gray));<br/>
 * } else {<br/>
 * tvMsgTitle.setTextColor(Color.BLACK);<br/>
 * }<br/>
 * getImageLoader().displayImage(MsgBean.getImg(), ivMsgIcon, imageOptions, new MyImageLoadingListener());<br/>
 * FormsUtil.setTextViewTxt(tvMsgTitle, MsgBean.getTitle(), null);<br/>
 * FormsUtil.setTextViewTxt(tvMsgTime, MsgBean.getDatetime(), null);<br/>
 * }<br/>
 * };<br/>
 * xlvMsgList.setAdapter(msgAdapter);<br/>
 *
 * @version 1.0
 * @see com.boc.jx.tools.OldViewHolder
 */
public abstract class BaseAdapter<T> extends android.widget.BaseAdapter {
    protected BaseActivity activity;
    protected List<T> tList;
    protected int layoutResId;

    /**
     * @param activity    应用上下文
     * @param tList       列表参数集合
     * @param layoutResId 列表ITEM布局文件
     */
    public BaseAdapter(BaseActivity activity, List<T> tList, int layoutResId) {
        this.activity = activity;
        this.tList = tList;
        this.layoutResId = layoutResId;
    }

    @Override
    public int getCount() {
        return tList.size();
    }

    @Override
    public Object getItem(int position) {
        return tList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(activity)
                    .inflate(layoutResId, parent, false);
        }
        T t = tList.get(position);
        viewHandler(position, t, convertView);
        return convertView;
    }

    public abstract void viewHandler(int position, T t, View convertView);
}
