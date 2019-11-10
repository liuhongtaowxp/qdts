package com.istonesoft.qdts.aspect;

import java.util.Date;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
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
		//初始化，先插入t_qd_provider表，状态设置为prepare,并且commit
		try {
			jdbcTemplate.executeSql(ds.getConnection(), "insert into t_qd_provider(group_id,status,exception,update_time,invoke_count) values(?,?,?,?,?)", new Object[] {
					NameThreadLocal.get("qdGroupId"),"PREPARE",null,new Date(),1	
			});
		}  catch (Exception e) {
			throw e;
		}
		//设置当前线程的变量
		NameThreadLocal.put("qdProvider", true);
		//调用业务逻辑
		QdResult result = invoke(joinPoint);
		//清理当前线程的变量
		NameThreadLocal.clear("qdProvider");
		return result;
	}

}
