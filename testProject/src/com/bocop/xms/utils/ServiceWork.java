/**
 * 
 */
package com.bocop.xms.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

/** 
 * @author luoyang  E-mail: luoyang8714@163.com
 * @version 创建时间：2016-12-19 上午11:31:38 
 * 类说明 
 */
/**
 * @author zhongye
 *
 */
public class ServiceWork {

	/** 
	 * 判断某个服务是否正在运行的方法 
	 *  
	 * @param mContext 
	 * @param serviceName 
	 *            是包名+服务的类名（例如：net.loonggg.testbackstage.TestService） 
	 * @return true代表正在运行，false代表服务没有正在运行 
	 */  
	public static boolean  isServiceWork(Context mContext, String serviceName) {  
	    boolean isWork = false;  
	    ActivityManager myAM = (ActivityManager) mContext  
	            .getSystemService(Context.ACTIVITY_SERVICE);  
	    List<RunningServiceInfo> myList = myAM.getRunningServices(40);  
	    if (myList.size() <= 0) {  
	        return false;  
	    }  
	    for (int i = 0; i < myList.size(); i++) {  
	        String mName = myList.get(i).service.getClassName().toString();  
	        if (mName.equals(serviceName)) {  
	            isWork = true;  
	            break;  
	        }  
	    }  
	    return isWork;  
	}  

}
