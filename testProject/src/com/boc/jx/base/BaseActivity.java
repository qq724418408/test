package com.boc.jx.base;

import java.util.List;

import com.boc.jx.baseUtil.cache.CacheBean;
import com.boc.jx.baseUtil.net.Action;
import com.boc.jx.baseUtil.net.RetCode;
import com.boc.jx.baseUtil.net.UiObject;
import com.boc.jx.baseUtil.view.ViewUtils;
//import com.boc.jx.constants.Constants;
import com.boc.jx.tools.LogUtils;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by user on 14-9-11.
 * <p>activity基类，</p>
 */
public class BaseActivity extends FragmentActivity implements UiObject{
    private ActivityManager activityManager;
    private BaseApplication baseApp;
    private String Tag;

    protected CacheBean cacheBean;
//    protected LoginUserInfo userInfo;
    private OnFinishingListener listener;
    
    private int state = 0;

    public final static int CREATED = 0;
    public final static int ON_PAUSED = 1;
    public final static int ON_STARTED = 2;
    public final static int ON_STOPED = 3;
    public final static int ON_DISTORY = 4;
    public final static int ON_RESULT_BACK = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ViewUtils.inject(this);
        baseApp = (BaseApplication) getApplication();

        activityManager = baseApp.getActivityManager();
        if (!addActivity()) {
            activityManager.add(this);
        }
        cacheBean = CacheBean.getInstance();
		if (savedInstanceState != null) {
			UserInfo userInfo = (UserInfo) savedInstanceState.getSerializable("userInfo");
			if (cacheBean != null && userInfo != null && userInfo.getList() != null
					&& userInfo.getList().size() > 0) {
				cacheBean.setUserInfo(userInfo);
			}
		}
		
		
//        userInfo = baseApp.getUserInfo();
  
    }
    
    @Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		if (cacheBean.getUserInfo() != null && cacheBean.getUserInfo().getList() != null
				&& cacheBean.getUserInfo().getList().size() > 0) {
			outState.putSerializable("userInfo", cacheBean.getUserInfo());
		}
		super.onSaveInstanceState(outState);
    }

    public boolean addActivity() {
        return false;
    }

    //发送网络请求CSP
