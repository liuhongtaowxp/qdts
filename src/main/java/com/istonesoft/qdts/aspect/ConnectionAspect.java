package com.istonesoft.qdts.aspect;

import java.sql.Connection;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import com.istonesoft.qdts.connection.QdConsumerConnection;
import com.istonesoft.qdts.connection.QdProviderConnection;
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
		
		if (NameThreadLocal.get("qdProvider") != null) {//调用者为提供方
			QdProviderConnection result = new QdProviderConnection(conn);
			NameThreadLocal.put("qdConnection", result);
			return result;
		} else  if (NameThreadLocal.get("qdConsumer") != null) {//调用者为消费方
			QdConsumerConnection result = new QdConsumerConnection(conn);
			NameThreadLocal.put("qdConnection", result);
			return result;
		} else {
			return conn;
		}
		
	}
	
}
