package net.xby1993.common.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取RedisSession的辅助工具类。
 * Created by taojw on 2017/1/10.
 */
public class SessionUtil {
    private static final Logger log= LoggerFactory.getLogger(SessionUtil.class);
    /**
     * 已被废弃，请使用新的接口get()
     * @param request
     * @return
     */
    @Deprecated
    public static RedisSession getRedisSession(HttpServletRequest request){
        if(request instanceof RedisSessionRequestWrapper){
            RedisSession session=((RedisSessionRequestWrapper) request).getRedisSession();
            return session;
        }
        RedisSessionException e=new RedisSessionException("无法获取RedisSession实例，可能是没有配置RedisSessonFilter或拦截路径配置错误");
        log.error("无法获取RedisSession实例，可能是没有配置RedisSessonFilter或拦截路径配置错误",e);
        throw e;
    }
    public static RedisSession get(){
    	return getRedisSession(getRequest());
    }
    /**
	 * 得到request对象
	 */
	public static HttpServletRequest getRequest() {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();	
		return request;
	}
	public static boolean isLogined(){
		return get().getAttribute("user")!=null;
	}
}
