package com.istonesoft.qdts.resource;

import java.util.HashMap;
import java.util.Map;

public class QdNameThreadLocal {

	public static final Map<String, ThreadLocal<Object>> maps = new HashMap<String, ThreadLocal<Object>>();
	
	public static void put(String name, Object val) {
		ThreadLocal<Object> nameThreadLocal = maps.get(name);
		if (nameThreadLocal == null) {
			nameThreadLocal = new ThreadLocal<Object>();
			maps.put(name, nameThreadLocal);
		}
		nameThreadLocal.set(val);
	}
	
	public static Object get(String name) {
		ThreadLocal<Object> nameThreadLocal = maps.get(name);
		if (nameThreadLocal == null) {
			return null;
		} else {
			return nameThreadLocal.get();
		}
	}
	
	public static void clear(String name) {
		ThreadLocal<Object> nameThreadLocal = maps.get(name);
		if (nameThreadLocal != null) {
			nameThreadLocal.remove();
		} 
	}

}
