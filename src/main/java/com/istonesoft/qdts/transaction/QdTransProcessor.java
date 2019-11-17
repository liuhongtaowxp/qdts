package com.istonesoft.qdts.transaction;
import javax.sql.DataSource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.istonesoft.qdts.context.QdConsumerContext;
import com.istonesoft.qdts.context.QdContext;
import com.istonesoft.qdts.context.QdProviderContext;
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
		Object obj = joinPoint.proceed();
		try {
			obj = joinPoint.proceed();
		} catch (Throwable t) {//网络异常
			obj = new QdResult(null, "netConnectException", "0");
		}
		if (obj instanceof QdResult) {
			result = (QdResult)obj;
			if (QdContext.getQdConnection() != null) {//service层开启过事务
				if (result.getFlag().equals("1")) {//执行结果为成功
					//提交事务
					QdContext.getQdConnection().realCommit(jdbcTemplate, result);
				} else {
					//回滚事务
					QdContext.getQdConnection().realRollback(jdbcTemplate, ds, result);
				}
			} else {//无事务
				if (result.getFlag().equals("1")) {//执行成功
					if (QdConsumerContext.isConsumer()) {
						jdbcTemplate.executeSql(ds, "update t_qd_consumer set status='SUCCESS',invoke_count=invoke_count+1,update_time=now() where group_id=?", new Object[] {QdConsumerContext.getQdGroupId()});
					} else {
						jdbcTemplate.executeSql(ds, "update t_qd_provider set status='SUCCESS',invoke_count=invoke_count+1,update_time=now(), result=? where group_id=?", new Object[] {JSON.toJSONString(result), QdProviderContext.getQdGroupId()});
					}
				} else {
					if (QdConsumerContext.isConsumer()) {
						jdbcTemplate.executeSql(ds, "update t_qd_consumer set exception=?,invoke_count=invoke_count+1,update_time=now() where group_id=?", new Object[] {result.getMsg(), QdConsumerContext.getQdGroupId()});
					} else {
						jdbcTemplate.executeSql(ds, "update t_qd_provider set exception=?,invoke_count=invoke_count+1,update_time=now()  where group_id=?", new Object[] {result.getMsg(), QdProviderContext.getQdGroupId()});
					}
				}
			}
			return result;
		} else {//返回类型不是QdResult
			throw new Exception("controller result must be QdResult instance");
		}
	}
	
}
