//package net.xby1993.springmvc.aspect;
//
//import java.lang.reflect.Method;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//import org.springframework.web.context.request.ServletWebRequest;
//
//import net.xby1993.springmvc.annotation.RoleControl;
//import net.xby1993.springmvc.util.GeneUtil;
//import net.xby1993.springmvc.util.StringUtils;
//
//@Component
//@Aspect
//public class RoleControlAspect {
//
//	@Autowired  
//	private HttpServletRequest request; 
//	/**类上注解情形 */
////	@Pointcut("@within(net.xby1993.springmvc.annotation.RoleControl)")
//	@Pointcut("execution(* net.xby1993.springmvc.controller..*.*(..)) && @within(net.xby1993.springmvc.annotation.RoleControl)")
//	public void aspect(){
//		
//	}
//	/**方法上注解情形 */
//	@Pointcut("execution(* net.xby1993.springmvc.controller..*.*(..)) && @annotation(net.xby1993.springmvc.annotation.RoleControl)")
//	public void aspect2(){
//		
//	}
//	/**aop实际拦截两种情形*/
//	@Around("aspect() || aspect2()")
//	public Object doBefore(ProceedingJoinPoint point) {
//		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
//		HttpSession session=request.getSession();
//		Object target = point.getTarget();
//		String method = point.getSignature().getName();
//		Class<?> classz = target.getClass();
//		Method m = ((MethodSignature) point.getSignature()).getMethod();
//		try {
//			if (classz!=null && m != null ) {
//				boolean isClzAnnotation= classz.isAnnotationPresent(RoleControl.class);
//				boolean isMethondAnnotation=m.isAnnotationPresent(RoleControl.class);
//				RoleControl rc=null;
//				//如果方法和类声明中同时存在这个注解，那么方法中的会覆盖类中的设定。
//				if(isMethondAnnotation){
//					rc=m.getAnnotation(RoleControl.class);
//				}else if(isClzAnnotation){
//					rc=classz.getAnnotation(RoleControl.class);
//				}
//				String value=rc.value();
//				Object obj=session.getAttribute(GeneUtil.USERTYPE_KEY);
//				String curUserType=obj==null?"":obj.toString();
//				//进行角色访问的权限控制，只有当前用户是需要的角色才予以访问。
//				boolean isEquals=StringUtils.checkEquals(value, curUserType);
//				if(isEquals){
//					try {
//						return point.proceed();
//					} catch (Throwable e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//				
//		    }
//		}catch(Exception e){
//			
//		}
//		return null;
//	}
//}
