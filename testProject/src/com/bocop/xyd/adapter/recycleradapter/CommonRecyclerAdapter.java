package com.bocop.xyd.adapter.recycleradapter;

import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Description : RecycleView适配器
 * <p/>
 * Created : TIAN FENG
 * Date : 2017/6/29
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public abstract class CommonRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolder> {

    protected Context mContext;
    protected LayoutInflater mInflater;
    //数据
    protected List<T> mData;
    // 布局
    private int mLayoutId;
    // 多布局支持
    private MultiTypeSupport mMultiTypeSupport;
    
    /**
     * 单布局使用
     */
    public CommonRecyclerAdapter(Context context, List<T> data, int layoutId) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mData = data;
        this.mLayoutId = layoutId;
    }

    /**
     * 多布局支持
     */
    public CommonRecyclerAdapter(Context context, List<T> data, MultiTypeSupport<T> multiTypeSupport) {
        this(context, data, -1);
        this.mMultiTypeSupport = multiTypeSupport;
    }

    /**
     * 根据当前位置获取不同的viewType
     */
    @Override
    public int getItemViewType(int position) {
        // 多布局支持
        if (mMultiTypeSupport != null) {
            return mMultiTypeSupport.getLayoutId(mData.get(position), position);
        }
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 多布局支持
        if (mMultiTypeSupport != null) {
            mLayoutId = viewType;
        }
        // 先inflate数据
        View itemView = mInflater.inflate(mLayoutId, parent, false);
        // 返回ViewHolder
        RecyclerViewHolder holder = new RecyclerViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {
        // 设置点击和长按事件
        if (mItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(holder.getPosition());
                }
            });
        }
        if (mLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return mLongClickListener.onLongClick(holder.getPosition());
                }
            });
        }

        // 绑定回传
        convert(holder, mData.get(position),position);
    }

    /**
     * 利用抽象方法回传出去，每个不一样的Adapter去设置
     *
     * @param itemData 当前的数据
     * @param position 当前的位置
     */
    public abstract void convert(RecyclerViewHolder holder, T itemData, int position);

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    /***************
     * 给条目设置点击和长按事件
     *********************/
    public OnItemClickListener mItemClickListener;
    public OnLongClickListener mLongClickListener;

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    public void setOnLongClickListener(OnLongClickListener longClickListener) {
        this.mLongClickListener = longClickListener;
    }
}
