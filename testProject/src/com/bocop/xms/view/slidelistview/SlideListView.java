package com.bocop.xms.view.slidelistview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ListView;

public class SlideListView extends ListView {
    private SlideView mFocusedItemView;
    private boolean itemClickable = false;

    public SlideListView(Context context) {
        super(context);
    }

    public SlideListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlideListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    int startX;
    int startY;
    int moveX;
    int moveY;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        int position = pointToPosition(x, y);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                startX = x;
                startY = y;
                //我们想知道当前点了哪一行
                Log.d(getClass().getSimpleName(), "position=" + position);
                if (position != INVALID_POSITION) {
                    /*得到当前点击行的数据从而取出 当前 行的 ITEM。
                    可能有人怀疑，为什么要这么干？为什么不用getChildAt(position)?
                    因为ListView会进行缓存，如果 你不这么干有些行的 view 你是得不到的。*/
                    SlideItem data = (SlideItem) getItemAtPosition(position);
                    mFocusedItemView = data.slideView;

                    Log.d(getClass().getSimpleName(), "FocusedItemView=" + mFocusedItemView);
                }
            }
            case MotionEvent.ACTION_MOVE:
                moveX = x - startX;
                moveY = y - startY;
                break;
            case MotionEvent.ACTION_UP:
                if (position != INVALID_POSITION && mFocusedItemView != null) {
                    float scrollX = mFocusedItemView.getScrollX();
                    float scrollY = mFocusedItemView.getScrollY();
                    if (scrollX == 0 && scrollY == 0) {
                        if (moveX > 0 || moveY > 0) {
                            itemClickable = false;
                        } else {
                            itemClickable = true;
                        }
                    } else {
                        itemClickable = false;
                    }
                    if (scrollX == 0 && itemClickable) {
                         /*滑动不满足条件，不做横向滑动*/
                        if (onmItemClickListener != null) {
                            onmItemClickListener.onItemClick(mFocusedItemView, position);
                        }
                    }
                }
                break;
            default:
                break;
        }
        if (mFocusedItemView != null) {
            mFocusedItemView.onRequestTouchEvent(ev);
        }
        return super.onTouchEvent(ev);
    }

    private OnmItemClickListener onmItemClickListener;

    public OnmItemClickListener getOnmItemClickListener() {
        return onmItemClickListener;
    }

    public void setOnmItemClickListener(OnmItemClickListener onmItemClickListener) {
        this.onmItemClickListener = onmItemClickListener;
    }

    public interface OnmItemClickListener {
        public void onItemClick(SlideView slideView, int position);
    }
}
