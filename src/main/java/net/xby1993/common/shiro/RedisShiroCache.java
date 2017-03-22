package net.xby1993.common.shiro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.xby1993.common.redis.JedisPipelineAction;
import net.xby1993.common.redis.JedisSerializerUtil;
import net.xby1993.common.redis.JedisTemplate;
import net.xby1993.common.redis.RedisUtil;
import net.xby1993.common.util.SpringContextHolder;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Pipeline;

public class RedisShiroCache<K,V> implements Cache<K,V> {  

    private static final Logger logger = LoggerFactory.getLogger(RedisShiroCache.class);  
    private String keyPrefix = "shiro_redis_cache:";
    private Set<byte[]> keylist=new HashSet<byte[]>();
    private JedisTemplate jt;
    private RedisUtil ru;
    public String getKeyPrefix() {  
        return keyPrefix;  
    }  

    public void setKeyPrefix(String keyPrefix) {  
        this.keyPrefix = keyPrefix;  
    }  

    public RedisShiroCache(){  
    	jt=(JedisTemplate) SpringContextHolder.getApplicationContext().getBean("jedisTemplateImpl"); 
    	ru=(RedisUtil) SpringContextHolder.getApplicationContext().getBean("redisUtil"); 
    }  

    public RedisShiroCache(String prefix){  
        this();    
        this.keyPrefix = prefix;  
    }  

    @SuppressWarnings("unchecked")
	@Override  
    public V get(K key) throws CacheException {  
    	return (V) ru.getSeria(getByteKey(key));
    }  

    @Override  
    public V put(K key, V value) throws CacheException {
    	byte[] k=getByteKey(key);
    	synchronized (keylist) {
    		keylist.add(k);			
		}
    	ru.setSeria(k, (Serializable)value);
    	return value;
    }  

    @Override  
    public V remove(K key) throws CacheException {  
        logger.debug("从redis中删除 key [" + key + "]");  
        try {  
            V previous = get(key);
            byte[] k=getByteKey(key);
            ru.delete(k);
            synchronized (keylist) {
            	keylist.remove(k);				
			}
            return previous;  
        } catch (Throwable t) {  
            throw new CacheException(t);  
        }  
    }  

    @Override  
    public void clear() throws CacheException {  
        logger.debug("从redis中删除所有元素");  
        try {
        	byte[][] keys=(byte[][])keylist.toArray();
            ru.delete(keys);  
        } catch (Throwable t) {  
            throw new CacheException(t);  
        }  
    }  

    @Override  
    
    public int size() {  
        try {  
            return keylist.size();  
        } catch (Throwable t) {  
            throw new CacheException(t);  
        }  
    }  

    @SuppressWarnings("unchecked")  
    @Override  
    public Set<K> keys() {
    	Set<K> set=new HashSet<K>();
    	synchronized (keylist) {
    		for(byte[] k:keylist){
    			set.add((K)k);
    		}
    	}
    	return set;
    }  

    @SuppressWarnings("unchecked")
	@Override  
    public Collection<V> values() {  
    	List<Object> list=jt.executeInPipeline(new JedisPipelineAction() {
			
			@Override
			public void action(Pipeline client) {
				for(byte[] key:keylist){
					client.get(key);
				}
			}
		});
    	Collection<V> col=new ArrayList<V>();
    	for(Object obj:list){
    		if(obj!=null){
    			col.add((V)JedisSerializerUtil.fromBinary((byte[])obj));
    		}else{
    			col.add((V)obj);
    		}
    	}
    	return col;
    	
    }
    private byte[] getByteKey(K key){  
        if(key instanceof String){  
            String preKey = this.keyPrefix + key;  
            return preKey.getBytes();  
        }else{  
            return JedisSerializerUtil.toBinary(key);  
        }  
    }
    public static void main(String[] args) {
		System.out.println((String)null);
	}
}  