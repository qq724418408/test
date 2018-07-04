package com.bocop.jxplatform.config;

import com.boc.jx.httpUnits.AndroidCommFactory;

/*
 * 设置中银开放平台KEY,SERRET,URL,PORT,通用URL地址信息
 */
public class BocSdkConfig {

	// 版本号
	public static String APP_VERSION = "3.0.3";

	public static String NEW_APP_VERSION;
	public static Boolean NEED_UPDATE = false;
	public static String APP_URL;
	public static String UPDATE_CONTENT; // 对比版本号，是否需要更新
	public static String UPDATE_NECESSARY; // 是否需要强制更新

	public static String responseType = "token";

	/**
	 * T1环境
	 */
	// public static String CONSUMER_KEY = "1139";// 测试环境
	// public static String CONSUMER_SECRET =
	// "0a47f37943dee85aa42f5d665706e9b00ba536cf2820c70c4f";// 测试环境
	// public static String CONSUMER_URL = "http://22.188.36.180";// 测试环境
	// public static int CONSUMER_PORT = 8080; // 测试环境

	/**
	 * 分行测试环境 //
	 */
	// public static String CONSUMER_KEY = "147";// 测试环境
	// public static String CONSUMER_SECRET =
	// "06b60a2b92995afd6937412443c3f1d49cccf1682ad9f9a40d";// 测试环境
	// public static String CONSUMER_URL = "http://22.188.12.161";// 测试环境
	// public static int CONSUMER_PORT = 8080; // 测试环境

	/*
	 * 生产环境
	 */
	public static String CONSUMER_KEY = "386";// 生产环境
	public static String CONSUMER_SECRET = "269f8e525d1d2fc9a002e68c667c4200b5176a2d47518faffe";// 生产环境
	public static int CONSUMER_PORT = 443; // 生产环境
	public static String CONSUMER_URL = "https://openapi.boc.cn";// 生产环境

	// **************************************************************************************************************

	// public static String HTTP_SAP_IP_PBTEST = "https://openapi.boc.cn";
	// public static String HTTP_URL = "http://22.188.12.105/wap";// 测试环境
	public static String HTTP_URL = "https://open.boc.cn/wap";// 生产环境

	// public static String HTTP_VERSION = "http://22.188.12.105";// 测试环境
	public static String HTTP_VERSION = "http://open.boc.cn";// 生产环境

	// 便民缴费URL
	// public static String bmjfurl =
	// "http://219.141.191.126:80/conPayH5/?channel=jxzyh";// 测试环境
	public static String bmjfurl = "http://219.141.191.126:80/conPayH5/?channel=jxeht";// 生产环境

	/*
	 * bocopSDK需要设置的port
	 */

	// public static int HTTP_SAP_PORT_PBTEST = 443;

	/*
	 * 注册地址
	 */
	public static String CONSUMER_REGISTER = "https://open.boc.cn/wap/register.php?act=perregister&clientid="
			+ BocSdkConfig.CONSUMER_KEY + "&themeid=1&devicetype=1";

	/*
	 * 是否启用注册功能（添按钮）
	 */
	public static Boolean CONSUMER_IS_REGISTER = true;

	/**
	 * 手势密码是否开启，缓存中的名称
	 */
	public static String GSNAMEINSHARED = "";
	/**
	 * 手势密码信息，缓存中的名称
	 */
	public static String GPNAMEINSHARED = "";

	/*
	 * 银行卡管理URL地址
	 */
	public static String CARDMANAGER_URL = "http://open.boc.cn/wap/cardmange.php?clientid=";

	/*
	 * 用户密码管理
	 */
	public static String USERPASSWORD_URL = "https://open.boc.cn/wap/pwdedit.php?clientid=" + CONSUMER_KEY;

	// 通用接口配置
	// public final static String LOGIN_MCIS = CONSUMER_URL + ":" +
	// CONSUMER_PORT
	// + "/bocop/mcis";
	// public final static String LOGIN_MCISCSP = CONSUMER_URL + ":" + CONSUMER_PORT
	// + "/mciscsp"; //测试 CSP

