package com.istonesoft.qdts.cast;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.istonesoft.qdts.cache.ClassCache;

public class StringCastStrategyImpl {

	private static Map<String, StringCast> maps = new HashMap<String, StringCast>();
	
	static {
		maps.put("int", new StringCastInt());
		maps.put("java.lang.Integer", new StringCastInteger());
		maps.put("long", new StringCastRawLong());
		maps.put("java.lang.Long", new StringCastLong());
	}
	
	public static Object castToVal(String str, String parameterType) throws ClassNotFoundException {
		StringCast stringCast = maps.get(parameterType);
		if (stringCast == null) {//不是常规类型
			//json转化为entity
			return JSONObject.parseObject(str, ClassCache.getClass(parameterType));
		} else {
			return stringCast.castToVal(str, parameterType);
		}
	}
	
	public static Class castToClass(String parameterType) throws ClassNotFoundException {
		StringCast stringCast = maps.get(parameterType);
		if (stringCast == null) {
			return ClassCache.getClass(parameterType);
		} else {
			return stringCast.castToClass();
		}
	}
}
