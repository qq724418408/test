package com.bocop.jxplatform.config;

import android.os.Environment;

/**
 * 主要放置交易报文相关的常量
 * 
 * @author 庄海滨
 * 
 */
public class TransactionValue {
	
	/**
	 * CSP外部交易码：EB4034  
	 */
	public static final String EB4034 = "EB4034";
	/**
	 * CSP外部交易码：AP1002 绑定车辆
	 */
	public static final String APJJ01 = "APJJ01";			//绑定车辆
	public static final String APJJ02 = "APJJ02";			//CSP外部交易码：AP1002 查询车辆列表
	public static final String APJJ03 = "APJJ03";			//CSP外部交易码：AP1003 查询车辆详细信息
	public static final String APJJ04 = "APJJ04";			//解除车辆绑定
	public static final String APJJ05 = "APJJ05";			//绑定驾驶证
	public static final String APJJ19 = "APJJ19";			//绑定驾驶证2
	public static final String APJJ06 = "APJJ06";			//绑定驾驶证
	public static final String APJJ07 = "APJJ07";			//接触啊驾驶证绑定
	public static final String APJJ08 = "APJJ08";
	public static final String APJJ09 = "APJJ09";			//接受交通违法处罚
	public static final String APJJ10 = "APJJ10";			//查询用户绑定驾驶证已经产生处罚决定书编号的信息
	public static final String APJJ11 = "APJJ11";			//查询历史缴费记录
	public static final String APJJ12 = "APJJ12";			//是否绑定驾驶证
	public static final String APJJ13 = "APJJ13";			//缴纳罚款
	
	public static final String APJJ14 = "APJJ14";			//机动车更改手机号 
	public static final String APJJ15 = "APJJ15";			//驾驶证更改手机号 
	public static final String APJJ16 = "APJJ16";			//查询违章处理失败
	public static final String APJJ17 = "APJJ17";			//查询违章处理中的信息
	public static final String APJJ18 = "APJJ18";			//接受交通违法处罚(新)
	public static final String EB4035 = "EB4035";
	
	public static final String SA0052 = "/banking/paysendchit";		//发送短信
	
	public static final String SA7114 = "/unlogin/gettelcode";		//发送短信
	public static final String SA7115 = "/unlogin/validatetelcode";		//发送短信
	public static final String SA0053 = "/app/useridquery";		//用户附加信息查询
	public static final String SA0056 = "/app/checkinfocode";		//验证短信验证码
	public static final String SA0015 = "/bocop/oauth/token";		//刷新TOKEN
//	public static final String SA0015 = "/oauth/token";		//刷新TOKEN
	public static final String SA0075 = "/app/findusrinfo";
	public static final String SA0059 = "/app/debitbalsearch";
	public static final String SA0093 = "/base/asr/messvalidate";
	public static final String SA0000 = "/interFace/getAppUpdate.php";
	public static final String SA0069 = "/app/queryetoken";//查询Etoken
	public static final String SA0083 = "/base/asr/checketoken";//验证Etoken
	/**
	 * CSP外部交易码：小秘书
	 */
	public static final String CSPSZF = "CSPSZF";
	public static final String CSPGXD = "GDTZF";
	public static final String GETMSG = "GETMSG";
	
	/**
	 * 个贷通改动
	 */
	public static final String DEBIT_BALANCE = "/base/asr/v2/query_debit_balance";//借记卡余额 
	public static final String QUERY_ETOKEN = "/base/asr/v2/queryetoken";//获取动态口令序列号
	public static final String BIND_ETOKEN = "/base/asr/v2/bindetoken";//绑定动态口令
	public static final String UNTIE_ETOKEN = "/base/asr/v2/untieetoken";//解绑动态口令

//	PD0004
	public static final String messageType = "PD0004";
	public static final String CHANNELFLAG = "1";
	public static final String telephoneNumber = "95566";// 中行号码
	public static final String bgFlag = "1.4.0";// 后台标识

	/** 图片存放位置 */
	public static final String IMGE_PATH_SDCARD = Environment.getExternalStorageDirectory() + "/";// 默认图片缓存地址

	/** 版本检查 */
	public static final String CHECKVERSION_TXN = "1COM000003";
	// public static final String CHECKVERSION = "/version.do";
	public static final String CHECKVERSION = "/getAppUpdate.php";// 新版本检测接口

	/** 下载密钥 */
	public static final String DES_UPDATE = "/getDesKey.do";
	public static final String DES_UPDATE_TXN = "1SYS020005";

	/** 用户附加信息 */
	public static final String COMM_USER_APP_INFO_QUERY = "/commUserAppInfoQuery.do"; // 用户附加信息查询
	public static final String COMM_USER_APP_INFO_QUERY_TXN = "1COM000002";
	public static final String COMM_USER_APP_ETOKEN = "/queryEtoken.do"; // etoken是否绑定
	public static final String COMM_USER_APP_ETOKEN_TXN = "1COM000002";
	public static final String COMM_USER_APP_BINDETOKEN = "/bindEtoken.do"; // 绑定etoken
	public static final String COMM_USER_APP_BINDETOKEN_TXN = "1COM000002";
	public static final String COMM_USER_APP_INFO_QUERY_SUM = "/commCardBalQuery.do"; // 查余额
	public static final String COMM_USER_APP_INFO_QUERY_SUM_TXN = "1COM000010";

	/** 发送短信验证码 */
	public static final String COMM_SEND_SMS_VERIFY_CODE_TXN = "1COM000006"; // 发送短信验证码交易号
	public static final String COMM_SEND_SMS_VERIFY_CODE = "/commSendSMSVerifyCode.do"; // 发送短信验证码

	/** 验证短信验证码 */
	public static final String COMM_CHECK_SMS_VERIFY_CODE_TXN = "1COM000007"; // 验证短信验证码交易号
	public static final String COMM_CHECK_SMS_VERIFY_CODE = "/commCheckSMSVerifyCode.do"; // 验证短信验证码
}