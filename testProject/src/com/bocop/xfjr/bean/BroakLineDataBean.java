package com.bocop.xfjr.bean;

/**
 * 折线图数据
 */
public class BroakLineDataBean {
	
	private int month;
	private float total;
	private float actual;

	public BroakLineDataBean(){}
	
	public BroakLineDataBean(int month, float total, float actual) {
		super();
		this.month = month;
		this.total = total;
		this.actual = actual;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public float getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
	}

	public float getActual() {
		return actual;
	}

	public void setActual(float actual) {
		this.actual = actual;
	}

}
