package net.xby1993.common.shiro;
import java.util.concurrent.ConcurrentHashMap;  
import java.util.concurrent.ConcurrentMap;  

import org.apache.shiro.cache.Cache;  
import org.apache.shiro.cache.CacheException;  
import org.apache.shiro.cache.CacheManager;  
import org.slf4j.Logger;  
import org.slf4j.LoggerFactory;  

public class RedisShiroCacheManager implements CacheManager{  

    private static final Logger logger = LoggerFactory  
            .getLogger(RedisShiroCacheManager.class);  
    private final ConcurrentMap<String, Cache> caches = new ConcurrentHashMap<String, Cache>();  
    private String keyPrefix = "shiro_redis_cache:";  
    public String getKeyPrefix() {  
        return keyPrefix;  
    }  
    public void setKeyPrefix(String keyPrefix) {  
        this.keyPrefix = keyPrefix;  
    }  

    @Override  
    public <K, V> Cache<K, V> getCache(String name) throws CacheException {  
        logger.debug("获取名称为: " + name + " 的RedisCache实例");  
        Cache c = caches.get(name);  
        if (c == null) {  
            c = new RedisShiroCache<K, V>(keyPrefix);  
            caches.put(name, c);  
        }  
        return c;  
    }  
}

