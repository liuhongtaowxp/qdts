package com.istonesoft.qdts.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
/**
 * class缓存
 * @author issuser
 *
 */
public class ClassCache {

	private static Map<String, Class<?>> maps = new ConcurrentHashMap<String, Class<?>>();
	
	public static Class<?> getClass(String className) throws ClassNotFoundException {
		Class<?> result = maps.get(className);
		if (result == null) {
			result = Class.forName(className);
			maps.put(className, result);
		}
		return result;
	}
	
}
