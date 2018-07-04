package com.bocop.gm.utils;

/**
 * 拦截配置
 * 
 * @author xiaxq
 *
 *         2016年7月5日
 */
public abstract class HybridCallBack {

	/**
	 * 拦截到的URL
	 * <p/>
	 * 调用HTML 时 该方法不会触发
	 * 
	 */
	public void getUrl(String url) {
	}

	/**
	 * 调用方法名
	 */
	public void getActionName(String actionName) {
	}

	/**
	 * 拦截到的初始参数
	 */
	public void getOldParams(String oldParams) {
	}

	/**
	 * 
	 * 拦截到参数之后,自动拼装的json字符串,若初始参数为json格式则返回初始参数
	 * <p/>
	 * 调用HTML时,返回拼装好的多参数,或者json
	 */
	public void getNewParams(String NewParams) {
	}

	/**
	 * 
	 * 错误信息
	 * </p>
	 * May be running the worker thread
	 * </p>
	 * 
	 * @param errorMsg
	 */
	public abstract void errorMsg(Exception e);

}