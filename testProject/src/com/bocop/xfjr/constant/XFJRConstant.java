package com.bocop.xfjr.constant;

/**
 * 消费金融常量类
 * 
 * @author wujunliu
 * 
 *
 */
public class XFJRConstant {

	public static final String M_ROLE = "0"; // 商户
	public static final String C_ROLE = "1"; // 客户经理
	
	public static final int M_STATUS_0_INT = 0; // 我的业务状态-处理中
	public static final int M_STATUS_1_INT = 1; // 我的业务状态-已通过
	public static final int M_STATUS_2_INT = 2; // 我的业务状态-已拒绝

	public static final String M_STATUS_0_STR = "处理中"; // 我的业务状态-处理中
	public static final String M_STATUS_1_STR = "已通过"; // 我的业务状态-已通过
	public static final String M_STATUS_2_STR = "已拒绝"; // 我的业务状态-已拒绝
	
	public static final int C_STATUS_0_INT = 0; // 我的业务状态-待预审
	public static final int C_STATUS_1_INT = 1; // 我的业务状态-待决策
	public static final int C_STATUS_2_INT = 2; // 我的业务状态-待审批
	public static final int C_STATUS_3_INT = 3; // 我的业务状态-待放款
	public static final int C_STATUS_4_INT = 4; // 我的业务状态-已放款
	public static final int C_STATUS_5_INT = 5; // 我的业务状态-已拒绝
	
	public static final String C_STATUS_0_STR = "待提交"; // 我的业务状态-待预审
	public static final String C_STATUS_1_STR = "转人工"; // 我的业务状态-待决策
	public static final String C_STATUS_2_STR = "待落实"; // 我的业务状态-待审批
	public static final String C_STATUS_3_STR = "待放款"; // 我的业务状态-待放款
	public static final String C_STATUS_4_STR = "已放款"; // 我的业务状态-已放款
	public static final String C_STATUS_5_STR = "已拒绝"; // 我的业务状态-已拒绝
	
	public static final String XFJR_SP_NAME = "jx_xfjr.name";
	public static final String KEY_IS_LOGIN = "isLogin.key";
	public static final String KEY_IS_FIRST_LOGIN = "isFirstLogin.key";
	public static final String KEY_IS_READ = "isRead.key";
	public static final String KEY_IS_READ0 = "isRead.key0";
	public static final String KEY_IS_READ1 = "isRead.key1";
	public static final String KEY_IS_READ2 = "isRead.key2";
	public static final String KEY_IS_READ3 = "isRead.key3";
	public static final String KEY_IS_READ4 = "isRead.key4";
	public static final String KEY_IS_READ5 = "isRead.key5";
	public static final String KEY_B_NUMBER0 = "B0_Number.key";
	public static final String KEY_B_NUMBER1 = "B1_Number.key";
	public static final String KEY_B_NUMBER2 = "B2_Number.key";
	public static final String KEY_B_NUMBER3 = "B3_Number.key";
	public static final String KEY_B_NUMBER4 = "B4_Number.key";
	public static final String KEY_B_NUMBER5 = "B5_Number.key";
	public static final String KEY_EHR_ID = "ehrId.key";
	public static final String KEY_CLIENT_PHONE = "clientPhone.key";
	public static final String KEY_USER_PHONE = "userPhone.key"; // 登录用户手机号
	// 申请人姓名
	public static final String KEY_USER_NAME = "KEY_USER_NAME.key";
	// 申请人身份证
	public static final String KEY_USER_IDCARD = "KEY_USER_IDCARD.key";
	
	public static final int GO_TO_QZZC = 1; // 欺诈侦测第一步
	public static final int GO_TO_FXYSX = 7; // 风险预授信
	public static final int GO_TO_BCZL = 8; // 欺诈侦测页面

	public static final String KEY_PRETRIAL_BEAN = "pretrialParamBean.key";
	public static final String KEY_PRETRIAL_RESULT = "pretrialResult.key";
	public static final String KEY_MARRIED_STATUS = "marriedStatus.key";
	public static final String KEY_HAS_HOUSE_STATUS = "hasHouseStatus.key";
	public static final String KEY_CREDIT_LINE = "creditLine.key"; // 授信额度
	public static final String KEY_BUSINESS_STATUS = "businessStatus.key"; // 
	public static final String ACTION_MARRIED_STATUS_CHANGE = "com.xfjr.br.marriedStatusChange";
	public static final String ACTION_HAS_HOUSE_STATUS_CHANGE = "com.xfjr.br.hasHouseStatusChange";
	public static final String ACTION_UPDATE_DATA = "com.xfjr.br.updateData";
	public static final String ACTION_FLAG_INT = "com.xfjr.br.flagInt";
	
}
