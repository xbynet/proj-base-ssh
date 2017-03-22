package net.xby1993.common.redis;

import redis.clients.jedis.Jedis;

/**
 * Created by taojw .
 */
public interface JedisAction<T> extends IJedisAction{
    T action(Jedis client);
}
