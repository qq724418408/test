package com.bocop.jxplatform.view;

import com.boc.jx.baseUtil.view.ViewUtils;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.bocop.jxplatform.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by huwentao on 2014/7/1.
 */
public class TabButton extends LinearLayout {
	//下层
    @ViewInject(R.id.ivTabImage)
    private ImageView ivTabImage;
    @ViewInject(R.id.tvTabName)
    private TextView tvTabName;
    //上层
    @ViewInject(R.id.ivTabImage_above)
    private ImageView ivTabImage_above;
    @ViewInject(R.id.tvTabName_above)
    private TextView tvTabName_above;
    //下层
    @ViewInject(R.id.ll_below)
    private LinearLayout ll_below;
    //上层
    @ViewInject(R.id.ll_above)
    private LinearLayout ll_above;
    
    

    private int imageResId;
    private String tabName;
    private boolean isSelect;
    private int backgroundResId;
    private int selectedBackgroundResId;
    private int selImageResId;
    private Context context;

    public TabButton(Context context) {
        super(context);
    }

    public TabButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (isInEditMode()) return;
        View view = LayoutInflater.from(context).inflate(R.layout.layout_tabbtn, this);
        ViewUtils.inject(this, view);
    }

    /**
     * @param imageResId    tab图片
     * @param selImageResId 选中状态的图片
     * @param tabName       tab 名字
     * @param isSelect      是否选中状态
     * @param context 		上下文
     * @return
     */
    public TabButton init(int imageResId,int selImageResId, String tabName, boolean isSelect, 
    		Context context) {
        this.imageResId = imageResId;
        this.selImageResId = selImageResId;
        this.tabName = tabName;
        this.isSelect = isSelect;
        this.context = context;
        ivTabImage.setImageResource(imageResId);
        tvTabName.setText(tabName);
        
        ivTabImage_above.setImageResource(selImageResId);
        tvTabName_above.setText(tabName);
        
//        tvTabName.setTextColor(context.getResources().getColor(R.color.black));
//        tvTabName_above.setTextColor(context.getResources().getColor(R.color.title_light_green));
        
        if (isSelect) {
        	ll_above.setAlpha(1);
        	ll_below.setAlpha(0);
        } else {
        	ll_above.setAlpha(0);
        	ll_below.setAlpha(1);
        }
        postInvalidate();
        return this;
    }

    public int getImageResId(){  
    	 return imageResId;
    }

    public TabButton setImageResId(int imageResId) {
        this.imageResId = imageResId;
        return this;
    }

    public String getTabName() {
        return tabName;
    }

    public TabButton setTabName(String tabName) {
        this.tabName = tabName;
        return this;
    }
    
    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean isSelect) {
        this.isSelect = isSelect;
        if (isSelect) {
        	ll_above.setAlpha(1);
        	ll_below.setAlpha(0);
        } else {
        	ll_above.setAlpha(0);
        	ll_below.setAlpha(1);
        }
        postInvalidate();
    }

    public int getBackgroundResId() {
        return backgroundResId;
    }

    public void setBackgroundResId(int backgroundResId) {
        this.backgroundResId = backgroundResId;
    }

    public int getSelectedBackgroundResId() {
        return selectedBackgroundResId;
    }

    public void setSelectedBackgroundResId(int selectedBackgroundResId) {
        this.selectedBackgroundResId = selectedBackgroundResId;
    }

    public int getSelImageResId() {
        return selImageResId;
    }

    public void setSelImageResId(int selImageResId) {
        this.selImageResId = selImageResId;
    }

	public ImageView getIvTabImage() {
		return ivTabImage;
	}

	public void setIvTabImage(ImageView ivTabImage) {
		this.ivTabImage = ivTabImage;
	}

	public TextView getTvTabName() {
		return tvTabName;
	}

	public void setTvTabName(TextView tvTabName) {
		this.tvTabName = tvTabName;
	}

	public LinearLayout getLl_below() {
		return ll_below;
	}

	public void setLl_below(LinearLayout ll_below) {
		this.ll_below = ll_below;
	}

	public LinearLayout getLl_above() {
		return ll_above;
	}

	public void setLl_above(LinearLayout ll_above) {
		this.ll_above = ll_above;
	}
    
    
}
