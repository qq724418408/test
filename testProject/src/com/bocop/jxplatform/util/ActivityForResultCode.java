package com.bocop.jxplatform.util;
/** 
 * @author luoyang  
 * @version 创建时间：2015-6-29 下午7:35:41 
 * 类说明 
 */

public class ActivityForResultCode {

	//模拟控制是否绑定驾驶证
	public static boolean flag = true;
	//模拟控制是否绑定驾驶证，认罚
	public static boolean iflag = true;
	/**
	 * 添加驾驶证返回码
	 */
	public static int CodeForLicenseAdd = 1;
	/**
	 * 添加车辆返回码
	 */
	public static int CodeForCarAdd =2;
	/**
	 * 解除车辆绑定返回
	 */
	public static int CodeForCarGo =3;
	/**
	 * 解除驾驶证绑定返回
	 */
	public static int CodeForLicenseGo =4;
	
	/**
	 * 办理违章跳转绑定车辆
	 */
	public static int CodeForCarPeccancy = 3;
	
	public static int CodeForGesture = 5;
	
	/**
	 * 签证通添加常用联系人
	 */
	public static int CodeForQztContactAdd = 6;
	
	public static int CodeForQztContactEdit = 7;
	
	public static int CodeForQztContactAddOne = 8;
}
