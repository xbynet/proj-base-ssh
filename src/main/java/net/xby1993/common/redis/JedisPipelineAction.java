package net.xby1993.common.redis;

import redis.clients.jedis.Pipeline;

/**
 * Created by taojw.
 */
public interface JedisPipelineAction extends IJedisAction{
    void action(Pipeline client);
}
