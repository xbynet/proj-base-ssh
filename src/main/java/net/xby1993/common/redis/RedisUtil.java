package net.xby1993.common.redis;

import java.io.Serializable;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;


@Component
public class RedisUtil {
	
	@Autowired
    private  JedisTemplate jedisTemplate;
	
 
    private static class RedisUtilHolder{  
        private static RedisUtil instance = new RedisUtil();  
    }  
   
    public static RedisUtil getInstance() {  
        return RedisUtilHolder.instance;  
    } 
   
    
    public Object get(final String key){
    	return jedisTemplate.execute(new JedisAction<Object>() {
			@Override
			public Object action(Jedis client) {
				return client.get(key);
			}
		});
    }
    public Object get(final String key,final int expireInSec){
    	return jedisTemplate.execute(new JedisAction<Object>() {
			@Override
			public Object action(Jedis client) {
				Object res;
				synchronized (jedisTemplate) {
					if((res=client.get(key))!=null){
						client.expire(key, expireInSec);
					}
				}
				return res;
			}
		});
    }
    public Object getSeria(final byte[] key){
    	Object obj= jedisTemplate.execute(new JedisAction<Object>() {
			@Override
			public Object action(Jedis client) {
				byte[] tmp=client.get(key);
				Object obj=JedisSerializerUtil.fromBinary(tmp);
				return obj;
			}
		});
    	if(obj instanceof byte[]){
    		return JedisSerializerUtil.fromBinary((byte[])obj);
    	}else{
    		return obj;
    	}
    }
    public Object getSeria(final byte[] key,final int expireInsec){
    	return jedisTemplate.execute(new JedisAction<Object>() {
			@Override
			public Object action(Jedis client) {
				byte[] res;
				synchronized (jedisTemplate) {
					res=client.get(key);
					if(res!=null && res.length>0){
						client.expire(key, expireInsec);
					}
				}
				return JedisSerializerUtil.fromBinary(res);
			}
		});
    }
    public void set(final String key,final String value,final int expireInsec){
    	jedisTemplate.execute(new JedisAction<Void>() {
			@Override
			public Void action(Jedis client) {
				client.setex(key, expireInsec, value);
				return null;
			}
		});
    }
    public void set(final String key,final String value){
    	jedisTemplate.execute(new JedisAction<Void>() {
			@Override
			public Void action(Jedis client) {
				client.set(key, (String)value);
				return null;
			}
		});
    }
    public void setSeria(final byte[] key,final Serializable value){
    	jedisTemplate.execute(new JedisAction<Void>() {
			@Override
			public Void action(Jedis client) {
				client.set(key, JedisSerializerUtil.toBinary(value));
				return null;
			}
		});
    }
    public void setSeria(final byte[] key,final Serializable value,final int expireInsec){
    	jedisTemplate.execute(new JedisAction<Void>() {
			@Override
			public Void action(Jedis client) {
				client.setex(key,expireInsec,JedisSerializerUtil.toBinary(value));
				return null;
			}
		});
    }
    public void delete(final byte[]... keys){
    	jedisTemplate.execute(new JedisAction<Void>() {
			@Override
			public Void action(Jedis client) {
				client.del(keys);
				return null;
			}
		});
    }
    public void delete(final String... keys){
    	jedisTemplate.execute(new JedisAction<Void>() {
			@Override
			public Void action(Jedis client) {
				client.del(keys);
				return null;
			}
		});
    }
    public Set<byte[]> keys(final byte[] key){
    	return jedisTemplate.execute(new JedisAction<Set<byte[]>>() {
			@Override
			public Set<byte[]> action(Jedis client) {
				return client.keys(key);
			}
		});
    }
}
