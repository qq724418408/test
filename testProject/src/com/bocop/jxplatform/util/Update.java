package com.bocop.jxplatform.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.bocop.jxplatform.R;
import com.bocop.jxplatform.config.BocSdkConfig;
import com.bocop.jxplatform.config.TransactionValue;
import com.bocop.jxplatform.util.BocOpUtilVersion.CallBackBoc;
import com.bocop.xfjr.XfjrMain;
import com.google.gson.Gson;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

public class Update {
	
	private Context mContext;
	private String appUrl; // 地址
	private String appUrl2; // 地址
	private String isNeedUpdate; // 强制更新
	private String version; // 版本号
	private String updateContent; // 更新内容
	
	private ProgressBar mProgress; //下载进度条控件
    private static final int DOWNLOADING = 1; //表示正在下载
    private static final int DOWNLOADED = 2; //下载完毕
    private static final int DOWNLOAD_FAILED = 3; //下载失败
    private int progress; //下载进度
    private boolean cancelFlag = false; //取消下载标志位
    private AlertDialog alertDialog1, alertDialog2; //表示提示对话框、进度条对话框
    private static final String savePath = getSDCardPath() + "/bocop/"; //apk保存到SD卡的路径
    private static final String saveFileName = savePath + "yhtUpdate.apk"; //完整路径名
	
	
	
	public Update(Context context) {
		super();
		mContext = context;
	}



	public void requestVersionFromBocop(final DialogShowingListener showingListener) {
		Gson gson = new Gson();
		Map<String, String> map = new HashMap<String, String>();
		map.put("clientid", BocSdkConfig.CONSUMER_KEY);// 应用KEY
		map.put("type", "1");// 应用KEY
		String strGson = gson.toJson(map);
		Log.i("tag", "检测干本JSON数据：：" + strGson);
		if (strGson != null && strGson.length() > 0) {
			BocOpUtilVersion util = new BocOpUtilVersion(mContext);
			util.postOpboc(strGson, TransactionValue.SA0000, new CallBackBoc() {
				
				@Override
				public void onSuccess(String responStr) {
					try {
						Log.i("tag", responStr);
						Map<String,String> map;
						map = JsonUtils.getMapStr(responStr);
						if(XfjrMain.isSkipUpdate){
							isNeedUpdate = "0000";
						} else {
							isNeedUpdate = map.get("need_update") + "";// 是否强制更新
						}
						appUrl = map.get("appurl") + ""; // 下载地址
						version = map.get("version") + "";//更新版本号
						updateContent = map.get("new_function") + "";// 更新内容
						
						Log.i("tag", isNeedUpdate+"," + appUrl+"," + version
						+ updateContent);
						if (isUpdate(BocSdkConfig.APP_VERSION, version)) {
							Log.i("tag", "need update");
							BocSdkConfig.NEED_UPDATE = true;
							BocSdkConfig.UPDATE_NECESSARY = isNeedUpdate;
							BocSdkConfig.NEW_APP_VERSION = version;
							BocSdkConfig.APP_URL = appUrl;
							BocSdkConfig.UPDATE_CONTENT = updateContent;
							Log.i("tag", "requestVersionFromBocop2");
							requestVersionFromBocop2(appUrl,showingListener);
						} else {
							showingListener.show(false);
//							Toast.makeText(mContext, "已经是最新版本",
//							Toast.LENGTH_LONG).show();
							Log.i("tag", "do not need update");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				@Override
				public void onStart() {
					
				}
				
				@Override
				public void onFinish() {
					
				}
				
				@Override
				public void onFailure(String responStr) {
//					CspUtil.onFailure(mContext, responStr);
					showingListener.showAdDia();
				}
			});
		}
	}

	
	public void requestVersionFromBocop2(String url,final DialogShowingListener showingListener){
		
		appUrl2 = url;
		BocOpUtilVersion2 util2 = new BocOpUtilVersion2(mContext);
		util2.postOpboc(appUrl2, new com.bocop.jxplatform.util.BocOpUtilVersion2.CallBackBoc() {
			
			@Override
			public void onSuccess(String responStr) {
				try {
					Log.i("tag", responStr);
					Map<String,String> map;
					map = JsonUtils.getMapStr(responStr);
					appUrl2 = map.get("url") + ""; // 下载地址
					BocSdkConfig.APP_URL = appUrl2;
					Log.i("tag", "url:" + appUrl2);
					showUpdate(showingListener); // 显示更新对话框
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				
			}
			
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onFailure(String responStr) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
	/**
	 * 比较版本号，判断是否需要更新
	 * 
	 * @param oldVersion
	 * @param newVersion
	 * @return
	 */
	private Boolean isUpdate(String oldVersion, String newVersion) {
		String oldVer = "";
		String newVer = "";
		String oldVerArrary[] = oldVersion.split("\\.");
		String newVerArrary[] = newVersion.split("\\.");
		for (int i = 0; i < oldVerArrary.length; i++) {
			oldVer += oldVerArrary[i];
		}
		for (int j = 0; j < newVerArrary.length; j++) {
			newVer += newVerArrary[j];
		}
		if (Integer.parseInt(newVer) > Integer.parseInt(oldVer)) {
			return true;
		} else {
			return false;
		}
	}
	
	public void showUpdate(){
		showUpdate(null);
	}

	public void showUpdate(final DialogShowingListener showingListener) {
		Dialog dialog;
		if ("1".equals(BocSdkConfig.UPDATE_NECESSARY)) {
			dialog = new AlertDialog.Builder(mContext)
					.setTitle("发现新版本")
					.setMessage(updateContent)
					// 设置内容
					.setPositiveButton("更新",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// network.getBigFile(activity, appUrl,
									// Environment.getExternalStorageDirectory()
									// + BaseValue.APK_DIR,
									// BaseValue.APK_NAME, downLoadHandler);
									Log.i("tag", "start downloaddialog");
									showDownloadDialog();
									dialog.cancel();
								}
							}).create();
			// 显示对话框
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		} else {// 设置内容
			dialog = new AlertDialog.Builder(mContext)
					.setTitle("发现新版本")
					.setMessage(BocSdkConfig.UPDATE_CONTENT)
					.setPositiveButton("以后再说",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									if (null != showingListener) {
										showingListener.show(false);
									}
									dialog.cancel();
								}
							})
					.setNeutralButton("立即更新",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
//									Intent updateIntent =new Intent(mContext, DownloadService.class);  
//									updateIntent.putExtra("url", appUrl);
//									mContext.startService(updateIntent);  
//									Log.i("tag", "startService");
									Log.i("tag", "start downloaddialog");
									showDownloadDialog();
								}
							}).create();// 创建
			// 显示对话框
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			if (null != showingListener) {
				showingListener.show(true);
			}
			dialog.show();
		}
	}

	   /** 显示进度条对话框 */
    public void showDownloadDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setTitle("正在更新");
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.softupdate_progress, null);
        mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
        dialog.setView(v);
        //如果是强制更新，则不显示取消按钮
//        if (forceUpdate == false) {
//            dialog.setNegativeButton("取消", new OnClickListener() {
//                @Override
//                public void onClick(DialogInterface arg0, int arg1) {
//                    // TODO Auto-generated method stub
//                    arg0.dismiss();
//                    cancelFlag = false;
//                }
//            });
//        }
        alertDialog2  = dialog.create();
        alertDialog2.setCancelable(false);
        alertDialog2.show();

