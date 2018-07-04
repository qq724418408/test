package com.boc.jx.base;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.baidu.mapapi.SDKInitializer;
//import com.baidu.mapapi.BMapManager;
//import com.baidu.mapapi.MKGeneralListener;
//import com.baidu.mapapi.map.MKEvent;
import com.boc.jx.baseUtil.cache.CacheBean;
import com.boc.jx.constants.Constants;
import com.boc.jx.httpUnits.AndroidCommFactory;
import com.boc.jx.httpUnits.ConnCommMachine;
import com.boc.jx.httpUnits.NetworkUtils;
import com.boc.jx.tools.FileUtils;
import com.bocop.gopushlibrary.service.GoPushManage;
import com.bocop.jxplatform.activity.way.pattern.ScreenObserver;
import com.bocop.jxplatform.activity.way.pattern.ScreenObserver.observerScreenStateUpdateListener;
import com.bocop.jxplatform.activity.way.pattern.UnlockGesturePasswordActivity;
import com.bocop.jxplatform.activity.way.view.LockPatternUtils;
import com.bocop.jxplatform.config.BocSdkConfig;
import com.bocop.jxplatform.util.FormsUtil;
import com.bocop.jxplatform.util.LoginUtil;
import com.bocop.xfjr.XfjrMain;
import com.bocop.xms.service.AlarmServiceManager;
import com.bocop.xms.xml.message.MessageBean;
import com.bocop.xyd.XydMain;
import com.nostra13.universalimageloader.cache.disc.impl.LimitedAgeDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

/**
 * Created by hwt on 14-9-11.
 * <p/>
 * 应用Bean ,添加数据缓存管理，登录用户管理，activity管理功能。
 * @param <mLockPatternUtils>
 */

@SuppressLint("NewApi")
public class BaseApplication extends Application {
	private CacheBean cacheBean;// 缓存bean
	private ActivityManager activityManager;// activity管理工具
	private Map<Integer, BaseFragment> fragmentList;
	private UserInfo userInfo;
	private static BaseApplication baseApplication;
	private ImageLoader imageLoader;

	public HashMap<String, ConnCommMachine> hs;// 存储url和对应machine的hashmap
	private AndroidCommFactory factory;// 联网器工厂类
	private ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
	private final Lock r = rwl.readLock();
	private final Lock w = rwl.writeLock();
	private boolean netStat = false;// 当前网络状态
	private Handler handler;

	private boolean isDownload;
	public static String deviceId;
	public static GoPushManage goPushManage = null;
//	public BMapManager mBMapManager = null;
	public static final String strKey = "8BB7F0E5C9C77BD6B9B655DB928B74B6E2D838FD";
	public boolean m_bKeyRight = true;
	
	public LockPatternUtils mLockPatternUtils;

	//added by gengjunying 2016-04-01
	public int count = 0;
	public boolean isfront = true;

	private ScreenObserver mScreenObserver;	
	private List<MessageBean> msgData;
	
