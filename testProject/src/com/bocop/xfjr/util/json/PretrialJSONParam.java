package com.bocop.xfjr.util.json;

import com.alibaba.fastjson.JSON;
import com.bocop.xfjr.bean.pretrial.PretrialParamBean;

/**
 * 风险预授信提交的json参数
 * 
 * @author wujunliu
 *
 */
public class PretrialJSONParam {

	public static String createJSONParam(PretrialParamBean bean){
		String p = JSON.toJSONString(bean);
		return p;
	}
	
}
