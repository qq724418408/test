package com.bocop.xfjr.view.brokelineview;

import java.util.Calendar;

/**
 * 功能说明
 * 说明：详细说明
 *
 * @author formssi
 */
public class DefaultShowRule implements ShowRule {
	private int mMaxY;
	
    public DefaultShowRule(int measureY) {
    	mMaxY = measureY;
	}

	/**
     *
     * @return横坐标上文字，返回为空时候，会使用默认值1-12
     */
    public  int[] landsTitle(){
        return null;
    };

    /**
     *
     * @return纵坐标上文字 返回为空时候会使用默认值1-15
     */
    public  int[] verticalTitle(){
    	if(mMaxY==0){
    		return new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
    	}
    	int[] i = new int[mMaxY];
    	for(int j = 1;j<=mMaxY;j++){
    		i[j-1] = j;
    	}
        return i;
    };

    /**
     *
     * @return 是否显示虚线网格
     */
    public   boolean showImaginaryLine(){
        return  false;
    };

    /**
     *  显示横坐标上的文字 规则
     * @param position  从0开始
     * @return 是否显示
     */
    public   boolean showLandsTitle(int position){
        return position%2!=0;
    };

    /**
     * 显示纵坐标上的文字 规则
     * @param position 从0开始
     * @return 是否显示
     */
    public   boolean showVerticalTitle(int position){
    	if(mMaxY==0){
    		return (position+1)%5==0;
    	}
        return (position+1)%(mMaxY/3)==0;
    };

    /**
     * 横坐标上文字重点标注规则
     * @param title
     * @return
     */
    public   boolean markLandsTitle(String title){
        Calendar c = Calendar.getInstance();
        int mMonth = c.get(Calendar.MONTH)+1;//获取当前月份;
        return Integer.valueOf(title)==mMonth;
    };

    /**
     * 横坐标单位
     * @return
     */
    public   String landsUnit(){
        return "(月)";
    };

    /**
     *是否需要绘制是的延迟效果，如果需要给延迟时间，否则返回0
     * @return
     */
    public int intervalTime(){
        return 0;
    };
}