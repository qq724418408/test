package com.boc.jx.constants;

import com.bocop.jxplatform.config.BocSdkConfig;

/**
 * 
 * @author xmtang
 * 
 */
public class Constants {
	
	public static final String SHARED_PREFERENCE_NAME = "RIDERS_SHARED_PREFERENCE";
	
	public static final String SHARED_CUSTOM_INFO = "CUSTOM_INFO";
	
	public static final String CUSTOM_PREFERENCE_NAME = "CUSTOM_PREFERENCE_NAME";
	public static final String CUSTOM_ID_NO = "CUSTOM_ID_NO";
	public static final String CUSTOM_MOBILE_NO = "CUSTOM_MOBILE_NO";
	public static final String CUSTOM_USER_NAME = "CUSTOM_USER_NAME";
	public static final String CUSTOM_CUS_ID = "CUSTOM_CUS_ID";
	public static final String CUSTOM_PUT_ALREADY = "CUSTOM_PUT_ALREADY";
	public static final String CUSTOM_FLAG = "CUSTOM_FLAG";
	public static final String CUSTOM_LOG_FLAG = "CUSTOM_LOG_FLAG";
	
	public static final String XMS_AD = "XMS_AD";
	// public static final String TOKEN = "csrftoken";
	public static final String SESSION_ID = "sessionid";
	
	public static String LOCK_PATTERN_FILE = "password.key";
	/**
	 * 登录token
	 */
	public static final String ACCESS_TOKEN = "access_token";
	/**
	 * SharedPreference 中的字段 保存是否已经登录
	 */
	public static final String LOGINED_IN = "logined_in";
	/**
	 * SharedPreference 中的字段 保存登录的用户名
	 */
	public static final String USER_NAME = "username";
	/**
	 * 登录userid
	 */
	public static final String USER_ID = "userid";
	/**
	 * 定位是否成功标识
	 */
	public static final String LOCATE_STATUS = "locate_status";
	/**
	 * 当前位置经度
	 */
	public static final String LNG_CURRENT_LOCATION = "lng_current_location";
	/**
	 * 当前位置纬度
	 */
	public static final String LAT_CURRENT_LOCATION = "lat_current_location";
	/**
	 * 当前位置纬度
	 */
	public static final String AUDIO_INTRODUCTION = "audio_introduction_auto_play";
	/**
	 * 是否需要登录
	 */
	public static final String NEED_LOGIN = "need_login";
	/**
	 * 中行卡号
	 */
	public static final String BANK_CARDNO = "bank_cardno";
	/**
	 * 身份证号
	 */
	public static final String ID_NO = "id_no";
	/**
	 * 手机号
	 */
	public static final String MOBILENO = "mobile_No";
	/**
	 * 当前城市
	 */
	public static final String CURRENT_CITY = "current_city";
	
	public static boolean isActive = true;//程序是否在前台
    public static boolean handFlg = true;//是否已经解锁
    
    //日志标志
    public static String eventId = "eventId";//  key
    public static String logSys = "yht";//  key
    public static String login = "login";//  登陆时间
    
    public static String UrlForZyys = "http://open.boc.cn/appstore/#/app/appDetail/299";
    
    //小秘书咨询链接
    public static String xmsUrlForDotbooking = "https://mbs.boc.cn/BOCWapBank/OutletQueryProvinces.do";//  key 
    public static String xmsUrlForConsult = "http://wechat.bocichina.com/zygj_weixin/weixin/info/list.jsp?channelid=000100130023&share=1&accountno=gh_ec00b5e1997a";//  key 
    public static String xmsUrlForMarket = "http://wechat.bocichina.com/zygj_weixin/weixin/hqboc/list.jsp";//  key 
    public static String xmsUrlForDzdp = "http://m.dianping.com/";//  大众点评
    public static String xmsUrlForHx = "http://m.hexun.com/";//  和讯财经
    public static String xmsUrlForLt100 = "http://mobile.lvtu100.com/wap/";//  旅途100
    public static String xmsUrlForSuning = "http://m.suning.com/";//  苏宁
    public static String xmsUrlForWeather = BocSdkConfig.qztUrl + "/xms/weather.html";//  天气
    public static String xmsUrlForDidi = "http://common.diditaxi.com.cn/general/webEntry";//  滴滴打车
    public static String xmsUrlForFight = "http://touch.qunar.com/h5/flight/";//  购机票
    public static String xmsUrlForTrain = "http://touch.qunar.com/h5/train/?bd_source=boc_e";//  购火车票
    
