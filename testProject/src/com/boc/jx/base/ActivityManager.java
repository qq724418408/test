package com.boc.jx.base;

import com.boc.jx.base.bean.AcitivtyBean;
import com.boc.jx.baseUtil.cache.CacheLinkedMap;
import com.bocop.jxplatform.activity.MainActivity;

import android.app.Activity;

/**
 * Created by hwt on 14-9-15.
 * <p/>
 * activity管理工具，提供结束指定activity方法，结束所有打开过的activity方法。
 */
public class ActivityManager {
    private static CacheLinkedMap<String, BaseActivity> activitys = new CacheLinkedMap<String, BaseActivity>(-1);

    /**
     * 添加activity到容器
     *
     * @param activity
     */
    public void add(BaseActivity activity) {
        activitys.put(activity.getClass().getName(), activity);
    }

    /**
     * 添加activity到容器
     *
     * @param activity
     */
    public void add(BaseActivity activity, String activityTag) {
        activitys.put(activityTag, activity);
    }

    /**
     * 从容器中拿出指定activity
     *
     * @param aClass
     * @return
     */
    public BaseActivity get(Class<? extends BaseActivity> aClass) {
        return activitys.get(aClass.getName());
    }

    /**
     * 从容器中拿出指定activity
     *
     * @return
     */
    public BaseActivity get(String activityTag) {
        return activitys.get(activityTag);
    }

    /**
     * 结束指定activity
     *
     * @param aClass 指定activity Class
     */
    public void finishAct(Class<? extends BaseActivity> aClass) {
        BaseActivity activity = activitys.removeCache(aClass.getName());
        if (activity != null) {
            activity.finish();
        }
    }

    /**
     * 结束指定activity
     */
    public void finishAct(String activityTag) {
        BaseActivity activity = activitys.removeCache(activityTag);
        if (activity != null) {
            activity.finish();
        }
    }

    /**
     * 删除最顶层activity
     */
    public void backFinish() {
        BaseActivity activity = activitys.getLast().getActivity();
        if (activity.getOnFinishingListener() != null) {
            if (!activity.getOnFinishingListener().onFinishing())
                return;
        }
        activitys.removeLast().getActivity().finish();
    }

    /**
     * 结束指定activity
     *
     * @param activity 指定activity
     */
    public void finishAct(BaseActivity activity) {
        finishAct(activity.getClass());
    }

    /**
     * 结束指定activity
     *
     * @param aClass     指定activity
     * @param resultCode 返回处理结果码
     */
    public void finishActForResult(Class<? extends BaseActivity> aClass, int resultCode) {
        BaseActivity activity = activitys.removeCache(aClass.getName());
        if (activity != null) {
            activity.setResult(resultCode);
            activity.finish();
        }
    }

    /**
     * 结束指定activity
     *
     * @param activity   指定activity
     * @param resultCode 返回处理结果码
     */
    public void finishActForResult(BaseActivity activity, int resultCode) {
        finishActForResult(activity.getClass(), resultCode);
    }

    /**
     * 结束容器中所有的activity
     */
    public void finishAll() {
        while (activitys.size() > 0) {
            AcitivtyBean bean = activitys.removeLast();
            bean.getActivity().finish();
  
        }
    }
    
    /**
     * 结束容器中除了MainActivity外的所有的activity
     */
    public void finishAllWithoutMain() {
        while (activitys.size() > 0) {
        	if(activitys.getLast().getActivity().getClass() != MainActivity.class){
        		activitys.removeLast().getActivity().finish();
        	}else{
        		break;
        	}
        }
    }
    
    /**
     * 结束容器中除了指定activity外的所有的activity
     */
    public void finishAllWithoutActivity(Class<? extends BaseActivity> aClass) {
    	while (activitys.size() > 0) {
    		if(activitys.getLast().getActivity().getClass() != aClass){
    			activitys.removeLast().getActivity().finish();
    		}else{
    			break;
    		}
    	}
    }
    
    /**
     * 结束容器中除了指定activity外的所有的activity
     */
    public void finishAllWithoutActivity(Class<? extends BaseActivity>... aClass) {
    	if (aClass==null) {
			throw new NullPointerException("aClass is null");
		}
    	while (activitys.size() > 0) {
    		for(Class<? extends BaseActivity> clazz:aClass){
        		if(activitys.getLast().getActivity().getClass() != clazz){
        			activitys.removeLast().getActivity().finish();
        		}else{
        			break;
        		}
    		}
    	}
    }
}
