package com.bocop.xfjr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.httptools.network.Executors;
import com.boc.jx.httptools.network.http.DConfing;
import com.boc.jx.httptools.network.util.SharedPreferencesUtil;
import com.bocop.xfjr.activity.XFJRLoginActivity;
import com.bocop.xfjr.adapter.recycleradapter.RecyclerViewHolder;
import com.bocop.xfjr.adapter.recycleradapter.RecyclerViewHolder.HolderImageLoader;
import com.bocop.xfjr.bean.TypeBean;
import com.bocop.xfjr.config.HttpRequest;
import com.bocop.xfjr.constant.XFJRConstant;
import com.bocop.xfjr.util.PreferencesUtil;
import com.bocop.xfjr.util.boc.BOCSPUtil;
import com.bocop.xfjr.util.image.ImageLoad;
import com.bocop.xfjr.util.image.loader.GlideLoader;
import com.bocop.xfjr.util.image.loader.ImageLoaderLoader;
import com.tencent.bugly.crashreport.CrashReport;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.widget.ImageView;
import dalvik.system.DexClassLoader;

/**
 * description： 消费金融模块的主入口， 为了方便后期将模块单独抽出来使用 所有需要在application中执行的算法，请在main中完成
 * <p/>
 * Created by TIAN FENG on 2017年8月25日 QQ：27674569 Email: 27674569@qq.com
 * Version：1.0
 */
public class XfjrMain {

	public static Application mApp;
	public static boolean isTest = false; //是否开始测试数据
	public static boolean isSit = false; //isSit 用于检查session是不是变了
	public static boolean isNet = true; // 接口开启，否则展示静态数据
	public static boolean isSimulator = false; // 在BOC_JX_APP/src/com/bocop/xfjr/util/network/http/task/FunctionsTask.java 38行
	public static boolean isSkipUpdate = true; // 跳过强制更新，在package com.bocop.jxplatform.util.Update; 78行
	public static String businessId = "1"; // 业务id
	public static String businessStatus = "1"; // 业务状态：1(转人工或者预授信提交不通过)、3(待放款)，跳转到补充资料的时候需要赋值
	public static String role = XFJRConstant.C_ROLE; // 0是商户、 1是客户经理
	public static int reqSmsCodeTime = 120; // 重新请求验证码时间间隔
	public static List<TypeBean> TYPES = new ArrayList<>();
	public static List<TypeBean> M_TYPES = new ArrayList<>(); // 商户页面

	static {
		M_TYPES.add(new TypeBean(XFJRConstant.M_STATUS_0_STR, XFJRConstant.M_STATUS_0_INT));
		M_TYPES.add(new TypeBean(XFJRConstant.M_STATUS_1_STR, XFJRConstant.M_STATUS_1_INT));
		M_TYPES.add(new TypeBean(XFJRConstant.M_STATUS_2_STR, XFJRConstant.M_STATUS_2_INT));
		TYPES.add(new TypeBean(XFJRConstant.C_STATUS_0_STR, XFJRConstant.C_STATUS_0_INT));
		TYPES.add(new TypeBean(XFJRConstant.C_STATUS_1_STR, XFJRConstant.C_STATUS_1_INT));
		TYPES.add(new TypeBean(XFJRConstant.C_STATUS_2_STR, XFJRConstant.C_STATUS_2_INT));
		TYPES.add(new TypeBean(XFJRConstant.C_STATUS_3_STR, XFJRConstant.C_STATUS_3_INT));
		TYPES.add(new TypeBean(XFJRConstant.C_STATUS_4_STR, XFJRConstant.C_STATUS_4_INT));
		TYPES.add(new TypeBean(XFJRConstant.C_STATUS_5_STR, XFJRConstant.C_STATUS_5_INT));
	}

//	static{
//		// 网络请求超时设置 单位秒
//		HttpTimeOuts.CONNECT_TIMEOUT = 30;
//		HttpTimeOuts.READ_TIMEOUT = 180;
//		HttpTimeOuts.WRITE_TIMEOUT = 180;
//	}
	
	public static void main(Application app){
		// 分包工具
		// MultiDex.install(app);
		// dexTool(app);
		mApp = app;
		//CrashReport.initCrashReport(app, "02fe88c627", true); // 腾讯bugly-->移到startXFJR()去了
		/**
		 * 图片加载框架，使用老项目中的imageLoader进行封装 换库请实现IImgLoader接口并实现逻辑
		 */
		ImageLoaderLoader.initImageLoader(app, "cache/");
		ImageLoad.init(new GlideLoader());
		/**
		 * 适配器图片加载使用框架
		 */
		RecyclerViewHolder.setImageLoader(new HolderImageLoader() {

			@Override
			public void displayImage(Context context, ImageView imageView, String imagePath) {
				ImageLoad.loadImage(context, imagePath, imageView);

			}
		});
		/**
		 * SharedPreferences
		 */
		PreferencesUtil.init(app, XFJRConstant.XFJR_SP_NAME);
		BOCSPUtil.initBOCSPUtil(app);
		/**
		 * 全局异常捕获
		 */
		// CarshExceptionHandler.getInstance().bind(mApp);
		//HttpRequest.initHttp(app); // -->移到startXFJR()去了
	}