    public static String xmsUrlForATM = "http://srh.bankofchina.com/search/wap/atm_l.jsp";//  ATM分布
    public static String xmsUrlForOrg = "http://srh.bankofchina.com/search/wap/opr_l.jsp";//  网点查询
    public static String xmsUrlForRate = "http://wap.boc.cn/data/rt";//  存贷款利率
    public static String xmsUrlForExchange = "http://wap.boc.cn/data/fx";//  外币牌价
    public static String xmsUrlForJJS = "https://longshortfx.boc.cn/index.html";//  基金净值
    
    public static String xmsUrlForBaidu = "https://map.baidu.com/mobile/webapp/index/index/vt=map";//  地图服务
    public static String xmsUrlForTuniu= "https://m.tuniu.com";//  旅游服务
    public static String xmsUrlForJd= "https://m.jd.com";//  网上购物
    public static String xmsUrlForCtrip= "https://m.ctrip.com";//  酒店服务
    public static String xmsUrlFor58Home= "http://m.58.com";//  家政服务
    public static String xmsUrlForSpider= "http://m.spider.com.cn";// 电影票
    public static String xmsUrlForExpress= "http://m.kuaidi100.com/index.jsp";// 快递100
//    public static String xmsUrlForTranlate= "https://m.youdao.com/translate";// 有道翻译
    public static String xmsUrlForTranlate= "https://fanyi.baidu.com";// 百度翻译
    public static String xmsUrlForToutiao= "https://m.toutiao.com";// 头条新闻
    public static String xmsUrlForJiudian= "https://touch.qunar.com/hotel";//酒店服务
    public static String xmsUrlForBoce= "http://mall.e.boc.cn/eshop-mobile/index.html";//中银E商
    
    public static String qztUrlForJiudian = "https://www.booking.com/index.zh.html";//  酒店预订
    public static String qztUrlForWifi = "http://m.ctrip.com/webapp/activity/wifi";//  全球WIFI
    
    
    
    public static String chtUrlForTxgc = "http://wx.wevein.com/app/index.php?i=3&c=entry&do=index&m=jy_yht";//  贴息购车
    public static String chtUrlForCxtb = "http://m.bocins.com/#index?mediaSource=bocyihuitong.html";//  // 车险投保
    
    public static String xmsUrlForTest= "http://720.yunwucm.com/SQS/index.html";// 电影票
    
    
    public static String qztUrlForshichang = BocSdkConfig.qztUrl + "/visa/qzt_shichangchaxun.html";//  key 
    public static String qztUrlForfeiyong = BocSdkConfig.qztUrl + "/visa/qzt_feiyongchaxun.html ";//  key 
    
    public static String fptUrlForfpsc = BocSdkConfig.qztUrl + "/fpsc/page/index.html";//  扶贫商城
    public static String fptUrlForhzhb = BocSdkConfig.qztUrl + "/fpt/index.html";//  合作伙伴
    public static String fptUrlForqyfp = "http://embipl.epub360.com/v2/manage/book/ltic1b/";//  企业扶贫
    
    public static String qztUrlForChuguo = "http://open.boc.cn/appstore/#/app/appDetail/13620";//  key 
    public static String qztUrlForJIncai = BocSdkConfig.qztUrl + "/visa/qzt_jingcaihuodong.html ";//  key 
    
    //证券通
    public static String qztUrlForOpen = "https://wechat.bocichina.com/zygj_weixin/weixin/urlhandler/?paramtype=102";// 证券开户
    public static String qztUrlForEtrade = "http://mentry.bocichina.com/special/etrade/phone_download.html";//  证券交易
    public static String xmsUrlForEshop = "http://mentry.bocichina.com/special/eshop/phone_download.html";//  中国红商城
    
    
    //首页  
    
