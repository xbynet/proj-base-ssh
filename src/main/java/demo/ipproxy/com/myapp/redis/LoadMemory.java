package demo.ipproxy.com.myapp.redis;

import java.util.Iterator;
import java.util.Set;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import demo.ipproxy.com.myapp.client.Client;
import demo.ipproxy.com.myapp.proxy.ProxyPool;
import redis.clients.jedis.Jedis;

/**
 * Created by gaorui on 17/1/9.
 */
public class LoadMemory implements Job {


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
//        QuartzManager.pauseTrigger(Timer.job_name_2);
        Jedis jedis = RedisStorage.getInstance();
        Set<String> set = jedis.keys("*");

        Iterator iterator = set.iterator();
        ProxyPool proxyPool = Client.proxyPool;
        while (iterator.hasNext()) {
            String proxyIp = iterator.next().toString().substring(8).split(":")[0];
            int proxyPort = Integer.valueOf(iterator.next().toString().substring(8).split(":")[1]);
            proxyPool.add(proxyIp, proxyPort);
//            jedis.del(iterator.next().toString());

        }

//        QuartzManager.rescheduleJob(Timer.job_name_2);
    }
}
