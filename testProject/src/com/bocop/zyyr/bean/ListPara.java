package com.bocop.zyyr.bean;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

public class ListPara {
	@XStreamImplicit(itemFieldName = "CAREER")
	private List<KeyAndValue> career;
	@XStreamImplicit(itemFieldName = "HOUSE_TP")
	private List<KeyAndValue> houseTp;
	@XStreamImplicit(itemFieldName = "COMP_TP")
	private List<KeyAndValue> compTp;
	@XStreamImplicit(itemFieldName = "CREDIT_STATUS")
	private List<KeyAndValue> creditStatus;

	public List<KeyAndValue> getCreditStatus() {
		return creditStatus;
	}

	public void setCreditStatus(List<KeyAndValue> creditStatus) {
		this.creditStatus = creditStatus;
	}

	public List<KeyAndValue> getCareer() {
		return career;
	}

	public void setCareer(List<KeyAndValue> career) {
		this.career = career;
	}

	public List<KeyAndValue> getHouseTp() {
		return houseTp;
	}

	public void setHouseTp(List<KeyAndValue> houseTp) {
		this.houseTp = houseTp;
	}

	public List<KeyAndValue> getCompTp() {
		return compTp;
	}

	public void setCompTp(List<KeyAndValue> compTp) {
		this.compTp = compTp;
	}

	public ListPara(List<KeyAndValue> career, List<KeyAndValue> houseTp,
			List<KeyAndValue> compTp, List<KeyAndValue> creditStatus) {
		super();
		this.career = career;
		this.houseTp = houseTp;
		this.compTp = compTp;
		this.creditStatus = creditStatus;
	}

	public ListPara() {
		super();
	}

}
