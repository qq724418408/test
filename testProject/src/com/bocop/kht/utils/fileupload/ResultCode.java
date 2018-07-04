package com.bocop.kht.utils.fileupload;

/**
 * 调用返回码
 * 
 * @author xiaxq
 *
 *         2016年7月21日
 */
public class ResultCode {

	public static final String SUCCESS = "00";// 成功
	public static final String IMAGE_SOURCE_TYPE_ERROR = "90";// 来源类型错误
	public static final String IMAGE_FORMAT_ERROR = "91";// 图片格式错误
	public static final String IMAGE_HEIGHT_ERROR = "92";// 图片高度超限
	public static final String IMAGE_WIDTH_ERROR = "93";// 图片宽度超限
	public static final String IMAGE_NAME_ERROR = "94";// 图片名称错误
	public static final String IMAGE_TOO_LARGE = "95";// 压后图片大小大于目标质量
	public static final String IMAGE_LESS_THAN_200 = "97";// 目标质量小于200
	public static final String OTHER_ERROR = "99";// 其他错误,例如回调函数不可为空

	public static final String IMAGE_OUT_OF_MEMORY = "50";// 图片过大导致读取时内存溢出
	
	
	public static final String FACE_DEFEAT = "1000";// 活体检测失败原因
	

}
