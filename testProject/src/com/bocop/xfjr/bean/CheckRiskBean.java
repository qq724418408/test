package com.bocop.xfjr.bean;

/**
 * description： 人行征信验证bean
 * <p/>
 * Created by TIAN FENG on 2017年9月8日
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */
public class CheckRiskBean {
	 /**
     * resultCode : 1
     * resultTips : 近24个月出现过逾期M3
     */

    private int resultCode;
    private String resultTips;

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultTips() {
        return resultTips;
    }

    public void setResultTips(String resultTips) {
        this.resultTips = resultTips;
    }
}
