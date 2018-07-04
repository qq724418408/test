package com.bocop.xfjr.config;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.boc.jx.httptools.http.HttpUtils;
import com.boc.jx.httptools.http.callback.IHttpCallback;
import com.boc.jx.httptools.http.callback.IStringCallaBack;
import com.boc.jx.httptools.http.decortor.DialogDecorter;
import com.boc.jx.httptools.http.decortor.LoginDecortor;
import com.boc.jx.httptools.http.engin.okhttp2engin.OkHttp2Engin;
import com.boc.jx.httptools.network.DDecortor;
import com.boc.jx.httptools.network.Executors;
import com.boc.jx.httptools.network.http.DConfing;
import com.boc.jx.httptools.network.util.AppInfoUtils;
import com.boc.jx.httptools.network.util.LogUtil;
import com.boc.jx.httptools.network.util.SharedPreferencesUtil;
import com.boc.jx.tools.LogUtils;
import com.bocop.xfjr.XfjrMain;
import com.bocop.xfjr.argument.ArgumentUtil;
import com.bocop.xfjr.bean.BusinessBean;
import com.bocop.xfjr.bean.FaceCheckBean;
import com.bocop.xfjr.bean.FaceIdCardBean;
import com.bocop.xfjr.bean.MyBusinessBean;
import com.bocop.xfjr.bean.SystemBasicInfo;
import com.bocop.xfjr.bean.UserInfoBean;
import com.bocop.xfjr.bean.add.AddBusinessParamBean;
import com.bocop.xfjr.bean.add.ChannelBean;
import com.bocop.xfjr.bean.add.MerchantBean;
import com.bocop.xfjr.bean.add.ProductBean;
import com.bocop.xfjr.bean.detail.BusinessBasicInfoBean;
import com.bocop.xfjr.bean.detail.DetectedInfoBean;
import com.bocop.xfjr.bean.login.LoginBean;
import com.bocop.xfjr.bean.pretrial.PretrialParamBean;
import com.bocop.xfjr.bean.pretrial.PretrialResultBean;
import com.bocop.xfjr.helper.index.callback.UserInfoBeanCallback;
import com.bocop.xfjr.util.boc.BOCSPUtil;
import com.bocop.xfjr.util.json.PretrialJSONParam;
import com.google.gson.Gson;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;

/**
 * description： 网络请求相关类
 * <p/>
 * Created by TIAN FENG on 2017年9月7日 QQ：27674569 Email: 27674569@qq.com
 * Version：1.0
 */
public class HttpRequest {

	/**
	 * 调用okhttp2进行网络请求 原项目已经包含了post，get请求，如不需要可以将，post和get注释掉 上传下载包含进度显示
	 * 如后续需要升级或替换网络库请实现IHttpEngin接口 将new OkHttp2Engin() 替换为对应封装类，其他调用部分逻辑不变
	 * 如升级okhttp3请将okhttp3代码注释部分放开new OkHttp2Engin()改为 new
	 * OkHttp3Engin()，并将okhttp3的jar包或者依赖库配置成功
	 */
	@SuppressWarnings("deprecation")
	public static void initHttp(Application app) {
		com.boc.jx.httptools.network.http.HttpUtils.init(new OkHttp2Engin());
		SharedPreferencesUtil.getInstance().init(app, DConfing.SP_NAME);
		Executors.init(app)
				.server(UrlConfig.BASE_SERVER, // 服务器
						UrlConfig.SERVER_ROOT_PATH)
				.addParam("deviceType", "1")// 添加需要的参数
				.addParam("deviceId", AppInfoUtils.getDeviceId(app))
				.addParam("systemVersion", AppInfoUtils.getSystemVersion())
				.addParam("appVersion", AppInfoUtils.getVersion(app))
				// .addParam("deviceToken", token == null ? "" : token)
				.exceptionCode("61001")// token 过期
				// 添加异常返回码，出现对应的异常返回码后，下次请求会刷新动态流程
				.exceptionCode("61002")// 单设备登录
				.autoLogOut(UrlConfig.ACTION_ID_028)// 自动登出（发送登出接口，该接口不受任何限制）
				.autoLogin(UrlConfig.ACCESST_TOKEN_LOGIN)// token 自动登录
				.loginState(false)// 初始化设置登录状态为false
				.otherCode("10009")// 其他返回码 不会刷新动态流程，单单显示失败页面
				// .otherCode("10001")
				.build();// .exceptionCode("10009");
//		HttpUtils.initHttpEngin(new DErrorDecortor(new DDecortor()));
		HttpUtils.initHttpEngin(new DDecortor());
		// HttpUtils.initHttpEngin(new ContentDecorter(new OkHttp2Engin()));
	}

