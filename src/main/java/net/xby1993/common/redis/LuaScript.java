package net.xby1993.common.redis;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;

import redis.clients.jedis.Jedis;

/**
 * lua脚本调用封装类
 * Created by taojw .
 */
public class LuaScript {
    private static final ConcurrentHashMap<String,String> scriptCache=new ConcurrentHashMap<String,String>();
    private String curScript;

    private LuaScript(String script){
        this.curScript=script;
    }
    public static LuaScript getInstance(String script){
        return new LuaScript(script);
    }
    public Object execute(Jedis client, List<String> keys,List<String> args){
        return execute(client, keys, args,false);
    }
    public Object execute(Jedis client, List<String> keys,List<String> args,boolean force){
        if(!force){
            String sha=scriptCache.get(this.curScript);
            if(StringUtils.isBlank(sha)){
                sha=client.scriptLoad(curScript);
                scriptCache.put(curScript,sha);
            }
            return client.evalsha(sha,keys,args);
        }else{
            return client.eval(curScript,keys,args);
        }
    }
}
