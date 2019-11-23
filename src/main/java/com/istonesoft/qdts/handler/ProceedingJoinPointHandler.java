package com.istonesoft.qdts.handler;
import javax.sql.DataSource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.istonesoft.qdts.context.QdContextHolder;
import com.istonesoft.qdts.jdbc.QdJdbcTemplate;
import com.istonesoft.qdts.resource.QdResult;
/**
 * 事务处理
 * @author issuser
 *
 */
@Component
public class ProceedingJoinPointHandler {
	@Autowired
	private DataSource ds;
	@Autowired
	private QdJdbcTemplate jdbcTemplate;
	
	public QdResult invoke(ProceedingJoinPoint joinPoint) throws Throwable {
		//执行controller逻辑
		QdResult result = null;
		Object obj = joinPoint.proceed();
		if (obj instanceof QdResult) {
			result = (QdResult)obj;
			if (result.getMsg() != null && result.getMsg().startsWith("I/O error on GET request for")) {
				result.setMsg("netConnectException");
			}
			if (result.getFlag().equals("1")) {
				QdContextHolder.getQdContext().getProceedingJoinPointResultHandler().successHandle(jdbcTemplate, ds, result);
			} else {
				QdContextHolder.getQdContext().getProceedingJoinPointResultHandler().failHandle(jdbcTemplate, ds, result);
			}
			return result;
		} else {//返回类型不是QdResult
			throw new Exception("controller result must be QdResult instance");
		}
	}
	
}
