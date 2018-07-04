package com.bocop.xyd;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.httptools.http.HttpTimeOuts;
import com.bocop.xyd.adapter.recycleradapter.RecyclerViewHolder;
import com.bocop.xyd.adapter.recycleradapter.RecyclerViewHolder.HolderImageLoader;
import com.bocop.xyd.util.PreferencesUtil;
import com.bocop.xyd.util.image.ImageLoad;
import com.bocop.xyd.util.image.loader.ImageLoaderLoader;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;

/**
 * description： 消费金融模块的主入口，
 * 				为了方便后期将模块单独抽出来使用
 * 				所有需要在application中执行的算法，请在main中完成
 * <p/>
 * Created by TIAN FENG on 2017年8月25日
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */
public class XydMain {
	
	public static Application mApp;
	public static boolean isNet = true; // 接口开启，否则展示静态数据
	public static boolean isSimulator = false; // 在BOC_JX_APP/src/com/bocop/xfjr/util/network/http/task/FunctionsTask.java 38行
	public static boolean isSkipUpdate = true; // 跳过强制更新，在package com.bocop.jxplatform.util.Update; 78行

	static{
		// 网络请求超时设置 单位秒
		HttpTimeOuts.CONNECT_TIMEOUT = 60;
		HttpTimeOuts.READ_TIMEOUT = 180;
		HttpTimeOuts.WRITE_TIMEOUT = 180;
	}
	
	public static void main(Application app){
		mApp = app;
		
		/**
		 * 图片加载框架，使用老项目中的imageLoader进行封装
		 * 换库请实现IImgLoader接口并实现逻辑
		 */
		ImageLoaderLoader.initImageLoader(app, "cache/");
		ImageLoad.init(new ImageLoaderLoader());
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
		PreferencesUtil.init(app, "jx_xyd");
		
		/**
		 * 全局异常捕获
		 */
//		CarshExceptionHandler.getInstance().bind(mApp);
		
//		HttpRequest.initHttp(app);
	}
	
	/**
	 * login页面直接跳转intent 这里拦截方便查找
	 * @param intent   InformalLoginActivity 里面signup()函数
	 * @return  
	 */
	public static Intent startXydMainActivity(Intent intent){
//		intent=new Intent(mApp,Xyd_IndexActivity.class);
		return intent;
	}
	
	/**
	 * 外部调用消费进入入口
	 * 
	 * @param context
	 */
	public static void startXYD(final BaseActivity context) {
		
	}
}
