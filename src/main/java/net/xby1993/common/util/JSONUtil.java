package net.xby1993.common.util;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

/**
 * 为了与特定JSON解析库解耦，提供一个JSON工具类
 * @author xby Administrator
 *
 */
public class JSONUtil {
	public static <T> List<T> parseStringToList(Class<T> t,String str){
		List<T> list=JSONObject.parseArray(str, t);
		return list;
	}
}
