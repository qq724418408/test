package com.bocop.yfx.bean;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

public class ImgUrlList {

	@XStreamImplicit(itemFieldName = "IMG_LIST")
	private List<ImgUrl> list;

	public List<ImgUrl> getList() {
		return list;
	}

	public void setList(List<ImgUrl> list) {
		this.list = list;
	}

}
