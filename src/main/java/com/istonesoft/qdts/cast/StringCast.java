package com.istonesoft.qdts.cast;
/**
 * String字符串转化器
 * @author issuser
 *
 */
public interface StringCast {
	/**
	 * 
	 * @param str 值的字符串
	 * @param parameterType class字符串
	 * @return
	 */
	public Object castToVal(String str, String parameterType);
	
	/**
	 * class字符串转化为class
	 * @return
	 */
	public Class castToClass();
	
}
