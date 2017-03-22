package net.xby1993.common.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;

public class ReqUtil {
	public static HttpServletRequest getRequest() {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();	
		return request;
	}
	public static HttpServletResponse getResponse(){
		HttpServletResponse resp = ((ServletWebRequest)RequestContextHolder.getRequestAttributes()).getResponse();
		return resp;
	}
	public static String getParam(String name){
		HttpServletRequest req=getRequest();
		Object tmp=req.getParameter(name);
		return tmp==null?"":tmp.toString();
	}
}