	/**
	 * 获取CPU型号
	 * 
	 * @return
	 */
	public static String getCpuName() {
		String arm = null;
	    try {  
	        InputStream is = new FileInputStream("/proc/cpuinfo");  
	        InputStreamReader ir = new InputStreamReader(is);  
	        BufferedReader br = new BufferedReader(ir);  
	        try {  
	            String nameProcessor = "Processor";  
	            String nameFeatures = "Features";  
	            String nameModel = "model name";  
	            String nameCpuFamily = "cpu family";  
	            while (true) {  
	                String line = br.readLine();  
	                String[] pair = null;  
	                if (line == null) {  
	                    break;  
	                }  
	                pair = line.split(":");  
	                if (pair.length != 2)  
	                    continue;  
	                String key = pair[0].trim();  
	                String val = pair[1].trim();  
	                if (key.compareTo(nameProcessor) == 0) {  
	                    String n = "";  
	                    for (int i = val.indexOf("ARMv") + 4; i < val.length(); i++) {  
	                        String temp = val.charAt(i) + "";  
	                        if (temp.matches("\\d")) {  
	                            n += temp;  
	                        } else {  
	                            break;  
	                        }  
	                    }  
	                    arm = "ARM";  
	                    continue;  
	                }
	                if (key.compareToIgnoreCase(nameModel) == 0) {  
	                    if (val.contains("Intel")) {  
	                    	arm = "INTEL";  
	                    }  
	                    continue;  
	                }   
	            }  
	        } finally {  
	            br.close();  
	            ir.close();  
	            is.close();  
	        }  
	    } catch (Exception e) {  
	        e.printStackTrace();  
	    }  
	  
	    return arm;

	}

	/**
	 * 清空SharedPreferences
	 */
	public static void clearAllSP() {
		PreferencesUtil.clear();
		SharedPreferencesUtil.getInstance().delete(DConfing.SP_NAME);
	}

	/**
	 * login页面直接跳转intent 这里拦截方便查找
	 * 
	 * @param intent
	 *            InformalLoginActivity 里面signup()函数
	 * @return
	 */
	public static Intent startXfjrMainActivity(Intent intent) {
		// intent = new Intent(mApp,XFJRMyApplicationActivity.class);
		// intent = new Intent(mApp,XFJRIndexActivity.class);
		return intent;
	}

	/**
	 * 外部调用消费进入入口
	 * 
	 * @param context
	 */
	public static void startXFJR(final BaseActivity context) {
		CrashReport.initCrashReport(context.getApplication(), "02fe88c627", true); // 腾讯bugly
		HttpRequest.initHttp(context.getApplication());
		Intent intent = new Intent(context, XFJRLoginActivity.class);
		intent.putExtra(XFJRConstant.KEY_IS_FIRST_LOGIN, true);
		context.startActivity(intent);
	}

	public static void gotoLogin(Context context) {
		Executors.getInstance().clearDynamicData();
		Intent intent = new Intent(context, XFJRLoginActivity.class);
		context.startActivity(intent);
	}

	@SuppressLint({ "NewApi", "SdCardPath" })
	private static void dexTool(Application app) {
		File dexDir = new File(app.getFilesDir(), "dlibs");
		dexDir.mkdir();
		File dexFile = new File(dexDir, "libs.apk");
		File dexOpt = app.getCacheDir();
		try {
			InputStream ins = app.getAssets().open("libs.apk");
			if (dexFile.length() != ins.available()) {
				FileOutputStream fos = new FileOutputStream(dexFile);
				byte[] buf = new byte[4096];
				int l;
				while ((l = ins.read(buf)) != -1) {
					fos.write(buf, 0, l);
				}
				fos.close();
			}
			ins.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		ClassLoader cl = app.getClassLoader();
		ApplicationInfo ai = app.getApplicationInfo();
		String nativeLibraryDir = null;
		if (Build.VERSION.SDK_INT > 8) {
			nativeLibraryDir = ai.nativeLibraryDir;
		} else {
			nativeLibraryDir = "/data/data/" + ai.packageName + "/lib/";
		}
		DexClassLoader dcl = new DexClassLoader(dexFile.getAbsolutePath(), dexOpt.getAbsolutePath(), nativeLibraryDir,
				cl.getParent());

		try {
			Field f = ClassLoader.class.getDeclaredField("parent");
			f.setAccessible(true);
			f.set(cl, dcl);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
