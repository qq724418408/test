package com.boc.jx.httptools.http.engin.okhttp2engin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.boc.jx.httptools.http.callback.IProgressCallback;
import com.boc.jx.httptools.http.callback.IStringCallaBack;
import com.boc.jx.httptools.http.callback.IUpLoadCallback;
import com.boc.jx.httptools.http.engin.IHttpEngin;
import com.boc.jx.httptools.http.utils.HttpEnginUtils;
import com.boc.jx.tools.LogUtils;
import com.bocop.xfjr.XfjrMain;
import com.bocop.xfjr.config.UrlConfig;
import com.bocop.xfjr.util.dialog.XFJRDialogUtil;
import com.bocop.xfjr.util.dialog.XFJRDialogUtil.DialogClick;
import com.bocop.xfjr.util.dialog.XfjrDialog;
import com.bocop.xfjr.util.enrypt.SignVerify;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

/**
 * description：okhttp2封装，post，get请求可以注释掉 ，支持断点续传下载
 * <p/>
 * Created by TIAN FENG on 2017/8/25 QQ：27674569 Email: 27674569@qq.com
 * Version：1.0
 */

public class OkHttp2Engin extends IHttpEngin {

	private static OkHttpClient mOkHttpClient;
	private static Handler mHandler = new Handler();
	public static String ssession = "";
	public static String tempSsession = "";

	public OkHttp2Engin() {
		mOkHttpClient = new OkHttpClient();
		SSLSocketFactory sSLSocketFactory = null;

	    try {
	        SSLContext sc = SSLContext.getInstance("TLS");
	        sc.init(null, new TrustManager[]{new TrustAllManager()},
	                new SecureRandom());
	        sSLSocketFactory = sc.getSocketFactory();
	    } catch (Exception e) {
	    }
	    // 信任所有证书
		mOkHttpClient.setSslSocketFactory(sSLSocketFactory);
		LogUtils.e("OkHttp2Engin--->mOkHttpClient(默认)=="+mOkHttpClient.getConnectTimeout());
		LogUtils.e("OkHttp2Engin--->getReadTimeout(默认)=="+mOkHttpClient.getReadTimeout());
		LogUtils.e("OkHttp2Engin--->getWriteTimeout(默认)=="+mOkHttpClient.getWriteTimeout());
		LogUtils.e("OkHttp2Engin--->getRetryOnConnectionFailure(默认)=="+mOkHttpClient.getRetryOnConnectionFailure());
		mOkHttpClient.setConnectTimeout(UrlConfig.CONNECT_TIMEOUT, TimeUnit.SECONDS);
		mOkHttpClient.setReadTimeout(UrlConfig.READ_TIMEOUT, TimeUnit.SECONDS);
		mOkHttpClient.setWriteTimeout(UrlConfig.WRITE_TIMEOUT, TimeUnit.SECONDS);
		mOkHttpClient.setRetryOnConnectionFailure(false);
		LogUtils.e("OkHttp2Engin--->mOkHttpClient=="+mOkHttpClient.getConnectTimeout());
		LogUtils.e("OkHttp2Engin--->getReadTimeout=="+mOkHttpClient.getReadTimeout());
		LogUtils.e("OkHttp2Engin--->getWriteTimeout=="+mOkHttpClient.getWriteTimeout());
		LogUtils.e("OkHttp2Engin--->getRetryOnConnectionFailure=="+mOkHttpClient.getRetryOnConnectionFailure());
	}
	
	private static class TrustAllManager implements X509TrustManager {
	    @Override
	    public void checkClientTrusted(X509Certificate[] chain, String authType)
	            throws CertificateException {
	    }

	    @Override
	    public void checkServerTrusted(X509Certificate[] chain, String authType)

	            throws CertificateException {
	    }

	    @Override
	    public X509Certificate[] getAcceptedIssuers() {
	        return new X509Certificate[0];
	    }

	}


	@Override
	public void get(final Context context, final String url, Map<String, Object> params, final IStringCallaBack callback) {
		// 请求路径 参数 + 路径代表唯一标识，存储md5加密url
		final String finalUrl = HttpEnginUtils.jointParams(url, params);
		Log.e("get url：", finalUrl);
		LogUtils.e("addHeader session----->" + ssession);
		if (TextUtils.isEmpty(OkHttp2Engin.ssession)) {
			ssession = "";
		}
		Request.Builder requestBuilder = new Request.Builder().addHeader("cookie", ssession).url(finalUrl).tag(url);
		// 可以省略，默认是GET请求
		Request request = requestBuilder.build();
		mOkHttpClient.newCall(request).enqueue(new Callback() {

			@Override
			public void onFailure(Request arg0, final IOException e) {
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						LogUtils.e("OkHttp2Engin--->onFailure=="+e);
						mOkHttpClient.cancel(url);
						callback.onError(url, e);
						callback.onFinal(url);
					}
				});
			}

