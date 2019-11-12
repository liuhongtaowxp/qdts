package com.istonesoft.qdts.aspect;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.istonesoft.qdts.context.QdConsumerContext;
import com.istonesoft.qdts.context.QdProviderContext;
import com.istonesoft.qdts.jdbc.RowHandler;
import com.istonesoft.qdts.resource.NameThreadLocal;
import com.istonesoft.qdts.resource.QdResult;
import com.istonesoft.qdts.transaction.QdTransProcessor;
/**
 * QdTransactionProvider注解拦截，拦截的 是提供方controller层
 * @author issuser
 *
 */
@Aspect
@Component
public class QdTransactionProviderAspect extends QdTransProcessor {

	@Around("@annotation(com.istonesoft.qdts.annotation.QdTransactionProvider)")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		//设置当前环境为消费者端
		QdProviderContext.setQdProvider();
		//初始化，先插入t_qd_provider表，状态设置为prepare,并且commit
		String qdGroupId = QdProviderContext.getQdGroupId();
		
		int invokeCount = jdbcTemplate.executeQuerySingle(ds.getConnection(), "select count(1) from t_qd_provider where group_id=?", new String[] {qdGroupId}, new RowHandler<Integer>() {

			public Integer handle(ResultSet rs) {
				try {
					return rs.getInt(1);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return 0;
			}
			
		});
		QdResult result = null;
		
		if (invokeCount <= 0) {//第一次执行
			try {
				QdConsumerContext.requiredNewConnection();
				jdbcTemplate.executeSql(ds.getConnection(), "insert into t_qd_provider(group_id,status,exception,update_time,invoke_count) values(?,?,?,?,?)", new Object[] {
						qdGroupId,"PREPARE",null,new Date(),0	
				});
				QdConsumerContext.cleanRequiredNewConnection();
			}  catch (Exception e) {
				throw e;
			}
			
			//调用业务逻辑
			result = invoke(joinPoint);
		} else {//重复执行
			String iResult = jdbcTemplate.executeQuerySingle(ds.getConnection(), "select result from t_qd_provider where group_id=? and status='SUCCESS'", new String[] {qdGroupId}, new RowHandler<String>() {

				public String handle(ResultSet rs) {
					try {
						return rs.getString(1);
					} catch (SQLException e) {
						e.printStackTrace();
					}
					return null;
				}
				
			});
			if (iResult == null) {//没有成功执行过
				//调用业务逻辑
				result = invoke(joinPoint);
				
			} else {//有成功执行过
				result = QdResult.createQdResultUseJson(iResult);
			}
		}
		QdProviderContext.clear();
		return result;
	}

}
