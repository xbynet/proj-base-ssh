
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.xby1993.common.redis.JedisAction;
import net.xby1993.common.redis.JedisTemplate;
import net.xby1993.common.redis.LuaScript;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import redis.clients.jedis.Jedis;

/**
 * Redis Lua脚本使用示例
 * Created by taojw .
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
//@Component
public class LuaTest {
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = webAppContextSetup(this.wac).build();
    }

    private LuaScript acquireLock=LuaScript.getInstance("-- 检测锁是否已经存在。KEYS参数为锁键，ARGV参数为过期时间与标识符组成的表格\n" +
            "if redis.call('exists',KEYS[1])==0 then\n" +
            "    redis.call('setex',KEYS[1],unpack(ARGV))\n" +
            "    return 1\n"+
            "end\n" +
            "return 0");
    private LuaScript releaseLock=LuaScript.getInstance("-- 释放锁\n" +
            "if redis.call('get',KEYS[1])==ARGV[1] then\n" +
            "    return redis.call('del',KEYS[1]) or 1\n" +
            "end\n" +
            "return 1");

    @Autowired
    private JedisTemplate jedisTemplate;

    @Test
    public void test(){
        int retryTimes=10;
        boolean acquired=false;
        final List<String> keys=new ArrayList<String>();
        keys.add("lock:key:test1");
        final List<String> lockargs=new ArrayList<String>();
        lockargs.add(String.valueOf(300));
        lockargs.add(UUID.randomUUID().toString());

        final List<String> releaseArgs=new ArrayList<String>();
        releaseArgs.add(lockargs.get(1));
        while(!acquired && retryTimes>0){
            retryTimes--;
            acquired=jedisTemplate.execute(new JedisAction<Boolean>() {
                @Override
                public Boolean action(Jedis client) {
                    long res=(Long)(acquireLock.execute(client,keys,lockargs));
                    return 1==res;
                }
            });
        }
        if(acquired){
            System.out.println("获取锁成功");
            jedisTemplate.execute(new JedisAction<Void>() {
                @Override
                public Void action(Jedis client) {
                    releaseLock.execute(client,keys,releaseArgs);
                    return null;
                }
            });
        }else{
            System.out.println("获取锁失败");
        }
    }
}
