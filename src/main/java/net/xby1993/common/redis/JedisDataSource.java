package net.xby1993.common.redis;

import redis.clients.jedis.Jedis;
/**
 * Created by taojw .
 */
public interface JedisDataSource {
    /**
     * 获取客户端
     * @return
     */
    Jedis getClient();

    /**
     * 释放连接池
     */
    void releaseClient(Jedis client);
    /**
     *关闭Redis连接池
     */
    void shutdown();
}
