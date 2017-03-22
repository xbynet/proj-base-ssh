package net.xby1993.common.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 除登录之外，其余非特殊拦截工作在此处理。
 */
public class GlobalInterceptor extends HandlerInterceptorAdapter {
    private final Logger log = LoggerFactory.getLogger(GlobalInterceptor.class);
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        initContextPath(request);
        recordLog(request,handler);
        return super.preHandle(request, response, handler);
    }
    private void initContextPath(HttpServletRequest request){
        String webRoot = request.getSession().getServletContext().getRealPath("/");
        if(webRoot == null){
            webRoot = this.getClass().getClassLoader().getResource("/").getPath();
            webRoot = webRoot.substring(0,webRoot.indexOf("WEB-INF"));
        }
        log.debug("context path为"+webRoot);
        request.setAttribute("webRoot",webRoot);
    }
    private void recordLog(HttpServletRequest request,Object handler){
        if(handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler;
//            Object target = hm.getBean();
            Class<?> clazz = hm.getBeanType();
            Method m = hm.getMethod();
            String ip=request.getRemoteAddr();
            ip=ip==null?"":ip;
            log.info("访问者ip："+ip+"; url:"+request.getRequestURL());
            log.debug("进入方法:"+clazz.getName()+"@"+m.getName()+";请求类型为"+(isAjax(request)?"ajax":"normal"));
        }
    }
    private boolean isAjax(HttpServletRequest request){
        if ("XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"))) {
            return true;
        }
        return false;
    }
}
