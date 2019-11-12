package com.istonesoft.qdts.aspect;

import java.sql.Connection;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.istonesoft.qdts.connection.QdConsumerConnection;
import com.istonesoft.qdts.connection.QdProviderConnection;
import com.istonesoft.qdts.context.QdConsumerContext;
import com.istonesoft.qdts.context.QdContext;
import com.istonesoft.qdts.context.QdProviderContext;
import com.istonesoft.qdts.resource.NameThreadLocal;
/**
 * connection切面，需截取connection
 * @author issuser
 *
 */
@Aspect
@Component
public class ConnectionAspect {

	@Around("execution(* javax.sql.DataSource.getConnection(..))")
	public Connection around(ProceedingJoinPoint joinPoint) {
		Connection conn = null;
		try {
			conn = (Connection)joinPoint.proceed();
		} catch (Throwable e1) {
			e1.printStackTrace();
		}
		if (QdContext.isRequiredNewConnection()) {//是否需要新的连接
			return conn;
		} else if (QdProviderContext.isProvider()) {//调用者为提供方
			QdProviderConnection result = new QdProviderConnection(conn);
			QdProviderContext.setQdConnection(result);
			return result;
		} else  if (QdConsumerContext.isConsumer()) {//调用者为消费方
			QdConsumerConnection result = new QdConsumerConnection(conn);
			QdConsumerContext.setQdConnection(result);
			return result;
		} else {
			return conn;
		}
		
	}
	
}
