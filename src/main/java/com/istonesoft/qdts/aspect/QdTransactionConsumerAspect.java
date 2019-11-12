package com.istonesoft.qdts.aspect;

import java.util.Date;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSON;
import com.istonesoft.qdts.context.QdConsumerContext;
import com.istonesoft.qdts.resource.NameThreadLocal;
import com.istonesoft.qdts.resource.QdGroup;
import com.istonesoft.qdts.resource.QdResult;
import com.istonesoft.qdts.transaction.QdTransProcessor;
/**
 * QdTransactionConsumer注解拦截，拦截的 是消费方controller层
 * @author issuser
 *
 */
@Aspect
@Component
public class QdTransactionConsumerAspect extends QdTransProcessor {

	
	@Around("@annotation(com.istonesoft.qdts.annotation.QdTransactionConsumer)")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		//设置当前环境为消费者端
		QdConsumerContext.setQdConsumer();
		//获得当前环境的QdGroup
		QdGroup qdGroup = QdConsumerContext.getQdGroup();
		if (qdGroup == null) {//第一次调用
			//新建事务组
			qdGroup = QdGroup.newQdGroup(joinPoint);
			//初始化，先插入t_qd_consumer表，状态设置为prepare,并且commit
			try {
				QdConsumerContext.requiredNewConnection();
				jdbcTemplate.executeSql(ds.getConnection(), "insert into t_qd_consumer(group_id,method,status,exception,update_time,invoke_count) values(?,?,?,?,?,?)", new Object[] {
						qdGroup.getGroupId(), JSON.toJSONString(qdGroup.getMethod()),qdGroup.getStatus().toString(),null,new Date(),0	
				});
				QdConsumerContext.cleanRequiredNewConnection();
			}  catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		
		//设置当前线程的groupId
		QdConsumerContext.setQdGroupId(qdGroup.getGroupId());
		
		//调用业务逻辑
		QdResult result = invoke(joinPoint);
		//清理当前线程的变量
		QdConsumerContext.clear();
		return result;
	}
	
}