			@Override
			public void onResponse(Response response) throws IOException {
				mHandler.post(new OkHttp2SuccessRunnable(context,response, callback, url));
			}
		});
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public void post(final Context context, final String url, Map<String, Object> params, final IStringCallaBack callback) {
		LogUtils.e("addHeader session----->" + ssession);
		if (TextUtils.isEmpty(OkHttp2Engin.ssession)) {
			ssession = "";
		}
		params.put("ACCESS-TOKEN", SignVerify.sha1Sign());
		params.put("accessToken", SignVerify.sha1Sign());
		params.put("time", SignVerify.time);

		Body body = appendBody(params, url);
		// 拼接请求头
		RequestBody requestBody = body.body;
		Request request = new Request.Builder().addHeader("cookie", ssession)
				.addHeader("Accept", "application/vnd.captech-v1.0+json").addHeader("accessToken", SignVerify.sha1Sign())
				.addHeader("content-type", "charset=utf-8")
				.addHeader("ACCESS-TOKEN", SignVerify.sha1Sign()).addHeader("time", SignVerify.time).url(url).tag(url)
				.post(requestBody).build();
		
		Callback resultCall = new Callback() {

			@Override
			public void onFailure(Request arg0, final IOException e) {
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						LogUtils.e("OkHttp2Engin--->onFailure=="+e);
						mOkHttpClient.cancel(url);
						callback.onError(url, e);
						callback.onFinal(url);
					}
				});
			}

			@Override
			public void onResponse(Response response) throws IOException {
				mHandler.post(new OkHttp2SuccessRunnable(context,response, callback, url));
			}
		};
		if (body.isFormData) {
			mOkHttpClient.setConnectTimeout(UrlConfig.CONNECT_TIMEOUT, TimeUnit.SECONDS);
			mOkHttpClient.setReadTimeout(UrlConfig.READ_TIMEOUT, TimeUnit.SECONDS);
			mOkHttpClient.setWriteTimeout(UrlConfig.WRITE_TIMEOUT, TimeUnit.SECONDS);
			LogUtils.e("OkHttp2Engin--->post timeout=="+mOkHttpClient.getConnectTimeout());
		} else {
			mOkHttpClient.setConnectTimeout(UrlConfig.UP_CONNECT_TIMEOUT, TimeUnit.SECONDS);
			mOkHttpClient.setReadTimeout(UrlConfig.UP_READ_TIMEOUT, TimeUnit.SECONDS);
			mOkHttpClient.setWriteTimeout(UrlConfig.UP_WRITE_TIMEOUT, TimeUnit.SECONDS);
			LogUtils.e("OkHttp2Engin--->upload file timeout=="+mOkHttpClient.getConnectTimeout());
		}
		mOkHttpClient.newCall(request).enqueue(resultCall);
	}

	@Override
	public void downLoad(final Context context, String url, Map<String, Object> params, final String fileName,
			final IProgressCallback callback) {
		// 储存下载文件的目录
		String savePath = null;
		try {
			savePath = HttpEnginUtils.isExistDir(fileName.substring(0, fileName.lastIndexOf("/")));
		} catch (IOException e1) {
			callback.onError(e1);
			callback.onFinal();
		}
		// 创建文件
		final File file = new File(savePath, fileName.substring(fileName.lastIndexOf("/")));
		// 文件的大小
		final long fileSize = file.length();
		// 下载为get请求 拼接url
		final String finalUrl = HttpEnginUtils.jointParams(url, params);
		final Request request = new Request.Builder().addHeader("cookie", ssession).url(finalUrl)
				.header("RANGE", "bytes=" + fileSize + "-")// 已经下载位置开始
				.tag(url).build();
		mOkHttpClient.newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(Request call, final IOException e) {
				// 下载失败
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						callback.onError(e);
						callback.onFinal();
					}
				});
			}

			@Override
			public void onResponse(Response response) throws IOException {
				Headers headers = response.headers();
				String c = headers.get("Set-Cookie");
				if (XfjrMain.isSit&&!TextUtils.isEmpty(ssession)&&!c.equals(OkHttp2Engin.ssession)) {
					XFJRDialogUtil.tipsDialog(context, "ssession变了", new DialogClick() {
						
						@Override
						public void onOkClick(View view, XfjrDialog dialog) {
							// TODO Auto-generated method stub
							dialog.cancel();
						}
						
						@Override
						public void onCancelClick(View view, XfjrDialog dialog) {
							// TODO Auto-generated method stub
							dialog.cancel();
						}
					}).show();
				}
				OkHttp2Engin.ssession = c;
				InputStream is = null;
				// 每次读100k
				byte[] buf = new byte[1024 * 100];
				int len;
				FileOutputStream fos = null;
				try {
					// 获取流
					is = response.body().byteStream();
					// 获取总大小 = 当前剩余大小 + 文件已经下载的大小
					final long total = response.body().contentLength() + fileSize;
					// 文件输出流，true 可以接着写实现断点续传
					fos = new FileOutputStream(file, true);
					// 起始位置为文件已经下载的大小
					long sum = fileSize;
					// 循环写
					while ((len = is.read(buf)) != -1) {
						fos.write(buf, 0, len);
						fos.flush();
						sum += len;
						// 回调进度
						final long finalSum = sum;
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								callback.onProgress(total, finalSum);
							}
						});
					}
					// 成功后返回
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							callback.onSuccess(file);
							callback.onFinal();
						}
					});
				} catch (final Exception e) {
					// 失败
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							callback.onError(e);
							callback.onFinal();
						}
					});

				} finally {
					try {
						if (is != null) {
							is.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					try {
						if (fos != null) {
							fos.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	@Override
	public void upLoad(Context context, final String url, Map<String, Object> params, final IUpLoadCallback callback) {
		params.put("ACCESS-TOKEN", SignVerify.sha1Sign());
		params.put("accessToken", SignVerify.sha1Sign());
		params.put("time", SignVerify.time);
		RequestBody requestBody = appendBody(params, url).body;
		final Request request = new Request.Builder().addHeader("cookie", ssession)
				.addHeader("Accept", "application/vnd.captech-v1.0+json").addHeader("content-type", "charset=utf-8")
				.addHeader("ACCESS-TOKEN", SignVerify.sha1Sign()).addHeader("time", SignVerify.time).url(url).tag(url)
				.post(new OkHttp2ProgressRequestBody(requestBody, callback))// 装饰requestbody进行上传进度监听
				.build();
		mOkHttpClient.newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(Request call, final IOException e) {
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						mOkHttpClient.cancel(url);
						callback.onError(e);
						callback.onFinal();
					}
				});
			}

			@Override
			public void onResponse(final Response response) throws IOException {
				final String result = response.body().string();
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						callback.onSuccess(result);
						callback.onFinal();
					}
				});
				Headers headers = response.headers();
				String c = headers.get("Set-Cookie");
				OkHttp2Engin.ssession = c;
			}
		});
	}

	/**
	 * 组装post请求参数body
	 */

	protected Body appendBody(Map<String, Object> params, String url) {
		Body body = new Body();
		if (body.isFormData = guessParamsType(params, url)) {
			FormEncodingBuilder builder = new FormEncodingBuilder();
			for (String key : params.keySet()) {
				builder.add(key, params.get(key) + "");
			}
			body.body = builder.build();
			return body;
		}
		MultipartBuilder builder = new MultipartBuilder();
		if (params != null && params.size() > 0) {
			builder.type(MultipartBuilder.FORM);
			addParams(builder, params);
			body.body = builder.build();
			return body;
		}
		body.isFormData = true;
		body.body =  RequestBody.create(null, "");
		return body;
	}

	private boolean guessParamsType(Map<String, Object> params, String url) {
		if (url.contains(UrlConfig.BASE_SERVER)) {
			boolean flag = false;
			if (params.get("key___") != null) {
				flag = (boolean) params.get("key___");
			}
			if (flag) {
				return true;
			}

		}
		for (String key : params.keySet()) {
			Object value = params.get(key);
			if (value instanceof File || value instanceof List) {
				return false;
			}
		}
		return true;
	}

	// 添加参数
	private void addParams(MultipartBuilder builder, Map<String, Object> params) {
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				// builder.addFormDataPart(key, params.get(key) + "");
				Object value = params.get(key);
				if (value instanceof File) {
					// 处理文件 --> Object File
					File file = (File) value;
					builder.addFormDataPart(key, file.getName(),
							RequestBody.create(MediaType.parse(guessMimeType(file.getAbsolutePath())), file));
				} else if (value instanceof List) {
					// 代表提交的是 List集合
					try {
						List<File> listFiles = (List<File>) value;
						for (int i = 0; i < listFiles.size(); i++) {
							// 获取文件
							File file = listFiles.get(i);
							builder.addFormDataPart(key + i, file.getName(),
									RequestBody.create(MediaType.parse(guessMimeType(file.getAbsolutePath())), file));
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					builder.addFormDataPart(key, value + "");
				}
			}
		}
	}

	/**
	 * 猜测文件类型
	 */
	private String guessMimeType(String path) {
		FileNameMap fileNameMap = URLConnection.getFileNameMap();
		String contentTypeFor = fileNameMap.getContentTypeFor(path);
		if (contentTypeFor == null) {
//			contentTypeFor = "application/octet-stream";
			contentTypeFor = "image/jpeg";
			contentTypeFor = "png";
		}
		return contentTypeFor;
	}

	/**
	 * 取消tag下的所有请求
	 *
	 * @param url
	 *            tag
	 */
	@Override
	public void cancle(String url) {
		mOkHttpClient.cancel(url);
	}

	class Body {
		public RequestBody body;
		public boolean isFormData = true;
	}
}
