package net.xby1993.common.util.http;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;


public class HttpClientFactory {
	
	public static CloseableHttpClient getSSLIngnoreClient(){
		RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.<ConnectionSocketFactory>create();  
		registryBuilder.register("http", PlainConnectionSocketFactory.INSTANCE);
		// Fixing: https://code.google.com/p/crawler4j/issues/detail?id=174
		                // By always trusting the ssl certificate
		SSLContext sslContext=null;
		try {
			sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustStrategy() {
			    @Override
			    public boolean isTrusted(final X509Certificate[] chain, String authType) {
			        return true;
			    }
			}).build();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
		SSLConnectionSocketFactory sslsf=new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
		registryBuilder.register("https", sslsf);
		Registry<ConnectionSocketFactory> registry = registryBuilder.build();  
		//设置连接管理器  
		PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager(registry);  
		poolingHttpClientConnectionManager.setMaxTotal(500);
		poolingHttpClientConnectionManager.setDefaultMaxPerRoute(1000);
		//构建客户端  
		CloseableHttpClient client= HttpClients.custom().setConnectionManager(poolingHttpClientConnectionManager).build();  
		return client;
	}

}
