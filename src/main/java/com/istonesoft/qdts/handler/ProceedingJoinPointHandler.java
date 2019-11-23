package com.istonesoft.qdts.handler;
import javax.sql.DataSource;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import com.istonesoft.qdts.context.QdContextHolder;
import com.istonesoft.qdts.jdbc.QdJdbcTemplate;
import com.istonesoft.qdts.resource.QdResult;
/**
 * 事务处理
 * @author issuser
 *
 */
public abstract class ProceedingJoinPointHandler {
	@Autowired
	protected DataSource ds;
	@Autowired
	protected QdJdbcTemplate jdbcTemplate;
	
	public QdResult invoke(ProceedingJoinPoint joinPoint) throws Throwable {
		//执行controller逻辑
		QdResult result = null;
		Object obj = joinPoint.proceed();
		try {
			obj = joinPoint.proceed();
		} catch (Throwable t) {//网络异常
			obj = new QdResult(null, "netConnectException", "0");
		}
		if (obj instanceof QdResult) {
			result = (QdResult)obj;
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
