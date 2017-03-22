package net.xby1993.common.redis;

import java.util.List;

/**
 * Created by taojw.
 */
public interface JedisTemplate {
    <T> T execute(JedisAction<T> action);

    /**
     * 在事务中执行
     * @param action
     * @param watchKeys 需要被监变化的key
     * @return
     */
    List<Object> executeInTransaction(JedisTransactionAction action,String... watchKeys);

    /**
     * 在Pipeline中批处理
     * @param action
     * @return
     */
    List<Object> executeInPipeline(JedisPipelineAction action);
}