        //下载apk
        downloadAPK();
    }

    /** 下载apk的线程 */
	public void downloadAPK() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Log.i("tag", "start downloadAPK" + "app_url:" + BocSdkConfig.APP_URL);
					URL url = new URL(BocSdkConfig.APP_URL);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.connect();
					int length = conn.getContentLength();
					InputStream is = conn.getInputStream();
					File file = new File(savePath);
					Log.i("tag", "File");
					if (!file.exists()) {
						Log.i("tag", "start mkdir");
						file.mkdir();
						Log.i("tag", "end mkdir");
						
					}
					String apkFile = saveFileName;
					Log.i("tag", "apkfile");
					File ApkFile = new File(apkFile);
					FileOutputStream fos = new FileOutputStream(ApkFile);
					int count = 0;
					byte buf[] = new byte[1024];
					do {
						int numread = is.read(buf);
						count += numread;
						progress = (int) (((float) count / length) * 100);
						// 更新进度
						mHandler.sendEmptyMessage(DOWNLOADING);
						if (numread <= 0) {
							// 下载完成通知安装
							mHandler.sendEmptyMessage(DOWNLOADED);
							break;
						}
						fos.write(buf, 0, numread);
					} while (!cancelFlag); // 点击取消就停止下载.
					fos.close();
					is.close();
					Log.i("tag", "byte" + progress);
				} catch (Exception e) {
					mHandler.sendEmptyMessage(DOWNLOAD_FAILED);
					e.printStackTrace();
				}
			}
		}).start();
	}

    /** 更新UI的handler */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
            case DOWNLOADING:
                mProgress.setProgress(progress);
                break;
            case DOWNLOADED:
                if (alertDialog2 != null)
                    alertDialog2.dismiss();
                installAPK();
                break;
            case DOWNLOAD_FAILED:
                Toast.makeText(mContext, "网络断开，请稍候再试", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
            }
        }
    };

    /** 下载完成后自动安装apk */
    public void installAPK() {
        File apkFile = new File(saveFileName);
        if (!apkFile.exists()) {
        	Log.i("tag", "file not exists");
            return;
        }
        Log.i("tag", "file  exists,start setup");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://" + apkFile.toString()), "application/vnd.android.package-archive");   
        mContext.startActivity(intent); 
    }
    
	private static String getSDCardPath() {  
	    File sdcardDir = null;  
	    // 判断SDCard是否存在  
	    boolean sdcardExist = Environment.getExternalStorageState().equals(  
	            android.os.Environment.MEDIA_MOUNTED);  
	    if (sdcardExist) {  
	        sdcardDir = Environment.getExternalStorageDirectory();  
	    }  
	    return sdcardDir.toString();  
	}
	
	public interface DialogShowingListener {
		void show(boolean isShowing);
		void showAdDia();
	}
	
}