//    public requestCspLogin(){}
    /**
     * 跳转到指定activity
     */
    public void callMe(Class<? extends BaseActivity> meClass) {
        /*通用打开ACTIVITY方法*/
        callMe(meClass, null);
    }

    /**
     * 跳转到指定activity
     */
    public void callMe(Class<? extends BaseActivity> meClass, Bundle bundle) {
        /*通用打开ACTIVITY方法*/
        Intent intent = new Intent(this, meClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * 跳转到指定activity
     */
    public void callMeForBack(Class<? extends BaseActivity> meClass, int requestCode) {
        /*通用打开ACTIVITY方法*/
        callMeForBack(meClass, null, requestCode);
    }

    /**
     * 跳转到指定activity
     */
    public void callMeForBack(Class<? extends BaseActivity> meClass, Bundle bundle, int requestCode) {
        Intent intent = new Intent(this, meClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * <p>传递onActivityResult方法到fragment</p>
     * <ul>如继承activity中重写了此方法，如还需要传递此回调到activity所有的fragment中则需再调一次：</ul><br/>
     * super.onActivityResult(requestCode, resultCode, data);
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null && fragments.size() > 0) {
            for (Fragment fragment : fragments) {
            	if (fragment==null) {
					continue;
				}
//            	if (Build.VERSION.SDK_INT<14) {
            		fragment.onActivityResult(requestCode, resultCode, data);
//				}
            }
        }
    }

    /**
     * @return BaseApplication
     * @see com.boc.jx.base.BaseApplication
     */
    public BaseApplication getBaseApp() {
        return (BaseApplication) getApplication();
    }

    /**
     * @return CacheBean
     * @see com.boc.jx.baseUtil.cache.CacheBean
     */
    public CacheBean getCacheBean() {
        return cacheBean;
    }
    
    /**
     * @return CacheBean
     * @see com.boc.jx.baseUtil.cache.CacheBean
     */
//    public LoginUserInfo getUserInfo() {
//        return userInfo;
//    }

    /**
     * @return ActivityManager
     * @see com.boc.jx.base.ActivityManager
     */
    public ActivityManager getActivityManager() {
        return activityManager;
    }

    public BaseFragment replaceFragment(int containerId, BaseFragment fragment, boolean flag) {
        /*在指定容器中切换fragment*/
        if (fragment != null) {
            FragmentManager manager = getSupportFragmentManager();
            BaseFragment fragmentTag = null;
            if (flag) {
                fragmentTag = (BaseFragment) manager.findFragmentByTag(fragment.getClass().getName());
            }
            fragment = fragmentTag == null ? fragment : fragmentTag;
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.addToBackStack(fragment.getClass().getName());
            transaction.replace(containerId, fragment, fragment.getClass().getName());
            transaction.commit();
            return fragment;
        } else {
            LogUtils.i("添加fragment失败,fragment=" + fragment);
        }
        return null;
    }

    public BaseFragment replaceFragment(int containerId, BaseFragment fragment) {
        /*在指定容器中切换fragment*/
        return replaceFragment(containerId, fragment, true);
    }

    public BaseFragment replaceFragment(int containerId, Class<? extends BaseFragment> fragmentClass) {
        return replaceFragment(containerId, fragmentClass, true);
    }

    public BaseFragment replaceFragment(int containerId, Class<? extends BaseFragment> fragmentClass, boolean flag) {
        /*在指定容器中切换fragment*/
        try {
            BaseFragment fragment = null;
            FragmentManager manager = getSupportFragmentManager();
            if (flag) {
                fragment = (BaseFragment) manager.findFragmentByTag(fragmentClass.getName());
            }
            fragment = fragment == null ? fragmentClass.newInstance() : fragment;
            return replaceFragment(containerId, fragment, flag);
        } catch (InstantiationException e) {
            LogUtils.i(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            LogUtils.i(e.getMessage(), e);
        }
        return null;
    }


    public BaseFragment loadFragment(int containerId, BaseFragment fragment) {
        /*在指定容器中添加一个fragment*/
        if (fragment != null) {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(containerId, fragment, fragment.getClass().getName());
            transaction.commit();
            return fragment;
        } else {
            LogUtils.i("添加fragment失败,fragment=" + fragment);
        }
        return null;
    }

    public BaseFragment loadFragment(int containerId, Class<? extends BaseFragment> fragmentClass) {
        /*在指定容器中添加一个fragment*/
        try {
            BaseFragment baseFragment = fragmentClass.newInstance();
            return loadFragment(containerId, baseFragment);
        } catch (InstantiationException e) {
            LogUtils.i(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            LogUtils.i(e.getMessage(), e);
        }
        return null;
    }

    public String getTag() {
        return Tag;
    }

    public void setTag(String tag) {
        Tag = tag;
    }

    /**
     * 结束前监听
     */
    public void setOnFinishingListener(OnFinishingListener listener) {
        this.listener = listener;
    }

    public OnFinishingListener getOnFinishingListener() {
        return listener;
    }

    public interface OnFinishingListener {
        public boolean onFinishing();
    }

	@Override
	public void requestSuccess(RetCode retCode, Object response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requestFalied(RetCode retCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
    public void addAction(Action action) {
        actionList.add(action);
    }

    @Override
    public void resetUiOject() {
        for (Action action : actionList) {
            action.reset();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        state = ON_PAUSED;
    }

    @Override
    protected void onResume() {
        super.onResume();
        state = CREATED;
        
      //手势解锁
//        if (!Constants.handFlg) {
//        	Constants.handFlg = true;
//            if (!Constants.isActive) {
//                //app 从后台唤醒，进入前台
//            	Constants.isActive = true;                
//            }
//        } 

    }

    @Override
    protected void onStart() {
        super.onStart();
        state = ON_STARTED;
    }

    @Override
    protected void onStop() {
        super.onStop();
        state = ON_STOPED;
        
//        Constants.handFlg = false; //程序未解锁
//        Constants.isActive = false; //程序未解锁

    }

    @Override
    protected void onDestroy() {
        resetUiOject();
        state = ON_DISTORY;
        super.onDestroy();
    }

    public int getState() {
        return state;
    }

	
}
