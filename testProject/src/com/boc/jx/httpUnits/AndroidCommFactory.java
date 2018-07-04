package com.boc.jx.httpUnits;

import java.io.InputStream;
import java.security.KeyStore;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;

import com.boc.jx.base.BaseActivity;
import com.bocop.jxplatform.config.BocSdkConfig;


/**
 * 
 * @author zhongye
 *
 */
public class AndroidCommFactory
{
	public static final int COMM_TYPE_SOCK = 0;
	public static final int COMM_TYPE_HTTP = 1;
	public static final int COMM_TYPE_HTTPS = 2;
	private static final String CLIENT_TRUST_PASSWORD = "formssi";// 信任证书密码
	private static final String CLIENT_KEY_KEYSTORE = "BKS";
	private HttpClient httpsClient;
	private HttpClient httpClient;
	private ConnCommMachine CommMachine;

	public AndroidCommFactory()
	{
		CommMachine = null;
	}

	/**
	 * 方法描述 :创建连接器
	 * @param CommType   连接器类型
	 * @return  连接器实例
	 * 
	 */
	public ConnCommMachine CreateNewCommMachine(int CommType)
	{
		switch (CommType)
		{

			case COMM_TYPE_HTTPS:

				CommMachine = new ConnCommMachine(getHttpsInstance());
				break;

			case COMM_TYPE_HTTP:

				CommMachine = new ConnCommMachine(getHttpInstance());
				break;

			default:
				CommMachine = null;
				break;
		}

		return CommMachine;
	}

	/**
	 * 方法描述 :创建连接器（带自动重连3次的）
	 * @param CommType   连接器类型
	 * @param istrue 是否开启3次重连机制
	 * @return  连接器实例
	 * 
	 */
	public ConnCommMachine CreateNewReconCommMachine(int CommType, boolean istrue)
	{
		switch (CommType)
		{

			case COMM_TYPE_HTTPS:

				CommMachine = new ConnCommMachine(getHttpsInstance(), istrue);
				break;

			case COMM_TYPE_HTTP:

				CommMachine = new ConnCommMachine(getHttpInstance(), istrue);
				break;

			default:
				CommMachine = null;
				break;
		}

		return CommMachine;
	}

	/**
	 * 方法描述 :获取HttpClient
	 * @return  HttpClient
	 * 
	 */
	private HttpClient getHttpInstance()
	{
		if (httpClient != null && httpClient.getConnectionManager() != null)
		{
			return httpClient;
		}
		else
		{
			SchemeRegistry schReg = new SchemeRegistry();
			schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			HttpParams params = new BasicHttpParams();
			ClientConnectionManager manager = new ThreadSafeClientConnManager(params, schReg);
			httpClient = new DefaultHttpClient(manager, params);
			if (null != httpClient)
			{
				httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, BocSdkConfig.REQUEST_TIMEOUT);
				httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, BocSdkConfig.RESPONSE_TIMEOUT);

			}
			return httpClient;
		}
	}

	/**
	 * 方法描述 :获取HttpsClient
	 * @return  HttpsClient
	 * 
	 */
	private HttpClient getHttpsInstance()
	{
		if (httpsClient != null && httpsClient.getConnectionManager() != null)
		{
			return httpsClient;
		}
		else
		{
			SchemeRegistry schemeRegistry = new SchemeRegistry();
			SocketFactory socketFactory = (SocketFactory) newSslSocketFactory();
			if (null == socketFactory)
			{
				httpsClient = null;
				return null;
			}
			schemeRegistry.register(new Scheme("https", socketFactory, BocSdkConfig.HTTPS_PORT));
			HttpParams params = new BasicHttpParams();
			ClientConnectionManager manager = new ThreadSafeClientConnManager(params, schemeRegistry);
			httpsClient = new DefaultHttpClient(manager, params);

			if (null != httpsClient)
			{
				httpsClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, BocSdkConfig.REQUEST_TIMEOUT);
				httpsClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, BocSdkConfig.RESPONSE_TIMEOUT);

			}
		}
		return httpsClient;

	}

	/**
	 * 方法描述 :ssL安全认证
	 * @return  SSLSocketFactory
	 * 
	 */
	private SSLSocketFactory newSslSocketFactory()
	{
		try
		{
			// Get an instance of the Bouncy Castle KeyStore format
			KeyStore trusted = KeyStore.getInstance(CLIENT_KEY_KEYSTORE);
			InputStream in = (BaseActivity.class).getResourceAsStream(BocSdkConfig.BKS_RES_PATH);
			try
			{
				trusted.load(in, CLIENT_TRUST_PASSWORD.toCharArray());
			}
			finally
			{
				if(in != null)
				{
					in.close();
				}
			}
			SSLSocketFactory sf = new SSLSocketFactory(trusted);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			return sf;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public void releaseClient()
	{
		if (httpsClient != null && httpsClient.getConnectionManager() != null)
		{
			httpsClient.getConnectionManager().shutdown();
		}
		if (httpClient != null && httpClient.getConnectionManager() != null)
		{
			httpClient.getConnectionManager().shutdown();
		}
	}
}
