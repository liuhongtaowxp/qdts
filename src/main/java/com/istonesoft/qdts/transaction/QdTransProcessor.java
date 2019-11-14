package com.istonesoft.qdts.transaction;
import javax.sql.DataSource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;

import com.istonesoft.qdts.context.QdContext;
import com.istonesoft.qdts.jdbc.QdJdbcTemplate;
import com.istonesoft.qdts.resource.QdResult;
/**
 * 事务处理
 * @author issuser
 *
 */
public abstract class QdTransProcessor {
	@Autowired
	protected DataSource ds;
	@Autowired
	protected QdJdbcTemplate jdbcTemplate;
	
	public QdResult invoke(ProceedingJoinPoint joinPoint) throws Throwable {
		//执行controller逻辑
		QdResult result = null;
		try {
			result = (QdResult)joinPoint.proceed();
		} catch (Throwable t) {//网络异常
			result = new QdResult(null, "netConnectException", "0");
		}
		if (result.getFlag().equals("1")) {//执行结果为成功
			//提交事务
			QdContext.getQdConnection().realCommit(jdbcTemplate, result);
		} else {
			//回滚事务
			QdContext.getQdConnection().realRollback(jdbcTemplate, ds, result);
		}
		return result;
		
	}
	
}
