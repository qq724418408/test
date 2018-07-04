package com.bocop.jxplatform.http;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;

import android.content.Context;

import com.bocop.jxplatform.config.BocSdkConfig;
import com.bocop.jxplatform.util.LoginUtil;
import com.bocsoft.ofa.http.asynchttpclient.AsyncHttpClient;
import com.bocsoft.ofa.http.asynchttpclient.ResponseHandlerInterface;
import com.bocsoft.ofa.http.asynchttpclient.expand.JsonRequestParams;

public final class RestTemplateJxBank {
	/**
	 * URL
	 */
	private static final String BASE_URL = "https://openapi.boc.cn";
	/**
	 * 实例化对象
	 */
	private AsyncHttpClient client = factoryHttpClinet();
	/**
	 * 缓存Cookie
	 */
	private Context context;
	/** 初始化相关参数 */
	static {
		try {
			KeyStore trustStore;
			trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			try {
				trustStore.load(null, null);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (CertificateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			SSLSocketFactory sf = null;
			try {
				sf = new MySSLSocketFactory(trustStore);
			} catch (KeyManagementException e) {
				e.printStackTrace();
			} catch (UnrecoverableKeyException e) {
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 8080));
			registry.register(new Scheme("https", sf, 443));
			// client = new AsyncHttpClient(registry);
			// client.setTimeout(15000);
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
	}

	public RestTemplateJxBank(Context c) {
		this.context = c;
	}

	public void getPublicHead() {
		String action = LoginUtil.getToken(context);
		String userid = LoginUtil.getUserId(context);
		client.addHeader("clentid", BocSdkConfig.CONSUMER_KEY);
		client.addHeader("acton", action);
		client.addHeader("userid", userid);
		client.addHeader("chnflg", "1");
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		// 获取当前时间
		String nowData = format.format(new Date(System.currentTimeMillis()));
		client.addHeader("trandt", nowData);
		SimpleDateFormat formatTime = new SimpleDateFormat("HHmmss");
		// 获取当前时间
		String nowTime = formatTime
				.format(new Date(System.currentTimeMillis()));
		client.addHeader("trantm", nowTime);
		client.addHeader("uuid", "");
	}

	public void post(String url, JsonRequestParams params,
			ResponseHandlerInterface responseHandler) {
		getPublicHead();
		client.post(getAbsoluteUrl(url), params, responseHandler);
	}


	public void postGetType(String url, JsonRequestParams params,
			ResponseHandlerInterface responseHandler) {
		getPublicHead();
		client.get(getAbsoluteUrl(url), params, responseHandler);
	}

	/**
	 * 获取绝对路径
	 * 
	 * @param relativeUrl
	 * @return
	 */
	private static String getAbsoluteUrl(String relativeUrl) {
		if (relativeUrl.startsWith("http://")
				|| relativeUrl.startsWith("https://"))
			return relativeUrl;
		return BASE_URL + relativeUrl;
	}


	private static class MySSLSocketFactory extends SSLSocketFactory {
		SSLContext sslContext = SSLContext.getInstance("TLS");

		public MySSLSocketFactory(KeyStore truststore)
				throws NoSuchAlgorithmException, KeyManagementException,
				KeyStoreException, UnrecoverableKeyException {
			super(truststore);
			TrustManager tm = new X509TrustManager() {
				@Override
				public void checkClientTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
				}

				@Override
				public void checkServerTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
				}

				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};
			sslContext.init(null, new TrustManager[] { tm }, null);
		}

		@Override
		public Socket createSocket(Socket socket, String host, int port,
				boolean autoClose) throws IOException, UnknownHostException {
			return sslContext.getSocketFactory().createSocket(socket, host,
					port, autoClose);
		}

		@Override
		public Socket createSocket() throws IOException {
			return sslContext.getSocketFactory().createSocket();
		}
	}

	private AsyncHttpClient factoryHttpClinet() {
		SSLSocketFactory sf = null;
		try {
			sf = new MySSLSocketFactory(KeyStore.getInstance(KeyStore
					.getDefaultType()));
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
		sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		SchemeRegistry registry = new SchemeRegistry();
		registry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 8080));
		registry.register(new Scheme("https", sf, 443));
		AsyncHttpClient client = new AsyncHttpClient(registry);
		client.setTimeout(15000);
		return client;
	}
}
