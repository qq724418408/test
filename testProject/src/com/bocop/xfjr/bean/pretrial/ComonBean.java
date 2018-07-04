package com.bocop.xfjr.bean.pretrial;

import java.io.Serializable;

public  class ComonBean implements Serializable{
    /**
     * companyName : 四方精创
     * monthPay : 5000.00
     * startPayTime : 1513165664
     * validatedStatus : 2
     */
	private int type=-1;
	private boolean isShow=true;
    private String companyName;
    private String monthPay;
    private String startPayTime;
    private String validatedStatus;

    public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public boolean isShow() {
		return isShow;
	}

	public void setShow(boolean isShow) {
		this.isShow = isShow;
	}

	public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getMonthPay() {
        return monthPay;
    }

    public void setMonthPay(String monthPay) {
        this.monthPay = monthPay;
    }

    public String getStartPayTime() {
        return startPayTime;
    }

    public void setStartPayTime(String startPayTime) {
        this.startPayTime = startPayTime;
    }

    public String getValidatedStatus() {
        return validatedStatus;
    }

    public void setValidatedStatus(String validatedStatus) {
        this.validatedStatus = validatedStatus;
    }
}