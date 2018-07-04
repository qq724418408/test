package com.bocop.xfjr.base;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.tools.LogUtils;
import com.bocop.xfjr.util.XFJRUtil;
import com.bocop.yfx.utils.ToastUtils;
import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

/**
 * description： 消费金融基类
 * <p/>
 * Created by TIAN FENG on 2017年8月30日
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */
public abstract class XfjrBaseActivity extends BaseActivity {

	protected String systemTime = XFJRUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"); // 系统时间
	protected String netWorkTime = XFJRUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"); // 网络时间
	
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				String netWorkDateString = (String) msg.obj;
				systemTime = XFJRUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss");
				LogUtils.e("系统时间：" + systemTime);
				LogUtils.e("网络时间：" + (netWorkTime = netWorkDateString));
				break;
			default:
				break;
			}
		}

	};

//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		if (getLoyoutId()!=-1) {
//			setContentView(getLoyoutId());
//		}
//		ViewUtils.inject(this);
//		initView();
//		initData();
//	}
//
//	protected int getLoyoutId(){
//		return -1;
//	}
//
//	
//	protected abstract void initView();
//	
//	protected abstract void initData();

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		getNetWorkDateThread();
		super.onResume();
	}

	protected void showToast(String content) {
		ToastUtils.show(this, content, Toast.LENGTH_SHORT);
	}

	private void getNetWorkDateThread() {

		new Thread(new Runnable() {

			@Override
			public void run() {
				Message msg = mHandler.obtainMessage();
				String netWorkDateString = XFJRUtil.getNetWorkDateString("yyyy-MM-dd HH:mm:ss");
				msg.obj = netWorkDateString;
				msg.what = 0;
				mHandler.sendMessage(msg);
			}
		}).start();
	}

}
