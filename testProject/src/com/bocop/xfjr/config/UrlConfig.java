
package com.bocop.xfjr.config;

import com.boc.jx.base.BaseApplication;
import com.boc.jx.httptools.network.http.DConfing;
import com.boc.jx.tools.LogUtils;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.activity.MainActivity;
import com.bocop.xfjr.XfjrMain;
import com.bocop.xfjr.bean.ErrorBean;
import com.bocop.xfjr.util.dialog.XFJRDialogUtil;
import com.bocop.xfjr.util.dialog.XFJRDialogUtil.DialogClick;
import com.bocop.xfjr.util.dialog.XfjrDialog;
import com.bocop.yfx.utils.ToastUtils;
import com.google.gson.Gson;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

/**
 * description： 接口
 * <p/>
 * Created by TIAN FENG on 2017年9月6日 QQ：27674569 Email: 27674569@qq.com
 * Version：1.0
 */
public class UrlConfig {

	public static String successCode = "10000";
	public static String emptyCode = "10013";
	public static String dynamicUrlExceptionCode = "66666"; // dynamic url exception:don't find this APP_ID
	public static int FRDNotEnoughCode = 1012; // (人脸识别度不够)The degree of face recognition is not enough
	public static int CONNECT_TIMEOUT = 10;// 链接超时
	public static int READ_TIMEOUT = 10;// 读取超时
	public static int WRITE_TIMEOUT = 10;// 写入超时
	public static int UP_CONNECT_TIMEOUT = 60;// 链接超时
	public static int UP_READ_TIMEOUT = 60;// 读取超时
	public static int UP_WRITE_TIMEOUT = 60;// 写入超时

	/**
	 * 网络请求错误提示语统一
	 * 
	 * @param context
	 */
	public static void showErrorTips(final Context context, Throwable e, boolean isShowToast) {
		String json = e.getMessage();
		LogUtils.e("error Json = " + json);
		ErrorBean b = new Gson().fromJson(json, ErrorBean.class);
		if (null != b) { // 66666 --> dynamic url exception:don't find this APP_ID
			if (dynamicUrlExceptionCode.equals(b.msg) || (DConfing.NeedReLogin.equals(b.msg))
					|| b.msg.contains("账号异常")) {
				needReLoginDialog(context);
				return;
			}
			switch (b.code) {
			case 10030:
			case 10040:
				needReLoginDialog(context);
				break;
			case 10050:
				if (!TextUtils.isEmpty(b.msg)) {
					ToastUtils.show(context, b.msg, 0);
				} else {
					ToastUtils.show(context, "请求两次了", 0);
				}
				break;
			default:
				if (!TextUtils.isEmpty(b.msg)) {
					if (isShowToast)
						ToastUtils.show(context, b.msg, 0);
				}
				break;
			}
		}
	}

	private static void needReLoginDialog(final Context context) {
		XfjrDialog dialog = XFJRDialogUtil.confirmDialog(context, DConfing.NeedReLogin, new DialogClick() {

			@Override
			public void onOkClick(View view, XfjrDialog dialog) {
				XfjrMain.gotoLogin(context);
				dialog.cancel();
			}

			@Override
			public void onCancelClick(View view, XfjrDialog dialog) {
				BaseApplication app = (BaseApplication) XfjrMain.mApp;
				app.getActivityManager().finishAllWithoutActivity(MainActivity.class);
				dialog.cancel();
			}
		});
		TextView tvOk = dialog.getView(R.id.tvOk);
		TextView tvCancel = dialog.getView(R.id.tvCancel);
		tvOk.setText("确定"); // 到登录页，重新登录
		tvCancel.setText("退出"); // 退出消费金融预审批系统，回到易惠通首页
		dialog.setCancelable(false);
		dialog.show();
	}

	// 生产环境----生产环境---生产环境
	public static String SERVER_ROOT_PATH = "/xfjr/";
	public static String SERVER = "http://123.124.191.179/xfjr/";
	public static String BASE_SERVER = "http://123.124.191.179";

	// TODO 外网UAT江西测试环境（http://fir.im/3sgl）
	// public static String SERVER_ROOT_PATH = "/ZeroApi/"; // 江西sit 动态流程模外网2
	// public static String SERVER = "http://182.106.129.135:9999/ZeroApi/"; //
	// 动态流程模外网2
	// public static String BASE_SERVER = "http://182.106.129.135:9999"; // 动态流程模外网2

	// TODO 内网DEV开发环境
	// public static String SERVER_ROOT_PATH = "/Zero-api/"; //dev 动态流程模内网1
	// public static String BASE_SERVER = "http://192.168.22.67:8080"; //
	// 动态流程模内网1(调试用)
	// public static String SERVER = "http://192.168.22.67:8080/Zero-api/"; //
	// 动态流程模内网1(调试用)

