package com.bocop.xfjr.helper;

import android.widget.TextView;

/**
 * 异常信息辅助
 * 
 * @author TIAN FENG
 */
public class ThrowableHelper {
	
	/**
	 * 根据返回的异常信息显示不同文字
	 * 
	 * @param e
	 * @param tv
	 */
	public static void setMsgByUrlThrowable(Throwable e,TextView tv){
		if(e == null){
			return ;
		}
		String code = e.getMessage();
		if (null != code) {
			switch (code) {
			case "10001":
			case "10012":
			case "66666":// 动态流程失败
				tv.setText("原因 ：服务器异常");
				break;

			case "10010":
				tv.setText("原因 ：用户不存在");
				break;

			case "1011":
				tv.setText("原因 ：请求失败");
				break;

			case "1013":
				tv.setText("原因 ：暂无数据");
				break;

			case "10020":
				tv.setText("原因 ：请求频繁,请稍候再试！");
				break;
			}
			if(code.contains("无网络")){
				tv.setText("原因 ：当前无网络");
			}
		}
	}
}
