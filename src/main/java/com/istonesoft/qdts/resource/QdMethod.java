package com.istonesoft.qdts.resource;

import java.util.ArrayList;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
/**
 * 方法 
 * @author issuser
 *
 */
public class QdMethod {
	//类名
	private String className;
	//方法名
	private String methodName;
	//参数
	private List<QdArg> argList;

	public QdMethod(String className, String methodName) {
		super();
		this.className = className;
		this.methodName = methodName;
		this.argList = new ArrayList<QdArg>();
	}
	
	public void addArg(Class parameterType, Object arg) {
		this.argList.add(new QdArg(parameterType, arg));
	}
	
	public  void addArg(QdArg arg) {
		this.argList.add(arg);
	}
	
	public static QdMethod createQdMethod(ProceedingJoinPoint joinPoint) {
		MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
		String methodName = methodSignature.getName();
		String className = joinPoint.getTarget().getClass().getName();
		QdMethod qdMethod = new QdMethod(className, methodName);
		Class[] clzs = methodSignature.getParameterTypes();
		Object[] objs = joinPoint.getArgs();
		int index = 0;
		for (Object arg : objs) {
			qdMethod.addArg(clzs[index++], arg);
		}
		return qdMethod;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public List<QdArg> getArgList() {
		return argList;
	}

	public void setArgList(List<QdArg> argList) {
		this.argList = argList;
	}

}
