package com.bocop.yfx.bean;

import java.io.Serializable;

/**
 * 提款数据
 *
 * @author lh
 */

public class PickUpDetails implements Serializable {

    private String pickUpAmount;// 提款额度
    private double rate;
    private String useID;// 用途ID
    private String use;
    private String periodID;// 期限ID
    private String period;
    private String methodID;// 还款方式ID
    private String method;// 还款方式ID
    private String repayCard;// 还款卡号
    private String drawingCard;// 提款卡号
    private String repayCardID;// 还款卡ID
    private String drawingCardID;// 提款卡ID
    private String phone;//电话号码
    private String etoken;// 免验证额度

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDrawingCardID() {
        return drawingCardID;
    }

    public void setDrawingCardID(String drawingCardID) {
        this.drawingCardID = drawingCardID;
    }

    public String getPickUpAmount() {
        return pickUpAmount;
    }

    public void setPickUpAmount(String pickUpAmount) {
        this.pickUpAmount = pickUpAmount;
    }

    public String getUseID() {
        return useID;
    }

    public void setUseID(String useID) {
        this.useID = useID;
    }

    public String getPeriodID() {
        return periodID;
    }

    public void setPeriodID(String periodID) {
        this.periodID = periodID;
    }

    public String getMethodID() {
        return methodID;
    }

    public void setMethodID(String methodID) {
        this.methodID = methodID;
    }

    public String getRepayCard() {
        return repayCard;
    }

    public void setRepayCard(String repayCard) {
        this.repayCard = repayCard;
    }

    public String getDrawingCard() {
        return drawingCard;
    }

    public void setDrawingCard(String drawingCard) {
        this.drawingCard = drawingCard;
    }

    public String getRepayCardID() {
        return repayCardID;
    }

    public void setRepayCardID(String repayCardID) {
        this.repayCardID = repayCardID;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getUse() {
        return use;
    }

    public void setUse(String use) {
        this.use = use;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

	public String getEtoken() {
		return etoken;
	}

	public void setEtoken(String etoken) {
		this.etoken = etoken;
	}

}
