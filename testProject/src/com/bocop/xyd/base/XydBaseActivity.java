package com.bocop.xyd.base;

import com.boc.jx.baseUtil.view.ViewUtils;
import com.bocop.yfx.utils.ToastUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

/**
 * description： 基类
 */
public abstract class XydBaseActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getLoyoutId()!=-1) {
			setContentView(getLoyoutId());
		}
		ViewUtils.inject(this);
		initView();
		initData();
	}

	protected abstract int getLoyoutId();

	
	protected abstract void initView();
	
	protected abstract void initData();
	
	
	public void showToast(String content) {
		ToastUtils.show(this, content, Toast.LENGTH_SHORT);
	}
	/**
     * 跳转到指定activity
     */
    public void callMe(Class<? extends Activity> meClass) {
        /*通用打开ACTIVITY方法*/
        callMe(meClass, null);
    }

    /**
     * 跳转到指定activity
     */
    public void callMe(Class<? extends Activity> meClass, Bundle bundle) {
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
    public void callMeForBack(Class<? extends Activity> meClass, int requestCode) {
        /*通用打开ACTIVITY方法*/
        callMeForBack(meClass, null, requestCode);
    }

    /**
     * 跳转到指定activity
     */
    public void callMeForBack(Class<? extends Activity> meClass, Bundle bundle, int requestCode) {
        Intent intent = new Intent(this, meClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }
}
