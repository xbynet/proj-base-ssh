package net.xby1993.common.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import net.xby1993.common.util.SpringContextHolder;

/**
 * Created by taojw.
 */
public class RedisSessionRequestWrapper extends HttpServletRequestWrapper {
    private HttpServletRequest request;
    private RedisSession redisSession;
    public RedisSessionRequestWrapper(HttpServletRequest request) {
        super(request);
        this.request=request;
    }

    /**
     * 获取RedisSession。
     * @return
     */
    public RedisSession getRedisSession(){
        String id=request.getSession(true).getId();
        if(redisSession ==null) {
            /*延迟初始化以提高性能(仅在需要操作session进行存储时才初始化加载)*/
            redisSession = SpringContextHolder.getBean(RedisSessionRepository.class).getSession(id);
        }
        return redisSession;
    }

    /**
     * 过滤器是否需要保存，此方法为提高性能。
     * @return
     */
    public boolean shouldFilterSave(){
        return redisSession!=null;
    }
}
