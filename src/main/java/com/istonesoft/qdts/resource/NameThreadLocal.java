package com.istonesoft.qdts.resource;

import java.util.HashMap;
import java.util.Map;

public class NameThreadLocal {


	public static final Map<String, ThreadLocal> maps = new HashMap<String, ThreadLocal>();
	
	public static void put(String name, Object val) {
		ThreadLocal nameThreadLocal = maps.get(name);
		if (nameThreadLocal == null) {
			nameThreadLocal = new ThreadLocal();
			maps.put(name, nameThreadLocal);
		}
		nameThreadLocal.set(val);
	}
	
	public static Object get(String name) {
		ThreadLocal nameThreadLocal = maps.get(name);
		if (nameThreadLocal == null) {
			return null;
		} else {
			return nameThreadLocal.get();
		}
	}
	
	public static void clear(String name) {
		ThreadLocal nameThreadLocal = maps.get(name);
		if (nameThreadLocal != null) {
			nameThreadLocal.remove();
		} 
	}

}
