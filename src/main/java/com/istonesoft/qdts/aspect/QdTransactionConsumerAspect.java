package com.istonesoft.qdts.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.istonesoft.qdts.context.QdConsumerContext;
import com.istonesoft.qdts.dao.QdConsumerDao;
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
	@Autowired
	private QdConsumerDao qdConsumerDao;
	
	@Around("@annotation(com.istonesoft.qdts.annotation.QdTransactionConsumer)")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		//设置当前环境为消费者端
		QdConsumerContext.setQdConsumer();
		//获得当前环境的事务组QdGroup
		QdGroup qdGroup = QdConsumerContext.getQdGroup();
		if (qdGroup == null) {//非定时器调用
			//新建事务组
			qdGroup = QdGroup.newQdGroup(joinPoint);
			String methodString = JSON.toJSONString(qdGroup.getMethod());
			//查询是否有相同请求执行网络超时过
			String dbGroupId = qdConsumerDao.getGroupIdByMethod(methodString);
			if (dbGroupId != null) {//如果曾有过相同请求，继续发已经的请求
				qdGroup.setGroupId(dbGroupId);
			} else {
				//初始化，先插入t_qd_consumer表，状态设置为prepare,并且commit
				qdConsumerDao.insertConsumerEntity(qdGroup.getGroupId(), methodString, qdGroup.getStatus().toString());
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