    //校园通
    public static String UrlForMainXyt = "https://pay.shang-lian.com/sltfmin/city.html";//  校园通
    public static String UrlForMainKhtcard = BocSdkConfig.qztUrl + "/kht/index.html"; //个人开户通
   
    
    public static String UrlForMainYdt ="http://L4PtMDjv.scene.eqxiu.cn/s/L4PtMDjv";
    
    //中国人民人寿保险股份有限公司
    public static String UrlForMainYbt ="http://m.picclife.cn/m/index.jhtml?from=singlemessage";
    //中国人民财产保险股份有限公司
    public static String UrlForMainTaiping ="http://baoxian.cntaiping.com/m/";
    //太平人寿保险有限公司
    public static String UrlForMainEpicc ="http://www.epicc.com.cn/m/";
  //淘宝网
    public static String UrlForMainTbw ="https://m.taobao.com/#index";
  //京东
    public static String UrlForMainJd ="https://m.jd.com/";
  //美团外卖
    public static String UrlForMainMtwm ="http://i.waimai.meituan.com/";
  //百度外卖
    public static String UrlForMainBdwm ="http://waimai.baidu.com/mobile/waimai?qt=confirmcity&amp";
    
//    //企贷通
//    public static String UrlForMainQdt = BocSdkConfig.qztUrl +"/companyFinance/h5/index.html#/enterpriseLoan/main?";
//  //保函通
//    public static String UrlForMainBht = BocSdkConfig.qztUrl +"/companyFinance/h5/index.html#/creditCard/main?";
//  //单证通
//    public static String UrlForMainDzt = BocSdkConfig.qztUrl +"/companyFinance/h5/index.html#/letterGuarantee/main?";
    
  //企贷通
    public static String UrlForMainQdt = BocSdkConfig.qztUrl +"/zyhtbanking/h5/page/enterpriseLoan/index.html?platform=yht";
  //保函通
    public static String UrlForMainBht = BocSdkConfig.qztUrl +"/zyhtbanking/h5/page/creditCard/index.html?platform=yht";
  //单证通
    public static String UrlForMainDzt = BocSdkConfig.qztUrl +"/zyhtbanking/h5/page/letterGuarantee/index.html?platform=yht";
   //企贷通
    public static String UrlForMainQyKhtcard = BocSdkConfig.qztUrl + "/zyhtbanking/h5/page/openAccount/index.html?platform=yht"; //企业开户通
    //支付密码管理
    public static String UrlForZfmmgl = "https://open.boc.cn/wap/loadResetPayPwd?";
    
    public static String chtUrlForCht =  BocSdkConfig.qztUrl + "/cht/app/pages/index.html";//  车惠通首页
    public static String chtUrlForSxc =  BocSdkConfig.qztUrl + "/cht/app/pages/index.html#school_list.html";//  新手学车
    public static String chtUrlForGrxx =  BocSdkConfig.qztUrl + "/cht/app/pages/index.html#person_info.html";//  个人信息
    public static String chtUrlForTghd =  BocSdkConfig.qztUrl + "/cht/app/pages/index.html#group_purchase.html";//  团购活动
    public static String chtUrlForWdcz =  BocSdkConfig.qztUrl + "/cht/app/pages/index.html#my_car.html";//  我的车子
    public static String chtUrlForCdcs=  BocSdkConfig.qztUrl + "/cht/app/pages/index.html#car_loan_calc.html";//  车贷测算
    public static String chtUrlForGjj = BocSdkConfig.qztUrl + "/gjt/?userAgent=";// 公积金查询
    
    public static String ZQTphoto_1 = "https://wechat.bocichina.com/zygj_weixin/weixin/khyl/index_kmh.jsp?actid=81&from=singlemessage&isappinstalled=0";
    public static String ZQThaitong = "http://h5kh.htsec.com/indexnew/p/SxnkqW";
    public static String ZQTguosheng = "https://kh.gsstock.com/osoa/views/downloadPhone.html";
    public static String ZQTdongbei = "https://mkh.nesc.cn/p/99910002";
}