	public static void reqSmsCode(Context context, String telephone, IHttpCallback<String> callback) {
		if (BOCSPUtil.isLogin(context)) { //判断易惠通是否登录
			HttpUtils.with(context).url(UrlConfig.REQ_LOGIN_CODE)
			.addParams("busiNo", XfjrMain.businessId)
			.addParams("userId",BOCSPUtil.getUserId())
			.addParams("actoken",BOCSPUtil.getActoken())
			.addParams("client","A")
			.addDecortor(new DialogDecorter())
			.addParams("telephone", telephone)
			.post(callback);
		} else {
			BOCSPUtil.reLogin(context);
		}
	}
	
	/**
	 * API000101 登录获取短信验证码
	 * 
	 * @param context
	 * @param telephone
	 * @param callback
	 */
	public static void reqLoginCode(Context context, String telephone, IHttpCallback<String> callback) {
		if (BOCSPUtil.isLogin(context)) { //判断易惠通是否登录
			String userId = BOCSPUtil.getUserId();
			String actoken = BOCSPUtil.getActoken();
			HttpUtils.with(context).url(UrlConfig.REQ_LOGIN_CODE)
			.addParams("busiNo", "system")
			.addParams("telephone", telephone)
			.addParams("userId", userId)
			.addParams("actoken", actoken)
			.addParams("client", "A")
			.addDecortor(new DialogDecorter())
			.addDecortor(new LoginDecortor())
			.post(callback);
		} else {
			BOCSPUtil.reLogin(context);
		}
	}

	/**
	 * API000201 登录校验短信验证码
	 * 
	 * @param context
	 * @param telephone
	 * @param smscode
	 * @param callback
	 */
	public static void reqLogin(Context context, String telephone, String smscode,
			IHttpCallback<LoginBean> callback) {
		if (BOCSPUtil.isLogin(context)) { //判断易惠通是否登录
			String userId = BOCSPUtil.getUserId();
			String actoken = BOCSPUtil.getActoken();
			Map<String, Object> params = new HashMap<>();
			params.put("telephone", telephone);
			params.put("smscode", smscode);
			params.put("type", "1");// 用户类型 1：商户 2：客户经理
			HttpUtils.with(context).url(UrlConfig.REQ_LOGIN)
			.addParams("userId", userId)
			.addParams("actoken", actoken)
			.addParams("client", "A")
			.addDecortor(new DialogDecorter())
			.addDecortor(new LoginDecortor())
			.addParams(params)
			.post(callback);
		} else {
			BOCSPUtil.reLogin(context);
		}
	}

	/**
	 * 退出登录
	 * 
	 * @param context
	 */
	public static void signOutLogin(Context context) {
		HttpUtils.with(context).url(UrlConfig.REQ_LOGOUT).post(null);
	}

	/**
	 * 临时解决找不到APPID的错误
	 * 
	 * @param context
	 * @param isLoading
	 * @param callback
	 */
	public static void autoLogin(Context context, boolean isLoading, IHttpCallback<LoginBean> callback) {
		XfjrMain.gotoLogin(context);
//		ToastUtils.show(context, "动态流程异常，请重新登录", 0);
//		HttpUtils.with(context).url(UrlConfig.REQ_LOGIN)
//			.addParams("telephone", "17665477766")
//			.addDecortor(isLoading ? new DialogDecorter() : null)
//			.addParams("smscode", "123456")
//			.post(callback);
	}

	/**
	 * API020102 获取系统基本信息 (什么时候请求，假如请求失败怎么办，什么时候重新请求)
	 * 
	 * @param context
	 * @param isLoading
	 * @param callback
	 */
	public static void reqSysBasicInfo(Context context, boolean isLoading,
			IHttpCallback<SystemBasicInfo> callback) {
		if (XfjrMain.role.equals("1")) {
			HttpUtils.with(context).url(UrlConfig.REQ_SYS_BASIC_INFO)
					.addDecortor(isLoading ? new DialogDecorter() : null)
					.post(callback);
		}
	}

