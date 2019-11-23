package com.istonesoft.qdts.resource;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationContext;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.istonesoft.qdts.annotation.QdTransactionConsumer;
import com.istonesoft.qdts.cast.StringCastStrategyImpl;
import com.istonesoft.qdts.context.QdConsumerContext;
import com.istonesoft.qdts.context.QdContextHolder;
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

	public QdMethod() {
		super();
		this.argList = new ArrayList<QdArg>();
	}

	public QdMethod(String className, String methodName) {
		super();
		this.className = className;
		this.methodName = methodName;
		this.argList = new ArrayList<QdArg>();
	}
	
	public QdMethod(String className, String methodName, List<QdArg> argList) {
		super();
		this.className = className;
		this.methodName = methodName;
		this.argList = argList;
	}

	public void addArg(String parameterType, Object arg) {
		this.argList.add(new QdArg(parameterType, arg));
	}
	
	public  void addArg(QdArg arg) {
		this.argList.add(arg);
	}
	/**
	 * 拦截点转化为QdMethod
	 * @param joinPoint
	 * @return
	 */
	public static QdMethod createQdMethod(ProceedingJoinPoint joinPoint) {
		MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
		String methodName = methodSignature.getName();
		String className = joinPoint.getTarget().getClass().getName();
		int endPos = className.indexOf("$$");
		//获得实际的类名
		if (endPos > 0) {
			className = className.substring(0, endPos);
		}
		QdMethod qdMethod = new QdMethod(className, methodName);
		Class<?>[] clzs = methodSignature.getParameterTypes();
		Object[] objs = joinPoint.getArgs();
		int index = 0;
		for (Object arg : objs) {
			qdMethod.addArg(clzs[index++].getName(), arg);
		}
		return qdMethod;
	}
	/**
	 * json反序列化对象
	 * @param json
	 * @return
	 */
	public static QdMethod createQdMethodUseJson(String json) {
		QdMethod m = new QdMethod();
		JSONObject jsonObject = JSONObject.parseObject(json);
		m.setClassName(jsonObject.getString("className"));
		m.setMethodName(jsonObject.getString("methodName"));
		//参数值集合
		JSONArray arr = jsonObject.getJSONArray("argList");
		for(int i = 0; i < arr.size(); i++) {
			String parameterType = arr.getJSONObject(i).getString("parameterType");
			//转化为实际类型的值
			Object arg = null;
			try {//字符串转实际值
				arg = StringCastStrategyImpl.castToVal(arr.getJSONObject(i).getString("arg"), parameterType);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			m.addArg(parameterType, arg);
		}
		return m;
	}
	/**
	 * 执行方法
	 * @param applicationContext
	 */
	public synchronized void invoke(ApplicationContext applicationContext) {
		int endPos = className.indexOf("$$");
		//获得实际的类名
		if (endPos > 0) {
			className = className.substring(0, endPos);
		}
		try {
			Class<?> targetClass = Class.forName(className);
			//得到bean
			Object target = applicationContext.getBean(targetClass);
			//得到参数集合
			List<QdArg> args = getArgList();
			Class<?>[] paramClzs = new Class[args.size()];
			Object[] paramObjs = new Object[args.size()];
			int index = 0;
			for (QdArg arg : args) {
				//参数class字符串
				String paramClz = arg.getParameterType();
				Class<?> paramClass = StringCastStrategyImpl.castToClass(paramClz);
				paramClzs[index] = paramClass;
				//参数值
				paramObjs[index] = arg.getArg();
				index++;
			}
			try {
				Method targetMethod = targetClass.getMethod(methodName, paramClzs);
				try {
					targetMethod.invoke(target, paramObjs);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					QdContextHolder.getQdContext().clear();
				}
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
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
