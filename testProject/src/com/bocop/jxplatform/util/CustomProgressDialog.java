package com.bocop.jxplatform.util;

import com.boc.jx.constants.Constants;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.activity.CFTActivity;
import com.bocop.jxplatform.activity.HTZQActivity;
import com.bocop.jxplatform.activity.WebActivity;
import com.bocop.jxplatform.activity.WebViewActivity;
import com.bocop.jxplatform.activity.WebViewGetTitleActivity;
import com.bocop.jxplatform.activity.ZqtFirstActivity;
import com.bocop.jxplatform.trafficassistant.BocOpWebActivity;
import com.bocop.xms.activity.MessageActivity;
import com.bocop.xms.activity.YbtActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * @Description:自定义对话框
 * @author http://blog.csdn.net/finddreams
 */
public class CustomProgressDialog extends ProgressDialog {

	private AnimationDrawable mAnimation;
	private Context mContext;
	private ImageView mImageView;
	private String mLoadingTip;
	private TextView mLoadingTv;
	private int count = 0;
	private String oldLoadingTip;
	private int mResid;

	public CustomProgressDialog(Context context, String content, int id) {
		super(context);
		this.mContext = context;
		this.mLoadingTip = content;
		this.mResid = id;
		setCanceledOnTouchOutside(true);
	}
	
	/**
	 * 网络设置工具方法
	 * 
	 * @param context
	 *            上下文对象
	 */
	public static void showNetworkSetDialog(final Context context) {
		AlertDialog.Builder builder = new Builder(context);
		builder.setTitle("提示");
		builder.setMessage("当前网络不可用，是否设置网络？");
		builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent("android.settings.WIFI_SETTINGS");
				context.startActivity(intent);
				dialog.cancel();
			}
		});
		if (!((Activity) context).isFinishing()) {
			builder.setCancelable(true).show();
		}
	}
	
	/**
	 * 网络设置工具方法
	 * 
	 * @param context
	 *            上下文对象
	 */
	public static void showBocNetworkSetDialog(final Context context) {
		BocopDialog dialog = new BocopDialog(context, "提示", "当前网络不可用，是否设置网络？");
		dialog.setPositiveButton(new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent("android.settings.WIFI_SETTINGS");
				context.startActivity(intent);
				dialog.cancel();
			}
		}, "设置");
		dialog.hideNegativeButton();
		if (!((Activity) context).isFinishing()) {
			dialog.show();
		}
	}
	
	/**
	 * 网络设置工具方法
	 * 
	 * @param context
	 *            上下文对象
	 */
	public static void showBocRegisterSetDialog(final Context context) {
		Activity activity = null;
		BocopDialog dialog = null;
		try {
		    activity = (Activity)context;
		    if(activity instanceof MessageActivity){
		    	dialog = new BocopDialog(context, "提示", "办理签约要实名认证，是否前往中银开发平台进行实名认证？");
			}
		    else{
		    	dialog = new BocopDialog(context, "提示", "办理违章需要实名认证，是否前往中银开发平台进行实名认证？");
		    }
		} catch (Exception e) {
		    e.printStackTrace();
		}
		
		
		dialog.setPositiveButton(new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(context, BocOpWebActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("title", "添加银行卡");
				bundle.putString("type", "bindCard");
				intent.putExtras(bundle);
				context.startActivity(intent);
				dialog.cancel();
			}
		}, "认证");
		dialog.setNegativeButton(new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(context, "请前往中银开发平台实名认证", Toast.LENGTH_LONG).show();
				dialog.cancel();
			}
		}, "取消");
		if (!((Activity) context).isFinishing()) {
			dialog.show();
		}
	}

	
	/**
	 * 网络设置工具方法
	 * 
	 * @param context
	 *            上下文对象
	 */
	public static void showBocFengxianDialog(final Context context,String strTitle,String strAdvise,final String className) {
		Activity activity = null;
		BocopDialog dialog = null;
		dialog = new BocopDialog(context, strTitle,strAdvise);
		
		
		dialog.setPositiveButton(new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
//				Intent intent = new Intent(context,
//						HTZQActivity.class);
				Intent intent = null;
				try {
					if(className.contains("HTZQActivity")){
						intent = new Intent(context,
								Class.forName(className));
					}
					else if(className.contains("YBTActivity")){
						intent = new Intent(context, YbtActivity.class);
						context.startActivity(intent);
					}else if(className.contains("ZqtFirstActivity")){
						intent = new Intent(context, ZqtFirstActivity.class);
						context.startActivity(intent);
					}
					else if(className.contains("tbw")){
						Bundle bundle = new Bundle();
						bundle.putString("url", Constants.UrlForMainTbw);
						bundle.putString("name", "淘宝网");
						intent = new Intent(context, WebViewGetTitleActivity.class);
						intent.putExtras(bundle);
						context.startActivity(intent);
					}else if(className.contains("jd")){
						Bundle bundle = new Bundle();
						bundle.putString("url", Constants.UrlForMainJd);
						bundle.putString("name", "京东");
						intent = new Intent(context, WebViewGetTitleActivity.class);
						intent.putExtras(bundle);
						context.startActivity(intent);
					}else if(className.contains("mtwm")){
						Bundle bundle = new Bundle();
						bundle.putString("url", Constants.UrlForMainMtwm);
						bundle.putString("name", "美团外卖");
						intent = new Intent(context, WebViewGetTitleActivity.class);
						intent.putExtras(bundle);
						context.startActivity(intent);
					}else if(className.contains("bdwm")){
						Bundle bundle = new Bundle();
						bundle.putString("url", Constants.UrlForMainBdwm);
						bundle.putString("name", "百度外卖");
						intent = new Intent(context, WebViewGetTitleActivity.class);
						intent.putExtras(bundle);
						context.startActivity(intent);
					}
					context.startActivity(intent);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				dialog.cancel();
			}
		}, "同意");
		dialog.setNegativeButton(new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		}, "不同意");
		if (!((Activity) context).isFinishing()) {
			dialog.show();
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initData();
	}

	private void initData() {

		mImageView.setBackgroundResource(mResid);
		// 通过ImageView对象拿到背景显示的AnimationDrawable
		mAnimation = (AnimationDrawable) mImageView.getBackground();
		// 为了防止在onCreate方法中只显示第一帧的解决方案之一
		mImageView.post(new Runnable() {
			@Override
			public void run() {
				mAnimation.start();

			}
		});
		mLoadingTv.setText(mLoadingTip);

	}

	public void setContent(String str) {
		mLoadingTv.setText(str);
	}

	private void initView() {
		setContentView(R.layout.progress_dialog);
		mLoadingTv = (TextView) findViewById(R.id.loadingTv);
		mImageView = (ImageView) findViewById(R.id.loadingIv);
	}

	/*@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		mAnimation.start(); 
		super.onWindowFocusChanged(hasFocus);
	}*/
}