	public final static String LOGIN_MCISCSP = CONSUMER_URL + "/mciscsp";
	public final static String UNLOGIN_MCISCSP = CONSUMER_URL + "/unlogin/mciscsp";
	// public final static String LOGIN_MCISCSP =
	// "http://59.63.161.77:8082/TestWeb/csptest.do";
	// public final static String LOGIN_MCISCSP = CONSUMER_URL
	// + "/mciscsp";
	// public final static String LOGIN_MCISCSP = CONSUMER_URL + ":" +
	// CONSUMER_PORT
	// + "/bocop/mciscsp";
	// public final static String UNLOGIN_MCIS = CONSUMER_URL + ":" +
	// CONSUMER_PORT
	// + "/bocop/unlogin/mcis";
	// public final static String UNLOGIN_MCISCSP = CONSUMER_URL + ":" +
	// CONSUMER_PORT
	// + "/bocop/unlogin/mciscsp";

	/** 生产 **/
	// public static final String HTTP_SVR = "http://kongbei.vicp.net:6081";
	// public static final String HTTP_SVR = "https://openapi.boc.cn/bocop";
	// public static final String HTTP_SVR = "http://219.141.191.126:80";
	// public static final String HTTP_ROOT = "/conPay";// 项目

	public static final String HTTP_SVR = "https://openapi.boc.cn";
	public static final String HTTP_ROOT = "443";// 项目

	// public static final String CONSUMER_KEY = "205"; //ios的key和secret
	// public static final String CONSUMER_SECRET =
	// "91255b88c36bbe25693cedd17e67821c2b901ac32880d12a9c";

	public static final String WEB_URL = "https://open.boc.cn";
	public static final String REGISTER_URL = WEB_URL + "/wap/register.php?act=perregister&themeid=4&clientid="
			+ CONSUMER_KEY + "&devicetype=1";
	public static final String BINDCARD_URL = WEB_URL + "/wap/cardmange.php";// 参数已在Activity中加载

	public static final String LOGIN_URL = "https://openapi.boc.cn";
	public static final int LOGIN_URL_PORT = 443;

	public static final String CONSUMER_KEY_CHECKVERSION = "200"; // 版本更新生产环境key
	public static final String HTTP_SVR_CHECKVERSION = "http://open.boc.cn/interFace"; // 生产版本更新
	public static final String HTTP_SVR_REQUEST_IMAGE = "http://open.boc.cn/ucs/public/userinterface"; // 生产请求开放平台头像
	// public static final String HTTP_ROOT_CHECKVERSION = "/interFace";

	/** 生产 **/
	public static final int HTTPS_PORT = 443;

	public static final int COMM_TYPE = AndroidCommFactory.COMM_TYPE_HTTP;
	public static final int APK_DOWNLOAD_COMM_TYPE = COMM_TYPE;
	public static final int IMG_DOWNLOAD_COMM_TYPE = COMM_TYPE;

	public static final int REQUEST_TIMEOUT = 10000;// 请求超时为10秒
	public static final int RESPONSE_TIMEOUT = 60000;// 响应超时为60秒
	public static final String ENCODE = "UTF-8";

	public static final int OTHER_ERROR = -1;// 其他错误
	public static final int TIMEOUT_ERROR = -2;// 超时
	public static final int FORCE_CLOSE = -3;// 强制关闭
	public static final int RIGHT_CODE = 0;// 联网正常
	public static final int FILE_EXIT = 1;// 文件已存在
	public static final int SD_NOSPACE = 2;// sd卡空间不足

	public static final int JSON_POST = 0;
	public static final int JSON_GET = 1;
	public static final int JSON_PUT = 2;

	public static final String BKS_RES_PATH = "/res/raw/https_keystore_wan.keystore";

	public final static String MCISCSP_PATH = "http://22.188.12.161:8080/mciscsp";

	/**
	 * 小秘书
	 */
	public static final String SECRETARY_PATH = "http://22.220.8.253:8081/jxxmsapp/xmsapp/queryAddress.do";
	public static final String SECRETARY_MESSAGE_PATH = "http://22.220.8.253:8081/jxxmsapp/xmsapp/queryPushInfo.do";
	public static final String SECRETARY_DEVICE_PATH = "http://22.220.8.253:8081/jxxmsapp/xmsapp/deviceInfo.do";
	// 注册推送设备url
	public static final String SECRETARY_REGISTER_DEVICE_PATH = "http://59.63.161.77:8081/PushServer/deviceRegister";
	// 获取uuid
	public static final String SECRETARY_REQUEST_UUID_PATH = "http://59.63.161.77:8081/PushServer/getUUID";

