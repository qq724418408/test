package com.bocop.zyyr.bean;

import java.io.Serializable;

import android.content.pm.PackageInfo;
import android.os.Parcel;
import android.os.Parcelable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class Period{

	@XStreamAlias("VAL")
	private String period;

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}
}
