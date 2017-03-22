package net.xby1993.common.redis;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;


/**
 * Created by taojw .
 */
public class JedisSerializerUtil {
	private static final Logger log=LoggerFactory.getLogger(JedisSerializerUtil.class);
    public static String toJSON(Object obj) {
        return JSON.toJSONString(obj);
    }
    public static <T> T objFromJSON(String str,Class<T> clz){
        return JSON.parseObject(str,clz);
    }
    public static <T> List<T> listFromJSON(String str,Class<T> clz){
        return JSON.parseArray(str,clz);
    }

    public static byte[] toBinary(Object obj){
        if(!(obj instanceof Serializable)){
            throw new RuntimeException("不支持的操作,类"+obj.getClass().getName()+"没有实现Serializable接口");
        }else{
            ObjectOutputStream oos=null;
            ByteArrayOutputStream baos=null;
            try {
                baos=new ByteArrayOutputStream();
                oos=new ObjectOutputStream(baos);
                oos.writeObject(obj);
                baos.flush();
                return baos.toByteArray();
            } catch (IOException e) {
                String msg="对象"+obj.getClass().getName()+"序列化过程中发生异常";
                log.error(msg,e);
                throw new JedisException(msg);
            }finally {
                IOUtils.closeQuietly(baos);
                IOUtils.closeQuietly(oos);
            }
        }
    }

    public static Object fromBinary(byte[] bytes){
    	if(bytes==null){
    		return null;
    	}
        ObjectInputStream ois=null;
        ByteArrayInputStream bais=null;

        try {
            bais=new ByteArrayInputStream(bytes);
            ois=new ObjectInputStream(bais);
            Object obj=ois.readObject();
            return obj;
        } catch (IOException e) {
            e.printStackTrace();
            log.error("反序列化时发生异常",e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            log.error("反序列化时发生异常",e);
        }finally {
            IOUtils.closeQuietly(bais);
            IOUtils.closeQuietly(ois);
        }
        return null;
    }

}