	// TODO 内网SIT深圳测试环境（https://www.pgyer.com/xfjrsit）
	// public static String customerPhone = "17607842058"; //TODO 打包的时候置空
	// public static String smsCode = "123456"; //TODO 打包的时候置空
	// public static String SERVER_ROOT_PATH = "/Zero-api/"; // SIT
	// public static String BASE_SERVER = "http://192.168.22.182:8080"; //
	// 动态流程模内网2(调试用)
	// public static String SERVER = "http://192.168.22.182:8080/Zero-api/"; //
	// 动态流程模内网2(调试用)

	public static final String ACTION_ID_028 = "App999";

	/**
	 * 公共接口模块*****************************************************************************start
	 **/
	public static String REQ_LOGIN_CODE = "API000101"; // 请求服务器发送短信验证码
	public static String REQ_LOGIN = "API000201"; // 用户登录
	public static String REQ_LOGOUT = "API000202"; // 用户退出
	public static String ACCESST_TOKEN_LOGIN = "API000203"; // ACCESST_TOKEN_LOGIN
	/**
	 * 公共接口模块*******************************************************************************end
	 **/

	/**
	 * 商户模块*******************************************************************************start
	 **/
	public static final String REQ_INDEX_CUSTOMER_LIST = "API010201"; // 获取商户业绩
	public static final String REQ_INDEX_BUSINESS_NUM = "API010202"; // 商户获取首页业务数目数据
	public static final String M_REQ_BUSINESS_LIST = "API010203"; // 获取业务列表--author wujunliu--API010203
	public static final String M_REQ_BUSINESS_DETAIL = "API010204"; // type=0/1/2/3 获取业务基本信息--author wujunliu--05
	public static final String M_QUERY_BUSINESS = "API010205"; // 商户业务查询
	/**
	 * 商户模块*********************************************************************************end
	 **/

	/**
	 * 客户经理模块
	 * ****************************************************************************start
	 **/
	public static String REQ_SYS_BASIC_INFO = "API020102"; // 获取系统基本信息
	public static final String REQ_INDEX_BUSINESSINFO = "API020202"; // 首页业务数据
	public static final String C_REQ_BUSINESS_LIST = "API020203"; // 获取业务列表--author wujunliu-
	public static final String C_REQ_BUSINESS_DETAIL = "API020204"; // type=0/1/2/3 获取业务基本信息
	public static final String REQ_INDEX_MANAGER_QUERYINFO = "API020205"; // 商户经理业务查询
	public static final String REQ_BUSINESS_STATUS = "API020206"; // 【API020206】通过商户编号、渠道编号和产品编号获取欺诈侦测规则和授信模型
	public static final String REQ_MERCHANT_LIST = "API020301"; // 根据merchantId、channelId获取商户、渠道和产品信息--author
																// wujunliu--01
	public static final String REQ_CHANNEL_PRODUCT_LIST = "API020302"; //
	public static final String REQ_QUERY_DETAILINFO = "API020204"; // 业务详情查询
	public static final String REQ_ASSESS_INFO = "API020303"; // 获取风险预决策信息--author wujunliu--07
	public static final String ADD_BUSINESS = "API020303"; // 新增进件开始评估提交--author wujunliu--02
	public static String WARRANT_SEND_URL = "API020304"; // 欺诈侦测流程第一步，上传征信授权书、信息授权书、客户身份证、合影照片
	public static String PHONT_CHECK = "API020305"; // 欺诈侦测-手机验证
	public static String SUBMIT_FACE_INFO = "API020306"; // 欺诈侦测 - 提交人脸信息
	public static String CHECK_RISK_URL = "API020307"; // 欺诈 -人行风险验证
	// public static String CHECK_OTHER_URL = "API020312"; // 欺诈 -第三方征信风险验证
	// API020307
	public static String CHECK_BANK_CARD_URL = "API020308"; // 欺诈 -银行卡验证
	public static String CHECK_THREE_PART = "API020309"; // 欺诈侦测-第三方征信验证
	public static final String SUBMIT_PRETRIAL_INFO = "API020310"; // 提交风险预决策
	public static final String ADD_DATA = "API020311"; // 补充资料提交--author wujunliu--03
	public static final String CHECK_SYNC = "API020312"; // 欺诈侦测-验证结果异步查询
	public static String ASYNC_RESULT = "API020312"; // 异步获取结果
	public static String OVER = "API020313"; // 结束申请
	/**
	 * 客户经理模块
	 * ******************************************************************************end
	 **/

	/**
	 * 开放接口模块****************************************************************************start
	 **/
	public static String VALIDATE_CHECK_PEOPLE = "API030101"; // 人行征信判断
	public static String GET_CHECK_NUMBER = "API030201"; // 获取验证码 API000101
	public static String VALIDATE_CHECK_NUMBER = "API030202"; // 校验验证码
	public static String VALIDATE_CHECK_CREDIT = "API030301"; // 验证银联信用卡
	public static String VALIDATE_CHECK_THREE_PART = "API030401"; // 第三方征信判断
	/**
	 * 开放接口模块
	 * ******************************************************************************end
	 **/

}