	/**
	 * API020301 新增进件的时候获取商户列表
	 * 
	 * @param context
	 * @param params
	 * @param callback
	 * @author wujunliu
	 */
	public static void reqMerchantInfo(Context context,
			IHttpCallback<List<MerchantBean>> callback) {
		HttpUtils.with(context).url(UrlConfig.REQ_MERCHANT_LIST).addDecortor(new DialogDecorter())
				.post(callback);
	}

	/**
	 * API020301 新增进件的时候获取商户列表
	 * 
	 * @param context
	 * @param params
	 * @param callback
	 * @author wujunliu
	 */
	public static void reqChannelProductListInfo(Context context, String merchantId,
			IHttpCallback<List<ChannelBean>> callback) {
		HttpUtils.with(context).url(UrlConfig.REQ_CHANNEL_PRODUCT_LIST)
				.addDecortor(new DialogDecorter()).addParams("merchantId", merchantId)
				.post(callback);
	}

	public static void reqChannelProductListInfo2(Context context, String merchantId,
			IHttpCallback<MerchantBean> callback) {
		HttpUtils.with(context).url(UrlConfig.REQ_CHANNEL_PRODUCT_LIST)
				.addDecortor(new DialogDecorter()).addParams("merchantId", merchantId)
				.post(callback);
	}

	/**
	 * API010205 商户业务查询
	 * 
	 * @param context
	 * @param params
	 * @param callback
	 * @author wujunliu
	 */
	public static <T> void queryMerchantBusiness(Context context, int page,
			Map<String, Object> params, IHttpCallback<T> callback) {
		params.put("page", page);
		LogUtils.e("page " + page);
		HttpUtils.with(context).url(UrlConfig.M_QUERY_BUSINESS)
				.addDecortor(page == 0 ? new DialogDecorter() : null).addParams(params)
				.post(callback);
	}

	/**
	 * 014 新增进件
	 * 
	 * @param context
	 * @param params
	 * @param callback
	 * @author wujunliu
	 */
	public static void addBussiness(Context context, AddBusinessParamBean params,
			IHttpCallback<BusinessBean> callback) {
		HttpUtils.with(context).url(UrlConfig.ADD_BUSINESS).addParams("ehrId", params.getEhrId())
				.addParams("merchantId", params.getMerchantId())
				.addParams("channelId", params.getChannelId())
				.addParams("productId", params.getProductId())
				.addParams("customerName", params.getCustomerName())
				.addParams("telephone", params.getTelephone())
				.addParams("totalMoney", params.getTotalMoney())
				.addParams("applyMoney", params.getApplyMoney())
				.addParams("periodsId", params.getPeriodsId()).addParams("rate", params.getRate())
				.addDecortor(new DialogDecorter()).post(callback);
	}

	/**
	 * NO.010 补充资料
	 * 
	 * @param context
	 * @param params
	 * @param callback
	 * @author wujunliu
	 */
	public static void addData(Context context, Map<String, Object> params,
			IHttpCallback<String> callback) {
		HttpUtils.with(context).url(UrlConfig.ADD_DATA).addDecortor(new DialogDecorter())// 提交需要显示dialog
				.addParams("businessId", XfjrMain.businessId).addParams(params).post(callback);
	}

	/**
	 * 
	 * NO.003 获取业务列表
	 * 
	 * @param context
	 * @param params
	 * @param callback
	 * @author wujunliu
	 */
	public static void reqBussinessList(Context context, int status,
			IHttpCallback<List<MyBusinessBean>> callback) {
		String url = UrlConfig.C_REQ_BUSINESS_LIST;
		if (XfjrMain.role.equals("0")) {
			url = UrlConfig.M_REQ_BUSINESS_LIST;
		}
		if (XfjrMain.role.equals("1")) {
			url = UrlConfig.C_REQ_BUSINESS_LIST;
		}
		HttpUtils.with(context).url(url).addParams("businessId", XfjrMain.businessId)
				.addParams("type", "" + status).addDecortor(new DialogDecorter()).post(callback);
	}

	/**
	 * NO.006 获取业务详情
	 * 
	 * @param context
	 * @param type
	 * @param params
	 * @param callback
	 * @author wujunliu
	 */
	public static void reqBussinessBasicInfo(Context context,
			IHttpCallback<BusinessBasicInfoBean> callback) {
		String url = UrlConfig.C_REQ_BUSINESS_DETAIL;
		if (XfjrMain.role.equals("0")) {
			url = UrlConfig.M_REQ_BUSINESS_DETAIL;
		}
		if (XfjrMain.role.equals("1")) {
			url = UrlConfig.C_REQ_BUSINESS_DETAIL;
		}
		HttpUtils.with(context).url(url).addParams("type", "0")
				.addParams("businessId", XfjrMain.businessId).addDecortor(new DialogDecorter())
				.post(callback);
	}

