package com.istonesoft.qdts.transaction;

import javax.sql.DataSource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import com.istonesoft.qdts.connection.QdConnection;
import com.istonesoft.qdts.jdbc.QdJdbcTemplate;
import com.istonesoft.qdts.resource.NameThreadLocal;
import com.istonesoft.qdts.resource.QdResult;

public abstract class QdTransProcessor {

	@Autowired
	protected DataSource ds;
	@Autowired
	protected QdJdbcTemplate jdbcTemplate;
	
	public QdResult invoke(ProceedingJoinPoint joinPoint) throws Throwable {
		
		QdResult result = (QdResult)joinPoint.proceed();
		if (result.getFlag().equals("1")) {//执行结果为成功
			((QdConnection)NameThreadLocal.get("qdConnection")).realCommit(jdbcTemplate, result);
		} else {
			NameThreadLocal.clear("qdConsumer");
			NameThreadLocal.clear("qdProvider");
			((QdConnection)NameThreadLocal.get("qdConnection")).realRollback(jdbcTemplate, ds.getConnection(), result);
		}
		return result;
		
	}
	
	
}