	public static final String DDPUSH_SERVER_IP = "59.63.161.77";// ddpush服务器IP
	public static final String DDPUSH_CLIENT_PORT = "9966"; // 推送服务器连接端口
	public static final String DDPUSH_PUSH_PORT = "9999"; // 推送服务器发起推送端口
	public static final String UUID_KEY = "UUID.key";
	public static final String REGIST_DEVICE_KEY = "RegistDevice.key";
	public static final String DEVICE_ID_KEY = "deviceId.key";
	public static final int APP_ID = 1;// appId

	public static String SENT_PKGS = "sentPkgs";
	public static String RECEIVE_PKGS = "receivePkgs";
	public static final String APP_PUSH_LOG_PATH = "/BOC_JX_APP/push";

	/**
	 * 健康通URL
	 */
	public static String jketurl = "http://123.124.191.179/jket/assets/widget/immp/cityhospital/selecthospital_content.html";
	// http://22.220.75.70:8080/qzt/selector.do?method=querySelector

	/**
	 * 签证通
	 */
	// 生产
	public static String qztUrl = "http://123.124.191.179";

	// public static String qztUrl = "http://22.220.75.79:8080";
	// public static String qztUrl = "http://22.220.75.113:8080";
	// public static String qztUrl = "http://22.220.13.64:8080";
	// public static String qztUrl = "http://22.220.75.101:8080";

	/**
	 * 积分商城
	 */
	public static String jfscurl = qztUrl + "/gmall-wap/";

	// public static String jfscCsurl =
	// "http://analysis.vicp.net:70/caishen/b.html";
	public static String jfscCsurl = qztUrl + "/gmall-wap/caishen/index.html";
	// public static String jfscurl = "http://22.220.84.181:8080/gmall-wap/";

	public static String qztApplyurl = qztUrl + "/qzt/selector.do?method=querySelector";
	public static String qztSubmitUrl = qztUrl + "/qzt/selector.do?method=apply";
	public static String qztAttentionUrl = qztUrl + "/qzt/selector.do?method=attention";
	public static String qztDocumentsUrl = qztUrl + "/qzt/selector.do?method=documents";
	// 查询订单
	public static String qztOrderUrl = qztUrl + "/qzt/selector.do?method=visainfo";
	// 查询订单
	public static String qztOrderDelUrl = qztUrl + "/qzt/selector.do?method=deleteordernum";
	// 签证进度
	public static String qztProgressUrl = qztUrl + "/qzt/selector.do?method=visastatus";
	// 缴费
	public static String qztpayUrl = qztUrl + "/qzt/selector.do?method=payment";

	public static String qztPostForXmsUrl = qztUrl + "/qzt/selector.do?method=logininfo";

	// 查询常用人联系人列表
	public static String qztContactListUrl = qztUrl + "/qzt/selector.do?method=ContactInfo";

	// 常用人联系人删除
	public static String qztContactDelete = qztUrl + "/qzt/selector.do?method=ContactDelete";

	// 常用人联系人添加
	public static String qztContactAdd = qztUrl + "/qzt/selector.do?method=ContactAdd";

	// 营销代码录入
	public static String marketAddUrl = qztUrl + "/yhtlog/selector.do?method=marketinginfo";

	// 营销代码录入
	public static String infomalLogin = qztUrl + "/yhtlog/selector.do?method=informallogin";
	// 营销代码查询
	public static String marketQueryUrl = qztUrl + "/yhtlog/selector.do?method=marketingselect";

	// public static String postForLog = qztUrl +
	// "/qzt/selector.do?method=logininfo";
	public static String postForLog = qztUrl + "/yhtlog/selector.do?method=eventinfo";

	public static String DialogUrl = qztUrl + "/yhtlog/selector.do?method=requestinfo";

	public static String urlForZhbbm = qztUrl + "/enroll_web/index.html";

	/**
	 * 工薪贷
	 */
	public static final String YFX_PATH = "http://127.0.0.1:9080/jxyrt/app/";
	public static final String YFX_IMG_LIST = YFX_PATH + "proList.do?method=query";
	public final static String YFX_LOGIN_MCISCSP = CONSUMER_URL + "/mciscsp";// 工薪贷环境
	public final static String YFX_LOGIN_MCISCSP_TEST = CONSUMER_URL + "/mciscsp";// 工薪贷测试环境
	public final static String YFX_DRAWING_CONFIRM = "http://123.124.191.179/wlsasr2/loan2.do";// 工薪贷测试环境