	/**
	 * NO.007 获取欺诈侦测信息（业务详情2）
	 * 
	 * @param context
	 * @param params
	 * @param callback
	 * @author wujunliu
	 */
	public static void reqDetectedInfo(Context context, IHttpCallback<DetectedInfoBean> callback) {
		String url = UrlConfig.C_REQ_BUSINESS_DETAIL;
		if (XfjrMain.role.equals("0")) {
			url = UrlConfig.M_REQ_BUSINESS_DETAIL;
		}
		if (XfjrMain.role.equals("1")) {
			url = UrlConfig.C_REQ_BUSINESS_DETAIL;
		}
		HttpUtils.with(context).url(url).addParams("type", "1")
				.addParams("businessId", XfjrMain.businessId).post(callback);
	}

	/**
	 * NO.008 获取风险预决策信息（业务详情3）
	 * 
	 * @param context
	 * @param params
	 * @param callback
	 * @author wujunliu
	 */
	public static <T> void reqAssessInfo(Context context, IHttpCallback<T> callback) {
		String url = UrlConfig.C_REQ_BUSINESS_DETAIL;
		if (XfjrMain.role.equals("0")) {
			url = UrlConfig.M_REQ_BUSINESS_DETAIL;
		}
		if (XfjrMain.role.equals("1")) {
			url = UrlConfig.C_REQ_BUSINESS_DETAIL;
		}
		HttpUtils.with(context).url(url).addParams("type", "2")
				.addParams("businessId", XfjrMain.businessId).post(callback);
	}

	/**
	 * NO.009 获取补充资料图片（业务详情4）
	 * 
	 * @param context
	 * @param params
	 * @param callback
	 * @author wujunliu
	 */
	public static <T> void reqMoreData(Context context, IHttpCallback<T> callback) {
		String url = UrlConfig.C_REQ_BUSINESS_DETAIL;
		if (XfjrMain.role.equals("0")) {
			url = UrlConfig.M_REQ_BUSINESS_DETAIL;
		}
		if (XfjrMain.role.equals("1")) {
			url = UrlConfig.C_REQ_BUSINESS_DETAIL;
		}
		HttpUtils.with(context).url(url).addParams("type", "3")
				.addParams("businessId", XfjrMain.businessId).post(callback);
	}

	/**
	 * 【API020206】 通过商户编号、渠道编号和产品编号获取欺诈侦测规则和授信模型
	 * 
	 * @param context
	 * @param params
	 * @param callback
	 * @author wujunliu
	 */
	public static void reqBusinessStatus(Context context, MyBusinessBean bean,
			IHttpCallback<ProductBean> callback) {
		HttpUtils.with(context).url(UrlConfig.REQ_BUSINESS_STATUS)
				.addParams("merchantId", bean.getMerchantId())
				.addParams("channelId", bean.getChannelId())
				.addParams("productId", bean.getProductId()).addDecortor(new DialogDecorter())
				.post(callback);
	}

	
	/**
	 * 【API020206】 通过商户编号、渠道编号和产品编号获取欺诈侦测规则和授信模型
	 * 
	 * @param context
	 * @param params
	 * @param callback
	 * @author wujunliu
	 */
	public static void reqBusinessStatus(Context context, 
			String merchantId,
			String channelId,
			String productId,
			IHttpCallback<ProductBean> callback) {
		HttpUtils.with(context).url(UrlConfig.REQ_BUSINESS_STATUS)
				.addParams("merchantId", merchantId)
				.addParams("channelId", channelId)
				.addParams("productId", productId).addDecortor(new DialogDecorter())
				.post(callback);
	}
	
	
	
	/**
	 * 获取用户信息
	 * 
	 * @param context
	 * @param type
	 * @param callback
	 */
	public static <T> void reqUserInfo(Context context, final UserInfoBeanCallback callback) {
		HttpUtils.with(context).url(UrlConfig.REQ_INDEX_CUSTOMER_LIST)
				.post(new IHttpCallback<UserInfoBean>() {
					@Override
					public void onSuccess(String url, UserInfoBean result) {
						callback.onSuccess(url, result);
					}

					@Override
					public void onError(String url, Throwable e) {
						LogUtils.e("369 Error " + e);
					}

					@Override
					public void onFinal(String url) {

					}
				});
	}

