package com.istonesoft.qdts.aspect;

import java.sql.Connection;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.istonesoft.qdts.connection.QdConsumerServiceConnection;
import com.istonesoft.qdts.connection.QdProviderServiceConnection;
import com.istonesoft.qdts.connection.QdServiceConnection;
import com.istonesoft.qdts.context.QdContext;
import com.istonesoft.qdts.context.QdContextHolder;
import com.istonesoft.qdts.context.State;
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
		//得到当前环境
		QdContext ctx = QdContextHolder.getQdContext();
		if (ctx == null) {
			return conn;
		}
		if (ctx.getState() == State.NOSERVICE) {//不是service层
			return conn;
		} else {
			QdServiceConnection result = initQdServiceConnection(conn, ctx);
			return result;
		}
	}

	private QdServiceConnection initQdServiceConnection(Connection conn, QdContext ctx) {
		QdServiceConnection result = null;
		if (ctx.isConsumerContxt()) {
			result = new QdConsumerServiceConnection(conn);
		} else {
			result = new QdProviderServiceConnection(conn);
		}
		ctx.setQdConnection(result);
		return result;
	}
	
}
