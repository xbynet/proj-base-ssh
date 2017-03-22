package net.xby1993.common.util;

import java.util.HashMap;
import java.util.Map;

public class MapUtil {
	/**
	 * 将map中的key转为驼峰形式
	 * 
	 * @param map
	 * @return
	 */

	public static Map<String,Object> formatKey(Map<String,Object> map) {
		Map<String,Object> newMap = new HashMap<>();
		String keyF;
		for (Object key : map.keySet()) {
			keyF = StringUtils.columnToFieldName(key.toString());
			newMap.put(keyF, map.get(key));
		}
		return newMap;
	}
}