	/**
	 * 获取业务数量
	 * 
	 * @param context
	 * @param callback
	 * @param boo
	 */
	public static <T> void reqBusinessInfo(Context context, IHttpCallback<T> callback,
			boolean boo) {
		HttpUtils.with(context).url(UrlConfig.REQ_INDEX_BUSINESSINFO)
				.addDecortor(boo ? new DialogDecorter() : null).post(callback);
	}

	/**
	 * 商户页面获取列表
	 * 
	 * @param context
	 * @param callback
	 */
	public static <T> void reqMerchantData(Context context, IHttpCallback<T> callback) {
		HttpUtils.with(context).url(UrlConfig.REQ_INDEX_BUSINESS_NUM).post(callback);
	}

	public static <T> void reqQueryList(Context context, IHttpCallback<T> callback) {

		HttpUtils.with(context).url(UrlConfig.REQ_INDEX_BUSINESSINFO)
				.addDecortor(new DialogDecorter()).post(callback);
	}

	/**
	 * 提交授权信息
	 * 
	 * @param context
	 * @param params
	 * @param callback
	 */
	public static <T> void sendWarrantMessage(Context context, Map<String, Object> params,
			IHttpCallback<T> callback) {
		params.put("businessId", XfjrMain.businessId);
		HttpUtils.with(context).url(UrlConfig.WARRANT_SEND_URL).addDecortor(new DialogDecorter())// 提交需要显示dialog
				.addParams(params).post(callback);
	}

	/**
	 * 人行征信
	 * 
	 * @param context
	 * @param callback
	 */
	public static <T> void checkRisk(Context context, IHttpCallback<T> callback) {
		if (BOCSPUtil.isLogin(context)) { //判断易惠通是否登录
			String userId = BOCSPUtil.getUserId();
			String actoken = BOCSPUtil.getActoken();
			LogUtils.e(userId + "--->>>" + actoken);
			HttpUtils.with(context).url(UrlConfig.CHECK_RISK_URL).addDecortor(new DialogDecorter())
			.addParams("businessId", XfjrMain.businessId)
			.addParams("userId", userId)
			.addParams("actoken", actoken)
			.addParams("client", "A")
			.post(callback);
		} else {
			BOCSPUtil.reLogin(context);
		}
	}

	/**
	 * 第三方征信
	 * 
	 * @param context
	 * @param callback
	 */
	public static <T> void checkOther(Context context,
			String creditCardNumber,// 银联卡号
			String telephone,// 手机号码
			String customerName,//客户姓名
			String idCard,//身份证号码
			IHttpCallback<T> callback) {
//		String userId = BOCSPUtil.getUserId();
//		String actoken = BOCSPUtil.getActoken();
		HttpUtils.with(context).url(UrlConfig.CHECK_THREE_PART).addDecortor(new DialogDecorter())
				.addParams("businessId", XfjrMain.businessId)
				.addParams("creditCardNumber", creditCardNumber.replace(" ", ""))
				.addParams("telephone",telephone)
				.addParams("customerName",  XfjrMain.isTest?"name":customerName)
				.addParams("idCard",XfjrMain.isTest&&TextUtils.isEmpty(idCard)?"432524196211023521":idCard)
				.post(callback);
	}

	/**
	 * 银行卡验证
	 * 
	 * @param context
	 * @param callback
	 */
	public static void checkBankCard(Context context, 
			boolean isHaveCard,
			String bankCard, 
			String telephone,
			String checkNum, 
			String customerName,
			String idCard,
			IHttpCallback<String> callback) {
		if (BOCSPUtil.isLogin(context)) { //判断易惠通是否登录
			HttpUtils.with(context).url(UrlConfig.CHECK_BANK_CARD_URL).addDecortor(new DialogDecorter())
			.addParams("businessId", XfjrMain.businessId)
			.addParams("isHaveCard", isHaveCard?"Y":"N")
			.addParams("creditCardNumber", XfjrMain.isTest?"6581261292626161256":bankCard.replace(" ", "")).addParams("telephone", telephone)
			.addParams("verificationCode", checkNum)
			.addParams("customerName", XfjrMain.isTest?"name":customerName)
			.addParams("idCard", XfjrMain.isTest&&TextUtils.isEmpty(idCard)?"432524196211023521":idCard)
			.addParams("userId",BOCSPUtil.getUserId())
			.addParams("actoken",BOCSPUtil.getActoken())
			.addParams("client","A")
			.post(callback);
		} else {
			BOCSPUtil.reLogin(context);
		}
	}

