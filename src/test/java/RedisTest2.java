import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.io.UnsupportedEncodingException;

import net.xby1993.common.redis.JedisAction;
import net.xby1993.common.redis.JedisSerializerUtil;
import net.xby1993.common.redis.JedisTemplate;
import net.xby1993.common.session.RedisSession;

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
 * Created by taojw.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class RedisTest2 {
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Autowired
    private JedisTemplate jedisTemplate;

    @Before
    public void setup() {
        this.mockMvc = webAppContextSetup(this.wac).build();
    }
    @Test
    public void testByte(){
        final RedisSession session=new RedisSession();
        jedisTemplate.execute(new JedisAction<Void>() {
            @Override
            public Void action(Jedis client) {
                try {
                    byte[] kbytes=session.getId().getBytes("UTF-8");
                    byte[] sbytes=JedisSerializerUtil.toBinary(session);
                    client.setex(kbytes,30*60, sbytes);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }
    @Test
    public void testSession() throws Exception {
        mockMvc.perform((get("/test/index")));
    }

}
