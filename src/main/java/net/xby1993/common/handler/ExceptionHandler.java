package net.xby1993.common.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 全局异常处理拦截器
 */
public class ExceptionHandler implements HandlerExceptionResolver {
    private static final Logger log= LoggerFactory.getLogger(ExceptionHandler.class);
    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler, Exception e) {
        if(handler instanceof HandlerMethod){
            HandlerMethod hm = (HandlerMethod ) handler ;
            String type=hm.getBeanType().getName();
            String method=hm.getMethod().getName();
            log.error("执行"+type+method+"时发生未捕获异常",e);
        }
        ModelAndView mv=new ModelAndView("/exception");
        mv.addObject("url",httpServletRequest.getRequestURL());
        return null;
    }


}
