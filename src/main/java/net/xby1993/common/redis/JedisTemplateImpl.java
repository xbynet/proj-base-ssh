package net.xby1993.common.redis;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Transaction;

/**
 * Created by taojw .
 */
@Component
public class JedisTemplateImpl implements JedisTemplate{
    private static final Logger log = LoggerFactory.getLogger(JedisTemplateImpl.class);
    @Autowired
    private JedisDataSource ds;

    @Override
    public <T> T execute(JedisAction<T> action){
        Jedis client=ds.getClient();
        T res=null;
        try {
            res=action.action(client);
        } finally {
            close(client);
        }
        return res;
    }

    /**
     * 在事务中执行。请不要在自己具体action中进行事务提交等操作。统一由该模板方法来提交。
     * @param action
     * @param watchKeys 需要被监变化的key,可为空
     * @return
     */
    @Override
    public List<Object> executeInTransaction(JedisTransactionAction action,String... watchKeys) {
        Jedis client=ds.getClient();
        if(watchKeys!=null && watchKeys.length>0){
            client.watch(watchKeys);
        }
        Transaction tx=client.multi();
        List<Object> reslist=null;
        try {
            action.action(tx);
            reslist = tx.exec();
        } finally {
            try {
                tx.close();
            } catch (IOException e) {
                e.printStackTrace();
                log.warn("事务关闭过程中出现异常",e);
            }
            if(watchKeys!=null && watchKeys.length>0){
                client.unwatch();
            }
            close(client);
        }

        return reslist;
    }

    /**
     * pipeline批处理。
     * @param action
     * @return
     */
    @Override
    public List<Object> executeInPipeline(JedisPipelineAction action) {
        Jedis client=ds.getClient();
        Pipeline pipeline=client.pipelined();
        List<Object> reslist=null;
        try {
            action.action(pipeline);
            reslist = pipeline.syncAndReturnAll();
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("批处理过程中发生异常",e);
        } finally {
            try {
                pipeline.close();
            } catch (IOException e) {
                e.printStackTrace();
                log.warn("pipeline关闭过程中发生异常",e);
            }
            close(client);
        }
        return reslist;
    }

    private void close(Jedis client){
        try {
            ds.releaseClient(client);
        } catch (Exception e) {
            log.error("关闭Jedis客户端时发生异常",e);
        }
    }
}
