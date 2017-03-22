package net.xby1993.common.session;

import java.nio.charset.Charset;

import net.xby1993.common.redis.JedisAction;
import net.xby1993.common.redis.JedisSerializerUtil;
import net.xby1993.common.redis.JedisTemplate;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;

/**
 * Created by taojw
 */
@Component
public class RedisSessionRepository implements SessionRepository<RedisSession> {

    /**
     * session超时时间,单位分钟
     */
    private final int SESSION_TIMEOUT = 30;

    @Autowired
    private JedisTemplate jedisTemplate;


    @Override
    public void save(final RedisSession session) {
        jedisTemplate.execute(new JedisAction<Void>() {
            @Override
            public Void action(Jedis client) {
                client.setex(session.getId().getBytes(Charset.forName("UTF-8")), SESSION_TIMEOUT * 60, JedisSerializerUtil.toBinary(session));
                return null;
            }
        });
    }

    @Override
    public RedisSession getSession(final String id) {
        RedisSession session = jedisTemplate.execute(new JedisAction<RedisSession>() {
            @Override
            public RedisSession action(Jedis client) {
                RedisSession tmp=null;
                if (StringUtils.isBlank(id) || !client.exists(id)) {
                    if(StringUtils.isNotBlank(id)){
                        tmp=new RedisSession(id);
                    }else{
                        tmp=new RedisSession();
                    }
                    client.setex(tmp.getId().getBytes(Charset.forName("UTF-8")), SESSION_TIMEOUT * 60, JedisSerializerUtil.toBinary(tmp));
                }else{
                    tmp= (RedisSession) JedisSerializerUtil.fromBinary(client.get(id.getBytes(Charset.forName("UTF-8"))));
                }
                return tmp;
            }
        });
        return session;
    }
    @Override
    public void delete(final String id) {
        jedisTemplate.execute(new JedisAction<Void>() {
            @Override
            public Void action(Jedis client) {
                client.del(id);
                return null;
            }
        });
    }

}
