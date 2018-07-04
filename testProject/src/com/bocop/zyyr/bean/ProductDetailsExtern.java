package com.bocop.zyyr.bean;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

public class ProductDetailsExtern {
	@XStreamAlias("PROINFO")
	private ProductDetails details;
	// 期限
	@XStreamImplicit(itemFieldName = "PERIOD")
	private List<Period> periodList;

	public List<Period> getPeriodList() {
		return periodList;
	}

	public void setPeriodList(List<Period> periodList) {
		this.periodList = periodList;
	}

	public ProductDetails getDetails() {
		return details;
	}

	public void setDetails(ProductDetails details) {
		this.details = details;
	}
}
