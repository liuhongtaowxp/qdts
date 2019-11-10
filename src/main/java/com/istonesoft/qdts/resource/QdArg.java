package com.istonesoft.qdts.resource;
/**
 * 入参
 * @author issuser
 *
 */
public class QdArg {
	//参数类型
	private Class parameterType;
	//参数值
	private Object arg;
	
	public QdArg(Class parameterType, Object arg) {
		super();
		this.parameterType = parameterType;
		this.arg = arg;
	}
	public Class getParameterType() {
		return parameterType;
	}
	public void setParameterType(Class parameterType) {
		this.parameterType = parameterType;
	}
	public Object getArg() {
		return arg;
	}
	public void setArg(Object arg) {
		this.arg = arg;
	}
	
	
	
}
