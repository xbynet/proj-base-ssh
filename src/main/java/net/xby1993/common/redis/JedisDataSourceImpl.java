package net.xby1993.common.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.PreDestroy;

/**
 * Created by taojw .
 */
@Component
public class JedisDataSourceImpl implements  JedisDataSource{
    private static final Logger log= LoggerFactory.getLogger(JedisDataSourceImpl.class);
    @Autowired
    private JedisPool jedisPool;
    @Override
    public Jedis getClient() {
        Jedis client=null;
        try{
            client=jedisPool.getResource();
            return client;
        }catch (Exception e){
            log.error("获取reedis连接时发生异常",e);
            if(client!=null){
                client.close();
                client=null;
            }
        }
        return null;
    }

    @Override
    public void releaseClient(Jedis client) {
        /**关于close()方法，从源码和官方文档中可以看出，如果当前实例是从连接池返回的，那么将对象返回连接池。否则就是断开连接并关闭。
        if(this.dataSource != null) {
            if(this.client.isBroken()) {
                this.dataSource.returnBrokenResource(this);
            } else {
                this.dataSource.returnResource(this);
            }
        } else {
            this.client.close();
        }
        */
        client.close();
    }
    @PreDestroy
    @Override
    public void shutdown() {
        jedisPool.destroy();
    }
}