	// 网络状态监听广播
	private BroadcastReceiver connectionReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			setNetStat(NetworkUtils.isNetworkConnected(context));// 设置当前网络状态
		}
	};

	
	@SuppressLint("NewApi")
	@Override
	public void onCreate() {
		super.onCreate();
		//added by gengjunying 2016-04-01
		mLockPatternUtils = new LockPatternUtils(this);
		// 百度地图初始化
		SDKInitializer.initialize(this);
		
		/* 应用初始化 */
		baseApplication = this;
		cacheBean = CacheBean.getInstance();
		activityManager = new ActivityManager();
		fragmentList = new HashMap<Integer, BaseFragment>();
		FormsUtil.getDisplayMetrics(this);
		isDownload = false;
		// 网络状态
		factory = new AndroidCommFactory();
		setNetStat(NetworkUtils.isNetworkConnected(this));// 设置当前网络状态

		// 注册网络状态检测监听广播
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(connectionReceiver, intentFilter);

		// SharedPreferences spf = getSharedPreferences(
		// OnlineService.SECRETARY_MSG, Context.MODE_PRIVATE);
		// deviceId = spf.getString(BocSdkConfig.DEVICE_ID_KEY, "");
		// if (TextUtils.isEmpty(deviceId)) {
		// Editor editor = spf.edit();
		// editor.remove(BocSdkConfig.UUID_KEY);
		// editor.remove(BocSdkConfig.REGIST_DEVICE_KEY);
		// deviceId = new DeviceInfo(this).getDeviceId();
		// editor.putString(BocSdkConfig.DEVICE_ID_KEY, deviceId);
		// editor.commit();
		// }
		// LogUtils.d("deviceId :" + deviceId);

		imageLoader = initImageLoader(getApplicationContext());

//		// 推送管理类
		goPushManage = GoPushManage.getInstance(this.getApplicationContext());
		goPushManage.setDebugMode(true);
		try{
//			initEngineManager(this);
		}
		catch(Exception ex){
			Log.i("tag", ex.getMessage());
		}
		
		
		initScreenObserver();
		AlarmServiceManager.getInstance().startAlarmService(getApplicationContext());
		
		//-------------------------gengjunying
		registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {

			@Override
			public void onActivityCreated(Activity arg0, Bundle arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onActivityDestroyed(Activity activity) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onActivityPaused(Activity activity) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onActivityResumed(Activity activity) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onActivityStarted(Activity activity) {
				// TODO Auto-generated method stub
				if (!isApplicationBroughtToBackground(baseApplication.getBaseContext())) {
					if (!Constants.handFlg) {
						if (LoginUtil.isLog(baseApplication.getBaseContext())) {
							if (mLockPatternUtils.savedPatternExists(LoginUtil.getUserId(baseApplication.getBaseContext()))) {
								// 输入手势密码
								Intent intent = new Intent();
								intent.putExtra("wayid", "wayid");
								intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								intent.setClass(baseApplication.getBaseContext(),UnlockGesturePasswordActivity.class);
							
								startActivity(intent);							
							}
						}
					}
				}
			}

			@Override
			public void onActivityStopped(Activity activity) {
				// TODO Auto-generated method stub
				isApplicationBroughtToBackground(baseApplication.getBaseContext());
			}
		});
		//消费金融模块入口
		XfjrMain.main(this);
		//新易贷模块入口
		XydMain.main(this);
	}
	
	// ==============================判断app是否在前台或者后台========================================================================
	/**
	 * 判断当前应用程序处于前台还是后台
	 */
	public static boolean isApplicationBroughtToBackground(final Context context) {
		android.app.ActivityManager am = (android.app.ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasks = am.getRunningTasks(1);
		if (!tasks.isEmpty()) {
			ComponentName topActivity = tasks.get(0).topActivity;
			if (!topActivity.getPackageName().equals(context.getPackageName())) {
				Constants.handFlg = false;
				return true;
			}
		}
		return false;

	}

	// ===========================监听电源键是否按下（即屏幕是否点亮）========start=================================================================
	private void initScreenObserver() {
		mScreenObserver = new ScreenObserver(baseApplication.getBaseContext());
		mScreenObserver
				.observerScreenStateUpdate(new observerScreenStateUpdateListener() {
					@Override
					public void onScreenOn() {						
						System.out.println(" 按下电源键,屏幕现在打开 ");
						if(!isApplicationBroughtToBackground(baseApplication.getBaseContext())){
							if (LoginUtil.isLog(baseApplication.getBaseContext())) {
								if (mLockPatternUtils.savedPatternExists(LoginUtil.getUserId(baseApplication.getBaseContext()))) {			
									// 输入手势密码
									Intent intent = new Intent();
									intent.putExtra("wayid", "wayid");
									intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
									intent.setClass(
											baseApplication.getBaseContext(),
											UnlockGesturePasswordActivity.class);								
									startActivity(intent);		
									//Toast.makeText(baseApplication.getBaseContext(), "电源按下！", 0).show();
								}
							}
						}
					}

					@Override
					public void onScreenOff() {
						System.out.println(" 按下电源键,屏幕现在关闭 ");
					}
				});
	}

	// ==========================================================end=============================================================

//	public void initEngineManager(Context context) {
//		if (mBMapManager == null) {
//			mBMapManager = new BMapManager(context);
//		}
//		if (!mBMapManager.init(strKey, new MyGeneralListener())) {
//			Log.i("tag", "BMapManager  初始化错误!");
//			Toast.makeText(
//					BaseApplication.getInstance().getApplicationContext(),
//					"BMapManager  初始化错误!", Toast.LENGTH_LONG).show();
//		}
//	}

	/**
	 * 开启推送服务
	 * 
	 * @param userId
	 */
	public static void startGoPush(String userId) {
		if (goPushManage != null) {
			goPushManage.startPushService(userId);
			Log.i("tag", "goPushManage.startPushService" +  userId);
		}
	}

	// 常用事件监听，用来处理通常的网络错误，授权验证错误等
//	public static class MyGeneralListener implements MKGeneralListener {
//		@Override
//		public void onGetNetworkState(int iError) {
//			if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
//				Toast.makeText(
//						BaseApplication.getInstance().getApplicationContext(),
//						"您的网络出错啦！", Toast.LENGTH_LONG).show();
//			} else if (iError == MKEvent.ERROR_NETWORK_DATA) {
//				Toast.makeText(
//						BaseApplication.getInstance().getApplicationContext(),
//						"输入正确的检索条件！", Toast.LENGTH_LONG).show();
//			}
//			// ...
//		}
//
//		@Override
//		public void onGetPermissionState(int iError) {
//			if (iError == MKEvent.ERROR_PERMISSION_DENIED) {
//				// 授权Key错误：
//				Toast.makeText(
//						BaseApplication.getInstance().getApplicationContext(),
//						"请输入正确的授权Key！" + iError, Toast.LENGTH_LONG).show();
//				BaseApplication.getInstance().m_bKeyRight = false;
//			}
//		}
//	}

	/**
	 * 关闭推送服务
	 */
	public static void stopGoPush() {
		if (goPushManage != null) {
			goPushManage.stopPushService();
		}
	}

	// 获取一个联网器实例
	public ConnCommMachine getCommMachine() {
		if (factory != null) {
			return factory.CreateNewCommMachine(BocSdkConfig.COMM_TYPE);
		} else {
			factory = new AndroidCommFactory();
			return factory.CreateNewCommMachine(BocSdkConfig.COMM_TYPE);
		}
	}

	public void putUrl(String url, ConnCommMachine machine) {
		w.lock();
		try {
			hs.put(url, machine);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			w.unlock();
		}
	}

	public boolean isDownload() {
		return isDownload;
	}

	public void setDownload(boolean isDownload) {
		this.isDownload = isDownload;
	}

	// 通过url获取对应machine
	public ConnCommMachine getMachine(String url) {
		r.lock();
		ConnCommMachine machine = null;
		try {
			machine = hs.get(url);
			return machine;
		} catch (Exception e) {
			e.printStackTrace();
			return machine;
		} finally {
			r.unlock();
		}

	}

	public void clearAll() {
		w.lock();
		try {
			hs.clear();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			w.unlock();
		}
	}

	public void clearPoint(String url) {
		w.lock();
		try {
			hs.remove(url);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			w.unlock();
		}
	}

	public boolean isNetStat() {
		return netStat;
	}

	public void setNetStat(boolean netStat) {
		this.netStat = netStat;
	}

	private List<Map<String, String>> listmap = new CopyOnWriteArrayList<Map<String, String>>();

	public List<Map<String, String>> getListmap() {
		return listmap;
	}

	// 以上网络部分 于20150703LUOYANG添加

	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	public static BaseApplication getInstance() {
		return baseApplication;
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	/**
	 * 获得缓存工具
	 * 
	 * @return CacheBean
	 */
	public CacheBean getCacheBean() {
		return cacheBean;
	}

	/**
	 * 取得activity管理工具
	 * 
	 * @return activity管理工具
	 * @see com.boc.jx.base.ActivityManager
	 */
	public ActivityManager getActivityManager() {
		return activityManager;
	}

	/**
	 * 退出应用 退出应用
	 */
	public void exit() {
		exit(null);
	}

	/**
	 * 退出应用
	 * 
	 * @param exitAppListener
	 *            应用退出时监听，在应用完全退出前可进行额外操作。
	 */
	public void exit(OnExitAppListener exitAppListener) {
		// 应用退出监听
		if (exitAppListener != null)
			exitAppListener.onExit();
		// 清除应用内缓存
		// FileUtils.deleteFilesByDirectory(getCacheDir());
		// FileUtils.deleteFilesByDirectory(getFilesDir());
		// FileUtils.deleteFilesByDirectory(getExternalCacheDir());
		// 清除图片缓存
		// imageLoader.clearDiskCache();
		// 清除临时缓存，结束所有已打开activity
		cacheBean.clearCache();
		fragmentList.clear();
		if (msgData != null) {
			msgData.clear();
		}
		activityManager.finishAll();
		// 杀进程，关闭应用
		android.os.Process.killProcess(android.os.Process.myPid());
		android.app.ActivityManager activityMgr = (android.app.ActivityManager) getSystemService(ACTIVITY_SERVICE);
		activityMgr.restartPackage(getPackageName());
		System.exit(0);
		System.gc();
	}

	/**
	 * 应用退出时监听
	 */
	public interface OnExitAppListener {
		public void onExit();
	}

	public Map<Integer, BaseFragment> getFragmentList() {
		return fragmentList;
	}

	public void setFragmentList(Map<Integer, BaseFragment> fragmentList) {
		this.fragmentList = fragmentList;
	}

	/**
	 * 初始化ImageLoader工具
	 * 
	 * @param context
	 *            应用上下文
	 */
	public ImageLoader initImageLoader(Context context) {
		ImageLoaderConfiguration config = null;
		File cacheFile = FileUtils
				.getStorageDerectory(BaseConfig.IMAGE_CACHE_PATH);
		if (cacheFile == null) {
			config = new ImageLoaderConfiguration.Builder(context)
					.threadPriority(Thread.NORM_PRIORITY - 2)
					.denyCacheImageMultipleSizesInMemory()
					.diskCacheFileNameGenerator(new Md5FileNameGenerator())
					.tasksProcessingOrder(QueueProcessingType.LIFO)
					// .writeDebugLogs() // Remove for release app
					.build();
		} else {
			config = new ImageLoaderConfiguration.Builder(context)
					.threadPriority(Thread.NORM_PRIORITY - 2)
					.denyCacheImageMultipleSizesInMemory()
					.diskCacheFileNameGenerator(new Md5FileNameGenerator())
					.tasksProcessingOrder(QueueProcessingType.LIFO)
					// .writeDebugLogs()// Remove for release app
					.diskCache(new LimitedAgeDiskCache(cacheFile, 30 * 60))
					.diskCacheFileCount(100).build();
		}
		ImageLoader loader = ImageLoader.getInstance();
		loader.init(config);
		return loader;
	}

	public ImageLoader getImageLoader() {
		return imageLoader;
	}
	
	public LockPatternUtils getLockPatternUtils() {
        return mLockPatternUtils;
    }

	public List<MessageBean> getMsgData() {
		return msgData;
	}

	public void setMsgData(List<MessageBean> msgData) {
		this.msgData = msgData;
	}
	
}