	/**
	 * 搜索业务
	 * 
	 * @param context
	 * @param page
	 *            页码
	 * @param isManager
	 *            是否是商户
	 * @param callback
	 */
	public static <T> void getSearchResult(boolean isShowDialog, Context context, String keyWord,
			int page, boolean isManager, IHttpCallback<T> callback) {
		Map<String, Object> paramsMap = new HashMap<>();
		if (!TextUtils.isEmpty(keyWord)) {
			paramsMap.put("keyword", keyWord);
		}
		paramsMap.put("page", page);
		HttpUtils.with(context)
				.url(isManager ? UrlConfig.REQ_INDEX_MANAGER_QUERYINFO : UrlConfig.M_QUERY_BUSINESS)
				.addParams(paramsMap).addDecortor(isShowDialog ? new DialogDecorter() : null)
				.post(callback);
	}

	/**
	 * 搜索结果详情
	 * 
	 * @param context
	 * @param bid
	 * @param callback
	 */
	public static <T> void getSearchDetail(Context context, boolean isManager, String businessId,
			IHttpCallback<T> callback) {
		HttpUtils.with(context)
				.url(isManager ? UrlConfig.REQ_QUERY_DETAILINFO : UrlConfig.M_REQ_BUSINESS_DETAIL)
				.addParams("businessId", businessId).addDecortor(new DialogDecorter())
				.post(callback);
	}

	public static <T> void reqPretrialInfo(Context context, String id, IHttpCallback<T> callback) {
		HttpUtils.with(context).url(UrlConfig.REQ_ASSESS_INFO).addParams("id", id)
				.addDecortor(new DialogDecorter()).post(callback);
	}

	/**
	 * 欺诈侦测 -身份证检测
	 * 
	 * @param contxt
	 *            上下文
	 * @param imageFile
	 *            图片
	 * @param callback
	 *            回调
	 */
	public static void getIdCardByNetWork(Context context, File imageFile,
			final IHttpCallback<FaceIdCardBean> callback) {
		DialogDecorter decorter = new DialogDecorter();
		final Dialog dialog = decorter.getDialog(context, FaceConfig.GET_FICE_ID_URL);
		dialog.show();
		Map<String, Object> params = new HashMap<>();
		params.put("api_key", FaceConfig.appKey);
		params.put("api_secret", FaceConfig.appSecret);
		params.put("image", imageFile);
		params.put("key___", false);
		OkHttp2Engin engin = new OkHttp2Engin();
		engin.post(context, FaceConfig.GET_FICE_ID_URL, params, new IStringCallaBack() {

			@Override
			public void onSuccess(String url, String result) {
				if (result.contains("error_message")) {
					String errorMessgae;
					try {
						JSONObject jsonObject = new JSONObject(result);
						errorMessgae = jsonObject.optString("error_message");
					} catch (JSONException e) {
						e.printStackTrace();
						errorMessgae = "json数据解析异常";
					}
					callback.onError(url, new Throwable(errorMessgae));
				} else {
					callback.onSuccess(url, new Gson().fromJson(result, FaceIdCardBean.class));
				}
			}

			@Override
			public void onFinal(String url) {
				callback.onFinal(url);
				dialog.dismiss();
			}

			@Override
			public void onError(String url, Throwable e) {
				callback.onError(url, e);
			}
		});
	}

