package com.bocop.yfx.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * 轮播图
 * 
 * @author lh
 * 
 */
@XStreamAlias("UTILITY_PAYMENT")
public class ImgUrlListResponse extends BaseResponse {

	@XStreamAlias("DATA_AREA")
	private ImgUrlList imgUrlList;

	public ImgUrlList getImgUrlList() {
		return imgUrlList;
	}

	public void setImgUrlList(ImgUrlList imgUrlList) {
		this.imgUrlList = imgUrlList;
	}

}
