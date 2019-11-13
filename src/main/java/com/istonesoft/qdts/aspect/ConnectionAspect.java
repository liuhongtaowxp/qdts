package com.istonesoft.qdts.aspect;

import java.sql.Connection;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.istonesoft.qdts.connection.QdConnection;
import com.istonesoft.qdts.connection.QdConsumerConnection;
import com.istonesoft.qdts.connection.QdProviderConnection;
import com.istonesoft.qdts.context.QdConsumerContext;
import com.istonesoft.qdts.context.QdContext;
import com.istonesoft.qdts.context.QdProviderContext;
import com.zaxxer.hikari.pool.HikariProxyConnection;
/**
 * connection切面，需截取connection
 * @author issuser
 *
 */
@Aspect
@Component
public class ConnectionAspect {
	/**
	 * 拦截数据源
	 * @param joinPoint
	 * @return
	 */
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
			QdConnection result = new QdProviderConnection(conn);
			QdContext.setQdConnection(result);
			return result;
		} else  if (QdConsumerContext.isConsumer()) {//调用者为消费方
			QdConnection result = new QdConsumerConnection(conn);
			QdContext.setQdConnection(result);
			return result;
		} else {
			return conn;
		}
	}
	
}
