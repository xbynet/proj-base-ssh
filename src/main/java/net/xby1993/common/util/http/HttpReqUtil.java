package net.xby1993.common.util.http;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpReqUtil {
	private static final Logger log=LoggerFactory.getLogger(HttpReqUtil.class);
	
	public static String getRawContent(String url,String method,Map<String,Object> params) {
		return getRawContent(url,method,params,null);
	}
	public static String getRawContent(String url,String method,Map<String,Object> params,Map<String,Object> headers) {
		String content=null;
		CloseableHttpClient client = HttpClients.custom().setRedirectStrategy(new CustomRedirectStrategy()).build();
		
		RequestBuilder requestBuilder=null;
		method=method==null?"get":method; 
		if("get".equalsIgnoreCase(method)){
			requestBuilder= RequestBuilder.get();
			if (params != null) {
				   for (Map.Entry<String, Object> entry : params.entrySet()) {
				        requestBuilder.addParameter(entry.getKey(), entry.getValue().toString());
				    }
			}
			
		}else if("post".equalsIgnoreCase(method)){
			requestBuilder = RequestBuilder.post();
            List<BasicNameValuePair> allNameValuePair=new ArrayList<BasicNameValuePair>();
            if (params!= null) {
                for (String key : params.keySet()) {
                    allNameValuePair.add(new BasicNameValuePair(key, params.get(key).toString()));
                }
            }
            requestBuilder.setEntity(new UrlEncodedFormEntity(allNameValuePair, Charset.forName("utf8")));
		}else{
			return null;
		}
		requestBuilder.setUri(url).setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
		RequestConfig.Builder requestConfigBuilder = RequestConfig.custom()
                .setConnectionRequestTimeout(3000)
                .setSocketTimeout(3000)
                .setConnectTimeout(3000)
                .setCookieSpec(CookieSpecs.BEST_MATCH);
		
		
		if(headers!=null){
			for(String key:headers.keySet()){
				requestBuilder.setHeader(key, headers.get(key).toString());
			}
		}
		requestBuilder.setConfig(requestConfigBuilder.build());
		
		
		CloseableHttpResponse resp=null;
		InputStream ins=null;
		try {
			resp = client.execute(requestBuilder.build());
			ins = resp.getEntity().getContent();
			String charset=CharsetDetectUtil.detectCharset(resp.getEntity().getContentType().getValue(), IOUtils.toByteArray(ins));
			if(StringUtils.isBlank(charset)){
				charset="UTF-8";
			}
			content = IOUtils.toString(ins,charset);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			log.error("",e);
		} catch (IOException e) {
			e.printStackTrace();
			log.error("", e);
		}finally {
			IOUtils.closeQuietly(ins);
			EntityUtils.consumeQuietly(resp.getEntity());
			close(resp);
			close(client);
		}
		return content;
	}
	public static void close(Closeable obj){
		if(obj!=null){
			try{
				obj.close();
			}catch(Exception e){
				log.info("",e);
			}
		}
	}
	public static void sleep(int milliseconds){
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


}
