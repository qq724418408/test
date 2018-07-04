package com.bocop.xfjr.bean.add;

import java.util.List;

import com.boc.jx.httptools.network.base.RootRsp;

/**
 * 渠道实体
 * 
 * @author wujunliu
 *
 */
public class ChannelBean extends RootRsp {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	private String channelId; // 编号
	private String channelName; // 渠道名称
	private List<ProductBean> productList;

	@Override
	public String toString() {
		return channelName;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public List<ProductBean> getProductList() {
		return productList;
	}

	public void setProductList(List<ProductBean> productList) {
		this.productList = productList;
	}

}
