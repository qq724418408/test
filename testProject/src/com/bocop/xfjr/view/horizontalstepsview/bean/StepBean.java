package com.bocop.xfjr.view.horizontalstepsview.bean;

/**
 * 日期：16/9/3 00:36
 * <p/>
 * 描述：
 */
public class StepBean
{
    public static final int STEP_UNDO = -1;//未完成  undo step
    public static final int STEP_CURRENT = 0;//正在进行 current step
    public static final int STEP_COMPLETED = 1;//已完成 completed step

    private String name;
    private String time;
    private int state;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getState()
    {
        return state;
    }

    public void setState(int state)
    {
        this.state = state;
    }

    public StepBean()
    {
    }

    
    public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public StepBean(String name, int state,String time)
    {
        this.name = name;
        this.state = state;
        this.time=time;
    }
}