	/**
	 * 中银易融
	 */
	public static final String ZYYR_SERVE = "http://123.124.191.179/jxyrt/app/";
	public static final String ZYYR_PDT_LIST = ZYYR_SERVE + "proList.do?method=proList";
	public static final String ZYYR_PDT_DETAILS = ZYYR_SERVE + "proInfo.do?method=proInfo";
	public static final String ZYYR_LOAN_LIST = ZYYR_SERVE + "myProList.do?method=myProList";
	public static final String ZYYR_LOAN_DETAILS = ZYYR_SERVE + "myProInfo.do?method=myProInfo";
	public static final String ZYYR_DELETE_LOAN = ZYYR_SERVE + "delPro.do?method=delPro";
	public static final String ZYYR_APPLY_LOAN = ZYYR_SERVE + "applyPro.do?method=applyPro";
	public static final String ZYYR_LIST_PARA = ZYYR_SERVE + "listPara.do?method=listPara";
	// 查询认证资料
	public static final String ZYYR_QUERY_USERINFO = ZYYR_SERVE + "userInfo.do?method=userInfo";
	// 修改、添加认证资料
	public static final String ZYYR_MODIFY_USERINFO = ZYYR_SERVE + "modify.do?method=modify";
	// 检查是否认证资料
	public static final String ZYYR_CHCEK_USERINFO = ZYYR_SERVE + "checkUser.do?method=checkUser";
	// 测试开关
	public static boolean isTest = false;
	public static final String FLAG_PROGRESS = "flag_progress";

	// 海涛专区
	// public static final String HTZQ = "http://22.220.13.63/htzq/index.html";
	public static final String HTZQ = "http://123.124.191.179/htzq/index.html";
	// 财富通
	// public static final String CFT = "http://22.220.13.63/cft/index.html";
	public static final String CFT = qztUrl + "/gjs/cft/securityCall.html?";

	public static final String fpsc = qztUrl + "/fpsc/page/index.html?";

	// "http://123.124.191.179/cft/index.html";

	// 旅游通
	public static final String LYT = "http://123.124.191.179/jxlytApp/appPage/index.html";
	// 开户通
	public static final String KHT = "http://123.124.191.179/OpenAccount/app.html?platform=yht";
	// 结汇
	public static final String GOUHUI = "https://openapi.boc.cn/af/mobileHtml/html/gouhui/buyExchange.html?channel=android";
	// 购汇通
	public static final String JIEHUI = "https://openapi.boc.cn/af/mobileHtml/html/jiehui/sellExchange.html?channel=android";
	// 优品通
	public static final String YPT = "https://jf365.boc.cn/BOCCMALL_M/index.do";

	// 信用卡申请
	public static final String CARD = "https://apply.mcard.boc.cn/apply/mobile/index ";

	// 卡惠通
	// public static final String WEIHUI =
	// "http://jxboc.uni-infinity.com/wxyx/wx/page/forword/jingxuan.ht?authToken=26fa5fbda9cd476d899432dad4c5a982&authApp=yht&userId=";
	// public static final String WEIHUI =
	// "http://22.220.13.64:8080/wxyx/wx/page/forword/jingxuan.ht?authToken=26fa5fbda9cd476d899432dad4c5a982&authApp=yht&userId=";
	public static final String WEIHUI = "http://jxboc.uni-infinity.com/wxyx/wx/page/forword/jingxuan.ht?authToken=26fa5fbda9cd476d899432dad4c5a982&authApp=yht&userId=";
	// public static final String HTZQ = "http://123.124.191.179/cft/index.html";
	// http://123.124.191.179/cyh/car_search.html
	public static final String RED_PACKET = "http://123.124.191.179/llyh/common/user/getRedbagInfo.do";
	public static final String LLYHURL = "http://123.124.191.179/llyh";
	// 大转盘
	public static final String DZP_URL = "http://123.124.191.179/JXRoulette/roulette/roulette.html";

	// 银行卡管理
	public static final String YHKGL_URL = "https://open.boc.cn/bocop/#/app/cardLists";
	// 注册新用户
	public static final String RES_URL = "http://open.boc.cn/bocop/#/app/register";
}
