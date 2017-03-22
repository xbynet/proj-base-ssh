package net.xby1993.common.shiro;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.xby1993.common.redis.JedisSerializerUtil;
import net.xby1993.common.redis.RedisUtil;
import net.xby1993.common.util.SpringContextHolder;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedisSessionDAO extends AbstractSessionDAO {  

    private static Logger logger = LoggerFactory.getLogger(RedisSessionDAO.class);  
    private RedisUtil ru;
    private String keyPrefix = "shiro_redis_session:";  
    
    public  RedisSessionDAO() {
    	super();
    	ru=(RedisUtil) SpringContextHolder.getApplicationContext().getBean("redisUtil");
	}
    @Override  
    public void update(Session session) throws UnknownSessionException {  
        this.saveSession(session);  
    }  
 
    private void saveSession(Session session) throws UnknownSessionException{  
        if(session == null || session.getId() == null){  
            logger.error("session or session id is null");  
            return;  
        }  
        byte[] key = getByteKey(session.getId());  
        byte[] value = JedisSerializerUtil.toBinary(session);
        ru.setSeria(key, value, (int)(session.getTimeout()/1000));
    }  

    @Override  
    public void delete(Session session) {  
        if(session == null || session.getId() == null){  
            logger.error("session or session id is null");  
            return;  
        }  
        ru.delete(this.getByteKey(session.getId()));

    }  

    //用来统计当前活动的session  
    @Override  
    public Collection<Session> getActiveSessions() {  
        Set<Session> sessions = new HashSet<Session>();  
        Set<byte[]> keys = ru.keys((keyPrefix + "*").getBytes());  
        if(keys != null && keys.size()>0){  
            for(byte[] key:keys){  
                Session s = (Session)ru.getSeria(key);  
                sessions.add(s);  
            }  
        }  

        return sessions;  
    }  

    @Override  
    protected Serializable doCreate(Session session) {  
        Serializable sessionId = this.generateSessionId(session);    
        this.assignSessionId(session, sessionId);  
        this.saveSession(session);  
        return sessionId;  
    }  

    @Override  
    protected Session doReadSession(Serializable sessionId) {  
        if(sessionId == null){  
            logger.error("session id is null");  
            return null;  
        }  
        
        Object s = ru.getSeria(this.getByteKey(sessionId));
        Session session=null;
      //getSeria该方法有问题，原因未知
        if(s instanceof byte[]){
        	session=(Session)JedisSerializerUtil.fromBinary((byte[])s); 
        }else{
        	session=(Session)s;
        }
        return session;  
    }  

    /** 
     * 获得byte[]型的key 
     * @param key 
     * @return 
     */  
    private byte[] getByteKey(Serializable sessionId){  
        String preKey = this.keyPrefix + sessionId;  
        return preKey.getBytes();  
    }  


    /** 
     * Returns the Redis session keys 
     * prefix. 
     * @return The prefix 
     */  
    public String getKeyPrefix() {  
        return keyPrefix;  
    }  

    /** 
     * Sets the Redis sessions key  
     * prefix. 
     * @param keyPrefix The prefix 
     */  
    public void setKeyPrefix(String keyPrefix) {  
        this.keyPrefix = keyPrefix;  
    }  
}  
