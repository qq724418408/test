package com.bocop.jxplatform.file;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLDecoder;

import org.apache.http.Header;
import org.apache.http.conn.ConnectTimeoutException;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.boc.jx.base.BaseApplication;
import com.boc.jx.baseUtil.asynchttpclient.AsyncHttpClient;
import com.boc.jx.baseUtil.asynchttpclient.AsyncHttpResponseHandler;
import com.boc.jx.httpUnits.ColseRequestException;
import com.boc.jx.httpUnits.ConnCommMachine;
import com.bocop.jxplatform.config.BocSdkConfig;
import com.bocop.jxplatform.util.CustomProgressDialog;

/**
 * @author luoyang
 * @version 创建时间：2015-7-7 下午10:03:32 类说明
 */

public class HttpDownloader {
	private BaseApplication appBean;
	private Context mCt;
	private ProgressDialog waitDialog;
	
	public void getBigFile(Context ct, String url, String direct, String fileName, Handler handler) {
		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			Toast.makeText(mCt, "sd卡不存在", Toast.LENGTH_SHORT).show();
			return;// 如果sd卡没挂载取消联网
		}
		if (appBean.isNetStat()) {// 网络状态正常G
			if (!appBean.hs.containsKey(url)) {
				GetBigFileThread getBigFileThread = new GetBigFileThread(ct, url, direct, fileName, handler,
						1);
				getBigFileThread.execute();
			}
		} else {// 网络异常
			CustomProgressDialog.showBocNetworkSetDialog(mCt);
		}
	}
	
	public interface ICallbackListener {
		
		// 普通联网请求
		public void callback(Integer statValue, String url, String str);

		// 小文件请求
		public void callbackByte(Integer statValue, String url, byte[] arr);

		// 大文件请求
		public void callbackBigFile(Integer statValue, String url);

	}
	/**
	 * 请求大文件异步任务
	 */
	private class GetBigFileThread extends AsyncTask<Integer, Integer, Integer> {
		
		@SuppressWarnings("unused")
		private int mWaitDialogType;// 请求等待对话框类型
		@SuppressWarnings("unused")
		private Context mct;

		ICallbackListener iCallback; // 回调接口
		private String url;
		private String direct;
		private String fileName;
		private Handler handler;
		private ConnCommMachine commMachine;// 联网器
		int i = -1;// 获取文件的放回结果默认为失败

		/**
		 * GetBigFileThread
		 * 
		 * @param paramList 请求参数
		 * @param ct 上下文
		 * @param url 请求地址
		 * @param direct 文件存放目录
		 * @param waitDialogType 请求等待对话框类型
		 */
		public GetBigFileThread(Context ct, String url, String direct, String fileName,
				int waitDialogType) {
			iCallback = (ICallbackListener) ct;
			this.mct = ct;
			this.url = url;
			this.direct = direct;
			this.fileName = fileName;
			this.mWaitDialogType = waitDialogType;
		}

		public GetBigFileThread(Context ct, ICallbackListener iCallback, String url, String direct,
				String fileName, Handler handler) {
			this.iCallback = iCallback;
			this.mct = ct;
			this.url = url;
			this.direct = direct;
			this.fileName = fileName;
			this.handler = handler;
		}

		public GetBigFileThread(Context ct, String url, String direct, String fileName,
				Handler handler, int waitDialogType) {
			iCallback = (ICallbackListener) ct;
			this.mct = ct;
			this.url = url;
			this.direct = direct;
			this.fileName = fileName;
			this.handler = handler;
			this.mWaitDialogType = waitDialogType;
		}

		/**
		 * 联网前准备连接器 存放url和machine到hashmap
		 */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			commMachine = appBean.getCommMachine();// 获取联网器
			if (handler != null) {
				commMachine.setHandler(handler);
			}
			appBean.putUrl(url, commMachine);// 将url加入到hashmap
		}

		@Override
		protected Integer doInBackground(Integer... params) {
			try {
				// 发起请求
				if (commMachine.isCancel) {
					return BocSdkConfig.FORCE_CLOSE;// 被强制关闭了
				}
				i = commMachine.getRemoteFileInputStream(url, direct, fileName);
			} catch (Exception e1) {
				deletFromsd();// 出现异常了则尝试删除未下载完成的缺损文件
				if (commMachine.isCancel || e1 instanceof ColseRequestException) {
					return BocSdkConfig.FORCE_CLOSE;// 被强制关闭了
				} else if (e1 instanceof ConnectTimeoutException || e1 instanceof SocketTimeoutException) {
					return BocSdkConfig.TIMEOUT_ERROR;// 超时的异常
				} else {
					return -1;// 其他异常
//					return ServerValue.OTHER_ERROR;// 其他异常
				}
			} finally {
				appBean.clearPoint(url);// 清理hashmap中url
			}

			return i;// 0正常；1为已经存在 ；2 为SD卡空间不足
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			// 取消等待对话框
			if (waitDialog != null && waitDialog.isShowing()) {
				waitDialog.cancel();
			}
			Log.i("tag", "请求状态：" + result);
			Log.i("tag", "入口名称: " + url);
//			LogHelper.iPrint("请求状态：" + result);
//			LogHelper.iPrint("入口名称: " + url);
			// 回调刷新主界面
			iCallback.callbackBigFile(result, url);
		}

		/**
		 * 从sd卡删除文件
		 */
		private void deletFromsd() {
			File file = new File(direct, fileName);
			if (file.exists()) {
				file.delete();
			}
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

	}
	
	
	
	private URL url = null;
	FileUtils fileUtils = new FileUtils();
	// InputStream input=null;
	ByteArrayInputStream is = null;

	public int downfile(String urlStr, final String path, final String fileName) {
		if (fileUtils.isFileExist(path + fileName)) {
			return 1;
		} else {
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(200 * 1000);
			client.get(urlStr, new AsyncHttpResponseHandler() {

				@Override
				public void onStart() {
					// TODO Auto-generated method stub
					super.onStart();
					Log.i("tag", "开始下载文件。。。");
				}

				@Override
				public void onSuccess(int statusCode, Header[] headers,
						byte[] responseBody) {
					// TODO Auto-generated method stub
					for (Header h : headers) {
						try {
							Log.d("tag", h.getName() + "----------->"
									+ URLDecoder.decode(h.getValue(), "UTF-8"));
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if (responseBody != null) {
						try {
							Log.d("tag", "content : "
									+ new String(responseBody, "GBK"));
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					if (statusCode == 200 && responseBody.length > 0) {
						Log.i("tag", String.valueOf(responseBody.length));
						is = new ByteArrayInputStream(responseBody);
						File resultFile = fileUtils.write2SDFromInput(path,
								fileName, is);
						if (resultFile == null) {
							return;
						}
					}
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						byte[] responseBody, Throwable error) {
					// TODO Auto-generated method stub

				}
			});

			// input = getInputStream(urlStr);
		}
		return 0;
	}

	// 由于得到一个InputStream对象是所有文件处理前必须的操作，所以将这个操作封装成了一个方法
	public InputStream getInputStream(String urlStr) throws IOException {
		InputStream is = null;
		try {
			url = new URL(urlStr);
			HttpURLConnection urlConn = (HttpURLConnection) url
					.openConnection();
			is = urlConn.getInputStream();

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return is;
	}
}
