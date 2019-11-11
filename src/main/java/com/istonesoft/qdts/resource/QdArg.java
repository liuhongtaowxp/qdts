package com.istonesoft.qdts.resource;
/**
 * 入参
 * @author issuser
 *
 */
public class QdArg {
	//参数类型
	private String parameterType;
	//参数值
	private Object arg;
	
	public QdArg(String parameterType, Object arg) {
		super();
		this.parameterType = parameterType;
		this.arg = arg;
	}
	public String getParameterType() {
		return parameterType;
	}
	public void setParameterType(String parameterType) {
		this.parameterType = parameterType;
	}
	public Object getArg() {
		return arg;
	}
	public void setArg(Object arg) {
		this.arg = arg;
	}
	
	
	
}
