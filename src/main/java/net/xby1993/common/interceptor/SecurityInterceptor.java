/*package net.xby1993.common.interceptor;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.xby1993.common.annotation.RoleControl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class SecurityInterceptor  extends HandlerInterceptorAdapter{
	private static Logger log=LoggerFactory.getLogger(GlobalInterceptor.class);
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession s=request.getSession();
		//角色权限控制访问
//		return roleControl(request,response,handler);
		return true;
	}
	*//**角色权限控制访问*//*
	private boolean roleControl(HttpServletRequest request,HttpServletResponse response, Object handler){
		HttpSession session=request.getSession();
		System.out.println(handler.getClass().getName());
		if(handler instanceof HandlerMethod){
			HandlerMethod hm=(HandlerMethod)handler;
			Object target=hm.getBean();
			Class<?> clazz=hm.getBeanType();
			Method m=hm.getMethod();
			try {
				if (clazz!=null && m != null ) {
					boolean isClzAnnotation= clazz.isAnnotationPresent(RoleControl.class);
					boolean isMethondAnnotation=m.isAnnotationPresent(RoleControl.class);
					RoleControl rc=null;
					//如果方法和类声明中同时存在这个注解，那么方法中的会覆盖类中的设定。
					if(isMethondAnnotation){
						rc=m.getAnnotation(RoleControl.class);
					}else if(isClzAnnotation){
						rc=clazz.getAnnotation(RoleControl.class);
					}
					String value=rc.value();
					//进行角色访问的权限控制，只有当前用户是需要的角色才予以访问。
					if(!GeneUtil.hasRoleInSession(value, session)){
						//401未授权访问
						response.setStatus(401);
						return false;
					}
			    }
			}catch(Exception e){
				
			}
		}
		
		return true;
	}

}
*/