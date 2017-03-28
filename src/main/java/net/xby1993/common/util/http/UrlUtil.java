package net.xby1993.common.util.http;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpHost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpCoreContext;

public class UrlUtil {
	public static String getUrlDomain(String url){
		Pattern p=Pattern.compile("http[s]?://([a-zA-Z0-9\\._]+)/.*");
		Matcher m=p.matcher(url);
		if(m.find()){
			String domain=m.group(1);
			String[] tmp=domain.split("\\.");
			return tmp[tmp.length-2];
		}
		return null;
	}
	/**
	 * 即使发生重定向，也能获取最终的真实请求地址
	 * @param httpContext
	 * @return
	 */
	public String getRealUrl(HttpContext httpContext){
        Object target = httpContext.getAttribute(HttpCoreContext.HTTP_TARGET_HOST);
        Object reqUri = httpContext.getAttribute(HttpCoreContext.HTTP_REQUEST);
        if (target==null||reqUri==null){
            return null;
        }
        HttpHost t = (HttpHost) target;
        HttpUriRequest r = (HttpUriRequest)reqUri;
        return r.getURI().isAbsolute()?r.getURI().toString():t.toString()+r.getURI().toString();
    }
}
