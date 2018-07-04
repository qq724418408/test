package com.bocop.zyyr.bean;

import com.bocop.yfx.bean.BaseResponse;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 职业、公司、房产参数列表
 * @author rd
 *
 */
@XStreamAlias("UTILITY_PAYMENT")
public class ListParaResponse extends BaseResponse{
	@XStreamAlias("DATA_AREA")
	private ListPara  listPara;

	public ListPara getListPara() {
		return listPara;
	}

	public void setListPara(ListPara listPara) {
		this.listPara = listPara;
	}
	
}
