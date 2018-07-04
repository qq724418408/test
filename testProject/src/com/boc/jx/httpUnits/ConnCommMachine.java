package com.boc.jx.httpUnits;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.boc.jx.baseUtil.util.MyUtils;
import com.bocop.jxplatform.config.BocSdkConfig;

/**
 * 类功能作用描述 :网络连接器
 * 
 * @author wq
 * @version 1.0
 * @since 2012-11-13下午03:06:28
 */
public class ConnCommMachine {

	private HttpClient client;
	private HttpPost postRequest; // post请求
	private HttpPut putRequest; // put请求
	private HttpGet getRequest; // get请求
	private HttpResponse response;
	public boolean isCancel = false;// 是否被强制关闭
	private boolean reconnect = false;// 是否重连
	private boolean showProgress = false;
	public int j = 0;// 显示进度信息
	private Handler handler;

	public ConnCommMachine(HttpClient client, boolean reconnect) {
		this.client = client;
		this.reconnect = reconnect;
		this.client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
				BocSdkConfig.REQUEST_TIMEOUT);
		this.client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
				BocSdkConfig.RESPONSE_TIMEOUT);

	}

	public ConnCommMachine(HttpClient client) {
		this.client = client;

	}

	/**
	 * 版本检测Http的post请求（新方法）
	 * 
	 * @param list
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public String requestPostCheckVersion(String jsonstr, String url) throws Exception {
		String resStr = null;
		InputStream in = null;
		ByteArrayOutputStream out = null;

		try {
			if (!url.startsWith("http")) {
				url = BocSdkConfig.HTTP_SVR_CHECKVERSION + url;
			}
			postRequest = new HttpPost(url);
			if (true == reconnect) {
				// request.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new
				// DefaultHttpMethodRetryHandler());
			}
			// HttpEntity entity = new UrlEncodedFormEntity(jsonstr,
			// NetworkConstant.ENCODE);
			StringEntity entity = new StringEntity(jsonstr, BocSdkConfig.ENCODE);
			postRequest.setEntity(entity);
			if (isCancel) {
				throw new ColseRequestException();
			}
			response = client.execute(postRequest);

			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity rentity = response.getEntity();
				if (rentity != null) {// modifiy by GMM
					in = rentity.getContent();
					out = new ByteArrayOutputStream();
					byte[] arr = new byte[1024 * 8];
					int len = 0;
					while ((len = in.read(arr)) > 0) {
						if (isCancel) {
							throw new ColseRequestException();
						}
						out.write(arr, 0, len);
						out.flush();
					}
					resStr = out.toString(BocSdkConfig.ENCODE);
				}
			}
			return resStr;
		} finally {
			closeStream(in, out);
		}

	}

	/**
	 * 普通Http的post请求
	 * 
	 * @param list
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public String requestPost(List<NameValuePair> list, String url) throws Exception {
		String resStr = null;
		InputStream in = null;
		ByteArrayOutputStream out = null;

		try {
			if (!url.startsWith("http")) {
				url = BocSdkConfig.HTTP_SVR + BocSdkConfig.HTTP_ROOT + url;
			}
//			LogUtils.print(null, LogUtils.TYPE_CONSOLE, "saf", url + "\n" + list.toString());
			postRequest = new HttpPost(url);
			if (true == reconnect) {
				// request.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new
				// DefaultHttpMethodRetryHandler());
			}
			HttpEntity entity = new UrlEncodedFormEntity(list, BocSdkConfig.ENCODE);
			postRequest.setEntity(entity);
			if (isCancel) {
				throw new ColseRequestException();
			}
			response = client.execute(postRequest);

			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity rentity = response.getEntity();
				if (rentity != null) {// modifiy by GMM
					in = rentity.getContent();
					out = new ByteArrayOutputStream();
					byte[] arr = new byte[1024 * 8];
					int len = 0;
					while ((len = in.read(arr)) > 0) {
						if (isCancel) {
							throw new ColseRequestException();
						}
						out.write(arr, 0, len);
						out.flush();
					}
					resStr = out.toString(BocSdkConfig.ENCODE);
				}
			}
			return resStr;
		} finally {
			closeStream(in, out);
		}

	}

	/**
	 * 普通Http的get请求
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public String requestGet(String url) throws Exception {
		String resStr = null;
		InputStream in = null;
		ByteArrayOutputStream out = null;

		try {
			getRequest = new HttpGet(url);
			if (true == reconnect) {
				// request.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new
				// DefaultHttpMethodRetryHandler());
			}
			if (isCancel) {
				throw new ColseRequestException();
			}
			HttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
					BocSdkConfig.REQUEST_TIMEOUT);
			httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
					BocSdkConfig.RESPONSE_TIMEOUT);

			response = httpClient.execute(getRequest);

			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity rentity = response.getEntity();
				if (rentity != null) {
					resStr = EntityUtils.toString(response.getEntity(), "UTF-8");
					// in = rentity.getContent();
					// out = new ByteArrayOutputStream();
					// byte[] arr = new byte[1024 * 8];
					// int len = 0;
					// while ((len = in.read(arr)) > 0) {
					// if (isCancel) {
					// throw new ColseRequestException();
					// }
					// out.write(arr, 0, len);
					// out.flush();
					// }
					// resStr = out.toString(ServerValue.ENCODE);
				}
			}
			return resStr;
		} finally {
			closeStream(in, out);
		}

	}

	/**
	 * 大文件下载默认不显示进度条 调用setHandler方法可以回传进度值到handler
	 * 
	 * @param url
	 *            连接地址
	 * @param direct
	 *            目录名
	 * @return -2表示空间不足-1表示失败 0 表示成功1表示文件已经存在
	 * @throws
	 * 
	 */
	public int getRemoteFileInputStream(String url, String direct, String fileName)
			throws Exception {
		InputStream in = null;
		OutputStream out = null;
		j = 0;
		File dirfile = new File(direct);
		if (!dirfile.exists()) {
			dirfile.mkdir();
		}
		File file = new File(direct, fileName);
		if (!file.exists()) {// 若不存在则新建
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return BocSdkConfig.OTHER_ERROR;
			}
		}

		try {
			// url="http://open.boc.cn/data/ios_file/Payment20141121v11416865701189020481.3.0.apk";
			getRequest = new HttpGet(url);
			if (true == reconnect) {
				// get.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new
				// DefaultHttpMethodRetryHandler());
			}
			if (isCancel) {
				throw new ColseRequestException();
			}
			response = client.execute(getRequest);
			if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
				HttpEntity entity = response.getEntity();
				long curBytes = 0, totalBytes = 0;
				totalBytes = entity.getContentLength();
				if (entity != null) {
					long lenth = entity.getContentLength();
					if (lenth > MyUtils.getSdRemainRoom()) {
						return BocSdkConfig.SD_NOSPACE;// sd卡空间不足 返回码
					} else {
						in = entity.getContent();

						out = new FileOutputStream(file);
						byte[] arr = new byte[1024 * 8];
						int len = 0;
						while ((len = in.read(arr)) > 0) {
							if (isCancel) {
								throw new ColseRequestException();
							}
							out.write(arr, 0, len);
							out.flush();
							if (showProgress) {
								curBytes += len;
								j = ((int) (curBytes / (float) totalBytes * 100));
								Message msg = new Message();
								msg.what = j;
								handler.sendMessage(msg);
							}
						}
					}
				}
			}
			return BocSdkConfig.RIGHT_CODE;// 获取成功返回码
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (out != null) {
					out.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获取小文件
	 * 
	 * @param url
	 * @param appBean
	 * @return
	 * @throws Exception
	 */
	public byte[] getBytes(String url) throws Exception {
		byte[] is = null;
		InputStream in = null;

		ByteArrayOutputStream out = null;

		try {
			if (null == client) {
				return null;
			}
			postRequest = new HttpPost(url);
			if (true == reconnect) {
				// get.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new
				// DefaultHttpMethodRetryHandler());
			}
			if (isCancel) {
				throw new ColseRequestException();
			}
			response = client.execute(postRequest);
			if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
				HttpEntity entity = response.getEntity();

				if (entity != null) {
					in = entity.getContent();
					out = new ByteArrayOutputStream();
					byte[] arr = new byte[1024 * 8];
					int len = 0;
					while ((len = in.read(arr)) > 0) {
						if (isCancel) {
							throw new ColseRequestException();
						}
						out.write(arr, 0, len);
						out.flush();
					}
					is = out.toByteArray();
				}
			}
			return is;
		} finally {
			closeStream(in, out);
		}
	}

	/**
	 * 
	 * @param handler
	 *            传递显示进度的handler
	 */
	public void setHandler(Handler handler) {
		showProgress = true;// 置显示进度true
		this.handler = handler;

	}

	/**
	 * 
	 * 采用json方式的post请求
	 * 
	 * @param url
	 *            请求的url
	 * @param json
	 *            请求参数
	 * @param headerMap
	 *            请求头信息
	 * @return 返回的json数据
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String requestPostByJson(String url, String json, Map<String, String> headerMap)
			throws Exception {
		String resStr = null;
		InputStream in = null;
		ByteArrayOutputStream out = null;

		try {
			postRequest = new HttpPost(url);
			if (true == reconnect) {
				// get.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new
				// DefaultHttpMethodRetryHandler());
			}
			// 添加头信息
			Set keys = headerMap.keySet();
			if (keys != null) {
				Iterator iterator = keys.iterator();
				while (iterator.hasNext()) {
					String key = (String) iterator.next();
					String value = headerMap.get(key);
					postRequest.addHeader(key, value);
				}
			}

			if (!TextUtils.isEmpty(json)) {
				StringEntity se = new StringEntity(json, BocSdkConfig.ENCODE);
				postRequest.setEntity(se);
			}
			if (isCancel) {
				throw new ColseRequestException();
			}
			response = client.execute(postRequest);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					in = entity.getContent();
					out = new ByteArrayOutputStream();
					byte[] arr = new byte[1024 * 8];
					int len = 0;
					while ((len = in.read(arr)) != -1) {
						if (isCancel) {
							throw new ColseRequestException();
						}
						out.write(arr, 0, len);
						out.flush();
					}
					resStr = out.toString(BocSdkConfig.ENCODE);
				}
			}
			return resStr;
		} finally {
			closeStream(in, out);
		}
	}

	/**
	 * 
	 * 采用json方式的put请求
	 * 
	 * @param url
	 *            请求的url
	 * @param json
	 *            请求参数
	 * @param headerMap
	 *            请求头信息
	 * @return 返回的json数据
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String requestPutByJson(String url, String json, Map<String, String> headerMap)
			throws Exception {
		String resStr = null;
		InputStream in = null;
		ByteArrayOutputStream out = null;

		try {
			putRequest = new HttpPut(url);
			if (true == reconnect) {
				// get.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new
				// DefaultHttpMethodRetryHandler());
			}
			// 添加头信息
			Set keys = headerMap.keySet();
			if (keys != null) {
				Iterator iterator = keys.iterator();
				while (iterator.hasNext()) {
					String key = (String) iterator.next();
					String value = headerMap.get(key);
					putRequest.addHeader(key, value);
				}
			}

			if (!TextUtils.isEmpty(json)) {
				StringEntity se = new StringEntity(json, BocSdkConfig.ENCODE);
				putRequest.setEntity(se);
			}
			if (isCancel) {
				throw new ColseRequestException();
			}
			response = client.execute(putRequest);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					in = entity.getContent();
					out = new ByteArrayOutputStream();
					byte[] arr = new byte[1024 * 8];
					int len = 0;
					while ((len = in.read(arr)) != -1) {
						if (isCancel) {
							throw new ColseRequestException();
						}
						out.write(arr, 0, len);
						out.flush();
					}
					resStr = out.toString(BocSdkConfig.ENCODE);
				}
			}
			return resStr;
		} finally {
			closeStream(in, out);
		}
	}

	/**
	 * 采用json方式的get请求
	 * 
	 * @param url1
	 *            要请求的url
	 * @param sb
	 *            封装了请求参数
	 * @param headerMap
	 *            请求头信息
	 * @return 返回的json数据，考虑到程序的拓展性，json数据结构的多样化，此处不做json解析，放到具体页面
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String requestGetByJson(String url, String jason, Map<String, String> headerMap)
			throws Exception {
		String resStr = null;
		InputStream in = null;
		ByteArrayOutputStream out = null;
		try {
			url = url + jason;
			getRequest = new HttpGet(url);
			if (true == reconnect) {
				// get.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new
				// DefaultHttpMethodRetryHandler());
			}
			// 添加头信息
			Set keys = headerMap.keySet();
			if (keys != null) {
				Iterator iterator = keys.iterator();
				while (iterator.hasNext()) {
					String key = (String) iterator.next();
					String value = headerMap.get(key);
					putRequest.addHeader(key, value);
				}
			}
			if (isCancel) {
				throw new ColseRequestException();
			}
			response = client.execute(getRequest);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					in = entity.getContent();
					out = new ByteArrayOutputStream();
					byte[] arr = new byte[1024 * 8];
					int len = 0;
					while ((len = in.read(arr)) != -1) {
						if (isCancel) {
							throw new ColseRequestException();
						}
						out.write(arr, 0, len);
						out.flush();
					}
					resStr = out.toString(BocSdkConfig.ENCODE);
				}
			}
			return resStr;
		} finally {
			closeStream(in, out);
		}

	}

	/**
	 * 关闭流
	 * 
	 * @param in
	 * @param out
	 */
	private void closeStream(InputStream in, ByteArrayOutputStream out) {
		try {
			if (in != null) {
				in.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if (out != null) {
				out.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 关闭联网任务
	 */
	public boolean close() {
		try {
			if (postRequest != null) {
				postRequest.abort();
			}
			if (putRequest != null) {
				putRequest.abort();
			}
			if (getRequest != null) {
				getRequest.abort();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		isCancel = true;
		return true;
	}

}
