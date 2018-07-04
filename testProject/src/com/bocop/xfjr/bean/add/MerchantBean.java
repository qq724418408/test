package com.bocop.xfjr.bean.add;

import java.util.List;

import com.boc.jx.httptools.network.base.RootRsp;

/**
 * 商户实体
 * 
 * @author wujunliu
 *
 */
public class MerchantBean extends RootRsp {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	private String merchantId; // 编号
	private String merchantName; // 商户名称
	private List<ChannelBean> channelList;

    public List<ChannelBean> getChannelList() {
		return channelList;
	}

	public void setChannelList(List<ChannelBean> channelList) {
		this.channelList = channelList;
	}

	public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }
    
    @Override
    public String toString() {
    	// TODO Auto-generated method stub
    	return merchantName;
    }
}
