package com.bocop.yfx.bean;

/**
 * 
 * 计算结果
 * 
 * @author lh
 * 
 */
public class SumPerStage {

	private String stage;
	private String sumPS;
	private String hChargePS;
	private boolean isClicked;

	public boolean isClicked() {
		return isClicked;
	}

	public void setClicked(boolean isClicked) {
		this.isClicked = isClicked;
	}

	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

	public String getSumPS() {
		return sumPS;
	}

	public void setSumPS(String sumPS) {
		this.sumPS = sumPS;
	}

	public String gethChargePS() {
		return hChargePS;
	}

	public void sethChargePS(String hChargePS) {
		this.hChargePS = hChargePS;
	}

}