	/**
	 * 欺诈侦测 - 有源对比
	 * 
	 * @param contxt
	 *            上下文
	 * @param imageFile
	 *            图片
	 * @param callback
	 *            回调
	 */
	public static void getIdUserInfoByNetWork(Context context, 
			String delta, 
			File imageFile,
			String name,
			String idCard,
			final IHttpCallback<FaceCheckBean> callback) {
		DialogDecorter decorter = new DialogDecorter();
		final Dialog dialog = decorter.getDialog(context, FaceConfig.GET_FICE_ID_URL);
		dialog.show();
		Map<String, Object> params = new HashMap<>();
		params.put("api_key", FaceConfig.appKey);
		params.put("api_secret", FaceConfig.appSecret);
		params.put("comparison_type", "1");
		params.put("face_image_type", "meglive");// 后续根据需求更改需要的参数
		params.put("idcard_name", name);
		params.put("idcard_number", idCard);
		params.put("delta", delta);
		params.put("image_best", imageFile);
		params.put("key___", false);
		OkHttp2Engin engin = new OkHttp2Engin();
		engin.post(context, FaceConfig.GET_CONTRAST_URL, params, new IStringCallaBack() {

			@Override
			public void onSuccess(String url, String result) {
				LogUtil.e("json = " + result);
				if (result.contains("error_message")) {
					String errorMessgae;
					try {
						JSONObject jsonObject = new JSONObject(result);
						errorMessgae = jsonObject.optString("error_message");
					} catch (JSONException e) {
						e.printStackTrace();
						errorMessgae = "json数据解析异常";
					}
					callback.onError(url, new Throwable(errorMessgae));
				} else {
					callback.onSuccess(url, new Gson().fromJson(result, FaceCheckBean.class));
				}
			}

			@Override
			public void onFinal(String url) {
				callback.onFinal(url);
				dialog.dismiss();
			}

			@Override
			public void onError(String url, Throwable e) {
				callback.onError(url, e);
			}
		});
	}

	/**
	 * 欺诈侦测 -提交人脸识别信息
	 * 
	 * @param context
	 * @param similarity
	 *            相似度
	 * @param callback
	 */
	public static void submitFaceInfo(Context context, int similarity,File file,
			IHttpCallback<String> callback) {
		HttpUtils.with(context).addDecortor(new DialogDecorter()).url(UrlConfig.SUBMIT_FACE_INFO)
				.addParams("businessId", XfjrMain.businessId)
				.addParams("faceImage", file)
				.addParams("similarity", similarity + "").post(callback);

	}
	
	
	
	/**
	 * 结束申请
	 * @param shutDownLink 终止环节 
	 *  02：手机验证
	 *  03：人脸识别模块
	 *  04：人行征信风险验证模块
	 *  05：银联信用卡卡验证模块
	 *  06：第三方征信数据核验模块
	 */
	public static void over(Context context, String shutDownLink,
			final IHttpCallback<String> callback) {
		HttpUtils.with(context).addDecortor(new DialogDecorter()).url(UrlConfig.OVER)
				.addParams("businessId", XfjrMain.businessId)
				.addParams("shutDownLink", shutDownLink).post(new IHttpCallback<String>() {

					@Override
					public void onSuccess(String url, String result) {
						ArgumentUtil.get().post("已拒绝");
						callback.onSuccess(url, result);
					}

					@Override
					public void onError(String url, Throwable e) {
						callback.onError(url, e);
					}

					@Override
					public void onFinal(String url) {
						callback.onFinal(url);
					}
				});

	}


	/**
	 * API020312 欺诈侦测-验证结果异步查询、
	 * 
	 * @param type类型 0：第三方征信验证 1：人行征信风险验证 2：银联信用卡验证
	 * @param userId 
	 * @param actoken 
	 * @param callback 
	 */
	public static <T> void getAsyncResult(Context context, 
			String type,
			String userId,
			String actoken,
			IHttpCallback<T> callback) {
		HttpUtils http = HttpUtils.with(context).addDecortor(new DialogDecorter()).url(UrlConfig.ASYNC_RESULT)
				.addParams("businessId", XfjrMain.businessId)
				.addParams("type", type);
			if(!TextUtils.isEmpty(userId)){
				http.addParams("userId",userId);
			}
			if(!TextUtils.isEmpty(actoken)){
				http.addParams("actoken",actoken);
			}
			http.addParams("client","A");
		http.post(callback);

	}
	
	
	/**
	 * 【API020310】风险预决策-提交预决策
	 * 
	 * @param context
	 * @param params
	 * @param callback
	 * @author wujunliu
	 */
	public static void submitPretrialInfo(Context context, PretrialParamBean bean, File imgFile,
			IHttpCallback<PretrialResultBean> callback) {
		String informationJson = PretrialJSONParam.createJSONParam(bean);
		LogUtils.e("informationJson:" + informationJson);
		HttpUtils.with(context).url(UrlConfig.SUBMIT_PRETRIAL_INFO)
				.addParams("informationJson", informationJson).addParams("imgFile", imgFile)
				.addDecortor(new DialogDecorter()).post(callback);
	}
}
